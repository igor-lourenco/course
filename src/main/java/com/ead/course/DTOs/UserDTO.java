package com.ead.course.DTOs;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UserDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private UserStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String cpf;

    @JsonIgnore
    private LocalDateTime creationDate;
    @JsonIgnore
    private LocalDateTime lastUpdateDate;
    @JsonIgnore
    private List<String> links;
}


