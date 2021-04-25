package com.adriel.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.factory.EntityFactory;

@Controller
public class NewMessageController {
	
	@RequestMapping(value="/app/new-msg", method=RequestMethod.GET)
	public String goToNewMessagePage(Model model) {
		model.addAttribute("message", EntityFactory.getEntity(SantaTrackerEntityType.MESSAGE));
		return "App/new-msg";
	}
}
