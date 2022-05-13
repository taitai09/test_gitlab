$(document).ready(function() {
	$("#messageTab").tabs({
		plain:true
	});
	
	// 쪽지유형 조회			
	$('#selectValue').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1017",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	
	$("#recvMessageList").datagrid({
		view: myview,
		rownumbers:true,
		onClickRow : function(index,row) {
			$("#submit_form #send_user_id").val(row.send_user_id);
			$("#submit_form #recv_user_id").val(row.recv_user_id);
			$("#submit_form #send_dt").val(row.send_dt);
			$("#submit_form #read_yn").val(row.read_yn);
			ShowMessageView("", "submit_form");
		},
		columns:[[
			{field:'send_dt',title:'받은 날짜',width:"13%",halign:"center",align:'center'},
			{field:'send_user_id',hidden:"true"},
			{field:'recv_user_id',hidden:"true"},
			{field:'send_user_nm',title:'보낸 사람',width:"12%",halign:"center",align:'center'},
			{field:'note_type_cd',hidden:"true"},
			{field:'note_type_nm',title:'쪽지 유형',width:"10%",halign:"center",align:'center'},
			{field:'note_title',title:'쪽지 제목',width:"42%",halign:"center",align:'left'},
			{field:'read_yn',hidden:"true"},
			{field:'read_text',title:'읽음 여부',width:"10%",halign:"center",align:'center'},
			{field:'read_dt',title:'읽은 날짜',width:"13%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#sendMessageList").datagrid({
		view: myview,
		rownumbers:true,
		onClickRow : function(index,row) {
			$("#submit_form #send_user_id").val(row.send_user_id);
			$("#submit_form #recv_user_id").val(row.recv_user_id);
			$("#submit_form #send_dt").val(row.send_dt);
			$("#submit_form #read_yn").val(row.read_yn);
			ShowMessageView("", "submit_form");
		},
		columns:[[
			{field:'send_dt',title:'보낸 날짜',width:"13%",halign:"center",align:'center'},
			{field:'send_user_id',hidden:"true"},
			{field:'recv_user_id',hidden:"true"},
			{field:'recv_user_nm',title:'받은 사람',width:"12%",halign:"center",align:'center'},
			{field:'note_type_cd',hidden:"true"},
			{field:'note_type_nm',title:'쪽지 유형',width:"10%",halign:"center",align:'center'},
			{field:'note_title',title:'쪽지 제목',width:"42%",halign:"center",align:'left'},
			{field:'read_yn',hidden:"true"},
			{field:'read_text',title:'읽음 여부',width:"10%",halign:"center",align:'center'},
			{field:'read_dt',title:'읽은 날짜',width:"13%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});		
});

function Btn_OnClick(){
	if(($('#submit_form #searchKey').combobox("getValue") != "" && $('#submit_form #searchValue').textbox("getValue") == "") ||
		($('#submit_form #searchKey').combobox("getValue") == "" && $('#submit_form #searchValue').textbox("getValue") != "")){
		$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}

	// 받은 쪽지함
	$('#recvMessageList').datagrid('loadData',[]);
	$('#recvMessageList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#recvMessageList').datagrid('loading');
	ajaxCall("/Common/RecvMessageList",
			$("#submit_form"),
			"POST",
			callback_RecvMessageListAction);
	
	// 보낸 쪽지함
	$('#sendMessageList').datagrid('loadData',[]);
	$('#sendMessageList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#sendMessageList').datagrid('loading');
	ajaxCall("/Common/SendMessageList",
			$("#submit_form"),
			"POST",
			callback_SendMessageListAction);	
}

//callback 함수
var callback_RecvMessageListAction = function(result) {
	var data = JSON.parse(result);
	$('#recvMessageList').datagrid("loadData", data);
	$('#recvMessageList').datagrid('loaded');	
};

//callback 함수
var callback_SendMessageListAction = function(result) {
	var data = JSON.parse(result);
	$('#sendMessageList').datagrid("loadData", data);
	$('#sendMessageList').datagrid('loaded');	
};

function Excel_DownClick(){
	if($('#strStartDt').textbox('getValue') == ""){
		$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	}

	$("#submit_form").attr("action","/PerformanceReport/PerformanceMngStatus/ExcelDown");
	$("#submit_form").submit();		
}