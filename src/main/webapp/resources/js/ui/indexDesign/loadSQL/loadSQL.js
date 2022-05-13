$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#loadSQLMain_form #selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#loadSQLMain_form #selectDbid").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$("#loadSQLMain_form #tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#loadSQLMain_form #file_no").val(row.file_no);
			
			$('#loadSQLIndexDesignTabs').tabs('select', 1);
			
			$('#loadActionPlan_form #selectDbid').combobox('setValue', $('#loadSQLMain_form #selectDbid').combobox('getValue'));
			
			// 파일번호 재조회
			setFileNo( $('#loadSQLMain_form #selectDbid').combobox('getValue') );
			
			$('#loadActionPlan_form #selectFileNo').combobox('setValue', row.file_no);
		},
		columns:[[
			{field:'file_no',title:'파일번호',halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB명',halign:"center",align:'center'},
			{field:'file_nm',title:'파일명',halign:"center",align:'left',sortable:"true"},
			{field:'sql_cnt',title:'SQL수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reg_dt',title:'적재일시',halign:"center",align:'center',sortable:"true"},
			{field:'plan_desc',title:'PLAN (생성 / 오류 / 미생성)',halign:"center",align:'center'},
			{field:'explain_exec_yn',title:'플랜생성여부',halign:"center",align:'center',sortable:"true"},
			{field:'access_path_exec_yn',title:'엑세스패스파싱여부',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert("ERROR","데이터 조회 중에 에러가 발생하였습니다.","error");
    	}
	});
	
	$('#loadSQLIndexDesignTabs').tabs({
		border:false,
		onSelect: function(title) {
			if(title == '적재SQL 실행 계획 생성') {
				setMarster_form_selector($('#loadActionPlan_form').selector);
			} else if(title == '적재SQL 인덱스 설계') {
				setMarster_form_selector($('#loadIndexDesign_form').selector);
			} else if(title == '적재SQL 인덱스 정비') {
				setMarster_form_selector($('#loadIndexUsage_form').selector);
			}
		}
	});
});

function sleep(milliseconds) {
	var start = new Date().getTime();
	for (var i = 0; i < 1e7; i++) {
		if ((new Date().getTime() - start) > milliseconds){
			break;
		}
	}
}

function Btn_OnClick_loadSQL(){
	if($('#loadSQLMain_form #selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('',"DB를 선택해 주세요.");
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#loadSQLMain_form #menu_id").val();
	
	$("#loadSQLMain_form #dbid").val($('#loadSQLMain_form #selectDbid').combobox('getValue'));

	$('#loadSQLMain_form #tableList').datagrid("loadData", []);
	$('#loadSQLMain_form #tableList').datagrid('loading');
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 적재"," "); 
	
	ajaxCall("/LoadSQL",
		$("#loadSQLMain_form"),
		"POST",
		callback_LoadSQLAddTable);
}

//callback 함수
var callback_LoadSQLAddTable = function(result) {
	json_string_callback_common(result,'#loadSQLMain_form #tableList',true);
};	

function goActionPlan(){
	$("#loadSQLMain_form").attr("action","/LoadActionPlan");
	$("#loadSQLMain_form").submit();		
}

/* 팝업 호출 */
function showLoadSqlPop(){
//	if($('#selectDbid').combobox('getValue') == ""){
//		parent.$.messager.alert('',"DB를 선택해 주세요.");
//		return false;
//	}
	
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#loadSQLMain_form #menu_id").val();
	
	$("#loadSQLMain_form #dbid").val($('#loadSQLMain_form #selectDbid').combobox('getValue'));
	
	$("#loadSQL_form #dbid").val($('#loadSQLMain_form #selectDbid').combobox('getValue'));
//	$('#loadSQL_form #tempDbid').textbox('setValue',$('#selectDbid').combobox('getText'));
	$('#loadSQL_form #tempDbid').combobox('setValue',$('#loadSQLMain_form #selectDbid').combobox('getValue'));
	//적재할 SQL 파일을 선택해 주세요... uploadFile 초기화
	$('#loadSQL_form #uploadFile').textbox('setValue','');

	$('#loadSqlPop').window("open");
}