package com.ead.course.services;

import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CourseServiceInterface {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel courseModel);

    Optional<CourseModel> findById(UUID courseId);

    List<CourseModel> findAll();

    Page<CourseModel> findAllPaged(Specification<CourseModel> spec, Pageable pageable);
}
