<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8" isELIgnored="false"%>
	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Order #${order.orderID} details</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Open+Sans&display=swap" rel="stylesheet">
<link href="../css/app.css" rel="stylesheet">
</head>
<body>
<div class="top">
	<div class="topnav">
	  <a href="/app/dashboard">Home</a>
	  <a href="/app/order">Orders</a>
	  <c:if test='${personLoggedIn.admin == 1}'><a href="/app/product">Products</a></c:if>
	  <a href="/logout">Logout</a>
	  <a>Welcome back, ${personLoggedIn.username}!</a>
	</div>
</div>

<div class="imgcontainer">
    <img src="../image/santa.png" alt="Santa welcomes you!" class="avatar">
    <p style="color:white;">TRACKER</p>
</div>

<div class="content">
  <h2>Order #${order.orderID} details</h2>
  <p>Please find below the most updated details.</p><br>

	<div class="tg-wrap"><table id="tg-R6anf" class="tg">
	  <tr>
		    <th class="tg-0lax">Product #</th>
		    <th class="tg-0lax">Product name</th>
		    <th class="tg-0lax">Specifications</th>
		    <th class="tg-0lax">Unit Price</th>
		    <th class="tg-0lax">Quantity</th>
	  </tr>
	  <c:forEach items="${order.orderDets}" var="orddet">
		  <tr>
		    <td class="tg-0lax">${orddet.product.productID}</td>
		    <td class="tg-0lax">${orddet.product.name}</td>
		    <td class="tg-0lax">${orddet.product.spec}</td>
		    <td class="tg-0lax">${orddet.product.unitPrice}</td>
		    <td class="tg-0lax">${orddet.quantity}</td>
		  </tr>
	  </c:forEach>
	  <tr>
	    <td class="tg-0lax"><b>Total</b></td>
	    <td class="tg-0lax"></td>
	    <td class="tg-0lax"></td>
	    <td class="tg-0lax"><b>${totalCost}</b></td>
	    <td class="tg-0lax"></td>
	  </tr>
	</table></div>
</div>
<script charset="utf-8">var TGSort=window.TGSort||function(n){"use strict";function r(n){return n.length}function t(n,t){if(n)for(var e=0,a=r(n);a>e;++e)t(n[e],e)}function e(n){return n.split("").reverse().join("")}function a(n){var e=n[0];return t(n,function(n){for(;!n.startsWith(e);)e=e.substring(0,r(e)-1)}),r(e)}function o(n,r){return-1!=n.map(r).indexOf(!0)}function u(n,r){return function(t){var e="";return t.replace(n,function(n,t,a){return e=t.replace(r,"")+"."+(a||"").substring(1)}),l(e)}}function i(n){var t=l(n);return!isNaN(t)&&r(""+t)+1>=r(n)?t:NaN}function s(n){var e=[];return t([i,m,g],function(t){var a;r(e)||o(a=n.map(t),isNaN)||(e=a)}),e}function c(n){var t=s(n);if(!r(t)){var o=a(n),u=a(n.map(e)),i=n.map(function(n){return n.substring(o,r(n)-u)});t=s(i)}return t}function f(n){var r=n.map(Date.parse);return o(r,isNaN)?[]:r}function v(n,r){r(n),t(n.childNodes,function(n){v(n,r)})}function d(n){var r,t=[],e=[];return v(n,function(n){var a=n.nodeName;"TR"==a?(r=[],t.push(r),e.push(n)):("TD"==a||"TH"==a)&&r.push(n)}),[t,e]}function p(n){if("TABLE"==n.nodeName){for(var e=d(n),a=e[0],o=e[1],u=r(a),i=u>1&&r(a[0])<r(a[1])?1:0,s=i+1,v=a[i],p=r(v),l=[],m=[],g=[],h=s;u>h;++h){for(var N=0;p>N;++N){r(m)<p&&m.push([]);var T=a[h][N],C=T.textContent||T.innerText||"";m[N].push(C.trim())}g.push(h-s)}var L="tg-sort-asc",E="tg-sort-desc",b=function(){for(var n=0;p>n;++n){var r=v[n].classList;r.remove(L),r.remove(E),l[n]=0}};t(v,function(n,t){l[t]=0;var e=n.classList;e.add("tg-sort-header"),n.addEventListener("click",function(){function n(n,r){var t=d[n],e=d[r];return t>e?a:e>t?-a:a*(n-r)}var a=l[t];b(),a=1==a?-1:+!a,a&&e.add(a>0?L:E),l[t]=a;var i=m[t],v=function(n,r){return a*i[n].localeCompare(i[r])||a*(n-r)},d=c(i);(r(d)||r(d=f(i)))&&(v=n);var p=g.slice();p.sort(v);for(var h=null,N=s;u>N;++N)h=o[N].parentNode,h.removeChild(o[N]);for(var N=s;u>N;++N)h.appendChild(o[s+p[N-s]])})})}}var l=parseFloat,m=u(/^(?:\s*)([+-]?(?:\d+)(?:,\d{3})*)(\.\d*)?$/g,/,/g),g=u(/^(?:\s*)([+-]?(?:\d+)(?:\.\d{3})*)(,\d*)?$/g,/\./g);n.addEventListener("DOMContentLoaded",function(){for(var t=n.getElementsByClassName("tg"),e=0;e<r(t);++e)try{p(t[e])}catch(a){}})}(document);</script>

<div class="footnav">
	<a href="/app/message/${order.orderID}/new" class="btn default">Send a message!</a>
	<a href="/app/message/${order.orderID}">See all messages for this order</a>
	<a href="/app/order" class="btn default">Back</a>
</div>
</body>
</html>
