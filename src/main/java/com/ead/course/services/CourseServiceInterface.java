package com.ead.course.services;

import com.ead.course.models.CourseModel;
import com.ead.course.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface CourseServiceInterface {

    void delete(CourseModel courseModel);

}
