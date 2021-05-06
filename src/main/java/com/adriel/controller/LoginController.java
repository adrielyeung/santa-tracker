package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.adriel.utils.ConstStrings;
import com.adriel.utils.Redirections;

@Controller
public class LoginController {
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value={"/", ConstStrings.INDEX}, method=RequestMethod.GET)
	public String goToIndexPage(Model model) {
		model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
		return "index";
	}
	
	@RequestMapping(value=ConstStrings.VERIFY_LOGIN, method=RequestMethod.POST)
	public void verifyLogin(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession personCurSess = req.getSession();
		
		if (personCurSess.getAttribute("personLoggedIn") == null) {
			String errMsg = verifyUsernameAndPassword(person, req);
			if ("".equals(errMsg)) {
				Redirections.redirect(req, resp, ConstStrings.DASHBOARD, null, null);
			} else {
				Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, errMsg);
			}
		} else {
			Redirections.redirect(req, resp, ConstStrings.DASHBOARD, null, null);
		}
	}
	
	private String verifyUsernameAndPassword(Person person, HttpServletRequest req) {
		Person u = null;
		HttpSession personCurSess = req.getSession();
		try {
			u = personService.getPersonByUsername(person.getUsername());
		} catch (ResourceNotFoundException e) {
			return ConstStrings.INVALID_USERNAME_PWORD;
		}
		
		if (u == null || !u.getPword().equals(person.getPword())) {
			return ConstStrings.INVALID_USERNAME_PWORD;
		} else if (u.getAdmin() == 0 && u.getAdminToken() != null && !"".equals(u.getAdminToken())) {
			return ConstStrings.PENDING_ADMIN_APPROVAL_ERR;
		} else {
			personCurSess.setAttribute("personOrd", u.getPersonOrders());
			personCurSess.setAttribute("personLoggedIn", u);
			return "";
		}
	}
	
}
