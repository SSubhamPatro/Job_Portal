package com.ssp.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ssp.DTO.InterviewScheduleRequestDTO;
import com.ssp.DTO.InterviewScheduleResponseDto;
import com.ssp.Entity.Application;
import com.ssp.Entity.InterviewSchedule;
import com.ssp.Exception.ApplicaionNotFoundException;
import com.ssp.Exception.InterviewIdNotFoundException;
import com.ssp.Repositry.ApplicationRepositry;
import com.ssp.Repositry.InterviewScheduleRepositry;

@Service
public class InterviewScheduleServiceImpl implements IInterviewScheduleService {

	@Autowired
	private InterviewScheduleRepositry irepo;

	@Autowired
	private ApplicationRepositry arepo;
	
	@Autowired
	private IEmailService emailService;

	@CacheEvict(value = "Interview-ViewAllDetails" ,allEntries = true)
	@Override
	public InterviewScheduleResponseDto createInterview(InterviewScheduleRequestDTO dto) {

		Application application = arepo.findById(dto.getApplicationId())
				.orElseThrow(() -> new ApplicaionNotFoundException("Application Not Found"));

		InterviewSchedule entity = new InterviewSchedule();
		entity.setMeetingLink(dto.getMeetingLink());
		entity.setInterviewerName(dto.getInterviewerName());
		entity.setInterviewDateTime(dto.getInterviewDateTime());
		entity.setApplication(application);
		InterviewSchedule saveToDb = irepo.save(entity);
          
		emailService.sendInterviewSchedule(application.getApplicant().getUserAccount().getEmail(), application.getApplicant().getName(), entity.getInterviewDateTime(), entity.getMeetingLink());
		
		return mapToResponse(saveToDb);
	}

	@Cacheable(value = "Interview-ViewByID", key = "#id")
	@Override
	public InterviewScheduleResponseDto getInterviewById(Long id) {

		InterviewSchedule entity = irepo.findById(id)
				.orElseThrow(() -> new InterviewIdNotFoundException("Interviewer Id Not Found"));
		return mapToResponse(entity);
	}

	private InterviewScheduleResponseDto mapToResponse(InterviewSchedule entity) {
		InterviewScheduleResponseDto dto = new InterviewScheduleResponseDto();
		dto.setId(entity.getId());
		dto.setInterviewDateTime(entity.getInterviewDateTime());
		dto.setMeetingLink(entity.getMeetingLink());
		dto.setInterviewerName(entity.getInterviewerName());
		dto.setApplicationId(entity.getApplication().getId());
		dto.setJobTitle(entity.getApplication().getJob().getTitle());
		dto.setApplicantName(entity.getApplication().getApplicant().getName());
		return dto;
	}
 	@CacheEvict(value = {"Interview-ViewAllDetails","Interview-ViewByID"},key = "#id",allEntries = true)
	@Override
	public String deleteInterviewById(Long id) 
	{
		if (irepo.existsById(id)) {
		    irepo.deleteById(id);
		    return "Interview deleted successfully with id " + id;
		}
		throw new InterviewIdNotFoundException("Interview Id Not Found");

	}

	@Cacheable(value = "Interview-ViewAllDetails")
	@Override
	public List<InterviewScheduleResponseDto> getAllInterviews() {

		return irepo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@CachePut(value = "Interview-ViewByID",key = "#dto.id")// update single recruiter cache
	@CacheEvict(value = "Interview-ViewAllDetails",allEntries = true)// evict list cache
	@Override
	public InterviewScheduleResponseDto updateInterviewDetails(Long id, InterviewScheduleRequestDTO dto) {

		InterviewSchedule schedule = irepo.findById(dto.getApplicationId()).orElseThrow(()->new InterviewIdNotFoundException("Interview Id Not Found"));
		
		schedule.setInterviewDateTime(dto.getInterviewDateTime());
		schedule.setMeetingLink(dto.getMeetingLink());
		schedule.setInterviewerName(dto.getInterviewerName());
		// If updating application also
		
		if (dto.getApplicationId()!=null) {
			Application application = arepo.findById(id).orElseThrow(()->new ApplicaionNotFoundException("Application Id Not Found "));
			schedule.setApplication(application);
		}
		InterviewSchedule updated = irepo.save(schedule);
		
		return mapToResponse(updated);
	}
}
