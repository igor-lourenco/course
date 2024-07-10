package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

/* Classe para especificar os filtros que não estão mapeados na Controller */
public class SpecificationTemplate {

    @And({  // Combina múltiplos campos para filtrar
            @Spec(path = "courseLevel", spec = EqualIgnoreCase.class), // Filtra pelo Enum courseLevel pelo valor exato usando o EqualsIgnoreCase.class
            @Spec(path = "courseStatus", spec = Equal.class), // Filtra pelo Enum courseStatus pelo valor exato usando o Equals.class
            @Spec(path = "name", spec = Like.class) // Filtra pelo campo name onde contém o valor semelhante ao valor especificado usando o Like.class
    })
    public interface CourseSpec extends Specification<CourseModel> {}
}
