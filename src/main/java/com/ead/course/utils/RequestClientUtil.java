package com.ead.course.utils;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class RequestClientUtil {

    private static String REQUEST_URI = "http://localhost:8087";

    public static String createUrl(UUID courseId, Pageable pageable){

        String url = REQUEST_URI + "/users?courseId=" + courseId + paginationParameters(pageable);

        return url;
    }

    private static String paginationParameters(Pageable pageable){

        StringBuilder builder = new StringBuilder();
        builder.append("&page=" + pageable.getPageNumber());
        builder.append("&size=" + pageable.getPageSize());
        builder.append("&sort=" + pageable.getSort().toString().replaceAll(": ", ","));

        return builder.toString();
    }
}
