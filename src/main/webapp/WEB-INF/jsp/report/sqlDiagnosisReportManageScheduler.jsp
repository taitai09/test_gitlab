<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="true" %>
<%
/***********************************************************
 * 2021.09.23	김원재	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>종합 진단 :: SQL 품질 진단 :: 스케쥴러 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
		<script type="text/javascript" src="/resources/js/ui/include/popup/filter_sql_perf_impact_popup.js?ver=<%=today%>"></script> <!-- FILTER SQL 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/report/sqlDiagnosisReportManageScheduler.js?ver=<%=today%>"></script>
	
	<!-- RSA 필수사항 자바라이브러리 -->
	<script type="text/javascript" src="/resources/js/lib/rsa.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rsa2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn.js"></script>
	<script type="text/javascript" src="/resources/js/lib/jsbn2.js"></script>
	<script type="text/javascript" src="/resources/js/lib/prng4.js"></script>
	<script type="text/javascript" src="/resources/js/lib/rng.js"></script>
	<!-- END RSA 필수사항 자바라이브러리 -->
	<style>
		.popup_text{
			margin:15px 15px 0px 15px;
			font-size:12px;
		}
		.popup_area{
			margin:10px 15px 0px 15px;
			font-size:12px;
		}
		.manageScheduler .searchOption a{
			margin-left: 10px;
		}
		.manageScheduler .searchOption {
			margin-bottom: 15px;
		}
/* 		.manageScheduler a:last-child { */
/* 			float: right; */
/* 		} */
		.manageScheduler .littleTitle {
			margin: 9px 5px 7px 5px;
			font-weight: bold;
			font-size: 13.5px;
		}
		.manageScheduler .detailT {
			margin-bottom: 10px;
		}
		.textbox-label{
		width : auto;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="manageScheduler">
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			
			<div class="well searchOption">
				<label>품질진단DB</label>
				<select id="db_name_combo" name="std_qty_target_dbid" data-options="editable:false" class="w170 easyui-combobox" required="true"></select>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon btnSearch fas fa-search fa-lg fa-fw"></i> 검색</a>
				<a href="javascript:;" style="float: right;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			
				<input type="hidden" id="currentPage" name="currentPage" value="1"/>
				<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
		</form:form>
		
		<form:form method="post" id="excel_submit_form" name="submit_form" class="form-inline">
			<input type="hidden" id="excel_db_id" name="std_qty_target_dbid" value="">
		</form:form>
	
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:280px">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="margin-top:10px; width:100%;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
		</div>
		

		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;margin-top: 5px;">
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<input type="hidden" id="nowDate" name="nowDate" value='${nowDate}'>
				<input type="hidden" id="lastMonth" name="lastMonth" value='${lastMonth}'>
				<input type="hidden" name="job_scheduler_type_cd">
				<input type="hidden" name="sql_std_qty_scheduler_no">
				<input type="hidden" name="db_name">
				<input type="hidden" name="std_qty_scheduler_div_cd" value="03">
				
				<p class="littleTitle">스케줄러 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:27%;">
					</colgroup>
					<tr>
						<th>스케줄러명</th>
						<td colspan="7">
							<input id="job_scheduler_nm" name="job_scheduler_nm" value="" data-options="editable:true" class="easyui-textbox" style="width: 100%;" required>
						</td>
					</tr>
					<tr>
						<th>스케줄러 시작일자</th>
						<td>
							<input type="text" id="exec_start_dt" name="exec_start_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" style="width: 100%;" required>
						</td>
						<th>스케줄러 종료일자</th>
						<td>
							<input type="text" id="exec_end_dt" name="exec_end_dt" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"style="width: 100%;" required>
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
						<th>스케줄러 설명</th>
						<td colspan="7">
							<input type="text" id="job_scheduler_desc" name="job_scheduler_desc" class="easyui-textbox" style="width: 100%;">
						</td>
					</tr>
				</table>
				
				<p class="littleTitle">SQL 품질 진단 대상 설정</p>
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:27%;">
					</colgroup>
					<tr>
						<th>품질 진단 DB</th>
						<td>
							<select id="std_qty_target_dbid" name="std_qty_target_dbid" value="" data-options="panelHeight:'auto',editable:false" class="easyui-combobox" style="width: 100%;" required></select>
						</td>
						<th>SQL 소스</th>
						<td>
<!-- 							<input id="svn_dir_nm" name="svn_dir_nm" value="" data-options="editable:true" class="easyui-textbox" style="width: 100%;" required> -->
							<div>
								<input class="easyui-radiobutton" id="sqlSourceTop" name="sql_source_type_cd" value="1" label="TOP" labelPosition="before">
								<div style="display: inline-block; width: 10px;"></div>
								<input class="easyui-radiobutton" id="sqlSourceAll" name="sql_source_type_cd" value="2" label="전체SQL" labelPosition="before">
							</div>
						</td>
						<th>수집기간</th>
						<td colspan="2">
							<select id="gather_term_type_cd" name="gather_term_type_cd" data-options="panelHeight:'auto',editable:false" class="easyui-combobox" style="width:20%;" required="true"></select>
<!-- 							<input id="gather_term_type_cd" name="gather_term_type_cd" value="" data-options="editable:true" class="easyui-textbox" style="width: 30%;" required> -->
							<div id="gather_term_type1" style="display:none;width:79%">
								<input class="easyui-radiobutton" id="gather_term_type_cd_week" name="gather_range_div_cd" value="2" label="주간 진단" labelPosition="after">
								<div style="display: inline-block; width: 10px;"></div>
								<input class="easyui-radiobutton" id="gather_term_type_cd_month" name="gather_range_div_cd" value="3" label="월간 진단" labelPosition="after">
							</div>
							<div id="gather_term_type2" style="display:none;width:79%">
								<input type="text" id="gather_term_start_day" name="gather_term_start_day" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"style="width: 48%;" required>
								<span> ~ </span>
								<input type="text" id="gather_term_end_day" name="gather_term_end_day" value="" class="datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false"style="width: 48%;" required>
							</div>
						</td>
					</tr>
					<tr>
						<th>Table Owner <span style="color:blue; font-weight:normal">In</span></th>
						<td>
<!-- 							<input type="text" id="owner_list" name="owner_list" value="" style="width:100%" class="easyui-textbox"  data-options="panelHeight:'auto',iconCls:'icon-search'" required> -->
							<input type="text" id="owner_list" name="owner_list" value="" style="width:100%" class="easyui-textbox"  data-options="panelHeight:'auto',iconCls:'icon-search'" prompt="ERP, MIS, BIZHUB">

						</td>
						<th>Module <span style="color:blue; font-weight:normal">like</span></th>
						<td>
<!-- 							<input type="text" id=module_list name="module_list" value="" style="width:100%" class="easyui-textbox" data-options="panelHeight:'auto',iconCls:'icon-search'" required> -->
							<input type="text" id=module_list name="module_list" value="" style="width:100%" class="easyui-textbox" data-options="panelHeight:'auto',iconCls:'icon-search'" prompt="JDBC, B_ERP0001, S_ERP0001">	
						</td>
			
						<th>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="showFilterSQL();"><i class="btnIcon btnSearch fas fa-plus-circle fa-lg fa-fw"></i> Filter SQL</a>
						</th>
						<td colspan="2">
							<input type="text" id="extra_filter_predication" name="extra_filter_predication" value="" style="width:100%" class="easyui-textbox" data-options="panelHeight:'auto'" required readonly>
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
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/filter_sql_perf_impact_popup.jsp" %> <!-- FILTER SQL 팝업 -->

</body>
</html>