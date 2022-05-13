<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능분석 :: SQL 분석 :: ASH 성능분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
    <script type="text/javascript" src="/resources/js/ui/sqlAnalysis/ashPerformance.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="selChart" name="selChart" value="N"/>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="inst_id" name="inst_id"/>
					<input type="hidden" id="partition_key" name="partition_key" value=""/>							
					<input type="hidden" id="partition_start_key" name="partition_start_key"/>
					<input type="hidden" id="partition_end_key" name="partition_end_key"/>
					<input type="hidden" id="real_search" name="real_search" value="Y"/>
					<input type="hidden" id="sample_start_time" name="sample_start_time"/>
					<input type="hidden" id="sample_end_time" name="sample_end_time"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="50"/>
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="sample_id" name="sample_id" value=""/>							
					<input type="hidden" id="session_id" name="session_id" value=""/>
					<input type="hidden" id="strGubun" name="strGubun" value="FIRST"/>

					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w90 easyui-combobox" required="true"></select>
						<label>INST_ID</label>
						<select id="selectInstance" name="selectInstance" data-options="panelHeight:'auto',editable:false" class="w70 easyui-combobox" required="true"></select>
						<label>SID</label>
						<input type="text" id="sid" name="sid" value="" class="w90 easyui-textbox"/>
						<label>SERIAL#</label>
						<input type="text" id="serial" name="serial" value="" class="w90 easyui-textbox"/>
						<label>SQL_ID</label>
						<input type="text" id="sql_id" name="sql_id" value="" class="w100 easyui-textbox"/>								
						<label>MODULE</label>
						<input type="text" id="module" name="module" value="" class="w90 easyui-textbox"/>
						<label>EVENT</label>
						<input type="text" id="event" name="event" value="" class="w90 easyui-textbox"/>

					</div>
					<div class="multi">
						<label>SAMPLE_TIME</label>
						<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" class="w100 datapicker easyui-datebox"/>
						<input type="text" id="strStartTime" name="strStartTime" value="${startTime}" class="w80 datatime easyui-timespinner"/> ~
						<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" class="w100 datapicker easyui-datebox"/>
						<input type="text" id="strEndTime" name="strEndTime" value="${nowTime}" class="w80 datatime easyui-timespinner"/>					
						<label>Realtime</label>
						<input type="checkbox" id="chkRealSearch" name="chkRealSearch" value="" class="w100 easyui-switchbutton"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>					
					</div>
					<div class="searchBtn multiBtn">
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_DownClick('1');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀 [AllSession]</a>
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_DownClick('2');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀 [TOPSQL]</a>
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_DownClick('3');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀 [TOPSession]</a>
					</div>
				</form:form>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="inst_id" name="inst_id"/>							
					<input type="hidden" id="sid" name="sid"/>
					<input type="hidden" id="serial" name="serial"/>
					<input type="hidden" id="sql_id" name="sql_id"/>
					<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
					<input type="hidden" id="module" name="module"/>
					<input type="hidden" id="event" name="event"/>
					<input type="hidden" id="partition_key" name="partition_key"/>
					<input type="hidden" id="partition_start_key" name="partition_start_key"/>
					<input type="hidden" id="partition_end_key" name="partition_end_key"/>
					<input type="hidden" id="real_search" name="real_search"/>
					<input type="hidden" id="sample_start_time" name="sample_start_time"/>
					<input type="hidden" id="sample_end_time" name="sample_end_time"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="50"/>
					<input type="hidden" id="currentPage" name="currentPage"/>
					<input type="hidden" id="sample_id" name="sample_id"/>
					<input type="hidden" id="session_id" name="session_id"/>
					<input type="hidden" id="strGubun" name="strGubun" value="NEXT"/>
				</form:form>
			</div>
		</div>
		<div id="chartTab" class="easyui-tabs" data-options="border:false" style="width:100%;padding-bottom:10px;height:630px;">
			<div title="Wait Class" style="padding:5px;">
				<div id="waitChart" style="width:100%;height:100%">
					<div id="waitChartInfo" style="padding-left:50px;padding-top:90px;text-align:center;"><b>검색된 조건으로 Wait Class Chart가 생성됩니다.</b></div>
				</div>							
			</div>
			<div title="Top Wait Event" style="padding:5px;">
				<div id="eventChart" style="width:100%;height:100%">
					<div id="topWaitChartInfo" style="padding-left:50px;padding-top:90px;text-align:center;"><b>검색된 조건으로 Top Wait Event Chart가 생성됩니다.</b></div>
				</div>							
			</div>
			<div title="All Session" style="padding:5px;">
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:530px">
					<table id="allSessionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:'true'" onClick="Btn_NextSearch();"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
			<div title="TOP SQL" style="padding:5px;">
				<table id="topsqlList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div title="TOP Session" style="padding:5px;">
				<table id="topsessionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>			
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>