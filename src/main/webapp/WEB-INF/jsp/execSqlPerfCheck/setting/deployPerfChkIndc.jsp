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
	<title>성능 검증 :: 성능 검증 지표 관리 :: 성능 검증 지표 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
	<meta http-equiv="cleartype" content="on" />	
	<%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
	<script type="text/javascript" src="/resources/js/ui/execSqlPerfCheck/setting/deployPerfChkIndc.js?ver=<%=today%>"></script>
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
					
					
					<label>검증 지표</label>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<label>사용여부</label>
					<select id="search_use_yn" name="search_use_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
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
				<input type="hidden" id="perf_check_indc_id" name="perf_check_indc_id" />
				<input type="hidden" id="old_perf_check_meth_cd" name="old_perf_check_meth_cd" />
					<input type="hidden" id="old_indc_use_yn" name="old_indc_use_yn" value="">
			
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
						<th>검증 지표</th>
						<td>
							<input type="text" id="perf_check_indc_nm" name="perf_check_indc_nm" class="w200 easyui-textbox" required="required"/>
						</td>	
						<th>검증 방법</th>
							<td>
								<select id="perf_check_meth_cd" name="perf_check_meth_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combotree" required="required"></select>
							</td>
						<th>사용여부</th>
							<td>
								<select id="indc_use_yn" name="indc_use_yn" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required="required">
									<option value=""></option>
									<option value="Y">사용</option>
									<option value="N">사용안함</option>
								</select>
							</td>
					</tr>
					<tr>
						<th >지표 설명</th>
							<td colspan="5"><textarea id="perf_check_indc_desc" name="perf_check_indc_desc" style="width:100%;height:70px" class="validatebox-invalid" ></textarea></td>
					</tr>
					<tr>
						<th>부적합 가이드</th>
							<td colspan="5"><textarea id="perf_check_fail_guide_sbst" name="perf_check_fail_guide_sbst" style="width:100%;height:70px" class="validatebox-invalid" ></textarea></td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
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