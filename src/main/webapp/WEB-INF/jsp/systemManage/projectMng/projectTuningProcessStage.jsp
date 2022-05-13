<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ page session="false" %>
<%
/***********************************************************
 * 2019.09.20	명성태	초도 작업
 * 2020.07.03	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>프로젝트 튜닝진행단계</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/systemManage/projectMng/projectTuningProcessStage.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
<!-- 			<input type="hidden" id="project_id" name="project_id"/> -->
			
			<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%">
				<div class="well">
					<div>
						<label>프로젝트</label>
<!-- 						<input type="text" id="project_nm" name="project_nm" value="" class="w250 easyui-textbox"/> -->
							<select id="project_id" name="project_id" class="w350 easyui-combobox" required="required">
								<option value=""></option>
								<c:forEach items="${projectList}" var="list">
									<option value="${list.project_id}"> ${list.project_nm}</option>
								</c:forEach>
							</select>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:440px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;padding-bottom:5px;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</form:form>
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:220px">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
<!-- 				<input type="hidden" id="project_id" name="project_id" value=""/> -->
				<input type="hidden" id="dbid" name="dbid" value=""/>
				<input type="hidden" id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq"/>
			
				<table class="detailT">
					<colgroup>
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:5%;">
						<col style="width:10%;">
						<col style="width:20%;">
						<col style="width:10%;">
						<col style="width:5%;">
					</colgroup>
					<tr>
						<th>프로젝트</th>
						<td>
<!-- 							<input type="text" id="project_nm" name="project_nm"  data-options="panelHeight:'220',editable:false,required:true" class="w250 easyui-textbox"/> -->
							<select id="project_id" name="project_id" class="w350 easyui-combobox" data-options="panelHeight:'220',editable:false,required:true">
								<option value=""></option>
								<c:forEach items="${projectList}" var="list">
									<option value="${list.project_id}"> ${list.project_nm}</option>
								</c:forEach>
							</select>
						</td>
						<th>튜닝진행단계일련번호</th>
						<td>
							<input type="text" id="tuning_prgrs_step_seq_web" name="tuning_prgrs_step_seq_web" data-options="disabled:true" class="w80 easyui-textbox"/>
						</td>
						<th>튜닝진행단계명</th>
						<td>
							<input type="text" id="tuning_prgrs_step_nm" name="tuning_prgrs_step_nm" class="w350 easyui-textbox" required="required"/>
						</td>
						<th>삭제여부</th>
						<td>
							<select id="del_yn" name="del_yn" data-options="panelHeight:'auto',editable:false" class="w70 easyui-combobox">
								<option value="Y">Y</option>
								<option value="N" selected>N</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>튜닝진행단계 설명</th>
						<td colspan="5">
							<textarea name="tuning_prgrs_step_desc" id="tuning_prgrs_step_desc" style="margin-top:5px;padding:5px;width:99%;height:83%" wrap="off"></textarea>
						</td>
						
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>