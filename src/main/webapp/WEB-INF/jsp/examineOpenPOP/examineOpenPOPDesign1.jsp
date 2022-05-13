<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.12.23 initiate 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>Open-POP 점검 설계</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/examineOpenPOP/examineOpenPOPDesign1.js?ver=<%=today%>"></script>
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
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="openpop_arch_ver" name="openpop_arch_ver" value="${openpop_arch_ver}"/>
			</form:form>
			
			<div id="examineOpenPOPDesignTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="Agent 점검" data-options="fit:true" style="padding:5px;min-height:680px;">
					<iframe id="examineOpenPOPDesignIF1" name="examineOpenPOPDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							src="/ExamineOpenPOP/examineAgent1" style="width:100%;min-height:680px;"></iframe>
				</div>
				<div title="스케줄러 점검" data-options="fit:true" style="padding:0px;min-height:680px;">
					<iframe id="examineOpenPOPDesignIF2" name="examineOpenPOPDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
							style="width:100%;min-height:680px;"></iframe>
<!-- 						src="/examineOpenPOP/examineScheduler"  -->
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>