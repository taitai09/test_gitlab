<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.27	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title> SQL 품질점검 지표 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
	<meta http-equiv="cleartype" content="on"/>
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp"%>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/rulesForWritingSqlQueries_popup.js?ver<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandards/projectSqlQtyChkRuleMng.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchArea" data-options="border:false" style="width:100%;">
			<div class="title">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp"%>
					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="project_id" name="project_id"/>

					<label>프로젝트</label>
					<input type="text" id="project_nm" name="project_nm" value="" class="w250 easyui-textbox"/>
					<label>적용여부</label>
					<span>
							<select id="apply_yn" name="apply_yn" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
								<option value="">전체</option>
								<option value="Y">적용</option>
								<option value="N">미적용</option>
							</select>
					</span>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w180 easyui-linkbutton" onClick="Btn_PrjtStdRuleApply();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 프로젝트 표준 RULE 적용</a>
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:310px;">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="project_id" name="project_id" />
				<input type="hidden" id="crud_flag" name="crud_flag" value=""/>
				
				<table class="detailT">
					<colgroup>
						<col style="width:5%;">
						<col style="width:5%;">
						<col style="width:5%;">
						<col style="width:15%;">
						<col style="width:5%;">
						<col style="width:10%;">
						<col style="width:5%;">
						<col style="width:10%;">
						<col style="width:5%;">
						<col style="width:5%;">
					</colgroup>
					<tr>
						<th>적용여부</th>
						<td>
							<select id="apply_yn" name="apply_yn" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">적용</option>
								<option value="N">미적용</option>
							</select>
						</td>
						<th>프로젝트</th>
						<td>
							<input type="text" id="project_nm" name="project_nm" value="" class="w250 easyui-textbox"/>
						</td>	
						<th>품질 점검 지표 코드</th>
						<td>
<!-- 							<input type="text" id="qty_chk_idt_cd" name="qty_chk_idt_cd" class="w220 easyui-textbox"/> -->
							<input type="text" id="qty_chk_idt_cd" name="qty_chk_idt_cd" class="w220 easyui-combobox"/>
						</td>	
						<th>품질 점검 지표명</th>
						<td>
							<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" class="w150 easyui-textbox"/>
						</td>
						<th>DML 여부</th>
						<td>
							<select id="dml_yn" name="dml_yn" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>품질 점검 RULE
							<br/><br/>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="copy_to_clipboard();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> RULE 복사</a>
							<br><br/>
							<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_OpenPopup();">RULE 작성 규칙</a>
						</th>
						<td colspan="9" style="vertical-align:top;">
							<textarea name="qty_chk_sql" id="qty_chk_sql" rows="10" cols="350" style="overlow:scroll;margin-top:3px;padding:5px;width:98%;height:187px" wrap="off"></textarea>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw regist"></i> 등록</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/rulesForWritingSqlQueries_popup.jsp" %>
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>