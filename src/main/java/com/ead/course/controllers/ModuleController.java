package com.ead.course.controllers;

import com.ead.course.DTOs.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseServiceInterface;
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
public class ModuleController {

    @Autowired //Em casos de múltiplas implementações, é necessário usar @Qualifier para evitar ambiguidades e especificar explicitamente qual implementação deve ser injetada.
    ModuleServiceInterface moduleService;
    @Autowired
    CourseServiceInterface courseService;
    @Autowired
    LogUtils logUtils;

    @GetMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModulesPaged(
            @PathVariable(value = "courseId") UUID courseId,
            SpecificationTemplate.ModuleSpec spec,
            @PageableDefault(page = 0, size = 12, sort = "moduleId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("REQUEST - GET [getAllModulesPaged] PARAMS :: courseId: {} - PAGED: {}",courseId.toString(), pageable.toString());

        Specification<ModuleModel> findModulesByCourseIdSpec = SpecificationTemplate.findModulesByCourseId(courseId).and(spec);
        Page<ModuleModel> moduleModelPage = moduleService.findAllModulesByCourseId(findModulesByCourseIdSpec, pageable);

        log.info("RESPONSE - GET [getAllModulesPaged] : {}", logUtils.convertObjectToJson(moduleModelPage));
        return ResponseEntity.ok(moduleModelPage);
    }

    @GetMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> getByIdModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId){
        log.info("REQUEST - GET [getByIdModule] PARAMS :: courseId : {} - moduleId: {}", courseId.toString(), moduleId.toString());

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleModelOptional.isEmpty()) {
            log.warn("RESPONSE - GET [getByIdModule] : Module not found for this course!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        log.info("RESPONSE - GET [getByIdModule] : {}", logUtils.convertObjectToJson(moduleModelOptional.get()));
        return ResponseEntity.ok(moduleModelOptional.get());
    }


    @PostMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<?> saveModule(@PathVariable(value = "courseId")UUID courseId, @RequestBody @Valid ModuleDTO moduleDTO){
        log.info("REQUEST - POST [saveModule] PARAMS :: courseId: {} - BODY: {}", courseId.toString(), logUtils.convertObjectToJson(moduleDTO));

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);

        if(courseModelOptional.isEmpty()){
        log.info("REQUEST - POST [saveModule] : Course Not Found: {}", courseId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found!");
        }

        ModuleModel moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(courseModelOptional.get());

        moduleModel = moduleService.save(moduleModel);

        log.info("RESPONSE - POST [saveModule] : BODY: {}", logUtils.convertObjectToJson(moduleModel));
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleModel);
    }


    @PutMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> updateModule(
            @PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody @Valid ModuleDTO moduleDTO){
        log.info("REQUEST - PUT [updateModule] PARAMS :: courseId: {} - moduleId: {} - BODY: {}", courseId.toString(), moduleId.toString(), logUtils.convertObjectToJson(moduleDTO));

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleModelOptional.isEmpty()) {
            log.warn("RESPONSE - PUT [updateModule] : Module not found for this course :: courseId: {} - moduleId: {}", courseId.toString(), moduleId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        ModuleModel moduleModel = moduleModelOptional.get();
        moduleModel.setTitle(moduleDTO.getTitle());
        moduleModel.setDescription(moduleDTO.getDescription());

        moduleModel = moduleService.save(moduleModel);

        log.info("RESPONSE - PUT [updateModule] : {}", logUtils.convertObjectToJson(moduleModel));
        return ResponseEntity.status(HttpStatus.OK).body(moduleModel);
    }

    @DeleteMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> deleteModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId) {
        log.info("REQUEST - DELETE [deleteModule] PARAMS :: courseId: {} - moduleId: {} ",  courseId.toString(), moduleId.toString());

        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (moduleModelOptional.isEmpty()) {
            log.warn("RESPONSE - PUT [deleteModule] : Module not found for this course :: courseId: {} - moduleId: {}", courseId.toString(), moduleId.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }
        moduleService.delete(moduleModelOptional.get());

        log.info("RESPONSE - DELETE [deleteModule] : Module deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully!");
    }

}
