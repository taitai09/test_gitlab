$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#indexUseSQL_form #selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#indexUseSQL_form #selectCombo").combobox("textbox").attr("placeholder","선택");
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#indexUseSQL_form #selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$("#indexUseSQL_form #selectUserName").combobox("textbox").attr("placeholder","선택");
					
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}
	});	
	
	$("#indexUseSQL_form #tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			// 신규 탭 생성..
			parent.createSQLNewTab($("#indexUseSQL_form #menu_id").val()
									,"indexUseTabs"
									,$("#indexUseSQL_form #dbid").val()
									,row.sql_id, row.plan_hash_value
									,580);
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
	
	$('#indexUseSQL_form #indexUseTabs').tabs({
		plain : true
	});
	
	var i = $('#indexUseSQL_form #index_name');
	var a = $('#indexUseSQL_form #action');
	var m = $('#indexUseSQL_form #module');
	i.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClickIndex();
	   }
	});	
	a.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClickIndex();
		}
	});	
	m.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClickIndex();
		}
	});
	
//	$('#tableIndexUseTabs').tabs({
//		border:false,
//		onSelect: function(title) {
//			if(title == '테이블 사용 SQL 분석') {
//				setMarster_form_selector($('#tableUseSQL_form').selector);
//			} else if(title == '인덱스 사용 SQL 분석') {
//				setMarster_form_selector($('#indexUseSQL_form').selector);
//			}
//		}
//	});
	
	setMarster_form_selector( '#indexUseSQL_form' );
});

function Btn_OnClickIndex(){
	if($('#indexUseSQL_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#indexUseSQL_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 입력해 주세요.');
		return false;
	}
	
	if($('#indexUseSQL_form #index_name').textbox('getValue') == ""){
		parent.$.messager.alert('','INDEX NAME을 입력해 주세요.');
		return false;
	}	
	
	$("#indexUseSQL_form #currentPage").val("1");
	$("#indexUseSQL_form #pagePerCount").val("20");
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#indexUseSQL_form #menu_id").val();

	$("#indexUseSQL_form #dbid").val($('#indexUseSQL_form #selectCombo').combobox('getValue'));
	$("#indexUseSQL_form #owner").val($('#indexUseSQL_form #selectUserName').combobox('getValue'));
	$('#indexUseSQL_form #module').val($('#indexUseSQL_form #module').textbox('getValue'));
	$('#indexUseSQL_form #action').val($('#indexUseSQL_form #action').textbox('getValue'));

	$('#indexUseSQL_form #tableListIndex').datagrid("loadData", []);
	
	ajaxIndexUseSQLAddTable();
}

function formValidationCheck(){
	return true;
}

function fnSearch( master_form_selector ){
	if(master_form_selector == '#tableUseSQL_form') {
		ajaxTableUseSQLAddTable();
	} else if(master_form_selector == '#indexUseSQL_form') {
		ajaxIndexUseSQLAddTable();
	}
}

function ajaxIndexUseSQLAddTable() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 사용 SQL 내역"," "); 
	
	ajaxCall("/IndexUseSQL",
		$('#indexUseSQL_form'),
		"POST",
		callback_IndexUseSQLAddTable);
}


//callback 함수
var callback_IndexUseSQLAddTable = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#indexUseSQL_form #tableList',true);
	
	setMarster_form_selector( '#indexUseSQL_form' );
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

var Excel_DownClickIndex = function(){
	if($('#indexUseSQL_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#indexUseSQL_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 입력해 주세요.');
		return false;
	}
	
	if($('#indexUseSQL_form #index_name').textbox('getValue') == ""){
		parent.$.messager.alert('','INDEX NAME을 입력해 주세요.');
		return false;
	}
	
	$("#indexUseSQL_form #dbid").val($('#indexUseSQL_form #selectCombo').combobox('getValue'));
	$("#indexUseSQL_form #owner").val($('#indexUseSQL_form #selectUserName').combobox('getValue'));
	$('#indexUseSQL_form #module').val($('#indexUseSQL_form #module').textbox('getValue'));
	$('#indexUseSQL_form #action').val($('#indexUseSQL_form #action').textbox('getValue'));
	
	var a = document.createElement('a');
	a.href = "/IndexUseSQL/downloadExcel?"+$("#indexUseSQL_form").serialize();
	//a.download = "IndexUseSQL";
	a.click();

}