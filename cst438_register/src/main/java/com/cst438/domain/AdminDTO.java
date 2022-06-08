package com.cst438.domain;

public class AdminDTO {
	public int admin_id;
	public String email;
	
	public AdminDTO() {
		this.admin_id = 0;
		this.email = null;
	}
	
	public AdminDTO(int id, String email) {
		this.admin_id = id;
		this.email = email;
	}

	@Override
	public String toString() {
		return "AdminDTO [admin_id=" + admin_id + ", email=" + email + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		AdminDTO other = (AdminDTO) obj;
		if (admin_id != other.admin_id)
			return false;
		
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		
		return true;
	}
}