package com.ssp.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssp.DTO.ApplicantDTO;
import com.ssp.DTO.ApplicantRegisterDTO;
import com.ssp.DTO.ApplicantResponse;

public interface IApplicantServiceManagement {

	ApplicantResponse cretateApplicantDetails(ApplicantRegisterDTO applicantDTO,MultipartFile file);
	List<ApplicantDTO>getAllDetails();
	String deleteApplicantDetailsById(Long id);
	ApplicantDTO getDetailsById(Long Id);
//	ApplicantDTO loginApplicant(String email,String password);
}
