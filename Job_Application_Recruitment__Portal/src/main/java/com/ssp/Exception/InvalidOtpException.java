package com.ssp.Exception;

@SuppressWarnings("serial")
public class InvalidOtpException extends RuntimeException {

	public InvalidOtpException() {
	}
	
	public InvalidOtpException(String msg) {
         
		super(msg);
	
	}
}
