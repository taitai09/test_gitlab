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
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 점검 :: 성능 점검 지표 관리 :: 성능 점검 예외 삭제</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />  
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/ckeditor4/ckeditor.js"></script>
    <script type="text/javascript" src="/resources/js/ui/performanceCheckIndex/proPerfExcReqDel.js?ver=<%=today%>"></script>
<style>
.tabs-wrap{
background-color:#ffffff;
}
.tabs-header {
border:#ffffff;
background-color:#ffffff;
}
#container{
height:800px;
}
/* #perf_exec_req_tab input[readonly] {
    background-color: #f9f9f9;
}
#perf_exec_req_tab textarea[readonly] {
    background-color: #f9f9f9;
} */
</style>


</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;margin-bottom:0px;!important;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>
			</div>					
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="user_nm" name="user_nm" value="${user_nm}">
					<input type="hidden" id="user_auth_id" name="user_auth_id" value="${user_auth_id}">
					<input type="hidden" id="search_user_id" name="search_user_id">
						<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					<input type="hidden" id="perf_check_auto_pass_del_yn" name="perf_check_auto_pass_del_yn" >
					
					<label>업무</label>
					<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree"  value="${search_wrkjob_cd}" required="true"></select>
					

<!-- 					<label>예외처리요청상태</label> -->
<%-- 						<select id="search_exception_prc_status_cd" name="search_exception_prc_status_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.search_exception_prc_status_cd}" > --%>
<!-- 						</select> -->
					
<!-- 						<label>최종점검단계</label> -->
<%-- 						<select id="search_perf_check_step_id" name="search_perf_check_step_id" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.search_perf_check_result_div_cd}"></select> --%>
	
						<label>검색조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
<!-- 							<option value="">전체</option> -->
<!-- 							<option value="01">SQL식별자(DBIO)</option> -->
<!-- 							<option value="02">프로그램</option> -->
						</select>
						<span id="search_1">
							<input type="text" id="search_dbio" name="search_dbio" class="w200 easyui-textbox" value="${search_dbio}"/>
						</span>
						
						<span id="search_2">
							<input type="text" id="search_program_nm" name="search_program_nm" class="w200 easyui-textbox" value="${search_program_nm}"/>
						</span>
						
						<span id="search_3">
							<input type="text" id="search_program_desc" name="search_program_desc" class="w200 easyui-textbox" />
						</span>
						
						<span id="search_4">
							<input type="text" id="search_trade" name="search_trade" class="w200 easyui-textbox" />
						</span>
						<label>삭제요청</label>
						<span style="position:relative;left:-6px !important;">
							<input class="easyui-checkbox" id="search_chk" name="search_chk" value="search_chk" style="margin-right:5px;" >
						</span>
<!-- 							<label>배포요청자</label> -->
<%-- 						<input type="text" id="search_deploy_requester" name="search_deploy_requester" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_deploy_requester}"/> --%>
						
<!-- 						<label>배포ID</label> -->
<%-- 						<input type="text" id="search_deploy_id" name="search_deploy_id" class="w200 easyui-textbox" value="${deployPerfChkIndc.search_deploy_id}"/> --%>
								
							<span style="margin-left:10px !important;" class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>	
				</form:form>
			</div>
		</div>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:170px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;text-align:right;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>	
		
		<div id="perf_exec_req_tab" class="easyui-tabs" data-options="border:false" style="width:100%;height:450px;">
			<div title="프로그램 소스">
					<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="crud_flag" name="crud_flag">
					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="user_auth_id" name="user_auth_id" value="${user_auth_id}">
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd">
					<input type="hidden" id="program_div_cd" name="program_div_cd">
					<input type="hidden" id="exception_prc_status_cd" name="exception_prc_status_cd">
					<input type="hidden" id="program_id" name="program_id">
					<input type="hidden" id="excepter_id" name="excepter_id">
					<input type="hidden" id="exception_prc_dt" name="exception_prc_dt">
					<input type="hidden" id="exception_prc_status_cd_nm" name="exception_prc_status_cd_nm">
					<input type="hidden" id="exception_prc_why" name="exception_prc_why">
					<input type="hidden" id="exception_request_why_cd" name="exception_request_why_cd">
					<input type="hidden" id="exception_prc_meth_cd" name="exception_prc_meth_cd">
					
					<input type="hidden" id="exception_request_id" name="exception_request_id">
					<input type="hidden" id="exception_requester_id" name="exception_requester_id">
					<input type="hidden" id="exception_del_request_why" name="exception_del_request_why">
					<input type="hidden" id="program_type_cd" name="program_type_cd" >	
					
					<table class="detailT">
						<colgroup>	
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18%;">
						</colgroup>
						<tr>
							<th>프로그램 구분</th>
								<td>
									<input type="text" id="program_div_cd_nm" name="program_div_cd_nm" class="w290 easyui-textbox"/>
								</td>
							<th>영구 점검제외 여부</th>
								<td>
									<input type="text" id="perf_check_auto_pass_yn" name="perf_check_auto_pass_yn" class="w290 easyui-textbox"/>
								</td>
							<th>프로그램명</th>
								<td>
									<input type="text" id="program_nm" name="program_nm" class="w290 easyui-textbox"/>
								</td>
						</tr>
						<tr>
							<th>SQL식별자(DBIO)</th>
								<td>
									<input type="text" id="dbio" name="dbio" class="w290 easyui-textbox"/>
								</td>
							<th>파일명</th>
								<td>
									<input type="text" id="file_nm" name="file_nm" class="w290 easyui-textbox"/>
								</td>
							<th>디렉토리명</th>
								<td>
									<input type="text" id="dir_nm" name="dir_nm" class="w290 easyui-textbox"/>
								</td>
						</tr>
						<tr>
							<th>프로그램 유형</th>
								<td>
									<input type="text" id="program_type_cd_nm" name="program_type_cd_nm" class="w290 easyui-textbox"/>
								</td>
							<th>SQL명령 유형</th>
								<td>
									<input type="text" id="sql_command_type_cd" name="sql_command_type_cd" class="w290 easyui-textbox"/>
								</td>
							<th>다이나믹SQL 여부</th>
								<td>
									<input type="text" id="dynamic_sql_yn" name="dynamic_sql_yn" class="w290 easyui-textbox"/>
								</td>
						</tr>
						<tr>
							<th>프로그램 설명</th>
								<td colspan="5">
									<input type="text" id="program_desc" name="program_desc" class="easyui-textbox"style="width:99.8%;" readonly="true"/>
								</td>	
						</tr>
						<tr>
							<th>
								SQL TEXT<br/><br/>
								<a href="javascript:;" id="sqlCopyBtn" class="w100 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#program_source_desc">
								<i class="btnIcon fas fa-save fa-lg fa-fw"></i> SQL 복사</a><br/><br/>
								<a href="javascript:;" class="w130 easyui-linkbutton" data-options="iconCls:'icon-reload'" onClick="Btn_SetSQLFormatter();">SQL Format</a>
							</th>
							<td colspan="5" style="padding:10px;height:250px;">
								<textarea name="program_source_desc" id="program_source_desc" style="width:100%;height:100%" wrap="off" readonly="true"></textarea>
<!-- 								<textarea name="program_source_desc_temp" id="program_source_desc_temp" style="display:none;"></textarea> -->
							</td>
						</tr>
							
					</table>
				</form:form>
			</div>
			
<%-- 			<div title="프로그램점검 지표">
				<div data-options="region:'center',border:false" style="padding:5px">
					<div id="perfCheckResultList" class="easyui-panel" data-options="border:false" style="width:100%;min-height:400px;padding-top:10px;">
						<table class="noneT">
						<colgroup>	
							<col style="width:15%;">
						</colgroup>
							<tr>
								<th style="padding-bottom:6px !important;"><span style="font-size:15px;">점검제외</span>
									<span id="check_span1" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="perf_check_auto_pass_yn_chk" value="perf_check_auto_pass_yn_chk" disabled></span> 
									<span id="check_span2" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="perf_check_auto_pass_yn_chk" value="perf_check_auto_pass_yn_chk" checked disabled></span>
								</th>
								<td></td>
							</tr>
						</table>
						<table id="perfCheckResultDelTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</div> --%>
			<div title="예외 삭제 요청" style="border-bottom:1px solid;height:250px;">
				<form:form method="post" id="exec_request_form" name="exec_request_form" class="form-inline">
					
					<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>

					<table class="detailT">
						<colgroup>	
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18%;">
							<col style="width:15%;">
							<col style="width:18%;">
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
									<input type="text" id="dbio" name="dbio" class="w290 easyui-textbox" readonly="readonly"/>
								</td>
						</tr>
						<tr>
								<th>예외 요청 사유</th>
								<td>
									<select id="exception_request_why_cd" name="exception_request_why_cd" data-options="panelHeight:'auto',editable:false" class="w290 easyui-combobox" readonly="true"></select>
								</td>
								<th>예외 처리 방법</th>
								<td>
									<select id="exception_prc_meth_cd" name="exception_prc_meth_cd" data-options="panelHeight:'auto',editable:false" class="w290 easyui-combobox" readonly="true"></select>
								</td>
								<th>예외 삭제 요청자</th>
								<td>
									<input type="text" id="exception_requester" name="exception_requester" class="w290 easyui-textbox" readonly="readonly"/>
								</td>
						</tr>
						<tr>
							<th>예외 요청<br/>상세 사유</th>
								<td colspan="5"><textarea id="exception_del_request_why" name="exception_del_request_why" class="easyui-textbox" data-options="multiline:true" style="width:99.7%;height:40px" ></textarea></td>
						</tr>
					</table>
					<div class="searchBtn innerBtn2">
						<a href="javascript:;" id="btn_request" class="w80 easyui-linkbutton" onClick="Btn_Request();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 요청</a>
						<a href="javascript:;" id="btn_cancel" class="w80 easyui-linkbutton" onClick="Btn_Cancel();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 요청 취소</a>
						<a href="javascript:;" id="btn_reset" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
					</div>
				</form:form>
				
				<div id="perfCheckResultList" class="easyui-panel" data-options="border:false" style="width:100%;height:262px;padding-top:0px;">
					<table class="noneT">
					<colgroup>	
						<col style="width:15%;">
					</colgroup>
						<tr>
							<th style="padding-bottom:6px !important;"><span style="font-size:15px;">점검제외</span>
								<span id="check_span1" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="perf_check_auto_pass_yn_chk" value="perf_check_auto_pass_yn_chk" disabled></span> 
								<span id="check_span2" style="position:relative;top:-1px !important;"><input class="easyui-checkbox" id="perf_check_auto_pass_yn_chk" value="perf_check_auto_pass_yn_chk" checked disabled></span>
							</th>
							<td></td>
						</tr>
					</table>
					<table id="perfCheckResultDelTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
			<div title="예외 삭제" style="overflow-y:hidden;" >
				<iframe id="exec_delete" src="" width="100%" height="540px;" scrolling="no" overflow="text" style="border-bottom:1px solid;"></iframe>
			</div>
		</div>
		
	</div>
	<!-- contents END -->
</div>

<!-- container END -->
</body>
</html>