<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<%
/***********************************************************
 * 2019.01.07
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 결과-프로그램 정보</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<link rel="stylesheet" href="/resources/css/lib/easyui/default/easyui.css"/>
	<script type="text/javascript" src="/resources/js/lib/jquery.easyui.min.js"></script>
</head>
<body>
	<script type="text/javascript">
		var dateArr= [],
			pagingYnArr = [],
			pagingCntArr = [];
		
		<c:forEach items="${programExecuteTmsList}" var="programExeuteInfo">
			
			dateArr.push("${programExeuteInfo.program_exec_dt}");
			pagingYnArr.push("${programExeuteInfo.pagingYn}");
			pagingCntArr.push("${programExeuteInfo.pagingCnt}");
 		
		</c:forEach>
	
	</script>
	<div style="width:100%;height:380px;overflow-y:auto;">
		<table class="detailT">
			<colgroup>
				<col style="width: 130px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: auto; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 150px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 120px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
				<col style="width: 180px; border-left: 1px solid lightgray; border-right: 1px solid lightgray;">
			</colgroup>
			<tbody>
				<tr>
					<th>프로그램명</th>
					<td>${perfCheckAllPgm.program_nm}</td>
					<th style="font-size:13px;">
						<span style="font-size:13px;">점검제외</span>
						<span id="check_span1" style="position: relative; top: -1px;margin-left: 3px;">
							<input class="easyui-checkbox checkbox-f" id="perf_check_auto_pass_yn_chk" disabled="" style="display: none;">
						</span>
					</th>
					<td>&nbsp;${perfCheckAllPgm.exception_prc_meth_nm}</td>
					<th>프로그램 구분</th>
					<td>${perfCheckAllPgm.program_div_nm}</td>
					<th>프로그램유형</th>
					<td>${perfCheckAllPgm.program_type_nm}</td>
					<th>SQL명령 유형</th>
					<td>${perfCheckAllPgm.sql_command_type_nm}</td>
				</tr>
				<tr>
					<th>프로그램설명</th>
					<td colspan="9">${perfCheckAllPgm.program_desc}</td>
				</tr>
				<tr>
					<th>SQL식별자(DBIO)</th>
					<td colspan="3">${perfCheckAllPgm.dbio}</td>
					<th>다이나믹SQL 여부</th>
					<td>${perfCheckAllPgm.dynamic_sql_yn}</td>
					<th>등록일시</th>
					<td>${perfCheckAllPgm.reg_dt}</td>
					<th>변경일시</th>
					<td>${perfCheckAllPgm.last_update_dt}</td>
				</tr>
				<tr>
					<th>파일명</th>
					<td colspan="3">${perfCheckAllPgm.file_nm}</td>
					<th>디렉토리</th>
					<td colspan="5">${perfCheckAllPgm.dir_nm}</td>
				</tr>
				<tr>
					<th rowspan="2" style="vertical-align:top;">
						<br /><br />
						<a href="javascript:;" id="sqlCopyBtn1" class="easyui-linkbutton l-btn l-btn-small" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea1">
							<span class="l-btn-left l-btn-icon-left" style="padding:5px"><i class="btnIcon fas fa-copy fa-lg fa-fw"></i> SQL 원본 복사</span>
						</a>
						<br /><br />
						<a href="javascript:;" id="sqlCopyBtn2" class="easyui-linkbutton l-btn l-btn-small" data-clipboard-action="copy" data-clipboard-target="#sqlTextArea2">
							<span class="l-btn-left l-btn-icon-left" style="padding:5px"><i class="btnIcon fas fa-copy fa-lg fa-fw"></i> SQL 변환 복사</span>
						</a>
					</th>
					<td colspan="3" rowspan="2" style="vertical-align:top">
						<div id="sqlTextArea1" style="width:100%;height:320px;overflow-y:auto;">${fn:replace(fn:replace(perfCheckAllPgm.program_source_desc,newLineChar,'<br/>'),' ','&nbsp;')}</div>
						<div id="sqlTextArea2" style="width:1px;height:1px;left:-100px;position:absolute;">${fn:replace(fn:replace(perfCheckAllPgm.program_source_desc2,newLineChar,'<br/>'),' ','&nbsp;')}</div>
					</td>
					<c:set var="deployPerfChkExecBindListSize" value="${fn:length(deployPerfChkExecBindList)}" />
						
					<th style="border-top:0px;padding:2px 4px;width:105px;border-right:1px;">수행회차</th>
					<td style="border-top:0px">
						<c:forEach items="${programExecuteTmsList}" var="program" varStatus="status">
							<input type="hidden" name="programExecDt" id="programExecDt${program.program_execute_tms}" value="${program.program_exec_dt}"/>
						</c:forEach>								
						<select id="programExecuteTms" name="programExecuteTms" onchange="fnChangeProgramExecuteTms()">
							<fmt:parseNumber var = "i1" type = "number" value = "${programExecuteTms}" />
							<c:forEach items="${programExecuteTmsList}" var="programExecuteTms" varStatus="status">
								<fmt:parseNumber var = "i2" type = "number" value = "${programExecuteTms.program_execute_tms}" />
								<option value="${programExecuteTms.program_execute_tms}" data-index="${status.index}" <c:if test="${i1 == i2 or i1 eq i2}">selected</c:if>>${programExecuteTms.program_execute_tms}</option>
							</c:forEach>
						</select>
						<span id="programExecDtSpan" style="padding-left:10px">
							${programExecuteTmsList[0].program_exec_dt}
						</span>
					</td>
					<th style="border-top:0px;padding:2px 4px;width:105px;border-right:1px;">페이징처리 여부</th>
					<td id="pagingYn">${programExecuteTmsList[0].pagingYn}</td>
					<th style="border-top:0px;padding:2px 4px;width:105px;border-right:1px;">페이징처리 건수</th>
					<td id="pagingCnt">${programExecuteTmsList[0].pagingCnt}</td>
					
				<tr>
					<td colspan="6" style="padding:1px;height:300px;vertical-align:top;overflow-y:auto">
						<table id="deployPerfChkExecBindTable" style="width:100%;border-right:1px solid #ddd;border-left:0px;">
							<thead>
<!-- 							<tr> -->
<!-- 								<th nowrap style="border-top:0px;padding:2px 4px;width:101px;border-right:1px;">수행회차</th> -->
<!-- 								<td colspan="3" style="border-top:0px;width:500px;border-right:1px;"> -->
<%-- 									<c:forEach items="${programExecuteTmsList}" var="program" varStatus="status"> --%>
<%-- 										<input type="hidden" name="programExecDt" id="programExecDt${program.program_execute_tms}" value="${program.program_exec_dt}"/> --%>
<%-- 									</c:forEach>								 --%>
<!-- 									<select id="programExecuteTms" name="programExecuteTms" onchange="fnChangeProgramExecuteTms()"> -->
<%-- 									    <fmt:parseNumber var = "i1" type = "number" value = "${programExecuteTms}" /> --%>
<%-- 										<c:forEach items="${programExecuteTmsList}" var="programExecuteTms" varStatus="status"> --%>
<%-- 										    <fmt:parseNumber var = "i2" type = "number" value = "${programExecuteTms.program_execute_tms}" /> --%>
<%-- 											<option value="${programExecuteTms.program_execute_tms}" <c:if test="${i1 == i2 or i1 eq i2}">selected</c:if>>${programExecuteTms.program_execute_tms}</option> --%>
<%-- 										</c:forEach> --%>
<!-- 									</select> -->
<!-- 									<span id="programExecDtSpan" style="padding-left:10px"> -->
<%-- 									${performanceCheckMng.program_exec_dt} --%>
<!-- 									</span> -->
<!-- 								</td> -->
<!-- 							</tr> -->
								<tr>
									<th>순번</th>
									<th>변수명</th>
									<th>변수타입</th>
									<th>변수값</th>
								</tr>
								</thead>
								<tbody>
								<c:choose>
									<c:when test="${deployPerfChkExecBindListSize > 0}">
										<c:forEach items="${deployPerfChkExecBindList}" var="deployPerfChkExecBind" varStatus="status">
											<tr>
												<td class="tac">${deployPerfChkExecBind.bind_seq}</td>
												<td class="tac">${deployPerfChkExecBind.bind_var_nm}</td>
												<td class="tac">${deployPerfChkExecBind.bind_var_type}</td>
												<td class="tac">${deployPerfChkExecBind.bind_var_value}</td>
											</tr>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<tr>
											<td colspan="4" class="tac" style="border-left:0px;border-right:1px !important;">바인드 값이 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
								</tbody>
							</table>
						</td>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>