<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div class="searchArea130" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid" value="${dbioLoadSql.dbid}"/>
			<input type="hidden" id="file_no" name="file_no" value="${dbioLoadSql.file_no}"/>
			<input type="hidden" id="temp_file_no" name="temp_file_no" value="${dbioLoadSql.file_no}"/>
			<input type="hidden" id="query_seq" name="query_seq"/>
			<input type="hidden" id="explain_exec_seq" name="explain_exec_seq"/>
			<input type="hidden" id="search_val" name="search_val"/>
			<input type="hidden" id="completeYn" name="completeYn"/>
			<input type="hidden" id="query_cnt" name="query_cnt"/>
			<input type="hidden" id="plan_yn" name="plan_yn"/>
			<input type="hidden" id="note" name="note"/>
			
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>
			<input type="hidden" id="rcount" name="rcount" value="20"/>
			
			<div>
				<label>DB</label>
				<select id="selectDbid" name="selectDbid" data-options="editable:false" class="w100 easyui-combobox" required="true"></select>
				<label>파일번호</label>
				<select id="selectFileNo" name="selectFileNo" data-options="editable:false" class="w150 easyui-combobox" required="true">
				</select>
				<label>파일명</label>
				<input type="text" id="file_nm" name="file_nm" data-options="disabled:true" class="w200 easyui-textbox"/>
				<label>적재일시</label>
				<input type="text" id="reg_dt" name="reg_dt" data-options="disabled:true" class="w150 easyui-textbox"/>
				<label>SQL수</label>
				<input type="text" id="query_load_cnt" name="query_load_cnt" data-options="disabled:true" class="w80 easyui-textbox"/>
				<label>정상/오류/미수행</label>
				<input type="text" id="proc_cnt" name="proc_cnt" data-options="disabled:true" class="w150 easyui-textbox"/>
			</div>
			<div class="multi">
				<label>생성순번</label>
				<select id="selectExplainExecSeq" name="selectExplainExecSeq" class="w80 easyui-combobox"></select>
				<label>생성일시</label>
				<input type="text" id="explain_ret_dt" name="explain_ret_dt" data-options="disabled:true" class="w150 easyui-textbox"/>
				<label>완료일시</label>
				<input type="text" id="explain_end_dt" name="explain_end_dt" data-options="disabled:true" class="w150 easyui-textbox"/>
				<label>플랜생성여부</label>
				<input type="checkbox" id="plan_creation_yn" name="plan_creation_yn" data-options="disabled:false" class="easyui-switchbutton"/>
				<label>ERROR여부</label>
				<input type="checkbox" id="error_yn" name="error_yn" data-options="disabled:false" class="easyui-switchbutton"/>
				<span class="searchBtnLeft">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick_loadActionPlan();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
				</span>
				<div class="searchBtn">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_Download_loadActionPlan();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
				</div>
			</div>
		</div>
		<div id="blockLoadActionPlan" style="padding-top:8px;">
			<label>파싱스키마</label>
			<select id="current_schema" name="current_schema" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
			<span class="searchBtnLeft multiBtn">
				<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_CreateActionPlan();"><i class="btnIcon fas fa-caret-square-right fa-lg fa-fw"></i> 실행계획생성</a>
				<a href="javascript:;" class="w160 easyui-linkbutton" onClick="Btn_OnForceComplete();"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 실행계획생성 강제완료처리</a>
			</span>
		</div>
	</div>
	<div class="easyui-panel tableBorder-zero tableHeight" data-options="border:false" style="width:100%;min-height:340px;">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false" style="width:100%;">
				<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,fitColumns:true,border:false" >
					<tbody><tr></tr></tbody>
				</table>
			</div>
			<div data-options="region:'south',border:false" style="width:100%;">
				<div class="easyui-layout" data-options="fit:true">
					<div class="searchBtn" data-options="region:'center',split:false,border:false" style="width:100%;height:10%;padding-top:5px;text-align:right;">
						<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
						<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="tabsDiv" class="easyui-tabs" data-options="plain:true,border:false" style="width:100%;height:200px;padding-bottom:1px;">
		<div id="sqlInfo" class="tabTxt" title="SQL TEXT" style="padding:10px;height:180px">
		</div>
		<div id="unusual" class="tabTxt" title="특이사항" style="padding:10px;height:180px">
		</div>
	</div>
</div>