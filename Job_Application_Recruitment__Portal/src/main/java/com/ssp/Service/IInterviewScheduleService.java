package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.InterviewScheduleRequestDTO;
import com.ssp.DTO.InterviewScheduleResponseDto;

public interface IInterviewScheduleService {

	InterviewScheduleResponseDto createInterview(InterviewScheduleRequestDTO dto);

	InterviewScheduleResponseDto getInterviewById(Long id);
       
	InterviewScheduleResponseDto updateInterviewDetails(Long id,InterviewScheduleRequestDTO dto);
	
	List<InterviewScheduleResponseDto> getAllInterviews();

	String deleteInterviewById(Long id);
     
 }
