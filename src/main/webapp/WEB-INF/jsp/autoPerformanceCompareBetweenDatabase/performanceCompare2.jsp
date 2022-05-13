<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능검증 :: DB 변경 성능 영향도 분석 :: 성능 영향도 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/include/popup/authorityScript_popup.js"></script>
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/performanceCompare2.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/filter_sql_perf_impact_popup.js?ver=<%=today%>"></script> <!-- FILTER SQL 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
	.popup_text {
		margin:15px 15px 0px 15px;
		font-size:12px;
	}
	.popup_area {
		margin:10px 15px 0px 15px;
		font-size:12px;
	}
	
	.radiobutton-readonly,
	.checkbox-readonly,
	.textbox-invalid {
		border-color: #ccc9c7;
	}
	.radiobutton-readonly .radiobutton-inner,
	.checkbox-readonly.checkbox-checked {
		background-color: #ccc9c7;
	}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="nowDate" name="nowDate" value="${nowDate}"/>
			<input type="hidden" id="startDate" name="startDate" value="${startDate}"/>
			<input type="hidden" id="endDate" name="endDate" value="${endDate}"/>
			<input type="hidden" id="startTime" name="startTime" value="${startTime}"/>
			<input type="hidden" id="endTime" name="endTime" value="${endTime}"/>
			<input type="hidden" id="commonCode" name="commonCode" value="${commonCode}"/>
			<input type="hidden" id="commonExecutionCode" name="commonExecutionCode" value="${commonExecutionCode}"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="well">
					<table>
						<colgroup>
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:7%">
							<col style="width:15%">
							<col style="width:30%">
							<col style="width:20%">
						</colgroup>
						
						<tr height="35px;">
							<td><label>프로젝트</label></td>
							<td colspan="3">
								<select id="project_id" name="project_id" class="w360 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td align="center"><label>SQL점검팩</label></td>
							<td>
								<select id="sqlPerformanceP" name="sqlPerformanceP" class="w360 easyui-combobox" required data-options="panelHeight:'auto',editable:false"></select>
							</td>
						</tr>
						<tr height="35px;">
							<td><label>ASIS DB</label><br><label>(원천DB)</label></td>
							<td>
								<select id="original_dbid" name="original_dbid" data-options="editable:false" class="w130 easyui-combobox" required="true" ></select>
							</td>
							<td><label>TOBE DB</label><br><label>(목표DB)</label></td>
							<td>
								<select id="perf_check_target_dbid" name="perf_check_target_dbid" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
							</td>
							<td align="center"><label>수집기간</label></td>
							<td>
								<input type="text" id="perf_check_range_begin_dt" name="perf_check_range_begin_dt" value="${startDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
								<input type="text" id="perf_period_start_time" name="perf_period_start_time" value="${startTime}" class="w70 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false"/> ~ 
								<input type="text" id="perf_check_range_end_dt" name="perf_check_range_end_dt" value="${endDate}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
								<input type="text" id="perf_period_end_time" name="perf_period_end_time" value="${endTime}" class="w70 datatime easyui-timespinner" data-options="panelHeight:'auto',editable:false"/>
							</td>
							<td colspan="3">
								<span style="border: 1px solid lightgray; padding:5px 10px 10px 5px; margin-left:10px; text-align: center;">
									<label>AWR</label><input class="easyui-radiobutton" id="AWR" name="perf_check_sql_source_type_cd" value="1"> 
									<label>전체 SQL</label><input class="easyui-radiobutton" id="entireSQL" name="perf_check_sql_source_type_cd" value="2" checked>
								</span> 
								<span style="margin-left: 10px;"><label>전체</label></span>
								<input id="all_sql_yn_chk" name="all_sql_yn_chk" class="easyui-checkbox">
								<input type="hidden" id="all_sql_yn" name="all_sql_yn">
								
								<span class="topn_cnt">
									<label>TOP N</label>
									<input type="number" id="topn_cnt" name="topn_cnt" class="w60 easyui-numberbox" required data-options="min:1"/>
								</span>
								<label>Literal 제외</label>
								<input id="literal_except_yn_chk" name="literal_except_yn_chk" checked class="easyui-checkbox">
								<input type="hidden" id="literal_except_yn" name="literal_except_yn" >
								
							</td>
						</tr>
					</table>
				</div>
				<div class="marginT10" style="border-bottom:3px solid darkgray; padding-bottom:5px;">
					<span><label style="font-weight:bold; font-size:14px; ">필터링</label></span>
				</div>
				<div class="well">
					<div>
						<span><label>TABLE_OWNER <a style="color:blue">in&nbsp;</a></label></span>
						<span class="owner_list" >
							<input type="text" id="owner_list" name="owner_list" class="w350 easyui-searchbox"/>
						</span>
						<span><label>MODULE <a style="color:blue">like</a></label></span>
						<span class="module_list">
							<input type="text" id="module_list" name="module_list" class="w350 easyui-searchbox"/>
						</span>
						<span style="margin-left:10px;">
							<a href="javascript:;" class="w80 easyui-linkbutton" id="btnFilterSQL" onClick="showFilterSQL();"><i class="btnIcon fas fa-plus-circle fa-lg fa-fw"></i> Filter SQL</a>
							<input type="text" id="extra_filter_predication" name="extra_filter_predication" readonly="readonly" class="w370 easyui-textbox"/>
						</span>
					</div>
				</div>
				<div class="marginT10" style="border-bottom:3px solid darkgray; padding-bottom:5px;">
					<span><label style="font-weight:bold; font-size:14px">실행</label></span>
				</div>
				<div class="well">
					<span><label style="margin-right:63px;">실행방법</label></span>
					<span>
						<select id="perf_compare_meth_cd" name="perf_compare_meth_cd" class="w220 easyui-combobox" data-options="editable:false"></select>
					</span>
					<span><label class="marginR5">병렬실행</label></span>
					<span>
						<input type="checkbox" id="parallel_degree_yn" name="parallel_degree_yn" class="w80 easyui-switchbutton"/>
						<input type="number" id="parallel_degree" name="parallel_degree" value="1" class="w40 easyui-numberbox" min="1" max="8"/>
					</span>
					<span><label class="marginR5">DML 실행</label></span>
					<span>
						<input type="checkbox" id="dml_exec_yn" name="dml_exec_yn" class="w80 easyui-switchbutton"/>
					</span>
					<span><label class="marginR5">Multiple 실행</label></span>
					<span>
						<input type="checkbox" id="multi_execution" name="multi_execution" class="w80 easyui-switchbutton"/>
						<input type="number" id="multiple_exec_cnt" name="multiple_exec_cnt" value="1" class="w40 easyui-numberbox" min="1" max="10"/>
					</span>
					<span><label class="marginR5">Multiple Bind 실행</label></span>
					<span>
						<input type="checkbox" id="multi_bind_execution" name="multi_bind_execution" class="w80 easyui-switchbutton"/>
						<input type="number" id="multiple_bind_exec_cnt" name="multiple_bind_exec_cnt" value="1" class="w40 easyui-numberbox" min="1" max="10"/>
					</span>
					<span class="sql_time_limt_cd">
						<label class="marginR5">SQL Time Limit</label>
						<select id="sql_time_limt_cd" name="sql_time_limt_cd" data-options="editable:false" class="w100 easyui-combobox"></select> 분
						<input type="hidden" id="sql_time_direct_pref_value" name="sql_time_direct_pref_value">
					</span>
					<span>
						<label class="marginR5">최대 Fetch 건수</label>
						<input type="number" id="max_fetch_cnt" name="max_fetch_cnt" class="w80 easyui-numberbox" required data-options=" min:1, max:1000000" value="100000">
					</span>
					
				</div>
				<div class="searchBtn marginB10" style="margin-top:10px;">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfCompare();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ForceUpdateSqlAutoPerformanceCheck();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
					<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_AuthorityScript();"><i class="btnIcon fas fa-ruler fa-lg fa-fw"></i> 권한</a>
				</div>
			</div>
			<div class="searchBtn marginB10">
				<label>수행중</label>
				<input id="inProgress" name="inProgress" checked class="easyui-checkbox">
				<label>완료</label>
				<input id="completion" name="completion" checked class="easyui-checkbox">
				<label>Refresh</label>
				<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
				<input type="number" id=timer_value name="timer_value" value="10" class="w40 easyui-numberbox" data-options="min:3"/> 초
				<a href="javascript:;" class="w20 easyui-linkbutton" onClick="Btn_LoadPerformanceCheckList();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw"></i></a>
			</div>
			
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:275px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
<!-- 					<img id="loadingBar" class="easyui-window popWin" src="/resources/images/Spinner_loading.gif" style="background:none; height:99%; width:99%;"> -->
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn" >
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"style="margin-right:0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<!-- ownerEditBox popup START -->
<div id="ownerEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:375px;height:250px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
			<textarea id="ownerEdit" style="height: 97%; width:99%;"></textarea>
		</div>
	</div>
</div>
<!-- ownerEditBox popup END -->
<!-- moduleEditBox popup START -->
<div id="moduleEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:360px;height:250px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
			<textarea id="moduleEdit" style="height: 97%; width:99%;"></textarea>
		</div>
	</div>
</div>
<!-- moduleEditBox popup END -->
<%@include file="/WEB-INF/jsp/include/popup/authorityScript_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/filter_sql_perf_impact_popup.jsp" %> <!-- FILTER SQL 팝업 -->
</body>
</html>