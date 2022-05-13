<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="sqlProfilePop" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:230px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
			<form:form method="post" id="profile_form" name="profile_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="sql_id" name="sql_id"/>
				<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
				<input type="hidden" id="sql_profile" name="sql_profile"/>
				<input type="hidden" id="save_yn" name="save_yn" value="Y"/>
				<table class="detailT" style="width:91%;">
					<colgroup>	
						<col style="width:30%;">
						<col style="width:70%;">
					</colgroup>
					<tr>	
						<th>SQL ID</th>
						<td><input type="text" id="sqlId" name="sqlId" data-options="readonly:'true'" class="w250 easyui-textbox"/></td>
					</tr>
					<tr>	
						<th>PLAN_HASH_VALUE</th>
						<td><input type="text" id="planHashValue" name="planHashValue" data-options="readonly:'true'" class="w250 easyui-textbox"/></td>
					</tr>
					<tr>	
						<th>PROFILE명</th>
						<td><input type="text" id="sqlProfile" name="sqlProfile" class="w250 easyui-textbox"/></td>
					</tr>
				</table>		
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SaveSqlProfile();"><i class="far fa-play-circle fa-lg fa-fw"></i> SQL Profile 적용</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('sqlProfilePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>