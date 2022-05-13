<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div class="searchArea100" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="owner" name="owner"/>
			<input type="hidden" id="table_name" name="table_name"/>
			<input type="hidden" id="file_no" name="file_no"/>
			<input type="hidden" id="explain_exec_seq" name="explain_exec_seq"/>
			<input type="hidden" id="usage_yn" name="usage_yn" value="N"/>
			<input type="hidden" id="pk_yn" name="pk_yn" value="N"/>
			<!-- 이전, 다음 처리 -->
<%-- 			<input type="hidden" id="currentPage" name="currentPage" value="${odsIndexs.currentPage}"/> --%>
<%-- 			<input type="hidden" id="pagePerCount" name="pagePerCount" value="${odsIndexs.pagePerCount}"/> --%>
			<input type="hidden" id="currentPage" name="currentPage"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount"/>
			
			<div>
				<label>DB</label>
				<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
				<label>파일번호</label>
				<select id="selectFileNo" name="selectFileNo" data-options="editable:false" class="w150 easyui-combobox" required="true"></select>
				<label>파일명</label>
				<input type="text" id="file_nm" name="file_nm" data-options="disabled:true" class="w200 easyui-textbox"/>
				<label>파싱순번</label>
				<select id="selectExplainExecSeq" data-options="editable:false" name="selectExplainExecSeq" class="w150 easyui-combobox" required="true"></select>
			</div>
			<div class="multi">
				<label>OWNER</label>
				<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w150 easyui-combobox" required="true"></select>
				<label>TABLE</label>
				<input type="text" id="selectTableName" name="selectTableName" data-options="readonly:false" class="w120 easyui-textbox"/>
				<label>미사용 인덱스</label>
				<input type="checkbox" id="chkUsage" name="chkUsage" class="easyui-switchbutton"/>
				<label>PK 제외</label>
				<input type="checkbox" id="chkPk_yn" name="chkPk_yn" class="easyui-switchbutton"/>
				<span class="searchBtnLeft multiBtn">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick_loadIndexUsage();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
				</span>
				<div class="searchBtn">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick_loadIndexUsage();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
				</div>
			</div>
		</div>
	</div>
	<div class="easyui-panel tableBorder-zero tableHeight" data-options="border:false" style="width:100%;min-height:530px">
		<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
		</table>
	</div>
	<div class="searchBtn" data-options="collapsible:false,border:false" style="height:40px;padding-top:10px;text-align:right;">
		<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
		<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
	</div>
</div>