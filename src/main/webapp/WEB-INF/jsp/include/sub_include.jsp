<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*, java.text.*"  %>
<%
 java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
 String today = formatter.format(new java.util.Date());
 //<spring:eval expression="@defaultConfig['version.date']"/>
%>

<%
	String strReferer = request.getHeader("referer");
	if(strReferer != null) {
		System.out.println("////////////////////////////////////////////////");
		System.out.println("strReferer:" + strReferer);
	}
	if(strReferer == null) {
%>
		<script language="javascript">
			alert("URL 주소창에 주소를 직접 입력해서 접근하셨습니다.\n\n정상적인 경로를 통해 다시 접근해 주십시오!");
			document.location.href="/auth/login";
// //  			document.location.href="../";
		</script>
<%
		return;
	}
%>

<sec:authorize access="isAuthenticated()">
	<sec:authentication var="loginId" property="principal.username"></sec:authentication>
	<sec:authentication var="users" property="principal.users"></sec:authentication>
	<sec:authentication var="uuid" property="principal.uuid"></sec:authentication>
</sec:authorize>
<!-- JQuery -->
<!--[if lt IE 9]>
  <script src="/resources/js/html5shiv.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="/resources/css/lib/jquery-ui.css">
<link rel="stylesheet" href="/resources/css/lib/jquery-ui.structure.min.css">
<link rel="stylesheet" href="/resources/css/lib/jquery-ui.theme.min.css">
<link rel="stylesheet" href="/resources/css/common.css?ver=<%=today%>" />
<link rel="stylesheet" href="/resources/css/layout.css?ver=<%=today%>" />
<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
<link rel="stylesheet" href="/resources/css/lib/easyui/color.css" />
<link rel="stylesheet" href="/resources/css/lib/easyui/icon.css" />
<link rel="stylesheet" href="/resources/js/lib/extjs/classic/theme-triton/resources/theme-triton-all.css">
<link rel="stylesheet" href="/resources/css/lib/fontawesome/fontawesome-all.css"/>
<link rel="stylesheet" href="/resources/css/reset.css?ver=<%=today%>" />

<script type="text/javascript" src="/resources/js/lib/jquery.3.4.1.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/clipboard.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/resources/js/lib/datagrid-filter.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery.form.js"></script>
<script type="text/javascript" src="/resources/js/lib/jquery.format.js"></script>
<script type="text/javascript" src="/resources/js/lib/datagrid-export.js"></script>
<script type="text/javascript" src="/resources/js/common.js?ver=<%=today%>"></script>
<script type="text/javascript" src="/resources/js/common_popup.js?ver=<%=today%>"></script>
<script type="text/javascript" src="/resources/js/script.js?ver=<%=today%>"></script>
<script type="text/javascript">
	var frameName;
	var strGb;
</script>


