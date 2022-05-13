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
	    	$("#file_no").val("");
	    	$('#file_nm').textbox('setValue',"");
	    	$("#dbid").val(rec.dbid);
	    	
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});	    	
	    	
	    	$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$("#selectFileNo").combobox({
				url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
				method:"get",
				valueField:'file_info',
				textField:'file_no',
				onLoadError: function(){
					parent.$.messager.alert('','파일번호 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				},
				onSelect:function(rec1){
					$("#explain_exec_seq").val("");

					setFileInfo(rec1.file_info);
					
					$("#selectExplainExecSeq").combobox({
						url:"/Common/getDBIOExplainExec?file_no="+rec1.file_no,
						method:"get",
						valueField:'explain_info',
						textField:'explain_exec_seq',
						onSelect:function(rec2){
							setExplainExec(rec2.explain_info);
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
	
	$('#selectTableName').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick();
				}				
			}
		})
	});
	
	$("#tableList").datagrid({
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
	
	$('#chkUsage').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#usage_yn").val("Y"); 
			else $("#usage_yn").val("N");
		}
	})
	
	$('#chkPk_yn').switchbutton({
		checked: false,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#pk_yn").val("Y");
			else $("#pk_yn").val("N");
		}
	})
});
function formValidationCheck(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	if($('#selectExplainExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱순번을 선택해 주세요.');
		return false;
	}
	return true;
}
function Btn_OnClick(){
	$("#currentPage").val("1");

	if(!formValidationCheck()){
		return;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_name").val($('#selectTableName').textbox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();		

	$('#tableList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 정비"," "); 
	
	fnSearch();
	
}
function fnSearch(){
	ajaxCall("/LoadIndexUsage",
			$("#submit_form"),
			"POST",
			callback_UsingIndexAddTable);		
}
//callback 함수
var callback_UsingIndexAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
	
};

function setFileInfo(selValue){
	$('#file_no').val(selValue);
	
	ajaxCall("/Common/getDBIOLoadFileInfo?dbid="+$("#dbid").val()+"&file_no="+selValue,
			null,
			"GET",
			callback_getDBIOLoadFileInfoAction);	
}

//callback 함수
var callback_getDBIOLoadFileInfoAction = function(result) {
	if(result.result){	
		var post = result.object;
		
		$('#file_nm').textbox('setValue',post.file_nm);
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function setExplainExec(selValue){
	$('#explain_exec_seq').val(selValue);
}

function Excel_DownClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectFileNo').combobox('getValue') == ""){
		parent.$.messager.alert('','파일번호를 선택해 주세요.');
		return false;
	}	
	
	if($('#selectExplainExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱순번을 선택해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_name").val($('#selectTableName').textbox('getValue'));
	
	$("#submit_form").attr("action","/LoadIndexUsage/ExcelDown");
	$("#submit_form").submit();
}
