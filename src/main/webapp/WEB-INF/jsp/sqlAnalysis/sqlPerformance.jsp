<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.13	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 성능 분석</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />    
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/sqlAnalysis/sqlPerformance.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/sqlProfile_popup.js?ver=<%=today%>"></script> <!-- SQL Profile 팝업 -->
<!-- 	<script type="text/javascript" src="/resources/js/paging.js"></script> 그리드 페이징, 이전/다음버튼 처리 -->

</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<input type="hidden" id="dbid" name="dbid" value="${dbid}"/>
		<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
		<input type="hidden" id="currentPage" name="currentPage" value=""/>
		<input type="hidden" id="bindPage" name="bindPage" value="1"/>
		<input type="hidden" id="historyPage" name="historyPage" value="1"/>
		<input type="hidden" id="bindValueIsNull" name="bindValueIsNull" value="Y"/>
		<input type="hidden" id="sqlStats_sql_id" name="sqlStats_sql_id" value=""/>
		<input type="hidden" id="sqlStats_plan_hash_value" name="sqlStats_plan_hash_value" value=""/>
		<input type="hidden" id="sqlStats_elapsed_time" name="sqlStats_elapsed_time" value=""/>
		<input type="hidden" id="sqlStats_buffer_gets" name="sqlStats_buffer_gets" value=""/>
		<input type="hidden" id="rcount" name="rcount" value="10"/>
		<input type="hidden" id="enable_func" name="enable_func" value="${enable_func}"/>
			
		<!-- contents START -->
		<div id="contents" style="min-height:770px;">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
					<label>DB</label>
					<select id="selectDbidCombo" name="selectDbidCombo" data-options="editable:false" class="w120 easyui-combobox" required="true" ></select>
					<label>SQL_ID</label>
					<input type="text" id="sql_id" name="sql_id" value="${sql_id}" class="w120 easyui-textbox" required="true" />
					<label>PLAN_HASH_VALUE</label>
					<input type="text" id="plan_hash_value" name="plan_hash_value" value="${plan_hash_value}" class="w120 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_OnPlanViewClick('3');"><i class="btnIcon fas fa-file-powerpoint fa-lg fa-fw"></i> PLAN 보기</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:630px">
				<div data-options="region:'north',border:false" style="height:280px;">
					<div class="easyui-layout easyui-border-zero" data-options="fit:true" >
						<div data-options="region:'west',border:false" style="width:50%;height:50%;padding-right:5px;">
							<div class="easyui-tabs" data-options="fit:true,plain:true,border:false" >
								<div class="tabTxt" title="SQL TEXT" style="width:100%;padding:10px;">
									<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:98%;height:74%" wrap="off" readonly></textarea>
									<div class="searchBtn dtlBtn" style="margin-top:10px;">
										<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#textArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a>
										<a href="javascript:;" class="w130 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-recycle fa-lg fa-fw"></i> SQL Format</a>
									</div>
								</div>
							</div>
						</div>
						<div data-options="region:'center',split:false,border:false" style="height:50%;">
							<div id="tabsDiv" class="easyui-tabs" data-options="fit:true,border:false">
								<div title="Bind Value" class="tabGrid">
									<table id="bindValueList" class="tbl easyui-datagrid" data-options="fit:false,border:false" style="width:100%;height:190px;">
										<tbody><tr></tr></tbody>
									</table>
									<div class="searchBtn" style="margin-top:10px;">
										<a href="javascript:;" id="bindBtn" class="w80 easyui-linkbutton" onClick="Btn_NextBindSearch();"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
									</div>
								</div>
								<div title="OutLine" class="tabGrid">
									<table id="outLineList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
								<div title="유사 SQL" class="tabGrid">
									<table id="similarityList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
								<div title="Tree Plan" class="tabGrid" style="padding:5px;">
									<ul id="treePlan" class="easyui-tree" style="padding:5px;"></ul>
								</div>
								<div title="Text Plan" class="tabGrid">
									<ul id="textPlan" style="padding:5px;font-size:13px;white-space:nowrap;"></ul>
								</div>
								<div title="Grid Plan" class="tabGrid">
									<table id="sqlGridPlanList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div data-options="region:'center',split:false,border:false">
					<div class="searchBtn" style="margin-top:10px;">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_CopyBindValue();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 바인드 값 복사</a>
						
						<c:if test="${enable_func eq 'TRUE'}"> <!-- dbmanager 와 opmanager 권한만 -->
							<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_GoSQLAdvisor('1');"><i class="btnIcon fab fa-leanpub fa-lg fa-fw"></i> SQL Tuning Advisor</a>
							<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_GoSQLAdvisor('2');"><i class="btnIcon fab fa-leanpub fa-lg fa-fw"></i> SQL Access Advisor</a>
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_GoSQLAdvisor('3');"><i class="btnIcon fab fa-leanpub fa-lg fa-fw"></i> SQL Monitor</a>
						</c:if>
					</div>
				</div>
				<div data-options="region:'south',split:false,border:false" style="height:300px;">
					<div id="tableTab" class="easyui-tabs" data-options="fit:true">
						<div title="AWR SQL Stats" style="padding:5px;">
							<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="width:99%;height:210px;">
							</table>
							<div class="searchBtn dtlBtn" style="margin-top:10px;">
								<a href="javascript:;" id="historyBtn" class="w80 easyui-linkbutton" onClick="Btn_NextHistorySearch();"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
							</div>
						</div>
						<div title="PLAN별 SQL Stats" style="padding:5px;">
							<div class="searchBtn innerBtn dtlBtn">
								<a href="javascript:;" id="reqBtn" class="w90 easyui-linkbutton" data-options="disabled:true" onClick="Btn_TuningRequest();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청</a>
								
								<c:if test="${enable_func eq 'TRUE'}"> <!-- dbmanager 와 opmanager 권한만 -->
									<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Show_SqlProfilePopup('', 'tablePlanList');"><i class="btnIcon fas fa-check-square fa-lg fa-fw"></i> SQL Profile 적용</a>
								</c:if>
								
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_PlanDownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
							<table id="tablePlanList" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="width:99%;height:210px;">
							</table>
						</div>
						<div id="awrExecutionPlanDiv" title="AWR Execution Plan" style="font-family:'굴림체';font-size:11px;padding:10px;">
						</div>
					</div>
				</div>
			</div>
			
<!-- 				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a> -->
<!-- 				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a> -->
			<!-- <div class="searchBtn" id="pagingBtn1" data-options="region:'south',split:false,border:false" style="width:100%;height:15%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>-->
			<!-- <div class="searchBtn" id="pagingBtn2" data-options="region:'south',split:false,border:false" style="width:100%;height:15%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="prevBtnDisabled2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled2" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled2" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled2" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div> -->
		</div>
		<!-- contents END -->
	</form:form>
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/sqlProfile_popup.jsp" %> <!-- SQL Profile 팝업 -->
</body>
</html>