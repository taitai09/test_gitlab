<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>

<!-- <!DOCTYPE html> -->
<!-- <html lang="ko"> -->
<!-- <head> -->
<!-- 	<meta charset="utf-8" /> -->
<!-- 	<meta http-equiv="content-type" content="text/html; charset=UTF-8" /> -->
<!-- 	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> -->
<!-- 	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" /> -->
<!-- 	<meta http-equiv="cleartype" content="on" />     -->
<!-- </head> -->
<!-- <body> -->
<div id="dashboardV2LeftEasyui" class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
	<div data-options="iconCls:'fas fa-caret-square-right',title:'DB 점검(${max_check_day_dash})',region:'north',split:false,collapsible:false,border:true" style="width:98%;height:34%;padding:5px;">
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
			<!-- DB 점검 현황 요약 -->
			<div id="dbCheckResultTotal" data-options="region:'west'" title="" style="width:15%;height:100%;padding:2px;">
				<div class="class_1"><div id="class_1" class="title" onclick="reloadDbCheckResultGrid01('3')">0</div></div>
				<div class="class_2"><div id="class_2" class="title" onclick="reloadDbCheckResultGrid01('2')">0</div></div>
				<div class="class_3"><div id="class_3" class="title" onclick="reloadDbCheckResultGrid01('1')">0</div></div>
				<div class="class_4"><div id="class_4" class="title" onclick="reloadDbCheckResultGrid01('0')">0</div></div>
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:true" style="width:35%;height:100%;padding:2px;">
				<table id="dbCheckResultGrid01" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="region:'east',split:false,collapsible:false,border:true" style="width:45%;height:100%;padding:2px;">
				<table id="dbCheckResultGrid02" class="tbl easyui-datagrid" data-options="fit:true,border:false,fitColumn:false">
					<tbody><tr><td>&nbsp;</td></tr></tbody>
				</table>
			</div>
		</div>
	</div>
	<div data-options="iconCls:'fas fa-caret-square-right',title:'SQL/APP 점검(${gather_day_dash})',region:'center',split:false,collapsible:false,border:true" style="width:98%;height:33%;padding:5px;">
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
			<div data-options="region:'west',split:false,collapsible:false,border:true" style="width:17%;height:100%;padding:2px;">
				<table id="sqlAppCheckGrid01" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:true" style="width:49%;height:100%;padding:2px;">
				<table id="sqlAppCheckGrid02" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="region:'east',split:false,collapsible:false,border:false" style="width:49%;height:100%;">
				<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;padding:2px;">
					<div id="sqlAppCheckChart" title="" style="width:100%;height:100%;padding-top:0px;">
					</div>
				</div>
			</div>
		</div>
	</div>
	<div data-options="iconCls:'fas fa-caret-square-right',title:'TOP SQL(${check_date_topsql_diag_summary})',region:'south',split:false,collapsible:false,border:true" style="width:98%;height:34%;padding:5px;">
		
		<div class="easyui-layout" data-options="border:false" style="width:100%;height:100%;">
			<div data-options="region:'west',split:false,collapsible:false,border:true" style="width:17%;height:100%;padding:2px;">
				<table id="topSqlGrid01" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			<div data-options="region:'center',split:false,collapsible:false,border:true" style="width:49%;height:100%;padding:2px;">
				<table id="topSqlGrid02" class="tbl easyui-datagrid" data-options="fit:true,border:false">
				</table>
			</div>
			
			<div data-options="region:'east',split:false,collapsible:false,border:true" style="padding:5px;width:49%;height:100%;">
				<div id="topSqlTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:100%;">
					<div id="tab_topSql1" title="3개월 발생추이" style="padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;padding:2px;">
							<div id="topSqlChart" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
					<div id="tab_topSql2" title="1개월 발생빈도 TOP" style="padding:5px;">
						<div class="easyui-panel" data-options="border:true" style="width:100%;height:100%;padding:2px;">
							<div id="topSqlChart2" title="" style="width:100%;height:100%;padding-top:0px;">
							</div>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
</div>
<!-- </body> -->
<!-- </html> -->