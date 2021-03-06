<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>

	
<!DOCTYPE html>
<html>
<head>
<title>Reset password</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="css/logout.css" rel="stylesheet">
</head>
<body>

<h2>Welcome back to Santa Tracker!</h2>
<p>Track your delivery here!</p>

<sf:form action="/verify-reset-email" method="POST" modelAttribute="person">
  <div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please input your email address here: </h3>
  	<span style="color:red;">${resetErrMsg}</span> <br>  	
    <sf:label path="email"><b>Email</b></sf:label>
    <sf:input type="email" path="email" placeholder="Enter Email" name="email" value="${email_reg}" required="required" />
        
    <button type="submit">Send reset password email</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/index">Login</a> | <a href="/register">Register</a> | Sign in as a <a href="/demo">demo user</a></span>
  </div>
</sf:form>

</body>
</html>
