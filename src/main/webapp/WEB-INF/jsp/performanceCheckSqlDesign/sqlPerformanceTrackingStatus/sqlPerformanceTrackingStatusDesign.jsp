<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.08.25	이재우	최초작성.(배포SQL성능변화 추적 copy)
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>실행 SQL 성능 변화 추적 탭 Design</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqlPerformanceTrackingStatus/sqlPerformanceTrackingStatusDesign.js?ver=<%=today%>"></script>
	<style>
		.tabs-header{
			background-color: rgb(255, 255, 255);
		}
	</style>
</head>
<body>
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="sqlPerformanceTraceStatusTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="성능 검증 SQL" data-options="fit:true,tabWidth:100" style="padding:5px;">
					<iframe id="sqlPerformanceTraceStatusDesignIF1" name="sqlPerformanceTraceStatusDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/RunSqlPerformanceChangeTracking/PerformanceVerifySql" style="width:100%;min-height:680px;"></iframe>
				</div>
				<div title="예외 처리 SQL" data-options="fit:true,tabWidth:100" style="padding:5px;">
					<iframe id="sqlPerformanceTraceStatusDesignIF2" name="sqlPerformanceTraceStatusDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				<div title="Charts" data-options="fit:true,tabWidth:100" style="padding:5px;">
					<iframe id="sqlPerformanceTraceStatusDesignIF3" name="sqlPerformanceTraceStatusDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
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