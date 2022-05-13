$(document).ready(function() {
	CKEDITOR.replace("bfac_chk_result_sbst",{
		width:"98%",
		height:"300px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
});

function Btn_GoList(){
	$("#submit_form").attr("action","/SourceCheck");
	$("#submit_form").submit();	
}

/* 튜닝이력 상세 탭 생성 */
function Btn_AddTabTuningHistory(){
	var menuId = "129";
	var menuNm = "튜닝이력조회";
	var menuUrl = "/TuningHistory/View";
	var menuParam = "tuning_no="+$("#tuning_no").val();	
	
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

function Btn_Complete(){
	var str = CKEDITOR.instances.bfac_chk_result_sbst.getData();

	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if(str.length <= 0){
		parent.$.messager.alert('','점검 결과를 입력해 주세요.');
		return false;			
	}
	
	$("#bfac_chk_result_sbst").val(str);

	ajaxCall("/SourceCheck/Update",
			$("#submit_form"),
			"POST",
			callback_SourceCheckUpdate);		
}

//callback 함수
var callback_SourceCheckUpdate = function(result) {
	if(result.result){
		parent.$.messager.alert('','점검결과가 정상적으로 등록되었습니다.','info',function(){
			setTimeout(function() {
				Btn_GoList();
			},1000);			
		});		
	}else{
		parent.$.messager.alert('',result.message);
		parent.$.messager.alert('','점검결과가 등록에 문제가 생겼습니다.<br/>다시 시도해 주세요.','error',function(){
			Btn_GoList();	
		});		
	}
};
