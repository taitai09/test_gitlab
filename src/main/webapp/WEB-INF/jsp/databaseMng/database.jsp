<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.12.08	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: OPEN-POP 설정 :: 데이터베이스 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/include/popup/color_popup.js?ver<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/databaseMng/database.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="currentPage" name="currentPage" value="${database.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${database.pagePerCount}"/>
					<input type="hidden" id="rgb_color_id" name="rgb_color_id"/>
					
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox" required="required">
						<option value="">전체</option>
						<option value="01">DBID</option>
						<option value="02">DB명</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
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
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding:10px 0px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="rgb_color_id" name="rgb_color_id"/>
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<input type="hidden" id="old_db_name" name="old_db_name" value=""/>
				<table class="detailT">
					<colgroup>	
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:19%;">
						<col style="width:15%;">
						<col style="width:19%;">
					</colgroup>
					<tr>
						<th>DBID</th>
						<td><input type="number" id="ui_dbid" name="ui_dbid" class="w200 easyui-textbox" required="required"/></td>
						<th>DB명</th>
						<td><input type="text" id="db_name" name="db_name" class="w200 easyui-textbox" required="required"/></td>
						<th>DB 한글명</th>
						<td><input type="text" id="db_abbr_nm" name="db_abbr_nm" class="w200 easyui-textbox" required="required"/></td>
						<th>Exadata여부</th>
						<td>
							<select id="exadata_yn" name="exadata_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required">
								<option value=""></option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>운영유형</th>
						<td>
							<select id="db_operate_type_cd" name="db_operate_type_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required"></select>
						</td>
						<th>CHARACTERSET</th>
						<td><input type="text" id="characterset" name="characterset" class="w200 easyui-textbox" required="required"/></td>
						<th>컬러</th>
						<td>
							<a href="javascript:;" class="w70 easyui-linkbutton" onClick="update();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 색상</a>
							<input type="text" id="colorDiv" name="colorDiv" data-options="disabled:false" class="w120 easyui-textbox" style="text-align:center"/>
						</td>
						<th>컬렉터인스턴스</th>
						<td><input type="number" id="collect_inst_id" name="collect_inst_id" class="w200 easyui-textbox" required="required"/></td>
					</tr>
					<tr>
						
						<th>수집인스턴스</th>
						<td><input type="number" id="gather_inst_id" name="gather_inst_id" class="w200 easyui-textbox" required="required"/></td>
						<th>정렬 순서</th>
						<td><input type="number" id="ordering" name="ordering" class="w200 easyui-textbox" required="required"/></td>
						<th>사용여부</th>
						<td>
							<select id="use_yn" name="use_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required">
								<option value=""></option>
								<option value="Y">Y</option>
								<option value="N">N</option>
							</select>
						</td>
						<th>DBMS 종류</th>
						<td>
							<select id=database_kinds_cd name="database_kinds_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="required"></select>
						</td>
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
<%@include file="/WEB-INF/jsp/include/popup/color_popup.jsp" %>
<!-- container END -->
</body>
</html>