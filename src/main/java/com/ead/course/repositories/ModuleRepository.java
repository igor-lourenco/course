package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID> {

//     Garante que a consulta também traga a entidade CourseModel relacionada a esse ModuleModel, forçando o carregamento
//     "EAGER" da entidade 'course', ou seja, a entidade relacionada é carregada imediatamente junto a entidade ModuleModel
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

//    @Modifying // Indica que o método anotado é uma operação de modificação no banco de dados, como uma atualização, inserção ou exclusão.
    @Query(value = "SELECT * FROM TB_MODULES WHERE course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);
}
