package com.ead.course.services.implementations;

import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseUserService implements CourseUserServiceInterface {

    @Autowired
    CourseUserRepository courseUserRepository;

}
