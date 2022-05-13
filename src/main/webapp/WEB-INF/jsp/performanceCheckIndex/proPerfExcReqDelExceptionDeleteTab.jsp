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
	<script type="text/javascript" src="/resources/js/ui/performanceCheckIndex/proPerfExcReqDelExceptionDeleteTab.js?ver=<%=today%>"></script>
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

/* #exec_handling_div input[readonly] {
    background-color: #f9f9f9;
}
#exec_handling_div textarea[readonly] {
    background-color: #f9f9f9;
} */
</style>
</head>



<body style="visibility:hidden;">
<!-- container START -->
			<!-- <div class="well" style="margin-top:10px;font-weight:bold;width:160px;height:20px;">
			<div> 보류
			    <label for="exec_handling"><input id="exec_handling" class="radio" type="radio" name="exec_handling" value="exec_handling" onclick="changeShowDiv('1')" checked/>예외처리</label>
			    <label for="exec_remove"><input id="exec_remove" class="radio" type="radio" name="exec_remove" value="exec_remove" onclick="changeShowDiv('2')"/>예외삭제</label>
			</div>
			</div> -->
<!-- 			<div id="exec_handling_div"> -->
			<div id="exec_handling_div" style="height:420px;overflow-y:auto;border-bottom:1px solid;">
				<div style="margin-top:5px;margin-left:5px;color:#ff0000;font-weight:bold;">
					<c:if test="${data.user_auth_id ne '5' and data.user_auth_id ne '6'}"> 
						※배포관리자, 개발_배포관리자 이외의 허용되지 않은 권한 입니다.<br/>
					</c:if>
				</div>
				 <form:form method="post" id="exec_handling_form" name="exec_handling_form" class="form-inline">
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
					<input type="hidden" id="deploy_requester_id" name="deploy_requester_id" value="${data.deployPerfChkIndc.deploy_requester_id}">
					<input type="hidden" id="perf_check_result_div_cd" name="perf_check_result_div_cd" value="${data.deployPerfChkIndc.perf_check_result_div_cd}">
					<input type="hidden" id="last_perf_check_step_id" name="last_perf_check_step_id" value="${data.deployPerfChkIndc.last_perf_check_step_id}">
					<input type="hidden" id="exception_prc_meth_cd" name="exception_prc_meth_cd" value="${data.deployPerfChkIndc.exception_prc_meth_cd}">
					<input type="hidden" id="perf_check_id" name="perf_check_id" value="${data.deployPerfChkIndc.perf_check_id}">
					<input type="hidden" id="exception_request_detail_why" name="exception_request_detail_why" value="${data.deployPerfChkIndc.exception_request_detail_why}">
<%-- 					<input type="hidden" id="exception_request_why_cd" name="exception_request_why_cd" value="${data.deployPerfChkIndc.exception_request_why_cd}"> --%>
					<input type="hidden" id="exception_prc_status_cd" name="exception_prc_status_cd" value="${data.deployPerfChkIndc.exception_prc_status_cd}">
					<input type="hidden" id="exception_request_why" name="exception_request_why" value="${data.deployPerfChkIndc.exception_request_why}">
					<input type="hidden" id="perf_check_auto_pass_yn" name="perf_check_auto_pass_yn" value="${data.deployPerfChkIndc.perf_check_auto_pass_yn}">
					<input type="hidden" id="program_id" name="program_id" value="${data.deployPerfChkIndc.program_id}">
					<input type="hidden" id="perf_check_auto_pass_del_yn" name="perf_check_auto_pass_del_yn" value="${data.deployPerfChkIndc.perf_check_auto_pass_del_yn}">
					<input type="hidden" id="except_reg_yn" name="except_reg_yn" value="N">
					<input type="hidden" id="RequestCnt" name="RequestCnt" value="${data.RequestCnt}">
					<input type="hidden" id="program_type_cd" name="program_type_cd" value="${data.deployPerfChkIndc.program_type_cd}">	
					
					
					<!-- 선택된 체크박스의 로우값을 ',' 로구분하여 데이터가 들어감 -->
					<input type="hidden" id="dbio_chk" name="dbio_chk" value="">
					<input type="hidden" id="perf_check_indc_id_chk" name="perf_check_indc_id_chk" value="">
					<input type="hidden" id="excpt_pass_max_value_chk" name="excpt_pass_max_value_chk" value="">
					<input type="hidden" id="excpt_yn_decide_div_cd_chk" name="excpt_yn_decide_div_cd_chk" value="">


					<table class="detailT">
						<colgroup>	
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18.5%;">
						</colgroup>
						<tr>
							<th>예외 처리자</th>
							<td style="display: none;">
								<input type="text" id="excepter_id" name="excepter_id" class="w290 easyui-textbox" 
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
								<input type="text" id="excepter_nm" name="excepter_nm" class="w290 easyui-textbox" 
								<c:choose>
									<c:when test="${data.deployPerfChkIndc.excepter_id eq ''}">
										value="${data.user_nm}" 
									</c:when>
									<c:when test="${data.deployPerfChkIndc.excepter_id ne ''}">
										value="${data.deployPerfChkIndc.excepter_id}" 
									</c:when>
								</c:choose>
								readonly="true"/>
							</td>	
							<th>예외 처리일시</th>
								<td>
									<input type="text" id="exception_prc_dt" name="exception_prc_dt" class="w290 easyui-textbox" 
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
<%-- 									<select id="exception_prc_status_cd" name="exception_prc_status_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" value="${deployPerfChkIndc.exception_prc_status_cd}"> --%>
									<input type="text" id="exception_prc_status_cd_nm" name="exception_prc_status_cd_nm" class="w300 easyui-textbox" value="${data.deployPerfChkIndc.exception_prc_status_cd_nm}" readonly="true"/>
								</td>
							</tr>
							<tr>
								<th rowspan="2">예외 처리 사유</th>
								<td class="textBtnClass">
									<c:forEach var="textValueList" items="${data.textValueList}" varStatus="status">
										<a href="javascript:;" class="textBtn" onclick="Btn_SettingText('${textValueList.cd_nm}')">${textValueList.cd_nm}
										<c:if test="${status.index ne fn:length(data.textValueList) -1}">
										/ 
										</c:if>
									</a>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td colspan="5">
									<textarea id="exception_del_prc_why" name="exception_del_prc_why" class="easyui-textbox" data-options="multiline:true" style="width:99.7%;height:40px"
										<c:if test="${data.user_auth_id eq '4'}">
											readonly="true"		
										</c:if>
										<c:if test="${data.user_auth_id eq '2'}">
											<c:choose>
												<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1'}">
												</c:when>
												<c:otherwise>
													readonly="true"
												</c:otherwise>
											</c:choose>
										</c:if>
									>${data.deployPerfChkIndc.exception_del_prc_why}
									</textarea>
								</td>
							</tr>
						</table>
						
						<table class="realT2" style="margin-top:10px;margin-bottom:5px;width:100%;border-right:1px solid lightgray;">
								<colgroup>	
									<col style="width:2%;"/>
									<col style="width:26%;"/>
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
										<th rowspan="2" style="border-left:1px solid #ddd"><input class="chkbox" id="checkedAll" type="checkbox" ></th>
										<th rowspan="2">점검지표</th>
										<th colspan="3">기준지표</th>
										<th rowspan="2">예외등록여부</th>
										<th colspan="3">예외적용지표</th>
									</tr>
									<tr>
										<th>적합</th>
										<th>부적합</th>
										<th>여부값판정구분</th>
										<th>적합</th>
										<th>부적합</th>
										<th>여부값판정구분</th>
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
											<input class="chkbox" id="chkform_${status.index}" type="checkbox" name="chk_info"
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
														<c:when test="${resultList.except_reg_yn eq 'Y'}">
														</c:when>
														<c:otherwise>
															disabled="true"
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													disabled="true"
												</c:otherwise>
											</c:choose>
											>
										</td>
										<td style="display:none;">${resultList.dbio}</td> <!-- 1 -->
										<td style="display:none;">${resultList.perf_check_indc_id}</td><!-- 2 -->
										<td>${resultList.perf_check_indc_nm}</td><!-- 3 -->
										<td>${resultList.perf_check_meth_cd_nm}</td><!-- 4 -->
										<td>${resultList.pass_max_value}</td><!-- 5 -->
										<td>${resultList.not_pass_pass}</td><!-- 6 -->
										<td>${resultList.except_reg_yn}</td><!-- 7 -->
										<td>${resultList.excpt_pass_max_value}</td><!-- 8 -->
										<td>${resultList.excpt_not_pass_pass}</td><!-- 9 -->
										<td>${resultList.excpt_yn_decide_div_cd_nm}</td><!-- 10 -->
										<td style="display:none;">${resultList.excpt_yn_decide_div_cd}</td><!-- 11 -->
									</tr>
								</c:forEach>
							</tbody>
						</table>
						
						
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" id="btn_except" class="w80 easyui-linkbutton" onClick="Btn_ExceptionDelete();"
							<c:choose>
								<c:when test="${data.user_auth_id eq '5' or data.user_auth_id eq '6'}">
									<c:choose>
										<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1'}">
										</c:when>
										<c:otherwise>
											disabled="true"
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									disabled="true"
								</c:otherwise>
							</c:choose>
							>
							<i class="btnIcon fas fa-save fa-lg fa-fw"></i> 예외삭제</a>
							
							<a href="javascript:;" id="btn_reject" class="w80 easyui-linkbutton" onClick="Btn_Reject();"
							<c:choose>
								<c:when test="${data.user_auth_id eq '5' or data.user_auth_id eq '6'}">
									<c:choose>
										<c:when test="${data.deployPerfChkIndc.exception_prc_status_cd eq '1'}">
										</c:when>
										<c:otherwise>
											disabled="true"
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									disabled="true"
								</c:otherwise>
							</c:choose>
							>
							<i class="btnIcon fas fa-save fa-lg fa-fw"></i> 반려</a>
						</div>
				</div>
			</form:form>
		</div>
				
</body>
</html>