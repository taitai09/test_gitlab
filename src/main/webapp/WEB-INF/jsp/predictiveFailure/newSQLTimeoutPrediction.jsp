<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.03.20	명성태	OPENPOP V2 고도화 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>신규 SQL 타임아웃 예측 </title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/predictiveFailure/newSQLTimeoutPrediction.js?ver=<%=today%>"></script>
	<script>
		loginUserId = "${loginId}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${newSQLTimeoutPrediction.dbid}"/>
		<input type="hidden" id="sql_id" name="sql_id" value="${newSQLTimeoutPrediction.sql_id}"/>
		<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${newSQLTimeoutPrediction.plan_hash_value}"/>
		<input type="hidden" id="start_first_exec_day" name="start_first_exec_day"/>
		<input type="hidden" id="end_first_exec_day" name="end_first_exec_day"/>

		<input type="hidden" id="temp_timeout_condition" name="temp_timeout_condition" value="${newSQLTimeoutPrediction.timeout_condition}"/>
		<input type="hidden" id="temp_except_yn" name="temp_except_yn" value="${newSQLTimeoutPrediction.except_yn}"/>
		<input type="hidden" id="except_yn" name="except_yn"/>
		
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
<!-- 					<font size="5">화면준비중입니다.</font> -->
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>발생일</label>
					<span id="span_occurrence_day">
						<input type="text" id="start_day" name="start_day" value="${newSQLTimeoutPrediction.start_first_exec_day}" data-options="panelHeight:'auto',editable:false" class="w100 datapicker easyui-datebox" required="true"/> ~
						<input type="text" id="end_day" name="end_day" value="${newSQLTimeoutPrediction.end_first_exec_day}" data-options="panelHeight:'auto',editable:false" class="w100 datapicker easyui-datebox" required="true"/>
					</span>
					<select id="timeout_condition" name="timeout_condition" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
						<option value="0"> 전체 </option>
						<option value="1" selected> 1 주일 후 </option>
						<option value="2"> 2 주일 후 </option>
						<option value="3"> 3 주일 후 </option>
						<option value="4"> 1 개월 후 </option>
						<option value="5"> 2 개월 후  </option>
						<option value="6"> 3 개월 후 </option>
						<option value="7"> 6 개월 후 </option>
						<option value="8"> 12 개월 후 </option>
					</select>
					<select id="except_yn_combo" name="except_yn_combo" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
						<option value="A"> 전체 </option>
						<option value="Y"> 예외 </option>
						<option value="N" selected> 미예외 </option>
					</select>
					<!-- <span class="searchBtn"> -->
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						<a id="btn_update_except_y" href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_UpdateExceptY();"><i class="btnIcon fas fa-plus fa-lg fa-fw"></i> 예측 예외 등록</a>
						<a id="btn_update_except_n" href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_UpdateExceptN();"><i class="btnIcon fas fa-minus fa-lg fa-fw"></i> 예측 예외 해제</a>
					</span>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:205px;">
				<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:100%;padding-top:5px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;height:450px; position:absolute;bottom:0px">
				<div id="sqlTabs" class="easyui-tabs" data-options="plain:true,fit:true,border:false">
					<div title="성능추이" style="padding-top:5px;">
						<div id="chartNewSQLTimeoutPredictionPanel" data-options="title:'',region:'west',split:false,collapsible:false,border:false" style="height:400px;width:calc(100%-10px);">
						</div>
					</div>
					<div title="성능분석" style="padding-top:5px;">
					</div>
				</div>
			</div>
		</div>
	</form:form>
	<form:form method="post" id="update_form" name="update_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${newSQLTimeoutPredictionUpdate.dbid}"/>
		<input type="hidden" id="sql_id" name="sql_id" value="${newSQLTimeoutPredictionUpdate.sql_id}"/>
		<input type="hidden" id="except_yn" name="except_yn" value="${newSQLTimeoutPredictionUpdate.tr_cd}"/>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body> </html>