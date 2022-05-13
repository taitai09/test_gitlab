<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.06.01	이원식	최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 리포트 - 성능개선결과 산출물</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performanceImprovementOutputs.js?ver=<%=today%>"></script>        
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>
			</div>
			
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${tuningTargetSql.dbid}"/>
					<input type="hidden" id="user_auth" name="user_auth" value="${users.auth_cd}"/>
					<input type="hidden" id="searchKey" name="searchKey" value="${tuningTargetSql.searchKey}"/>
					<input type="hidden" id="bfac_chk_no" name="bfac_chk_no" value="${tuningTargetSql.bfac_chk_no}"/>
					<input type="hidden" id="tuning_no" name="tuning_no"/>
					<input type="hidden" id="choice_div_cd" name="choice_div_cd"/>
					<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
					<input type="hidden" id="choiceDivArry" name="choiceDivArry"/>
					<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt"/>
					<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w100 easyui-combobox"></select>
						<label>기준일자</label>
						<select id="strGubun" name="strGubun" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="01" <c:if test="${tuningTargetSql.strGubun eq '01'}">selected</c:if>>튜닝요청일자</option>
							<option value="02" <c:if test="${tuningTargetSql.strGubun eq '02'}">selected</c:if>>튜닝반려일자</option>
							<option value="03" <c:if test="${tuningTargetSql.strGubun eq '03' || tuningTargetSql.strGubun eq null}">selected</c:if>>튜닝완료일자</option>
							<option value="04" <c:if test="${tuningTargetSql.strGubun eq '04'}">selected</c:if>>튜닝적용일자</option>
							<option value="05" <c:if test="${tuningTargetSql.strGubun eq '05'}">selected</c:if>>튜닝종료일자</option>
						</select>
						<input type="text" id="strStartDt" name="strStartDt" value="${tuningTargetSql.strStartDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox"/> ~
						<input type="text" id="strEndDt" name="strEndDt" value="${tuningTargetSql.strEndDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox"/>
						<label>담당자</label>
						<select id="selectUserRoll" name="selectUserRoll" data-options="panelHeight:'auto',editable:false" class="w90 easyui-combobox">
							<option value="">전체</option>
							<sec:authorize access="hasRole('ROLE_DBMANAGER')">
								<option value="01">튜닝담당자</option>
							</sec:authorize>
							<option value="02">튜닝요청자</option>
							<option value="03">업무담당자</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}" class="w100 easyui-textbox"/>
						<label>SQL ID</label>
						<input type="text" id="sql_id" name="sql_id" value="${tuningTargetSql.sql_id}" class="w120 easyui-textbox"/>
					</div>
					<div class="multi">
						<label>소스파일명(Full Path)</label>
						<input type="text" id="tr_cd" name="tr_cd" value="${tuningTargetSql.tr_cd}" class="w120 easyui-textbox"/>
						<label>SQL식별자(DBIO)</label>
						<input type="text" id="dbio" name="dbio" value="${tuningTargetSql.dbio}" class="w120 easyui-textbox"/>
							
						<label>프로젝트</label>
						<select id="project_id" name="project_id" data-options="panelHeight:'300',editable:false" class="w220 easyui-combobox"></select>
						<label>튜닝진행단계</label>
						<select id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" data-options="panelHeight:'300',editable:false"  class="w220 easyui-combobox">
							<option value="">전체</option>
						</select>
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_DownloadOutputsAll();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 산출물 다운로드</a>
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:500px;">
			<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;padding-top:5px;">
				<table id="tableList" class="tbl easyui-datagrid" style="height:340px;" data-options="border:false">
				</table>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container START -->
</body>
</html>