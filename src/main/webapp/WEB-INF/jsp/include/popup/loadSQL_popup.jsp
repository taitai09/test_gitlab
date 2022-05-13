<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="loadSqlPop" class="easyui-window popWin" style="background-color:#ffffff;width:400px;height:190px;">
	<div class="easyui-layout" data-options="fit:true">
		<form:form method="post" id="loadSQL_form" name="loadSQL_form" enctype="multipart/form-data" class="form-inline">
			<input type="hidden" id="dbid" name="dbid"/>
			<div data-options="region:'north',border:false" style="height:50px;padding:10px;">
				<label>DB</label>
<!-- 				<input type="text" id="tempDbid" name="tempDbid" data-options="disabled:false" class="w200 easyui-textbox"/> -->
				<input type="text" id="tempDbid" name="tempDbid" data-options="disabled:false" class="w200 easyui-combobox"/>
			</div>
			<div data-options="region:'center',border:false" style="padding:10px 25px;">
				<!-- <input id="uploadFile" name="uploadFile" class="easyui-filebox" style="width:100%" data-options="prompt:'적재할 SQL 파일을 선택해 주세요.',accept:'.sql,.txt'"> -->
				<input id="uploadFile" name="uploadFile" class="easyui-filebox" style="width:100%" data-options="prompt:'적재할 SQL 파일을 선택해 주세요.'">
				<div class="searchBtn" style="margin-top:20px;margin-right:-5px;">
					<a href="javascript:;" class="w90 easyui-linkbutton" onClick="loadSQLAction();"><i class="btnIcon fas fa-upload fa-lg fa-fw"></i> SQL 적재</a>
					<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('loadSqlPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
				</div>
			</div>
		</form:form>
	</div>
</div>