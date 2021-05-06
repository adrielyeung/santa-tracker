package com.adriel.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.OrderService;
import com.adriel.utils.ConstStrings;

@Controller
public class OrderDetailController {
	
	@Autowired
	OrderService orderService;
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	@RequestMapping(value=ConstStrings.ORDER_DETAIL, method=RequestMethod.GET)
	public String goToOrderDetail(HttpServletRequest req, HttpServletResponse resp, @PathVariable String orderid) {
		
		HttpSession personCurSess = req.getSession();
		
		if (personCurSess.getAttribute("personLoggedIn") != null) {
			int orderID = Integer.parseInt(orderid);
			
			Order o = null;
			try {
				o = orderService.getOrderById(orderID);
			} catch (ResourceNotFoundException e1) {
				personCurSess.setAttribute("ordIdErrMsg", "Cannot access order #" + orderID + ".");
				personCurSess.setAttribute("msgIderrMsg", "");
				try {
					resp.sendRedirect("/app/dashboard");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			req.setAttribute("curOrderID", orderID);
			
			if (o != null && o.getPerson().getUsername().equals(((Person)personCurSess.getAttribute("personLoggedIn")).getUsername())) {
				List<OrderDetail> ordDet = o.getOrderDets();
				
				Collections.sort(ordDet);
				
				double totalCost = 0.0;
				for(OrderDetail orddet : ordDet) {
					totalCost += orddet.getProduct().getUnitPrice() * orddet.getQuantity();
				}
				String roundedCost = df.format(totalCost);
				
				req.setAttribute("ordDet", ordDet);
				req.setAttribute("totalCost", roundedCost);
				return "App/orderdet";
			} else {
				personCurSess.setAttribute("ordIdErrMsg", "Cannot access order #" + orderID + ".");
				personCurSess.setAttribute("msgIderrMsg", "");
				try {
					resp.sendRedirect("/app/dashboard");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		} else {
			personCurSess.setAttribute("errMsg", "Session has expired. Please login again.");
			try {
				resp.sendRedirect("/index");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
