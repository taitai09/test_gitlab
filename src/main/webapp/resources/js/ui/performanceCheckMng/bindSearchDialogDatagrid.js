var totalCount = 0;

$(document).ready(function() {
	$('#bindSearchDialog').window({
		title : "바인드 검색",
		top:getWindowTop(700),
		left:getWindowLeft(900)
	});

	$("#bindSearchList1").datagrid({
		view: myview,
		singleSelect : true,
		onClickRow : function(index,row) {
			//--perf_check_id 성능점검ID
			//--perf_check_step_id 성능점검단계ID
			//--program_id 프로그램ID
			//--program_execute_tms 프로그램수행회차			
			bindSearchList2();
		},
		columns:[[
			{field:'no',title:'NO',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'db_name',title:'수행DB',width:"19%",halign:"center",align:'center',sortable:"true"},
			{field:'program_exec_dt',title:'수행일시',width:"19%",halign:"center",align:'center'},
			{field:'bind_cnt',title:'바인드수',width:"19%",halign:"center",align:'center'},
			{field:'elapsed_time',title:'Elapsed Time',width:"19%",halign:"center",align:'center'},
			{field:'buffer_gets',title:'Buffer Gets',width:"19%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#bindSearchList2").datagrid({
		view: myview,
		columns:[[
			{field:'bind_seq',title:'순번',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_nm',title:'변수명',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_value',title:'변수타입',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_type',title:'변수값',width:"25%",halign:"center",align:'center',sortable:"true"}
		]],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    }    	
	});
	$("#bindSearchList3").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'no',title:'NO',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',width:"19%",halign:"center",align:'center',sortable:"true"},
			{field:'program_exec_dt',title:'수행일시',width:"19%",halign:"center",align:'center'},
			{field:'bind_cnt',title:'바인드수',width:"19%",halign:"center",align:'center'},
			{field:'elapsed_time',title:'Elapsed Time',width:"19%",halign:"center",align:'center'},
			{field:'buffer_gets',title:'Buffer Gets',width:"19%",halign:"center",align:'center'}
			]],
			
			onLoadError:function() {
				$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});
	$("#bindSearchList4").datagrid({
		view: myview,
		columns:[[
			{field:'bind_seq',title:'순번',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_nm',title:'변수명',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_value',title:'변수타입',width:"25%",halign:"center",align:'center',sortable:"true"},
			{field:'bind_var_type',title:'변수값',width:"25%",halign:"center",align:'center',sortable:"true"}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}    	
	});

	Btn_OnClosePopup('bindSearchDialog');
	
	/**바인드 검색 탭 1, 2 */
	$("#bindSearchTabs").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
				fnBindSearchList1();
			}else if(index == 1){
				fnBindSearchList3();
			}
		}		
	});
	
});

function fnBindSearchList1(){
	$('#bindSearchList1').html("");
	$('#bindSearchList1').append("<tbody><tr><td></td></tr></tbody>");
	$('#bindSearchList1').datagrid('loadData',[]);

	ajaxCall("/PerfCheckResultList/selectDeployPerfChkExecBindListPop", $("#submit_form"), "POST", callback_BindSearch1);		
}
function fnBindSearchList2(){
	totalCount = 0;
	$('#bindSearchList2').html("");
	$('#bindSearchList2').append("<tbody><tr><td></td></tr></tbody>");
	$('#bindSearchList2').datagrid('loadData',[]);

	ajaxCall("/PerfCheckResultList/selectDeployPerfChkExecBindValue", $("#submit_form"), "POST", callback_BindSearch2);		
}
function fnBindSearchList3(){
	$('#bindSearchList3').html("");
	$('#bindSearchList3').append("<tbody><tr><td></td></tr></tbody>");
	$('#bindSearchList3').datagrid('loadData',[]);

	ajaxCall("/PerfCheckResultList/selectDeployPerfChkAllPgmList", $("#submit_form"), "POST", callback_BindSearch3);		
}
function fnBindSearchList4(){
	totalCount = 0;
	$('#bindSearchList4').html("");
	$('#bindSearchList4').append("<tbody><tr><td></td></tr></tbody>");
	$('#bindSearchList4').datagrid('loadData',[]);

	ajaxCall("/PerfCheckResultList/selectVsqlBindCaptureList", $("#submit_form"), "POST", callback_BindSearch4);		
}
//callback 함수
var callback_BindSearch1 = function(result) {
	$("#bindSearchList1 tbody tr").remove();
	$("#bindSearchList2").prev().first().find(".datagrid-btable tbody").remove();
	json_string_callback_common(result,'#bindSearchList1',true);
	$("#bindSearchTabs").tabs('select',0);
};

//callback 함수
var callback_BindSearch2 = function(result) {
	$("#bindSearchList2 tbody tr").remove();
	json_string_callback_common(result,'#bindSearchList2',true);
	
	try{
		var data = JSON.parse(result);
		console.log("json_string_callback_common data:",data);
		console.log("data.result:",data.result);
		if(data.resultCode == "00000"){
			totalCount = data.totalCount;
			console.log("totalCount:",totalCount);
		}
	}catch(e){
		console.log("e.message:"+e.message);
		parent.$.messager.alert('',e.message);
	}	
};
//callback 함수
var callback_BindSearch3 = function(result) {
	$("#bindSearchList3 tbody tr").remove();
	$("#bindSearchList4").prev().first().find(".datagrid-btable tbody").remove();
	json_string_callback_common(result,'#bindSearchList3',true);
};
//callback 함수
var callback_BindSearch4 = function(result) {
	$("#bindSearchList4 tbody tr").remove();
	json_string_callback_common(result,'#bindSearchList4',true);
	
	try{
		var data = JSON.parse(result);
		console.log("json_string_callback_common data:",data);
		console.log("data.result:",data.result);
		if(data.resultCode == "00000"){
			totalCount = data.totalCount;
			console.log("totalCount:",totalCount);
		}
	}catch(e){
		console.log("e.message:"+e.message);
		parent.$.messager.alert('',e.message);
	}
};

function fnBinding(){
	
	var pp = $('#bindSearchTabs').tabs('getSelected');
	var index = pp.panel('options').index;

	var rows;
	var rowsLength = 0;
	if(index == 0){
		rows = $('#bindSearchList2').datagrid('getRows');
	}else{
		rows = $('#bindSearchList4').datagrid('getRows');
	}
	console.log("rows:",rows);
	rowsLength = rows.length;
	
	var bindTarget = $("#bindTbl input[name='bind_seq']");
	var bindTargetLength = bindTarget.length;
	

	for(var i=0;i<rowsLength;i++){
		//순번
		var bind_seq = rows[i].bind_seq;
		//변수명
		var bind_var_nm = rows[i].bind_var_nm;
		//변수타입
		var bind_var_type = rows[i].bind_var_type;
		//변수값
		var bind_var_value = rows[i].bind_var_value;
		
		for(var j=0;j<bindTargetLength;j++){
			var bind_var_nm2 = $("#bindTbl #bind_var_nm"+(j+1)).textbox("getValue");
			console.log("bind_var_nm2:"+bind_var_nm2);
			if(bind_var_nm == bind_var_nm2){
				$("#bindTbl #bind_var_type"+(j+1)).combobox("setValue",bind_var_type);
				$("#bindTbl #bind_var_value"+(j+1)).textbox("setValue",bind_var_value);
			}
		}

	}
	Btn_OnClosePopup('bindSearchDialog');
}

