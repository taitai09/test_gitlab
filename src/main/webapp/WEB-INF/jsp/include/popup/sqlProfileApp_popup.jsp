<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="sqlProfileAppPop" class="easyui-window popWin" style="background-color:#ffffff;width:500px;height:250px;">
	<div class="easyui-layout" data-options="fit:true" style="width:98%;">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;padding-right:20px;">
			<form:form method="post" id="profile_form" name="profile_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="sql_id" name="sql_id"/>
				<input type="hidden" id="plan_hash_value" name="plan_hash_value"/>
				<input type="hidden" id="sql_profile" name="sql_profile"/>
				<input type="hidden" id="isAll" name="isAll"/>
				<input type="hidden" id="save_yn" name="save_yn" value="Y"/>
				<input type="hidden" id="sqlProCnt" name="sqlProCnt" />
				<input type="hidden" id="transDBValue" name="transDBValue" />
				<input type="hidden" id="sqlProfileType" name="sqlProfileType" />
				
				<input type="hidden" id="cntPage" name="cntPage" />
				
				<span><label>▶ <span id="Btn_profile" >적용</span>대상 Profile : </label><a id="profileCnt"></a>개</span><span id="profileText" style="float: right; text-align: right;"><label>※SELECT ~ INTO 구문은 SQL Profile이 2개 적용됨</label></span>
				
				<table class="detailT" class="easyui-datagrid" data-options="fit:true,border:false">
					<colgroup>	
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:40%;">
					</colgroup>
					<tr>	
						<th>SQL ID</th>
						<th>PLAN_HASH_VALUE</th>
						<th>PROFILE명</th>
					</tr>
					<tr>
						<td><span id="sqlIdPop"></span></td>
						<td align="right"><span id="planHashValuePop"></span></td>
						<td><span id="profilePop"></span></td>
					</tr>
					<tr id="showHide">
						<td>...</td>
						<td></td>
						<td></td>
					</tr>
					
				</table>
			</form:form>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:20px;">
				<span class="transBtn">
					<label>이관대상DB</label>
					<select id="transTargetDB" name="transTargetDB" class="w150 easyui-combobox" required="true" data-options="editable:false"></select>
				</span>
				<a href="javascript:;" class="w110 easyui-linkbutton" onClick="Btn_SqlProfile();"><i class="far fa-play-circle fa-lg fa-fw"></i>SQL Profile <span id="Btn_profile" >적용</span></a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('sqlProfileAppPop');"><i style="color: black;" class="btnRIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>