<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="tableUseSQL" class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div class="searchAreaSingle" data-options="border:false">
		<div class="well">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="owner" name="owner"/>
			
			<!-- 이전, 다음 처리 -->
			<input type="hidden" id="currentPage" name="currentPage" value="1"/>
			<input type="hidden" id="pagePerCount" name="pagePerCount" value="20"/>
			
			<label>DB</label>
				<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
			<label>OWNER</label>
				<select id="selectUserName" name="selectUserName" data-options="editable:true" class="w130 easyui-combobox" required="true"></select>
			<label>TABLE NAME</label>
				<input type="text" id="table_name" name="table_name" class="w130 easyui-textbox" required="true" />
			<label>MODULE</label>
				<input type="text" id="module" name="module" class="w130 easyui-textbox" />
			<label>ACTION</label>
				<input type="text" id="action" name="action" class="w130 easyui-textbox" />
			<span class="searchBtnLeft">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
			</span>
			<div class="searchBtn innerBtn">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Excel_DownClick();"><i class="btnIcon fas fa-file-excel fa-lg fa-fw"></i> 엑셀</a>
			</div>
		</div>
	</div>
	<div data-options="border:false" style="width:100%;min-height:640px;">
		<div id="tableUseTabs" class="easyui-tabs tableBorder-zero tableHeight" data-options="plain:true,fit:true,border:false" style="width:100%;">
			<div title="SQL 현황" style="padding:5px;">
				
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%">
					<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:550px;">
						<table id="tableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					
					<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:40px;margin-top:5px;">
						<div class="searchBtn" style="height:40px;text-align:right;">
							<a href="javascript:;" id="prevBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-left fa-lg fa-fw"></i> 이전</a>
							<a href="javascript:;" id="nextBtn" class="w80 easyui-linkbutton" data-options="disabled:true"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a>
						</div>
					</div>
				</div>
				
			</div> <!-- end of tab -->
		</div>
	</div>
</div>