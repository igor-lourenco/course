package com.ead.course.controllers;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.DTOs.UserDTO;
import com.ead.course.clients.CourseRequestClient;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class CourseUserController {

    @Autowired
    CourseRequestClient courseRequestClient;
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

}
