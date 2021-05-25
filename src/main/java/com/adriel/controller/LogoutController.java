package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.utils.Constants;
import com.adriel.utils.Redirections;

@Controller
public class LogoutController {
	
	@RequestMapping(value=Constants.LOGOUT, method=RequestMethod.GET)
	public void logOut(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession personCurSess = req.getSession(false);
		
		if (personCurSess != null) {
			personCurSess.invalidate();
		}
		
		Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.LOGGED_OUT);
	}
}
