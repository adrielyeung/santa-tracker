package com.adriel.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Redirections {
	
	public static void redirect(HttpServletRequest req, HttpServletResponse resp, String url, String errMsg) {
		req.getSession().setAttribute("errMsg", errMsg);
		try {
			resp.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
