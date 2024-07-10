package com.ead.course.configs;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration    // essa classe vai converte os dados dos parâmetros vindo da requisição para tipos básicos do Java
public class ResolverConfig extends WebMvcConfigurationSupport {

    /*
    O método addArgumentResolvers() serve para, adicionar HandlerMethodArgumentResolvers personalizados para usar além
        daqueles registrados por padrão.
    Os resolvedores de argumentos personalizados são invocados antes dos resolvedores integrados,
        exceto aqueles que dependem da presença de anotações (por exemplo, @RequestParameter, @PathVariable, etc).
    Este último pode ser personalizado configurando o RequestMappingHandlerAdapter diretamente.
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());

        /*
        A classe PageableHandlerMethodArgumentResolver extrai informações de paginação de solicitações da web e, assim,
            permite injetar instâncias Pageable em métodos de controlador. As propriedades da solicitação a
            serem analisadas podem ser configuradas.

        A configuração padrão usa parâmetros de solicitação começando
            com DEFAULT_PAGE_PARAMETERDEFAULT_QUALIFIER_DELIMITER.
         */
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(resolver);

        super.addArgumentResolvers(argumentResolvers);
    }
}
