<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>품질 점검 :: 실행기반 SQL 표준 일괄 점검 :: SQL 품질점검 예외 대상 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlStdChk/maintainQualityCheckException.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<style>
		.manageException .searchOption {
			margin-bottom: 15px;
		}
	</style>
</head>
<body style="visibility:hidden;">
<div id="container">
	<div id="contents" class="manageException">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="well searchOption">
				<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
				<!-- 이전, 다음 처리 -->
				<input type="hidden" id="currentPage" name="currentPage" value="${sqlStandards.currentPage}">
				<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlStandards.pagePerCount}">
				
				<label>품질 점검 지표 코드</label>
				<select id="search_qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220px',editable:false" class="w200 easyui-combobox"></select>
				
				<label style="margin-left:12px;">표준점검DB</label>
				<select id="search_dbid" name="dbid" data-options="panelHeight:'220px',editable:false" class="w200 easyui-combobox"></select>
				
				<label style="margin-left:25px;">업무</label>
				<select id="search_wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'300px',editable:false" class="w200 easyui-combotree" value="${sqlStandards.wrkjob_cd}"></select>
				
				<div class="multi">
					<label>SQL 식별자(DBIO)</label>
					<input type="text" id="search_dbio" name="dbio" class="w480 easyui-textbox">
					
					<label>SQL ID</label>
					<input type="text" id="search_sql_id" name="sql_id" class="easyui-textbox" style="width:199px;">
					
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Upload();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 일괄 업로드</a>
					</div>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="border:false" style="width:100%; height:305px;">
				<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:100%;">
					<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:200px;margin-top:2px;">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<table class="detailT">
					<colgroup>	
						<col style="width:14%;">
						<col style="width:22%;">
						<col style="width:14%;">
						<col style="width:50%;">
					</colgroup>
					<tr>
						<th>품질 점검 지표 코드</th>
						<td>
							<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'300px',editable:true, prompt:'선택'" class="easyui-combobox" style="width:90%;" required></select>
						</td>
						<th>표준점검DB</th>
						<td>
							<select id="std_qty_target_db_name" value="" class="w200 easyui-combobox" data-options="panelHeight:'auto', editable:false, prompt:'선택'"></select>
							<input type="hidden" id="dbid" name="dbid" value="">
						</td>
					</tr>
					<tr>
						<th>SQL ID</th>
						<td>
							<input type="text" id="sql_id" name="sql_id" class="easyui-textbox" style="width:90%;">
						</td>
						<th>SQL 식별자(DBIO)</th>
						<td>
							<input type="text" id="dbio" name="dbio" class="easyui-textbox" style="width:100%;">
						</td>
					</tr>
					</tr>
						<th>업무</th>
						<td>
							<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'300px',editable:false, prompt:'선택'" class="easyui-combotree" style="width:90%;"></select>
						</td>
						<th>디렉토리명</th>
						<td>
							<input type="text" id="dir_nm" name="dir_nm" class="easyui-textbox" style="width:100%;">
						</td>
					</tr>
					<tr>
						<th>요청자</th>
						<td>
							<input type="text" id="requester" name="requester" class="easyui-textbox" style="width:90%;">
						</td>
						<th>등록자</th>
						<td>
							<input type="text" id="user_id" name="user_id" value="${user_id}" class="w200 easyui-textbox" readonly>
						</td>
					</tr>
					<tr>
						<th>예외사유</th>
						<td colspan="3">
							<input id="except_sbst" name="except_sbst" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:40px"></textarea>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" id="btnSave" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
			
			<form:form method="post" id="excel_form" name="submit_form" class="form-inline">
				<input type="hidden" name="qty_chk_idt_cd" value="">
				<input type="hidden" name="dbid" value="">
				<input type="hidden" name="wrkjob_cd" value="">
				<input type="hidden" name="dbio" value="">
				<input type="hidden" name="sql_id" value="">
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/maintainQualityCheckException_excel_upload.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>