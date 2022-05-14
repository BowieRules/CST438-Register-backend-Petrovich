package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/student")
	public StudentDTO getStudent( @RequestParam("email") String email ) {
		System.out.println("/student called.");
		Student student = studentRepository.findByEmail(email);
		if(student == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + email);
		else
			return createStudentDTO(student);
	}
	
	/* add student (email and name) if not yet exists */
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO studentDTO) {
		// check if email already exists
		Student student = studentRepository.findByEmail(studentDTO.email);		
		if (student == null) {
			// create new instance of a student
			student = new Student();
			student.setEmail(studentDTO.email);
			student.setName(studentDTO.name);
			Student result = studentRepository.save(student);
			return createStudentDTO(result);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email already exists: " + studentDTO.email);
		}
	}
	
	/* put student registration on HOLD */
	@PutMapping("/student/hold/set/{id}")
	@Transactional
	public void setHoldForStudent( @PathVariable(value="id") int id) {
		Student student = studentRepository.findById(id);		
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student id does not exist: " + id);
		}
		else {		
			student.setStatus("HOLD");
			student.setStatusCode(1);
			studentRepository.save(student);
		}
	}
	
	/* I can release the HOLD on student registration */
	@PutMapping("/student/hold/release/{id}")
	public void releaseHoldForStudent( @PathVariable(value="id") int id) {
		Student student = studentRepository.findById(id);		
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist: " + id);
		}
		else {		
			student.setStatus(null);
			student.setStatusCode(0);
			studentRepository.save(student);
		}
	}
	
	private StudentDTO createStudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO(student.getName(), student.getEmail());
		studentDTO.student_id = student.getStudent_id();
		return studentDTO;
	}
}