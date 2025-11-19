package com.ssp.Security;
	
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.core.userdetails.UserDetails;
	import org.springframework.security.core.userdetails.UserDetailsService;
	import org.springframework.security.core.userdetails.UsernameNotFoundException;
	import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
	
import com.ssp.Entity.UserAccount;
import com.ssp.Repositry.IUserAccountRepositry;
	
	@Service
	public class CustomUserDetailsService implements UserDetailsService {
	
		   @Autowired
		    private IUserAccountRepositry userRepo;
		
		@Override
		public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
			 UserAccount user = userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found: "+email));
			 
			 var authorities=List.of(new SimpleGrantedAuthority(user.getRole().name()));
			
			return new User(user.getEmail(),user.getPassword(),authorities);
			
			
		}
	
	}
