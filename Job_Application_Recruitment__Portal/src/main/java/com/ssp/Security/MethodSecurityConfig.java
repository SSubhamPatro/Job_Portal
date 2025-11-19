package com.ssp.Security;

import com.ssp.Controller.AuthController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {

   
	@Bean
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		
		DefaultMethodSecurityExpressionHandler expressionHandler = 
				new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setDefaultRolePrefix(""); //✅ Remove ROLE_ prefix
	  return expressionHandler;
	}
}
