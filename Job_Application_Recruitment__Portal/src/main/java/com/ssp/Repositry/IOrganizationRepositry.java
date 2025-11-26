package com.ssp.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssp.DTO.OrganizationDto;
import com.ssp.Entity.Organization;

public interface IOrganizationRepositry extends JpaRepository<Organization, Long> {

	boolean existsByNameIgnoreCase(String name);

	List<Organization> findByNameContainingIgnoreCase(String name);
}
