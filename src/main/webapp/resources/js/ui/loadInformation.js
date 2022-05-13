$(document).ready(function() {
	$("#popGraphicList").treegrid({
		idField:'id',
		treeField:'query_output',		
		lines: true,
		columns:[[
			{field:'id',title:'ID',width:"8%",halign:"center",align:"center"},
			{field:'query_output',title:'OPERATION',width:"92%",halign:"center",align:'left'},
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	searchDetailSQLInfo();
});

function searchDetailSQLInfo(){
	$('#popGraphicList').treegrid('loadData',[]);
	
	/* modal progress open */
	top.openMessageProgress("SQL 정보를 조회중입니다.");	

	/* SQL TEXT */
	ajaxCall("/SQLInformation/Load/loadSqlText",
			$("#submit_form"),
			"POST",
			callback_loadSqlTextAction);	
	
	/* Graphic */
	ajaxCall("/SQLInformation/Load/SQLGraphicList",
			$("#submit_form"),
			"POST",
			callback_SQLGraphicListAction);
	
	/* Text */
	ajaxCall("/SQLInformation/Load/SQLTextList",
			$("#submit_form"),
			"POST",
			callback_SQLTextListAction);	
}

//callback 함수
var callback_loadSqlTextAction = function(result) {	
	if(result.result){
		var post = result.object;
		if(post != null){
			$("#textArea").val(post.sql_text);
		}
	}
};

//callback 함수
var callback_SQLGraphicListAction = function(result) {
	var data = JSON.parse(result);
	$('#popGraphicList').treegrid("loadData", data);

	$("#executePlanDiv td,span,div").css("cursor","default");

	/* modal progress close */
	top.closeMessageProgress();		
};

//callback 함수
var callback_SQLTextListAction = function(result) {
	var strHtml = "";
	$("#textPlan li").remove();
	
	if(result.result){
		strHtml += "<li><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];		
			strHtml += "<li>"+strReplace(post.query_output,' ','&nbsp;')+"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#textPlan").append(strHtml);
	}
};

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}