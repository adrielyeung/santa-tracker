<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>

	
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="css/logout.css" rel="stylesheet">
</head>
<body>

<h2>Welcome back to Santa Tracker!</h2>
<p>Track your delivery here!</p>

<sf:form action="/verify-login" method="POST" modelAttribute="person">
  <div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please login</h3>
  	<span style="color:red;">${indexErrMsg}</span> <br>  	
    <sf:label path="username"><b>Username</b></sf:label>
    <sf:input type="text" path="username" placeholder="Enter Username" name="uname" required="required" />

    <sf:label path="pword"><b>Password</b></sf:label>
    <sf:password path="pword" placeholder="Enter Password" name="pword" required="required" />
        
    <button type="submit">Login</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/register">Register</a> | Forgot <a href="/reset">password</a>? | Sign in as a <a href="/demo">demo user</a></span>
  </div>
</sf:form>

</body>
</html>
