$(document).ready(function() {
	//var height = $("#sqlImproveDiv").height();
	var height = document.body.clientHeight;
	console.log("height:"+height);
	console.log("document.body.clientHeight:"+document.body.clientHeight);
	top.resizeTopFrame($("#menu_id").val(), height+350); 
	parent.resizeSqlHistoryIF(height+350);

	var clipboard = new Clipboard('#sqlCopyBtn');
    clipboard.on('success', function(e) {
    	top.$.messager.alert('','복사 되었습니다.');
    });

	CKEDITOR.replace("impr_sql_text",{
		width:"99%",
		height:"300px",
		contentsCss: "body {font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	CKEDITOR.replace("imprb_exec_plan",{
		width:"99%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	CKEDITOR.replace("impra_exec_plan",{
		width:"99%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
	
	/* html 태크 제거 및 공백 제거*/
	var impr_sql_text_h = $("#impr_sql_text_h").val();
	$("#impr_sql_text_h").val(top.formatHTML(impr_sql_text_h));

	var imprb_exec_plan_h = $("#imprb_exec_plan_h").val();
	$("#imprb_exec_plan_h").val(top.formatHTML(imprb_exec_plan_h));

	var impra_exec_plan_h = $("#impra_exec_plan_h").val();
	$("#impra_exec_plan_h").val(top.formatHTML(impra_exec_plan_h));

	//인덱스 이력 조회
	createSqlTuningIndexHistoryTable();	
	
});

function Btn_GoList(gubun){
	$("#submit_form").attr("action","/ImprovementManagement/SQLImproveHistory");
	$("#submit_form").submit();	
}