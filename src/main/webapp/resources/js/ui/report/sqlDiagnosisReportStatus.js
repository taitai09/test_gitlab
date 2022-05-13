var search = false;

$(document).ready(function(){
	$("body").css("visibility", "visible");
	
	createDbCombo();
});

function sqlDiagnosisReportOpen(std_qty_target_dbid, sql_std_qty_scheduler_no){
	let menuParam = "/SQLDiagnosisReportDesign/SQLDiagnosisReport";
	menuParam += "?std_qty_target_dbid=" + std_qty_target_dbid;
	menuParam += "&sql_std_qty_scheduler_no=" + sql_std_qty_scheduler_no;
	menuParam += "&call_from_parent=Y";

	let tabId = 'sqlDiagnosisReportCheckTab';
	let tabName = 'SQL 품질 진단 결과';
	let menuName = 'SQL 품질 진단';
	let menuId = parseInt( $("#menu_id").val() );
	
	parent.parent.goToExceptionTab(String(menuId), tabId, tabName, menuName, menuParam);
}
function Btn_OnClick(){
	$("#excel_std_qty_target_dbid").val($("#db_name_combo").combobox('getValue'));
	$("#std_qty_target_dbid").val($("#db_name_combo").combobox('getValue'));

	ajaxCall_GetColumns();
	setSqlDiagnosisreportStatus();
	search = true;
}

function setSqlDiagnosisreportStatus(){
	ajaxCall_GetSqlDiagnosisreportStatus();
}

function ajaxCall_GetSqlDiagnosisreportStatus(){
	ajaxCall("/SQLDiagnosisReportStatus/status",
			$("#detail_form"),
			"POST",
			callback_GetSqlDiagnosisreportStatus);
}
function callback_GetSqlDiagnosisreportStatus(result){
	try{
		json_string_callback_common(result,'#reportStatusTable',true);

	}catch(e){
		console.log("LoadLowerData:"+e.message);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
}

function ajaxCall_GetColumns(){
	ajaxCall("/SQLDiagnosisReportStatus/headers",
			null,
			"POST",
			callback_GetColumns);
}

var callback_GetColumns = function(result){
	if ( result ) {
		let data = JSON.parse(result);
		let indexList = data.rows;
		
		let columns = new Array();
		columns.push({field:'DB_NMAE',title:'품질진단DB',width:'100px',height:'200px',halign:'center',align:'left'})
		columns.push({field:'JOB_SCHEDULER_NM',title:'스케줄러',width:'150px',halign:'center',align:'left'})
		columns.push({field:'DIAG_DT',title:'진단일시',width:'120px',halign:'center',align:'center'})
		columns.push({field:'EXEC_STATUS',title:'진행상태',width:'100px',halign:'center',align:'left'})
		columns.push({field:'SQL_ID_CNT',title:'미준수 SQL 합계',width:'100px',halign:'center',align:'right'})

		let position = 9 + data.rows.length;
		indexList.forEach(
			function(idx){
				columns.splice(position, 0,
					{field:'SQL'+idx.qty_chk_idt_cd+'ERR_CNT',title:idx.qty_chk_idt_nm, width:'130px',halign:'center',align:'right'});
				position++;
			}
		);
		columns.push({field:'GATHER_TERM',title:'수집기간',width:'150px',halign:'center',align:'center'})
		columns.push({field:'SQL_STD_QTY_SCHEDULER_NO', hidden:true})
		columns.push({field:'SQL_STD_QTY_CHKT_ID', hidden:true})

//		columns.push({field:'SQL_STD_QTY_CHKT_ID',hidden:true})
//		columns.push({field:'SQL_STD_QTY_PROGRAM_SEQ',hidden:true})

		$("#reportStatusTable").datagrid({
			view: myview,
			singleSelect: true,
			checkOnSelect : false,
			selectOnCheck : false,
			columns: [columns],
			onSelect: function(index,row){
//				ajaxCallSchedulerProgramSourceDESC()
			}
			,onLoadError:function() {
				errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
			}
			,onSelect:function(index,row){
				console.log(row);
				sqlDiagnosisReportOpen(row.STD_QTY_TARGET_DBID,row.SQL_STD_QTY_SCHEDULER_NO);
				
			}

		});
		
	}else {
		errorMessager('데이터 조회 중에 에러가 발생하였습니다.');
	}
}

function createDbCombo(){
	$('#db_name_combo').combobox({
		url:"/Common/getDatabase?isAll=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		panelHeight:'auto',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(){
			$("#scheduler_no_combo").combobox('loadData', []);
			$("#scheduler_no_combo").combobox('setValue','');

		}
	});
}

function Excel_Download() {

	if (search === false){
		parent.$.messager.alert('경고','데이터 조회 후 엑셀 다운로드 바랍니다.','warning');
		return false;
	}
	
	$("#excel_submit_form").attr("action","/SQLDiagnosisReportStatus/excelDownload");
	$("#excel_submit_form").submit();
	$("#excel_submit_form").attr("action","");
}

