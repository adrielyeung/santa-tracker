package com.adriel.controller;

import java.io.IOException;
import java.time.LocalDateTime;

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
import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.service.MessageService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;

@Controller
public class VerifyNewMsgController {
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value="/verify-msg", method=RequestMethod.POST)
	public void verifyNewMsg(@ModelAttribute("message") Message msg, HttpServletRequest req, HttpServletResponse resp) {
		
		HttpSession personCurSess = req.getSession();
		
		boolean isLoggedIn = verifyLoggedIn(req, personCurSess);
		
		if (!isLoggedIn) {
			personCurSess.setAttribute("errMsg", "Session has expired. Please login again.");
			try {
				resp.sendRedirect("/index");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			msg.setFromCustomer(1);
			msg.setSentTime(LocalDateTime.now());
			int orderID = Integer.parseInt(req.getParameter("orderID"));
			Order order = null;
			try {
				order = orderService.getOrderById(orderID);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			String uname = (String) personCurSess.getAttribute("uname");
			boolean orderOk = verifyOrder(order, uname, personCurSess, personService);
			if (orderOk) {
				msg.setOrder(order);
				msg.getOrder().addMessage(msg);
				messageService.createMessage(msg);
				personCurSess.setAttribute("resMsg", "Thank you for contacting us! We aim to reply you within 2 working days.");
				try {
					resp.sendRedirect("/app/message");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				personCurSess.setAttribute("newMsgErrMsg", "Order ID entered does not match any of your orders. Please try again.");
				try {
					resp.sendRedirect("/app/new-msg");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean verifyLoggedIn(HttpServletRequest req, HttpSession curSess) {
		return (curSess.getAttribute("uname") != null && curSess.getAttribute("psw") != null);
	}
	
	private boolean verifyOrder(Order ord, String uname, HttpSession curSess, PersonService persondao) {
		if (ord == null) {
			return false;
		} else {
			Person person = null;
			try {
				person = persondao.getPersonByUsername(uname);
			} catch (ResourceNotFoundException e) {
				return false;
			}
			return ord.getPersonID() == person.getPersonID();
		}
	}
}
