<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.23	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>튜닝이력조회</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/tuningHistory.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
	<div id="contents">
		<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${tuningTargetSql.dbid}"/>
					<input type="hidden" id="tuning_no" name="tuning_no" value="${tuningTargetSql.tuning_no}"/>
					<input type="hidden" id="gubun" name="gubun" value="Tuning"/>							
					<input type="hidden" id="temp_start_tuning_complete_dt" name="temp_start_tuning_complete_dt" value="${tuningTargetSql.start_tuning_complete_dt}"/>							
					<input type="hidden" id="temp_end_tuning_complete_dt" name="temp_end_tuning_complete_dt" value="${tuningTargetSql.end_tuning_complete_dt}"/>							
					<input type="hidden" id="temp_searchKey" name="temp_searchKey" value="${tuningTargetSql.searchKey}"/>							
					<input type="hidden" id="temp_searchValue" name="temp_searchValue" value="${tuningTargetSql.searchValue}"/>							
					<input type="hidden" id="temp_program_type_cd" name="temp_program_type_cd" value="${tuningTargetSql.program_type_cd}"/>							
					<input type="hidden" id="temp_tuning_status_cd" name="temp_tuning_status_cd" value="${tuningTargetSql.tuning_status_cd}"/>							
					<input type="hidden" id="dbio" name="dbio" value="${tuningTargetSql.dbio}"/>							
					<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value=""/>							
					<input type="hidden" id="nowDate" value="${tuningTargetSql.end_tuning_complete_dt}">
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${tuningTargetSql.currentPage}"/>
<%-- 					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${tuningTargetSql.pagePerCount}"/> --%>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="22"/>

					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w100 easyui-combobox" required="true"></select>
					<label>튜닝완료일</label>
					<span id="span_tuning_complete_dt">
						<input type="text" id="start_tuning_complete_dt" name="start_tuning_complete_dt" value="" class="w100 datapicker easyui-datebox" required="true"/> ~
						<input type="text" id="end_tuning_complete_dt" name="end_tuning_complete_dt" value="" class="w100 datapicker easyui-datebox" required="true"/>
					</span>
					<label>프로그램 유형</label>
					<select id="program_type_cd" name="program_type_cd" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox" tabindex="4" required="true"></select>
					<label>진행상태</label>
					<select id="tuning_status_cd" name="tuning_status_cd" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox"></select>								
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="01">튜닝담당자</option>
						<option value="02">업무담당자</option>
						<option value="03">소스파일명(Full Path)</option>
						<option value="04">SQL식별자(DBIO)</option>
						<option value="05">인덱스명</option>
						<option value="06">MODULE</option>
						<option value="07">담당업무</option>
						<option value="08">SQLTEXT 테이블명</option>
						<option value="09">인덱스 테이블명</option>
					</select>
					<span id="spanSearchValue">
						<input type="text" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}" class="w150 easyui-textbox" data-options="required:false"/>
					</span>
					
					<div class="multi">
						<label>프로젝트</label>
						<select id="project_id" name="project_id" data-options="panelHeight:'300',editable:false" class="w220 easyui-combobox"></select>
						<label>튜닝진행단계</label>
						<select id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" data-options="panelHeight:'300',editable:false"  class="w220 easyui-combobox">
							<option value="">전체</option>
						</select>
						
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
					
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:600px">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>		
	</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>