<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 :: 실행 SQL 성능 검증 :: 성능 검증 결과</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/inspectionResult.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/inspectionResult_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script>
		var auth_cd = "${auth_cd}";
		var deploy_id = "${deployCheckStatus.deploy_id}";
		var perf_check_id = "${performanceCheckMng.perf_check_id}";
		var perf_check_result_div_cd = "${deployCheckStatus.perf_check_result_div_cd}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;vertical-align:top;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}
						<c:if test="${deployCheckStatus.deploy_id != null and deployCheckStatus.deploy_id != ''}">
							 [${deployCheckStatus.deploy_id}]
						</c:if>
					</span>
				</div>
				<div class="well">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="dbid" name="dbid" value="${perfCheckResultBasicInfo.dbid}"/>
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${performanceCheckMng.wrkjob_cd}"/>
					<input type="hidden" id="wrkjob_cd_nm" name="wrkjob_cd_nm" value="${performanceCheckMng.wrkjob_cd_nm}"/>
					<input type="hidden" id="top_wrkjob_cd" name="top_wrkjob_cd" value="${performanceCheckMng.top_wrkjob_cd}"/>
					<input type="hidden" id="top_wrkjob_cd_nm" name="top_wrkjob_cd_nm" value="${performanceCheckMng.top_wrkjob_cd_nm}"/>
					<input type="hidden" id="search_wrkjob_cd" name="search_wrkjob_cd" value="${performanceCheckMng.search_wrkjob_cd}"/>
					<input type="hidden" id="search_wrkjob_cd_nm" name="search_wrkjob_cd_nm" value="${performanceCheckMng.search_wrkjob_cd_nm}"/>
					<input type="hidden" id="perf_check_id" name="perf_check_id" value="${performanceCheckMng.perf_check_id}"/>
					<!-- 검증단계ID -->
					<input type="hidden" id="perf_check_step_id" name="perf_check_step_id" value="${performanceCheckMng.perf_check_step_id}"/>
					<input type="hidden" id="program_id" name="program_id" value="${performanceCheckMng.program_id}"/>
					<input type="hidden" id="hidden_program_execute_tms" name="hidden_program_execute_tms" value="${performanceCheckMng.program_execute_tms}"/>
					<input type="hidden" id="program_execute_tms2" name="program_execute_tms" value="${performanceCheckMng.program_execute_tms}"/>
					<input type="hidden" id="parsing_schema_name" name="parsing_schema_name" value=""/>
					<input type="hidden" id="deploy_request_dt_1" name="deploy_request_dt_1" value="${performanceCheckMng.deploy_request_dt_1}"/>
					<input type="hidden" id="deploy_request_dt_2" name="deploy_request_dt_2" value="${performanceCheckMng.deploy_request_dt_2}"/>
					<input type="hidden" id="deploy_request_dt" name="deploy_request_dt" value=""/>
					<input type="hidden" id="search_deploy_check_status_cd" name="search_deploy_check_status_cd" value="${performanceCheckMng.search_deploy_check_status_cd}"/>
					<input type="hidden" id="search_last_perf_check_step_id" name="search_last_perf_check_step_id" value="${performanceCheckMng.search_last_perf_check_step_id}"/>
					<input type="hidden" id="deploy_requester_nm" name="deploy_requester_nm" value="${performanceCheckMng.deploy_requester_nm}"/>
					<input type="hidden" id="deploy_id" name="deploy_id" value="${performanceCheckMng.deploy_id}"/>
					<input type="hidden" id="perf_check_result_div_nm" name="perf_check_result_div_nm" />
					<input type="hidden" id="snap_time" name="snap_time" />
					<input type="hidden" id="last_captured" name="last_captured" />
					<input type="hidden" id="sql_id" name="sql_id" />
					<input type="hidden" id="sql_desc" name="sql_desc" />
					
					<input type="hidden" id="program_exec_dt" name="program_exec_dt" />
					<input type="hidden" id="pop_perf_check_id" name="pop_perf_check_id" />
					<input type="hidden" id="pop_perf_check_step_id" name="pop_perf_check_step_id" />
					<input type="hidden" id="pop_program_id" name="pop_program_id" />
					<input type="hidden" id="pop_program_execute_tms" name="pop_program_execute_tms" />
					
					<input type="hidden" id="auto_skip" name="auto_skip" value="N"/>
					<input type="hidden" id="hidden_program_type_cd" name="hidden_program_type_cd"/>
					<input type="hidden" id="hidden_sql_command_type_cd" name="hidden_sql_command_type_cd"/>
					<input type="hidden" id="html" name="html" value=""/>
					<input type="hidden" id="clickedRowIndex" name="clickedRowIndex" value="0"/>
					<input type="hidden" id="params" name="params" value="">
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="5"/>
					
					<div>
						<label>검색조건</label>
						<select id="selectValue" name="selectValue" data-options="panelHeight:'auto',editable:false,required:true" class="w150 easyui-combobox">
							<option value="01">SQL식별자(DBIO)</option>
							<option value="02">프로그램</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" data-options="disabled:false,editable:true" class="w150 easyui-textbox"/>
						<label>프로그램유형</label>
						<select id="program_type_cd" name="program_type_cd" data-options="panelHeight:'auto',editable:false,required:false" class="w80 easyui-combobox">
						</select>
						<label>SQL명령 유형</label>
						<select id="sql_command_type_cd" name="sql_command_type_cd" data-options="panelHeight:'auto',editable:false,required:false" class="w100 easyui-combobox">
						</select>
						<label>검증결과</label>
						<select id="perf_check_result_div_cd" name="perf_check_result_div_cd" data-options="panelHeight:'auto',editable:false,required:true" class="w100 easyui-combobox">
						</select>
						<label>미수행</label>
						<input class="easyui-checkbox" id="unperformed" name="unperformed" value="false">
						<label>검증제외</label>
						<input class="easyui-checkbox" id="improper" name="improper" value="false">
						
						<label>검색건수</label>
						<select id="searchCount" name="searchCount" data-options="editable:true" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto'">
							<option value="5" selected>5</option>
							<option value="10">10</option>
							<option value="20" >20</option>
							<option value="40">40</option>
							<option value="60">60</option>
							<option value="80">80</option>
							<option value="100">100</option>
						</select>
						
						<span class="searchBtnLeft multiBtn">
							<a href="javascript:;" id="aSearchBtn" class="w70 easyui-linkbutton"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
						<div class="searchBtn innerBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>
					</div>
				</div>
			</div>
			
			<div style="height:24px;padding-bottom:3px">
				<input type="text" class="perf_check_result_blue perf_check_result_common" size="10" value="전체 : ${perfCheckResultBasicInfo.total_cnt}" readonly/>
				<input type="text" class="perf_check_result_green perf_check_result_common" size="10" value="적합 : ${perfCheckResultBasicInfo.pass_cnt}" readonly/>
				<input type="text" class="perf_check_result_red perf_check_result_common" size="11" value="부적합 : ${perfCheckResultBasicInfo.fail_cnt}" readonly/>
				<input type="text" class="perf_check_result_gray perf_check_result_common" size="11" value="미수행 : ${perfCheckResultBasicInfo.test_miss_cnt}" readonly/>
				<input type="text" class="perf_check_result_navy perf_check_result_common" size="12" value="검증제외 : ${perfCheckResultBasicInfo.exception_cnt}" readonly/>
			</div>
			
			<div id="perfCheckResultDataGrid" style="height:165px;padding-bottom:5px;">
				<div class="easyui-layout" fit="true" data-options="border:false" style="width:100%;min-height:165px">
					<div data-options="region:'center',border:false" style="padding-top:5px;">
						<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</div>
			
			<div class="multi">
				<!-- 개발자일경우에만 성능 점검 완료 버튼 보임 -->
				<!-- 배포성능점검상태코드가 점검중(01)일때 성능 점검 완료 버튼 보임 -->
				<!-- 성능 점검 완료가 안되었을때 성능 점검 완료 버튼 보임 -->
				<sec:authorize access="hasAnyRole('ROLE_DEV')">
					<span class="searchBtnLeft innerBtn">
						<c:if test="${deployCheckStatus.deploy_check_status_cd eq '01' and perfCheckResultBasicInfo.perf_test_complete_yn ne 'Y'}">
							<a href="javascript:;" id="perfChkCompleteBtn" class="w150 easyui-linkbutton original" data-options="iconCls:'icon-ok'" style="font-weight:bold;">
								성능 검증 완료
							</a>
						</c:if>
						<!-- 점검 완료상태이고 점검통보일시가 없을 때 -->
						<c:if test="${deployCheckStatus.deploy_check_status_cd eq '02' and deployCheckStatus.last_check_result_anc_dt eq ''}">
							<a href="javascript:;" id="perfChkRsltNoti" class="w150 easyui-linkbutton original" data-options="disabled:false">
								<i class="btnIcon far fa-bell fa-lg fa-fw"></i>
								 검증결과 통보
							</a>
						</c:if>
						<!-- 점검 완료상태이고 점검통보일시가 있는데 점검결과통보성공여부가 실패일 때 -->
						<c:if test="${deployCheckStatus.deploy_check_status_cd eq '02' and deployCheckStatus.last_check_result_anc_dt ne '' and deployCheckStatus.check_result_anc_yn ne 'Y'}">
							<a href="javascript:;" id="perfChkRsltNoti" class="w150 easyui-linkbutton original" data-options="disabled:false">
								<i class="btnIcon far fa-bell fa-lg fa-fw"></i>
								 검증결과 통보
							</a>
						</c:if>
					</span>
					<input type="hidden" id="role" value="ROLE_DEV">
				</sec:authorize>
				<sec:authorize access="!hasAnyRole('ROLE_DEV')">
					<input type="hidden" id="role" value="">
				</sec:authorize>
				<span class="searchBtn innerBtn">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</span>
			</div>
		</form:form>
			
		<form:form method="post" id="search_form" class="form-inline">
			<input type="hidden" name="selectValue" value="">
			<input type="hidden" name="searchValue" value="">
			<input type="hidden" name="program_type_cd" value="">
			<input type="hidden" name="sql_command_type_cd" value="">
			<input type="hidden" name="perf_check_result_div_cd" value="">
			<input type="hidden" name="perf_check_step_id" value="">
			<input type="hidden" name="perf_check_id" value="">
			<input type="hidden" name="unperformed" value="">
			<input type="hidden" name="improper" value="">
			<input type="hidden" name="onlyIncorrectYn" value="">
			<input type="hidden" name="program_id" value="${performanceCheckMng.program_id}">
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" name="currentPage" value="">
			<input type="hidden" name="pagePerCount" value="">
		</form:form>
		
		<div id="perfChkResultTabs" class="easyui-tabs" data-options="border:false" style="width:100%;min-height:440px">
			<div id="perfCheckBasicInfoTab" title="성능검증기본정보" style="padding:10px;">
				<table class="detailT">
					<colgroup>
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
					</colgroup>
					<tr>
						<th>업무</th>
						<td> ${perfCheckResultBasicInfo.wrkjob_cd_nm} </td>
						<th>성능검증ID</th>
						<td>${perfCheckResultBasicInfo.perf_check_id}</td>
						<th>배포명</th>
						<td colspan="3">${perfCheckResultBasicInfo.deploy_nm}</td>
					</tr>
					<tr>
						<th>배포ID</th>
						<td>${perfCheckResultBasicInfo.deploy_id}</td>
						<th>배포요청자</th>
						<td>${perfCheckResultBasicInfo.deploy_requester_nm}</td>
						<th>배포요청일시</th>
						<td>${perfCheckResultBasicInfo.deploy_request_dt}</td>
						<th>검증단계</th>
						<td>${perfCheckResultBasicInfo.perf_check_step_nm}</td>
					</tr>
					<tr>
						<th>검증결과</th>
						<td>${perfCheckResultBasicInfo.perf_check_result_div_nm}</td>
						<th>검증완료여부</th>
						<td>${perfCheckResultBasicInfo.perf_test_complete_yn}</td>
						<th>검증요청일시</th>
						<td>${perfCheckResultBasicInfo.perf_check_request_dt}</td>
						<th>검증완료일시</th>
						<td>${perfCheckResultBasicInfo.perf_check_complete_dt}</td>
					</tr>
					<tr>
						<th>검증상태</th>
						<td>${perfCheckResultBasicInfo.deploy_check_status_nm}</td>
						<th>검증대상DB</th>
						<td>${perfCheckResultBasicInfo.perf_test_db_name}</td>
						<th></th>
						<td></td>
						<th></th>
						<td></td>
					</tr>
				</table>
			</div>
			<div id="programInfo" class="gridLine" title="프로그램 정보" style="padding:5px 0px 5px 0px;">
			</div>
			
			<div id="perfCheckResult" title="성능 검증 결과" style="padding:5px 0px 5px 0px;height:360px;">
				<div style="width:100%;height:360px;">
					<table class="detailT" style="border-top: none">
						<colgroup>
							<col style="width:6%;">
							<col style="width:84%;">
						</colgroup>
						<tr class="tr_perf_check_result_table">
							<th>상세 검증 결과</th>
							<td>
								<table id="detailResultTable" class="detailT3 clickCell">
									<colgroup>
										<col style="width:200px;">
										<col style="width:70px;">
										<col style="width:100px;">
										<col style="width:100px;">
										<col style="width:90px;">
										<col style="width:80px;">
										<col style="width:300px;">
									</colgroup>
									<thead>
										<tr>
											<th rowspan="2">검증 지표</th>
											<th rowspan="2">적합</th>
											<th colspan="2">성능 검증 결과값</th>
											<th rowspan="2">성능 검증 결과</th>
											<th rowspan="2">예외등록여부</th>
											<th rowspan="2">성능검증 결과내용</th>
										</tr>
										<tr>
											<th>평균</th>
											<th>최대</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</td>
						</tr>
						<tr class="tr_perf_impr_guide">
							<th>개선가이드</th>
							<td id="td_perf_impr_guide">
								<table class="detailT3" id="perf_impr_guide_table">
									<colgroup>
										<col style="width:200px;">
										<col style="width:370px;">
										<col style="width:370px;">
									</colgroup>
									<thead>
									<tr>
										<th>검증지표</th>
										<th>지표설명</th>
										<th>개선가이드</th>
									</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								
							</td>
						</tr>
					</table>
				</div>
				<div style="width:100%" id="div_perf_chk_result2">
					<ul id="treePlan" class="easyui-tree" style="padding:5px;">
					</ul>
				</div>
			</div>
		</div>
		
	</div>
	<!-- contents END -->
</div>
<%@include file="/WEB-INF/jsp/include/popup/inspectionResult_popup.jsp" %>
</body>
</html>