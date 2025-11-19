package com.ssp.Response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {

   private Integer statusCode;
	
	private String status;
	
	private String message;
	
	private Object data;
	
	private List<?>list;

	public ApiResponse(Integer statusCode, String status, String message, Object data) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public ApiResponse(Integer statusCode, String status, String message) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
	}

	public ApiResponse(Integer statusCode, String status, String message, List<?> list) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.list = list;
	}
	
	public ApiResponse(Integer statusCode, String status,Object data) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.data = data;
	}
	
	
}
