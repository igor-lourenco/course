package com.ead.course.services;

import com.ead.course.models.ModuleModel;
import org.springframework.stereotype.Service;

@Service
public interface ModuleServiceInterface {

    void delete(ModuleModel moduleModel);
}
