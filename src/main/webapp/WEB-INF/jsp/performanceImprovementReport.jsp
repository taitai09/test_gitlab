<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.09.27	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능개선현황 보고서</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performanceImprovementReport.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<label>요청일자</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${perfList.strStartDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/>
					<input type="text" id="strEndDt" name="strEndDt" value="${perfList.strEndDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="true"/>
					<label>프로젝트</label>
					<select id="project_id" name="project_id" data-options="panelHeight:'300',editable:false" class="w220 easyui-combobox"></select>
					<label>튜닝진행단계</label>
					<select id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" data-options="panelHeight:'300',editable:false"  class="w220 easyui-combobox">
						<option value="">전체</option>
					</select>
						
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn innerBtn">
<!-- 						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a> -->
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="ExcelDownClick(0,'성능개선현황_보고서','성능개선현황_보고서');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
<!-- 		<div class="searchBtn innerBtn"> -->
<!-- 			<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a> -->
<!-- 		</div>		 -->
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:580px">
			<table id="tableList" name="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				<thead>
					<tr>
						<th colspan="2">구분</th>
						<th colspan="5">개선대상</th>
						<th colspan="8">처리단계</th>
					</tr>
					<tr>
						<th rowspan="3" data-options="field:'db_name',halign:'center',align:'center',width:'7%'">DB</th>
						<th rowspan="3" data-options="field:'db_abbr_nm',halign:'center',align:'center',width:'8%'">업무</th>
						<th colspan="2">요청</th>
						<th colspan="2">선정</th>
						<th rowspan="3" data-options="field:'improve_tot',halign:'center',align:'right',width:'7%'">총계</th>
						<th colspan="4">성능관리팀</th>
						<th colspan="4">업무개발팀</th>
					</tr>
					<tr>
						<th rowspan="2" data-options="field:'req_before',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">전일누적</th>
						<th rowspan="2" data-options="field:'req_today',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">당일추가</th>
						<th rowspan="2" data-options="field:'sel_before',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">전일누적</th>
						<th rowspan="2" data-options="field:'sel_today',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">당일추가</th>
						<th rowspan="2" data-options="field:'mng_analyzing',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">처리중</th>
						<th colspan="3">처리완료</th>
						<th rowspan="2" data-options="field:'dev_analyzing',halign:'center',align:'right',width:'7%',formatter:getNumberFormat">처리중</th>
						<th colspan="3">처리완료</th>
					</tr>
					<tr>
						<th data-options="field:'mng_complete',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">개선완료</th>
						<th data-options="field:'mng_not_improve',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">개선사항없음</th>
						<th data-options="field:'mng_error',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">오류</th>
						<th data-options="field:'dev_cancel',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">튜닝반려</th>
						<th data-options="field:'dev_complete',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">적용완료</th>
						<th data-options="field:'dev_apply_cancel',halign:'center',align:'right',width:'6%',formatter:getNumberFormat">적용반려</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>