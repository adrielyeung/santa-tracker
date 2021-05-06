package com.adriel.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;
import com.adriel.utils.ConstStrings;

@Controller
public class DashboardController {
	
	@GetMapping(value=ConstStrings.DASHBOARD)
	public String goToDashboardAfterLogin(HttpServletRequest req, HttpServletResponse resp) {
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		Person personLoggedIn = (Person) req.getSession().getAttribute("personLoggedIn");
		List<Order> personOrd = personLoggedIn.getPersonOrders();
		if (personOrd == null) {
			Redirections.redirect(req, resp, ConstStrings.INDEX, ConstStrings.INDEX_ERR, ConstStrings.SESSION_EXPIRED);
			return null;
		}
		Collections.sort(personOrd);
		List<Order> personOrdLatest = new ArrayList<>();
		if (personOrd.size() < 5) {
			personOrdLatest = personOrd;
		} else {
			for (int i = 0; i < 5; i++) {
				personOrdLatest.add(personOrd.get(i));
			}
		}
		req.getSession().setAttribute("personOrdLatest", personOrdLatest);
		
		return "App/dashboard";
	}
	
}
