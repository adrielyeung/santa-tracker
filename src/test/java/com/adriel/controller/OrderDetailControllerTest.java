package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.adriel.entity.Order;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class OrderDetailControllerTest {
	
	@InjectMocks
	OrderDetailController testOrderDetailController;
	
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
	OrderService mockOrderService;
	@Mock
	PersonService mockPersonService;
	@Mock
	List<Order> mockOrderList;
	@Mock
	List<Person> mockPersonList;
	
	@BeforeEach
	public void init() {
		when(mockReq.getSession()).thenReturn(mockSess);
		when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
	}
	
	@Test
	public void whenGoToOrderDetailPageCalled_ValidCustomer_thenReturnAddOrderString() {
		
		int orderid = 1;
		String username = "test";
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockPerson);
		when(mockPerson.getUsername()).thenReturn(username);
		
		String ret = testOrderDetailController.goToOrderDetail(mockReq, mockResp, String.valueOf(orderid));
		
		assertEquals("App/orderdet", ret);
	}
	
	@Test
	public void whenGoToOrderDetailPageCalled_Admin_thenReturnAddOrderString() {
		
		int orderid = 1;
		String username = "test";
		String customerUsername = "test2";
		Person mockCustomer = mock(Person.class);
		try {
			when(mockOrderService.getOrderById(orderid)).thenReturn(mockOrder);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		when(mockOrder.getPerson()).thenReturn(mockCustomer);
		when(mockPerson.getUsername()).thenReturn(username);
		when(mockCustomer.getUsername()).thenReturn(customerUsername);
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testOrderDetailController.goToOrderDetail(mockReq, mockResp, String.valueOf(orderid));
		
		assertEquals("App/orderdet", ret);
	}
}
