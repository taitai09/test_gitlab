$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});

	    	$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    }	    
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#table_name").val(row.table_name);
			getDmlTable();
		},			
		columns:[[
			{field:'owner',title:'OWNER',width:"14%",halign:"center",align:"center",sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',width:"34%",halign:"center",align:'left',sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',width:"20%",halign:"center",align:'left',sortable:"true"},
			{field:'subpartition_name',title:'SUBPARTITION_NAME',width:"25%",halign:"center",align:'left',sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',width:"20%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dmlList").datagrid({
		view: myview,
		columns:[[
			{field:'table_name',title:'TABLE_NAME',width:"18%",halign:"center",align:"center",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',width:"20%",halign:"center",align:'left',sortable:"true"},
			{field:'subpartition_name',title:'SUBPARTITION_NAME',width:"25%",halign:"center",align:'left',sortable:"true"},
			{field:'inserts',title:'INSERTS',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'updates',title:'UPDATES',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'deletes',title:'DELETES',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'truncated',title:'TRUNCATED',width:"15%",halign:"center",align:'center',sortable:"true"},
			{field:'timestamp',title:'변경일시',width:"24%",halign:"center",align:'center',sortable:"true"},
			{field:'base_day',title:'수집일자',width:"15%",halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'all_change_cnt',title:'전체변경건수',width:"16%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'rank',title:'변경순위',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').textbox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 입력해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("테이블별 DML 변경 내역"," "); 
	
	ajaxCall("/DMLChangeTable",
		$("#submit_form"),
		"POST",
		callback_DMLChangeTableAddTable);		
}

//callback 함수
var callback_DMLChangeTableAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function getDmlTable(){
	$('#dmlList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DML 발생 현황"," "); 
	/* 컬럼정보 조회 */
	ajaxCall("/DMLChangeTable/DMLOccurrence",
			$("#submit_form"),
			"POST",
			callback_DMLOccurrenceAddTable);
}

//callback 함수
var callback_DMLOccurrenceAddTable = function(result) {
	json_string_callback_common(result,'#dmlList',true);
};	