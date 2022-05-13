$(document).ready(function() {
	CKEDITOR.replace("board_contents",{
		width:"99%",
		height:"400px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	

	$('#chkTopNotice').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#top_notice_yn").val("Y"); 
			else $("#top_notice_yn").val("N");
		}
	});
});

function Btn_OnClick() {
	var str = CKEDITOR.instances.board_contents.getData();
	let totMaxSize = 100 * 1024 * 1024;
	let maxSize = 0;
	let totSize = 0;
	let fileSize = 0;
	let max = 0;
	
	if($("#title").textbox('getValue') == "") {
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	if($('#title').textbox('getValue').length > 50) {
		parent.$.messager.alert('','제목은 50자 이내로 입력해 주세요.');
		return false;
	}
	
	let tit = $('#title').textbox('getValue');
	tit = tit.replace(/&nbsp;/g,"");
	tit = tit.replace(/<(\/a|a)([^>]*)>/gi,"");
	tit = tit.replace(/<(\/p|p)([^>]*)>/gi,"");
	tit = tit.replace(/<(\/span|span)([^>]*)>/gi,"");
	tit = tit.replace(/<(\/script|script)([^>]*)>/gi,"");
	tit = tit.replace(/&lt;/g,"");
	tit = tit.replace(/&gt;/g,"");
	$('#title').textbox('setValue',tit);
	
	if(str.length <= 0){
		parent.$.messager.alert('','내용을 등록해주세요.');
		return false;
	}
	
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/&lt;/gi,"<<");
	str = str.replace(/&gt;/gi,">>");
	str = str.replace(/<<(\/a|a)([^>>]*)>>/gi,"");
	str = str.replace(/<<(\/p|p)([^>>]*)>>/gi,"");
	str = str.replace(/<<(\/span|span)([^>>]*)>>/gi,"");
	str = str.replace(/<<(\/script|script)([^>>]*)>>/gi,"");
	str = str.replace(/<</gi,"<");
	str = str.replace(/>>/gi,">");
	str = str.replace(/<(\/a|a)([^>]*)>/gi,"");
	str = str.replace(/<(\/p|p)([^>]*)>/gi,"");
	str = str.replace(/<(\/span|span)([^>]*)>/gi,"");
	str = str.replace(/<(\/script|script)([^>]*)>/gi,"");
	str = str.replace(/&lt;/gi,"");
	str = str.replace(/&gt;/gi,"");
	str = str.replace(/\r\n/g,"");
	$("#board_contents").val(str);
	
	if ( $("#board_mgmt_no").val() == '2' ) {
		max = 10;
	} else {
		max = 100;
	}
	maxSize = max * 1024 * 1024;
	
	if ( $("#uploadFile").textbox("getValue") != null && $("#uploadFile").textbox("getValue") != '' ) {
		let fileAddr = document.submit_form.uploadFile[1].files;
		
		for ( var fileIdx = 0; fileIdx < fileAddr.length; fileIdx++ ) {
			totSize += fileAddr[fileIdx].size;
			
			console.log(fileAddr[fileIdx].name+" = 파일사이즈 : "+ fileAddr[fileIdx].size + ", MAX : "+maxSize +" , totSize : "+totSize);
			if ( fileAddr[fileIdx].size >= maxSize ) {
				fileSize = fileAddr[fileIdx].size/1024/1024;
				
				parent.$.messager.alert('','[ '+fileAddr[fileIdx].name+' ]<br>파일의 용량이 너무 큽니다.<br>현재용량 : '+fileSize.toFixed(2)+' MB , 최대용량 : '+max+' MB','error');
				return false;
			}
		}
	}
	
	if ( totSize >= totMaxSize ) {
		parent.$.messager.alert('','파일의 총용량이 100MB보다 큽니다.<br>100MB 이하로 선택해 주세요.<br>현재용량 : '+(totSize/1024/1024).toFixed(2)+' MB','error');
		totSize = 0;
		return false;
	} else {
		totSize = 0;
	}
	
//	$("#board_contents").val( CKEDITOR.instances.board_contents.getData() );
	
	var formData = new FormData($("#submit_form")[0]);
	
	ajaxMultiCall("/Board/InsertAction",
			formData,
			"POST",
			callback_insertAction);
}

//callback 함수
var callback_insertAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 등록되었습니다','info',function(){
			setTimeout(function() {
				Btn_goList();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			//오류가 났을 경우 리스트로 가는 것을 주석처리
			//Btn_GoList();	
		});
	}
};	

function Btn_goList(){
	$("#submit_form #uploadFile").filebox('clear');
	$("#submit_form").attr("action","/Board/List");
	$("#submit_form").submit();	
}