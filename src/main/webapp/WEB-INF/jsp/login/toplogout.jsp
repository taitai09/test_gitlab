<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: Open-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/include.jsp" %>        
	<script>
	function start(){
		//alert("세션이 종료되어 로그인화면으로 이동합니다.");
		//top.location.href="/auth/login";		
		$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
			setTimeout(function() {
				top.location.href="/auth/login";
			},5000);	
		});		
	}
	</script>	
</head>
<body onLoad="start();">
  
</body>
</html>