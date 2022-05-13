<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.05.06 initialize 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 개선 현황 Design</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceImprovementDesign/situation/design.js?ver=<%=today%>"></script>
	<style>
		.tabs-header{
			background-color: rgb(255, 255, 255);
		}
	</style>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			<input type="hidden" id="menu_id" name="menu_id" value="${menu_id}">
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:740px">
			<div id="situationDesignTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="성능개선현황 보고서" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="situationDesignIF1" name="situationDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/PerformanceImprovementDesign/PerformanceImprovementReport" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="성능개선결과 산출물" data-options="fit:true" style="padding:5px;min-height:690px;">
					<iframe id="situationDesignIF2" name="situationDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:690px;"></iframe>
				</div>
				
				<div title="프로그램 유형별 성능개선현황" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="situationDesignIF3" name="situationDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="요청 유형별 성능개선현황" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="situationDesignIF4" name="situationDesignIF4" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="개선 유형별 성능개선현황" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="situationDesignIF5" name="situationDesignIF5" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
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