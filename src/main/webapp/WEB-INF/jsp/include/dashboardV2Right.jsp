<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 *
 **********************************************************/
%>
	<style>
 		.easyuiTitleBackColor{ background: #fff; }
 		.borderBottomStyle{border-bottom-style:none};
		.btn
		{
		background-color: white !important;
		background-image: none;
		border:0px;
		}
/* 		.x-btn-pressed{ */
		.custom_button_pressed{
			color: #ff0000 !important;
/* 			background-color:#497aa2 !important;  */
			background-color:#fc1926 !important; 
		}
		.button_default{
            background-color: #60a2d9;
            color: #ff0000;
            border-right: 1px solid #007aff;
            border-left: 1px solid #007aff;
            border-top: 1px solid #007aff;
            border-bottom: 1px solid #007aff;
            font-size: 15px;		
		}
		.my-btn {
		    background: #60a2d9
		}
		
		.my-btn-pressed {
		    background: #007aff;
		}
		.filterbar-segmented-button {
		  padding-left: 18%;
		  color: blue;
		}
		
		.filterbar-segmented-button.filterbar-segmented-button-pressed{
		    background-color: blue;
		    color: white;
		}
		.newSqlAppCls{
			color:#ffffff;
		}
	</style>

	<div id="dashboardV2RightEasyui" class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
		<div data-options="iconCls:'fas fa-caret-square-right',title:'자원 한계점 예측(3월내 한계도래)',region:'north',split:false,collapsible:false,border:true" style="width:98%;height:34%;padding:5px;">
<%-- 		<div data-options="iconCls:'fas fa-caret-square-right',title:'<center>자원 한계점 예측(3월내 한계도래)</center>',region:'north',split:false,collapsible:false,border:false" style="width:98%;height:33%;padding:5px;"> --%>
<%-- 		<div data-options="title:'<center>자원 한계점 예측(3월내 한계도래)</center>',region:'north',split:false,collapsible:false,border:false,headerCls:'easyuiTitleBackColor borderBottomStyle'" style="width:98%;height:33%;padding:5px;"> --%>
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
					<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:50%;height:100%;padding:5px;">
<!-- 						<div id="cpuMmTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:100%;"> -->
								<table id="resourceLimitPointPrediction" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true,fitColumn:false">
									<tbody><tr><td colspan="7">&nbsp;</td></tr>
									</tbody>
								</table>							
<!-- 						</div> -->
					</div>
					<div data-options="region:'center',split:false,collapsible:false,border:true" style="width:50%;height:100%;">
						<div style="position:relative;top:5px;margin:5px 0px 0px 52px">
							<input class="easyui-radiobutton" id="cpuMemory1" name="cpuMemory" value="CPU" checked/> CPU &nbsp;&nbsp;&nbsp;
							<input class="easyui-radiobutton" id="cpuMemory2" name="cpuMemory" value="MEMORY"> SGA+PGA
						</div>	
						<div class="easyui-panel" data-options="border:false" style="margin-top:5px;width:100%;height:176px;padding:0px 5px;">
							<div id="resourceLimitPointPredictionChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
				</div>			
		</div>
		<div data-options="iconCls:'fas fa-caret-square-right',title:'Tablespace/Sequence 한계점 예측(3월내 한계도래)',region:'center',split:false,collapsible:false,border:true" style="width:98%;height:33%;padding:5px;">
<%-- 		<div data-options="iconCls:'fas fa-caret-square-right',title:'<center>Tablespace/Sequence 한계점 예측(3월내 한계도래)</center>',region:'center',split:false,collapsible:false,border:false" style="width:98%;height:33%;padding:5px;"> --%>
<%-- 		<div data-options="title:'<center>Tablespace/Sequence 한계점 예측(3월내 한계도래)</center>',region:'center',split:false,collapsible:false,border:false,headerCls:'easyuiTitleBackColor borderBottomStyle'" style="width:98%;height:33%;padding:5px;"> --%>
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
				
					<div data-options="region:'west',split:false,collapsible:false,border:true" style="padding:5px;width:50%;height:100%;">
					
						<div id="tbsSeqTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:100%;">
							<div id="tab_tbs" title="Tablespace" style="padding:5px;">
								<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
									<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:126px;height:100%;padding:5px;">
										<table id="tablespacePresentConditionDBList" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
										</table>						
									</div>
									<div data-options="title:'',region:'center',split:false,collapsible:false,border:true" style="width:69%;height:100%;padding:5px;">
										<table id="tablespacePresentCondition" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
										</table>						
									</div>
								</div>
							</div>
							<div id="tab_seq" title="Sequence" style="padding:5px">
								<div class="easyui-layout" data-options="border:true" style="width:100%;height:100%;">
									<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:30%;height:100%;padding:5px;">
										<table id="sequencePresentConditionDBList" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
										</table>						
									</div>
									<div data-options="title:'',region:'center',split:false,collapsible:false,border:true" style="width:70%;height:100%;padding:5px;">
										<table id="sequencePresentCondition" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
										</table>						
									</div>
								</div>
							</div>
						</div>

					</div>					
					<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:50%;height:100%;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;padding:0px 5px;">
								<div id="tablespaceSequencePresentConditionChart" title="" style="width:100%;height:100%;padding-top:0px;">
								</div>
						</div>
					</div>
				</div>
		</div>
		
		<div data-options="iconCls:'fas fa-caret-square-right',title:'신규SQL/App Timeout 예측',region:'south',split:false,collapsible:false,border:true" style="width:98%;height:34%;padding:5px;">
<%-- 		<div data-options="iconCls:'fas fa-caret-square-right',title:'<center>신규SQL/App Timeout 예측</center>',region:'south',split:false,collapsible:false,border:false" style="width:98%;height:33%;padding:5px;"> --%>
<%-- 		<div data-options="title:'<center>신규SQL/App Timeout 예측</center>',region:'south',split:false,collapsible:false,border:false,headerCls:'easyuiTitleBackColor borderBottomStyle'" style="width:98%;height:33%;padding:5px;"> --%>
				<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
					<div data-options="title:'',region:'west',split:false,collapsible:false,border:true" style="width:50%;height:100%;padding:5px;">

						<div id="sqlAppTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:100%;">
							<div id="tab_sql" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SQL&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" style="padding:5px;">
								<table id="newSQLTimeoutPrediction" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
								</table>							
							</div>
							<div id="tab_app" title="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;App&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" style="padding:5px">
								<table id="newAppTimeoutPrediction" class="tbl easyui-datagrid" data-options="fit:true,border:false,nowrap:true">
								</table>							
							</div>
						</div>						
					</div>
					<div data-options="region:'center',split:false,collapsible:false,border:true" style="width:50%;height:100%;">
						<div class="easyui-panel" data-options="border:false" style="width:100%;height:100%;padding:0px 5px;">
							<div id="newSQLAppTimeoutPredictChart" title="" style="width:100%;height:100%;padding-top:0px;border:none;">
							</div>
						</div>
					</div>
				</div>
		</div>
		
<%-- 		<div data-options="title:'<center>신규SQL/App Timeout 예측</center>',region:'south',split:false,collapsible:false,border:false,headerCls:'easyuiTitleBackColor borderBottomStyle'" style="width:98%;height:33%;padding:5px;"> --%>
<!-- 			<div class="easyui-layout" data-options="border:false" style="width:100%;min-height:650px"> -->
<!-- 				<div id="topTablespaceChart" data-options="title:'▶ TOP Tablespace Growth',region:'center',split:false,collapsible:false,border:false" style="padding:5px;"> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 		</div> -->
		
	</div>
