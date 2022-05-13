<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.12.29	이재우	최초작성
 **********************************************************/
%>
<!DOCTYPE html >
<html lang="ko">
<head>
	<title>성능검증 :: DB간 SQL성능비교 :: 튜닝 SQL 일괄 검증</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
	<script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>
	<script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/loadExplainPlan_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/tunerAssign_popup.js?ver=<%=today%>"></script> <!-- 튜닝담당자 지정 팝업 -->
	<script type="text/javascript" src="/resources/js/ui/dbChangePerformanceImpactAnalysisForTibero/tuningSqlBatchVerifyForTibero.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
	.non-click .datagrid td, .non-click .datagrid td div{
		cursor : default;
	}
	
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="project_id" name="project_id"/>
			<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
			<input type="hidden" id="select_sql" name="select_sql"/>
			<input type="hidden" id="select_perf_impact" name="select_perf_impact"/>
			<input type="hidden" id="sql_id" name="sql_id"/>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="parent_project_id" name="parent_project_id"/>
			<input type="hidden" id="parent_sql_auto_perf_check_id" name="parent_sql_auto_perf_check_id"/>
			<!-- 자식 튜닝일괄검증 성능점검 프로젝트ID, 점검팩ID -->
			<input type="hidden" id="verify_project_id" name="verify_project_id"/>
			<input type="hidden" id="verify_sql_auto_perf_check_id" name="verify_sql_auto_perf_check_id"/>
			
			<input type="hidden" id="sqlExclude" name="sqlExclude" value=""/>
			<input type="hidden" id="sqlCheckId" name="sqlCheckId"/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="commonCode" name="commonCode" value="${commonCode}"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			
			<!-- 기본값 입력 -->
			<input type="hidden" id="perf_compare_meth_cd" name="perf_compare_meth_cd" value="2"/>
			<input type="hidden" id="dml_exec_yn" name="dml_exec_yn" value="N"/>
			<input type="hidden" id="parallel_degree" name="parallel_degree" value="1"/>
			<input type="hidden" id="multiple_exec_cnt" name="multiple_exec_cnt" value="1"/>
			<input type="hidden" id="multiple_bind_exec_cnt" name="multiple_bind_exec_cnt" value="1"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
			
			<div class="easyui-panel" data-options="border:false" style="width:100%">
				<div class="well">
					<table>
						<colgroup>
							<col style="width:5%">
							<col style="width:20%">
							<col style="width:10%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							
						</colgroup>
						<tr style="height:30px;">
							<td><label>프로젝트</label></td>
							<td colspan="1">
								<select id="project_id_cd" name="project_id_cd" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td align="right"><label>SQL점검팩</label></td>
							<td colspan="5">
								<select id="sqlPerformanceP" name="sqlPerformanceP" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
						</tr><tr style="height:5px;"></tr>
						
						<tr height="30px;">
							<td colspan="2" class="well placeholderColorChange" style="padding-top:10px; border:1px solid #eaeaea;">
								<table style="width:100%;">
									<tr height="30px;">
										<td style="width:130px;" align="left"><label>ORACLE 대비 성능저하</label></td>
										<td align="left">
											<input id="asis_perf_degrade_versus_yn" name="asis_perf_degrade_versus_yn" class="easyui-checkbox">
										</td>
										<td colspan="2" style="width:100px;" align="center"><label>튜닝 대비 성능저하</label></td>
										<td align="left">
											<input id="tuning_perf_degrade_versus_yn" name="tuning_perf_degrade_versus_yn" class="easyui-checkbox">
										</td>
										<td align="center"><label>오류</label></td>
										<td align="left">
											<input id="error_yn" name="error_yn" class="easyui-checkbox">
										</td>
									</tr>
								</table>
							</td>
							<td align="right"><label>일괄검증성능</label></td>
							<td colspan="6" class="well placeholderColorChange" style="padding-top:10px; border:1px solid #eaeaea;">
								<table>
									<tr height="30px;">
										<td><label>BUFFER GETS</label></td>
										<td>
											<input type="number" id="verify_buffer_gets" name="verify_buffer_gets" class="w100 easyui-numberbox" data-options="min:1"/>&nbsp;&nbsp; 이상&emsp;&emsp;
										</td>
										<td ><label>ELAPSED TIME</label></td>
										<td>
											<input type="number" id="verify_elapsed_time" name="verify_elapsed_time" class="w100 easyui-textbox" data-options="" style="text-align: right"/>&nbsp;&nbsp; 초 이상
										</td>
									</tr>
								</table>
							</td>
							<td style="width:15%"></td>
							<td align="center" style="width:12.5%">
								<table style="widht:15%;">
									<tr height="30px;">
										<td class="searchBtn" style="margin-top:5px;">
											<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch(1);"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
										</td>
									</tr>
								</table>
							</td>
							<td style="width:5%"></td>
						</tr>
					</table>
				</div>
				<div class="multi" style="margin-top:10px; vertical-align: '' ">
					<label>수행결과</label>
					<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체건수 : " readonly/>
					<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly/>
					<input type="text" class="perf_check_result_gray perf_check_result_common" size="14" value="수행중 : " readonly/>
					<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly/>
					<input type="text" id="perf_check_result_red" class="perf_check_result_red perf_check_result_common" size="14" value="강제완료 : " readonly/>
					<input type="text" id="perf_check_result_violet" class="perf_check_result_violet perf_check_result_common" size="14" value="검증완료" readonly/>
					<label>Refresh</label>
					<input type="checkbox" id="chkRefresh" name="chkRefresh" class="w80 easyui-switchbutton"/>
					<input type="number" id=timer_value name="timer_value" value="10" class="w40 easyui-numberbox" data-options="min:3"/> 초
					<a href="javascript:;" class="w20 easyui-linkbutton" onClick="Btn_LoadPerformanceCheckCount();"><i class="btnIcon fas fa-sync-alt fa-lg fa-fw"></i></a>
					<div class="searchBtn" style="margin-top:5px;">
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_CollectiveVerification();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 일괄검증</a>
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ForceUpdateSqlAutoPerformanceCheck();" style=" margin-right: 0px;"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 강제완료처리</a>
					</div>
					<span class="sql_time_limt_cd">
						<label>SQL Time Limit</label>
						<select id="sql_time_limt_cd" name="sql_time_limt_cd" data-options="editable:false" class="w100 easyui-combobox"></select> 분
						<input type="hidden" id="sql_time_direct_pref_value" name="sql_time_direct_pref_value">
					</span>
					<span><label>최대 Fetch 건수</label></span>
					<input type="number" id="max_fetch_cnt" name="max_fetch_cnt" class="w100 easyui-numberbox" required data-options=" min:1, max:1000000" value="100000">
				</div>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download_North();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout non-click" data-options="border:false" style="width:100%;min-height:100px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableNorthList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div class="multi" style="margin:10px 0px; " align="right">
				<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download_South();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:283px">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
					<table id="tableSouthList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
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
	</div>
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
				<input type="hidden" id="module" name="module"/>
				<input type="hidden" id="sqlIdArry" name="sqlIdArry"/>
				<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
				<input type="hidden" id="planHashValueArry" name="planHashValueArry"/>
				<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd"/>
				<input type="hidden" id="perfr_id" name="perfr_id"/>
				<input type="hidden" id="strGubun" name="strGubun"/>
				<input type="hidden" id="before_tuning_no" name="before_tuning_no"/>
				<input type="hidden" id="verify_project_id" name="verify_project_id"/>
				<input type="hidden" id="verify_sql_auto_perf_check_id" name="verify_sql_auto_perf_check_id"/>
				<input type="hidden" id="asis_sql_id" name="asis_sql_id"/>
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
								<select id="sqlPerformanceP" name="sqlPerformanceP" data-options="editable:false"  class="w290 easyui-combobox">
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
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_popup.jsp" %>
</body>
</html>