package com.cst438.domain;

public class StudentDTO {
	public int student_id;
	public String name;
	public String email;
	public String status;
	public int status_code;

	/* default constructor */
	public StudentDTO() {
		this.name = "";
		this.email = "";
		this.student_id = 0;
		this.status = "";
		this.status_code = 0;
	}

	public StudentDTO(String sname, String semail) {
		this.name = sname;
		this.email = semail;
		this.student_id = 0;
		this.status = "";
		this.status_code = 0;
	}

	@Override
	public String toString() {
		return "StudentDTO [Name=" + name + ", Email=" + email + ", studentStatus="
				+ status + ", studentCode=" + status_code + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		StudentDTO other = (StudentDTO) obj;
		if (student_id != other.student_id || !(email.equals(other.email)) ||
				!(name.equals(other.name)) || !(status.equals(other.status)) ||
				status_code != other.status_code)
			return false;
		return true;
	}
}