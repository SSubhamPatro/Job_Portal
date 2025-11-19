package com.ssp.Service;

import java.util.List;

import com.ssp.DTO.JobDTO;

//add/update/search jobs (with caching)
/*
GET /jobs → getAllJobs()

GET /jobs/{id} → getJobById()

PUT /jobs/{id} → updateJob()

DELETE /jobs/{id} → deleteJob()

GET /jobs/search → searchJobs(title, location)
 */
public interface IJobServiceManagement {

	String registerJobDetails(JobDTO dto);

	String searchDetailsById(Long id);

	List<JobDTO> searchAllDetails();

	String removeById(Long id);

	List<JobDTO> searchJobs(String title, String location);
	
	String updateJobs(Long id,JobDTO dto);
	
	List<JobDTO>searchFilterAllJobs(String keyword,String location,Integer experience);

}
