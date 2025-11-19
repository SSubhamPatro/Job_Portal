package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.ApplicationDTO;

public interface IApplicationService {

	String createApplication(ApplicationDTO dto);
	List<ApplicationDTO>getAllApplication();
	ApplicationDTO getApplicationById(Long id);
	String deleteApplicationById(Long id);
	String updateApplication(ApplicationDTO dto);
}
