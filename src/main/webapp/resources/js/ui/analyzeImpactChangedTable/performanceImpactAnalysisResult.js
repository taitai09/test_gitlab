var beforeValue = '';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#tuningAssignPop').window({
		title : "튜닝 요청",
		top:getWindowTop(550),
		left:getWindowLeft(550)
	});
	
	initPerfCheckResult();
	
	createLeftList();
	createRightList();
	
	//소스DB 불러오기
	$("#original_dbid").combobox({
		url:"/AnalyzeImpactChangedTable/getOperationDB?db_operate_type_cd=&database_kinds_cd="+$('#database_kinds_cd').val(),
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight: 300,
		onChange: function(newValue, oldValue) {
			$('#sqlPerformanceP').combobox('clear');
			$("#sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
			
			if ( newValue == '' ) {
				return; 
			}
			
			var item = $.grep($(this).combobox('getData'), function(row) {
				if ( row.original_dbid == newValue ) {
					return true;
				} else {
					return false;
				}
			});
			
		},
		onLoadError: function() {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		},
		onLoadSuccess: function(items) {
			if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
			
			$("#original_dbid").combobox("textbox").attr("placeholder",'선택');
		}
	});
	
	// SQL점검팩 combobox 클릭시 데이터 불러오기
	$('#sqlPerformanceP').siblings('span').on('click', function(){
		$('#sqlPerformanceP').combobox('clear');
		
		beginDate = encodeURIComponent( $('#perf_check_exec_begin_dt').datebox('getText') );
		endDate = encodeURIComponent( $('#perf_check_exec_end_dt').datebox('getText') );
		
		if ( $("#original_dbid").textbox("getValue") == '' ) {
			parent.$.messager.alert('경고','소스DB를 선택해 주세요.','warning');
			return false;
			
		}else if ( checkConditionDate() == false) {
			return;
			
		}else {
			let original_dbid = encodeURIComponent( $("#original_dbid").textbox("getValue") );
			
			$('#sqlPerformanceP').combobox({
				url:"/AnalyzeImpactChangedTable/getSqlPerfPacName?perf_check_exec_begin_dt="
					+ beginDate + "&perf_check_exec_end_dt=" + endDate + "&original_dbid=" + original_dbid
					+ "&database_kinds_cd=" + $('#database_kinds_cd').val()
					+ "&condition=1", 
				method:"get",
				valueField:'sql_auto_perf_check_id',
				textField:'perf_check_name',
				panelHeight: 300,
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onLoadSuccess: function(item) {
					$('#sqlPerformanceP').combobox('textbox').attr('placeholder','선택');
				},
				onLoadError: function() {
					parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
					return false;
				},
				onSelect: function(data) {
					$("#data_yn").val(data.data_yn);
					$("#sql_auto_perf_check_id").val( data.sql_auto_perf_check_id );
				},
				onChange: function(){
					$("#table_owner").val('');
					$("#table_name").val('');
				}
			});
			
		}
	});
	
	// SELECT 문 > 전체 check
	$("#all_sql_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").checkbox('setValue','Y');
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				$("#all_sql_yn").checkbox('setValue','N');
				if ( $("#plan_change_yn").checkbox("options").checked != true && 
						$("#perf_down_yn").checkbox("options").checked != true && 
						$("#notPerf_yn").checkbox("options").checked != true && 
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > plan변경 check
	$("#plan_change_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#plan_change_yn").checkbox('setValue','Y');
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				$("#plan_change_yn").checkbox('setValue','N');
				if ( $("#perf_down_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 성능저하 check
	$("#perf_down_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#perf_down_yn").checkbox('setValue','Y');
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				$("#perf_down_yn").checkbox('setValue','N');
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 성능 부적합 check
	$("#notPerf_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#notPerf_yn").checkbox('setValue','Y');
				$("#all_sql_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			} else {
				$("#notPerf_yn").checkbox('setValue','N');
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#perf_down_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true &&
						$("#error_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
			}
		}
	});
	
	// SELECT 문 > 오류 check
	$("#error_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#error_yn").checkbox('setValue','Y');
				$("#all_sql_yn").checkbox("uncheck");
				$("#plan_change_yn").checkbox("uncheck");
				$("#perf_down_yn").checkbox("uncheck");
				$("#notPerf_yn").checkbox("uncheck");
				$("#error_yn").checkbox("check");
				
				$("#error_dml_yn").checkbox("check");
			} else {
				$("#error_yn").checkbox('setValue','N');
				if ( $("#plan_change_yn").checkbox("options").checked != true &&
						$("#perf_down_yn").checkbox("options").checked != true &&
						$("#notPerf_yn").checkbox("options").checked != true &&
						$("#all_sql_yn").checkbox("options").checked != true ) {
					$("#all_sql_yn").checkbox("check");
				}
				
				$("#error_dml_yn").checkbox("uncheck");
				$("#error_yn").checkbox("uncheck");
			}
		}
	});
	
	// dml문 check
	$("#dml_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#dml_yn").checkbox('setValue','Y');
				if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false && 
						$("#error_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked && 
						$("#partition_yn").checkbox("options").checked == false && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#fullScan_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked && 
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#partition_yn").checkbox("check");
				} else if ( $("#all_dml_yn").checkbox("options").checked == false && 
						$("#fullScan_yn").checkbox("options").checked == false && 
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked ) {
					$("#error_dml_yn").checkbox("check");
				} else if ( $("#error_yn").checkbox("options").checked ) {
					$("#error_dml_yn").checkbox("check");
				} else {
					$("#all_dml_yn").checkbox("uncheck");
					$("#fullScan_yn").checkbox("uncheck");
					$("#partition_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
					$("#dml_yn").checkbox("uncheck");
				}
			} else {
				$("#dml_yn").checkbox('setValue','N');
				$("#all_dml_yn").checkbox("uncheck");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#error_dml_yn").checkbox("uncheck");
				$("#dml_yn").checkbox("uncheck");
			}
		}
	});

	// DML 문 > 전체 check
	$("#all_dml_yn").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_dml_yn").checkbox('setValue','Y');
				$("#dml_yn").checkbox("check");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#error_dml_yn").checkbox("uncheck");
			} else {
				$("#all_dml_yn").checkbox('setValue','N');
				if ( $("#fullScan_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#dml_yn").checkbox("uncheck");
				}
			}
		}
	});

	// DML 문 > FULL SCAN check
	$("#fullScan_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#fullScan_yn").checkbox('setValue','Y');
				$("#dml_yn").checkbox("check");
				
				if ( $("#all_dml_yn").checkbox("options").checked ||
						$("#error_dml_yn").checkbox("options").checked ) {
					$("#all_dml_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
				}
			} else {
				$("#fullScan_yn").checkbox('setValue','N');
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#all_dml_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
			}
		}
	});

	// DML 문 > PARTITION ALL ACCESS check
	$("#partition_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#partition_yn").checkbox('setValue','Y');
				$("#dml_yn").checkbox("check");
				
				if ( $("#all_dml_yn").checkbox("options").checked ||
						$("#error_dml_yn").checkbox("options").checked ){
					$("#all_dml_yn").checkbox("uncheck");
					$("#error_dml_yn").checkbox("uncheck");
				}
			} else {
				$("#partition_yn").checkbox('setValue','N');
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#all_dml_yn").checkbox("options").checked == false &&
						$("#fullScan_yn").checkbox("options").checked == false &&
						$("#error_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
			}
		}
	});
	
	// DML 문 > 오류 check
	$("#error_dml_yn").checkbox({
		value:"N",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$("#error_dml_yn").checkbox('setValue','Y');
				$("#dml_yn").checkbox("check");
				$("#fullScan_yn").checkbox("uncheck");
				$("#partition_yn").checkbox("uncheck");
				$("#all_dml_yn").checkbox("uncheck");
				
				$("#error_yn").checkbox("check");
			} else {
				$("#error_dml_yn").checkbox('setValue','N');
				if ( $("#dml_yn").checkbox("options").checked &&
						$("#fullScan_yn").checkbox("options").checked == false &&
						$("#partition_yn").checkbox("options").checked == false &&
						$("#all_dml_yn").checkbox("options").checked == false ) {
					$("#all_dml_yn").checkbox("check");
				}
				
				if ( $("#dml_yn").checkbox("options").checked ) {
					$("#error_yn").checkbox("uncheck");
				}
			}
		}
	});
	
	// 이전튜닝대상 선정 SQL 제외 check (팝업)
	$('#chkExcept').switchbutton({
		value:"Y",
		checked:true,
		onText:"Yes",
		offText:"No",
		onChange:function( checked ) {
			if ( checked ) {
				$("#sqlExclude").val("Y");
			} else {
				$("#sqlExclude").val("");
			}
		}
	});
	
	$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	$("#asis_elapsed_time").numberbox("textbox").attr("placeholder","3");
	$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	$("#elapsed_time_regres").numberbox("textbox").attr("placeholder","10");
	
	$("#buffer_gets_1day").textbox("textbox").focus(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_1day").textbox("textbox").blur(function(){
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
	});
	
	$("#asis_elapsed_time").textbox("textbox").focus(function(){
		$("#asis_elapsed_time").numberbox("textbox").attr("placeholder","");
	});
	$("#asis_elapsed_time").textbox("textbox").blur(function(){
		$("#asis_elapsed_time").numberbox("textbox").attr("placeholder","3");
	});
	
	$("#buffer_gets_regres").textbox("textbox").focus(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","");
	});
	$("#buffer_gets_regres").textbox("textbox").blur(function(){
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
	});
	
	$("#elapsed_time_regres").textbox("textbox").focus(function(){
		$("#elapsed_time_regres").numberbox("textbox").attr("placeholder","");
	});
	$("#elapsed_time_regres").textbox("textbox").blur(function(){
		$("#elapsed_time_regres").numberbox("textbox").attr("placeholder","10");
	});
});

function createLeftList() {
	var cnt = 0;
	$("#leftTableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'rnum',title:'NO',width:'10%',halign:'center',align:'center',rownumbers:'true',sortable:"true"},
			{field:'table_owner',title:'TABLE_OWNER',width:'25%',halign:'center',align:'left',sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',width:'25%',halign:'center',align:'left',sortable:"true"},
			{field:'sql_cnt',title:'SQL수',width:'15%',halign:'center',align:'right',sortable:"true"},
			{field:'plan_change_yn_cnt',title:'플랜변경<br>건수',width:'15%',halign:'center',align:'right',sortable:"true"},
			{field:'perf_impact_type_cd_cnt',title:'성능저하<br>건수',width:'15%',halign:'center',align:'right',sortable:"true"}
		]],
		onSelect:function( index, row ) {
			let equalCheck = false;
			let currentValue = getCurrentValue();
			
			//이전에 검색했던 조회값과 현재 조회값이 같은지 확인
			for(let i = 0; i < currentValue.length; i++){
				equalCheck = beforeValue[i] === currentValue[i];
				if(equalCheck == false ){
					i = currentValue.length+1;
				}
			}
			
			//이전에 검색했던 조회값과 현재 조회값이 같을 경우에만 우측 테이블 조회
			if(equalCheck == true){
				$("#table_owner").val(row.table_owner);
				$("#table_name").val(row.table_name);
				
				$("#currentPage").val("1");
				$("#pagePerCount").val("20");
				
				loadPerformanceResultCount();
				ajaxCallRightTableList();
			}else {
				$('#leftTableList').datagrid('loadData',[]);
				$('#rightTableList').datagrid('loadData',[]);
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function createRightList() {
	var cnt = 0;
	$("#rightTableList").datagrid({
		view: myview,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'table_name',title:'TABLE_NAME',width:'10%',halign:'center',align:'left'},
			{field:'tuning_status_nm',title:'튜닝상태',width:'5%',halign:'center',align:'center'},
			{field:'perf_impact_type_nm',title:'성능임팩트<br>유형',width:'6%',halign:'center',align:'center'},
			{field:'buffer_increase_ratio',title:'버퍼<br> 임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'5%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'5%',halign:'center',align:'center',styler:cellStyler},
			{field:'plan_change_yn',title:'Plan<br>변경여부',width:'5%',halign:'center',align:'center'},
			{field:'sql_id',title:'SQL ID',width:'9%',halign:'center',align:'left'},
			
			{field:'asis_plan_hash_value',title:'ASIS<br>PLAN<br>HASH VALUE',width:'7%',halign:"center",align:'right'},
			{field:'tobe_plan_hash_value',title:'TOBE<br>PLAN<br>HASH VALUE',width:'7%',halign:"center",align:'right'},
			{field:'asis_executions',title:'ASIS<br>EXECUTIONS',width:'7%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_executions',title:'TOBE<br>EXECUTIONS',width:'7%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_rows_processed',title:'ASIS<br>ROWS<br>PROCESSED',width:'7%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_rows_processed',title:'TOBE<br>ROWS<br>PROCESSED',width:'7%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_elapsed_time',title:'ASIS<br>ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_elapsed_time',title:'TOBE<br>ELAPSED<br>TIME',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_buffer_gets',title:'ASIS<br>BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'tobe_buffer_gets',title:'TOBE<br>BUFFER<br>GETS',width:'5%',halign:"center",align:'right',formatter:getNumberFormatNullChk,sortable:"true"},
			{field:'asis_fullscan_yn',title:'ASIS<br>FULLSCAN<br>YN',width:'5%',halign:"center",align:'center'},
			{field:'tobe_fullscan_yn',title:'TOBE<br>FULLSCAN<br>YN',width:'5%',halign:"center",align:'center'},
			{field:'asis_partition_all_access_yn',title:'ASIS<br>PARTITION ALL<br>ACCESS YN',width:'5%',halign:"center",align:'center'},
			{field:'tobe_partition_all_access_yn',title:'TOBE<br>PARTITION ALL<br>ACCESS YN',width:'5%',halign:"center",align:'center'},
			
			{field:'sql_command_type_cd',title:'SQL 명령 유형',width:'7%',halign:"center",align:'center'},
			{field:'err_code',title:'에러코드',width:'6%',halign:"center",align:'left'},
			{field:'err_msg',title:'에러메시지',width: '20%',halign:"center",align:'left'},
			{field:'sql_text_web',title:'SQL TEXT',width: '19%',halign:"center",align:'left'},
			{field:'tuning_no',title:'튜닝번호',width:'4%',halign:"center",align:'right'},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'13.5%',halign:'center',align:'left'},
			
			/*HIDDEN DATA*/
			{field:'original_dbid',title:'소스DB',hidden:true},
			{field:'project_id',title:'프로젝트ID',hidden:true},
			{field:'sql_auto_perf_check_id',title:'SQL_AUTO_PERF_CHECK_ID',hidden:true},
			{field:'perf_check_sql_source_type_cd',title:'PERF_CHECK_SQL_SOURCE_TYPE_CD',hidden:true}	//1 : AWR  2 : 전체 SQL
		]],
		onSelect:function( index, row ) {
			$('#sql_id').val(row.sql_id);
			$('#dbid').val(row.original_dbid);
			$('#plan_hash_value').val(row.asis_plan_hash_value);
			$('#asis_plan_hash_value').val(row.tobe_plan_hash_value);
			$('#sql_command_type_cd').val(row.sql_command_type_cd);
			$('#sql_auto_perf_check_id').val(row.sql_auto_perf_check_id);
			$('[name=perf_check_sql_source_type_cd]').val(row.perf_check_sql_source_type_cd);

			openExplainPlan();
		},
		onUncheck:function(index, rows) {
			$('#isAll').val("");
			
		},
		onCheckAll:function( rows ) {
			$('#isAll').val("A");
		},
		onUncheckAll:function( rows ) { 
			$('#isAll').val("");
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function cellStyler( value,row,index ) {
	// 적합
	if ( row.perf_check_result_yn == '적합' ) {
		return 'background-color:#1A9F55;color:white;';
	} else if ( row.perf_check_result_yn == '부적합'){
		return 'background-color:#E41E2C;color:white;';
	} else { // 부적합
		return '';
	}
}
function openExplainPlan() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$("#tabs").tabs('update',{
		tab: $("#tabs").tabs('getTab',1),
		options:{
			title:'ASIS Plan'
		}
	});
	$("#tabs").tabs('update',{
		tab: $("#tabs").tabs('getTab',2),
		options:{
			title:'TOBE Plan'
		}
	});
	$(".tabs-last:not(.tabs-first)").hide();//('display','none');
	
	$("#bindValueList").datagrid({
		view: myview,
		fitColumns:true,
		columns:[[
			{field:'bind_var_nm',title:'BIND_VAR_NM',halign:"center",align:'center'},
			{field:'bind_var_value',title:'BIND_VAR_VALUE',halign:"center",align:'left'},
			{field:'bind_var_type',title:'BIND_VAR_TYPE',halign:"center",align:'right'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#operPlanCopy").linkbutton({
		text:"TOBE PLAN 복사"
	});
	
	$("#loadExplainPlanPop").window({
		top:0,
		left:50
	});
	
	$('#loadExplainPlanPop').window("open");
	
	$("#loadExplainPlan_form #bindValueList").datagrid("resize",{
		width: 400
	});
	
	loadExplainPlanNew();
	
	$("#loadExplainPlanPop").window('setTitle', "SQL Info( " + $('#sql_id').val() + " )");
	
	$('#tabs').tabs('select', 0);
}

function loadExplainPlanNew() {
	$("#textArea").val('');
	$("#asisTextPlan").val('');
	$("#operTextPlan").val('');
	
	/* 상단 텍스트 박스 */
	if($('#perf_check_sql_source_type_cd').val() == '1'){
		// AWR(1)일 경우
		/* sql text */
		ajaxCall("/SQLInformation/SQLText",
				$('#submit_form'),
				"POST",
				callback_SQLTextAction);
		
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlan",
				$('#submit_form'),
				"POST",
				callback_SQLPerformTextAsisPlanListAction );
	
	}else {
		// 전체SQL(2) 일경우
		/* sql text */
		ajaxCall("/SQLInformation/SQLTextAll",
				$('#submit_form'),
				"POST",
				callback_SQLTextAction);
		
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlanAll",
				$('#submit_form'),
				"POST",
				callback_SQLPerformTextAsisPlanListAction );
	}
	
	$('#bindValueList').datagrid('loadData',[]);
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	/* bind value */
	ajaxCall("/AnalyzeImpactChangedTable/loadExplainBindValueNew",
			$('#submit_form'),
			"POST",
			callback_BindValueListNewAction);
	
	let planHashValue = $("#submit_form #plan_hash_value").val();
	$("#submit_form #plan_hash_value").val( $("#submit_form #asis_plan_hash_value").val() );
	
	/* tobe plan */
	if ( $('#sql_command_type_cd').val() == "SELECT" ) {
		ajaxCall("/AnalyzeImpactChangedTable/loadAfterSelectTextPlanListAll",
				$('#submit_form'),
				"POST",
				callback_SQLPerformTextOperPlanListAction );
		
	} else {
		ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadAfterDMLTextPlanListAll",
				$('#submit_form'),
				"POST",
				callback_SQLPerformTextOperPlanListAction );
	}
	
	$("#submit_form #plan_hash_value").val( planHashValue );
	
}

// callback
var callback_BindValueListNewAction = function(result) {
	let data = null;
	try{
		data = JSON.parse(result);
	
		if ( data.result != undefined && !data.result ) {
			let opts = $("#bindValueList").datagrid('options');
			let vc = $("#bindValueList").datagrid('getPanel').children('div.datagrid-view');
			vc.children('div.datagrid-empty').remove();
			if ( !$("#bindValueList").datagrid('getRows').length ) {
				let emptyMsg = "검색된 데이터가 없습니다.";
				let d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
				d.css({
					top:50
				});
				$('#bindValueList').datagrid('loaded');
			}
		} else {
			$("#bindValueList").datagrid({rownumbers:true});
			$('#bindValueList').datagrid("loadData", data);
			$('#bindValueList').datagrid('loaded');
			$("#bindValueList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
			$("#bindValueIsNull").val("Y");  //bindValueList 에 데이터가 없을경우	
			
			if ( data.totalCount > 0 ) {
				$("#bindValueIsNull").val("N"); //bindValueList 에 데이터가 있을경우	
				$("#bindBtn").linkbutton({disabled:false});	
			}
		}
	
	} catch(data) {
		$('#bindValueList').datagrid('loaded');
	}
}

function Btn_SqlAutoPerfSearch(code) {
	if ( checkConditionSqlP() == false ) {
		return;
	}
	if (code != null) {
		$("#table_owner").val("");
		$("#table_name").val("");
	}
	//현재 검색한 조건값 변수에 저장
	getBeforeValue();
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	$("#sqlExclude").val("");
	$("#isAll").val("");
	$('#sqlIdArry').val("");
	
	$("#popup_sqlPerformanceP").combobox("readonly", false);
	
	$('#leftTableList').datagrid("loadData", []);
	$('#rightTableList').datagrid("loadData", []);
	
	//수행결과조회
	Btn_LoadPerfResultCount();
	//검색결과건수 조회
	loadPerformanceResultCount();
	//좌측 grid조회
	ajaxCallLeftTableList();
	//우측 grid조회
	ajaxCallRightTableList();
}

function ajaxCallLeftTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능비교 결과 조회"," ");
	
	let submitData = {
			sql_auto_perf_check_id: $("#sql_auto_perf_check_id").val(),
			database_kinds_cd: $('#database_kinds_cd').val()
	};
	
	// 테이블별 현황 조회
	ajaxCallWithSimpleData(
			"/AnalyzeImpactChangedTable/loadLeftTableList",
			submitData,
			"GET",
			callback_LoadLeftTableList);

}

var callback_LoadLeftTableList = function(result) {
	json_string_callback_common(result,'#leftTableList',true);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function ajaxCallRightTableList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능비교 결과 조회"," ");
	
	// 테이블별 현황 조회
	ajaxCall("/AnalyzeImpactChangedTable/loadRightTableList",
			$("#submit_form"),
			"GET",
			callback_LoadRightTableList);
}

var callback_LoadRightTableList = function(result) {
	json_string_callback_common(result,'#rightTableList',true);
	
	let dataLength = JSON.parse(result).dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

//수행결과 초기화
function initPerfCheckResult() {
	$(".perf_check_result_blue").val("전체: 0");
	$(".perf_check_result_green").val("수행완료: 0");
	$(".perf_check_result_orange").val("오류: 0");
	$("#perf_check_result_red").hide();
	$("#perf_check_result_violet").hide();
}

//수행 결과 조회
function Btn_LoadPerfResultCount() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("수행결과 조회"," ");
	
	let submitData = {
			sql_auto_perf_check_id: $("#sql_auto_perf_check_id").val(),
			database_kinds_cd: $('#database_kinds_cd').val()
	};
	
	ajaxCallWithSimpleData(
			"/AnalyzeImpactChangedTable/loadPerfResultCount",
			submitData,
			"POST",
			callback_LoadPerformanceCheckCountAction);
}

var callback_LoadPerformanceCheckCountAction = function(rows) {
	let data = JSON.parse(rows)[0];
	
	if ( typeof data == 'undefined') {
		/* modal progress close */
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
		
		// 04. 수행결과 초기화
		initPerfCheckResult();
		
		return;
	}
	
	$(".perf_check_result_blue").val(data.total_cnt);
	$(".perf_check_result_green").val(data.completed_cnt);
	$(".perf_check_result_orange").val(data.err_cnt);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

function Excel_Download() {
	if ( checkConditionSqlP() == false ) {
		return;
	}
	
	$('#select_sql').val("00");
	$('#submit_form #select_perf_impact').val("00");
	
	$("#submit_form").attr("action","/AnalyzeImpactChangedTable/excelDownload");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

// 검색결과 건수
function loadPerformanceResultCount() {
	if ( checkConditionSqlP() == false ) {
		return;
	}
	let sqlAutoPerfCheckId = $("#sql_auto_perf_check_id").val();
	
	ajaxCall("/AnalyzeImpactChangedTable/getPerformanceResultCount",
			$('#submit_form'),
			"POST",
			callback_loadPerformanceResultCountAction);
}

var callback_loadPerformanceResultCountAction = function( result ) {
	$(".performanceResultCount").val("검색결과 건수 : "+parseInt(result.txtValue).toLocaleString() );
}
function initPopupSet(){
	// popup 초기화
	$('#assign_form #chkAutoShare').switchbutton("uncheck");
	$('#assign_form #chkExcept').switchbutton("check");
	$('#assign_form #auto_share').val("N");
	$('#assign_form #selectTuner').combobox({readonly:false});
	$('#assign_form #selectTuner').combobox('setValue','');
	$("#assign_form #project_id").combobox('setValue','');
	$("#assign_form #sqlPerformanceP").combobox('setValue','');
	
};

// 튜닝대상선정 popup
function showTuningReqPopup(){
	if ( checkConditionSqlP() == false ) {
		return;
	}
	
	// 팝업 SQL점검팩 콤보
	$('#popup_sqlPerformanceP').combobox({
		url:"/AnalyzeImpactChangedTable/getSqlPerfPacName?"
			+ "database_kinds_cd=" + $('#database_kinds_cd').val(),
		method:"get",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onSelect: function(item) {
			$("#popup_sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
			$("#data_yn").val(item.data_yn);
		},
		onLoadSuccess: function() {
			$("#popup_sqlPerformanceP").combobox("setValue", $("#sqlPerformanceP").combobox("getValue") );
		},
		onLoadError: function() {
			parent.$.messager.alert('경고','DB 조회중 오류가 발생하였습니다.','warning');
			return false;
		}
	});
	
	let tuningNoArry = "";
	let rows = $('#rightTableList').datagrid('getChecked');
	
	initPopupSet();
	
	$("#popup_sqlPerformanceP").combobox( "setValue", $("#sqlPerformanceP").combobox("getValue") );
	$("#popup_sqlPerformanceP").combobox("readonly","true");
	
	if ( rows.length > 0 ) {
		$("#dbid").val( rows[0].original_dbid );
		
		$('#tuningAssignPop').window({
			top:getWindowTop(550),
			left:getWindowLeft(550)
		});
		
		$('#tuningAssignPop').window("open");
		$('#popup_dbid').val("");
		$("#popup_sql_auto_perf_check_id").val("");
		
		$('#tuningNoArry').val("");
		$('#perfr_id').val("");
		$('#strGubun').val("M");
		
		const CHOICE_DIV_CD = "J";
		const TUNING_STATUS_CD = "3";
		const PARSING_SCHEMA_NAME = "OPENSIMUL";
		
		$('#popup_dbid').val($("#dbid").val());
		$("#choice_div_cd").val( CHOICE_DIV_CD );
		$("#tuning_status_cd").val( TUNING_STATUS_CD );
		$("#parsing_schema_name").val( PARSING_SCHEMA_NAME );
		$("#choice_cnt").val(rows.length);

		let dbid = encodeURIComponent( $("#dbid").val() );
		// 튜닝 담당자 조회
		$('#selectTuner').combobox({
			url:"/Common/getTuner?dbid="+dbid,
			method:"get",
			valueField:'tuner_id',
			textField:'tuner_nm',
			onLoadSuccess:function() {
				$("#assign_form #selectTuner").combobox("textbox").attr("placeholder",'선택');
			}
		});		
	} else {
		parent.$.messager.alert('경고','튜닝 대상을 선택해 주세요.','warning');
	}
}

// tunerAssign_popup의 기능들은 tunerAssign_popup.js를 사용
// 튜닝요청 및 담당자 지정만 재정의.
function Btn_SaveTuningDesignation(){
	let popup_sql_auto_perf_check_id = $("#popup_sql_auto_perf_check_id").val();
	
	if ( popup_sql_auto_perf_check_id == "" ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	params = "sql_auto_perf_check_id="+popup_sql_auto_perf_check_id;
	
	if ( $('#selectTuner').combobox('getValue') == "" ) {
		if ( $('#auto_share').val() == "N" ) {
			parent.$.messager.alert('경고','튜닝담당자를 선택해 주세요.','warning');
			return false;
		}
	}
	
	$("#perfr_id").val( $('#selectTuner').combobox('getValue') );
	$('#sqlIdArry').val("");
	
	if ( $("#isAll").val() == "A" ) {	// 전체 check 시
		/* modal progress open */
		if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("자동 성능 점검 조회"," ");
		
		$('#select_sql').val("00");
		$('#select_perf_impact').val("00");
		
		if ( $("#chkExcept").switchbutton("options").checked ) {
			$("#sqlExclude").val("Y");
		} else {
			$("#sqlExclude").val("");
		}
		
		/* 전체시 List 재조회 */
		ajaxCall("/AnalyzeImpactChangedTable/loadPerformanceResultListAll",
				$("#submit_form"),
				"POST",
				callback_ReLoadPerfResultListAllAction)
	
	} else {	// 개별 check 시
		let data = $('#rightTableList').datagrid('getChecked');
		
		if ( $("#chkExcept").switchbutton("options").checked == true ) {
			let numbers = "";
			// tuning_no로 판단
			for ( let checkds = data.length-1; checkds > 0-1; checkds-- ) {
				if ( data[checkds].tuning_no != null ) {
					data.splice( checkds, 1 );
				}
			}
		}
		dataListCall( data );
	}
}

var callback_ReLoadPerfResultListAllAction = function( result ) {
	let data = JSON.parse( result );
	
	if ( data.rows.length > 0 ) {
		dataListCall( data.rows );
	}
}

//페이징처리
function formValidationCheck(){
	return true;
}
function fnSearch(){
	ajaxCallRightTableList();
}

// 개별 시
function dataListCall( data ) {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('튜닝 요청','튜닝 요청중입니다.');
	
	if ( data.length > 0 ) {
		let sqlIdArry = "";
		let moduleArry = "";
		let parsingSchemaNameArry = "";
		let asisPlanHashValueArry = "";
		let tobePlanHashValueArry = "";
		
		for ( let idx = 0 ; idx < data.length; idx++ ) {
			sqlIdArry += data[idx].sql_id + "|";
			moduleArry += data[idx].module + "|";
			parsingSchemaNameArry += data[idx].parsing_schema_name + "|";
			asisPlanHashValueArry += data[idx].asis_plan_hash_value + "|";
			tobePlanHashValueArry += data[idx].tobe_plan_hash_value + "|";
		}
		
		$("#sqlIdArry").val( strRight( sqlIdArry,1 ) );
		$("#module").val( strRight( moduleArry,1 ) );
		$("#parsing_schema_name").val( strRight( parsingSchemaNameArry,1 ) );
		$("#assign_form #asisPlanHashValueArry").val( strRight( asisPlanHashValueArry,1 ) );
		$("#assign_form #planHashValueArry").val( strRight( tobePlanHashValueArry,1 ) );
		
		ajaxCall("/AnalyzeImpactChangedTable/Popup/InsertTuningRequest",
				$("#assign_form"),
				"POST",
				callback_InsertTuningRequest);
		
	} else {
		Btn_OnClosePopup('tuningAssignPop');
		parent.$.messager.alert('','이전 튜닝대상 선정 SQL을 제외한<br> 0건이 선정되었습니다.','info');
		parent.closeMessageProgress();
		Btn_SqlAutoPerfSearch(null);
	}
}

//callback 함수
var callback_InsertTuningRequest = function(result) {
	if ( result.result ) {
		/* modal progress close */
		parent.closeMessageProgress();
		
		let cnt = 0;
		let msg = "";
		
		for ( var checkCnt = 0; checkCnt < result.object.length; checkCnt++ ) {
			cnt =  cnt + ( 1 * result.object[checkCnt].perfr_id_cnt);
		} 
		
		if ( result.message == "Y" ) {
			msg = "이전 튜닝대상선정 SQL을 제외한<br> "+ cnt + "건이 선정 되었습니다."
		} else {
			msg = "튜닝대상 "+ cnt +"건이 선정 되었습니다."
		}
		
		$.messager.alert('', msg ,'info',function(){
			setTimeout(function() {
				
				Btn_OnClosePopup('tuningAssignPop');
				Btn_SqlAutoPerfSearch(null);
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.parent.searchWorkStatusCount();
				
			},1000);
		});
	}
};
/*검색 클릭 시 값 저장*/
function getBeforeValue() {
	beforeValue = [
		$('#sqlPerformanceP').combobox('getValue')
		,$('[name=all_sql_yn]').val()			//select - 전체
		,$('[name=plan_change_yn]').val()		//select - plan변경
		,$('[name=perf_down_yn]').val()			//select - 성능저하
		,$('[name=notPerf_yn]').val()			//select - 성능부적합
		,$('[name=error_yn]').val()				//select - 오류
		,$('[name=buffer_gets_1day]').val()		//BUFFER GETS(ASIS 평균)
		,$('[name=asis_elapsed_time]').val()	//ELAPSED TIME(ASIS 평균)
		,$('[name=buffer_gets_regres]').val()	//BUFFER GETS 성능 저하
		,$('[name=elapsed_time_regres]').val()	//ELAPSED TIME 성능 저하
		,$('[name=dml_yn]').val()				//DML 문
		,$('[name=all_dml_yn]').val()			//dml - 전체
		,$('[name=fullScan_yn]').val()			//dml - full scan
		,$('[name=partition_yn]').val()			//dml - PARTITION ALL ACCESS
		,$('[name=error_dml_yn]').val()			//dml - 오류
	];
}
/*왼쪽 테이블 클릭할 당시의 조건 값들*/
function getCurrentValue() {
	return [
		$('#sqlPerformanceP').combobox('getValue')
		,$('[name=all_sql_yn]').val()			//select - 전체
		,$('[name=plan_change_yn]').val()		//select - plan변경
		,$('[name=perf_down_yn]').val()			//select - 성능저하
		,$('[name=notPerf_yn]').val()			//select - 성능부적합
		,$('[name=error_yn]').val()				//select - 오류
		,$('[name=buffer_gets_1day]').val()		//BUFFER GETS(ASIS 평균)
		,$('[name=asis_elapsed_time]').val()	//ELAPSED TIME(ASIS 평균)
		,$('[name=buffer_gets_regres]').val()	//BUFFER GETS 성능 저하
		,$('[name=elapsed_time_regres]').val()	//ELAPSED TIME 성능 저하
		,$('[name=dml_yn]').val()				//DML 문
		,$('[name=all_dml_yn]').val()			//dml - 전체
		,$('[name=fullScan_yn]').val()			//dml - full scan
		,$('[name=partition_yn]').val()			//dml - PARTITION ALL ACCESS
		,$('[name=error_dml_yn]').val()			//dml - 오류
	];
}
//점검팩 삭제 시 SQL점검팩 클리어
function sqlPerfPacClear( sqlPerfId ) {
	let pastSqlPerfId = $('#sqlPerformanceP').combobox('getValue');
	
	if( sqlPerfId == pastSqlPerfId ){
		$('#sqlPerformanceP').combobox('clear');
		$('#sqlPerformanceP').combobox('textbox').attr('placeholder','선택');
		
		$('#perf_check_exec_begin_dt').datebox('setValue', beginDate);
		$('#perf_check_exec_end_dt').datebox('setValue', endDate);
		
		$('#oneMonth').radiobutton('check');
		
		$("#all_sql_yn").checkbox("check");
		$("#dml_yn").checkbox("check");
		
		$("#buffer_gets_1day").numberbox("clear");
		$("#asis_elapsed_time").numberbox("clear");
		$("#buffer_gets_regres").numberbox("clear");
		$("#elapsed_time_regres").numberbox("clear");
		
		$("#buffer_gets_1day").numberbox("textbox").attr("placeholder","1000");
		$("#asis_elapsed_time").numberbox("textbox").attr("placeholder","3");
		$("#buffer_gets_regres").numberbox("textbox").attr("placeholder","10");
		$("#elapsed_time_regres").numberbox("textbox").attr("placeholder","10");
		
		$('#leftTableList').datagrid('loadData',[]);
		$('#rightTableList').datagrid('loadData',[]);
	}else {
		return;
	}
}