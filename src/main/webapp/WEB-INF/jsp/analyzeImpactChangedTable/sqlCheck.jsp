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
	<title>성능시험 :: 테이블 변경 성능 영향도 분석 :: SQL점검팩</title>
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
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/sqlCheck.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/analyzeImpactChangedTableCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="sqlCheck">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="sqlExclude" name="sqlExclude" value=""/>
			
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
								<select id="original_dbid" name="original_dbid" data-options="editable:false" class="w110 easyui-combobox"></select>
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
							<td class="sqlPerfPack">
								<select id="sqlPerformanceP" name="sql_auto_perf_check_id" class="w350 easyui-combobox" data-options="panelHeight:'auto',editable:false"></select>
							</td>
							<td>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="multi" style="margin:10px 0px;" align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSaveSQLPerformance_popup();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 등록 / 수정</a>
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="deleteTable();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:531px;">
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

<!-- popup START -->
<div id="saveSQLPerformance" class="easyui-window popWin" style="background-color:#ffffff;width:540px;height:230px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;">
			<form:form method="post" id="popup_form" name="popup_form" class="form-inline">
				<input type="hidden" name="database_kinds_cd" value="${database_kinds_cd}">
				<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
				<input type="hidden" id="project_id" name="project_id"/>
				<input type="hidden" id="perf_check_type_cd" name="perf_check_type_cd"/>
				<input type="hidden" id="popup_sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
				<table class="noneT_popup">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:31%;">
					</colgroup>
					<tr>
						<td style="font-size: 14px; padding:10px 0px;">수정할 SQL점검팩 선택</td>
						<td class="ltext">
							<select id="popup_sqlPerformanceP" name="sqlPerformanceP" data-options="panelHeight:'auto',editable:false"
								class="easyui-combobox" style="width:304px;"></select>
						</td>
					</tr>
					<tr>
						<td style="font-size: 14px; padding:10px 0px;">SQL점검팩명</td>
						<td class="ltext">
							<input type="text" id=popup_perf_check_name name="perf_check_name" class="w300 easyui-textbox" required>
						</td>
					</tr>
					<tr>
						<td style="font-size: 14px; padding:10px 0px;">SQL점검팩 설명</td>
						<td class="ltext">
							<input type="text" id="popup_perf_check_desc" name="perf_check_desc" class="w300 easyui-textbox" required>
						</td>
					</tr>
				</table>
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;right:-8px;position:relative;" >
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSqlPerfPac();"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ResetField()"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw"></i> 초기화</a>
			</div>
		</div>
	</div>
</div>
<!-- popup END -->

</body>
</html>