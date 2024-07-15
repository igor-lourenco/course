package com.ead.course.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ResponsePageDTO<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) // Modo que indica que os argumentos para o criador devem ser vinculados a partir das propriedades correspondentes do valor do Objeto de entrada, usando nomes de argumentos do criador (explícitos ou implícitos) para corresponder as propriedades do Objeto de entrada aos argumentos.
    public ResponsePageDTO(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements,
            @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public ResponsePageDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ResponsePageDTO(List<T> content) {
        super(content);
    }
}
