$(document).ready(function() {
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사 되었습니다.');
	});
	
	var clipImprSql = new Clipboard('#imprSqlCopyBtn',{
		text: function() {
			return $("#impr_sql_text_h").val();
		}
	});	
	clipImprSql.on('success', function(e) {
		parent.$.messager.alert('','개선 SQL TEXT가 복사되었습니다.');
	});
	
	var clipImprbExec = new Clipboard('#imprbExecCopyBtn',{
		text: function() {
			return $("#imprb_exec_plan_h").val();
		}
	});	
	clipImprbExec.on('success', function(e) {
		parent.$.messager.alert('','개선전 실행계획이 복사되었습니다.');
	});
	
	var clipImpraExec = new Clipboard('#impraExecCopyBtn',{
		text: function() {
			return $("#impra_exec_plan_h").val();
		}
	});	
	clipImpraExec.on('success', function(e) {
		parent.$.messager.alert('','개선후 실행계획이 복사되었습니다.');
	});	
	
	$("#historyViewTab").tabs({
		plain: true,
		onSelect: function(title,index){
			/* 탭을 클릭시 화면을 높이 자동 조절 */
			var height = $("#container").height();
			parent.resizeTopFrame($("#menu_id").val(), height);  
		}
	});
	
	CKEDITOR.replace("impr_sql_text",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	CKEDITOR.replace("imprb_exec_plan",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});
	
	CKEDITOR.replace("impra_exec_plan",{
		width:"100%",
		height:"300px",
		contentsCss: "body {font-family:'굴림체';font-size:11px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
	
	/* html 태크 제거 및 공백 제거*/
	var impr_sql_text_h = $("#impr_sql_text_h").val();
	$("#impr_sql_text_h").val(parent.formatHTML(impr_sql_text_h));

	var imprb_exec_plan_h = $("#imprb_exec_plan_h").val();
	$("#imprb_exec_plan_h").val(parent.formatHTML(imprb_exec_plan_h));

	var impra_exec_plan_h = $("#impra_exec_plan_h").val();
	$("#impra_exec_plan_h").val(parent.formatHTML(impra_exec_plan_h));
	
	//인덱스 이력 조회
	createSqlTuningIndexHistoryTable();
});

function Btn_GoNext(strIndex){
	$('#historyViewTab').tabs('select', strIndex);
}

function Btn_GoList(gubun){
	$("#call_from_child").val("Y");
	document.location.href="/TuningHistory?"+$("#search_form").serialize();
}

function downTuningFile(file_nm, org_file_nm){
	$("#file_nm").val(file_nm);
	$("#org_file_nm").val(org_file_nm);
	
	$("#submit_form").attr("action", "/Tuning/DownFile");
	$("#submit_form").submit();
}