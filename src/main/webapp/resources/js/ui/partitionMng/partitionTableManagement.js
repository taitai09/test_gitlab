$(document).ready(function() {
	
	$("body").css("visibility", "visible");
	$('#searchKey').combobox({
		onChange : function(newValue, oldValue) {
			$('#searchValue').textbox('setValue', '');
			
			if(newValue == '') {
				$('#searchValue').textbox('readonly', true);
			} else{
				$('#searchValue').textbox('readonly', false);
			}
		},
		onLoadSuccess : function(items) {
			$('#searchValue').textbox('readonly', true);
		}
	});

	selectTable();
	
	//파티션 작업 유형
	$('#partition_work_type_cd_nm').combobox({
		url:"/selectType?grp_cd_id=1030",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
//		onSelect : function(rec){
//	    	selectWorkType(rec); 
//	    	
//	    	
//	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#partition_work_type_cd_nm").combobox('textbox').attr("placeholder","선택");
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_getPartitionList();
	   }
	});	
	
	//파티션간격유형
	$('#partition_interval_type_cd_nm').combobox({
		url:"/selectType?grp_cd_id=1029",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
//		onSelect : function(rec){
//	    	selectIntervalType(rec); 
//	    	
//	    	
//	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#partition_interval_type_cd_nm").combobox('textbox').attr("placeholder","선택");
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_getPartitionList();
		}
	});	
	
	//보관기간유형
	$('#shelf_life_type_cd_nm').combobox({
		url:"/selectType?grp_cd_id=1028",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
//		onSelect : function(rec){
//	    	selectLifeType(rec); 
//	    	
//	    	
//	    },
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#shelf_life_type_cd_nm").combobox('textbox').attr("placeholder","선택");
			$("#compress_yn").combobox('textbox').attr("placeholder","선택");
		}
	});
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_getPartitionList();
		}
	});
	
	$('#partitionList').datagrid('loadData',[]);
	$("#partitionList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			clickPartitonList(row);
		},
		columns:[[
			{field:'table_name',title:'테이블명',width:"22%",halign:"center",align:"left",sortable:"true"},
			{field:'partition_work_type_cd',hidden:'true'},			
			{field:'partition_work_type_cd_nm',title:'파티션 작업유형',width:"14%",halign:"center",align:'center'},			
			{field:'partition_interval',title:'파티션 간격',width:"8%",halign:"center",align:'center'},
			{field:'partition_interval_type_cd',hidden:'true'},
			{field:'partition_interval_type_cd_nm',title:'파티션 간격유형',width:"9%",halign:"center",align:'center'},
			{field:'compress_yn',title:'압축여부',width:"6%",halign:"center",align:'center'},
			{field:'shelf_life_cnt',title:'보관기간',width:"6%",halign:"center",align:'center'},
			{field:'shelf_life_type_cd',hidden:'true'},
			{field:'shelf_life_type_cd_nm',title:'보관기간유형',width:"9%",halign:"center",align:'center'},
			{field:'partition_key_composite_value',title:'파티션키구성값',width:"18%",halign:"center",align:'center'},
			{field:'spare_partition_cnt',title:'예비파티션수',width:"8%",halign:"center",align:'center'}
		]],
		fitColumns:true,
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
});



var selectTable = function (){
	
	$('#table_name').combobox({       /*나중에 추가 예정*/
		url:"/selectTableName",
		method:"get",
		valueField:'table_name',
		textField:'table_name',
		onSelect : function(rec){
			selectTableName(rec); 
			
		},
		onChange : function(newValue, oldValue){
			changeTableName(newValue, oldValue);
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#table_name").combobox('textbox').attr("placeholder","선택");
		}
	});
}	



function changeTableName(newValue, oldValue){
	var crud_flag = $("#detail_form #crud_flag").val();
	if(crud_flag==="U"){
		parent.$.messager.alert('','테이블 수정시 KEY 값을 변경할 수 없습니다.');
		backToOldTableName(oldValue);
//		$("#detail_form #table_name").combobox("setValue", oldValue);
	}
}
function backToOldTableName(oldValue){
//		$("#detail_form #table_name").combobox("setValue", oldValue);
		$("#detail_form #table_name").textbox("setValue", oldValue);
	
	
}
function selectTableName(rec){
	
	var tableName = $("#detail_form #table_name").textbox("getValue");
	$("#detail_form #table_name").val(tableName);
//	$("#detail_form #table_name").val(newValue);


//	ajaxCall("/getTableName",
//			$("#left_form"),
//			"POST",
//			callback_selectTableNameAction);	
};

//callback 함수
//result=string, db_abbr_nm
var callback_selectTableNameAction = function(result) {
	$('#db_abbr_nm').textbox("setValue", result);
	selectTable();
};

function Btn_getPartitionList(){
	
//	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
//		($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
//		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
//		return false;
//	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$('#partitionList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터베이스 파티션 관리"," ");	

	ajaxCall("/getPartitionList",
			$("#left_form"),
			"GET",
			callback_getPartitonListAction);		
}

	
//callback 함수
var callback_getPartitonListAction = function(result) {

	var data = JSON.parse(result);
	
	$('#partitionList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
	selectTable();
};


function clickPartitonList(selRow){
//	$("#detail_form #dbid").val(selRow.dbid);
	$("#detail_form #table_name").textbox("setValue",selRow.table_name);   //나중에 변경
	$("#detail_form #partition_work_type_cd").val(selRow.partition_work_type_cd);
	$("#detail_form #partition_work_type_cd_nm").combobox("setValue",selRow.partition_work_type_cd);
	
	$("#detail_form #partition_interval_type_cd").val(selRow.partition_interval_type_cd);
	$("#detail_form #partition_interval_type_cd_nm").combobox("setValue",selRow.partition_interval_type_cd);
	
	$("#detail_form #shelf_life_type_cd").val(selRow.shelf_life_type_cd);
	$("#detail_form #shelf_life_type_cd_nm").combobox("setValue",selRow.shelf_life_type_cd);
	
	$("#detail_form #partition_interval").textbox("setValue",selRow.partition_interval);
	$("#detail_form #compress_yn").textbox("setValue",selRow.compress_yn);
	$("#detail_form #shelf_life_cnt").textbox("setValue",selRow.shelf_life_cnt);
//	$("#detail_form #partition_key_composite_value").textbox("setValue",selRow.partition_key_composite_value);
	$("#detail_form #partition_key_composite_value").val(selRow.partition_key_composite_value);
	$("#detail_form #spare_partition_cnt").textbox("setValue",selRow.spare_partition_cnt);
	$("#detail_form #crud_flag").val("U");
//	$("#detail_form #db_abbr_nm").textbox({readonly:true});
//	$("#detail_form #hint_nm").textbox({readonly:false});
//	$("#detail_form #hint_reg_dt").textbox({readonly:true});
//	$("#detail_form #hint_reg_id").textbox({readonly:true});


}



function Btn_deletePartition(){
	var data = $("#partitionList").datagrid('getSelected');
	
	if(data == null) {
		parent.$.messager.alert('','테이터를 선택해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('', '[ 테이블명 : ' + data.table_name + ' ]' + ' 을(를) 정말로 삭제하시겠습니까?', function(check) {
		if (check) {
			ajaxCall("/deletePartitionTable", 
					$("#detail_form"), 
					"POST",
					callback_deletePartitonAction);
		}
	});
	
}

var callback_deletePartitonAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_getPartitionList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
	
	selectTable();
};


function Btn_savePartition(){
	var crud_flag = $("#detail_form #crud_flag").val();
	
	
	if($("#table_name").combobox('getValue') == ""){
		parent.$.messager.alert('','테이블명을 선택해 주세요.');
		return false;
	}
	
	if($("#partition_work_type_cd_nm").combobox('getValue') == ""){
		parent.$.messager.alert('','파티션 작업유형을 선택해 주세요.');
		return false;
	}
	if($("#partition_interval").textbox('getValue') == ""){
		parent.$.messager.alert('','파티션 간격을 입력해주세요.');
		return false;
	}
	if($("#partition_interval_type_cd_nm").combobox('getValue') == ""){
		parent.$.messager.alert('','파티션 간격유형을 선택해 주세요.');
		return false;
	}
	if($("#compress_yn").val() == ""){
		parent.$.messager.alert('','압축여부를 선택해 주세요.');
		return false;
	}
	if($("#shelf_life_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','보관기간입력해 주세요.');
		return false;
	}
	if($("#shelf_life_type_cd_nm").combobox('getValue') == ""){
		parent.$.messager.alert('','보관기유형을 선택해 주세요.');
		return false;
	}
	if($("#spare_partition_cnt").textbox('getValue') == ""){
		parent.$.messager.alert('','예비파티션수를 입력해 주세요.');
		return false;
	}
	
	if($('#partition_interval').textbox('getValue') > 2147483647 ||
			$('#partition_interval').textbox('getValue') <= 0 ) {
		parent.$.messager.alert('','파티션 간격은 1 ~ 2147483647를 허용합니다.');
		return false;
	}
	
	if($('#shelf_life_cnt').textbox('getValue') > 2147483647 ||
			$('#shelf_life_cnt').textbox('getValue') <= 0 ) {
		parent.$.messager.alert('','보관기간은 1 ~ 2147483647를 허용합니다.');
		return false;
	}
	
	if(byteLength($('#partition_key_composite_value').val()) > 200) {
		parent.$.messager.alert('','파티션키구성값 정보가 200 Byte를 초과 하였습니다.');
		return false;
	}
	
	if($('#spare_partition_cnt').textbox('getValue') > 2147483647 ||
			$('#spare_partition_cnt').textbox('getValue') <= 0 ) {
		parent.$.messager.alert('','예비파티션수는  1 ~ 2147483647를 허용합니다.');
		return false;
	}
	
	/*if(crud_flag === 'C'){
	
		
		var partition_work_type_cd_nm = $("#partition_work_type_cd_nm").combobox("getValue");
		var partition_interval_type_cd_nm = $("#partition_interval_type_cd_nm").combobox("getValue");
		var shelf_life_type_cd_nm = $("#shelf_life_type_cd_nm").combobox("getValue");
		
		if(partition_work_type_cd_nm === '월단위RANGE파티션'){
			$("#partition_work_type_cd").val("1");
		}else{
			$("#partition_work_type_cd").val("2");
		}
		
		if(partition_interval_type_cd_nm === 'DAY'){
			$("#partition_interval_type_cd").val("1");
		}else{
			$("#partition_interval_type_cd").val("2");
		}
		
		if(shelf_life_type_cd_nm === 'DAY'){
			$("#shelf_life_type_cd").val("1");
		}else{
			$("#shelf_life_type_cd").val("2");
		}
		
		alert("확인");
	}*/
	

	
	
//	if(crud_flag == 'U'){
//		parent.$.messager.confirm('','\''+$("#hint_nm").textbox('getValue')+'\''+' 힌트를 정말로 수정하시겠습니까?',function(check){
//			if(check){
//				ajaxCall("/deletePartitonMenuInfo",
//						$("#detail_form"),
//						"POST",
//						callback_deletePartitonMenuInfo);			
//			}
//		});
//	}

	ajaxCall("/savePartitionTable",
			$("#detail_form"),
			"POST",
			callback_savePartitionAction);		
}
//callback 함수
var callback_savePartitionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_getPartitionList();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
	selectTableName();
};

/*function selectWorkType(rec){
	$("#detail_form #partition_work_type_cd").val(rec.partition_work_type_cd);
}
function selectIntervalType(rec){
	$("#detail_form #partition_interval_type_cd").val(rec.partition_interval_type_cd);
}
function selectLifeType(rec){
	$("#detail_form #shelf_life_type_cd").val(rec.shelf_life_type_cd);
}
*/


function Btn_ResetField(){
	$("#partitionList").datagrid('clearSelections');
	$("#detail_form #table_name").textbox("setValue","");
//	$("#detail_form #table_name").combobox("setValue",""); //나중에 추가
	$("#detail_form #partition_interval_type_cd").val("");
	$("#detail_form #table_name").combobox("setValue","");
	$("#detail_form #partition_work_type_cd_nm").combobox("setValue","");
	$("#detail_form #partition_interval").textbox("setValue","");
	$("#detail_form #partition_interval_type_cd_nm").combobox("setValue","");
	$("#detail_form #compress_yn").textbox("setValue","");
	$("#detail_form #crud_flag").val("C");
	$("#detail_form #shelf_life_cnt").textbox("setValue","");
	$("#detail_form #shelf_life_type_cd_nm").combobox("setValue","");
	$("#detail_form #partition_key_composite_value").val("");
	$("#detail_form #spare_partition_cnt").textbox("setValue",$('#user_id').val());
	
//	$("#detail_form #hint_reg_id").textbox({disabled: true});
//	$("#detail_form #hint_nm").textbox({readonly:false});

	
//	for(var i = 0; i < 4; i++){
//		$("input[id='auth_id"+i+"']:checkbox").removeProp("checked");
//	}
}
