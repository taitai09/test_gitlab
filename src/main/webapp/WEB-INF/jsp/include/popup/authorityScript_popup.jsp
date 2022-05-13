<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div id="authorityScriptPop" class="easyui-window popWin" style="background-color:#ffffff;width:420px;height:410px;">
	<div class="easyui-layout" style="height:350px;margin:5px;">
		<form:form method="post" id="authorityScript_form" name="authorityScript_form" class="form-inline">
			<div data-options="region:'north',border:false" style="width:100%;height:150px;">
				<div class="well">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="authority_sql" name="authority_sql"/>
					
					<div>
						<label>DB</label>
						<input type="text" id="db" name="db" value="" class="w100 easyui-textbox" readonly="true"/>
						<label>Object Owner</label>
						<select id="authorityUserName" name="authorityUserName" data-options="editable:false" class="w130 easyui-combobox" required="true"></select>
					</div>
					<div class="multi" style="margin-left:15px;">
						<input id="checkTable" name="checkTable" class="easyui-checkbox" value="TABLE" label="Table" labelPosition="after">
						<input id="checkView" name="checkView" class="easyui-checkbox" value="VIEW" label="View" labelPosition="after">
						<input id="checkSequence" name="checkSequence" class="easyui-checkbox" value="SEQUENCE" label="Sequence" labelPosition="after">
					</div>
					<div class="multi" style="margin-left:15px;">
						<input id="checkFunction" name="checkFunction" class="easyui-checkbox" value="FUNCTION" label="Function" labelPosition="after">
						<input id="checkProcedure" name="checkProcedure" class="easyui-checkbox" value="PROCEDURE" label="Procedure" labelPosition="after">
						<input id="checkPackage" name="checkPackage" class="easyui-checkbox" value="PACKAGE" label="Package" labelPosition="after">
					</div>
					<div class="multi">
						<span class="searchBtnLeft">
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_OnCreate();"><i class="btnIcon fas fa-plus-square fa-lg fa-fw"></i> 권한스크립트 생성</a>
							<a href="javascript:;" id="scriptCopyBtn" class="w120 easyui-linkbutton" data-clipboard-action="copy" data-clipboard-target="#scriptView"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 권한스크립트 복사</a>
							<a href="javascript:;" class="w50 easyui-linkbutton" onClick="Btn_OnClosePopup('authorityScriptPop');"><i class="btnIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
						</span>
					</div>
				</div>
			</div>
			<div class="easyui-layout" data-options="region:'center',border:false" style="width:100%;min-height:5px">
				<textarea name="scriptView" id="scriptView" style="margin-top:5px;padding:5px;width:94%;height:83%" wrap="off" readonly></textarea>
			</div>
		</form:form>
	</div>
</div>