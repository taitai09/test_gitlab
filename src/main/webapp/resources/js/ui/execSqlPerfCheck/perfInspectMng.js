var final_perf_check_step;	//최종 성능검증 단계
var clickedFieldId;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 업무구분 조회
	createWrkjobCode();
	
	// 검증상태 조회
	createStatusCombo();
	
	// 성능검증완료방법코드 조회
	createCompleteMethCombo();
	
	createUpperTable();
	createLowerTable();
	
	//이전, 다음 처리
	$("#prevBtnEnabled").on('click', function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	
	$("#nextBtnEnabled").on('click', function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	let call_from_parent = $("#call_from_parent").val();
	if(call_from_parent == "Y"){
		$("#wrkjob_cd").val(wrkjob_cd);
		$("#deploy_request_dt_1").val(deploy_request_dt_1);
		$("#deploy_request_dt_2").val(deploy_request_dt_2);
		$("#deploy_check_status_cd").val(deploy_check_status_cd);
		$("#last_perf_check_step_id").val(last_perf_check_step_id);
		$("#deploy_requester_nm").val(deploy_requester_nm);
		$("#deploy_id").val(deploy_id);
		
		$("input[name='wrkjob_cd']").val(wrkjob_cd);
		$("input[name='deploy_request_dt_1']").val(deploy_request_dt_1);
		$("input[name='deploy_request_dt_2']").val(deploy_request_dt_2);
		$("input[name='deploy_check_status_cd']").val(deploy_check_status_cd);
		$("input[name='last_perf_check_step_id']").val(last_perf_check_step_id);
		$("input[name='deploy_requester_nm']").val(deploy_requester_nm);
		$("input[name='deploy_id']").val(deploy_id);
		
		fnSearch();
	}
	/**
	 * 성능검증결과 통보 대상
	 * 검증상태 : 검증완료(02), 강제검증완료(09)
	 * 검증결과통보여부 : 미통보(N)
	 */
	$("#perfChkRsltNoti").on("click", function(){
		inspectRsltNoti();
	});
	
	/**
	 * 강제 검증완료
	 * 강제 검증완료 대상
	 * 검증상태 : 개발확정(00), 검증중(01)
	 * 성능검증결과 통보 권한 : 배포성능관리자(5)
	 */
	$("#perfChkForceFinish").on("click", function(){
		forceComplete();
	});
	
	$('#deploy_id').textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){	// when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});
	
});

function inspectRsltNoti(){
	let isOk= true;
	let rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length <= 0){
		warningMessager("검증결과 통보 대상을 선택하여 주세요.")
		return;
	}
	rows.every( function(value, index, array){			//검증완료인 건만 선택됐는지 확인
		if( value.deploy_check_status_cd != '02' ){
			isOk = false;
			return false;
		}
	});
	
	if( isOk == false ){
		warningMessager('검증이 완료되지 않았습니다. 검증완료 건에 대해 통보가 가능합니다.');
		return;
		
	}else{
		parent.$.messager.confirm('', "검증결과를 배포시스템에 통보하시겠습니까?", function(r){
			if (r){
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("검증결과 통보"," ");
				let paramArry = "";
				
				for(let i=0; i<rows.length; i++){
					paramArry += rows[i].deploy_id + "^" + rows[i].perf_check_id + "^" + rows[i].perf_check_result_div_cd + "|";
				}
				if(strRight(paramArry,1) == ""){
					warningMessager("튜닝중으로 변경할 항목을 다시 선택하여 주세요");
				}
				$("#submit_form #params").val(strRight(paramArry,1));
				
				ajaxCall("/perfInspectMng/inspectRsltNoti",
						$("#submit_form"),
						"POST",
						callback_inspectRsltNoti);
			}
		});
	}
}

var callback_inspectRsltNoti = function(data){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	try{
		if(data.result){
			parent.$.messager.alert('성능검증결과통보',data.message,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
				},1000);
			});
		}else{
			infoMessager(result.message);
		}
	}catch(e){
		errorMessager("오류가 발생하였습니다.");
	}
}

function forceComplete(){
	let isOk= true;
	let rows = $('#tableList').datagrid('getChecked');
	
	if(rows.length <= 0){
		warningMessager("강제 검증 완료 대상을 선택하여 주세요.");
		return;
	}
	
	let row;
	for(let i=0; i < rows.length; i++){
		row = rows[i];
		let deploy_check_status_cd = row.deploy_check_status_cd;
		
		// 개발확정이거나 검증중 일 경우에만 강제 검증 완료 가능
		if(!(deploy_check_status_cd == '00' || deploy_check_status_cd == "01")){
			isOk = false;
		}
	}
	
	if(!isOk){
		warningMessager("강제 검증 완료 미대상이 포함되었습니다.");
		return;
		
	}else{
		parent.$.messager.confirm('', "강제 검증 완료하시겠습니까? 해당 성능검증대상이 강제 검증 완료된 후에 배포시스템으로 완료 통보됩니다.", function(r){
			if (r){
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("강제 검증 완료"," ");
				let paramArry = "";
				
				rows = $('#tableList').datagrid('getSelections');
				
				for(let i=0; i<rows.length; i++){
					paramArry += rows[i].deploy_id +"^"+rows[i].perf_check_id + "|";
				}
				if(strRight(paramArry,1) == ""){
					errorMessager("튜닝중으로 변경할 항목을 다시 선택하여 주세요");
				}
				$("#submit_form #params").val(strRight(paramArry,1));
				
				ajaxCall("/perfInspectMng/inspectForceFinish",
						$("#submit_form"),
						"POST",
						callback_forceComplete);
			}
		});
	}
}

//callback 함수
var callback_forceComplete = function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	try{
		if(result.result){
			parent.$.messager.alert('강제 검증 완료',result.message,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
				},1000);
			});
			
		}else{
			if(result.message == undefined){
				$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
					setTimeout(function() {
						top.location.href="/auth/login";
					},1000);
				});
			}else{
				infoMessager(result.message);
			}
		}
	}catch(e){
		errorMessager("오류가 발생하였습니다.");
	}
};

function createWrkjobCode(){
	$('#wrkjob_cd').combobox({
		url:"/Common/getWrkJob",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			errorMessager('업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items){
			if (items.length){
				$(this).combobox('setValue',wrkjob_cd);
			}
			
			$('#wrkjob_cd').combobox("textbox").attr("placeholder","선택");
		},
		onSelect:function(rec){
			$("#wrkjob_cd").val(rec.wrkjob_cd);
			// 최종검증단계 조회
			createStepCombo(rec.wrkjob_cd);
		}
	});
}
//업무구분 조회
function createStepCombo(wrkjob_cd){
	$('#last_perf_check_step_id').combobox({
		url:"/perfInspectMng/getPerfCheckStep?wrkjob_cd="+wrkjob_cd,
		method:"get",
		valueField:'perf_check_step_id',
		textField:'perf_check_step_nm',
		onLoadSuccess: function(items){
			if (items.length){
				$(this).combobox('setValue',last_perf_check_step_id);
			}
		}
	});
}

//검증상태 조회
function createStatusCombo(){
	$('#deploy_check_status_cd').combobox({
		url:"/Common/getCommonCodeRef2?grp_cd_id=1056&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
				var default_deploy_check_status_cd = deploy_check_status_cd;
				for(var i=0;i<items.length;i++){
					if(items[i].ref_vl_1 == "Y"){
						default_deploy_check_status_cd = items[i].cd;
					}
				}
				$(this).combobox('setValue',default_deploy_check_status_cd);
			}
		},
		onSelect:function(rec){
		}
	});
}

//성능검증완료방법코드 조회
function createCompleteMethCombo(){
	$('#perf_check_complete_meth_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1071&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
				let default_perf_check_complete_meth_cd = perf_check_complete_meth_cd;
				if(default_perf_check_complete_meth_cd != ""){
					$(this).combobox('setValue',default_perf_check_complete_meth_cd);
				}
			}
		},
	});
}

function createUpperTable(){
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'deploy_id',title:'배포ID',halign:"center",sortable:"true",align:'center',styler:cellStylerUnderLine},
			{field:'deploy_nm',title:'배포명',width:'300px',halign:"center",sortable:"true",align:'left'},
			{field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true"},
			{field:'deploy_check_status_nm',title:'검증상태',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_step_nm',title:'최종검증단계',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_result_div_nm',title:'최종검증단계결과',halign:"center",sortable:"true",align:'center',styler:cellStyler},
			{field:'check_tgt_cnt',title:'전체(신규/변경/동일)',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_complete_meth_nm',title:'완료방법',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_requester_nm',title:'배포요청자',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_request_dt',title:'배포요청일시',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_check_status_update_dt',title:'검증요청일시',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_complete_dt',title:'검증완료일시',halign:"center",sortable:"true",align:'center'},
			{field:'check_result_anc_yn',title:'검증결과통보여부',halign:"center",sortable:"true",align:'center'},
			{field:'last_check_result_anc_dt',title:'검증결과통보일시',halign:"center",sortable:"true",align:'center'},
			{field:'check_result_anc_cnt',title:'검증결과통보횟수',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_id',title:'성능검증ID',halign:"center",align:'center',sortable:"true"},
			
			{field:'deploy_update_sbst',title:'배포변경내용',hidden:"true"},
			{field:'last_perf_check_step_id',title:'최종성능검증단계ID',hidden:"true"},
			{field:'wrkjob_cd',title:'업무코드',hidden:"true"},
			{field:'before_perf_check_id',title:'이전성능검증ID',hidden:"true"},
			{field:'deploy_check_status_cd',title:'배포성능검증상태코드',hidden:"true"},
			{field:'perf_check_result_div_cd',title:'성능검증결과구분코드',hidden:"true"},
			{field:'deploy_requester_id',title:'배포요청자ID',hidden:"true"},
			{field:'final_perf_check_step',title:'최종성능검증단계',hidden:"true"}
		]],
		onClickRow : function(index,row) {
			final_perf_check_step = row.final_perf_check_step;
		},
		onClickCell : function(rowIdx, fieldId) {
			let rows = $(this).datagrid('getRows');
			
			for(let i=0;i<rows.length;i++){
				$(this).datagrid('unselectRow', i);
			}
			clickedFieldId = fieldId;
		},
		onSelect:function(index,row){
			setDataToForm( row );
			fnGoToPerfCheckStage();
		},
		onLoadSuccess: function(data){
			let opts = $(this).datagrid('options');
			
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);
			}
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function createLowerTable(){
	$("#tableList2").datagrid({
		view: myview,
		columns:[[
			{field:'perf_check_step_id',title:'검증단계ID',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_step_nm',title:'검증단계',halign:"center",align:'center',sortable:"true"},
			{field:'perf_test_complete_yn',title:'검증완료',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'perf_test_db_name',title:'검증대상DB',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_request_dt',title:'검증요청일시',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_complete_dt',title:'검증완료일시',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_result_div_nm',title:'검증결과',halign:"center",align:'center',sortable:"true",styler:cellStyler},

			{field:'total_cnt',title:'전체건수',halign:"center",align:'center',sortable:"true"},
			{field:'exception_cnt',title:'검증제외건수',halign:"center",align:'center',sortable:"true"},
			{field:'pass_cnt',title:'적합건수',halign:"center",align:'center',sortable:"true"},
			{field:'fail_cnt',title:'부적합건수',halign:"center",align:'center',sortable:"true"},
			{field:'test_miss_cnt',title:'미수행건수',halign:"center",sortable:"true",align:'center'},
			
			{field:'perf_check_id', hidden:"true"},
			{field:'perf_check_result_div_cd', hidden:"true"},
			{field:'perf_check_request_type_cd', hidden:"true"},
			{field:'perf_test_target_step_yn', hidden:"true"}
		]],
		onSelect : function(index,row) {
			if(row.perf_test_target_step_yn == 'N'){
				infoMessager('이전 단계에서 성능검증이 완료되지 않아 성능검증결과 화면으로 이동할 수 없습니다.');
				return;
			}
			
			$("#submit_form #perf_check_step_id").val(row.perf_check_step_id);
			$("#submit_form #search_perf_check_step_id").val(row.perf_check_step_id);
			
			fnPerfCheckResult(row);
		},
		onLoadSuccess: function(data){
			var opts = $(this).datagrid('options');
			for(var i = 0; i < data.rows.length ; i++){
				var row = data.rows[i];
				if(row.perf_test_complete_yn == "Y"){
					var tr = opts.finder.getTr(this,i);
					tr.find('input[type=checkbox]').attr('disabled','disabled');
				}
			}
			if(final_perf_check_step == 1){
				final_perf_check_step = 0;
				$("#submit_form #perf_check_step_id").val(1);
				$("#submit_form #search_perf_check_step_id").val(1);
				
				if(clickedFieldId == "deploy_id"){
					$(this).datagrid('selectRow', 0);
				}
			}
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function formValidationCheck(){
	if($('#submit_form #wrkjob_cd').combobox('getValue') == ""){
		warningMessager('업무를 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #deploy_request_dt_1').textbox('getValue') == ""){
		warningMessager('배포요청일자 검색 시작일을 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #deploy_request_dt_2').textbox('getValue') == ""){
		warningMessager('배포요청일자 검색 종료일을 선택해 주세요.');
		return false;
	}	
	
	return true;
}

function Btn_OnClick(){
	//검색버튼을 누를 경우 현재 페이지 1번으로 초기화
	$("#submit_form #currentPage").val("1");
	
	if(!formValidationCheck()){
		return;
	}
	
	fnSearch();
}

function fnSearch(){
	//페이징버튼 초기화
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid("loadData", []);
	$('#tableList2').datagrid("loadData", []);
	
	setDataToSearch();
	
	ajaxCallPerformanceCheckMngList();
}

function ajaxCallPerformanceCheckMngList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 검증 관리"," ");
	
	ajaxCall("/perfInspectMng/loadInspectionList",
			$("#submit_form"),
			"POST",
			callback_PerformanceCheckMngList);
}

var callback_PerformanceCheckMngList = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	try{
		let data = JSON.parse(result);
		let dataLength=0;
		
		dataLength = data.dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
		
	}catch(e){
		console.log("e.message:"+e.message);
	}

};

function Excel_Download(no){
	if(!formValidationCheck()){
		return;
	}
	
	let url;
	if(no == 1){
		setDataToSearch();
		url = "/perfInspectMng/loadInspectionList/ExcelDown";
		
	}else if(no == 2){
		setDataToForm( $('#tableList').datagrid('getSelected') );
		url = "/perfInspectMng/loadInspectionStep/ExcelDown";
	}
	
	$("#submit_form").attr("action", url);
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}
// 성능검증 재수행
function ReExecution(){
	let selectData = $('#tableList').datagrid('getChecked');	//현재 선택된 데이터들
	
	if( !selectData || (selectData && selectData.length < 1) ){	//선택 데이터가 없는 경우
		warningMessager('배포 프로그램을 선택 해 주세요.');
		return false;
	}
	
	let isOnlyIncomplete = true;
	selectData.every( function(value, index, array){			//검증중인 건만 선택됐는지 확인
		if( value.deploy_check_status_cd != '01' ){
			isOnlyIncomplete = false;
			return false;
		}
	});
	
	if( isOnlyIncomplete == false ){
		warningMessager('검증상태가 검증중인 배포 프로그램만 재수행 가능합니다.');
		return false;
	}
	
	let param = "확인";
	let msgStr = '성능검증 재수행시 이전 성능검증 결과가 삭제됩니다.<br>';
		msgStr+= '계속 진행하시겠습니까?';
		
	parent.$.messager.confirm(param,msgStr,function(r) {
		if (r) {
			/* modal progress open */
			if (parent.openMessageProgress != undefined) parent.openMessageProgress("성능검증 재수행", "성능검증 재수행 중입니다.");
			
			let perf_check_id_arr = "";
			for(i = 0; i < selectData.length; i++){
				perf_check_id_arr += selectData[i].perf_check_id + "&";
			}
			$('#perf_check_id').val(perf_check_id_arr);
			
			ajaxCallReExecution();
		}
	});
	
}

function ajaxCallReExecution(){
	ajaxCall("/perfInspectMng/reExecution",
			$("#submit_form"),
			"POST",
			callback_ReExecution);
}

var callback_ReExecution = function(result){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		infoMessager('성능검증 재수행이 완료되었습니다.');
		
	} else {
		errorMessager("성능검증 재수행 중<br>오류가 발생하였습니다.");
	}
	
	Btn_OnClick();
}

function fnGoToPerfCheckStage(){
	ajaxCallPerformanceCheckStageList();
}

function ajaxCallPerformanceCheckStageList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능검증단계 데이터 조회"," ");
	
	ajaxCall("/perfInspectMng/loadInspectionStep",
			$("#submit_form"),
			"POST",
			callback_PerformanceCheckStageList);
}

var callback_PerformanceCheckStageList = function(result) {
	json_string_callback_common(result,'#tableList2',true);
};

//성능검증결과탭
function fnPerfCheckResult(row){
	let menuId = parseInt( $("#menu_id").val() ) + 1;
	
	let menuParam = ""
	menuParam += "perf_check_id="+row.perf_check_id;
	menuParam += "&perf_check_step_id="+row.perf_check_step_id;
	menuParam += "&wrkjob_cd="+$("#wrkjob_cd").combobox('getValue');
	menuParam += "&wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd").combobox('getText'));
	menuParam += "&search_wrkjob_cd="+$("#search_wrkjob_cd").val();
	menuParam += "&search_wrkjob_cd_nm="+encodeURIComponent($("#search_wrkjob_cd_nm").val());
	menuParam += "&deploy_id="+$("#search_deploy_id").val();
	
	parent.parent.openLink("Y", String(menuId), "성능 검증 결과",
			"/perfInspectMng/inspectionResult", menuParam);
}

function cellStyler(value,row,index){
	if(row.perf_check_result_div_nm == '적합'){
		return 'background-color:#1A9F55;color:white;';
	}else if(row.perf_check_result_div_nm == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	}else if(row.perf_check_result_div_nm == '오류'){
		return 'background-color:#ED8C33;color:white;';
	}else if(row.perf_check_result_div_nm == '미수행'){
		return 'background-color:#7F7F7F;color:white;';
	}else if(row.perf_check_result_div_nm == '수행중'){
		return 'background-color:#89BD4C;color:white;';
	}else if(row.perf_check_result_div_nm == '검증제외'){
		return 'background-color:#012753;color:white;';
	}
}

function cellStyler1(value,row,index){
	if(row.perf_test_complete_yn == 'Y'){
		return 'background-color:#00FF00;';
	}else if(row.perf_test_complete_yn == 'N'){
		return 'background-color:#FFF000;';
	}
}

function cellStylerUnderLine(value,row,index){
	return "color:#0000ff;";
}

function setDataToSearch(){
	$("input[name='wrkjob_cd']").val( $("#wrkjob_cd").combobox('getValue') );
	$("input[name='deploy_request_dt_1']").val( $("#deploy_request_dt_1").datebox('getValue') );
	$("input[name='deploy_request_dt_2']").val( $("#deploy_request_dt_2").datebox('getValue') );
	$("input[name='deploy_check_status_cd']").val( $("#deploy_check_status_cd").combobox('getValue') );
	$("input[name='perf_check_complete_meth_cd']").val( $("#perf_check_complete_meth_cd").combobox('getValue') );
	$("input[name='last_perf_check_step_id']").val( $("#last_perf_check_step_id").combobox('getValue') );
	$("input[name='check_result_anc_yn']").val( $("#check_result_anc_yn").combobox('getValue') );
	$("input[name='deploy_id']").val( $("#deploy_id").textbox('getValue') );
	
	try{
		$("input[name='deploy_requester_nm']").val( $("#deploy_requester_nm").textbox('getValue') );
		
	}catch(e){
		console.log("input not exist");
	}
}

function setDataToForm(row){
	$("#submit_form [name='wrkjob_cd']").val(row.wrkjob_cd);
	$("#submit_form #top_wrkjob_cd").val( $("#wrkjob_cd").combobox("getValue") );
	$("#submit_form #search_wrkjob_cd").val(row.search_wrkjob_cd);
	$("#submit_form #wrkjob_cd_nm").val(row.wrkjob_cd_nm);
	$("#submit_form #search_wrkjob_cd_nm").val(row.wrkjob_cd_nm);
	$("#submit_form #perf_check_id").val(row.perf_check_id);
	$("#submit_form #search_deploy_id").val(row.deploy_id);
	$("#submit_form [name='deploy_id']").val(row.deploy_id);
	$("#submit_form #perf_check_result_div_nm").val(row.perf_check_result_div_nm);
	$("#submit_form #perf_check_result_div_cd").val(row.perf_check_result_div_cd);
}

