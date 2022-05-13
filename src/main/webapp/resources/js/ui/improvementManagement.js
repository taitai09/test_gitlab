headerBtnIsClicked = "N";
var opts_a = "";
var selected = false;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	headerBtnIsClicked = $("#submit_form #headerBtnIsClicked").val();
	
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
	
	//요청유형 조회 choice_div_cd_nm
	$('#selectChoiceDiv').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1003&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue, oldValue){
		},
		onLoadSuccess: function(data){
			var choice_div_cd = $("#choice_div_cd").val();
			
			$(this).combobox('setValue',choice_div_cd);
		}
	});
	
	// 진행상태 조회
	$('#selectTuningStatus').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1004&isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onChange:function(newValue, oldValue){
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
				
				$('#submit_form #sql_auto_perf_check_id').combobox({
					url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?isAll=Y&project_id="+project_id,
					method:"post",
					valueField:'sql_auto_perf_check_id',
					textField:'perf_check_name',
					panelHeight: 300,
					onSelect: function(item) {
					},
					onLoadError: function() {
						parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
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
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		onUncheckAll:function(rows){
		},
		onCheckAll:function(rows){
			var opts = $(this).datagrid('options');
			for(var i = 0; i < rows.length; i++){
				var row = rows[i];
				
				if(row.tuning_status_cd != "2" && row.tuning_status_cd != "3" && row.tuning_status_cd != "5" && row.tuning_status_cd != "6"){
					$(this).context.checked = false;
					
					var tr = opts.finder.getTr(this,i);
					tr.find('input[type=checkbox]').attr('checked',false);
					$(this).datagrid('unselectRow',i);
				}
			}
		},
		onClickRow : function(index,row) {
			$("#submit_form #search_choice_div_cd").val( $("#submit_form #choice_div_cd").val() );
			$("#submit_form #dbid").val(row.dbid);
			$("#submit_form #temp_dbio").val(row.dbio);
			$("#submit_form #tuning_no").val(row.tuning_no);
			$("#submit_form #choice_div_cd").val(row.choice_div_cd);
			$("#submit_form #tuningStatusCd").val(row.tuning_status_cd);
			$("#submit_form #tuning_complete_dt").val(row.tuning_complete_dt);
			if(row.temporary_save_yn == "Y"){
				$("#submit_form #tuning_complete_dt").val(row.temporary_save_dt);
			}
			var opts = $(this).datagrid('options');
			opts_a = $(this).datagrid('options');
			
			var tr = opts.finder.getTr(this,index);
			tr.find('input[type=checkbox]').prop('checked',false);
			
			getDetailView();
		},
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true", rowspan:2},
			{field:'tuning_no',title:'튜닝번호',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'before_tuning_no',title:'이전튜닝번호',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'choice_div_cd',title:'튜닝대상선정코드',hidden:"true",rowspan:2},
			{field:'choice_div_cd_nm',title:'요청유형',halign:"center",sortable:"true",align:'left', rowspan:2},
			{field:'dbid',title:'DB',hidden:"true",rowspan:2},
			{field:'temporary_save_yn',hidden:"true",rowspan:2},
			{field:'temporary_save_dt',hidden:"true",rowspan:2},
			{field:'db_name',title:'DB',halign:"center",align:'center', rowspan:2},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'tr_cd',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'perfr_id',hidden:"true",rowspan:2},
			{field:'perfr_nm',title:'튜닝담당자',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'wrkjob_mgr_nm',title:'업무담당자',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'tuning_status_cd',title:'진행상태코드',hidden:"true",rowspan:2},
			{field:'tuning_status_nm',title:'진행상태',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_why_nm',title:'완료/종료 사유',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_request_dt',title:'튜닝요청일시',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_requester_nm',title:'튜닝요청자',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'tuning_complete_dt',title:'튜닝완료일시',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'tuning_apply_dt',title:'적용일시',halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'project_nm',title:'프로젝트',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'tuning_prgrs_step_nm',title:'튜닝진행단계',halign:"center",align:'left',sortable:"true", rowspan:2},
			{field:'perf_check_name',title:'SQL점검팩',halign:"center",align:'left',sortable:"true", rowspan:2}
		],[]],
		onLoadSuccess: function(data){
			var opts = $(this).datagrid('options');
			for(var i = 0; i < data.rows.length ; i++){
				var row = data.rows[i];
				if(row.tuning_status_cd != "2" && row.tuning_status_cd != "3" && row.tuning_status_cd != "5" && row.tuning_status_cd != "6"){
					var tr = opts.finder.getTr(this,i);
					tr.find('input[type=checkbox]').attr('disabled','disabled');
				}
			}
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	if($("#dbid").val() != 'null'){
		$('#submit_form #selectCombo').combobox('setValue',$("#dbid").val());
	}
	$('#submit_form #selectTuningStatus').combobox('setValue',$("#tuning_status_cd").val());
	$('#submit_form #selectUserRoll').combobox('setValue',$("#searchKey").val());
	
	$('#submit_form #tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능 개선 관리"," "); 
	
	Btn_OnClick();
	
	$("#search_tuning_no").numberbox('textbox').keyup(function(e) {
		if ( !floatCheck( $("#search_tuning_no").numberbox("getText") ) ) {
			parent.$.messager.alert('경고','튜닝번호는 소수점을 입력할 수 없습니다.','warning');
			$("#search_tuning_no").numberbox('clear').focus();
			return false;
		}
	});
});

function floatCheck( obj ) {
	var fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;
	
	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

function Btn_OnClick(isPassed){
	$("#currentPage").val("1");
	
	if(isPassed == 'Y'){
		headerBtnIsClicked = isPassed;
	}else{
		headerBtnIsClicked = isPassed;
	}
	$("#submit_form #first_tuning_status_cd").val($('#submit_form #selectTuningStatus').combobox('getValue'));
	$("#submit_form #choice_div_cd").val($('#submit_form #selectChoiceDiv').combobox('getValue'));
	fnSearch();
}

function fnSearch(){
	parent.openMessageProgress("성능 개선 관리 조회"," ");
	
	if(!formValidationCheck()){
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#submit_form #tuning_status_cd").val($('#submit_form #selectTuningStatus').combobox('getValue'));
	$("#submit_form #searchKey").val($('#submit_form #selectUserRoll').combobox('getValue'));
	
	ajaxCall_ImprovementManagementAction();
	ajaxCall_ImprovementManagementAction2();
}
function fnSearch_isnotpaging(){
	
	$("#currentPage").val("1");
	
	if(!formValidationCheck()){
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#submit_form #tuning_status_cd").val($('#submit_form #selectTuningStatus').combobox('getValue'));
	$("#submit_form #searchKey").val($('#submit_form #selectUserRoll').combobox('getValue'));
	
	ajaxCall_ImprovementManagementAction();
	ajaxCall_ImprovementManagementAction2();
}

function ajaxCall_ImprovementManagementAction(){
	ajaxCall("/ImprovementManagementAction", $("#submit_form"), "POST", callback_ImprovementManagementAddTable);
}
function ajaxCall_ImprovementManagementAction2(){
	/* header 부분에 튜닝요청, 튜닝대기, 튜닝중, 적용대기로 클릭해서 들어오면 $("#submit_form #headerBtnIsClicked").val('Y'); 가 된다. */
	ajaxCall("/ImprovementManagementAction2?headerBtnIsClicked="+headerBtnIsClicked, $("#submit_form"), "POST", callback_ImprovementManagementAddTable2);
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
	if(!compareAnBDate($('#strStartDt').textbox('getValue'), $('#strEndDt').textbox('getValue'))) {
		var msg = "기준 일자를 확인해 주세요.<br>시작일자[" + $('#strStartDt').textbox('getValue') + "] 종료일자[" + $('#strEndDt').textbox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	if(($('#submit_form #selectUserRoll').combobox('getValue') == "" && $('#submit_form #searchValue').textbox('getValue') != "") ||
		($('#submit_form #selectUserRoll').combobox('getValue') != "" && $('#submit_form #searchValue').textbox('getValue') == "")){
		parent.$.messager.alert('','담당자 정보를 정확히 입력해 주세요.');
		return false;
	}
	return true;
}

function Excel_Download(){	
	$("#submit_form #first_tuning_status_cd").val($('#submit_form #selectTuningStatus').combobox('getValue'));

	if(!formValidationCheck()){
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		return;
	}
	
	$("#submit_form #dbid").val($('#submit_form #selectCombo').combobox('getValue'));
	$("#submit_form #tuning_status_cd").val($('#submit_form #selectTuningStatus').combobox('getValue'));
	$("#submit_form #searchKey").val($('#submit_form #selectUserRoll').combobox('getValue'));
	
	$("#submit_form").attr("action","/ImprovementManagement/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

//callback 함수
var callback_ImprovementManagementAddTable = function(result) {
	if(result.result){
		var post = result.object;
		
		$("#process_all").html(post.process_all);
		$("#process_1").html(post.process_1);
		$("#process_3").html(post.process_3);
		$("#process_5").html(post.process_5);
		$("#process_6").html(post.process_6);
		$("#process_7").html(post.process_7);
		$("#process_8").html(post.process_8);
		$("#process_4").html(post.process_4);
		
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

//callback 함수
var callback_ImprovementManagementAddTable2 = function(result) {
	
	var data;
	var dataLength=0;
	if(result.result){
		var post = result.object;
		
		data = JSON.parse(result.txtValue);
		dataLength = data.dataCount4NextBtn;
		$('#tableList').datagrid("loadData", data);
		
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	fnEnableDisablePagingBtn(dataLength);
};

function showTuningAssignAllPopup(){
	var tuningNoArry = "";
	var totalCnt = 0;
	var newAssignCnt = 0; // 튜닝담당자 지정 count
	var reAssignCnt = 0; // 튜닝담당자 재지정 count
	var dbCnt = 0;
	var tempDB = "";
	var strDbid = "";
	var errCnt = 0;
	
	rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		totalCnt = rows.length;
		$("#assignAll_form #tuningNoArry").val("");
		
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";
			strDbid = rows[0].dbid;
			
			/* 1. 튜닝담당자 지정
			 *  - 튜닝담당자가 없고, 요청상태가 "요청"이면서 진행상태가 "요청,선정" 인 경우
			 * 2. 튜닝담당자 재지정 
			 *  - 튜닝담당자가 있고, 진행상태가 "튜닝중","접수" 인 경우 
			*/
			
			/* SQL (요청) 상세 : CHOICE_DIV_CD -> 3 */
			/* 배포전성능점검 : CHOICE_DIV_CD -> B */
			/* 배포전자동성능검증 : CHOICE_DIV_CD -> K */
			
			if(rows[i].tuning_status_cd == '5'){
				errCnt++;
			}
			
			if((rows[i].perfr_id == "" || rows[i].perfr_id == null) 
					&& ( (rows[i].choice_div_cd == "3" || rows[i].choice_div_cd == "B" || rows[i].choice_div_cd == "K") 
					  && (rows[i].tuning_status_cd == "1" || rows[i].tuning_status_cd == "2") )){
				
				newAssignCnt++;
				
			}else if((rows[i].perfr_id != "" || rows[i].perfr_id != null) && (rows[i].tuning_status_cd == "3" || rows[i].tuning_status_cd == "5")){
				reAssignCnt++;
			}
			
			// DB가 다른 경우
			if(rows[i].dbid != tempDB && tempDB != ""){
				dbCnt++;
			}else{
				tempDB = rows[i].dbid;
			}
		}
		
		if(dbCnt > 0){
			parent.$.messager.alert('','튜닝담당자 지정하려는 대상에 다른 DB가 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
			return false;
			
		}else{
			if((newAssignCnt < totalCnt) && !(reAssignCnt == totalCnt)){
				parent.$.messager.alert('','요청상태가 \"요청\"이면서 진행상태가 \"요청/선정\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
				return false;
			}
			
			if((reAssignCnt < totalCnt) && !(newAssignCnt == totalCnt)){
				parent.$.messager.alert('','진행상태가 \"튜닝대기\", \"튜닝중\"이<br/>아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝담당자 지정을 해주세요.');
				return false;
			}
			
			if(errCnt > 0){
				parent.$.messager.alert('','진행상태가 튜닝중인 건에 대해서는<br/>튜닝담당자를 지정할 수 없습니다.');
				return false;
			}
		}
		
		if(newAssignCnt == totalCnt){
			$("#assignAll_form #tuningStatusArry").val("N");
			
		}else{
			$("#assignAll_form #tuningStatusArry").val("R");
		}
		
		// 튜닝담당자 조회
		$('#assignAll_form #selectTuner').combobox({
			url:"/Common/getTuner?dbid="+strDbid,
			method:"get",
			valueField:'tuner_id',
			textField:'tuner_nm'
		});
		
		$("#assignAll_form #tuningNoArry").val(strRight(tuningNoArry,1));
		$('#tuningAssignAllPop').window("open");
		
	}else{
		parent.$.messager.alert('','튜닝담당자 지정할 대상을 선택해 주세요.');
	}
}

function getDetailView_bak(){
	var userAuth = $("#submit_form #user_auth").val();
	
	if(userAuth == "ROLE_TUNER" && $("#submit_form #tuningStatusCd").val() == "3"){
		
		parent.$.messager.confirm('', '해당 요청건이 튜닝대기 상태입니다.<br/>튜닝중으로 상태를 변경하시겠습니까?', function(r){
			if (r){
				var tuningNoArry = "";
				
				rows = $('#tableList').datagrid('getSelections');
				
				for(var i=0; i<rows.length; i++){
					tuningNoArry += rows[i].tuning_no + "|";
				}
				if(strRight(tuningNoArry,1) == ""){
					parent.$.messager.alert('',"튜닝중으로 변경할 항목을 다시 선택하여 주세요.",'error');
				}
				$("#submit_form #tuningNoArry").val(strRight(tuningNoArry,1));
					
				ajaxCall("/ImprovementManagement/SaveTuning",
						$("#submit_form"),
						"POST",
						callback_SaveTuningAction2);
			}else{
				getDetailViewAction();
			}
		});
		
	}else{
		getDetailViewAction();
	}
}

function getDetailView(){
	var userAuth = $("#submit_form #user_auth").val();
	
	if(userAuth == "ROLE_TUNER" && $("#submit_form #tuningStatusCd").val() == "3"){
		
		$.messager.confirm('', '해당 요청건이 튜닝대기 상태입니다.<br/>튜닝중으로 상태를 변경하시겠습니까?', function(r){
			if (r){
				var tuning_no = $("#submit_form #tuning_no").val();
				$("#submit_form #tuningNoArry").val(tuning_no);
				
				ajaxCall("/ImprovementManagement/SaveTuning",
						$("#submit_form"),
						"POST",
						callback_SaveTuningAction2);
			}else{
				getDetailViewAction();
			}
		});
		
	}else{
		getDetailViewAction();
	}
}
//튜너가 접수한 튜닝건을 튜닝중 처리한 후(해당 요청건이 접수 상태입니다.튜닝중으로 상태를 변경하시겠습니까?)
//상세화면으로 이동=>상세화면을 새 탭으로 생성
function getDetailViewAction(){
	parent.openMessageProgress("성능 개선 관리 상세"," ");
	//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
	parent.searchWorkStatusCount();
	
	var menuId = $("#menu_id").val();
	var newMenuId = menuId+$("#submit_form #tuning_no").val();
	var menuNm = "성능 개선 관리 상세(튜닝번호:"+$("#submit_form #tuning_no").val()+")";
	var menuUrl = "/ImprovementManagementView";
	$("#submit_form #menu_id").remove();
	var menuParam = $("#submit_form ").serialize();

	var menuIdHtml = '<input type="hidden" id="menu_id" name="menu_id" value="'+menuId+'"/>';

	$("#submit_form").append(menuIdHtml);
	
	parent.openLink("Y", newMenuId, menuNm, menuUrl, menuParam);
	
	$("#tableList").datagrid("clearSelections");
	$("#submit_form #choice_div_cd").val( $("#submit_form #search_choice_div_cd").val() );
}
/**
 * 튜닝중 처리 버튼
 * @returns
 */
function Btn_SaveTuning(){
	var tuningNoArry = "";
	var errCnt = 0;
	
	rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		for(var i = 0; i < rows.length; i++){
			tuningNoArry += rows[i].tuning_no + "|";
			
			// 접수, 적용반려가 아닌 건수 체크 (튜닝대기 = 3, 적용대기 = 6, 적용반려 = 7)
			if(rows[i].tuning_status_cd != "3" && rows[i].tuning_status_cd != "6" && rows[i].tuning_status_cd != "7"){
				errCnt++;
			}
		}
		
		if(errCnt > 0){
			parent.$.messager.alert('','튜닝대기[적용반려]가 아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝중 처리 해주세요.');
		}else{
			$("#submit_form #tuningNoArry").val(strRight(tuningNoArry,1));
			
			ajaxCall("/ImprovementManagement/SaveTuning",
					$("#submit_form"),
					"POST",
					callback_SaveTuningAction);
		}
		
	}else{
		parent.$.messager.alert('','튜닝중 처리할 데이터를 선택해 주세요.');
	}
}

//callback 함수
var callback_SaveTuningAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝중으로 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				Btn_OnClick();
			},1000);
		});
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

//callback 함수 :/ImprovementManagement/SaveTuning이 끝난 후 호출됨
var callback_SaveTuningAction2 = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝중으로 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				getDetailViewAction();
			},1000);
		});
		
	}else{
		if(result.message == undefined){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.','error');
		}else{
			parent.$.messager.alert('',result.message,'error');
		}
	}
};

/* 리스트에서 튜닝취소 처리 */
function Btn_TuningCancelAll(){
	var tuningNoArry = "";
	var errCnt = 0;
	
	rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";
			
			// 튜닝중이 아닌 건수 체크
			if(rows[i].tuning_status_cd != "5"){
				errCnt++;
			}
		}
		
		if(errCnt > 0){
			parent.$.messager.alert('','튜닝중이 아닌 상태값이 존재합니다.<br/>다시 확인 후 튜닝취소 해주세요.');
			
		}else{
			$("#submit_form #tuningNoArry").val(strRight(tuningNoArry,1));
			
			ajaxCall("/ImprovementManagement/SaveTuningCancelAll",
					$("#submit_form"),
					"POST",
					callback_SaveTuningCancelAllAction);
		}
		
	}else{
		parent.$.messager.alert('','튜닝취소 처리할 데이터를 선택해 주세요.');
	}
}

//callback 함수
var callback_SaveTuningCancelAllAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','튜닝 취소가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				Btn_OnClick();
			},1000);
		});
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

function goStatusLink(strStatus){
	headerBtnIsClicked = "Y";
		$("#selectTuningStatus").combobox("setValue", strStatus);
		fnSearch_isnotpaging();
}

function Btn_GoList(){
	var menuId = $("#menu_id").val();
	document.location.href = parent.document.getElementById('if_'+$("#menu_id").val()).src;
}

/* 리스트에서 튜닝요청 취소 처리 */
function Btn_RequestCancelAll(){
	var tuningNoArry = "";
	var errCnt = 0;
	
	rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		for(var i = 0 ; i < rows.length ; i++){
			tuningNoArry += rows[i].tuning_no + "|";
			
			// 요청중이 아닌 건수 체크
			if(rows[i].tuning_status_cd != "2"){
				errCnt++;
			}
		}
		
		if(errCnt > 0){
			parent.$.messager.alert('','요청중이 아닌 상태값이 존재합니다.<br/>다시 확인 후 요청취소 해주세요.');
			
		}else{
			$("#submit_form #tuningNoArry").val(strRight(tuningNoArry,1));
			
			ajaxCall("/ImprovementManagement/CancelTuningRequestAction",
					$("#submit_form"),
					"POST",
					callback_RequestCancelAction);
		}
		
	}else{
		parent.$.messager.alert('','요청취소 처리할 데이터를 선택해 주세요.');
	}
}

//callback 함수
var callback_RequestCancelAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','요청취소가 정상적으로 처리되었습니다.','info',function(){
			setTimeout(function() {
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				Btn_OnClick();
			},1000);
		});
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
};

function selectOnSearch() {
	Btn_OnClick();
}