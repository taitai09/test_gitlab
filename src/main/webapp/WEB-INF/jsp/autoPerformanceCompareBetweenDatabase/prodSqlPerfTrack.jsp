<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.11.05	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능검증 :: DB간 SQL성능비교 :: 운영 SQL 성능 추적</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
<%-- 	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script> --%>
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadExplainPlan_operation_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/prodSqlPerfTrack.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="select_sql" name="select_sql"/>
			<input type="hidden" id="defaultText" name="defaultText"/>
			<input type="hidden" id="select_perf_impact" name="select_perf_impact"/>
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="operation_dbid" name="operation_dbid"/>
			<input type="hidden" id="asis_plan_hash_value" name="asis_plan_hash_value"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="nowDate" name="nowDate" value="${nowDate}"/>
			<input type="hidden" id="startDate" name="startDate" value="${startDate}"/>
			<input type="hidden" id="endDate" name="endDate" value="${endDate}"/>
			<input type="hidden" id="sqlExclude" name="sqlExclude" value=""/>
			<input type="hidden" id="db_operate_type_cd" name="db_operate_type_cd"/>
			<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="easyui-panel multi" data-options="border:false" style="width:100%">
				<div class="well">
					<table>
						<colgroup>
							<col style="width:6%">
							<col style="width:10%">
							<col style="width:5%">
							<col style="width:15%">
							<col style="width:5%">
							<col style="width:10%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:10%">
							<col style="width:5%">
							<col style="width:5%">
						</colgroup>
						<tr style="height:30px;">
							<td><label>프로젝트</label></td>
							<td colspan="3">
								<select id="project_id" name="project_id" class="w350 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td><label>SQL점검팩</label></td>
							<td colspan="2">
								<select id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" class="w350 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td><label>운영DB</label></td>
							<td colspan="2">
								<select id="operation_dbid_cd" name="operation_dbid_cd" class="w350 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
						</tr><tr style="height:5px;"></tr>
						<tr style="height:30px;">
							<td><label>SQL 수행일자</label></td>
							<td colspan="2" id="span_analysis_day">
								<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" class="w100 datapicker easyui-datebox" required="true"/> ~
								<input type="text" id="strEndDt" name="strEndDt" value="${endDate}" class="w100 datapicker easyui-datebox" required="true"/>
							</td>
							
							<td>
								<input class="easyui-radiobutton" id="base_daily" name="base_daily" value="1" label="1일" labelPosition="after" labelWidth="40px;">
								<input class="easyui-radiobutton" id="base_weekly" name="base_weekly" value="2" label="1주일" labelPosition="after" labelWidth="40px;">
								<input class="easyui-radiobutton" id="base_monthly" name="base_monthly" value="3" label="1개월" labelPosition="after" labelWidth="40px;">
							</td>
							<td><label>SQL ID</label></td>
							<td colspan="2"><input type="text" id="sql_id_text" name="sql_id_text" class="w340 easyui-textbox" style=""/></td>
							<td><label>SQL Text</label></td>
							<td><input type="text" id="sql_full_text" name="sql_full_text" class="w340 easyui-textbox" style=""/></td>
						</tr>
						<tr style="height:5px;"></tr>
						<tr height="100px;">
							<td colspan="9" class="well placeholderColorChange" style="padding-top:0px; border:1px solid #eaeaea;">
								<table style="width:100%;">
									<colgroup>
										<col style="width:5%">
										<col style="width:5%">
										<col style="width:5%">
										<col style="width:5%">
										<col style="width:5%">
										<col style="width:10%">
										<col style="width:5%">
										<col style="width:5%">
										<col style="width:15%">
									</colgroup>
									<tr height="30px;">
										<td style="width:100px;"><label>전체</label></td>
										<td align="left">
											<input id="all_sql_yn" name="all_sql_yn" class="easyui-checkbox">
										</td>
										<td colspan="2" style="width:100px;" align="right"><label>Plan 변경</label></td>
										<td align="left">
											<input id="plan_change_yn" name="plan_change_yn" class="easyui-checkbox">
										</td>
										<td colspan="2" style="width:100px;" align="right"><label>성능저하(BUFFER GETS 기준)</label></td>
										<td align="left">
											<input id="perf_down_yn" name="perf_down_yn" class="easyui-checkbox">
										</td>
										<td align="right"><label>성능 부적합</label></td>
										<td colspan="2" align="left">
											<input id="notPerf_yn" name="notPerf_yn" class="easyui-checkbox">
										</td>
										<td>
											<label>SQL Profile 적용</label>
											<input id="sql_profile_yn" name="sql_profile_yn" class="easyui-checkbox">
										</td>
										
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>BUFFER GETS(ASIS 평균)</label></td>
										<td colspan="2" width="">
											<input type="number" id="buffer_gets_1day" name="buffer_gets_1day" class="w100 easyui-numberbox" data-options="min:1"/>&nbsp;&nbsp; 이상
										</td>
										<td colspan="2" align="center"><label style="font-size:13px; color:blue;"> &emsp;&emsp;&emsp;&emsp;and</label></td>
										<td colspan="2" align="center"><label>ELAPSED TIME(ASIS 평균)</label></td>
										<td colspan="1" align="left">
											<input type="number" id="asis_elapsed_time" name="asis_elapsed_time" class="w100 easyui-textbox" data-options=""/>&nbsp;&nbsp; 초 이상
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>BUFFER GETS 성능 저하</label></td>
										<td colspan="2">
											<input type="number" id="buffer_gets_regres" name="buffer_gets_regres" class="w100 easyui-numberbox" data-options="min:1,precision:1"/>&nbsp;&nbsp; 배 이상
										</td>
										<td colspan="2" align="center"><label style="font-size:13px; color:blue;"> &emsp;&emsp;&emsp;&emsp;and</label></td>
										<td colspan="2" align="center"><label>ELAPSED TIME 성능 저하</label></td>
										<td colspan="1">
											<input type="number" id="elapsed_time_regres" name="elapsed_time_regres" class="w100 easyui-textbox" data-options=""/>&nbsp;&nbsp; 배 이상
										</td>
									</tr>
								</table>
							</td>
							
							<td colspan="2" align="center">
								<table style="widht:15%;">
									<tr height="40px;">
										<td class="searchBtn" style="margin-top:5px;">
											<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
											<a href="javascript:;" class="w120 easyui-linkbutton" style="margin-top:10px;" onClick="operationExcelDownload();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="multi">
			</div>
			<div class="easyui-layout multi" data-options="border:false" style="width:100%; margin-top:10px; min-height:423px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtnLeft2">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showTuningReqPopup();" style=" margin-left: 0px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 튜닝대상선정</a>
				</div>
				<div class="searchBtn">
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true" style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
		<form method="post" id="sqlinfo_form" name="sqlinfo_form" class="form-inline">
			<input type="hidden" id="project_id" name="project_id"/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="module" name="module"/>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="strStartDt" name="strStartDt"/>
			<input type="hidden" id="strEndDt" name="strEndDt"/>
			<input type="hidden" id="operation_dbid" name="operation_dbid"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="asis_plan_hash_value" name="asis_plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
		</form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<!-- 튜닝대상선정 popup START -->
<div id="tuningAssignPop" class="easyui-window popWin" style="background-color:#ffffff;width:490px;height:290px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="assign_form" name="assign_form" class="form-inline">
			<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="auto_share" name="auto_share" value="N"/>
				<input type="hidden" id="isAll" name="isAll"/>
				<input type="hidden" id="sqlIdArry" name="sqlIdArry"/>
				<input type="hidden" id="module" name="module"/>
				<input type="hidden" id="planHashValueArry" name="planHashValueArry"/>
				
				<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
				<input type="hidden" id="operation_rows_processed" name="operation_rows_processed"/>
				<input type="hidden" id="operation_elapsed_time" name="operation_elapsed_time"/>
				<input type="hidden" id="operation_buffer_gets" name="operation_buffer_gets"/>
				<input type="hidden" id="operation_executions" name="operation_executions"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd"/>
				<input type="hidden" id="perfr_id" name="perfr_id"/>
				<input type="hidden" id="strGubun" name="strGubun"/>
				<input type="hidden" id="choice_div_cd" name="choice_div_cd"/> <!-- FULL SCAN SQL : 4, TEMP_USAGE_SQL : 7, PLAN_CHANGE_SQL : 5, NEW_SQL : 6 -->
				<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
				
				<input type="hidden" id="cntPage" name="cntPage"/>
				
				<table class="noneT_popup">
						<colgroup>	
							<col style="width:25%;">
							<col style="width:15%;">
							<col style="width:15%;">
							<col style="width:25%;">
						</colgroup>
						<tr>
							<th >튜닝담당자 자동할당</th>
							<td class="ltext">
								<input type="checkbox" id="chkAutoShare" name="chkAutoShare" value="" data-options="panelHeight:'auto',editable:false" class="w60 easyui-switchbutton" required="true"/>
							</td>
							<th >튜닝담당자</th>
							<td class="ltext">
								<select id="selectTuner" name="selectTuner" data-options="panelHeight:'300',editable:false" class="w110 easyui-combobox" required="true"></select>
							</td>
						</tr>
						<tr>
							<th>이전 튜닝대상 선정<br> SQL 제외</th>
							<td class="ltext">
								<input type="checkbox" id="chkExcept" name="chkExcept" class="w120 easyui-switchbutton" data-options="panelHeight:'auto',editable:false" required/>
							</td>
						</tr>
						<tr>
							<th >프로젝트</th>
							<td colspan="3" class="ltext">
								<select id="project_id" name="project_id" data-options="editable:false"  class="w290 easyui-combobox"></select>
							</td>
						</tr>
						<tr>
							<th>SQL점검팩</th>
							<td colspan="3"  class="ltext">
								<select id="sql_auto_perf_check_id" name="sql_auto_perf_check_id" data-options="editable:false"  class="w290 easyui-combobox">
									<option value="">해당없음</option>
								</select>
							</td>
						</tr>
					</table>
					
					
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
				<div class="searchBtn" style="padding-right:35px;">
					<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_SaveTuningDesignation();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝요청 및 담당자지정</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('tuningAssignPop');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>
<!-- 튜닝대상선정 popup END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_operation_popup.jsp" %>
</body>
</html>