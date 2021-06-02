package com.adriel.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.service.MessageService;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;
import com.adriel.utils.Constants;

@Controller
public class DashboardController {
	
	@Autowired
	MessageService messageService;
	
	private static final int NUM_RECENT_ITEMS_CUSTOMER = 5;
	private static final int NUM_RECENT_ITEMS_ADMIN = 10;
	
	@RequestMapping(value=Constants.DASHBOARD, method=RequestMethod.GET)
	public String goToDashboardAfterLogin(HttpServletRequest req, HttpServletResponse resp) {
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		Person personLoggedIn = (Person) req.getSession().getAttribute("personLoggedIn");
		
		if (personLoggedIn.getAdmin() == 0) {
			// If customer, display 5 most recent orders
			List<Order> personOrd = personLoggedIn.getPersonOrders();
			if (personOrd == null) {
				Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
				return null;
			}
			
			req.getSession().setAttribute("personOrdLatest", Utils.getRecent(personOrd, NUM_RECENT_ITEMS_CUSTOMER));
			
			// If customer, display 5 most recent conversations of their orders
			// Display 5 most recent conversations
			List<Message> personMsg = messageService.getMessagesByPersonId(personLoggedIn.getPersonID());
			
			if (personMsg == null) {
				Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
				return null;
			}
			req.getSession().setAttribute("personMsgLatest", getRecentMessagesByUniqueOrders(personMsg, NUM_RECENT_ITEMS_CUSTOMER));
			
		} else {
			// If admin, display 10 most recent conversations only across all orders
			List<Message> personMsg = messageService.getAllMessages(((Person) req.getSession().getAttribute("personLoggedIn")).getDemo());
			
			if (personMsg == null) {
				Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
				return null;
			}
			req.getSession().setAttribute("personMsgLatest", getRecentMessagesByUniqueOrders(personMsg, NUM_RECENT_ITEMS_ADMIN));
		}
		
		return "App/dashboard";
	}
	
	private List<Message> getRecentMessagesByUniqueOrders(List<Message> fullList, int numOfRecentOrders) {
		Collections.sort(fullList);
		List<Message> recentList = new ArrayList<>();
		List<Integer> idList = new ArrayList<>();
		
		int i = 0;
		int count = 0;
		while (count < numOfRecentOrders && i < fullList.size()) {
			if (!idList.contains(fullList.get(i).getOrderID())) {
				recentList.add(fullList.get(i));
				idList.add(fullList.get(i).getOrderID());
				count++;
			}
			i++;
		}
		return recentList;
	}
	
}
