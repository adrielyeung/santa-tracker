package com.adriel.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.adriel.entity.Person;

public class Utils {
	
	private Utils() {}
	
	public static String generateRandomAlphanumericString(int length) {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();

	    // 48 - 57: numerals 0 - 9
	    // 65 - 90: alphabets A - Z
	    // 97 - 122: alphabets a - z
	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(length)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	
	public static String getSiteURL(HttpServletRequest req) {
		String url = req.getRequestURL().toString();
		return url.replace(req.getServletPath(), "");
	}
	
	public static String verifyPassword(Person person, HttpServletRequest req) {
		
		String errMsg = "";
		String repword_inp = req.getParameter("repsw");
		HttpSession personCurSess = req.getSession();
		if (!person.getPword().equals(repword_inp)) {
			errMsg = ConstStrings.PWD_NOT_MATCH;
			personCurSess.setAttribute("uname_reg", person.getUsername());
			personCurSess.setAttribute("email_reg", person.getEmail());
			personCurSess.setAttribute("addr_reg", person.getAddress());
		} else if (person.getPword().length() < 5) {
			errMsg = ConstStrings.PWD_LENGTH;
			personCurSess.setAttribute("uname_reg", person.getUsername());
			personCurSess.setAttribute("email_reg", person.getEmail());
			personCurSess.setAttribute("addr_reg", person.getAddress());
		}
		return errMsg;
		
	}
	
	public static boolean isLoggedOut(HttpServletRequest req) {
		return ((Person) req.getSession().getAttribute("personLoggedIn")) == null;
	}
}
