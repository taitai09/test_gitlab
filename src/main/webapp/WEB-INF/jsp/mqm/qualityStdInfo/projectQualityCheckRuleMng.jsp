<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.08.13	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title> 품질 점검 :: DB 구조 품질 점검 :: 프로젝트 구조 품질점검 지표/RULE 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/include/popup/project_popup.js?ver=<%=today%>"></script>
	<script type="text/javascript" src="/resources/js/ui/include/popup/project_popup_paging.js"></script>
    <script type="text/javascript" src="/resources/js/ui/mqm/qualityStdInfo/projectQualityCheckRuleMng.js?ver=<%=today%>"></script>    
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
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" />
					<input type="hidden" id="pagePerCount" name="pagePerCount" />

					<input type="hidden" id="project_id" name="project_id" />
				
					<label>프로젝트</label>
					<input type="text" id="project_nm" name="project_nm" value="" class="w350 easyui-textbox"/>
					
					<label>품질점검 지표코드</label>
					<select id="qty_chk_idt_cd" name="qty_chk_idt_cd" data-options="panelHeight:'220',editable:false" required="true" class="w200 easyui-combobox"></select>
						
					<label>적용여부</label>
					<select id="apply_yn" name="apply_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">적용</option>
						<option value="N">미적용</option>
					</select>
						
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>	
					<div class="searchBtn">
						<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_ApplyStandardProject();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 프로젝트 표준 적용</a>
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
				<input type="hidden" id="project_id" name="project_id" />
				
				
				<table class="detailT">
					<colgroup>	
						<col style="width:14%;">
						<col style="width:18%;">
						<col style="width:14%;">
						<col style="width:18%;">
						<col style="width:14%;">
						<col style="width:18%;">
					</colgroup>
					<tr>
						<th>적용여부</th>
						<td>
						<select id="apply_yn" name="apply_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
							<option value="">선택</option>
							<option value="Y">적용</option>
							<option value="N">미적용</option>
						</select>						
						</td>
						<th>프로젝트</th>
						<td>
							<input type="text" id="project_nm" name="project_nm" class="w300 easyui-textbox"/>
						</td>
						<th>모델링 단계</th>
						<td>
							<select id="mdi_pcs_cd" name="mdi_pcs_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>
						</td>
					</tr>
					<tr>
						<th>품질점검 지표코드</th>
						<td>
							<input type="text" id="qty_chk_idt_cd" name="qty_chk_idt_cd" class="w200 easyui-textbox"/>
						</td>
						<th>품질점검 지표명</th>
						<td>
							<input type="text" id="qty_chk_idt_nm" name="qty_chk_idt_nm" class="w220 easyui-textbox"/>
						</td>
						<th>품질지표 유형</th>
						<td>
							<select id="qty_idt_tp_cd" name="qty_idt_tp_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>
						</td>	
					</tr>
					<tr>
						
						<th>품질점검 대상여부</th>
						<td>
							<select id="qty_chk_tg_yn" name="qty_chk_tg_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
						<th>품질점검 유형</th>
						<td>
							<select id="qty_chk_tp_cd" name="qty_chk_tp_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select>
						</td>
						<th>정렬순서</th>
						<td>
							<input type="number" id="srt_ord" name="srt_ord" class="w200 easyui-textbox"/>
						</td>
					</tr>
					<tr>
					<th>품질점검내용</th>
						<td colspan="5">
							<input id="qty_chk_cont" name="qty_chk_cont" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:60px" ></textarea>
						</td>
					</tr>
					<tr>
					<th>해결방안내용</th>
						<td colspan="5">
							<input id="slv_rsl_cont" name="slv_rsl_cont" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:60px" ></textarea>
						</td>
					</tr>
					<tr>
						<th>품질점검 RULE
							<br/><br/>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">RULE 복사</a>
						</th>
						<td colspan="5" style="vertical-align:top;">
							<textarea name="qty_chk_sql" id="qty_chk_sql" rows
							="10" cols="350" style="overlow:scroll;margin-top:3px;padding:5px;width:98%;height:180px" wrap="off"></textarea>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> <span id="btn_save">저장</span></a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/project_popup.jsp" %>
</body>
</html>