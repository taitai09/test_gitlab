<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2017.11.15	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업 
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>권한 관리(일괄 변경)</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/userMng/auth.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="currentPage" name="currentPage" value="${users.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${users.pagePerCount}"/>
		
					<label>권한명</label>
<!-- 					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox"> -->
<!-- 						<option value="">전체</option> -->
<!-- 						<option value="01">권한명</option> -->
<!-- 						<option value="02">권한코드</option> -->
<!-- 					</select> -->
					<select id="auth_id" name="auth_id" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox" required></select>
					<label>사용자명</label>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<label>검색건수</label>
					<select id="searchCount" name="selectRcount" data-options="editable:true" class="w60 easyui-combobox easyui-validatebox" required="true" data-options="panelHeight:'auto'">
<!-- 							<option value="">전체</option> -->
						<option value="10" selected>10</option>
						<option value="20" >20</option>
						<option value="40">40</option>
						<option value="60">60</option>
						<option value="80">80</option>
						<option value="100">100</option>
					</select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
<!-- 		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:350px;margin-bottom:10px;"> -->
<!-- 			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 			</table> -->
<!-- 		</div> -->
		
		<div data-options="region:'center',border:false" style="padding-top:5px;top:350px important!;">
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:600px;margin-bottom:5px;">
				<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:5px 0px;">
						<form:form method="post" id="workjob_form" name="workjob_form" class="form-inline">
							<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
							<input type="hidden" id="user_id" name="user_id" />
							<input type="hidden" id="workjob_start_day" name="workjob_start_day" />
						</form:form>
						<div class="well marginB10">
							<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 사용자 목록</span></div>
						</div>
						<table id="tableList" class="tbl easyui-datagrid" data-options="fit:false,border:false" style="height:88%;" >
							<tr></tr>
						</table>
						
						<div class="searchBtn innerBtn2">
<!-- 						<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SaveWorkJobLeader();"><i class="btnIcon far fa-id-card fa-lg fa-fw"></i> 리더 설정</a> -->
						</div>
				</div>
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding:5px 0px 0px 5px;cursor:default;">
					<div class="well marginB10">
						<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 권한 일괄 변경</span></div>
					</div>
					<form:form method="post" id="auth_form" name="auth_form" class="form-inline">
						<input type="hidden" id="auth_id" name="auth_id" value="">
						<input type="hidden" id="user_id" name="user_id"/>
						<input type="hidden" id="user_ids" name="user_ids"/>
						<input type="hidden" id="auth_start_day" name="auth_start_day"/>
						<input type="hidden" id="auth_end_day" name="auth_end_day"/>
						<input type="hidden" id="auth_comp_day" name="auth_comp_day"/>
						<table class="detailT" style="margin-left:0px;margin-top:0px;">
							<colgroup>	
								<col style="width:40%;">
								<col style="width:60%;">
							</colgroup>		
							<!-- <tr>
								<th>권한</th>
								<td><select id="auth_grp_id" name="auth_grp_id" data-options="panelHeight:'auto'" class="w200 easyui-combobox"></select></td>
							</tr> -->
							<tr>
								<th>권한시작일자</th>
								<td><input type="text" id="authStartDay" name="authStartDay" value="${nowDate}" class="w200 datapicker easyui-datebox"/></td>
							</tr>
							<tr>
								<th>권한종료일자</th>
								<td><input type="text" id="authEndDay" name="authEndDay" value="9999-12-31" class="w200 datapicker easyui-datebox"/></td>
							</tr>
						</table>
						<div class="searchBtn innerBtn2">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveUserAuth();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
							<!-- <a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField('auth');"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a> -->
						</div>
					</form:form>
				</div>
			</div>
			<div class="searchBtn" data-options="region:'south',split:false,border:false" style="position:relative;left:-860px;!important;">
				<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
				<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
				<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>