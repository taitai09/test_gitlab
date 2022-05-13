<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2021.02.08	황예지	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능시험 :: 테이블 변경 성능 영향도 분석 :: 성능 영향도 분석 결과</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<link rel="stylesheet" href="/resources/css/analyzeImpactChangedTable.css">
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadExplainPlan_operation_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/performanceImpactAnalysisResult.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/analyzeImpactChangedTable/analyzeImpactChangedTableCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" class="perfImptAnalysisRslt"> 
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="select_sql" name="select_sql"/>
			<input type="hidden" id="select_perf_impact" name="select_perf_impact"/>
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="asis_plan_hash_value" name="asis_plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="sqlExclude" name="sqlExclude" value=""/>
			<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd" value=""/>
			<input type="hidden" id="table_owner" name="table_owner" value=""/>
			<input type="hidden" id="table_name" name="table_name" value=""/>
			<input type="hidden" name="choice_div_cd" value="J">
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>
			
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="well result-wrapper clearfix">
					<table>
						<colgroup>
							<col style="width:64px">
							<col style="width:125px">
							<col style="width:100px">
							<col style="width:270px">
							<col style="width:160px">
							<col style="width:76px">
							<col style="width:340px">
						</colgroup>
						<tbody>
							<tr>
								<td><label>소스DB</label></td>
								<td>
									<select id="original_dbid" name="original_dbid" data-options="editable:false" class="w110 easyui-combobox" required></select>
								</td>
								<td><label>점검팩 등록일자</label></td>
								<td>
									<input type="text" id="perf_check_exec_begin_dt" name="perf_check_range_begin_dt" value="${aMonthAgo}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
									<span class="spacing">	~	</span>
									<input type="text" id="perf_check_exec_end_dt" name="perf_check_range_end_dt" value="${nowDate}" class="w100 datapicker easyui-datebox" data-options="panelHeight:'auto',editable:false,required:true"/>
								</td>
								<td class="radio-area">
									<span><input class="easyui-radiobutton" name="analysisPeriod" id="aWeek"><label>1주일</label></span>
									<span><input class="easyui-radiobutton" name="analysisPeriod" id="oneMonth" checked><label>1개월</label></span>
								</td>
								<td><label>SQL점검팩</label></td>
								<td>
									<select id="sqlPerformanceP" name="sqlPerformanceP" class="w360 easyui-combobox" required data-options="panelHeight:'auto',editable:false" style="width:90.5%;"></select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="well borderLine">
						<table>
							<colgroup>
								<col style="width:60px">
								<col style="width:30px">
								<col style="width:30px">
								<col style="width:70px">
								<col style="width:30px">
								<col style="width:80px">
								<col style="width:160px">
								<col style="width:60px">
								<col style="width:79px">
								<col style="width:160px">
								<col style="width:46px">
								<col style="width:30px">
								<col style="width:120px">
								<col style="width:60px">
							</colgroup>
							<tbody>
								<tr>
									<td colspan="13">
										<label >SELECT 문</label>
										<input type="hidden" id="select_yn" name="select_yn" value="Y">
									</td>
								</tr>
								<tr>
									<td><label>전체</label></td>
									<td>
										<input id="all_sql_yn" name="all_sql_yn" class="easyui-checkbox">
									</td>
									
									<td colspan="2" align="right"><label>Plan 변경</label></td>
									<td colspan="2" style="padding-left: 25px;">
										<input id="plan_change_yn" name="plan_change_yn" class="easyui-checkbox">
									</td>
									
									<td colspan="2" align="right"><label>성능저하(BUFFER GETS 기준)</label></td>
									<td style="padding-left: 28px;">
										<input id="perf_down_yn" name="perf_down_yn" class="easyui-checkbox">
									</td>
									
									<td align="right"><label>성능 부적합</label></td>
									<td colspan="2" style="padding-left: 13px;">
										<input id="notPerf_yn" name="notPerf_yn" class="easyui-checkbox">
									</td>
									<td align="right" style="padding-right: 10px;"><label>오류</label></td>
									<td style="padding-left: 18px;">
										<input id="error_yn" name="error_yn" class="easyui-checkbox">
									</td>
								</tr>
								<tr>
									<td colspan="4"><label>BUFFER GETS(ASIS 평균)</label></td>
									<td colspan="3" width="">
										<input type="number" id="buffer_gets_1day" name="buffer_gets_1day" class="w100 easyui-numberbox" data-options="min:1"/>이상
									</td>
									<td colspan="2"><label style="font-size:13px; color:blue;padding-left:11px;">and</label></td>
									<td colspan="2"><label>ELAPSED TIME(ASIS 평균)</label></td>
									<td colspan="2">
										<input type="number" id="asis_elapsed_time" name="asis_elapsed_time" class="w100 easyui-numberbox" data-options="precision:1"/>초 이상
									</td>
								</tr>
								<tr>
									<td colspan="4"><label>BUFFER GETS 성능 저하</label></td>
									<td colspan="3">
										<input type="number" id="buffer_gets_regres" name="buffer_gets_regres" class="w100 easyui-numberbox" data-options="min:1,precision:1"/>배 이상
									</td>
									<td colspan="2"><label style="font-size:13px; color:blue;padding-left:11px;">and</label></td>
									<td colspan="2"><label>ELAPSED TIME 성능 저하</label></td>
									<td colspan="2">
										<input type="number" id="elapsed_time_regres" name="elapsed_time_regres" class="w100 easyui-numberbox" data-options="min:0,precision:1"/>배 이상
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="well borderLine">
						<table>
							<colgroup>
								<col style="width:77px">
								<col style="width:27px">
								<col style="width:105px">
								<col style="width:27px">
							</colgroup>
							<tbody>
								<tr>
									<td><label >DML 문</label></td>
									<td colspan="3">
										<input id="dml_yn" name="dml_yn" class="easyui-checkbox">
									</td>
								</tr>
								<tr>
									<td><label>전체</label></td>
									<td>
										<input id="all_dml_yn" name="all_dml_yn" class="easyui-checkbox">
									</td>
									<td align="center"><label>FULL SCAN</label></td>
									<td align="center">
										<input id="fullScan_yn" name="fullScan_yn" class="easyui-checkbox">
									</td>
								</tr>
								<tr>
									<td colspan="3"><label>PARTITION ALL ACCESS</label></td>
									<td align="center">
										<input id="partition_yn" name="partition_yn" class="easyui-checkbox">
									</td>
								</tr>
								<tr>
									<td><label>오류</label></td>
									<td colspan="3">
										<input id="error_dml_yn" name="error_dml_yn" class="easyui-checkbox">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<table style="float: left;margin: 41px 0px 0px 8px;">
						<tr>
							<td class="searchBtn">
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch('initial');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i>검색</a>
							</td>
						</tr>
						<tr>
							<td class="searchBtn">
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i>엑셀</a>
							</td>
						</tr>
					</table>
				</div>
				
				<div class="multi">
					<label>수행결과</label>
					<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체 : " readonly/>
					<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly/>
					<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly/>
					<input type="text" class="performanceResultCount" size="20" value="검색결과 건수 : 0" readonly>
				</div>
			</div>
			<div class="table-wrapper">
				<table>
					<tr>
						<td>
							<div class="easyui-layout" data-options="border:false" style="width: 370px;">
								<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:20%;height:99%;">
									<table id="leftTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
							</div>
						</td>
						<td>
							<div class="easyui-layout" data-options="border:false" style="width: 1319px;">
								<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:80%;height:99%;">
									<table id="rightTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
									</table>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</div>
			<div class="innerBtn2">
				<div class="searchBtnLeft2">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showTuningReqPopup();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 튜닝대상선정</a>
				</div>
				<div class="searchBtn" >
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<!-- 튜닝대상선정 popup START -->
<div id="tuningAssignPop" class="easyui-window popWin" style="background-color:#ffffff;width:490px;height:290px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="assign_form" name="assign_form" class="form-inline">
			<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
				<input type="hidden" id="popup_dbid" name="dbid"/>
				<input type="hidden" id="auto_share" name="auto_share" value="N"/>
				<input type="hidden" id="isAll" name="isAll"/>
				<input type="hidden" id="sqlIdArry" name="sqlIdArry"/>
				<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
				<input type="hidden" id="asisPlanHashValueArry" name="asisPlanHashValueArry"/>
				<input type="hidden" id="planHashValueArry" name="planHashValueArry"/>
				<input type="hidden" id="popup_sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd"/>
				<input type="hidden" id="module" name="module"/>
				<input type="hidden" id="perfr_id" name="perfr_id"/>
				<input type="hidden" id="strGubun" name="strGubun"/>
				<input type="hidden" id="choice_div_cd" name="choice_div_cd"/>
				<input type="hidden" id="choice_cnt" name="choice_cnt"/>
				<input type="hidden" name="perf_check_sql_source_type_cd">
				<input type="hidden" name="database_kinds_cd" value="${database_kinds_cd}"/>
				
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
								<input type="checkbox" id="chkAutoShare" name="chkAutoShare" value="" data-options="panelHeight:'auto',editable:false" class="w60 easyui-switchbutton" required/>
							</td>
							<th >튜닝담당자</th>
							<td class="ltext">
								<select id="selectTuner" name="selectTuner" data-options="panelHeight:'300',editable:false" class="w110 easyui-combobox" required></select>
							</td>
						</tr>
						<tr>
							<th>이전 튜닝대상 선정<br> SQL 제외</th>
							<td class="ltext">
								<input type="checkbox" id="chkExcept" name="chkExcept" class="w120 easyui-switchbutton" data-options="panelHeight:'auto',editable:false" required/>
							</td>
						</tr>
						<tr>
							<th>SQL점검팩</th>
							<td colspan="3"  class="ltext">
								<select id="popup_sqlPerformanceP" name="sqlPerformanceP" data-options="editable:false"  class="w290 easyui-combobox">
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