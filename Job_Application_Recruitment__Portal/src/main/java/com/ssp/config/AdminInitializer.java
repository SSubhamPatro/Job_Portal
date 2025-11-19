package com.ssp.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssp.Entity.Role;
import com.ssp.Entity.UserAccount;
import com.ssp.Repositry.IUserAccountRepositry;

@Configuration
public class AdminInitializer {
/*
 {
    "email":"tanvi.patra@example.com",
    "password":"SecurePass123"
}
  */
	@Bean
	public CommandLineRunner initAdmin(IUserAccountRepositry repo,PasswordEncoder encoder) {
		
		return args->{
			if(repo.findByEmail("superadmin@system.com").isEmpty()) {
				UserAccount admin = new UserAccount();
				admin.setEmail("superadmin@system.com");
				admin.setPassword(encoder.encode("System@123"));
				admin.setRole(Role.ADMIN);
				repo.save(admin);
				System.out.println("✅ Super Admin account created!	");
			}
		};
}
}