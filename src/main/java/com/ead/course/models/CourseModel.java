package com.ead.course.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TB_COURSES")
public class CourseModel implements Serializable {
//    private static final long serialVersionUID = 1L;

    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;
//    private CourseStatus courseStatus;
//    private CourseLevel courseLevel;
    private UUID userInstructor;


}
