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
		},
		onLoadSuccess: function() {
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
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
					$('#selectUserName').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}	
	});
	
	$("#tableList").datagrid({
		view: myview,
		columns:[[
			{field:'table_name',title:'TABLE_NAME',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'partition_name',title:'PARTITION_NAME',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'subpartition_name',title:'SUBPARTITION_NAME',width:"13%",halign:"center",align:"left",sortable:"true"},
			{field:'inserts',title:'INSERTS',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'updates',title:'UPDATES',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'deletes',title:'DELETES',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'truncated',title:'TRUNCATED',width:"10%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'timestamp',title:'변경일시',width:"12%",halign:"center",align:'center',sortable:"true",sortable:"true"},
			{field:'base_day',title:'수집일자',width:"8%",halign:"center",align:'center',formatter:getDateFormat,sortable:"true",sortable:"true"},
			{field:'all_change_cnt',title:'전체변경건수',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'rank',title:'변경순위',width:"7%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],
		
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		} 
	});
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').textbox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 입력해 주세요.');
		return false;
	}
	
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	}	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	
	$('#tableList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일자별 DML 변경 내역"," "); 
	
	ajaxCall("/DMLChangeDaily",
		$("#submit_form"),
		"POST",
		callback_DMLChangeDailyAddTable);		
}

//callback 함수
var callback_DMLChangeDailyAddTable = function(result) {
	json_string_callback_common(result,'#tableList',true);
};