package com.ead.course.services;

import com.ead.course.models.CourseModel;


public interface CourseServiceInterface {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel courseModel);
}
