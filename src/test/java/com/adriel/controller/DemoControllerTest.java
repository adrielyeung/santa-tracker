package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class DemoControllerTest {
	
	@InjectMocks
	DemoController testDemoController;
	
	@Mock
	Model mockMod;
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSess;
	@Mock
	PersonService mockPersonService;
	@Mock
	Person mockPerson;
	
	@Test
	public void whenGoToDemoPageCalled_thenReturnDemoString() {
		
		String ret = testDemoController.goToDemoPage(mockMod, mockReq);
		
		assertEquals("demo", ret);
	}
	
	@Nested
	class VerifyLoginTest {
		
		@BeforeEach
		public void init() {
			when(mockReq.getSession()).thenReturn(mockSess);
		}
		
		@Test
		public void whenVerifyDemoCalledWithDemoCustomer_thenSetDemoCustomerAndRedirectToDashboard() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			when(mockPerson.getUsername()).thenReturn(Constants.DEMO_CUSTOMER);
			
			try {
				when(mockPersonService.getPersonByUsername(Constants.DEMO_CUSTOMER)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			
			// Act
			testDemoController.verifyDemo(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(Constants.DEMO_CUSTOMER);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute("personLoggedIn", mockPersonCheck);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.DASHBOARD);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyDemoCalledWithDemoAdmin_thenSetDemoCustomerAndRedirectToDashboard() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			when(mockPerson.getUsername()).thenReturn(Constants.DEMO_ADMIN);
			
			try {
				when(mockPersonService.getPersonByUsername(Constants.DEMO_ADMIN)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			
			// Act
			testDemoController.verifyDemo(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(Constants.DEMO_ADMIN);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute("personLoggedIn", mockPersonCheck);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.DASHBOARD);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
