package org.omnera.picbloom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name="omnera-demo-api",scheme="bearer",type=SecuritySchemeType.HTTP,in =SecuritySchemeIn.HEADER)
@ConfigurationPropertiesScan
public class PicbloomApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicbloomApplication.class, args);
	}

}
