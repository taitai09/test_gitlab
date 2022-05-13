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
 * 2018.06.01	이원식	V2 최초작업
 * 2021.07.16	이재우	성능개선
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 리포트 :: 성능개선결과 산출물 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/performanceImprovementDesign/situation/performanceImprovementOutputsView.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents" >
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<div id="statusViewTab" class="easyui-tabs" data-options="border:false" style="width:100%;padding-bottom:10px;height:auto;">
				<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${users.wrkjob_cd}"/>
				<input type="hidden" id="project_id" name="project_id" value="${tuningTargetSql.project_id}"/>
				<input type="hidden" id="tuning_prgrs_step_seq" name="tuning_prgrs_step_seq" value="${tuningTargetSql.tuning_prgrs_step_seq}"/>
				<input type="hidden" id="selectValue" name="selectValue" value="${sqlTuning.dbid}"/>
				<input type="hidden" id="dbid" name="dbid" value="${sqlTuning.dbid}"/>
				<input type="hidden" id="operation_dbid" name="operation_dbid" value="${sqlDetail.operation_dbid}"/>
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
				<input type="hidden" id="tuning_complete_why_origin_cd" name="tuning_complete_why_origin_cd" value="${tuningTargetSql.tuning_complete_why_cd}"/>
				<input type="hidden" id="tuning_end_why_cd" name="tuning_end_why_cd" value="${sqlTuning.tuning_end_why_cd}"/>
				<input type="hidden" id="tuning_case_posting_yn" name="tuning_case_posting_yn" value="${sqlTuning.tuning_case_posting_yn}"/>
				<input type="hidden" id="except_target_yn" name="except_target_yn" value="${sqlTuning.except_target_yn}"/>
				<input type="hidden" id="list_sql_id" name="list_sql_id" value="${tuningTargetSql.sql_id}"/>
				<input type="hidden" id="list_selectValue" name="list_selectValue" value="${tuningTargetSql.selectValue}"/>
				<input type="hidden" id="list_tuning_status_cd" name="list_tuning_status_cd" value="${tuningTargetSql.tuning_status_cd}"/>
				<input type="hidden" id="list_tr_cd" name="list_tr_cd" value="${tuningTargetSql.tr_cd}"/>
				<input type="hidden" id="list_dbio" name="list_dbio" value="${tuningTargetSql.dbio}"/>
				<input type="hidden" id="tr_cd" name="tr_cd" value="${tuningTargetSql.tr_cd}"/>
				<input type="hidden" id="dbio" name="dbio" value="${tuningTargetSql.dbio}"/>
				<input type="hidden" id="bfac_chk_source" name="bfac_chk_source"/>
				<input type="hidden" id="tuningNoArry" name="tuningNoArry"/>
				<input type="hidden" id="tuningStatusArry" name="tuningStatusArry"/>
				<input type="hidden" id="completeArry" name="completeArry"/> <!-- 완료 사유 관련 파라미터 -->
				<input type="hidden" id="push_yn" name="push_yn" value="N"/>
				<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${tuningTargetSql.tuning_complete_dt}"/>
				<input type="hidden" id="currentPage" name="currentPage" value="${tuningTargetSql.currentPage}"/>
				<input type="hidden" id="menu_nm" name="menu_nm" value="${menu_nm}"/>
				<div title="SQL 요청 상세">
					<!-- SQL 요청 상세  START -->
<!-- 					<div style="height:655px;overflow-y:auto"> -->
					<div style="height:599px;border-bottom:1px solid lightgray;overflow-y:auto">
						<c:choose>
							<c:when test="${choickDivCd eq '1' || choickDivCd eq '2'}"> <!-- SQL 상세(선정)  -->
								<input type="hidden" id="sql_id" name="sql_id" value="${selection.sql_id}"/>
								<input type="hidden" id="plan_hash_value" name="plan_hash_value" value="${selection.plan_hash_value}"/>
								<input type="hidden" id="pop_tuning_no" name="pop_tuning_no" value="${selection.tuning_no}"/>
								<input type="hidden" id="pop_tuning_request_dt" name="pop_tuning_request_dt" value="${selection.tuning_request_dt}"/>
								<input type="hidden" id="pop_choice_div_cd_nm" name="pop_choice_div_cd_nm" value="${selection.choice_div_cd_nm}"/>

								<input type="hidden" id="tuning_requester_id" name="tuning_requester_id" value="${selection.tuning_requester_id}"/>
								<input type="hidden" id="tuning_requester_nm" name="tuning_requester_nm" value="${selection.tuning_requester_nm}"/>
								<input type="hidden" id="tuning_requester_wrkjob_cd" name="tuning_requester_wrkjob_cd" value="${selection.tuning_requester_wrkjob_cd}"/>
								<input type="hidden" id="tuning_requester_wrkjob_nm" name="tuning_requester_wrkjob_nm" value="${selection.tuning_requester_wrkjob_nm}"/>
								<input type="hidden" id="tuning_requester_tel_num" name="tuning_requester_tel_num" value="${selection.tuning_requester_tel_num}"/>
								<table class="detailT" style="width:99%">
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
											${selection.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
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
											${selection.sql_id}
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${selection.plan_hash_value}</td>
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
										<td>${selection.tr_cd}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${selection.dbio}</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${selection.sql_text}</textarea>
										</td>
									</tr>
								</table>
							</c:when>
							<c:when test="${choickDivCd eq '3'||choickDivCd eq 'B'}"> <!-- SQL 상세(요청)  -->
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
								<table class="detailT" style="width:99%">
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
											${request.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
										</td>
										<th>요청 일자</th>
										<td>${request.tuning_request_dt}</td>
										<th>튜닝대상선정구분</th>
										<td>${request.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>요청자</th>
										<td>${request.tuning_requester_nm}</td>
										<th>요청업무</th>
										<td>${request.tuning_requester_wrkjob_nm}</td>
										<th>요청자연락처</th>
										<td>${request.tuning_requester_tel_num}</td>
									</tr>
									<tr>
										<th>프로그램 유형</th>
										<td>${request.program_type_cd_nm}</td>
										<th>배치작업주기</th>
										<td>${request.batch_work_div_cd_nm}</td>
										<th>튜닝완료요청일자</th>
										<td>${request.tuning_complete_due_dt}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${request.db_name}</td>
										<th>소스파일명(Full Path)</th>
										<td colspan="3">${request.tr_cd}</td>
									</tr>
									<tr>
										<th>DB접속정보</th>
										<td>${request.parsing_schema_name}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${request.dbio}</td>
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
								<table class="detailT" style="width:99%">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tr>
										<th>업무특이사항</th>
										<td><textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${request.wrkjob_peculiar_point}</textarea></td>
									</tr>
									<tr>
										<th>요청사유</th>
										<td><textarea name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${request.request_why}</textarea></td>
									</tr>
									<tr>
										<th>SQL BIND</th>
										<td><textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:5px;width:98%;height:300px" readonly>${request.sql_desc}</textarea></td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td style="padding:5px;height:350px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${request.sql_text}</textarea>
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
														<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:98%">
															<colgroup>
																<col style="width:10%;">
																<col style="width:90%;">
															</colgroup>
															<c:forEach items="${bindSetList}" var="bindSet" varStatus="status">
																<tr>
																	<th>바인드<br/>${status.count}</th>
																	<td>
																		<table class="detailT" style="margin-left:3px;margin-top:3px;margin-bottom:3px;width:98%">
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
								<table class="detailT" style="width:99%">
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
											${fullScan.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
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
											${fullScan.sql_id}
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
										<td colspan="3">${fullScan.tr_cd}</td>
									</tr>
									<tr>
										<th>SQL식별자(DBIO)</th>
										<td colspan="5">${fullScan.dbio}</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${fullScan.sql_text}</textarea>
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
								<table class="detailT" style="width:99%">
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
											${planChange.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
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
											${planChange.sql_id}
										</td>
										<th>PLAN_HASH_VALUE</th>
										<td>${planChange.after_plan_hash_value}</td>
									</tr>
									<tr>
										<th>소스파일명(Full Path)</th>
										<td>${planChange.tr_cd}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${planChange.dbio}</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${planChange.sql_text}</textarea>
										</td>
									</tr>
								</table>
								<table class="detailT" style="width:99%">
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
										<th>PLAN 변경전</th>
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
								<table class="detailT" style="width:99%">
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
											${newSql.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
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
											${newSql.sql_id}
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
										<td>${newSql.tr_cd}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${newSql.dbio}</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${newSql.sql_text}</textarea>
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
								<table class="detailT" style="width:99%">
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
											${tempOver.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
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
											${tempOver.sql_id}
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
										<td>${tempOver.tr_cd}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${tempOver.dbio}</td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td colspan="5" style="padding:10px;height:300px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${tempOver.sql_text}</textarea>
										</td>
									</tr>
								</table>
							</c:when>
							<c:otherwise>
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
								<table class="detailT" style="width:99%">
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
											${request.tuning_no}&nbsp;&nbsp;(${sqlTuning.tuning_status_nm})
										</td>
										<th>요청 일자</th>
										<td>${request.tuning_request_dt}</td>
										<th>튜닝대상선정구분</th>
										<td>${request.choice_div_cd_nm}</td>
									</tr>
									<tr>
										<th>요청자</th>
										<td>${request.tuning_requester_nm}</td>
										<th>요청업무</th>
										<td>${request.tuning_requester_wrkjob_nm}</td>
										<th>요청자연락처</th>
										<td>${request.tuning_requester_tel_num}</td>
									</tr>
									<tr>
										<th>프로그램 유형</th>
										<td>${request.program_type_cd_nm}</td>
										<th>배치작업주기</th>
										<td>${request.batch_work_div_cd_nm}</td>
										<th>튜닝완료요청일자</th>
										<td>${request.tuning_complete_due_dt}</td>
									</tr>
									<tr>
										<th>DB</th>
										<td>${request.db_name}</td>
										<th>소스파일명(Full Path)</th>
										<td colspan="3">${request.tr_cd}</td>
									</tr>
									<tr>
										<th>DB접속정보</th>
										<td>${request.parsing_schema_name}</td>
										<th>SQL식별자(DBIO)</th>
										<td colspan="3">${request.dbio}</td>
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
								<table class="detailT" style="width:99%">
									<colgroup>
										<col style="width:15%;">
										<col style="width:85%;">
									</colgroup>
									<tr>
										<th>업무특이사항</th>
										<td><textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${request.wrkjob_peculiar_point}</textarea></td>
									</tr>
									<tr>
										<th>요청사유</th>
										<td><textarea name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${request.request_why}</textarea></td>
									</tr>
									<tr>
										<th>SQL BIND</th>
										<td><textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:5px;width:98%;height:300px" readonly>${request.sql_desc}</textarea></td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br/><br/>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
											<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatter();"><i class="btnIcon fas fa-tasks fa-lg fa-fw"></i> SQL Format</a>
										</th>
										<td style="padding:5px;height:350px;">
											<textarea name="sqlTextArea" id="sqlTextArea" style="margin-top:5px;padding:5px;width:98%;height:93%" wrap="off" readonly>${request.sql_text}</textarea>
										</td>
									</tr>
								</table>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="dtlBtn inlineBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
						<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_DownloadOutputs();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 산출물 다운로드</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(1);"><i class="btnIcon fas fa-chevron-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
					<!-- SQL 요청 상세  END -->
				</div>
				<div title="SQL 개선 상세">
					<!-- SQL 개선 상세  START -->
<!-- 					<div style="height:655px;overflow-y:auto"> -->
					<div style="height:599px;border-bottom:1px solid lightgray;overflow-y:auto">
						<table class="detailT" style="width:99.5%">
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
								<th>응답시간 (Sec)</th>
								<th>블럭수</th>
								<th>PGA 사용량(MB)</th>
							</tr>
							<tr>
								<td class="rtext">${sqlDetail.imprb_elap_time}</td>
								<td class="rtext">${sqlDetail.imprb_buffer_cnt}</td>
								<td class="rtext">${sqlDetail.imprb_pga_usage}</td>
								<td class="rtext">${sqlDetail.impra_elap_time}</td>
								<td class="rtext">${sqlDetail.impra_buffer_cnt}</td>
								<td class="rtext">${sqlDetail.impra_pga_usage}</td>
								<td class="rtext">${sqlDetail.elap_time_impr_ratio}</td>
								<td class="rtext">${sqlDetail.buffer_impr_ratio}</td>
								<td class="rtext">${sqlDetail.pga_impr_ratio}</td>
							</tr>
						</table>
						<table class="detailT2" style="width:99.5%">
							<colgroup>
								<col style="width:10%;"/>
								<col style="width:40%;"/>
								<col style="width:10%;"/>
								<col style="width:40%;"/>
							</colgroup>
							<tr>
								<th>문제점</th>
								<td><textarea name="controversialist" id="controversialist" rows="10" style="margin-top:5px;width:100%;height:100px" readonly>${sqlDetail.controversialist}</textarea></td>
								<th>개선내역</th>
								<td><textarea name="impr_sbst" id="impr_sbst" rows="10" style="margin-top:5px;width:100%;height:100px" readonly>${sqlDetail.impr_sbst}</textarea></td>
							</tr>
							<tr>
								<th>인덱스내역</th>
								<td colspan="3">
									<div id="indexHist" class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;padding-top:10px;">
										<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
										</table>
									</div>
									<!-- 튜닝상태가 6:튜닝완료(적용대기)이거나 8:튜닝종료일 경우에는 수정불가 -->
									<c:choose>
										<c:when test="${sqlTuning.tuning_status_cd eq '6'}">
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<th>
									개선SQL<br/><br/>
									<a href="javascript:;" id="imprSqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a>
								</th>
								<td colspan="3">
									<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:100%;height:300px" wrap="off" readonly>
									${sqlDetail.impr_sql_text}
									</textarea>
									<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="display:none;">
									${sqlDetail.impr_sql_text}
									</textarea>
								</td>
							</tr>
							<tr>
								<th colspan="2">
									개선전 실행계획<br/><br/>
									<a href="javascript:;" id="imprbExecCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
								</th>
								<th colspan="2">
									개선후 실행계획<br/><br/>
									<a href="javascript:;" id="impraExecCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
								</th>
							</tr>
							<tr>
								<td colspan="2">
									<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:100%;height:300px" wrap="off" readonly>
									${sqlDetail.imprb_exec_plan}
									</textarea>
									<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="display:none;">
									${sqlDetail.imprb_exec_plan}
									</textarea>
								</td>
								<td colspan="2">
									<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:100%;height:300px" wrap="off" readonly>
									${sqlDetail.impra_exec_plan}
									</textarea>
									<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="display:none;">
									${sqlDetail.impra_exec_plan}
									</textarea>
								</td>
							</tr>
						</table>
					</div>
					<div class="dtlBtn inlineBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoList();"><i class="btnIcon fas fa-list fa-lg fa-fw"></i> 목록</a>
						<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_DownloadOutputs();"><i class="btnIcon fas fa-download fa-lg fa-fw"></i> 산출물 다운로드</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_GoNext(0);"><i class="btnIcon fas fa-chevron-circle-left fa-lg fa-fw"></i> 이전</a>
					</div>
					<!-- SQL 개선 상세  END -->
				</div>
			</div>
		</form:form>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>