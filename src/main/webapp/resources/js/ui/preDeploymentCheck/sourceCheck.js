$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#bfac_chk_no").val(row.bfac_chk_no);
			getDetailView();
		},			
		columns:[[
			{field:'bfac_chk_no',title:'사전성능점검번호',halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB명',halign:"center",align:'center',sortable:"true"},
			{field:'bfac_chk_request_dt',title:'요청일시',width:'120px',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_mgr_nm',title:'업무담당자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_mgr_dtpt_nm',title:'업무담당부서',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_dt',title:'튜닝일자',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_id',title:'담당튜너',halign:"center",align:'center',sortable:"true"},
			{field:'bfac_chkr_id',title:'사전성능점검자',halign:"center",align:'center',sortable:"true"},
			{field:'bfac_chk_dt',title:'사전성능점검일시',halign:"center",align:'center',sortable:"true"}
		]],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$('#selectCombo').combobox('setValue',$("#dbid").val());
	
	//ajaxCallSourceCheck();
});

function ajaxCallSourceCheck(){
	$('#tableList').datagrid("loadData", []);
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("소스 점검"," "); 
	
	ajaxCall("/SourceCheckAction",
			$("#submit_form"),
			"POST",
			callback_SourceCheckAddTable);		
}

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 기준일자를 선택해 주세요.');
		return false;
	}
	
	if($('#strEndDt').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 기준일자를 선택해 주세요.');
		return false;
	}	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
//	$("#submit_form").attr("action","/SourceCheck");
//	$("#submit_form").submit();
	ajaxCallSourceCheck();
}

//callback 함수
var callback_SourceCheckAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};

function getDetailView(){
	$("#submit_form").attr("action","/SourceCheck/View");
	$("#submit_form").submit();	
}