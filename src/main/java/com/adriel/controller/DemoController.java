package com.adriel.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Person;
import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.factory.EntityFactory;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;
import com.adriel.utils.Redirections;

@Controller
public class DemoController {
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value=Constants.DEMO, method=RequestMethod.GET)
	public String goToDemoPage(Model model, HttpServletRequest req) {
		List<String> demoRoleList = new ArrayList<>();
		demoRoleList.add(Constants.DEMO_CUSTOMER);
		demoRoleList.add(Constants.DEMO_ADMIN);
		req.setAttribute("demoRoleList", demoRoleList);
		model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
		return "demo";
	}
	
	@RequestMapping(value=Constants.VERIFY_DEMO, method=RequestMethod.POST)
	public void verifyDemo(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		try {
			if (Constants.DEMO_CUSTOMER.equals(person.getUsername())) {
				req.getSession().setAttribute("personLoggedIn", personService.getPersonByUsername(Constants.DEMO_CUSTOMER));
			} else if (Constants.DEMO_ADMIN.equals(person.getUsername())) {
				req.getSession().setAttribute("personLoggedIn", personService.getPersonByUsername(Constants.DEMO_ADMIN));
			}
			Redirections.redirect(req, resp, Constants.DASHBOARD, null, null);
		} catch (ResourceNotFoundException e) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.INTERNAL_SERVER_ERROR_MESSAGE);
		}
	}
	
}
