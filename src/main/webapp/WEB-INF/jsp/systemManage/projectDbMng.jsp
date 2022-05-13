<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.11	명성태	OPENPOP V2 최초작업
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
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadExplainPlan_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/systemManage/projectDbMng.js?ver=<%=today%>"></script>
	
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="project_id" name="project_id"/>
			
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

				</div>
				<div class="well">
					<div>
						<label>프로젝트</label>
						<input type="text" id="project_nm" name="project_nm" value="" class="w250 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:370px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;padding:10px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</form:form>
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<div class="easyui-panel" data-options="border:false" style="width:99%;min-height:220px">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<input type="hidden" id="project_id" name="project_id" value=""/>
			
				<table class="detailT">
					<colgroup>
						<col style="width:10%;">
						<col style="width:30%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:10%;">
					</colgroup>
					<tr>
						<th>프로젝트</th>
						<td>
							<input type="text" id="project_nm" name="project_nm"  data-options="panelHeight:'220',editable:false,required:true" class="w250 easyui-textbox"/>
						</td>
						<th>성능점검원천 DB</th>
						<td>
							<select id="perf_check_original_dbid" name="perf_check_original_dbid" data-options="panelHeight:'220',editable:false,required:true" class="w150 easyui-combobox"></select>
						</td>
						<th>성능점검대상 DB</th>
						<td>
							<select id="perf_check_target_dbid" name="perf_check_target_dbid" data-options="panelHeight:'220',editable:false,required:true" class="w120 easyui-combobox"></select>
						</td>
					</tr>
					<tr>
						<th>Parsing Schema Name</th>
						<td>
							<select id="parsing_schema_name" name="parsing_schema_name" data-options="panelHeight:'220',editable:true,required:true" class="w150 easyui-combobox"></select>
						</td>
						<th>SQL성능자동점검 대상 여부</th>
						<td colspan="3">
							<select id="perf_check_target_yn" name="perf_check_target_yn" data-options="panelHeight:'auto',editable:false,required:true" class="w70 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">Y</option>
								<option value="N" selected>N</option>
							</select>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>