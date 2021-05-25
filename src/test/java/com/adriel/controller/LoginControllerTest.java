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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Order;
import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
	
	@InjectMocks
	LoginController testLoginController;
	
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
	@Mock
	List<Order> mockPersonOrders;
	
	String username = "Hello";
	String pword = "World";
	String wrongPword = "Word";
	
	@Test
	public void whenGoToInputPageCalled_thenReturnInputString() {
		
		String ret = testLoginController.goToIndexPage(mockMod);
		
		assertEquals("index", ret);
	}
	
	@Nested
	class VerifyLoginTest {
		
		@BeforeEach
		public void init() {
			when(mockReq.getSession()).thenReturn(mockSess);
		}
		
		@Test
		public void whenVerifyLoginCalledWithCorrectUsernameAndPassword_thenSetSessionAttributeAndForwardDashboardPage() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			when(mockSess.getAttribute("personLoggedIn")).thenReturn(null);
			when(mockPerson.getUsername()).thenReturn(username);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockReq.getSession()).thenReturn(mockSess);
			try {
				when(mockPersonService.getPersonByUsername(username)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			
			when(mockPersonCheck.getPword()).thenReturn(pword);
			when(mockPersonCheck.getAdmin()).thenReturn(0);
			when(mockPersonCheck.getAdminToken()).thenReturn("");
			
			// Act
			testLoginController.verifyLogin(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(username);
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
		public void whenVerifyLoginCalledWithCorrectUsernameAndPassword_PendingAdminApproval_thenForwardIndexPageWithError() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			when(mockSess.getAttribute("personLoggedIn")).thenReturn(null);
			when(mockPerson.getUsername()).thenReturn(username);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockReq.getSession()).thenReturn(mockSess);
			try {
				when(mockPersonService.getPersonByUsername(username)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			
			when(mockPersonCheck.getPword()).thenReturn(pword);
			when(mockPersonCheck.getAdmin()).thenReturn(0);
			when(mockPersonCheck.getAdminToken()).thenReturn("afggEAERBEfer23221r1r");
			
			// Act
			testLoginController.verifyLogin(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(username);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, Constants.PENDING_ADMIN_APPROVAL_ERR);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyLoginCalledWithIncorrectUsername_thenForwardToIndexWithErrorMessage() {
			// Arrange
			Person mockPersonCheck = null;
			when(mockSess.getAttribute("personLoggedIn")).thenReturn(null);
			when(mockReq.getSession()).thenReturn(mockSess);
			try {
				when(mockPersonService.getPersonByUsername(username)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			when(mockPerson.getUsername()).thenReturn(username);
			
			// Act
			testLoginController.verifyLogin(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(username);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, Constants.INVALID_USERNAME_PWORD);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyLoginCalledWithIncorrectPassword_thenForwardToIndexWithErrorMessage() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			when(mockPerson.getPword()).thenReturn(wrongPword);
			when(mockReq.getSession()).thenReturn(mockSess);
			try {
				when(mockPersonService.getPersonByUsername(username)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			when(mockPerson.getUsername()).thenReturn(username);
			when(mockPersonCheck.getPword()).thenReturn(pword);
			
			// Act
			testLoginController.verifyLogin(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(username);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, Constants.INVALID_USERNAME_PWORD);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyLoginCalledWithPreviousSession_thenFowardDashboardPage() {
			// Arrange
			when(mockSess.getAttribute("personLoggedIn")).thenReturn(mockPerson);
			when(mockReq.getSession()).thenReturn(mockSess);

			// Act
			testLoginController.verifyLogin(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockPerson, mockSess, mockResp);
			inOrder.verify(mockReq).getSession();
			inOrder.verify(mockSess).getAttribute("personLoggedIn");
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.DASHBOARD);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
