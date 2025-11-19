package com.ssp.Service;


public interface IApplicationReviewService {

	void reviewApplication(Long applicationId,Long recruiterId,Boolean shortlisted);
	
}
