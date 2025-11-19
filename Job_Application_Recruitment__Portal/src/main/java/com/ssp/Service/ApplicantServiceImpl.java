package com.ssp.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssp.DTO.ApplicantDTO;
import com.ssp.DTO.ApplicantRegisterDTO;
import com.ssp.DTO.ApplicantResponse;
import com.ssp.Entity.Applicant;
import com.ssp.Entity.Role;
import com.ssp.Entity.UserAccount;
import com.ssp.Events.ApplicantCreatedEvent;
import com.ssp.Exception.ApplicantIdNotFoundException;
import com.ssp.Exception.DuplicateEmailException;
import com.ssp.Exception.ResumeUploadFailedException;
import com.ssp.Repositry.ApplicantRepositry;
import com.ssp.Repositry.IUserAccountRepositry;


@Service
public class ApplicantServiceImpl implements IApplicantServiceManagement {

	@Autowired
	private ApplicantRepositry arepo;

	@Autowired
	private IS3Service s3service;

	/*@Autowired
	private BCryptPasswordEncoder encoder;*/
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private IUserAccountRepositry userRepo;

	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	// Create
	@CacheEvict(value = "Applicant-viewAllDetails", allEntries = true)
	@Transactional
	@Override
	public ApplicantResponse cretateApplicantDetails(ApplicantRegisterDTO registerDTO, MultipartFile resumeFile) {

		String s3url;

		try {
//			1. Upload resume to S3 and get URL
			s3url = s3service.uploadFile(resumeFile);
		} catch (IOException e) {
			throw new ResumeUploadFailedException("Failed To Upload Resume To S3", e);
		}

		// 2. Check duplicate email
//		registerDTO.setResumeLink(s3url);
		if (userRepo.existsByEmail(registerDTO.getEmail())) {
			throw new DuplicateEmailException("Email already registered!");
		}

		// 3️ Create & save UserAccount
		UserAccount account = new UserAccount();
		account.setEmail(registerDTO.getEmail());
		account.setPassword(encoder.encode(registerDTO.getPassword()));
		account.setRole(Role.APPLICANT);
		userRepo.save(account);
		
		// 4️ Create & save Applicant linked to UserAccount
		Applicant applicant = new Applicant();
		applicant.setName(registerDTO.getName());
		applicant.setPhoneNumber(registerDTO.getPhoneNumber());
		applicant.setResumeLink(s3url);
		applicant.setSkills(registerDTO.getSkills());
		applicant.setExperienceYears(registerDTO.getExperienceYears());
		applicant.setRegisteredDate(registerDTO.getRegisteredDate());
        applicant.setUserAccount(account);    
		Applicant saved = arepo.save(applicant);

		// 5️ Publish async event for email sending
		
		
		eventPublisher.publishEvent(new ApplicantCreatedEvent(account.getEmail(), applicant.getName()));
		
		return new ApplicantResponse( "Applicant Saved with ID: " + saved.getId() + " And Resume Url: " + s3url,mapToDTO(saved));
	}

	// Cache The Full List
	@Cacheable(value = "Applicant-viewAllDetails")
	@Override
	public List<ApplicantDTO> getAllDetails() {

//		List<Applicant> all = arepo.findAll();
//		List<ApplicantDTO> dtoList = new ArrayList<ApplicantDTO>();
//
//		for (Applicant applicant : all) {
//			ApplicantDTO dto = new ApplicantDTO();
//			dto.setId(applicant.getId());
//			dto.setName(applicant.getName());
//			dto.setEmail(applicant.getEmail());
//			dto.setPassword(applicant.getPassword());
//			dto.setPhoneNumber(applicant.getPhoneNumber());
//			dto.setResumeLink(applicant.getResumeLink());
//			dto.setSkills(applicant.getSkills());
//			dto.setExperienceYears(applicant.getExperienceYears());
//			dto.setRegisteredDate(applicant.getRegisteredDate());
//			dtoList.add(dto);
//		}

		return arepo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	// Evict both single recruiter cache and list cache
	@CacheEvict(value = { "Applicant-viewAllDetails", "Applicant-viewById" }, allEntries = true)
	@Override
	public String deleteApplicantDetailsById(Long id) {

		if (arepo.existsById(id)) {
			arepo.deleteById(id);
			return "Applicant deleted successfully with ID: " + id;
		} else {
			throw new ApplicantIdNotFoundException("Applicant ID not available: " + id);
		}

	}

	@Cacheable(value = "Applicant-viewById", key = "#id")
	@Override
	public ApplicantDTO getDetailsById(Long id) {
		Applicant applicant = arepo.findById(id)
				.orElseThrow(() -> new ApplicantIdNotFoundException("Applicant Id Not Found"));

		
		return mapToDTO(applicant);
		
	}

	/*@Override
	public ApplicantDTO loginApplicant(String email, String password) {
	
		Applicant applicant = arepo.findByEmail(email)
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
	
		if (!encoder.matches(password, applicant.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}
	
		return mapToDTO(applicant);
	}*/

	// Map Applicant entity → ApplicantDTO (response)
	private ApplicantDTO mapToDTO(Applicant applicant) {
		ApplicantDTO dto = new ApplicantDTO();
		dto.setId(applicant.getId());
		dto.setName(applicant.getName());
		dto.setPhoneNumber(applicant.getPhoneNumber());
		dto.setResumeLink(applicant.getResumeLink());
		dto.setSkills(applicant.getSkills());
		dto.setExperienceYears(applicant.getExperienceYears());
		dto.setRegisteredDate(applicant.getRegisteredDate());
		
		if (applicant.getUserAccount()!=null) {
			dto.setEmail(applicant.getUserAccount().getEmail());
		}
		
		return dto;
	}

}
