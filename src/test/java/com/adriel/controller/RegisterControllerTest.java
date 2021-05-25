package com.adriel.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;
import com.adriel.utils.EmailSender;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {
	
	@InjectMocks
	RegisterController testRegisterController;
	
	@Mock
	Model mockMod;
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSess;
	@Mock
	Person mockPerson;
	@Mock
	PersonService mockPersonService;
	@Mock
	EmailSender mockEmailSender;
	
	@Test
	public void whenGoToRegisterPageCalled_NonAdmin_thenReturnRegisterString() {
		
		String admin = "0";
		
		String ret = testRegisterController.goToRegisterPage(mockMod, admin, mockReq, mockResp);
		
		assertEquals("register", ret);
	}
	
	@Nested
	class VerifyRegisterTest {
		
		@BeforeEach
		public void init() {
			when(mockReq.getSession()).thenReturn(mockSess);
		}
		
		@Test
		public void whenGoToRegisterPageCalled_Admin_thenReturnRegisterString() {
			
			String admin = "1";
			when(mockReq.getSession()).thenReturn(mockSess);
			
			String ret = testRegisterController.goToRegisterPage(mockMod, admin, mockReq, mockResp);
			
			assertEquals("register", ret);
		}
		
		@Test
		public void whenGoToRegisterPageCalled_Error_thenReturnRegisterStringOfNonAdmin() {
			
			String admin = "a";
			when(mockReq.getSession()).thenReturn(mockSess);
			
			String ret = testRegisterController.goToRegisterPage(mockMod, admin, mockReq, mockResp);
			
			assertEquals("register", ret);
		}
		
		@Test
		public void whenVerifyRegisterCalledWithValidUsernameAndPasswordMatches_thenAddToDBAndForwardIndexPage() {
			// Arrange
			String uname = "Hello";
			String pword = "World";
			String repword = "World";
			String email = "abc@def.com";
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				when(mockPersonService.getPersonByEmail(email)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			when(mockSess.getAttribute("admin")).thenReturn(0);
			when(mockReq.getParameter("repsw")).thenReturn(repword);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockPerson.getEmail()).thenReturn(email);
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockReq).getParameter("repsw");
			inOrder.verify(mockPersonService).createPerson(mockPerson);
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, Constants.SUCCESS_REGISTER);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyRegisterCalledWithValidUsernameAndPasswordMatches_ForAdmin_thenAddToDBAndSendEmailAndForwardIndexPage() {
			// Arrange
			String uname = "Hello";
			String pword = "World";
			String repword = "World";
			String email = "abc@def.com";
			String adminToken = "12re3h30f2u3";
			Person currentAdmin = new Person("Admin", "Admin", "admin@admin.com", "Admin Road", 1);
			List<Person> currentAdminList = new ArrayList<>();
			currentAdminList.add(currentAdmin);
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				when(mockPersonService.getPersonByEmail(email)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			when(mockSess.getAttribute("admin")).thenReturn(1);
			when(mockReq.getParameter("repsw")).thenReturn(repword);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockPerson.getEmail()).thenReturn(email);
			when(mockPerson.getAdminToken()).thenReturn(adminToken);
			try {
				when(mockPersonService.getAllAdminPersons()).thenReturn(currentAdminList);
				when(mockPersonService.generateAdminApproveToken(mockPerson)).thenReturn(mockPerson);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			StringBuffer sb = new StringBuffer();
			sb.append("localhost:8080/register");
			when(mockReq.getRequestURL()).thenReturn(sb);
			when(mockReq.getServletPath()).thenReturn("localhost:8080");
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess, mockEmailSender);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockReq).getParameter("repsw");
			inOrder.verify(mockPersonService).generateAdminApproveToken(mockPerson);
			try {
				inOrder.verify(mockEmailSender).sendEmail(any(List.class), any(List.class), any(List.class), anyString(), anyString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (MessagingException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, Constants.PENDING_ADMIN_APPROVAL);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyRegisterCalledWithUsernameIdenticalToAnotherUserButPasswordMatches_thenForwardRegisterPageWithCorrectErrorMessage() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			String uname = "Hello";
			String email = "abc@def.com";
			String addr = "123 Abc Road";
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			when(mockPerson.getEmail()).thenReturn(email);
			when(mockPerson.getAddress()).thenReturn(addr);
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute("email_reg", email);
			inOrder.verify(mockSess).setAttribute("addr_reg", addr);
			inOrder.verify(mockSess).setAttribute(Constants.REGISTER_ERR, Constants.USERNAME_TAKEN);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.REGISTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyRegisterCalledWithEmailIdenticalToAnotherUserButPasswordMatches_thenForwardRegisterPageWithCorrectErrorMessage() {
			// Arrange
			Person mockPersonCheck = mock(Person.class);
			String uname = "Hello";
			String email = "abc@def.com";
			String addr = "123 Abc Road";
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				when(mockPersonService.getPersonByEmail(email)).thenReturn(mockPersonCheck);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			when(mockPerson.getEmail()).thenReturn(email);
			when(mockPerson.getAddress()).thenReturn(addr);
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute("uname_reg", uname);
			inOrder.verify(mockSess).setAttribute("addr_reg", addr);
			inOrder.verify(mockSess).setAttribute("email_reg", "");
			inOrder.verify(mockSess).setAttribute(Constants.REGISTER_ERR, Constants.EMAIL_TAKEN);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.REGISTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyRegisterCalledWithValidUsernameButPasswordDoesNotMatch_thenForwardRegisterPageWithCorrectErrorMessage() {
			// Arrange
			String uname = "Hello";
			String pword = "World1";
			String repword = "World";
			String email = "abc@def.com";
			String addr = "123 Abc Road";
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				when(mockPersonService.getPersonByEmail(email)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			when(mockReq.getParameter("repsw")).thenReturn(repword);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockPerson.getEmail()).thenReturn(email);
			when(mockPerson.getAddress()).thenReturn(addr);
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockReq).getParameter("repsw");
			inOrder.verify(mockSess).setAttribute("uname_reg", uname);
			inOrder.verify(mockSess).setAttribute("email_reg", email);
			inOrder.verify(mockSess).setAttribute("addr_reg", addr);
			inOrder.verify(mockSess).setAttribute(Constants.REGISTER_ERR, Constants.PWD_NOT_MATCH);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.REGISTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyRegisterCalledWithShortPasswordButMatches_thenForwardRegisterPageWithCorrectErrorMessage() {
			// Arrange
			String uname = "Hello";
			String pword = "Wor";
			String repword = "Wor";
			String email = "abc@def.com";
			String addr = "123 Abc Road";
			
			when(mockPerson.getUsername()).thenReturn(uname);
			try {
				when(mockPersonService.getPersonByUsername(uname)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				when(mockPersonService.getPersonByEmail(email)).thenThrow(ResourceNotFoundException.class);
			} catch (ResourceNotFoundException e2) {
				e2.printStackTrace();
			}
			when(mockReq.getParameter("repsw")).thenReturn(repword);
			when(mockPerson.getPword()).thenReturn(pword);
			when(mockPerson.getEmail()).thenReturn(email);
			when(mockPerson.getAddress()).thenReturn(addr);
			
			// Act
			testRegisterController.verifyRegister(mockPerson, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess);
			try {
				inOrder.verify(mockPersonService).getPersonByUsername(uname);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockReq).getParameter("repsw");
			inOrder.verify(mockSess).setAttribute("uname_reg", uname);
			inOrder.verify(mockSess).setAttribute("email_reg", email);
			inOrder.verify(mockSess).setAttribute("addr_reg", addr);
			inOrder.verify(mockSess).setAttribute(Constants.REGISTER_ERR, Constants.PWD_LENGTH);
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.REGISTER);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Test
		public void whenVerifyAdmin_thenUpdateDBAndForwardIndexPage() {
			// Arrange
			String token = "aaagfdgagage";
			Person person = new Person("Admin", "Admin", "admin@admin.com", "Admin Road", 0);
			person.setAdminToken(token);
			
			try {
				when(mockPersonService.getPersonByAdminToken(token)).thenReturn(person);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			StringBuffer sb = new StringBuffer();
			sb.append("localhost:8080/register");
			when(mockReq.getRequestURL()).thenReturn(sb);
			when(mockReq.getServletPath()).thenReturn("localhost:8080");
			
			// Act
			testRegisterController.verifyAdmin(token, mockReq, mockResp);
			
			// Assert
			InOrder inOrder = inOrder(mockReq, mockPersonService, mockResp, mockSess, mockEmailSender);
			try {
				inOrder.verify(mockPersonService).getPersonByAdminToken(token);
			} catch (ResourceNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				inOrder.verify(mockEmailSender).sendEmail(any(List.class), any(List.class), any(List.class), anyString(), anyString());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (MessagingException e1) {
				e1.printStackTrace();
			}
			inOrder.verify(mockSess).setAttribute(Constants.INDEX_ERR, String.format(Constants.SUCCESS_ADMIN, "Admin"));
			try {
				inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
