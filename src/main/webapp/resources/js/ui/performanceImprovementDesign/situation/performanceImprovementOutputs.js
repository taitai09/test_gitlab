$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	// Database 조회
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isAll=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	//프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/Common/getProject?isAll=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(rec){
			if(rec.project_id == ''){
				$("#tuning_prgrs_step_seq").combobox('setValue','');
			}
			
			project_id = rec.project_id;
			
			if(project_id != null && project_id != ''){
				//튜닝진행단계 조회
				$('#submit_form #tuning_prgrs_step_seq').combobox({
					url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?isAll=Y&project_id="+project_id,
					method:"get",
					valueField:'tuning_prgrs_step_seq',
					textField:'tuning_prgrs_step_nm',
					onLoadError: function(){
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
				});	
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
	cnt = 0;
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		onClickRow : function(index,row) {
			$("#submit_form #tuning_no").val(row.tuning_no);
			$("#submit_form #choice_div_cd").val(row.choice_div_cd);
			$("#submit_form #tuningStatusCd").val(row.tuning_status_cd);
			$("#submit_form #tuning_complete_dt").val(row.tuning_complete_dt);
			getDetailView();
		},
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true", rowspan:2},
			{field:'tuning_no',title:'TUNING_NO',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_div_cd',title:'튜닝대상선정코드',hidden:"true",rowspan:2},
			{field:'choice_div_cd_nm',title:'요청유형',halign:"center",sortable:"true",align:'center', rowspan:2},
			{field:'dbid',title:'DB',hidden:"true",rowspan:2},
			{field:'db_name',title:'DB',halign:"center",align:'center', rowspan:2},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",sorter:sorterDatetime, rowspan:2},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true",sorter:sorterDatetime, rowspan:2},
			{field:'perfr_id',hidden:"true",rowspan:2},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_status_cd',title:'진행상태코드',hidden:"true",rowspan:2},
			{field:'tuning_status_nm',title:'진행상태',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_why_nm',title:'완료/종료 사유',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_request_dt',title:'요청일',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_requester_nm',title:'요청자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'wrkjob_mgr_nm',title:'업무담당자',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_complete_dt',title:'튜닝완료일',halign:"center",align:'center',sortable:"true", rowspan:2},
			{title:'개선전',halign:"center",colspan:2},
			{title:'개선후',halign:"center",colspan:2},
			{title:'개선율(%)',halign:"center",colspan:2},
			{field:'tuning_apply_dt',title:'적용일',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'project_nm',title:'프로젝트',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'controversialist',title:'문제점',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'impr_sbst',title:'개선내용',halign:"center",align:'center',sortable:"true", rowspan:2}
		],[
			{field:'imprb_elap_time',title:'ELAPSED_TIME',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat},
			{field:'imprb_buffer_cnt',title:'BUFFER_GETS',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat},
			{field:'impra_elap_time',title:'ELAPSED_TIME',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat},
			{field:'impra_buffer_cnt',title:'BUFFER_GETS',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat},
			{field:'elap_time_impr_ratio',title:'ELAPSED_TIME',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat},
			{field:'buffer_impr_ratio',title:'BUFFER_GETS',halign:"center",sortable:"true",align:'right',formatter:getNumberFormat}
		]],
		onUncheck:function(index, rows) {
			if ( $("#profile_form #isAll").val() == "A" ) {
				$("#submit_form #isAll").val("");
			}
		},
		onUncheckAll:function( rows ) { 
			$("#submit_form #isAll").val("");
		},
		onCheckAll:function( rows ) {
			$("#submit_form #isAll").val("A");
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#submit_form #selectCombo').combobox('setValue',$("#dbid").val());
	$('#submit_form #selectTuningStatus').combobox('setValue',$("#tuning_status_cd").val());
	$('#submit_form #selectUserRoll').combobox('setValue',$("#searchKey").val());
	
	if( $("#projectId").val() != null && $("#projectId").val() != "" ){
		$('#submit_form #project_id').combobox('setValue', $("#projectId").val() );
		$("#projectId").val("");
	}
	if( $("#tuningPrgrsStepSeq").val() != null && $("#tuningPrgrsStepSeq").val() != "" ){
		$('#submit_form #tuning_prgrs_step_seq').combobox('setValue', $("#tuningPrgrsStepSeq").val() );
		$("#tuningPrgrsStepSeq").val("");
	}
	
	$('#submit_form #tableList').datagrid("loadData", []);
	
	var call_from_parent = $("#call_from_parent").val();
	if(call_from_parent == "Y"){
		Btn_OnClick();
	}
});

function ajaxCallPerformanceImprovementOutputs(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능개선결과 산출물"," "); 
	
	ajaxCall("/PerformanceImprovementOutputsAction", $("#submit_form"), "POST", callback_PerformanceImprovementOutputsAddTable);
}

function formValidationCheck(){
	if($('#submit_form #strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 기준일자를 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 기준일자를 선택해 주세요.');
		return false;
	}
	
	if(($('#submit_form #selectUserRoll').combobox('getValue') == "" && $('#submit_form #searchValue').textbox('getValue') != "") ||
		($('#submit_form #selectUserRoll').combobox('getValue') != "" && $('#submit_form #searchValue').textbox('getValue') == "")){
		parent.$.messager.alert('','담당자 정보를 정확히 입력해 주세요.');
		return false;
	}
	return true;
}

function Btn_OnClick(){
	if(!formValidationCheck()){
		return;
	}
	$("#submit_form #isAll").val("");
	
	if( $("#subCurrentPage").val() != null && $("#subCurrentPage").val() != "" ){
		$('#submit_form #currentPage').val($("#subCurrentPage").val());
		$("#subCurrentPage").val("");
	} else {
		$("#currentPage").val("1");
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#submit_form #searchKey").val($('#submit_form #selectUserRoll').combobox('getValue'));
	
	ajaxCallPerformanceImprovementOutputs();
}

//callback 함수
var callback_PerformanceImprovementOutputsAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	data = JSON.parse(result);
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
};

function Excel_Download(){
	if(!formValidationCheck()){
		return;
	}
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#submit_form #searchKey").val($('#submit_form #selectUserRoll').combobox('getValue'));
	
	$("#submit_form").attr("action","/PerformanceImprovementOutputs/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function getDetailView(){
	$("#submit_form").attr("action","/PerformanceImprovementDesign/PerformanceImprovementOutputsView");
	$("#submit_form").submit();
}

function Btn_DownloadOutputsAll(){
	if ( $("#isAll").val() != null && $("#isAll").val() == "A" ) {
		ajaxCall("/PerformanceImprovementOutputsListAll", $("#submit_form"), "POST", callback_PerformanceImprovementOutputsListAll );
	} else {
		let rows = $('#tableList').datagrid('getSelections');
		
		dataDownLoadOutputs( rows );
	}
}

var callback_PerformanceImprovementOutputsListAll = function(result){
	let data = JSON.parse(result);
	dataDownLoadOutputs(data.rows);
}

function dataDownLoadOutputs(rows) {
	var tuningNoArry = "";
	var choiceDivArry = "";
	
	if ( rows != null && rows != "" ) {
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		totalCnt = rows.length;
		$("#submit_form #tuningNoArry").val("");
		$("#submit_form #choiceDivArry").val("");
		
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";
			choiceDivArry += rows[i].choice_div_cd + "|";
		}
		
		$("#submit_form #tuningNoArry").val(strRight(tuningNoArry,1));
		$("#submit_form #choiceDivArry").val(strRight(choiceDivArry,1));
		
		if ( rows.length > 1 ){
			$("#submit_form").attr("action","/getPerformanceImprovementOutputsAll");
		}else {
			$("#submit_form #tuning_no").val( rows[0].tuning_no );
			$("#submit_form").attr("action","/getPerformanceImprovementOutputs_V2");
			
		}
		$("#submit_form").submit();
		
	} else {
		parent.$.messager.alert('','산출물 다운로드할 대상을 선택해 주세요.');
	}
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallPerformanceImprovementOutputs();
}
