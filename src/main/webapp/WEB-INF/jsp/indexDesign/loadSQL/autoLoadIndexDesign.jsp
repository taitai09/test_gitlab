<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	    <style>
		.datagrid-header-check input{ display: none; }
		input[type="text"]{
			line-height:23px !important;
		    margin: 0px 26px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 23px !important;
		    line-height: 23px !important;
		}
		input[type="checkbox"]{
			line-height:23px !important;
		    margin: 0px 0px 0px 0px !important;
		    padding: 0px 4px 0px 4px !important;
		    height: 20px !important;
		    line-height: 23px !important;
		}		
	</style>    
	<div class="searchArea100" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="table_owner" name="table_owner"/>
			<input type="hidden" id="exec_seq" name="exec_seq"/>
			<input type="hidden" id="access_path_type" name="access_path_type" value="DBIO"/>
			
			<label>DB</label>
			<select id="selectCombo" name="selectCombo" data-options="editable:false,required:true" class="w130 easyui-combobox"></select>
			<span class="searchBtnLeft">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick_autoLoadIndexDesign();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
			</span>
		</div>
		<div id="blockAutoLoadIndex" style="padding-top:5px;">
			<label>OWNER</label>
			<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
			
			<label style="margin-left:30px;">Selectivity 계산 : </label>
			<span style="padding-right:2px;padding-left:5px;"><input class="easyui-radiobutton" name="selectivity_calc_method" value="STAT" checked> Statistics 기반 </span> 
			<span>
				<i id="selectivity_statistics_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
			</span>
		    <span style="padding-right:2px;padding-left:10px;"><input class="easyui-radiobutton" name="selectivity_calc_method" value="DATA"> Data Sampling 기반</span>
			<span>
				<i id="selectivity_data_tooltip" class="fas fa_question_circle easyui-tooltip" title="" position="top"></i>
			</span>
			
			<span class="marginL20 searchBtnLeft">
				<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_IndexAutoDesign();"><i class="btnIcon fab fa-modx fa-lg fa-fw"></i> 인덱스 자동설계</a>
			</span>
		</div>
	</div>
	<div class="easyui-panel tableBorder-zero" data-options="border:false" style="width:100%;min-height:580px">
		<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
</div>