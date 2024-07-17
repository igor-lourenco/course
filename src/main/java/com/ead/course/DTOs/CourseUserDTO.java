package com.ead.course.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseUserDTO {

    private UUID userId;
    private UUID courseId;
}
