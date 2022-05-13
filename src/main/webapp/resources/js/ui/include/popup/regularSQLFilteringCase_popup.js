$(document).ready(function() {
	$('#regularSQLFilterCasePop').window({
		title : "정규SQL 필터링 조건 관리",
		top:getWindowTop(600),
		left:getWindowLeft(600)
	});
	
	// Database 조회
	$('#selectDbidComboRegularSQL').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onChange: function(newValue, oldValue){
			if(newValue != '' && newValue != oldValue) {
				Btn_OnClickR();
			}
		},
		onLoadSuccess: function(data){
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#selectDbidComboRegularSQL').combobox("setValue",$("#dbid").val());
	
	$("#regularSQLParsingSchemaNameList").datagrid({
		view: myview,
		nowrap : true,
		fitColumns : false,
		checkOnSelect : false,
		selectOnCheck : false,
		singleSelect : true,
		columns:[[
			{field:'rnum',title:'No',width:"50px",halign:"center",align:'center'},
			{field:'user_id',title:'Parsing Schema Name',width:"355px",halign:"center",align:'center'},
			{field:'action',title:'삭제',width:"50px",align:'center',
				formatter: function(value,row,index) {
					if(row.rnum) {
						return '<a href="javascript:void(0)" onclick="deleteRegularSQLTargetUser(this)">Delete</a>';
					}
				}
			}
		]],
		onLoadSuccess: function(data){
		},
		onLoadError:function() {
			$.message.alert('검색 에러', '데이터 조회 중에 에러가 발생하였습니다.', 'error');
		}
	});
	
	$("#regularSQLFilteringCaseList").datagrid({
		view: myview,
		nowrap : true,
		fitColumns : false,
		checkOnSelect : true,
		selectOnCheck : false,
		singleSelect : true,
		columns:[[
			{field:'rnum',title:'No',width:"50px",halign:"center",align:'center'},
			{field:'regular_sql_filter_type_nm',title:'필터유형',width:"135px",halign:"center",align:'center'},
			{field:'regular_sql_filter_type_cd',title:'필터유형cd',width:"150px",halign:"center",align:'center',hidden:true},
			{field:'regular_sql_filter_condition',title:'필터조건',width:"220px",halign:"center",align:'center'},
			{field:'action',title:'삭제',width:"50px",align:'center',
				formatter: function(value,row,index) {
					if(row.rnum) {
						return '<a href="javascript:void(0)" onclick="deleteRegularSQLModuleFilterCase(this)">Delete</a>';
					}
				}
			}
		]],
		onLoadSuccess: function(data){
		},
		onLoadError:function() {
			$.message.alert('검색 에러', '데이터 조회 중에 에러가 발생하였습니다.', 'error');
		}
	});
});

function Btn_OnClickR() {
	if($('#selectDbidComboRegularSQL').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	$('#regularSQLFilterCase_form #dbid').val($('#selectDbidComboRegularSQL').combobox('getValue'));
	
	$('#regularSQLParsingSchemaNameList').datagrid("loadData", []);
	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("정규 SQL 필터링 조건 관리"," ");
	
	loadParsingSchemaNameCombobox();
	getRegularSQLParsingSchemaNameFilteringCase();

	loadRegularSQLModuleFilterCombobox();
	getRegularSQLModuleFilterCase();
}

function loadParsingSchemaNameCombobox() {
	$("#selectUserNameCombo").combobox({
		url:"/selectParsingSchemaNameComboBox?dbid="+$('#regularSQLFilterCase_form #dbid').val(),
		method:"get",
		valueField:'parsing_schema_name',
		textField:'parsing_schema_name',
		onSelect:function(rec){
			$('#regularSQLFilterCase_form #user_id').val(rec.parsing_schema_name);
		},
		onLoadSuccess: function(event) {
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onLoadError: function(){
			parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
			
			if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		}
	});
	
}

function loadRegularSQLModuleFilterCombobox() {
	$("#selectFilterTypeCombo").combobox({
		url:"/selectRegularSQLModuleFilterCombobox?dbid="+$('#regularSQLFilterCase_form #dbid').val(),
		method:"get",
		valueField:'cd_nm',
		textField:'cd_nm',
		onSelect:function(rec){
			$('#regularSQLFilterCase_form #regular_sql_filter_type_cd').val(rec.cd);
			$('#regularSQLFilterCase_form #regular_sql_filter_type_nm').val(rec.cd_nm);
		},
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		},
		onLoadError: function(){
			parent.$.messager.alert('','정규 SQL 모듈 필터링 콤보 박스 조회중 오류가 발생하였습니다.');
			parent.$.messager.progress('close');
		}
	});
}

function reloadParsingSchemaNameCombobox() {
	ajaxCall("/selectParsingSchemaNameComboBox",
			$("#regularSQLFilterCase_form"),
			"GET",
			callback_reloadParsingSchemaNameComboboxData);
}

var callback_reloadParsingSchemaNameComboboxData = function(result) {
	var data = JSON.parse(result);
	
	$("#selectUserNameCombo").combobox("clear");
	$("#selectUserNameCombo").combobox("loadData", data);
}

function getRegularSQLParsingSchemaNameFilteringCase() {
	ajaxCall("/selectRegularSQLParsingSchemaNameFilteringCase",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_methodLoadRegularSQLTargetUserNameList);
}

var callback_methodLoadRegularSQLTargetUserNameList = function(result) {
	$('#regularSQLParsingSchemaNameList').datagrid("loadData", []);
	
	json_string_callback_common(result, '#regularSQLParsingSchemaNameList', true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function Btn_AddTargetUserName() {
	if($('#selectDbidComboRegularSQL').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserNameCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','Parsing Schema Name을 선택해 주세요.');
		return false;
	}
	
	if(!confirmDbCombobox()) {
		parent.$.messager.alert('','변경된 DB가 반영되지 못하였습니다. 검색 버튼을 선택해 주세요.');
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("정규 SQL 대상 사용자 등록"," ");
	
	ajaxCall("/insertRegularSQLTargetUser",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_insertRegularSQLTargetUser);
}

function clearTargetUserNameCombobox() {
	$("#selectUserNameCombo").combobox("clear");
}

var callback_insertRegularSQLTargetUser  = function(result) {
	if(result.result == false) {
		parent.$.messager.alert('','정규 SQL Parsing Schema Name 필터 조건 등록 중 오류가 발생하였습니다.');
		parent.$.messager.progress('close');
		
		return false;
	}
	
	clearTargetUserNameCombobox();
	
	reloadRegularSQLParsingSchemaNameFilteringCase();
}

function deleteRegularSQLTargetUser(target) {
	if(!confirmDbCombobox()) {
		parent.$.messager.alert('','변경된 DB가 반영되지 못하였습니다. 검색 버튼을 선택해 주세요.');
		return;
	}
	
	var rowIndex = $(target).closest('tr.datagrid-row').attr('datagrid-row-index');
	var rowData = $('#regularSQLParsingSchemaNameList').datagrid('getRows')[rowIndex];
	
	$('#regularSQLFilterCase_form #user_id').val(rowData.user_id);
	
	/* modal progress open */ 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("정규 SQL 모듈 필터링 조건 삭제"," ");
	
	ajaxCall("/deleteRegularSQLTargetUser",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_deleteRegularSQLTargetUser);
}

var callback_deleteRegularSQLTargetUser  = function(result) {
	if(result.result == false) {
		parent.$.messager.alert('','정규 SQL 대상 사용자 삭제 중 오류가 발생하였습니다.');
		parent.$.messager.progress('close');
	}
	
	clearTargetUserNameCombobox();
	
	reloadRegularSQLParsingSchemaNameFilteringCase();
}

function reloadRegularSQLParsingSchemaNameFilteringCase() {
	reloadParsingSchemaNameCombobox();
	getRegularSQLParsingSchemaNameFilteringCase();
}
//////////End of Parsing Schema Name

//////////Start of 필터 유형
function getRegularSQLModuleFilterCase() {
	$('#regularSQLFilteringCaseList').datagrid("loadData", []);
	
	ajaxCall("/selectRegularSQLModuleFilterCase",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_methodLoadRegularSQLModuleFilterCase);
}

var callback_methodLoadRegularSQLModuleFilterCase = function(result) {
	json_string_callback_common(result, '#regularSQLFilteringCaseList', true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function Btn_AddFilterCase() {
	if($('#selectDbidComboRegularSQL').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectFilterTypeCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','필터유형을 선택해 주세요.');
		return false;
	}
	
	if($('input[name="filterCondition"]').val() == "") {
		parent.$.messager.alert('','필터조건을 입력해 주세요.');
		return false;
	}
	
	if(!confirmDbCombobox()) {
		parent.$.messager.alert('','변경된 DB가 반영되지 못하였습니다. 검색 버튼을 선택해 주세요.');
		return;
	}
	
	$('#regularSQLFilterCase_form #regular_sql_filter_condition').val($('input[name="filterCondition"]').val());
	
	ajaxCall("/checkRegisteredRegularSQLModuleFilterCase",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_methodReasonFilterCase);
}

function clearFilterCondition() {
	$('#regularSQLFilterCase_form #regular_sql_filter_condition').val('');
	$('#filterCondition').textbox('setValue', '');
}

var callback_methodReasonFilterCase = function(result) {
	console.log(result);
	
	var resultJSON = JSON.parse(result);
	var data = resultJSON.rows[0];
	
	if(data.rcount > 0) {
		parent.$.messager.alert('','이미 등록된 정규 SQL 모듈 필터 조건입니다.');
		clearFilterCondition();
		
		return false;
	}
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("정규 SQL 모듈 필터링 조건 등록"," ");
	
	ajaxCall("/insertRegularSQLModuleFilterCase",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_insertRegularSQLModuleFilterCase);
}

var callback_insertRegularSQLModuleFilterCase  = function(result) {
	if(result.result == false) {
		parent.$.messager.alert('','정규 SQL 모듈 필터링 조건 등록 중 오류가 발생하였습니다.');
		parent.$.messager.progress('close');
		
		return false;
	}
	
	parent.$.messager.progress('close');
	
	clearFilterCondition();
	
	getRegularSQLModuleFilterCase();
}

function deleteRegularSQLModuleFilterCase(target) {
	if(!confirmDbCombobox()) {
		parent.$.messager.alert('','변경된 DB가 반영되지 못하였습니다. 검색 버튼을 선택해 주세요.');
		return;
	}
	
	var rowIndex = $(target).closest('tr.datagrid-row').attr('datagrid-row-index');
	var rowData = $('#regularSQLFilteringCaseList').datagrid('getRows')[rowIndex];
	
	$('#regularSQLFilterCase_form #regular_sql_filter_type_cd').val(rowData.regular_sql_filter_type_cd);
	$('#regularSQLFilterCase_form #regular_sql_filter_condition').val(rowData.regular_sql_filter_condition);
	
	/* modal progress open */ 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("정규 SQL 모듈 필터링 조건 삭제"," ");
	
	ajaxCall("/deleteRegularSQLModuleFilterCase",
			$("#regularSQLFilterCase_form"),
			"POST",
			callback_deleteRegularSQLModuleFilterCase);
}

var callback_deleteRegularSQLModuleFilterCase  = function(result) {
	if(result.result == false) {
		parent.$.messager.alert('','정규 SQL 모듈 필터링 조건 삭제 중 오류가 발생하였습니다.');
		parent.$.messager.progress('close');
	}
	
	parent.$.messager.progress('close');
	
	clearFilterCondition();
	
	getRegularSQLModuleFilterCase();
}

function initializeRegularSQLFilterCasePop() {
	$("#selectDbidComboRegularSQL").combobox("clear");
	clearTargetUserNameCombobox();
	$("#selectFilterTypeCombo").combobox("clear");
	$('#regularSQLParsingSchemaNameList').datagrid("loadData", []);
	$('#regularSQLFilteringCaseList').datagrid("loadData", []);
	clearFilterCondition();
	$("#selectUserNameCombo").combobox("loadData", []);
	$("#selectFilterTypeCombo").combobox("loadData", []);
}

function Btn_CloseRegularSQLFilterCasePop() {
	initializeRegularSQLFilterCasePop();
	
	Btn_OnClosePopup("regularSQLFilterCasePop");
}

function confirmDbCombobox() {
	var currentDb = $('#selectDbidComboRegularSQL').combobox('getValue');
	var dbid = $('#regularSQLFilterCase_form #dbid').val();
	
	if(currentDb != dbid) {
		return false;
	}
	
	return true;
}