package com.adriel.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;
import com.adriel.utils.ConstStrings;
import com.adriel.utils.EmailSender;

@Controller
public class VerifyRegisterController {
	
	@Autowired
	PersonService personService;
	@Autowired
	EmailSender emailSender;
	
	@RequestMapping(value="/verify-register", method=RequestMethod.POST)
	public void verifyRegister(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		
		String usernameErrMsg = verifyUsername(person, req);
		
		if ("".equals(usernameErrMsg)) {
			String emailErrMsg = verifyEmail(person, req);
			
			if ("".equals(emailErrMsg)) {
				String passwordErrMsg = Utils.verifyPassword(person, req);
			
				if ("".equals(passwordErrMsg)) {
					// Send approval request email
					if ((int) req.getSession().getAttribute("admin") == 1) {
						try {
							List<Person> adminList = personService.getAllAdminPersons();
							List<String> adminEmailList = adminList.stream().map(Person::getEmail).collect(Collectors.toCollection(ArrayList::new));
							Person personWithToken = personService.generateAdminApproveToken(person);
							String tokenUrl = Utils.getSiteURL(req) + ConstStrings.ADMIN_TOKEN + personWithToken.getAdminToken();
							
							emailSender.sendEmail(new ArrayList<String>(), new ArrayList<String>(), adminEmailList, 
									ConstStrings.EMAIL_ADMIN_TITLE, String.format(ConstStrings.EMAIL_ADMIN_BODY, personWithToken.getUsername(), personWithToken.getEmail(), tokenUrl));
						} catch (ResourceNotFoundException e) {
							Redirections.redirect(req, resp, ConstStrings.REGISTER, String.format(ConstStrings.PERSON_NOT_FOUND_EMAIL, person.getEmail()));
						} catch (MessagingException | UnsupportedEncodingException e) {
							Redirections.redirect(req, resp, ConstStrings.RESET, ConstStrings.EMAIL_SEND_ERROR);
						}
						Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.PENDING_ADMIN_APPROVAL);
					} else {
						personService.createPerson(person);
						Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.SUCCESS_REGISTER);
					}
				} else {
					Redirections.redirect(req, resp, ConstStrings.REGISTER, passwordErrMsg);
				}
			} else {
				Redirections.redirect(req, resp, ConstStrings.REGISTER, emailErrMsg);
			}
		} else {
			Redirections.redirect(req, resp, ConstStrings.REGISTER, usernameErrMsg);
		}
	}
	
	@RequestMapping(value="/verify-admin", method=RequestMethod.GET)
	public void verifyAdmin(@Param(value = "token") String token, HttpServletRequest req, HttpServletResponse resp) {
		try {
			Person person = personService.getPersonByAdminToken(token);
			person.setAdmin(1);
			person.setAdminToken(null);
			personService.updatePerson(person.getPersonID(), person);
			// Notify admin requester
			emailSender.sendEmail(new ArrayList<>(Arrays.asList(person.getEmail())), new ArrayList<>(), new ArrayList<>(), 
					ConstStrings.EMAIL_ADMIN_SUCCESS_REQUESTER_TITLE, String.format(ConstStrings.EMAIL_ADMIN_SUCCESS_REQUESTER_BODY, person.getUsername(), Utils.getSiteURL(req) + ConstStrings.INDEX));
			// Notify all other admins of new admin
			List<Person> adminList = personService.getAllAdminPersons();
			List<String> adminEmailList = adminList.stream().map(Person::getEmail).collect(Collectors.toCollection(ArrayList::new));
			emailSender.sendEmail(new ArrayList<String>(), new ArrayList<String>(), adminEmailList, 
					ConstStrings.EMAIL_ADMIN_SUCCESS_ALERT_ALL_TITLE, String.format(ConstStrings.EMAIL_ADMIN_SUCCESS_ALERT_ALL_BODY, person.getUsername(), person.getEmail()));
			Redirections.redirect(req, resp, ConstStrings.INDEX, String.format(ConstStrings.SUCCESS_ADMIN, person.getUsername()));
		} catch (ResourceNotFoundException e) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.PERSON_NOT_FOUND_TOKEN_ADMIN);
		} catch (UnsupportedEncodingException | MessagingException e) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.EMAIL_SEND_ERROR);;
		}
	}
	
	private String verifyUsername(Person person, HttpServletRequest req) {
		
		String errMsg = "";
		try {
			personService.getPersonByUsername(person.getUsername());
			errMsg = ConstStrings.USERNAME_TAKEN;
			req.getSession().setAttribute("uname_reg", "");
			req.getSession().setAttribute("email_reg", person.getEmail());
			req.getSession().setAttribute("addr_reg", person.getAddress());
		} catch (ResourceNotFoundException e) {}
		return errMsg;
	}
	
	private String verifyEmail(Person person, HttpServletRequest req) {
		
		String errMsg = "";
		try {
			personService.getPersonByEmail(person.getEmail());
			errMsg = ConstStrings.EMAIL_TAKEN;
			req.getSession().setAttribute("uname_reg", person.getUsername());
			req.getSession().setAttribute("addr_reg", person.getAddress());
			req.getSession().setAttribute("email_reg", "");
		} catch (ResourceNotFoundException e) {}
		return errMsg;
	}
}
