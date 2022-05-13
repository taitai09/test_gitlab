$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
		onLoadSuccess: function() {
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			var win = $.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			// Instance 조회			
			$('#selectInstance').combobox({
				url:"/Common/getMasterInstance?dbid="+rec.dbid,
				method:"get",
				valueField:'inst_id',
				textField:'inst_name',
				onLoadSuccess: function(items) {
					$.messager.progress('close');
					
					var inst_id=$("#inst_id").val();
					var parameter_name = $("#parameter_name").val();
					
					if(inst_id != '' && parameter_name != ''){
						if(dbid != "" && inst_id != "" && parameter_name != ""){
							$('#selectInstance').combobox('setValue',inst_id);
							$("#parameterName").textbox('setValue',parameter_name);
							Btn_OnClick();
						}		
					}else{
						if (items.length){
							var opts = $(this).combobox('options');
							$(this).combobox('select', items[0][opts.valueField]);
						}
					}
					
					var call_from_parent = $("#call_from_parent").val();
					if(call_from_parent == "Y"){
						Btn_OnClick();
					}
				}
			});
		}
	});	

	$("#tableList").treegrid({
		idField:'id',
		treeField:'parameter_name',
		lines: true,
		onClickRow : function(row) {
//			var menuParam = "dbid=" + row.dbid
//			+ "&parameter_name=" + row.parameter_name
//			+ "&call_from_parent=Y";
//			;
//			createNewTab("183", "파라미터 변경 이력", "/ParameterChangeHistory", menuParam);
		},
		columns:[[
			{field:'dbid',hidden:"true"},
			{field:'parameter_name',title:'PARAMETER_NAME',halign:"center",align:"left"},
			{field:'value',title:'VALUE',halign:"center",align:'left'},
			{field:'modify_time',title:'MODIFY_TIME',halign:"center",align:'center'}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		} 
	});
	
	var parameter_name = $("#parameter_name").val();
	$("#parameterName").textbox("setValue",parameter_name);
	
	var dbid = $("#dbid").val();
	$('#selectCombo').combobox('setValue',dbid);
	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','INSTANCE를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#inst_id").val($('#selectInstance').combobox('getValue'));
	$("#parameter_name").val($('input[name="parameterName"]').val());

	$('#tableList').treegrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DB 파라미터 변경이력"," "); 
	
	ajaxCall("/ParameterChangeHistory", $("#submit_form"), "POST", callback_ParameterChangeHistoryAddTable);
}

//callback 함수
var callback_ParameterChangeHistoryAddTable = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').treegrid("loadData", data);
		$("#tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		$("#tableList div.datagrid-cell.tree-node" ).css( "cursor", "default !important" );
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};