<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.07	임승률	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능개선 :: 데이터구조품질 :: 업무 분류체계 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/mqm/qualityStdInfo/businessClassMng.js?ver=<%=today%>"></script>
    <script type="text/javascript" src="/resources/js/ui/include/popup/businessClassMng_excel_upload.js?ver=<%=today%>"></script>
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
					<input type="hidden" id="currentPage" name="currentPage" value="${BusinessClassMng.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${BusinessClassMng.pagePerCount}"/>
					
					<label>라이브러리명</label>
					<input type="text" id="lib_nm" name="lib_nm" class="w200 easyui-textbox"/>
					<label>모델명</label>
					<input type="text" id="model_nm" name="model_nm" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>	
					
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Upload();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 일괄 업로드</a>
					</div>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>						
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:300px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="rgb_color_id" name="rgb_color_id"/>
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
						<th>라이브러리명</th>
						<td class="ltext"><input type="text" id="lib_nm" name="lib_nm" required="true" class="w200 easyui-textbox"/></td>
						<th>모델명</th>
						<td class="ltext"><input type="text" id="model_nm" name="model_nm" required="true" class="w200 easyui-textbox"/></td>
						<th>주제영역명</th>
						<td class="ltext"><input type="text" id="sub_nm" name="sub_nm" required="true" class="w200 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>시스템명</th>
						<td class="ltext"><input type="text" id="sys_nm" name="sys_nm" class="w200 easyui-textbox"/></td>
						<th>시스템코드</th>
						<td class="ltext"><input type="text" id="sys_cd" name="sys_cd" style="text-align:center" class="w200 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>업무대분류명</th>
						<td class="ltext"><input type="text" id="main_biz_cls_nm" name="main_biz_cls_nm" class="w200 easyui-textbox"/></td>
						<th>업무대분류코드</th>
						<td class="ltext"><input type="text" id="main_biz_cls_cd" name="main_biz_cls_cd" style="text-align:center" class="w200 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>업무중분류명</th>
						<td class="ltext"><input type="text" id="mid_biz_cls_nm" name="mid_biz_cls_nm" class="w200 easyui-textbox"/></td>
						<th>업무중분류코드</th>
						<td class="ltext"><input type="text" id="mid_biz_cls_cd" name="mid_biz_cls_cd" style="text-align:center" class="w200 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>주제영역설명</th>
						<td colspan="3"><input id="biz_desc" name="biz_desc" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:60px"></td>
					</tr>
					<tr>
						<th>비고</th>
						<td colspan="3"><input id="remark" name="remark" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:60px" ></td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_DeleteInstance();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_ResetField();"><i class="btnIcon fas fa-retweet fa-lg fa-fw"></i> 초기화</a>
				</div>
			</form:form>
		</div>
	</div>
	<!-- contents END -->
</div>
<!-- container END -->
<%@include file="/WEB-INF/jsp/include/popup/businessClassMng_excel_upload.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>