$(document).ready(function() {
	CKEDITOR.replace("board_contents",{
		width:"99%",
		height:"400px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
});

function Btn_OnClick() {
	var str = CKEDITOR.instances.board_contents.getData();

	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if($("#title").textbox('getValue') == "") {
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	if(str.length <= 0){
		parent.$.messager.alert('','내용을 등록해주세요.');
		return false;			
	}
	
	$("#board_contents").val( CKEDITOR.instances.board_contents.getData() );

	var formData = new FormData($("#submit_form")[0]);
	
	ajaxMultiCall("/BoardMng/ReplyAction",
			formData,
			"POST",
			callback_replyAction);
}

//callback 함수
var callback_replyAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 답글 등록되었습니다','info',function(){
			setTimeout(function() {
				Btn_goList();
			},1000);			
		});
	}
};	

function Btn_goList(){
	$("#submit_form").attr("action","/BoardMng/List");
	$("#submit_form").submit();	
}