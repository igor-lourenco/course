package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<ModuleModel, UUID>, JpaSpecificationExecutor<ModuleModel> {

//     Garante que a consulta também traga a entidade CourseModel relacionada a esse ModuleModel, forçando o carregamento
//     "EAGER" da entidade 'course', ou seja, a entidade relacionada é carregada imediatamente junto a entidade ModuleModel
    @EntityGraph(attributePaths = {"course"})
    ModuleModel findByTitle(String title);

//    @Modifying // Indica que o método anotado é uma operação de modificação no banco de dados, como uma atualização, inserção ou exclusão.
    @Query(value = "SELECT * FROM TB_MODULES WHERE course_course_id = :courseId", nativeQuery = true)
    List<ModuleModel> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "SELECT * FROM TB_MODULES WHERE course_course_id = :courseId AND module_id = :moduleId", nativeQuery = true)
    Optional<ModuleModel> findModuleIntoCourse(@Param("courseId") UUID courseId, @Param("moduleId")UUID moduleId);

}
