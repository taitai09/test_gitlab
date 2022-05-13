$(document).ready(function() {
	CKEDITOR.replace("sched_sbst",{
		width:"99%",
		height:"400px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	

	$('.datatime').timespinner({
		showSeconds: false
	});
});

function Btn_OnClick() {
	var str = CKEDITOR.instances.sched_sbst.getData();

	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");

	if($("#sched_title").textbox('getValue') == "") {
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	if(str.length <= 0){
		parent.$.messager.alert('','내용을 등록해주세요.');
		return false;			
	}

	$("#sched_type_cd").val($("#selSchedTypeCd").combobox('getValue'));
	$("#sched_sbst").val( CKEDITOR.instances.sched_sbst.getData() );
	
	$("#sched_start_dt").val($('#sched_start_date').datebox('getValue') + " " + $('#sched_start_time').timespinner('getValue') + ":00");
	$("#sched_end_dt").val($('#sched_end_date').datebox('getValue') + " " + $('#sched_end_time').timespinner('getValue') + ":00");
	
	ajaxCall("/Schedule/UpdateAction",
			$("#submit_form"),
			"POST",
			callback_updateAction);
}

//callback 함수
var callback_updateAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 수정되었습니다','info',function(){
			setTimeout(function() {
				Btn_goView();
			},1000);			
		});
	}
};	

function Btn_goList(){
	$("#submit_form").attr("action","/Schedule/List");
	$("#submit_form").submit();	
}

function Btn_goView(){
	$("#submit_form").attr("action","/Schedule/View");
	$("#submit_form").submit();	
}