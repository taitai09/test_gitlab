<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="selftunSqlPop" class="easyui-window popWin" style="background-color:#ffffff;width:1000px;height:480px;">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="margin-top:10px;">
			<input type="hidden" id="bindCnt" name="bindCnt" />
			<table id="testPopTbl" class="detailT" style="width:98%">
				<colgroup>	
					<col style="width:55%;"/>
					<col style="width:45%;"/>
				</colgroup>
				<tr>
					<th>SQL TEXT</th>
					<td rowspan="2" style="vertical-align:top">
						<table id="bindPopTbl" class="detailT" style="margin-top:5px;margin-bottom:5px;width:97%;">
							<colgroup>	
								<col style="width:16%;"/>
								<col style="width:28%;"/>
								<col style="width:28%;"/>
								<col style="width:28%;"/>
							</colgroup>
							<thead>
								<tr>
									<th>순번</th>
									<th>변수명</th>
									<th>변수타입</th>
									<th>변수값</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</td>							
				</tr>
				<tr>
					<td><textarea name="sql_text_pop" id="sql_text_pop" rows="30" style="margin-top:5px;width:98%;height:300px" wrap="off" readonly></textarea></td>
				</tr>
			</table>		
		</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;">
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w130 easyui-linkbutton" onClick="Btn_SetSqlInfo();"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 셀프테스트 다시하기</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('selftunSqlPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>