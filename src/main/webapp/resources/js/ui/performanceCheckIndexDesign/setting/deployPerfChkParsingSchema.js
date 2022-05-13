var check_first = 0;
var set_check = 0;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#search_wrkjob_cd').combotree({
//		url:"/Common/getWrkJobCd",
		url:"/Common/getWrkJobCd?isAll=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$('#wrkjob_cd').combotree({
//		url:"/Common/getWrkJobCd",
		url:"/Common/getWrkJobCd?deploy_check_target_yn=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#wrkjob_cd').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	//점검단계
	$('#perf_check_step_id').combobox({
		url:"/PerformanceCheckIndex/DeployPerfChkStep/DeployPerfChkStepId",
		method:'get',
		valueField:'perf_check_step_id',
		textField:'perf_check_step_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#del_yn').combobox("textbox").attr("placeholder","선택");
			$('#perf_check_step_id').combobox("textbox").attr("placeholder","선택");
		}
	});
	
//	// Database 조회			
//	$('#dbid').combobox({
//	    url:"/Common/getDatabase",
//	    method:"get",
//		valueField:'dbid',
//	    textField:'db_name',
//		onLoadError: function(){
//			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
//			return false;
//		},
//	});	
//	
	// Database 조회			
	$('#dbid').combobox({
		url:"/Common/getDatabase",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#dbid').combobox("textbox").attr("placeholder","선택");
			$('#parsing_schema_name').combobox("textbox").attr("placeholder","선택");
		},
		onSelect:function(rec){
			
			$("#parsing_schema_name").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				panelHeight: 200,
				onChange:function(newval,oldval){
					if(check_first == 0){
						$("#old_parsing_schema_name").val(newval);
						check_first += 1;
					}
				},
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#parsing_schema_name').combobox("textbox").attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			
			$('#selectParsingSchemaName').combobox("setValue",$("#parsing_schema_name").val());
			
		}
	});	
	
	
	//삭제여부
//	$('#del_yn').combobox({
//		onSelect:function(rec){
//
//			console.log("1::"+rec.del_yn);
//			if(set_check == 0){
//				("실행");
//				$("#old_del_yn").val(rec.del_yn);
//				set_check += 1;
//			}
//			
//		},
//	});
	
	Btn_OnClick();
	
//	var t = $('#searchValue');
//	t.textbox('textbox').bind('keyup', function(e){
//	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
//		   Btn_OnClick();
//	   }
//	});	
	
	$("#step_ordering").textbox({disabled:true});
	$("#perf_check_step_id").combobox({readonly:false});
	$("#wrkjob_cd").combotree({readonly:false});
	
});

//changeList
function getDeployPerfChkParsingSchema(){
	
	$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'wrkjob_cd',hidden:true},
				{field:'perf_check_step_id',hidden:true},
				{field:'program_div_cd',hidden:true},
				{field:'wrkjob_cd_nm', title:'업무',width:"10%",halign:"center",align:'center'},
				{field:'perf_check_step_nm',title:'점검 단계',width:"22%",halign:"center",align:'left'},
				{field:'step_ordering',title:'단계순서',width:"10%",halign:"center",align:'center'},
				{field:'del_yn',title:'삭제여부',width:"10%",halign:"center",align:'center'},
				{field:'dbid',hidden:true},
				{field:'db_name',title:'DB',width:"10%",halign:"center",align:'center'},
//				{field:'perf_check_program_div_cd_nm',title:'프로그램',width:"10%",halign:"center",align:'center'},
				{field:'parsing_schema_name',title:'스키마',width:"10%",halign:"center",align:'center'}
			]],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};



function Btn_OnClick(){
	
	
//	if($("#search_wrkjob_cd").combobox('getValue') == ""){
//		parent.$.messager.alert('','업무를 선택해 주세요.');
//		return false;
//	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 점검 단계 관리"," ");
	
		ajaxCall("/PerformanceCheckIndex/DeployPerfChkParsingSchema",
				$("#submit_form"),
				"POST",
				callback_DeployPerfChkParsingSchemaAction);
}

//callback 함수
var callback_DeployPerfChkParsingSchemaAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getDeployPerfChkParsingSchema();
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SaveSetting(){
	if($("#wrkjob_cd").combotree('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
	if($("#perf_check_step_id").combobox('getValue') == ""){
		parent.$.messager.alert('','점검단계를 선택해 주세요.');
		return false;
	}
	if($("#del_yn").combobox('getValue') == ""){
		parent.$.messager.alert('','삭제여부를 선택해 주세요.');
		return false;
	}
	if($("#dbid").combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#parsing_schema_name").combobox('getValue') == ""){
		parent.$.messager.alert('','스키마를 선택해 주세요.');
		return false;
	}
	
	saveDeployPerfChkParsingSchema("N");
}

function saveDeployPerfChkParsingSchema(val){  
	//acceptUpdate == 'Y' 배포성능 점검중인 건수가 존재하더라도 업데이트
	
	ajaxCall("/PerformanceCheckIndex/DeployPerfChkParsingSchema/Save?acceptUpdate="+val,
			$("#detail_form"),
			"POST",
			callback_SaveSettingAction);
}
//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
	
		if(result.txtValue == 'Y'){  //배포성능 점검중인 건수가 존재해서 업데이트를 체크할지 여부물어봐야함.
			parent.$.messager.confirm('', '배포성능 점검중인 건수가 존재합니다. '+
					'스키마를 변경할 경우 배포성능 점검이 정상적으로 종료되지 않을 수 있습니다. ' +
					'스키마를 변경하시겠습니까?', function(check) {
				if(check){
					saveDeployPerfChkParsingSchema("Y");	
				}
			});
		}else{  //N 일경우는 배포성능 점검중인 건수가 존재하지않아서 물어보지않고 업데이트 성공
			parent.$.messager.alert('','저장이 완료 되었습니다.','info',function(){
				setTimeout(function() {
					Btn_OnClick();
					Btn_ResetField();
				},1000);	
			});
		}
		
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	set_check = 0;
	check_first = 0;
	
	$("#crud_flag").val("U");
	
	$("#step_ordering").textbox({disabled:false});
	$("#perf_check_step_id").combobox({readonly:true});
	$("#wrkjob_cd").combotree({readonly:true});


	$("#perf_check_step_id").combobox("setValue", selRow.perf_check_step_id);
	$("#wrkjob_cd").combotree("setValue", selRow.wrkjob_cd);
	$("#step_ordering").textbox("setValue", selRow.step_ordering);
	$("#del_yn").combobox("setValue", selRow.del_yn);
	$("#old_del_yn").val(selRow.del_yn);
	$("#dbid").combobox("setValue",selRow.dbid);
	$("#parsing_schema_name").combobox("setValue",selRow.parsing_schema_name);
	$("#old_parsing_schema_name").val(selRow.parsing_schema_name);
	$("#program_div_cd").combobox("setValue",selRow.program_div_cd);
	
}

function Btn_ResetField(){
	
	$("#crud_flag").val("C");

	$("#perf_check_step_id").combobox("setValue", "");
	$("#program_div_cd").combobox("setValue", "");
	$("#wrkjob_cd").combotree("setValue", "");
	$("#step_ordering").textbox("setValue", "");
	$("#del_yn").combobox("setValue", "");
	$("#old_del_yn").val("");
	$("#dbid").combobox("setValue", "");
	$("#parsing_schema_name").combobox("setValue", "");
	$("#old_parsing_schema_name").val("");
	
	$("#step_ordering").textbox({disabled:true});
	$("#perf_check_step_id").combobox({readonly:false});
	$("#wrkjob_cd").combotree({readonly:false});
	
	$("#tableList").datagrid("unselectAll");
}

function Excel_Download(){
	$("#submit_form").attr("action","/PerformanceCheckIndex/DeployPerfChkParsingSchema/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}


