<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div class="searchAreaSingle" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="file_no" name="file_no"/>
			<input type="hidden" id="explain_exec_seq" name="explain_exec_seq"/>
			<input type="hidden" id="exec_seq" name="exec_seq"/>
			
			<label>DB</label>
			<select id="dbid" name="dbid" data-options="editable:false" class="w130 easyui-combobox" required="true">
			</select>
			<label>파일번호</label>
			<select id="selectFileNo" name="selectFileNo" data-options="editable:false" class="w130 easyui-combobox" required="true">
			</select>
			<span class="searchBtnLeft">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick_parseLoadingCondition();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
			</span>
			<div class="searchBtn">
				<a href="javascript:;" class="w150 easyui-linkbutton" onClick="insertParseLoadingCondition();"><i class="btnIcon fab fa-quinscape fa-lg fa-fw"></i> 적재SQL 조건절 파싱</a>
			</div>
		</div>
	</div>
	<div class="easyui-panel tableBorder-zero" data-options="border:false" style="width:100%;min-height:360px">
		<table id="explainList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
	<div class="easyui-panel tableBorder-zero" data-options="border:false" style="width:100%;min-height:240px;margin-top:20px;">
		<table id="accesspathList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
</div>