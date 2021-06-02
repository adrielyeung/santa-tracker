package com.adriel.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.adriel.entity.Order;
import com.adriel.entity.OrderDetail;
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
			errMsg = Constants.PWD_NOT_MATCH;
			personCurSess.setAttribute("uname_reg", person.getUsername());
			personCurSess.setAttribute("email_reg", person.getEmail());
			personCurSess.setAttribute("addr_reg", person.getAddress());
		} else if (person.getPword().length() < 5) {
			errMsg = Constants.PWD_LENGTH;
			personCurSess.setAttribute("uname_reg", person.getUsername());
			personCurSess.setAttribute("email_reg", person.getEmail());
			personCurSess.setAttribute("addr_reg", person.getAddress());
		}
		return errMsg;
		
	}
	
	public static boolean checkAccess(Order order, Person person, HttpServletRequest req) {
		
		// Order is requested by the same user
		if (order.getPerson().getUsername().equals(person.getUsername())) {
			return true;
		}
		
		// Admin allowed to view all items
		if (person.getDemo() == 0 && person.getAdmin() == 1) {
			return true;
		}
		
		// Demo admin not allowed to view non-demo items
		if (person.getDemo() == 1 && person.getAdmin() == 1 && order.getDemo() == 1) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isLoggedOut(HttpServletRequest req) {
		return ((Person) req.getSession().getAttribute("personLoggedIn")) == null;
	}
	
	public static int findGreaterMultipleOfTen(int num) {
		if (num % 10 == 0) {
			return num + 10;
		} else {
			return ((int)(num / 10) + 1) * 10;
		}
	}
	
	public static double findTotalCost(List<OrderDetail> orderDetailList) {
		
		double totalCost = 0.0;
		for (OrderDetail orddet : orderDetailList) {
			totalCost += orddet.getProduct().getUnitPrice() * orddet.getQuantity();
		}
		
		return totalCost;
	}
	
	public static <T extends Comparable<T>> List<T> getRecent(List<T> fullList, int numOfRecentItems) {
		Collections.sort(fullList);
		List<T> recentList = new ArrayList<>();
		if (fullList.size() < numOfRecentItems) {
			recentList = fullList;
		} else {
			for (int i = 0; i < numOfRecentItems; i++) {
				recentList.add(fullList.get(i));
			}
		}
		
		return recentList;
	}
	
}
