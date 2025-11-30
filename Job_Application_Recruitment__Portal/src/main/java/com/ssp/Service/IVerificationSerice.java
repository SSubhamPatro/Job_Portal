package com.ssp.Service;

import com.ssp.Entity.UserAccount;

public interface IVerificationSerice {

	void createAndSendToken(UserAccount account);
	
	String verifyToken(String token);
}
