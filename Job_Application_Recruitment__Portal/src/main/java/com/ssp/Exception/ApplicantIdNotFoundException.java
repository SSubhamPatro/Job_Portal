package com.ssp.Exception;

@SuppressWarnings("serial")
public class ApplicantIdNotFoundException extends RuntimeException {
   
	public ApplicantIdNotFoundException() {
        super();
	}
	public ApplicantIdNotFoundException(String msg) {
        super(msg);
	}
}
