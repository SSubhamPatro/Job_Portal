package com.ssp.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponse {

	private String message;
	private ApplicantDTO dto;
	
}
