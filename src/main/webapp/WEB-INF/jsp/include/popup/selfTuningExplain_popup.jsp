<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="explainPlanPop" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:777px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false">
			<form:form method="post" id="explain_form" name="explain_form" class="form-inline">
				<input type="hidden" id="dbid" name="dbid"/>
				<input type="hidden" id="owner" name="owner"/>
				<input type="hidden" id="table_name" name="table_name"/>
			</form:form>
			<div id="explainPlanTab" class="easyui-tabs" data-options="fit:true,border:false">
				<div title="Table Detail" style="padding:10px;overflow-y:hidden;">
					<table id="columnInfoTbl" class="noneT">
						<colgroup>	
							<col style="width:20%;"/>
							<col style="width:20%;"/>
							<col style="width:20%;"/>
							<col style="width:55%;"/>
						</colgroup>
						<thead>
							<tr>
								<th colspan="4">
									<span class="tableTitle"></span><br/>
									-------------------------------------------------------------------
								</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table><br/>
					<table id="columnInfoList" title="▶ COLUMN INFO" class="tbl easyui-datagrid" data-options="border:true" style="width:100%;height:245px;margin-top:5px;margin-bottom:5px;border-top:1px solid black;">
					</table><br/>
					<table id="indexInfoList" title="▶ INDEX INFO" class="tbl easyui-datagrid" data-options="border:true" style="width:100%;height:150px;overflow-y:auto;border-top:2px solid;">
					</table>
				</div>
				<div title="Statistics History" style="padding:10px;">
					<table class="noneT">
						<tr>
							<th>
								<span class="tableTitle"></span><br/>
								-------------------------------------------------------------------
							</th>
						</tr>
					</table>
					<table id="statisticsList" title="▶ Statistics History" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:615px;margin-top:5px;">
					</table>
				</div>
				<div title="Access Path" style="padding:10px;">
					<table id="accessPathList" class="tbl easyui-datagrid" data-options="fit:false,border:false" style="width:100%;height:645px;">
					</table>
				</div>
				<div title="Change History" style="padding:10px;">
					<table id="columnHistoryList" title="▶ COLUMN HISTORY" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:320px;margin-bottom:10px;">
					</table><br/>
					<table id="indexHistoryList" title="▶ INDEX HISTORY" class="tbl easyui-datagrid" data-options="border:false" style="width:100%;height:315px;">
					</table>				
				</div>
			</div>			
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:35px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-cancel'" onClick="Btn_OnClosePopup('explainPlanPop');">닫기</a>
			</div>
		</div>
	</div>
</div>
