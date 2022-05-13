//최종 성능 점검 단계
var final_perf_check_step;
var clickedFieldId;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 업무구분 조회
	$('#wrkjob_cd').combobox({
		url:"/Common/getWrkJob",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
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
			// 최종점검단계 조회
			$('#last_perf_check_step_id').combobox({
				url:"/getPerfCheckStep?wrkjob_cd="+rec.wrkjob_cd,
				method:"get",
				valueField:'perf_check_step_id',
				textField:'perf_check_step_nm',
				onLoadSuccess: function(items){
					if (items.length){
						$(this).combobox('setValue',last_perf_check_step_id);
					}
				},
				onChange:function(newValue, oldValue){
				}
			});
		}
	});
	
	// 점검상태 조회
	$('#deploy_check_status_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1056&isAll=Y",
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
		onChange:function(newValue, oldValue){
			//Btn_OnClick();
		},
		onSelect:function(rec){
		}
	});
	// 성능점검완료방법코드 조회
	$('#perf_check_complete_meth_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1071&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
				var default_perf_check_complete_meth_cd = perf_check_complete_meth_cd;
				if(default_perf_check_complete_meth_cd != ""){
					$(this).combobox('setValue',default_perf_check_complete_meth_cd);
				}
			}
		},
		onChange:function(newValue, oldValue){
		},
		onSelect:function(rec){
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		onClickRow : function(index,row) {
			$("#submit_form #wrkjob_cd").val(row.wrkjob_cd);
			$("#submit_form #wrkjob_cd_nm").val(row.wrkjob_cd_nm);
			$("#submit_form #perf_check_id").val(row.perf_check_id);
			$("#submit_form #search_wrkjob_cd").val(row.wrkjob_cd);
			$("#submit_form #search_wrkjob_cd_nm").val(row.wrkjob_cd_nm);
			$("#submit_form #search_deploy_id").val(row.deploy_id);
			
			$("#submit_form #top_wrkjob_cd").val($("#wrkjob_cd").combobox("getValue"));
			
			final_perf_check_step = row.final_perf_check_step;
			fnGoToPerfCheckStage();
		},
		onClickCell : function(rowIdx, fieldId) {
			var rows = $(this).datagrid('getRows');
			for(var i=0;i<rows.length;i++){
				$(this).datagrid('unselectRow', i);			
			}
			clickedFieldId = fieldId;
		},
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'deploy_id',title:'배포ID',halign:"center",sortable:"true",align:'center',styler:cellStylerUnderLine},
			{field:'deploy_nm',title:'배포명',width:'300px',halign:"center",sortable:"true",align:'left'},
			{field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true"},
			{field:'deploy_check_status_nm',title:'점검상태',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_step_nm',title:'최종점검단계',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_result_div_nm',title:'최종점검단계결과',halign:"center",sortable:"true",align:'center',styler:cellStyler},
			{field:'check_tgt_cnt',title:'전체(신규/변경/동일)',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_complete_meth_nm',title:'완료방법',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_requester_nm',title:'배포요청자',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_request_dt',title:'배포요청일시',halign:"center",sortable:"true",align:'center'},
			{field:'deploy_check_status_update_dt',title:'점검요청일시',halign:"center",sortable:"true",align:'center'},
//			{field:'deploy_expected_day',title:'배포예정일',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_complete_dt',title:'점검완료일시',halign:"center",sortable:"true",align:'center'},
			{field:'check_result_anc_yn',title:'점검결과통보여부',halign:"center",sortable:"true",align:'center'},
			{field:'last_check_result_anc_dt',title:'점검결과통보일시',halign:"center",sortable:"true",align:'center'},
			{field:'check_result_anc_cnt',title:'점검결과통보횟수',halign:"center",sortable:"true",align:'center'},
			{field:'perf_check_id',title:'성능점검ID',halign:"center",align:'center',sortable:"true"},
//			{field:'final_perf_check_step',title:'최종성능점검단계',halign:"center",sortable:"true",align:'center'},

			{field:'deploy_update_sbst',title:'배포변경내용',hidden:"true"},
//			{field:'last_perf_check_step_id',title:'최종성능점검단계ID',halign:"center",sortable:"true",align:'center'},
			{field:'last_perf_check_step_id',title:'최종성능점검단계ID',hidden:"true"},
			{field:'wrkjob_cd',title:'업무코드',hidden:"true"},
			{field:'before_perf_check_id',title:'이전성능점검ID',hidden:"true"},
			{field:'deploy_check_status_cd',title:'배포성능점검상태코드',hidden:"true"},
			{field:'perf_check_result_div_cd',title:'성능점검결과구분코드',hidden:"true"},
			{field:'deploy_requester_id',title:'배포요청자ID',hidden:"true"},
			{field:'final_perf_check_step',title:'최종성능점검단계',hidden:"true"}
		]],		
		onLoadSuccess: function(data){
			var opts = $(this).datagrid('options');
			if(data.rows.length > 0){
				$(this).datagrid('selectRow', 0);			
				$("#submit_form #wrkjob_cd").val(data.rows[0].wrkjob_cd);
				$("#submit_form #top_wrkjob_cd").val($("#wrkjob_cd").combobox("getValue"));
				$("#submit_form #perf_check_id").val(data.rows[0].perf_check_id);
				$("#submit_form #search_wrkjob_cd").val(data.rows[0].search_wrkjob_cd);
				fnGoToPerfCheckStage();
			}
		},		
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});

	$("#tableList2").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			if(row.perf_test_target_step_yn == 'N'){
				parent.$.messager.alert('알림','이전 단계에서 성능점검이 완료되지 않아 성능점검결과 화면으로 이동할 수 없습니다.','info');
				return;
			}
			$("#submit_form #perf_check_step_id").val(row.perf_check_step_id);
			$("#submit_form #search_perf_check_step_id").val(row.perf_check_step_id);
			fnPerfCheckResult();
		},			
		columns:[[
//			{field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true"},
			{field:'perf_check_step_id',title:'점검단계ID',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_step_nm',title:'점검단계',halign:"center",align:'center',sortable:"true"},
			{field:'perf_test_complete_yn',title:'점검완료',halign:"center",align:'center',sortable:"true",styler:cellStyler1},
			{field:'perf_test_db_name',title:'점검대상DB',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_request_dt',title:'점검요청일시',halign:"center",align:'center',sortable:"true"},
			{field:'perf_check_complete_dt',title:'점검완료일시',halign:"center",align:'center',sortable:"true"},
//			{field:'perf_check_result_div_nm',title:'점검결과',halign:"center",align:'center',sortable:"true",styler:cellStyler2},
			{field:'perf_check_result_div_nm',title:'점검결과',halign:"center",align:'center',sortable:"true",styler:cellStyler},
//			{field:'perf_check_request_type_nm',title:'점검요청유형',halign:"center",align:'center',sortable:"true"},

			{field:'total_cnt',title:'전체건수',halign:"center",align:'center',sortable:"true"},
			{field:'exception_cnt',title:'점검제외건수',halign:"center",align:'center',sortable:"true"},
			{field:'pass_cnt',title:'적합건수',halign:"center",align:'center',sortable:"true"},
			{field:'fail_cnt',title:'부적합건수',halign:"center",align:'center',sortable:"true"},
			{field:'err_cnt',title:'오류건수',halign:"center",align:'center',sortable:"true"},
			{field:'test_miss_cnt',title:'미수행건수',halign:"center",sortable:"true",align:'center'},
//			{field:'perf_test_target_step_yn',title:'성능점검대상단계여부',halign:"center",sortable:"true",align:'center'},
			
			{field:'perf_check_id',title:'성능점검ID',hidden:"true"},
			{field:'perf_check_result_div_cd',title:'성능점검결과구분코드',hidden:"true"},
			{field:'perf_check_request_type_cd',title:'성능점검요청유형코드',hidden:"true"},
			{field:'perf_test_target_step_yn',title:'성능점검대상단계여부',hidden:"true"}
		]],		
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
					fnPerfCheckResult();
				}
			}			
		},		
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});	
	
	var call_from_parent = $("#call_from_parent").val();
	if(call_from_parent == "Y"){
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
	 * 성능점검결과 통보 대상
	 * 점검상태 : 점검완료(02), 강제점검완료(09)
	 * 점검결과통보여부 : 미통보(N)
	 */
	$("#perfChkRsltNoti").click(function(){
		var isOk= true;
		var rows = $('#tableList').datagrid('getChecked');
		if(rows.length <= 0){
			parent.$.messager.alert('',"점검결과 통보 대상을 선택하여 주세요.");
			return;
		}
//		var row;
//		for(var i=0;i<rows.length;i++){
//			row = rows[i];
//			var deploy_check_status_cd = row.deploy_check_status_cd;
//			if(!(deploy_check_status_cd == '02' || deploy_check_status_cd == "09")){
//				isOk = false;
//			}
//			var check_result_anc_yn = row.check_result_anc_yn;
//			if(check_result_anc_yn != 'N'){
//				isOk = false;
//			}
//		}
		isOk = true;
		if(!isOk){
			parent.$.messager.alert('',"점검결과 통보 미대상이 포함되었습니다.");
			return;
		}else{
			parent.$.messager.confirm('', "점검결과를 배포시스템에 통보하시겠습니까?", function(r){
				if (r){
					if(parent.openMessageProgress != undefined) parent.openMessageProgress("점검결과 통보"," ");
					var paramArry = "";
					
					for(var i=0; i<rows.length; i++){
						paramArry += rows[i].deploy_id + "^" + rows[i].perf_check_id + "^" + rows[i].perf_check_result_div_cd + "|";
					}		
					if(strRight(paramArry,1) == ""){
						parent.$.messager.alert('',"튜닝중으로 변경할 항목을 다시 선택하여 주세요",'error');
					}
					$("#submit_form #params").val(strRight(paramArry,1));
						
					ajaxCall("/PerfChkRsltNoti", $("#submit_form"), "POST", callback_PerfChkRsltNotiAction);	
				}
			});
		}
	});
	/**
	 * 강제 점검완료
	 * 강제 점검완료 대상
	 * 점검상태 : 개발확정(00), 점검중(01)
	 * 성능점검결과 통보 권한 : 배포성능관리자(5)
	 */
	$("#perfChkForceFinish").click(function(){
		var isOk= true;
		var rows = $('#tableList').datagrid('getChecked');
		console.log("rows.length :"+rows.length);
		if(rows.length <= 0){
			parent.$.messager.alert('',"강제 점검완료 대상을 선택하여 주세요.");
			return;
		}
		var row;
		for(var i=0;i<rows.length;i++){
			row = rows[i];
			var deploy_check_status_cd = row.deploy_check_status_cd;
			if(!(deploy_check_status_cd == '00' || deploy_check_status_cd == "01")){
				isOk = false;
			}
			//var check_result_anc_yn = row.check_result_anc_yn;
			//console.log("check_result_anc_yn :"+check_result_anc_yn);
			//if(check_result_anc_yn != 'N'){
			//	isOk = false;
			//}
		}
		//isOk = true;
		if(!isOk){
			parent.$.messager.alert('',"강제점검완료 미대상이 포함되었습니다.");
			return;
		}else{
			parent.$.messager.confirm('', "강제 점검 완료하시겠습니까? 해당 성능점검대상이 강제 점검 완료된 후에 배포시스템으로 완료 통보됩니다.", function(r){
				if (r){
					if(parent.openMessageProgress != undefined) parent.openMessageProgress("강제 점검 완료"," ");
					var paramArry = "";
					
					rows = $('#tableList').datagrid('getSelections');

					for(var i=0; i<rows.length; i++){
						paramArry += rows[i].deploy_id +"^"+rows[i].perf_check_id + "|";
					}		
					if(strRight(paramArry,1) == ""){
						parent.$.messager.alert('',"튜닝중으로 변경할 항목을 다시 선택하여 주세요",'error');
					}
					$("#submit_form #params").val(strRight(paramArry,1));
					//성능점검결과 > 성능점검완료 참조
//					ajaxCall("/PerformanceCheckComplete", $("#submit_form"), "POST", callback_PerformanceCheckComplete);
					ajaxCall("/PerfChkForceFinish", $("#submit_form"), "POST", callback_PerfChkForceFinish);	
				}
			});
		}
	});
	
	$('#deploy_id').textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});
	
});

var callback_PerfChkRsltNotiAction = function(data){
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	try{
		if(data.result){
			parent.$.messager.alert('성능점검결과통보',data.message,'info',function(){
				setTimeout(function() {
					Btn_OnClick();
				},1000);
			});
		}else{
//			if(result.message == undefined){
//				$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
//					setTimeout(function() {
//						top.location.href="/auth/login";
//					},1000);
//				});
//			}else{
//				parent.$.messager.alert('',result.message,'info');
//			}
			parent.$.messager.alert('',result.message,'info');
			
		}
	}catch(e){
//		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
//			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
//				setTimeout(function() {
//					top.location.href="/auth/login";
//				},1000);
//			});
//		}else{
//			parent.$.messager.alert('',e.message);
//		}
		parent.$.messager.alert('',"오류가 발생하였습니다.");
		
	}
}
//callback 함수
var callback_PerfChkForceFinish = function(result) {
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	try{
		if(result.result){
			parent.$.messager.alert('강제 점검완료',result.message,'info',function(){
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
				parent.$.messager.alert('',result.message,'info');
			}
		}
	}catch(e){
//		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
//			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
//				setTimeout(function() {
//					top.location.href="/auth/login";
//				},1000);	
//			});
//		}else{
//			parent.$.messager.alert('',e.message);
//		}
		parent.$.messager.alert('',"오류가 발생하였습니다.");
		
	}
};

//function fnSetCurrentPage(direction){
//	var currentPage = $("#submit_form #currentPage").val();
//	
//	if(currentPage != null && currentPage != ""){
//		if(direction == "PREV"){
//			currentPage--;
//		}else if(direction == "NEXT"){
//			currentPage++;
//		}
//		
//		$("#submit_form #currentPage").val(currentPage);
//	}else{
//		$("#submit_form #currentPage").val("1");
//	}
//}

//function fnGoPrevOrNext(direction){
//	fnSetCurrentPage(direction);
//	
//	var currentPage = $("#submit_form #currentPage").val();
//	currentPage = parseInt(currentPage);
//	if(currentPage <= 0){
//		$("#submit_form #currentPage").val("1");
//		return;
//	}
//	fnSearch();
//}

function formValidationCheck(){
	if($('#submit_form #wrkjob_cd').combobox('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #deploy_request_dt_1').textbox('getValue') == ""){
		parent.$.messager.alert('','배포요청일시 검색 시작일을 선택해 주세요.');
		return false;
	}
	
	if($('#submit_form #deploy_request_dt_2').textbox('getValue') == ""){
		parent.$.messager.alert('','배포요청일시 검색 종료일을 선택해 주세요.');
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
	$('#tableList').datagrid("loadData", []);
	//페이징버튼 초기화
//	fnInitPagingBtn();
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	ajaxCallPerformanceCheckMngList();
}

function ajaxCallPerformanceCheckMngList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능개선결과 산출물"," ");
	ajaxCall("/PerformanceCheckMngList", $("#submit_form"), "POST", callback_PerformanceCheckMngList);
}

//callback 함수
var callback_PerformanceCheckMngList = function(result) {
	json_string_callback_common(result,'#tableList',true);
//	fnControlPaging(result);
	
	try{
		var data = JSON.parse(result);
		var dataLength=0;
		dataLength = data.dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
	}catch(e){
		console.log("e.message:"+e.message);
	}

};

//var fnInitPagingBtn = function(){
//	$("#prevBtnEnabled").hide();
//	$("#prevBtnDisabled").show();
//	$("#nextBtnEnabled").hide();
//	$("#nextBtnDisabled").show();
//}
//
//var fnControlPaging = function(result) {
//	//페이징 처리
//	var currentPage = $("#submit_form #currentPage").val();
//	currentPage = parseInt(currentPage);
//	var pagePerCount = $("#submit_form #pagePerCount").val();
//	pagePerCount = parseInt(pagePerCount);
//
//	var data;
//	var dataLength=0;
//	try{
//		data = JSON.parse(result);
//		dataLength = data.dataCount4NextBtn;
//	}catch(e){
//		if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
//			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
//				setTimeout(function() {
//					top.location.href="/auth/login";
//				},1000);	
//			});			
//		}else{
//			parent.$.messager.alert('',e.message);
//		}		
//	}
//	
//	if(currentPage > 1){
//		$("#prevBtnDisabled").hide();
//		$("#prevBtnEnabled").show();
//		
//		if(dataLength > 10){
//			$("#nextBtnDisabled").hide();
//			$("#nextBtnEnabled").show();
//		}else{
//			$("#nextBtnDisabled").show();
//			$("#nextBtnEnabled").hide();
//		}
//	}
//	if(currentPage == 1){
//		$("#prevBtnDisabled").show();
//		$("#prevBtnEnabled").hide();
//		if(dataLength > pagePerCount){
//			$("#nextBtnDisabled").hide();
//			$("#nextBtnEnabled").show();
//		}
//	}	
//};

/**
 * 엑셀 다운로드
 * @returns
 */
function Excel_Download(no){	
	if(!formValidationCheck()){
		return;
	}
	if(no == 1){
		var rows = $('#tableList').datagrid('getRows');
		if(rows.length <= 0){
			parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
			return false;	
		}
	}else if(no == 2){
		var rows = $('#tableList2').datagrid('getRows');
		if(rows.length <= 0){
			parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
			return false;	
		}
	}

	$("#submit_form").attr("action","/PerformanceCheckMngList/ExcelDown"+no);
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function fnGoToPerfCheckStage(){
	ajaxCallPerformanceCheckStageList();
}

function ajaxCallPerformanceCheckStageList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능점검단계 데이터 조회"," ");
	ajaxCall("/PerformanceCheckStageList", $("#submit_form"), "POST", callback_PerformanceCheckStageList);
}

//callback 함수
var callback_PerformanceCheckStageList = function(result) {
	json_string_callback_common(result,'#tableList2',true);
};
//성능점검결과탭
function fnPerfCheckResult(){
	var menuId = $("#menu_id").val();
	var menuParam = "perf_check_id="+$("#perf_check_id").val();
	menuParam += "&perf_check_step_id="+$("#perf_check_step_id").val();
	menuParam += "&wrkjob_cd="+$("#wrkjob_cd").combobox('getValue');
//	menuParam += "&wrkjob_cd_nm="+$("#wrkjob_cd").combobox('getText');
	menuParam += "&wrkjob_cd_nm="+encodeURIComponent($("#wrkjob_cd").combobox('getText'));
	menuParam += "&search_wrkjob_cd="+$("#search_wrkjob_cd").val();
//	menuParam += "&search_wrkjob_cd_nm="+$("#search_wrkjob_cd_nm").val();
	menuParam += "&search_wrkjob_cd_nm="+encodeURIComponent($("#search_wrkjob_cd_nm").val());
	menuParam += "&deploy_id="+$("#search_deploy_id").val();

	parent.openLink("Y",menuId+1, "성능 점검 결과", "/PerformanceCheckResult", menuParam);
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
	}else if(row.perf_check_result_div_nm == '점검제외'){
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

//function cellStyler2(value,row,index){
//	if(row.perf_check_result_div_nm == '적합'){
//		return 'background-color:#1A9F55;color:white;';
//	}else if(row.perf_check_result_div_nm == '부적합'){
//		return 'background-color:#E41E2C;color:white;';
//	}else if(row.perf_check_result_div_nm == '오류'){
//		return 'background-color:#ED8C33;color:white;';
//	}else if(row.perf_check_result_div_nm == '미수행'){
//		return 'background-color:#7F7F7F;color:white;';
//	}else if(row.perf_check_result_div_nm == '수행중'){
//		return 'background-color:#89BD4C;color:white;';
//	}else if(row.perf_check_result_div_nm == '점검제외'){
//		return 'background-color:#012753;color:white;';
//	}	
//}
