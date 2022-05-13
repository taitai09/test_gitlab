<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.01.03	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 :: 성능 검증 단계 관리 :: 성능 검증 현황</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/situation/perfCheckState.js?ver=<%=today%>"></script>
    
	<style>
.tabs-wrap {
	background-color: #ffffff;
}

.tabs-header {
	border: #ffffff;
	background-color: #ffffff;
}

#container {
	height: 1000px;
}

.title {
	margin-top: 6px;
	margin-bottom: 8px;
	height: 20px !important;
}
.easyui-panel.searchArea .title {
	margin-bottom: 6px;
	height: 20px !important;
}

#sub_title {
	margin-top: 10px; margin-left : 3px;
	font-size: 13px;
	margin-left: 3px;
}
#container {
	height:600px; !important;
}

</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">

		<div class="title">
			<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i>${menu_nm}</span>
		</div>	
					
					
		<div id="perf_exec_req_tab" class="easyui-tabs" data-options="border:false" style="width:100%;height:720px;">
		<div title="CM 검증 현황" style="border:0px;">
				<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
					<div class="title" style="border:0px;">
						<span id="sub_title" class="h3">▶ CM 검증 현황</span>
					</div>					
					<div class="well">
						<form:form method="post" id="submit_form0" name="submit_form0" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							<input type="hidden" id="user_id" name="user_id" value="${user_id}">
							<!-- 이전, 다음 처리 -->
					
							<label>업무</label>
							<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree" required="required"></select>
							
							<label>검색조건</label>
							<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.searchKey}">
								<option value="">전체</option>
								<option value="01">배포요청일시</option>
								<option value="02">검증완료일시</option>
								<option value="03">배포예정일시</option>
							</select>
							
							<input type="text" id="search_from_deploy_request_dt" name="search_from_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${startDate}" /> ~
							<input type="text" id="search_to_deploy_request_dt" name="search_to_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${nowDate}" />
														
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('0');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('0');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
						</form:form>
					</div>
				</div>
				
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:570px;margin-bottom:10px;border:0px;">
					<table id="tableList0" class="tbl easyui-datagrid" data-options="fit:true,border:false" style="border:0px;">
						<tbody><tr><td>&nbsp;</td></tr></tbody>
					</table>
				</div>
			</div>
		<div title="프로그램 검증 현황">
				<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
					<div class="title" style="border:0px;">
						<span id="sub_title" class="h3">▶ 프로그램 검증 현황</span>
					</div>					
					<div class="well">
						<form:form method="post" id="submit_form1" name="submit_form1" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							
							<input type="hidden" id="user_id" name="user_id" value="${user_id}">
							<!-- 이전, 다음 처리 -->
							
							<label>업무</label>
							<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree" required="required"></select>
							
							<label>검색조건</label>
							<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.searchKey}">
								<option value="">전체</option>
								<option value="01">배포요청일시</option>
								<option value="02">검증완료일시</option>
								<option value="03">배포예정일시</option>
							</select>
							
							<input type="text" id="search_from_deploy_request_dt" name="search_from_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${startDate}" /> ~
							<input type="text" id="search_to_deploy_request_dt" name="search_to_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${nowDate}" />
														
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('1');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('1');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
						</form:form>
					</div>
				</div>
				
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:570px;margin-bottom:10px;">
					<table id="tableList1" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
			<div title="지표별 부적합 현황">
				<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
					<div class="title" style="border:0px;">
						<span id="sub_title" class="h3">▶ 지표별 부적합 현황</span>
					</div>
					<div class="well">
						<form:form method="post" id="submit_form2" name="submit_form2" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							
							<input type="hidden" id="user_id" name="user_id" value="${user_id}">
							<!-- 이전, 다음 처리 -->
							
							<label>업무</label>
							<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree" required="required"></select>
							
							<label>검색조건</label>
							<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" value="${deployPerfChkIndc.searchKey}">
								<option value="">전체</option>
								<option value="01">배포요청일시</option>
								<option value="02">검증완료일시</option>
								<option value="03">배포예정일시</option>
							</select>
							
							<input type="text" id="search_from_deploy_request_dt" name="search_from_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${startDate}" /> ~
							<input type="text" id="search_to_deploy_request_dt" name="search_to_deploy_request_dt" data-options="panelHeight:'auto',editable:false" class="w130 datapicker easyui-datebox" required="required" value="${nowDate}" />
							
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('2');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('2');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
						</form:form>
					</div>
				</div>
				
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:570px;margin-bottom:10px;">
					<table id="tableList2" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
			<div title="성능 검증 예외 처리 현황">
				<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
					<div class="title" style="border:0px;">
						<span id="sub_title" class="h3">▶ 성능 검증 예외 처리 현황</span>
					</div>
					<div class="well">
						<form:form method="post" id="submit_form3" name="submit_form3" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							
							<input type="hidden" id="user_id" name="user_id" value="${user_id}">
							<!-- 이전, 다음 처리 -->
							
							<label>업무</label>
							<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true,required:true" class="w150 easyui-combotree"></select>
							
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('3');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('3');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
						</form:form>
					</div>
				</div>
				
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:570px;margin-bottom:10px;">
					<table id="tableList3" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
			<div title="성능 검증 예외 처리 사유별 현황">
				<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
					<div class="title" style="border:0px;">
						<span id="sub_title" class="h3">▶ 성능 검증 예외 처리 사유별 현황</span>
					</div>					
					<div class="well">
						<form:form method="post" id="submit_form4" name="submit_form4" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							
							<input type="hidden" id="user_id" name="user_id" value="${user_id}">
							<!-- 이전, 다음 처리 -->
							
							<label>업무</label>
							<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'200px',editable:true,required:true" class="w150 easyui-combotree"></select>
							
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick('4');"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
							</span>
							<div class="searchBtn">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download('4');"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</div>
						</form:form>
					</div>
				</div>
				
				<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:570px;margin-bottom:10px;">
					<table id="tableList4" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
		</div>
	<!-- contents END -->
	</div>
<!-- container END -->
</div>
</body>
</html>