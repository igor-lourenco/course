package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_COURSES")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignora campos com valores nulos durante a serialização para JSON
public class CourseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 250)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Padrão ISO 8601 UTC
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") // Padrão ISO 8601 UTC
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private UUID userInstructor;

//  @JsonIgnore // Essa anotação sobressai ao @JsonProperty
//  @OnDelete(action = OnDeleteAction.CASCADE) // Permite definir o comportamento de exclusão em cascata no nível do banco de dados para relacionamentos entre entidades
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Configuração de acesso que significa que essa propriedade só pode ser escrita (set) para desserialização, mas não será lida (get) na serialização, ou seja, o valor da propriedade não é incluído na serialização.
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY) // Campo que está a chave estrangeira na outra tabela para referenciar esse course e carregamento lento(FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT) //  Evita a N+1 consulta, o hibernate carrega as entidades relacionadas usando uma subconsulta. Em vez de executar uma consulta separada para cada entidade relacionada, o Hibernate executa uma única subconsulta para carregar todas as entidades relacionadas em uma única operação.
    private Set<ModuleModel> modules;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Configuração de acesso que significa que essa propriedade só pode ser escrita (set) para desserialização, mas não será lida (get) na serialização, ou seja, o valor da propriedade não é incluído na serialização.
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY) // Campo que está a chave estrangeira na outra tabela para referenciar esse course e carregamento lento(FetchType.LAZY)
    private Set<CourseUserModel> coursesUsers;

}
