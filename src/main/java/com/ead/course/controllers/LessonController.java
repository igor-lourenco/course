package com.ead.course.controllers;

import com.ead.course.DTOs.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonServiceInterface;
import com.ead.course.services.ModuleServiceInterface;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600) // configura CORS para permitir solicitações de qualquer origem e define que a resposta de pré-verificação pode ser armazenada em cache por uma hora
@Log4j2
public class LessonController {

    @Autowired
    LessonServiceInterface lessonService;
    @Autowired
    ModuleServiceInterface moduleService;
    @Autowired
    LogUtils logUtils;

    @GetMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessonPaged(
            @PathVariable(value = "moduleId") UUID moduleId,
            SpecificationTemplate.LessonSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable){
        log.info("REQUEST - GET [getAllLessonPaged] PARAMS :: moduleId: {} - PAGED: {}", moduleId.toString(), pageable.toString());

        Specification<LessonModel> findLessonByModuleIdSpec = SpecificationTemplate.findLessonsByModuleId(moduleId).and(spec);
        Page<LessonModel> lessonModelPage = lessonService.findLessonByModuleId(findLessonByModuleIdSpec, pageable);

        log.info("RESPONSE - GET [getAllLessonPaged] : {}", logUtils.convertObjectToJson(lessonModelPage));
        return ResponseEntity.ok(lessonModelPage);
    }

    @GetMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getByIdLesson(@PathVariable(value = "moduleId") UUID moduleId, @PathVariable(value = "lessonId") UUID lessonId){
        log.info("REQUEST - GET [getByIdLesson] PARAMS :: moduleId: {} - lessonId: {}", moduleId.toString(), lessonId.toString());

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonModelOptional.isEmpty()) {
            log.warn("RESPONSE - GET [getByIdLesson] : Lesson not found for this module!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
        }

        log.info("RESPONSE - GET [getByIdLesson] : {}", logUtils.convertObjectToJson(lessonModelOptional.get()));
        return ResponseEntity.ok(lessonModelOptional.get());
    }

    @PostMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<?> saveLesson(@PathVariable(value = "moduleId") UUID moduleId, @RequestBody @Valid LessonDTO lessonDTO){
        log.info("REQUEST - POST [saveLesson] PARAMS :: moduleId: {} - BODY: {}", moduleId.toString(), logUtils.convertObjectToJson(lessonDTO));

        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);

        if(moduleModelOptional.isEmpty()){
            log.info("REQUEST - POST [saveLesson] : Module Not Found: {}", moduleId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found!");
        }

        LessonModel lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());

        lessonModel = lessonService.save(lessonModel);

        log.info("RESPONSE - POST [saveLesson] : BODY: {}", logUtils.convertObjectToJson(lessonModel));
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonModel);
    }

    @PutMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> updateLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId,
            @RequestBody @Valid LessonDTO lessonDTO){
        log.info("REQUEST - PUT [updateLesson] PARAMS :: moduleId: {} - lessonId: {} - BODY: {}", moduleId.toString(), lessonId.toString(), logUtils.convertObjectToJson(lessonDTO));

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonModelOptional.isEmpty()) {
            log.warn("RESPONSE - PUT [updateLesson] : Lesson not found for this module :: moduleId: {} - lessonId: {}", moduleId.toString(), lessonId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
        }

        LessonModel lessonModel = lessonModelOptional.get();
        lessonModel.setTitle(lessonDTO.getTitle());
        lessonModel.setDescription(lessonDTO.getDescription());
        lessonModel.setVideoUrl(lessonDTO.getVideoUrl());

        lessonModel = lessonService.save(lessonModel);

        log.info("RESPONSE - PUT [updateLesson] : {}", logUtils.convertObjectToJson(lessonModel));
        return ResponseEntity.status(HttpStatus.OK).body(lessonModel);
    }


    @DeleteMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId, @PathVariable(value = "lessonId") UUID lessonId) {
        log.info("REQUEST - DELETE [deleteLesson] PARAMS :: moduleId: {} - lessonId: {} ",  moduleId.toString(), lessonId.toString());

        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonModelOptional.isEmpty()) {
            log.warn("RESPONSE - PUT [deleteLesson] : Module not found for this course :: moduleId: {} - lessonId: {} ",  moduleId.toString(), lessonId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module!");
        }
        lessonService.delete(lessonModelOptional.get());

        log.info("RESPONSE - DELETE [deleteLesson] : Lesson deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully!");
    }

}
