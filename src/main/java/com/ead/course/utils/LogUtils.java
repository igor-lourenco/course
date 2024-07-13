package com.ead.course.utils;

import com.ead.course.models.CourseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LogUtils {

    @Autowired
    ObjectMapper objectMapper;

    public String convertObjectToJson(Page<?> modelPage) {
        try {
            objectMapper.registerModule(new JavaTimeModule());                     // Adiciona suporte para serialização e desserialização de tipos de data e hora do Java 8 (por exemplo, LocalDate, LocalDateTime).
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Força a não escrever datas como timestamps (Long), mas sim como String ("2024-07-12T16:29:23").
            String pageJson = objectMapper.writeValueAsString(modelPage);
            return pageJson;                                                     // Converte o objeto para JSON
        } catch (JsonProcessingException e) {
            log.error("Error converting page to JSON", e.getMessage());
            return modelPage.toList().toString();
        }
    }

    public String convertObjectToJson(Object obj) {
        try {
            objectMapper.registerModule(new JavaTimeModule());                     // Adiciona suporte para serialização e desserialização de tipos de data e hora do Java 8 (por exemplo, LocalDate, LocalDateTime).
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Força a não escrever datas como timestamps (Long), mas sim como String ("2024-07-12T16:29:23").
            String pageJson = objectMapper.writeValueAsString(obj);
            return pageJson;                                                     // Converte o objeto para JSON
        } catch (JsonProcessingException e) {
            log.error("Error converting page to JSON", e.getMessage());
            return obj.toString();
        }
    }
}
