package com.ssp.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssp.DTO.OrganizationDto;
import com.ssp.Entity.Organization;
import com.ssp.Repositry.IOrganizationRepositry;

@Service
public class OrganizationServiceImpl implements IOrganizationService {
	

    @Autowired
    private IOrganizationRepositry orRepo;

    private final String CACHE_NAME = "orgSearchCache";

    @Cacheable(value = CACHE_NAME, key = "#query.toLowerCase()")
    @Override
    public List<String> searchOrganization(String query) {
        System.out.println("Fetching from DB (not cache) → " + query);

        if (query == null || query.isBlank()) return List.of();

        String formatted = formatName(query);
        return orRepo.findByNameContainingIgnoreCase(formatted)
                .stream()
                .map(Organization::getName)
                .toList();
    }

    @CacheEvict(value = CACHE_NAME, allEntries = true)
    @Override
    public OrganizationDto addOrganization(String name) {

        String formatted = formatName(name);

        // If exists → return DTO
        if (orRepo.existsByNameIgnoreCase(formatted)) {
            Organization org = orRepo.findByNameContainingIgnoreCase(formatted)
                    .stream()
                    .filter(s -> s.getName().equalsIgnoreCase(formatted))
                    .findFirst()
                    .get();

            return toDto(org);
        }

       
        Organization org = new Organization(formatted);
        Organization saved = orRepo.save(org);

        return toDto(saved);
    }

    // Convert entity → DTO
    private OrganizationDto toDto(Organization org) {
        return new OrganizationDto(org.getId(), org.getName());
    }

    
    private String formatName(String name) {
        name = name.trim();
        return Arrays.stream(name.split("\\s+"))
                .filter(s -> !s.isBlank())
                .map(w -> w.substring(0, 1).toUpperCase() + w.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}
