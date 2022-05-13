$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#loadIndexUsage_form #selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#loadIndexUsage_form #selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			$("#loadIndexUsage_form #file_no").val("");
			$('#loadIndexUsage_form #file_nm').textbox('setValue',"");
			$("#loadIndexUsage_form #dbid").val(rec.dbid);
			
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#loadIndexUsage_form #selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#loadIndexUsage_form #selectUserName').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			
			$("#loadIndexUsage_form #selectFileNo").combobox({
				url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
				method:"get",
				valueField:'file_info',
				textField:'file_no',
				onLoadError: function(){
					parent.$.messager.alert('','파일번호 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				},
				onLoadSuccess: function() {
					$('#loadIndexUsage_form #selectFileNo').combobox('textbox').attr("placeholder","선택");
				},
				onShowPanel: function() {
					// 파일번호 재조회
					$("#loadIndexUsage_form #selectFileNo").combobox({
						url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
						method:"get",
						valueField:'file_info',
						textField:'file_no',
					});
					$(".textbox").removeClass("textbox-focused");
					$(".textbox-text").removeClass("tooltip-f");
				},
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onSelect:function(rec1){
					$("#loadIndexUsage_form #explain_exec_seq").val("");

					setFileInfo_loadIndexUsage(rec1.file_info);
					
					$("#loadIndexUsage_form #selectExplainExecSeq").combobox({
						url:"/Common/getDBIOExplainExec?file_no="+rec1.file_no,
						method:"get",
						valueField:'explain_info',
						textField:'explain_exec_seq',
						onSelect:function(rec2){
							setExplainExec(rec2.explain_info);
						},
						onLoadSuccess: function() {
							$('#loadIndexUsage_form #selectExplainExecSeq').combobox('textbox').attr("placeholder","선택");
						},
						onLoadError: function(){
							parent.$.messager.alert('','생성순번 조회중 오류가 발생하였습니다.');
							parent.$.messager.progress('close');
						}
					});	
				}
			});
		}
	});
	
	$('#loadIndexUsage_form #selectTableName').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick_loadIndexUsage();
				}
			}
		})
	});
	
	$("#loadIndexUsage_form #tableList").datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'table_name',title:'테이블 영문명',halign:"center",align:"left",sortable:"true"},
			{field:'index_name',title:'인덱스명',halign:"center",align:"left",sortable:"true"},
			{field:'pk_yn',title:'PK여부',halign:"center",align:"center",sortable:"true"},
			{field:'usage_count',title:'사용횟수',halign:"center",align:"right",sortable:"true"},
			{field:'table_hname',title:'COMMENTS',width:'400px',halign:"center",align:"left",sortable:"true"}					
		]],
		
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#loadIndexUsage_form #chkUsage').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#loadIndexUsage_form #usage_yn").val("Y"); 
			else $("#loadIndexUsage_form #usage_yn").val("N");
		}
	});
	
	$('#loadIndexUsage_form #chkPk_yn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#loadIndexUsage_form #pk_yn").val("Y");
			else $("#loadIndexUsage_form #pk_yn").val("N");
		}
	});
	
	setMarster_form_selector('#loadIndexUsage_form');
//	setMarster_form_selector($('#loadIndexUsage_form').selector);
	
	$('#loadIndexUsage_form #prevBtn').click(function(){
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 정비"," "); 
		
		if(formValidationCheck()){
			fnGoPrevOrNext('PREV');
		}
	});
	
	$('#loadIndexUsage_form #nextBtn').click(function(){
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 정비"," "); 
		
		if(formValidationCheck()){
			fnGoPrevOrNext('NEXT');
		}
	});	
});
function formValidationCheck(){
	return true;
}
function formValidationCheck_loadIndexUsage(){
	if($('#loadIndexUsage_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexUsage_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexUsage_form #selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	if($('#loadIndexUsage_form #selectExplainExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱순번을 선택해 주세요.');
		return false;
	}
	return true;
}
function Btn_OnClick_loadIndexUsage(){
	$("#loadIndexUsage_form #currentPage").val("1");
	$("#loadIndexUsage_form #pagePerCount").val("20");
	
	if(!formValidationCheck_loadIndexUsage()){
		return;
	}
	
	$("#loadIndexUsage_form #dbid").val($('#loadIndexUsage_form #selectCombo').combobox('getValue'));
	$("#loadIndexUsage_form #owner").val($('#loadIndexUsage_form #selectUserName').combobox('getValue'));
	$("#loadIndexUsage_form #table_name").val($('#loadIndexUsage_form #selectTableName').textbox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#loadIndexUsage_form #menu_id").val();

	$('#loadIndexUsage_form #tableList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 정비"," "); 
	
	fnSearchUsingIndexAddTable();
	
}
function fnSearch(master_form_selector){
	
	if(master_form_selector == '#loadActionPlan_form') {
		searchActionPlan();
	} else if(master_form_selector == '#loadIndexDesign_form') {
		fnSearchOdsTableAddTable();
	} else if(master_form_selector == '#loadIndexUsage_form') {
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 정비"," "); 
		
		fnSearchUsingIndexAddTable();
	}
}
function fnSearchUsingIndexAddTable() {
	
	ajaxCall("/LoadIndexUsage",
			$("#loadIndexUsage_form"),
			"POST",
			callback_UsingIndexAddTable);
}
//callback 함수
var callback_UsingIndexAddTable = function(result) {
	json_string_callback_common(result,'#loadIndexUsage_form #tableList',true);
	
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	
	setMarster_form_selector('#loadIndexUsage_form');
	
	fnEnableDisablePagingBtn(dataLength);
	
};

function setFileInfo_loadIndexUsage(selValue){
	$('#loadIndexUsage_form #file_no').val(selValue);
	
	ajaxCall("/Common/getDBIOLoadFileInfo?dbid="+$("#loadIndexUsage_form #dbid").val()+"&file_no="+selValue,
			null,
			"GET",
			callback_getDBIOLoadFileInfoAction_loadIndexUsage);	
}

//callback 함수
var callback_getDBIOLoadFileInfoAction_loadIndexUsage = function(result) {
	if(result.result){	
		var post = result.object;
		
		$('#loadIndexUsage_form #file_nm').textbox('setValue',post.file_nm);
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function setExplainExec(selValue){
	$('#loadIndexUsage_form #explain_exec_seq').val(selValue);
}

function Excel_DownClick_loadIndexUsage(){	
	if($('#loadIndexUsage_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexUsage_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexUsage_form #selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	if($('#loadIndexUsage_form #selectExplainExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱순번을 선택해 주세요.');
		return false;
	}

	$("#loadIndexUsage_form #dbid").val($('#loadIndexUsage_form #selectCombo').combobox('getValue'));
	$("#loadIndexUsage_form #owner").val($('#loadIndexUsage_form #selectUserName').combobox('getValue'));
	$("#loadIndexUsage_form #table_name").val($('#loadIndexUsage_form #selectTableName').textbox('getValue'));
	
	$("#loadIndexUsage_form").attr("action","/LoadIndexUsage/ExcelDown");
	$("#loadIndexUsage_form").submit();
}
