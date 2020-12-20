package com.test.webservices.restfulwebservices;

//import io.swagger.models.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.test.webservices.restfulwebservices")
public class SwaggerConfig {

    public static final String DEFAULT_CONTACT = "Maria Mendeleva";

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("User Api Documentation",
                                                                "User Api Documentation", "1.0",
                                                                "http://localhost:8080/", DEFAULT_CONTACT,
                                                                "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0");

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json",
                                                                                    "application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(DEFAULT_API_INFO)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
