$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// popup location setting
	$('#saveSQLPerformance').window({
		title : "SQL점검팩",
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	createList();
	
	$(".datagrid-header-check").css("height","35px");
	$(".datagrid-header-check").html("<label style='margin-left:9px;'>선</label><br><br><label style='margin-left:9px;'>택</label>");
	
	// 프로젝트 조회
	$('#submit_form #project_id_cd').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			// popup 점검팩 필드값 초기화
			$("#popup_form #project_id").val(data.project_id);
			Btn_ResetField();
		},
		onLoadSuccess: function() {
			$("#submit_form #project_id_cd").combobox("textbox").attr("placeholder",'선택');
			$("#popup_form #perf_check_type_cd").val("1");
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'chk',halign:'center',align:'center',checkbox:true,rowspan:2},
			{field:'perf_check_name',title:'SQL점검팩명',halign:'center',width:'12%',align:'left',rowspan:2},
			{field:'perf_check_desc',title:'SQL점검팩 설명',halign:'center',width:'12%',align:'left',rowspan:2},
			{field:'original_db_name',title:'ASIS DB<br>(원천DB)',width:'4%',halign:'center',align:'left',rowspan:2},
			{field:'perf_check_target_db_name',title:'TOBE DB<br>(목표DB)',width:'4%',halign:'center',align:'left',rowspan:2},
			{field:'check_range_period',title:'수집기간',width:'9%',halign:'center',align:'center',rowspan:2},
			{field:'topn_cnt',title:'TOP N',halign:'center',align:'right',rowspan:2},
			{field:'literal_except_yn',title:'Literal<br>제외',width:'3%',halign:'center',align:'center',rowspan:2},
			{field:'owner_list',title:'Owner',width:'12%',halign:'center',align:'left',rowspan:2},
			{field:'module_list',title:'Module',width:'12%',halign:'center',align:'left',rowspan:2},
//			{field:'table_name_list',title:'Table Name',width:'12%',halign:'center',align:'left',rowspan:2},
			{field:'perf_compare_meth_cd',title:'실행방법',width:'12%',halign:'center',align:'left',rowspan:2},
			{field:'parallel_degree',title:'병렬실행',width:'4%',halign:'center',align:'right',rowspan:2},
			{field:'dml_exec_yn',title:'DML 실행',width:'4%',halign:'center',align:'center',rowspan:2},
			{field:'multiple_exec_cnt',title:'Multiple 실행',width:'4%',halign:'center',align:'right',rowspan:2},
			{field:'multiple_bind_exec_cnt',title:'Multiple Bind 실행',width:'4%',halign:'center',align:'right',rowspan:2},
			{field:'sql_time_limt_nm',title:'SQL Time<br>Limit(분)',width:'4%',halign:'center',align:'right',rowspan:2},
			{field:'max_fetch_cnt',title:'최대 Fetch<br>건수',width:'4%',halign:'center',align:'right',rowspan:2},
			{field:'perf_check_exec_end_dt',title:'최종수행일시',halign:"center",align:'right',rowspan:2},
			{field:'perf_check_exec_time',title:'수행시간',halign:"center",align:'right',rowspan:2},
			{field:'perf_check_force_close_yn',title:'강제완료<br>처리',width:'3%',halign:'center',align:'center',rowspan:2},
			{field:'user_nm',title:'수행자명',halign:'center',align:'left',rowspan:2}
			],[
			]
		],
		onSelect:function( index, row ) {
			$("#sql_auto_perf_check_id").val( row.sql_auto_perf_check_id );
			$("#perf_check_name").val( row.perf_check_name );
		},
		onSuccess:function() {
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function Btn_ResetField() {
	$("#popup_form #sqlPerformanceP_cd").combobox("setValue","");
	$("#popup_form #sql_auto_perf_check_id").val("");
	$("#popup_form #perf_check_name").textbox("setValue","");
	$("#popup_form #perf_check_desc").textbox("setValue","");
}

function Btn_SqlAutoPerfSearch( num ) {
	if ( !checkCondition() ) {
		return;
	}
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$('#tableList').datagrid("loadData", []);
	
	if ( num == 1) {
		$("#submit_form #project_id").val( $("#submit_form #project_id_cd").combobox("getValue") );
		$("#popup_form #project_id").val( $("#submit_form #project_id_cd").combobox("getValue") );
	}
	//grid조회
	ajaxCallTableList();
}

/* 팝업 호출 */
function showSaveSQLPerformance_popup() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	//parent.frameName = $("#menu_id").val();
	
	if ( $("#submit_form #project_id_cd").combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	Btn_ResetField();
	
	loadSqlPerformancePopupCombo( $("#submit_form #project_id_cd").combobox("getValue") );
	
	let rows = $('#tableList').datagrid('getChecked');
	if ( rows.length > 0 ) {
		
		let sqlAutoId = rows[0].sql_auto_perf_check_id;
		if ( sqlAutoId != "" && sqlAutoId != null ) {
			
			$.ajax({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerformanceInfo",
				data:{
					project_id : $("#submit_form #project_id_cd").combobox("getValue") ,
					sql_auto_perf_check_id : sqlAutoId
				},
				type:"POST",
				dataType:"json",
				success: function(result) {
					if ( result.length != 0 ) {
						$("#popup_form #sqlPerformanceP_cd").combobox('setValue',sqlAutoId);
						$("#popup_form #perf_check_name").textbox("setValue",result[0].perf_check_name);
						$("#popup_form #perf_check_desc").textbox("setValue",result[0].perf_check_desc);
					} else {
						$("#popup_form #sqlPerformanceP_cd").combobox('setValue','');
						$("#popup_form #perf_check_name").textbox("setValue",'');
						$("#popup_form #perf_check_desc").textbox("setValue",'');
					}
				}
			});
		}
	}
	$('#saveSQLPerformance').window({
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	$('#saveSQLPerformance').window("open");
}

function loadSqlPerformancePopupCombo( projectId ) {
	
	// SQLPerformancePacCombo(Popup) // SQL점검팩 팝업콤보
	$('#popup_form #sqlPerformanceP_cd').combobox({
		url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerfPacName?project_id="+projectId
		+"&database_kinds_cd="+$("#database_kinds_cd").val(),
		method:"post",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onChange: function(newValue) {
			$("#popup_form #sql_auto_perf_check_id").val(newValue);
			
			// SQLPerformancePac info(popup)
			$.ajax({
				url:"/AutoPerformanceCompareBetweenDatabase/getSqlPerformanceInfo",
				data:{
					project_id : projectId,
					sql_auto_perf_check_id : newValue,
					database_kinds_cd : $("#database_kinds_cd").val()
				},
				type:"POST",
				dataType:"json",
				success: function(result) {
					if ( result.length != 0 ) {
						$("#popup_form #perf_check_name").textbox("setValue",result[0].perf_check_name);
						$("#popup_form #perf_check_desc").textbox("setValue",result[0].perf_check_desc);
					}
				}
			});
			
		},
		onLoadSuccess: function() {
			$("#popup_form #sqlPerformanceP_cd").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
}

// 등록 및 수정.
function Btn_SaveSqlPerfPac() {
	if ( $("#submit_form #project_id_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#popup_form #perf_check_name" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩명을 입력해 주세요.','warning');
		return false;
	}
	
	if ( $("#popup_form #perf_check_desc").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩 설명을 입력해 주세요.','warning');
		return false;
	}
	
	if ( getByteSize( $("#popup_form #perf_check_name" ).textbox("getValue") ) > 100 || getByteSize( $("#popup_form #perf_check_desc" ).textbox("getValue") ) > 100 ) {
		parent.$.messager.alert('경고','입력한 값이 100byte 초과합니다.<br> 다시 입력해 주세요.','warning');
		return false;
	}
	
	//insert / update 
	if ( $("#popup_form #project_id").val() == "" || $("#popup_form #sqlPerformanceP_cd").combobox("getValue") == "" ) {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/insertSqlPerformanceInfo",
			$("#popup_form"),
			"POST",
			callback_SaveSqlPerfPac);
	} else {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/updateSqlPerformanceInfo",
			$("#popup_form"),
			"POST",
			callback_SaveSqlPerfPac);
	}
}

//문자열 byte
function getByteSize(s) {
	const str = s.toString();
	let byteSize = 0;
	let char = '';
	
	for (let strArr = 0; !isNaN(str.charCodeAt(strArr)); strArr++) {
		char = str.charCodeAt(strArr);
		
		// 12비트 이상으로 표현 가능한 유니코드
		if (char >> 11) {
			byteSize += 3;
		// 8비트 ~ 11비트로 표현 가능한 유니코드
		} else if (char >> 7) {
			byteSize += 2;
		// 7비트 이하로 표현 가능한 유니코드
		} else {
			byteSize += 1;
		}
	}
	
	return byteSize;
}

var callback_SaveSqlPerfPac = function(result) {
	/* modal progress close */
	if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('',result.message,'info',function() {
			setTimeout(function() {
				
				$('#saveSQLPerformance').window("close");
				Btn_SqlAutoPerfSearch();
				
			},500);
		});
		
	} else {
		parent.$.messager.alert('정보',result.message,'info');
	}
}

function getMaxSqlCheckId( projectId ) {
	
	$.ajax({
		url:"/AutoPerformanceCompareBetweenDatabase/getMaxSqlCheckId?project_id="+projectId,
		type:"POST",
		dataType:"json",
		success:function(result) {
			$("#submit_form #sqlPerformanceP").combobox("setValue", result.sql_auto_perf_check_id );
		}
	});
}

function checkCondition() {
	if ( $("#submit_form #project_id_cd" ).textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	return true;
}

//페이지처리
function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallTableList();
}

function ajaxCallTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("SQL점검팩 조회"," ");
	
	$("#sql_auto_perf_check_id").val('');
	$("#perf_check_name").val('');
	
	/* SQL점검팩 리스트 */
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadSqlPerformancePacList",
			$("#submit_form"),
			"POST",
			callback_LoadSqlPerformancePacListAction);
}

var callback_LoadSqlPerformancePacListAction = function( result ) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn( dataLength );
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

// SQL점검팩 삭제
function deleteTable() {
	if ( !checkCondition() ) {
		return;
	}
	
	var data = $('#tableList').datagrid('getChecked');
	
	if ( data.length > 0 ) {
		
//		alert(data[0].sql_auto_perf_check_id+", 점검팩명 : "+data[0].perf_check_name+", sql_apcid :"+$("#sql_auto_perf_check_id").val() );
		/* 튜닝선정된 점검팩 조회  */
		
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/getTuningTargetCount",
				$("#submit_form"),
				"POST",
				callback_getTuningTargetCountAction);
	} else {
		parent.$.messager.alert('경고','삭제할 점검팩을 선택해 주세요.','warning');
	}
	
}

var callback_getTuningTargetCountAction = function( result ) {
	if ( result.result ) {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/countExecuteTms",
				$("#submit_form"),
				"POST",
				callback_CountExecuteTmsAction);
	} else {
		parent.$.messager.alert('정보','튜닝대상으로 선정되었던 SQL이 존재합니다.<br>튜닝대상 선정 작업이 수행되었을 경우 SQL점검팩은 삭제할 수 없습니다.','info');
	}
}

var callback_CountExecuteTmsAction = function( result ) {
	if ( result.txtValue == "true" ) {
		var param = "확인";
		var msg = '선택된 SQL점검팩의 성능비교 정보가 모두 삭제됩니다.<br>삭제하시겠습니까?';
		
		parent.$.messager.confirm( param, msg ,function(r) {
			if (r) {
				
				// SQL점검팩 삭제
				ajaxCall("/AutoPerformanceCompareBetweenDatabase/deleteSqlAutoPerformanceChk",
						$("#submit_form"),
						"POST",
						callback_deleteSqlAutoPerformanceChkAction);
			}
		});
	} else if ( result.txtValue == "false"  ){
		parent.$.messager.alert('정보','해당 SQL점검팩에 대한 성능비교 작업을 수행 중입니다.<br>성능비교 탭에서 진행상황을 검색할 수 있습니다.','info');
	}
	
}
var callback_deleteSqlAutoPerformanceChkAction = function( result ) {
	if ( result.result ) {
		var data = $('#tableList').datagrid('getChecked');
		
		parent.$.messager.alert('정보','점검팩 '+$("#perf_check_name").val()+'을(를) 삭제하였습니다.','info');
		
		parent.ReloadSqlPerformancePac( data[0].project_id, data[0].sql_auto_perf_check_id );
	} else {
		parent.$.messager.alert('정보','점검팩 '+$("#perf_check_name").val()+'을(를) 삭제하지 못했습니다.','info');
	}
	
	Btn_SqlAutoPerfSearch();
}
