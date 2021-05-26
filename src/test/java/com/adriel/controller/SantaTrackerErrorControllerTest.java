package com.adriel.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.adriel.utils.Constants;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class SantaTrackerErrorControllerTest {
	
	@InjectMocks
	SantaTrackerErrorController testErrorController;
	
	@Mock
	HttpServletRequest mockReq;
	
	@Test
	public void whenGetErrorPathCalled_thenReturnErrorPath() {
		
		String ret = testErrorController.getErrorPath();
		assertEquals(Constants.ERROR, ret);
		
	}
	
	@Test
	public void whenGoToErrorPageCalled_Forbidden_thenReturnErrorString() {
		
		when(mockReq.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.FORBIDDEN.value());
		
		String ret = testErrorController.goToErrorPage(mockReq);
		
		InOrder inOrder = inOrder(mockReq);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_TITLE, HttpStatus.FORBIDDEN.value() + Constants.FORBIDDEN_TITLE);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_MSG, Constants.FORBIDDEN_MESSAGE);
		
		assertEquals("Error/error", ret);
		
	}
	
	@Test
	public void whenGoToErrorPageCalled_NotFound_thenReturnErrorString() {
		
		when(mockReq.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.NOT_FOUND.value());
		
		String ret = testErrorController.goToErrorPage(mockReq);
		
		InOrder inOrder = inOrder(mockReq);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_TITLE, HttpStatus.NOT_FOUND.value() + Constants.PAGE_NOT_FOUND_TITLE);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_MSG, Constants.PAGE_NOT_FOUND_MESSAGE);
		
		assertEquals("Error/error", ret);
		
	}
	
	@Test
	public void whenGoToErrorPageCalled_InternalServerError_thenReturnErrorString() {
		
		when(mockReq.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		String ret = testErrorController.goToErrorPage(mockReq);
		
		InOrder inOrder = inOrder(mockReq);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_TITLE, HttpStatus.INTERNAL_SERVER_ERROR.value() + Constants.INTERNAL_SERVER_ERROR_TITLE);
		inOrder.verify(mockReq).setAttribute(Constants.ERROR_PAGE_MSG, Constants.INTERNAL_SERVER_ERROR_MESSAGE);
		
		assertEquals("Error/error", ret);
		
	}
	
}
