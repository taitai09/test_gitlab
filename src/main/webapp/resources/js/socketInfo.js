$(document).ready(function(){
	$('#messageAll').dialog({
		width:300,
		height:200,
		top:getWindowTop(200),
		left:getWindowLeft(300),
		title:"서버공지",
		closed:true,
		modal:true
	});
	
	webSock = new SockJS("/WebSocket");
	
	// 메시지 받았을때 처리 함수
	webSock.onmessage = function(evt) {
		var revMsg = JSON.parse(evt.data);
		
		if(revMsg.send_type == "ALL"){ // 전체 전송을 경우는 팝업
			$('#messageAll').html(revMsg.message);
			$('#messageAll').dialog('open');	
		}else{
			$.messager.show({
				title : revMsg.title,
				msg : revMsg.message,
				timeout : 5000,
				showType : 'slide'
			});
			
			// 쪽지 건수 조회
			searchMessageCount();
		}
	};
});

function messageSendALL(strTitle, strMsg){
	sendMessage("ALL", "", strTitle, strMsg)
}

function messageSendByUser(userId, strTitle, strMsg){
	sendMessage("USER", userId, strTitle, strMsg)
}

function messageSendByAuth(userAuth, strTitle, strMsg){
	sendMessage("AUTH", userAuth, strTitle, strMsg)
}

function messageSendByWorkJobCd(wrkJobCd, strTitle, strMsg){
	sendMessage("WRKJOB", wrkJobCd, strTitle, strMsg)
}

function sendMessage(sendType, sendGubun, title, message){
	var socketMsg = {
		send_type : sendType,
		send_gubun : sendGubun,
		title : title,
		message : message
	};
	
	webSock.send(JSON.stringify(socketMsg));	
}