package com.ssp.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssp.DTO.ForgotPasswordRequest;
import com.ssp.DTO.OtpCacheData;
import com.ssp.DTO.ResetPasswordRequest;
import com.ssp.DTO.VerifyOtpRequest;
import com.ssp.Entity.UserAccount;
import com.ssp.Exception.InvalidOtpException;
import com.ssp.Exception.OtpExpiredException;
import com.ssp.Exception.OtpNotFoundException;
import com.ssp.Exception.PasswordMismatchException;
import com.ssp.Exception.UserNotFoundException;
import com.ssp.Repositry.IUserAccountRepositry;

@Service
public class ForgotPasswordService implements IForgotPasswordService{
	
	
	@Autowired
	private IUserAccountRepositry userRepo;

	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private IEmailService emailService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	private static final long OTP_EXPIRY_SEC=120;
	
	//-------------Send OTP-------------
	@Override
	public String sendotp(ForgotPasswordRequest req) {

		UserAccount user = userRepo.findByEmail(req.getEmail()).orElseThrow(()->new UserNotFoundException("Email Not Registered"));
		
		String otp= String.valueOf(new Random().nextInt(900000)+100000);
		
		LocalDateTime expiryTime=LocalDateTime.now().plusSeconds(OTP_EXPIRY_SEC);
		
		OtpCacheData otpData = new OtpCacheData(otp, expiryTime);
		
		cacheManager.getCache("otpCache").put(req.getEmail(), otpData);
		
		emailService.sendOtp(req.getEmail(), otp);
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {

				cacheManager.getCache("otpCache").evict(req.getEmail());
			}
		},OTP_EXPIRY_SEC*1000);
		return "OTP Sent To Registered Email";
	}

	//---------- STEP 2 — VERIFY OTP-----------
	@Override
	public String verifyOtp(VerifyOtpRequest req) {
       
		OtpCacheData cached = cacheManager.getCache("otpCache")
				               .get(req.getEmail(),OtpCacheData.class);
		
		if (cached==null) {
			throw new OtpNotFoundException("OTP expired or not generated");
		}
		
		// First check expiry
		if (cached.getExpiry().isBefore(LocalDateTime.now())) {
		    throw new OtpExpiredException("Otp expired");
		}

		// Then check validity
		if (!cached.getOtp().equals(req.getOtp())) {
		    throw new InvalidOtpException("Invalid OTP");
		}

		
	   return "OTP Verified Successfully";
	}

	//-----------STEP 3 — RESET PASSWORD--------
	@Override
	public String resetPassword(ResetPasswordRequest req) {

		 if (!req.getNewPassword().equals(req.getConfirmPassword())) {
	            throw new PasswordMismatchException("Passwords do not match");
	        }

	        UserAccount user = userRepo.findByEmail(req.getEmail())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        user.setPassword(encoder.encode(req.getNewPassword())); // encode if needed
	        userRepo.save(user);

	        cacheManager.getCache("otpCache").evict(req.getEmail());

	        return "Password reset successfully";
	}

	
}
