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
	<title>스케줄러 수행내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performScheduler.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${jobSchedulerExecLog.dbid}"/>
					<input type="hidden" id="job_exec_no" name="job_exec_no" />
					<input type="hidden" id="job_scheduler_type_cd" name="job_scheduler_type_cd" value="${jobSchedulerExecLog.job_scheduler_type_cd}"/>
					<input type="hidden" id="job_scheduler_type_nm" name="job_scheduler_type_nm"/>
					<input type="hidden" id="list_job_scheduler_type_cd" name="list_job_scheduler_type_cd"/>
					<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${jobSchedulerExecLog.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${jobSchedulerExecLog.pagePerCount}"/>
					
					<label>기준일자</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${jobSchedulerExecLog.strStartDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${jobSchedulerExecLog.strEndDt}" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:true" class="w120 easyui-combobox"></select>							
					<label>스케쥴러 유형</label>
					<select id="schedulerTypeCd" name="schedulerTypeCd" data-options="panelHeight:'auto',editable:false" class="w300 easyui-combobox"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>					
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>		
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:570px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>