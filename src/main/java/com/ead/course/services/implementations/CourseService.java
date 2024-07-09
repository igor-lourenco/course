package com.ead.course.services.implementations;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService implements CourseServiceInterface {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    LessonRepository lessonRepository;

    @Override
    @Transactional
    public void delete(CourseModel courseModel) {

        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if(!moduleModelList.isEmpty()){

            for(ModuleModel moduleModel : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

                if(!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList); // remove todas as 'lessons' vinculadas a esse 'module'
                }
            }

            moduleRepository.deleteAll(moduleModelList); //remove todas os 'modules' vinculadas a esse 'course'
        }

        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }
}
