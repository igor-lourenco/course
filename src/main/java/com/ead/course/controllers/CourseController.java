package com.ead.course.controllers;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.implementations.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody CourseDTO courseDTO){
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseModel = courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

}
