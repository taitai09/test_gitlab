$(document).ready(function() {
	$('#loadExplainPlanPop').window({
		title : "SQL 적재",
		top:getWindowTop(700),
		left:getWindowLeft(1600)
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
	
	$('#planCompare').on('click', function(){
		
		ajaxCall("/Common/getPlanCompareResult"
				,$('#loadExplainPlan_form')
				,"GET"
				,callback_planCompare);
		
	});
});

function loadExplainPlan(submit_form) {
	$("#textArea").val('');
	$("#asisTextPlan").val('');
	$("#operTextPlan").val('');
	
	/* 상단 텍스트 박스 */
	
	let code = $("#submit_form #perf_check_sql_source_type_cd").val();
	let planHashValue = $("#sqlinfo_form #plan_hash_value").val();
	console.log("code ====> "+code);
	
	$("#sqlinfo_form #plan_hash_value").val( $("#sqlinfo_form #asis_plan_hash_value").val() );
	// SQLText
	if ( code == "1" ) {
		console.log("AWR_SQLText");
		// AWR
		ajaxCall("/SQLInformation/SQLText", submit_form, "POST", callback_SQLTextAction);
		
		console.log("AWR_ASISPlan");
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlan", submit_form, "POST", callback_SQLPerformTextAsisPlanListAction );
	} else {
		console.log("VSQL_SQLText");
		// VSQL
		ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainSqlText", submit_form, "POST", callback_SQLTextAction);
		
		console.log("VSQL_ASISPlan");
		/* asis Plan */
		ajaxCall("/SQLInformation/TextPlanAll", submit_form, "POST", callback_SQLPerformTextAsisPlanListAction);
	}
	$("#sqlinfo_form #plan_hash_value").val( planHashValue );
	
	$('#bindValueList').datagrid('loadData',[]);
	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#bindValueList').datagrid('loading');
	
	ajaxCall("/SQLAutomaticPerformanceCheck/loadExplainBindValue",submit_form, "POST", callback_BindValueListAction);
	
	/* 운영 Plan */
	ajaxCall("/SQLInformation/TextPlanAll", submit_form, "POST", callback_SQLPerformTextOperPlanListAction);
	
	/*성능 점검 결과*/
	ajaxCall("/AutoPerformanceCompareBetweenDatabase/loadPerfCheckResultList", submit_form, "POST", callback_LoadPerfCheckResultListAction);
}

function copy_to_clipboard() {
	if ($("#textArea").val( ) == "") {
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
	if ( $("#submit_form #sql_id").val() == "") {
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

function asisTextPlanCopy() {
	if ( $("#asisTextPlan_h").val() == "" ) {
		return;
	}
	
	let element = document.createElement('textarea');
	
	element.value = document.getElementById("asisTextPlan_h").value;
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

function operTextPlanCopy() {
	if ( $("#operTextPlan_h").val( ) == "") {
		return;
	}

	let element = document.createElement('textarea');
	
	element.value = document.getElementById("operTextPlan_h").value;
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

var callback_LoadPerfCheckResultListAction = function(result) {
	try{
		var data = JSON.parse(result);
		
		if ( data.result != undefined && !data.result ) {
			if ( data.message == 'null' ) {
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			} else {
				parent.$.messager.alert('',data.message);
			}
		} else {
			var html = "";
			if ( data.length == 0 ){
				html = "<tr><td colspan='6' align='center'><a>검색된 데이터가 없습니다.</a></td></tr>";
				
			} else {
				jQuery.each(data, function(index, row ) {
					html += "<tr>\n";
					html += "	<td><input type='text' size='15' class='font11px width100per border0px' value='"+row.perf_check_indc_nm+"' readonly></td>\n";
					
					var passMaxValue = row.pass_max_value;
					
					if ( passMaxValue != null ) {
						if ( passMaxValue.indexOf('.') == 0 ) {
							passMaxValue = "0"+passMaxValue;
						}
						
						html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+passMaxValue+"' readonly></td>\n";
						html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+passMaxValue+" 초과' readonly></td>\n";
					} else {
						passMaxValue = "";
						html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+passMaxValue+"' readonly></td>\n";
						html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+passMaxValue+"' readonly></td>\n";
					}
					
					html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exec_result_value+"'readonly></td>\n";
					
					var backColor = "";
					if ( row.perf_check_result_div_nm == '적합' ) {
						backColor = 'style="background-color:#1A9F55;color:white;"';
					} else if ( row.perf_check_result_div_nm == '부적합' ) {
						backColor = 'style="background-color:#E41E2C;color:white;"';
					} else if ( row.perf_check_result_div_nm == '오류' ) {
						backColor = 'style="background-color:#ED8C33;color:white;"';
					} else if ( row.perf_check_result_div_nm == '미수행' ) {
						backColor = 'style="background-color:#7F7F7F;color:white;"';
					} else if ( row.perf_check_result_div_nm == '수행중' ) {
						return 'background-color:#89BD4C;color:white;';
					} else if ( row.perf_check_result_div_nm == '점검제외' ) {
						backColor = 'style="background-color:#012753;color:white;"';
					}
					
					var resultDivNm = row.perf_check_result_div_nm;
					
					if ( resultDivNm == null ) {
						resultDivNm = "";
					}
					
					html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+resultDivNm+"'readonly></td>\n";
					html += "	<td>"+ nvl(row.perf_check_result_desc,'')+"</td>\n";
					html += "</tr>\n";
				});
			}
				
			$("#detailCheckResultTable tbody").html("");
			$("#detailCheckResultTable tbody").html( html );
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
}

var callback_BindValueListAction = function(result) {
	var data = null;
	try{
		data = JSON.parse(result);
	
		if ( data.result != undefined && !data.result ) {
			var opts = $("#bindValueList").datagrid('options');
			var vc = $("#bindValueList").datagrid('getPanel').children('div.datagrid-view');
			vc.children('div.datagrid-empty').remove();
			if ( !$("#bindValueList").datagrid('getRows').length ) {
				var emptyMsg = "검색된 데이터가 없습니다.";
				var d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
				d.css({
					top:50
				});
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

//callback 함수
var callback_SQLPerformTextAsisPlanListAction = function(result) {
	var strHtml = "";
	
	strHtml += "<li><b> EXECUTION_PLAN </b></li>";
	strHtml += "<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>";
	
	if ( result.result ) {
		document.getElementById("asisTextPlan").style.width = "755px";
		let max = 120;
		
		for ( var i = 0 ; i < result.object.length ; i++ ) {
			var post = result.object[i];
			
			if ( post.execution_plan.length > max ){
				max = post.execution_plan.length;
				document.getElementById("asisTextPlan").style.width = ( 900 + max)+"px";
				console.log("asisplan width ====> "+ document.getElementById("asisTextPlan").style.width +" , max Length ====> "+max);
			}
			
			strHtml += "<li>"+ strReplace( post.execution_plan,' ','&nbsp;&nbsp;' ) +"</li>";
		}
		if ( result.object.length == 0 ) {
			strHtml += "<li> 해당 정보가 없습니다. </li>";
		} else if ( result.result != undefined && !result.result) {
			if ( result.message == 'null' ) {
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
				return false;
			} else {
				strHtml += "<li>"+ result.message +"</li>";
			}
		}
	}
	
	strHtml += "<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>";
	$("#asisTextPlan").html( strHtml );
	
	strHtml = strHtml.replace(/\<\/b\>/g,"");
	strHtml = strHtml.replace(/\<\/li\>/g,"\n");
	strHtml = strHtml.replace(/(\<b\>|\<li\>)/g,"");
	
	$("#asisTextPlan_h").html( strHtml );
}

//callback 함수
var callback_SQLPerformTextOperPlanListAction = function(result) {
	var strHtml = "";
	
	strHtml += "<li><b> EXECUTION_PLAN </b></li>";
	strHtml += "<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>";
	
	if ( result.result ) {
		document.getElementById("operTextPlan").style.width = "755px";
		
		let max = 120;
		for ( var i = 0 ; i < result.object.length ; i++ ) {
			var post = result.object[i];
			
			if ( post.execution_plan.length > max ){
				max = post.execution_plan.length;
				document.getElementById("operTextPlan").style.width = ( 900 + max)+"px";
				console.log("operplan width ====> "+ document.getElementById("asisTextPlan").style.width +" , max Length ====> "+max);
			}
			
			strHtml += "<li>"+ strReplace( post.execution_plan,' ','&nbsp;&nbsp;' ) +"</li>";
		}
		if ( result.object.length == 0 ) {
			strHtml += "<li> 해당 정보가 없습니다. </li>";
		} else if ( result.result != undefined && !result.result) {
			if ( result.message == 'null' ) {
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
				return false;
			} else {
				strHtml += "<li>"+ result.message +"</li>";
			}
		}
	}
	
	strHtml += "<li>-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------</li>";
	$("#operTextPlan").html( strHtml );
	
	strHtml = strHtml.replace(/\<\/b\>/g,"");
	strHtml = strHtml.replace(/\<\/li\>/g,"\n");
	strHtml = strHtml.replace(/(\<b\>|\<li\>)/g,"");
	
	$("#operTextPlan_h").html( strHtml );
}
var callback_planCompare = function(result){

	drawDiffTable(result);
}

function Btn_CloseLoadExplainPlanPop() {
	$("#textArea").val('');
	$('#asisTextPlan').val("");
	$('#operTextPlan').val("");
	
	Btn_OnClosePopup('loadExplainPlanPop');
}

function Btn_SetSQLFormatter(){
	$('#textArea').format({method: 'sql'});
}
