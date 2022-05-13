<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="projectSqlIdfyConditionPop" class="easyui-window popWin" style="background-color:#ffffff;width:700px;height:640px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:10px;margin-left:5px;">
			<form:form method="post" id="projectSqlIdfyConditionPop_form" name="projectSqlIdfyConditionPop_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="project_id" name="project_id"/>
				<input type="hidden" id="crud_flag" name="crud_flag"/>
				<input type="hidden" id="owner" name="owner"/>
				<input type="hidden" id="table_name" name="table_name"/>
				<input type="hidden" id="module" name="module"/>
				<input type="hidden" id="tableOwnerArry" name="tableOwnerArry"/>
				<input type="hidden" id="tableNameArry" name="tableNameArry"/>
				<input type="hidden" id="project_check_target_type_cd" name="project_check_target_type_cd"/>
				<table class="detailT" style="width:96%;">
					<colgroup>
						<col style="width:20%;">
						<col style="width:30%;">
						<col style="width:20%;">
						<col style="width:30%;">
					</colgroup>
					<tr>
						<th>DB</th>
						 <td class="ltext"><select id="selectComboOrigin" name="selectComboOrigin" data-options="editable:false" class="w150 easyui-combobox" required="true"></select></td>
						<th>OWNER</th>
						<td>
							<select id="selTableOwner" name="selTableOwner" data-options="editable:false" required="true" class="w130 easyui-combobox"></select>
						</td>
					</tr>
				</table>
				
				<table style="width:96%;margin-left:10px; table-layout: fixed;height:450px;">
					<tr>
						<td>
							<div class="easyui-layout" data-options="border:false" style="width:100%;height:410px;">
								<div data-options="region:'north',border:false" style="height:35px;">
									<div class="easyui-layout" data-options="fit:true,border:false">
										<div data-options="region:'west',border:false" style="width:42%;">
											<span style="font-size:12px;">▶ 테이블 목록</span>&nbsp;&nbsp;&nbsp;&nbsp;
										</div>
										<div data-options="region:'center',border:false">
										</div>
										<div data-options="region:'east',border:false" required="true" style="width:50%;">
											<span style="font-size:12px;">▶ SQL 자동성능점검 대상 테이블 목록</span>
										</div>
									</div>
								</div>
								<div data-options="region:'center',border:false" style="height:400px;">
									<div class="easyui-layout" data-options="fit:true,border:false">
										<div data-options="region:'west'" style="width:42%;">
											<table id="selectList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
										<div data-options="region:'center', border:false">
											<div style="width:100%;text-align:center;margin-top:100px;">
												<a href="javascript:;" id="addBtn" class="easyui-linkbutton" data-options="iconCls:'icon-next'" onClick="Btn_AddTarget();"></a>
											</div>
											<div style="width:100%;text-align:center;margin-top:30px;">
												<a href="javascript:;" id="removeBtn" class="easyui-linkbutton" data-options="iconCls:'icon-back'" onClick="Btn_RemoveTarget();"></a>
											</div>
										</div>
										<div data-options="region:'east'" style="width:50%;">
											<table id="targetList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
											</table>
										</div>
									</div>
								</div>
							</div>
						</td>
					</tr>
				</table>
			</form:form>
			<span style="color:#ff0000;font-weight:700;">&nbsp;&nbsp;※ SQL 자동성능점검 대상 테이블 목록은 "저장" 후에 DB에 저장됩니다.</span> 
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" id="saveObjectBtn" class="w80 easyui-linkbutton" onClick="Btn_BatchInsertSave();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('projectSqlIdfyConditionPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>