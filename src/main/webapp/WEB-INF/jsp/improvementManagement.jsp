<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.02.21	이원식	OPENPOP V2 최초작업
 * 2020.06.03	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 개선 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/improvementManagement.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
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
					<input type="hidden" id="temp_dbio" name="temp_dbio" value=""/>
					<input type="hidden" id="user_auth" name="user_auth" value="${users.auth_cd}"/>
					<input type="hidden" id="tuning_status_cd" name="tuning_status_cd" value="${tuningTargetSql.tuning_status_cd}"/>
					<input type="hidden" id="searchKey" name="searchKey" value="${tuningTargetSql.searchKey}"/>
					<input type="hidden" id="bfac_chk_no" name="bfac_chk_no" value="${tuningTargetSql.bfac_chk_no}"/>
					<input type="hidden" id="tuning_no" name="tuning_no"/>
					<input type="hidden" id="search_choice_div_cd" name="search_choice_div_cd" value=""/>
					<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="${tuningTargetSql.choice_div_cd}"/>
					<input type="hidden" id="tuningStatusCd" name="tuningStatusCd" value="${tuningTargetSql.tuning_status_cd}"/>
					<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
					<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt"/>
					<input type="hidden" id="first_tuning_status_cd" name="first_tuning_status_cd" value="${tuningTargetSql.first_tuning_status_cd}"/>
<%-- 					<input type="hidden" id="headerBtnIsClicked" value="${headerBtnIsClicked}"/> --%>
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${tuningTargetSql.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="15"/>
<%-- 					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${tuningTargetSql.pagePerCount}"/> --%>
					
					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="panelHeight:'auto'" class="w120 easyui-combobox" required="true"></select>
						<label>기준일자</label>
						<select id="strGubun" name="strGubun" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="01" <c:if test="${tuningTargetSql.strGubun eq '01'}">selected</c:if>>튜닝요청일자</option>
							<option value="02" <c:if test="${tuningTargetSql.strGubun eq '02'}">selected</c:if>>튜닝반려일자</option>
							<option value="03" <c:if test="${tuningTargetSql.strGubun eq '03'}">selected</c:if>>튜닝완료일자</option>
							<option value="04" <c:if test="${tuningTargetSql.strGubun eq '04'}">selected</c:if>>튜닝적용일자</option>
							<option value="05" <c:if test="${tuningTargetSql.strGubun eq '05'}">selected</c:if>>튜닝종료일자</option>
						</select>
						<input type="text" id="strStartDt" name="strStartDt" value="${tuningTargetSql.strStartDt}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required="true"/> ~
						<input type="text" id="strEndDt" name="strEndDt" value="${tuningTargetSql.strEndDt}" class="w90 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false" required="true"/>
						<label>요청유형</label>
						<select id="selectChoiceDiv" name="selectChoiceDiv" data-options="panelHeight:'250px',editable:false" class="w180 easyui-combobox"></select>
						<label>진행상태</label>
						<select id="selectTuningStatus" name="selectTuningStatus" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox"></select>
						<label>완료여부</label>
						<select id="is_completed" name="is_completed" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox">
							<option value="" <c:if test="${tuningTargetSql.is_completed eq ''}">selected</c:if>>전체</option>
							<option value="Y" <c:if test="${tuningTargetSql.is_completed eq 'Y'}">selected</c:if>>완료</option>
							<option value="N" <c:if test="${tuningTargetSql.is_completed eq 'N'}">selected</c:if>>미완료</option>
						</select>
						<label>튜닝번호</label>
						<input type="number" id="search_tuning_no" name="search_tuning_no" value="${tuningTargetSql.search_tuning_no}" class="w60 easyui-numberbox" min="1"/>
					
						<label>담당자</label>
						<select id="selectUserRoll" name="selectUserRoll" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox">
							<option value="">전체</option>
							<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER')">
								<option value="01">튜닝담당자</option>
							</sec:authorize>
							<option value="02">튜닝요청자</option>
							<option value="03">업무담당자</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}" class="w90 easyui-textbox"/>
						<span class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('N');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
					</div>
					<div class="multi">
						<label>SQL ID</label>
						<input type="text" id="sql_id" name="sql_id" value="${tuningTargetSql.sql_id}" class="w100 easyui-textbox"/>
						<label>소스파일명(Full Path)</label>
						<input type="text" id="tr_cd" name="tr_cd" value="${tuningTargetSql.tr_cd}" class="w130 easyui-textbox"/>
						<label>SQL식별자(DBIO)</label>
						<input type="text" id="dbio" name="dbio" value="${tuningTargetSql.dbio}" class="w130 easyui-textbox"/>
						
						<label>프로젝트</label>
						<select id="project_id" name="project_id" data-options="panelHeight:'300',editable:false" class="w220 easyui-combobox"></select>
						<label>튜닝진행단계</label>
						<select id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" data-options="panelHeight:'300',editable:false"  class="w220 easyui-combobox">
							<option value="">전체</option>
						</select>
						<label>SQL점검팩</label>
						<select id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" class="w220 easyui-combobox" required="true" data-options="editable:false,panelHeight:'300'">
							<option value="">전체</option>
						</select>
						
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
			</div>
		</div>
		<!-- 성능개선 현황 요약 -->
		<div id="statusSummary" class="easyui-panel" data-options="border:false" style="overflow-y:auto;width:100%;margin-top:10px;margin-bottom:10px;height:90px">
			<div style="width:1230px;margin:auto;">
				<div class="status_all"><p class="title">&nbsp;전체</p><div id="process_all" style="cursor:pointer;" onClick="goStatusLink('');" class="count">0</div></div>
				<div class="status_step"><p class="title">&nbsp;튜닝요청</p><div id="process_1" style="cursor:pointer;" onClick="goStatusLink('1');" class="count">0</div></div>
				<div class="status_step"><p class="title">&nbsp;튜닝대기</p><div id="process_3" style="cursor:pointer;" onClick="goStatusLink('3');" class="count">0</div></div>
				<div class="status_step"><p class="title">&nbsp; 튜닝중</p><div id="process_5" style="cursor:pointer;" onClick="goStatusLink('5');" class="count">0</div></div>
				<div class="status_step"><p class="title">&nbsp;적용대기</p><div id="process_6" style="cursor:pointer;" onClick="goStatusLink('6');" class="count">0</div></div>
				<div class="status_all"><p class="title">&nbsp;튜닝종료</p><div id="process_8" style="cursor:pointer;" onClick="goStatusLink('8');" class="count">0</div></div>
				<div class="status_step2 status_last2"><p class="title">&nbsp;적용반려</p><div id="process_7" style="cursor:pointer;" onClick="goStatusLink('7');" class="count">0</div></div>
				<div class="status_step2 status_last2"><p class="title">&nbsp;튜닝반려</p><div id="process_4" style="cursor:pointer;" onClick="goStatusLink('4');" class="count">0</div></div>
			</div>
		</div>
		<!-- 성능개선 현황 요약 -->
		<div class="easyui-panel" data-options="border:false" style="width:100%;height:40px">
			<div class="searchBtn innerBtn">
				<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER')">
					<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showTuningAssignAllPopup();"><i class="btnIcon fas fa-street-view fa-lg fa-fw"></i> 튜닝담당자 지정</a>
				</sec:authorize>
				
				<sec:authorize access="hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')">
					<c:if test="${tuningTargetSql.tuning_status_cd ne '4' and tuningTargetSql.tuning_status_cd ne '7'}">
						<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_SaveTuning();"><i class="btnIcon fas fa-spinner fa-lg fa-fw"></i> 튜닝중 처리</a>
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_TuningCancelAll();"><i class="btnIcon fas fa-times-circle fa-lg fa-fw"></i> 튜닝취소 처리</a>
					</c:if>
				</sec:authorize>
				
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_RequestCancelAll();"><i class="btnIcon fas fa-times-circle fa-lg fa-fw"></i> 요청취소 처리</a>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:450px">
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
<%@include file="/WEB-INF/jsp/include/popup/tunerAssign_popup.jsp" %> <!-- 튜닝담당자 지정 팝업 -->
</body>
</html>