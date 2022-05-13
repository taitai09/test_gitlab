<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.18	임승률	최초작성
 * 2020.07.02	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>DB 구조 품질 점검 :: 구조 품질 집계표</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/include/popup/design/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/mqmDesign/operation/qualityInspectionJobSheet.js?ver=<%=today%>"></script>    
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
					<input type="hidden" id="extrac_dt" name="extrac_dt" value=""/>
<!-- 					<input type="hidden" id="project_id" name="project_id" value=""/> -->
<!-- 					<input type="hidden" id="lib_nm" name="lib_nm" value=""/> -->
<!-- 					<input type="hidden" id="model_nm" name="model_nm" value=""/> -->
					
					<label>프로젝트</label>
<!-- 					<input type="text" id="project_nm" name="project_nm" value="" class="w350 easyui-textbox"/> -->
					<select id="project_id" name="project_id" class="w350 easyui-combobox" data-options="onChange:function(newValue,oldValue){settingComboBox(newValue);}">
						<option value="">선택</option>
						<c:forEach items="${projectList}" var="list" >
							<option value="${list.project_id}"> ${list.project_nm}</option>
						</c:forEach>
					</select>
					<label>라이브러리명</label>
					<select id="lib_nm" name="lib_nm" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="">전체</option>
					</select>
					<label>모델명</label>
					<select id="model_nm" name="model_nm" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="">전체</option>
					</select>
					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<span class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</span>
				</form:form>
				<form:form method="post" id="save_form" name="save_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd" value=""/>
					<input type="hidden" id="qty_chk_result_tbl_nm" name="qty_chk_result_tbl_nm" value=""/>
					<input type="hidden" id="dml_yn" name="dml_yn" value=""/>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:600px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/design/project_popup.jsp" %>
</body>
</html>