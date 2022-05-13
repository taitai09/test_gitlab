<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="processHistoryPop" class="easyui-window popWin" style="background-color:#ffffff;width:680px;height:450px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
			<table id="processHistoryList" class="tbl easyui-datagrid" data-options="border:false" style="width:650px;height:330px;">
			</table>
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('processHistoryPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="workTunerPop" class="easyui-window popWin" style="background-color:#ffffff;width:400px;height:350px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,border:false" style="height:295px;">
			<div class="easyui-layout" data-options="border:false" style="width:360px;height:240px;margin-top:10px;margin-left:10px;">
				<div data-options="title:'부서',region:'west',split:false,collapsible:false,border:true" style="width:155px;padding:10px;">
					<ul id="deptTree" class="easyui-tree"></ul>
				</div>
				<div data-options="region:'center',border:false" style="width:10px;"></div>
				<div data-options="title:'담당자',region:'east',split:false,collapsible:false,border:true" style="width:195px;padding:5px;">
					<table id="workUserList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
					</table>
				</div>
			</div>
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:55px;">
			<div class="searchBtn" style="margin-top:5px;padding-right:10px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('workTunerPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
<div id="perCheckPop" class="easyui-window popWin" style="background-color:#ffffff;width:700px;height:330px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,border:false" style="height:295px;">
			<table class="detailT" style="width:97%">
				<colgroup>
					<col style="width:15%;"/>
					<col style="width:17%;"/>
					<col style="width:15%;"/>
					<col style="width:21%;"/>
					<col style="width:15%;"/>
					<col style="width:17%;"/>
				</colgroup>
				<tr>
					<th>튜닝번호</th>
					<td><span id="chk_tuning_no"></span></td>
					<th>요청(선정)일자</th>
					<td><span id="chk_tuning_request_dt"></span></td>
					<th>튜닝요청구분</th>
					<td><span id="chk_choice_div_cd_nm"></span></td>
				</tr>
				<tr>
					<td colspan="6"><b>성능 점검  소스</b></td>
				</tr>
				<tr>
					<td colspan="6">
						<form name="submit_form" id="submit_form">
							<input type="hidden" name="bfac_chk_source" id="bfac_chk_source"/>
							<textarea name="bfac_chk_source_text" id="bfac_chk_source_text" rows="15" style="margin-top:5px;width:98%;height:140px"></textarea>
						</form>
					</td>
				</tr>
			</table>
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:55px;">
			<div class="searchBtn" style="margin-top:5px;padding-right:10px;">
				<a href="javascript:;" class="w100 easyui-linkbutton" data-options="iconCls:'icon-req'" onClick="Btn_SavePreCheck();">사전점검요청</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('perCheckPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
