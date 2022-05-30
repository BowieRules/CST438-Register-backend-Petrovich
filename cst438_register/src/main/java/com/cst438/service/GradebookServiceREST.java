package com.cst438.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.cst438.domain.EnrollmentDTO;


public class GradebookServiceREST extends GradebookService {
	
	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	String gradebook_url;
	
	public GradebookServiceREST() {
		System.out.println("REST grade book service");
	}

	@Override
	/*
	 * enroll student using HTTP post 
	 */
	public void enrollStudent(String student_email, String student_name, int course_id) {
		
		//TODO  complete this method in homework 4 -- Completed and tested
		System.out.println("Register:enrollStudent -- Sending REST EnrollmentDTO for student: "+student_name);
		EnrollmentDTO dtoEnroll = new EnrollmentDTO(student_email, student_name, course_id);
		ResponseEntity<EnrollmentDTO> response = restTemplate.postForEntity(gradebook_url+"/enrollment", // URL
				dtoEnroll,										// data to send
				EnrollmentDTO.class);							// return data type		
		HttpStatus rc = response.getStatusCode();
		System.out.println("HttpStatus: "+rc);
		EnrollmentDTO returnObject = response.getBody();
		System.out.println(returnObject);
	}

}
