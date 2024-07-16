package com.ead.course.utils;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class RequestClientUtil {


    public static String createUrlGETAllUsersByCourse(UUID courseId, Pageable pageable){

        String url = "/users?courseId=" + courseId + paginationParameters(pageable);

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
