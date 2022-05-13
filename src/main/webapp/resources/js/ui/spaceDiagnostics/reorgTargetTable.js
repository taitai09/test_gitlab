var call_from_parent;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onChange: function(newValue,oldValue) {
			fnInitSearchBtnClickFlag();
		},
		onLoadSuccess: function(items) {
			$("#selectCombo").combobox('textbox').attr("placeholder","선택");
			
			if (items.length){
				$(this).combobox('setValue', $("#dbid").val());
				
				call_from_parent = $("#call_from_parent").val();
				if(call_from_parent == "Y"){
					Btn_OnClick();
				}
				
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#oracleTableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#recommend_seq").val(row.recommend_seq);
			$("#base_day").val(row.base_day);
			
			showRecommendaion('1');
		},
		columns:[[
			{field:'base_day',title:'분석일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'recommend_seq',title:'NO',halign:"center",align:'center',sortable:"true"},
			{field:'segment_owner',title:'OWNER',halign:"center",align:"center",sortable:"true"},
			{field:'segment_name',title:'SEGMENT',halign:"center",align:'left',sortable:"true"},
			{field:'segment_type',title:'SEGMENT TYPE',halign:"center",align:'center',sortable:"true"},
			{field:'partition_name',title:'PARTITION NAME',halign:"center",align:'left',sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE',halign:"center",align:'center',sortable:"true"},
			{field:'allocated_space',title:'ALLOCATED SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space',title:'USED SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space',title:'RECLAIMABLE SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'chain_rowexcess',title:'CHAIN RATIO(%)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	/*$("#openpopTableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#gather_day").val(row.gather_day);
			$("#owner").val(row.owner);
			$("#table_name").val(row.table_name);
			
			showRecommendaion('2');
		},
		columns:[[
			{field:'gather_day',title:'분석일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'owner',title:'OWNER',halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE_NAME',halign:"center",align:'center',sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'allocated_space',title:'ALLOCATED_SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space',title:'USED_SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space',title:'RECLAIMABLE_SPACE(MB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space_ratio',title:'RECLAIMABLE_SPACE_RATIO(%)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});*/
	
	$("#recommendDiv").hide();
		
	/*$("#recommendTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			if($('#selectCombo').combobox('getValue') == ""){
				parent.$.messager.alert('','DB를 선택해 주세요.');
				return false;
			}else{
				if(searchBtnClickCount != 1){
					parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
					return false;
				}
			}
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			
			if(tabClickCount < searchBtnClickCount){
				if(index == 0){
					$("#submit_form #tab1ClickCount").val(1);
					ajaxCall("/ReorgTargetTable/Oracle", $("#submit_form"), "POST", callback_ReorgTargetTableOracleAddTable);
				}else if(index == 1){
					$("#submit_form #tab2ClickCount").val(1);
					ajaxCall("/ReorgTargetTable/OpenPOP", $("#submit_form"), "POST", callback_ReorgTargetTableOpenPOPAddTable);	
				}
			}
			$(".detailT tbody").html("");
			$("#recommendDiv").hide();
		}
	});
	*/
});

function Btn_OnClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();
	//select Open-POP Recommendation tab
	/*if(call_from_parent == "Y"){
		$('#recommendTab').tabs('select', 1);
	}*/
	
	/*var pp = $('#recommendTab').tabs('getSelected');
	var index = pp.panel('options').index;*/

	fnUpdateSearchBtnClickFlag();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#strGb").val("");
	$("#recommendDiv").hide();
	$('#oracleTableList').datagrid("loadData", []);
	/*$('#openpopTableList').datagrid("loadData", []);*/
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("Reorg 대상 테이블"," "); 	

/*	if(index == 0){*/
		$("#submit_form #tab1ClickCount").val(1);
		ajaxCall("/ReorgTargetTable/Oracle", $("#submit_form"), "POST", callback_ReorgTargetTableOracleAddTable);
	/*}else if(index == 1){
		$("#submit_form #tab2ClickCount").val(1);
		ajaxCall("/ReorgTargetTable/OpenPOP", $("#submit_form"), "POST", callback_ReorgTargetTableOpenPOPAddTable);	
	}*/
	
}

//callback 함수
var callback_ReorgTargetTableOracleAddTable = function(result) {
	json_string_callback_common(result,'#oracleTableList',true);
};

/*//callback 함수
var callback_ReorgTargetTableOpenPOPAddTable = function(result) {
	json_string_callback_common(result,'#openpopTableList',true);
};*/

function showRecommendaion(strGb){
	var url = "";
	$("#strGb").val(strGb);
	
	if(strGb == "1"){
		url = "/ReorgTargetTable/OracleRecommendation";
	}else{
		url = "/ReorgTargetTable/OpenPOPRecommendation";
	}
	
	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_RecommendationAction);	
}

//callback 함수
var callback_RecommendationAction = function(result) {
	var strHtml = "";
	$("#recommendTbl tbody tr").remove();
	
	if(result.result){
		var post = result.object;
		
		if($("#strGb").val() == "1"){
			strHtml += "<tr><th>Recommendations</th><td>" + post.recommendations + "<td/></tr>";	
		}
		
		strHtml += "<tr><th>Run First</th><td>" + post.run_first + "<td/></tr>";
		strHtml += "<tr><th>Run Second</th><td>" + post.run_second + "<td/></tr>";
		strHtml += "<tr><th>Run Third</th><td>" + post.run_third + "<td/></tr>";		
		
		$("#recommendTbl tbody").append(strHtml);
		$('#recommendDiv').show();
	}else{
		parent.$.messager.alert('',result.message);
	}
};