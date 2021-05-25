package com.adriel.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adriel.utils.Constants;

import static org.mockito.Mockito.*;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class LogoutControllerTest {
	
	@InjectMocks
	LogoutController lCon;
	
	@Mock
	HttpServletRequest mockReq;
	@Mock
	HttpServletResponse mockResp;
	@Mock
	HttpSession mockSession;
	@Mock
	HttpSession mockNewSession;
	
	@Test
	public void whenLogOutCalledWithSession_thenInvalidatesSessionAndForwardsIndexPage() {
		// Arrange
		when(mockReq.getSession(false)).thenReturn(mockSession);
		when(mockReq.getSession()).thenReturn(mockNewSession);
		
		// Act
		lCon.logOut(mockReq, mockResp);
		
		// Assert
		InOrder inOrder = inOrder(mockNewSession, mockSession, mockReq, mockResp);
		inOrder.verify(mockReq).getSession(false);
		inOrder.verify(mockSession).invalidate();
		inOrder.verify(mockReq).getSession();
		inOrder.verify(mockNewSession).setAttribute(Constants.INDEX_ERR, Constants.LOGGED_OUT);
		try {
			inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void whenLogOutCalledWithoutSession_thenForwardsIndexPage() {
		// Arrange
		HttpSession mockSession = null;
		
		when(mockReq.getSession(false)).thenReturn(mockSession);
		when(mockReq.getSession()).thenReturn(mockNewSession);
		
		// Act
		lCon.logOut(mockReq, mockResp);
		
		// Assert
		InOrder inOrder = inOrder(mockReq, mockNewSession, mockResp);
		inOrder.verify(mockReq).getSession(false);
		inOrder.verify(mockReq).getSession();
		inOrder.verify(mockNewSession).setAttribute(Constants.INDEX_ERR, Constants.LOGGED_OUT);
		try {
			inOrder.verify(mockResp).sendRedirect(Constants.INDEX);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
