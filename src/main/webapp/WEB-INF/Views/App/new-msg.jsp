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
<style>
body {font-family: 'Open Sans', Arial, Helvetica, sans-serif;}
form {border: 3px solid #f1f1f1;}

input[type=text], input[type=password] {
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

<h2>${uname}, thank you for contacting us!</h2>

<sf:form action="/verify-msg" method="POST" modelAttribute="message">
  <div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please fill in the details below:</h3>
  	<span style="color:red;">${newMsgErrMsg}</span> <br>  
  	<label for="orderID"><b>Order ID</b></label>
  	<input type="text" placeholder="Enter order ID" name="orderID" required="required" />	
    <sf:label path="title"><b>Title</b></sf:label>
    <sf:input type="text" path="title" placeholder="Enter Title" name="title" required="required" />
    <sf:label path="message"><b>Message</b></sf:label>
    <sf:textarea type="text" path="message" rows="10" cols="80" placeholder="Enter Message" name="message" />

    <button type="submit">Send!</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/app/message">Back</a></span>
  </div>
</sf:form>

</body>
</html>
