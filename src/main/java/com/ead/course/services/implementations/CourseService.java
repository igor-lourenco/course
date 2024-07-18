package com.ead.course.services.implementations;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService implements CourseServiceInterface {

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    CourseUserRepository courseUserRepository;

    @Override
    @Transactional
    public void delete(CourseModel courseModel) {

        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if(!moduleModelList.isEmpty()){

            for(ModuleModel moduleModel : moduleModelList){
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

                if(!lessonModelList.isEmpty()){
                    lessonRepository.deleteAll(lessonModelList); // Deleta todas as 'lessons' vinculadas a esse 'module'
                }
            }

            moduleRepository.deleteAll(moduleModelList); //Deleta todas os 'modules' vinculadas a esse 'course'
        }

        List<CourseUserModel> courseUserModelList = courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());

        if(!courseUserModelList.isEmpty()){
           courseUserRepository.deleteAll(courseUserModelList);  // Deleta da tabela TB_COURSES_USERS todos os registros vinculados a esse courseId
        }

//        TODO: Falta fazer a deleção também no outro micro-serviço User, relacionado a esse 'courseId' para manter a consistência dos dados em ambos micro-serviços

        courseRepository.delete(courseModel); // Depois de deletar em cascata os registros vinculados a esse course, deleta esse course
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public List<CourseModel> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Page<CourseModel> findAllPaged(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }
}
