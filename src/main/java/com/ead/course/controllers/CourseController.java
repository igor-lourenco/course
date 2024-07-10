package com.ead.course.controllers;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseServiceInterface;
import com.ead.course.specifications.SpecificationTemplate;
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
public class CourseController {

    @Autowired // Em casos de múltiplas implementações, é necessário usar @Qualifier para evitar ambiguidades e especificar explicitamente qual implementação deve ser injetada.
    CourseServiceInterface courseService;

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCoursesPaged(
            SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable){

        Page<CourseModel> courseModelPage = courseService.findAllPaged(spec, pageable);

        return ResponseEntity.ok(courseModelPage);
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<?> getByIdCourse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        return ResponseEntity.ok(courseModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<?> saveCourse(@RequestBody @Valid CourseDTO courseDTO){
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        courseModel = courseService.save(courseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDTO courseDTO){
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
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

        return ResponseEntity.status(HttpStatus.OK).body(courseModel);


    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }
        courseService.delete(courseModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully!");


    }
}
