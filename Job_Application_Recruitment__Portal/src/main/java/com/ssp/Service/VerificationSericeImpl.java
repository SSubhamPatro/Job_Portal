package com.ssp.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssp.Entity.Role;
import com.ssp.Entity.UserAccount;
import com.ssp.Entity.VerificationToken;
import com.ssp.Exception.DuplicateEmailException;
import com.ssp.Exception.InvalidTokenException;
import com.ssp.Repositry.IUserAccountRepositry;
import com.ssp.Repositry.IVerificationTokenRepositry;


@Service
public class VerificationSericeImpl implements IVerificationSerice {

	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Autowired
	private IVerificationTokenRepositry repo;
	
	@Autowired
	private IEmailService service;
	
	@Autowired
	private IUserAccountRepositry userRepo;
	
	//Save +Email Token
	@Override
	public void createAndSendToken(UserAccount user) {

		String token =UUID.randomUUID().toString();
		 System.out.println("Generated Token: "+token);
		VerificationToken verify = new VerificationToken();
		verify.setToken(token);
		verify.setExpiryDate(LocalDateTime.now().plusMinutes(2));
		verify.setAccount(user);
		repo.save(verify);
		 
		String link = frontendUrl+"/activate-account?token="+token;
		
		
		service.sendLink(user.getEmail(),
				user.getRecruiter().getName(),
				verify.getAccount().getEmail(),
				link
				);
		
		
	}
	
	@Override
	@Transactional
	public String verifyToken(String token) {
      
		VerificationToken vt = repo.findByToken(token).orElseThrow(()->new InvalidTokenException("Invalid Token"));
		
		if(vt.getExpiryDate().isBefore(LocalDateTime.now())) {
			repo.delete(vt);
			return "Token Expired";
		}
		
		UserAccount user = vt.getAccount();
		user.setRole(Role.RECRUITER);
		userRepo.save(user);
		
		repo.delete(vt); //delete after successful activation
		
		return "Account Activated Successfully";
	}

	@Override
	public String resendToken(String email) {

		UserAccount userAccount = userRepo.findByEmail(email).orElseThrow(()->new DuplicateEmailException("Email Not Found"));
	     
		//Delete Old Token
		repo.deleteByAccount(userAccount);
		
		createAndSendToken(userAccount);
		return "Activate Link Sent Again";
	}

}
