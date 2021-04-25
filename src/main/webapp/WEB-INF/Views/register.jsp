<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>

	
<!DOCTYPE html>
<html>
<head>
<title>Register</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<style>
body {font-family: 'Open Sans', Arial, Helvetica, sans-serif;}
form {border: 3px solid #f1f1f1;}

input[type=text], input[type=password], input[type=email] {
  width: 100%;
  padding: 12px 20px;
  margin: 8px 0;
  display: inline-block;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
}

button:hover {
  opacity: 0.8;
}

.cancelbtn {
  width: auto;
  padding: 10px 18px;
  background-color: #f44336;
}

.imgcontainer {
  text-align: center;
  margin: 24px 0 12px 0;
}

img.avatar {
  max-width:15%;
  max-height:15%;
  border-radius: 50%;
}

.container {
  padding: 16px;
}

span.psw {
  float: right;
  padding-top: 16px;
}

/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
  span.psw {
     display: block;
     float: none;
  }
  .cancelbtn {
     width: 100%;
  }
}
</style>
</head>
<body>

<h2>Welcome to Santa Tracker!</h2>
<p>Track your delivery here!</p>

<sf:form action="/verify-register" method="POST" modelAttribute="person">
  <div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please register <c:if test = "${ admin == 1 }">[ADMIN ROLE]</c:if></h3>
  	<c:if test = "${ admin == 1 }"><h3>You will need a current admin to approve your registration by email.</h3></c:if>
  	<c:if test = "${ admin == 0 }"><a href="/register?admin=1">Register as admin</a></c:if>
  	<c:if test = "${ admin == 1 }"><a href="/register">Register as customer</a></c:if>
  	<span style="color:red;">${errMsg}</span> <br>  
    <sf:label path="username"><b>Username</b></sf:label>
    <sf:input type="text" path="username" placeholder="Enter Username" name="uname" value="${uname_reg}" required="required" />
    <sf:label path="pword"><b>Password (at least 5 characters)</b></sf:label>
    <sf:password path="pword" placeholder="Enter Password" name="pword" required="required" />
    
    <label for="repsw"><b>Re-enter Password</b></label>
    <input type="password" placeholder="Re-enter Password" name="repsw" required>
    
    <sf:label path="address"><b>Address</b></sf:label>
    <sf:input type="text" path="address" placeholder="Enter Address" name="addr" value="${addr_reg}" required="required" />
    
    <sf:label path="email"><b>Email</b></sf:label>
    <sf:input type="email" path="email" placeholder="Enter Email" name="email" value="${email_reg}" required="required" />
    
    <button type="submit">Register</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/index">Login</a> | Forgot <a href="/reset">password</a>? | Sign in as a <a href="/demo">demo user</a></span>
  </div>
</sf:form>

</body>
</html>
