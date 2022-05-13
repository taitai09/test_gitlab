<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검 :: 스케쥴러 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/execSqlStdChkCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/manageScheduler.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/filter_sql_perf_impact_popup.js?ver=<%=today%>"></script> <!-- FILTER SQL 팝업 -->
	
	<style>
		.manageScheduler .searchOption a{
			margin-left: 10px;
		}
		.manageScheduler .searchOption {
			margin-bottom: 15px;
		}
		.manageScheduler a:last-child {
			float: right;
		}
		.manageScheduler table a:last-child {
			float: inherit;
		}
		.manageScheduler .littleTitle {
			margin: 9px 5px 7px 5px;
			font-weight: bold;
			font-size: 13.5px;
		}
		.manageScheduler .detailT {
			margin-bottom: 10px;
		}
		.manageScheduler .blue {
			color: blue;
			font-weight: normal;
		}
	</style>
	<script>
		var defaultStartDt = '${aMonthAgo}';
		var defaultEndDt = '${yesterDay}';
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="manageScheduler">
		<form:form method="post" id="search_form" name="search_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
			
			<div class="well searchOption">
				<label>프로젝트</label>
				<select id="project_id" name="project_id" data-options="editable:false" class="w350 easyui-combobox"></select>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
		</form:form>
		<form:form method="post" id="excel_submit_form" name="submit_form" class="form-inline">
			<input type="hidden" id="excel_project_id" name="project_id" value="">
			<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
		</form:form>
	
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:280px">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;margin-top: 5px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<input type="hidden" name="job_scheduler_type_cd" value="37">
				<input type="hidden" name="sql_std_qty_scheduler_no" value="">
				<input type="hidden" name="use_yn" value="Y">
				<input type="hidden" name="std_qty_scheduler_div_cd" value="02">
				
				<p class="littleTitle">스케줄러 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:40%;">
					</colgroup>
					<tr>
						<th>프로젝트</th>
						<td colspan="3">
							<select id="project_id_modify" name="project_id" value="" data-options="panelHeight:'140px',editable:false" class="easyui-combobox" style="width: 62.5%;" required></select>
						</td>
						<th>스케줄러명</th>
						<td>
							<input id="job_scheduler_nm" name="job_scheduler_nm" value="" data-options="editable:true" class="easyui-textbox" style="width: 61.5%;" required>
						</td>
					</tr>
					<tr>
						<th>스케줄러 시작일자</th>
						<td>
							<input type="text" id="exec_start_dt" name="exec_start_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" style="width: 83.5%;" required>
						</td>
						<th>스케줄러 종료일자</th>
						<td>
							<input type="text" id="exec_end_dt" name="exec_end_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" style="width: 83.5%;" required>
						</td>
						<th>실행주기</th>
						<td colspan="3">
							<select id="date_fir" name="exec_cycle_div_cd" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox" required></select>
							<select id="date_scnd" data-options="panelHeight:'150px',editable:false" class="w100 easyui-combobox" required></select>
							<input type="hidden" id="execOption" name="">
							<select id="date_thrd" name="exec_hour" data-options="panelHeight:'300px',editable:false" class="w100 easyui-combobox" required></select>
							<select id="date_frth" name="exec_minute" data-options="panelHeight:'300px',editable:false" class="w100 easyui-combobox" required></select>
							<input type="hidden" name="exec_cycle" data-part="">
						</td>
					</tr>
					<tr>
						<th>표준점검DB</th>
						<td>
							<select id="std_qty_target_dbid" name="std_qty_target_dbid" value="" class="w200 easyui-combobox" data-options="panelHeight:'auto',editable:false" required>
						</td>
						<th>Parser Code</th>
						<td>
							<input type="text" id="parse_code" name="parse_code" class="w200 easyui-textbox" required>
						</td>
						<th>스케줄러 설명</th>
						<td>
							<input type="text" id="job_scheduler_desc" name="job_scheduler_desc" class="easyui-textbox" style="width: 62%;">
						</td>
					</tr>
				</table>
				
				<p class="littleTitle" style="margin: 17px 5px 7px 5px;">SQL 점검 대상 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:40%;">
					</colgroup>
					<tr>
						<th>SQL 소스</th>
						<td>
							<label for="btn-AWR">TOP</label>
							<input name="sql_source_type_cd" value="1" id="btn-AWR" class="easyui-radiobutton" checked>
							<label for="btn-all" style="margin-left: 15px;">전체SQL</label>
							<input name="sql_source_type_cd" value="2" id="btn-all" class="easyui-radiobutton">
						</td>
						<th>수집기간</th>
						<td colspan="3">
							<select id="gather_term_type_cd" name="gather_term_type_cd" value="" class="w100 easyui-combobox" data-options="panelHeight:'auto',editable:false" required></select>
							<span id="gather_term_1">
								<select id="gather_range_div_cd" name="gather_range_div_cd" value="" class="w140 easyui-combobox" data-options="panelHeight:'auto',editable:false" required></select>
							</span>
							<span id="gather_term_2">
								<input type="text" id="gather_term_start_day" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required>
								<span style="margin: 0px 5px;"> ~ </span>
								<input type="text" id="gather_term_end_day" class="w130 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required>
								<input type="hidden" name="gather_term_start_day" value="">
								<input type="hidden" name="gather_term_end_day" value="">
							</span>
						</td>
					</tr>
					<tr>
						<th>Table Owner <span class="blue">in</span></th>
						<td class="owner_list">
							<input type="text" id="owner_list" name="owner_list" class="easyui-searchbox" style="width: 83.5%;">
						</td>
						<th>Module <span class="blue">like</span></th>
						<td class="module_list">
							<input type="text" id="module_list" name="module_list" class="easyui-searchbox" style="width: 83.5%;">
						</td>
						<th>
							<a href="javascript:;" class="w80 easyui-linkbutton" id="btnFilterSQL" onClick="showFilterSQL();">
								<i class="btnIcon fas fa-plus-circle fa-lg fa-fw"></i> Filter SQL
							</a>
						</th>
						<td>
							<input type="text" id="extra_filter_predication" name="extra_filter_predication" class="easyui-textbox" style="width: 61.5%;" readonly>
						</td>
					</tr>
				</table>
				
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" id="btnSave" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" id="btnDelete" onClick="Btn_PreventDelete();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div> 	<!-- contents END -->
</div>	<!-- container END -->
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
<%@include file="/WEB-INF/jsp/include/popup/filter_sql_perf_impact_popup.jsp" %> <!-- FILTER SQL 팝업 -->
</body>
</html>