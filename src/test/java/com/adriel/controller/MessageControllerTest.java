package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.entity.Message;
import com.adriel.entity.Order;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.MessageService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
	
	@InjectMocks
	MessageController testMessageController;
	
	@Mock
	Model mockMod;
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSess;
	@Mock
	Order mockOrder;
	@Mock
	Person mockPerson;
	@Mock
	Message mockMessage;
	@Mock
	OrderService mockOrderService;
	@Mock
	PersonService mockPersonService;
	@Mock
	MessageService mockMessageService;
	@Mock
	List<Order> mockOrderList;
	@Mock
	List<Message> mockMessageList;
	@Mock
	List<Person> mockPersonList;
	
	@BeforeEach
	public void init() {
		when(mockReq.getSession()).thenReturn(mockSess);
		when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
	}
	
	@Test
	public void whenGoToNewMessagePageCalled_ValidCustomer_thenReturnAddMessageString() {
		
		int orderid = 1;
		String username = "test";
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockPerson);
		when(mockPerson.getUsername()).thenReturn(username);
		
		String ret = testMessageController.goToNewMessagePage(mockMod, mockReq, mockResp, String.valueOf(orderid));
		
		assertEquals("App/add-msg", ret);
	}
	
	@Test
	public void whenGoToNewMessagePageCalled_Admin_thenReturnAddMessageString() {
		
		int orderid = 1;
		String username = "test";
		String customerUsername = "test2";
		Person mockCustomer = mock(Person.class);
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockCustomer);
		when(mockPerson.getUsername()).thenReturn(username);
		when(mockCustomer.getUsername()).thenReturn(customerUsername);
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testMessageController.goToNewMessagePage(mockMod, mockReq, mockResp, String.valueOf(orderid));
		
		assertEquals("App/add-msg", ret);
	}
	
	@Test
	public void whenGoToMessagePageCalled_ValidCustomer_thenReturnMessageString() {
		
		int orderid = 1;
		String username = "test";
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getOrderMsgs()).thenReturn(mockMessageList);
		when(mockOrder.getPerson()).thenReturn(mockPerson);
		when(mockPerson.getUsername()).thenReturn(username);
		
		String ret = testMessageController.goToMessagePage(mockReq, mockResp, String.valueOf(orderid));
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("order", mockOrder);
		
		assertEquals("App/msg", ret);
	}
	
	@Test
	public void whenGoToMessagePageCalled_Admin_thenReturnMessageString() {
		
		int orderid = 1;
		String username = "test";
		String customerUsername = "test2";
		Person mockCustomer = mock(Person.class);
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getOrderMsgs()).thenReturn(mockMessageList);
		when(mockOrder.getPerson()).thenReturn(mockCustomer);
		when(mockPerson.getUsername()).thenReturn(username);
		when(mockCustomer.getUsername()).thenReturn(customerUsername);
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testMessageController.goToMessagePage(mockReq, mockResp, String.valueOf(orderid));
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockSess);
		inOrder.verify(mockReq).setAttribute("order", mockOrder);
		
		assertEquals("App/msg", ret);
	}
	
	@Test
	public void whenVerifyNewMessagePageCalled_ValidCustomer_thenCreateMessage() {
		
		int orderid = 1;
		String username = "test";
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockPerson);
		when(mockPerson.getUsername()).thenReturn(username);
		when(mockReq.getParameter("orderID")).thenReturn(String.valueOf(orderid));
		
		testMessageController.verifyNewMessage(mockMessage, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockMessageService);
		inOrder.verify(mockMessageService).createMessage(mockMessage);
	}
	
	@Test
	public void whenVerifyNewMessagePageCalled_Admin_thenCreateMessage() {
		
		int orderid = 1;
		String username = "test";
		String customerUsername = "test2";
		Person mockCustomer = mock(Person.class);
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e1) {
			e1.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockCustomer);
		when(mockPerson.getUsername()).thenReturn(username);
		when(mockCustomer.getUsername()).thenReturn(customerUsername);
		when(mockPerson.getAdmin()).thenReturn(1);
		when(mockReq.getParameter("orderID")).thenReturn(String.valueOf(orderid));
		
		testMessageController.verifyNewMessage(mockMessage, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockMessageService);
		inOrder.verify(mockMessageService).createMessage(mockMessage);
	}
	
}
