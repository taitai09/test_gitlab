<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="baseLinePop" class="easyui-window popWin" style="background-color:#ffffff;width:680px;height:450px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;">
			<table id="baseLineList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
			</table>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('baseLinePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="timePop" class="easyui-window popWin" style="background-color:#ffffff;width:380px;height:150px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;">
			<label>Date</label>
			<input type="text" id="strDate" name="strDate" class="w130 datapicker easyui-datebox"/>
			<label>Time</label>		
			<input type="text" id="strTime" name="strTime" class="w80 easyui-timespinner"/>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SetDateTime();"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 확인</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('timePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>	