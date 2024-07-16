package com.ead.course.controllers;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.DTOs.SubscriptionDTO;
import com.ead.course.DTOs.UserDTO;
import com.ead.course.clients.CourseRequestClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseServiceInterface;
import com.ead.course.services.CourseUserServiceInterface;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class CourseUserController {

    @Autowired
    CourseRequestClient courseRequestClient;
    @Autowired
    CourseServiceInterface courseService;
    @Autowired
    CourseUserServiceInterface courseUserService;
    @Autowired
    LogUtils logUtils;

    @GetMapping(value = "/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> findAllUsersByCoursePaged(
            @PageableDefault(page = 0, size = 12, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId){
        log.info("REQUEST - GET [findAllUsersByCoursePaged] PARAMS :: courseId: {} - PAGED: {}", courseId, pageable.toString());

        Page<UserDTO> coursesByUserPaged = courseRequestClient.getAllUsersByCoursePaged(courseId, pageable);

        String pageJson = logUtils.convertObjectToJson(coursesByUserPaged);
        log.info("RESPONSE - GET [findAllUsersByCoursePaged] : {}", pageJson);
        return ResponseEntity.ok().body(coursesByUserPaged);
    }

    @PostMapping(value = "/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDTO subscriptionDTO){
        log.info("REQUEST - GET [saveSubscriptionUserInCourse] PARAMS :: courseId: {} - BODY: {}", courseId, subscriptionDTO);

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            log.warn("RESPONSE - GET [saveSubscriptionUserInCourse] : Course Not Found! :: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())){
            log.warn("RESPONSE - GET [saveSubscriptionUserInCourse] : Subscription already exists! :: course: {} - userId: {}", courseModelOptional.get(), subscriptionDTO.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: subscription already exists!");
        }

        //TODO: verificação no User

        CourseUserModel courseUserModel = courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId());
        courseUserModel = courseUserService.save(courseUserModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }























}
