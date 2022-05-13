<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.03.26 명성태 Unknown SQL Fault Prediction 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="EUC-KR">
	<title>Unknown SQL 장애 예측</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />    
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/regularSQLFilteringCase_popup.js?ver=<%=today%>"></script> <!-- 정규 SQL 필터링 조건 관리 -->
	<script type="text/javascript" src="/resources/js/ui/predictiveFailure/unknownSQLFaultPrediction.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid"/>
		<input type="hidden" id="inst_id" name="inst_id"/>
		
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
					<a href="javascript:;" class="marginB2 marginL5 w180 easyui-linkbutton" onClick="Btn_ShowRegularSQLFilteringCase();"><i class="btnIcon fas fa-cog fa-lg fa-fw"></i> 정규SQL 필터링 조건 관리</a>
				</div>
				<div class="well">
<!-- 					<font size="5">화면준비중입니다.</font> -->
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
					<label>INSTANCE</label>
					<select id="selectInstance" name="selectInstance" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox"></select>
					<label>분석일</label>
					<span id="span_analysis_day">
							<input type="text" id="start_first_analysis_day" name="start_first_analysis_day" value="${unknownSQLFaultPrediction.start_first_analysis_day}" class="w100 datapicker easyui-datebox" required="true"/> ~
							<input type="text" id="end_first_analysis_day" name="end_first_analysis_day" value="${unknownSQLFaultPrediction.end_first_analysis_day}" class="w100 datapicker easyui-datebox" required="true"/>
					</span>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:250px;min-height:230px">
				<!-- CPU 사용률 -->
				<div data-options="title:'▶  CPU 사용률',region:'west',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartCpuUtilizationPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
				<!-- Elapsed Time Diff -->
				<div data-options="title:'▶  Elapsed Time Diff',region:'center',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartElapsedTimeDiffPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
				<!-- CPU Time Diff -->
				<div data-options="title:'▶  CPU Time Diff',region:'east',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartCpuTimeDiffPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:250px;min-height:230px">
				<!-- Physical Read Diff -->
				<div data-options="title:'▶  Physical Reads Diff',region:'west',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartPhysicalReadsDiffPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
				
				<!-- Logical Read Diff -->
				<div data-options="title:'▶  Logical Reads Diff',region:'center',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartLogicalReadsDiffPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
				
				<!-- sort Diff -->
				<div data-options="title:'▶  Sort Diff',region:'east',collapsible:false,split:false,border:true" style="width:33%;padding:5px;">
					<div id="chartSortDiffPanel" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:170px;">
				<!-- ADHOC TOP SQL -->
				<div data-options="title:'▶  ADHOC TOP SQL',region:'west',split:false,collapsible:false,border:true" style="width:50%;height:100%;padding:5px;">
					<table id="adhocTopSqlGrid" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				
				<!-- ADHOC TOP Module -->
				<div data-options="title:'▶  ADHOC TOP Module',region:'center',split:false,collapsible:false,border:true" style="width:50%;height:100%;padding:5px;">
					<table id="adhocTopModuleGrid" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
	</form:form>
</div>
<%@include file="/WEB-INF/jsp/include/popup/regularSQLFilteringCase_popup.jsp" %> <!-- 성능개선 - 인덱스 요청 팝업 -->
</body>
</html>