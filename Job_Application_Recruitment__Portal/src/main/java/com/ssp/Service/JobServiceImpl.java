package com.ssp.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssp.DTO.JobDTO;
import com.ssp.Entity.Job;
import com.ssp.Entity.Recruiter;
import com.ssp.Entity.Status;
import com.ssp.Exception.JobIdNotFoundException;
import com.ssp.Exception.RecruiterIdNotFoundException;
import com.ssp.Repositry.IJobRepositry;
import com.ssp.Repositry.IRecruiterRepositry;

@Service
public class JobServiceImpl implements IJobServiceManagement {

	@Autowired
	private IJobRepositry jobrepo;

	@Autowired
	private IRecruiterRepositry recRepo;
	
	@Autowired
	private IEmailService emailService;

	@CacheEvict(value = "SearchAll-JobDetails", allEntries = true)
	@Override
	public String registerJobDetails(JobDTO dto) {

		// Fetch Recruiter
		Recruiter recruiter = recRepo.findById(dto.getRecruiterId()).orElseThrow(
				() -> new RecruiterIdNotFoundException("Recruiter not found with id: " + dto.getRecruiterId()));

		Job job = mapToEntity(dto, recruiter);
		Long id = jobrepo.save(job).getId();

		return "Job saved successfully with ID: " + id;
	}

	@Override
	@Cacheable(value = "Job-ViewByID", key = "#id")

	public String searchDetailsById(Long id) {
		System.out.println("JobServiceImpl.searchFindById()");
		Optional<Job> byId = jobrepo.findById(id);
		if (byId.isPresent()) {
			return "Id Is Present: " + id;
		} else {
			throw new JobIdNotFoundException("Job Not Found With Id: " + id);
		}
	}

	@Override
	@Cacheable(value = "SearchAll-JobDetails")
	public List<JobDTO> searchAllDetails() {

		List<JobDTO> allJobs = jobrepo.findAll().stream().map(this::mapToDto).toList();

		return allJobs;
	}

	@CacheEvict(value = { "SearchAll-JobDetails", "Job-ViewByID" }, key = "#id", allEntries = true)

	@Override
	public String removeById(Long id) {

		if (jobrepo.existsById(id)) {
			jobrepo.deleteById(id);
			return "Job Deleted With Id: " + id;
		} else {
			throw new JobIdNotFoundException("Job Id Is Not Found " + id);
		}

	}

	@Override
	public List<JobDTO> searchJobs(String title, String location) {
		System.out.println("DEBUG: searchJobs called with title='" + title + "', location='" + location + "'");
		List<Job> jobsSearch = jobrepo.findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(title, location);

		List<JobDTO> jobSearchDto = jobsSearch.stream().map(this::mapToDto).collect(Collectors.toList());
//       jobSearchDto.forEach(System.out::println);
		System.out.println("DEBUG: jobsSearch returned " + jobsSearch.size() + " records");
		return jobSearchDto;
	}
	
	@Override
	public List<JobDTO> searchFilterAllJobs(String keyword, String location, Integer experience) {

	    // Clean the parameters (so nulls skip filters)
	    String sanitizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword : null;
	    String sanitizedLocation = (location != null && !location.isBlank()) ? location : null;

	    // Fetch from repository
	    List<Job> jobs = jobrepo.searchFilterJobs(sanitizedKeyword, sanitizedLocation, experience);

	    // Convert to DTO list to prevent JSON recursion
	    return jobs.stream()
	            .map(this::mapToDto)
	            .collect(Collectors.toList());
	}


	@CachePut(value = "Job-ViewByID", key = "#dto.id") // update single recruiter cache
	@CacheEvict(value = "SearchAll-JobDetails", allEntries = true) // evict list cache
	@Override
	public String updateJobs(Long id, JobDTO dto) {

		Job existingJob = jobrepo.findById(id)
				.orElseThrow(() -> new JobIdNotFoundException("Job Not Found With Id: " + id));

		Recruiter recruiter = recRepo.findById(dto.getRecruiterId()).orElseThrow(
				() -> new RecruiterIdNotFoundException("Recruiter not found with id: " + dto.getRecruiterId()));

		// Update Fields
		dto.setId(existingJob.getId());// Keep Existing Id
		Job updatedJob = mapToEntity(dto, recruiter);

		jobrepo.save(updatedJob);

		return "Job Updated Successfully With Id: " + id;
	}
//------------------------------------------------------------------
	// Helper Method:Convert Entity To Dto

	private JobDTO mapToDto(Job job) {
		JobDTO dto = new JobDTO();
		dto.setId(job.getId());
		dto.setTitle(job.getTitle());
		dto.setDescription(job.getDescription());
		dto.setLocation(job.getLocation());
		dto.setEmployementType(job.getEmployementType());
		dto.setSalaryRange(job.getSalaryRange());
		dto.setCompanyName(job.getCompanyName());
		dto.setPostedDate(job.getPostedDate());
		dto.setStatus(job.getStatus() != null ? job.getStatus() : Status.ACTIVE);

		if (job.getRecruiter() != null) {

			dto.setRecruiterId(job.getRecruiter().getRid());
		}
		return dto;
	}

	// Helper Method:Convert Dto To Entity

	private Job mapToEntity(JobDTO dto, Recruiter recruiter) {

		Job job = new Job();
		job.setId(dto.getId()); // nullable, only used in update
		job.setTitle(dto.getTitle());
		job.setDescription(dto.getDescription());
		job.setLocation(dto.getLocation());
		job.setEmployementType(dto.getEmployementType());
		job.setSalaryRange(dto.getSalaryRange());
		job.setCompanyName(dto.getCompanyName());
		job.setPostedDate(dto.getPostedDate() != null ? dto.getPostedDate() : LocalDate.now());
		System.out.println("DEBUG => Status before save: " + job.getStatus());

//		job.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.ACTIVE);
        
		if (dto.getStatus() == null) {
	        job.setStatus(Status.ACTIVE); // or Status.APPLIED — choose your preferred default
	    } else {
	        job.setStatus(dto.getStatus());
	    }
		System.out.println("DEBUG => Status After save: " + job.getStatus());

		job.setRecruiter(recruiter);

		return job;
	}

}
