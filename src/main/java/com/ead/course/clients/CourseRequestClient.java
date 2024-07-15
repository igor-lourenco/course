package com.ead.course.clients;

import com.ead.course.DTOs.CourseDTO;
import com.ead.course.utils.LogUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Page<CourseDTO> getAllCoursesByUserPaged(UUID userId, Pageable pageable) {

        List<CourseDTO> searchResult = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;
        String url = RequestClientUtil.createUrl(userId, pageable);

        try {
            var responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};

            log.info("REQUEST GET [getAllCoursesByUserPaged] - URL: {}", url);

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            Page<CourseDTO> courseDTOPage = result.getBody();

            log.info("RESPONSE GET [getAllCoursesByUserPaged] - BODY: {}", logUtils.convertObjectToJson(courseDTOPage));

            searchResult = result.getBody().getContent();
            log.info("RESPONSE [getAllCoursesByUserPaged] - Number of Elements: {}", searchResult.size());

        } catch (HttpStatusCodeException e) {

            log.error("ERROR REQUEST [getAllCoursesByUserPaged] - url: {}", url);
            log.error("ERROR [getAllCoursesByUserPaged]: {}", e);
        }

        return result.getBody();
    }
}
