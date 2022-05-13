<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.01.29	이원식	V2 화면 최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: OPEN-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/dev/include/include.jsp" %>        
    <script>
    $(document).ready(function(){
    	$(".window").hide();
    	$(".window-shadow").hide();
    	$("div.panel.window.panel-htop").hide();
    });    
    </script>
</head>
<body>
<div id="wrapper">
	<!-- 외곽 LAYOUT START -->
	<div id="mainLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height:84px;overflow:hidden;">
			<%@include file="/WEB-INF/jsp/dev/include/header.jsp"%>
		</div>
		<div id="leftArea" data-options="region:'west',hideExpandTool:true,expandMode:null,hideCollapsedContent:false,collapsedSize:45,collapsedContent:function(){$('#leftImgBar').show(); return $('#leftImgBar');}" title=" " style="width:200px;">
		</div>
		<!-- LEFT IMAGE MENU START -->
		<div id="leftImgBar" style="display:none;"></div>
		<!-- LEFT IMAGE MENU END -->
		<div data-options="region:'center'">
			<div id="mainTab" class="easyui-tabs" data-options="fit:true,border:false"></div>
		</div>
		<div id="footer" data-options="region:'south',border:false" style="height:30px;overflow:hidden;">
			<div id="copyright">Copyright © OPENMADE CONSULTING. All rights reserved.</div>
			<div id="messageAll" class="easyui-dialog" style="padding:15px;"></div>
		</div>
	</div>
	<!-- 외곽 LAYOUT END -->
</div>
</body>
</html>