var timer;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("10");
	
	$('#ownerEditBox').window({
		title : "OWNER <a style='color:lightblue'>in</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeOwnerBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(315),
		left:getWindowLeft(1435),
		closable:false
	});
	$('#moduleEditBox').window({
		title : "MODULE <a style='color:lightblue'>like</a> <a href='javascript:;' style='float:right; font-size:15px;' class='w20 easyui-linkbutton' onclick='closeModuleBox();'><i class='btnIcon fas fa-close fa-lg fa-fw'></i></a>",
		top:getWindowTop(315),
		left:getWindowLeft(545),
		closable:false
	});
	
	$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
	$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
	
	$('#saveSQLPerformance').window("close");
	$('#ownerEditBox').window("close");
	$('#moduleEditBox').window("close");
	
	// 프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/DBChangePerformanceImpactAnalysisForTibero/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			$('#tableList').datagrid("loadData", []);
			$("#submit_form #data_yn").val("N");
			
			// 점검팩 호출
			$("#submit_form #sql_auto_perf_check_id").val("");
			loadSqlPerformanceCombo( data.project_id );
			
			// 원천 DB
			$("#submit_form #original_dbid").combobox({
				url:"/DBChangePerformanceImpactAnalysisForTibero/loadOriginalDb?project_id="+data.project_id
				+"&database_kinds_cd="+$("#database_kinds_cd").val(),
				method:"get",
				valueField:'original_dbid',
				textField:'original_db_name',
				panelHeight: 300,
				onLoadError: function() {
					/* modal progress close */
					if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
				},
				onLoadSuccess: function(items) {
					/* modal progress close */
					if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
					
					$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
					$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
					
					$("#submit_form #perf_check_target_dbid").combobox('setValue','');
				},
				onSelect: function(data) {
					if(data.original_dbid != null && data.original_dbid != "") {
						// 대상 DB
						$("#submit_form #perf_check_target_dbid").combobox({
							url:"/DBChangePerformanceImpactAnalysisForTibero/loadOriginalDb?project_id="+data.project_id
							+"&original_dbid="+data.original_dbid+"&database_kinds_cd="+$("#database_kinds_cd").val(),
							method:"get",
							valueField:'perf_check_target_dbid',
							textField:'perf_check_target_db_name',
							panelHeight: 300,
							onLoadError: function() {
								/* modal progress close */
								if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
							},
							onLoadSuccess: function(items) {
								/* modal progress close */
								if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
								onReadonlyCheck();
								$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
							}
						});
					} else {
						$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
					}
					
				}
			});	
			
			onReadonlyCheck();
		},
		onLoadSuccess: function(items) {
			$("#submit_form #project_id").combobox("textbox").attr("placeholder",'선택');
			$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
			
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	$("#topn_cnt").textbox("readonly",true);
	
	$("#all_sql_yn").val("Y");
	$("#all_sql_yn_chk").checkbox({
		value:"Y",
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$("#all_sql_yn").val("Y");
				$("#topn_cnt").textbox("setValue","");
				$("#topn_cnt").textbox("readonly",true);
			} else {
				$("#all_sql_yn").val("N");
				$("#topn_cnt").textbox("readonly",false);
				$(".topn_cnt .textbox-prompt").removeClass("textbox-prompt");
				$(".topn_cnt .validatebox-readonly").removeClass("validatebox-readonly");
			}
		}
	});
	
	$("#literal_except_yn").val("Y");
	$("#literal_except_yn_chk").checkbox({
		value:"Y",
		checked:true,
		onChange: function( checked ) {
			if ( checked ) {
				$("#literal_except_yn").val("Y");
			} else {
				$("#literal_except_yn").val("N");
			}
		}
	});
	$(".sql_time_limt_cd .textbox").keyup(function() {
		if ( floatCheck( $(".sql_time_limt_cd .textbox-text").val() ) == false ) {
			parent.$.messager.alert('경고','SQL Time Limit는 소수점을 입력할 수 없습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
		
		if ( $("#sql_time_limt_cd").combobox("getText") != "무제한" && $("#sql_time_limt_cd").combobox("getText") != "" && /^(\-|\+)?([0-9]+)$/.test( $("#sql_time_limt_cd").combobox("getText") ) == false ) {
			parent.$.messager.alert('경고','SQL Time Limit는 1이상 정수만 입력할 수 있습니다.','warning');
			$("#sql_time_limt_cd").combobox("setValue","");
			return false;
		}
	});
	
	$('.owner_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).combobox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}
		
		$("#ownerEdit").val($("#owner_list").val() );
		$('#ownerEditBox').window({
			top:getWindowTop(315),
			left:getWindowLeft(1435)
		});
		$("#ownerEditBox").window("open");
		
	});
	$('.module_list .searchbox-button').click( function() {
		if ( $("#submit_form #project_id" ).combobox("getValue") == '' ) {
			parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
			return false;
		}
		
		$("#moduleEdit").val( $("#module_list").val() );
		$('#moduleEditBox').window({
			top:getWindowTop(315),
			left:getWindowLeft(545)
		});
		$("#moduleEditBox").window("open");
	});
	
	$("#owner_list").keyup(function() {
		$("#ownerEdit").val( $("#owner_list").textbox("getValue") );
	});
	$("#module_list").keyup(function() {
		$("#moduleEdit").val( $("#module_list").textbox("getValue") );
	});
	
	$("#sql_time_limt_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id="+$("#commonCode").val(),
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess:function( item ) {
			$("#sql_time_limt_cd").combobox("setValue",item[0].cd);
		},
		onChange:function( newValue, oldValue ) {
			if ( newValue == "99" ) {
				if ( oldValue == "99") {
					$("#sql_time_limt_cd").combobox("setText","99");
				} else {
					$("#sql_time_limt_cd").combobox("setValue","");
				}
				
				$("#sql_time_direct_pref_value").val("99");
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",false);
			} else if( newValue == "98" ) {
				$("#sql_time_limt_cd").combobox("setValue","98");
				$("#sql_time_direct_pref_value").val( "" );
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",true);
			} else {
				$("#sql_time_limt_cd").combobox("textbox").attr("readonly",true);
			}
			
		}
	});
	
	// 실행방법
	$("#perf_compare_meth_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id="+$("#commonExecutionCode").val()
		+"&ref_vl_2="+$("#database_kinds_cd").val(),
		method:"get",
		valueField:'cd',
		textField:'ref_vl_1',
		onLoadSuccess:function( item ) {
			$("#perf_compare_meth_cd").combobox("setValue",item[0].ref_vl_1);
		},
		onSelect: function ( item ) {
			if ( item.cd == '3' ) {
//				$('#dml_exec_yn').switchbutton({disabled:true});
				$('#dml_exec_yn').switchbutton("readonly",true);
				$('#dml_exec_yn').switchbutton("uncheck");
				$('#multi_execution').switchbutton("readonly",true);
				$('#multi_execution').switchbutton("uncheck");
				$("#multiple_exec_cnt").textbox("readonly",true);
				$("#multiple_exec_cnt").textbox("setValue","1");
				$('#multi_bind_execution').switchbutton("readonly",true);
				$('#multi_bind_execution').switchbutton("uncheck");
				$("#multiple_bind_exec_cnt").textbox("readonly",true);
				$("#multiple_bind_exec_cnt").textbox("setValue","1");
				$("#sql_time_limt_cd").combobox("readonly",true);
				$("#max_fetch_cnt").textbox("readonly",true);
				
			} else {
				$('#dml_exec_yn').switchbutton("readonly",false);
				$('#multi_execution').switchbutton("readonly",false);
				$("#multiple_exec_cnt").textbox("setValue","1");
				$('#multi_bind_execution').switchbutton("readonly",false);
				$("#multiple_bind_exec_cnt").textbox("setValue","1");
				$("#sql_time_limt_cd").combobox("readonly",false);
				$("#max_fetch_cnt").textbox("readonly",false);
				
			}
		}
	});
	// 병렬실행
	$('#parallel_degree_yn').switchbutton({
		checked: false,
		onText:'YES',
		offText:'NO',
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked ) {
					$("#parallel_degree").textbox("readonly",false);
					$("#parallel_degree").textbox("setValue","4");
				} else {
					$("#parallel_degree").textbox("readonly",true);
					$("#parallel_degree").textbox("setValue","1");
				}
			},100);
		}
	});
	$("#parallel_degree").textbox("readonly",true);
	
	// DML 실행
	$('#dml_exec_yn').switchbutton({
		checked: false,
		onText:'YES',
		offText:'NO',
		value:'Y'
	});
	
	// Multiple실행
	$('#multi_execution').switchbutton({
		checked: false,
		onText:'YES',
		offText:'NO',
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked ) {
					$("#multiple_exec_cnt").textbox("readonly",false);
					$("#multiple_exec_cnt").textbox("setValue","4");
				} else {
					$("#multiple_exec_cnt").textbox("readonly",true);
					$("#multiple_exec_cnt").textbox("setValue","1");
				}
			},100);
		}
	});
	$("#multiple_exec_cnt").textbox("readonly",true);
	
	// Multiple Bind 실행
	$('#multi_bind_execution').switchbutton({
		checked: false,
		onText:'YES',
		offText:'NO',
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked ) {
					$("#multiple_bind_exec_cnt").textbox("readonly",false);
					$("#multiple_bind_exec_cnt").textbox("setValue","4");
				} else {
					$("#multiple_bind_exec_cnt").textbox("readonly",true);
					$("#multiple_bind_exec_cnt").textbox("setValue","1");
				}
			},100);
		}
	});
	$("#multiple_bind_exec_cnt").textbox("readonly",true);
	
	$('#chkRefresh').switchbutton({
		checked: false,
		onChange: function( checked ) {
			setTimeout(function() {
				if ( checked && $("#refresh").val() == "N" ) {
					$("#refresh").val("Y");
					
					if ( $('#project_id').combobox("getValue") == "" ) {
						parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					if ( $("#submit_form #sqlPerformanceP" ).combobox("getValue") == '' ) {
						parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
						$("#refresh").val("N");
						$('#submit_form #chkRefresh').switchbutton("uncheck");
						
						return;
					}
					
					$("#timer_value").textbox({disabled:true});
					
					Btn_RefreshChange();
				} else {
					$("#refresh").val("N");
					$("#timer_value").textbox({disabled:false});
					
					window.clearTimeout(timer);
				}
			},300);
		}
	});
	
	document.getElementsByName('timer_value')[0].parentElement.firstChild.setAttribute('min','3');
	
	$("#inProgress").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			$("#currentPage").val("1");
			Btn_LoadPerformanceCheckList();
		}
	});
	
	$("#completion").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			$("#currentPage").val("1");
			Btn_LoadPerformanceCheckList();
		}
	});
	
	createList();
	
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function loadSqlPerformanceCombo( projectId ) {
	// submit_form SQL점검팩 콤보
	$('#submit_form #sqlPerformanceP').combobox({
		url:"/DBChangePerformanceImpactAnalysisForTibero/getSqlPerfPacName?project_id="+projectId
		+"&database_kinds_cd="+$("#database_kinds_cd").val(),
		method:"post",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		panelHeight: 300,
		onSelect: function(item) {
			if ( item.sql_auto_perf_check_id == null) {
				$("#submit_form #sqlPerformanceP").combobox("setValue",'');
				$("#submit_form #sql_auto_perf_check_id").val('');
			}	else {
				$("#submit_form #data_yn").val("N");
				$("#submit_form #data_yn").val(item.data_yn);
			}
			
			$("#sql_time_direct_pref_value").val("");
			
			if ( item.sql_auto_perf_check_id != $("#submit_form #sql_auto_perf_check_id").val()) {
				$("#submit_form #sql_auto_perf_check_id").val( item.sql_auto_perf_check_id );
				ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/getSqlPerfDetailInfo",
						$("#submit_form"),
						"POST",
						callback_readSqlPerfDetail);
			}
		},
		onShowPanel: function() {
				loadSqlPerformanceCombo( projectId );
				$(".textbox").removeClass("textbox-focused");
				$(".textbox-text").removeClass("tooltip-f");
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
		onLoadSuccess: function(data) {
			$("#submit_form #sqlPerformanceP").combobox("textbox").attr("placeholder",'선택');
			$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
			
			let sqlAutoPerfId = $("#submit_form #sql_auto_perf_check_id").val(); 
			
			$("#submit_form #sqlPerformanceP").combobox("setValue", sqlAutoPerfId );
			
			$("#all_sql_yn_chk").checkbox("check");
			$("#submit_form #perf_check_range_begin_dt").datebox("setValue", $("#startDate").val() );
			$("#submit_form #perf_period_start_time").textbox("setValue", $("#startTime").val() );
			$("#submit_form #perf_check_range_end_dt").datebox("setValue", $("#endDate").val() );
			$("#submit_form #perf_period_end_time").textbox("setValue", $("#endTime").val() );
			$("#sql_time_limt_cd").combobox("setValue", "01");
			$("#submit_form #max_fetch_cnt").numberbox("setValue","100000");
			
			if ( sqlAutoPerfId == null || sqlAutoPerfId == '' ) {
				Btn_LoadPerformanceCheckList();
			}
		},
		onLoadError: function() {
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
}

function commaCheck( array ) {
	if ( array.length > 100) {
		parent.$.messager.alert('경고','값은 100개 까지 입력 가능합니다.','warning');
		return false;
	}
}

function floatCheck( obj ) {
	var fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;
	 
	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

var callback_readSqlPerfDetail = function(value) {
	var data = JSON.parse(value)[0];
	
	if (typeof data == 'undefined') {
		/* modal progress close */
		if (parent.closeMessageProgress != undefined) parent.closeMessageProgress();
		
		return;
	}
	
	if ( data.perf_check_sql_source_type_cd == '1' || data.perf_check_sql_source_type_cd == 1) {
		$('#AWR').radiobutton({checked: true});
	} else {
		$("#entireSQL").radiobutton({checked: true});
	}
	
	$("#submit_form #sqlPerformanceP").combobox("setValue", data.sql_auto_perf_check_id);
	$("#submit_form #sql_auto_perf_check_id").val( data.sql_auto_perf_check_id );
	
	$("#submit_form #original_dbid").combobox("setValue", data.original_dbid);
	$("#submit_form #perf_check_target_dbid").combobox("setValue", data.perf_check_target_dbid);
	
	$("#submit_form #perf_check_range_begin_dt").textbox("setValue", data.perf_check_range_begin_dt);
	$("#submit_form #perf_check_range_end_dt").textbox("setValue", data.perf_check_range_end_dt);
	$("#submit_form #perf_period_start_time").textbox("setValue", data.perf_period_start_time);
	
	if ( data.perf_period_end_time != null && data.perf_period_end_time != '' && data.perf_period_end_time != "00:00" ) {
		$("#submit_form #perf_period_end_time").textbox("setValue", data.perf_period_end_time);
	} else {
		$("#submit_form #perf_period_end_time").textbox("setValue", $("#endTime").val() );
	}
	
	if ( data.all_sql_yn != null && data.all_sql_yn != "" ) {
		$("#submit_form #all_sql_yn").val( data.all_sql_yn );
		
		if ( data.all_sql_yn == "N" ) {
			$("#submit_form #all_sql_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #all_sql_yn_chk").checkbox("check");
		}
	} else {
		$("#submit_form #all_sql_yn").val("Y");
		$("#submit_form #all_sql_yn_chk").checkbox("check");
	}
	
	$("#submit_form #topn_cnt").textbox("setValue", data.topn_cnt);
	
	if ( data.literal_except_yn != null && data.literal_except_yn != "" ) {
		$("#submit_form #literal_except_yn").val( data.literal_except_yn );
		
		if ( data.literal_except_yn == "N" ) {
			$("#submit_form #literal_except_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #literal_except_yn_chk").checkbox("check");
		}
	} else {
		$("#submit_form #literal_except_yn").val("Y");
		$("#submit_form #literal_except_yn_chk").checkbox("check");
	}
	
	$("#submit_form #sql_time_limt_cd").combobox("setValue", data.sql_time_limt_cd);
	
	if ( data.sql_time_limt_cd != null && data.sql_time_limt_cd == "99" && data.sql_time_direct_pref_value == "98" ) {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", "99");
		$("#submit_form #sql_time_limt_cd").combobox("setText", "98");
		$("#submit_form #sql_time_direct_pref_value").val("99");
	} else if ( data.sql_time_limt_cd != null && data.sql_time_limt_cd == "99" && data.sql_time_direct_pref_value == "99" ) {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", "99");
		$("#submit_form #sql_time_limt_cd").combobox("setText", "99");
		$("#submit_form #sql_time_direct_pref_value").val("99");
	} else if ( data.sql_time_limt_cd != null && data.sql_time_limt_cd == "99" 
		&& data.sql_time_direct_pref_value != null && data.sql_time_direct_pref_value != '') {
		$("#submit_form #sql_time_limt_cd").combobox("setValue", data.sql_time_direct_pref_value);
		$("#submit_form #sql_time_direct_pref_value").val("99");
	}
	
	if ( data.max_fetch_cnt != '' && data.max_fetch_cnt != null ) {
		$('#max_fetch_cnt').textbox('setValue', data.max_fetch_cnt);
	} else {
		$('#max_fetch_cnt').textbox('setValue', "100000");
	}
	
	$("#submit_form #owner_list").textbox("setValue", data.owner_list);
	$("#submit_form #module_list").textbox("setValue", data.module_list);
	$("#submit_form #extra_filter_predication").textbox("setValue", data.extra_filter_predication);
	$("#filter_sql_form #filter_sql").textbox("setValue", data.extra_filter_predication);
	
	if ( data.perf_compare_meth_cd != null && data.perf_compare_meth_cd != '' ){
		$("#submit_form #perf_compare_meth_cd").combobox("setValue", data.perf_compare_meth_cd);
	} else {
		$("#submit_form #perf_compare_meth_cd").combobox("setValue", "2");
	}
	
	if ( parseInt(data.parallel_degree) > 1 ) {
		$("#submit_form #parallel_degree_yn").switchbutton("check");
		$("#submit_form #parallel_degree").textbox("readonly",false);
		setTimeout(function() {
			$("#submit_form #parallel_degree").textbox("setValue", data.parallel_degree);
		},100);
	} else {
		$("#submit_form #parallel_degree_yn").switchbutton("uncheck");
		$("#submit_form #parallel_degree").textbox("readonly",true);
		$("#submit_form #parallel_degree").textbox("setValue", "1");
	}
	
	if ( data.dml_exec_yn == 'Y' ) {
		$("#submit_form #dml_exec_yn").switchbutton("check");
	} else {
		$("#submit_form #dml_exec_yn").switchbutton("uncheck");
	}
	
	if ( data.multiple_exec_cnt > 1 ) {
		$("#submit_form #multi_execution").switchbutton("check");
		$("#multiple_exec_cnt").textbox("readonly",false);
		setTimeout(function() {
			$("#submit_form #multiple_exec_cnt").textbox("setValue", data.multiple_exec_cnt);
		},100);
	} else {
		$("#submit_form #multi_execution").switchbutton("uncheck");
		$("#multiple_exec_cnt").textbox("readonly",true);
		$("#submit_form #multiple_exec_cnt").textbox("setValue", "1");
	}
	
	if ( data.multiple_bind_exec_cnt > 1 ) {
		$("#submit_form #multi_bind_execution").switchbutton("check");
		$("#multiple_bind_exec_cnt").textbox("readonly",false);
		setTimeout(function() {
			$("#submit_form #multiple_bind_exec_cnt").textbox("setValue", data.multiple_bind_exec_cnt);
		},100);
	} else {
		$("#submit_form #multi_bind_execution").switchbutton("uncheck");
		$("#multiple_bind_exec_cnt").textbox("readonly",true);
		$("#submit_form #multiple_bind_exec_cnt").textbox("setValue", "1");
	}
	
	$("#submit_form #data_yn").val( data.data_yn );
	onReadonlyCheck();
	
	let gridData = $("#tableList").datagrid('getData');
	
	if ( gridData.rows.length > 0 ) {
		let checkCnt = 0;
		for (var dataIdx = 0; dataIdx < gridData.rows.length; dataIdx++) {
			if ( gridData.rows[dataIdx].sql_auto_perf_check_id == $("#submit_form #sqlPerformanceP").combobox("getValue") ) {
				checkCnt++;
				break;
			}
		}
		
		if ( checkCnt != 0) {
			$("#tableList").datagrid("selectRow",dataIdx);
			$("#submit_form #sql_auto_perf_check_id").val( $("#submit_form #sqlPerformanceP").combobox("getValue") );
		} else {
			$("#tableList").datagrid("unselectAll");
		}
	}
	
	$("#submit_form #original_dbid").combobox("textbox").attr("placeholder",'선택');
	$("#submit_form #perf_check_target_dbid").combobox("textbox").attr("placeholder",'선택');
}

function onReadonlyCheck() {
	if ( $("#submit_form #data_yn").val() == "Y" ) {
		$("#submit_form #original_dbid").combobox("readonly","true");
		$("#submit_form #perf_check_target_dbid").combobox("readonly","true");
		$("#submit_form #perf_check_range_begin_dt").textbox("readonly","true");
		$("#submit_form #perf_period_start_time").textbox("readonly","true");
		$("#submit_form #perf_check_range_end_dt").textbox("readonly","true");
		$("#submit_form #perf_period_end_time").textbox("readonly","true");
		$("#submit_form #all_sql_yn_chk").checkbox("readonly","true");
		$("#submit_form #topn_cnt").textbox("readonly","true");
		$("#submit_form #literal_except_yn_chk").checkbox("readonly","true");
		$("#submit_form #owner_list").textbox("readonly","true");
		$("#submit_form #module_list").textbox("readonly","true");
		$("#submit_form #extra_filter_predication").textbox("readonly","true");
		
		$("#ownerEdit").attr("readonly","true");
		$("#moduleEdit").attr("readonly","true");
		$("#submit_form #perf_compare_meth_cd").combobox("readonly","true");
		$("#submit_form #AWR").radiobutton("readonly", true);
		$("#submit_form #entireSQL").radiobutton("readonly", true);
		
		$("#submit_form #btnFilterSQL").linkbutton({disabled:true});
	} else {
		$(".textbox").removeClass("textbox-readonly");
		
		$(".textbox-icon-disabled").removeClass("textbox-icon-disabled");
		$(".validatebox-readonly").removeClass("validatebox-readonly");
		$(".checkbox-readonly").removeClass("checkbox-readonly");
		
		$("#original_dbid").combobox("readonly",false);
		$("#perf_check_target_dbid").combobox("readonly",false);
		$("#perf_check_range_begin_dt").textbox("readonly",false);
		$("#perf_period_start_time").textbox("readonly",false);
		$("#perf_check_range_end_dt").textbox("readonly",false);
		$("#perf_period_end_time").textbox("readonly",false);
		
		if ( $("#perf_check_range_begin_dt").textbox("getValue") == "" || $("#perf_check_range_begin_dt").textbox("getValue") == null ) {
			$("#perf_check_range_begin_dt").textbox("setValue", $("#startDate").val());
		}
		
		if ( $("#perf_check_range_end_dt").textbox("getValue") == "" || $("#perf_check_range_end_dt").textbox("getValue") == null ) {
			$("#perf_check_range_end_dt").textbox("setValue", $("#endDate").val());
		}
		
		if ( $("#perf_period_start_time").textbox("getValue") == "" || $("#perf_period_start_time").textbox("getValue") == null ) {
			$("#perf_period_start_time").textbox("setValue", $("#startTime").val());
		}
		
		if ( $("#perf_period_end_time").textbox("getValue") == "" || $("#perf_period_end_time").textbox("getValue") == null ) {
			$("#perf_period_end_time").textbox("setValue", $("#endTime").val());
		}
		
		$("#all_sql_yn_chk").checkbox("readonly",false);
		
		$("#topn_cnt").textbox("readonly",false);
		
		if ( $("#submit_form #all_sql_yn").val() == "N") {
			$("#submit_form #all_sql_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #all_sql_yn_chk").checkbox("check");
			$("#topn_cnt").textbox("readonly",true);
		}
		
		$("#literal_except_yn_chk").checkbox("readonly",false);
		
		if ( $("#literal_except_yn").val() == "N" ) {
			$("#submit_form #literal_except_yn_chk").checkbox("uncheck");
		} else {
			$("#submit_form #literal_except_yn_chk").checkbox("check");
		}
		
		$("#owner_list").textbox("readonly",false);
		$("#module_list").textbox("readonly",false);
		
		$("#ownerEdit").attr("readonly",false);
		$("#moduleEdit").attr("readonly",false);
		
		$("#submit_form #btnFilterSQL").linkbutton({disabled:false});
		$("#extra_filter_predication").textbox("readonly", false );
		$("#extra_filter_predication").textbox("setValue", "" );
		$("#filter_sql_form #filter_sql").textbox("setValue", "" );
		
		$('#owner_list').textbox('textbox').prop('placeholder', 'ERP, MIS, BIZHUB');
		$('#module_list').textbox('textbox').prop('placeholder', "JDBC, B_ERP0001, S_ERP0001");
		
		$("#submit_form #perf_compare_meth_cd").combobox("readonly",false);
		$("#AWR").radiobutton("readonly", false);
		$("#entireSQL").radiobutton("readonly", false);
		$("#entireSQL").radiobutton({checked: true});
		
		$("#parallel_degree").textbox("readonly",true);
		$("#multiple_exec_cnt").textbox("readonly",true);
		$("#multiple_bind_exec_cnt").textbox("readonly",true)
		
		if ( $("#submit_form #sql_time_limt_cd").combobox("getValue") == "" || $("#submit_form #sql_time_limt_cd").combobox("getValue") == null ) {
			$("#sql_time_limt_cd").combobox("setValue","1");
		}
		
		if ( $("#submit_form #perf_compare_meth_cd").combobox("getValue") != '3' ) {
			$("#sql_time_limt_cd").combobox("readonly",false);
			$("#max_fetch_cnt").textbox("readonly",false);
		}
		
	}
}

function checkCondition() {
	if ( $("#submit_form #project_id" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #sqlPerformanceP" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','SQL 점검팩을 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #original_dbid").combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','ORACLE DB(원천DB)를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #perf_check_target_dbid").combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','TIBERO DB(목표DB)를 선택해 주세요.','warning');
		return false;
	}
	
	if ( $("#submit_form #perf_check_range_begin_dt").textbox("getValue") == '' ||
		 $("#submit_form #perf_check_range_end_dt").textbox("getValue") == '' ||
		 $("#submit_form #perf_period_start_time").textbox("getValue") == '' ||
		 $("#submit_form #perf_period_end_time").textbox("getValue") == '' ) {
		parent.$.messager.alert('경고','수집기간을 입력해 주세요.','warning');
		return false;
	}
	
	let startDate = $("#submit_form #perf_check_range_begin_dt").textbox("getValue");
	let endDate = $("#submit_form #perf_check_range_end_dt").textbox("getValue");
	let startTime = $("#submit_form #perf_period_start_time").textbox("getValue");
	let endTime = $("#submit_form #perf_period_end_time").textbox("getValue");
	
	if ( startDate > endDate || startDate == endDate && startTime > endTime ) {
		parent.$.messager.alert('경고','수집기간을 확인해 주세요.','warning');
		return false;
	}
	
	let textArea = $("#extra_filter_predication").textbox("getValue");
	let byte = 0;
	
	for (var textIdx = 0; textIdx < textArea.length; textIdx++) {
		(textArea.charCodeAt(textIdx) > 127)?byte+=3:byte++;
	}
	
	if ( byte >= 4000 ) {
		parent.$.messager.alert('경고','FilterSQL조건이 너무 많습니다.<br>최대 : 4000 Byte , 현재 : '+byte+' Byte','warning');
		return false;
	}
	
	if ( $("#submit_form #topn_cnt").textbox("getValue") == '' &&  $("#all_sql_yn_chk").checkbox("options").checked == false ) {
		parent.$.messager.alert('경고','TOP N을 입력해 주세요.','warning');
		return false;
	}
	
	return true;
}

function textCheck() {
	var split = ""; 
	
	split1 = $(".owner_list .textbox-text").val().trim().split("\,");
	if ( split1.length > 100) {
		parent.$.messager.alert('경고','TABLE_OWNER 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( split1.length > 1 ) {
			for ( var ownerIdx = 0; ownerIdx < split1.length; ownerIdx++) {
				if ( split1[ownerIdx].trim() == "" ) {
					parent.$.messager.alert('경고','TABLE_OWNER 값을 확인 후 다시 성능비교 를 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	split2 = $(".module_list .textbox-text").val().trim().split("\,");
	if ( split2.length > 100) {
		parent.$.messager.alert('경고','MODULE 값은 100개 까지 입력 가능합니다.','warning');
		return false;
	} else {
		if ( split2.length > 1 ) {
			for ( var moduleIdx = 0; moduleIdx < split2.length; moduleIdx++) {
				if ( split2[moduleIdx].trim() == "" ) {
					parent.$.messager.alert('경고','MODULE 값을 확인 후 다시 성능비교 를 실행해 주세요.','warning');
					return false;
				}
			}
		}
	}
	
	return true;
}

function Btn_SqlAutoPerfCompare() {
	if ( checkCondition() == false ) {
		return;
	}
	
	if ( $("#sql_time_limt_cd").combobox("getText").trim() == "" ) {
		parent.$.messager.alert('경고','SQL Time Limit를 입력해주세요.','warning');
		$("#sql_time_limt_cd").combobox("setText","");
		return;
	}
	
	if ( !textCheck() ) {
		return;
	}
	
	var param = "확인";
	var msgStr ="실행 하시겠습니까?";
	
	parent.$.messager.confirm( param,msgStr,function(r) {
		if (r) {
			ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/getTuningTargetCount",
					$("#submit_form"),
					"POST",
					callback_getTuningTargetCountAction);
		}
	});
}

var callback_getTuningTargetCountAction = function( result ) {
	if ( result.result ) {
		
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/countExecuteTms",
				$("#submit_form"),
				"POST",
				callback_CountExecuteTmsAction);
	} else {
		parent.$.messager.alert('정보','튜닝대상으로 선정되었던 SQL이 존재합니다.<br>튜닝대상 선정 작업이 수행되었을 경우 재실행 할 수 없습니다.','info');
	}
}

var callback_CountExecuteTmsAction = function( result ) {
	let sqlTimeLimt_cd = $("#sql_time_limt_cd").combobox("getValue");
	let sqlTimeLimt_text = $("#sql_time_limt_cd").combobox("getText");
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( sqlTimeLimt_text == "98" && $("#sql_time_direct_pref_value").val() == "99" ) {
		// 직접입력
		console.log("직접입력 98");
		$("#sql_time_limt_cd").combobox("setValue","98");
		$("#sql_time_limt_cd").combobox("setText","98");
		$("#sql_time_direct_pref_value").val( "99" );
	} else if ( sqlTimeLimt_text == "99" && $("#sql_time_direct_pref_value").val() == "99" ) {
		// 직접입력
		console.log("직접입력 99");
		$("#sql_time_limt_cd").combobox("setValue","'99'");
		$("#sql_time_limt_cd").combobox("setText","99");
		$("#sql_time_direct_pref_value").val( "99" );
	} else if ( $("#sql_time_direct_pref_value").val() == "99" ) {
		// 직접입력
		console.log("직접입력");
		$("#sql_time_limt_cd").combobox("setValue",$("#sql_time_limt_cd").combobox("getText"));
	} else if ( sqlTimeLimt_cd == "1" ) {
		// 처음값 세팅
		console.log("처음값");
		$("#sql_time_limt_cd").combobox("setValue","01");
		$("#sql_time_direct_pref_value").val( '' );
	} else if ( sqlTimeLimt_cd == "98" && $("#sql_time_direct_pref_value").val() != "99" ) {
		// 무제한
		console.log("무제한");
		$("#sql_time_direct_pref_value").val( "0" );
	} else if ( sqlTimeLimt_cd == "" && $("#sql_time_direct_pref_value").val() == "" ) {
		// 신규
		console.log("신규");
		$("#sql_time_limt_cd").combobox("setValue", $("#sql_time_limt_cd").combobox("getText") );
		$("#sql_time_direct_pref_value").val( '99' );
	} else {
		// 그 외
		console.log("그외"+sqlTimeLimt_cd + " , "+$("#sql_time_direct_pref_value").val());
		$("#sql_time_direct_pref_value").val( '' );
	}
	
	console.log("성능영향도분석 실행["+$("#sql_time_limt_cd").combobox("getText") + "] , ["+ $("#sql_time_limt_cd").combobox("getValue")+"] , ["+ $("#sql_time_direct_pref_value").val()+"]" );
	if ( result.result && result.txtValue == "true" ) {
		if (parent.openMessageProgress != undefined) parent.openMessageProgress("대상을 선정중입니다.<br>대상 선정이 완료되면 작업이 자동실행 됩니다."," ");
		
		// 화면  readOnly 처리
		$("#submit_form #data_yn").val("Y");
		onReadonlyCheck();
		
		// 02. SQL 자동성능점검 수행
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/updateSqlAutoPerformance",
				$("#submit_form"),
				"POST",
				callback_updateSqlAutoPerformanceAction);
	} else {
		if ( result.result == false && result.txtValue == "true" ) {
			var param = "확인";
			var msgStr ="이전에 수행된 실행 정보가 있습니다. 재수행시 이전 실행 정보가 삭제됩니다.<br>계속 진행하시겠습니까?";
			
			parent.$.messager.confirm( param,msgStr,function(r) {
				if (r) {
					if (parent.openMessageProgress != undefined) parent.openMessageProgress("대상을 선정중입니다.<br>대상 선정이 완료되면 작업이 자동실행 됩니다."," ");
					
					// 화면  readOnly 처리
					$("#submit_form #data_yn").val("Y");
					onReadonlyCheck();
					
					// 02. SQL 자동성능점검 수행
					ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/updateSqlAutoPerformance",
							$("#submit_form"),
							"POST",
							callback_updateSqlAutoPerformanceAction);
					
				} else {
					if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
				}
			});
		} else {
			/* modal progress close */
			if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
			
			parent.$.messager.alert('정보','해당 SQL점검팩에 대한 작업을 수행 중입니다.<br>수행결과에서 진행상황을 검색할 수 있습니다.','info');
		}
	}
	
}

function callback_updateSqlAutoPerformanceAction( result ) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	// 등록된 값이 0건 이면 실행.
	if ( result.txtValue == '0' ) {
		parent.$.messager.alert('정보','수집된 SQL이 0건 입니다.<br> 조건값들을 조정 후 재수행 바랍니다.','info');
		
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/updateAutoPerfChkIsNull",
				$("#submit_form"),
				"POST",
				callback_UpdateAutoPerfChkIsNullAction);
		
		return false;
	}
	
	if ( !result.result ) {
		parent.$.messager.alert('정보',result.message, 'warning');
		
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/updateAutoPerfChkIsNull",
				$("#submit_form"),
				"POST",
				callback_UpdateAutoPerfChkIsNullAction);
		
		return false;
	} else {
		ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/performanceCompareCall",
				$("#submit_form"),
				"POST",
				null);
		
		setTimeout(function() {
			// 수행 결과 조회
			Btn_RefreshChange();
		},200);
	}
}

/* SQL-3 : 수행결과 조회 */
function Btn_RefreshChange() {
	var intSec = 0;
	
	Btn_LoadPerformanceCheckList();
	
	if ( $("#refresh").val() == "Y" ) {
		intSec = strParseInt( $("#timer_value").textbox("getValue"),0 );
		window.clearTimeout(timer);
		timer = window.setTimeout("Btn_RefreshChange()",(intSec*1000));
	} else {
		window.clearTimeout(timer);
	}
}

function Btn_LoadPerformanceCheckList() {
	$("#tableList").datagrid("loading");
	
	if ( $("#submit_form #project_id" ).combobox("getValue") == '' ) {
		parent.$.messager.alert('경고','프로젝트를 먼저 선택해 주세요.','warning');
		return false;
	}
	
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadPerformanceList",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceCheckListAction);
}

var callback_LoadPerformanceCheckListAction = function( result ) {
	
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	var data = JSON.parse(result);
	
	json_string_callback_common(result,'#tableList',true);
	fnEnableDisablePagingBtn(dataLength);
	
	// list 조회시 기존 점검팩 ID 가 동일한경우 클릭 선택되어진다.
	if ( data.rows.length > 0 ) {
		for (var dataIdx = 0; dataIdx < data.rows.length; dataIdx++) {
			if ( data.rows[dataIdx].sql_auto_perf_check_id == $("#submit_form #sqlPerformanceP").combobox("getValue") ) {
				$("#tableList").datagrid("selectRow",dataIdx);
				$("#submit_form #sql_auto_perf_check_id").val( $("#submit_form #sqlPerformanceP").combobox("getValue") );
			}
		}
	}
	
	/* modal progress close */
	$("#tableList").datagrid("loaded");
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

var callback_UpdateAutoPerfChkIsNullAction = function( result ) {
	$("#filter_sql_form #sql_filter").val("");
	$("#submit_form #data_yn").val("N");
	
	onReadonlyCheck();
	
	window.clearTimeout(timer);
}

function dateFormatValidationCheck(dateString) {
	// First check for the pattern
	if (!/^\d{2}\/\d{1,2}\/\d{1,2}$/.test(dateString)) {
		return false;
	}
	
	return true;
}

function Btn_ForceUpdateSqlAutoPerformanceCheck() {
	if ( checkCondition() == false ) {
		return;
	}
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/countExecuteTms",
			$("#submit_form"),
			"POST",
			callback_ForceCountExecuteTmsAction);
}

var callback_ForceCountExecuteTmsAction = function(result) {
	if ( result.txtValue == 'false' ) {
		
		// 수행중일 경우
		var param = "확인";
		var msgStr = $('#submit_form #sqlPerformanceP').combobox('getText') + "에서 수행 중인 작업을 강제완료처리 하시겠습니까?";
		
		parent.$.messager.confirm(param,msgStr,function(r) {
			if (r) {
				/* modal progress open */
				if (parent.openMessageProgress != undefined) parent.openMessageProgress("강제완료처리", "강제 완료 처리 중입니다.");
				
				ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/forceUpdateSqlAutoPerformance",
						$("#submit_form"),
						"POST",
						callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
			}
		});
	} else {
		parent.$.messager.alert('정보','해당 SQL점검팩에서 실행 중인 작업이 없습니다.','info');
	}
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	
	if ( result.result ) {
		parent.$.messager.alert('정보','해당 SQL점검팩에 대한 강제 완료 처리가 완료되었습니다.','info');
	} else {
		console.log("resultMsg ===> "+ result.message );
		parent.$.messager.alert('정보','강제완료처리 하지 못했습니다.' , 'info');
	}
	
	Btn_RefreshChange();
}

function closeOwnerBox() {
	$("#owner_list").textbox( "setValue", $("#ownerEdit").val() );
	$('#ownerEditBox').window("close");
}
function closeModuleBox() {
	$("#module_list").textbox( "setValue", $("#moduleEdit").val() );
	$('#moduleEditBox').window("close");
}


function Btn_AuthorityScript() {
	if ( checkCondition() == false ) {
		return;
	}
	
	$('#authorityScript_form #db').textbox( 'setValue', $('#perf_check_target_dbid').combobox('getText') );
	$('#authorityScript_form #dbid').val( $('#perf_check_target_dbid').combobox('getValue') );
	
	$('#authorityScript_form #checkTable').checkbox('reset');
	$('#authorityScript_form #checkView').checkbox('reset');
	$('#authorityScript_form #checkSequence').checkbox('reset');
	$('#authorityScript_form #checkFunction').checkbox('reset');
	$('#authorityScript_form #checkProcedure').checkbox('reset');
	$('#authorityScript_form #checkPackage').checkbox('reset');
	
	$('#authorityScript_form #scriptView').val('');
	
//	LoadUserName();
	LoadAuthUserName();
	
	$('#authorityScriptPop').window("open");
}

function LoadAuthUserName(){
	let targetDbid = $('#perf_check_target_dbid').combobox('getValue');
	
	$("#authorityScript_form #authorityUserName").combobox("setValue",'');
	$("#authorityScript_form #authorityUserName").combobox("textbox").attr("placeholder",'선택');
	
	$.ajax({
		type: "POST",
		url: "/DBChangePerformanceImpactAnalysisForTibero/getAuthUserNameTibero",
		datatype: 'json',
		data: { DBID : targetDbid } ,
		success: function( result ) {
			let data = JSON.parse(result);
			let jData = data.message;
			console.log(jData);
			
			if ( data.is_error == "false" ) {
				for (var dataIdx = 0; dataIdx < jData.length; dataIdx++) {
					jData[dataIdx] = JSON.parse(jData[dataIdx]);
				}
				
				$("#authorityScript_form #authorityUserName").combobox({
					valueField:"user_name",
					textField:"user_name",
					data:jData,
					onLoadSuccess:function(){
						$("#authorityScript_form #authorityUserName").combobox("textbox").attr("placeholder",'선택');
					}
				});
			} else {
				console.log(reulst);
				parent.$.messager.alert('정보','데이터를 불러오지 못했습니다.' , 'info');
			}
		},
		error:function ( result ){
			console.log(reulst);
			parent.$.messager.alert('정보','데이터를 불러오지 못했습니다.' , 'info');
		}
	});
}

//점검팩 삭제 시 SQL점검팩 RELOAD
function sqlPerfPacReload( projectId, sqlPerfId ) {
	var url = "/DBChangePerformanceImpactAnalysisForTibero/getSqlPerfPacName?project_id="+projectId
	+"&database_kinds_cd="+$("#database_kinds_cd").val();
	$("#submit_form #sqlPerformanceP").combobox("reload", url);
	console.log("reload됨");
	setTimeout(function() {
		if ( $("#submit_form #sqlPerformanceP").combobox("getValue") == sqlPerfId ) {
			$("#submit_form #sqlPerformanceP").combobox("setValue","");
			$("#submit_form #sql_auto_perf_check_id").val("");
			console.log( $("#submit_form #sqlPerformanceP").combobox("getValue") +" , "+ sqlPerfId );
		}
	},300);
	
}

function showFilterSQL(){
	$("#condition_1").combobox("setValue","");
	$("#condition_2").combobox("setValue","");
	
	old_filter_sql = filter_sql;
	$('#filterSqlPopup').window("open");
}

function createList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'perf_check_name',title:'SQL점검팩명',width:'12%',halign:'center',align:'left',rowspan:'2'},
			{field:'asis_db_name',title:'ORACLE<br>DB',width:'6%',halign:'center',align:'center',rowspan:'2'},
			{field:'tobe_db_name',title:'TIBERO<br>DB',width:'6%',halign:'center',align:'center',rowspan:'2'},
			{field:'perf_check_force_close_yn',title:'수행상태',width:'6%',halign:'center',align:'center',styler:cellStyler,rowspan:'2'},
			{title:'수행결과(SQL 수)',width:'8%',halign:'center',align:'center',colspan:3},
			{field:'complete_percent',title:'수행률(%)',width:'4%',halign:'center',align:'right',rowspan:'2',formatter:getNumberFormatNullChk},
			{field:'perf_check_exec_begin_dt',title:'작업시작일시',width:'8%',halign:'center',align:'center',rowspan:'2'},
			{field:'perf_check_exec_end_dt',title:'작업종료일시',width:'8%',halign:'center',align:'center',rowspan:'2'},
			{field:'exec_time',title:'수행시간',width:'8%',halign:'center',align:'right',rowspan:'2'},
			{field:'defaultText',title:'분석결과',width:'3%',halign:'center',align:'center',rowspan:'2'},
			],[
			{field:'total_cnt',title:'전체',width:'4%',halign:"center",align:'right'},
			{field:'completed_cnt',title:'완료',width:'4%',halign:"center",align:'right'},
			{field:'err_cnt',title:'오류',width:'4%',halign:"center",align:'right'},
			{field:'sql_auto_perf_check_id',title:'점검팩ID',hidden:'true'}
		]],
		onSelect:function( index, row ) {
			
			$("#submit_form #data_yn").val("Y");
			
			if ( row.sql_auto_perf_check_id != $("#submit_form #sql_auto_perf_check_id").val() ) {
				$("#submit_form #sql_auto_perf_check_id").val(row.sql_auto_perf_check_id);
				
				ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/getSqlPerfDetailInfo",
						$("#submit_form"),
						"POST",
						callback_readSqlPerfDetail);
			}
		},
		onLoadSuccess:function(data) {
			
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function cellStyler( value,row,index ) {
	
	if ( row.perf_check_force_close_yn == '강제완료' ) {
		  return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
	} else if ( row.perf_check_force_close_yn == '완료'){
		return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
	} else { 
		return ' background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
	}
}

function Btn_resultGo( projectId, sqlAutoPerfCheckId, dbcd ) {
	setTimeout(function() {
		parent.openMessageProgress("성능 영향도 분석 결과"," ");
		parent.selectTab(projectId, sqlAutoPerfCheckId, dbcd );
	},150);
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
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("성능 영향도 분석 조회"," ");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/DBChangePerformanceImpactAnalysisForTibero/loadPerformanceList",
			$("#submit_form"),
			"POST",
			callback_LoadPerformanceListAction);
}

var callback_LoadPerformanceListAction = function(result) {
	var dataLength = JSON.parse(result).dataCount4NextBtn;
	
	json_string_callback_common(result,'#tableList',true);
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}
