package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Person;
import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.factory.EntityFactory;
import com.adriel.service.PersonService;
import com.adriel.utils.ConstStrings;
import com.adriel.utils.Redirections;

@Controller
public class ResetController {
	
	@Autowired
	private PersonService personService;
	
	@RequestMapping(value="/reset", method=RequestMethod.GET)
	public String goToResetEmailPage(Model model) {
		model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
		return "reset";
	}
	
	@RequestMapping(value="/reset-password", method=RequestMethod.GET)
	public String goToResetPasswordPage(Model model, @Param(value = "token") String token, HttpServletRequest req, HttpServletResponse resp) {
		try {
			Person person = personService.getPersonByResetPasswordToken(token);
			req.getSession().setAttribute("personReset", person);
			model.addAttribute("person", person);
			return "reset-password";
		} catch (ResourceNotFoundException | IllegalStateException e) {
			Redirections.redirect(req, resp, ConstStrings.RESET, ConstStrings.PERSON_NOT_FOUND_TOKEN_RESET);
			return null;
		}
	}
}
