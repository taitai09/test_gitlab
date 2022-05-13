<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.10.27	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능검증 :: DB간 SQL성능비교 :: SQL점검팩</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/sqlCheck.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
		.datagrid-header-check input{ display: none; }
		
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="project_id" name="project_id"/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="perf_check_name" name="perf_check_name"/>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="perf_check_type_cd" name="perf_check_type_cd" value="1"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="well">
				<table>
					<colgroup>
						<col style="width:5%">
						<col style="width:80%">
					</colgroup>
					
					<tr>
						<td><label>프로젝트</label></td>
						<td colspan="2">
							<select id="project_id_cd" name="project_id_cd" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch(1);"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
						</td>
					</tr>
				</table>
			</div>
			<div class="multi" style="margin:10px 0px;" align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSaveSQLPerformance_popup();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 등록 / 수정</a>&emsp;
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
				<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
				<input type="hidden" id="project_id" name="project_id"/>
				<input type="hidden" id="perf_check_type_cd" name="perf_check_type_cd"/>
				<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
				<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
				<table class="noneT_popup">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:31%;">
					</colgroup>
					<tr>
						<td style="font-size:14px; padding:10px 0px;">수정할 SQL점검팩 선택</td>
						<td class="ltext">
							<select id="sqlPerformanceP_cd" name="sqlPerformanceP_cd" data-options="panelHeight:'auto',editable:false"
								class="easyui-combobox" style="width:304px;"></select>
						</td>
					</tr>
					<tr>
						<td style="font-size:14px; padding:10px 0px;">SQL점검팩명</td>
						<td class="ltext">
							<input type="text" id=perf_check_name name="perf_check_name" class="w300 easyui-textbox" required="true">
						</td>
					</tr>
					<tr>
						<td style="font-size:14px; padding:10px 0px;">SQL점검팩 설명</td>
						<td class="ltext">
							<input type="text" id="perf_check_desc" name="perf_check_desc" class="w300 easyui-textbox" required="true">
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