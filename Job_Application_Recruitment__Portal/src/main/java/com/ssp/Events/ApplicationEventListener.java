package com.ssp.Events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ssp.Service.IEmailService;

@Component
public class ApplicationEventListener {

	@Autowired
	private IEmailService emailservice;

	// 🔑 Trigger only after DB commit + run asynchronously
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleApplicantCreated(ApplicantCreatedEvent event) {
		System.out.println("Async email sending started for: " + event.getEmail());
		try {
			emailservice.sendAccountCreate(event.getEmail(), event.getName(), event.getEmail());
			System.out.println("Email Sent Successfully To: " + event.getEmail());
		} catch (Exception e) {

			System.err.println("Failed To Send Email To: " + event.getEmail() + " : " + e.getMessage());
		}

	}
}
/*
BEFORE_COMMIT → Runs just before transaction commit.

AFTER_COMMIT → (most common) Runs only if transaction commits successfully.

AFTER_ROLLBACK → Runs if transaction fails/rolls back.

AFTER_COMPLETION → Runs in both cases (commit or rollback).

✅ Why it’s useful

Example use cases:

Send email after user registration → AFTER_COMMIT.

Clear cache after DB update → AFTER_COMMIT.

Log rollback reason → AFTER_ROLLBACK.

🔹 Flow Now

1.User registers.

2.Applicant is saved in DB inside a transaction.

3.On successful commit, ApplicantCreatedEvent is fired.

4.Listener is triggered, but thanks to @Async, it runs in a separate thread.

5.The API response is returned immediately to the user ("Registration successful"), 
  while the email is sent in the background.
  
  */
