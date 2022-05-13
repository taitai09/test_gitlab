$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		columns:[[
			{field:'job_exec_no',hidden:"true"},
			{field:'job_scheduler_type_cd',hidden:"true"},
			{field:'job_scheduler_detail_type_cd',hidden:"true"},
			{field:'job_scheduler_detail_type_nm',title:'스케쥴러 상세 유형',width:"15%",halign:"center",align:'left',sortable:"true"},
			{field:'job_exec_dt',title:'작업수행일시',width:"11%",halign:"center",align:'center'},
			{field:'job_target_cnt',title:'작업대상건수',width:"7%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'job_err_code',title:'작업오류코드',width:"8%",halign:"center",align:'center'},
			{field:'job_err_sbst',title:'작업오류내용',width:"36%",halign:"center",align:'left'},
			{field:'hndop_job_exec_yn',title:'수동작업수행여부',width:"9%",halign:"center",align:'center'},
			{field:'hndop_worker_id',hidden:"true"},
			{field:'hndop_worker_nm',title:'수동작업자',width:"7%",halign:"center",align:'center'},
			{field:'hndop_worker_nm',title:'재작업',width:"7%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	var checkJobErr = false;
	if($("#job_err_yn").val() == "Y") checkJobErr = true;
	
	$('#chkJobErrYn').switchbutton({
		checked: checkJobErr,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#job_err_yn").val("Y"); 
			}else{
				$("#job_err_yn").val("N");
			}
			
			Btn_OnClick();
		}
	})
	
	var checkHndopJobExecYn = false;
	if($("#hndop_job_exec_yn").val() == "Y") checkHndopJobExecYn = true;	
	
	$('#chkHndopJobExecYn').switchbutton({
		checked: checkHndopJobExecYn,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#hndop_job_exec_yn").val("Y"); 
			}else{
				$("#hndop_job_exec_yn").val("N");
			}
			
			Btn_OnClick();
		}
	})	

	$('#schedulerTypeDetailCd').combobox('setValue',$("#job_scheduler_detail_type_cd").val());
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("스케쥴러 수행 내역 - 상세"," "); 
	
	ajaxCall("/PerformSchedulerDetailAction",
		$("#submit_form"),
		"POST",
		callback_PerformSchedulerDetailAddTable);		
});

//callback 함수
var callback_PerformSchedulerDetailAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_OnClick(){
	$('#tableList').datagrid('loadData',[]);
	$('#tableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#tableList').datagrid('loading'); 

	$("#submit_form").attr("action","/PerformSchedulerDetail");
	$("#submit_form").submit();			
}

function Btn_GoList(){
	$("#job_scheduler_type_cd").val($("#list_job_scheduler_type_cd").val());
	
//	$("#submit_form #menu_nm").val("스케줄러 수행 내역");
//	alert($("#submit_form #menu_nm").val());
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/PerformScheduler");
	$("#submit_form").submit();	
}