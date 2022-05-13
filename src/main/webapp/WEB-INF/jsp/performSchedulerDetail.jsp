<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.14	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>스케줄러 수행내역 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performSchedulerDetail.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm} - [수행번호 : ${jobSchedulerExecDetailLog.job_exec_no} - ${jobSchedulerExecDetailLog.job_scheduler_type_nm}] 상세</span>

			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${jobSchedulerExecDetailLog.dbid}"/>
					<input type="hidden" id="strStartDt" name="strStartDt" value="${jobSchedulerExecDetailLog.strStartDt}"/>
					<input type="hidden" id="strEndDt" name="strEndDt" value="${jobSchedulerExecDetailLog.strEndDt}"/>
					<input type="hidden" id="job_exec_no" name="job_exec_no" value="${jobSchedulerExecDetailLog.job_exec_no}"/>
					<input type="hidden" id="list_job_scheduler_type_cd" name="list_job_scheduler_type_cd" value="${jobSchedulerExecDetailLog.list_job_scheduler_type_cd}"/>
					<input type="hidden" id="job_scheduler_type_cd" name="job_scheduler_type_cd" value="${jobSchedulerExecDetailLog.job_scheduler_type_cd}"/>
					<input type="hidden" id="job_err_yn" name="job_err_yn" value="${jobSchedulerExecDetailLog.job_err_yn}"/>
					<input type="hidden" id="hndop_job_exec_yn" name="hndop_job_exec_yn" value="${jobSchedulerExecDetailLog.hndop_job_exec_yn}"/>
					<input type="hidden" id="menu_nm" name="menu_nm" value="${jobSchedulerExecDetailLog.menu_nm}"/>
					<label>작업 오류 조회</label>
					<input type="checkbox" id="chkJobErrYn" name="chkJobErrYn" value="" class="w120 easyui-switchbutton"/>
					<label>수동 작업 조회</label>
					<input type="checkbox" id="chkHndopJobExecYn" name="chkHndopJobExecYn" value="" class="w120 easyui-switchbutton"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-undo fa-lg fa-fw"></i> 돌아가기</a>
					</span>							
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:670px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>