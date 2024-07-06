package com.ead.course.services.implementations;

import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class ModuleService implements ModuleServiceInterface {

    @Autowired
    private ModuleRepository moduleRepository;
}
