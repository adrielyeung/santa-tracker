package com.adriel.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
import com.adriel.entity.Person;
import com.adriel.entity.Product;
import com.adriel.entity.SantaTrackerEntityType;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.factory.EntityFactory;
import com.adriel.service.OrderDetailService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;
import com.adriel.service.ProductService;
import com.adriel.utils.Constants;
import com.adriel.utils.EmailSender;
import com.adriel.utils.Redirections;
import com.adriel.utils.Utils;

@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	@Autowired
	OrderDetailService orderDetailService;
	@Autowired
	ProductService productService;
	@Autowired
	PersonService personService;
	@Autowired
	EmailSender emailSender;
	
	private static final Integer STATUS_OK = 1;
	private static final Integer STATUS_NOT_OK = 2;
	
	private static final Integer EDITABLE_TIME = 5;
	
	@RequestMapping(value=Constants.ADD_ORDER, method=RequestMethod.GET)
	public String goToNewOrderPage(Model model, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		if (((Person) req.getSession().getAttribute("personLoggedIn")).getAdmin() == 1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, Constants.ACCESS_DENIED);
			return null;
		}
		
		if (((Person) req.getSession().getAttribute("personLoggedIn")).getDemo() == 1) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, Constants.DEMO_ACCESS_DENIED);
			return null;
		}
		
		String errMsg = checkAccess(null, "Add", (Person) req.getSession().getAttribute("personLoggedIn"));
		if (!"".equals(errMsg)) {
			Redirections.redirect(req, resp, Constants.ORDER, Constants.ORDER_ERR, errMsg);
		}
		
		int numOfRowsDefault = 10;
		
		Order newOrder = (Order) EntityFactory.getEntity(SantaTrackerEntityType.ORDER);
		
		for (int i = 0; i < numOfRowsDefault; i++) {
			newOrder.addOrderDetail((OrderDetail) EntityFactory.getEntity(SantaTrackerEntityType.ORDERDETAIL));
		}
		
		model.addAttribute("order", newOrder);
		
		List<Product> productList = productService.getAllProducts(0);
		req.setAttribute("productList", productList);
		
		req.setAttribute("type", "Add");
		
		return "App/edit-order";
		
	}
	
	@RequestMapping(value=Constants.ORDER, method=RequestMethod.GET)
	public String goToOrderPage(Model model, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		if (((Person) req.getSession().getAttribute("personLoggedIn")).getPersonOrders() != null) {
			Collections.sort(((Person) req.getSession().getAttribute("personLoggedIn")).getPersonOrders());
			model.addAttribute("person", EntityFactory.getEntity(SantaTrackerEntityType.PERSON));
			try {
				req.setAttribute("customerList", personService.getAllCustomers(((Person) req.getSession().getAttribute("personLoggedIn")).getDemo()));
			} catch (ResourceNotFoundException e) {
				Redirections.redirect(req, resp, Constants.ORDER, Constants.ORDER_ERR, Constants.CUSTOMER_NOT_FOUND);
			}
			
			return "App/order";
		} else {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
	}
	
	@RequestMapping(value=Constants.ORDER, method=RequestMethod.POST)
	public String loadCustomerDataInOrderPage(@ModelAttribute("person") Person person, Model model, HttpServletRequest req, HttpServletResponse resp) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		Person personSelected = null;
		try {
			personSelected = personService.getPersonById(person.getPersonID());
		} catch (ResourceNotFoundException e) {
			Redirections.redirect(req, resp, Constants.ORDER, Constants.ORDER_ERR, Constants.CUSTOMER_NOT_FOUND);
		}
		
		if (personSelected.getPersonOrders() != null) {
			Collections.sort(personSelected.getPersonOrders());
			model.addAttribute("person", personSelected);
			req.setAttribute("personSelected", personSelected);
			try {
				req.setAttribute("customerList", personService.getAllCustomers(((Person) req.getSession().getAttribute("personLoggedIn")).getDemo()));
			} catch (ResourceNotFoundException e) {
				Redirections.redirect(req, resp, Constants.ORDER, Constants.ORDER_ERR, Constants.CUSTOMER_NOT_FOUND);
			}
			return "App/order";
		} else {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
	}
	
	@RequestMapping(value=Constants.EDIT_ORDER, method=RequestMethod.GET)
	public String goToUpdateOrderPage(Model model, HttpServletRequest req, HttpServletResponse resp, @PathVariable String orderid, @PathVariable String submitType) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return null;
		}
		
		if (((Person) req.getSession().getAttribute("personLoggedIn")).getDemo() == 1 && !"Schedule".equals(submitType)) {
			Redirections.redirect(req, resp, Constants.DASHBOARD, Constants.DASHBOARD_ERR, Constants.DEMO_ACCESS_DENIED);
			return null;
		}
		
		Order orderToUpdate = null;
		try {
			orderToUpdate = orderService.getOrderById(Integer.parseInt(orderid));
			
			String errMsg = checkAccess(orderToUpdate, submitType, (Person) req.getSession().getAttribute("personLoggedIn"));
			if (!"".equals(errMsg)) {
				Redirections.redirect(req, resp, Constants.ORDER, Constants.ORDER_ERR, errMsg);
			}
			
			int orderSize = orderToUpdate.getOrderDets().size();
			int numOfRowsDefault = Utils.findGreaterMultipleOfTen(orderSize);
			for (int i = 0; i < numOfRowsDefault - orderSize; i++) {
				orderToUpdate.addOrderDetail((OrderDetail) EntityFactory.getEntity(SantaTrackerEntityType.ORDERDETAIL));
			}
			
			model.addAttribute("order", orderToUpdate);
			
			List<Product> productList = productService.getAllProducts(0);
			req.setAttribute("productList", productList);
			
			req.setAttribute("type", submitType);
			
			return "App/edit-order";
		} catch (NumberFormatException | ResourceNotFoundException e) {
			Redirections.redirect(req, resp, Constants.ADD_ORDER, Constants.EDIT_ORDER_ERR, String.format(Constants.EDIT_ENTITY_ERR, "order"));
		}
		return null;
		
	}
	
	@RequestMapping(value=Constants.VERIFY_ORDER, method=RequestMethod.POST)
	public void verifyOrder(@ModelAttribute("order") Order order, HttpServletRequest req, HttpServletResponse resp, @PathVariable String submitType) {
		
		if (Utils.isLoggedOut(req)) {
			Redirections.redirect(req, resp, Constants.INDEX, Constants.INDEX_ERR, Constants.SESSION_EXPIRED);
			return;
		}
		
		if (order.getEstimatedTime() != null && order.getPlannedTime() != null) {
			if (order.getEstimatedTime().isAfter(order.getPlannedTime())) {
				order.setStatus(STATUS_NOT_OK);
			} else {
				order.setStatus(STATUS_OK);
			}
		}
		
		if ("Add".equals(submitType)) {
			order.setOrderTime(LocalDateTime.now());
			order.setPerson((Person) req.getSession().getAttribute("personLoggedIn"));
			
			List<OrderDetail> orderDetailsRemoveEmpty = removeEmptyDetail(order);
			if (orderDetailsRemoveEmpty.size() == 0) {
				Redirections.redirect(req, resp, Constants.ADD_ORDER, null, null);
				return;
			}
			Order orderSaved = orderService.createOrder(order);
			
			for (OrderDetail orderDetail : orderDetailsRemoveEmpty) {
				orderDetail.setOrder(order);
				orderDetailService.createOrderDetail(orderDetail);
			}
			
			String totalCost = Constants.TWO_DECIMAL_DIGITS_FORMATTER.format(Utils.findTotalCost(orderDetailsRemoveEmpty));
			req.getSession().setAttribute("totalCost", totalCost);
			
			// Email notification
			try {
				emailSender.sendEmail(new ArrayList<String>(Arrays.asList(orderSaved.getPerson().getEmail())), new ArrayList<String>(), new ArrayList<String>(),
						String.format(Constants.EMAIL_ORDER_SUCCESS_TITLE, orderSaved.getOrderID()), String.format(Constants.EMAIL_ORDER_SUCCESS_BODY, orderSaved.getPerson().getUsername(), String.valueOf(orderSaved.getOrderID()), getOrderDetailEmailList(orderDetailsRemoveEmpty), 
								totalCost, Constants.DATETIME_FORMATTER.format(orderSaved.getOrderTime()), Utils.getSiteURL(req) + Constants.INDEX));
				Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(orderSaved.getOrderID())).replaceAll("\\{submitType\\}", "Edit"), Constants.EDIT_ORDER_ERR, String.format(Constants.SUCCESS_CREATE_ENTITY, "order"));
			} catch (UnsupportedEncodingException | MessagingException e) {
				Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(orderSaved.getOrderID())).replaceAll("\\{submitType\\}", "Edit"), Constants.EDIT_ORDER_ERR, String.format(Constants.EMAIL_SEND_ERROR_SUCCESS_CREATE_EDIT_ENTITY, "order", "created"));
			}
			
		} else if ("Schedule".equals(submitType)) {
			try {
				Order existingOrder = orderService.getOrderById(order.getOrderID());
				order.setOrderID(existingOrder.getOrderID());
				order.setPerson(existingOrder.getPerson());
				order.setOrderTime(existingOrder.getOrderTime());
				Order orderSaved = orderService.updateOrder(order.getOrderID(), order);
				List<OrderDetail> orderDetailsRemoveEmpty = removeEmptyDetail(orderSaved);
				
				// Email notification (not applicable to demo users)
				if (orderSaved.getPerson().getDemo() == 1) {
					Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(order.getOrderID())).replaceAll("\\{submitType\\}", "Schedule"), Constants.EDIT_ORDER_ERR, String.format(Constants.SUCCESS_EDIT_ENTITY, "order"));
					return;
				}
				emailSender.sendEmail(new ArrayList<String>(Arrays.asList(orderSaved.getPerson().getEmail())), new ArrayList<String>(), new ArrayList<String>(),
						String.format(Constants.EMAIL_ORDER_SCHEDULED_TITLE, orderSaved.getOrderID(), setStatusString(orderSaved.getStatus())), 
						String.format(Constants.EMAIL_ORDER_SCHEDULED_BODY, orderSaved.getPerson().getUsername(), 
								String.valueOf(orderSaved.getOrderID()), getOrderDetailEmailList(orderDetailsRemoveEmpty), Constants.TWO_DECIMAL_DIGITS_FORMATTER.format(Utils.findTotalCost(orderDetailsRemoveEmpty)), 
								Constants.DATETIME_FORMATTER.format(orderSaved.getOrderTime()), Constants.DATETIME_FORMATTER.format(orderSaved.getPlannedTime()), Constants.DATETIME_FORMATTER.format(orderSaved.getEstimatedTime()), orderSaved.getLocation(), 
								setStatusString(orderSaved.getStatus()), Utils.getSiteURL(req) + Constants.INDEX));
				Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(order.getOrderID())).replaceAll("\\{submitType\\}", "Schedule"), Constants.EDIT_ORDER_ERR, String.format(Constants.SUCCESS_EDIT_ENTITY, "order"));
			} catch (ResourceNotFoundException e) {
				Redirections.redirect(req, resp, Constants.ADD_ORDER, Constants.EDIT_ORDER_ERR, String.format(Constants.EDIT_ENTITY_ERR, "order"));
			} catch (UnsupportedEncodingException | MessagingException e) {
				Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(order.getOrderID())).replaceAll("\\{submitType\\}", "Schedule"), Constants.EDIT_ORDER_ERR, String.format(Constants.EMAIL_SEND_ERROR_SUCCESS_CREATE_EDIT_ENTITY, "order", "edited"));
			}
		} else if ("Edit".equals(submitType)) {
			order.setOrderTime(LocalDateTime.now());
			try {
				List<OrderDetail> orderDetailsOld = orderDetailService.getOrderDetailsByOrderId(order.getOrderID());
				for (OrderDetail orderDetail : orderDetailsOld) {
					orderDetailService.deleteOrderDetail(orderDetail.getOrderDetID());
				}
				List<OrderDetail> orderDetailsRemoveEmpty = removeEmptyDetail(order);
				for (OrderDetail orderDetail : orderDetailsRemoveEmpty) {
					orderDetail.setOrder(order);
					orderDetailService.createOrderDetail(orderDetail);
				}
				req.getSession().setAttribute("totalCost", Constants.TWO_DECIMAL_DIGITS_FORMATTER.format(Utils.findTotalCost(orderDetailsRemoveEmpty)));
				Redirections.redirect(req, resp, Constants.EDIT_ORDER.replaceAll("\\{orderid\\}", String.valueOf(order.getOrderID())).replaceAll("\\{submitType\\}", "Edit"), Constants.EDIT_ORDER_ERR, String.format(Constants.SUCCESS_EDIT_ENTITY, "order"));
			} catch (ResourceNotFoundException e) {
				Redirections.redirect(req, resp, Constants.ADD_ORDER, Constants.EDIT_ORDER_ERR, String.format(Constants.EDIT_ENTITY_ERR, "order"));
			}
		}
	
	}
	
	private String setStatusString(int status) {
		switch (status) {
		case 1:
			return "On time";
		case 2:
			return "Delayed";
		default:
			return "Not assigned";
		}
	}
	
	private String getOrderDetailEmailList(List<OrderDetail> orderDetailsRemoveEmpty) {
		String orderDetailEmail = "";
		for (OrderDetail orderDetail : orderDetailsRemoveEmpty) {
			orderDetailEmail += String.format(Constants.EMAIL_TABLE_ROW, orderDetail.getProduct().getNameAndUnitPrice(), orderDetail.getQuantity());
		}
		return orderDetailEmail;
	}
	
	private List<OrderDetail> removeEmptyDetail(Order order) {
		List<OrderDetail> orderDetails = order.getOrderDets();
		List<OrderDetail> orderDetailsRemoveEmpty = new ArrayList<>();
		
		for (OrderDetail orderDetail : orderDetails) {
			if (orderDetail.getProduct() != null && orderDetail.getQuantity() > 0) {
				orderDetailsRemoveEmpty.add(orderDetail);
			}
		}
		order.setOrderDets(orderDetailsRemoveEmpty);
		return orderDetailsRemoveEmpty;
	}
	
	private String checkAccess(Order order, String submitType, Person person) {
		
		if (("Add".equals(submitType) || "Edit".equals(submitType)) && person.getAdmin() == 1) {
			return Constants.ACCESS_DENIED;
		}
		
		if ("Schedule".equals(submitType) && person.getAdmin() == 0) {
			return Constants.ACCESS_DENIED;
		}
		
		// Only allow edits within "EDITABLE_TIME" mins of order time
		if (order != null) {
			if ("Edit".equals(submitType)) {
				LocalDateTime editableTime = order.getOrderTime().plusMinutes(EDITABLE_TIME);
				
				if (LocalDateTime.now().isAfter(editableTime)) {
					return Constants.AFTER_EDITABLE_TIME;
				}
			}
		}
		
		return "";
	}
}
