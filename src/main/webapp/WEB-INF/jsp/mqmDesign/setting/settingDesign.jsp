<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.04.29 initialize 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>구조 품질점검 설정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/mqmDesign/setting/settingDesign.js?ver=<%=today%>"></script>
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
			<div id="settingDesignTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="구조 품질점검 지표 관리" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF1" name="settingDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/MqmDesign/Setting/QualityCheckManagement" style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="구조 품질점검 RULE 관리 " data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF2" name="settingDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="업무 분류체계 관리 " data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF3" name="settingDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="엔터티 유형 관리" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF4" name="settingDesignIF4" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="프로젝트 모델 관리 " data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF5" name="settingDesignIF5" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="프로젝트 구조 품질점검 지표/RULE 관리 " data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF6" name="settingDesignIF6" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
				</div>
				
				<div title="모델 엔터티 유형 관리 " data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="settingDesignIF7" name="settingDesignIF7" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
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