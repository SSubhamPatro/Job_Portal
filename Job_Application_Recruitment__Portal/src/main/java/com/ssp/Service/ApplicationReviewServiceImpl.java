package com.ssp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssp.Entity.Application;
import com.ssp.Entity.Status;
import com.ssp.Exception.ApplicaionNotFoundException;
import com.ssp.Repositry.ApplicationRepositry;

@Service
public class ApplicationReviewServiceImpl implements IApplicationReviewService {

	@Autowired
	private ApplicationRepositry service;

	@Autowired
	private IEmailService emailService;

	@Override
	public void reviewApplication(Long applicationId, Long recruiterId, Boolean shortlisted) {

		Application application = service.findById(applicationId)
				.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found: " + applicationId));

		if (!application.getJob().getRecruiter().getRid().equals(recruiterId)) {
			throw new IllegalStateException("Recruiter not authorized to review this application!");
		}
		if (shortlisted) {
			application.setStatus(Status.SHORTLISTED);
			emailService.sendShortList(application.getApplicant().getUserAccount().getEmail(), application.getApplicant().getName(),
					application.getJob().getTitle());
		} else {
			application.setStatus(Status.REJECTED);
			emailService.sendRejection(application.getApplicant().getUserAccount().getEmail(), application.getApplicant().getName(),
					application.getJob().getTitle());
		}

	}

}
