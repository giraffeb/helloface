<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="<%= request.getContextPath()%>"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>updated</p>
	<h1>당신의 감정을 분석해보세요.</h1>
	
	<p>이미지 업로드하기</p>
	<form id="frm" name="frm" 
			action="${contextPath}/faceupload"
			method="post"
			enctype="multipart/form-data" >
		<input id="file" type="file" name="file">
		<input type="button" value="submit" onclick="sizeCheck();">
	</form>
</body>
<script>
function sizeCheck(){
	
	var maxSize = 2097152;
	var size = document.getElementById("file").files[0].size;
	if(size > maxSize){
		alert("can not over Maxsize("+maxSize+")!");
	}else{
		document.getElementById("frm").submit();
	}
	
	console.log(size);	
}

</script>
</html>