<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="licenceCheckPop" class="easyui-window popWin" title="Licence" style="background-color:#ffffff;width:600px;height:350px;" >
	<div class="easyui-layout" data-options="fit:true" style="width:98%;">
		
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:5px;margin-left:10px;padding-right:20px;">
			<form:form method="post" id="profile_form" name="profile_form" class="form-inline">
				<input type="hidden" id="licence_id" name="licence_id"/>
				
				<div class="easyui-layout" style="margin-bottom:10px;">
					<span><label>계약된 CPU 개수를 초과했습니다.</label><br><label>1개월 후 Open-POP이 정상적으로 작동되지 않습니다. 계약을 갱신하시기 바랍니다. </label></span>
				</div>
				<div class="easyui-layout tuningPerf" data-options="border:false" style="width:100%;min-height:200px">
					<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
						<table id="licenceTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:40px;">
			<span class="searchBtnLeft2" style="padding-left:10px;">
				<input id="week_yn" name="week_yn" class="easyui-checkbox"><label>1주일 간 보지 않기</label>
			</span>
			<div class="searchBtn" style="padding-right:20px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('licenceCheckPop');"><i style="color: black;" class="btnRIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>

<div id="noLicenceCheckPop" class="easyui-window popWin" title="Licence" style="background-color:#ffffff;width:500px;height:350px;">
	<div class="easyui-layout" data-options="fit:true" style="width:98%;">
		
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:5px;margin-left:10px;padding-right:20px;">
			<form:form method="post" id="profile_form" name="profile_form" class="form-inline">
				<div class="easyui-layout" style="margin-bottom:10px;">
					<span><label>라이선스 CPU 개수가 셋팅 되지 않았습니다.</label><br><label>확인 후 셋팅 바랍니다. </label></span>
				</div>
				<div class="easyui-layout tuningPerf" data-options="border:false" style="width:100%;min-height:200px">
					<div data-options="region:'center',split:false,collapsible:true,border:false" style="width:100%;height:99%;">
						<table id="noLicenceTableList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
				</div>
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:40px;">
			<div class="searchBtn" style="padding-right:20px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('noLicenceCheckPop');"><i style="color: black;" class="btnRIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>