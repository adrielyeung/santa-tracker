package com.adriel.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.Message;

@Controller
public class MessageController {
	
	@RequestMapping(value="/app/message", method=RequestMethod.GET)
	public String goToMessagePage(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession custCurSess = req.getSession();
		
		List<Message> custMsg = (List<Message>) custCurSess.getAttribute("custMsg");
		
		if (custMsg != null) {
			return "App/msg";
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
