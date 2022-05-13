<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.13	임승률	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title> 성능개선 :: 데이터구조품질 :: 구조 품질점검 SQL 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/mqmDesign/setting/qualityCheckSql.js?ver=<%=today%>"></script>    
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
					<input type="hidden" id="currentPage" name="currentPage" value="${BusinessClassMng.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${BusinessClassMng.pagePerCount}"/>
					
					<label>품질점검지표코드</label>
					<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220',editable:false" required="true" class="w200 easyui-combobox"></select>
					<label>DML여부</label>
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
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:300px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<table class="detailT">
					<colgroup>
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:52%;">
					</colgroup>
					<tr>
						<th>품질점검지표코드</th>
						<td class="ltext"><select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220',editable:false" class="w200 easyui-combobox"></select></td>
						<th>DML여부</th>
						<td><select id="dml_yn" name="dml_yn" data-options="panelHeight:'auto',editable:false" class="w60 easyui-combobox">
							<option value="">전체</option>
							<option value="Y">Y</option>
							<option value="N">N</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>품질점검 RULE
							<br/><br/>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">RULE 복사</a>
						</th>
						<td colspan="3" style="vertical-align:top;">
							<textarea name="qty_chk_sql" id="qty_chk_sql" rows="10" cols="350" style="overlow:scroll;margin-top:3px;padding:5px;width:98%;height:150px" wrap="off"></textarea>
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
<!-- container END -->
</body>
</html>