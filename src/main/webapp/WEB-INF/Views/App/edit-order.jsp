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
<link href="../css/app-form.css" rel="stylesheet">
</head>
<body>

<h2>${personLoggedIn.username}, please add/update an order here:</h2>

<sf:form action="/verify-order" method="POST" modelAttribute="order">
  <div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Order ID: ${order.orderID}</h3> <br>
  	<h3>Please fill in the details below:</h3>
  	<span style="color:red;">${editOrderErrMsg}</span> <br>  
    <sf:label path="plannedTime"><b>Planned time</b></sf:label>
    <sf:input type="datetime-local" path="plannedTime" id="plannedTime" name="plannedTime" onchange="setEstimatedTime(this)" required="required" />
    <sf:label path="estimatedTime"><b>Estimated time</b></sf:label>
    <sf:input type="datetime-local" path="estimatedTime" id="estimatedTime" name="estimatedTime" required="required" />
    <sf:label path="location"><b>Location</b></sf:label>
    <sf:input type="text" path="location" placeholder="Enter Location" name="location" required="required" />
	
	<div class="dropdown">
	  <button onclick="myFunction()" class="dropbtn">Dropdown</button>
	  <div id="myDropdown" class="dropdown-content">
	    <a href="#">Link 1</a>
	    <a href="#">Link 2</a>
	    <a href="#">Link 3</a>
	  </div>
	</div>
	
    <button type="submit">Add / Update</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/app/dashboard">Back</a></span>
  </div>
</sf:form>

<script>
	function setEstimatedTime(timeInput) {
	    var estimatedTimeInput = document.getElementById('estimatedTime');
	    estimatedTimeInput.value = timeInput.value;
	}
</script>
</body>
</html>
