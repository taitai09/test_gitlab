<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%  
// String id = "test20190613";
// String id = "dev";
// String id = "dev1";
// response.sendRedirect("/auth/login?user_id="+id+"&isConnectedBySSO=Y");
// response.sendRedirect("/auth/login?user_id="+id+"&isConnectedBySSO=Y");
// String deploy_id = (String) request.getParameter("deploy_id");
// System.out.println("deploy_id:"+deploy_id);
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/resources/js/lib/jquery.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery.form.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SSO</title>

<script>
$(document).ready(function() {
	$("#submit_form").attr("action","/auth/login");
	$("#submit_form").submit();
});
</script>
</head>
<body>
	<form:form method="post" name="submit_form" id="submit_form" >
<%--   		<input type="text" name="user_id" value="<%=request.getAttribute("user_id")%>"> --%>
<!--   		<input type="hidden" name="user_id" value="dbmanager"> -->
<!--   		<input type="hidden" name="user_id" value="deploy1"> -->
  		<input type="hidden" name="user_id" value="opmanager1">
		<input type="hidden" name="isConnectedBySSO" value="Y">				
		<input type="hidden" name="secureSessionId" value="asdfasdfasdf">				
	</form:form>
</body>
</html>

