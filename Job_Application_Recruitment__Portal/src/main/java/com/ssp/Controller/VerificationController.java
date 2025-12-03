package com.ssp.Controller;

import java.net.HttpURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssp.Response.ApiResponse;
import com.ssp.Service.IVerificationSerice;
import com.ssp.utility.IResponseMessage;

@RestController
@RequestMapping("/verify")
public class VerificationController {

	@Autowired
	private IVerificationSerice service;
     
	
	@GetMapping("/activate")
	 public ResponseEntity<ApiResponse>verifyToken(@RequestParam String token){
		 String verifyToken = service.verifyToken(token);
		 return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK, IResponseMessage.SUCCESS, verifyToken));
	 }
	
	@GetMapping("/resend")
	public ResponseEntity<ApiResponse> resendLink(@RequestParam String email) {
		
		String link =service.resendToken(email);
		return ResponseEntity.ok(new ApiResponse(HttpURLConnection.HTTP_OK,IResponseMessage.SUCCESS,link));
	}
	


}

