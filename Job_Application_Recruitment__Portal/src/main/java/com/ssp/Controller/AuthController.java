package com.ssp.Controller;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.DTO.ForgotPasswordRequest;
import com.ssp.DTO.LoginRequestDTO;
import com.ssp.DTO.LoginResponse;
import com.ssp.DTO.ResetPasswordRequest;
import com.ssp.DTO.VerifyOtpRequest;
import com.ssp.Entity.UserAccount;
import com.ssp.Repositry.IUserAccountRepositry;
import com.ssp.Response.ApiResponse;
import com.ssp.Security.JwtUtil;
import com.ssp.Service.IForgotPasswordService;
import com.ssp.utility.IResponseMessage;

//👤 Step 4: Authentication Controller
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	//How We can access unreferenced object
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JwtUtil jwtUtil; 
	
	@Autowired
	private IUserAccountRepositry userRepo;
	
	@Autowired
	private IForgotPasswordService forgotService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse>login(@RequestBody LoginRequestDTO loginRequestDTO){
		try {
			
			Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
			//Generate JWt
			System.out.println(authentication);
			UserAccount user = userRepo.findByEmail(loginRequestDTO.getEmail()).orElseThrow();
			String token = jwtUtil.generateToken(loginRequestDTO.getEmail(),user.getRole());
			return  ResponseEntity.ok(new LoginResponse("Login Successful",token,user));
					}
		catch (BadCredentialsException e) {
          return ResponseEntity.status(401).body(new LoginResponse("Invalid credentials",null,null));
		} 
	}
	// Send OTP
	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse>sendOtp(@RequestBody ForgotPasswordRequest req){
		String sendotp = forgotService.sendotp(req);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK,IResponseMessage.SUCCESS,sendotp));
	}
	
	//Verify OTP
	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse>verifyOtp(@RequestBody VerifyOtpRequest req){
		
		String verifyOtp = forgotService.verifyOtp(req);
		
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK,IResponseMessage.SUCCESS,verifyOtp));
	}
	
	//Reset password (after OTP verified by client)
	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse>resetPassword(@RequestBody ResetPasswordRequest req){
		
		String resetPassword = forgotService.resetPassword(req);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK,IResponseMessage.SUCCESS,resetPassword));
 
		
	}
}
