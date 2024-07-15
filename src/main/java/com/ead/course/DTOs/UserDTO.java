package com.ead.course.DTOs;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDTO {

    private UUID userId;
    private String username;
    private String email;
//    private String password;
//    private String oldPassword;
    private String fullName;

    private UserStatus userStatus;

    private UserType userType;

    private String phoneNumber;
    private String cpf;
//    private String imageUrl;

    private LocalDateTime creationDate;
    private LocalDateTime lastUpdateDate;


}
