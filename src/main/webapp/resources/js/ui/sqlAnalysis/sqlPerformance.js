var currentPagePerCount = 10;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var clipboard = new Clipboard('#sqlCopyBtn');
	clipboard.on('success', function(e) {
		parent.$.messager.alert('','복사되었습니다.');
	});
	
	// Database 조회
	$('#selectDbidCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#selectDbidCombo").combobox("textbox").attr("placeholder","선택");
		}
	});	
	
	$('#selectDbidCombo').combobox("setValue",$("#dbid").val());
	
	$("#tabsDiv").tabs({
		plain:true,
		onSelect:function(title, index){
			Btn_SearchTabLink(index);
		}
	});
	
	$("#tableTab").tabs({
		plain:true,
		onSelect:function(title, index){
			Btn_SearchTableLink(index);
		}
	});
	
	$('#treePlan').tree({
		animate:true,
		lines:true
	});
	
	$("#tableList").datagrid({
		view: myview,
		rownumbers:true,
		columns:[[
			{field:'snap_id',title:'SNAP_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'begin_interval_time',title:'BEGIN_INTERVAL_TIME',halign:"center",align:"center",sortable:"true",styler:cellStyler},			
			{field:'instance_number',title:'INST_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'parsing_schema_name',title:'PARSING_SCHEMA_NAME',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'module',title:'MODULE',halign:"center",align:'left',sortable:"true",styler:cellStyler},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'optimizer_env_hash_value',title:'OPTIMIZER_ENV_HASH_VALUE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#tablePlanList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,		
		columns:[[
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',sortable:"true",styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getNumberFormat},			
			{field:'clwait_time',title:'CLWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'iowait_time',title:'IOWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'apwait_time',title:'APWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_time',title:'CCWAIT_TIME',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'cpu_rate',title:'CPU_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'clwait_rate',title:'CLWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'iowait_rate',title:'IOWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'apwait_rate',title:'APWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler},
			{field:'ccwait_rate',title:'CCWAIT_RATE',halign:"center",align:'right',sortable:"true",styler:cellStyler}
		]],
		onSelect:function(index, value){
			$('#sqlStats_sql_id').val(value.sql_id); 
			$('#sqlStats_plan_hash_value').val(value.plan_hash_value); 
			$('#sqlStats_elapsed_time').val(value.elapsed_time); 
			$('#sqlStats_buffer_gets').val(value.buffer_gets);
		},
		fitColumns:true,
		onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#outLineList").datagrid({
		view: myview,
		columns:[[
			{field:'hint',title:'OUTLINE',width:"130%",halign:"center",align:'left'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});

	$("#bindValueList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
//		rownumbers:true,
		columns:[[
			{field:'snap_id',title:'SNAP_ID',halign:"center",align:'center',sortable:"true"},
			{field:'exec_time',title:'EXECUTE_TIME',halign:"center",align:'center',sortable:"true"},	
			{field:'name',title:'NAME',halign:"center",align:'center'},
			{field:'value',title:'VALUE',halign:"center",align:'left'},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'right'},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right'},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right'},			
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right'},
			{field:'exec_inst_id',title:'INST_ID',halign:"center",align:'right'}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#bindValueList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable td").attr({ "style": "border:0px" });
	
	$("#similarityList").datagrid({
		view: myview,
		columns:[[
			{field:'sql_id',title:'SQL_ID',halign:"center",align:'right',sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',sortable:"true"},
			{field:'executions',title:'EXECUTIONS',halign:"center",align:'right',sortable:"true"},
			{field:'disk_reads',title:'DISK_READS',halign:"center",align:'right',sortable:"true"},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',sortable:"true"},			
			{field:'max_elapsed_time',title:'MAX_ELAPSED_TIME',halign:"center",align:'right',sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:'right',sortable:"true"}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#sqlGridPlanList").treegrid({
		idField:'id',
		treeField:'operation',		
		lines: true,
		columns:[[
			{field:'id',title:'ID',halign:"center",align:"center",sortable:"true"},
			{field:'imid',title:'IMID',halign:"center",align:'right',sortable:"true"},
			{field:'operation',title:'OPERATION',halign:"center",align:'left',sortable:"true"},
			{field:'object_node',title:'OBJECT_NODE',halign:"center",align:'right',sortable:"true"},
			{field:'object',title:'OBJECT#',halign:"center",align:'right',sortable:"true"},
			{field:'object_owner',title:'OBJECT_OWNER',halign:"center",align:'center',sortable:"true"},
			{field:'object_name',title:'OBJECT_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'object_type',title:'OBJECT_TYPE',halign:"center",align:'left',sortable:"true"},
			{field:'optimizer',title:'OPTIMIZER',halign:"center",align:'left',sortable:"true"},
			{field:'cost',title:'COST',halign:"center",align:'right',sortable:"true"},
			{field:'cardinality',title:'CADINALITY',halign:"center",align:'right',sortable:"true"},
			{field:'bytes',title:'BYTES',halign:"center",align:'right',sortable:"true"},
			{field:'other_tag',title:'OTHER_TAR',halign:"center",align:'right',sortable:"true"},
			{field:'partition_start',title:'PARTITION_START',halign:"center",align:'right',sortable:"true"},
			{field:'partition_stop',title:'PARTITION_STOP',halign:"center",align:'right',sortable:"true"},
			{field:'cpu_cost',title:'CPU_COST',halign:"center",align:'right',sortable:"true"},
			{field:'io_cost',title:'IO_COST',halign:"center",align:'right',sortable:"true"},
			{field:'access_predicates',title:'ACCESS_PREDICATES',halign:"center",align:'right',sortable:"true"},
			{field:'filter_predicates',title:'FILTER_PREDICATES',halign:"center",align:'right',sortable:"true"},
			{field:'projection',title:'PROJECTION',halign:"center",align:'right',sortable:"true"},
			{field:'time',title:'TIME',halign:"center",align:'right',sortable:"true"},
			{field:'qblock_name',title:'QBLOCK_NAME',halign:"center",align:'left',sortable:"true"},
			{field:'timestamp',title:'TIMESTAMP',halign:"center",align:'center',sortable:"true"}
		]],
		fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#bindBtn").linkbutton({disabled:true});
	$("#historyBtn").linkbutton({disabled:true});
	
	if($("#dbid").val() != ""){
		Btn_OnClick();	
	}
	
	$('#sql_id').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick();
				}				
			}
		})
	});
	
	//pagingBtn1
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
			fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled").click(function(){
			fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();

	
/*
	//pagingBtn2
	//이전, 다음 처리
	$("#pagingBtn2").hide();
	$("#prevBtnEnabled2").click(function(){
		fnGoPrevOrNext("PREV");
	});
	$("#nextBtnEnabled2").click(function(){
		fnGoPrevOrNext("NEXT");
	});
	
	$("#prevBtnEnabled2").hide();
	$("#nextBtnEnabled2").hide();
	*/
	
});

/*function Btn_OnClick(){	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();	

	 iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();

	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	$('#reqBtn').linkbutton({disabled:false});
	
	$("#textArea").html("");	
	$("#currentPage").val("1");
	$("#bindPage").val("1");
	$("#historyPage").val("1");

	 modal progress open 
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능분석"," "); 	
	
	 상단 텍스트 박스 
	ajaxCall("/SQLPerformance/SQLText",
			$("#submit_form"),
			"POST",
			callback_SQLTextAction);
	
	$('#tableList').datagrid('loadData',[]);

	 AWR SQL Stats 
	ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTable);
	$("#submit_form #tab7ClickCount").val(1);
	
	 Bind Value 
	$('#bindValueList').datagrid('loadData',[]);
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	ajaxCall("/SQLPerformance/BindValueList", $("#submit_form"), "POST", callback_BindValueListAction);
	$("#submit_form #tab1ClickCount").val(1);

}*/

function Btn_OnPlanViewClick(strIndex){
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	
	$('#tabsDiv').tabs('select', parseInt(strIndex));
	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}
	
	if($('#plan_hash_value').textbox('getValue') == ""){
		parent.$.messager.alert('','PLAN HASH VALUE를  입력해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));

	if(strIndex == "3"){	
		var tab4ClickCount = $("#submit_form #tab4ClickCount").val();
		if(tab4ClickCount < searchBtnClickCount){
			$("#submit_form #tab4ClickCount").val(1);
			/* Tree Plan */
			ajaxCall("/SQLPerformance/SQLPerformTreePlanList", $("#submit_form"), "POST", callback_SQLPerformTreePlanListAction);
		}	
	}else if(strIndex == "4"){		
		var tab5ClickCount = $("#submit_form #tab5ClickCount").val();
		if(tab5ClickCount < searchBtnClickCount){
			$("#submit_form #tab5ClickCount").val(1);
			/* Text Plan */
			ajaxCall("/SQLPerformance/SQLPerformTextPlanList", $("#submit_form"), "POST", callback_SQLPerformTextPlanListAction);	
		}	
	}else if(strIndex == "5"){	
		var tab6ClickCount = $("#submit_form #tab6ClickCount").val();
		if(tab6ClickCount < searchBtnClickCount){
			$("#submit_form #tab6ClickCount").val(1);
			/* Grid Plan */
			$('#sqlGridPlanList').treegrid('loadData',[]);
			ajaxCall("/SQLPerformance/SQLPerformGridPlanList", $("#submit_form"), "POST", callback_SQLPerformGridPlanListAction);	
		}	
	}	
}

//callback 함수
var callback_SQLTextAction = function(result) {
	if(result.result){
		var post = result.object;
		//$("#textArea").html(strReplace(post.sql_text,'\n','<br/>'));
		if(post != null){
			$("#textArea").val(post.sql_text);
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
};

//callback 함수
var callback_SQLPerformTreePlanListAction = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#treePlan').tree("loadData", data);
	}
	$("#treePlan .tree-node" ).css( "cursor", "default" );
}

//callback 함수
var callback_SQLPerformGridPlanListAction = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#sqlGridPlanList').treegrid("loadData", data);
		$('#sqlGridPlanList').treegrid('loaded');
		$("#sqlGridPlanList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#sqlGridPlanList div.datagrid-cell.tree-node" ).css( "cursor", "default !important" );
	}
}

/*//callback 함수
var callback_BindValueListAction = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{

		$("#bindValueList").datagrid({rownumbers:true});
		$('#bindValueList').datagrid("loadData", data);
		$('#bindValueList').datagrid('loaded');
		$("#bindValueList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#bindValueIsNull").val("Y");  //bindValueList 에 데이터가 없을경우	
		if(data.totalCount > 0){
			$("#bindValueIsNull").val("N"); //bindValueList 에 데이터가 있을경우	
			$("#bindBtn").linkbutton({disabled:false});	
		}
	}
}
*/
//callback 함수  //임시
var callback_BindValueListAction = function(result) {
	var data = null;
	try{
		data = JSON.parse(result);
	
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
	
			$("#bindValueList").datagrid({rownumbers:true});
			$('#bindValueList').datagrid("loadData", data);
			$('#bindValueList').datagrid('loaded');
			$("#bindValueList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
			$("#bindValueIsNull").val("Y");  //bindValueList 에 데이터가 없을경우	
			if(data.totalCount > 0){
				$("#bindValueIsNull").val("N"); //bindValueList 에 데이터가 있을경우	
				$("#bindBtn").linkbutton({disabled:false});	
			}
		}
	
	}catch(data){
		$('#bindValueList').datagrid('loaded');
	}
}


function Btn_SearchTabLink(strIndex){
	$('#tabsDiv').tabs('select', parseInt(strIndex));
	
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	
	if($("#dbid").val() != ""){
		if(strIndex == "0"){
			var tab1ClickCount = $("#submit_form #tab1ClickCount").val();
			if(tab1ClickCount < searchBtnClickCount){
				$("#submit_form #tab1ClickCount").val(1);
				/* Bind Value */
				$('#bindValueList').datagrid('loadData',[]);
				$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
				$('#bindValueList').datagrid('loading');
				
				ajaxCall("/SQLPerformance/BindValueList", $("#submit_form"), "POST", callback_BindValueListAction);	
			}
		}else if(strIndex == "1"){	
			var tab2ClickCount = $("#submit_form #tab2ClickCount").val();
			if(tab2ClickCount < searchBtnClickCount){
				$("#submit_form #tab2ClickCount").val(1);
				/* OutLine */
				$('#outLineList').datagrid('loadData',[]);
				$('#outLineList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
				$('#outLineList').datagrid('loading');		
				
				ajaxCall("/SQLPerformance/OutLineList", $("#submit_form"), "POST", callback_OutLineListAction);
			}
		}else if(strIndex == "2"){	
			var tab3ClickCount = $("#submit_form #tab3ClickCount").val();
			if(tab3ClickCount < searchBtnClickCount){
				$("#submit_form #tab3ClickCount").val(1);
				/* 유사SQL */
				$('#similarityList').datagrid('loadData',[]);
				$('#similarityList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
				$('#similarityList').datagrid('loading');	
				
				ajaxCall("/SQLPerformance/SimilaritySqlList", $("#submit_form"), "POST", callback_SimilaritySqlListAction);	
			}
		}else{
			Btn_OnPlanViewClick(strIndex);
		}
	}
}

//callback 함수
var callback_SQLPerformTextPlanListAction = function(result) {
	var strHtml = "";
	$("#textPlan li").remove();
	
	if(result.result){
		strHtml += "<li><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];		
			strHtml += "<li>"+strReplace(post.execution_plan,' ','&nbsp;')+"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#textPlan").append(strHtml);
	}else{
		parent.$.messager.alert('',result.message);
	}
}

//callback 함수
var callback_OutLineListAction = function(result) {
	json_string_callback_common(result,'#outLineList',false);
	$("#outLineList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
}

//callback 함수
var callback_SimilaritySqlListAction = function(result) {
	json_string_callback_common(result,'#similarityList',false);
	$("#similarityList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
}

function Btn_SearchTableLink(strIndex){
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	$('#tableTab').tabs('select', parseInt(strIndex));

	if($("#dbid").val() != ""){
		if(strIndex == "0"){
			$("#pagingBtn1").show(); //페이징버튼 보임
			var tab7ClickCount = $("#submit_form #tab7ClickCount").val();
			if(tab7ClickCount < searchBtnClickCount){
				/* 하단 그리드 */
				/* modal progress open */
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능분석 - AWR SQL Stats"," "); 			
				$('#tableList').datagrid('loadData',[]);
				$("#submit_form #tab7ClickCount").val(1);
				ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTable);
			}
		}else if(strIndex == "1"){
			$("#pagingBtn1").hide(); //페이징버튼 안보임
			
			var tab8ClickCount = $("#submit_form #tab8ClickCount").val();
			if(tab8ClickCount < searchBtnClickCount){
				/* 하단 그리드 */
				/* modal progress open */
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능분석 - PLAN별 SQL Stats"," "); 		
				$('#tablePlanList').datagrid('loadData',[]);
				$("#submit_form #tab8ClickCount").val(1);
				ajaxCall("/SQLPerformance/SQLPerformPlanHistory", $("#submit_form"), "POST", callback_SQLPerformPlanHistoryAddTable);
			}
		}else if(strIndex == "2"){
			$("#pagingBtn1").hide(); //페이징버튼 안보임
			var tab9ClickCount = $("#submit_form #tab9ClickCount").val();
			if(tab9ClickCount < searchBtnClickCount){
				/* modal progress open */
				if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능분석 - AWR Execution Plan"," "); 		
				$("#submit_form #tab9ClickCount").val(1);
				ajaxCall("/SQLPerformance/AWRExecutionPlan", $("#submit_form"), "POST", callback_AWRExecutionPlanAddTable);
			}
		}
	}
}

/*//callback 함수
var callback_SQLPerformHistoryAddTable = function(result) {
	var data = JSON.parse(result);

//	var dataLength = JSON.parse(result).dataCount4NextBtn;
//	fnEnableDisablePagingBtn(dataLength); //페이징추가
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').datagrid("loadData", data);
		$("#tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#historyBtn").linkbutton({disabled:false});
		

		
	}
	 modal progress close 
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();			
};*/

//callback 함수
var callback_SQLPerformPlanHistoryAddTable = function(result) {
	json_string_callback_common(result, '#tablePlanList', true);
	$("#tablePlanList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_AWRExecutionPlanAddTable = function(result) {
	var strHtml = "";
	$("#awrExecutionPlanDiv").html("");
	
	if(result.result){
		for(var i = 0 ; i < result.stringList.length ; i++){
			var value = result.stringList[i];		
			strHtml += strReplace(value,' ','&nbsp;') + "<br/>";
		}
		$("#awrExecutionPlanDiv").append(strHtml);
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}

function Btn_NextBindSearch(){
	var currentPage = $("#bindPage").val();
	currentPage++;
	
	$("#bindPage").val(currentPage);	
	$("#currentPage").val(currentPage);	
	
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');	
	
	ajaxCall("/SQLPerformance/BindValueListNext",
		$("#submit_form"),
		"POST",
		callback_BindValueListAddTableNext);	
}

//callback 함수
var callback_BindValueListAddTableNext = function(result) {
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			$('#bindValueList').datagrid('appendRow',{
				snap_id         : post.snap_id,
				name            : post.name,
				value           : post.value,
				elapsed_time    : parseFloat(post.elapsed_time),
				buffer_gets     : parseFloat(post.buffer_gets),
				rows_processed  : parseFloat(post.rows_processed),
				plan_hash_value : post.plan_hash_value,
				exec_time       : post.exec_time,
				exec_inst_id    : post.exec_inst_id
			});			
		}
	}else{
		$.messager.alert('','더 이상 검색된 데이터가 없습니다.');
		$("#bindBtn").linkbutton({disabled:true});
	}
	
	$('#bindValueList').datagrid('loaded');
	$('#bindValueList').datagrid('reload');
};

function Btn_NextHistorySearch(){
	var currentPage = $("#historyPage").val();
	currentPage++;
	
	$("#historyPage").val(currentPage);	
	$("#currentPage").val(currentPage);
	
	$('#tableList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#tableList').datagrid('loading');	
	
	ajaxCall("/SQLPerformance/SQLPerformHistoryNext",
		$("#submit_form"),
		"POST",
		callback_SQLPerformHistoryAddTableNext);	
}

//callback 함수
var callback_SQLPerformHistoryAddTableNext = function(result) {
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			$('#tableList').datagrid('appendRow',{
				begin_interval_time      : post.begin_interval_time,
				snap_id                  : post.snap_id,
				instance_number          : post.instance_number,
				sql_id                   : post.sql_id,
				plan_hash_value          : post.plan_hash_value,
				module                   : post.module,
				parsing_schema_name      : post.parsing_schema_name,
				elapsed_time             : parseFloat(post.elapsed_time),
				cpu_time                 : parseFloat(post.cpu_time),
				buffer_gets              : parseFloat(post.buffer_gets),
				disk_reads               : parseFloat(post.disk_reads),
				rows_processed           : parseFloat(post.rows_processed),
				clwait_time              : parseFloat(post.clwait_time),
				iowait_time              : parseFloat(post.iowait_time),
				apwait_time              : parseFloat(post.apwait_time),
				ccwait_time              : parseFloat(post.ccwait_time),
				cpu_rate                 : parseFloat(post.cpu_rate),
				clwait_rate              : parseFloat(post.clwait_rate),
				iowait_rate              : parseFloat(post.iowait_rate),
				apwait_rate              : parseFloat(post.apwait_rate),
				ccwait_rate              : parseFloat(post.ccwait_rate),
				executions               : post.executions,
				parse_calls              : post.parse_calls,
				fetches                  : post.fetches,
				optimizer_env_hash_value : post.optimizer_env_hash_value
			});			
		}
	}else{
		$.messager.alert('','더 이상 검색된 데이터가 없습니다.');
		$("#historyBtn").linkbutton({disabled:true});
	}
	
	$('#tableList').datagrid('loaded');
	$('#tableList').datagrid('reload');
};

//function Show_SqlProfilePopup(){
//	var profileText = "SPOP_SQLPROFILE_";	
//	var rows = $('#tablePlanList').datagrid('getChecked');
//	var sqlId = "";
//	var planHashValue = "";
//
//	if(rows.length > 0){
//		if(rows.length > 1){
//			parent.$.messager.alert('','SQL Profile 적용할 하나의 Row만 선택해 주세요.');
//		}else{
//			parent.$("#profile_form #dbid").val($('#dbid').val());
//			
//			sqlId = rows[0].sql_id;
//			
//			planHashValue = rows[0].plan_hash_value;
//			parent.$("#profile_form #plan_hash_value").val(planHashValue);
//
//			parent.$("#profile_form #sqlId").textbox("setValue",sqlId);
//			parent.$("#profile_form #sql_id").val(sqlId);	
//
//			parent.$("#profile_form #planHashValue").textbox("setValue",planHashValue);
//			parent.$("#profile_form #save_yn").val("N");
//
//			profileText += sqlId;
//			
//			parent.$("#profile_form #sqlProfile").textbox("setValue",profileText);			
//			parent.$('#sqlProfilePop').window("open");	
//		}
//	}else{
//		parent.$.messager.alert('','SQL Profile 적용할 Row를 선택해 주세요.');
//	}
//}
function Show_SqlProfilePopup(){
	var profileText = "SPOP_SQLPROFILE_";	
	var rows = $('#tablePlanList').datagrid('getChecked');
	var sqlId = "";
	var planHashValue = "";

	if(rows.length > 0){
		if(rows.length > 1){
			parent.$.messager.alert('','SQL Profile 적용할 하나의 Row만 선택해 주세요.');
		}else{
//			parent.$("#profile_form #dbid").val($('#dbid').val());
			$("#profile_form #dbid").val($('#dbid').val());
			
			sqlId = rows[0].sql_id;
			
			planHashValue = rows[0].plan_hash_value;
			$("#profile_form #plan_hash_value").val(planHashValue);

			$("#profile_form #sqlId").textbox("setValue",sqlId);
			$("#profile_form #sql_id").val(sqlId);	

			$("#profile_form #planHashValue").textbox("setValue",planHashValue);
			$("#profile_form #save_yn").val("N");

			profileText += sqlId;
			
			$("#profile_form #sqlProfile").textbox("setValue",profileText);			
			$('#sqlProfilePop').window("open");	
		}
	}else{
		parent.$.messager.alert('','SQL Profile 적용할 Row를 선택해 주세요.');
	}
}


function Excel_DownClick(){	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}
	
	$("#currentPage").val("0");	
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/SQLPerformance/SQLPerformHistory/ExcelDown");
	$("#submit_form").submit();
}

function Excel_PlanDownClick(){	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	
	$("#submit_form").attr("action","/SQLPerformance/SQLPerformPlanHistory/ExcelDown");
	$("#submit_form").submit();
}

function Btn_TuningRequest(){	
	rows = $('#tablePlanList').datagrid('getChecked');
	if(rows != null && rows != ""){
		var menuParam = "dbid="+$("#dbid").val()+
		"&sql_id="+rows[0].sql_id+
		"&plan_hash_value="+rows[0].plan_hash_value+
		"&current_elap_time="+rows[0].elapsed_time+
		"&forecast_result_cnt="+rows[0].rows_processed+
		"&call_from_sqlPerformance=Y"+
		"&bindValueIsNull="+$("#bindValueIsNull").val()+
		"&sqlStats_sql_id="+$("#sqlStats_sql_id").val()+
		"&sqlStats_plan_hash_value="+$("#sqlStats_plan_hash_value").val()+
		"&sqlStats_elapsed_time="+$("#sqlStats_elapsed_time").val()+
		"&sqlStats_buffer_gets="+$("#sqlStats_buffer_gets").val();
		;
		
		parent.openLink("Y", "109", "SQL 성능 분석 :: 튜닝 요청", "/RequestImprovement/FromSqlPerformance", menuParam);
	}else{
		parent.$.messager.alert('','튜닝 요청할 데이터를 선택해 주세요.');
	}
}

function Btn_GoSQLAdvisor(strGb){
	var menuId = $("#menu_id").val() + strGb;
	var menuNm = "";
	var strUrl = "";
	var menuParam = "";
	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}	

	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	menuParam = "dbid="+$("#dbid").val()+"&sql_id="+$("#sql_id").textbox("getValue")+"&call_from_parent=Y";
	
	if(strGb == "1"){
		menuNm = "SQL Tuning Advisor";
		strUrl = "/SQLPerformance/SQLTuningAdvisor";
	}else if(strGb == "2"){
		menuNm = "SQL Access Advisor";
		strUrl = "/SQLPerformance/SQLAccessAdvisor";		
	}else if(strGb == "3"){
		menuNm = "SQL Monitor";
		strUrl = "/SQLPerformance/SQLMonitor";		
	}
	
	parent.openLink("Y", menuId, menuNm, strUrl, menuParam);
}

function cellStyler(value,row,index){
	var executions = row.executions;
	if(executions == "0"){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';		
	}
}















/*페이징처리시작*/
function fnSetCurrentPage(direction){
	var currentPage = $("#submit_form #currentPage").val();
	
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}

	Btn_OnClick('P');
}

/*function Btn_OnClick(val){

	if(!formValidationCheck()){
		return false;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
	}
	fnSearch();
	$("#call_from_parent").val("N");
	$("#call_from_child").val("N");
}*/


function Btn_OnClick(val){
	
	if($('#selectDbidCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를  입력해 주세요.');
		return false;
	}
	$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
	
	if(val != 'P'){ //페이징으로 검색 하지 않는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val(currentPagePerCount);
		
		/* AWR SQL Stats */
		fnSearch();
		
		fnUpdateSearchBtnClickFlag();	
	
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
	
		$("#dbid").val($('#selectDbidCombo').combobox('getValue'));
		$('#reqBtn').linkbutton({disabled:false});
		
		$("#textArea").html("");	
		$("#currentPage").val("1");
		$("#bindPage").val("1");
		$("#historyPage").val("1");
	
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능분석"," "); 	
		
		/* 상단 텍스트 박스 */
		ajaxCall("/SQLPerformance/SQLText",
				$("#submit_form"),
				"POST",
				callback_SQLTextAction);
		
		$('#tableList').datagrid('loadData',[]);
	
		
		/* Bind Value */
		$('#bindValueList').datagrid('loadData',[]);
		$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
		$('#bindValueList').datagrid('loading');
		
		ajaxCall("/SQLPerformance/BindValueList", $("#submit_form"), "POST", callback_BindValueListAction);
		$("#submit_form #tab1ClickCount").val(1);
	}else{
		
		/* AWR SQL Stats */
		fnSearch();
	/*	ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTable);
		$("#submit_form #tab7ClickCount").val(1);*/
	}


}

function Btn_CopyBindValue(){

	var data = $("#bindValueList").datagrid('getChecked');

    var bind_value = "";
	console.log(data);
	
	if(data.length == 0){
		  $.messager.alert('','복사할 바인드값이 없습니다.');
		  return false;
	}
		
	for(var i = 0; i < data.length; i++){
		console.log(data[i].value);
		bind_value += data[i].value + "\n";
	}
	
	//바인드값을 복사한다.
	  var dummy = document.createElement("textarea");
	  document.body.appendChild(dummy);
	  dummy.value = bind_value;
	  dummy.select();
	  document.execCommand("copy");
	  document.body.removeChild(dummy);
	
	  $.messager.alert('','바인드값이 복사되었습니다. <BR/> \'Ctrl + V\' 로 붙여넣기 하세요.');

}



function fnSearch(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능 분석"," ");
	ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTable);
	
//	ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTableForPaging);
	$("#submit_form #tab7ClickCount").val(1);

}
function fnSearch_paging(){
	$('#submit_form #tableList').datagrid("loadData", []);
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 성능 분석"," ");
	ajaxCall("/SQLPerformance/SQLPerformHistory", $("#submit_form"), "POST", callback_SQLPerformHistoryAddTableForPaging);
	$("#submit_form #tab7ClickCount").val(1);

}

//callback 함수
var callback_SQLPerformHistoryAddTableForPaging = function(result) {
	json_string_callback_common(result,'#tableList',true);
	fnControlPaging(result);  //페이버튼세팅

};
//callback 함수
var callback_SQLPerformHistoryAddTable = function(result) {
	var data = JSON.parse(result);

//	var dataLength = JSON.parse(result).dataCount4NextBtn;
//	fnEnableDisablePagingBtn(dataLength); //페이징추가
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').datagrid("loadData", data);
		$("#tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#historyBtn").linkbutton({disabled:false});
		
//		json_string_callback_common(result,'#tableList',true);
		fnControlPaging(result);  //페이버튼세팅

		
	}
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();			
};

var fnControlPaging = function(result) {
	
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);

	var data;
	var dataLength=0;
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	//페이지를 보여줄지말지 여부를 결정
	if(currentPage > 1){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if(dataLength > 10){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else{
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	if(currentPage == 1){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		if(dataLength > pagePerCount){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}
};
