$(document).ready(function() {
	CKEDITOR.replace("guide_sbst",{
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
				$("#submit_form #top_fix_yn").val("Y");
			}else{
				$("#submit_form #top_fix_yn").val("N");
			}
		}
	})
	
});

function Btn_OnClick(){
	if($('#guide_title_nm').textbox('getValue') == ""){
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	var str = CKEDITOR.instances.guide_sbst.getData();
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if(str == ""){
		parent.$.messager.alert('','내용을 입력해 주세요.');
		return false;
	}

	$("#guide_sbst").val( CKEDITOR.instances.guide_sbst.getData() );

	var formData = new FormData($("#submit_form")[0]);

//	console.log("formDATA::::::",formData);
	ajaxMultiCall("/Precedent/Insert",
			formData,
			"POST",
			callback_insertAction);	
}

//callback 함수
var callback_insertAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 등록되었습니다.','info',function(){
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
	location.href = "/Precedent?menu_id=" + $("#menu_id").val() + "&menu_nm="
			+ encodeURIComponent("SQL 튜닝 사례/가이드")
			+ "&call_from_parent=Y"
			+ "&searchBtnClickCount1="+$("#searchBtnClickCount1").val()
			+ "&searchBtnClickCount2="+$("#searchBtnClickCount2").val()
			+ "&guide_div_cd=1"
			;
}
