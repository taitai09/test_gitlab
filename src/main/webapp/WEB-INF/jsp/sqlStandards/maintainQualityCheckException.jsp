<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.06.03	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 품질검토 예외 대상 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/sqlStandards/maintainQualityCheckException.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/paging.js"></script><!-- 그리드 페이징, 이전/다음버튼 처리 -->
	<script type="text/javascript" src="/resources/js/ui/include/popup/maintainQualityCheckException_excel_upload.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
			<div class="easyui-panel searchAreaMulti" data-options="border:false" style="width:100%;">
				<div class="title">
					<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
				</div>
				<div class="well">
						<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
						<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
						<input type="hidden" id="wrkjob_cd" name="wrkjob_cd"/>
						
						<!-- 이전, 다음 처리 -->
						<input type="hidden" id="currentPage" name="currentPage" value="${sqlStandards.currentPage}"/>
						<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlStandards.pagePerCount}"/>
						
						<label>품질 점검 지표 코드</label>
						<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220px',editable:false" class="w200 easyui-combobox"></select>
						<label>업무</label>
						<select id="search_wrkjob_cd" name="search_wrkjob_cd" data-options="panelHeight:'300px',editable:true" class="w200 easyui-combotree" value="${sqlStandards.wrkjob_cd}"></select>
						
						<div class="multi">
							<label>SQL 식별자(DBIO)</label>
							<input type="text" id="dbio" name="dbio" class="easyui-textbox" style="width:50%;"/>
							
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
			</div>
			<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:300px;margin-bottom:10px;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
				<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</form:form>
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:280px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
				<input type="hidden" id="wrkjob_cd" name="wrkjob_cd"/>
				<input type="hidden" id="rowStatus" name="rowStatus"/>
				<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/>
				
				<table class="detailT">
					<colgroup>	
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:15%;">
						<col style="width:25%;">
					</colgroup>
					<tr>
						<th>품질 점검 지표 코드</th>
						<td colspan="3">
							<select id="bottom_qty_chk_idt_cd" name="bottom_qty_chk_idt_cd" data-options="panelHeight:'220px',editable:false" class="w200 easyui-combobox"></select>
							<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" data-options="disabled:true" class="w400 easyui-textbox"/>
						</td>
						<th>업무</th>
						<td>
							<select id="bottom_search_wrkjob_cd" name="bottom_search_wrkjob_cd" data-options="panelHeight:'300px',editable:true" class="w200 easyui-combotree"></select>
						</td>
					</tr>
					<tr>
						<th>디렉토리명</th>
						<td colspan="5">
							<input type="text" id="dir_nm" name="dir_nm" class="easyui-textbox" style="width:100%;"/>
						</td>
					</tr>
					<tr>
						<th>SQL 식별자(DBIO)</th>
						<td colspan="5">
							<input type="text" id="dbio" name="dbio" class="easyui-textbox" style="width:100%;"/>
						</td>
					</tr>
					<tr>
						<th>요청자</th>
						<td colspan="2">
							<input type="text" id="requester" name="requester" class="w300 easyui-textbox"/>
						</td>
					</tr>
					<tr>
						<th>예외사유</th>
						<td colspan="5">
							<input id="except_sbst" name="except_sbst" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:80px" ></textarea>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/maintainQualityCheckException_excel_upload.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>