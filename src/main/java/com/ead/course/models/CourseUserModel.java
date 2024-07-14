package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "TB_COURSES_USERS")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignora campos com valores nulos durante a serialização para JSON
public class CourseUserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // cria chave estrangeira(course_course_id) para referenciar o CourseModel
    private CourseModel course;

    @Column(nullable = false)
    private UUID userId;

}
