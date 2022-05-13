<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.09.25 initialize 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>자동 선정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlPerformanceDesign/autoSelection/autoSelectionDesign.js?ver=<%=today%>"></script>
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
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:730px">
			<div id="autoSelectionDesignTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="자동 선정" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="autoSelectionDesignIF1" name="autoSelectionDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/sqlPerformanceDesign/autoSelection/autoSelection" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="자동 선정 현황(선정회차)" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="autoSelectionDesignIF2" name="autoSelectionDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/sqlPerformanceDesign/autoSelection/autoSelectionStatus"  -->
				</div>
				
				<div title="자동 선정 현황(검색)" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="autoSelectionDesignIF3" name="autoSelectionDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/sqlPerformanceDesign/autoSelection/autoSelectionStatusSearch"  -->
				</div>
				
				<div title="자동 선정 이력" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="autoSelectionDesignIF4" name="autoSelectionDesignIF4" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 					src="/sqlPerformanceDesign/autoSelection/autoSelectionHistory"  -->
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>