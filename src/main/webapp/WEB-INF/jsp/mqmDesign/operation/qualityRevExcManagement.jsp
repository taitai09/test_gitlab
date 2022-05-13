<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2020.04.28	명성태	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title> 구조 품질검토 예외 대상 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/mqmDesign/operation/qualityRevExcManagement.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/include/popup/qualityRevExcMng_excel_upload.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					<label>품질점검 지표코드</label>
					<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'300',editable:true" class="w200 easyui-combobox"></select>
					
					<label>예외대상 객체구분</label>
					<select id="obj_type_cd" name="obj_type_cd" data-options="panelHeight:'300',editable:true" class="w120 easyui-combobox"></select>
					
					<label>라이브러리명</label>
					<select id="lib_nm" name="lib_nm" data-options="panelHeight:'300',editable:true" class="w120 easyui-combobox"></select>
					
					<label>모델명</label>
					<select id="model_nm" name="model_nm" data-options="panelHeight:'300',editable:true" class="w120 easyui-combobox"></select>
					
					<label>주제영역명</label>
					<select id="sub_nm" name="sub_nm" data-options="panelHeight:'300',editable:true" class="w120 easyui-combobox"></select>
					
					<div class="multi">
						<label>엔터티명</label>
						<input type="text" id="ent_nm" name="ent_nm" class="w200 easyui-textbox"/>
						
						<label>속성명</label>
						<input type="text" id="att_nm" name="att_nm" class="w200 easyui-textbox"/>
						
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
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>	
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
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
						<th>품질점검 지표코드</th>
						<td colspan="3">
							<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'300',editable:false" class="w250 easyui-combobox"></select>
							<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" class="w400 easyui-textbox" readonly="true"/>
						</td>

						<th>예외대상 객체구분</th>
						<td>
							<select id="obj_type_cd" name="obj_type_cd" data-options="panelHeight:'300',editable:false" class="w250 easyui-combobox"></select>
						</td>	
					</tr>
					<tr>
						<th>라이브러리명</th>
						<td>
							<select id="lib_nm" name="lib_nm" data-options="panelHeight:'300',editable:false" class="w250 easyui-combobox"></select>
						</td>
						<th>모델명</th>
						<td>
							<select id="model_nm" name="model_nm" data-options="panelHeight:'300',editable:false" class="w250 easyui-combobox"></select>
						</td>
						<th>주제영역명</th>
						<td>
							<select id="sub_nm" name="sub_nm" data-options="panelHeight:'300',editable:false" class="w250 easyui-combobox"></select>
						</td>
					</tr>
					<tr>
						<th>엔터티명</th>
						<td>
							<input type="text" id="ent_nm" name="ent_nm" class="w250 easyui-textbox"/>
						</td>
						<th>속성명</th>
						<td>
							<input type="text" id="att_nm" name="att_nm" class="w250 easyui-textbox"/>
						</td>
						<th>요청자</th>
						<td>
							<input type="text" id="rqpn" name="rqpn" class="w250 easyui-textbox"/>
						</td>
					</tr>
					<tr>
						<th>비    고</th>
						<td colspan="5">
							<input id="remark" name="remark" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:30px" ></textarea>
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
<%@include file="/WEB-INF/jsp/include/popup/qualityRevExcMng_excel_upload.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>