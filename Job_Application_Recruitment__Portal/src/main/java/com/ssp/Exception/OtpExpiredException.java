package com.ssp.Exception;

@SuppressWarnings("serial")
public class OtpExpiredException extends RuntimeException {

	public OtpExpiredException() {
	}
	
	public OtpExpiredException(String msg) {
       
		super(msg);
	
	  }
}

