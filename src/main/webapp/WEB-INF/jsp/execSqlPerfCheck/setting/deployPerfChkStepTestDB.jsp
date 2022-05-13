<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.01.03	임호경	최초작성
 * 2021.08.20	황예지	용어변경
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 :: 성능 검증 단계 관리 :: 업무별 성능 검증 단계 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />	
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/setting/deployPerfChkStepTestDB.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					
					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<label>업무</label>
					<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:false" class="w150 easyui-combotree" required="required"></select>
					
					<label>삭제여부</label>
					<select id="search_del_yn" name="search_del_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">Y</option>
						<option value="N">N</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<input type="hidden" id="old_parsing_schema_name" name="old_parsing_schema_name" value=""/>
				<input type="hidden" id="old_del_yn" name="old_del_yn" value=""/>
			
				<table class="detailT">
					<colgroup>	
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
						<col style="width:10%;">
						<col style="width:15%;">
					</colgroup>
					<tr>
						<th>업무</th>
						<td>
							<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
							</select>
						</td>
						<th>검증 단계</th>
						<td>
							<select id="perf_check_step_id" name="perf_check_step_id" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
							</select>
						</td>
						<th>단계순서</th>
						<td colspan="3">
							<input type="number" id="step_ordering" name="step_ordering" class="w150 easyui-textbox" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<th>검증평가방법</th>
						<td>
							<select id="perf_check_evaluation_meth_cd" name="perf_check_evaluation_meth_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
							</select>
						</td>
						<th>DB</th>
						<td>
							<select id="dbid" name="dbid" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
							</select>
						</td>
						<th>프로그램</th>
						<td>
							<select id="program_div_cd" name="program_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
							</select>
						</td>
						<th>삭제여부</th>
						<td>
							<select id="del_yn" name="del_yn" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
								<option value=""></option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>