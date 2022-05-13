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
	<title>성능검증 :: DB간 SQL성능비교 :: 성능비교 결과</title>
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
	<script type="text/javascript" src="/resources/js/ui/include/popup/planCompare_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/autoPerformanceCompareBetweenDatabase/performanceCompareResult_V2.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script> <!-- 그리드 페이징, 이전/다음버튼 처리 -->
	
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
			<input type="hidden" id="asis_plan_hash_value" name="asis_plan_hash_value"/>
			<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd"/>
			<input type="hidden" id="data_yn" name="data_yn"/>
			<input type="hidden" id="sqlExclude" name="sqlExclude" value=""/>
			<input type="hidden" id="refresh" name="refresh" value="N"/>
			<input type="hidden" id="tobe_executions" name="tobe_executions"/>
			<input type="hidden" id="isAll" name="isAll"/>
			<input type="hidden" id="database_kinds_cd" name="database_kinds_cd" value="${database_kinds_cd}"/>
			<input type="hidden" id="choice_div_cd" name="choice_div_cd"/> <!-- FULL SCAN SQL : 4, TEMP_USAGE_SQL : 7, PLAN_CHANGE_SQL : 5, NEW_SQL : 6 -->
			
			<!-- SQL Profile 이관할 DBID -->
			<input type="hidden" id="transfer_dbid" name="transfer_dbid"/>
			<input type="hidden" id="sqlIdList" name="sqlIdList"/>
			<input type="hidden" id="planHashValueList" name="planHashValueList"/>
			<input type="hidden" id="searchKey2" name="searchKey2"/>
			<input type="hidden" id="perf_check_target_dbid" name="perf_check_target_dbid"/>
			<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div class="easyui-panel" id="searchHide" data-options="border:false" style="width:100%">
				<div class="well">
					<table style="width: 100%">
						<colgroup>
							<col style="width:6%">
							<col style="width:7%">
							<col style="width:3%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:1%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:5%">
							<col style="width:1%">
							<col style="width:5%">
							<col style="width:5%">
						</colgroup>
						<tr style="height:30px;">
							<td><label>프로젝트</label></td>
							<td colspan="3">
								<select id="project_id_cd" name="project_id_cd" class="w340 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td colspan="2" style="text-align: right;"><label>SQL점검팩</label></td>
							<td colspan="3">
								<select id="sqlPerformanceP" name="sqlPerformanceP" class="w380 easyui-combobox" required="true" data-options="editable:false"></select>
							</td>
							<td></td>
							<td colspan="1" style="text-align: right;"><label>정렬</label></td>
							<td colspan="2">
								<select id="line_up_cd" name="line_up_cd" class="w160 easyui-combobox" required="true" data-options="editable:false">
									<option value="00">TOTAL BUFFER GETS</option>
									<option value="01">TOTAL ELAPSED TIME</option>
									<option value="02">EXECUTIONS</option>
									<option value="03">AVG BUFFER GETS</option>
									<option value="04">AVG ELAPSED TIME</option>
									<option value="05" selected >SQL ID</option>
									<option value="06">SQL TEXT</option>
								</select>
							</td>
							<td colspan="2" style="text-align: left;">
								<select id="orderOf" name="orderOf" class="w100 easyui-combobox" required="true" data-options="editable:false">
									<option id="ASC" value="ASC" selected >오름차순</option>
									<option id="DESC" value="DESC">내림차순</option>
								</select>
							</td>
						</tr><tr style="height:5px;"></tr>
						
						<tr height="80px;">
							<td colspan="9" class="well placeholderColorChange" style="padding:5px 12px;vertical-align:top; border:1px solid #eaeaea;">
								<table style="width:100%;">
									<thead height=30px; style="border-bottom:1px solid darkgray;">
										<td width="80px"><label >SELECT 문</label></td>
										<td>
											<input id="select_yn" name="select_yn" class="easyui-checkbox">
										</td>
										<td width="80px" style="text-align: right;"><label style="padding-right:5px;">DML 문</label></td>
										<td align="center">
											<input id="dml_yn" name="dml_yn" class="easyui-checkbox">
										</td>
									</thead>
									<tbody>
										<tr height="30px;">
											<td style="width:60px;"><label>전체</label></td>
											<td>
												<input id="all_sql_yn" name="all_sql_yn" class="easyui-checkbox">
											</td>
											<td style="width:100px;" align="right"><label>Plan 변경</label></td>
											<td align="center">
												<input id="plan_change_yn" name="plan_change_yn" class="easyui-checkbox">
											</td>
											<td colspan="2" style="width:130px;" align="right"><label>성능저하(BUFFER GETS 기준)</label></td>
											<td align="center">
												<input id="perf_down_yn" name="perf_down_yn" class="easyui-checkbox">
											</td>
											<td align="right"><label>성능 부적합</label></td>
											<td colspan="2" align="center">
												<input id="notPerf_yn" name="notPerf_yn" class="easyui-checkbox">
											</td>
											<td align="right"><label>오류</label></td>
											<td align="center">
												<input id="error_yn" name="error_yn" class="easyui-checkbox">
											</td>
										</tr>
										<tr height="30px;">
											<td colspan="3" align="right"><label>FULL SCAN</label></td>
											<td align="center">
												<input id="fullScan_yn" name="fullScan_yn" class="easyui-checkbox">
											</td>
											<td colspan="2" align="right"><label>PARTITION ALL ACCESS</label></td>
											<td align="center">
												<input id="partition_yn" name="partition_yn" class="easyui-checkbox">
											</td>
											<td align="right"><label>TIME-OUT</label></td>
											<td colspan="2" align="center">
												<input id="timeOut_yn" name="timeOut_yn" class="easyui-checkbox">
											</td>
											<td align="right"><label>최대 FETCH 초과</label></td>
											<td align="center">
												<input id="maxFetch_yn" name="maxFetch_yn" class="easyui-checkbox">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
							<td></td>
							<td colspan="6" class="well" style="padding:5px 12px;border:1px solid #eaeaea;">
								<table style="width:100%;">
									<tr height="30px;">
										<td colspan="3"><label>BUFFER GETS 성능 저하</label></td>
										<td colspan="2">
											<input type="number" id="buffer_gets_regres" name="buffer_gets_regres" class="w100 easyui-numberbox" data-options="min:1,precision:1"/>&nbsp;&nbsp; 배 이상
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>ELAPSED TIME 성능 저하</label></td>
										<td colspan="2" class="elapsed_time_regres">
											<input type="number" id="elapsed_time_regres" name="elapsed_time_regres" class="w100 easyui-textbox"  data-options="min:0"/>&nbsp;&nbsp; 배 이상
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>BUFFER GETS(ASIS 평균)</label></td>
										<td colspan="2">
											<input type="number" id="buffer_gets_1day" name="buffer_gets_1day" class="w100 easyui-numberbox" data-options="min:1"/>&nbsp;&nbsp; 이상
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>ELAPSED TIME(ASIS 평균)</label></td>
										<td colspan="2" class="asis_elapsed_time">
											<input type="number" id="asis_elapsed_time" name="asis_elapsed_time" class="w100 easyui-textbox" data-options="min:0"/>&nbsp;&nbsp; 초 이상
										</td>
									</tr>
								</table>
							</td>
							<td></td>
							<td colspan="8" class="well" style="padding:5px 12px;border:1px solid #eaeaea;">
								<table style="width:220px;">
									<tr height="30px;">
										<td colspan="3" ><label>SQL Profile</label></td>
										<td colspan="2" align="right">
											<label>적용</label><input id="sql_profile_yn" name="sql_profile_yn" class="easyui-checkbox">
										</td>
										<td colspan="2" align="right">
											<label>미적용</label><input id="not_sql_profile_yn" name="not_sql_profile_yn" class="easyui-checkbox">
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3" ><label>SQL</label></td>
										<td colspan="2" align="right">
											<label>검토</label><input id="sql_review_yn" name="sql_review_yn" class="easyui-checkbox">
										</td>
										<td colspan="2" align="right">
											<label>미검토</label><input id="not_sql_review_yn" name="not_sql_review_yn" class="easyui-checkbox">
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"></td>
										<td colspan="4" align="right">
											<input id="review_keyword" name="review_keyword" class="w120 easyui-textbox">
										</td>
									</tr>
									<tr height="30px;">
										<td colspan="3"><label>SQL ID</label></td>
										<td colspan="4" align="right">
											<input id="search_sql_id" name="search_sql_id" class="w120 easyui-textbox">
										</td>
									</tr>
								</table>
							</td>
							<td></td>
							<td align="center">
								<table style="width:15%;">
									<tr height="40px;">
										<td class="searchBtn" style="margin-top:5px;">
											<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SqlAutoPerfSearch(1);"><i class="btnIcon fas fa-search fa-lg fa-fw"></i></i> 검색</a>
										</td>
									</tr>
									<tr height="40px;">
										<td class="searchBtn" style="margin-top:5px;">
											<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
										</td>
									</tr>
									
								</table>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="easyui-layout" id="chartHide" data-options="border:false" style="width:100%;height:150px;min-height:140px">
				<table style="width:100%;height:140px;" class="detailT4">
					<colgroup>
						<col style="width:10%;">
						<col style="width:25%;">
						<col style="width:20%;">
						<col style="width:20%;">
						<col style="width:15%;">
						<col style="width:15%;">
					</colgroup>
					<thead>
						<tr>
							<th style="height:5px;">수행결과</th>
							<th style="height:5px;">성능 임팩트</th>
							<th style="height:5px;">Elapsed Time</th>
							<th style="height:5px;">Buffer Gets</th>
							<th style="height:5px;">Plan 변경</th>
							<th style="height:5px;">성능</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td style="border-left:1px solid #FFF;">
								<div class="multi" style="text-align: center;">
									<input type="text" class="perf_check_result_blue perf_check_result_common" size="14" value="전체건수 : " readonly/><br>
									<input type="text" class="perf_check_result_green perf_check_result_common" size="14" value="수행완료 : " readonly/><br>
									<input type="text" class="perf_check_result_orange perf_check_result_common" size="14" value="오류 : " readonly/>
								</div>
							</td>
							<td>
								<div id="chartPerfImpactPanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartElapsedTimePanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartBufferGetsPanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartPlanChangePanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
							<td>
								<div id="chartPerfFitPanel" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="easyui-layout" id="tableListHeight" data-options="border:true" style="width:100%;min-height:287px; border: 0.5px solid lightgray; border-width:1px 0px;">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;">
					<table id="tableList" class="tbl easyui-datagrid" style="height:285px;" data-options="border:false">
					</table>
				</div>
			</div>
			<div class="innerBtn2">
				<div class="searchBtnLeft2">
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showTuningReqPopup();" style=" margin-left: 0px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 튜닝대상선정</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSqlProfilePopup(1);" style=" margin-left: 0px;"><i class="btnIcon far fa-play-circle fa-lg fa-fw"></i> SQL Profile 적용</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSqlProfilePopup(2);" style=" margin-left: 0px;"><i class="btnIcon far fa-play-circle fa-lg fa-fw"></i> SQL Profile 삭제</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showSqlProfilePopup(3);" style=" margin-left: 0px;"><i class="btnIcon far fa-play-circle fa-lg fa-fw"></i> SQL Profile 이관</a>
					<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ReSqlAutoPerfCompare();" style=" margin-left: 0px;"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 재실행</a>
				</div>
				<div class="searchBtn" >
					<input type="text" class="performanceResultCount" size="20" value="검색결과 건수 : 0" readonly style="border:0; font-size: 14px;vertical-align: middle;" >
					<a href="javascript:;" class="w20 easyui-linkbutton" onclick="gridWide();"><img src="/resources/images/wideView.png" width="15px;" style="vertical-align: middle;"></a>
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
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="auto_share" name="auto_share" value="N"/>
				<input type="hidden" id="isAll" name="isAll"/>
				<input type="hidden" id="sqlIdArry" name="sqlIdArry"/>
				<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
				<input type="hidden" id="planHashValueArry" name="planHashValueArry"/>
				<input type="hidden" id="asisPlanHashValueArry" name="asisPlanHashValueArry"/>
				<input type="hidden" id="sql_auto_perf_check_id" name="sql_auto_perf_check_id"/>
				<input type="hidden" id="parsing_schema_name" name="parsing_schema_name"/>
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd"/>
				<input type="hidden" id="module" name="module"/>
				<input type="hidden" id="perfr_id" name="perfr_id"/>
				<input type="hidden" id="strGubun" name="strGubun"/>
				<input type="hidden" id="choice_div_cd" name="choice_div_cd"/> <!-- FULL SCAN SQL : 4, TEMP_USAGE_SQL : 7, PLAN_CHANGE_SQL : 5, NEW_SQL : 6 -->
				<input type="hidden" id="perf_check_sql_source_type_cd" name="perf_check_sql_source_type_cd"/>
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
<!-- SqlInfoMemoBox popup START -->
<div id="memoBox" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:350px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="memo_form" name="memo_form" class="form-inline">
			<input type="hidden" name="sql_id"/>
			<input type="hidden" name="dbid"/>
			<input type="hidden" id="review_sbst" name="review_sbst"/>
			<div data-options="plain:true,region:'center',split:false,border:false" style="padding:5px;">
				<textarea id="memoEdit" style="height: 98.5%; width:99%;" placeholder="플랜검증 1단계(2021.06.02), 홍튜너 검토
-----------------------------------------------------------
SELECT-LIST 에 바인드값이 수집되지 않아 TO_DATE 함수에서 FORMAT 팅시에 오류"></textarea>
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:30px;">
				<div class="searchBtn" style="padding-right:16px;">
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_memoSave();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_memoDelete();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('memoBox');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>
<!-- 튜닝대상선정 popup END -->
<%@include file="/WEB-INF/jsp/include/popup/loadExplainPlan_operation_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/sqlProfileApp_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/planCompare_popup.jsp" %>
</body>
</html>