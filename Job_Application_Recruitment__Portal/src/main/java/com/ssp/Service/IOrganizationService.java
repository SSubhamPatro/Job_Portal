package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.OrganizationDto;

public interface IOrganizationService {

	List<String>searchOrganization(String query);
	
	OrganizationDto addOrganization(String name);
}
