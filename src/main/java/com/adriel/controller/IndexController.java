package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.factory.EntityFactory;

@Controller
public class IndexController {
	
	@RequestMapping(value={"/", "/index"}, method=RequestMethod.GET)
	public String goToIndexPage(Model model) {
		model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
		return "index";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String goToRegisterPage(Model model, @Param(value = "admin") String admin, HttpServletRequest req, HttpServletResponse resp) {
		int adminInt = 0;
		// Admin > 0 for admin role, any other string for customer role
		try {
			adminInt = Integer.parseInt(admin);
			if (adminInt > 0) {
				req.getSession().setAttribute("admin", 1);
			}
		} catch (NumberFormatException e) {
			req.getSession().setAttribute("admin", 0);
		}
		
		model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
		return "register";
	}
	
}
