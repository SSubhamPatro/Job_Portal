package com.ssp.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssp.DTO.JobDTO;
import com.ssp.DTO.RecruiterDTO;
import com.ssp.DTO.RecruiterResponseDTO;
import com.ssp.DTO.RecruiterUpdateDTO;
import com.ssp.Entity.Organization;
import com.ssp.Entity.Recruiter;
import com.ssp.Entity.Role;
import com.ssp.Entity.Status;
import com.ssp.Entity.UserAccount;
import com.ssp.Exception.DuplicateEmailException;
import com.ssp.Exception.RecruiterIdNotFoundException;
import com.ssp.Repositry.IRecruiterRepositry;
import com.ssp.Repositry.IUserAccountRepositry;


@Service
public class RecruiterServiceImpl implements IRecruiterServiceManagement {

    @Autowired
    private IRecruiterRepositry recruiterRepo;

    @Autowired
    private IUserAccountRepositry userRepo;

    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private IOrganizationService  organizationService;

    @Autowired
    private IVerificationSerice verificationSerice;
    
    
    // ------------------------------------------------------------
    // CREATE RECRUITER
    // ------------------------------------------------------------
    @Transactional
    @Override
    @CacheEvict(value = "recruiters-viewAllDetails", allEntries = true)
    public String registerRecruiterDetails(RecruiterDTO dto) {

        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already registered!");
        }

        // Create new user account
        UserAccount account = new UserAccount();
        account.setEmail(dto.getEmail());
        account.setPassword(encoder.encode(dto.getPassword()));
        account.setRole(Role.RECRUITER);
//        SAVE USER FIRST
        userRepo.save(account);
        
        //Create New Organization 
        Organization organization = organizationService.getOrCreateEntity(dto.getOrganization());
       
        
        
        // Create recruiter
        Recruiter recruiter = new Recruiter();
        recruiter.setName(dto.getName());
//        recruiter.setCompanyName(dto.getCompanyName());
        recruiter.setDepartment(dto.getDepartment());
        recruiter.setDesignation(dto.getDesignation());
        recruiter.setLocation(dto.getLocation());
        recruiter.setPhone(dto.getPhone());
        recruiter.setCompanyType(dto.getCompanyType());
        recruiter.setOrganization(organization);
        recruiter.setUserAccount(account);
        account.setRecruiter(recruiter);// IMPORTANT: set both sides
        Long rid = recruiterRepo.save(recruiter).getRid();
     // SAVE USER AGAIN BECAUSE reverse side changed
       userRepo.save(account);        
        verificationSerice.createAndSendToken(account);
        return "Recruiter saved with ID: " + rid;
    }
/*
 Why save userRepo.save(account) again?

Because:

Recruiter has foreign key to User (owning side)

But User also has recruiter mapped (inverse side)

Hibernate will NOT auto-update inverse side unless saved
  
  
  */
    

    // ------------------------------------------------------------
    // VIEW ALL RECRUITERS
    // ------------------------------------------------------------
    @Override
    @Cacheable(value = "recruiters-viewAllDetails")
    public List<RecruiterResponseDTO> viewAllRecruiterDetails() {

        List<Recruiter> recruiters = recruiterRepo.findAll();

        return recruiters.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    // ------------------------------------------------------------
    // VIEW RECRUITER BY ID
    // ------------------------------------------------------------
    @Override
    @Cacheable(value = "recruiters-viewById", key = "#id")
    public RecruiterResponseDTO viewByIdRecruiterDetails(Long id) {

        Recruiter recruiter = recruiterRepo.findById(id)
                .orElseThrow(() -> new RecruiterIdNotFoundException("Invalid ID: " + id));

        return mapToResponseDto(recruiter);
    }


    // ------------------------------------------------------------
    // DELETE RECRUITER
    // ------------------------------------------------------------
    @Override
    @CacheEvict(value = {"recruiters-viewAllDetails", "recruiters-viewById"}, allEntries = true)
    public String removeById(Long id) {

        if (!recruiterRepo.existsById(id)) {
            throw new RecruiterIdNotFoundException("Invalid ID: " + id);
        }

        recruiterRepo.deleteById(id);
        return "Recruiter removed with ID: " + id;
    }


    // ------------------------------------------------------------
    // UPDATE RECRUITER
    // ------------------------------------------------------------
    @Override
    @CachePut(value = "recruiters-viewById", key = "#dto.rid")
    @CacheEvict(value = "recruiters-viewAllDetails", allEntries = true)
    public String updateDetails(RecruiterUpdateDTO dto) {

        Recruiter existing = recruiterRepo.findById(dto.getRid())
                .orElseThrow(() -> new RecruiterIdNotFoundException("Invalid ID: " + dto.getRid()));

        // Update fields
        existing.setName(dto.getName());
        existing.setDepartment(dto.getDepartment());
//        existing.setCompanyName(dto.getCompanyName());
        existing.setDesignation(dto.getDesignation());
        existing.setLocation(dto.getLocation());

        // If phone and companyType included in DTO, add:
        // existing.setPhone(dto.getPhone());
        // existing.setCompanyType(dto.getCompanyType());

        recruiterRepo.save(existing);

        return "Recruiter updated successfully with ID: " + dto.getRid();
    }


    // ------------------------------------------------------------
    // MAPPING Recruiter -> RecruiterResponseDTO
    // ------------------------------------------------------------
    private RecruiterResponseDTO mapToResponseDto(Recruiter recruiter) {

        RecruiterResponseDTO dto = new RecruiterResponseDTO();

        dto.setRid(recruiter.getRid());
        dto.setName(recruiter.getName());
        dto.setEmail(recruiter.getUserAccount() != null ? recruiter.getUserAccount().getEmail() : "N/A");
//        dto.setCompanyName(recruiter.getCompanyName());
        dto.setDepartment(recruiter.getDepartment());
        dto.setDesignation(recruiter.getDesignation()!=null?recruiter.getDesignation():null);
        dto.setLocation(recruiter.getLocation()!=null?recruiter.getLocation():null);
        dto.setPhone(recruiter.getPhone()!=null?recruiter.getPhone():null);
        dto.setCompanyType(recruiter.getCompanyType());
        dto.setOrganization(recruiter.getOrganization()!=null?recruiter.getOrganization().getName():null);
        
        List<JobDTO> jobDtos =
                (recruiter.getJobs() != null)
                        ? recruiter.getJobs().stream().map(job -> {

            JobDTO jobDto = new JobDTO();
            jobDto.setId(job.getId());
            jobDto.setTitle(job.getTitle());
            jobDto.setDescription(job.getDescription());
            jobDto.setLocation(job.getLocation());
            jobDto.setEmployementType(job.getEmployementType());
            jobDto.setSalaryRange(job.getSalaryRange());
            jobDto.setPostedDate(job.getPostedDate());
            jobDto.setCompanyName(job.getCompanyName());
            jobDto.setStatus(job.getStatus() != null ? job.getStatus() : Status.ACTIVE);
            jobDto.setRecruiterId(job.getRecruiter() != null ? job.getRecruiter().getRid() : null);

            return jobDto;
            
        }).collect(Collectors.toList()) : List.of();

        dto.setJobs(jobDtos);
        dto.setJobCount(jobDtos.size());

        return dto;
    }
}
