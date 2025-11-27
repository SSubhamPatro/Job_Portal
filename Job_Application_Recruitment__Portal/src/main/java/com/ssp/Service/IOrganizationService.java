package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.OrganizationDto;
import com.ssp.Entity.Organization;

public interface IOrganizationService {

	List<String>searchOrganization(String query);
	
	OrganizationDto addOrganization(String name);
	
	//FOR RECRUITER ONLY
	Organization getOrCreateEntity(String name);
}
