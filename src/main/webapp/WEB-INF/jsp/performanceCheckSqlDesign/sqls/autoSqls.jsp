<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.08.31	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 1차 메뉴 > SQL 성능 추적 현황 2차 메뉴 > 성능 점검 SQL 3차 메뉴 > SQLs 탭</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckSqlDesign/sqls/autoSqls.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup_sqls.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		.datagrid-row{
			cursor: pointer;
		}
		.datagrid-row-over{
			cursor: pointer;
		}
		#btn_sqlInfo_pop {
			margin-right: 10px;
			position: absolute;
			top: 8px;
			right: 0px; 
			cursor: pointer;
			visibility: hidden;
			font-size: 16px;
			color: #38312a;
		}
		.sqlInfoTable {
			width: 100%;
		}
		.sqlInfoTable tr th {
			width: 50%;
			height: 30px;
			padding: 5px;
			background-color: #F3F2F2;
			font-size: 16px;
			font-weight: bold;
			text-align: left;
			line-height: 16px;
			color: #000000;
		}
		.sqlInfoTable tr th i {
			margin-right: 10px;
		}
		.sqlInfoTable tr th,
		.sqlInfoTable tr td {
			border: 1px solid #cccccc;
		}
		.sqlInfoTable tr:nth-child(2) td {
			padding-top: 5px;
		}
		.sqlInfoTable textarea {
			width: 99.8%;
			resize: none;
			overflow: hidden;
			white-space: pre-wrap;
			background-color: rgb(242, 243, 239);
			color: rgb(84, 84, 84);
			cursor: default;
			font-size: 11px;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" style="padding:5px;">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="db_name" name="db_name"/>
			
			<input type="hidden" id="base_period_value" name="base_period_value" value="2" />
			<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
			<input type="hidden" id="after_fail_yn_condition" name="after_fail_yn_condition" />
			<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="L"/>
			<input type="hidden" id="choice_cnt" name="choice_cnt"/>
			<input type="hidden" id="before_prd_sql_id_condition" name="before_prd_sql_id_condition"/>
			<input type="hidden" id="before_prd_plan_hash_value_condition" name="before_prd_plan_hash_value_condition"/>
			<input type="hidden" id="is_regress" name="is_regress"/>
			<input type="hidden" id="html" name="html"/>
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="${sqls.currentPage}"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqls.pagePerCount}"/>
			
			<input type="hidden" id="_isHandOff" name="_isHandOff" value="${isHandOff}"/>
			<input type="hidden" id="_is_check_highest_rank" name="_is_check_highest_rank" value="${isCheckHighestRank}"/>
			<input type="hidden" id="_wrkjob_cd" name="_wrkjob_cd" value="${wrkjob_cd}"/>
			<input type="hidden" id="_begin_dt" name="_begin_dt" value="${begin_dt}"/>
			<input type="hidden" id="_end_dt" name="_end_dt" value="${end_dt}"/>
			<input type="hidden" id="_base_period_value" name="_base_period_value" value="${base_period_value}"/>
			<input type="hidden" id="_is_check_fail" name="_is_check_fail" value="${isCheckFail}"/>
			<input type="hidden" id="_is_check_pass" name="_is_check_pass" value="${isCheckPass}"/>
			<input type="hidden" id="_select_search_type" name="_select_search_type" value="${selectSearchType}"/>
			<input type="hidden" id="_isRegressYn" name="_isRegressYn" value="${isRegressYn}"/>
			<input type="hidden" id="_select_perf_regressed_metric" name="_select_perf_regressed_metric" value="${selectPerfRegressedMetric}"/>
			
			<input type="hidden" id="_selectSqlPerfTrace" name="_selectSqlPerfTrace" value="${selectSqlPerfTrace}"/>
			<input type="hidden" id="_selectElapsedTimeMetirc" name="_selectElapsedTimeMetirc" value="${selectElapsedTimeMetirc}"/>
			
			<div class="easyui-panel searchArea100" data-options="border:false" style="width:100%">
				<div class="well">
					<div>
						<label>업무</label>
						<select id="wrkjob_cd_top_level" name="wrkjob_cd_top_level" data-options="panelHeight:'300px',editable:true,required:true" class="w200 easyui-combotree"></select>
						<span style="margin-left:5px"/>
						<input id="checkHighestRankYn" name="checkHighestRankYn" class="easyui-checkbox">
						<label style="margin-left:0px;">최상위</label>
						<span style="margin-left:20px"/>
						<label>배포일자</label>
						<span id="span_analysis_day">
							<input type="text" id="begin_dt" name="begin_dt" value="${sqls.begin_dt}" class="w100 datapicker easyui-datebox" required="true"/> ~
							<input type="text" id="end_dt" name="end_dt" value="${sqls.end_dt}" class="w100 datapicker easyui-datebox" required="true"/>
						</span>
						<span style="margin-left:5px;"/>
						<input class="easyui-radiobutton" id="base_daily" name="base_daily" value="1" label="1일" labelPosition="after" labelWidth="40px;">
						<input class="easyui-radiobutton" id="base_weekly" name="base_weekly" value="2" label="1주일" labelPosition="after" labelWidth="40px;">
						<input class="easyui-radiobutton" id="base_monthly" name="base_monthly" value="3" label="1개월" labelPosition="after" labelWidth="40px;">
						<span style="margin-left:20px"/>
						<input id="checkFail" name="checkFail" class="easyui-checkbox">
						<label style="margin-left:0px;">부적합</label>
						<input id="checkPass" name="checkPass" class="easyui-checkbox">
						<label style="margin-left:0px;">적합</label>
					</div>
					<div class="multi">
						<span style="margin-right: 20px;">
							<label>검색조건</label>
							<select id="selectSearchType" name="selectSearchType" data-options="panelHeight:'auto',editable:false,editable:true,required:true" class="w120 easyui-combobox">
								<option value="00">전체</option>
								<option value="01">성능 검증 SQL</option>
								<option value="02">예외 처리 SQL</option>
							</select>
						</span>
						<span id="regress_type">
							<input id="checkRegress" name="checkRegress" class=""easyui-checkbox""/>
							<label style="margin-left: 0px;margin-right: 0px;">성능 저하</label>
							<input id="checkImprove" name="checkImprove" class=""easyui-checkbox""/>
							<label style="margin-left: 0px;margin-right: 0px;">성능 향상</label>
							<span id="perfRegressedMetric" style="margin-left:0px">
								<label>블럭수</label>
								<select id="selectPerfRegressedMetric" name="selectPerfRegressedMetric" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
									<option value="">전체</option>
									<option value="2XLT">&#60; 2x</option>
									<option value="5XLT">&#60; 5x</option>
									<option value="10XLT">&#60; 10x</option>
									<option value="30XLT">&#60; 30x</option>
									<option value="50XLT">&#60; 50x</option>
									<option value="100XLT">&#60; 100x</option>
									<option value="100XMT">&#62;&#61; 100</option>
								</select>
							</span>
						</span>
						<span id="sql_perf_trace" style="margin-right: -45px;">
							<label>예외처리방법</label>
							<select id="selectSqlPerfTrace" name="selectSqlPerfTrace" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							</select>
							<label>수행시간</label>
							<select id="selectElapsedTimeMetirc" name="selectElapsedTimeMetirc" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
								<option value="">전체</option>
								<option value="0.1XLT">&#60;&#61; 0.1s</option>
								<option value="0.3XLT">&#60; 0.3s</option>
								<option value="1XLT">&#60; 1s</option>
								<option value="3XLT">&#60; 3s</option>
								<option value="10XLT">&#60; 10s</option>
								<option value="60XLT">&#60; 60s</option>
								<option value="60XMT">&#62;&#61; 60s</option>
							</select>
						</span>
						<span style="margin-left: 60px;"/>
						<label>SQL ID</label>
						<input type="text" id="strSqlId" name="strSqlId" class="w150 easyui-textbox"/>
						<span style="margin-left:20px"/>
						<span style="margin-left:20px"/>
						<label>SQL 식별자</label>
						<input type="text" id="strDbio" name="strDbio" class="w200 easyui-textbox"/>
						<span style="margin-left:20px"/>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						<div class="searchBtn innerBtn">
<!-- 							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="testTableList();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> Test</a> -->
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;height:275px;">
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;height:210px;padding-top:5px;">
					<table id="tableList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:40px;padding-top:5px;text-align:right;">
					<div class="searchBtnLeft2">
						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="showTuningReqPopup();"><i class="btnIcon fas fa-check-circle fa-lg fa-fw"></i> 튜닝대상선정</a>
					</div>
					<div class="searchBtn">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:295px">
				<div id="bottomSqlsTab" class="easyui-tabs" data-options="fit:true,border:false,plain:true">
					<div title="SQL 성능 추적" data-options="fit:true" style="padding:5px;overflow-y: hidden;">
						<iframe id="bottomSqlsTabDesignIF1" name="bottomSqlsTabDesignIF1" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
								style="width:100%;min-height:680px;"></iframe>
					</div>
					<div title="SQL Info<i class='fas fa-expand-arrows-alt' id='btn_sqlInfo_pop'></i>" data-options="fit:true" style="padding-top:5px;border-bottom:1px solid;">
						<iframe id="bottomSqlsTabDesignIF2" name="bottomSqlsTabDesignIF2" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
								style="width: 100%;min-height: 540px;"></iframe>
					</div>
					<div title="성능 검증 결과" data-options="fit:true" style="padding-top:5px;border-bottom:1px solid";">
						<iframe id="bottomSqlsTabDesignIF3" name="bottomSqlsTabDesignIF3" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
								style="width: 100%;min-height: 280px;"></iframe>
					</div>
					<div id="sqlsProgramInfo" title="프로그램 정보" data-options="fit:true" style="padding:5px;overflow-y: hidden;">
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<div id="sqlInfoPop" class="easyui-window popWin" style="background-color:#ffffff;width:1070px;height:550px;">
	<div class="easyui-layout" data-options="fit:true" style="width:100%;height: 550px;">
		<form:form method="post" id="compare_form" name="" class="form-inline" value="">
			<input type="hidden" name="program_id" value="">
			<input type="hidden" name="program_execute_tms" value="">
			<input type="hidden" name="perf_check_step_id" value="">
			<input type="hidden" name="perf_check_id" value="">
			<input type="hidden" name="dbid" value="">
			<input type="hidden" name="after_prd_sql_id" value="">
			<input type="hidden" name="after_prd_plan_hash_value" value="">
		</form:form>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
			<div data-options="title:'운영SQL',region:'center',split:false,border:false" style="width:100%;height:100%;">
<!-- 				<div id="pop_tabs" class="easyui-tabs" data-options="fit:true,border:false" style="width:100%;height:100%;"> -->
					<div title="SQL TEXT" class="tabGrid easyui-layout" style="width:100%;padding:1px;height:50%;overflow:hidden;">
						<table class="sqlInfoTable" style="overflow: scroll;height:100%;">
							<tr>
								<td width="79.8%;">
									<textarea id="sql_text_operation" readonly style="height: 100%;overflow:auto;"></textarea>
								</td>
								<td width="0.2%;"></td>
								<td width="20%;">
									<textarea id="sql_bind_operation" readonly style="height: 98%;overflow:auto;"></textarea>
								</td>
							</tr>
						</table>
					</div>
					<div class="easyui-panel" data-options="border:false" style="width:100%;padding:1px;height:50%;overflow:auto;">
						<table id="sql_plan_operation" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
<!-- 				</div> -->
			</div>
			<div data-options="region:'south'" style="height:50px;" >
				<div class="inlineBtn" style="padding-top:8px;padding:5px;float: right;">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_CloseSqlInfoPop();"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/jsp/include/popup/message_dialog.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/tunerAssign_popup_sqls.jsp" %> <!-- 튜닝담당자 지정 팝업 -->
</body>
</html>