package com.adriel.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.factory.EntityFactory;
import com.adriel.service.MessageService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;
import com.adriel.utils.EmailSender;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class MessageController {
	
	@Autowired
	MessageService messageService;
	@Autowired
	OrderService orderService;
	@Autowired
	PersonService personService;
	@Autowired
	EmailSender emailSender;
	
	private static final Integer FROM_ADMIN = 0;
	private static final Integer FROM_CUSTOMER = 1;
	
	@RequestMapping(value=Constants.MESSAGE, method=RequestMethod.GET)
	public String goToMessagePage(HttpServletRequest req, HttpServletResponse resp, @PathVariable String orderid) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		Order order = null;
		try {
			order = orderService.getOrderById(Integer.parseInt(orderid));
			Collections.sort(order.getOrderMsgs());
		} catch (ResourceNotFoundException e1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return null;
		}
		req.setAttribute("order", order);
		
		if (!Utils.checkAccess(order, ((Person)req.getSession().getAttribute("personLoggedIn")), req)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return null;
		}
		
		return "App/msg";
	}
	
	@RequestMapping(value=Constants.NEW_MESSAGE, method=RequestMethod.GET)
	public String goToNewMessagePage(Model model, HttpServletRequest req, HttpServletResponse resp, @PathVariable String orderid) {
		
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
		
		if (!Utils.checkAccess(order, ((Person)req.getSession().getAttribute("personLoggedIn")), req)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return null;
		}
		
		Message message = (Message) EntityFactory.getEntity(SantaTrackerEntityType.MESSAGE);
		message.setOrder(order);
		
		model.addAttribute("message", message);
		return "App/add-msg";
	}
	
	@RequestMapping(value=Constants.VERIFY_MESSAGE, method=RequestMethod.POST)
	public void verifyNewMessage(@ModelAttribute("message") Message msg, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return;
		}
		
		Order order = null;
		String orderid = req.getParameter("orderID");
		Person personLoggedIn = (Person)req.getSession().getAttribute("personLoggedIn");
		try {
			order = orderService.getOrderById(Integer.parseInt(orderid));
		} catch (ResourceNotFoundException e1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return;
		}
		req.setAttribute("order", order);
		
		if (!Utils.checkAccess(order, personLoggedIn, req)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, String.format(Constants.ORDER_NOT_FOUND, orderid));
			return;
		}
		
		if (personLoggedIn.getAdmin() == 1) {
			msg.setFromCustomer(FROM_ADMIN);
		} else {
			msg.setFromCustomer(FROM_CUSTOMER);
		}
		msg.setSentTime(LocalDateTime.now());
		msg.setOrder(order);
		msg = messageService.createMessage(msg);
		
		// Notification email (not applicable to demo users)
		if (personLoggedIn.getDemo() == 1) {
			Redirections.redirect(req, resp, Constants.MESSAGE.replaceAll("\\{orderid\\}", orderid), Constants.MESSAGE_ERR, Constants.SUCCESS_SEND_MESSAGE);
			return;
		}
		
		try {
			if (personLoggedIn.getAdmin() == 1) {
				emailSender.sendEmail(new ArrayList<String>(Arrays.asList(order.getPerson().getEmail())), new ArrayList<String>(), new ArrayList<String>(), 
						Constants.EMAIL_MESSAGE_RECEIVED_TITLE, String.format(Constants.EMAIL_MESSAGE_RECEIVED_BODY, order.getPerson().getUsername(), String.valueOf(msg.getMessageID()), msg.getTitle(), msg.getBody(),
								Constants.DATETIME_FORMATTER.format(msg.getSentTime()), Utils.getSiteURL(req) + Constants.INDEX));
			} else {
				emailSender.sendEmail(new ArrayList<String>(Arrays.asList(personLoggedIn.getEmail())), new ArrayList<String>(), new ArrayList<String>(), 
						Constants.EMAIL_MESSAGE_SENT_TITLE, String.format(Constants.EMAIL_MESSAGE_SENT_BODY, personLoggedIn.getUsername(), String.valueOf(msg.getMessageID()), msg.getTitle(), msg.getBody(), 
								Constants.DATETIME_FORMATTER.format(msg.getSentTime())));
			}
			Redirections.redirect(req, resp, Constants.MESSAGE.replaceAll("\\{orderid\\}", orderid), Constants.MESSAGE_ERR, Constants.SUCCESS_SEND_MESSAGE);
		} catch (UnsupportedEncodingException | MessagingException e) {
			Redirections.redirect(req, resp, Constants.MESSAGE.replaceAll("\\{orderid\\}", orderid), Constants.MESSAGE_ERR, String.format(Constants.EMAIL_SEND_ERROR_SUCCESS_CREATE_EDIT_ENTITY, "message", "sent"));
		}
		
	}
	
}
