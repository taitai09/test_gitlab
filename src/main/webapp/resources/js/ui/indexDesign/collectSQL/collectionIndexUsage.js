var editIndex = undefined;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess:function(){
			$("#selectCombo").combobox("textbox").attr("placeholder","선택");
		},
		onSelect:function(rec){
			$("#exec_seq").val("");
			$('#start_snap_no').val("");
			$('#end_snap_no').val("");
			$('#strStartDt').textbox('setValue',"");
			$('#strEndDt').textbox('setValue',"");
			$('#analysis_sql_cnt').textbox('setValue',"");
			$('#access_path_exec_dt').textbox('setValue',"");
			
			var win = parent.parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.parent.$.messager.progress('close');
					$("#selectUserName").combobox("textbox").attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.parent.$.messager.progress('close');
				}
			});
			
//			$("#selectExecSeq").combobox({
//				url:"/Common/getAccPathExec?dbid="+rec.dbid+"&access_path_type="+$("#access_path_type").val(),
//				method:"get",
//				valueField:'access_path_value',
//				textField:'exec_seq',
//				onSelect:function(rec){
//					setExecValue(rec.access_path_value);
//				}
//			});
			
			$("#selectExecSeq").combobox({
				url:"/Common/getAccPathExecAddSec?dbid="+rec.dbid+"&access_path_type="+$("#access_path_type").val(),
				method:"get",
				valueField:'access_path_value',
				textField:'exec_seq',
				panelHeight: 500,
				onSelect:function(rec){
					setExecValue(rec.access_path_value);
				},
				onLoadSuccess:function(){
					$("#selectExecSeq").combobox("textbox").attr("placeholder","선택");
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
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'index_name',title:'INDEX_NAME',halign:"center",align:"left",sortable:"true"},			
			{field:'pk_yn',title:'PK여부',halign:"center",align:"center",sortable:"true"},
			{field:'usage_count',title:'사용횟수',halign:"center",align:"right",formatter:getNumberFormat,sortable:"true"},
			{field:'table_hname',title:'COMMENTS',width:'400px',halign:"center",align:"left"}
		]],

    	onLoadError:function() {
    		parent.parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
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
		parent.parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectExecSeq').combobox('getValue') == ""){
		parent.parent.$.messager.alert('','파싱 순번을 선택해 주세요.');
		return false;
	}
	return true;
}

function Btn_OnClick(){
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	
	if(!formValidationCheck()){
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_owner").val($("#owner").val());
	$("#table_name").val($('#selectTableName').textbox('getValue'));
	
//	/* modal progress open */
//	if(parent.parent.openMessageProgress != undefined) parent.parent.openMessageProgress("수집 SQL 인덱스 정비"," "); 
	
	fnSearch();
	
}

function fnSearch(){
	/* modal progress open */
	if(parent.parent.openMessageProgress != undefined) parent.parent.openMessageProgress("수집 SQL 인덱스 정비"," "); 
	
	ajaxCall("/CollectionIndexUsage",
			$("#submit_form"),
			"POST",
			callback_CollectionIndexUsageAddTable);		
}

//callback 함수
var callback_CollectionIndexUsageAddTable = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').datagrid("loadData", data);
		
		$( ".datagrid-view2:eq(0) td" ).css( "cursor", "default" );
	}

	/* modal progress close */
	if(parent.parent.closeMessageProgress != undefined) parent.parent.closeMessageProgress();	
	
	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
};

function setExecValue(selValue){
	var txData = [];
//	console.log(selValue);
	txData = selValue.split("|");
	$("#exec_seq").val(txData[0]);
	$('#strStartDt').textbox('setValue',txData[1]);
	$('#strEndDt').textbox('setValue',txData[2]);
	$('#analysis_sql_cnt').textbox('setValue',txData[3]);
	$('#access_path_exec_dt').textbox('setValue',txData[4]);
	
	$('#start_snap_no').val(txData[5]);
	$('#end_snap_no').val(txData[6]);
}

function Excel_DownClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectExecSeq').combobox('getValue') == ""){
		parent.parent.$.messager.alert('','파싱 순번을 선택해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	
	$("#submit_form").attr("action","/CollectionIndexUsage/ExcelDown");
	$("#submit_form").submit();
}
