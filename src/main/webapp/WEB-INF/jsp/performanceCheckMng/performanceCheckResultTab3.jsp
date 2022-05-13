<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2019.01.07
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능점검</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
<!-- 	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/> -->
<!-- 	<link rel="stylesheet" href="/resources/css/layout.css" /> -->
<!-- 	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script> -->
    <script type="text/javascript" src="/resources/js/ui/performanceCheckMng/performanceCheckResultTab3.js?ver=<%=today%>"></script> 
	<script type="text/javascript" src="/resources/js/ui/performanceCheckMng/bindSearchDialog.js?ver=<%=today%>"></script>
      
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<form:form method="post" id="tab_submit_form" name="tab_submit_form" class="form-inline">
		<input type="hidden" id="dbid" name="dbid" value="${defaultParsingSchemaInfo.dbid}"/>
		<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${performanceCheckMng.wrkjob_cd}"/>
		<input type="hidden" id="top_wrkjob_cd" name="top_wrkjob_cd" value="${performanceCheckMng.top_wrkjob_cd}"/>
		<input type="hidden" id="perf_check_result_div_nm" name="perf_check_result_div_nm" value="${performanceCheckMng.perf_check_result_div_nm}"/>
		<input type="hidden" id="perf_check_id" name="perf_check_id" value="${performanceCheckMng.perf_check_id}"/>
		<input type="hidden" id="perf_check_step_id" name="perf_check_step_id" value="${performanceCheckMng.perf_check_step_id}"/>
		<input type="hidden" id="program_id" name="program_id" value="${performanceCheckMng.program_id}"/>
		<input type="hidden" id="default_parsing_schema_name" name="default_parsing_schema_name" value="${defaultParsingSchemaInfo.parsing_schema_name}"/>
		<input type="hidden" id="program_type_cd" name="program_type_cd" value="${performanceCheckMng.program_type_cd}"/>
		<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd" value="${performanceCheckMng.sql_command_type_cd}"/>
		<input type="hidden" id="defaultPagingCnt" name="defaultPagingCnt" value="${defaultPagingCnt}"/>
		<input type="hidden" id="program_execute_tms" name="program_execute_tms" value="${performanceCheckMng.program_execute_tms}"/>
		<input type="hidden" id="pagingYn_chk" name="pagingYn_chk" value="${perfCheckMng.pagingYn}"/>
		<input type="hidden" id="pagingCnt_chk" name="pagingCnt_chk" value="${perfCheckMng.pagingCnt}"/>

		<!-- 바인드 검색 팝업관련 파라미터 -->		
		<input type="hidden" id="pop_perf_check_id" name="pop_perf_check_id" />
		<input type="hidden" id="pop_perf_check_step_id" name="pop_perf_check_step_id" />
		<input type="hidden" id="pop_program_id" name="pop_program_id" />
		<input type="hidden" id="pop_program_execute_tms" name="pop_program_execute_tms" />
		<input type="hidden" id="sql_id" name="sql_id" />
		<input type="hidden" id="snap_time" name="snap_time" />
		<input type="hidden" id="last_captured" name="last_captured" />
		
		<div id="contents">
			<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%">
				<div class="well">
					<label>수행회차</label>
					<input type="text" id="program_execute_tms1" name="program_execute_tms1" value="${performanceCheckMng.program_execute_tms}" class="w50 tac easyui-textbox" readonly/>
					<label>점검대상DB</label>
					<input type="text" id="db_name" name="db_name" value="${defaultParsingSchemaInfo.db_name}" class="w150 tac easyui-textbox" readonly/>
					<label>Parsing Schema Name</label>
<%-- 					<input type="text" id="parsing_schema_name" name="parsing_schema_name" value="${defaultParsingSchemaInfo.parsing_schema_name}" class="w100 tac easyui-textbox" readonly/> --%>
					<select id="parsing_schema_name" name="parsing_schema_name" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combobox"></select>
					<label>수행유형</label>
					<select id="program_exec_div_cd" name="program_exec_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>
					
					<c:if test="${performanceCheckMng.sql_command_type_cd eq 'SELECT'}">
						<label>페이징처리</label>
						<span id="check_span1" style="position: relative; top: -1px;padding-right:5px;">
							<input class="easyui-checkbox" id="pagingYn" name="pagingYn" value="Y">
						</span>
						<select id="pagingCnt" name="pagingCnt" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox"></select>
					</c:if>
					
					<div class="dtlBtn inlineBtn" style="margin-top:0px;">
	<%-- 					<sec:authorize access="hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')"> --%>
	<%-- 					</sec:authorize> --%>
	
						<c:set var="dataOptions" scope="page" value="disabled:true"/>
						<c:set var="bindSearchBtnClick" scope="page" value=""/>
						<c:set var="perfChkExecuteBtnClick" scope="page" value=""/>
	
						<sec:authorize access="hasAnyRole('ROLE_DEV')">
							<c:set var="dataOptions" scope="page" value="disabled:false"/>
							<c:set var="bindSearchBtnClick" scope="page" value="fnBindSearchBtnClick()"/>
							<c:set var="perfChkExecuteBtnClick" scope="page" value="fnPerfChkExecuteBtnClick()"/>
						</sec:authorize>
						<!-- 개발자가 아니면 성능점검수행 버튼 비활성화 -->
						<sec:authorize access="!hasAnyRole('ROLE_DEV')">
							<c:set var="dataOptions" scope="page" value="disabled:true"/>
							<c:set var="bindSearchBtnClick" scope="page" value=""/>
							<c:set var="perfChkExecuteBtnClick" scope="page" value=""/>
						</sec:authorize>
						<!-- 배포성능점검상태코드가 점검중(01)이 아니면... -->
						<c:if test="${deploy_check_status_cd ne '01'}">
							<c:set var="dataOptions" scope="page" value="disabled:true"/>
						</c:if>
	
						<!-- 프로그램유형이 온라인일 경우에만 성능점검수행 -->
						<c:if test="${performanceCheckMng.program_type_cd eq '1'}">
							<!-- SQL명령 유형이 SELECT일 경우에만 바인드값 처리 -->
							<c:if test="${performanceCheckMng.sql_command_type_cd eq 'SELECT'}">
								<a href="javascript:;" class="w110 easyui-linkbutton" id="bindSearchBtn" data-options="${dataOptions}" onclick="${bindSearchBtnClick}"><i class="btnIcon fab fa-digital-ocean fa-lg fa-fw"></i> 바인드 검색</a>
							</c:if>
							<a href="javascript:;" class="w120 easyui-linkbutton" id="perfChkExecuteBtn" data-options="${dataOptions}" onclick="${perfChkExecuteBtnClick}"><i class="btnIcon fas fa-keyboard fa-lg fa-fw"></i> 성능 점검 수행</a>
						</c:if>
						
					</div>
				</div>
			</div>
			<table id="testTbl" class="detailT">
				<colgroup>
<%-- 					<col style="width:38%;"/> --%>
					<col style="width:43%;"/>
<%-- 					<col style="width:75px"/> --%>
<%-- 					<col style="width:35px;"/> --%>
					<col style="width:3%;"/>
					<col style="width:43%;"/>
				</colgroup>
				<tr>
					<td style="vertical-align:top;">
						<textarea name="sql_text" id="sql_text" rows="30" style="padding:10px;margin-top:5px;width:97%;height:270px" wrap="soft" readonly>${perfCheckAllPgm.program_source_desc}</textarea>
					</td>
					<th style="vertical-align:top;border-left:1px solid #ddd;">
						<a href="javascript:;" class="w30 easyui-linkbutton" onClick="Btn_SetSQLFormatter();" title="Format SQL"><i class="btnIcon fas fa-align-left fa-lg fa-fw"></i></a>
					</th>
					<td style="vertical-align:top">
						<div style="height:300px;overflow-y:auto">
							<table id="bindTbl" class="detailT3" style="margin-top:5px;margin-bottom:5px;width:98%;">
								<colgroup>	
									<col style="width:10%;"/>
									<col style="width:35%;"/>
									<col style="width:20%;"/>
									<col style="width:35%;"/>
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
							
							<table id="bindTbl2" class="detailT3" style="margin-top:5px;margin-bottom:5px;width:98%;display:none;">
								<colgroup>	
									<col style="width:10%;"/>
									<col style="width:35%;"/>
									<col style="width:20%;"/>
									<col style="width:35%;"/>
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
								<c:choose>
									<c:when test="${fn:length(bindValueList) > 0}">
										<c:forEach items="${bindValueList}" var="bindValue" varStatus="status">
											<tr>
												<td class="tac">${bindValue.bind_seq}</td>
												<td class="tac">${bindValue.bind_var_nm}</td>
												<td class="tac">${bindValue.bind_var_type}</td>
												<td class="tac">
	<%-- 												<input type='text' id='compare_bind_var_value${status.index}' name='compare_bind_var_value' value='${bindValue.bind_var_value}' /> --%>
													<input type='hidden' id='compare_bind_var_value${status.index}' name='compare_bind_var_value' value="${bindValue.bind_var_value}" />
												</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4" class="tac">바인드 값이 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
				<tr>
<%-- 					<td colspan="2"><textarea name="sql_text" id="sql_text" rows="30" onBlur="setBindValue();" style="margin-top:5px;width:98%;height:300px" wrap="off">${apmApplSql.sql_text}</textarea></td> --%>
<%-- 					<td colspan="2"><textarea name="sql_text" id="sql_text" rows="30" style="margin-top:5px;width:98%;height:300px" wrap="off">${apmApplSql.sql_text}</textarea></td> --%>
				</tr>
			</table>
			<div id="testDiv" class="easyui-panel" data-options="border:false" style="width:100%;margin-top:10px;">

			</div>
		</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container END -->
    <%@include file="/WEB-INF/jsp/performanceCheckMng/bindSearchDialog.jsp" %>

</body>
</html>