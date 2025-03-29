package com.pizzastudio.centerpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@SpringBootApplication
public class CenterpointApplication extends SpringBootServletInitializer {

//	public static void main(String[] args) {
//		SpringApplication.run(CenterpointApplication.class, args);
//	}


//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(SpringApplication.class);
//	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CenterpointApplication.class);
	}

	// for tomcat
	public static void main(String[] args) {
		SpringApplication.run(CenterpointApplication.class, args);
	}


}
