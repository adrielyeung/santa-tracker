package com.adriel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adriel.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
	List<Person> findByUsername(String username);
	List<Person> findByEmail(String email);
	List<Person> findByResetPasswordToken(String resetPasswordToken);
	List<Person> findByAdminToken(String adminToken);
	List<Person> findByAdmin(int admin);
}
