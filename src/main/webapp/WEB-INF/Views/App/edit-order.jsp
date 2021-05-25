<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="s"		uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" 		uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	
<!DOCTYPE html>
<html>
<head>
<title>${type} order</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="../css/app-form.css" rel="stylesheet">
</head>
<body>

<h2>${personLoggedIn.username}, please ${type} an order here:</h2>

<sf:form action="/app/verify-order/${type}" method="POST" modelAttribute="order">
  <div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
  </div>

  <div class="container">
  	<h3>Please fill in the details below:</h3>
  	<span style="color:red;">${editOrderErrMsg}</span> <br>
  	<c:if test="${type == 'Edit' || type == 'Schedule'}">
	  	<sf:label path="orderID"><b>Order ID</b></sf:label>
		<sf:input type="text" path="orderID" id="orderID" name="orderID" value="${order.orderID}" readonly="true" />
  	</c:if>
  	<c:if test="${type == 'Edit'}">
  		<p><b>Order total:</b> $${totalCost}</p>
  	</c:if>
  	
  	<c:if test="${type == 'Schedule'}">
	  	<sf:label path="personID"><b>Customer's ID</b></sf:label>
	    <sf:input type="text" path="personID" id="personID" name="personID" value="${order.person.personID}" readonly="true" />
	    <sf:label path="plannedTime"><b>Planned time</b></sf:label>
	    <sf:input type="datetime-local" path="plannedTime" id="plannedTime" name="plannedTime" onchange="setEstimatedTime(this)" required="required" />
	    <sf:label path="estimatedTime"><b>Estimated time</b></sf:label>
	    <sf:input type="datetime-local" path="estimatedTime" id="estimatedTime" name="estimatedTime" required="required" />
	    <sf:label path="location"><b>Location</b></sf:label>
	    <sf:input type="text" path="location" placeholder="Enter Location" name="location" required="required" />
    </c:if>
    
    <c:if test="${type == 'Add' || type == 'Edit'}">
    	<p>If you would like to submit more products in the order, please fill in and hit '${type} order' first. We will add more space for you.</p>
	    <div class="tg-wrap"><table id="tg-R6anf" class="tg">
	    <thead>
			<tr>
				<th class="tg-0lax">#</th>
				<th class="tg-0lax">Product</th>
				<!-- <th class="tg-0lax">Unit price</th> -->
				<th class="tg-0lax">Quantity</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${order.orderDets}" var="orderDet" varStatus="status">
	    		<tr>
					<td align="center" class="tg-0lax">${status.count}</td>
					<td class="tg-0lax"><sf:select path="orderDets[${status.index}].product" items="${productList}" itemLabel="nameAndUnitPrice" itemValue="productID" /></td>
					<!-- <td class="tg-0lax"><p id="productUnitPrice" /></td> -->
					<!-- <input name="orderDets[${status.index}].product.name" value="${orderDet.product.name}"/> -->
					<td class="tg-0lax"><sf:input type="number" path="orderDets[${status.index}].quantity" value="${orderdet.quantity}"/></td>
				</tr>
	    	</c:forEach>
	    </tbody>
		</table></div>
	</c:if>
    <button type="submit">${type} order</button>
  </div>
</sf:form>

<div class="container" style="background-color:#f1f1f1">
    <span class="psw"><a href="/app/order">Back</a></span>
    <!-- <c:if test="${type == 'Edit'}"><span class="psw"><a href="/app/delete-order/${order.orderID}" onclick="return confirm('Are you sure you want to delete this order?');">Delete this order</a></span></c:if> -->
</div>

<script>
	function setEstimatedTime(timeInput) {
	    var estimatedTimeInput = document.getElementById('estimatedTime');
	    estimatedTimeInput.value = timeInput.value;
	}
	
	function getUnitPrice(product) {
		var productUnitPrice = document.getElementById('productUnitPrice');
		productUnitPrice.innerText = product.unitPrice;
	}
</script>
</body>
</html>
