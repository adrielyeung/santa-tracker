package com.adriel.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.OrderService;
import com.adriel.utils.Constants;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class OrderDetailController {
	
	@Autowired
	OrderService orderService;
	
	@RequestMapping(value=Constants.ORDER_DETAIL, method=RequestMethod.GET)
	public String goToOrderDetail(HttpServletRequest req, HttpServletResponse resp, @PathVariable String orderid) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		Order order = null;
		try {
			order = orderService.getOrderById(Integer.parseInt(orderid));
		} catch (ResourceNotFoundException e1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return null;
		}
		req.setAttribute("order", order);
		
		if (!checkAccess(order, ((Person)req.getSession().getAttribute("personLoggedIn")), req)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return null;
		}
		
		Collections.sort(order.getOrderDets());
		
		req.setAttribute("totalCost", Constants.TWO_DECIMAL_DIGITS_FORMATTER.format(Utils.findTotalCost(order.getOrderDets())));
		return "App/orderdet";
	}
	
	private boolean checkAccess(Order order, Person person, HttpServletRequest req) {
		
		// Order is requested by the same user
		if (order.getPerson().getUsername().equals(person.getUsername())) {
			return true;
		}
		
		// Admin allowed to view all orders
		if (person.getAdmin() == 1) {
			return true;
		}
		
		return false;
	}
}
