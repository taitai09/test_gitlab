<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2018.02.22	이원식	OPENPOP V2 최초작업
 * 2020.06.02	이재우	기능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능개선 :: 성능 개선 관리 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/improvementManagementView.js?ver=<%=today%>"></script>

	<!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 -->
<%-- 	<script type="text/javascript" src="/resources/js/ui/include/popup/improvementManagementView_popup.js?ver=<%=today%>"></script> --%>
	<script type="text/javascript" src="/resources/js/ui/include/popup/imprMgmtView_popup.js?ver=<%=today%>"></script>
	
	<!-- 운영,개발여부 -->
	<c:set var="operation_yn" scope="page">
		<spring:eval expression="@defaultConfig['operation.yn']"/>
	</c:set>
					
	<!-- 성능개선 - 인덱스 요청  팝업 -->
	<c:choose>
		<c:when test="operation_yn eq 'Y'">
			<script type="text/javascript" src="/resources/js/ui/include/popup/indexRequest_popup.js?ver=<%=today%>"></script>
		</c:when>
		<c:otherwise>
			<script type="text/javascript" src="/resources/js/ui/include/popup/indexRequest_popup_dev.js?ver=<%=today%>"></script>
		</c:otherwise>
	</c:choose>
	
	<script type="text/javascript">
		var auth_cd = "${users.auth_cd}";
		var loginUserWrkjobCd = "${users.wrkjob_cd}";
	</script>
	<style>
		input::-webkit-outer-spin-button, 
		input::-webkit-inner-spin-button { margin-left: 10px; } 
		
		input[type=number] {
			-moz-appearance:textfield;
		}
		
		.cke_bottom{display:none}	
		#cke_1_bottom{display:none}	
		#cke_2_bottom{display:none}	
		#cke_3_bottom{display:none}	
		
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
		</div>
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<div id="statusViewTab" class="easyui-tabs" data-options="border:false" style="width:100%;padding-bottom:0px;height:auto;">
				<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${users.wrkjob_cd}"/>
				<input type="hidden" id="dbid" name="dbid" value="${sqlTuning.dbid}"/>
				<input type="hidden" id="db_name" name="db_name" value="${sqlTuning.db_name}"/>
				<input type="hidden" id="tuning_no" name="tuning_no" value="${sqlTuning.tuning_no}"/>
				<input type="hidden" id="choice_div_cd" name="choice_div_cd" value="${sqlTuning.choice_div_cd}"/>
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd" value="${sqlTuning.tuning_status_cd}"/>
				<input type="hidden" id="perfr_id" name="perfr_id" value="${sqlTuning.perfr_id}"/>
				<input type="hidden" id="searchKey" name="searchKey" value="${tuningTargetSql.searchKey}"/>
				<input type="hidden" id="bfac_chk_no" name="bfac_chk_no" value="${tuningTargetSql.bfac_chk_no}"/>
				<input type="hidden" id="strStartDt" name="strStartDt" value="${tuningTargetSql.strStartDt}"/>
				<input type="hidden" id="strEndDt" name="strEndDt" value="${tuningTargetSql.strEndDt}"/>
				<input type="hidden" id="strGubun" name="strGubun" value="${tuningTargetSql.strGubun}"/>
				<input type="hidden" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}"/>
				<input type="hidden" id="tuning_complete_why_cd" name="tuning_complete_why_cd" value="${sqlTuning.tuning_complete_why_cd}"/>
				<input type="hidden" id="tuning_case_type_cd_temp" name="tuning_case_type_cd_temp" value="${sqlDetail.tuning_case_type_cd}"/>
				<input type="hidden" id="tuning_end_why_cd" name="tuning_end_why_cd" value="${sqlTuning.tuning_end_why_cd}"/>
				<input type="hidden" id="tuning_case_posting_yn" name="tuning_case_posting_yn" value="${sqlTuning.tuning_case_posting_yn}"/>
				<input type="hidden" id="except_target_yn" name="except_target_yn" value="${sqlTuning.except_target_yn}"/>
				<input type="hidden" id="list_sql_id" name="list_sql_id" value="${tuningTargetSql.sql_id}"/>
				<input type="hidden" id="list_tuning_status_cd" name="list_tuning_status_cd" value="${tuningTargetSql.tuning_status_cd}"/>
				<input type="hidden" id="list_tr_cd" name="list_tr_cd" value="${tuningTargetSql.tr_cd}"/>
				<input type="hidden" id="list_dbio" name="list_dbio" value="${tuningTargetSql.dbio}"/>
				<input type="hidden" id="list_selectValue" name="list_selectValue" value="${tuningTargetSql.selectValue}"/>
				<input type="hidden" id="VSQLCheck" name="VSQLCheck" value="${VSQLCheck}"/>
				<input type="hidden" id="bfac_chk_source" name="bfac_chk_source"/>
				<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
				<input type="hidden" id="tuningStatusArry" name="tuningStatusArry"/>
				<input type="hidden" id="completeArry" name="completeArry"/> <!-- 완료 사유 관련 파라미터 -->
				<input type="hidden" id="push_yn" name="push_yn" value="N"/>
				<input type="hidden" id="temporary_save_yn" name="temporary_save_yn" value=""/>
				<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${sqlTuning.tuning_complete_dt}"/>
<%-- 				<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${tuningTargetSql.tuning_complete_dt}"/> --%>
				
				<input type="hidden" id="file_nm" name="file_nm" value=""/>
				<input type="hidden" id="file_seq" name="file_seq" value=""/>
				<input type="hidden" id="org_file_nm" name="org_file_nm" value=""/>
				<input type="hidden" id="leader_yn" name="leader_yn" value="${leader_yn}"/>
				<input type="hidden" id="after_tuning_no" name="after_tuning_no" value="${sqlTuning.after_tuning_no}"/>
				<input type="hidden" id="operation_yn" name="operation_yn" value="${operation_yn}"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<input type="hidden" id="sqlTuningStatusCd" name="sqlTuningStatusCd" value="${sqlTuning.tuning_status_cd}"/>

				<!-- 튜닝중 텍스트박스 편집 가능/불가능 -->
				<c:set var="dataOptionsReadonlyFlag" scope="page" value=""/>
				<c:set var="textAreaReadonlyFlag" scope="page" value=""/>
				<c:set var="validatebox_invalid" scope="page" value=""/>
				<c:choose>
					<c:when test="${sqlTuning.tuning_status_cd eq '5'}">
						<c:set var="dataOptionsReadonlyFlag" scope="page" value="readonly:false"/>
						<c:set var="textAreaReadonlyFlag" scope="page" value=""/>
						<c:set var="validatebox_invalid" scope="page" value="validatebox-invalid"/>
					</c:when>
					<c:otherwise>
						<c:set var="dataOptionsReadonlyFlag" scope="page" value="readonly:true"/>
						<c:set var="textAreaReadonlyFlag" scope="page" value="readonly"/>
						<c:set var="validatebox_invalid" scope="page" value=""/>
					</c:otherwise>
				</c:choose>
				
				<div title="SQL 요청 상세">
					<div style="height:655px;overflow-y:auto;overflow-x:hidden;border:0px;border-bottom:1px solid;">
						<!-- SQL 요청 상세  START -->
						<c:choose>
							<c:when test="${choickDivCd eq '1' || choickDivCd eq '2' || choickDivCd eq 'G' || choickDivCd eq 'N' || choickDivCd eq 'H' || choickDivCd eq 'I' || choickDivCd eq 'J' }"> <!-- SQL 상세(선정)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${selection.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${selection.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${selection.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${selection.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${selection.choice_div_cd_nm}"/>
								<input type="hidden" id="asisElapsedTime" name="asisElapsedTime" value="${sqlDetail.asis_elapsed_time}"/>
								<input type="hidden" id="asisBufferGets" name="asisBufferGets" value="${sqlDetail.asis_buffer_gets}"/>
								<input type="hidden" id="asisPlanHashValue" name="asisPlanHashValue" value="${sqlDetail.asis_plan_hash_value}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${selection.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${selection.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${selection.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${selection.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${selection.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:19%;">
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${selection.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${selection.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${selection.tuning_request_dt}</td>
										<th>튜닝요청구분</th>
										<td>${selection.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${selection.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${selection.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${selection.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${selection.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${selection.sql_id}&nbsp;&nbsp;
											<c:if test="${ choickDivCd ne 'G' && choickDivCd ne 'H' && choickDivCd ne 'J'}">
												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${selection.sql_id}','${selection.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
											</c:if>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<c:if test="${choickDivCd eq 'G' }">
											<td>${sqlDetail.asis_plan_hash_value}</td>
										</c:if>
										<c:if test="${choickDivCd ne 'G' }">
											<td>${selection.plan_hash_value}</td>
										</c:if>
									</tr>
									<tr>
										<th>Elapsed Time per Exec(s)</th>
										<td>${selection.avg_elapsed_time}</td>
										<th>Buffer Gets per Exec</th>
										<td>${selection.avg_buffer_gets}</td>
										<th>Row Processed per Exec</th>
										<td>${selection.avg_row_processed}</td>
									</tr>
									<tr>
										<th>Executions</th>
										<td>${selection.executions}</td>
										<th>Module</th>
										<td>${selection.module}</td>
										<th>Parsing Schema Name</th>
										<td>${selection.parsing_schema_name}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td><input type="text" id="tr_cd" name="tr_cd" value="${selection.tr_cd}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${selection.dbio}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w130 easyui-linkbutton" data-options="" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-recycle fa-lg fa-fw"> </i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:400px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly><c:if test="${not empty sqlBindList}">/*&#10<c:forEach items="${sqlBindList}" var="sql">	${sql.bind_var_nm}	    ${sql.bind_var_value}&#10</c:forEach>*/&#10&#10&#10</c:if>${selection.sql_text} </textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '3' || choickDivCd eq 'B' }"> <!-- SQL 상세(요청)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${request.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value=""/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${request.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${request.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${request.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${request.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${request.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${request.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${request.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${request.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:19%;">
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${request.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${request.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>요청 일자</th>
										<td>${request.tuning_request_dt}</td>
										<th>튜닝대상선정구분</th>
										<td>${request.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>요청자</th>
<%-- 										<td>${request.tuning_requester_nm} ( ${request.tuning_requester_id} )</td> --%>
										<td><a href="javascript:;" class="" onClick="showUserInfoPopup();">${request.tuning_requester_nm}</a></td>
										<th>요청업무</th>
										<td>${request.tuning_requester_wrkjob_nm}</td>
										<th>요청자연락처</th>
										<td>${request.tuning_requester_tel_num}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${sqlTuning.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${sqlTuning.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자연락처</th>
										<td>${sqlTuning.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>프로그램 유형</th>
										<td>${request.program_type_cd_nm}</td>
										<th>배치작업주기</th>
										<td>${request.batch_work_div_cd_nm}</td>
										<th>수행횟수</th>
										<td>${request.exec_cnt}</td>

									</tr>
									<tr>
										<th>DB</th>
										<td>${request.db_name}</td>
										<th>DB접속정보</th>
										<td>${request.parsing_schema_name}</td>
										<th>튜닝완료요청일자</th>
										<td>${request.tuning_complete_due_dt}</td>
									</tr>
									<tr>

										<th>소스파일명(Full Path)</th>
										<td ><input type="text" id="tr_cd" name="tr_cd" value="${request.tr_cd}" class="easyui-textbox" style="width:98%" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${request.dbio}" class="easyui-textbox" style="width:548px;" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>현재수행시간</th>
										<td>${request.current_elap_time}</td>
										<th>결과건수</th>
										<td>${request.forecast_result_cnt}</td>
										<th>목표 수행시간</th>
										<td>${request.goal_elap_time}</td>
									</tr>
								</table>
								<table class="detailT">
									<colgroup>	
										<col style="width:10%;">
										<col style="width:35%;">
										<col style="width:10%;">
										<col style="width:35%;">
									</colgroup>
									<tr>
										<th>업무특이사항</th>
										<td><textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:45px" readonly>${request.wrkjob_peculiar_point}</textarea></td>
										<th>요청사유</th>
										<td><textarea name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:45px" readonly>${request.request_why}</textarea></td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td style="/* padding:5px; */ height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:300px;" wrap="off" readonly>${request.sql_text}</textarea> 
										</td>
										<th>SQL BIND</th>
										<td><textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:5px;width:98%;height:300px;" readonly>${request.sql_desc}</textarea></td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="3">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
									<%-- <tr>
										<td colspan="2" style="padding:5px;height:350px;">
											<div class="easyui-layout" style="width:100%;height:350px;">
												<div data-options="region:'center'" title="SQL Text" style="padding:5px;">
													<div id="sqlTextArea" style="width:100%;height:100%;overflow-y:auto;">
														${fn:replace(request.sql_text,newLineChar,'<br/>')}
													</div>
												</div>
												<div id="bindList" data-options="region:'east',split:true,hideCollapsedContent:false" title="Bind Varable List" style="width:40%;padding:3px">
													<c:if test="${bindSetList != null}">
														<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:99%">
															<colgroup>
																<col style="width:10%;">
																<col style="width:90%;">
															</colgroup>
															<c:forEach items="${bindSetList}" var="bindSet" varStatus="status">
																<tr>
																	<th>바인드<br/>${status.count}</th>
																	<td>
																		<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:99%">
																			<colgroup>
																				<col style="width:15%;">
																				<col style="width:24%;">
																				<col style="width:24%;">
																				<col style="width:22%;">
																				<col style="width:15%;">
																			</colgroup>
																			<tr>
																				<th>순번</th>
																				<th>변수명</th>
																				<th>변수값</th>
																				<th>변수타입</th>
																				<th>필수여부</th>
																			</tr>
																			<c:forEach items="${sqlBindList}" var="sqlBind" varStatus="stat">
																				<c:if test="${bindSet.bind_set_seq eq sqlBind.bind_set_seq}">
																					<tr>
																						<td class="ctext">${sqlBind.bind_seq}</td>
																						<td>${sqlBind.bind_var_nm}</td>
																						<td>${sqlBind.bind_var_value}</td>
																						<td class="ctext">
																							<c:choose>
																								<c:when test="${sqlBind.bind_var_type eq 'string'}">String 타입</c:when>
																								<c:when test="${sqlBind.bind_var_type eq 'number'}"> Number 타입</c:when>
																								<c:when test="${sqlBind.bind_var_type eq 'date'}">Date 타입</c:when>
																								<c:when test="${sqlBind.bind_var_type eq 'char'}">Char 타입</c:when>
																							</c:choose>
																						</td>
																						<td class="ctext">${sqlBind.mandatory_yn}</td>
																					</tr>
																				</c:if>
																			</c:forEach>
																		</table>
																	</td>
																</tr>
															</c:forEach>
														</table>
													</c:if>
												</div>
											</div>
										</td>
									</tr>	 --%>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '4'}"> <!-- SQL 상세(FULL SCAN)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${fullScan.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${fullScan.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${fullScan.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${fullScan.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${fullScan.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${fullScan.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${fullScan.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${fullScan.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${fullScan.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${fullScan.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:19%;">
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${fullScan.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${fullScan.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${fullScan.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${fullScan.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${fullScan.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${fullScan.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${fullScan.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${fullScan.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${fullScan.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${fullScan.sql_id}','${fullScan.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${fullScan.plan_hash_value}</td>
									</tr>
									<tr>
										<th>Module</th>
										<td>${fullScan.module}</td>
										<th>Action</th>
										<td>${fullScan.action}</td>
										<th>Operations</th>
										<td>${fullScan.operations}</td>
									</tr>
									<tr>
										<th>Parsing Schema Name</th>
										<td>${fullScan.parsing_schema_name}</td>
										<th>Executions</th>
										<td>${fullScan.executions}</td>
										<th>Elapsed Time</th>
										<td>${fullScan.elapsed_time}</td>
									</tr>
									<tr>
										<th>CPU Time</th>
										<td>${fullScan.cpu_time}</td>
										<th>Rows Processed</th>
										<td>${fullScan.rows_processed}</td>
										<th>Disk Read</th>
										<td>${fullScan.disk_reads}</td>
									</tr>
									<tr>
										<th>Buffer Gets</th>
										<td>${fullScan.buffer_gets}</td>
										<th>소스파일명(Full Path)</th>
										<td colspan="3"><input type="text" id="tr_cd" name="tr_cd" value="${fullScan.tr_cd}" class="w300 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
									</tr>
									<tr>
										<th>SQL식별자(DBIO)</th>
										<td colspan="5">
											<input type="text" id="dbio" name="dbio" value="${fullScan.dbio}" class="w300 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:420px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${fullScan.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '5'}"> <!-- SQL 상세(PLAN 변경)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${planChange.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${planChange.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${planChange.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${planChange.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${planChange.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${planChange.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${planChange.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${planChange.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${planChange.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${planChange.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:18%;">
										<col style="width:15%;">
										<col style="width:19%;">
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${planChange.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${planChange.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${planChange.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${planChange.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${planChange.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${planChange.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${planChange.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${planChange.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${planChange.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformancePlan('${planChange.sql_id}','${planChange.before_plan_hash_value}','${planChange.after_plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${planChange.after_plan_hash_value}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td><input type="text" id="tr_cd" name="tr_cd" value="${planChange.tr_cd}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${planChange.dbio}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:420px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${planChange.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:14%;">
										<col style="width:14%;">
										<col style="width:14%;">
										<col style="width:14%;">
										<col style="width:14%;">
										<col style="width:15%;">
									</colgroup>
									<tr>
										<th></th>
										<th>PLAN_HASH_VALUE</th>
										<th>수행시간</th>
										<th>수행횟수</th>
										<th>버퍼수</th>
										<th>CPU시간</th>
										<th>최대수행시간</th>
									</tr>
									<tr>
										<th>PLAN 변경전</th>
										<td class="ctext">${planChange.before_plan_hash_value}</td>
										<td class="rtext">${planChange.before_elapsed_time}</td>
										<td class="rtext">${planChange.before_executions}</td>
										<td class="rtext">${planChange.before_buffer_gets}</td>
										<td class="rtext">${planChange.before_cpu_time}</td>
										<td class="rtext">${planChange.before_max_elapsed_time}</td>
									</tr>
									<tr>
										<th>PLAN 변경후</th>
										<td class="ctext">${planChange.after_plan_hash_value}</td>
										<td class="rtext">${planChange.after_elapsed_time}</td>
										<td class="rtext">${planChange.after_executions}</td>
										<td class="rtext">${planChange.after_buffer_gets}</td>
										<td class="rtext">${planChange.after_cpu_time}</td>
										<td class="rtext">${planChange.after_max_elapsed_time}</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '6'}"> <!-- SQL 상세(신규SQL)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${newSql.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${newSql.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${newSql.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${newSql.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${newSql.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${newSql.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${newSql.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${newSql.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${newSql.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${newSql.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:19%;"/>
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${newSql.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${newSql.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${newSql.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${newSql.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${newSql.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${newSql.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${newSql.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${newSql.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${newSql.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${newSql.sql_id}','${newSql.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${newSql.plan_hash_value}</td>
									</tr>
									<tr>
										<th>First Load Time</th>
										<td>${newSql.first_load_time}</td>
										<th>Last Load Time</th>
										<td colspan="3">${newSql.last_load_time}</td>
									</tr>
									<tr>
										<th>Elapsed Time per Exec(s)</th>
										<td>${newSql.avg_elapsed_time}</td>
										<th>Buffer Gets per Exec</th>
										<td>${newSql.avg_buffer_gets}</td>
										<th>Row Processed per Exec</th>
										<td>${newSql.avg_rows_processed}</td>
									</tr>
									<tr>
										<th>Executions</th>
										<td>${newSql.executions}</td>
										<th>Module</th>
										<td>${newSql.module}</td>
										<th>Parsing Schema Name</th>
										<td>${newSql.parsing_schema_name}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td><input type="text" id="tr_cd" name="tr_cd" value="${newSql.tr_cd}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${newSql.dbio}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:420px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${newSql.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '7'}"> <!-- SQL 상세(TEMP과다사용)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${tempOver.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${tempOver.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${tempOver.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${tempOver.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${tempOver.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${tempOver.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${tempOver.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${tempOver.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${tempOver.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${tempOver.tuning_requester_tel_num}"/>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:19%;"/>
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${tempOver.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${tempOver.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${tempOver.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${tempOver.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${tempOver.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${tempOver.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${tempOver.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${tempOver.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${tempOver.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${tempOver.sql_id}','${tempOver.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${tempOver.plan_hash_value}</td>
									</tr>
									<tr>
										<th>Module</th>
										<td>${tempOver.module}</td>
										<th>Program</th>
										<td>${tempOver.program}</td>
										<th>Temp Usage(MB)</th>
										<td>${tempOver.temp_usage}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td><input type="text" id="tr_cd" name="tr_cd" value="${tempOver.tr_cd}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${tempOver.dbio}" class="w200 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:420px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${tempOver.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq 'C' or choickDivCd eq 'D' or choickDivCd eq 'E' }"> <!-- SQL 상세(TOPSQL , OFFLOAD 비효율 SQL, OFFLOAD 효울저하 SQL)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${topSqlUnionOffloadSql.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${topSqlUnionOffloadSql.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${topSqlUnionOffloadSql.tuning_no}"/>
<%-- 								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${topSqlUnionOffloadSql.tuning_request_dt}"/> --%>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${topSqlUnionOffloadSql.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${topSqlUnionOffloadSql.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${topSqlUnionOffloadSql.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${topSqlUnionOffloadSql.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${topSqlUnionOffloadSql.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${topSqlUnionOffloadSql.tuning_requester_tel_num}"/>
								
								<table class="detailT">
									<colgroup>
										<col style="width:9%;"/>
										<col style="width:14%;"/>
										<col style="width:9%;"/>
										<col style="width:14%;"/>
										<col style="width:10%;"/>
										<col style="width:13%;"/>
										<col style="width:10%;"/>
										<col style="width:13%;"/>
										<col style="width:10%;"/>
										<col style="width:13%;"/>
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${topSqlUnionOffloadSql.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w100 easyui-linkbutton" style="letter-spacing:-1px; important;"onClick="showProcessHistoryPopup('${topSqlUnionOffloadSql.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i>프로세스 처리이력</a>
											</c:if>
										</td>
										<th>진단일자</th>
										<td>${topSqlUnionOffloadSql.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${topSqlUnionOffloadSql.choice_div_cd_nm}</td>
										<th>업무담당자</th>
										<td>${topSqlUnionOffloadSql.wrkjob_mgr_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${topSqlUnionOffloadSql.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${topSqlUnionOffloadSql.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${topSqlUnionOffloadSql.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${topSqlUnionOffloadSql.sql_id}','${topSqlUnionOffloadSql.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${topSqlUnionOffloadSql.plan_hash_value}</td>
										<th>First Load Time</th>
										<td>${topSqlUnionOffloadSql.first_load_time}</td>
										<th>Last Load Time</th>
										<td>${topSqlUnionOffloadSql.last_load_time}</td>
									</tr>
									<tr>
										<th>Parsing Schema Name</th>
										<td>${topSqlUnionOffloadSql.parsing_schema_name}</td>
										<th>Elapsed Time</th>
										<td>${topSqlUnionOffloadSql.avg_elapsed_time}</td>
										<th>CPU Time</th>
										<td>${topSqlUnionOffloadSql.avg_cpu_time}</td>
										<th>Buffer Gets</th>
										<td>${topSqlUnionOffloadSql.avg_buffer_gets}</td>
										<th>Disk Reads</th>
										<td>${topSqlUnionOffloadSql.avg_disk_reads}</td>
									</tr>
									<tr>
										<th>Row Processed</th>
										<td>${topSqlUnionOffloadSql.avg_rows_processed}</td>
										<th>Executions</th>
										<td>${topSqlUnionOffloadSql.executions}</td>
										<th  style="font-size:11px;letter-spacing:-1;">Elapsed Time Activity(%)</th>
										<td>${topSqlUnionOffloadSql.ratio_elapsed_time}</td>
										<th style="font-size:11px;letter-spacing:-1;">CPU Time Activity(%)</th>
										<td>${topSqlUnionOffloadSql.ratio_cpu_time}</td>
										<th style="font-size:11px;letter-spacing:-1;">Buffer Gets Activity(%)</th>
										<td>${topSqlUnionOffloadSql.ratio_buffer_gets}</td>
									</tr>
									<tr>
										<th>Offload 여부</th>
										<td>${topSqlUnionOffloadSql.offload_yn}</td>
										<th style="font-size:11px;letter-spacing:-1;">Offload I/O Saved(%)</th>
										<td>${topSqlUnionOffloadSql.io_saved}</td>
										<th style="font-size:11px;letter-spacing:-1;">일주일전 I/O Saved(%)</th>
										<td>${topSqlUnionOffloadSql.before_1_week_io_saved}</td>
										<th style="font-size:11px;letter-spacing:-1;">전주대비 I/O 감소량(%)</th>
										<td>${topSqlUnionOffloadSql.offload_io_saved_decrease}</td>
										<th>Parallel Servers(cnt)</th>
										<td>${topSqlUnionOffloadSql.parallel_servers}</td>
									</tr> 
									<tr>
										<th>Module</th>
										<td>${topSqlUnionOffloadSql.module}</td>
										<th>소스파일명(Full Path)</th>
										<td colspan ="3" ><input type="text" id="tr_cd" name="tr_cd" value="${topSqlUnionOffloadSql.tr_cd}" class="w500 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${topSqlUnionOffloadSql.dbio}" class="w350 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="9" style="padding:10px;height:420px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${topSqlUnionOffloadSql.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="9">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq 'F' or choickDivCd eq 'K' or choickDivCd eq 'L' }"> <!-- SQL 상세(배포후 성능점검, 사용자검증, 자동검증)  -->
								<table class="detailT">
									<colgroup>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:18%;"/>
										<col style="width:15%;"/>
										<col style="width:19%;"/>
									</colgroup>
									<tr>
										<th>튜닝번호</th>
										<td>
											${deployAfter.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})&nbsp;&nbsp;
											<c:if test="${auth_cd ne 'ROLE_DEV'}">
												<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showProcessHistoryPopup('${deployAfter.tuning_no}');"><i class="btnIcon fas fa-history fa-lg fa-fw"></i> 프로세스 처리이력</a>
											</c:if>
										</td>
										<th>선정일자</th>
										<td>${deployAfter.gather_day}</td>
										<th>튜닝요청구분</th>
										<td>${deployAfter.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>업무담당자</th>
										<td>${deployAfter.wrkjob_mgr_nm}</td>
										<th>담당업무</th>
										<td>${deployAfter.wrkjob_mgr_wrkjob_nm}</td>
										<th>업무담당자 연락처</th>
										<td>${deployAfter.wrkjob_mgr_tel_num}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${deployAfter.db_name}</td>
										<th>SQL_ID</th>
										<td>
											${deployAfter.sql_id}&nbsp;&nbsp;
											<a href="javascript:;" class="w80 easyui-linkbutton" onClick="createSqlPerformance('${deployAfter.sql_id}','${deployAfter.plan_hash_value}');"><i class="btnIcon fas fa-info-circle fa-lg fa-fw"></i> SQL 정보</a>
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${deployAfter.plan_hash_value}</td>
									</tr>
									<tr>
										<th>업무</th>
										<td>${deployAfter.wrkjob_cd_nm}</td>
										<th>배포ID</th>
										<td>${deployAfter.deploy_id}</td>
										<th>배포요청자</th>
										<td>${deployAfter.user_nm}</td>
									</tr>
									<tr>
										<th>배포요청일자</th>
										<td>${deployAfter.deploy_request_dt}</td>
										<th>성능점검ID</th>
										<td>${deployAfter.perf_check_id}</td>
										<th>프로그램ID</th>
										<td>${deployAfter.program_id}</td>
									</tr>
									<tr>
										<th>Elapsed Time per Exec(s)</th>
										<td>${deployAfter.avg_elapsed_time}</td>
										<th>Buffer Gets per Exec</th>
										<td>${deployAfter.avg_buffer_gets}</td>
										<th>Row Processed per Exec</th>
										<td>${deployAfter.avg_row_processed}</td>
									</tr>
									<tr>
										<th>Executions</th>
										<td>${deployAfter.executions}</td>
										<th>Module</th>
										<td>${deployAfter.module}</td>
										<th>Parsing Schema Name</th>
										<td>${deployAfter.parsing_schema_name}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td><input type="text" id="tr_cd" name="tr_cd" value="${deployAfter.tr_cd}" class="w290 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/></td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">
											<input type="text" id="dbio" name="dbio" value="${deployAfter.dbio}" class="w548 easyui-textbox" data-options="${dataOptionsReadonlyFlag}"/>&nbsp;
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_AddTabTuningHistory();"><i class="btnIcon fas fa-list-alt fa-lg fa-fw"></i> 튜닝 이력 조회</a>
											&nbsp;${sqlTuningHistoryCount}건
										</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:360px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly><c:if test="${not empty sqlBindList}">/*&#10<c:forEach items="${sqlBindList}" var="sql">	${sql.bind_var_nm}	    ${sql.bind_var_value}&#10</c:forEach>*/&#10&#10&#10</c:if>${deployAfter.sql_text}</textarea>
										</td>
									</tr>
									<tr>
										<th>첨부파일</th>
										<td colspan="5">
											<c:forEach items="${tuningFiles}" var="result" varStatus="status">
												<c:if test="${result.file_seq ne ''}">
													<span>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="downTuningFile('${result.file_nm}','${result.org_file_nm}');">${result.org_file_nm}</a>
													</span>
												</c:if>
											</c:forEach>
										</td>
									</tr>
								</table>
							</c:when>
						</c:choose>
					</div>
					<div class="dtlBtn inlineBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(1);"><i class="btnIcon fas fa-chevron-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
					<!-- SQL 요청 상세  END -->
				</div>
				
				<div title="SQL 개선 상세">
					<div style="height:655px;overflow-y:auto;overflow-x:hidden;border:0px;border-bottom:1px solid;">
						<!-- SQL 개선 상세  START -->
						<c:if test="${imprBeforeAfter ne null}">
							<table class="detailT">
								<colgroup>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:11%;"/>
									<col style="width:12%;"/>
								</colgroup>
								<tr>
									<th colspan="3">개선전</th>
									<th colspan="3">개선후</th>
									<th colspan="3">개선율 (%)</th>
								</tr>
								<tr>
									<th>응답시간 (Sec)</th>
									<th>블럭수</th>
									<th>PGA 사용량(MB)</th>
									<th>응답시간 (Sec)</th>
									<th>블럭수</th>
									<th>PGA 사용량(MB)</th>
									<th>응답시간</th>
									<th>블럭수</th>
									<th>PGA 사용량</th>
								</tr>
								<tr>
									<td class="ctext"><input type="number" id="imprb_elap_time1" name="imprb_elap_time1" value="${sqlDetail.imprb_elap_time}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="imprb_buffer_cnt1" name="imprb_buffer_cnt1" value="${sqlDetail.imprb_buffer_cnt}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="imprb_pga_usage1" name="imprb_pga_usage1" value="${sqlDetail.imprb_pga_usage}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="impra_elap_time1" name="impra_elap_time1" value="${sqlDetail.impra_elap_time}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="impra_buffer_cnt1" name="impra_buffer_cnt1" value="${sqlDetail.impra_buffer_cnt}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="impra_pga_usage1" name="impra_pga_usage1" value="${sqlDetail.impra_pga_usage}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
		
									<td class="ctext"><input type="number" id="elap_time_impr_ratio1" name="elap_time_impr_ratio1" value="${sqlDetail.elap_time_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="buffer_impr_ratio1" name="buffer_impr_ratio1" value="${sqlDetail.buffer_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
									<td class="ctext"><input type="number" id="pga_impr_ratio1" name="pga_impr_ratio1" value="${sqlDetail.pga_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:95%"/></td>
								</tr>
							</table>
						</c:if>
						<table class="detailT2">
							<colgroup>
								<col style="width:10%;"/>
								<col style="width:40%;"/>
								<col style="width:10%;"/>
								<col style="width:40%;"/>
							</colgroup>
							<tr>
								<th>문제점</th>
								<td><textarea name="controversialist" id="controversialist" rows="10" style="margin-top:5px;width:100%;height:100px" ${textAreaReadonlyFlag} class="${validatebox_invalid}">${sqlDetail.controversialist}</textarea></td>
								<th>개선내역</th>
								<td><textarea name="impr_sbst" id="impr_sbst" rows="10" style="margin-top:5px;width:100%;height:100px" ${textAreaReadonlyFlag} class="${validatebox_invalid}">${sqlDetail.impr_sbst}</textarea></td>
							</tr>

							<tr>
								<th>
									개선SQL<br/><br/>
									<a href="javascript:;" id="sqlCopyBtn_imprSql" class="sqlCopyBtn w100 easyui-linkbutton" data-clipboard-target="#impr_sql_text_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a>
								</th>
								<td colspan="3">
									<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:100%;height:300px;" wrap="off" ${textAreaReadonlyFlag} class="${validatebox_invalid}">
										<c:out value="${sqlDetail.impr_sql_text}"/>
									</textarea>
									<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="width:0px;height:0px;">
										${sqlDetail.impr_sql_text}
									</textarea>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									개선전 실행계획<br/><br/>
									<a href="javascript:;" id="sqlCopyBtn_imprb_plan" class="sqlCopyBtn w100 easyui-linkbutton" data-clipboard-target="#imprb_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
								</th>
								<th colspan="2">
									개선후 실행계획<br/><br/>
									<a href="javascript:;" id="sqlCopyBtn_impra_plan" class="sqlCopyBtn w100 easyui-linkbutton" data-clipboard-target="#impra_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
								</th>
							</tr>
							<tr>

								<td colspan="2">
									<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:100%;height:300px;" wrap="off" ${textAreaReadonlyFlag} class="${validatebox_invalid}">
										<c:out value="${sqlDetail.imprb_exec_plan}"/>
									</textarea>
									<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="width:0px;height:0px;">
										${sqlDetail.imprb_exec_plan}
									</textarea>
								</td>
								<td colspan="2">
									<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:100%;height:300px;" wrap="off" ${textAreaReadonlyFlag} class="${validatebox_invalid}">
										<c:out value="${sqlDetail.impra_exec_plan}"/>
									</textarea>
									<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="width:0px;height:0px;">
										${sqlDetail.impra_exec_plan}
									</textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table class="detailT">
										<colgroup>
											<col style="width:11%;"/>
											<col style="width:11%;"/>
											<col style="width:11%;"/>
										</colgroup>
										<tr>
											<th>응답시간 (Sec)</th>
											<th>블럭수</th>
											<th>PGA 사용량(MB)</th>
										</tr>
										<tr>
											<td class="ctext"><input type="number" id="imprb_elap_time" name="imprb_elap_time" value="${sqlDetail.imprb_elap_time}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
											<td class="ctext"><input type="number" id="imprb_buffer_cnt" name="imprb_buffer_cnt" value="${sqlDetail.imprb_buffer_cnt}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
											<td class="ctext"><input type="number" id="imprb_pga_usage" name="imprb_pga_usage" value="${sqlDetail.imprb_pga_usage}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
										</tr>
										
									</table>
								</td>
								<td colspan="2">
									<table class="detailT">
										<colgroup>
											<col style="width:11%;"/>
											<col style="width:11%;"/>
											<col style="width:11%;"/>
										</colgroup>
										<tr>
											<th>응답시간 (Sec)</th>
											<th>블럭수</th>
											<th>PGA 사용량(MB)</th>
										</tr>
										<tr>
											<td class="ctext"><input type="number" id="impra_elap_time" name="impra_elap_time" value="${sqlDetail.impra_elap_time}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
											<td class="ctext"><input type="number" id="impra_buffer_cnt" name="impra_buffer_cnt" value="${sqlDetail.impra_buffer_cnt}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
											<td class="ctext"><input type="number" id="impra_pga_usage" name="impra_pga_usage" value="${sqlDetail.impra_pga_usage}" data-options="${dataOptionsReadonlyFlag}" class="easyui-textbox" style="width:95%;"/></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<th>개선율(%)</th>
								<td colspan="3">
									<table width="100%">
										<colgroup>
											<col style="width:10%;"/>
											<col style="width:23%;"/>
											<col style="width:10%;"/>
											<col style="width:23%;"/>
											<col style="width:10%;"/>
											<col style="width:23%;"/>
										</colgroup>
										<tr>
											<th>응답시간</th>
											<td class="ctext"><input type="number" id="elap_time_impr_ratio" name="elap_time_impr_ratio" value="${sqlDetail.elap_time_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:250px"/></td>
											<th>블럭수</th>
											<td class="ctext"><input type="number" id="buffer_impr_ratio" name="buffer_impr_ratio" value="${sqlDetail.buffer_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:250px"/></td>
											<th>PGA 사용량</th>
											<td class="ctext"><input type="number" id="pga_impr_ratio" name="pga_impr_ratio" value="${sqlDetail.pga_impr_ratio}" data-options="readonly:true" class="easyui-textbox" style="width:250px"/></td>
										</tr>
									</table>
								
								</td>
							<tr>
								<th>인덱스내역
								</th>
								<td colspan="3">
									<div id="indexHist" class="easyui-panel tableBorder-zero" data-options="border:false" style="width:100%;min-height:200px;padding-top:10px;">
										<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false" >
											<tbody><tr></tr></tbody>
										</table>
									</div>
									
									<!-- 튜닝상태가 6:튜닝완료(적용대기)이거나 8:튜닝종료일 경우에는 수정불가 -->
									<c:choose>
										<c:when test="${sqlTuning.tuning_status_cd eq '6'}">
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
									
<!-- 									<div class="delBtn"> -->
<!-- 										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ModifyIndex();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 인덱스 변경</a> -->
<!-- 										<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSelectedRow();"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 선택 삭제</a> -->
<!-- 									</div>									 -->
									<sec:authorize access="hasAnyRole('ROLE_DBMANAGER','ROLE_TUNER')">
										<!-- 튜닝중 -->
										<c:if test="${sqlTuning.tuning_status_cd eq '5'}">
											<div class="delBtn">
												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_ModifyIndex();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 인덱스 변경</a>
<!-- 												<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSelectedRow();"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 선택 삭제</a> -->
											</div>									
										</c:if>
									</sec:authorize>										
								</td>
							</tr>							
						</table>

					</div>

					<div class="dtlBtn inlineBtn">
						<!-- 튜닝종료 후 DBA/튜너가 튜닝 값을 수정할 수 있도록 함 -->
<%-- 						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER','ROLE_TUNER')"> --%>
<%-- 								<c:if test="${sqlTuning.tuning_status_cd eq '6'}"> --%>
<!-- 									<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ModifyTuningResult(this);"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 튜닝 결과값 변경</a> -->
<%-- 								</c:if> --%>
<%-- 						</sec:authorize> --%>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(0);"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(2);"><i class="btnIcon fas fa-chevron-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
					<!-- SQL 개선 상세  END -->
				</div>

				<div title="개선 프로세스">
					<div style="height:655px;overflow-y:auto;overflow-x:hidden;border-bottom:1px solid;">
						<!-- 개선 프로세스  START -->
						<!-- 튜닝중 버튼 활성/비활성 -->
						<c:set var="tuningCancelStatus" scope="page" value=""/>
						<c:if test="${sqlTuning.tuning_status_cd ne '5'}">
							<c:set var="tuningCancelStatus" scope="page" value="disabled:true"/>
						</c:if>
						<!-- DB관리자도 튜닝이 가능하도록 처리, 튜닝중으로 변경 2018.12.14 -->
						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER','ROLE_TUNER')">
							<div class="dtlBtn inlineBtn">
								<!-- 튜닝대기(3), 튜닝완료(6), 적용반려(7) 일 경우 튜닝중으로 변경 가능  -->
<%-- 								<c:if test="${sqlTuning.tuning_status_cd eq '3' || sqlTuning.tuning_status_cd eq '6' || sqlTuning.tuning_status_cd eq '7'}"> --%>
<%-- 									<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SaveTuning('${sqlTuning.tuning_status_cd}');"><i class="btnIcon fas fa-hourglass-half fa-lg fa-fw"></i> 튜닝중 처리</a> --%>
<%-- 								</c:if> --%>
								<!-- 튜닝대기(3), 튜닝완료(6)일 경우 튜닝중으로 변경 가능  -->
								<!-- 적용반려(7)은 튜닝중 처리 불가 -->
								<c:if test="${sqlTuning.tuning_status_cd eq '3' || sqlTuning.tuning_status_cd eq '6'}">
									<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SaveTuning('${sqlTuning.tuning_status_cd}');"><i class="btnIcon fas fa-hourglass-half fa-lg fa-fw"></i> 튜닝중 처리</a>
								</c:if>
								<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${tuningCancelStatus}" onClick="Btn_SaveTuningCancel();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝 취소</a>
							</div>
						</sec:authorize>
						<!-- 튜닝담당자 버튼 활성/비활성 -->
						<c:set var="tunerStatus" scope="page" value=""/>
						<c:set var="tunerReAssign" scope="page" value="N"/>
						<c:if test="${sqlTuning.tuning_status_cd ne '1' && sqlTuning.tuning_status_cd ne '2'}">
							<c:set var="tunerStatus" scope="page" value="disabled:true"/>
						</c:if>
						<c:if test="${sqlTuning.tuning_status_cd eq '3' || sqlTuning.tuning_status_cd eq '5'}">
							<c:set var="tunerReAssign" scope="page" value="Y"/>
						</c:if>

						<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER')">
							<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 튜닝담당자 지정/변경</span></div>
							<table class="detailT">
								<colgroup>
									<col style="width:15%;"/>
									<col style="width:85%;"/>
								</colgroup>
								<tr>
									<th>튜닝담당자</th>
									<td><select id="selectTuner" name="selectTuner" data-options="panelHeight:'auto'" required="required" class="w120 easyui-combobox"></select></td>
								</tr>
							</table>
							<div class="dtlBtn inlineBtn">
								<a href="javascript:;" class="w130 easyui-linkbutton" data-options="${tunerStatus}" onClick="Btn_SaveTunerAssign('N');"><i class="btnIcon fas fa-user-plus fa-lg fa-fw"></i> 튜닝담당자 지정</a>
								<c:if test="${tunerReAssign eq 'Y' && sqlTuning.tuning_status_cd ne '5'}">
									<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_SaveTunerAssign('R');"><i class="btnIcon fab fa-slideshare fa-lg fa-fw"></i> 튜닝담당자 변경</a>
								</c:if>
							</div>
						</sec:authorize>
						<!-- 튜닝완료 버튼 활성/비활성 -->
						<c:set var="completeStatus" scope="page" value=""/>
						<c:set var="completeStatusIn" scope="page" value=""/>
						<c:set var="checkboxDisabled" scope="page" value=""/>
						<c:set var="changeWrkUser" scope="page" value=""/>
						<!-- DB관리자도 튜닝이 가능하도록 처리, 버튼 활성화를 TUNER와 똑같이 변경 2018.12.14 -->
<%-- 						<sec:authorize access="hasAnyRole('ROLE_TUNER')"> --%>
						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER','ROLE_TUNER')">
							<c:if test="${sqlTuning.tuning_status_cd ne '5'}">
								<c:set var="completeStatus" scope="page" value="disabled:true"/>
								<c:set var="completeStatusIn" scope="page" value="disabled:true,"/>
								<c:set var="checkboxDisabled" scope="page" value="disabled"/>
							</c:if>
							<c:if test="${sqlTuning.tuning_status_cd ne '6'}">
								<c:set var="changeWrkUser" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<!-- DB관리자도 튜닝이 가능하도록 처리, 버튼 활성화를 TUNER와 똑같이 변경 2018.12.14 -->
<%-- 						<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER','ROLE_DEV')"> --%>
						<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DEV')">
							<c:set var="completeStatus" scope="page" value="disabled:true"/>
							<c:set var="completeStatusIn" scope="page" value="disabled:true,"/>
							<c:set var="checkboxDisabled" scope="page" value="disabled"/>
							<c:if test="${leader_yn eq 'N'}">
								<c:set var="changeWrkUser" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_DEV')">
							<c:if test="${leader_yn eq 'Y'}">
								<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 업무 담당자 변경</span></div>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tr>
										<th>업무담당자</th>
										<td>
											<input type="hidden" id="u_before_wrkjob_mgr_id" name="u_before_wrkjob_mgr_id" value="${sqlTuning.wrkjob_mgr_id}"/>
											<input type="hidden" id="u_before_wrkjob_mgr_nm" name="u_before_wrkjob_mgr_nm" value="${sqlTuning.wrkjob_mgr_nm}"/>
											<input type="hidden" id="u_wrkjob_mgr_id" name="u_wrkjob_mgr_id"/>
											<input type="hidden" id="u_wrkjob_mgr_wrkjob_cd" name="u_wrkjob_mgr_wrkjob_cd"/>
	<!-- 										<input type="text" id="u_wrkjob_mgr_nm" name="u_wrkjob_mgr_nm" data-options="readonly:true" class="w120 easyui-textbox"/> -->
	<!-- 										<input type="text" id="u_wrkjob_mgr_wrkjob_nm" name="u_wrkjob_mgr_wrkjob_nm" data-options="readonly:true" class="w120 easyui-textbox"/> -->
	<!-- 										<input type="text" id="u_wrkjob_mgr_tel_num" name="u_wrkjob_mgr_tel_num" data-options="readonly:true" class="w120 easyui-textbox"/> -->
											<input type="text" id="u_wrkjob_mgr_nm" name="u_wrkjob_mgr_nm"  value="${sqlTuning.wrkjob_mgr_nm}" data-options="readonly:true" class="w120 easyui-textbox"/>
											<input type="text" id="u_wrkjob_mgr_wrkjob_nm" name="u_wrkjob_mgr_wrkjob_nm"  value="${sqlTuning.wrkjob_mgr_wrkjob_nm}" data-options="readonly:true" class="w120 easyui-textbox"/>
											<input type="text" id="u_wrkjob_mgr_tel_num" name="u_wrkjob_mgr_tel_num"  value="${sqlTuning.wrkjob_mgr_tel_num}" data-options="readonly:true" class="w120 easyui-textbox"/>

											<a href="javascript:;" class="w120 easyui-linkbutton" onClick="showWorkTunerPopup();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 업무담당자 조회</a>
										</td>
									</tr>
								</table>
								<div class="dtlBtn inlineBtn">
									<a href="javascript:;" class="w120 easyui-linkbutton" data-options="${changeWrkUser}" onClick="Btn_ChangeWorkUser();"><i class="btnIcon fab fa-slideshare fa-lg fa-fw"></i> 업무 담당자 변경</a>
								</div>
							</c:if>
						</sec:authorize>

						<sec:authorize access="hasAnyRole('ROLE_TUNER','ROLE_DBMANAGER')">
							<!-- 접수(튜닝대기) 취소 출력 -->
							<c:set var="receiptCancelStatus" scope="page" value=""/>
							<c:if test="${sqlTuning.tuning_status_cd ne '3'}">
								<c:set var="receiptCancelStatus" scope="page" value="disabled:true"/>
							</c:if>
							<!-- 튜닝 상태가 튜닝중일 경우는 접수 취소가 나오지 않도록 처리 2018-08-17 -->
							<c:if test="${sqlTuning.tuning_status_cd ne '5'}">
								<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 접수 취소</span></div>
								<table class="detailT">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<c:if test="${sqlDetail.tuning_receipt_cancel_dt ne null}">
										<tr>
											<th>접수 취소 일시</th>
											<td>${sqlDetail.tuning_receipt_cancel_dt}</td>
										</tr>
									</c:if>
									<tr>
										<th>접수 취소 사유</th>
										<td><textarea name="tuning_receipt_cancel_why" id="tuning_receipt_cancel_why" rows="5" style="margin-top:5px;width:98%;height:40px">${sqlDetail.tuning_receipt_cancel_why}</textarea></td>
									</tr>
								</table>
								<div class="dtlBtn inlineBtn">
									<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${receiptCancelStatus}" onClick="Btn_SaveReceiptCancel();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 접수 취소</a>
								</div>
							</c:if>
						</sec:authorize>

						<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> SQL 튜닝완료</span></div>
						<table class="detailT">
							<colgroup>
								<col style="width:15%;">
								<col style="width:85%;">
							</colgroup>
							<c:if test="${sqlTuning.tuning_status_cd eq '6' || (sqlTuning.tuning_status_cd eq '8' && sqlTuning.tuning_complete_dt ne '')}">
								<tr>
									<th>완료일시</th>
									<td>${sqlTuning.tuning_complete_dt}</td>
								</tr>
							</c:if>
							<tr>
								<th>완료사유</th>
								<td>
									<select id="selectComplete" name="selectComplete" data-options="${completeStatusIn}panelHeight:'auto',editable:false"  required="required" class="w150 easyui-combobox"></select>
									<label>사례 게시</label>
									<input type="checkbox" id="chkTuningCase" name="chkTuningCase" data-options="${completeStatus}" class="easyui-switchbutton"/>
									<label>사례 제목</label>
									<input type="text" id="tuning_case_posting_title" name="tuning_case_posting_title" data-options="${completeStatus}" value="${sqlTuning.tuning_case_posting_title}" class="w250 easyui-textbox"/>
									
									<label>튜닝사례유형</label>
									<select id="tuning_case_type_cd" name="tuning_case_type_cd" data-options="${completeStatus}" required class="w100 easyui-combobox"></select>
									
								</td>
							</tr>
							<c:forEach items="${completeReasonList}" var="result" varStatus="status">
								<tr id="${result.ref_vl_1}${result.cd}" class="${result.ref_vl_1}" style="display:none;">
									<th>${result.cd_nm}</th>
									<td>
										<c:forEach items="${completeReasonDetailList}" var="resultDtl" varStatus="statusDtl">
											<c:if test="${result.cd eq resultDtl.ref_vl_1}">
												<c:set var="checkValue" scope="page" value="${result.cd}_${resultDtl.cd}"/>
												<c:set var="checkboxStatus" scope="page" value=""/>
												<!-- 체크 여부 처리 -->
												<c:forEach items="${improvementList}" var="improvement" varStatus="statusC">
													<c:set var="checkCompValue" scope="page" value="${improvement.impr_type_cd}_${improvement.impr_detail_type_cd}"/>
													<c:if test="${checkValue eq checkCompValue}">
														<c:set var="checkboxStatus" scope="page" value="checked"/>
													</c:if>
												</c:forEach>
												<div style="float:left;width:105px;"><input type="checkbox" id="${result.cd}${resultDtl.cd}" name="${result.ref_vl_1}" value="${checkValue}" ${checkboxStatus} ${checkboxDisabled}/> ${resultDtl.cd_nm}</div>
											</c:if>
										</c:forEach>
									</td>
								</tr>
							</c:forEach>
							<c:set var="wrkjobFlag" scope="page" value="N"/>
							<c:set var="wrkjobMgrId" scope="page" value="${sqlTuning.wrkjob_mgr_id}"/>
							<c:set var="wrkjobMgrWrkjobCd" scope="page" value="${sqlTuning.wrkjob_mgr_wrkjob_cd}"/>
							<c:set var="wrkjobMgrNm" scope="page" value="${sqlTuning.wrkjob_mgr_nm}"/>
							<c:set var="wrkjobMgrWrkjobNm" scope="page" value="${sqlTuning.wrkjob_mgr_wrkjob_nm}"/>
							<c:set var="wrkjobMgrTelNum" scope="page" value="${sqlTuning.wrkjob_mgr_tel_num}"/>

							<!-- 요청이면서 업무담당자 정보가 없는 경우.. 요청자로 셋팅 -->
							<c:if test="${choickDivCd eq '3' and (sqlTuning.wrkjob_mgr_id eq '' or sqlTuning.wrkjob_mgr_id eq null)}">
								<c:set var="wrkjobFlag" scope="page" value="Y"/>
								<c:set var="wrkjobMgrId" scope="page" value="${request.tuning_requester_id}"/>
								<c:set var="wrkjobMgrWrkjobCd" scope="page" value="${request.tuning_requester_wrkjob_cd}"/>
								<c:set var="wrkjobMgrNm" scope="page" value="${request.tuning_requester_nm}"/>
								<c:set var="wrkjobMgrWrkjobNm" scope="page" value="${request.tuning_requester_wrkjob_nm}"/>
								<c:set var="wrkjobMgrTelNum" scope="page" value="${request.tuning_requester_tel_num}"/>
							</c:if>
							<tr>
								<th>업무담당자</th>
								<td>
									<input type="hidden" id="wrkjob_flag" name="wrkjob_flag" value="${wrkjobFlag}"/>
									<input type="hidden" id="wrkjob_mgr_id" name="wrkjob_mgr_id" value="${wrkjobMgrId}"/>
									<input type="hidden" id="wrkjob_mgr_wrkjob_cd" name="wrkjob_mgr_wrkjob_cd" value="${wrkjobMgrWrkjobCd}"/>
									<input type="text" id="wrkjob_mgr_nm" name="wrkjob_mgr_nm" value="${wrkjobMgrNm}" data-options="readonly:true" class="w120 easyui-textbox"/>
									<input type="text" id="wrkjob_mgr_wrkjob_nm" name="wrkjob_mgr_wrkjob_nm" value="${wrkjobMgrWrkjobNm}" data-options="readonly:true" class="w120 easyui-textbox"/>
									<input type="text" id="wrkjob_mgr_tel_num" name="wrkjob_mgr_tel_num" value="${wrkjobMgrTelNum}" data-options="readonly:true" class="w120 easyui-textbox"/>
									<label>튜닝요청자와 동일</label>
									<input type="checkbox" id="chkTuner" name="chkTuner" data-options="${completeStatus}" class="easyui-switchbutton"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:;" id="chrTunerSearch" class="w120 easyui-linkbutton" data-options="${completeStatus}" onClick="showWorkTunerPopup();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 업무담당자 조회</a>
								</td>
							</tr>
							<tr>
								<td colspan="2"><textarea name="tuning_complete_why" id="tuning_complete_why" rows="15" style="margin-top:5px;width:98%;height:100px">${sqlTuning.tuning_complete_why}</textarea></td>
							</tr>
						</table>
						<div class="dtlBtn inlineBtn">
							<c:if test="${sqlTuning.tuning_status_cd ne 'A'}">
								<a href="javascript:;" class="w80 easyui-linkbutton" data-options="${completeStatus}" onClick="Btn_TempSave();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 임시 저장</a>
								<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${completeStatus}" onClick="Btn_SaveComplete();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝 완료</a>
							</c:if>
						</div>
						<c:set var="tunerCancelDt" scope="page" value="${sqlDetail.tuning_rcess_dt}"/>
						<c:set var="tunerCancelWhy" scope="page" value="${sqlDetail.tuning_rcess_why}"/>
						<c:set var="cancelDt" scope="page" value="${sqlTuning.tuning_apply_rcess_dt}"/>
						<c:set var="cancelWhy" scope="page" value="${sqlTuning.tuning_apply_rcess_why}"/>
						<c:set var="cancelTitle" scope="page" value="튜닝 반려"/>
						<c:set var="cancelTitle2" scope="page" value="튜닝 반려 사유"/>
						<!-- 반려버튼 버튼 활성/비활성 -->
						<c:set var="cancelStatus" scope="page" value=""/>
						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER')">
							<c:if test="${sqlTuning.tuning_status_cd ne '2'}">
								<c:set var="cancelStatus" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_TUNER')">
							<c:if test="${sqlTuning.tuning_status_cd ne '5'}">
								<c:set var="cancelStatus" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>

						<c:set var="endBtnView" scope="page" value="Y"/>
						<sec:authorize access="hasAnyRole('ROLE_DEV')">
							<c:set var="cancelTitle" scope="page" value="적용 반려"/>
							<c:set var="cancelTitle2" scope="page" value="적용 반려 사유"/>
							<c:if test="${sqlTuning.tuning_status_cd ne '6'}">
								<c:set var="cancelStatus" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_TUNER')">
						 	<!-- SQL 상세(요청)  -->
							<c:if test="${choickDivCd eq '3'}">
								<c:set var="endBtnView" scope="page" value="N"/>
							</c:if>
						</sec:authorize>

						<!-- 반려 정보 출력 -->
						<c:if test="${sqlTuning.tuning_status_cd eq '4' || (sqlTuning.tuning_status_cd eq '8' && sqlDetail.tuning_rcess_dt ne null)}">
							<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 튜닝반려</span></div>
							<table class="detailT">
								<colgroup>
									<col style="width:15%;">
									<col style="width:85%;">
								</colgroup>
									<tr>
										<th>반려일시</th>
										<td>${tunerCancelDt}</td>
									</tr>
									<tr>
										<th>반려사유</th>
										<td colspan="3"><textarea name="tunerCancelWhy" id="tunerCancelWhy" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${tunerCancelWhy}</textarea></td>
									</tr>
							</table>
						</c:if>
						<!-- 적용반려 정보 출력 -->
						<c:if test="${sqlTuning.tuning_status_cd eq '7' || (sqlTuning.tuning_status_cd eq '8' && sqlTuning.tuning_apply_rcess_dt ne null)}">
							<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> 적용반려</span></div>
							<table class="detailT">
								<colgroup>
									<col style="width:15%;">
									<col style="width:85%;">
								</colgroup>
									<tr>
										<th>적용반려일시</th>
										<td>${cancelDt}</td>
									</tr>
									<tr>
										<th>적용반려사유</th>
										<td colspan="3"><textarea name="tunerCancelWhy" id="tunerCancelWhy" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${cancelWhy}</textarea></td>
									</tr>
							</table>
						</c:if>
						<c:if test="${sqlTuning.tuning_status_cd eq '2' || sqlTuning.tuning_status_cd eq '5' || sqlTuning.tuning_status_cd eq '6'}">
							<div class="tbl_title">
								<span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> ${cancelTitle2}</span>
								<!-- 개발자일경우 적용반려,적용반려사유,재요청 -->
								<sec:authorize access="hasAnyRole('ROLE_DEV')">
									<span style="vertical-align:middle;padding-left:10px;"><input type="checkbox" id="rerequest" name="rerequest"/></span>
									<span>재요청</span>
								</sec:authorize>
							</div>
							<table class="detailT">
									<tr>
										<td><textarea name="tuning_rcess_why" id="tuning_rcess_why" rows="5" style="margin-top:5px;width:98%;height:50px"></textarea></td>
									</tr>
							</table>
						</c:if>
						
						<c:if test="${cancelStatus ne 'disabled:true' && sqlTuning.tuning_status_cd ne '4' && sqlTuning.tuning_status_cd ne 'A'}">
							<div class="dtlBtn inlineBtn">
								<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${cancelStatus}" onClick="Btn_SaveCancel('${cancelTitle}');"><i class="btnIcon fas fa-undo-alt fa-lg fa-fw"></i> ${cancelTitle}</a>
							</div>
						</c:if>
						
						<!-- 튜닝종료 버튼 활성/비활성 -->
						<c:set var="endStatus" scope="page" value=""/>
						<c:set var="endCheck" scope="page" value="N"/>
						<sec:authorize access="hasAnyRole('ROLE_DBMANAGER')">
							<c:if test="${sqlTuning.tuning_status_cd ne '4' && (loginUser ne sqlTuning.tuning_requester_id)}">
								<c:set var="endStatus" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_TUNER')">
							<c:if test="${sqlTuning.tuning_status_cd ne '5'}">
								<c:set var="endStatus" scope="page" value="disabled:true"/>
							</c:if>
							<c:set var="endCheck" scope="page" value="Y"/>
						</sec:authorize>
						<sec:authorize access="hasAnyRole('ROLE_DEV')">
							<c:if test="${!((loginUser eq sqlTuning.tuning_requester_id)||(loginUser eq sqlTuning.wrkjob_mgr_id)) or (sqlTuning.tuning_status_cd eq '8') or (sqlTuning.tuning_status_cd eq '2')}">
								<c:set var="endStatus" scope="page" value="disabled:true"/>
							</c:if>
						</sec:authorize>
						<br/>
						<c:if test="${endBtnView eq 'Y'}">
							<div class="tbl_title"><span class="h3"><i class="btnIcon fab fa-wpforms fa-lg fa-fw"></i> SQL 튜닝 종료</span></div>
							<table class="detailT">
								<colgroup>
									<col style="width:15%;">
									<col style="width:85%;">
								</colgroup>
								<c:if test="${sqlTuning.tuning_status_cd eq '8'}">
									<tr>
										<th>종료일시</th>
										<td>${sqlTuning.tuning_end_dt}</td>
									</tr>
								</c:if>
								<tr>
									<th>종료사유</th>
									<td>
										<c:if test="${endStatus ne ''}">
											<select id="selectEnd" name="selectEnd" data-options="panelHeight:'auto',editable:false, ${endStatus}" required class="w150 easyui-combobox"></select>
										</c:if>
										<c:if test="${endStatus eq ''}">
											<select id="selectEnd" name="selectEnd" data-options="panelHeight:'auto',editable:false ${endStatus}" required class="w150 easyui-combobox"></select>
										</c:if>
										<sec:authorize access="hasAnyRole('ROLE_ITMANAGER','ROLE_DBMANAGER','ROLE_TUNER')">
											<label>제외 대상 등록</label>
											<input type="checkbox" id="chkExceptTarget" name="chkExceptTarget" data-options="${endStatus}" class="easyui-switchbutton"/>
										</sec:authorize>
									</td>
								</tr>
								<tr>
									<td colspan="2"><textarea name="tuning_end_why" id="tuning_end_why" rows="5" style="margin-top:5px;width:98%;height:50px">${sqlTuning.tuning_end_why}</textarea></td>
								</tr>
							</table>
						</c:if>
					</div>
					<div class="dtlBtn inlineBtn">
						<c:choose>
							<c:when test="${endBtnView eq 'Y'}">
									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(1);"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 이전</a>
									<sec:authorize access="hasAnyRole('ROLE_DEV')">
										<!-- 요청변경 활성 / 비활성 -->
										<c:set var="requestStatus" scope="page" value=""/>
										<c:if test="${sqlTuning.tuning_status_cd ne '2' && sqlTuning.tuning_status_cd ne '4'}">
											<c:set var="requestStatus" scope="page" value="disabled:true"/>
										</c:if>
										<!-- 사전점검 버튼 활성/비활성 -->
										<c:set var="preCheckStatus" scope="page" value=""/>
										<c:if test="${sqlTuning.tuning_status_cd ne '6'}">
											<c:set var="preCheckStatus" scope="page" value=",disabled:true"/>
										</c:if>
										<c:choose>
											<c:when test="${sqlTuning.after_tuning_no == null or sqlTuning.after_tuning_no == ''}">
	<%-- 										<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${requestStatus}" onClick="Btn_ChangeRequest();"><i class="btnIcon fas fa-redo-alt fa-lg fa-fw"></i> 요청변경</a> --%>
												<!--  [튜닝재요청] 버튼은 튜닝반려된 건에서만 활성화 되어야함 -->
												<c:if test="${sqlTuning.tuning_status_cd eq '4' and sqlTuning.before_tuning_no == null}">
													<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${requestStatus}" onClick="Btn_ChangeRequest();"><i class="btnIcon fas fa-redo-alt fa-lg fa-fw"></i> 튜닝재요청</a>
												</c:if>
											</c:when>
										</c:choose>
										<c:if test="${operationYn eq 'Y'}">  <!-- 운영(operationYn = 'Y')일 경우 소스점검 사용 -->
											<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-end'${preCheckStatus}" onClick="showPreCheckPopup();">소스점검&튜닝종료</a>
										</c:if>
									</sec:authorize>
									
									<c:choose>
										<c:when test="${(sqlTuning.after_tuning_no == null or sqlTuning.after_tuning_no == '') and sqlTuning.tuning_status_cd ne '4' and sqlTuning.tuning_status_cd ne '7' and sqlTuning.tuning_status_cd ne 'A'}">
											<a href="javascript:;" class="w90 easyui-linkbutton" data-options="${endStatus}" onClick="Btn_SaveEnd('${endCheck}');"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 튜닝 종료</a>
										</c:when>
									</c:choose>

							</c:when>
							<c:when test="${endBtnView eq 'N'}">
									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(1);"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 이전</a>
							</c:when>
						</c:choose>
					</div>
					<!-- 개선 프로세스  END -->
				</div>
				<div title="SQL 개선 이력">
					<iframe id="sqlHistoryIF" name="sqlHistoryIF" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" 
					src="/ImprovementManagement/SQLImproveHistory?tuning_no=${sqlTuning.tuning_no}&menu_id=${menu_id}" style="width:100%;min-height:700px;"></iframe>
				</div>
			</div>
		</form:form>
		<form:form method="post" id="modify_form" name="modify_form" class="form-inline">
			<input type="hidden" id="tuning_no" name="tuning_no" />
			<input type="hidden" id="tuning_status_cd" name="tuning_status_cd" />
			<input type="hidden" id="before_wrkjob_mgr_id" name="before_wrkjob_mgr_id" />
			<input type="hidden" id="before_wrkjob_mgr_nm" name="before_wrkjob_mgr_nm" />
			<input type="hidden" id="wrkjob_mgr_id" name="wrkjob_mgr_id" />
			<input type="hidden" id="wrkjob_mgr_wrkjob_cd" name="wrkjob_mgr_wrkjob_cd" />
			<input type="hidden" id="wrkjob_mgr_nm" name="wrkjob_mgr_nm" />
			<input type="hidden" id="wrkjob_mgr_wrkjob_nm" name="wrkjob_mgr_wrkjob_nm" />
			<input type="hidden" id="wrkjob_mgr_tel_num" name="wrkjob_mgr_tel_num" />
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%-- <%@include file="/WEB-INF/jsp/include/popup/improvementManagementView_popup.jsp" %> <!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 --> --%>
<%@include file="/WEB-INF/jsp/include/popup/imprMgmtView_popup.jsp" %> <!-- 성능개선 - 성능 개선 관리 상세 관련 팝업 -->
<c:choose>
	<c:when test="operation_yn eq 'Y'">
		<%@include file="/WEB-INF/jsp/include/popup/indexRequest_popup.jsp" %> <!-- 성능개선 - 인덱스 요청 팝업 -->
	</c:when>
	<c:otherwise>
		<%@include file="/WEB-INF/jsp/include/popup/indexRequest_popup_dev.jsp" %> <!-- 성능개선 - 인덱스 요청 팝업 -->
	</c:otherwise>
</c:choose>
<div id="userInfoBox" class="easyui-window popWin" style="background-color:#ffffff;width:400px;height:250px;">
	<div class="easyui-layout" data-options="fit:true">
		<table class="detailT4">
			<colgroup>
				<col style="width:30%;">
				<col style="width:70%;">
			</colgroup>
			<tr height="30px;">
				<th style="font-size:small;" align="left">사용자명</th>
				<td style="font-size:small;">&nbsp;&nbsp;${userInfo.user_nm}</td>
			</tr>
			<tr height="30px;">
				<th style="font-size:small;" align="left">사용자ID</th>
				<td style="font-size:small;">&nbsp;&nbsp;${userInfo.user_id}</td>
			</tr>
			<tr height="30px;">
				<th style="font-size:small;" align="left">휴대폰번호</th>
				<td style="font-size:small;">&nbsp;&nbsp;${userInfo.hp_no}</td>
			</tr>
			<tr height="30px;">
				<th style="font-size:small;" align="left">업무</th>
				<td style="font-size:small;">&nbsp;&nbsp;${userInfo.wrkjob_cd_nm}</td>
			</tr>
			<tr height="30px;">
				<th style="font-size:small;" align="left">이메일주소</th>
				<td style="font-size:small;">&nbsp;&nbsp;${userInfo.email}</td>
			</tr>
		</table>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:30px;">
			<div class="searchBtn" style="padding-right:16px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('userInfoBox');"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
</body>
</html>