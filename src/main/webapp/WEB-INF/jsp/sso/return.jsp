<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./agentInfo.jsp"%>
<%
	String resultCode = session.getAttribute("resultCode") == null ? "" : session.getAttribute("resultCode").toString();
	String resultMessage = session.getAttribute("resultMessage") == null ? "" : session.getAttribute("resultMessage").toString();
	String resultData = session.getAttribute("resultData") == null ? "" : session.getAttribute("resultData").toString();
	boolean isErrorPage = session.getAttribute("isErrorPage") == null? false: (Boolean)session.getAttribute("isErrorPage");
	
	//String logOutUrl == AUTHORIZATION_URL + "logout";
	//String logOutUrlAgentId = logOutUrl = "?agentId=" + agentId ;
	String secureSessionId = session.getAttribute("secureSessionId") == null ? "" : session.getAttribute("secureSessionId").toString();
	String id = session.getAttribute("id") == null ? "" : session.getAttribute("id").toString();
	String path = "token/saveToken.html";
	String sendUrl = AUTHORIZATION_URL + path + "?secureSessionId=" + secureSessionId;
	System.out.println("sendUrl : " + sendUrl);
	System.out.println("isErrorPage : " + isErrorPage);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript" src="/resources/js/lib/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>	
	<script type="text/javascript" src="/resources/js/lib/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery.form.js"></script>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
table {
	border-collapse: collapse;
}

th, td {
border : 1px solid;
}

</style>
</head>
<body>
	<iframe src="<%=sendUrl%>" method="get" style="visibility:hidden;display:none"></iframe>
	<%
		if(isErrorPage == true || !resultCode.equals("000000")){
	%>

	<p>토큰검증이 실패하였습니다.</p>
	<p><%=resultCode%></p>

	<%
		} else {
	%>
		<table>
			<tr>
				<td>사용자ID</td>
				<td><%=id%></td>
			</tr>
			<tr>
				<td>세션ID</td>
				<td><%=secureSessionId%></td>
			</tr>
		</table>
		
	<form method="post" name="submit_form" id="submit_form" >
  		<input type="text" name="user_id" value="<%=id%>">
		<input type="text" name="isConnectedBySSO" value="Y">				
		<input type="text" name="secureSessionId" value="<%=secureSessionId%>">				
	</form>
	
	<%
		}
	%>
</body>
</html>
