<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.09.15	김원재	최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 관리 :: SQL 품질 진단</title>
	<meta charset="utf-8"/>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReportCheckDesign.js?ver=<%=today%>"></script><!-- 수정할 것 -->

</head>
<body>
<div id="container">
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			<input type=hidden id="menu_id" value="${menu_id}">
			<input type=hidden id="menu_nm" value="${menu_nm}">
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="sqlDiagnosisReportCheckTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="SQL 품질 진단 현황" data-options="fit:true" style="padding-top:5px;">
					<iframe id="sqlDiagnosisReportStatus" name="sqlDiagnosisReportStatus" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
			
			
				<div title="SQL 품질 진단 결과" data-options="fit:true" style="padding-top:5px;">
					<iframe id="sqlDiagnosisReport" name="sqlDiagnosisReport" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="스케줄러 관리" data-options="fit:true" style="padding-top:5px;">
					<iframe id="sqlDiagnosisReportmanageScheduler" name="sqlDiagnosisReportmanageScheduler" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>