package com.ssp.Repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.Entity.Organization;

public interface IOrganizationRepositry extends JpaRepository<Organization, Long> {

	 boolean existsByNameIgnoreCase(String name);
    
	 Optional<Organization>findByNameIgnoreCase(String name);
	
     List<Organization>findByNameContainingIgnoreCase(String name);
	
}
