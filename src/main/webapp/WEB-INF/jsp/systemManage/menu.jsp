<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.08.21	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>메뉴 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/treegrid-dnd.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/systemManage/menu.js?ver=<%=today%>"></script>    
<style>
label{margin-left:5px; margin-right:10px}
</style>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel" data-options="border:false">
			<div class="title paddingT5">
				<span class="h3"><i class="far fa-check-square fa-lg fa-fw"></i> ${menu_nm}</span>
			</div>			
		</div>
			
		<div class="easyui-layout" data-options="border:false," style="width:100%;height:750px;">
			
				<div data-options="region:'west',border:false" style="width:70%;height:100%;padding:5px 5px 0px 0px;overflow:hidden;">
					<div class="well marginB10">
						<form:form method="POST" id="submit_form" name="submit_form" class="form-inline">
							<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		
		<!-- 					<input type="text" id="menu_id" name="menu_id"/> -->
							
							<label>검색 </label>
							<select id="selectMenu" name="selectMenu" data-options="panelHeight:'300',editable:true" class="w300 easyui-combotree" required>
							</select>
							
							<span class="searchBtnLeft">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 선택 </a>
							</span>							
						</form:form>						
					</div>
<!-- 						<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 메뉴 TREE</span></div> --> <!-- 현재 필요 없으나 추가될 수 있음. -->
					<div class="easyui-panel" data-options="border:false" style="height:88%;margin-bottom:10px;">
						<table id="tableList" class="tbl easyui-treegrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
					
				<div data-options="region:'center',border:false" style="height:1200;padding:5px;">
					<div class="well marginB10">
						<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:0px;">▶ 메뉴 추가/수정</span></div>
					</div>
					<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
						<div class="easyui-panel" data-options="border:false" style="height:600">
							<input type="hidden" name="crud_flag" id="crud_flag" value="C"/>
							<input type="hidden" name="menu_id_list" id="menu_id_list" />
							<input type="hidden" name="menu_ordering_list" id="menu_ordering_list" />
							
							
							<table id="dbAuthList" class="detailT click" style="margin-left:0px;margin-top:0px;">
								<colgroup>
									<col style="width:30%;">
									<col style="width:70%;">
								</colgroup>
								<tr>
									<th>상위메뉴</th>
<!-- 										<td><select id="upper_wrkjob_cd" name="upper_wrkjob_cd" data-options="panelHeight:'auto'" class="w200 easyui-combotree"></select></td> -->
									<td><select id="parent_menu_id" name="parent_menu_id" data-options="editable:true" class="w200 easyui-combotree" ></select></td>
								</tr>
								<tr>
									<th>메뉴코드</th>
									<td><input type="text" id="menu_id" name="menu_id" class="w200 easyui-textbox" required/></td>
								</tr>
								<tr>
									<th>메뉴명</th>
<!-- 										<td><input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w200 easyui-textbox" /></td> -->
									<td><input type="text" id="menu_nm" name="menu_nm" class="w200 easyui-textbox" required/></td>
								</tr>
								<tr>
									<th>설명</th>
<!-- 										<td><input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w200 easyui-textbox"/></td> -->
									<td><input type="text" id="menu_desc" name="menu_desc" class="w200 easyui-textbox" required/></td>
								</tr>
								<tr>
									<th>호출 URL주소</th>
<!-- 										<td><input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w200 easyui-textbox"/></td> -->
									<td><input type="text" id="menu_url_addr" name="menu_url_addr" class="w200 easyui-textbox"/></td>
								</tr>
								<tr>
									<th>이미지 정보</th>
<!-- 										<td><input type="text" id="wrkjob_cd_nm" name="wrkjob_cd_nm" class="w200 easyui-textbox"/></td> -->
									<td><input type="text" id="menu_image_nm" name="menu_image_nm" class="w200 easyui-textbox"/></td>
								</tr>
								<tr>
									<th>정렬순서</th>
<!-- 										<td><select id="dbid" name="dbid" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox"></select></td> -->
									<td>
										<input type="number" id="menu_ordering" name="menu_ordering" class="w200 easyui-textbox" required/>
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
									<th>권한</th>
								<td>
<!-- 										<div id="auth_id"></div> -->
										<c:forEach var="authNm" items="${authNmList}" varStatus="status">
											<c:if test="${status.index  eq 4 or status.index  eq 7 }">
												<br/>
											</c:if>
											<label for="auth_id${authNm.auth_id}">
												<input type="checkbox" name="auth_id" id="auth_id${authNm.auth_id}" value="${authNm.auth_id}"/> ${authNm.auth_nm}
											</label>
										</c:forEach>
								</td>
								</tr>
							</table>
							<div class="searchBtn innerBtn2">
								<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_saveMenuInfo();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
								<a href="javascript:;" class="w80 easyui-linkbutton" id="deleteButton" onClick="Btn_deleteMenuInfo();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
<!-- 									<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveWorkJob();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 수정</a> -->
								<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
							</div>								
						</div>
					</form:form>
				</div>
		</div>
				
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>