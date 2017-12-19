<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<c:set var="contextPath" value="<%= request.getContextPath()%>"></c:set>
<html>
<style>
.mask{
	border: 2px yellow solid; position:absolute; z-index:4;
	font-size: 30px;
	color: red;
	font-weight: bold;
}

</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="resultDiv">
	<img id="resultImage" src="data:image/jpeg;base64,${file}">
</div>
<button type="button" onclick="location.href='${contextPath}/face'" style="font-wieght: bold; font-size: 30px;">다시 분석하기</button>
</body>
<script>

var obj = ${result};


var table = document.createElement("table");

var number = 0;

obj.forEach(function(ele){
	var tableBody = document.createElement("tbody");
	
	var tr = document.createElement("tr");
	var td1 = document.createElement("td");
	td1.innerHTML = ++number;
	tr.appendChild(td1)
	tableBody.appendChild(tr)
	
	var scores = ele.scores
	Object.keys(scores).forEach(
			function(key){
				var tr = document.createElement("tr");
				var td1 = document.createElement("td");
				td1.innerHTML = key;
				tr.appendChild(td1)
				
				var td2 = document.createElement("td");
				td2.innerHTML = scores[key];
				tr.appendChild(td2);
				tableBody.appendChild(tr);
			})
	table.appendChild(tableBody);
	document.body.appendChild(table);
})

var resImg = document.getElementById("resultImage");

var left = document.getElementById("resultImage").offsetLeft;
var top = document.getElementById("resultImage").offsetTop;


resImg.addEventListener("load", function(){
	
	var maskDiv = document.getElementById("resultDiv")
	var number2 = 0
	
	obj.forEach(function(o){
		var mask = document.createElement("div");
		mask.innerHTML = ++number2;
		mask.className = "mask";
		
		o = o.faceRectangle;
		
		mask.style.left = o.left;
		mask.style.top = o.top;
		mask.style.width = o.width;
		mask.style.height = o.height;
		
		maskDiv.appendChild(mask);
	})
	
})


</script>
<style>
td{
	max-width:100%;
	white-space:nowrap;
}
</style>
</html>