<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검 :: SQL 표준 준수 현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/standardComplianceState.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="stdComplicanceState">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			<input type="hidden" name="sql_std_qty_div_cd" value="4">
			<input type="hidden" id="project_id_inherited" value="${project_id}">
			<input type="hidden" id="scheduler_no_inherited" value="${sql_std_qty_scheduler_no}">
			<!-- test용, 삭제예정 -->
			<input type="hidden" id="sql_std_gather_day" name="sql_std_gather_day" value="">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_combo" data-options="editable:false,prompt:'선택'" class="w350 easyui-combobox" required></select>
				<input type="hidden" id="project_id" name="project_id" value="${project_id}">
				
				<label>스케줄러</label>
				<select id="scheduler_no" name="sql_std_qty_scheduler_no" data-options="editable:false,prompt:'선택'" class="w350 easyui-combobox" required></select>
				<label>기준일자</label>
				<input type="text" id="start_day" name="start_day" value="${aMonthAgo}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true">
				<span style="margin: 0px 7px;"> ~ </span>
				<input type="text" id="end_day" name="end_day" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
			</div>
		</form:form>
	
		<div style="width:100%;height:620px;margin-top:10px;">
			<div style="width:40%;height:100%;float:left;">
				<div style="width:95%;height:40%;border:1px solid #ccc;padding:15px;">
					<div id="countChart" style="width: 95%;height: 100%;">
					</div>
				</div>
				
				<div style="width:95%;height:46%;border:1px solid #ccc;padding:15px;margin-top:10px;">
					<div id="ratioChart" style="width: 95%;height: 100%;">
					</div>
				</div>
			</div>
			
			<div style="width:59.3%;height:92.7%;float:left;margin-left:5px;">
				<div style="width:97.3%;height:100%;border:1px solid #ccc;padding:15px;">
					<div id="countByIndexChart" style="width: 95%;height: 100%;">
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>