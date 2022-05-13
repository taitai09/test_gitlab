<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.12	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: 일예방점검 설정관리 :: DB점검 예외내역</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/performanceCheckMng/performanceCheckException.js?ver=<%=today%>"></script>
</head>
<body>
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

					<label>검색 조건</label>
					<select id="select_dbid" name="select_dbid" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox" placeholder="점검설정명을 입력해주세요."/>
					<label>사용여부</label>
					<select id="use_yn" name="use_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>							
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
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
						<th>DB</th>
						<td>
							<select id="dbid" name="dbid" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox"></select>
						</td>
						<th>점검설정ID</th>
						<td><input type="text" id="check_pref_id" name="check_pref_id" class="w150 easyui-textbox" readonly="readonly"/></td>
						<th>점검설정명</th>
						<td>
							<div id="div_check_pref_nm1" style="display:block">
								<input type="text" id="check_pref_nm" name="check_pref_nm" class="w150 easyui-textbox"/>
							</div>
							<div id="div_check_pref_nm2" style="display:none">
								<select id="check_pref_nm2" name="check_pref_nm" data-options="panelHeight:'300',editable:false" class="w150 easyui-combobox"></select>
							</div>
						</td>
					</tr>
					<tr>
						<th>점검활성화여부</th>
						<td>
							<select id="check_enable_yn" name="check_enable_yn" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
						</td>
						<th>점검값단위</th>
						<td>
							<select id="check_value_unit" name="check_value_unit" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
								<option value="">선택</option>
								<option value="count">count</option>
								<option value="percent">percent</option>
							</select>
						</td>
						<th>임계값</th>
						<td><input type="number" id="default_threshold_value" name="default_threshold_value" class="w150 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>점검등급코드명</th>
						<td>
							<select id="check_grade_cd" name="check_grade_cd" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
							</select>
						</td>
						<th>점검클래스구분코드명</th>
						<td>
							<select id="check_class_div_cd" name="check_class_div_cd"  data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
							</select>								
																						
						</td>
						<th>긴급조치대상여부</th>
						<td>
							<select id="emergency_action_yn" name="emergency_action_yn" data-options="panelHeight:'auto'" class="w150 easyui-combobox">
								<option value="">선택</option>
								<option value="Y">사용</option>
								<option value="N">미사용</option>
							</select>
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