package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.context.ContextConfiguration;

/* 
 * Junit and Mockito to use mock object to test student API. This
 * was modeled after provided example, JunitTestSchedule.
 */

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JUnitTestStudent {

	public static final String TEST_STUDENT_EMAIL = "test1@csumb.edu";
	public static final String TEST_STUDENT_NAME = "John Doe";
	public static final int TEST_STUDENT_ID = 1;
	public static final int HOLD_STATUS_CODE = 1;
	public static final int ACTIVE_STATUS_CODE = 0;

	@MockBean
	StudentRepository studentRepository;

	@Autowired
	private MockMvc mvc;
	
	//Test add new student
	@Test
	public void addStudentCheck() throws Exception {
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = TEST_STUDENT_NAME;
		
		//post request using test email and name
		MockHttpServletResponse response = mvc.perform(
				MockMvcRequestBuilders
				.post("/student")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn()
				.getResponse();

		//verify success
		assertEquals(200, response.getStatus());
		
		boolean found = false;
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		if((result.email.equals(TEST_STUDENT_EMAIL)) &&
				(result.name.equals(TEST_STUDENT_NAME))) {
			found = true;
		}
		
		assertEquals(true, found);
		verify(studentRepository).save(any(Student.class));
		verify(studentRepository, times(1)).findByEmail(TEST_STUDENT_EMAIL);
	}
	
	
	//Test placing hold
	@Test
	public void addHold()  throws Exception {
			
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(0);
		student.setStudent_id(TEST_STUDENT_ID);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
				.get("/student?email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		assertEquals(200, response.getStatus());
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);		
		assertEquals(TEST_STUDENT_ID, result.student_id);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);
		//post request with student id
		response = mvc.perform(MockMvcRequestBuilders
				.put("/student/hold/set/" + TEST_STUDENT_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		verify(studentRepository, times(1)).findById(TEST_STUDENT_ID);
	}
	
	//Test releasing hold
	@Test
	public void releaseHold()  throws Exception {
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(1);
		student.setStudent_id(TEST_STUDENT_ID);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
				.get("/student?email=" + TEST_STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		
		assertEquals(200, response.getStatus());
		assertEquals(TEST_STUDENT_ID, result.student_id);
		
		given(studentRepository.findById(TEST_STUDENT_ID)).willReturn(student);
		//post request with student id
		response = mvc.perform(MockMvcRequestBuilders
				.put("/student/hold/release/" + TEST_STUDENT_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		verify(studentRepository, times(1)).findById(TEST_STUDENT_ID);
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}