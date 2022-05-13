<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!-- <div class="panel window panel-htop" style="display: block; width: 888px; left: 82px; top: 39px; z-index: 9010; height: 688px;"> -->
<!-- <div id="indexRequestPop" class="easyui-window popWin panel-noscroll panel-body panel-body-noborder window-body" style="display: block;width: 886px; height: 664px;" title=""> -->
<div id="indexRequestPop" class="" style="display: block;width: 886px; height: 664px;background-color: rgb(255, 255, 255);" title="">
	<div class="panel-header panel-header-noborder window-header" style="width: 888px;">
		<div class="panel-title" style="">인덱스 요청</div>
		<div class="panel-tool"><a href="javascript:;" onclick="Btn_OnClosePopup('indexRequestPop');" class="panel-tool-close"></a></div>
	</div>

<!-- 	<div id="indexRequestPop" class="easyui-window popWin panel-noscroll panel-body panel-body-noborder window-body" style="width: 886px; height: 664px;" title=""> -->
	
		<div class="easyui-layout" data-options="fit:true">
			<form:form method="post" id="indexRequest_form" name="indexRequest_form" class="form-inline">
				<div data-options="region:'north',split:false,border:false" style="height:55px">
					<div class="well" style="background-color:#ffffff; border: 0px;">
						<input type="hidden" id="dbid" name="dbid"/>
						
						<label>DB</label>
						<input type="text" id="db_name" name="db_name" data-options="readonly:true" class="w100 easyui-textbox"/>
						
						<label>OWNER</label>
						<select id="owner" name="owner" class="w150 easyui-combobox" tabindex="8" data-options="editable:true" required="true"></select>
						
						<label>테이블명</label>
<!-- 						<input type="text" id="table_name" name="table_name" data-options="editable:true,required:true" class="w200 easyui-textbox" onKeyUP="this.value = this.value.toUpperCase();" value="TUNING_TARGET_SQL"/> -->
						<input type="text" id="table_name" name="table_name" data-options="editable:true,required:true" class="w200 easyui-textbox" onKeyUP="this.value = this.value.toUpperCase();" value=""/>
						
						<div class="searchBtn">
	<!-- 						<a href="javascript:;" class="w150 easyui-linkbutton" onClick="Btn_StartIndexAutoDesign();"><i class="btnIcon fas fa-cogs fa-lg fa-fw"></i> 인덱스 자동 설계 시작</a> -->
	<!-- 						<a id="applyBtn" href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_AppendIndexDesignList();"><i class="btnIcon fab fa-steam fa-lg fa-fw"></i> 인덱스 설계 반영</a> -->
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="Btn_Search();"><i class="btnIcon fab fa-steam fa-lg fa-fw"></i> 검색</a>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="Btn_OnClosePopup('indexRequestPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
						</div>
					</div>
				</div>
				<div class="easyui-layout" data-options="fit:true,region:'center'" style="width:100%;min-height:300px;padding-top:10px;">
					<div id="indexRequestDiv1" class="easyui-panel" data-options="region:'north',border:false" style="width:100%;min-height:250px;padding-top:10px;">
						<table id="indexsList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="indexRequestDiv2" class="easyui-panel" data-options="region:'south',border:false" style="width:100%;min-height:50px;padding-top:10px;">
						<div class="searchBtn">
							<a href="javascript:;" class="w120 easyui-linkbutton" onClick="Btn_IndexSelectAndApply();"><i class="btnIcon far fa-save fa-lg fa-fw"></i> 선택 반영</a>
						</div>
					</div>
				</div>
				<div class="easyui-layout" data-options="fit:true,region:'south'" style="width:100%;min-height:300px;padding-top:10px;">
					<div id="indexRequestDiv3" class="easyui-panel" data-options="region:'north',border:false" style="width:100%;min-height:250px;padding-top:10px;">
						<table id="indexsRequestList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
						</table>
					</div>
					<div id="indexRequestDiv4" class="easyui-panel" data-options="region:'south',border:false" style="width:100%;min-height:50px;padding-top:10px;">
						<div class="searchBtn">
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="Btn_IndexApply();"><i class="btnIcon far fa-save fa-lg fa-fw"></i> 반영</a>
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="Btn_SelectedIndexDelete();"><i class="btnIcon fas fa-minus-circle fa-lg fa-fw"></i> 선택 삭제</a>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>
<!-- </div> -->
