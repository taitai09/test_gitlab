$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#recommend_seq").val(row.recommend_seq);
			$("#base_day").val(row.base_day);
			
			showRecommendaion();
		},			
		columns:[[
			{field:'base_day',title:'BASE DAY',width:"7%",halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'recommend_seq',title:'RECOMMEND SEQ',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'segment_owner',title:'OWNER',width:"6%",halign:"center",align:"center",sortable:"true"},
			{field:'segment_name',title:'SEGMENT',width:"12%",halign:"center",align:'left',sortable:"true"},
			{field:'segment_type',title:'SEGMENT TYPE',width:"8%",halign:"center",align:'center',sortable:"true"},
			{field:'partition_name',title:'PARTITION NAME',width:"9%",halign:"center",align:'left',sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE',width:"7%",halign:"center",align:'center',sortable:"true"},
			{field:'allocated_space',title:'ALLOCATED SPACE',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'used_space',title:'USED SPACE',width:"9%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reclaimable_space',title:'RECLAIMABLE SPACE',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'chain_rowexcess',title:'CHAIN ROWEXCESS',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#recommendDiv").hide();
});

function Btn_OnClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#recommendDiv").hide();
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("오브젝트 진단"," "); 

	ajaxCall("/ObjectDiagnostics",
		$("#submit_form"),
		"POST",
		callback_ObjectDiagnosticsAddTable);		
}

//callback 함수
var callback_ObjectDiagnosticsAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function showRecommendaion(){
	ajaxCall("/ObjectDiagnostics/Recommendation",
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
		strHtml += "<tr><th>Recommendations</th><td>" + post.recommendations + "<td/></tr>";
		strHtml += "<tr><th>Run First</th><td>" + post.run_first + "<td/></tr>";
		strHtml += "<tr><th>Run Second</th><td>" + post.run_second + "<td/></tr>";
		strHtml += "<tr><th>Run Third</th><td>" + post.run_third + "<td/></tr>";		
		
		$("#recommendTbl tbody").append(strHtml);
		$('#recommendDiv').show();
	}
};