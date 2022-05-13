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
	<script type="text/javascript" src="/resources/js/ui/mqm/modelEntityTypeManagement.js?ver=<%=today%>"></script>
	
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
			<input type="hidden" id="project_id" name="project_id"/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="performance_yn" name="performance_yn"/>
			<input type="hidden" id="error_yn" name="error_yn"/>
			<input type="hidden" id="select_sql" name="select_sql"/>
			<input type="hidden" id="select_perf_impact" name="select_perf_impact"/>
			<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
			<input type="hidden" id="perf_check_range_begin_dt" name="perf_check_range_begin_dt"/>
			<input type="hidden" id="perf_check_range_end_dt" name="perf_check_range_end_dt"/>
			
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="${modelEntityType.currentPage}"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="${modelEntityType.pagePerCount}"/>
			
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

				</div>
				<div class="well">
					<div>
						<label>라이브러리명</label>
						<select id="lib_nm" name="lib_nm" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"></select>
						<label>모델명</label>
						<select id="model_nm" name="model_nm" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"><option value="">전체</option> </select>						
						<label>주제영역명</label>
						<select id="sub_nm" name="sub_nm" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"><option value="">전체</option> </select>						
						
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:300px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;padding:10px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</form:form>
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<input type="hidden" id="project_id" name="project_id" value=""/>
				<input type="hidden" id="dbid" name="dbid" value=""/>
			
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
					</colgroup>
					<tr>
						<th>라이브러리명</th>
						<td>
							<select id="lib_nm" name="lib_nm" data-options="panelHeight:'220',editable:false,required:true" class="w200 easyui-combobox"></select>
						</td>
						<th>모델명</th>
						<td>
							<select id="model_nm" name="model_nm" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"> </select>						
						</td>
						<th>주제영역명</th>
						<td>
							<select id="sub_nm" name="sub_nm" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"> </select>						
						</td>
					</tr>
					<tr>
						<th>엔터티유형</th>
						<td>
							<input type="text" id="ent_type_cd" data-options="required:true" name="ent_type_cd" class="w250 easyui-textbox"/>
						<th>참조 엔터티 PREFIX</th>
						<td>
							<input type="text" id="ref_ent_type_nm" name="ref_ent_type_nm" class="w250 easyui-textbox"/>
						</td>
						<th>엔터티 SUFFIX</th>
						<td>
							<input type="text" id="ent_type_nm" name="ent_type_nm" class="w250 easyui-textbox"/>
						</td>
					</tr>
					<tr>
						<th>테이블 유형</th>
						<td>
							<input type="text" id="tbl_type_nm" name="tbl_type_nm" class="w250 easyui-textbox"/>
						</td>
						<th>테이블 유형 코드</th>
						<td colspan="3">
							<input type="text" id="tbl_type_cd" name="tbl_type_cd" class="w250 easyui-textbox"/>
						</td>
					</tr>					
					<tr>
						<th>설명</th>
						<td colspan="5">
							<textarea id="ent_type_desc" name="ent_type_desc" style="width:100%;height:100px"></textarea>
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