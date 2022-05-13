<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="batchTuningBundlePop" class="easyui-window popWin" style="background-color:#ffffff;width:440px;height:280px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="batchTuningBundle_form" name="batchTuningBundle_form" class="form-inline">
			<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:20px;margin-left:10px;">
				<input type="hidden" id="tuning_end_why_cd" name="tuning_end_why_cd"/>
				<input type="hidden" id="autoChoiceCondNoArray" name="autoChoiceCondNoArray"/>
				<input type="hidden" id="choiceTmsArray" name="choiceTmsArray"/>
				<label>튜닝종료사유</label>
				<select id="selectTuningEnd" name="selectTuningEnd" data-options="panelHeight:'auto',editable:false" class="w120 easyui-combobox"></select>
				<textarea name="tuning_end_why" id="tuning_end_why" rows="15" style="margin-top:5px;width:92%;height:120px"></textarea>		
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
				<div class="searchBtn" style="padding-right:35px;">
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnSave();"><i class="btnIcon fas fa-save fa-lg fa-fw"></i> 확인</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('batchTuningBundlePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 취소</a>
				</div>
			</div>
		</form:form>
	</div>
</div>	
