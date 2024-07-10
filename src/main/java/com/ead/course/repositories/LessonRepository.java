package com.ead.course.repositories;

import com.ead.course.models.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

//  @Modifying // Indica que o método anotado é uma operação de modificação no banco de dados, como uma atualização, inserção ou exclusão.
    @Query(value = "SELECT * FROM TB_LESSONS WHERE module_module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);

    @Query(value = "SELECT * FROM TB_LESSONS WHERE module_module_id = :moduleId AND lesson_id = :lessonId", nativeQuery = true)
    Optional<LessonModel> findLessonIntoModule(@Param("moduleId")UUID moduleId, @Param("lessonId") UUID lessonId);
}
