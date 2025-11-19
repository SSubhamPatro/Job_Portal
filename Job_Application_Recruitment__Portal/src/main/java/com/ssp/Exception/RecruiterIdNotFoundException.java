package com.ssp.Exception;

@SuppressWarnings("serial")
public class RecruiterIdNotFoundException extends RuntimeException {

	public RecruiterIdNotFoundException() {
		super();
	}

	public RecruiterIdNotFoundException(String msg) {
		super(msg);
	}
}
