<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>튜닝이력조회 :: 상세</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
	<script type="text/javascript" src="/resources/js/ui/tuningHistoryView.js?ver=<%=today%>"></script>
</head>
<body>
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel detailArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-bottom:20px;">
			<form:form method="post" id="search_form" name="submit_form" class="form-inline">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<input type="hidden" id="dbid" name="dbid" value="${tuningTargetSql.dbid}">
				<input type="hidden" id="dbio" name="dbio" value="${tuningTargetSql.dbio}">
				<input type="hidden" id="bfac_chk_no" name="bfac_chk_no" value="${tuningTargetSql.bfac_chk_no}">
				<input type="hidden" id="start_tuning_complete_dt" name="start_tuning_complete_dt" value="${tuningTargetSql.start_tuning_complete_dt}">
				<input type="hidden" id="end_tuning_complete_dt" name="end_tuning_complete_dt" value="${tuningTargetSql.end_tuning_complete_dt}">
				<input type="hidden" id="searchKey" name="searchKey" value="${tuningTargetSql.searchKey}">
				<input type="hidden" id="searchValue" name="searchValue" value="${tuningTargetSql.searchValue}">
				<input type="hidden" id="program_type_cd" name="program_type_cd" value="${tuningTargetSql.program_type_cd}">
				<input type="hidden" id="tuning_status_cd" name="tuning_status_cd" value="${tuningTargetSql.tuning_status_cd}">
			</form:form>
			
			<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
				<input type="hidden" id="tuning_no" name="tuning_no" value="${tuningTargetSql.tuning_no}">
				<input type="hidden" id="tuning_complete_dt" name="tuning_complete_dt" value="${tuningTargetSql.tuning_complete_dt}">
				<div id="historyViewTab" class="easyui-tabs" data-options="border:false" style="width:100%;padding-bottom:10px;height:auto;">
					<div title="SQL 요청 상세">
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
								<td>${sqlDetail.tuning_no}</td>
								<th>요청(선정)일자</th>
								<td>${sqlDetail.tuning_request_dt}</td>
								<th>튜닝요청구분</th>
								<td>${sqlDetail.choice_div_cd_nm}</td>
							</tr>
							<tr>
								<th>업무담당자</th>
								<td>${sqlDetail.wrkjob_mgr_nm}</td>
								<th>담당업무</th>
								<td>${sqlDetail.wrkjob_mgr_wrkjob_nm}</td>
								<th>업무담당자 연락처</th>
								<td>${sqlDetail.wrkjob_mgr_tel_num}</td>
							</tr>
							<tr>
								<th>프로그램 유형</th>
								<td>${sqlDetail.program_type_cd_nm}</td>
								<th>배치작업주기</th>
								<td >${sqlDetail.batch_work_div_cd_nm}</td>
								<th>수행횟수</th>
								<td >${sqlDetail.exec_cnt}</td>
							</tr>
							<tr>
								<th>DB</th>
								<td>${sqlDetail.db_name}</td>
								<th>DB접속정보</th>
								<td>${sqlDetail.parsing_schema_name}</td>
								<th>튜닝담당자</th>
								<td>${sqlDetail.perfr_nm}</td>
							</tr>
							<tr>
								<th>소스파일명(Full Path)</th>
								<td>${sqlDetail.tr_cd}</td>
								<th>SQL식별자(DBIO)</th>
								<td>${sqlDetail.dbio}</td>
								<th>튜닝일자</th>
								<td>${sqlDetail.tuning_complete_dt}</td>
							</tr>
						</table>
						<div style="height:500px;overflow-y:auto;overflow-x:hidden;">
							<table class="detailT">
								<colgroup>
									<col style="width:10%;">
									<col style="width:40%;">
									<col style="width:10%;">
									<col style="width:40%;">
								</colgroup>
									<tr>
										<th>업무특이사항</th>
										<td><textarea name="wrkjob_peculiar_point" id="wrkjob_peculiar_point" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.wrkjob_peculiar_point}</textarea></td>
										<th>요청사유</th>
										<td><textarea name="request_why" id="request_why" rows="5" style="margin-top:5px;width:98%;height:50px" readonly>${sqlDetail.request_why}</textarea></td>
									</tr>
									<tr>
										<th>
											SQL TEXT<br><br>
											<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea"><i class="btnIcon fas fa-save fa-lg fa-fw"></i>SQL 복사</a>
										</th>
										<td style="padding:10px;height:370px;">
											<div id="sqlTextArea" style="width:100%;height:100%;overflow-y:auto;">
												${fn:replace(fn:replace(sqlDetail.sql_text,newLineChar,'<br/>'),' ','&nbsp;')}
											</div>
										</td>
										<th>SQL BIND</th>
										<td><textarea name="sql_desc" id="sql_desc" rows="30" style="margin-top:5px;width:98%;height:100%;" readonly>${sqlDetail.sql_desc}</textarea></td>
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
											<input type="hidden" id="file_nm" name="file_nm" value="">
											<input type="hidden" id="org_file_nm" name="org_file_nm" value="">
										</td>
									</tr>
							</table>
						</div>
						<div class="dtlBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" data-options="disabled:false" onClick="Btn_GoNext(1);"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						</div>
					</div>
					<div title="SQL 개선 상세">
						<table class="detailT">
							<colgroup>
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:11%;">
								<col style="width:12%;">
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
								<td class="ctext">${sqlDetail.imprb_elap_time}</td>
								<td class="ctext">${sqlDetail.imprb_buffer_cnt}</td>
								<td class="ctext">${sqlDetail.imprb_pga_usage}</td>
								<td class="ctext">${sqlDetail.impra_elap_time}</td>
								<td class="ctext">${sqlDetail.impra_buffer_cnt}</td>
								<td class="ctext">${sqlDetail.impra_pga_usage}</td>
								<td class="ctext">${sqlDetail.elap_time_impr_ratio} %</td>
								<td class="ctext">${sqlDetail.buffer_impr_ratio} %</td>
								<td class="ctext">${sqlDetail.pga_impr_ratio} %</td>
							</tr>
						</table>
						<div style="height:560px;border-bottom:1px solid lightgray;overflow-y:auto;overflow-x:hidden;">
							<table class="detailT2">
								<colgroup>
									<col style="width:10%;">
									<col style="width:40%;">
									<col style="width:10%;">
									<col style="width:40%;">
								</colgroup>
								<tr>
									<th>문제점</th>
									<td><textarea name="controversialist" id="controversialist" rows="10" style="margin-top:5px;width:99%;height:100px" readonly>${sqlDetail.controversialist}</textarea></td>
									<th>개선내역</th>
									<td><textarea name="impr_sbst" id="impr_sbst" rows="10" style="margin-top:5px;width:99%;height:100px" readonly>${sqlDetail.impr_sbst}</textarea></td>
								</tr>
								<tr>
									<th>인덱스내역</th>
									<td colspan="3">
										<div id="indexHist" class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;padding-top:10px;">
											<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</td>
								</tr>
								<tr>
									<th>
										개선SQL<br><br>
										<a href="javascript:;" id="imprSqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impr_sql_text_h" ><i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a>
									</th>
									<td colspan="3" style="width:100%;">
										<textarea name="impr_sql_text" id="impr_sql_text" rows="30" style="margin-top:5px;width:100%;height:300px;" readonly wrap="off">
										${sqlDetail.impr_sql_text}
										</textarea>
										<textarea name="impr_sql_text_h" id="impr_sql_text_h" style="display:none;">
										${sqlDetail.impr_sql_text}
										</textarea>
									</td>
								</tr>
								
								<tr>
									<th colspan="2">
										개선전 실행계획<br><br>
										<a href="javascript:;" id="imprbExecCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#imprb_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
									</th>
									<th colspan="2">
										개선후 실행계획<br><br>
										<a href="javascript:;" id="impraExecCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#impra_exec_plan_h"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 실행계획 복사</a>
									</th>
								</tr>
								<tr>
									<td colspan="2">
										<textarea name="imprb_exec_plan" id="imprb_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
										${sqlDetail.imprb_exec_plan}
										</textarea>
										<textarea name="imprb_exec_plan_h" id="imprb_exec_plan_h" style="display:none;">
										${sqlDetail.imprb_exec_plan}
										</textarea>
									</td>
									<td colspan="2">
										<textarea name="impra_exec_plan" id="impra_exec_plan" rows="30" style="margin-top:5px;width:99%;height:300px" readonly wrap="off">
										${sqlDetail.impra_exec_plan}
										</textarea>
										<textarea name="impra_exec_plan_h" id="impra_exec_plan_h" style="display:none;">
										${sqlDetail.impra_exec_plan}
										</textarea>
									</td>
								</tr>
							</table>
						</div>
						<div class="dtlBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" data-options="disabled:false" onClick="Btn_GoNext(0);"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>