package com.ead.course.services;

import com.ead.course.models.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ModuleServiceInterface {

    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    List<ModuleModel> findAll();

    List<ModuleModel> findAllModulesIntoCourse(UUID courseId);

    Optional<ModuleModel> findById(UUID moduleId);
}
