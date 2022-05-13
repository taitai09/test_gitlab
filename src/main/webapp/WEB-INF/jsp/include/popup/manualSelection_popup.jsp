<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="manualSelectionHistoryPop" class="easyui-window popWin" style="background-color:#ffffff;width:700px;height:450px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
			<table id="manualHistoryList" class="tbl easyui-datagrid" data-options="border:false" style="width:97%;height:330px;">
			</table>		
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('manualSelectionHistoryPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>