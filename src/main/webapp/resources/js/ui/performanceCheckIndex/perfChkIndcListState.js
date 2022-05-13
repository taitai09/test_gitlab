tab_index = "";
$(document).ready(function() {
	$("body").css("visibility", "visible");
		  
	$('#submit_form #search_wrkjob_cd').combotree({
//		url:"/Common/getWrkJobCd",
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
		method:'get',
		valueField:'id',
		textField:'text',
	});
	$('#submit_form2 #search_wrkjob_cd').combotree({
//		url:"/Common/getWrkJobCd",
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
		method:'get',
		valueField:'id',
		textField:'text',
	});

	
	getPerfChkIndcListState();
	getPerfChkIndcListState2();
	
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


function getPerfChkIndcListState(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {

				if(tab_index=='2'){
					checkValidRequest(row); // [예외요청]개발자와 요청자 아이디가 같은지 확인한다.
				}
				setDetailView(row);
//				getDeployPerfChkDetailResult();  //체크박스 테이블
			},		
			columns:[[
				{field:'wrkjob_cd_nm',title:'업무',width:"6%",halign:"center",align:'center'},
				{field:'total_excpt_cnt',title:'전체',width:"4%",halign:"center",align:'center'},
				{field:'sql_auto_pass_cnt',title:'SQL-점검제외',width:"8%",halign:"center",align:'center'},
				{field:'sql_indc_unit_cnt',title:'SQL-지표단위',width:"6%",halign:"center",align:'center'},
				{field:'program_auto_pass_cnt',title:'프로그램-점검제외',width:"12%",halign:"center",align:'center'},
				{field:'tr_auto_pass_cnt',title:'거래-점검제외',width:"6%",halign:"center",align:'center'}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};
function getPerfChkIndcListState2(){
	
	$("#tableList2").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'wrkjob_cd_nm',title:'업무',width:"6%",halign:"center",align:'center'},
			{field:'total_cnt',title:'합계',width:"4%",halign:"center",align:'center'},
			{field:'exception_request_why_cd01',title:'테스트불가',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd02',title:'배치 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd03',title:'센터컷 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd04',title:'임시처리',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd05',title:'오류처리 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd06',title:'에러처리 전용',width:"8%",halign:"center",align:'center'},
//			{field:'exception_request_why_cd07',title:'삼성페이 가동거래',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd08',title:'데몬 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd09',title:'내부거래 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd10',title:'MQ 전용',width:"8%",halign:"center",align:'center'},
			{field:'exception_request_why_cd_etc',title:'기타',width:"8%",halign:"center",align:'center'}
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
	if(val == '1'){
		fnSearch();
	}else{
		fn_Search2();
	}
}

function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 예외 요청"," ");
	
	
	ajaxCall("/PerformanceCheckIndex/PerfChkIndcListState",
			$("#submit_form"),
			"POST",
			callback_PerfChkIndcListStateAction);
}

function fn_Search2(){
	$('#submit_form2 #tableList2').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 예외 처리 사유별 현황"," ");
	
	
	ajaxCall("/PerformanceCheckIndex/PerfChkIndcListState2",
			$("#submit_form2"),
			"POST",
			callback_PerfChkIndcListStateAction2);
}

//callback 함수
var callback_PerfChkIndcListStateAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
};
//callback 함수
var callback_PerfChkIndcListStateAction2 = function(result) {
	json_string_callback_common(result,'#tableList2',true);
};


function formValidationCheck(val){
	
	if(val == '1'){
		if($("#submit_form #search_wrkjob_cd").combobox('getValue') == ""){
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
	
	if(val == '1'){
		if(!formValidationCheck(val)){
			return false;
		}
		
		$("#submit_form").attr("action","/PerformanceCheckIndex/PerfChkIndcListState/ExcelDown");
		$("#submit_form").submit();
	}else{
		if(!formValidationCheck(val)){
			return false;
		}

		$("#submit_form2").attr("action","/PerformanceCheckIndex/PerfChkIndcListState2/ExcelDown");
		$("#submit_form2").submit();
	}
}

