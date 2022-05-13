<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.10.26	이재우	최초작성.
 **********************************************************/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
<head>
	<title>DB간 자동성능비교</title>
	<meta charset="utf-8"/>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/autoPerformanceCompareBetweenDatabaseDesign.js?ver=<%=today%>"></script>
</head>
<body>
<div id="container">
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="autoPerformanceCompareBetweenDatabaseTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="성능 영향도 분석" data-options="fit:true" style="padding-top:5px;">
					<iframe id="performanceCompare2" name="performanceCompare2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/AutoPerformanceCompareBetweenDatabase/PerformanceCompare2" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="성능 영향도 분석 결과" data-options="fit:true" style="padding-top:5px;">
					<iframe id="performanceCompareResult_V2" name="performanceCompareResult_V2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="튜닝 실적" data-options="fit:true" style="padding-top:5px;">
					<iframe id="tuningPerformance" name="tuningPerformance" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="튜닝 SQL 일괄 검증" data-options="fit:true" style="padding-top:5px;">
					<iframe id="tuningSqlBatchVerify" name="tuningSqlBatchVerify" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="운영 SQL 성능 추적" data-options="fit:true" style="padding-top:5px;">
					<iframe id="prodSqlPerfTrack" name="prodSqlPerfTrack" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="SQL점검팩" data-options="fit:true" style="padding-top:5px;">
					<iframe id="sqlCheck" name="sqlCheck" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
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