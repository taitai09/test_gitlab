var defaultDate = '2001-01-01';
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#selectCombo").combobox("textbox").attr("placeholder",'선택');
		}
	});	
	// 프로그램 유형 조회			
	$('#program_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1005&isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
	    onSelect:function(rec){
	    },
	    onLoadSuccess:function(){
	    	var programTypeCd = $("#temp_program_type_cd").val();
			$(this).combobox('setValue', programTypeCd);
	    }	    
	});
	//2019-01-03 추가
	// 진행상태 조회
	$('#tuning_status_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1004&isAll=Y&ref_vl_1=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
	    onChange:function(newValue, oldValue){
	    },
	    onLoadSuccess:function(){
	    	var tuningStatusCd = $("#temp_tuning_status_cd").val();
			$(this).combobox('setValue', tuningStatusCd);
		}
	});
	
	//검색조건 			
	$('#searchKey').combobox({
		onChange: function(value){
			$("#searchValue").textbox("setValue","");
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
//					onSelect: function(rec){
//						tuning_prgrs_step_seq = rec.tuning_prgrs_step_seq;
//					},
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
	
	
	
	$('#searchValue').textbox('textbox').bind('keydown', function(e){
		//var s = String.fromCharCode(e.keyCode);
		if(e.keyCode == 13){
			Btn_OnClick();
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#tuning_no").val(row.tuning_no);
			//$("#dbid").val(row.dbid);
			$("#dbid").val($('#selectCombo').combobox('getValue'));
			$("#tuning_complete_dt").val(row.tuning_complete_dt);
			
			getDetailView();
			$("#tuning_no").val("");
		},			
		columns:[[
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB',halign:"center",align:'center',sortable:"true"},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'tuning_request_dt',title:'튜닝요청일',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_mgr_nm',title:'업무담당자',halign:"center",align:'center',sortable:"true"},
			{field:'wrkjob_mgr_wrkjob_nm',title:'담당업무',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd_nm',title:'요청유형',halign:"center",align:'center',sortable:"true"},
			{field:'choice_div_cd_nm',title:'요청유형상태',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_dt',title:'튜닝완료일',halign:"center",align:'center',sortable:"true"},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_status_nm',title:'튜닝진행상태',halign:"center",align:'center',sortable:"true"},
			{field:'all_tuning_end_yn',title:'일괄튜닝종료여부',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_complete_why_nm',title:'개선유형',halign:"center",align:'center',sortable:"true"},
			{field:'program_type_nm',title:'프로그램유형',halign:"center",align:'center',sortable:"true"},
			{field:'project_nm',title:'프로젝트',halign:"center",align:'center',sortable:"true"},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계',halign:"center",align:'center',sortable:"true"},
			{field:'project_id',hidden:true},
			{field:'tuning_prgrs_step_seq',hidden:true}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$('#selectCombo').combobox('setValue',$("#dbid").val());
	$("#start_tuning_complete_dt").textbox("setValue",$("#temp_start_tuning_complete_dt").val());
	$("#end_tuning_complete_dt").textbox("setValue",$("#temp_end_tuning_complete_dt").val());
	$("input[name='tuning_status_cd']").val($("#temp_tuning_status_cd").val());

	var call_from_parent = $("#call_from_parent").val();
	var call_from_child = $("#call_from_child").val();
	if(call_from_parent == "Y"||call_from_child == "Y"){
		$("#searchKey").combobox("setValue",$("#temp_searchKey").val());
		$("#searchValue").textbox("setValue",$("#temp_searchValue").val());
		$("#program_type_cd").combobox("setValue",$("#temp_program_type_cd").val());
		$("#tuning_status_cd").combobox("setValue",$("#temp_tuning_status_cd").val());
		
		$("#start_tuning_complete_dt").textbox("setValue", defaultDate);
		$("#end_tuning_complete_dt").textbox("setValue", $("#nowDate").val());
		
		Btn_OnClick();
	}	
	
});

function ajaxCallTuningHistory(){
	$('#tableList').datagrid("loadData", []);
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("튜닝이력조회"," "); 
	
	ajaxCall("/TuningHistoryAction", $("#submit_form"), "POST", callback_TuningHistoryAddTable);		
	
}

function Btn_OnClick(){
	$("#currentPage").val("1");

	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	if($('#start_tuning_complete_dt').datebox('getValue') == ""){
		parent.$.messager.alert('','튜닝완료일의 검색시작일을 선택해 주세요.');
		return false;
	}
	if($('#end_tuning_complete_dt').datebox('getValue') == ""){
		parent.$.messager.alert('','튜닝완료일의 검색종료일을 선택해 주세요.');
		return false;
	}
	
	var start_tuning_complete_dt = $('#start_tuning_complete_dt').textbox('getValue');
	var end_tuning_complete_dt = $('#end_tuning_complete_dt').textbox('getValue');
	if(start_tuning_complete_dt != "" && end_tuning_complete_dt != ""){
		var i_start_tuning_complete_dt = parseInt(start_tuning_complete_dt.replace(/-/g,''));
		var i_end_tuning_complete_dt = parseInt(end_tuning_complete_dt.replace(/-/g,''));
		if(i_start_tuning_complete_dt > i_end_tuning_complete_dt){
			parent.$.messager.alert('','튜닝완료일의 검색종료일을 검색시작일 이후로 선택해 주세요.','info',function(){
				setTimeout(function() {
					$('#end_tuning_complete_dt').textbox('textbox').focus();
				},500);
			});
			return false;
		}
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	//$("#submit_form").attr("action","/TuningHistory");
	//$("#submit_form").submit();
	ajaxCallTuningHistory();
}

function fnSearch(){
	ajaxCallTuningHistory();
}

//callback 함수
var callback_TuningHistoryAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);

	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
};

function formValidationCheck(){
	return true;
}

function getDetailView(){
//	$("#submit_form").attr("action","/TuningHistory/View");
//	$("#submit_form").submit();	
	
	var menuParam = $("#submit_form").serialize();
	var menuId = $("#menu_id").val()+1;
	/* 탭 생성 */
	parent.openLink("Y", menuId, "튜닝 이력 조회 상세", "/TuningHistory/View", menuParam);
}

/**
 * 인덱스 자동설계현황 엑셀 다운로드
 * @returns
 */
function Excel_Download(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	if($('#start_tuning_complete_dt').datebox('getValue') == ""){
		parent.$.messager.alert('','튜닝완료일의 검색시작일을 선택해 주세요.');
		return false;
	}
	if($('#end_tuning_complete_dt').datebox('getValue') == ""){
		parent.$.messager.alert('','튜닝완료일의 검색종료일을 선택해 주세요.');
		return false;
	}
	
	var start_tuning_complete_dt = $('#start_tuning_complete_dt').textbox('getValue');
	var end_tuning_complete_dt = $('#end_tuning_complete_dt').textbox('getValue');
	if(start_tuning_complete_dt != "" && end_tuning_complete_dt != ""){
		var i_start_tuning_complete_dt = parseInt(start_tuning_complete_dt.replace(/-/g,''));
		var i_end_tuning_complete_dt = parseInt(end_tuning_complete_dt.replace(/-/g,''));
		if(i_start_tuning_complete_dt > i_end_tuning_complete_dt){
			parent.$.messager.alert('','튜닝완료일의 검색종료일을 검색시작일 이후로 선택해 주세요.','info',function(){
				setTimeout(function() {
					$('#end_tuning_complete_dt').textbox('textbox').focus();
				},500);
			});
			return false;
		}
	}


	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/TuningHistory/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}