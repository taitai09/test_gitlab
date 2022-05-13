<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.01.04	이원식	최초작성
 * 2018.03.07	이원식	OPENPOP V2 최초작업
 * 2021.11.09	황예지	DB항목 추가
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>업무 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/treegrid-dnd.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/userMng/workJob.js?ver=<%=today%>"></script>    
    
	<style>
	.workjob table tr td label.chkbox_label{
		color: rgb(51, 51, 51);
		font-size: 10px;
		font-weight: bold;
	}
	</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents" class="workjob">
		<div class="easyui-panel" data-options="border:false">
			<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>
		</div>
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:750px;">
			<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:355px;padding-top:5px;">
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
					<div data-options="region:'west',border:false" style="width:50%;height:100%;padding:5px 5px 0px 0px;overflow:hidden;">
						<div class="well marginB10">
							<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 업무 TREE</span></div>
						</div>
						<div class="easyui-panel" data-options="border:false" style="height:80%;margin-bottom:10px;">
							<table id="tableList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
							</table>
						</div>
					</div>
					<div data-options="region:'center',border:false" style="height:100%;padding:5px 0px 0px 5px">
						<div class="well marginB10">
							<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 업무 추가/수정</span></div>
						</div>
						
						<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
							<div class="easyui-panel" data-options="border:false" style="height:100%">
								<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
								<input type="hidden" id="wrkjob_cd_target" name="wrkjob_cd_target">
								<input type="hidden" id="old_wrkjob_div_cd" name="old_wrkjob_div_cd" />
								<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
								
								<!-- 이전, 다음 처리 -->
								<input type="hidden" id="currentPage" name="currentPage" value="${users.currentPage}"/>
								<input type="hidden" id="pagePerCount" name="pagePerCount" value="${users.pagePerCount}"/>
								
								<table id="dbAuthList" class="detailT click" style="margin-left:0px;margin-top:0px;">
									<colgroup>
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>
									<tr>
										<th>상위업무</th>
										<td><select id="upper_wrkjob_cd" name="upper_wrkjob_cd" data-options="panelHeight:'300',editable:true" class="w200 easyui-combotree"></select></td>
									</tr>
									<tr>
										<th>업무코드</th>
										<td><input type="text" id="wrkjob_div_cd" name="wrkjob_div_cd" class="w200 easyui-textbox" required="required"/></td>
									</tr>
									<tr>
										<th>업무명</th>
										<td><input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w200 easyui-textbox" required/></td>
									</tr>
									<tr>
										<th>배포성능점검대상여부</th>
										<td>
											<select id="deploy_check_target_yn" name="deploy_check_target_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required>
												<option value=""></option>
												<option value="Y">Y</option>
												<option value="N">N</option>
											</select>
										</td>
									</tr>
									<tr>
										<th>사용여부</th>
										<td>
											<select id="use_yn" name="use_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required>
												<option value=""></option>
												<option value="Y">Y</option>
												<option value="N">N</option>
											</select>
										</td>
									</tr>
									<tr>
										<th rowspan="3">
											DB<br>
											<label class="chkbox_label">하위 업무 적용</label><input id="sub_apply" name="sub_apply" class="easyui-checkbox">
										</th>
										<td>
											<select id="db1" name="db" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<select id="db2" name="db" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<select id="db3" name="db" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
											</select>
										</td>
									</tr>
								</table>
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveWorkJob();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<div data-options="region:'center',border:false" style="padding-top:5px;top:350px important!;">
			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:380px;">
				<div data-options="region:'west',split:false,collapsible:false,border:false" style="width:50%;padding:5px 5px 0px 0px;">
					<form:form method="post" id="workjob_form" name="workjob_form" class="form-inline">
						<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
						<input type="hidden" id="user_id" name="user_id" />
						<input type="hidden" id="workjob_start_day" name="workjob_start_day" />
					</form:form>
					<div class="well marginB10">
						<div class="dtl_title">
							<span id="subTitle" class="h3" style="margin-left:0px;">▶ 업무별 사용자 내역</span>
							<span style="position:relative;float:right;">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
							</span>	
						</div>
					</div>
					<table id="wrkUserList" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:250px;">
					</table>
					<div class="searchBtn innerBtn2">
					</div>
					<div class="searchBtn" data-options="region:'south',split:false,border:false" style="height:6%;padding-top:10px;text-align:right;">
						<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
				<div data-options="region:'center',split:false,collapsible:false,border:false" style="padding:5px 0px 0px 5px;cursor:default;">
					<div class="well marginB10">
						<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 업무리더 여부 변경</span></div>
					</div>
						<form:form method="post" id="user_detail_form" name="user_detail_form" class="form-inline">
							<div class="easyui-panel" data-options="border:false" style="height:100%;">
								<input type="hidden" id="workjob_start_day" name="workjob_start_day" value="${menu_id}">
								<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" />
								<input type="hidden" id="user_id" name="user_id" />
								
								<table id="userList" class="detailT click" style="margin-left:0px;margin-top:0px;">
									<colgroup>
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>
									<tr>
										<th>사용자ID</th>
										<td><input id="user_id_temp" data-options="panelHeight:'auto',editable:false" class="w200 easyui-textbox"/></td>
									</tr>
									<tr>
										<th>사용자명</th>
										<td><input type="text" id="user_nm" name="user_nm" data-options="panelHeight:'auto',editable:false" class="w200 easyui-textbox"/></td>
									</tr>
									<tr>
										<th>내선번호</th>
										<td><input type="text" id="ext_no" name="ext_no" data-options="panelHeight:'auto',editable:false" class="w200 easyui-textbox"/></td>
									</tr>
									<tr>
										<th>휴대폰번호</th>
										<td><input id="hp_no" name="hp_no" data-options="panelHeight:'auto',editable:false" class="w200 easyui-textbox"/></td>
									</tr>
									<tr>
										<th>사용여부</th>
										<td><input type="text" id="use_yn" name="use_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-textbox"/></td>
									</tr>
									<tr>
										<th>업무리더 여부</th>
										<td>
											<select id="leader_yn" name="leader_yn" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox">
												<option value="Y">Y</option>
												<option value="N">N</option>
											</select>
										</td>
									</tr>
								</table>
								<div class="searchBtn innerBtn2">
									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveWorkJobLeader();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>