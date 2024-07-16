package com.ead.course.clients;

import com.ead.course.DTOs.CourseDTO;
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
public class CourseRequestClient {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LogUtils logUtils;

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL;

    public Page<UserDTO> getAllUsersByCoursePaged(UUID courseId, Pageable pageable) {

        List<UserDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<UserDTO>> result = null;



        String url = REQUEST_URL + RequestClientUtil.createUrlGETAllUsersByCourse(courseId, pageable);

        try {
            var responseType = new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            log.info("REQUEST GET [getAllUsersByCoursePaged] - URL: {}", url);

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            Page<UserDTO> courseDTOPage = result.getBody();

            log.info("RESPONSE GET [getAllUsersByCoursePaged] - BODY: {}", logUtils.convertObjectToJson(courseDTOPage));

            searchResult = result.getBody().getContent();
            log.info("RESPONSE [getAllUsersByCoursePaged] - Number of Elements: {}", searchResult.size());

        } catch (HttpStatusCodeException e) {

            log.error("ERROR REQUEST [getAllUsersByCoursePaged] - url: {}", url);
            log.error("ERROR [getAllUsersByCoursePaged]: {}", e);
        }

        return result.getBody();
    }
}
