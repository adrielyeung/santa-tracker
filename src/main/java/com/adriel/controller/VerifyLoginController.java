package com.adriel.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.ConstStrings;
import com.adriel.utils.Redirections;

@Controller
public class VerifyLoginController {
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value="/verify-login", method=RequestMethod.POST)
	public void verifyLogin(@ModelAttribute("person") Person person, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession personCurSess = req.getSession();
		String uname_inp;
		String pword_inp;
		
		boolean isLoggedIn = verifyLoggedIn(req, personCurSess);
		
		if (!isLoggedIn) {
			String errMsg = verifyUsernameAndPassword(person, req);
			if ("".equals(errMsg)) {
				uname_inp = person.getUsername();
				pword_inp = person.getPword();
				personCurSess.setAttribute("uname", uname_inp);
				personCurSess.setAttribute("psw", pword_inp);
				try {
					resp.sendRedirect("/app/dashboard");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Redirections.redirect(req, resp, ConstStrings.INDEX, errMsg);
			}
		} else {
			try {
				resp.sendRedirect("/app/dashboard");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean verifyLoggedIn(HttpServletRequest req, HttpSession curSess) {
		return (curSess.getAttribute("uname") != null && curSess.getAttribute("psw") != null);
	}

	private String verifyUsernameAndPassword(Person person, HttpServletRequest req) {
		Person u = null;
		HttpSession personCurSess = req.getSession();
		try {
			u = personService.getPersonByUsername(person.getUsername());
		} catch (ResourceNotFoundException e) {
			return "Invalid username or password.";
		}
		
		if (u == null || !u.getPword().equals(person.getPword())) {
			return "Invalid username or password.";
		} else if (u.getAdmin() == 0 && !"".equals(u.getAdminToken())) {
			return "Please wait for an admin to approve your admin request before we set up your account.";
		} else {
			personCurSess.setAttribute("personLoggedIn", u);
			return "";
		}
	}
}
