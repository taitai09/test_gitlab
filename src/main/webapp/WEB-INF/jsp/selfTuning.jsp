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
	<title>SQL 셀프 성능 점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/selfTuning.js?ver=<%=today%>"></script> 
    <script type="text/javascript" src="/resources/js/ui/include/popup/selfIndexAutoDesign_popup.js?ver=<%=today%>"></script> <!-- 성능개선 - 셀프튜닝 - 인덱스 자동설계  팝업 -->
    <script type="text/javascript" src="/resources/js/ui/include/popup/selfTuningExplain_popup.js?ver=<%=today%>"></script> <!-- 성능개선 - 셀프튜닝 - Explain Plan 정보  팝업 -->
    <script type="text/javascript" src="/resources/js/ui/include/popup/selfTuningGuide_popup.js?ver=<%=today%>"></script> <!-- 성능개선 - 셀프튜닝 - 인덱스/튜닝체크 가이드  팝업 -->
    <script type="text/javascript" src="/resources/js/ui/include/popup/selfTuningSql_popup.js?ver=<%=today%>"></script> <!-- 성능개선 - 셀프튜닝 - SQL Statistics 정보  팝업 -->
<style>
</style>      
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="uuid" name="uuid" value="${uuid}"/>
		<input type="hidden" id="dbid" name="dbid" value="${apmApplSql.dbid}"/>
		<input type="hidden" id="tr_cd" name="tr_cd" value="${apmApplSql.dbid}"/>
		<input type="hidden" id="dbio" name="dbio" value="${apmApplSql.dbio}"/>
		<input type="hidden" id="current_elap_time" name="current_elap_time"/>							
		<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${apmApplSql.wrkjob_cd}"/>
		<input type="hidden" id="searchKey" name="searchKey" value="${apmApplSql.searchKey}"/>

		
		<input type="hidden" id="sql_id" name="sql_id"/>
		<input type="hidden" id="ndv_ratio" name="ndv_ratio"/>
		<input type="hidden" id="col_null" name="col_null"/>
		<input type="hidden" id="idx_ad_no" name="idx_ad_no"/>
		<input type="hidden" id="selftun_query_seq" name="selftun_query_seq"/>
		<input type="hidden" id="dbio_search_yn" name="dbio_search_yn" value="${dbio_search_yn}"/>
		<input type="hidden" id="sql_desc" name="sql_desc" value=""/>
		
		<div id="contents">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<c:set var="dbioSearchStatus" scope="page" value=""/>
				<c:if test="${dbio_search_yn eq 'N'}"> <!-- APM연동 체크 -->
					<c:set var="dbioSearchStatus" scope="page" value="disabled:true"/>						
				</c:if>				
				<div class="well">						
					<label>업무구분</label>
					<select id="selectWrkJob" name="selectWrkJob" data-options="editable:false" class="w150 easyui-combobox"></select>&nbsp;&nbsp;
					<c:if test="${dbio_search_yn eq 'Y'}"> <!-- APM연동 체크 -->
						<select id="selectKey" name="selectKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
							<option value="">선택</option>
							<option value="01">애플리케이션</option>
							<option value="02">SQL식별자(DBIO)</option>
						</select>&nbsp;&nbsp;
						<input type="text" id="searchValue" name="searchValue" value="${apmApplSql.searchValue}" class="w200 easyui-textbox"/>
					</c:if>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" data-options="${dbioSearchStatus}" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</div>
			</div>
			<c:if test="${dbio_search_yn eq 'Y'}"> <!-- APM연동 체크 -->
				<div class="easyui-panel" data-options="border:false" style="width:100%">
					<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> SQL식별자(DBIO) 목록</span></div>
					<div class="dtlBtn inlineBtn" style="margin-top:-13px;">
						<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_TableShowYn('dbio','Y');"><i class="btnIcon fas fa-eye fa-lg fa-fw"></i> 보이기</a>
						<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_TableShowYn('dbio','N');"><i class="btnIcon fas fa-eye-slash fa-lg fa-fw"></i> 숨기기</a>
					</div>						
				</div>
				<div id="dbioDiv" class="easyui-panel" data-options="border:false" style="padding-left:5px;min-height:230px">
					<table id="dbioList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</c:if>
			<div class="easyui-panel" data-options="border:false" style="width:100%;margin-top:10px;">
				<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 성능 테스트</span></div>
				<div class="dtlBtn inlineBtn" style="margin-top:-13px;">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ShowIndexGuide();"><i class="btnIcon fas fa-info fa-lg fa-fw"></i> 인덱스 가이드</a>
<!-- 					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="WinOpen_ShowIndexGuide();"><i class="btnIcon fas fa-info fa-lg fa-fw"></i> 인덱스 가이드</a> -->
					<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_ShowTuningCheckGuide();"><i class="btnIcon fas fa-info fa-lg fa-fw"></i> 튜닝체크 리스트</a>
<!-- 					<a href="javascript:;" class="w110 easyui-linkbutton" onClick="WinOpen_ShowTuningCheckGuide();"><i class="btnIcon fas fa-info fa-lg fa-fw"></i> 튜닝체크 리스트</a> -->
					<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_TableShowYn('test','Y');"><i class="btnIcon fas fa-eye fa-lg fa-fw"></i> 보이기</a>
					<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_TableShowYn('test','N');"><i class="btnIcon fas fa-eye-slash fa-lg fa-fw"></i> 숨기기</a>
				</div>						
			</div>
			<table id="testTbl" class="detailT">
				<colgroup>	
					<col style="width:38%;"/>
					<col style="width:28%;"/>
					<col style="width:75px"/>
					<col style="width:40%;"/>
				</colgroup>
				<tr>
					<th>Parsing Schema Name : <select id="parsing_schema_name" name="parsing_schema_name" data-options="editable:true" class="w180 easyui-combobox"></select></th>
<!-- 					<th>SQL TEXT 변경시 바인드 변수 리스트가 자동 초기화 세팅됩니다.</th> -->
					<th>화면에서 입력되는 조건에 대해 반드시 바인드 변수 형태로 입력되어야 합니다.</th>
					<td rowspan="2">
						<a href="javascript:;" class="w60 easyui-linkbutton" onClick="setBindValue();">변수추출</a>
					</td>
					<td rowspan="2" style="vertical-align:top">
						<table id="bindTbl" class="detailT" style="margin-top:5px;margin-bottom:5px;width:98%;">
							<colgroup>	
								<col style="width:16%;"/>
								<col style="width:28%;"/>
								<col style="width:28%;"/>
								<col style="width:28%;"/>
							</colgroup>
							<thead>
								<tr>
									<th>순번</th>
									<th>변수명</th>
									<th>변수타입</th>
									<th>변수값</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</td>							
				</tr>
				<tr>
<%-- 					<td colspan="2"><textarea name="sql_text" id="sql_text" rows="30" onBlur="setBindValue();" style="margin-top:5px;width:98%;height:300px" wrap="off">${apmApplSql.sql_text}</textarea></td> --%>
					<td colspan="2"><textarea name="sql_text" id="sql_text" rows="30" style="margin-top:5px;width:98%;height:300px" wrap="off">${apmApplSql.sql_text}</textarea></td>
				</tr>
			</table>
			<div id="testDiv" class="easyui-panel" data-options="border:false" style="width:100%;margin-top:10px;">
				<div class="dtlBtn inlineBtn" style="margin-top:0px;">
					<sec:authorize access="hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')">
						<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_GoSQLAdvisor('1');"><i class="btnIcon fab fa-leanpub fa-lg fa-fw"></i> SQL Tuning Advisor</a>
						<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_GoSQLAdvisor('2');"><i class="btnIcon fab fa-leanpub fa-lg fa-fw"></i> SQL Access Advisor</a>
					</sec:authorize>
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ReqExplainPlan();"><i class="btnIcon far fa-calendar-check fa-lg fa-fw"></i> Explain Plan</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_RequestTuning();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
					<a href="javascript:;" class="w110 easyui-linkbutton" onClick="showIndexAutoDesign();"><i class="btnIcon fab fa-digital-ocean fa-lg fa-fw"></i> 인덱스 자동설계</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_SelfTuningTest();"><i class="btnIcon fas fa-keyboard fa-lg fa-fw"></i> 셀프테스트</a>
				</div>
			</div>
			<div id="selfTuningTab" class="easyui-tabs" data-options="border:false" style="width:100%;padding-bottom:10px;height:400px;">
				<div title="SQL Statistics" style="padding:5px;">
					<table id="testResultList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div title="Execution Plan" style="padding:5px">
					<ul id="textPlan" style="font-family:'굴림체';padding:5px;font-size:13px;">
					</ul>
				</div>
				<div title="Explain Plan" style="padding:5px">
					<ul id="treePlan" class="easyui-tree" style="padding:5px;">
					</ul>
				</div>
			</div>
		</div>
	</form:form>			
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/selfIndexAutoDesign_popup.jsp" %> <!-- 성능개선 - 셀프튜닝 - 인덱스 자동설계  팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/selfTuningExplain_popup.jsp" %> <!-- 성능개선 - 셀프튜닝 - Explain Plan 정보  팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/selfTuningGuide_popup.jsp" %> <!-- 성능개선 - 셀프튜닝 - 인덱스/튜닝체크 가이드  팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/selfTuningSql_popup.jsp" %> <!-- 성능개선 - 셀프튜닝 - SQL Statistics 정보  팝업 -->

</body>
</html>