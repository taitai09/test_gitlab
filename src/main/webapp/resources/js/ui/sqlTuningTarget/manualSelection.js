$(document).ready(function() {
	var before_choice_sql_except_yn = $("#before_choice_sql_except_yn").val();
	
	if(before_choice_sql_except_yn == "") $("#before_choice_sql_except_yn").val("Y");
	
	before_choice_sql_except_yn = $("#before_choice_sql_except_yn").val();

	var dbid = $("#submit_form #dbid").val();

	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadSuccess: function(items) {
			if (items.length){
				var dbid = $("#submit_form #dbid").val();
		    	if(dbid != '' && dbid != 'null'){
		    		$(this).combobox('setValue', dbid);
		    		
		    		var callFromParent = $("#call_from_parent").val();
		    		var callFromChild = $("#call_from_child").val();
		    		if(callFromParent == "Y" || callFromChild == "Y"){
		    			Btn_OnClick();
		    		}		    		
		    	}
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    },
	    onClick:function(rec){
	    	$("#snap_id").val("");
	    	$('#start_snap_id').textbox('setValue',"");
	    	$('#end_snap_id').textbox('setValue',"");
	    }
	});	
	

	
	$('#chkExcept').switchbutton({
		checked: true,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			console.log("이전 튜닝:" + checked);
			if(checked) $("#before_choice_sql_except_yn").val("Y"); 
			else $("#before_choice_sql_except_yn").val("N");
		}
	});
	
	if(before_choice_sql_except_yn == "Y"){
		$('#chkExcept').switchbutton({
			checked: true,
		});
	}else{
		$('#chkExcept').switchbutton({
			checked: false,
		});
	}
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,		
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
		    {field:'rnum',title:'순번',halign:"center",align:"center",sortable:"true"},
		    {field:'dbid',title:'DBID',halign:"center",align:"center",sortable:"true"},
		    {field:'sql_id',title:'SQL_ID',halign:"center",align:"center",sortable:"true"},
		    {field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
		    {field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
		    {field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true"},
		    {field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
		    {field:'avg_buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'max_buffer_gets',title:'MAX_BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'total_buffer_gets',title:'TOTAL_BUFFER_GETS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'avg_elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},		    
		    {field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'avg_cpu_time',title:'CPU_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'avg_disk_reads',title:'DISK_READS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'avg_rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
		    {field:'ratio_bget_total',title:'RATIO_BGET_TOTAL(%)',halign:"center",align:'right',sortable:"true"},
		    {field:'ratio_cpu_total',title:'RATIO_CPU_TOTAL(%)',halign:"center",align:'right',sortable:"true"},
		    {field:'ratio_cpu_per_execution',title:'RATIO_CPU_PER_EXECUTION(%)',halign:"center",align:'right',sortable:"true"},
		    {field:'sql_text',title:'SQL_TEXT',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	
	$('#excpt_module_list').combobox('textbox').prop('placeholder', 'jdbc, toad, scott123');
	$('#excpt_parsing_schema_name_list').combobox('textbox').prop('placeholder', "OPENPOP, ORACLE, DBSNMP");
	$('#excpt_sql_id_list').combobox('textbox').prop('placeholder', 'G29329vhw2pmn, AWa93ufw77usy');
	
	
	$('#elapsed_time').textbox('textbox').bind('keydown', function(aa){
		//var s = String.fromCharCode(e.keyCode);
		if(aa.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#buffer_gets').textbox('textbox').bind('keydown', function(bb){
		//var s = String.fromCharCode(e.keyCode);
		if(bb.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#executions').textbox('textbox').bind('keydown', function(cc){
		//var s = String.fromCharCode(e.keyCode);
		if(cc.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#topn_cnt').textbox('textbox').bind('keydown', function(a){
		//var s = String.fromCharCode(e.keyCode);
		if(a.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#module').textbox('textbox').bind('keydown', function(b){
		//var s = String.fromCharCode(e.keyCode);
		if(b.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#parsing_schema_name').textbox('textbox').bind('keydown', function(c){
		//var s = String.fromCharCode(e.keyCode);
		if(c.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#sql_text').textbox('textbox').bind('keydown', function(d){
		//var s = String.fromCharCode(e.keyCode);
		if(d.keyCode == 13){
			Btn_OnClick();
		}
	});$('#excpt_module_list').textbox('textbox').bind('keydown', function(e){
		//var s = String.fromCharCode(e.keyCode);
		if(e.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#excpt_parsing_schema_name_list').textbox('textbox').bind('keydown', function(f){
		//var s = String.fromCharCode(e.keyCode);
		if(f.keyCode == 13){
			Btn_OnClick();
		}
	});
	$('#excpt_sql_id_list').textbox('textbox').bind('keydown', function(g){
		//var s = String.fromCharCode(e.keyCode);
		if(g.keyCode == 13){
			Btn_OnClick();
		}
	});
	
	if($('#call_from_parent').val() != null && $("#call_from_parent").val == 'Y') {
		Btn_OnClick();
	}
	
	$('#selectRcount').combobox({
		onChange: function(){
			var rcount = $(this).combobox("getValue");
			$("#pagePerCount").val(rcount);
		}
	});
});

function fnFormValidationCheck(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#start_snap_id').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	if($('#end_snap_id').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 SNAP ID를 선택해 주세요.');
		return false;
	}	
	
	if($('#elapsed_time').textbox('getValue') == ""){
		parent.$.messager.alert('','Elapsed Time를 입력해 주세요.');
		return false;
	}
	
	if($('#buffer_gets').textbox('getValue') == ""){
		parent.$.messager.alert('','Buffer Gets를 입력해 주세요.');
		return false;
	}
	
	if($('#executions').textbox('getValue') == ""){
		parent.$.messager.alert('','Executions를 입력해 주세요.');
		return false;
	}	
	
	if($('#topn_cnt').textbox('getValue') == ""){
		parent.$.messager.alert('','Top N을 입력해 주세요.');
		return false;
	}	
	
	if($('#selectOrdered').combobox('getValue') == ""){
		parent.$.messager.alert('','Ordered를 선택해 주세요.');
		return false;
	}
	return true;
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallManualSelection();
}

function Btn_OnClick(){
	if(!fnFormValidationCheck()){
		return;
	}
	
	$("#currentPage").val("1");
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#order_div_cd").val($('#selectOrdered').combobox('getValue'));
	$("#elap_time").val($('#elapsed_time').val());
	$("#buffer_cnt").val($('#buffer_gets').val());
	
	ajaxCallManualSelection();
}

function ajaxCallManualSelection(){
	ajaxCall("/ManualSelectionAction", $("#submit_form"), "POST", callback_ManualSelectionAddTable);
}

var callback_ManualSelectionAddTable = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
}

function showHistoryPopup(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	$('#manualSelectionHistoryPop').window("open");
	
	$('#manualHistoryList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("수동선정 이력"," "); 
	
	ajaxCall("/ManualSelection/Popup/History",
			$("#submit_form"),
			"POST",
			callback_ManualSelectionHistory);	
}

//callback 함수
var callback_ManualSelectionHistory = function(result) {
	json_string_callback_common(result,'#manualHistoryList',true);
};

function showSnapPopup1(gubun){
	$('#snapList_form #snap_id').val("");

	strGb = gubun;
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	$('#snapList_form #startSnapIdList').datagrid('loadData',[]);
	$('#snapList_form #endSnapIdList').datagrid('loadData',[]);
	
	$('#snapList_form #dbid').val($('#selectCombo').combobox('getValue'));
	$('#snapList_form #dbName').textbox('setValue',$('#selectCombo').combobox('getText'));

	$('#startSnapIdList').datagrid('loadData',[]);
	$('#endSnapIdList').datagrid('loadData',[]);
	
//	$("#snapList_form #strStartDt").datebox("setValue", startDate);
//	$("#snapList_form #strEndDt").datebox("setValue", nowDate);	
	$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
	$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);	
	
	$('#snapListPop').window("open");
}

function showTuningReqPopup(){
	var sqlIdArry = "";
	var tuningNoArry = "";
	var rows = "";
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}	
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	rows = $('#tableList').datagrid('getChecked');
	initPopupSet();  //튜닝 요청 버그를 방지하기위해 초기화.
	if(rows.length > 0){
		$('#tuningAssignPop').window("open");
		
		$('#assign_form #dbid').val("");
		$('#assign_form #auto_share').val("N");
		$('#assign_form #sqlIdArry').val("");
		$('#assign_form #tuningNoArry').val("");
		$('#assign_form #perfr_id').val("");
		$('#assign_form #strGubun').val("M");

		
		for(var i = 0 ; i < rows.length ; i++){
			sqlIdArry += rows[i].sql_id + "|";
		}
		
		$("#assign_form #sqlIdArry").val(strRight(sqlIdArry,1));

		$('#assign_form #dbid').val($('#dbid').val());
		$("#assign_form #choice_div_cd").val($("#choice_div_cd").val());
		$("#submit_form #choice_cnt").val(rows.length);
		
		// 튜닝 담당자 조회			
		$('#assign_form #selectTuner').combobox({
		    url:"/Common/getTuner?dbid="+$("#dbid").val(),
		    method:"get",
			valueField:'tuner_id',
		    textField:'tuner_nm'
		});		
	}else{
		parent.$.messager.alert('','튜닝 대상을 선택해 주세요.');
	}
}

function Btn_ManualChoiceStatus(){
	var menuId = $("#menu_id").val()+1;
	var menuNm = "수동 선정  이력";
	var menuUrl = "/ManualSelectionStatus";
	var menuParam = 
		"dbid=" + $("#selectCombo").combobox("getValue")
		+ "&db_name=" + $("#selectCombo").combobox("getText")
		+ "&nav_from_parent=Y" 
		;
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam)
}

function Excel_Download(){
	if(!fnFormValidationCheck()){
		return;
	}

	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#order_div_cd").val($('#selectOrdered').combobox('getValue'));
	$("#elap_time").val($('#elapsed_time').val());
	$("#buffer_cnt").val($('#buffer_gets').val());

	$("#submit_form").attr("action","/ManualSelectionAction/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
