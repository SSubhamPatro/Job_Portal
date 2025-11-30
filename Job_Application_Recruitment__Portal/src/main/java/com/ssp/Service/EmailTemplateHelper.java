package com.ssp.Service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateHelper implements IEmailTemplate {

	// 1. On Job Application
	@Override
	public String getApplicationThankYouTemplate(String applicantName, String jobTitle) {

		return "Dear " + applicantName + ",\n\n" + "Thank you for applying for the position of " + jobTitle + ".\n"
				+ "We will review your application and get back to you soon.\n\n" + "Best Regards,\nHR Team";
	}

	// 2. On Interview Schedule
	@Override
	public String getInterViewScheduleTemplate(String applicantName, LocalDateTime dateTime, String meetingLink) {

		return "Dear " + applicantName + ",\n\n" + "Your interview is scheduled on " + dateTime + ".\n"
				+ "Please join using this link: " + meetingLink + "\n\n" + "Best of luck!\nHR Team";
		
	}

    // 3. On Rejection
	@Override
	public String getRejectionTemplate(String applicantName, String jobTitle) {
		return  "Dear " + applicantName + ",\n\n" +
	               "We regret to inform you that your application for " + jobTitle +
	               " has not been successful at this time.\n\n" +
	               "We wish you the best in your future endeavors.\n\n" +
	               "Sincerely,\nHR Team";
	}

	//4. On ShortList 
	@Override
	public String getShortListedTemplate(String applicantName, String jobTitle) {

		return "Dear " + applicantName + ",\n\n"
                + "Congratulations! 🎉\n"
                + "Your application for the position of " + jobTitle
                + " has been shortlisted.\n"
                + "Our HR team will contact you shortly with the next steps.\n\n"
                + "Best Regards,\nHR Team";
    }
   
	// 5. On Account Creation
	@Override
	public String getAccountCreationTemplate(String applicantName, String email) {

		return "Dear " + applicantName + ",\n\n"
                + "Welcome to our platform! 🎉\n"
                + "Your account has been successfully created with the email: " + email + ".\n\n"
                + "You can now log in and explore our services.\n\n"
                + "Best Regards,\nSupport Team";
	}
	
	@Override
	public String getOtpTemplate(String email, String otp, long expirySeconds) {

		return "Dear user,\n\n"
	            + "Your OTP for password reset is: " + otp + "\n"
	            + "This OTP will expire in " + expirySeconds + " seconds.\n\n"
	            + "If you did not request this, please ignore this email.\n\n"
	            + "Regards,\nSupport Team";
	}
	
	@Override
	public String getLinkTemplate(String name,String email, String link) {
       
		return "Welcome, "+name+"!\n\n"
			     +"Thank you for signing up!We are excited to have you on board."+"\n\n"
				 +"Before you start posting jobs,we need you to verify your email address,We've sent you an email at "+email+"\n"
				 +"Please click on the activation link to get started."+"\n\n"
				 +"If you did not receive any email,please check your spam folder or requst"+link;
	}
}
