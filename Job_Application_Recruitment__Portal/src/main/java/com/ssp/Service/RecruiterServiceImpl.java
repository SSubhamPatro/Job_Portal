package com.ssp.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssp.DTO.JobDTO;
import com.ssp.DTO.RecruiterDTO;
import com.ssp.DTO.RecruiterResponseDTO;
import com.ssp.DTO.RecruiterUpdateDTO;
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
	private IRecruiterRepositry recRepositry;

	@Autowired
	private IUserAccountRepositry userRepo;
	
	@Autowired
	private PasswordEncoder encoder;

    
	
//	Create
	@CacheEvict(value = "recruiters-viewAllDetails" ,allEntries = true)
	@Override
	public String registerRecruiterDetails(RecruiterDTO recruiterdtDto) {

		if (userRepo.existsByEmail(recruiterdtDto.getEmail())) {
			throw new DuplicateEmailException("Email Already registered!");
		}
		
		
		UserAccount account = new UserAccount();
		account.setEmail(recruiterdtDto.getEmail());
		account.setPassword(encoder.encode(recruiterdtDto.getPassword()));
		account.setRole(Role.RECRUITER);
		userRepo.save(account);
		
		Recruiter recruiter = new Recruiter();
		recruiter.setRid(recruiterdtDto.getRdid());
		recruiter.setName(recruiterdtDto.getName());
		recruiter.setCompanyName(recruiterdtDto.getCompanyName());
		recruiter.setDepartment(recruiterdtDto.getDepartment());
		recruiter.setUserAccount(account);
		Long rid = recRepositry.save(recruiter).getRid();
		return "Recruiter Save With This Id: " + rid;
	}

	// Cache the full list
	@Cacheable(value = "recruiters-viewAllDetails")
	@Override
	public List<RecruiterResponseDTO> viewAllRecruiterDetails() {

		System.out.println("RecruiterServiceImpl.viewAllRecruiterDetails()");
		System.err.println("Hitting The Db");
		 List<Recruiter> recruiters = recRepositry.findAll();

		 return recruiters.stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}

	// Cache single recruiter by ID
	@Cacheable(value = "recruiters-viewById" ,key = "#id")
	@Override
	public String viewByIdRecruiterDetails(Long id) {

		Optional<Recruiter> byId = recRepositry.findById(id);
		System.err.println("Hitting db again And Again");
		if (byId.isPresent()) {

			return "Id Is Present In Db: " + id;
		} else {
			throw new RecruiterIdNotFoundException("Invalid Id " + id);
		}

	}

	// Evict both single recruiter cache and list cache
	@CacheEvict(value = {"recruiters-viewAllDetails","recruiters-viewById"},key = "#id",allEntries = true)
	@Override
	public String removeById(Long id) {
          
		if (recRepositry.existsById(id)) {
			recRepositry.deleteById(id);
			return "Id Details Is Removed From DB: "+id;
		}
		
		throw new RecruiterIdNotFoundException("Invalid Id: "+id);
	}
	
	//update
	@CachePut(value = "recruiters-viewById",key = "#recruiter.rid")// update single recruiter cache
	@CacheEvict(value = "recruiters-viewAllDetails",allEntries = true)// evict list cache
	@Override
	public String updateDetails(RecruiterUpdateDTO recruiter) {

		if(!recRepositry.existsById(recruiter.getRid())) {
			throw new RecruiterIdNotFoundException("Invalid Id: "+recruiter.getRid());
		}
		Recruiter recruiterUpdate = new Recruiter();
		recruiterUpdate.setRid(recruiter.getRid());
		recruiterUpdate.setName(recruiter.getName());
		recruiterUpdate.setDepartment(recruiter.getDepartment());
		recruiterUpdate.setCompanyName(recruiter.getCompanyName());
		
		
		return recRepositry.save(recruiterUpdate).getRid()+"Id Is Successfully Updated";
	}
	
	private RecruiterResponseDTO mapToResponseDto(Recruiter recruiter) {
		RecruiterResponseDTO dto = new RecruiterResponseDTO();
		
		dto.setRid(recruiter.getRid());
		dto.setName(recruiter.getName());
		System.out.println("Recruiter: " + recruiter.getName());
		System.out.println("UserAccount: " + recruiter.getUserAccount());
		if(recruiter.getUserAccount() != null) {
		    System.out.println("Email: " + recruiter.getUserAccount().getEmail());
		}

		dto.setEmail(recruiter.getUserAccount()!=null?recruiter.getUserAccount().getEmail():"N/A");
		dto.setCompanyName(recruiter.getCompanyName());
		dto.setDepartment(recruiter.getDepartment());
		
		//Map Jobs To JobDto
		
		List<JobDTO> jobs = recruiter.getJobs().stream().map(job -> {
            JobDTO jobDto = new JobDTO();
            jobDto.setId(job.getId());
            jobDto.setTitle(job.getTitle());
            jobDto.setDescription(job.getDescription());
            jobDto.setLocation(job.getLocation());
            jobDto.setEmployementType(job.getEmployementType());
            jobDto.setSalaryRange(job.getSalaryRange());
            jobDto.setCompanyName(job.getCompanyName());
            jobDto.setPostedDate(job.getPostedDate());
            jobDto.setStatus(job.getStatus()!=null?job.getStatus():Status.ACTIVE);
            if(job.getRecruiter()!=null) {
            	jobDto.setRecruiterId(job.getRecruiter().getRid());
            }
            return jobDto;
        }).collect(Collectors.toList());

        dto.setJobs(jobs);
        dto.setJobCount(jobs.size()); // set job count
		
		return dto;
		
	}
	
	
}
