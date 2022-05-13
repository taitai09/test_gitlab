<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2019.05.13	임승률	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <script type="text/javascript" src="/resources/js/lib/jquery.color.js?ver=<%=today%>"></script>
        <script type="text/javascript" src="/resources/js/ui/solutionProgramMng/programRule.js?ver=<%=today%>"></script>    
	<script type="text/javascript">
		var auth_cd = "${auth_cd}";
	</script>
</head>
<body style="visibility:hidden;">
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<div id="contents">
		<div class="easyui-panel searchAreaSingle" data-options="border:false" style="width:100%;">
			<div class="well" >
				<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
					<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>

					<!-- 이전, 다음 처리 -->
					<input type="hidden" id="currentPage" name="currentPage" value="${BusinessClassMng.currentPage}"/>
					<input type="hidden" id="pagePerCount" name="pagePerCount" value="${BusinessClassMng.pagePerCount}"/>
					
					<label>솔루션프로그램구분</label>
					<select id="slt_program_div_cd" name="slt_program_div_cd" data-options="panelHeight:'auto',editable:false" class="w200 easyui-combobox" required="true"></select>
					<label>프로그램 RULE명</label>
						<input type="text" id="slt_program_sql_name" name="slt_program_sql_name" class="w200 easyui-textbox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>	
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
					</div>
				</form:form>
			</div>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:220px;margin-bottom:10px;">
			<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="searchBtn" data-options="region:'south',split:false,border:false" style="width:100%;height:6%;padding:10px 0px;text-align:right;">
			<a href="javascript:;" id="prevBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="prevBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
			<a href="javascript:;" id="nextBtnDisabled" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
			<a href="javascript:;" id="nextBtnEnabled" class="w80 easyui-linkbutton" data-options="disabled:false"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
		</div>
		<div class="easyui-panel" data-options="border:false" style="width:100%;min-height:300px">
			<form:form method="post" id="detail_form" name="detail_form" class="form-inline">
				<input type="hidden" id="crud_flag" name="crud_flag" value="C"/>
				<table class="detailT">
					<colgroup>
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
						<col style="width:15%;">
						<col style="width:18%;">
					</colgroup>
					<tr>
						<th>솔루션프로그램구분</th>
						<td>
							<select id="slt_program_div_cd" name="slt_program_div_cd" data-options="panelHeight:'auto',editable:false" class="w180 easyui-combobox" required="true"></select>
						</td>
						<th>프로그램 RULE번호</th>
						<td class="ltext">
							<input type="text" id="slt_program_sql_number" name="slt_program_sql_number" class="w180 easyui-textbox" readonly="true"/>
						</td>
						<th>프로그램 RULE명</th>
						<td class="ltext">
							<input type="text" id="slt_program_sql_name" name="slt_program_sql_name" class="w180 easyui-textbox" required="true"/>
						</td>
						<th>목차</th>
						<td>
							<select id="contents_id" name="contents_id" data-options="panelHeight:'300',editable:false" class="w210 easyui-combobox"></select>
						</td>
					</tr>
					<tr>
						<th>설명</th>
						<td colspan="7" class="ltext"><input type="text" id="slt_program_sql_desc" name="slt_program_sql_desc" style="width:1470px;" !important; class="w600 easyui-textbox"/></td>
					</tr>
					<tr>
						<th>프로그램 RULE
							<br/><br/>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">RULE 복사</a>
							<c:if test="${auth_cd eq 'ROLE_OPENPOPMANAGER'}">
								<br><br>
								<a href="javascript:;" class="w120 easyui-linkbutton" onClick="copy_encrypted_rule();">암호화 RULE 복사</a>
							</c:if>
						</th>
						<td colspan="7"  style="vertical-align:top;">
							<textarea name="slt_program_chk_sql" id="slt_program_chk_sql" rows="10" cols="350" class="validatebox-invalid" style="overlow:scroll;margin-top:3px;padding:5px;width:98.8%;height:190px" wrap="off"></textarea>
							<c:if test="${auth_cd eq 'ROLE_OPENPOPMANAGER'}">
								<textarea id="encrypted_rule" style="width: 0; height: 0; margin: 0; padding: 0;"></textarea>
							</c:if>
						</td>
					</tr>
				</table>
				<div class="searchBtn innerBtn2">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSetting();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="Btn_DeleteSetting();"><i class="btnIcon fas fa-trash fa-lg fa-fw"></i> 삭제</a>
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