package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.RecruiterDTO;
import com.ssp.DTO.RecruiterResponseDTO;
import com.ssp.DTO.RecruiterUpdateDTO;

public interface IRecruiterServiceManagement {

	String registerRecruiterDetails(RecruiterDTO recruiter);
	List<RecruiterResponseDTO>viewAllRecruiterDetails();
	String viewByIdRecruiterDetails(Long id);
	String removeById(Long id);
	String updateDetails(RecruiterUpdateDTO recruiter);
	
}
