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
 * 2020.08.14	이재우	기능개선
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

    <meta http-equiv="Cache-Control" content="no-cache"/>
	<meta http-equiv="Expires" content="0"/>
	<meta http-equiv="Pragma" content="no-cache"/>
    <%@include file="/WEB-INF/jsp/include/include.jsp" %>

    <style>
	.todo{
 		font-size:13px; 
		position:relative;
		bottom:1px;
		letter-spacing: 1px;
	}
	</style>        
    <script>
		var customer = "${customer}";
		 
	    $(document).ready(function(){
	    	$(".window").hide();
	    	$(".window-shadow").hide();
	    	//$("div.panel.window.panel-htop").hide();
	//     	$(".tabs-p-tool a").css("font-size","10px");
	//     	$(".tabs-p-tool a").css("margin-top","3px");
	// 		$(".tabs-p-tool a").css("color","black");
				
			//saveWatchLoginUsers();
	    });   
    </script>
</head>
<body style="visibility:hidden;">
<div id="wrapper">
	<!-- 외곽 LAYOUT START -->
	<div id="mainLayout" class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',border:false" style="height:84px;overflow:hidden;">
			<%@include file="/WEB-INF/jsp/include/header.jsp"%>
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

<!-- 		<div id="messageAll" class="easyui-dialog" style="padding:15px;"></div> 
			  해당 기능 : $("#messageAll")은 서버에서 넘겨주는 이벤트massage를 수신하여 우측하단에 알람메시지로 띄워준다.
			문제점 원인 : 로고 이미지를 반복 클릭 할 경우 알수없는 dialog창이 잠시 띄워지고 없어지는 현상 발생.
 			문제점 조치 : 
 					1. 웹 오픈팝은 24시간 띄워서 운영되지 않는다.
 					2. 현재 이 기능은 사용되지 않는다.
 					3. 원인을 해결하기 위해 주석처리하였다.
-->
		</div>
	</div>
	<!-- 외곽 LAYOUT END -->
</div>
<%@include file="/WEB-INF/jsp/include/popup/licenceCheck_popup.jsp" %>
</body>
</html>