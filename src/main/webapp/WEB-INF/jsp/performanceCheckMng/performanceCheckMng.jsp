<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
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
	<title>성능 점검 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performanceCheckMng/performanceCheckMng.js?ver=<%=today%>"></script>       
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
    <script>
		var wrkjob_cd = '${performanceCheckMng.wrkjob_cd}';
		var deploy_request_dt_1 = '${performanceCheckMng.deploy_request_dt_1}';
		var deploy_request_dt_2 = '${performanceCheckMng.deploy_request_dt_2}';
		var deploy_check_status_cd = '${performanceCheckMng.search_deploy_check_status_cd}';
		var last_perf_check_step_id = '${performanceCheckMng.search_last_perf_check_step_id}';
		var deploy_requester_nm = '${performanceCheckMng.deploy_requester_nm}';
		var deploy_id = '${performanceCheckMng.deploy_id}';
		var perf_check_complete_meth_cd = '${performanceCheckMng.perf_check_complete_meth_cd}';
    </script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
	<div id="contents">
		<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>

			</div>
			<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value=""/>
					<input type="hidden" id="perf_check_id" name="perf_check_id" value=""/>
					<input type="hidden" id="perf_check_step_id" name="perf_check_step_id" value=""/>
					<input type="hidden" id="wrkjob_cd_nm" name="wrkjob_cd_nm" value=""/>
					<input type="hidden" id="top_wrkjob_cd" name="top_wrkjob_cd" value=""/>
					<input type="hidden" id="top_wrkjob_cd_nm" name="top_wrkjob_cd_nm" value=""/>
					<input type="hidden" id="search_wrkjob_cd" name="search_wrkjob_cd" value=""/>
					<input type="hidden" id="search_wrkjob_cd_nm" name="search_wrkjob_cd_nm" value=""/>
					<input type="hidden" id="perf_check_result_div_nm" name="perf_check_result_div_nm" value=""/>
					<input type="hidden" id="search_deploy_id" name="search_deploy_id" value=""/>
					<input type="hidden" id="search_deploy_check_status_cd" name="search_deploy_check_status_cd" value=""/>
					<input type="hidden" id="search_perf_check_step_id" name="search_perf_check_step_id" value=""/>
					<input type="hidden" id="search_last_perf_check_step_id" name="search_last_perf_check_step_id" value=""/>
					<input type="hidden" id="params" name="params" value=""/>
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${performanceCheckMng.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${performanceCheckMng.pagePerCount}"/>

					<div>
						<label>업무</label>
						<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'200px',editable:false,required:true" class="w100 easyui-combobox" required="true"></select>
						<label>배포요청일자</label>
						<input type="text" id="deploy_request_dt_1" name="deploy_request_dt_1" value="${performanceCheckMng.deploy_request_dt_1}" data-options="panelHeight:'auto',editable:false,required:true" required="true" class="w100 datapicker easyui-datebox"/> ~
						<input type="text" id="deploy_request_dt_2" name="deploy_request_dt_2" value="${performanceCheckMng.deploy_request_dt_2}" data-options="panelHeight:'auto',editable:false,required:true" required="true" class="w100 datapicker easyui-datebox"/>
						<label>점검상태</label>
						<select id="deploy_check_status_cd" name="deploy_check_status_cd" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox"></select>					
						<label>완료방법</label>
						<select id="perf_check_complete_meth_cd" name="perf_check_complete_meth_cd" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox"></select>					
						<label>최종점검단계</label>
						<select id="last_perf_check_step_id" name="last_perf_check_step_id" value="${performanceCheckMng.last_perf_check_step_id}" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>					
						<label>점검결과통보</label>
						<select id="check_result_anc_yn" name="check_result_anc_yn" value="${performanceCheckMng.check_result_anc_yn}" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
							<option value="">전체</option>
							<option value="Y">통보</option>
							<option value="N">미통보</option>
						</select>
					</div>
					<div class="multi">
						<sec:authorize access="!hasAnyRole('ROLE_DEV')">
							<label>배포요청자</label>
							<input type="text" id="deploy_requester_nm" name="deploy_requester_nm" value="${performanceCheckMng.deploy_requester_nm}" class="w150 easyui-textbox"/>
						</sec:authorize>
					
						<label>배포ID</label>
						<input type="text" id="deploy_id" name="deploy_id" value="${performanceCheckMng.deploy_id}" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" class="w70 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<sec:authorize access="hasAnyRole('ROLE_DEV','ROLE_DEV_DEPLOYMANAGER','ROLE_DEPLOYMANAGER')">
								<a href="javascript:;" id="perfChkRsltNoti" class="w120 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon far fa-bell fa-lg fa-fw"></i> 점검결과 통보</a>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('ROLE_DEPLOYMANAGER')">
								<a href="javascript:;" id="perfChkForceFinish" class="w120 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-stop-circle fa-lg fa-fw"></i> 강제 점검완료</a>
							</sec:authorize>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download(1);"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(上)</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download(2);"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀(下)</a>
						</div>
					</div>
			</div>
		</div>
		
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:640px">
			<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:355px;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:345px">
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="height:80%;">
						<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div class="searchBtn" data-options="region:'south',split:false,collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding-bottom:5px; cursor:default;">
				<table id="tableList2" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
		</div>
		
	</div>
	</form:form>
	<!-- contents END -->
</div>
<!-- container START -->
</body>
</html>