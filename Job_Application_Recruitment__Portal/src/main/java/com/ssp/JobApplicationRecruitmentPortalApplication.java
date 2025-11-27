package com.ssp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "Job Application Recruitment Portal", 
				version = "3.0", 
				description = "Welcome To SSP Techonolgies", 
				contact = @Contact(name = "SSP Technologies", email = "subhampatra160@gmail.com")))
@EnableCaching
@EnableAsync // 🔑 enables async execution
@SpringBootApplication
public class JobApplicationRecruitmentPortalApplication {

	/*@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}*/
	
	
	public static void main(String[] args) {
		SpringApplication.run(JobApplicationRecruitmentPortalApplication.class, args);
	}

}

