package com.ead.course.services.implementations;

import com.ead.course.repositories.CourseRepository;
import com.ead.course.services.CourseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class CourseService implements CourseServiceInterface {

    @Autowired
    private CourseRepository courseRepository;
}
