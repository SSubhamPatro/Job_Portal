package com.ssp.Service;

import com.ssp.DTO.ForgotPasswordRequest;
import com.ssp.DTO.ResetPasswordRequest;
import com.ssp.DTO.VerifyOtpRequest;

public interface IForgotPasswordService {

	 String sendotp(ForgotPasswordRequest req);
	 
	 String verifyOtp(VerifyOtpRequest req);
	 
	 String resetPassword(ResetPasswordRequest req);
}
