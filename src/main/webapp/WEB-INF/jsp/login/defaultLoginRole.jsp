<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>로그인 권한 변경</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/login/defaultLoginRole.js"></script>
		<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<script>
	<!-- position:absolute;z-index:1000;background-color:#ffffff; -->
	$(document).ready(function() {
		$("#selectRolePop").css("top","40px");
		$("#selectRolePop").css("left","15px");
	});
	</script>
</head>
<body>
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

			</div>
		</div>
		<!-- 기본권한선택팝업 -->
		<%@include file="/WEB-INF/jsp/login/defaultLoginRolePop.jsp"%>
	</div>
	<!-- contents END -->
</div>
<form:form method="post" id="login_form" name="login_form" class="form-inline">
	<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						
	<input type="hidden" id="pass_decrypt" name="pass_decrypt" value="${pass_decrypt}"/>
	<input type="hidden" id="login_user_id" name="login_user_id" value="${login_user_id}"/>
	<input type="hidden" id="userpassword" name="userpassword" value="${userpassword}"/>
	<input type="hidden" id="userpwd" name="userpwd" value="${userpwd}"/>
	<input type="hidden" id="user_id" name="user_id"/>
	<input type="hidden" id="default_auth_grp_cd" name="default_auth_grp_cd" />
	<input type="hidden" id="default_auth_grp_id" name="default_auth_grp_id" />
	<input type="hidden" id="default_wrkjob_cd" name="default_wrkjob_cd" />
</form:form>
<!-- container END -->
</body>
</html>