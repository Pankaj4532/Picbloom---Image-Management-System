package org.omnera.picbloom.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Picbloom User Api", version = "Versio 1.0", 
    contact = @Contact(name = "Pankaj", email = "psapkal474@gmail.com", url = "https://github.com/Pankaj4532"), 
    license = @License(name = "Apache 2.0", url = "https://apache.org/licenses/LICENSE-2.0"), 
    termsOfService = "https://studyeasy.org/", 
    description = "Spring boot RestFul Api For picbloom by Pankaj"))


public class SwaggerConfig {

}
