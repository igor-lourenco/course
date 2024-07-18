package com.ead.course.controllers;

import com.ead.course.DTOs.SubscriptionDTO;
import com.ead.course.DTOs.UserDTO;
import com.ead.course.clients.AuthUserRequestClient;
import com.ead.course.enums.UserStatus;
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
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class CourseUserController {

    @Autowired
    AuthUserRequestClient authUserRequestClient;
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
        log.info("REQUEST - POST [findAllUsersByCoursePaged] PARAMS :: courseId: {} - PAGED: {}", courseId, pageable.toString());

        Page<UserDTO> coursesByUserPaged = authUserRequestClient.getAllUsersByCoursePaged(courseId, pageable);

        String pageJson = logUtils.convertObjectToJson(coursesByUserPaged);
        log.info("RESPONSE - POST [findAllUsersByCoursePaged] : {}", pageJson);
        return ResponseEntity.ok().body(coursesByUserPaged);
    }

    @PostMapping(value = "/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Valid SubscriptionDTO subscriptionDTO){
        log.info("REQUEST - POST [saveSubscriptionUserInCourse] PARAMS :: courseId: {} - BODY: {}", courseId, logUtils.convertObjectToJson(subscriptionDTO));

        UserDTO userDTO = null;
        CourseUserModel courseUserModel = null;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : Course Not Found! :: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        if(courseUserService.existsByCourseAndUserId(courseModelOptional.get(), subscriptionDTO.getUserId())){
            log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : Subscription already exists! :: course: {} - userId: {}", courseModelOptional.get(), subscriptionDTO.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: subscription already exists!");
        }


        try {
            userDTO = authUserRequestClient.getOneUserById(subscriptionDTO.getUserId());

            if (userDTO.getUserStatus().equals(UserStatus.BLOCKED)) {
                log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : User is blocked! :: userId: {}", subscriptionDTO.getUserId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("ERROR: User is blocked!");
            }

            courseUserModel = courseModelOptional.get().convertToCourseUserModel(subscriptionDTO.getUserId());
            courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(courseUserModel);

        } catch (HttpStatusCodeException e) {

            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : User not found :: userId: {}", subscriptionDTO.getUserId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
            }

            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                log.warn("RESPONSE - POST [saveSubscriptionUserInCourse] : {} ! :: userId: {}", e.getMessage(), subscriptionDTO.getUserId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

            }
        }

        log.info("RESPONSE - POST [saveSubscriptionUserInCourse] : {}", logUtils.convertObjectToJson(courseUserModel));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);

    }

}
