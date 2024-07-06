package com.ead.course.services.implementations;

import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

public class LessonService implements LessonServiceInterface {

    @Autowired
    private LessonRepository lessonRepository;
}
