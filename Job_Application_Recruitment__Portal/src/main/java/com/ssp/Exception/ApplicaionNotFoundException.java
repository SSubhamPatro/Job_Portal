package com.ssp.Exception;

@SuppressWarnings("serial")
public class ApplicaionNotFoundException extends RuntimeException {

	public ApplicaionNotFoundException() {
		super();
	}

	public ApplicaionNotFoundException(String msg) {
		super(msg);
	}

}
