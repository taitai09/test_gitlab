<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.03.15	이원식	OPENPOP V2 최초작업
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>수집 SQL 조건설정</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/indexDesignPreProcessing/setCollectionCondition.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="dbid" name="dbid"/>
					<label>DB</label>
					<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w120 easyui-combobox" required="true"></select>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="min-height:650px;margin-bottom:10px;">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="global_view_yn" name="global_view_yn" value="Y"/>
				<input type="hidden" id="parsingSchemaArry" name="parsingSchemaArry"/>
				<input type="hidden" id="moduleNameArry" name="moduleNameArry"/>
				<input type="hidden" id="modCnt" name="modCnt" value="1" />				
				<div class="dtl_title"><span id="subTitle" class="h3" style="margin-left:10px;"><i class="fas fa-check fa-lg fa-fw"></i> 수집 대상 스키마</span></div>
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:530px;">						
					<div data-options="region:'center',border:false" style="padding-left:10px;">
						<div class="easyui-layout" data-options="border:false" style="width:95%;height:370px;">
							<div data-options="region:'west'" style="width:46%;">
								<table id="collectionList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
							<div data-options="region:'center', border:false">
								<div style="width:100%;text-align:center;margin-top:150px;">
									<a href="javascript:;" class="easyui-linkbutton" onClick="Btn_AddApplyTarget();"><i class="btnIcon fas fa-angle-right fa-lg fa-fw"></i></a>											
								</div>
								<div style="width:100%;text-align:center;margin-top:30px;">
									<a href="javascript:;" class="easyui-linkbutton" onClick="Btn_RemoveApplyTarget();"><i class="btnIcon fas fa-angle-left fa-lg fa-fw"></i></a>
								</div>
							</div>
							<div data-options="region:'east'" style="width:46%;">
								<table id="applyList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
								</table>
							</div>
						</div>
					</div>
					<div data-options="region:'east',border:false" style="width:40%;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;height:370px;">
							<div data-options="region:'north', border:false" style="height:80px">
								<div class="dtl_title marginB20">
									<span id="subTitle" class="h3" style="margin-left:0px;margin-right:30px;"><i class="fas fa-check fa-lg fa-fw"></i> GV$ 수집</span>
									<input type="checkbox" id="chkGlobalViewYn" name="chkGlobalViewYn" value="" class="w120 easyui-switchbutton"/>
									<span id="subTitle" class="h3" style="margin-left:30px;margin-right:30px;"><i class="fas fa-check fa-lg fa-fw"></i> Instance</span>
									<select id="instance_number" name="instance_number" data-options="panelHeight:'auto', readonly:true" class="w120 easyui-combobox"></select>
								</div>									
								<div class="dtl_title">
									<span id="subTitle" class="h3" style="margin-left:0px"><i class="fas fa-check fa-lg fa-fw"></i> 수집 모듈</span>
								</div>
							</div>
							<div data-options="region:'center', border:false">										
								<table id="moduleTable" class="detailT" style="width:97%;margin-left:0px;margin-top:0px;">
									<colgroup>	
										<col style="width:30%;">
										<col style="width:70%;">
									</colgroup>	
									<tbody>	
										<tr>
											<th>모듈 1</th>
											<td style="padding-left:10px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;LIKE&nbsp;&nbsp;&nbsp;<input type="text" id="module_name1" name="module_name" class="w150 easyui-textbox"/>&nbsp;&nbsp;%</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div data-options="region:'south', border:false" style="height:50px">
								<div class="dtlBtn marginR10" style="margin-bottom:10px;">
									<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_addModule();"><i class="btnIcon fas fa-plus-circle fa-lg fa-fw"></i> 모듈 추가</a>						
									<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_removeModule();"><i class="btnIcon fas fa-minus-circle fa-lg fa-fw"></i> 모듈 삭제</a>
								</div>
							</div>
						</div>
					</div>
					<div data-options="region:'south',border:false" style="height:120px;">
						<div class="searchBtn innerBtn2 marginR5">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetCollectionCondition();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
</body>
</html>