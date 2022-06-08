package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.cst438.domain.AdminRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = { "http://localhost:3000" })
public class StudentController {

	@Autowired
	StudentRepository studentRepo;
	AdminRepository adminRepo;
	
	@GetMapping("/student")
	public StudentDTO getStudent( @RequestParam("email") String email ) {
		System.out.println("/student called.");
		Student student = studentRepo.findByEmail(email);
		if(student == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + email);
		else
			return createStudentDTO(student);
	}
	
	/* add student (email and name) if not yet exists */
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO, @AuthenticationPrincipal OAuth2User principal) {
		// make sure user has privileges to add a student
		// only admins allowed here
		if (adminRepo.findByEmail(principal.getAttribute("email")) != null) {
			// check if email already exists
			Student student = studentRepo.findByEmail(studentDTO.email);	
			if (student == null) {
				// create new instance of a student
				student = new Student();
				student.setEmail(studentDTO.email);
				student.setName(studentDTO.name);
				Student result = studentRepo.save(student);
				return createStudentDTO(result);
			}
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email already exists: " + studentDTO.email);
		}
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to add students: " + principal.getAttribute("email"));
				
	}
	
	/* put student registration on HOLD */
	@PutMapping("/student/hold/set/{id}")
	@Transactional
	public void setHoldForStudent( @PathVariable(value="id") int id, @AuthenticationPrincipal OAuth2User principal) {
		// make sure logged in user has privileges to change status
		// only admins allowed here
		if (adminRepo.findByEmail(principal.getAttribute("email")) != null) {			
			Student student = studentRepo.findById(id);		
			if(student == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student id does not exist: " + id);
			}
			else {		
				student.setStatus("HOLD");
				student.setStatusCode(1);
				studentRepo.save(student);
			}
		}
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to add students: " + principal.getAttribute("email"));
	}
	
	/* I can release the HOLD on student registration */
	@PutMapping("/student/hold/release/{id}")
	public void releaseHoldForStudent( @PathVariable(value="id") int id, @AuthenticationPrincipal OAuth2User principal) {
		// make sure logged in user has privileges to change status
		// only admins allowed here
		if (adminRepo.findByEmail(principal.getAttribute("email")) != null) {		
	
			Student student = studentRepo.findById(id);		
			if(student == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + id);
			}
			else {		
				student.setStatus(null);
				student.setStatusCode(0);
				studentRepo.save(student);
			}
		}
		else
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to add students: " + principal.getAttribute("email"));
	}
	
	private StudentDTO createStudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO(student.getName(), student.getEmail());
		studentDTO.student_id = student.getStudent_id();
		return studentDTO;
	}
}