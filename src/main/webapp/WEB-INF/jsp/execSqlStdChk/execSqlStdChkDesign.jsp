<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/execSqlStdChkDesign.js?ver=<%=today%>"></script>
	<style>
		.tabs-header{
			background-color: rgb(255, 255, 255);
		}
	</style>
</head>
<body>
<div id="container">
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="execSqlStdChkTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="SQL 표준 점검 결과" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="sqlStandardCheckResult" name="sqlStandardCheckResult" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/execSqlStdChkDesign/sqlStandardCheckResult" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="표준 미준수 SQL" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="nonStandardSql" name="nonStandardSql" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="SQL 표준 준수 현황" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="standardComplianceState" name="standardComplianceState" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="SQL 표준 점검 예외 대상 관리" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="maintainQualityCheckException" name="maintainQualityCheckException" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="스케줄러 관리" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="manageScheduler" name="manageScheduler" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
			</div>
		</div>
	</div>	<!-- contents END -->
</div>	<!-- container END -->
</body>
</html>