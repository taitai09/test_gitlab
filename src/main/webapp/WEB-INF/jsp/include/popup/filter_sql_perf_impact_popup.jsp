<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style type="text/css">
	#filterSqlPopup .popup_text {
		margin:15px 15px 0px 15px;
		font-size:12px;
	}
	#filterSqlPopup .popup_area {
		margin:10px 15px 0px 15px;
		font-size:12px;
	}
	
	#filterSqlPopup .radiobutton-readonly,
	#filterSqlPopup .checkbox-readonly,
	#filterSqlPopup .textbox-invalid {
		border-color: #ccc9c7;
	}
	#filterSqlPopup .radiobutton-readonly .radiobutton-inner,
	#filterSqlPopup .checkbox-readonly.checkbox-checked {
		background-color: #ccc9c7;
	}
	
</style>
<div id="filterSqlPopup" class="easyui-window popWin" style="background-color:#ffffff;width:494px;height:500px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="filter_sql_form" name="assign_form" class="form-inline">
			<div class="popup_text">
				<snap>
				* Filter 조건으로 추가할 컬럼과 연산자는 [추가]버튼을 이용하세요.<br/>
				* 콤보박스 컬럼 이외의 다른 컬럼을 조건으로 사용하면 SQL  오류가 발생할 수 있습니다.
				</snap>
			</div>
			
			<div class="popup_text">
				<select id="condition_1" name="condition_1" data-options="panelHeight:'auto',editable:false" class="w230 easyui-combobox">
					<option value="">선택</option>
					<option value="MODULE">MODULE</option>
					<option value="ACTION">ACTION</option>
					<option value="PARSING_SCHEMA_NAME">PARSING_SCHEMA_NAME</option>
					<option value="SQL_TEXT">SQL_TEXT</option>
					<option value="SQL_ID">SQL_ID</option>
				</select>
				<select id="condition_2" name="condition_2"  data-options="panelHeight:'auto',editable:false" class="w130 easyui-combobox">
					<option value="">선택</option>
				</select>
				<span style="margin-left:1px;">
					<a id= 'btn_Filter_Add' href="javascript:;" class="w80 easyui-linkbutton" onClick="addSql();"><i class="btnIcon fas fa-plus-circle fa-lg fa-fw"></i> 추가</a>
				</span>
			</div>
			
			<div class="popup_area">
				<input id="filter_sql" name="sql_filter" class="easyui-textbox"  data-options="multiline:true" style="width:450px;height:300px;" > </textarea>
			</div>
			<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:46px;">
				<div class="searchBtn" style="padding-right:24px;">
					<a id='btn_Filter_Save' href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveSql();"><i class="btnIcon fas fa-edit fa-lg fa-fw"></i> 저장</a>
					<a id='btn_close' href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnCloseSqlPopup('filterSqlPopup');"><i class="fa fa-times fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>
