package com.ssp.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssp.Entity.Job;
//JobService → add/update/search jobs (with caching)
public interface IJobRepositry extends JpaRepository<Job, Long> {

	//GET /jobs/search → searchJobs(title, location)
	//serach by title and location
	List<Job>findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(String title,String location);
	@Query("""
		    SELECT DISTINCT j FROM Job j
		    LEFT JOIN j.applications a
		    LEFT JOIN a.applicant ap
		    WHERE 
		      (
		        (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
		        OR (:keyword IS NULL OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))
		        OR (:keyword IS NULL OR LOWER(ap.skills) LIKE LOWER(CONCAT('%', :keyword, '%')))
		      )
		      AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
		      AND (:experience IS NULL OR ap.experienceYears <= :experience)
		""")
		List<Job> searchFilterJobs(
		    @Param("keyword") String keyword,
		    @Param("location") String location,
		    @Param("experience") Integer experience
		);

}
