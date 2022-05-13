<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<title>사용자 DB권한관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/userMng/userDBAuth.js?ver=<%=today%>"></script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:700px;">
			<div data-options="region:'west',border:false" style="width:55%;padding:5px 5px 0px 0px;overflow:hidden;">
				<div class="well marginB10">
					<form:form method="post" id="left_form" name="left_form" class="form-inline">
					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="100"/>
					
						<label>검색 조건</label>
						<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w100 easyui-combobox">
							<option value="">전체</option>
							<option value="01">사용자명</option>
							<option value="02">사용자 ID</option>
						</select>
						<input type="text" id="searchValue" name="searchValue" class="w150 easyui-textbox"/>

					<label>사용여부</label>
					<select id="use_yn" name="search_use_yn" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox">
						<option value="">전체</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
					</select>
					<label>DB</label>
					<select id="search_dbid" name="search_dbid" data-options="panelHeight:'auto',editable:true" class="w120 easyui-combobox">
					</select>
					<span style="margin-left:10px;margin-right:10px;" id="span_dbAuth">
						<input class="easyui-checkbox" id="chk_dbAuth" name="chk_dbAuth" value="Y" style="margin-right:5px;">
						DB권한부여
					</span>
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						</span>
					</form:form>
				</div>
				<div class="easyui-panel" data-options="border:false" style="min-height:550px;margin-bottom:10px;">
					<table id="userList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
				<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-right:10px;text-align:right;">
					<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
					<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				</div>	
			</div>
			
			<div data-options="region:'center',border:false" style="padding:5px 0px 0px 5px">
				<div class="well marginB10">
					<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ DB권한 추가</span></div>
				</div>
				<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
					<input type="hidden" id="user_id" name="user_id"/>
					<input type="hidden" id="chk_user_id" name="chk_user_id"/>
					<div class="easyui-panel" data-options="border:false" style="height:550px;border:1px solid;border-top:0px;">
						<table id="dbAuthList" class="detailT click" style="margin-left:0px;margin-top:0px;">
							<colgroup>
								<col style="width:8%;">
								<col style="width:22%;">
								<col style="width:35%;">
								<col style="width:35%;">
							</colgroup>
							<thead>
								<tr>
									<th><input type="checkbox" id="chkAll" name="chkAll" class="chkBox"/></th>
									<th>DB명</th>
									<th>권한시작일자</th>
									<th>권한종료일자</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<div class="searchBtn innerBtn2" style="display:block;padding-top:0px;">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserDBAuth();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					</div>
				</form:form>
<!-- 				<div class="dtl_title" style="clear:both;"><span id="histTitle" class="h3" style="margin-left:0px;">▶ DB권한 이력</span></div> -->
<%-- 				<form:form method="post" id="history_form" name="history_form" class="form-inline"> --%>
<!-- 					<input type="hidden" id="dbid" name="dbid"/> -->
<!-- 					<input type="hidden" id="user_id" name="user_id"/> -->
<!-- 					<div class="easyui-panel" data-options="border:false" style="height:330px">							 -->
<!-- 						<table id="dbAuthHistoryList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
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