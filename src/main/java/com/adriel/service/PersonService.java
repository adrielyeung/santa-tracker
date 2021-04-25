package com.adriel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.repository.PersonRepository;
import com.adriel.utils.Utils;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	private static final int TOKEN_LENGTH = 45;

	public Person getPersonById(Integer personID) throws ResourceNotFoundException {
		return personRepository.findById(personID)
				.orElseThrow(() -> new ResourceNotFoundException("Person not found for this id."));
	}
	
	public Person createPerson(Person person) {
		return personRepository.save(person);
	}
	
	public Person updatePerson(Integer personID, Person personDetails) 
			throws ResourceNotFoundException {
		Person person = personRepository.findById(personID)
				.orElseThrow(() -> new ResourceNotFoundException("Person not found for this id."));
		
		person.setUsername(personDetails.getUsername());
		person.setPword(personDetails.getPword());
		person.setEmail(personDetails.getEmail());
		person.setAddress(personDetails.getAddress());
		person.setResetPasswordToken(personDetails.getResetPasswordToken());
		person.setAdmin(personDetails.getAdmin());
		person.setAdminToken(personDetails.getAdminToken());
		return personRepository.save(person);
	}
	
	public Map<String, Boolean> deletePerson(Integer personID)
			throws ResourceNotFoundException {
		Person person = personRepository.findById(personID)
				.orElseThrow(() -> new ResourceNotFoundException("Person not found for this id."));
		
		personRepository.delete(person);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	public Person getPersonByUsername(String username) 
			throws ResourceNotFoundException {
		List<Person> personList = personRepository.findByUsername(username);
		if (personList == null || personList.size() == 0) {
			throw new ResourceNotFoundException("Person not found for this username.");
		}
		return personList.get(0);
	}
	
	public Person getPersonByEmail(String email) 
			throws ResourceNotFoundException {
		List<Person> personList = personRepository.findByEmail(email);
		if (personList == null || personList.size() == 0) {
			throw new ResourceNotFoundException("Person not found for this email.");
		}
		return personList.get(0);
	}
	
	public Person getPersonByResetPasswordToken(String resetPasswordToken) 
			throws ResourceNotFoundException {
		List<Person> personList = personRepository.findByResetPasswordToken(resetPasswordToken);
		if (personList == null || personList.size() == 0) {
			throw new ResourceNotFoundException("Person not found for this reset password token.");
		}
		return personList.get(0);
	}
	
	public Person getPersonByAdminToken(String adminToken) 
			throws ResourceNotFoundException {
		List<Person> personList = personRepository.findByAdminToken(adminToken);
		if (personList == null || personList.size() == 0) {
			throw new ResourceNotFoundException("Person not found for this reset password token.");
		}
		return personList.get(0);
	}
	
	public List<Person> getAllAdminPersons() 
			throws ResourceNotFoundException {
		List<Person> personList = personRepository.findByAdmin(1);
		if (personList == null || personList.size() == 0) {
			throw new ResourceNotFoundException("No admin found.");
		}
		return personList;
	}
	
	public Person generateResetPasswordToken(Person person) {
		person.setResetPasswordToken(Utils.generateRandomAlphanumericString(TOKEN_LENGTH));
		return personRepository.save(person);
	}
	
	public Person generateAdminApproveToken(Person person) {
		person.setAdminToken(Utils.generateRandomAlphanumericString(TOKEN_LENGTH));
		return personRepository.save(person);
	}
}
