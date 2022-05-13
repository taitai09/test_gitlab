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
	<title>수동선정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/sqlTuningTarget/manualSelection.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/manualSelection_popup.js?ver=<%=today%>"></script> <!-- 성능진단 - SQL튜닝대상선정 - 수동선정 이력 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/snapId_popup.js?ver=<%=today%>"></script> <!-- snap id 조회 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
    
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchAreaMulti2" data-options="border:false" style="width:100%">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${odsHistSqlstat.dbid}"/>
					<input type="hidden" id="order_div_cd" name="order_div_cd"/>							
					<input type="hidden" id="before_choice_sql_except_yn" name="before_choice_sql_except_yn" value="${odsHistSqlstat.before_choice_sql_except_yn}"/>
					<input type="hidden" id="perfr_auto_assign_yn" name="perfr_auto_assign_yn"/>
					<input type="hidden" id="elap_time" name="elap_time"/>
					<input type="hidden" id="buffer_cnt" name="buffer_cnt" value="${odsHistSqlstat.dbid}"/>
					<input type="hidden" id="perfr_id" name="perfr_id"/>
					<input type="hidden" id="choice_cnt" name="choice_cnt"/>
					<input type="hidden" id="choice_tms" name="choice_tms"/>
					<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="2"/>
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					<ipnut type="hidden" id="call_from_parent" name="call_from_parent" value="${odsHistSqlstat.call_from_parent}"/>
					
<%-- 					<input type="hidden" id="elapsed_time" name="elapsed_time" value="${odsHistSqlstat.elapsed_time}"/> --%>
<%-- 					<input type="hidden" id="buffer_gets" name="buffer_gets" value="${odsHistSqlstat.buffer_gets}"/> --%>
<%-- 					<input type="hidden" id="executions" name="executions" value="${odsHistSqlstat.executions}"/> --%>
<%-- 					<input type="hidden" id="topn_cnt" name="topn_cnt" value="${odsHistSqlstat.topn_cnt}"/> --%>
					
					<div>
						<label>DB</label>
						<select id="selectCombo" name="selectCombo" data-options="editable:false" required="true" class="w100 easyui-combobox"></select>
						<label>SNAP ID</label>
						<input type="text" id="start_snap_id" name="start_snap_id" value="${odsHistSqlstat.start_snap_id}" data-options="panelHeight:'auto',editable:false" required="true" class="w60 easyui-textbox"/> ~
						<input type="text" id="end_snap_id" name="end_snap_id" value="${odsHistSqlstat.end_snap_id}" data-options="panelHeight:'auto',editable:false" required="true" class="w60 easyui-textbox"/>
						&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" onClick="showSnapPopup1('manual');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></a>
						&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="w100 easyui-linkbutton" onClick="showHistoryPopup();"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 수동선정이력</a>
						<label>이전 튜닝대상 선정 SQL 제외</label>
						<input type="checkbox" id="chkExcept" name="chkExcept" value="${odsHistSqlstat.before_choice_sql_except_yn}" class="w120 easyui-switchbutton"/>

						<label>Elapsed Time(sec) >= </label>
						<input type="number" id="elapsed_time" name="elapsed_time" value="${odsHistSqlstat.elapsed_time}" data-options="panelHeight:'auto'" required="true" class="w60 easyui-textbox" step="0.01"/>
						<label>Buffer Gets >= </label>
						<input type="number" id="buffer_gets" name="buffer_gets" value="${odsHistSqlstat.buffer_gets}" data-options="panelHeight:'auto'" required="true" class="w60 easyui-textbox" step="0.01"/>
						<label>Executions >= </label>
						<input type="number" id="executions" name="executions" value="${odsHistSqlstat.executions}" data-options="panelHeight:'auto'" required="true" class="w60 easyui-textbox" step="0.01"/>
						<label>TOP N</label>
						<input type="number" id="topn_cnt" name="topn_cnt" value="${odsHistSqlstat.topn_cnt}" data-options="panelHeight:'auto'" required="true" class="w60 easyui-textbox"/>
					</div>
					<div class="multi">
						
						<label>Ordered</label>
						<select id="selectOrdered" name="selectOrdered" value="${odsHistSqlstat.selectOrdered}" data-options="panelHeight:'auto',editable:false" required="true" class="w120 easyui-combobox">
							<option value="" <c:if test="${odsHistSqlstat.selectOrdered eq ''}">selected</c:if>>선택</option>
							<option value="1" <c:if test="${odsHistSqlstat.selectOrdered eq '1'}">selected</c:if>>Elapsed Time</option>
							<option value="2" <c:if test="${odsHistSqlstat.selectOrdered eq '2'}">selected</c:if>>CPU Time</option>
							<option value="3" <c:if test="${odsHistSqlstat.selectOrdered eq '3'}">selected</c:if>>Buffer Gets</option>
							<option value="4" <c:if test="${odsHistSqlstat.selectOrdered eq '4'}">selected</c:if>>Physical Reads</option>
							<option value="5" <c:if test="${odsHistSqlstat.selectOrdered eq '5'}">selected</c:if>>Executions</option>
						</select>
						<label>MODULE</label>
						<input type="text" id="module" name="module" class="w120 easyui-textbox"/>
						<label>Parsing Schema Name</label>
						<input type="text" id="parsing_schema_name" name="parsing_schema_name" class="w120 easyui-textbox"/>
						<label>SQL TEXT</label>
						<input type="text" id="sql_text" name="sql_text" class="w120 easyui-textbox"/>
					</div>
					<div class="multi">
						<label>MODULE(일괄제외)</label>
						<input type="text" id="excpt_module_list" name="excpt_module_list" class="w200 easyui-textbox" placeholder="형태로 입력해주세요." />
						<label>Parsing Schema Name(일괄제외)</label>
						<input type="text" id="excpt_parsing_schema_name_list" name="excpt_parsing_schema_name_list" class="w200 easyui-textbox"/>
						<label>SQL ID(일괄제외)</label>
						<input type="text" id="excpt_sql_id_list" name="excpt_sql_id_list" class="w200 easyui-textbox"/>
						<label>검색건수</label>
						<select id="selectRcount" name="selectRcount" data-options="panelHeight:'auto',editable:false" required="true" class="w60 easyui-combobox">
							<option value="10" selected>10</option>
							<option value="20">20</option>
							<option value="30">30</option>
							<option value="40">40</option>
							<option value="50">50</option>
						</select>
						
						<span class="searchBtnLeft multiBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:500px">
				<!-- type 1 -->
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div class="innerBtn2">
				<div class="searchBtnLeft2">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_ManualChoiceStatus();"><i class="btnIcon fas fa-file-alt fa-lg fa-fw"></i> 선정 현황</a>
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="showTuningReqPopup();"><i class="btnIcon fas fa-check-circle fa-lg fa-fw"></i> 튜닝대상선정</a>
				</div>
				<div class="searchBtn">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/manualSelection_popup.jsp" %> <!-- 성능진단 - SQL튜닝대상선정 - 수동선정 이력 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/snapId_popup.jsp" %> <!-- snap id 조회 팝업 -->
<%@include file="/WEB-INF/jsp/include/popup/tunerAssign_popup.jsp" %> <!-- 튜닝담당자 지정 팝업 -->
</body>
</html>