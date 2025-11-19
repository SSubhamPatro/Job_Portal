package com.ssp.Service;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IEmailTemplate templateHelper;

	@Value("${spring.mail.username}")
	private String fromMail;

	@Override
	public void sendApplicationThankYou(String to, String name, String jobTitle) {
		String sendMail = sendMail(to, "Application Received",
				templateHelper.getApplicationThankYouTemplate(name, jobTitle));
		System.out.println(sendMail);
	}

	@Override
	public void sendInterviewSchedule(String to, String name, LocalDateTime dateTime, String meetingLink) {
		String sendMail = sendMail(to, "Interview Scheduled",
				templateHelper.getInterViewScheduleTemplate(name, dateTime, meetingLink));
		System.out.println(sendMail);
	}

	@Override
	public void sendRejection(String to, String name, String jobTitle) {
		String sendMail = sendMail(to, "Application Update", templateHelper.getRejectionTemplate(name, jobTitle));
		System.out.println(sendMail);
	}

	
	@Override
	public void sendShortList(String to, String name, String jobTitle) {

		String sendMail = sendMail(to, "Application ShortListed", templateHelper.getShortListedTemplate(name, jobTitle));
	    System.out.println(sendMail);
	}
	
	@Override
	public void sendAccountCreate(String to, String name, String email) {

		String body=templateHelper.getAccountCreationTemplate(name, email);
		String sendMail = sendMail(to, "Account Created Successfully", body);
		System.out.println(sendMail);
	}
	
	@Override
	public void sendOtp(String to, String otp) {

		String body = templateHelper.getOtpTemplate(to, otp, 30);
		String sendMail = sendMail(to, otp, body);
		System.out.println(sendMail);
	}
	
	private String sendMail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromMail);
		message.setTo(to);
		message.setSubject(subject);
		message.setSentDate(new Date());
		message.setText(body);
		mailSender.send(message);
		return fromMail+" Email Message Is Sent To: "+to;
	}

}
