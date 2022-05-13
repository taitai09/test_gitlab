tab_index = "";
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#submit_form0 #search_wrkjob_cd').combobox({
//	    url:"/Common/getWrkJob",
	    url:"/Common/getWrkJob?deploy_check_target_yn=Y",
		method:'get',
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#submit_form1 #search_wrkjob_cd').combobox({
//	    url:"/Common/getWrkJob",
		url:"/Common/getWrkJob?deploy_check_target_yn=Y",
		method:'get',
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#submit_form2 #search_wrkjob_cd').combobox({
//		url:"/Common/getWrkJob",
		url:"/Common/getWrkJob?deploy_check_target_yn=Y",
		method:'get',
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	$("#submit_form0 #search_from_deploy_request_dt").textbox({disabled:true});
	$("#submit_form0 #search_to_deploy_request_dt").textbox({disabled:true});
	$("#submit_form1 #search_from_deploy_request_dt").textbox({disabled:true});
	$("#submit_form1 #search_to_deploy_request_dt").textbox({disabled:true});
	$("#submit_form2 #search_from_deploy_request_dt").textbox({disabled:true});
	$("#submit_form2 #search_to_deploy_request_dt").textbox({disabled:true});
	
	// 검색조건 프로그램
	$('#submit_form0 #searchKey').combobox({
		onChange:function(val){
			if(val == '01'){//배포요청일시
				$("#submit_form0 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form0 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '02'){//점검완료일시
				$("#submit_form0 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form0 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '03'){//배포예정일시
				$("#submit_form0 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form0 #search_to_deploy_request_dt").textbox({disabled:false});
			}else{
				$("#submit_form0 #search_from_deploy_request_dt").textbox({disabled:true});
				$("#submit_form0 #search_to_deploy_request_dt").textbox({disabled:true});
			}
		},
	});
	// 검색조건 프로그램
	$('#submit_form1 #searchKey').combobox({
		onChange:function(val){
			if(val == '01'){//배포요청일시
				$("#submit_form1 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form1 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '02'){//점검완료일시
				$("#submit_form1 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form1 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '03'){//배포예정일시
				$("#submit_form1 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form1 #search_to_deploy_request_dt").textbox({disabled:false});
			}else{
				$("#submit_form1 #search_from_deploy_request_dt").textbox({disabled:true});
				$("#submit_form1 #search_to_deploy_request_dt").textbox({disabled:true});
			}
		},
	});
	// 검색조건 프로그램2
	$('#submit_form2 #searchKey').combobox({
		onChange:function(val){
			if(val == '01'){//배포요청일시
				$("#submit_form2 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form2 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '02'){//점검완료일시
				$("#submit_form2 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form2 #search_to_deploy_request_dt").textbox({disabled:false});
			}else if(val == '03'){//배포예정일시
				$("#submit_form2 #search_from_deploy_request_dt").textbox({disabled:false});
				$("#submit_form2 #search_to_deploy_request_dt").textbox({disabled:false});
			}else{
				$("#submit_form2 #search_from_deploy_request_dt").textbox({disabled:true});
				$("#submit_form2 #search_to_deploy_request_dt").textbox({disabled:true});
			}
		},
	});

	getPerfCheckState0();
	getPerfCheckState1();
	getPerfCheckState2();
	
	var call_from_parent = $("#call_from_parent").val();
	var call_from_child = $("#call_from_child").val();
	if (call_from_parent == "Y" || call_from_child == "Y") {
		$('#search_wrkjob_cd').combotree('setValue',{
			id: search_wrkjob_cd,
			text: search_wrkjob_cd_nm
		});		
		
		Btn_OnClick(1);
		Btn_OnClick(2);
	}
	
});


function getPerfCheckState0(){
	
	$("#tableList0").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
			if(tab_index=='2'){
				checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
			}
			setDetailView(row);
//				getDeployPerfChkDetailResult();  //체크박스 테이블
		},		
		columns:
			[
				[
					{field:'wrkjob_cd_nm',title:'업무',width:"8%",halign:"center",align:'center',rowspan:2},
					{field:'request_cm_cnt',title:'요청',width:"8%",halign:"center",align:'center',rowspan:2},
					{title:'완료',halign:"center",align:'center',colspan:3},
					{title:'미완료',halign:"center",align:'center',colspan:5}
				],
				[
					{field:'complete_cm_cnt',title:'소계',width:"8%",halign:"center",align:'center'},
					{field:'pass_cm_cnt',title:'적합',width:"8%",halign:"center",align:'center'},
					{field:'fail_cm_cnt',title:'부적합',width:"8%",halign:"center",align:'center'},
					
					{field:'none_complete_cm_cnt',title:'소계',width:"8%%",halign:"center",align:'center'},
					{field:'dev_confirm_cm_cnt',title:'개발확정',width:"8%",halign:"center",align:'center'},
					{field:'checking_cm_cnt',title:'점검중',width:"8%",halign:"center",align:'center'},
					{field:'dev_confirm_cancel_cm_cnt',title:'개발확정취소',width:"8%",halign:"center",align:'center'},
					{field:'delete_cm_cnt',title:'배포삭제',width:"8%",halign:"center",align:'center'}
				]
			],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
};

function getPerfCheckState1(){
	
//	$("#tableList1").datagrid({
//			view: myview,
//			onClickRow : function(index,row) {
//
//				if(tab_index=='2'){
//					checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
//				}
//				setDetailView(row);
//			},		
//			columns:[[
//				{field:'wrkjob_cd_nm',title:'업무',width:"8%",halign:"center",align:'center'},
//				{field:'request_cm_cnt',title:'요청',width:"8%",halign:"center",align:'center'},
//				{field:'complete_cm_cnt',title:'완료',width:"8%",halign:"center",align:'center'},
//				{field:'none_complete_cm_cnt',title:'미완료',width:"8%",halign:"center",align:'center'},
//				{field:'pass_cm_cnt',title:'적합',width:"8%",halign:"center",align:'center'},
//				{field:'fail_cm_cnt',title:'부적합',width:"8%",halign:"center",align:'center'},
//				{field:'all_pgm_cnt',title:'전체 프로그램',width:"8%%",halign:"center",align:'center'},
//				{field:'pass_pgm_cnt',title:'적합 프로그램',width:"8%",halign:"center",align:'center'},
//				{field:'fail_pgm_cnt',title:'부적합 프로그램',width:"8%",halign:"center",align:'center'},
//				{field:'error_pgm_cnt',title:'오류 프로그램',width:"8%",halign:"center",align:'center'},
//				{field:'none_exec_pgm_cnt',title:'미수행 프로그램',width:"8%",halign:"center",align:'center'},
//				{field:'auto_pass_cnt',title:'점검제외 프로그램',width:"8%",halign:"center",align:'center'}
//			]],
//	    	onLoadError:function() {
//	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
//	    	}
//	});
	$("#tableList1").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			
			if(tab_index=='2'){
				checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
			}
			setDetailView(row);
		},		
		columns:
		[
			[
				{field:'wrkjob_cd_nm',title:'업무',width:"8%",halign:"center",align:'center',rowspan:2},
				{field:'all_pgm_cnt',title:'전체',width:"8%",halign:"center",align:'center',rowspan:2},
				{title:'점검결과',halign:"center",align:'center',colspan:5},
				{title:'개발구분',halign:"center",align:'center',colspan:3},
			],
			[
				{field:'pass_pgm_cnt',title:'적합',width:"8%",halign:"center",align:'center'},
				{field:'fail_pgm_cnt',title:'부적합',width:"8%",halign:"center",align:'center'},
				{field:'error_pgm_cnt',title:'오류',width:"8%",halign:"center",align:'center'},
				{field:'none_exec_pgm_cnt',title:'미수행',width:"8%",halign:"center",align:'center'},
				{field:'auto_pass_cnt',title:'점검제외',width:"8%",halign:"center",align:'center'},

				{field:'new_pgm_cnt',title:'신규',width:"8%%",halign:"center",align:'center'},
				{field:'change_pgm_cnt',title:'변경',width:"8%",halign:"center",align:'center'},
				{field:'same_pgm_cnt',title:'동일',width:"8%",halign:"center",align:'center'},
			]			
		],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
};

function getPerfCheckState2(){
	
	$("#tableList2").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'wrkjob_cd_nm',title:'업무',width:"8%",halign:"center",align:'center'},
			{field:'all_pgm_cnt',title:'전체 프로그램',width:"8%",halign:"center",align:'center'},
			{field:'fail_pgm_cnt',title:'부적합 프로그램',width:"10%",halign:"center",align:'center'},
			{field:'elaspsed_time',title:'수행시간',width:"8%",halign:"center",align:'center'},
			{field:'buffer_gets',title:'블럭수',width:"8%",halign:"center",align:'center'},
			{field:'row_processed',title:'처리건수',width:"8%",halign:"center",align:'center'},
			{field:'pga',title:'메모리사용량(MB)',width:"11%",halign:"center",align:'center'},
			{field:'fullscan',title:'대용량테이블 Full Scan',width:"12%",halign:"center",align:'center'},
			{field:'partition_range_all',title:'전체 파티션 액세스',width:"11%",halign:"center",align:'center'}
		]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
};


function Btn_OnClick(val){
	if(!formValidationCheck(val)){
		return false;
	}
	if(val == '0'){
		fnSearch0();
	}else if(val == '1'){
		fnSearch1();
	}else{
		fn_Search2();
	}
}

function fnSearch0(){
	$('#submit_form0 #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("CM 점검 현황"," ");
	
	
	ajaxCall("/PerformanceCheckIndex/PerfCheckState0",
			$("#submit_form0"),
			"POST",
			callback_PerfChkIndcListStateAction0);
}

function fnSearch1(){
	$('#submit_form1 #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로그램 점검 현황"," ");
	
	
	ajaxCall("/PerformanceCheckIndex/PerfCheckState1",
			$("#submit_form1"),
			"POST",
			callback_PerfChkIndcListStateAction1);
}

function fn_Search2(){
	$('#submit_form2 #tableList2').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("지표별 점검 현황"," ");
	
	
	ajaxCall("/PerformanceCheckIndex/PerfCheckState2",
			$("#submit_form2"),
			"POST",
			callback_PerfChkIndcListStateAction2);
}

//callback 함수
var callback_PerfChkIndcListStateAction0 = function(result) {
	json_string_callback_common(result,'#tableList0',true);
};
//callback 함수
var callback_PerfChkIndcListStateAction1 = function(result) {
	json_string_callback_common(result,'#tableList1',true);
};
//callback 함수
var callback_PerfChkIndcListStateAction2 = function(result) {
	json_string_callback_common(result,'#tableList2',true);
};


function formValidationCheck(val){
	
	if(val == '0'){
		if($("#submit_form0 #search_wrkjob_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','업무를 선택해주세요.');
			return false;
		}
	}else if(val == "1"){
		if($("#submit_form1 #search_wrkjob_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','업무를 선택해주세요.');
			return false;
		}
	}else{
		if($("#submit_form2 #search_wrkjob_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','업무를 선택해주세요.');
			return false;
		}
	}
	
	return true;
}
/*페이징처리끝*/



function setDetailView(selRow){

}

function Btn_ResetField(){

}


function Excel_Download(val){
	if(val == '0'){
		if(!formValidationCheck(val)){
			return false;
		}
		
		$("#submit_form0").attr("action","/PerformanceCheckIndex/PerfCheckState0/ExcelDown");
		$("#submit_form0").submit();
	}else if(val == '1'){
		if(!formValidationCheck(val)){
			return false;
		}
		
		$("#submit_form1").attr("action","/PerformanceCheckIndex/PerfCheckState1/ExcelDown");
		$("#submit_form1").submit();
	}else{
		if(!formValidationCheck(val)){
			return false;
		}

		$("#submit_form2").attr("action","/PerformanceCheckIndex/PerfCheckState2/ExcelDown");
		$("#submit_form2").submit();
	}
}

