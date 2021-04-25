package com.adriel.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.utils.Redirections;
import com.adriel.utils.ConstStrings;

@Controller
public class DashboardController {
	
	@GetMapping(value="/app/dashboard")
	public String goToDashboardAfterLogin(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession custCurSess = req.getSession();
		
		Person personLoggedIn = (Person) custCurSess.getAttribute("personLoggedIn");
		if (personLoggedIn == null) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		List<Order> custOrd = personLoggedIn.getPersonOrders();
		if (custOrd == null) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		Collections.sort(custOrd);
		List<Order> custOrdLatest = new ArrayList<>();
		if (custOrd.size() < 5) {
			custOrdLatest = custOrd;
		} else {
			for (int i = 0; i < 5; i++) {
				custOrdLatest.add(custOrd.get(i));
			}
		}
		custCurSess.setAttribute("custOrdLatest", custOrdLatest);
		
		return "App/dashboard";
	}
}
