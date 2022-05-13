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
	<title>성능개선 :: 데이터 구조 품질 :: 구조 품질점검 지표 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/mqmDesign/setting/qualityCheckManagement.js?ver=<%=today%>"></script>
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

					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					<label>모델링 단계</label>
					<select id="mdi_pcs_cd" name="mdi_pcs_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>
					
					<label>품질지표 유형</label>
					<select id="qty_chk_tp_cd" name="qty_chk_tp_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>
					
					<label>품질점검 지표명</label>
					<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" class="w200 easyui-textbox"/>
					
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
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<table class="detailT">
					<colgroup>	
						<col style="width:12%;">
						<col style="width:16%;">
						<col style="width:12%;">
						<col style="width:16%;">
						<col style="width:12%;">
						<col style="width:16%;">
						<col style="width:12%;">
						<col style="width:16%;">
					</colgroup>
					<tr>
						<th>모델링 단계</th>
						<td>
							<select id="mdi_pcs_cd" name="mdi_pcs_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox"></select>
						</td>
						<th>품질점검 지표코드</th>
						<td>
							<input type="text" id="qty_chk_idt_cd" name="qty_chk_idt_cd" required="true" class="w200 easyui-textbox"/>
						</td>
						<th>품질점검 지표명</th>
						<td colspan="3">
							<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" required="true" class="w620 easyui-textbox"/>
						</td>	
					</tr>
					<tr>
						<th>품질지표 유형</th>
						<td>
							<select id="qty_idt_tp_cd" name="qty_idt_tp_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox"></select>
						</td>
						<th>품질점검 대상여부</th>
						<td>
							<select id="qty_chk_tg_yn" name="qty_chk_tg_yn" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox">
								<option value=""></option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
						<th>품질점검 유형</th>
						<td>
							<select id="qty_chk_tp_cd" name="qty_chk_tp_cd" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox"></select>
						</td>
						<th>정렬순서</th>
						<td>
							<input type="number" id="srt_ord" name="srt_ord" class="w200 easyui-textbox"/>
						</td>
					</tr>
					<tr>
					<th>품질점검내용</th>
						<td colspan="7">
							<input id="qty_chk_cont" name="qty_chk_cont" rows="30" data-options="multiline:true" required="true" class="easyui-textbox" style="width:100%;height:50px" ></textarea>
						</td>
					</tr>
					<tr>
					<th>해결방안내용</th>
						<td colspan="7">
							<input id="slv_rsl_cont" name="slv_rsl_cont" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:50px" ></textarea>
						</td>
					</tr>
					<tr>
					<th>품질점검결과 테이블명</th>
					<td>
						<input type="text" id="qty_chk_result_tbl_nm" name="qty_chk_result_tbl_nm" class="w200 easyui-textbox"/>
					</td>
					<th>엑셀출력 대상여부</th>
					<td>
						<select id="excel_output_yn" name="excel_output_yn" data-options="panelHeight:'auto',editable:false" required="true" class="w200 easyui-combobox">
							<option value=""></option>
							<option value="Y">Y</option>
							<option value="N">N</option>
						</select>
					</td>
					<th>엑셀출력 시작행</th>
					<td>
						<input type="number" id="output_start_row" name="output_start_row" class="w200 easyui-textbox"/>
					</td>
					<th>엑셀출력 시작열</th>
					<td>
						<input type="number" id="output_start_col" name="output_start_col" class="w200 easyui-textbox"/>
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
</body>
</html>