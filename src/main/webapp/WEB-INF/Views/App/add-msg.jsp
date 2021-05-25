<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>

	
<!DOCTYPE html>
<html>
<head>
<title>New message</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="../css/app-form.css" rel="stylesheet">
</head>
<body>

<h2>${personLoggedIn.username}, thank you for contacting us!</h2>

<sf:form action="/app/verify-message" method="POST" modelAttribute="message">
  <div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please fill in the details below:</h3>
  	<span style="color:red;">${newMsgErrMsg}</span> <br>  
  	
  	<label for="orderID"><b>Order ID</b></label>
  	<input type="text" id="orderID" name="orderID" value="${message.order.orderID}" readonly="readonly" />
    <sf:label path="title"><b>Title</b></sf:label>
    <sf:input type="text" path="title" placeholder="Enter Title" name="title" required="required" />
    <sf:label path="body"><b>Body</b></sf:label>
    <sf:textarea type="text" path="body" rows="10" cols="80" placeholder="Enter Message Body" name="body" />

    <button type="submit">Send!</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/app/message/${order.orderID}">Back</a></span>
  </div>
</sf:form>

</body>
</html>
