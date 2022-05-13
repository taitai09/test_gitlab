<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.01.03	임호경	최초작성
 * 2021.08.20	황예지	용어변경
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능 검증 :: 성능 검증 지표 관리 :: 업무별 성능 검증 지표 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />	
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/setting/wjPerfChkIndc.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="search_wrkjob_cd" name="search_wrkjob_cd" value="">
					<label>업무</label>
					<select id="upper_wrkjob_cd" name="upper_wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w200 easyui-combotree"  required="true"></select>
					<label>적용여부</label>
					<select id="search_indc_apply_yn" name="search_indc_apply_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">적용</option>
						<option value="N">미적용</option>
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
						<col style="width:19%;">
					</colgroup>
					<tr>
						<th>업무</th>
						<td>
							<select id="temp_wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'200px',editable:true" class="w150 easyui-combotree" readonly="true" required="required"></select>
						</td>
						<th>검증 지표</th>
						<td>
							<select id="perf_check_indc_id" name="perf_check_indc_id" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" readonly="true" required="required"></select>
						</td>
						<th>프로그램</th>
						<td>
							<select id="perf_check_program_div_cd" name="perf_check_program_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" required="required"></select>
						</td>
					</tr>
					<tr>
						<th>검증 방법</th>
						<td>
							<select id="perf_check_meth_cd" name="perf_check_meth_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" readonly="true" required="required"></select>
						</td>
						<th>적합</th>
						<td>
							<input type="number" id="pass_max_value" name="pass_max_value" class="w150 easyui-textbox" required="required"/>
						</td>
						<th>부적합</th>
						<td>
							<input type="text" id="not_pass_pass" name="not_pass_pass" class="w150 easyui-textbox" readonly="readonly"/>
						</td>
					</tr>
					<tr>
						<th>여부값 판정구분</th>
						<td>
							<select id="yn_decide_div_cd" name="yn_decide_div_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required"></select>
						</td>
						<th>지표적용여부</th>
						<td>
							<select id="indc_apply_yn" name="indc_apply_yn" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
								<option value=""></option>
								<option value="Y">적용</option>
								<option value="N">미적용</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>지표 설명</th>
						<td colspan="5"><textarea id="perf_check_indc_desc" name="perf_check_indc_desc" style="width:100%;height:40px" readonly="true"></textarea></td>
					</tr>
					<tr>
						<th>부적합 가이드</th>
						<td colspan="5"><textarea id="perf_check_fail_guide_sbst" name="perf_check_fail_guide_sbst" style="width:100%;height:40px" readonly="true"></textarea></td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i><span id="Btn_SaveSetting"> 등록</span></a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetAllField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>