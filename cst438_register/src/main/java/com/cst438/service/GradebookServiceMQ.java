package com.cst438.service;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class GradebookServiceMQ extends GradebookService {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Autowired
	Queue gradebookQueue;
	
	
	public GradebookServiceMQ() {
		System.out.println("MQ grade book service");
	}
	
	// send message to grade book service about new student enrollment in course
	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
		 
		//TODO  complete this method in homework 4 - completed and tested
		System.out.println("Register:enrollStudent -- Sending RabbitMQ EnrollmentDTO for student: " + student_name);
		EnrollmentDTO dtoEnroll = new EnrollmentDTO(student_email, student_name, course_id);
		rabbitTemplate.convertAndSend(gradebookQueue.getName(), dtoEnroll);	
		System.out.println("Register:enrollStudent -- Sent RabbitMQ EnrollmentDTO for student: " + student_name);
	}
	
	@RabbitListener(queues = "registration-queue")
	@Transactional
	public void receive(CourseDTOG courseDTOG) {
		
		//TODO  complete this method in homework 4 -- Completed and tested
		int course_id = courseDTOG.course_id;
		System.out.println("Register: CourseController -- updateCourseGrade received from gradebook for course " + course_id);		
		for(CourseDTOG.GradeDTO grd : courseDTOG.grades) {
		      Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(grd.student_email, course_id);
		      enrollment.setCourseGrade(grd.grade);
		      enrollmentRepository.save(enrollment);
		      System.out.println("Register: CourseController -- final grade updated for " + grd.student_email + " [" + course_id + "] " + grd.grade);
		   }		
	}
}