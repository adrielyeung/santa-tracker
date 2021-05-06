package com.adriel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.factory.EntityFactory;
import com.adriel.service.PersonService;
import com.adriel.utils.ConstStrings;

@Controller
public class EditOrderController {
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value=ConstStrings.EDIT_ORDER, method=RequestMethod.GET)
	public String goToNewOrderPage(Model model) {
		
		model.addAttribute("order", EntityFactory.getEntity(SantaTrackerEntityType.ORDER));
		return "App/edit-order";
		
	}
}
