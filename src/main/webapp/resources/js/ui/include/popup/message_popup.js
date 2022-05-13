$(document).ready(function() {
	$("#messagePopTab").tabs({
		plain:true
	});
	
	// 쪽지유형 조회			
	$('#note_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1017",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
	
	$('#sendMessagePop').window({
		title : "쪽지 보내기",
		top:getWindowTop(400),
		left:getWindowLeft(550)
	});
	
	$('#recvMessagePop').window({
		title : "쪽지 읽기",
		top:getWindowTop(400),
		left:getWindowLeft(550)
	});
	
	$('#recvMessageUserPop').window({
		title : "쪽지 받는 사람 조회",
		top:getWindowTop(500),
		left:getWindowLeft(750)
	});
	
	$('#messageListPop').window({
		title : "쪽지함",
		top:getWindowTop(550),
		left:getWindowLeft(900)
	});

	$("#recvMsgList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#msgList_form #send_user_id").val(row.send_user_id);
			$("#msgList_form #recv_user_id").val(row.recv_user_id);
			$("#msgList_form #send_dt").val(row.send_dt);
			$("#msgList_form #read_yn").val(row.read_yn);
			$("#msgList_form #send_yn").val("N");
			ShowMessageView();
		},
		columns:[[
			{field:'send_dt',title:'받은 날짜',width:"16%",halign:"center",align:'center'},
			{field:'send_user_id',hidden:"true"},
			{field:'recv_user_id',hidden:"true"},
			{field:'send_user_nm',title:'보낸 사람',width:"10%",halign:"center",align:'center'},
			{field:'note_type_cd',hidden:"true"},
			{field:'note_type_nm',title:'쪽지 유형',width:"10%",halign:"center",align:'center'},
			{field:'note_title',title:'쪽지 제목',width:"40%",halign:"center",align:'left'},
			{field:'read_yn',hidden:"true"},
			{field:'read_text',title:'읽음 여부',width:"8%",halign:"center",align:'center'},
			{field:'read_dt',title:'읽은 날짜',width:"16%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#sendMsgList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#msgList_form #send_user_id").val(row.send_user_id);
			$("#msgList_form #recv_user_id").val(row.recv_user_id);
			$("#msgList_form #send_dt").val(row.send_dt);
			$("#msgList_form #read_yn").val(row.read_yn);
			$("#msgList_form #send_yn").val("Y");
			ShowMessageView();
		},
		columns:[[
			{field:'send_dt',title:'보낸 날짜',width:"16%",halign:"center",align:'center'},
			{field:'send_user_id',hidden:"true"},
			{field:'recv_user_id',hidden:"true"},
			{field:'recv_user_nm',title:'받은 사람',width:"10%",halign:"center",align:'center'},
			{field:'note_type_cd',hidden:"true"},
			{field:'note_type_nm',title:'쪽지 유형',width:"10%",halign:"center",align:'center'},
			{field:'note_title',title:'쪽지 제목',width:"40%",halign:"center",align:'left'},
			{field:'read_yn',hidden:"true"},
			{field:'read_text',title:'읽음 여부',width:"8%",halign:"center",align:'center'},
			{field:'read_dt',title:'읽은 날짜',width:"16%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	

	$("#recvUserList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true"},
			{field:'user_id',title:'사용자 ID',width:"13%",halign:"center",align:'center',sortable:"true"},
			{field:'user_nm',title:'사용자명',width:"10%",halign:"center",align:'center'},
			{field:'auth_cd',hidden:"true"},
			{field:'auth_nm',title:'권한',width:"12%",halign:"center",align:'center'},			
			{field:'wrkjob_cd',hidden:"true"},
			{field:'wrkjob_nm',title:'업무명',width:"12%",halign:"center",align:'center'},
			{field:'ext_no',title:'내선번호',width:"14%",halign:"center",align:'center'},
			{field:'hp_no',title:'핸드폰번호',width:"14%",halign:"center",align:'center'},
			{field:'email',title:'EMAIL',width:"21%",halign:"center",align:'left'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	

	// 권한 리스트 조회	
	$('#selUser_form #popAuth_cd').combobox({
	    url:"/Common/getAuth",
	    method:"get",
		valueField:'auth_id',
	    textField:'auth_nm',
	    onSelect:function(rec){
	    	if(rec.auth_id == "4"){
	    		$("#selUser_form #popWrkjob_cd").combotree({disabled:false});
	    	}else{
	    		$("#selUser_form #popWrkjob_cd").combotree({disabled:true});
	    	}
	    }
	});

	// 업무 리스트 조회	
	$('#selUser_form #popWrkjob_cd').combotree({
	    url:"/Common/getWrkJobCd",
	    method:'get'
	});
});

// 쪽지 팝업
function ShowMessageListPopup(){
	// 위치초기화
	$('#messageListPop').window({
		top:getWindowTop(550),
		left:getWindowLeft(900)
	});
	
	$('#messageListPop').window("open");
	$('#messagePopTab').tabs('select', 0);
	
	$("#msgList_form #selReadYn").combobox("setValue","N");
	
	Btn_SearchMessageList();
}

// 쪽지 리스트 조회
function Btn_SearchMessageList(){
	if(($('#msgList_form #searchKey').combobox("getValue") != "" && $('#msgList_form #searchValue').textbox("getValue") == "") ||
		($('#msgList_form #searchKey').combobox("getValue") == "" && $('#msgList_form #searchValue').textbox("getValue") != "")){
		$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}	
	
	$("#read_yn").val($("#selReadYn").combobox("getValue"));
	
	// 받은 쪽지함
	$('#recvMsgList').datagrid('loadData',[]);
	$('#recvMsgList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#recvMsgList').datagrid('loading');	
	
	ajaxCall("/Message/RecvList",
			$("#msgList_form"),
			"POST",
			callback_RecvListAction);	
	
	// 보낸 쪽지함
	$('#sendMsgList').datagrid('loadData',[]);
	$('#sendMsgList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#sendMsgList').datagrid('loading');
	
	ajaxCall("/Message/SendList",
			$("#msgList_form"),
			"POST",
			callback_SendListAction);	
}

//callback 함수
var callback_RecvListAction = function(result) {
	var data = JSON.parse(result);	
	$('#recvMsgList').datagrid("loadData", data);
	$('#recvMsgList').datagrid('loaded');
};

//callback 함수
var callback_SendListAction = function(result) {
	var data = JSON.parse(result);
	$('#sendMsgList').datagrid("loadData", data);
	$('#sendMsgList').datagrid('loaded');	
};

// 쪽지 읽기
function ShowMessageView(strGubun){
	ajaxCall("/Message/MessageView",
			$("#msgList_form"),
			"POST",
			callback_MessageViewAction);	
}

//callback 함수
var callback_MessageViewAction = function(result) {
	if(result.result){
		$("#recvMessage_form #send_yn").val(result.object.send_yn);
		$("#recvMessage_form #send_dt").textbox("setValue",result.object.send_dt);
		$("#recvMessage_form #send_user_id").val(result.object.send_user_id);
		$("#recvMessage_form #send_user_nm").textbox("setValue",result.object.send_user_nm);
		$("#recvMessage_form #note_title").textbox("setValue",result.object.note_title);
		$("#recvMessage_form #note_contents").val(result.object.note_contents);
		
		if(result.object.send_yn == "Y"){
			$("#replyBtn").hide();
		}else{
			$("#replyBtn").show();
		}

		ContentRecvSize(result.object.note_contents);
		// 위치 초기화
		$('#recvMessagePop').window({
			top:getWindowTop(400),
			left:getWindowLeft(550)
		});
		
		$('#recvMessagePop').window("open");
	}else{
		$.messager.alert('',result.message,'error',function(){
			Btn_RecvMessageClose();
		});
	}
};

// 쪽지 보내기 팝업
function Btn_ShowSendMessage(){
	// 위치초기화
	$('#sendMessagePop').window({
		top:getWindowTop(400),
		left:getWindowLeft(550)
	});
	
	$('#sendMessagePop').window("open");
}

// 전체 쪽지 페이지 이동 
function Btn_GoMessageList(){
	Btn_MessageListClose();
	Btn_goCommonLink("message","");
}

// 쪽지 보낼 사용자 조회 팝업
function Btn_ShowRecvMessageUser(){
	// 위치 초기화
	$('#recvMessageUserPop').window({
		top:getWindowTop(500),
		left:getWindowLeft(750)
	});
	
	$('#recvMessageUserPop').window("open");
}

// 사용자 조회
function Btn_SearchRecvUser(){
	if(($('#selUser_form #popSearchKey').combobox("getValue") != "" && $('#selUser_form #popSearchValue').textbox("getValue") == "") ||
		($('#selUser_form #popSearchKey').combobox("getValue") == "" && $('#selUser_form #popSearchValue').textbox("getValue") != "")){
		$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}

	$('#recvUserList').datagrid('loadData',[]);
	$('#recvUserList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#recvUserList').datagrid('loading');
	
	ajaxCall("/Message/RecvMessageUser",
			$("#selUser_form"),
			"POST",
			callback_RecvMessageUserAction);		
}

//callback 함수
var callback_RecvMessageUserAction = function(result) {
	var data = JSON.parse(result);	
	$('#recvUserList').datagrid("loadData", data);
	$('#recvUserList').datagrid('loaded');
};

// 쪽지 보낼 사용자 선택
function Btn_SelectRecvUser(){
	var recvUserArry = "";
	var recvUserNameArry = "";
	
	rows = $('#recvUserList').datagrid('getSelections');
	for(var i=0; i<rows.length; i++){
		recvUserArry += rows[i].user_id + ";";	
		recvUserNameArry += rows[i].user_nm + ";";
	}		
	
	$("#message_form #recvUserArry").val(strRight(recvUserArry,1));	
	$("#message_form #recv_user_nm").textbox("setValue", strRight(recvUserNameArry,1));
	
	Btn_RecvMessageUserClose();
}

// 쪽지 보내기
function Btn_SendMessage(strGb){
	strGubun = strGb;
	
	if($('#message_form #recvUserArry').val() == ""){
		$.messager.alert('','쪽지 받을 사용자를 선택해 주세요.');
		return false;
	}
	
	if($('#message_form #note_title').textbox("getValue") == ""){
		$.messager.alert('','쪽지 제목을 입력해 주세요.');
		return false;
	}
	
	if($('#message_form #note_contents').val() == ""){
		$.messager.alert('','쪽지 내용을 입력해 주세요.');
		return false;
	}
	
	ajaxCall("/Message/SendMessage",
			$("#message_form"),
			"POST",
			callback_SendMessageAction);
}

//callback 함수
var callback_SendMessageAction = function(result) {
	if(result.result){
		$.messager.alert('','쪽지 보내기가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				// 쪽지 알림 전송
				var userArry = $("#message_form #recvUserArry").val().split(";");
				var sendMsg = "<a href='javascript:;' onClick='ShowMessageListPopup();'>["+ $("#message_form #sendUserName").val() +"]님에게서 쪽지가 도착하였습니다.</a>";
				
				for(var i = 0 ; i < userArry.length ; i++){
					messageSendByUser(userArry[i], "쪽지", sendMsg);
				}
				
				Btn_SendMessageClose();
				Btn_SearchMessageList();
			},1000);			
		});	
	}else{
		$.messager.alert('',result.message,'error',function(){
			resetSendMessage();
		});
	}
};

//쪽지 답장
function Btn_ReplyMessage(){
	$('#recvMessagePop').window("close");
	
	// 위치 초기화
	$('#sendMessagePop').window({
		top:getWindowTop(400),
		left:getWindowLeft(550)
	});
	
	$('#sendMessagePop').window("open");
	
	$("#message_form #recvUserArry").val($("#recvMessage_form #send_user_id").val());
	$("#message_form #recv_user_nm").textbox("setValue",$("#recvMessage_form #send_user_nm").textbox("getValue"));
	$("#message_form #findUserBtn").linkbutton({disabled:true});
}

//쪽지 삭제
function Btn_DeleteMessage(){
	$.messager.confirm('', '쪽지를 삭제하시겠습니까?', function(r){
		if (r){
			ajaxCall("/Message/DeleteMessage",
					$("#recvMessage_form"),
					"POST",
					callback_DeleteMessageAction);
		}
	});
}

//callback 함수
var callback_DeleteMessageAction = function(result) {
	if(result.result){
		$.messager.alert('','쪽지 삭제가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				Btn_RecvMessageClose();
				Btn_SearchMessageList();
			},1000);			
		});	
	}else{
		$.messager.alert('',result.message,'error',function(){
			resetRecvMessage();
		});
	}
};

// 쪽지 리스트 팝업 닫기
function Btn_MessageListClose(){
	$('#messageListPop').window("close");	
	
	// 리스트 초기화
	resetMessageList();
}

// 팝업 리스트 초기화
function resetMessageList(){
	$('#messageList').datagrid('loadData',[]);

	$("#msgList_form #send_searchKey").combobox("setValue","");
	$("#msgList_form #send_searchValue").textbox("setValue","");
	$("#msgList_form #send_selectValue").combobox("setValue","N");
}

// 쪽지 보내기 팝업 닫기
function Btn_SendMessageClose(){
	$('#sendMessagePop').window("close");
	
	if($("#recvMessage_form #send_user_id").val() != ""){
		// 위치 초기화
		$('#recvMessagePop').window({
			top:getWindowTop(400),
			left:getWindowLeft(550)
		});
		
		$('#recvMessagePop').window("open");
	}

	// 쪽지 보내기 초기화
	resetSendMessage();
}

// 쪽지 보내기 초기화
function resetSendMessage(){
	$("#message_form #findUserBtn").linkbutton({disabled:false});
	$("#message_form #recvUserArry").val("");
	$("#message_form #recv_user_nm").textbox("setValue","");
	$("#message_form #note_title").textbox("setValue","");
	$("#message_form #note_contents").val("");
	
	$("#contSizeDiv").html("0 / 4,000 Bytes");
}

// 쪽지 받을 사용자 선택 팝업 닫기
function Btn_RecvMessageClose(){
	$('#recvMessagePop').window("close");
	Btn_SearchMessageList();
	
	// 쪽지 받을 사용자 초기화
	resetRecvMessage();	
}

// 쪽지 받을 사용자 초기화
function resetRecvMessage(){
	$("#replyBtn").show();
	$("#recvMessage_form #send_yn").val("");
	$("#recvMessage_form #send_dt").textbox("setValue","");
	$("#recvMessage_form #send_user_id").val("");
	$("#recvMessage_form #send_user_nm").textbox("setValue","");
	$("#recvMessage_form #note_title").textbox("setValue","");
	$("#recvMessage_form #note_contents").val("");
	
	$("#recvSizeDiv").html("0 / 4,000 Bytes");
}

// 쪽지 읽기 팝업 닫기
function Btn_RecvMessageUserClose(){
	$('#recvMessageUserPop').window("close");
	
	// 쪽지 읽기 정보 초기화
	resetRecvMessageUser();
}

// 쪽지 읽기 정보 초기화
function resetRecvMessageUser(){
	$('#recvUserList').datagrid('loadData',[]);	
	$("#selUser_form #popSearchKey").combobox("setValue","");
	$("#selUser_form #popSearchValue").textbox("setValue","");
	$("#selUser_form #popAuth_cd").combobox("setValue","");
	$("#selUser_form #popSearchKey").combobox("setValue","");
}

// 쪽지 4000 바이트 제한
function ContentSize(){
	var tempi1 = 0;
	var tempi2 = 0;
	var byteIs = 0;
	var maxcount = 4000;
	var noteContents = $("#message_form #note_contents").val();

	for (var i = 0 ; i < noteContents.length ; i++) {
		tmp = noteContents.charAt(i);
		escChar = escape(tmp);
		if (escChar=='%0D') {
		} else if (escChar.length > 4) {
			byteIs += 2;
		} else {
			byteIs += 1;
		}
		if (byteIs > maxcount) {break;}
		if (byteIs == (maxcount - 1)) {tempi1 = i+1;}
		if (byteIs == maxcount) {tempi2 = i+1;}
	}
	
	if (byteIs > maxcount) {
		$.messager.alert('','4,000 Byte 를 초과하실 수 없습니다.');
		if (tempi2 > 0) { 
			tmpval = noteContents.substr(0,tempi2);
			
			byteIs = maxcount;
		}else{
			tmpval = noteContents.substr(0,tempi1);
			byteIs = (maxcount-1);
		}
		
		$("#message_form #note_contents").val(tmpval);
		$("#contSizeDiv").html(byteIs+" / 4,000 Bytes");				
	} else {
		$("#contSizeDiv").html(byteIs+" / 4,000 Bytes");				
	}
}

// 쪽지 내용 바이트 계산
function ContentRecvSize(cont){
	var byteIs = 0;
	var maxcount = 4000;
	
	for (var i = 0 ; i < cont.length ; i++) {
		tmp = cont.charAt(i);
		escChar = escape(tmp);
		if (escChar=='%0D') {
		} else if (escChar.length > 4) {
			byteIs += 2;
		} else {
			byteIs += 1;
		}
		if (byteIs > maxcount) {break;}
		if (byteIs == (maxcount - 1)) {tempi1 = i+1;}
		if (byteIs == maxcount) {tempi2 = i+1;}
	}
	
	$("#recvSizeDiv").html(byteIs+" / 4,000 Bytes");
}