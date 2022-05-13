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
<div id="workTunerPop" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:555px;">
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',split:false,border:false" style="height:500px;">
			<div class="easyui-layout" data-options="border:false" style="width:970px;height:450px;margin-top:10px;margin-left:10px;">
			
				<form:form method="get" id="tmpForm" name="tmpForm" class="form-inline">
					<input type="hidden" id="wrkjob_cd" name="wrkjob_cd" value=""/>
					<input type="hidden" id="selectValue" name="selectValue" value=""/>
					<input type="hidden" id="searchValue" name="searchValue" value=""/>
				</form:form>
				
				<form:form method="post" id="submitForm" name="submitForm" class="form-inline">
					<input type="hidden" name="node_id" id="node_id"/>
					<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:470px;padding:10px;">
						<!-- 부서 -->
						<div data-options="region:'north',border:false" style="height:80px;">
							<div class="well">
									<label>업무</label>
									<label>검색조건</label>
									<select id="selectValue1" name="selectValue1" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
										<option value="">전체</option>
										<option value="1">업무명</option>
										<option value="2">업무코드</option>
									</select>
									<span id="searchValue1Span" style="visibility:hidden">
										<input type="text" id="searchValue1" name="searchValue1" data-options="disabled:false" class="w120 easyui-textbox"/>
									</span>
									<span class="searchBtnLeft">
										<a href="javascript:;" class="w70 easyui-linkbutton" onClick="searchWrkjob();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
									</span>
							</div>
						</div>
						<div style="width:450px;height:345px">
							<table id="deptTree" class="tbl easyui-treegrid" data-options="fit:true,border:false">
							</table>
						</div>	
<!-- 						<ul id="deptTree" class="easyui-tree"></ul> -->
					</div>
					<div data-options="title:'',region:'east',split:false,collapsible:false,border:true" style="width:480px;padding:10px;">
						<!-- 담당자 -->
						<div data-options="region:'north',border:false" style="height:80px;">
							<div class="well">
									<label>담당자</label>
									<label>검색조건</label>
									<select id="selectValue2" name="selectValue2" data-options="panelHeight:'auto',editable:false" class="w80 easyui-combobox">
										<option value="">전체</option>
										<option value="1" selected>사용자명</option>
										<option value="2">사용자ID</option>
										<option value="3">업무리더</option>
									</select>
									<span id="searchValue2Span" style="visibility:visible">
										<input type="text" id="searchValue2" name="searchValue2" data-options="disabled:false" class="w120 easyui-textbox"/>
									</span>
									<span class="searchBtnLeft">
										<a href="javascript:;" class="w70 easyui-linkbutton" onClick="searchUser();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
									</span>
							</div>
						</div>
						<div style="width:460px;height:345px">
							<table id="workUserList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
					</div>
				</form:form>
				
			</div>
		</div>
		<div data-options="region:'south',split:false,border:false" style="height:45px;">
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
