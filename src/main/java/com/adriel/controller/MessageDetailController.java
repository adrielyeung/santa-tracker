package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.MessageService;
import com.adriel.utils.Constants;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class MessageDetailController {
	
	@Autowired
	MessageService messageService;
	
	@RequestMapping(value=Constants.MESSAGE_DETAIL, method=RequestMethod.GET)
	public String goToMessageDetail(HttpServletRequest req, HttpServletResponse resp, @PathVariable String msgid) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		int msgID = Integer.parseInt(msgid);
		
		Message message = null;
		try {
			message = messageService.getMessageById(msgID);
		} catch (ResourceNotFoundException e1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.MESSAGE_NOT_FOUND, msgid));
			return null;
		}
		
		if (!checkAccess(message.getOrder(), (Person)req.getSession().getAttribute("personLoggedIn"), req)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.MESSAGE_NOT_FOUND, msgid));
			return null;
		}
		
		req.setAttribute("msg", message);
		return "App/msgdet";
		
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
