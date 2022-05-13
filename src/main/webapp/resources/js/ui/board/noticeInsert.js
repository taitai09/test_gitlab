$(document).ready(function() {
	CKEDITOR.replace("board_contents",{
		width:"99%",
		height:"400px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
	
	
	$('#chkTopFixYn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#submit_form #top_notice_yn").val("Y");
			}else{
				$("#submit_form #top_notice_yn").val("N");
			}
		}
	})
	
});

function Btn_OnClick(){
	if($('#title').textbox('getValue') == ""){
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	var str = CKEDITOR.instances.board_contents.getData();
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if(str == ""){
		parent.$.messager.alert('','내용을 입력해 주세요.');
		return false;
	}

	$("#board_contents").val(CKEDITOR.instances.board_contents.getData());
	$("#contetns").val($("#board_contents").val());
	var formData = new FormData($("#submit_form")[0]);

	console.log("board_contents:::",$("#board_contents").val());
	console.log("contetns:::",$("#contetns").val());
	
	ajaxMultiCall("/BoardMng/Notice/insert",
			formData,
			"POST",
			callback_insertAction);	
}

//callback 함수
var callback_insertAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','공지사항이 등록되었습니다.','info',function(){
			setTimeout(function() {
				Btn_GoList();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			//오류가 났을 경우 리스트로 가는 것을 주석처리
			//Btn_GoList();	
		});
	}
};

function Btn_GoList(){
	location.href="/BoardMng/Notice?menu_id="+$("#menu_id").val()+"&call_from_parent=Y";
}
