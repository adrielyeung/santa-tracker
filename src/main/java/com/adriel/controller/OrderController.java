package com.adriel.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Order;

@Controller
public class OrderController {
	
	@RequestMapping(value="/app/order", method=RequestMethod.GET)
	public String goToOrderPage(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession custCurSess = req.getSession();
		
		List<Order> custOrd = (List<Order>) custCurSess.getAttribute("custOrd");
		
		if (custOrd != null) {
			return "App/order";
		} else {
			req.getSession().setAttribute("errMsg", "Session has expired. Please login again.");
			try {
				resp.sendRedirect("/index");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
