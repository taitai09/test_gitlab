<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.14	임승률	OPENPOP V2 최초작업
 * 2020.01.01	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 자동 성능 점검 대상 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/projectSqlIdfyCondition_popup.js"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlAutomaticPerformanceCheck/sqlAutomaticPerformanceCheckTargetMng.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>

				</div>
				<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="db_name" name="db_name"/>
<!-- 					<input type="hidden" id="project_id" name="project_id"/> -->
					<input type="hidden" id="owner" name="owner"/>
					
					<!-- 이전, 다음 처리 --> 
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					<label>프로젝트</label>
<!-- 					<input type="text" id="project_nm" name="project_nm" value="" class="w300 easyui-textbox" data-options="disabled:'true'"/> -->
<!-- 					<span class="searchBtnLeft"> -->
<!-- 						<a href="javascript:;" class="w30 easyui-linkbutton" onClick="Btn_ShowProjectList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></a> -->
<!-- 					</span> -->
					<select id="project_id" name="project_id" class="w350 easyui-combobox" required="true" data-options="panelHeight:'300',onChange: function(newValue,oldValue){loadSelectCombo(newValue); getUserName();}">
						<option value=""></option>
						<c:forEach items="${projectList}" var="list" >
							<option value="${list.project_id}"> ${list.project_nm}</option>
						</c:forEach>
					</select>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="panelHeight:'300',editable:false" class="w150 easyui-combobox"></select>
					<label>OWNER</label>
					<select id="owner_search" name="owner_search" data-options="panelHeight:'300',editable:false" class="w130 easyui-combobox"></select>
					<!-- 
					<input type="text" id="owner" name="owner" value="" class="w100 easyui-textbox"/>
					 -->
					<label>테이블명</label>
					<input type="text" id="table_name" name="table_name" value="" class="w150 easyui-textbox"/>
					<label>MODULE</label>
					<input type="text" id="module" name="module" value="" class="w150 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn innerBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px;margin-bottom:10px;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					<tbody><tr></tr></tbody>
				</table>
			</div>
			
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px">
			<form:form method="post" id="detail_form"           name="detail_form" class="form-inline">
				<input type="hidden" id="crud_flag"             name="crud_flag" value="C"/>
				<input type="hidden" id="sql_idfy_cond_type_cd" name="sql_idfy_cond_type_cd"/>
				<input type="hidden" id="owner_old"             name="owner_old"/>
				<input type="hidden" id="table_name_old"        name="table_name_old"/>
				<input type="hidden" id="module_old"            name="module_name_old"/>
				<input type="hidden" id="check_target_seq"      name="check_target_seq"/>
				<input type="hidden" id="owner"                 name="owner"/>
				<input type="hidden" id="dbid"                  name="dbid"/>
				<input type="hidden" id="project_check_target_type_cd" name="project_check_target_type_cd"/>
				
				<table class="detailT">
					<colgroup>
						<col style="width:10%;">
						<col style="width:30%;">
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:20%;">
					</colgroup>
					<tr>
						<th>프로젝트명</th>
						<td class="ltext">
							<select id="project_id" name="project_id" data-options="panelHeight:'300',onChange:function(newValue,oldValue){loadDetailSelectCombo(newValue);}" class="w350 h100 easyui-combobox" required="true">
								<option value=""></option>
								<c:forEach items="${projectList}" var="list" >
									<option value="${list.project_id}"> ${list.project_nm}</option>
								</c:forEach>
							</select>
						</td>
						<th>DB</th>
						<td class="ltext"><select id="selectComboOrigin" name="selectComboOrigin" data-options="panelHeight:'300',editable:false" class="w150 easyui-combobox" required="true"></select></td>
						<th>성능점검대상범위</th>
						<td class="ltext">
						<span style="padding-left:10px;">OWNER  <input class="easyui-radiobutton" id="sql_idfy_cond_type1" name="sql_idfy_cond_type1" value="1" checked></span> 
						<span style="padding-left:10px;">테이블명  <input class="easyui-radiobutton" id="sql_idfy_cond_type2" name="sql_idfy_cond_type2" value="2"></span> 
						<span style="padding-left:10px;">MODULE  <input class="easyui-radiobutton" id="sql_idfy_cond_type3" name="sql_idfy_cond_type3" value="3"></span>
						</td> 
					</tr>
					<tr>
						<th>OWNER</th>
						<td>
							<select id="owner_detail" name="owner_detail" data-options="panelHeight:'300',editable:false" class="w200 easyui-combobox" ></select>
						</td>
						<th>테이블명</th>
						<td class="ltext"><input type="text" id="table_name" name="table_name" style="text-align:left" class="w300 easyui-textbox"/></td>
						<th>MODULE</th>
						<td class="ltext"><input type="text" id="module" name="module" style="text-align:left" class="w300 easyui-textbox"/></td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w140 easyui-linkbutton" onClick="Btn_TableBatchSavePopUp();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 테이블 일괄 등록/삭제</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveProjectSqlIdfyCondition();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_DeleteProjectSqlIdfyCondition();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField('2');"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/projectSqlIdfyCondition_popup.jsp" %>
</body>
</html>