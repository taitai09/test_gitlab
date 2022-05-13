<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.02.08	황예지	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능시험 :: 테이블 변경 성능 영향도 분석 :: 튜닝 실적</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<link rel="stylesheet" href="/resources/css/analyzeImpactChangedTable.css">
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/tuningPerformance.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/analyzeImpactChangedTableCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="tuningPerf">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" id="plan_change_cnt" name="plan_change_cnt" value="N"/>
			<input type="hidden" id="tuning_selection_cnt" name="tuning_selection_cnt" value="N"/>
			<input type="hidden" id="elapsed_time_std_cnt" name="elapsed_time_std_cnt" value="N"/>
			<input type="hidden" id="buffer_std_cnt" name="buffer_std_cnt" value="N"/>
			<input type="hidden" id="tuning_end_cnt" name="tuning_end_cnt" value="N"/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" value=""/>
			<input type="hidden" id="tuning_cnt" name="tuning_cnt" value="N"/>
			<input type="hidden" id="field" name="field" value=""/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="well">
				<table>
					<colgroup>
						<col style="width:64px">
						<col style="width:125px">
						<col style="width:100px">
						<col style="width:280px">
						<col style="width:160px">
						<col style="width:76px">
						<col style="width:360px">
						<col style="width:150px">
					</colgroup>
					<tbody>
						<tr>
							<td><label>소스DB</label></td>
							<td>
								<select id="original_dbid" name="original_dbid" data-options="editable:false" class="w110 easyui-combobox" required></select>
							</td>
							<td><label>점검팩 등록일자</label></td>
							<td>
								<input type="text" id="perf_check_exec_begin_dt" name="perf_check_range_begin_dt" value="${aMonthAgo}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
								<span class="spacing">	~	</span>
								<input type="text" id="perf_check_exec_end_dt" name="perf_check_range_end_dt" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
							</td>
							<td class="radio-area">
								<span><input class="easyui-radiobutton" name="analysisPeriod" id="aWeek"><label>1주일</label></span>
								<span><input class="easyui-radiobutton" name="analysisPeriod" id="oneMonth" checked><label>1개월</label></span>
							</td>
							<td><label>SQL점검팩</label></td>
							<td>
								<select id="sqlPerformanceP" name="sqlPerformanceP" class="w350 easyui-combobox" data-options="panelHeight:'auto',editable:false"></select>
							</td>
							<td>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:200px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableDefaultList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Detail_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout bottom_table" data-options="border:false" style="width:100%;min-height:280px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style="margin-right:0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->

</body>
</html>