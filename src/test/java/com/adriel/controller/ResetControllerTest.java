package com.adriel.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.adriel.entity.Person;
import com.adriel.exception.ResourceNotFoundException;
import com.adriel.service.PersonService;
import com.adriel.utils.Constants;
import com.adriel.utils.EmailSender;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ResetControllerTest {
	
	@InjectMocks
	ResetController testResetController;
	
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSession;
	@Mock
	Model mockModel;
	@Mock
	PersonService mockPersonService;
	@Mock
	Person mockPerson;
	@Mock
	EmailSender mockEmailSender;
	
	@Test
	public void whenGoToResetEmailPageCalled_thenForwardsResetPage() {
		// Act
		String ret = testResetController.goToResetEmailPage(mockModel);
		
		// Assert
		assertEquals("reset", ret);
	}

	@Test
	public void whenGoToResetPasswordPageCalled_thenForwardsResetPasswordPage() {
		// Arrange
		String token = "adfd9fug0u0g08ug";
		try {
			when(mockPersonService.getPersonByResetPasswordToken(token)).thenReturn(mockPerson);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		when(mockReq.getSession()).thenReturn(mockSession);
		
		// Act
		String ret = testResetController.goToResetPasswordPage(mockModel, token, mockReq, mockResp);
		
		// Assert
		InOrder inOrder = inOrder(mockReq, mockResp, mockModel);
		inOrder.verify(mockReq).getSession();
		inOrder.verify(mockModel).addAttribute("person", mockPerson);
		
		assertEquals("reset-password", ret);
	}
	
	@Test
	public void whenGoToResetPasswordPageCalledWithInvalidToken_thenForwardsResetPage() {
		// Arrange
		String token = "adfd9fug0u0g08ug";
		try {
			when(mockPersonService.getPersonByResetPasswordToken(token)).thenThrow(ResourceNotFoundException.class);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		when(mockReq.getSession()).thenReturn(mockSession);
		
		// Act
		testResetController.goToResetPasswordPage(mockModel, token, mockReq, mockResp);
		
		// Assert
		InOrder inOrder = inOrder(mockReq, mockResp, mockModel);
		inOrder.verify(mockReq).getSession();
		try {
			inOrder.verify(mockResp).sendRedirect(Constants.RESET);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void whenVerifyResetCalled_thenSendEmail() {
		
		String email = "email@email.com";
		Person mockPersonFromDb = mock(Person.class);
		Person mockPersonWithToken = mock(Person.class);
		when(mockPerson.getEmail()).thenReturn(email);
		try {
			when(mockPersonService.getPersonByEmail(email)).thenReturn(mockPersonFromDb);
			when(mockPersonService.generateResetPasswordToken(mockPersonFromDb)).thenReturn(mockPersonWithToken);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("localhost:8080/register");
		when(mockReq.getRequestURL()).thenReturn(sb);
		when(mockReq.getServletPath()).thenReturn("localhost:8080");
		when(mockReq.getSession()).thenReturn(mockSession);
		
		testResetController.verifyReset(mockPerson, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockEmailSender);
		try {
			inOrder.verify(mockEmailSender).sendEmail(any(ArrayList.class), any(ArrayList.class), any(ArrayList.class), anyString(), anyString());
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void whenVerifyResetPasswordCalled_ValidPassword_thenReset() {
		
		String psw = "eaijgreajgp0";
		int personResetId = 1;
		Person mockPersonReset = mock(Person.class);
		when(mockPerson.getPword()).thenReturn(psw);
		when(mockPersonReset.getPersonID()).thenReturn(personResetId);
		when(mockReq.getParameter("repsw")).thenReturn(psw);
		when(mockReq.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute("personReset")).thenReturn(mockPersonReset);
		
		testResetController.verifyResetPassword(mockPerson, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockPersonService, mockPersonReset);
		inOrder.verify(mockPersonReset).setPword(psw);
		try {
			inOrder.verify(mockPersonService).updatePerson(personResetId, mockPersonReset);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void whenVerifyResetPasswordCalled_ShortPassword_thenReturnError() {
		
		String psw = "eaij";
		int personResetId = 1;
		Person mockPersonReset = mock(Person.class);
		when(mockPerson.getPword()).thenReturn(psw);
		when(mockReq.getParameter("repsw")).thenReturn(psw);
		when(mockReq.getSession()).thenReturn(mockSession);
		
		testResetController.verifyResetPassword(mockPerson, mockReq, mockResp);
		
		InOrder inOrder = inOrder(mockReq, mockResp, mockPersonService, mockPersonReset);
		inOrder.verify(mockPersonReset, never()).setPword(psw);
		try {
			inOrder.verify(mockPersonService, never()).updatePerson(personResetId, mockPersonReset);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
