package com.adriel.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.ConstStrings;
import com.adriel.utils.EmailSender;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class VerifyResetController {
	
	@Autowired
	PersonService personService;
	
	@Autowired
	EmailSender emailSender;
	
	@RequestMapping(value="/verify-reset", method=RequestMethod.POST)
	public void verifyReset(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		
		try {
			Person personFromDb = personService.getPersonByEmail(person.getEmail());
			Person personWithToken = personService.generateResetPasswordToken(personFromDb);
			String tokenUrl = Utils.getSiteURL(req) + ConstStrings.RESET_PWD_TOKEN + personWithToken.getResetPasswordToken();
			
			emailSender.sendEmail(new ArrayList<String>(Arrays.asList(person.getEmail())), new ArrayList<String>(), new ArrayList<String>(), 
					ConstStrings.EMAIL_RESET_PWD_TITLE, String.format(ConstStrings.EMAIL_RESET_PWD_BODY, personWithToken.getUsername(), tokenUrl));
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.SUCCESS_RESET_EMAIL);
		} catch (ResourceNotFoundException e) {
			Redirections.redirect(req, resp, ConstStrings.RESET, String.format(ConstStrings.PERSON_NOT_FOUND_EMAIL, person.getEmail()));
		} catch (MessagingException | UnsupportedEncodingException e) {
			Redirections.redirect(req, resp, ConstStrings.RESET, ConstStrings.EMAIL_SEND_ERROR);
		}
	}
	
	@RequestMapping(value="/verify-reset-password", method=RequestMethod.POST)
	public void verifyResetPassword(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		String passwordErrMsg = Utils.verifyPassword(person, req);
		if ("".equals(passwordErrMsg)) {
			Person personReset = (Person) req.getSession().getAttribute("personReset");
			personReset.setPword(person.getPword());
			try {
				personService.updatePerson(personReset.getPersonID(), personReset);
				Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.SUCCESS_RESET);
			} catch (ResourceNotFoundException e) {
				Redirections.redirect(req, resp, ConstStrings.RESET, ConstStrings.PERSON_NOT_FOUND_TOKEN_RESET);
			}
		} else {
			Redirections.redirect(req, resp, ConstStrings.RESET, passwordErrMsg);
		}
	}
	
}
