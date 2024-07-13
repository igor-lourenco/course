package com.ead.course.controllers;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseServiceInterface;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@RequestMapping("/courses")
@Log4j2
public class CourseController {

    @Autowired // Em casos de múltiplas implementações, é necessário usar @Qualifier para evitar ambiguidades e especificar explicitamente qual implementação deve ser injetada.
    CourseServiceInterface courseService;
    @Autowired
    LogUtils logUtils;

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCoursesPaged(
            SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable){
        log.info("REQUEST - GET [getAllCoursesPaged] PARAMS PAGED: {}", pageable.toString());

        Page<CourseModel> courseModelPage = courseService.findAllPaged(spec, pageable);

        log.info("RESPONSE - GET [getAllCoursesPaged] : {}", logUtils.convertObjectToJson(courseModelPage));
        return ResponseEntity.ok(courseModelPage);
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<?> getByIdCourse(@PathVariable(value = "courseId") UUID courseId){
        log.info("REQUEST - GET [getByIdCourse] PARAMS : {}", courseId.toString());

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            log.warn("RESPONSE - GET [getByIdCourse] : Course Not Found! :: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        log.info("RESPONSE - GET [getByIdCourse] : {}", logUtils.convertObjectToJson(courseModelOptional.get()));
        return ResponseEntity.ok(courseModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        log.info("REQUEST - POST [saveCourse] : BODY: {}", logUtils.convertObjectToJson(courseDTO));

        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseModel = courseService.save(courseModel);

        log.info("RESPONSE - POST [saveCourse] : BODY: {}", logUtils.convertObjectToJson(courseModel));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDTO courseDTO){
        log.info("REQUEST - PUT [updateCourse] PARAMS :: courseId: {} - BODY: {}", courseId.toString(), logUtils.convertObjectToJson(courseDTO));


        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            log.warn("RESPONSE - PUT [updateCourse] : Course not found :: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        CourseModel courseModel = courseModelOptional.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageUrl(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseModel = courseService.save(courseModel);

        log.info("RESPONSE - PUT [updateCourse] : {}", logUtils.convertObjectToJson(courseModel));
        return ResponseEntity.status(HttpStatus.OK).body(courseModel);
    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        log.info("REQUEST - DELETE [deleteCourse] PARAMS :: userId: {} ", courseId.toString());

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if (courseModelOptional.isEmpty()) {
            log.warn("RESPONSE - DELETE [deleteCourse] : Course not found :: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        courseService.delete(courseModelOptional.get());

        log.info("RESPONSE - DELETE [deleteUser] : Course deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully!");
    }
}
