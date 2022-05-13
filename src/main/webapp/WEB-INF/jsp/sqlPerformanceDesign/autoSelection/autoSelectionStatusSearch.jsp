<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.12	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>자동선정 :: 자동선정 현황(검색)</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlPerformanceDesign/autoSelection/autoSelectionStatusSearch.js?ver=<%=today%>"></script>
	<!-- 자동/수정 선정현황  일괄튜닝종료 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/batchTuningBundle_popup.js?ver=<%=today%>"></script>

</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%">
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${topsqlAutoChoice.dbid}"/>
					<input type="hidden" id="choice_tms" name="choice_tms"/>
					<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="1"/>
					<input type="hidden" id="auto_choice_cond_no" name="auto_choice_cond_no" value="${topsqlAutoChoice.auto_choice_cond_no}"/>
					<input type="hidden" id="project_id" name="project_id"/>
					<input type="hidden" id="perfr_id" name="perfr_id"/>
					
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w150 easyui-combobox"></select>
					<label>선정조건번호</label>
					<select id="select_auto_choice_cond_no" name="select_auto_choice_cond_no" data-options="editable:false" class="w80 easyui-combobox"></select>
					<label>선정일자</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${topsqlAutoChoice.strStartDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${topsqlAutoChoice.strEndDt}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					<label>튜닝담당자</label>
					<select id="selectTuner" name="selectTuner" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox"></select>
					<label>프로젝트</label>
					<select id="selectProject" name="selectProject" data-options="panelHeight:'auto',editable:false" class="w250 easyui-combobox"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('M');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(M)</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('S');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(S)</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:260px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn innerBtn2" style="margin-top:10px;">
			<a href="javascript:;" class="w100 easyui-linkbutton" onClick="endBatchTuningBundle();"><i class="btnIcon fas fa-stop-circle fa-lg fa-fw"></i> 일괄 튜닝 종료</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;margin-top:20px;min-height:280px">
			<table id="dtlTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/batchTuningBundle_popup.jsp" %> <!-- 성능개선 - 성능개선관리 상세 관련 팝업 -->
</body>
</html>