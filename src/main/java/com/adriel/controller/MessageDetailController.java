package com.adriel.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Message;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.MessageService;
import com.adriel.utils.ConstStrings;

@Controller
public class MessageDetailController {
	
	@Autowired
	MessageService messageService;
	
	@RequestMapping(value=ConstStrings.MESSAGE_DETAIL, method=RequestMethod.GET)
	public String goToMsgDetail(HttpServletRequest req, HttpServletResponse resp, @PathVariable String msgid) {
		
		HttpSession personCurSess = req.getSession();
		
		if (personCurSess.getAttribute("personLoggedIn") != null) {
			int msgID = Integer.parseInt(msgid);
			
			Message m = null;
			try {
				m = messageService.getMessageById(msgID);
			} catch (ResourceNotFoundException e1) {
				personCurSess.setAttribute("ordIdErrMsg", "");
				personCurSess.setAttribute("msgIderrMsg", "Cannot access message #" + msgID + ".");
				try {
					resp.sendRedirect("/app/dashboard");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			req.setAttribute("curMsgID", msgID);
			
			if (m != null) {
				req.setAttribute("msgTitle", m.getTitle());
				req.setAttribute("msgBody", m.getMessage());
				req.setAttribute("fromCustomer", m.getFromCustomer());
				req.setAttribute("sentTime", m.getSentTime());
				return "App/msgdet";
			} else {
				personCurSess.setAttribute("ordIdErrMsg", "");
				personCurSess.setAttribute("msgIderrMsg", "Cannot access message #" + msgID + ".");
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
