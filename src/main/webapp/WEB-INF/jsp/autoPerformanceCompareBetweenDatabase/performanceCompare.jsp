<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.10.27	이재우	최초작성-현재사용안함
 **********************************************************/
%>
<!DOCTYPE html >
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
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/performanceCompare.js?ver=<%=today%>"></script>
	<style type="text/css">
	
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
			<input type="hidden" id="commonCode" name="commonCode" value="${commonCode}"/>
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="well">
					<table>
						<colgroup>
							<col style="width:7%">
							<col style="width:7%">
							<col style="width:5%">
							<col style="width:8%">
							<col style="width:5%">
							<col style="width:20%">
							<col style="width:6.5%">
							<col style="width:30%">
						</colgroup>
						
						<tr height="35px;">
							<td><label>프로젝트</label></td>
							<td colspan="3">
								<select id="project_id" name="project_id" class="w370 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td><label>SQL점검팩</label></td>
							<td>
								<select id="sqlPerformanceP" name="sqlPerformanceP" class="w360 easyui-combobox" required data-options="panelHeight:'auto',editable:false"></select>
							</td>
							<td colspan="2">
								<span style="border: 1px solid lightgray;padding:5px 5px 10px 5px; text-align: center;">
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
							</td>
						</tr>
						<tr height="35px;">
							<td><label>ASIS DB<br>&nbsp;&nbsp;&nbsp;&nbsp;(원천DB)</label></td>
							<td>
								<select id="original_dbid" name="original_dbid" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
							</td>
							<td>&nbsp;&nbsp;<label>TOBE DB<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(목표DB)</label></td>
							<td>
								<select id="perf_check_target_dbid" name="perf_check_target_dbid" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
							</td>
							<td><label>수집기간</label></td>
							<td>
								<input type="text" id="perf_check_range_begin_dt" name="perf_check_range_begin_dt" value="${startDate}" class="w150 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	~	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" id="perf_check_range_end_dt" name="perf_check_range_end_dt" value="${endDate}" class="w150 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
							<td colspan="2">
								<label>Literal 제외</label>
								<input id="literal_except_yn_chk" name="literal_except_yn_chk" checked class="easyui-checkbox">
								<input type="hidden" id="literal_except_yn" name="literal_except_yn" >
								<span class="sql_time_limt_cd">
									<label>SQL Time Limit</label>
									<select id="sql_time_limt_cd" name="sql_time_limt_cd" data-options="editable:false" class="w100 easyui-combobox"></select> 분
									<input type="hidden" id="sql_time_direct_pref_value" name="sql_time_direct_pref_value">
								</span>
								<span><label>최대 Fetch 건수</label></span>
								<input type="number" id="max_fetch_cnt" name="max_fetch_cnt" class="w100 easyui-numberbox" required data-options=" min:1, max:1000000" value="100000">
							</td>
						</tr>
						<tr height="35px;">
							<td><label>TABLE_OWNER <a style="color:blue">in</a></label></td>
							<td colspan="3" class="owner_list" >
								<input type="text" id="owner_list" name="owner_list" class="w380 easyui-searchbox" style="width:91%;"/>
							</td>
							<td><label>MODULE <a style="color:blue">like</a></label></td>
							<td class="module_list">
								<input type="text" id="module_list" name="module_list" class="w360 easyui-searchbox" style="width:93%;"/>
							</td>
							<td><label>TABLE_NAME <a style="color:blue">in</a></label></td>
							<td class="table_name_list">
								<input type="text" id="table_name_list" name="table_name_list" class="w380 easyui-searchbox"/>
							</td>
						</tr>
						<tr height="35px;">
							<td></td><td></td><td></td><td></td><td></td><td></td><td></td>
							<td class="searchBtn" style="margin-top:5px;">
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfCompare();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행</a>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ForceUpdateSqlAutoPerformanceCheck();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
								<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_AuthorityScript();"><i class="btnIcon fas fa-ruler fa-lg fa-fw"></i> 권한</a>
							</td>
						</tr>
					</table>
				</div>
				<div class="multi" style="margin-top:10px; vertical-align: '' ">
					<label>수행결과</label>
					<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체건수 : " readonly/>
					<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly/>
					<input type="text" class="perf_check_result_gray perf_check_result_common" size="14" value="수행중 : " readonly/>
					<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly/>
					<input type="text" id="perf_check_result_red" class="perf_check_result_red perf_check_result_common" size="14" value="강제완료 : " readonly/>
					<input type="text" id="perf_check_result_violet" class="perf_check_result_violet perf_check_result_common" size="14" value="점검완료" readonly/>
					<label>Refresh</label>
					<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
					<input type="number" id=timer_value name="timer_value" value="10" class="w40 easyui-numberbox" data-options="min:3"/> 초
					<a href="javascript:;" class="w20 easyui-linkbutton" onClick="Btn_LoadPerformanceCheckCount();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw"></i></a>
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
<!-- table_nameEditBox popup START -->
<div id="table_nameEditBox" class="easyui-window popWin" style="background-color:#ffffff;width:360px;height:250px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:10px;">
			<textarea id="table_nameEdit" style="height: 97%; width:99%;"></textarea>
		</div>
	</div>
</div>
<!-- table_nameEditBox popup END -->
<%@include file="/WEB-INF/jsp/include/popup/authorityScript_popup.jsp" %>
</body>
</html>