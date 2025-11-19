package com.ssp.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssp.DTO.ApplicationDTO;
import com.ssp.Entity.Applicant;
import com.ssp.Entity.Application;
import com.ssp.Entity.Job;
import com.ssp.Exception.ApplicaionNotFoundException;
import com.ssp.Exception.JobIdNotFoundException;
import com.ssp.Repositry.ApplicantRepositry;
import com.ssp.Repositry.ApplicationRepositry;
import com.ssp.Repositry.IJobRepositry;
//import com.ssp.Repositry.InterviewScheduleRepositry;

@Service
public class ApplicationServiceImpl implements IApplicationService {

	@Autowired
	private ApplicationRepositry apprepo;

	@Autowired
	private IJobRepositry jobrepo;

	@Autowired
	private ApplicantRepositry applrepo;

	@Autowired
	private IEmailService emailService;
//	@Autowired
//	private InterviewScheduleRepositry interrepo;

	// Create
	@CacheEvict(value = "Application-ViewAllApplication", allEntries = true)
	@Override
	public String createApplication(ApplicationDTO dto) {

		Job job = jobrepo.findById(dto.getJobID()).orElseThrow(() -> new JobIdNotFoundException("Job Not Found"));

		Applicant applicant = applrepo.findById(dto.getApplicantID())
				.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

//		InterviewSchedule interviewSchedule = interrepo.findById(dto.getInterviewId())
//				.orElseThrow(() -> new InterviewIdNotFoundException("Interview Not Found"));

		Application application = new Application();

		application.setId(dto.getId());
		application.setAppliedDate(dto.getAppliedDate());
		application.setStatus(dto.getStatus());

		application.setJob(job);

		application.setApplicant(applicant);

//		application.setSchedule(interviewSchedule);

		Long id = apprepo.save(application).getId();
		
		emailService.sendApplicationThankYou(applicant.getUserAccount().getEmail(), applicant.getName(),
				application.getJob().getTitle());


		return "Application Created Successfully With Id: " + id;
	}

	// get All

	@Cacheable(value = "Application-ViewAllApplication") // Cache the full list
	@Override
	public List<ApplicationDTO> getAllApplication() {

		return apprepo.findAll().stream().map(this::convertTODTO).collect(Collectors.toList());
	}

	// Get By Id
	@Cacheable(value = "Application-ViewByID", key = "#id")
	@Override
	public ApplicationDTO getApplicationById(Long id) {

		Application application = apprepo.findById(id)
				.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

		return convertTODTO(application);
	}

	// Evict both single Application cache and list cache
	@CacheEvict(value = { "Application-ViewAllApplication", "Application-ViewByID" }, key = "#id", allEntries = true)
	@Override
	public String deleteApplicationById(Long id) {

		apprepo.findById(id).orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

		apprepo.deleteById(id);

		return "Application Successfully Deleted With This Id: " + id;
	}

	// update The Field
	@CachePut(value = "Application-ViewByID", key = "#dto.id") // update single recruiter cache
	@CacheEvict(value = "Application-ViewAllApplication", allEntries = true) // evict list cache
	@Override
	public String updateApplication(ApplicationDTO dto) {

		// Find existing application
		Application application = apprepo.findById(dto.getId())
				.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

		// Update Scalar Fields
		application.setAppliedDate(dto.getAppliedDate());
		application.setStatus(dto.getStatus());

		// Update Job (If Changed)

		if (dto.getJobID() != null) {
			Job job = jobrepo.findById(dto.getJobID()).orElseThrow(() -> new JobIdNotFoundException("Job Not Found"));
			application.setJob(job);

		}

		// Update applicant (If Changed)

		if (dto.getApplicantID() != null) {
			Applicant applicant = applrepo.findById(dto.getApplicantID())
					.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

			application.setApplicant(applicant);
		}

		// Update Interview (if Changed)
//		if (dto.getInterviewId()!=null) {
//			InterviewSchedule interview = interrepo.findById(dto.getInterviewId()).orElseThrow(()->new InterviewIdNotFoundException("InterView Not Found"));
//			application.setSchedule(interview);
//		}

		// saved The Update Application
		apprepo.save(application);
		// if Rejected,notify applicant
		// one Way
//		 String s =dto.getStatus().REJECTED.name();
//		 if ("REJECTED".equalsIgnoreCase(s)) {
//			
//		}

		// Second Way
		// if ("REJECTED".equalsIgnoreCase(dto.getStatus().REJECTED.name())) {
		if ("REJECTED".equalsIgnoreCase(dto.getStatus().getClass().getSimpleName())) {
			emailService.sendRejection(application.getApplicant().getUserAccount().getEmail(), application.getApplicant().getName(),
					application.getJob().getTitle());
		}

		return "Application With Id " + dto.getId() + " update Sucessfully";
	}

	// Utility: Entity → DTO
	// Helper Method
	private ApplicationDTO convertTODTO(Application application) {

		ApplicationDTO dto = new ApplicationDTO();
		dto.setId(application.getId());
		dto.setAppliedDate(application.getAppliedDate());
		dto.setStatus(application.getStatus());

		if (application.getJob() != null) {
			dto.setJobID(application.getJob().getId());
			dto.setJobTitle(application.getJob().getTitle());
		}

		if (application.getApplicant() != null) {
			dto.setApplicantID(application.getApplicant().getId());
			dto.setApplicantName(application.getApplicant().getName());
		}
//		if (application.getSchedule() != null) {
//			dto.setInterviewId(application.getSchedule().getId());
//			dto.setInterviewDate(application.getSchedule().getInterviewDateTime());
//		}

		return dto;
	}

}
