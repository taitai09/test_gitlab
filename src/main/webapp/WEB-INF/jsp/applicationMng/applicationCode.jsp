<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.09.17	임호경	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>설정 :: 기준정보 설정 :: 애플리케이션 코드 관리</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/ui/applicationMng/applicationCode.js?ver=<%=today%>"></script>
   	<script type="text/javascript" src="/resources/js/ui/include/popup/applicationCodeMng_excel_upload_popup.js?ver=<%=today%>"></script> <!-- 어플리케이션코드 등록 팝업 -->
    
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
					<input type="hidden" id="currentPage" name="currentPage" value="${trCd.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${trCd.pagePerCount}"/>
					
					<label>검색 조건</label>
					<select id="searchKey" name="searchKey" data-options="panelHeight:'auto',editable:false" class="w150 easyui-combobox">
						<option value="">전체</option>
						<option value="01">업무명</option>
						<option value="02">애플리케이션 코드</option>
					</select>
					<input type="text" id="searchValue" name="searchValue" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 조회</a>
						<a href="javascript:;" class="w140 easyui-linkbutton" onClick="Btn_ApplicationCodeRegist();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 애플리케이션 코드 등록</a>
						<a href="javascript:;" id="multiDeleteBtn" class="w80 easyui-linkbutton" onClick="Btn_MultiDeleteSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 일괄 삭제</a>
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
				
				<input type="hidden" id="crud_flag" name="crud_flag" value="C" />
				<input type="hidden" id="wrkjob_cd_nm" name="wrkjob_cd_nm" />
				<input type="hidden" id="chk_tr_cd" name="chk_tr_cd" />
				<input type="hidden" id="chk_wrkjob_cd" name="chk_wrkjob_cd" />
				
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
						<th>업무명</th>
							<td>
								<select id="wrkjob_cd" name="wrkjob_cd" data-options="panelHeight:'220',editable:false" class="w150 easyui-combotree" required="required"></select>
							</td>
						<th>업무코드</th>
							<td>
								<input type="text" id="wrkjob_cd_ui" name="wrkjob_cd_ui" class="w150 easyui-textbox" readonly="readonly"></select>
							</td>
						<th>애플리케이션 코드</th>
							<td>
								<input type="text" id="tr_cd" name="tr_cd" class="w150 easyui-textbox" required/>
							</td>
					</tr>
					<tr>
						<th>애플리케이션 코드이름</th>
							<td>
								<input type="text" id="tr_cd_nm" name="tr_cd_nm" class="w150 easyui-textbox" required/>
							</td>
						<th>담당자ID</th>
							<td>
								<input type="text" id="mgr_id" name="mgr_id" class="w150 easyui-textbox"/>
							</td>
						<th>등록일시</th>
							<td>
								<input type="text" id="reg_dt" name="reg_dt"" class="w150 easyui-textbox"/>
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
<%@include file="/WEB-INF/jsp/include/popup/applicationCodeMng_excel_upload_popup.jsp" %> <!-- snap id 조회 팝업 -->
</body>
</html>