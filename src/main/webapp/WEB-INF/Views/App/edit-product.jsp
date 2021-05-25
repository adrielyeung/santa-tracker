<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<!DOCTYPE html>
<html>
<head>
<title>${type} product</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="../css/app-form.css" rel="stylesheet">
</head>
<body>

<h2>${personLoggedIn.username}, please ${type} a product here:</h2>

<sf:form action="/app/verify-product" method="POST" modelAttribute="product">
  <div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please fill in the details below:</h3>
  	<span style="color:red;">${addProductErrMsg}</span> <br>
  	<c:if test="${type == 'Edit'}">
	  	<sf:label path="productID"><b>Product ID</b></sf:label>
	    <sf:input type="text" path="productID" id="productID" name="productID" value="${product.productID}" readonly="true" />
    </c:if>
    <sf:label path="name"><b>Product name</b></sf:label>
    <sf:input type="text" path="name" id="name" name="name" value="${product.name}" required="required" />
    <sf:label path="spec"><b>Product specifications</b></sf:label>
    <sf:input type="text" path="spec" id="spec" name="spec" value="${product.spec}" required="required" />
    <sf:label path="unitPrice"><b>Unit price</b></sf:label>
    <sf:input type="number" path="unitPrice" min="1" step="any" name="unitPrice" value="${product.unitPrice}" required="required" />
    
    <button type="submit">${type} product</button>
  </div>

  <div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/app/product">Back</a></span><br>
    <!-- <c:if test="${type == 'Edit'}"><span class="psw"><a href="/app/delete-product/${product.productID}" onclick="return confirm('Are you sure you want to delete this product?');">Delete this product</a></span></c:if> -->
  </div>
</sf:form>
</body>
</html>
