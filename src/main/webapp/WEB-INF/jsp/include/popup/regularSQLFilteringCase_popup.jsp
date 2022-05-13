<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="regularSQLFilterCasePop" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:550px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="regularSQLFilterCase_form" name="regularSQLFilteringCase_form" class="form-inline">
			<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
			<input type="hidden" id="dbid" name="dbid"/>
			<input type="hidden" id="user_id" name="user_id"/>
			<input type="hidden" id="regular_sql_filter_type_nm" name="regular_sql_filter_type_nm"/>
			<input type="hidden" id="regular_sql_filter_type_cd" name="regular_sql_filter_type_cd"/>
			<input type="hidden" id="regular_sql_filter_condition" name="regular_sql_filter_condition"/>
			
			<div class="searchArea" region='north' style="width:100%;height:53px;">
				<div class="well" style="border: 0px;">
					<label>DB</label>
					<select id="selectDbidComboRegularSQL" name="selectDbidComboRegularSQL" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
				</div>
			</div>
			
			<div region='center' style="width:450px;height:350px;">
				<div region='north' style="width:98%;height:45%;padding:5px;">
					<div region='north'>
						<label>Parsing Schema Name</label>
						<select id="selectUserNameCombo" name="selectUserNameCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
						<span class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_AddTargetUserName();"><i class="btnIcon fas fa-plus fa-lg fa-fw"></i> 추가</a>
						</span>
					</div>
					<div region='center' style="height:85%;margin-top:2px;">
						<!-- 원 페이지의 datagrid와 충돌이 발생되어 class 정보를 삭제 -->
						<table id="regularSQLParsingSchemaNameList" data-options="fit:true" style="width:100%;height:100%;"></table>
					</div>
				</div>
				<div region='center' style="width:98%;height:45%;padding:5px;">
					<div region='north'>
						<label>필터유형</label>
						<select id="selectFilterTypeCombo" name="selectFilterTypeCombo" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
						<input type="text" id="filterCondition" name="filterCondition" class="w120 easyui-textbox easyui-validatebox"/>
						<span class="searchBtn">
							<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_AddFilterCase();"><i class="btnIcon fas fa-plus fa-lg fa-fw"></i> 추가</a>
						</span>
					</div>
					<div region='center' style="height:99%;margin-top:2px;">
						<!-- 원 페이지의 datagrid와 충돌이 발생되여 class 정보를 삭제 -->
						<table id="regularSQLFilteringCaseList" data-options="fit:true" style="width:100%;height:100%;"></table>
					</div>
				</div>
			</div>		
		</form:form>
		<div region='south' style="height:40px;" >
			<div class="searchBtn" style="padding-right:1px;padding-top:6px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_CloseRegularSQLFilterCasePop();"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>