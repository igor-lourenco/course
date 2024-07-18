package com.ead.course.validation;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.DTOs.UserDTO;
import com.ead.course.clients.AuthUserRequestClient;
import com.ead.course.enums.UserType;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

/** Classe para validar objetos CourseDTO, essa Classe usa o validador padrão(springframework.validation) para verificar as anotações de validação
      mapeadas no próprio CourseDTO e, adiciona uma verificação se o instrutor do curso é válido (ou seja, deve ser um instrutor ou admin).

    Obs: Essa classe é uma alternativa da anotação @Valid usado no Controller, para validar o body(CourseDTO) da requisição
 */
@Component
@Log4j2
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator") // Especifica qual bean deve ser injetado quando há múltiplos candidatos possíveis.
    private Validator validator;

    @Autowired
    AuthUserRequestClient authUserRequestClient;
    @Autowired
    LogUtils logUtils;


    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object obj, Errors errors) { //Método principal Validator.

        CourseDTO courseDTO = (CourseDTO) obj;

        // Faz a mesma coisa quando usa o @Valid no controller
        validator.validate(courseDTO, errors); // Delega a validação para o validador padrão para validar as anotações de validação mapeadas no CourseDTO

        if(!errors.hasErrors()){ // Se não tiver nenhum erro detectado nas anotações de validação mapeadas no CourseDTO
            validateUserInstructor(courseDTO.getUserInstructor(), errors);
        }
    }


    private void validateUserInstructor(UUID userInstructor, Errors errors) {

        try {
            UserDTO userDTO = authUserRequestClient.getOneUserById(userInstructor); // Comunicação síncrona com o AuthUser para buscar os dados do 'userInstructor'

            if(userDTO.getUserType().equals(UserType.STUDENT)){
                log.warn("VALIDATION - [validateUserInstructor] : The user is not an INSTRUCTOR or ADMIN to save a course :: userInstructor: {} - UserType: {}", userInstructor, userDTO.getUserType());
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN!");
            }

        }catch (HttpStatusCodeException e){

            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                log.warn("VALIDATION - [validateUserInstructor] : User not found :: {}", userInstructor);
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found!");

            }
        }
    }
}
