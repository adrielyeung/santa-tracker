<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>

	
<!DOCTYPE html>
<html>
<head>
<title>Login as demo user</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="css/logout.css" rel="stylesheet">
</head>
<body>

<h2>Welcome back to Santa Tracker!</h2>
<p>Track your delivery here!</p>

<sf:form action="/verify-demo" method="POST" modelAttribute="person">
  <div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please select the demo user role you would like to use:</h3>
  	<span style="color:red;">${demoErrMsg}</span> <br>  	
    <sf:radiobuttons path="username" items="${demoRoleList}" required="true" />
        
    <button type="submit">Confirm</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/index">Login</a> | <a href="/register">Register</a> | Forgot <a href="/reset">password</a>?</span>
  </div>
</sf:form>

</body>
</html>
