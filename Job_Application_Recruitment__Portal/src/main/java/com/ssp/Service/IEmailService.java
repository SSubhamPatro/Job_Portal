package com.ssp.Service;

import java.time.LocalDateTime;

public interface IEmailService {

	void sendApplicationThankYou(String to, String name, String jobTitle);

	void sendInterviewSchedule(String to, String name, LocalDateTime dateTime, String meetingLink);

	void sendRejection(String to, String name, String jobTitle);
    
	void sendShortList(String to,String name,String jobTitle);
	
	void sendAccountCreate(String to,String name,String email);
	
	void sendOtp(String to,String otp);
}
