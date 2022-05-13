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
 * 2021.07.2?	???	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 :: 실행 SQL 성능 검증 :: 성능 검증 예외 요청</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />  
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/requestException.js?ver=<%=today%>"></script>
   	<script type="text/javascript" src="/resources/js/ui/include/popup/requestException_popup.js?ver=<%=today%>"></script>
    
<style>
.tabs-wrap {
	background-color: #ffffff;
}

.tabs-header {
	border: #ffffff;
	background-color: #ffffff;
}

#container {
	height: 800px;
}
.searchBtn.innerBtn2{
	margin-bottom: 10px;
}
#detailCheckResultTable {
	margin-top: 0px;
}
.detailT3 tr th, .detailT3 tr td {
	border-top: 1px solid rgb(221, 221, 221);
}
</style>
<script type="text/javascript">
var search_wrkjob_cd = "${search_wrkjob_cd}";
var search_wrkjob_cd_nm = "${search_wrkjob_cd_nm}";
var user_auth_id = "${user_auth_id}";
</script>

</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="user_nm" name="user_nm" value="${user_nm}">
					<input type="hidden" id="user_auth_id" name="user_auth_id" value="${user_auth_id}">
					<input type="hidden" id="search_user_id" name="search_user_id">

					<input type="hidden" id="search_perf_check_id" name="search_perf_check_id" value="${search_perf_check_id}">
					<!-- 최종점검단계 검색조건에서  seach_perf_check_step_id 사용하여 perf_check_step_id로 변수명 변경-->
					<input type="hidden" id="perf_check_step_id" name="perf_check_step_id" value="${search_perf_check_step_id}">
					<input type="hidden" id="search2_perf_check_step_id" name="search2_perf_check_step_id" value="${search_perf_check_step_id}">
					<input type="hidden" id="search_program_id" name="search_program_id" value="${search_program_id}">
					<input type="hidden" id="search_program_execute_tms" name="search_program_execute_tms" value="${search_program_execute_tms}">
						<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					<input type="hidden" id="deploy_id" name="deploy_id">
					<input type="hidden" id="perf_check_id" name="perf_check_id">

					<label>업무</label>
					<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree"  value="${deployPerfChkIndc.search_wrkjob_cd}" required="true"></select>
			
					<label>배포요청일자</label>
<!-- 					<input class="chkbox" class="chkBox" id="search_date_chk" type="checkbox" name="search_date_chk" value="check" style="vertical-align:middle; width:15px; height:18px;"> -->
						<input type="text" id="search_from_deploy_request_dt" name="search_from_deploy_request_dt" 
						<c:choose>
							<c:when test="${deployPerfChkIndc.search_from_deploy_request_dt eq '' or deployPerfChkIndc.search_from_deploy_request_dt eq null}">
							value="${startDate}" 
							</c:when>
							<c:when test="${deployPerfChkIndc.search_from_deploy_request_dt ne '' or deployPerfChkIndc.search_from_deploy_request_dt ne null}">
							value="${deployPerfChkIndc.search_from_deploy_request_dt}" 
							</c:when>
						</c:choose>
						data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required"/> ~
						<input type="text" id="search_to_deploy_request_dt" name="search_to_deploy_request_dt" 
						<c:choose>
							<c:when test="${deployPerfChkIndc.search_to_deploy_request_dt eq '' or deployPerfChkIndc.search_to_deploy_request_dt eq null}">
							value="${nowDate}" 
							</c:when>
							<c:when test="${deployPerfChkIndc.search_to_deploy_request_dt ne '' or deployPerfChkIndc.search_to_deploy_request_dt ne null}">
							value="${deployPerfChkIndc.search_to_deploy_request_dt}" 
							</c:when>
						</c:choose>
						data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required"/>
			
					<label>예외처리요청상태</label>
					<select id="search_exception_prc_status_cd" name="search_exception_prc_status_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.search_exception_prc_status_cd}">
						<option value="">전체</option>
						<option value="1">요청</option>
						<option value="5">미요청</option>
					</select>
<!-- 					<label>성능점검결과</label> -->
<%-- 					<select id="search_perf_check_result_div_cd" name="search_perf_check_result_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" value="${deployPerfChkIndc.search_perf_check_result_div_cd}"></select> --%>
				
					<label>최종검증단계</label>
					<select id="search_perf_check_step_id" name="search_perf_check_step_id" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.search_perf_check_result_div_cd}"></select>

<!-- 					<label>예외처리방법</label> -->
<!-- 					<select id="search_exception_prc_meth_cd" name="search_exception_prc_meth_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" ></select> -->
					
					<span style="padding-right:5px;padding-left:10px;"><input class="easyui-radiobutton" id="way1" name="howToWay" value="A" checked> 개별 예외요청  </span> 
				    <span style="padding-right:5px;padding-left:10px;"><input class="easyui-radiobutton" id="way2" name="howToWay" value="B"> 일괄 예외요청  </span> 
				    <span style="padding-right:5px;padding-left:10px;"><input class="easyui-radiobutton" id="way3" name="howToWay" value="C"> 일괄 예외처리  </span> 
					    
					
					<div class="multi">
						<label>검색조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.searchKey}">
<!-- 							<option value="">전체</option> -->
<!-- 							<option value="01">SQL식별자(DBIO)</option> -->
<!-- 							<option value="02">프로그램</option> -->
						</select>
						<span id="search_1" >
							<input type="text" id="search_dbio" name="search_dbio" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_dbio}"/>
						</span>
						
						<span id="search_2" >
							<input type="text" id="search_program_nm" name="search_program_nm" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_program_nm}"/>
						</span>
						
						<span id="search_3">
							<input type="text" id="search_program_desc" name="search_program_desc" class="w200 easyui-textbox" />
						</span>
						
						<span id="search_4">
							<input type="text" id="search_trade" name="search_trade" class="w200 easyui-textbox" />
						</span>
						
						<label>배포요청자</label>
						<input type="text" id="search_deploy_requester" name="search_deploy_requester" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_deploy_requester}"/>
						
						<label>배포ID</label>
						<input type="text" id="search_deploy_id" name="search_deploy_id" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_deploy_id}"/>
	
						<label>검색건수</label>
						<select id="searchCount" name="selectRcount" data-options="editable:true" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto'">
<!-- 							<option value="">전체</option> -->
							<option value="10" selected>10</option>
							<option value="20" >20</option>
							<option value="40">40</option>
							<option value="60">60</option>
							<option value="80">80</option>
							<option value="100">100</option>
						</select>
					
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
						</span>
				       
						<div class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
						</div>				
					</div>			
				</form:form>
			</div>
		</div>

			      <div class="easyui-panel" data-options="border:false" style="width:100%;min-height:170px;margin-bottom:10px;">
			            <table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			               <tbody><tr></tr></tbody>
			            </table>
			      </div>
      			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
		
		<div id="perf_exec_req_tab" class="easyui-tabs" data-options="border:false" style="width:100%;height:350px;">

				<div title="프로그램 정보" style="border-bottom:1px solid;">
					<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="crud_flag" name="crud_flag">
					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="user_auth_id" name="user_auth_id" value="${user_auth_id}">
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd">
					<input type="hidden" id="exception_request_id" name="exception_request_id">
					<input type="hidden" id="deploy_check_status_cd" name="deploy_check_status_cd">
					<input type="hidden" id="exception_requester_id" name="exception_requester_id">
					<input type="hidden" id="excepter_id" name="excepter_id">
					<input type="hidden" id="program_div_cd" name="program_div_cd">
					<input type="hidden" id="deploy_requester_id" name="deploy_requester_id">
					<input type="hidden" id="perf_check_result_div_cd" name="perf_check_result_div_cd">
					<input type="hidden" id="perf_check_result_div_cd_nm" name="perf_check_result_div_cd_nm">
					<input type="hidden" id="perf_check_step_id" name="perf_check_step_id">
					<input type="hidden" id="last_perf_check_step_id" name="last_perf_check_step_id">
					<input type="hidden" id="exception_prc_meth_cd" name="exception_prc_meth_cd">
					<input type="hidden" id="perf_check_id" name="perf_check_id">
					<input type="hidden" id="exception_request_detail_why" name="exception_request_detail_why">
					<input type="hidden" id="exception_request_why_cd" name="exception_request_why_cd">
					<input type="hidden" id="exception_request_why_cd_nm" name="exception_request_why_cd_nm">
					<input type="hidden" id="exception_prc_status_cd" name="exception_prc_status_cd">
					<input type="hidden" id="except_processor" name="except_processor">
					<input type="hidden" id="exception_request_why" name="exception_request_why">
					<input type="hidden" id="exception_prc_dt" name="exception_prc_dt">
					<input type="hidden" id="exception_prc_status_cd_nm" name="exception_prc_status_cd_nm">
					<input type="hidden" id="perf_check_auto_pass_yn" name="perf_check_auto_pass_yn">
					<input type="hidden" id="eception_prc_status_cd" name="eception_prc_status_cd" >	
					<input type="hidden" id="exception_prc_why" name="exception_prc_why" >	
					<input type="hidden" id="search_user_id" name="search_user_id" >	
					<input type="hidden" id="perf_check_auto_pass_del_yn" name="perf_check_auto_pass_del_yn" >	
					<input type="hidden" id="dbid" name="dbid" >	
					<input type="hidden" id="program_type_cd" name="program_type_cd" >
					<input type="hidden" id="howToWay" name="howToWay" value="A" >
					<input type="hidden" name="onlyIncorrectYn" value="">
					
					<input type="hidden" id="program_id" name="program_id"/>
					<input type="hidden" id="program_nm" name="program_nm"/>
					<input type="hidden" id="program_execute_tms" name="program_execute_tms"/>
					<input type="hidden" id="dbio" name="dbio"/>
					<input type="hidden" id="deploy_id" name="deploy_id"/>
					<input type="hidden" id="top_wrkjob_cd" name="top_wrkjob_cd"/>
					
<!-- 					<input type="hidden" id="sql_command_type_cd" name="sql_command_type_cd" value="" > -->
					
					<table class="detailT">
						<tr>
							<th>배포명</th>
								<td colspan="5" id="deploy_nm" class="w620">
								</td>
							<th>배포ID</th>
								<td id="deploy_id" class="w120" >
								</td>
							<th>성능검증상태</th>
								<td id="deploy_check_status_cd_nm" class="w120">
								</td>
							<th>배포예정일자</th>
								<td id="deploy_expected_day" class="w120">
								</td>
						</tr>
						<tr>
							<th>프로그램명</th>
								<td colspan="5" id="program_nm" class="w620">
								</td>
							<th style="display: none;">검증제외 <input class="easyui-checkbox" id="perf_check_auto_pass_yn_chk" name="perf_check_auto_pass_yn_checkBox" style="margin-left:1px;" readonly="true"></th>
								<td id ="exception_prc_meth_cd_nm" class="w120" style="margin-left:1px;display: none;">
								</td>
							<th>프로그램구분</th>
								<td id="program_div_cd_nm" class="w120">
								</td>
							<th>프로그램유형</th>
								<td id="program_type_cd_nm" class="w120">
								</td>
							<th>SQL명령유형</th>
								<td id="sql_command_type_cd" class="w120">
								</td>
								
						</tr>
						<tr>
							<th>프로그램설명</th>
							<td colspan="11" id="program_desc" class="w1365">
							</td>
						</tr>
						<tr> 
							<th>SQL식별자(DBIO)</th>
								<td colspan="5" id="dbio" class="w620">
								</td>
							<th>다이나믹SQL 여부</th>
								<td id="dynamic_sql_yn" class="w120">
								</td>
							<th>등록일시</th>
								<td id="reg_dt" class="w120">
								</td>
							<th>변경일시</th>
								<td id="last_update_dt" class="w120">
								</td>
						</tr>
						<tr>
							<th>파일명</th>
								<td colspan="5" id="file_nm" class="w620">
								</td>
							<th>디렉토리</th>
								<td colspan="5" id="dir_nm" class="w620">
								</td>
						</tr>
						<tr>
							<th>
								<a href="javascript:;" id="sqlCopyBtn" class="w110 easyui-linkbutton l-btn l-btn-small" data-clipboard-action="copy" data-clipboard-target="#program_source_desc">
								<i class="btnIcon fas fa-save fa-lg fa-fw"></i>SQL 복사</a><br/><br/>
								<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SetSQLFormatterEspc();"><i class="btnIcon fas fa-recycle fa-lg fa-fw"></i> SQL Format</a>							
							</th>
							<td colspan="11" style="padding:10px;height:300px;">
								<textarea name="program_source_desc" id="program_source_desc" style="width:100%;height:100%;border:none;" wrap="off" readonly="true"></textarea>
<!-- 								<textarea name="program_source_desc_temp" id="program_source_desc_temp" style="display:none;"></textarea> -->
								
							</td>
<!-- 							<td colspan="2" style="padding:10px;"> -->
<!-- 								<div id="bindList" class="easyui-panel" data-options="border:false" style="width:100%;min-height:400px;"> -->
<!-- 									<table id="bindTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 									</table> -->
<!-- 								</div> -->
<!-- 							</td> -->
						</tr>
						
					</table>
				</form:form>
			</div>
			
			<!-- <div title="성능검증결과">
				<div data-options="region:'center',border:false" style="padding:5px">
					<div id="perfCheckResultList" class="easyui-panel" data-options="border:false" style="width:100%;min-height:400px;padding-top:10px;">
						<table id="perfCheckResultTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>					
			</div> -->
			<div title="예외 요청" style="border-bottom:1px solid;">
				<form:form method="post" id="exec_request_form" name="exec_request_form" class="form-inline">
					
					<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>

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
							<th>업무</th>
								<td>
									<input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w290 easyui-textbox" readonly="readonly"/>
								</td>
							<th>프로그램명</th>
								<td>
									<input type="text" id="program_nm" name="program_nm" class="w290 easyui-textbox" readonly="readonly"/>
								</td>	
							<th>SQL식별자(DBIO)</th>
								<td>
									<input type="text" id="dbio" name="dbio" class="w300 easyui-textbox" readonly="readonly"/>
								</td>
						</tr>
						<tr>
								<th>예외 처리 방법</th>
								<td>
									<select id="exception_prc_meth_cd" name="exception_prc_meth_cd" data-options="panelHeight:'auto',editable:false" class="w290 easyui-combobox" required="true"></select>
								</td>
								<th>예외 요청 사유</th>
								<td>
									<select id="exception_request_why_cd" name="exception_request_why_cd" data-options="panelHeight:'auto',editable:false" class="w290 easyui-combobox" required="true"></select>
								</td>
								<th>예외 요청자</th>
								<td>
									<input type="text" id="exception_requester" name="exception_requester" class="w300 easyui-textbox" readonly="readonly"/>
								</td>
						</tr>
						<tr>
							<th>예외 요청<br/>상세 사유</th>
								<td colspan="5"><textarea id="exception_request_detail_why" class="easyui-textbox" data-options="multiline:true" name="exception_request_detail_why" style="width:100%;height:40px" required="true" readonly="readonly"></textarea></td>
						</tr>
					</table>
					<div class="searchBtn innerBtn2">
						<a href="javascript:;" id="btn_request" class="w80 easyui-linkbutton" onClick="Btn_Request();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 요청</a>
						<a href="javascript:;" id="btn_cancel" class="w80 easyui-linkbutton" onClick="Btn_Cancel();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 요청 취소</a>
						<a href="javascript:;" id="btn_reset" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
					</div>
				</form:form>
					<div data-options="region:'center',border:false">
						<div id="perfCheckResultList" class="easyui-panel" data-options="border:false" style="width:100%;min-height:100px;margin-top:10px;">
							<table id="perfCheckResultTableList" class="tbl easyui-datagrid" data-options="border:false">
							</table>
						</div>
					</div>
					
					
			</div>
			
			<div title="예외 처리" style="overflow-y:hidden;">
<!-- 				<iframe id="exec_handling" src="/PerformanceCheckIndex/ExceptionHandling/CreateTab" width="100%" height="460px;" overflow="text"/> -->
				<iframe id="exec_handling" src="" width="100%" height="540px;" scrolling="no" overflow="text"></iframe>
			</div>
		</div>
		
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/inspectionResult_popup.jsp" %>
</body>
</html>