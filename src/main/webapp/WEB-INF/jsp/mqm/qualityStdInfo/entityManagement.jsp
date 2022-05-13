<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.01.03	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>성능개선 :: 데이터 구조 품질 :: 엔터티 유형 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/mqm/qualityStdInfo/entityManagement.js?ver=<%=today%>"></script>
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

					<input type="hidden" id="user_id" name="user_id" value="${user_id}">
											<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="10"/>
					
					
					<label>엔터티 유형</label>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>							
				</form:form>								
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;padding-left:5px;min-height:350px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding-top:10px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>	
		
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:320px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
			
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
						<th>엔터티 유형</th>
						<td>
							<input type="text" id="ent_type_cd" name="ent_type_cd" required="true" class="w200 easyui-textbox"/>
						</td>
						<th>참조 엔터티 PREFIX</th>
						<td>
							<input type="text" id="ref_ent_type_nm" name="ref_ent_type_nm" class="w200 easyui-textbox"/>
						</td>
						<th>엔터티 SUFFIX</th>
						<td>
							<input type="text" id="ent_type_nm" name="ent_type_nm" class="w200 easyui-textbox"/>
						</td>	
					</tr>
					<tr>
						<th>테이블 유형</th>
						<td>
							<input type="text" id="tbl_type_nm" name="tbl_type_nm" class="w200 easyui-textbox"/>
						</td>
						<th>테이블 유형 코드</th>
						<td>
							<input type="text" id="tbl_type_cd" name="tbl_type_cd" class="w200 easyui-textbox"/>
						</td>
					</tr>
					<tr>
						<th >설명</th>
							<td colspan="5"><input id="ent_type_desc" name="ent_type_desc" rows="30" data-options="multiline:true" class="easyui-textbox" style="width:100%;height:80px" ></textarea></td>
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