package com.ssp.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VerificationTokenDto {

	private Long id;
	
	private String token;
	
	private LocalDateTime expiryDate;
	
//	private 
}
