package com.ssp.DTO;

import java.util.List;

import com.ssp.Entity.CompanyType;

import lombok.Data;

@Data
public class RecruiterResponseDTO {
	private Long rid;
	private String name;
	private String email;
	private String companyName;
	private String department;
	private String designation;
	private String location;
	private String phone;
	private CompanyType companyType;

	private List<JobDTO> jobs; // nested jobs
	private int jobCount; // number of jobs
}
