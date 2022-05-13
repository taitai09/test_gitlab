<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.03	임호경	최초작성
  **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>DB 힌트 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/databaseMngSetting/notUseHint.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:750px;">
			<div data-options="region:'west',border:false" style="width:55%;padding:5px 5px 0px 0px;">
				<div class="well marginB10">
					<form:form method="post" id="left_form" name="left_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

						<input type="hidden" id="dbid" name="dbid"/>
						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">전체</option>
							<option value="01">DB명</option>
							<option value="02">등록아이디</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_getHintList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:650px;margin-bottom:10px;">
					<table id="hintList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
			<div data-options="region:'center',border:false" style="padding:5px">
				<div class="well marginB10">	
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 힌트 제외 추가/수정</span></div>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<div class="easyui-panel" data-options="border:false" style="height:600">
						<input type="hidden" id="user_id" name="user_id" value="${user_id}">
						<input type="hidden" id="dbid" name="dbid"/>
						<input type="hidden" id="old_dbid" name="old_dbid"/>
						<input type="hidden" id="old_hint_nm" name="old_hint_nm" value=""/>
						<input type="hidden" name="crud_flag" id="crud_flag" value="C"/>
<!-- 								<input type="hidden" id="old_db_name" name="db_name" value=""/> -->
						
						<table id="dbHintList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:30%;">
								<col style="width:70%;">
							</colgroup>
							
							<tr>
								<th>DB</th>
<!-- 										<td><select id="upper_wrkjob_cd" name="upper_wrkjob_cd" data-options="panelHeight:'auto'" class="w200 easyui-combotree"></select></td> -->
								<td><select id="selectDbid" name="selectDbid" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combotree" required ></select></td>
							</tr>
<!-- 									<tr> -->
<!-- 										<th>DB명</th> -->
<!-- 										<td><input type="text" id="db_name" name="db_name" class="w200 easyui-textbox" readonly="readonly" disabled="disabled"/></td> -->
<!-- 									</tr> -->
							<tr>
								<th>DB 약어명</th>
								<td><input type="text" id="db_abbr_nm" name="db_abbr_nm" class="w200 easyui-textbox" readonly="readonly"/></td>
							</tr>
							<tr>
								<th>힌트명</th>
								<td>
									<input type="text" id="hint_nm" name="hint_nm" class="w200 easyui-textbox" required="required"/>
								</td>
							</tr>
							<tr>
								<th>등록날짜</th>
								<td><input type="text" id="hint_reg_dt" name="hint_reg_dt" class="w200 easyui-textbox" readonly="readonly"/></td>
							</tr>
							<tr>
								<th>등록아이디</th>
								<td><input type="text" id="hint_reg_id" name="hint_reg_id" class="w200 easyui-textbox" readonly="readonly"></td>
							</tr>
						</table>
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_saveHint();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_deleteHint();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
							<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
						</div>
					</div>
				</form:form>
						
<!-- 				<div class="dtl_title" style="clear:both;"><span id="histTitle" class="h3" style="margin-left:0px;">▶ DB 튜닝담당자 이력</span></div> -->
<%-- 				<form:form method="post" id="history_form" name="history_form" class="form-inline"> --%>
<!-- 					<input type="hidden" id="dbid" name="dbid"/> -->
<!-- 					<input type="hidden" id="tuner_id" name="tuner_id"/> -->
<!-- 					<div class="easyui-panel" data-options="border:false" style="height:300px">							 -->
<!-- 						<table id="dbOfficerHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 						</table> -->
<!-- 					</div> -->
<%-- 				</form:form> --%>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>