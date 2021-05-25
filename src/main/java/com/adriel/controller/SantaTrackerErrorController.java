package com.adriel.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.service.PersonService;
import com.adriel.utils.Constants;

@Controller
public class SantaTrackerErrorController implements ErrorController {
	
	@Autowired
	PersonService personService;
	
	@Override
	public String getErrorPath() {
		return Constants.ERROR;
	}
	
	@RequestMapping(value=Constants.ERROR, method=RequestMethod.GET)
	public String goToErrorPage(HttpServletRequest req) {
		
		Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			Integer statusCode = Integer.parseInt(status.toString());
			
			String errorPageTitle = Constants.UNEXPECTED_ERROR_TITLE;
			String errorPageMsg = Constants.UNEXPECTED_ERROR_MESSAGE;
			
			if (statusCode == HttpStatus.FORBIDDEN.value()) {
				errorPageTitle = HttpStatus.FORBIDDEN.value() + Constants.FORBIDDEN_TITLE;
				errorPageMsg = Constants.FORBIDDEN_MESSAGE;
			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				errorPageTitle = HttpStatus.NOT_FOUND.value() + Constants.PAGE_NOT_FOUND_TITLE;
				errorPageMsg = Constants.PAGE_NOT_FOUND_MESSAGE;
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				errorPageTitle = HttpStatus.INTERNAL_SERVER_ERROR.value() + Constants.INTERNAL_SERVER_ERROR_TITLE;
				errorPageMsg = Constants.INTERNAL_SERVER_ERROR_MESSAGE;
			}
			
			req.setAttribute(Constants.ERROR_PAGE_TITLE, errorPageTitle);
			req.setAttribute(Constants.ERROR_PAGE_MSG, errorPageMsg);
		}
		
		return "Error/error";
	}

}
