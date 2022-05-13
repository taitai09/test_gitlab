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
	<script type="text/javascript" src="/resources/js/ui/systemManage/projectWrkjobMng.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	
	<script type="text/javascript">
		var search_wrkjob_cd = "${search_wrkjob_cd}";
		var search_wrkjob_cd_nm = "${search_wrkjob_cd_nm}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="project_id" name="project_id"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="${projectWrkjob.currentPage}"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="${projectWrkjob.pagePerCount}"/>
			
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
			<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
		<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:220px">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<input type="hidden" id="project_id" name="project_id" value=""/>
				<input type="hidden" id="dbid" name="dbid" value=""/>
			
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
							<input type="text" id="project_nm" name="project_nm"  data-options="required:true" class="w250 easyui-textbox"/>
						</td>
						<th>업무</th>
						<td>
							<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'220',editable:false" class="w150 easyui-combotree"></select>
<!-- 							<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'auto'" class="w130 easyui-combobox"></select> -->
						</td>
						<th>SQL 표준점검 대상 여부</th>
						<td>
							<select id="sql_std_qty_target_yn" name="sql_std_qty_target_yn" data-options="panelHeight:'auto',editable:false" class="w70 easyui-combobox">
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