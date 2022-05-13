<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.11	명성태	OPENPOP V2 최초작업
 * 2020.07.01	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 자동 성능 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/authorityScript_popup.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadExplainPlan_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlAutomaticPerformanceCheck/sqlAutomaticPerformanceCheck.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	
	<script type="text/javascript">
		var search_wrkjob_cd = "${search_wrkjob_cd}";
		var search_wrkjob_cd_nm = "${search_wrkjob_cd_nm}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="db_name" name="db_name"/>
<!-- 			<input type="hidden" id="project_id" name="project_id"/> -->
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="performance_yn" name="performance_yn"/>
			<input type="hidden" id="error_yn" name="error_yn"/>
			<input type="hidden" id="select_sql" name="select_sql"/>
			<input type="hidden" id="select_perf_impact" name="select_perf_impact"/>
			<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
			<input type="hidden" id="perf_check_range_begin_dt" name="perf_check_range_begin_dt"/>
			<input type="hidden" id="perf_check_range_end_dt" name="perf_check_range_end_dt"/>
			<input type="hidden" id="perf_check_target_dbid" name="perf_check_target_dbid"/>
			
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="initStartDate" name="initStartData" value="${startDate}"/>
			<input type="hidden" id="initNowDate" name="initNowData" value="${nowDate}"/>
			<input type="hidden" id="topn_cnt" name="topn_cnt"/>
			<input type="hidden" id="literal_except_yn" name="literal_except_yn" value="Y"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="plan_change_status" name="plan_change_status"/>
			<input type="hidden" id="original_dbid" name="original_dbid"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="${sqlAutomaticPerformanceCheck.currentPage}"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlAutomaticPerformanceCheck.pagePerCount}"/>
			
			<div class="easyui-panel searchArea170" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

				</div>
				<div class="well">
					<div>
						<label>프로젝트</label>
<!-- 						<input type="text" id="project_nm" name="project_nm" value="" class="w350 easyui-textbox"/> -->
						<select id="project_id" name="project_id" class="w350 easyui-combobox" required="true" data-options="onChange: function(newValue,oldValue){searchPerformanceCheckId(newValue);}">
							<option value=""></option>
							<c:forEach items="${projectList}" var="list" >
								<option value="${list.project_id}"> ${list.project_nm}</option>
							</c:forEach>
						</select>
						<label>수행회차</label>
						<select id="perfCheckIdCombo" name="perfCheckIdCombo" data-options="panelHeight:'auto',editable:false" class="w50 easyui-combobox"></select>
						<label>미수행여부</label>
						<input type="checkbox" id="performanceYnSwitch" name="performanceYnSwitch" data-options="disabled:false" class="easyui-switchbutton"/>
						<label>오류여부</label>
						<input type="checkbox" id="errorYnSwitch" name="errorYnSwitch" data-options="disabled:false" class="easyui-switchbutton"/>
						<label>SQL유형</label>
						<select id="selectSql" name="selectSql" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox"></select>
						<label>성능 임팩트유형</label>
						<select id="selectPerfImpact" name="selectPerfImpact" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox"></select>
						<label>Plan 변경여부</label>
						<input type="checkbox" id="planChangeStatusSwitch" name="planChangeStatusSwitch" data-options="disabled:false" class="easyui-switchbutton"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
					<div class="multi">
						<label>수행결과</label>
						<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체건수 : " readonly/>
						<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly/>
						<input type="text" class="perf_check_result_gray perf_check_result_common" size="14" value="수행중 : " readonly/>
						<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly/>
						<input type="text" id="perf_check_result_red" class="perf_check_result_red perf_check_result_common" size="14" value="강제완료 : " readonly/>
						<input type="text" id="perf_check_result_violet" class="perf_check_result_violet perf_check_result_common" size="14" value="점검완료" readonly/>
						<label>Refresh</label>
						<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
						<input type="number" id="refresh_sec" name="refresh_sec" value="10" class="w40 easyui-numberbox" data-options="min:3"/> 초
					</div>
				</div>
				<div id="sqlAutomaticPerformanceCheckActionPlan" style="padding-top:10px;">
					<label>원천 DB</label>
					<select id="selectComboOrigin" name="selectComboOrigin" data-options="editable:false" class="w100 easyui-combobox" required="true"></select>
					<label>성능점검대상 DB</label>
					<input id="targetDbName" name="targetDbName" class="w80 easyui-textbox" readonly/>
					<label>Parsing Schema Name</label>
					<input id="parsingSchemaName" name="parsingSchemaName" class="w110 easyui-textbox" readonly/>
					<label>성능점검범위 일시</label>
					<input type="text" id="startDate" name="startDate" value="${startDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
					<input id="startTime" name="startTime" value="" class="w60 easyui-timespinner" data-options="panelHeight:'auto'"/> ~
					<input type="text" id="endDate" name="endDate" value="${nowDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"/>
					<input id="endTime" name="endTime" value="" class="w60 easyui-timespinner" data-options="panelHeight:'auto'"/>
					<label>TOP N</label>
					<input type="number" id="topN" name="topN" class="w60 easyui-numberbox" data-options="min:1"/>
					<span>
						<i id="topN_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
					</span>
					<label>Literal 제외</label>
					<input id="checkLiteralYn" name="checkLiteralYn" class="easyui-checkbox">
					<div class="searchBtn innerBtn">
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutomaticPerformanceCheck();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> SQL성능자동점검</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ForceUpdateSqlAutomaticPerformanceCheck();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
						<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_AuthorityScript();"><i class="btnIcon fas fa-ruler fa-lg fa-fw"></i> 권한</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:150px;min-height:140px">
				<table style="width:100%;height:140px;" class="detailT4">
					<colgroup>
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<thead>
						<tr>
							<th style="height:5px;">성능 임팩트</th>
							<th style="height:5px;">Elapsed Time</th>
							<th style="height:5px;">Buffer Gets</th>
							<th style="height:5px;">Plan 변경</th>
							<th style="height:5px;">SQL유형</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div id="chartPerfImpactPanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartElapsedTimePanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartBufferGetsPanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartPlanChangePanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartSqlTypePanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:400px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/authorityScript_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>