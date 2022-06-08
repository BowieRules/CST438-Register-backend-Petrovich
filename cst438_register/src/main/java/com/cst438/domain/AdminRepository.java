package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository <Admin, Integer> {
	public Admin findByEmail(String email);
}