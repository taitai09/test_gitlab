$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// Instance 조회			
	    	$('#selectInstance').combobox({
	    	    url:"/Common/getAgentInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}
				},
				onLoadError: function(){
					parent.$.messager.alert('','Instance 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$("#inst_id").val("");
	    	$("#snap_id").val("");	    	
	    	$("#start_snap_id1").textbox("setValue","");
	    	$("#end_snap_id1").textbox("setValue","");
	    }	    
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance를 선택해 주세요.');
		return false;
	}	
	
	if($('#start_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','시작 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	if($('#end_snap_id1').textbox('getValue') == ""){
		parent.$.messager.alert('','종료 SNAP ID를 선택해 주세요.');
		return false;
	}
	
	if($('#sql_id').textbox('getValue') == ""){
		parent.$.messager.alert('','SQL ID를 입력해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#gubun").val($('#selectGubun').combobox('getValue'));
	
	ajaxCall("/AWRSQLReport",
			$("#submit_form"),
			"POST",
			callback_AWRSQLReportAction);		
	
	var win = parent.$.messager.progress({
		title:'Please waiting',
		msg:'AWR SQL 분석',
		text:'생성중입니다.',
		interval:100
	});	
}

//callback 함수
var callback_AWRSQLReportAction = function(result) {
	if(result.result){
		var strHtml = "";
		$("#dataArea").html("");
		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			if($("#selectGubun").combobox("getValue") == "html"){
				strHtml += post.row_value;
			}else{
				strHtml += strReplace(strReplace(post.row_value,"\n","<br/>")," ","&nbsp;") + "<br/>";	
			}
		}
		
		$("#dataArea").html(strHtml);
		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};

/* SNAP_ID 선택 팝업 */
//function showSnapPopup(gubun){
//	parent.strGb = gubun;
//	
//	if($('#selectCombo').combobox('getValue') == ""){
//		parent.$.messager.alert('','DB를 선택해 주세요.');
//		return false;
//	}
//	
//	$("#dbid").val($('#selectCombo').combobox('getValue'));
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();	
//	
//	parent.$('#snapList_form #snap_id').val("0");
//	parent.$('#snapList_form #startSnapIdList').datagrid('loadData',[]);
//	parent.$('#snapList_form #endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$('#snapList_form #dbid').val($('#selectCombo').combobox('getValue'));
//	parent.$('#snapList_form #dbName').textbox('setValue',$('#selectCombo').combobox('getText'));
//	parent.$('#snapList_form #instance_number').val($('#selectInstance').combobox('getValue'));
//	
//	parent.$('#startSnapIdList').datagrid('loadData',[]);
//	parent.$('#endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
//	parent.$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);	
//	
//	parent.$('#snapListPop').window("open");
//}

/* 베이스라인 선택 팝업 */
function showBaseLinePopup(strNo){
	strNoGb = strNo;
	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','Instance 선택해 주세요.');
		return false;
	}		
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));	

	$('#baseLinePop').window("open");
	
	$('#baseLineList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("베이스 라인 선택"," "); 
	
	ajaxCall("/AWRAdvancedAnalysis/Popup/BaseLineList",
			$("#submit_form"),
			"POST",
			callback_BaseLineList);	
}

//callback 함수
var callback_BaseLineList = function(result) {
	json_string_callback_common(result,'#baseLineList',true);
}


function Btn_SaveClick(){
	$("#submit_form").attr("action","/AWRSQLReport/Save");
	$("#submit_form").submit();		
}

function Btn_PrintClick() {
   $("#dataArea").printElement({pageTitle: 'AWR SQL REPORT',printMode: 'popup',overrideElementCSS:true,leaveOpen:false});
}