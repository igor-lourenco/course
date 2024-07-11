package com.ead.course.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_MODULES")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Padrão ISO 8601 UTC
    private LocalDateTime creationDate;

//  @JsonIgnore // Essa anotação sobressai ao @JsonProperty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Configuração de acesso que significa que essa propriedade só pode ser escrita (set) para desserialização, mas não será lida (get) na serialização, ou seja, o valor da propriedade não é incluído na serialização.
    @ManyToOne(optional = false, fetch = FetchType.LAZY)  // cria chave estrangeira(course_course_id) para o course e carregamento lento(FetchType.LAZY)
    private CourseModel course;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY) // Campo que está a chave estrangeira na outra tabela para referenciar esse module
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<LessonModel> lessons;

}
