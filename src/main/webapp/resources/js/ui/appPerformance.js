var appDailyChart;
var appTimeChart;
var dbioDailyChart;
var dbioTimeChart;

Ext.EventManager.onWindowResize(function () {
    var width1 = $("#appDailyChart").width();
    var width2 = $("#appTimeChart").width();
    var width3 = $("#dbioDailyChart").width();
    var width4 = $("#dbioTimeChart").width();
    
    if(appDailyChart != "undefined" && appDailyChart != undefined){
    	appDailyChart.setSize(width1);		
	}
    if(appTimeChart != "undefined" && appTimeChart != undefined){
    	appTimeChart.setSize(width2);		
	}
    if(dbioDailyChart != "undefined" && dbioDailyChart != undefined){
    	dbioDailyChart.setSize(width3);		
	}
    if(dbioTimeChart != "undefined" && dbioTimeChart != undefined){
    	dbioTimeChart.setSize(width4);		
	}
});	

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
    var clipboard = new Clipboard('#sqlCopyBtn');
    clipboard.on('success', function(e) {
    	parent.$.messager.alert('','SQL TEXT가 복사되었습니다.');
    });
	
	createAppDailyChart();
	createAppTimeChart();
//	createDbioDailyChart();
//	createDbioTimeChart();
	
	// Work Job 조회
	$('#selectWrkJob').combobox({
	    url:"/Common/getWrkJob",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
		},
	    onSelect:function(rec){
	    	$("#wrkjob_cd").val(rec.wrkjob_cd);
	    	$("#dbid").val(rec.dbid);
	    },
		onLoadSuccess: function(data){
			$('#selectWrkJob').combobox("textbox").attr("placeholder","선택");
			$('#selectKey').combobox("textbox").attr("placeholder","선택");
			
			var tempWrkjobCd = $("#wrkjob_cd").val();
			
			$(this).combobox('setValue',tempWrkjobCd);
			if($("#dbio").val() != ""){
				$('#selectKey').combobox('setValue',"02");
				$('#searchValue').textbox('setValue',$("#dbio").val());
			}else if($("#tr_cd").val() != ""){
				$('#selectKey').combobox('setValue',"01");
				$('#searchValue').textbox('setValue',$("#tr_cd").val());
			}else{
				$('#selectKey').combobox('setValue',$("#searchKey").val());
				$('#searchValue').textbox('setValue',$("#tr_cd").val());
			}
			
			var tempSearchKey = $("#selectKey").combobox('getValue');
			var tempSearchValue = $("#searchValue").val();

			if(tempWrkjobCd != "" && tempSearchKey != "" && tempSearchValue != ""){
				fnSearch();
			}
		}
	});
	
	// 업무구분 조회
	/*
	$('#selectWrkJob').combobox({
	    url:"/Common/getWrkJobDev",
	    method:"get",
		valueField:'wrkjob_cd',
	    textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	$("#wrkjob_cd").val(rec.wrkjob_cd);
	    	$("#dbid").val(rec.dbid);
	    },
		onLoadSuccess: function(data){

			var tempWrkjobCd = $("#wrkjob_cd").val();
			console.log("tempWrkjobCd:"+tempWrkjobCd);
			var tempSearchKey = $("#searchKey").val();
			var tempTr_cd = $("#tr_cd").val();
			
			$(this).combobox('setValue',tempWrkjobCd);
			$('#selectKey').combobox('setValue',$("#searchKey").val());
			$('#searchValue').textbox('setValue',$("#tr_cd").val());
			
			if(tempWrkjobCd != "" && tempSearchKey != "" && tempTr_cd != ""){
				Btn_OnClick();
			}
		}	    
	});
	*/
	$('#searchValue').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick();
				}				
			}
		})
	});	
	
	$("#dbioList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : false,
		selectOnCheck : true,		
		onClickRow : function(index,row) {
			$("#appl_hash").val(row.appl_hash);
			$("#sql_hash").val(row.sql_hash);

			$("#appTab").tabs('select',0);
			$("#dbioTab").tabs('select',0);
			
			fnUpdateSearchBtnClickFlag();	
			getSubTables();
		},		
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'appl_name',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:"left",sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'left',sortable:"true"},
			{field:'exec_cnt',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'appl_hash',title:'APPL_HASH',halign:"center",align:'right',sortable:"true"},
			{field:'wrkjob_cd',hidden:"true"},
			{field:'sql_hash',title:'SQL_HASH',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',width:"500px",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#dbioList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td input").attr({ "style": "display:none" });
	$("#dbioList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ "style": "border:0px" });
	$("#dbioList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ style: "border:0px" });
	$("#dbioList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ "style": "border:0px" });
	$("#dbioList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ style: "border:0px" });

	$("#dailyTradeList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',width:"14%",halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',width:"15%",halign:"center",align:'right',sortable:"true"},
			{field:'fail_cnt',title:'FAIL_CNT',width:"14%",halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onClickRow:function(index,row){
    		//console.log("onClickRow index:"+index+" row:"+row);
    		$("table tr td div").css("cursor","default");
    		$("div").css("cursor","default");
    	},
    	onClickCell:function(index,field,value){
    		//console.log("onClickCell index:"+index+" field:"+field+" value:"+value);
    		$("table tr td div").css("cursor","default");
    		$("div").css("cursor","default");
    	}
	});
	
	$("#timeTradeList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG DT',width:"14%",halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',width:"15%",halign:"center",align:'right',sortable:"true"},
			{field:'fail_cnt',title:'FAIL_CNT',width:"14%",halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',width:"19%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onClickRow:function(index,row){
    		//console.log("onClickRow index:"+index+" row:"+row);
    		$("table, tr, td, div").css("cursor","default");
    		$(".datagrid-row ,.datagrid-row-checked ,.datagrid-row-selected,.datagrid-row-over").css("cursor","default !important");
    	},
    	onClickCell:function(index,field,value){
    		//console.log("onClickCell index:"+index+" field:"+field+" value:"+value);
    		$("table, tr, td, div").css("cursor","default");
    		$(".datagrid-row ,.datagrid-row-checked ,.datagrid-row-selected,.datagrid-row-over").css("cursor","default !important");
    	}
	});
	
	$("#dailyDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG_DT',width:"16%",halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',width:"18%",halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#timeDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'log_dt',title:'LOG_DT',width:"16%",halign:"center",align:"center",sortable:"true"},
			{field:'executions',title:'EXECUTIONS',width:"18%",halign:"center",align:'right',sortable:"true"},
			{field:'avg_elapsed_time',title:'AVG_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"},
			{field:'min_elapsed_time',title:'MIN_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"},
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',width:"22%",halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	//SQL식별자(DBIO), SQL 수행이력
	$("#historyDbioList").datagrid({
		view: myview,
		columns:[[
			{field:'snap_time',title:'SNAP TIME',halign:"center",align:"center",sortable:"true"},
			{field:'snap_id',title:'SNAP ID',halign:"center",align:'center',sortable:"true"},
			{field:'instance_number',title:'INST_ID',halign:"center",align:'right',sortable:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true"},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true"},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'row_processed',title:'ROW_PROCESSED',halign:"center",align:'right',sortable:"true"},
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true"},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true"},			
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true"},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true"},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true"},
			{field:'optimizer_env_hash_value',title:'OPTIMIZER ENV HASH VALUE',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onClickRow:function(index,row){
			var menuParam = "wrkjob_cd="
					+ $("#wrkjob_cd").val()
					+ "selectKey=" + $('#selectKey').combobox( 'getValue')
					+ "selectKey=" + $('#selectKey').combobox( 'getValue') 
					+ "&sql_id=" + row.sql_id
					+ "&dbid=" + $("#dbid").val()
					;
			parent.openLink("Y", "131", "SQL 성능 분석", "/SQLPerformance", menuParam);    		
    	},
    	onClickCell:function(index,field,value){
    	}
	});
	
	$("#appTab").tabs({
		plain: true,
		onSelect: function(title,index){
			console.log("title:"+title+" index:"+index);
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val(1);
//			if(searchBtnClickCount == "" || searchBtnClickCount == "0"){
//				if($('#selectCombo').combobox('getValue') == ""){
//					parent.$.messager.alert('','업무명을 선택해 주세요.');
//					createDayChart();
//					createWeekChart();
//					createMonthChart();
//					return false;
//				}else{
//					parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
//					createDayChart();
//					createWeekChart();
//					createMonthChart();
//					return false;
//				}
//			}			
			if(index == 0){
				var tab1ClickCount = $("#submit_form #tab1ClickCount").val();
				if(tab1ClickCount < searchBtnClickCount){
					createAppDailyChart();
					/* 거래 일별 */
					$('#dailyTradeList').datagrid("loadData", []);
					$('#dailyTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
					$('#dailyTradeList').datagrid('loading');	
					ajaxCall("/APPPerformance/APPDailyList", $("#submit_form"), "POST", callback_APPDailyListAddTable);
					$("#submit_form #tab1ClickCount").val(1);
				}
			}else if(index == 1){
				var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
				console.log("tab2ClickCount :"+tab2ClickCount);
				console.log("searchBtnClickCount :"+searchBtnClickCount);
				if(tab2ClickCount < searchBtnClickCount){
					console.log(tab2ClickCount+ "<"+searchBtnClickCount);
					//createAppTimeChart();
					createDbioDailyChart();
					/* SQL식별자(DBIO) 일별 */
					$('#dailyDbioList').datagrid("loadData", []);
					$('#dailyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
					$('#dailyDbioList').datagrid('loading');	
					ajaxCall("/APPPerformance/DBIODailyList", $("#submit_form"), "POST", callback_DBIODailyListAddTable);
					$("#submit_form #tab2ClickCount").val(1);
				}
			}else if(index == 2){
				var tab3ClickCount = $("#submit_form #tab3ClickCount").val();
				if(tab3ClickCount < searchBtnClickCount){
					/* SQL식별자(DBIO) 수행이력 */
					$('#historyDbioList').datagrid("loadData", []);
					$('#historyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
					$('#historyDbioList').datagrid('loading');		
					ajaxCall("/APPPerformance/DBIOHistoryList", $("#submit_form"), "POST", callback_DBIOHistoryListAddTable);				
					$("#submit_form #tab3ClickCount").val(1);
				}
			}else if(index == 3){
				var tab6ClickCount = $("#submit_form #tab6ClickCount").val();
				console.log("tab6ClickCount :"+tab6ClickCount);
				console.log("searchBtnClickCount :"+searchBtnClickCount);
				console.log("tab6ClickCount < searchBtnClickCount:"+(tab6ClickCount < searchBtnClickCount));
				
				if(tab6ClickCount < searchBtnClickCount){
					/* SQL TEXT */
					ajaxCall("/APPPerformance/GetSQLText", $("#submit_form"), "POST", callback_GetSQLTextAddTable);
					$("#submit_form #tab6ClickCount").val(1);
				}
			}
		}
	});
	
	$("#dbioTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val(1);
			if(index == 0){
				var tab4ClickCount = $("#submit_form #tab4ClickCount").val();
				console.log("tab4ClickCount :"+tab4ClickCount);
				console.log("searchBtnClickCount :"+searchBtnClickCount);
				if(tab4ClickCount < searchBtnClickCount){
					//createDbioDailyChart();
					createAppTimeChart();
					/* 거래 시간별 */
					$('#timeTradeList').datagrid("loadData", []);
					$('#timeTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
					$('#timeTradeList').datagrid('loading');		
					ajaxCall("/APPPerformance/APPTimeList", $("#submit_form"), "POST", callback_APPTimeListAddTable);
					$("#submit_form #tab4ClickCount").val(1);
				}
			}else if(index == 1){
				var tab5ClickCount = $("#submit_form #tab5ClickCount").val();
//				console.log("tab5ClickCount :"+tab2ClickCount);
//				console.log("searchBtnClickCount :"+searchBtnClickCount);
				if(tab5ClickCount < searchBtnClickCount){
					console.log(tab5ClickCount+ "<"+searchBtnClickCount);
					createDbioTimeChart();
					/* SQL식별자(DBIO) 시간별 */
					$('#timeDbioList').datagrid("loadData", []);
					$('#timeDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
					$('#timeDbioList').datagrid('loading');	
					ajaxCall("/APPPerformance/DBIOTimeList", $("#submit_form"), "POST", callback_DBIOTimeListAddTable);
					$("#submit_form #tab5ClickCount").val(1);
				}
			}
		}
	});
});

function Btn_OnClick(){
	if($('#wrkjob_cd').val() == ""){
		$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
//	if(($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
//		($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
//		parent.$.messager.alert('','조회 구분값을 입력해주세요.');
//		return false;
//	}	
	
	if($('#selectKey').combobox('getValue') == "" ){
			parent.$.messager.alert('','검색조건을 선택해 주세요.');
			return false;
	}
	if($('#searchValue').textbox('getValue') == ""){
		parent.$.messager.alert('','검색조건값을 입력해 주세요.');
		return false;
	}

	$("#call_from_parent").val("");
	
	fnSearch();
}

function fnSearch(){
	
	$('#dbioList').datagrid("loadData", []);
	$('#dailyTradeList').datagrid("loadData", []);
	$('#dailyDbioList').datagrid("loadData", []);
	$('#historyDbioList').datagrid("loadData", []);
	$('#timeTradeList').datagrid("loadData", []);
	$('#timeDbioList').datagrid("loadData", []);
	
	var appl_hash = $("#appl_hash").val();
	var sql_hash = $("#sql_hash").val();
	
	if(appl_hash != ""){
		var pp1 = $('#appTab').tabs('getSelected');
		var index1 = pp1.panel('options').index;
		if(index1 == 0){
			//createAppDailyChart();
			$("#submit_form #tab1ClickCount").val(1);
		}else if(index1 == 1){
			//createDbioDailyChart();
			$("#submit_form #tab2ClickCount").val(1);
		}else if(index1 == 2){
			$("#submit_form #tab3ClickCount").val(1);
		}
		
		var pp2 = $('#dbioTab').tabs('getSelected');
		var index2 = pp2.panel('options').index;
		if(index2 == 0){
			//createAppTimeChart();				
			$("#submit_form #tab4ClickCount").val(1);
		}else if(index2 == 1){
			$("#submit_form #tab5ClickCount").val(1);
			//createDbioTimeChart();
		}
	}
	
	$("#searchKey").val($('#selectKey').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	$('#reqBtn').linkbutton({disabled:false});
	
	$('#dbioList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("거래 성능 분석"," "); 

	ajaxCall("/APPPerformance", $("#submit_form"), "POST", callback_APPPerformanceAddTable);
}

//callback 함수
var callback_APPPerformanceAddTable = function(result) {
	json_string_callback_common(result,'#dbioList',true);	
};

function Btn_TuningRequest(){	
	rows = $('#dbioList').datagrid('getChecked');
	
	if(rows != null && rows != ""){
		var menuParam = "dbid=" + $("#dbid").val() + "&tr_cd="
				+ rows[0].appl_name + "&dbio=" + rows[0].dbio
				+ "&current_elap_time=" + rows[0].elapsed_time
				+ "&wrkjob_cd=" + rows[0].wrkjob_cd
				+ "&sql_hash=" + rows[0].sql_hash
				;

		parent.openLink("Y", "109", "애플리케이션 분석 :: 튜닝 요청", "/RequestImprovement", menuParam);
	}else{
		parent.$.messager.alert('','튜닝 요청할 데이터를 선택해 주세요.');
	}
}

function getSubTables(){
	createAppDailyChart();
//	createDbioDailyChart();
	createAppTimeChart();
//	createDbioTimeChart();
	
	/* 거래 일별 */
	$('#dailyTradeList').datagrid("loadData", []);
	$('#dailyTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#dailyTradeList').datagrid('loading');	
	ajaxCall("/APPPerformance/APPDailyList", $("#submit_form"), "POST", callback_APPDailyListAddTable);
	$("#submit_form #tab1ClickCount").val(1);
	
	/* 거래 시간별 */
	$('#timeTradeList').datagrid("loadData", []);
	$('#timeTradeList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#timeTradeList').datagrid('loading');		
	ajaxCall("/APPPerformance/APPTimeList", $("#submit_form"), "POST", callback_APPTimeListAddTable);
	$("#submit_form #tab4ClickCount").val(1);
	
//	/* SQL식별자(DBIO) 일별 */
//	$('#dailyDbioList').datagrid("loadData", []);
//	$('#dailyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#dailyDbioList').datagrid('loading');	
//	ajaxCall("/APPPerformance/DBIODailyList",
//			$("#submit_form"),
//			"POST",
//			callback_DBIODailyListAddTable);
//	$("#submit_form #tab2ClickCount").val(1);
	
//	/* SQL식별자(DBIO) 시간별 */
//	$('#timeDbioList').datagrid("loadData", []);
//	$('#timeDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#timeDbioList').datagrid('loading');	
//	ajaxCall("/APPPerformance/DBIOTimeList",
//			$("#submit_form"),
//			"POST",
//			callback_DBIOTimeListAddTable);
//	$("#submit_form #tab5ClickCount").val(1);
	
//	/* SQL식별자(DBIO) 수행이력 */
//	$('#historyDbioList').datagrid("loadData", []);
//	$('#historyDbioList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#historyDbioList').datagrid('loading');		
//	ajaxCall("/APPPerformance/DBIOHistoryList",
//			$("#submit_form"),
//			"POST",
//			callback_DBIOHistoryListAddTable);	
}

//callback 함수
var callback_APPDailyListAddTable = function(result) {
	json_string_callback_common(result,'#dailyTradeList',false);	

	$(".easyui-tabs:eq(0) td").css("cursor","default");
};

//callback 함수
var callback_APPTimeListAddTable = function(result) {
	json_string_callback_common(result,'#timeTradeList',false);	
	
	$(".easyui-tabs:eq(1) td").css("cursor","default");
};

//callback 함수
var callback_DBIODailyListAddTable = function(result) {
	json_string_callback_common(result,'#dailyDbioList',false);	
};

//callback 함수
var callback_DBIOTimeListAddTable = function(result) {
	json_string_callback_common(result,'#timeDbioList',false);	
};

//callback 함수
//SQL식별자(DBIO), SQL 수행이력
var callback_DBIOHistoryListAddTable = function(result) {
	json_string_callback_common(result,'#historyDbioList',false);	
};

var callback_GetSQLTextAddTable = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			$("#sqlTextArea").val(data.rows[0].sql_text);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	
};

function createAppDailyChart(){
	if(appDailyChart != "undefined" && appDailyChart != undefined){
		appDailyChart.destroy();
	}

	appDailyChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("appDailyChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 0 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : '5 5 0 5', // 차트 밖 여백	
			padding: '10 0 0 0',
			legend : {
				docked : 'right',
				cls : {
					fontSize : '9px'
				}
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				xField : 'log_dt',
				yField : 'executions',
				title : 'EXECUTIONS',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("executions") + " 건");
					}
				}
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'fail_cnt',
				title : 'FAIL_CNT',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("fail_cnt") + " 건");
					}
				}				
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'avg_elapsed_time',
				title : 'AVG_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("avg_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'min_elapsed_time',
				title : 'MIN_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("min_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'max_elapsed_time',
				title : 'MAX_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("max_elapsed_time") + " 건");
					}
				}				
			}]
		}]
	});
	
	ajaxCall("/APPPerformance/APPDailyList",
			$("#submit_form"),
			"POST",
			callback_APPDailyChartAction);
}

//callback 함수
var callback_APPDailyChartAction = function(result) {
	chart_callback(result, appDailyChart);
};

function createAppTimeChart(){
	if(appTimeChart != "undefined" && appTimeChart != undefined){
		appTimeChart.destroy();
	}

	appTimeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("appTimeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',				
			border : false,
			innerPadding : '15 15 0 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : '5 5 0 5', // 차트 밖 여백
			padding : '10 0 0 0',
			legend : {
				type: 'sprite',
				docked : 'right'
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				xField : 'log_dt',
				yField : 'executions',
				title : 'EXECUTIONS',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("executions") + " 건");
					}
				}
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'fail_cnt',
				title : 'FAIL_CNT',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("fail_cnt") + " 건");
					}
				}				
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'avg_elapsed_time',
				title : 'AVG_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("avg_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'min_elapsed_time',
				title : 'MIN_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("min_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'max_elapsed_time',
				title : 'MAX_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("max_elapsed_time") + " 건");
					}
				}				
			}]
		}]
	});
	
	ajaxCall("/APPPerformance/APPTimeList",
			$("#submit_form"),
			"POST",
			callback_APPTimeChartAction);
}

//callback 함수
var callback_APPTimeChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	store = appTimeChart.down("chart").getStore();
	store.loadData(data.rows);
	appTimeChart.down("chart").redraw();
};

function createDbioDailyChart(){
	if(dbioDailyChart != "undefined" && dbioDailyChart != undefined){
		dbioDailyChart.destroy();
	}

	dbioDailyChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dbioDailyChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '15 15 0 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : '5 5 0 5', // 차트 밖 여백
			padding : '10 0 0 0',
			legend : {
				type: 'sprite',
				docked : 'right'
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				xField : 'log_dt',
				yField : 'executions',
				title : 'EXECUTIONS',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("executions") + " 건");
					}
				}
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'avg_elapsed_time',
				title : 'AVG_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("avg_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				xField : 'log_dt',
				yField : 'min_elapsed_time',
				title : 'MIN_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("min_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'max_elapsed_time',
				title : 'MAX_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("max_elapsed_time") + " 건");
					}
				}				
			}]
		}]
	});
	
	ajaxCall("/APPPerformance/DBIODailyList",
			$("#submit_form"),
			"POST",
			callback_DBIODailyChartAction);
}

//callback 함수
var callback_DBIODailyChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	console.log("callback_DBIODailyChartAction data.rows:",data.rows);
	store = dbioDailyChart.down("chart").getStore();
	store.loadData(data.rows);
	dbioDailyChart.down("chart").redraw();
};

function createDbioTimeChart(){
	if(dbioTimeChart != "undefined" && dbioTimeChart != undefined){
		dbioTimeChart.destroy();
	}

	dbioTimeChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById("dbioTimeChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			innerPadding : '15 15 0 15', // 차트안쪽 여백[top, right, bottom, left]
			insetPadding : '5 5 0 5', // 차트 밖 여백
			padding: '10 0 0 0',
			legend : {
				type: 'sprite',
				docked : 'right'
			},
			store : {
				data : []
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				title : '(건)',
				minimum : 0,
				minorTickSteps: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    }
			},{
				type : 'category',
				position : 'bottom'
			}],
			series : [{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'executions',
				title : 'EXECUTIONS',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("executions") + " 건");
					}
				}
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'avg_elapsed_time',
				title : 'AVG_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("avg_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'min_elapsed_time',
				title : 'MIN_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("min_elapsed_time") + " 건");
					}
				}				
			},{
				type : 'line',
				smooth : true,
				xField : 'log_dt',
				yField : 'max_elapsed_time',
				title : 'MAX_ELAP',
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record){
						tooltip.setHtml(record.get("log_dt")+" : " + record.get("max_elapsed_time") + " 건");
					}
				}				
			}]
		}]
	});
	
	ajaxCall("/APPPerformance/DBIOTimeList",
			$("#submit_form"),
			"POST",
			callback_DBIOTimeChartAction);
}

//callback 함수
var callback_DBIOTimeChartAction = function(result) {
	var store;
	var data = JSON.parse(result);
	console.log("callback_DBIOTimeChartAction data.rows:",data.rows);
	store = dbioTimeChart.down("chart").getStore();
	store.loadData(data.rows);
	dbioTimeChart.down("chart").redraw();
};

function Btn_SetSQLFormatter(){
	$('#sqlTextArea').format({method: 'sql'});
}
