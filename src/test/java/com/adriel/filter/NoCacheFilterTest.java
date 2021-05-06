package com.adriel.filter;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NoCacheFilterTest {	
	@Test
	public void whenDoFilterCalled_thenClearCacheAndDoNextFilter() {
		// Arrange
		HttpServletRequest mockHReq = mock(HttpServletRequest.class);
		HttpServletResponse mockHResp = mock(HttpServletResponse.class);
		FilterChain mockFC = mock(FilterChain.class);
		RequestDispatcher mockRD = mock(RequestDispatcher.class);
		NoCacheFilter ncf = new NoCacheFilter();
		
		
		// Act
		try {
			ncf.doFilter(mockHReq, mockHResp, mockFC);
		} catch (IOException | ServletException e) {
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
