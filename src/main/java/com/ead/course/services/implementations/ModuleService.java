package com.ead.course.services.implementations;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public class ModuleService implements ModuleServiceInterface {

    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    LessonRepository lessonRepository;

    @Override
    @Transactional
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

        if(!lessonModelList.isEmpty()){
            lessonRepository.deleteAll(lessonModelList); // remove todas as 'lessons' vinculadas a esse 'module'
        }

        moduleRepository.delete(moduleModel);
    }
}
