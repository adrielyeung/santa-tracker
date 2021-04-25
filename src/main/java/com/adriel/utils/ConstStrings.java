package com.adriel.utils;

public final class ConstStrings {
	
	public static final String APP_NAME = "SantaTracker"; 

	public static final String INDEX = "/index";
	public static final String REGISTER = "/register";
	public static final String RESET = "/reset";
	public static final String RESET_PWD_TOKEN = "/reset-password?token=";
	public static final String ADMIN_TOKEN = "/verify-admin?token=";
	
	public static final String SESSION_EXPIRED = "Session has expired. Please login again.";
	public static final String PERSON_NOT_FOUND_EMAIL = "No user was found with this email %s. Please try again.";
	public static final String ADMIN_NOT_FOUND = "No admin was found within the system. Please contact system support to configure an initial admin.";
	public static final String PERSON_NOT_FOUND_TOKEN_RESET = "An error occurred with this link. Please input your email address to send another reset password email.";
	public static final String PERSON_NOT_FOUND_TOKEN_ADMIN = "An error occurred with this link.";
	public static final String EMAIL_SEND_ERROR = "Email could not be sent. Please retry.";
	public static final String USERNAME_TAKEN = "Username is already taken, please choose another one.";
	public static final String EMAIL_TAKEN = "Email is already used for registration, please choose another one.";
	
	public static final String SUCCESS_REGISTER = "Successful! Please login with your selected username and password.";
	public static final String SUCCESS_RESET = "Successful! Please login with your new password.";
	public static final String SUCCESS_RESET_EMAIL = "Email is sent successfully. Please use the link provided to reset your password.";
	public static final String SUCCESS_ADMIN = "You have approved the request for %s. We will alert them by email.";
	public static final String PENDING_ADMIN_APPROVAL = "We have saved your details but please allow time for a current admin to approve your request. We will alert you via email when you can log in as an admin.";
	
	public static final String EMAIL_RESET_PWD_TITLE = "Reset password for SantaTracker";
	public static final String EMAIL_RESET_PWD_BODY = "<h3>Dear %s,</h3><br>"
			+ "<h3>Please click the below link to reset your password.</h3><br>"
			+ "<a href=\"%s\">Change password</a><br>"
			+ "<p>~SantaTracker</p>"
			+ "---------------------------<br>"
			+ "<i>This email is system-generated from an unmonitored address. Please do not reply to this email.</i><br>"
			+ "<i>Please do not forward this email.</i><br>"
			+ "<i>Please ignore this email if you have not requested to change your password.</i>";
	public static final String EMAIL_ADMIN_TITLE = "New admin registration request for SantaTracker";
	public static final String EMAIL_ADMIN_BODY = "<h3>We have received a new registration for admin role.</h3><br>"
			+ "<h3>Please click the below link to approve this request from:</h3><br>"
			+ "<p><b>Username: </b>%s</p>"
			+ "<p><b>Email: </b>%s</p>"
			+ "<a href=\"%s\">Approve this request</a><br>"
			+ "<p>~SantaTracker</p>"
			+ "---------------------------<br>"
			+ "<i>This email is system-generated from an unmonitored address. Please do not reply to this email.</i><br>"
			+ "<i>Please do not forward this email.</i><br>"
			+ "<i>Please ignore this email if you are not an intended recipient.</i>";
	
	public static final String EMAIL_ADMIN_SUCCESS_REQUESTER_TITLE = "Successful admin registration for SantaTracker";
	public static final String EMAIL_ADMIN_SUCCESS_REQUESTER_BODY = "<h3>Dear %s,</h3><br>"
			+ "<h3>Your admin request has been approved.</h3><br>"
			+ "<h3>Please click the below link to sign in as admin:</h3><br>"
			+ "<a href=\"%s\">Log in to SantaTracker</a><br>"
			+ "<p>~SantaTracker</p>"
			+ "---------------------------<br>"
			+ "<i>This email is system-generated from an unmonitored address. Please do not reply to this email.</i><br>"
			+ "<i>Please do not forward this email.</i><br>"
			+ "<i>Please ignore this email if you are not an intended recipient.</i>";
	
	public static final String EMAIL_ADMIN_SUCCESS_ALERT_ALL_TITLE = "New admin for SantaTracker approved";
	public static final String EMAIL_ADMIN_SUCCESS_ALERT_ALL_BODY = "<h3>We have set up a new admin:</h3><br>"
			+ "<p><b>Username: </b>%s</p>"
			+ "<p><b>Email: </b>%s</p>"
			+ "<p>~SantaTracker</p>"
			+ "---------------------------<br>"
			+ "<i>This email is system-generated from an unmonitored address. Please do not reply to this email.</i><br>"
			+ "<i>Please do not forward this email.</i><br>"
			+ "<i>Please ignore this email if you are not an intended recipient.</i>";
}
