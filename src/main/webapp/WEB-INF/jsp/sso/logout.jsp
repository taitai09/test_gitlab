<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./agentInfo.jsp"%>
<%
	session.invalidate();
	String sendUrl = AUTHORIZATION_URL + "logout.html";
	System.out.println("logout");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Pragma" content = "no-cache">
	</head>
<body>
	<form name="sendForm" method="post">
		<input type="hidden" name="agentId" value="<%=agentId%>"/>
	</form>
<script>
	var sendUrl = "<%=sendUrl%>";
	var sendForm = document.sendForm;
	sendForm.action = sendUrl;
	sendForm.submit();
</script>
</body>
</html>