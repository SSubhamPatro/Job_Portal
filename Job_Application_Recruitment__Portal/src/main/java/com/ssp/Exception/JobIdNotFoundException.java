package com.ssp.Exception;


public class JobIdNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	public JobIdNotFoundException() {
         super();
	}
	public JobIdNotFoundException(String msg) {
        super(msg);
	}
}
