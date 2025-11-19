package com.ssp.DTO;

import lombok.Data;

@Data
public class ResetPasswordRequest {

	private String email;
	
	private String newPassword;
	
	private String confirmPassword;
}
