<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<div id="loadExplainPlanPop" class="easyui-window popWin" style="background-color:#ffffff;width:1600px;height:600px;">
	<div class="easyui-layout" data-options="fit:true" style="height:100%;">
		<form:form method="post" id="loadExplainPlan_form" name="loadExplainPlan_form" enctype="multipart/form-data" class="form-inline">
			<input type="hidden" id="dbid" name="dbid"/>
		</form:form>
		<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:515px">
			<div data-options="region:'west',border:false" style="width:50%;height:100%;padding-right:5px;">
				<div class="easyui-tabs" data-options="fit:true,plain:true,border:false">
					<div class="tabTxt" title="SQL TEXT" style="width:100%;padding:10px;height:400px;">
						<textarea name="textArea" id="textArea" style="margin-top:5px;padding:5px;width:98%;height:85%" wrap="off" readonly></textarea>
						<div class="searchBtn" style="margin-top:10px;">
							<a href="javascript:;" class="w100 easyui-linkbutton" onClick="copy_to_sqlId();">SQL ID 복사</a>
							<a href="javascript:;" class="w60 easyui-linkbutton" onClick="copy_to_clipboard();">SQL 복사</a>
						</div>
					</div>
				</div>
			</div>
			
			<div data-options="region:'center',split:false,border:false" style="width:100%;height:100%;">
				<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false" style="width:100%;height:100%;">
					<div title="Bind Value" class="tabGrid" style="width:100%;padding:10px;height:100%;">
						<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:400px">
							<table id="bindValueList" class="tbl easyui-datagrid" data-options="fit:true,border:false">
							</table>
						</div>
						
<!-- 						<div class="searchBtn" style="margin-top:10px;"> -->
<!-- 							<a href="javascript:;" id="bindBtn" class="w80 easyui-linkbutton" onClick="Btn_NextBindSearch();"><i class="btnIcon fas fa-arrow-alt-circle-right fa-lg fa-fw"></i> 다음</a> -->
<!-- 						</div> -->
					</div>
					<div title="Before Plan" class="tabGrid" style="padding:5px;">
						<ul id="beforeTreePlan" class="easyui-tree" style="padding:5px;"></ul>
					</div>
					<div title="After Plan" class="tabGrid" style="padding:5px;">
						<ul id="afterTreePlan" class="easyui-tree" style="padding:5px;"></ul>
					</div>
				</div>
			</div>
		</div>
		<div region='south' style="height:46px;" >
			<div class="inlineBtn" style="padding-left:1490px;padding-top:8px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_CloseLoadExplainPlanPop();"><i class="btnRBIcon fas fa-times fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>