<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="<%= request.getContextPath()%>"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>emotion API</title>
<style>
	img{
		width : 100%;
		height : auto;
	}
</style>
</head>
<body>

<img id="resultImage" src="data:image/jpeg;base64,${file}">


</body>
</html>