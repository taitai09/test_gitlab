$(document).ready(function() {
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#wrkjob_cd").val(row.wrkjob_cd);
			$("#tr_cd").val(row.tr_cd);
			
			goApplicationDbioCheck();
		},			
		columns:[[
		    {field:'deploy_perf_check_no',title:'점검번호',halign:"center",align:'center',sortable:"true",styler:cellStyler},
		    {field:'wrkjob_cd',hidden:'true'},
		    {field:'wrkjob_cd_nm',title:'업무명',halign:"center",align:'center',sortable:"true",styler:cellStyler},
		    {field:'tr_cd',title:'애플리케이션명',halign:"center",align:"left",sortable:"true",styler:cellStyler},
			{field:'dbio_cnt',title:'DBIO수',halign:"center",align:'right',styler:cellStyler,sortable:"true",formatter:getNumberFormat},
			{field:'last_exec_dt',title:'최종 수행일시',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'perf_fitness_yn',title:'성능적합여부',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'prohibit_hint_use_yn',title:'금지힌트사용여부',halign:"center",align:'center',sortable:"true",styler:cellStyler}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	var chkValue = false;
	
	if($("#prohibit_hint_use_yn").val() == "Y") chkValue = true;
	
	$('#chkProhibitUseYn').switchbutton({
		checked: chkValue,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#prohibit_hint_use_yn").val("Y"); 
			else $("#prohibit_hint_use_yn").val("N");
		}
	});

	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallApplicationDetailCheck();	
	}

});

function cellStyler(value,row,index){
	var perf_fitness_yn = row.perf_fitness_yn;
	if(perf_fitness_yn == "부적합"){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
	}
}

function ajaxCallApplicationDetailCheck(){
	$('#tableList').datagrid("loadData", []);
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("애플리케이션 성능점검 - 상세내역"," "); 
	
	ajaxCall("/ApplicationDetailCheckAction",
			$("#submit_form"),
			"POST",
			callback_ApplicationDetailCheckActionAddTable);		
	
}

//callback 함수
var callback_ApplicationDetailCheckActionAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
}

function Btn_OnClick(){
	goApplicationDetailCheck();
}

function goApplicationDetailCheck(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ApplicationDetailCheck");
	$("#submit_form").submit();
}

function goApplicationDbioCheck(){
	$("#submit_form #call_from_parent").val("Y");
	$("#submit_form").attr("action","/ApplicationDBIOCheck");
	$("#submit_form").submit();
}

function goApplicationCheck(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form").attr("action","/ApplicationCheck");
	$("#submit_form").submit();	
}