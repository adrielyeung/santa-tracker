<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
	
<!DOCTYPE html>
<html>
<head>
<title>${errPageTitle}</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="css/logout.css" rel="stylesheet">
</head>
<body>

<h2>Welcome to Santa Tracker!</h2>
<h3>${errPageMsg}</h3>

<div class="imgcontainer">
    <img src="image/santa.png" alt="Santa welcomes you!" class="avatar">
</div>

<div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/index">Return to login page</a></span>
</div>

</body>
</html>
