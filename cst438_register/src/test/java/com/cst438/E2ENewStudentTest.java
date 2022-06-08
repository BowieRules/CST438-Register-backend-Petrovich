package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class E2ENewStudentTest {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/temp/chromedriver_win32/chromedriver.exe";
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_NAME = "John Doe";
	public static final String TEST_USER_EMAIL = "jdoe@csumb.edu";
	public static final int SLEEP_DURATION = 1000; // in milliseconds
	
	@Autowired
	StudentRepository studentRepository;
	
	/*
	 * Add new student with TEST_USER_EMAIL and TEST_USER_NAME
	 */
	
	@Test
	public void newStudentTest() throws Exception {
		
		Student student = null;
		/*
		 * if student already exists, delete him
		 */
			
		do {
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}
		} while (student != null);
		
		// set the driver location and start driver
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Puts an Implicit wait for 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
		
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// Click Add Student button on home screen
			driver.findElement(By.xpath("//button[@id='btnAddStudent1']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// populate student name and email, submit using btnAddStudent2
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='btnAddStudent2']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// Check to see that added student is present in local database.
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(student, "Student failed to add");
			assertTrue(student.getName().equals(TEST_USER_NAME), "Student was added with wrong name.");
			Thread.sleep(SLEEP_DURATION);
			
			//Try to insert this student again, this should not be allowed
			// Click Add Student button on home screen
			driver.findElement(By.xpath("//button[@id='btnAddStudent1']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			// populate student name and email, submit using btnAddStudent2
			driver.findElement(By.xpath("//input[@name='name']")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys(TEST_USER_EMAIL);
			driver.findElement(By.xpath("//button[@id='btnAddStudent2']")).click();
			Thread.sleep(SLEEP_DURATION);
			
			
		} catch (Exception ex) {
			throw ex;
		} finally {
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}
			driver.quit();
		}
	} 
}