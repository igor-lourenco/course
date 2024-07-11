package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

/** Classe para especificar os filtros que não estão mapeados na Controller */
public class SpecificationTemplate {

    @And({  // Combina múltiplos campos para filtrar
            @Spec(path = "courseLevel", spec = EqualIgnoreCase.class), // Filtra pelo Enum courseLevel pelo valor exato usando o EqualsIgnoreCase.class
            @Spec(path = "courseStatus", spec = Equal.class), // Filtra pelo Enum courseStatus pelo valor exato usando o Equals.class
            @Spec(path = "name", spec = Like.class) // Filtra pelo campo name onde contém o valor semelhante ao valor especificado usando o Like.class
    })
    public interface CourseSpec extends Specification<CourseModel> {}


    @Spec(path = "title", spec = Like.class) // Filtra pelo campo title onde contém o valor semelhante ao valor especificado usando o Like.class
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like.class) // Filtra pelo campo title onde contém o valor semelhante ao valor especificado usando o Like.class
    public interface LessonSpec extends Specification<LessonModel> {}


    public static Specification<ModuleModel> findModulesByCourseId(final UUID courseId) {
        return ((root, query, criteriaBuilder) -> {

            // Especifica pra eliminar os resultados duplicados da consulta
            query.distinct(true);

            // Define a raiz da entidade ModuleModel
            Root<ModuleModel> moduleRoot = root;

            // Adiciona a entidade CourseModel ao objeto query para a junção entre ModuleModel e CourseModel
            Root<CourseModel> courseRoot = query.from(CourseModel.class);

            // Obtém a coleção de 'modules' associados a um 'course'
            Expression<Collection<ModuleModel>> modulesOfThisCourse = courseRoot.get("modules");

            // Cria um Predicate para verificar se o courseId do curso é igual ao courseId fornecido
            Predicate criteriaBuilderCourseEqualCourseId = criteriaBuilder.equal(courseRoot.get("courseId"), courseId);

            // Cria um Predicate para verificar se o module atual(moduleRoot) é um membro da coleção de modules do curso(modulesOfThisCourse)
            Predicate criteriaBuilderModuleIsMemberCourse = criteriaBuilder.isMember(moduleRoot, modulesOfThisCourse);

            // Combina os dois Predicate usando o "AND" e retorna o resultado
            return criteriaBuilder.and(criteriaBuilderCourseEqualCourseId, criteriaBuilderModuleIsMemberCourse);
        });
    }


    public static Specification<LessonModel> findLessonsByModuleId(final UUID moduleId) {
        return ((root, query, criteriaBuilder) -> {

            // Especifica pra eliminar os resultados duplicados da consulta
            query.distinct(true);

            // Define a raiz da entidade LessonModel
            Root<LessonModel> lessonRoot = root;

            // Adiciona a entidade ModuleModel ao objeto query para a junção entre ModuleModel e LessonModel
            Root<ModuleModel> moduleRoot = query.from(ModuleModel.class);

            // Obtém a coleção de 'lessons' associados a um 'module'
            Expression<Collection<LessonModel>> lessonsOfThisModule = moduleRoot.get("lessons");

            // Cria um Predicate para verificar se o moduleId do module é igual ao moduleId fornecido
            Predicate criteriaBuilderModuleEqualModuleId = criteriaBuilder.equal(moduleRoot.get("moduleId"), moduleId);

            // Cria um Predicate para verificar se o lesson atual(lessonRoot) é um membro da coleção de modules (lessonsOfThisModule)
            Predicate criteriaBuilderLessonIsMemberModule = criteriaBuilder.isMember(lessonRoot, lessonsOfThisModule);

            // Combina os dois Predicate usando o "AND" e retorna o resultado
            return criteriaBuilder.and(criteriaBuilderModuleEqualModuleId, criteriaBuilderLessonIsMemberModule);
        });
    }

}
