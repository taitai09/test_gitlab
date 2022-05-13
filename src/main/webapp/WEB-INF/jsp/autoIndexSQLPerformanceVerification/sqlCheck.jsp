<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능시험 :: 자동 인덱싱 SQL 성능 검증 :: SQL점검팩</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/sqlCheck.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
		.sqlCheck .datagrid-header-check input{ display: none; }
		.combobox-item{
			line-height: 11px;
			font-weight: 300;
			-webkit-font-smoothing: antialiased;
		}
		#saveSQLPerformance .noneT_popup tr .label{
			padding: 8px 0px;
			font-size: 14px;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="sqlCheck">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" name="perf_check_type_cd" value="4">
			<input type="hidden" id="data_yn" name="data_yn">
			<input type="hidden" id="refresh" name="refresh" value="N">
			<input type="hidden" id="sqlExclude" name="sqlExclude" value="">
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage">
			<input type="hidden" id="pagePerCount" name="pagePerCount">
			
			<div class="well">
				<table>
					<colgroup>
						<col style="width:5%">
						<col style="width:80%">
					</colgroup>
					<tbody>
						<tr>
							<td><label>프로젝트</label></td>
							<td>
								<select id="project_combo" class="w340 easyui-combobox" required="true" data-options="editable:false,prompt:'선택'"></select>
								<input type="hidden" name="project_id" value="">
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="multi" style="margin:10px 0px;" align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSaveSQLPerformance_popup();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 등록 / 수정</a>
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_delete();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
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
	</div> <!-- contents END -->
</div> <!-- container END -->
<!-- popup START -->
<div id="saveSQLPerformance" class="easyui-window popWin" style="background-color:#ffffff;width:540px;height:230px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;">
			<form:form method="post" id="popup_form" name="popup_form" class="form-inline">
				<input type="hidden" name="database_kinds_cd" value="${database_kinds_cd}">
				<input type="hidden" name="user_id" value="${user_id}">
				<input type="hidden" name="perf_check_type_cd" value="4">
				<input type="hidden" id="project_id_popup" name="project_id">
				<input type="hidden" id="sql_auto_perf_check_id_popup" name="sql_auto_perf_check_id">
				
				<table class="noneT_popup">
					<colgroup>
						<col style="width:20%;">
						<col style="width:31%;">
					</colgroup>
					<tr>
						<td class="label">수정할 SQL점검팩 선택</td>
						<td class="ltext">
							<select id="sqlPerformanceP_popup" data-options="panelHeight:'auto',editable:false,prompt:'선택'"class="easyui-combobox" style="width:304px;"></select>
						</td>
					</tr>
					<tr>
						<td class="label">SQL점검팩명</td>
						<td class="ltext">
							<input type="text" id="perf_check_name_popup" name="perf_check_name" class="w300 easyui-textbox" required>
						</td>
					</tr>
					<tr>
						<td class="label">SQL점검팩 설명</td>
						<td class="ltext">
							<input type="text" id="perf_check_desc_popup" name="perf_check_desc" class="w300 easyui-textbox" required>
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