<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.14	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>테이블 / 인덱스 사용 SQL 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/tableUseSQL_variable.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/paging_variable.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/indexUseSQL_variable.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/paging_variable.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:760px">
			<div id="tableIndexUseTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
				<div title="테이블 사용 SQL 분석" style="padding:5px;">
					<form:form method="post" id="tableUseSQL_form" name="tableUseSQL_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/sqlAnalysis/tableUseSQL_variable.jsp"></jsp:include>
					</form:form>
				</div>
				<div title="인덱스 사용 SQL 분석" style="padding:5px;">
					<form:form method="post" id="indexUseSQL_form" name="indexUseSQL_form" class="form-inline">
						<jsp:include page="/WEB-INF/jsp/sqlAnalysis/indexUseSQL_variable.jsp"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>