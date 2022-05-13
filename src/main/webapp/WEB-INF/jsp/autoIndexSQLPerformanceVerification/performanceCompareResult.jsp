<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능시험 :: 자동 인덱싱 SQL 성능 검증 :: SQL별 성능 영향도 분석 결과</title>
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
	
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/performanceCompareResult.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/autoIndexSQLPerformanceVerification/autoISQLPVCommon.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style type="text/css">
		#loadExplainPlanPop #operTextPlan_h {
			position: absolute;
			top: -500px;
			left: -500px;
			width: 1px;
			height: 1px;
			margin: 0;
			padding:0;
			border: 0;
		}
		.performanceCompareResult #searchWrapper table tr{
			height: 30px;
		}
		.performanceCompareResult #searchWrapper table:first-child tr:first-child{
			height: 33px;
		}
		.performanceCompareResult .innerTableWrapper{
			margin-left: 8px;
			padding: 5px 10px;
			vertical-align: top;
			border: 1px solid #eaeaea;
		}
		.performanceCompareResult .txt-center{
			text-align: center;
		}
		.performanceCompareResult .txt-right{
			text-align: right;
		}
		.performanceCompareResult .txt-left{
			text-align: left;
		}
		.performanceCompareResult #chartWrapper table thead tr th{
			height: 5px;
		}
		.performanceCompareResult .fitExtend{
			width: 100%;
			height: 100%;
			padding-top: 0px;
		}
		.performanceCompareResult .text-indent{
			margin-left: 8px;
		}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container" class="performanceCompareResult">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid">
			<input type="hidden" id="plan_hash_value" name="plan_hash_value">
			<input type="hidden" id="asis_plan_hash_value" name="asis_plan_hash_value">
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd">
			<input type="hidden" id="tobe_executions" name="tobe_executions">
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}">
			<input type="hidden" id="choice_div_cd" name="choice_div_cd"> <!-- FULL SCAN SQL : 4, TEMP_USAGE_SQL : 7, PLAN_CHANGE_SQL : 5, NEW_SQL : 6 -->
			<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd">
			<input type="hidden" name="perf_check_type_cd" value="4">
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="1">
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="40">
			
			<div class="easyui-panel width100per" id="searchWrapper" data-options="border:false" style="overflow:hidden;">
				<div class="well">
					<table class="width100per">
						<colgroup>
							<col style="width:26%">
							<col style="width:24.8%">
							<col style="width:1.4%">
							<col style="width:20%">
							<col style="width:19.8%">
							<col style="width:7.8%">
						</colgroup>
						<tbody>
							<tr>
								<td>
									<label>프로젝트</label>
									<select id="project_combo" class="w340 easyui-combobox" data-options="editable:false,prompt:'선택'" required></select>
									<input type="hidden" id="project_id" name="project_id">
								</td>
								<td colspan="5" class="txt-left">
									<label>SQL점검팩</label>
									<select id="sqlPack_combo" class="w340 easyui-combobox" data-options="editable:false,prompt:'선택'" required></select>
									<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id">
								</td>
							</tr>
							<tr>
								<td colspan="2" class="innerTableWrapper" style="margin-left: 0;">
									<table class="width100per">
										<colgroup>
											<col style="width:9.7%">
											<col style="width:3.5%">
											<col style="width:20.4%">
											<col style="width:3.5%">
											<col style="width:17.5%">
											<col style="width:3.5%">
											<col style="width:17.5%">
											<col style="width:3.5%">
											<col style="width:17.5%">
											<col style="width:3.5%">
										</colgroup>
										<tbody>
											<tr style="border-bottom:1px solid darkgray;">
												<td>
													<label>SELECT 문</label>
												</td>
												<td>
													<input id="select_yn" class="easyui-checkbox">
													<input type="hidden" name="select_yn">
												</td>
												<td class="txt-right">
													<label class="paddingR5">DML 문</label>
												</td>
												<td>
													<input id="dml_yn" class="easyui-checkbox">
													<input type="hidden" name="dml_yn">
												</td>
											</tr>
											<tr>
												<td rowspan="2" class="paddingB30"">
													<label>전체</label>
												</td>
												<td rowspan="2" class="paddingB30">
													<input id="all_sql_yn" class="easyui-checkbox">
													<input type="hidden" name="all_sql_yn">
												</td>
												<td class="txt-right">
													<label>성능저하(BUFFER GETS 기준)</label>
												</td>
												<td>
													<input id="perf_down_yn" class="easyui-checkbox">
													<input type="hidden" name="perf_down_yn">
												</td>
												<td class="txt-right">
													<label>성능 부적합</label>
												</td>
												<td>
													<input id="notPerf_yn" class="easyui-checkbox">
													<input type="hidden" name="notPerf_yn">
												</td>
												<td class="txt-right">
													<label>오류</label>
												</td>
												<td>
													<input id="error_yn" class="easyui-checkbox">
													<input type="hidden" name="error_yn">
												</td>
												<td class="txt-right">
													<label>FULL SCAN</label>
												</td>
												<td>
													<input id="fullScan_yn" class="easyui-checkbox">
													<input type="hidden" name="fullScan_yn">
												</td>
											</tr>
											<tr>
												<td class="txt-right">
													<label>PARTITION ALL ACCESS</label>
												</td>
												<td>
													<input id="partition_yn" class="easyui-checkbox">
													<input type="hidden" name="partition_yn">
												</td>
												<td class="txt-right">
													<label>TIME-OUT</label>
												</td>
												<td>
													<input id="timeOut_yn" class="easyui-checkbox">
													<input type="hidden" name="timeOut_yn">
												</td>
												<td class="txt-right">
													<label>최대 FETCH 초과</label>
												</td>
												<td>
													<input id="maxFetch_yn" class="easyui-checkbox">
													<input type="hidden" name="maxFetch_yn">
												</td>
											</tr>
										</tbody>
									</table>
								</td>
								<td colspan="2">
									<div class="well placeholderColorChange innerTableWrapper">
										<table class="width100per">
											<tr>
												<td colspan="3"><label>BUFFER GETS 성능 저하</label></td>
												<td colspan="2">
													<input type="number" id="buffer_gets_regres" class="w100 easyui-textbox" data-options="min:1,precision:1" prompt="10"><span class="text-indent">배 이상</span>
													<input type="hidden" name="buffer_gets_regres">
												</td>
											</tr>
											<tr>
												<td colspan="3"><label>ELAPSED TIME 성능 저하</label></td>
												<td colspan="2" class="elapsed_time_regres">
													<input type="number" id="elapsed_time_regres" class="w100 easyui-textbox" data-options="min:0" prompt="10"><span class="text-indent">배 이상</span>
													<input type="hidden" name="elapsed_time_regres">
												</td>
											</tr>
											<tr>
												<td colspan="3"><label>BUFFER GETS(ASIS 평균)</label></td>
												<td colspan="2">
													<input type="number" id="buffer_gets_1day" class="w100 easyui-textbox" data-options="min:1" prompt="1000"><span class="text-indent">이상</span>
													<input type="hidden" name="buffer_gets_1day">
												</td>
											</tr>
											<tr>
												<td colspan="3"><label>ELAPSED TIME(ASIS 평균)</label></td>
												<td colspan="2" class="asis_elapsed_time">
													<input type="number" id="asis_elapsed_time" class="w100 easyui-textbox" data-options="min:0" prompt="3"><span class="text-indent">초 이상</span>
													<input type="hidden" name="asis_elapsed_time">
												</td>
											</tr>
										</table>
									</div>
								</td>
								<td>
									<div class="well placeholderColorChange innerTableWrapper" style="width:90%;height:120px;">
										<table class="width100per">
											<colgroup>
												<col style="width:20%">
												<col style="width:80%">
											</colgroup>
											<tbody>
												<tr>
													<td><label>SQL ID</label></td>
													<td align="left">
														<input id="search_sql_id" class="easyui-textbox" style="width:63%;">
														<input type="hidden" name="search_sql_id">
													</td>
												</tr>
												<tr>
													<td><label>정렬</label></td>
													<td>
														<select id="line_up_combo" class="w150 easyui-combobox" required="true" data-options="editable:false">
															<option value="00">TOTAL BUFFER GETS</option>
															<option value="01">TOTAL ELAPSED TIME</option>
															<option value="02">EXECUTIONS</option>
															<option value="03">AVG BUFFER GETS</option>
															<option value="04">AVG ELAPSED TIME</option>
															<option value="05" selected >SQL ID</option>
															<option value="06">SQL TEXT</option>
														</select>
														
														<input type="hidden" name="line_up_cd">
														<select id="order_combo" class="w80 easyui-combobox" required="true" data-options="editable:false">
															<option id="ASC" value="ASC" selected >오름차순</option>
															<option id="DESC" value="DESC">내림차순</option>
														</select>
														<input type="hidden" name="orderOf">
													</td>
												</tr>
											</tbody>
										</table>
									</div>
								</td>
								<td class="searchBtn width100per" style="height:132px; line-height:40px;position:relative;">
									<div style="position:absolute; top:50%; transform:translateY(-50%);margin-left:5px;width:160px;">
										<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
										<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="easyui-layout width100per" id="chartWrapper" data-options="border:false" style="height:150px;min-height:140px">
				<table class="detailT4" style="height:140px;">
					<colgroup>
						<col style="width:10%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:20%;">
					</colgroup>
					<thead>
						<tr>
							<th>수행결과</th>
							<th>성능 임팩트</th>
							<th>Elapsed Time</th>
							<th>Buffer Gets</th>
							<th>성능</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div class="multi txt-center">
									<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체 : " readonly><br>
									<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly><br>
									<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly>
								</div>
							</td>
							<td>
								<div id="chartPerfImpactPanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartElapsedTimePanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartBufferGetsPanel" class="fitExtend">
								</div>
							</td>
							<td>
								<div id="chartPerfFitPanel" class="fitExtend">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="easyui-layout width100per" id="tableListHeight" data-options="border:true" style="min-height:287px; border: 0.5px solid lightgray; border-width:1px 0px;">
				<div class="width100per" data-options="region:'center',split:false,collapsible:true,border:false" style="height:100%;">
					<table id="tableList" class="tbl easyui-datagrid" style="height:285px;" data-options="border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtn" >
					<input type="text" class="performanceResultCount border0px" size="20" value="검색결과 건수 : 0" style="font-size: 14px;vertical-align: middle;" readonly>
					<a href="javascript:;" class="w20 easyui-linkbutton" onclick="extendArea(this);" data-extended="true"><img src="/resources/images/wideView.png" width="15px;" style="vertical-align: middle;"></a>
					<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"style=" margin-right: 0px;"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>
			</div>
		</form:form>
	</div>	<!-- contents END -->
</div>	<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_operation_popup.jsp" %>
</body>
</html>