package com.ssp.Service;

import java.time.LocalDateTime;

public interface IEmailTemplate{
	
	String getApplicationThankYouTemplate(String applicantName,String jobTitle);
	String getInterViewScheduleTemplate(String applicantName,LocalDateTime dateTime,String mettingLink);
	String getRejectionTemplate(String applicantName, String jobTitle);
    String getShortListedTemplate(String applicantName,String jobTitle);
    String getAccountCreationTemplate(String applicantName,String email);
    String getOtpTemplate(String email,String otp,long expirySeconds);
    String getLinkTemplate(String name,String email,String link); 
}
