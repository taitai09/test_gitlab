<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="snapListPop" class="easyui-window popWin" style="background-color:#ffffff;width:950px;height:570px;">
	<div class="easyui-layout" style="height:500px;margin:10px;">
		<div data-options="region:'north',border:false" style="height:80px;">
			<div class="well">
				<form:form method="post" id="snapList_form" name="snapList_form" class="form-inline">
					<input type="hidden" id="dbid" name="dbid"/>
					<input type="hidden" id="snap_id" name="snap_id"/>
					<input type="hidden" id="start_snap_id" name="start_snap_id"/>
					<input type="hidden" id="end_snap_id" name="end_snap_id"/>
					<input type="hidden" id="instance_number" name="instance_number"/>
					<label>DB</label>
					<input type="text" id="dbName" name="dbName" data-options="disabled:true" class="w130 easyui-textbox"/>
					<label>INTERVAL_TIME</label>
					<input type="text" id="strStartDt" name="strStartDt" value="${startDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/> ~
					<input type="text" id="strEndDt" name="strEndDt" value="${nowDate}" data-options="panelHeight:'auto',editable:false" required="true" class="w130 datapicker easyui-datebox"/>
					<span class="searchBtnLeft">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="searchStartSnapIdList();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
					</span>
					<div class="searchBtn">
						<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('snapListPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
					</div>
				</form:form>
			</div>
		</div>		
		<div data-options="title:'START_SNAP_ID',region:'west',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="startSnapIdList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div data-options="region:'center',border:false" style="width:10px;">
		</div>
		<div data-options="title:'END_SNAP_ID',region:'east',split:false,collapsible:false,border:true" style="width:440px;padding:10px;">
			<table id="endSnapIdList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
	</div>
</div>