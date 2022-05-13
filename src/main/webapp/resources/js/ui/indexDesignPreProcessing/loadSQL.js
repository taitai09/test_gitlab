$(document).ready(function() {
	// Database 조회			
	$('#selectDbid').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#file_no").val(row.file_no);
			//goActionPlan();
//			$("#submit_form").attr("action","/LoadActionPlan");
//			$("#submit_form").submit();
			var menuParam = $("#submit_form").serialize();
			parent.openLink("Y", 146, "적재SQL 실행 계획 생성", "/LoadActionPlan", menuParam);	
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
});

function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('',"DB를 선택해 주세요.");	
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));

	$('#tableList').datagrid("loadData", []);
	$('#tableList').datagrid('loading');
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 적재"," "); 
	
	ajaxCall("/LoadSQL",
		$("#submit_form"),
		"POST",
		callback_LoadSQLAddTable);		
}

//callback 함수
var callback_LoadSQLAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};	

function goActionPlan(){
	$("#submit_form").attr("action","/LoadActionPlan");
	$("#submit_form").submit();		
}

/* 팝업 호출 */
function showLoadSqlPop(){
//	if($('#selectDbid').combobox('getValue') == ""){
//		parent.$.messager.alert('',"DB를 선택해 주세요.");
//		return false;
//	}
	
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));
	
	$("#loadSQL_form #dbid").val($('#selectDbid').combobox('getValue'));
//	$('#loadSQL_form #tempDbid').textbox('setValue',$('#selectDbid').combobox('getText'));
	$('#loadSQL_form #tempDbid').combobox('setValue',$('#selectDbid').combobox('getValue'));
	//적재할 SQL 파일을 선택해 주세요... uploadFile 초기화
	$('#loadSQL_form #uploadFile').textbox('setValue','');

	$('#loadSqlPop').window("open");
}