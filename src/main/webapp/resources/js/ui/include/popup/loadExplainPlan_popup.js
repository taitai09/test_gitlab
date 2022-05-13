$(document).ready(function() {
	$('#loadExplainPlanPop').window({
		title : "SQL 적재",
		top:getWindowTop(700),
		left:getWindowLeft(1600)
	});
	
	$('#beforePlan').tree({
		animate:true,
		lines:true
	});
	
	$('#afterPlan').tree({
		animate:true,
		lines:true
	});
	
	$("#bindValueList").datagrid({
		view: myview,
		fitColumns:true,
		columns:[[
			{field:'name',title:'NAME',halign:"center",align:'center'},
			{field:'value',title:'VALUE_STRING',halign:"center",align:'left'},
			{field:'sql_bind',title:'DATATYPE_STRING',halign:"center",align:'right'}
		]],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#bindBtn").linkbutton({disabled:true});
});

function loadExplainPlan(submit_form) {
//	$("#textArea").html("");
	$("#textArea").val('');
	$('#beforeTreePlan').tree("loadData", []);
	$('#afterTreePlan').tree("loadData", []);
	
	/* 상단 텍스트 박스 */
	ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainSqlText",
			submit_form,
			"POST",
			callback_SQLTextAction);
	
	$('#bindValueList').datagrid('loadData',[]);
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainBindValue",submit_form, "POST", callback_BindValueListAction);
	
	/* Tree Plan */
	ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainBeforePlan", submit_form, "POST", callback_SQLPerformTreeBeforePlanListAction);
	
	/* Tree Plan */
	if($("#submit_form #sql_command_type_cd").val() == 'SELECT') {
		ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainAfterSelectPlan", submit_form, "POST", callback_SQLPerformTreeAfterPlanListAction);
	} else if($("#submit_form #sql_command_type_cd").val() == '') {
		$('#afterTreePlan').append("<li>" + "<div id='afterTreePlan01' class='tree-node'>" +
			"<span class='tree-indent'></span>" +
			"<span class='tree-icon tree-file '></span>" +
			"<span class='tree-title'>해당 정보가 없습니다.</span>" +
			"</div>" + "</li>");
	} else {
		ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainAfterNotSelectPlan", submit_form, "POST", callback_SQLPerformTreeAfterPlanListAction);
	}
}

function copy_to_clipboard() {
	if ($("#textArea").val( ) == ""){
		return;
	}
	
	var copyText = document.getElementById("textArea");
	copyText.focus();
	copyText.select();
	document.execCommand("Copy");
	copyText.setSelectionRange(0, 0);
	copyText.scrollTop = 0;   
	parent.$.messager.alert('','복사되었습니다.');
}

function copy_to_sqlId() {
	if ( $("#submit_form #sql_id").val() == ""){
		return;
	}
	
	let element = document.createElement('textarea');
	
	element.value = document.getElementById("sql_id").value;
	element.setAttribute('readonly', '');
	element.style.position = 'absolute';
	element.style.left = '-9999px';
	document.body.appendChild(element);
	element.select();
	
	var returnValue = document.execCommand('copy');
	document.body.removeChild(element);
	
	if (!returnValue) {
		throw new Error('copied nothing');
	}
	
	parent.$.messager.alert('','복사되었습니다.');
}

var callback_SQLTextAction = function(result) {
	if(result.result){
		var post = result.object;
		//$("#textArea").html(strReplace(post.sql_text,'\n','<br/>'));
		if(post != null){
			$("#textArea").val(post.sql_text);
		}
	}else{
		$("#textArea").val(result.message);
	}
};

var callback_BindValueListAction = function(result) {
	var data = null;
	try{
		data = JSON.parse(result);
	
		if(data.result != undefined && !data.result){
			var opts = $("#bindValueList").datagrid('options');
			var vc = $("#bindValueList").datagrid('getPanel').children('div.datagrid-view');
			vc.children('div.datagrid-empty').remove();
			if (!$("#bindValueList").datagrid('getRows').length){
				var emptyMsg = "검색된 데이터가 없습니다.";
				var d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
				d.css({
					top:50
				});
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

//callback 함수
var callback_SQLPerformTreeBeforePlanListAction = function(result) {
	var data = JSON.parse(result);
	if(data.length == 0) {
		$('#beforeTreePlan').append("<li>" + "<div id='beforeTreePlan01' class='tree-node'>" +
				"<span class='tree-indent'></span>" +
				"<span class='tree-icon tree-file '></span>" +
				"<span class='tree-title'>해당 정보가 없습니다.</span>" +
				"</div>" + "</li>");
	} else if (data.result != undefined && !data.result) {
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			//parent.$.messager.alert('',data.message);
			$('#beforeTreePlan').append("<li>" + "<div id='beforeTreePlan01' class='tree-node'>" +
					"<span class='tree-indent'></span>" +
					"<span class='tree-icon tree-file '></span>" +
					"<span class='tree-title'>" + data.message + "</span>" +
					"</div>" + "</li>");
		}
	}else{
		$('#beforeTreePlan').tree("loadData", data);
	}
	
	$("#beforeTreePlan .tree-node" ).css( "cursor", "default" );
}

//callback 함수
var callback_SQLPerformTreeAfterPlanListAction = function(result) {
	var data = JSON.parse(result);
	if(data.length == 0) {
		$('#afterTreePlan').append("<li>" + "<div id='afterTreePlan01' class='tree-node'>" +
				"<span class='tree-indent'></span>" +
				"<span class='tree-icon tree-file '></span>" +
				"<span class='tree-title'>해당 정보가 없습니다.</span>" +
				"</div>" + "</li>");
	} else if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			//parent.$.messager.alert('',data.message);
			$('#afterTreePlan').append("<li>" + "<div id='afterTreePlan01' class='tree-node'>" +
					"<span class='tree-indent'></span>" +
					"<span class='tree-icon tree-file '></span>" +
					"<span class='tree-title'>" + data.message + "</span>" +
					"</div>" + "</li>");
		}
	}else{
		$('#afterTreePlan').tree("loadData", data);
	}
	$("#afterTreePlan .tree-node" ).css( "cursor", "default" );
}

function Btn_CloseLoadExplainPlanPop() {
	$("#textArea").val('');
	$('#beforeTreePlan').tree("loadData", []);
	$('#afterTreePlan').tree("loadData", []);
	
	Btn_OnClosePopup('loadExplainPlanPop');
}