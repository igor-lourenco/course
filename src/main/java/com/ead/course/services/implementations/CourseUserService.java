package com.ead.course.services.implementations;

import com.ead.course.clients.AuthUserRequestClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserService implements CourseUserServiceInterface {

    @Autowired
    CourseUserRepository courseUserRepository;
    @Autowired
    AuthUserRequestClient authUserRequestClient;

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return courseUserRepository.save(courseUserModel);
    }

    @Override
    @Transactional
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel = courseUserRepository.save(courseUserModel);

        authUserRequestClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());

        return courseUserModel;
    }
}
