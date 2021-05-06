package com.adriel.utils;

public final class ConstStrings {
	
	public static final String APP_NAME = "SantaTracker"; 
	
	// Paths
	public static final String INDEX = "/index";
	public static final String REGISTER = "/register";
	public static final String RESET = "/reset";
	public static final String RESET_PWD = "/reset-password";
	public static final String LOGOUT = "/logout";
	public static final String DASHBOARD = "/app/dashboard";
	public static final String RESET_PWD_TOKEN = "/reset-password?token=";
	public static final String ADMIN_TOKEN = "/verify-admin?token=";
	public static final String VERIFY_LOGIN = "/verify-login";
	public static final String VERIFY_REGISTER = "/verify-register";
	public static final String VERIFY_RESET_EMAIL = "/verify-reset-email";
	public static final String VERIFY_RESET_PWD = "/verify-reset-password";
	public static final String VERIFY_ADMIN = "/verify-admin";
	public static final String VERIFY_PRODUCT = "/app/verify-product";
	public static final String MESSAGE_DETAIL = "/app/msgdet/{msgid}";
	public static final String ORDER_DETAIL = "/app/orderdet/{orderid}";
	public static final String ADD_ORDER = "/app/add-order";
	public static final String EDIT_ORDER = "/app/edit-order";
	public static final String ADD_PRODUCT = "/app/add-product";
	public static final String EDIT_PRODUCT = "/app/edit-product/{productid}";
	public static final String DELETE_PRODUCT = "/app/delete-product/{productid}";
	public static final String PRODUCT = "/app/product";
	
	// Error attribute names
	public static final String INDEX_ERR = "indexErrMsg";
	public static final String RESET_ERR = "resetErrMsg";
	public static final String RESET_PWD_ERR = "resetPwdErrMsg";
	public static final String PRODUCT_ERR = "productErrMsg";
	public static final String ADD_PRODUCT_ERR = "addProductErrMsg";
	public static final String REGISTER_ERR = "registerErrMsg";
	
	// Errors
	public static final String SESSION_EXPIRED = "Session has expired. Please login again.";
	public static final String PERSON_NOT_FOUND_EMAIL = "No user was found with this email %s. Please try again.";
	public static final String ADMIN_NOT_FOUND = "No admin was found within the system. Please contact system support to configure an initial admin.";
	public static final String PERSON_NOT_FOUND_TOKEN_RESET = "An error occurred with this link. Please input your email address to send another reset password email.";
	public static final String PERSON_NOT_FOUND_TOKEN_ADMIN = "An error occurred with this link.";
	public static final String EMAIL_SEND_ERROR = "Email could not be sent. Please retry.";
	public static final String USERNAME_TAKEN = "Username is already taken, please choose another one.";
	public static final String EMAIL_TAKEN = "Email is already used for registration, please choose another one.";
	public static final String PWD_NOT_MATCH = "The two passwords entered do not match.";
	public static final String PWD_LENGTH = "Password must be 5 characters or above.";
	public static final String INVALID_USERNAME_PWORD = "Invalid username or password.";
	public static final String PENDING_ADMIN_APPROVAL_ERR = "Please wait for an admin to approve your admin request before we set up your account.";
	public static final String CREATE_ENTITY_ERR = "There was a problem creating your %s. Please retry.";
	public static final String READ_ENTITY_ERR = "There was a problem getting your %s. Please retry.";
	public static final String DELETE_ENTITY_ERR = "There was a problem deleting your %s. Please retry.";
	
	// Message
	public static final String SUCCESS_REGISTER = "Successful! Please login with your selected username and password.";
	public static final String SUCCESS_RESET = "Successful! Please login with your new password.";
	public static final String SUCCESS_RESET_EMAIL = "Email is sent successfully. Please use the link provided to reset your password.";
	public static final String SUCCESS_ADMIN = "You have approved the request for %s. We will alert them by email.";
	public static final String PENDING_ADMIN_APPROVAL = "We have saved your details but please allow time for a current admin to approve your request. We will alert you via email when you can log in as an admin.";
	public static final String LOGGED_OUT = "Successfully logged out! Thank you for using Santa Tracker.";
	public static final String SUCCESS_CREATE_ENTITY = "Your new %s is successfully created.";
	public static final String SUCCESS_EDIT_ENTITY = "Your %s is successfully edited.";
	public static final String SUCCESS_DELETE_ENTITY = "Your %s is successfully deleted.";
	
	// Email
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
