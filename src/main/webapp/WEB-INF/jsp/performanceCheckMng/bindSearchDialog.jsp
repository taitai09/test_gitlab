<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="bindSearchDialog" class="easyui-dialog" style="padding:0px;">
	<div class="easyui-layout" data-options="fit:true,border:false" style="width:100%;height:300px">
		<div data-options="region:'center',split:false,collapsible:false,border:false" style="height:260px;padding:10px;overflow-y:hidden;">
			<div id="bindSearchTabs" class="easyui-tabs" data-options="border:false" style="width:100%;">
				<div id="programInfo" title="성능 점검 이력" style="padding:0px;height:260px;">
					<div class="easyui-layout" data-options="border:false" style="width:100%;height:260px;">
						<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:110px;padding:5px 5px 10px 5px;">
							<div id="bindSearchList1" style="height:auto;">
							</div>
						</div>
						<div data-options="region:'center',split:false,collapsible:false,border:false" style="width:100%;height:150px;padding:10px 5px 5px 5px;">
							<div id="bindSearchList2">
								<table id="bindSearchList2Table" class="detailT" style="margin-top:5px;margin-bottom:5px;width:98%;height:auto;">
									<colgroup>
										<col style="width:7%">
										<col style="width:32%">
										<col style="width:30%">
										<col style="width:32%">
									</colgroup>
									<thead>
									<tr>
										<th class='tar font11px'>순번</th>
										<th class='tar font11px'>변수명</th>
										<th class='tar font11px'>변수타입</th>
										<th class='tar font11px'>변수값</th>
									</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<div id="perfCheck" title="SQL 수행 이력" style="padding:0px;height:260px;">
					<div class="easyui-layout" data-options="border:false" style="width:100%;height:260px;">
						<div data-options="region:'north',split:false,collapsible:false,border:false" style="height:110px;padding:5px 5px 10px 5px;">
							<div id="bindSearchList3" style="height:auto;">
							</div>
						</div>
						<div data-options="region:'center',split:false,collapsible:false,border:false" style="height:150px;padding:10px 5px 5px 5px;">
							<div id="bindSearchList4">
								<table id="bindSearchList4Table" class="detailT" style="margin-top:5px;margin-bottom:5px;width:98%;height:auto;">
									<colgroup>
										<col style="width:7%">
										<col style="width:32%">
										<col style="width:30%">
										<col style="width:32%">
									</colgroup>
									<thead>
									<tr>
										<th class='tar font11px'>순번</th>
										<th class='tar font11px'>변수명</th>
										<th class='tar font11px'>변수타입</th>
										<th class='tar font11px'>변수값</th>
									</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div data-options="region:'south',split:false,collapsible:false,border:false" style="height:40px;padding:5px;">
			<div class="searchBtn">
				<a href="javascript:;" class="w60 easyui-linkbutton" onClick="fnBinding();"><i class="btnIcon fas fa-link fa-lg fa-fw"></i> 바인딩</a>
				<a href="javascript:;" class="w60 easyui-linkbutton" onClick="fnClose();"><i class="btnIcon fa fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>
