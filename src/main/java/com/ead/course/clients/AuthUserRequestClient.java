package com.ead.course.clients;

import com.ead.course.DTOs.CourseUserDTO;
import com.ead.course.DTOs.ResponsePageDTO;
import com.ead.course.DTOs.UserDTO;
import com.ead.course.utils.LogUtils;
import com.ead.course.utils.RequestClientUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class AuthUserRequestClient {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LogUtils logUtils;

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;

    public Page<UserDTO> getAllUsersByCoursePaged(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;

        String url = REQUEST_URL_AUTHUSER + RequestClientUtil.createUrlGETAllUsersByCourse(courseId, pageable);

        try {
            var responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            log.info("REQUEST GET [getAllUsersByCoursePaged] - URL: {}", url);

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            Page<UserDTO> userDTOPage = result.getBody();

            log.info("RESPONSE GET [getAllUsersByCoursePaged] - BODY: {}", logUtils.convertObjectToJson(userDTOPage));

            searchResult = result.getBody().getContent();
            log.info("RESPONSE [getAllUsersByCoursePaged] - Number of Elements: {}", searchResult.size());

        } catch (HttpStatusCodeException e) {

            log.error("ERROR REQUEST [getAllUsersByCoursePaged] - url: {}", url);
            log.error("ERROR [getAllUsersByCoursePaged]: {}", e);
        }

        return result.getBody();
    }

    public UserDTO getOneUserById(UUID userId){
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        ResponseEntity<UserDTO> result = null;

        try {

            log.info("REQUEST GET [getOneUserById] - URL: {}", url);

            result = restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
            UserDTO userDTO = result.getBody();

            log.info("RESPONSE GET [getOneUserById] - BODY: {}", logUtils.convertObjectToJson(userDTO));

            return userDTO;

        } catch (HttpStatusCodeException e) {
            log.error("ERROR [getOneUserById]: {}", e.getMessage());
            throw e;
        }
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
        CourseUserDTO body = new CourseUserDTO(userId, courseId);

        log.info("REQUEST POST [postSubscriptionUserInCourse] - URL: {} - BODY: {}", url, logUtils.convertObjectToJson(body));
        ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
        log.info("RESPONSE POST [postSubscriptionUserInCourse] - BODY: {}", response.getBody());
    }
}
