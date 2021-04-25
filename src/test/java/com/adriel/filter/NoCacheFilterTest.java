package com.adriel.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.InOrder;

public class NoCacheFilterTest {	
	@Test
	public void whenDoFilterCalled_thenClearCacheAndDoNextFilter() {
		// Arrange
		HttpServletRequest mockHReq = mock(HttpServletRequest.class);
		HttpServletResponse mockHResp = mock(HttpServletResponse.class);
		FilterChain mockFC = mock(FilterChain.class);
		RequestDispatcher mockRD = mock(RequestDispatcher.class);
		HttpSession mockSession = mock(HttpSession.class);
		NoCacheFilter ncf = new NoCacheFilter();
		
		when(mockHReq.getRequestDispatcher("/WEB-INF/Views/index.jsp")).thenReturn(mockRD);
		when(mockHReq.getSession(false)).thenReturn(mockSession);
		
		// Act
		try {
			ncf.doFilter(mockHReq, mockHResp, mockFC);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		// Assert
		InOrder inOrder = inOrder(mockHReq, mockHResp, mockFC, mockRD);
		inOrder.verify(mockHResp).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		inOrder.verify(mockHResp).setDateHeader("Expires", 0);
		try {
			inOrder.verify(mockFC).doFilter(mockHReq, mockHResp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
