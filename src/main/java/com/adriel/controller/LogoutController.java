package com.adriel.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogoutController {
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public void logOut(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession custCurSess = req.getSession(false);
		
		if (custCurSess != null) {
			custCurSess.invalidate();
		}
		
		HttpSession custNewSess = req.getSession();
		custNewSess.setAttribute("errMsg", "Successfully logged out! Thank you for using Santa Tracker.");
		try {
			resp.sendRedirect("/index");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
