$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
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
			// 신규 탭 생성..
			parent.createSQLNewTab($("#menu_id").val(), "indexUseTabs", $("#dbid").val(), row.sql_id, row.plan_hash_value);
		},		
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'avg_elap',title:'ELAPSED_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_cpu',title:'CPU_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_bget',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_drds',title:'DISK_READS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avg_rows',title:'ROWS_PROCESSED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'action',title:'ACTION',halign:"center",align:'left',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#indexUseTabs').tabs({
		plain : true
	});
	
	var i = $('#index_name');
	var a = $('#action');
	var m = $('#module');
	i.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	a.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
	m.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 입력해 주세요.');
		return false;
	}
	
	if($('#index_name').textbox('getValue') == ""){
		parent.$.messager.alert('','INDEX NAME을 입력해 주세요.');
		return false;
	}	
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));

	$('#tableList').datagrid("loadData", []);
	
	ajaxIndexUseSQLAddTable();
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxIndexUseSQLAddTable();
}

function ajaxIndexUseSQLAddTable() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 사용 SQL 내역"," "); 
	
	ajaxCall("/IndexUseSQL",
		$("#submit_form"),
		"POST",
		callback_IndexUseSQLAddTable);
}


//callback 함수
var callback_IndexUseSQLAddTable = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

var Excel_DownClick = function(){
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	
	var a = document.createElement('a');
	a.href = "/IndexUseSQL/downloadExcel?"+$("#submit_form").serialize();
	//a.download = "IndexUseSQL";
	a.click();

}