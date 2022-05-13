<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div class="searchAreaSingle" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="file_no" name="file_no"/>
			
			<label>DB</label>
			<select id="selectDbid" name="selectDbid" data-options="editable:false" class="w130 easyui-combobox" required="true">
			</select>
			<span class="searchBtnLeft">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick_loadSQL();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
			</span>
			<div class="searchBtn">
				<a href="javascript:;" class="w90 easyui-linkbutton" onClick="showLoadSqlPop();"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> SQL 적재</a>
			</div>
		</div>
	</div>
	<div class="easyui-panel tableBorder-zero" data-options="border:false" style="width:100%;min-height:620px">
		<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
</div>