<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.28	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>SQL 품질점검 SQL 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
	<meta http-equiv="cleartype" content="on"/>
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/rulesForWritingSqlQueries_popup.js?ver<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/sqlStandards/maintainQualityCheckSql.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
			<div class="well">
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${sqlStandards.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${sqlStandards.pagePerCount}"/>
					
					<label>품질 점검 지표 코드</label>
					<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220',editable:false" required="true" class="w200 easyui-combobox"></select>
					<label>DML 여부</label>
						<select id="dml_yn" name="dml_yn" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox">
							<option value="">전체</option>
							<option value="Y">Y</option>
							<option value="N">N</option>
						</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>	
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
<!-- 		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;"> -->
<!-- 			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a> -->
<!-- 			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a> -->
<!-- 			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a> -->
<!-- 			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a> -->
<!-- 		</div> -->
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:310px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="rowStatus" name="rowStatus"/>
				<input type="hidden" id="qty_chk_idt_cd" name="qty_chk_idt_cd"/>
				<table class="detailT">
					<colgroup>
						<col style="width:5%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:12%;">
						<col style="width:15%;">
						<col style="width:12%;">
					</colgroup>
					<tr >
						<th>품질 점검 지표 코드</th>
						<td class="ltext"><select id="ui_qty_chk_idt_cd" name="ui_qty_chk_idt_cd" data-options="panelHeight:'220',editable:false,required:true" required="true" class="w200 easyui-combobox"></select></td>
						<th>DML 여부</th>
						<td><select id="dml_yn" name="dml_yn" data-options="panelHeight:'auto',editable:false,required:true" required="true" class="w60 easyui-combobox">
							<option value="" disabled ></option>
							<option value="Y">Y</option>
							<option value="N">N</option>
							</select>
						</td>
						<th>프로젝트 단위 관리 여부</th>
						<td><select id="project_by_mgmt_yn" name="project_by_mgmt_yn" data-options="panelHeight:'auto',editable:false,required:true" required="true" class="w60 easyui-combobox">
							<option value="" disabled ></option>
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
						<td colspan="5" style="vertical-align:top;">
							<textarea name="qty_chk_sql" id="qty_chk_sql" rows="10" style="overlow:scroll;margin-top:3px;padding:5px;width:98%;height:135px;" required wrap="off" placeholder="품질 점검 RULE을 입력하세요." class="validatebox-invalid "></textarea>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_DeleteInstance();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<%@include file="/WEB-INF/jsp/include/popup/rulesForWritingSqlQueries_popup.jsp" %>
<!-- container END -->
</body>
</html>