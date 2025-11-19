package com.ssp.DTO;

import java.util.List;

import lombok.Data;

@Data
public class RecruiterResponseDTO {
	    private Long rid;
	    private String name;
	    private String email;
	    private String companyName;
	    private String department;
	    private List<JobDTO> jobs;  // nested jobs
	    private int jobCount;       // number of jobs
	}
