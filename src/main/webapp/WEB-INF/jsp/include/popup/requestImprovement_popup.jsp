<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <div id="baseLinePop" class="easyui-window popWin" style="background-color:#ffffff;width:680px;height:450px;"> -->
<!-- 	<div class="easyui-layout" data-options="fit:true"> -->
<!-- 		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;"> -->
<!-- 			<table id="baseLineList" class="tbl easyui-datagrid" data-options="fit:true,border:false"> -->
<!-- 			</table> -->
<!-- 		</div> -->
<!-- 		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;"> -->
<!-- 			<div class="searchBtn" style="padding-right:35px;"> -->
<!-- 				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('baseLinePop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->
<div id="saveInitSettingPop" class="easyui-window popWin" style="background-color:#ffffff;width:540px;height:270px; !important">
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="plain:true,region:'center',split:false,border:false" style="padding:20px 10px 10px 10px;">
			<form:form method="post" id="popup_form" name="popup_form" class="form-inline">
					<input type="hidden" id="user_id" name="user_id" value="${user_id}"/>
					
					<table class="noneT_popup">
						<colgroup>	
							<col style="width:10%;">
							<col style="width:15%;">
							<col style="width:10%;">
							<col style="width:15%;">
						</colgroup>
						<tr>
							<th>전화번호</th>
							<td class="ltext">
								<input type="text" id="tuning_requester_tel_num" name="tuning_requester_tel_num" class="w140 easyui-textbox" value="${initValues.tuning_requester_tel_num}" >
							</td>
							<th>프로그램 유형</th>
							<td class="ltext">
								<select id="program_type_cd" name="program_type_cd" data-options="panelHeight:'auto',editable:false" class="w140 easyui-combobox" ></select>
							</td>
						</tr>
						<tr>
							<th>DB</th>
							<td class="ltext">
								<select id="dbid" name="dbid" data-options="panelHeight:'200px',editable:true" class="w140 easyui-combobox" ></select>
							</td>
							<th>DB접속계정</th>
							<td class="ltext">
								<select id="parsing_schema_name" name="parsing_schema_name" data-options="panelHeight:'200px',editable:true" class="w140 easyui-combobox" ></select>
							</td>
						</tr>
						<tr>
							<th >업무특이사항</th>
							<td class="ltext" colspan="3">
								<input type="text" id="wrkjob_peculiar_point" name="wrkjob_peculiar_point" class="w390 easyui-textbox">
							</td>
						</tr>
						<tr>
							<th>요청사유</th>
							<td class="ltext" colspan="3">
								<input type="text" id="request_why" name="request_why" class="w390 easyui-textbox">
							</td>
							</td>
						</tr>
					</table>
			</form:form>
			</div>
		<div class="easyui-layout" data-options="region:'south',split:false,border:false" style="height:50px;right:-8px;position:relative;" >
			<div class="searchBtn" style="padding-right:35px;">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_SaveInit();"><i class="btnIcon fas fa-check fa-lg fa-fw"></i> 저장</a>
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClosePopup('saveInitSettingPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>

<!-- 업무담당자 조회 -->
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
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="closeBtn('workTunerPop');"><i class="btnRIcon fas fa-window-close fa-lg fa-fw"></i> 닫기</a>
			</div>
		</div>
	</div>
</div>