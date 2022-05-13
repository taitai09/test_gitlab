<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.14	임호경	최초작성
 **********************************************************/
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
	String today = formatter.format(new java.util.Date());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: Open-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />	
	<link rel="stylesheet" href="/resources/css/common.css" />
	<link rel="stylesheet" href="/resources/css/login.css" />
	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
	<link rel="stylesheet" href="/resources/css/lib/easyui/icon.css" />	
	<link rel="stylesheet" href="/resources/css/reset.css" />
	<link rel="stylesheet" href="/resources/css/customer/<spring:eval expression="@defaultConfig['customer']"/>_style.css?ver=<%=today%>" />
	
	<script type="text/javascript" src="/resources/js/lib/jquery.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>	
	<script type="text/javascript" src="/resources/js/lib/jquery-ui.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jquery.form.js"></script>
	
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	
	
	<script src="/resources/js/common.js"></script>	
	<script src="/resources/js/ui/loginBySSO.js?ver=<%=today%>"></script>
	<script>
		var deploy_id="${deploy_id}";
		var customer='${customer}';
		var sso_yn='${sso_yn}';
		var user_id='${user_id}';
		var isConnectedBySSO='${isConnectedBySSO}';
		var error='${error}';
	</script>
</head>
<body>
	
	<div id="wrapper" style="display:none;">
		<div class="contents">
			<div id="login_contents">
				<h1 class="logo"><img src="/resources/images/<spring:eval expression="@defaultConfig['customer']"/>_login_logo.png" alt="Open-POP"></h1>
				<div class="body">    
					<form:form method="post" name="login_form" id="login_form" class="login_iuput">
						<input type="hidden" id="deploy_id" name="deploy_id" value="${deploy_id}" />
<!-- 						<input type="hidden" id="deploy_id" name="deploy_id" value="AB000001_11501" /> -->
						<input type="hidden" id="userpwd" name="userpwd" />
						<input type="hidden" id="default_auth_grp_cd" name="default_auth_grp_cd" />
						<input type="hidden" id="default_auth_grp_id" name="default_auth_grp_id" />
						<input type="hidden" id="default_wrkjob_cd" name="default_wrkjob_cd" />
						<input type="hidden" id="user_id" name="user_id" />
						<input type="text" id="userid" name="userid" class="ins contrast" placeholder="아이디를 입력하세요">
						<input type="password" id="userpassword" name="userpassword" class="ins contrast" placeholder="비밀번호를 입력하세요" value="<spring:eval expression="@serverConfig['userpassword']"/>">
						<div id="login_button">
							<a href="javascript:;" id="login_submit"><img src="/resources/images/login.gif"/></a>
						</div>
						<div id="bottom">
							<p class="lgn_check"><input type="checkbox" id="id_save" name="id_save" class="redInput">&nbsp;&nbsp;아이디 저장</p>
							<div id="newUser" onClick="showNewUserPopup();">신규 사용자 등록</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- 기본권한선택팝업 -->
	<%@include file="/WEB-INF/jsp/login/defaultLoginRolePop.jsp"%>
	<!-- 기본권한선택팝업 -->
</body>
</html>