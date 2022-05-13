$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#selectDbid").combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		onClickRow : function(index,row) {
			$("#idx_ad_no").val(row.idx_ad_no);
			$("#access_path_type").val(row.access_path_type);
			$("#table_owner").val(row.table_owner);
			$("#db_name").val($('#selectDbid').combobox('getText'));
			
			if(row.status == "COMPLETE"){
				goIndexRecommendStatus();	
			}
		},			
		columns:[[
			{field:'chk',halign:"center",align:'center',checkbox:'true'},
			{field:'idx_ad_no',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'access_path_type',hidden:"true"},
			{field:'access_path_type_nm',title:'소스구분',halign:"center",align:'center',sortable:"true"},
			{field:'table_owner',title:'OWNER',halign:"center",align:'center',sortable:"true"},
			{field:'start_dt',title:'시작일시',halign:"center",align:'center',sortable:"true"},
			{field:'end_dt',title:'종료일시',halign:"center",align:'center',sortable:"true"},
			{field:'table_cnt',title:'대상 테이블수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'running_table_cnt',title:'추천 테이블수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'recommend_index_cnt',title:'추천 인덱스수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'start_collect_dt',title:'시작 수집일시',halign:"center",align:'right',sortable:"true"},
			{field:'end_collect_dt',title:'종료 수집일시',halign:"center",align:'right',sortable:"true"},
			{field:'analysis_sql_cnt',title:'ACCESS-PATH수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'status',title:'진행상태',halign:"center",align:'center',sortable:"true"}
		]],
		rowStyler: function(index,row){
			if(row.status == 'EXECUTING'){
				return 'background-color:#6293BB;color:#fff;'; // return inline style
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onCheck:function(index, row) {
			if(row.status != 'EXECUTING') $('#tableList').datagrid('unselectRow', index);
		},
		onCheckAll:function(rows) {
			for(var index = 0; index < rows.length; index++) {
				if(rows[index].status == 'COMPLETE') $('#tableList').datagrid('unselectRow', index);
			}
		}
	});
	
	$('#selectDbid').combobox('setValue',$("#dbid").val());
	
	var callFromParent = $("#call_from_parent").val();
	var callFromChild = $("#call_from_child").val();
	if(callFromParent == "Y" || callFromChild == "Y"){
		ajaxCallAutoIndexStatus();	
	}

});

function fnSearch(){
	ajaxCallAutoIndexStatus();
}

function formValidationCheck(){
	return true;
}

function ajaxCallAutoIndexStatus(){
//	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 자동 설계 현황"," ");
	
	if(!compareAnBDate($('#start_dt').textbox('getValue'), $('#end_dt').textbox('getValue'))) {
		parent.$.messager.alert('','자동설계일시를 확인해 주세요.');
		return false;
	}
	
	ajaxCall("/AutoIndexStatusAction",
		$("#submit_form"),
		"POST",
		callback_AutoIndexStatusAddTable);		
}

function Btn_OnClick(){
	$("#currentPage").val("1");
	
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#start_dt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	} else {
		if($('#end_dt').textbox('getValue') == ""){
			parent.$.messager.alert('','종료일자를 선택해 주세요.');
			return false;
		}
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$("#dbid").val($('#selectDbid').combobox('getValue'));

//	$("#submit_form").attr("action","/AutoIndexStatus");
//	$("#submit_form").submit();
	
	ajaxCallAutoIndexStatus();
}

//callback 함수
var callback_AutoIndexStatusAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
	
	var data = JSON.parse(result);
	var dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
}

function goIndexRecommendStatus(){
//	$("#submit_form").attr("action","/IndexRecommendStatus");
//	$("#submit_form").submit();
//	var menuParam = "dbid="+$('#selectDbid').combobox('getValue')+"&db_name="+$('#selectDbid').combobox('getText');
	$("#dbid").val($('#selectDbid').combobox('getValue'));
	$("#db_name").val($('#selectDbid').combobox('getText'));
	var menuParam = $("#submit_form").serialize();
	createNewTab($("#menu_id").val()+1, "인덱스 Recommend 현황", "/IndexRecommendStatus", menuParam);
}

/**
 * 인덱스 자동설계현황 엑셀 다운로드
 * @returns
 */
function Excel_Download(){	
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectDbid').combobox('getValue'));
	
	$("#submit_form").attr("action","/indexDesignAdviser/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function Btn_OnForceComplete() {
	var rows = $('#tableList').datagrid("getChecked");
	
	if(rows.length == 0) {
		$.messager.alert('','인덱스 자동설계 강제완료처리를 진행할 실행번호를 선택해 주세요.');
		return;
	}
	
	var dataLen = rows.length;
	var idxAdNo = "";
	
	for(var index = 0; index < dataLen; index++) {
		if(index > 0) idxAdNo = idxAdNo.concat(",");
		
		idxAdNo = idxAdNo.concat(rows[index].idx_ad_no);
	}
	
	parent.$.messager.confirm('', '미완료된 인덱스 자동설계작업을 전부 완료처리합니다', function(check) {
		if (check) {
			//jQuery.ajaxSettings.traditional = true;
			
			$.ajax({
				type: 'POST',
				data: idxAdNo,
				url: "/indexDesignAdviser/updateForceComplete",
				//dataType: "json",
				contentType: "application/json",
				success: function(result) {
					parent.$.messager.alert('인덱스 자동설계 강제완료처리 완료', result.message, 'info', function(){
						Btn_OnClick();
					});
				},
				error: function(xhr, status, error) {
					alert(status);
					alert(error)
				}
			});
		}
	});
}