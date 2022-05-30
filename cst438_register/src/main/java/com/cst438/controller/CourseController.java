package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.CourseDTOG;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@RestController
public class CourseController {
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	/*
	 * endpoint used by gradebook service to transfer final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades( @RequestBody CourseDTOG courseDTO, @PathVariable("course_id") int course_id) {
		
		// Completed and tested for Homework 4
		System.out.println("Register: CourseController -- updateCourseGrade received from gradebook for course " + course_id);		
		for(CourseDTOG.GradeDTO grd : courseDTO.grades) {
		      Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(grd.student_email, course_id);
		      enrollment.setCourseGrade(grd.grade);
		      enrollmentRepository.save(enrollment);
		      System.out.println("Register: CourseController -- final grade updated for " + grd.student_email + " [" + course_id + "] " + grd.grade);
		   }	
	}
}