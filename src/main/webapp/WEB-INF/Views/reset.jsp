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
  display: block;
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

<h2>Welcome back to Santa Tracker!</h2>
<p>Track your delivery here!</p>

<sf:form action="/verify-reset" method="POST" modelAttribute="person">
  <div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please input your email address here: </h3>
  	<span style="color:red;">${errMsg}</span> <br>  	
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
