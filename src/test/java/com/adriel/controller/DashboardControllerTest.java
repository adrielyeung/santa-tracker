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
import com.adriel.service.MessageService;
import com.adriel.service.OrderService;
import com.adriel.service.PersonService;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {
	
	@InjectMocks
	DashboardController testDashboardController;
	
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
	MessageService mockMessageService;
	@Mock
	List<Order> mockOrderList;
	@Mock
	List<Person> mockPersonList;
	@Mock
	List<Message> mockMessageList;
	
	@BeforeEach
	public void init() {
		when(mockReq.getSession()).thenReturn(mockSess);
		when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
	}
	
	@Test
	public void whenGoToDashboardCalled_NonAdmin_thenReturnDashboardStringWithOrderList() {
		
		int personid = 1;
		when(mockPerson.getAdmin()).thenReturn(0);
		when(mockPerson.getPersonOrders()).thenReturn(mockOrderList);
		when(mockPerson.getPersonID()).thenReturn(personid);
		
		when(mockMessageService.getMessagesByPersonId(personid)).thenReturn(mockMessageList);
		
		String ret = testDashboardController.goToDashboardAfterLogin(mockReq, mockResp);
		
		assertEquals("App/dashboard", ret);
	}
	
	@Test
	public void whenGoToDashboardCalled_Admin_thenReturnDashboardStringWithoutOrderList() {
		
		when(mockPerson.getAdmin()).thenReturn(1);
		
		String ret = testDashboardController.goToDashboardAfterLogin(mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockSess);
		inOrder.verify(mockSess, never()).setAttribute("personOrdLatest", mockOrderList);
		assertEquals("App/dashboard", ret);
	}
	
}
