$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : true,
		columns:[[
			{field:'qty_chk_idt',title:'품질점검지표',width:"10%",halign:"center",align:"left"},
			{field:'qty_chk_idt_nm',title:'품질점검지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_inspection_cnt',title:'작업결과(건수)',width:"05%",halign:"center",align:'right',formatter:changeNull},
			{field:'output_start_row',title:'출력시작행',width:"05%",halign:"center",align:'center'},
			{field:'qty_result_sheet_nm',title:'Sheet명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_idt_cd',hidden:true},
			{field:'mdi_pcs_cd',hidden:true},
			{field:'qty_chk_result_tbl_nm',hidden:true},
			{field:'excel_output_yn',hidden:true},
			{field:'dml_yn',hidden:true}
			]],

	    onLoadError:function() {
	    	parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    }
	});
		
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
});

function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}
function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#project_nm").textbox("setValue", row.project_nm);
	$("#project_id").val(row.project_id);
	
//	$("#dbid").val(row.dbid);
	
//	searchPerformanceCheckId(row.project_id);
}

function Btn_SaveSetting(){

	var qty_chk_idt_cd = "";
	var qty_chk_result_tbl_nm = "";
	var dml_yn = "";
	var dml_yn_cnt = 0;
	var rows = $('#tableList').datagrid('getRows');
	var vgetRows = $('#tableList').datagrid('getRows').length;
	//parent.$.messager.alert('check row',rows.length);
	//parent.$.messager.alert('get row',vgetRows);
//	if(rows.length <= 0){
//		parent.$.messager.alert('','구조 품질검토 작업 대상을 선택해 주세요.');
//		return false;
//	}
	
	for(var i=0;i<rows.length;i++){
		//var row = rows[i];
		if(rows[i].dml_yn == 'Y') {
			dml_yn_cnt ++;
		}
		if(qty_chk_idt_cd == ""){
			qty_chk_idt_cd = "'" + rows[i].qty_chk_idt_cd +"'";
			if (rows[i].qty_chk_result_tbl_nm !== null && rows[i].qty_chk_result_tbl_nm !== ""){
				qty_chk_result_tbl_nm = rows[i].qty_chk_result_tbl_nm;
			}
			dml_yn = rows[i].dml_yn;
		}
		else {
			qty_chk_idt_cd += ",'" + rows[i].qty_chk_idt_cd  +"'";
			if (rows[i].qty_chk_result_tbl_nm !== null && rows[i].qty_chk_result_tbl_nm !== ""){
				qty_chk_result_tbl_nm += "," + rows[i].qty_chk_result_tbl_nm;
			}
			else{
				qty_chk_result_tbl_nm += ",";
			}
			dml_yn += "," + rows[i].dml_yn;
		}
		
	}

	if (dml_yn_cnt == 0) {
		parent.$.messager.alert('','프로젝트 검색 후 실행 가능합니다.');
//		parent.$.messager.alert('확인','프로젝트 검색 후 실행 가능합니다.');
		return false;
	}
	
	$('#save_form #qty_chk_idt_cd').val(qty_chk_idt_cd);
	$('#save_form #qty_chk_result_tbl_nm').val(qty_chk_result_tbl_nm);
	$('#save_form #dml_yn').val(dml_yn);
	
	//parent.$.messager.alert('qty_chk_idt_cd',qty_chk_idt_cd);
	//parent.$.messager.alert('qty_chk_result_tbl_nm.val()',$('#save_form #qty_chk_result_tbl_nm').val());
	//parent.$.messager.alert('dml_yn.val()',$('#save_form #dml_yn').val());

	//return;
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질 검토 작업"," ");
	
	ajaxCall("/Mqm/QualityInspectionJob/Save",
			$("#save_form"),
			"POST",
			callback_SaveInstanceAction);
}

//callback 함수
var callback_SaveInstanceAction = function(result) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if(result.result){
		parent.$.messager.alert('','구조 품질 검토 작업이 완료 되었습니다.','info',function(){
//		$("#searchKey").combobox('setValue', 2);
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function Btn_OnClick(val){
	if(!formValidationCheck()){
		return;
	}
	
	fnSearch();
}


function fnSearch(){

		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		$('#tableList').datagrid('loadData',[]);

		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질 검토 작업"," "); 
		
		ajaxCall("/Mqm/QualityInspectionJob",
				$("#submit_form"),
				"POST",
				callback_QualityInspectionJobAction);	
}

//callback 함수
var callback_QualityInspectionJobAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	$('#save_form #project_id').val($('#submit_form #project_id').val());

};


function formValidationCheck(){
	
	if($('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
//	if($('#searchKey').combobox('getValue') == ""){
//		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
//		return false;
//	}
	return true;
};

// 모델정보수집
function Btn_ModelCollecting(){
	if(!formValidationCheck()){
		return false;
	}
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("모델정보수집"," ");
	$('#save_form #qty_chk_idt_cd').val("002");
	ajaxCall("/Mqm/QualityInspectionJob/ModelCollecting",
			$("#save_form"),
			"POST",
			callback_ModelCollecting);
};

var callback_ModelCollecting = function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	if(result.result){
		parent.$.messager.alert('','모델정보수집 작업이 완료 되었습니다.','info')
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Excel_Download(){
	
	if($('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	
	var qty_chk_idt_cd = "";
	var qty_chk_result_tbl_nm = "";
	var excel_output_yn = "";
	var excel_output_yn_cnt = 0;
	var rows = $('#tableList').datagrid('getRows');
	var vgetRows = $('#tableList').datagrid('getRows').length;
	//parent.$.messager.alert('check row',rows.length);
	//parent.$.messager.alert('get row',vgetRows);
//	if(rows.length <= 0){
//		parent.$.messager.alert('','구조 품질검토 작업 대상을  선택해 주세요.');
//		return false;
//	}
	
	for(var i=0;i<rows.length;i++){
		//var row = rows[i];
		if(rows[i].excel_output_yn == 'Y') {
			excel_output_yn_cnt ++;
		}
		else{
			continue;
		}
		if(qty_chk_idt_cd == ""){
			qty_chk_idt_cd = "'" + rows[i].qty_chk_idt_cd +"'";
			if (rows[i].qty_chk_result_tbl_nm !== null && rows[i].qty_chk_result_tbl_nm !== ""){
				qty_chk_result_tbl_nm = rows[i].qty_chk_result_tbl_nm;
			}
			excel_output_yn = rows[i].excel_output_yn;
		}
		else {
			qty_chk_idt_cd += ",'" + rows[i].qty_chk_idt_cd  +"'";
			if (rows[i].qty_chk_result_tbl_nm !== null && rows[i].qty_chk_result_tbl_nm !== ""){
				qty_chk_result_tbl_nm += "," + rows[i].qty_chk_result_tbl_nm;
			}
			else{
				qty_chk_result_tbl_nm += ",";
			}
			excel_output_yn += "," + rows[i].excel_output_yn;
		}
		
		/*
		$('#save_form #wrkjob_cd').val($('#selectCombo').combobox('getValue'));
		$('#save_form #tr_cd').val(row.tr_cd);
		$('#save_form #except_yn').val(row.except_yn);
		*/
		
		//ajaxCall("/newAppTimeoutPredictionExceptYnUpdate",
		//		$('#save_form'),
		//		"POST",
		//		null);
	}

	if (excel_output_yn_cnt == 0) {
		parent.$.messager.alert('','프로젝트 검색 후 실행 가능합니다.');
		return false;
	}
	
	$('#save_form #qty_chk_idt_cd').val(qty_chk_idt_cd);
	$('#save_form #qty_chk_result_tbl_nm').val(qty_chk_result_tbl_nm);
	$('#save_form #excel_output_yn').val(excel_output_yn);
	
	$("#submit_form").attr("action","/Mqm/QualityInspectionJob/ExcelDown");
	$("#submit_form").submit();
	
	/*
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질검토 집계표"," ");
	ajaxCall("/Mqm/QualityInspectionJob/ExcelDown",
			$("#save_form"),
			"POST",
			callback_Excel_Download);	
	//$("#submit_form").attr("action","/Mqm/QualityCheckSql/ExcelDown");
	//$("#submit_form").submit();
	*/
}
//callback 함수

var callback_Excel_Download = function(result) {
	//json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

};

function changeNull(val, row){
	if(row.qty_chk_idt_cd == '001'){
		return "";
	}
	return val;
}