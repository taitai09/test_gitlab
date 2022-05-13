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
 * 2019.01.03	임호경	최초작성
 **********************************************************/
%>
<html lang="ko">
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <meta http-equiv="cleartype" content="on" />    
	    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/performanceCheckIndex/proPerfExcReqExceptionHandlingTab.js?ver=<%=today%>"></script>
<style>
input {
	height: 23px !important;
}

html {
	overflow: text;
}
#_easyui_textbox_input5 {
	background-color:#fff;
}
.textbox {
	background-color:#F2F3EF;
}
.easyui-fluid {
	background-color:#fff;
}
/* #exec_handling_div input[readonly] { */
/* 	background-color: #f9f9f9; */
/* } */

/* #exec_handling_div textarea[readonly] { */
/* 	background-color: #f9f9f9; */
/* } */
</style>
</head>



<body>
<!-- container START -->
			<!-- <div class="well" style="margin-top:10px;font-weight:bold;width:160px;height:20px;">
			<div> 보류
			    <label for="exec_handling"><input id="exec_handling" class="radio" type="radio" name="exec_handling" value="exec_handling" onclick="changeShowDiv('1')" checked/>예외처리</label>
			    <label for="exec_remove"><input id="exec_remove" class="radio" type="radio" name="exec_remove" value="exec_remove" onclick="changeShowDiv('2')"/>예외삭제</label>
			</div>
			</div> -->
			<div id="exec_handling_div" style="height:360px;overflow-y:auto;">
				<div style="margin-top:10px;margin-left:10px;color:#ff0000;font-weight:bold;">
				<%-- 	<c:if test="${data.user_auth_id ne '2' and data.user_auth_id ne '4'}"> <!-- 개발자 배포성능관리자만 허용, 현재는 dbmanager도 포함 -->
						※개발자, 배포성능관리자 이외의 허용되지 않은 권한 입니다.<br/>
					</c:if> --%>
					<c:if test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '2'}"> <!-- 요청중 AND 점검제외 (메세지 뿌리기) -->
						※예외처리방법이 영구 점검제외로 요청되었습니다. <br/>
					</c:if>
					<c:if test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '3'}"> <!-- 요청중 AND 점검제외 (메세지 뿌리기) -->
						※예외처리방법이 한시 점검제외로 요청되었습니다. <br/>
					</c:if>
					<c:if test="${data.deployPerfChkIndc.perf_check_meth_cd eq '1' and data.deployPerfChkIndc.perf_check_auto_pass_yn eq 'Y' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}">
						※ 예외처리방법이 영구 점검제외에서 지표단위로 변경 요청되었습니다. [예외 삭제] 버튼 탭을 통해 처리바랍니다.	
					</c:if>				
				</div>
				 <form:form method="post" id="exec_handling_form" name="exec_handling_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				 
					<input type="hidden" id="search_deploy_id" name="search_deploy_id" value="${data.deployPerfChkIndc.search_deploy_id}">
					<input type="hidden" id="search_from_deploy_request_dt" name="search_from_deploy_request_dt" value="${data.deployPerfChkIndc.search_from_deploy_request_dt}">
					<input type="hidden" id="search_to_deploy_request_dt" name="search_to_deploy_request_dt" value="${data.deployPerfChkIndc.search_to_deploy_request_dt}">
					<input type="hidden" id="search_exception_prc_status_cd" name="search_exception_prc_status_cd" value="${data.deployPerfChkIndc.search_exception_prc_status_cd}">
					<input type="hidden" id="searchKey" name="searchKey" value="${data.deployPerfChkIndc.searchKey}">
					<input type="hidden" id="search_dbio" name="search_dbio" value="${data.deployPerfChkIndc.search_dbio}">
					<input type="hidden" id="search_program_nm" name="search_program_nm" value="${data.deployPerfChkIndc.search_program_nm}">
					<input type="hidden" id="search_deploy_requester" name="search_deploy_requester" value="${data.deployPerfChkIndc.search_deploy_requester}">
					
					<input type="hidden" id="crud_flag" name="crud_flag" value="C">
					<input type="hidden" id="user_auth_id" name="user_auth_id" value="${data.user_auth_id}">
					<input type="hidden" id="user_id" name="user_id" value="${data.user_id}">
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value="${data.deployPerfChkIndc.wrkjob_cd}">
					<input type="hidden" id="exception_request_id" name="exception_request_id" value="${data.deployPerfChkIndc.exception_request_id}">
					<input type="hidden" id="deploy_check_status_cd" name="deploy_check_status_cd" value="${data.deployPerfChkIndc.deploy_check_status_cd}">
					<input type="hidden" id="exception_requester_id" name="exception_requester_id" value="${data.deployPerfChkIndc.exception_requester_id}">
<%-- 					<input type="hidden" id="excepter_id" name="excepter_id" value="${data.deployPerfChkIndc.excepter_id}"> --%>
					<input type="hidden" id="program_div_cd" name="program_div_cd" value="${data.deployPerfChkIndc.program_div_cd}">
					<input type="hidden" id=deploy_requester_id name="deploy_requester_id" value="${data.deployPerfChkIndc.deploy_requester_id}">
					<input type="hidden" id="perf_check_result_div_cd" name="perf_check_result_div_cd" value="${data.deployPerfChkIndc.perf_check_result_div_cd}">
					<input type="hidden" id="last_perf_check_step_id" name="last_perf_check_step_id" value="${data.deployPerfChkIndc.last_perf_check_step_id}">
					<input type="hidden" id="program_execute_tms" name="program_execute_tms" value="${data.deployPerfChkIndc.program_execute_tms}">
					<input type="hidden" id="perf_check_step_id" name="perf_check_step_id" value="${data.deployPerfChkIndc.perf_check_step_id}">	
					
					<input type="hidden" id="exception_prc_meth_cd" name="exception_prc_meth_cd" value="${data.deployPerfChkIndc.exception_prc_meth_cd}">
					<input type="hidden" id="perf_check_id" name="perf_check_id" value="${data.deployPerfChkIndc.perf_check_id}">
					<input type="hidden" id="exception_request_detail_why" name="exception_request_detail_why" value="${data.deployPerfChkIndc.exception_request_detail_why}">
<%-- 					<input type="hidden" id="exception_request_why_cd" name="exception_request_why_cd" value="${data.deployPerfChkIndc.exception_request_why_cd}"> --%>
					<input type="hidden" id="exception_prc_status_cd" name="exception_prc_status_cd" value="${data.deployPerfChkIndc.exception_prc_status_cd}">
					<input type="hidden" id="exception_request_why" name="exception_request_why" value="${data.deployPerfChkIndc.exception_request_why}">
					<input type="hidden" id="perf_check_auto_pass_yn" name="perf_check_auto_pass_yn" value="${data.deployPerfChkIndc.perf_check_auto_pass_yn}">
					<input type="hidden" id="program_id" name="program_id" value="${data.deployPerfChkIndc.program_id}">
					<input type="hidden" id="RequestCnt" name="RequestCnt" value="${data.RequestCnt}">
					<input type="hidden" id="perf_check_auto_pass_del_yn" name="perf_check_auto_pass_del_yn" value="${data.deployPerfChkIndc.perf_check_auto_pass_del_yn}">
					
					<!-- 선택된 체크박스의 로우값을 ',' 로구분하여 데이터가 들어감 -->
					<input type="hidden" id="perf_check_indc_id_chk" name="perf_check_indc_id_chk" value="">
					<input type="hidden" id="indc_yn_decide_div_cd_chk" name="indc_yn_decide_div_cd_chk" value="">
					<input type="hidden" id="perf_check_result_div_cd_chk" name="perf_check_result_div_cd_chk" value="">
					<input type="hidden" id="perf_check_meth_cd_chk" name="perf_check_meth_cd_chk" value="">
					<input type="hidden" id="except_reg_yn_chk" name="except_reg_yn_chk" value="">
					<input type="hidden" id="indc_pass_max_value_chk" name="indc_pass_max_value_chk" value="">
					<input type="hidden" id="change_indc_pass_max_value_chk" name="change_indc_pass_max_value_chk" value="">
					<input type="hidden" id="change_not_pass_pass_chk" name="change_not_pass_pass_chk" value="">
					<input type="hidden" id="change_yn_decide_div_cd_chk" name="change_yn_decide_div_cd_chk" value="">

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
							<th>예외 처리자</th>
							<td style="display: none;">
								<input type="text" id="excepter_id" name="excepter_id" class="w200 easyui-textbox" 
								<c:choose>
									<c:when test="${data.deployPerfChkIndc.excepter_id eq ''}">
										value="${data.user_id}" 
									</c:when>
									<c:when test="${data.deployPerfChkIndc.excepter_id ne ''}">
										value="${data.deployPerfChkIndc.excepter_id}" 
									</c:when>
								</c:choose>
								readonly="true"/>
							</td>	
							<td>
								<input type="text" id="excepter_nm" name="excepter_nm" class="w200 easyui-textbox" 
								<c:choose>
									<c:when test="${data.deployPerfChkIndc.excepter_id eq ''}">
										value="${data.user_nm}" 
									</c:when>
									<c:when test="${data.deployPerfChkIndc.excepter_id ne ''}">
										value="${data.deployPerfChkIndc.except_processor}" 
									</c:when>
								</c:choose>
								readonly="true"/>
							</td>	
							<th>예외 처리일시</th>
								<td>
									<input type="text" id="exception_prc_dt" name="exception_prc_dt" class="w200 easyui-textbox" 
									<c:choose>
										<c:when test="${data.deployPerfChkIndc.exception_prc_dt eq ''}">
											value="${data.today}" 
										</c:when>
										<c:when test="${data.deployPerfChkIndc.exception_prc_dt ne ''}">
											value="${data.deployPerfChkIndc.exception_prc_dt}" 
										</c:when>
									</c:choose>
									readonly="true"/>
								</td>
							<th>예외 처리상태</th>
								<td>
									<input type="text" id="exception_prc_status_cd_nm" name="exception_prc_status_cd_nm" class="w200 easyui-textbox" value="${data.deployPerfChkIndc.exception_prc_status_cd_nm}" readonly="true"/>
								</td>
						</tr>
						
						<tr>
							<th>예외 처리 사유</th>
								<td colspan="5">
								<textarea id="exception_prc_why" name="exception_prc_why" class="easyui-textbox" data-options="multiline:true" style="width:100%;height:40px"
									<c:if test="${data.user_auth_id eq '4'}">
										readonly="true"		
									</c:if>
									<c:if test="${data.user_auth_id eq '2'}">
										<c:choose>
											<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.perf_check_auto_pass_yn eq 'Y' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}">
												readonly="true"		
											</c:when>
											<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1'}">

											</c:when>
											<c:otherwise>
												readonly="true"
											</c:otherwise>
										</c:choose>
									</c:if>
								>${data.deployPerfChkIndc.exception_prc_why}
								</textarea>
								</td>
						</tr>
					</table>
					<c:if test="${data.deployPerfChkIndc.exception_prc_meth_cd eq '2' and data.deployPerfChkIndc.exception_prc_status_cd eq '2'}"> 		
						<table class="noneT">
						<colgroup>	
							<col style="width:15%;">
						</colgroup>
							<tr>
								<th style="padding-top:14px !important;"><span style="font-size:15px;">영구 점검제외</span>
									<span id="check_span1" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="exception_prc_meth_cd_chk" value="exception_prc_meth_cd_chk" 
										checked
									></span> 
								</th>
								<td></td>
							</tr>
						</table>
					</c:if>
					<c:if test="${data.deployPerfChkIndc.exception_prc_meth_cd eq '3' and data.deployPerfChkIndc.exception_prc_status_cd eq '2'}"> 		
						<table class="noneT">
						<colgroup>	
							<col style="width:15%;">
						</colgroup>
							<tr>
								<th style="padding-top:14px !important;"><span style="font-size:15px;">한시 점검제외</span>
									<span id="check_span1" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="exception_prc_meth_cd_chk" value="exception_prc_meth_cd_chk" 
										checked
									></span> 
								</th>
								<td></td>
							</tr>
						</table>
					</c:if>
					<table class="realT2" style="margin-top:10px;margin-bottom:5px;width:100%;border-right:1px solid lightgray;">
							<colgroup>	
								<col style="width:2%;"/>
								<col style="width:22%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
								<col style="width:8%;"/>
							</colgroup>
							<thead>
								<tr>
									<th rowspan="2" style="border-left:1px solid lightgray">
										<input class="chkbox" id="checkedAll" type="checkbox" >
									</th>
									<th rowspan="2">점검지표</th>
									<th colspan="5">성능 점검 결과값</th>
									<th colspan="3">예외적용값</th>
								</tr>
								<tr>
									<th>적합</th>
									<th>부적합</th>
									<th style="display:none">여부값 판정구분(필요없어짐 수정)</th>
									<th>성능 점검 결과값</th>
									<th>지표 성능 점검 결과</th>
									<th>예외 등록 여부</th>
									<th>적합</th>
									<th>부적합</th>
									<th>여부값 판정구분</th>
								</tr>
							</thead>
							<tbody>
				<c:forEach var="resultList" items="${data.resultList}" varStatus="status">
					<tr
					<c:if test="${resultList.is_checked eq 'Y'}">
						class="chkRow"
					</c:if>
					>
						<td>
							<input class="chkbox" id="chkform_${status.index}" type="checkbox" name="chk_info !important;"
							<c:if test="${resultList.is_checked eq 'Y'}">
								checked="true"
							</c:if>
							<c:choose>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.perf_check_auto_pass_yn eq 'Y' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}"> 
									disabled="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '2'}">
									disabled="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}">
									<c:choose>
										<c:when test="${resultList.perf_check_result_div_cd eq 'A' or resultList.perf_check_result_div_cd eq 'C'}">
											disabled="true"
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									disabled="true"
								</c:otherwise>
							</c:choose>
							>
						</td>
						<td style="display: none;"></td>
						<td style="display:none;">${resultList.perf_check_indc_id}</td>
						<td style="display:none;">${resultList.indc_yn_decide_div_cd}</td>
						<td style="display:none;">${resultList.perf_check_result_div_cd}</td>
						<td style="display:none;">${resultList.perf_check_meth_cd}</td>
						<td style="display:none;">${resultList.except_reg_yn}</td>
						<td>${resultList.perf_check_indc_nm}</td>
						<td>${resultList.indc_pass_max_value}</td>
						<td style="display:none;">${resultList.not_pass_pass}</td>
						<td>${resultList.indc_yn_decide_div_cd_nm}</td>
						<td>${resultList.exec_result_value}</td>
						<td 
							<c:choose>
								<c:when test="${resultList.perf_check_result_div_cd_nm eq '적합'}">
									style="background-color:#1A9F55;color:white;";
								</c:when>						
								<c:when test="${resultList.perf_check_result_div_cd_nm eq '부적합'}">
									style="background-color:#E41E2C;color:white;";
								</c:when>						
								<c:otherwise>
									style="background-color:#ED8C33;color:white;";
								</c:otherwise>						
							</c:choose>
						>${resultList.perf_check_result_div_cd_nm}</td>
						<td>${resultList.except_reg_yn}</td>
						<td>
							<input type="number" id="change_indc_pass_max_value_${status.index}" name="change_indc_pass_max_value" class="w110 easyui-textbox" value="${resultList.change_indc_pass_max_value}"
							
							<c:choose>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.perf_check_auto_pass_yn eq 'Y' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}"> 
									readonly="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '2'}">
									readonly="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}">
									<c:choose>
										<c:when test="${resultList.perf_check_result_div_cd eq 'A' or resultList.perf_check_result_div_cd eq 'C'}">
											readonly="true"
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									readonly="true"
								</c:otherwise>
							</c:choose>
							
							<c:if test="${resultList.perf_check_meth_cd eq '2'}">
								disabled="disabled"
							</c:if>
							/>
						</td>
						<td>
							<input type="text" id="change_not_pass_pass_${status.index}" name="change_not_pass_pass" class="w110 easyui-textbox" 
							
							<c:if test="${resultList.change_not_pass_pass eq ' 초과'}">
								value=""
							</c:if>
							<c:if test="${resultList.change_not_pass_pass ne ' 초과'}">
								value="${resultList.change_not_pass_pass}"
							</c:if>
							 
							<c:if test="${resultList.perf_check_meth_cd eq '2'}">
								disabled="disabled"
							</c:if>
							<c:if test="${resultList.perf_check_meth_cd eq '1'}">
								readonly="false"
							</c:if>
							/>
						</td>
						<td>
							<select id="change_yn_decide_div_cd_${status.index}" name="change_yn_decide_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${resultList.change_yn_decide_div_cd}"
							
							<c:choose>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.perf_check_auto_pass_yn eq 'Y' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}"> 
									readonly="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '2'}">
									readonly="true"
								</c:when>
								<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1' and data.deployPerfChkIndc.exception_prc_meth_cd eq '1'}">
									<c:choose>
										<c:when test="${resultList.perf_check_result_div_cd eq 'A' or resultList.perf_check_result_div_cd eq 'C'}">
											readonly="true"
										</c:when>
										<c:otherwise>

										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									readonly="true"
								</c:otherwise>
							</c:choose>
							
							<c:if test="${resultList.perf_check_meth_cd eq '1'}">
								disabled="disabled"
							</c:if>
							>
								<option value="">선택</option>
								<c:if test="${resultList.change_yn_decide_div_cd eq 1}">
									<option value="1" selected>적합</option>
								</c:if>
								<c:if test="${resultList.change_yn_decide_div_cd eq 2}">
									<option value="2" selected>부적합</option>
								</c:if>
								<option value="1">적합</option>
								<option value="2">부적합</option>
							</select>
						</td>
					</tr>
				</c:forEach>
				</tbody>
				</table>
				</form:form>
			</div>	
			
</body>
</html>