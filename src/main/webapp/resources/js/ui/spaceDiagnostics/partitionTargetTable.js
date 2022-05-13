$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회			
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadSuccess: function(){
			$("#selectCombo").combobox("textbox").attr("placeholder","선택");
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	$("#partitionList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#owner").val(row.owner);
			$("#table_name").val(row.table_name);
			
			searchDetail();
		},			
		columns:[[
			{field:'gather_day',title:'진단일',halign:"center",align:'center',formatter:getDateFormat,sortable:"true"},
			{field:'owner',title:'OWNER',halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"center",sortable:"true"},
			{field:'cur_size',title:'현재 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_1_ago_size',title:'1개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_2_ago_size',title:'2개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_3_ago_size',title:'3개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_4_ago_size',title:'4개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_5_ago_size',title:'5개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_6_ago_size',title:'6개월전 사이즈(GB)',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'cur_num_rows',title:'현재 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_1_ago_num_rows',title:'1개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_2_ago_num_rows',title:'2개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_3_ago_num_rows',title:'3개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_4_ago_num_rows',title:'4개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_5_ago_num_rows',title:'5개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'month_6_ago_num_rows',title:'6개월전 건수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'reads_activity',title:'READS_ACTIVITY',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'recommend_part_type',title:'RECOMMEND_PART_TYPE',halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#accessPathList").datagrid({
		view: myview,		
		columns:[[
			{field:'rnum',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'access_path',title:'ACCESS_PATH',halign:"center",align:"left",sortable:"true"},
			{field:'access_path_count',title:'ACCESS_PATH_COUNT',halign:"center",align:'right',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#partitionKeyList").datagrid({
		view: myview,		
		columns:[[
			{field:'column_name',title:'COLUMN_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'usage_cnt',title:'USAGE_CNT',halign:"center",align:'right',sortable:"true"},
			{field:'partition_key_recommend_rank',title:'PARTITION_KEY_RECOMMEND_RANK',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
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

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#partitionList').datagrid("loadData", []);
	$('#accessPathList').datagrid("loadData", []);
	$('#partitionKeyList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("파티셔닝 대상 테이블"," "); 		

	ajaxCall("/PartitionTargetTable/PartitionRecommendation",
		$("#submit_form"),
		"POST",
		callback_PartitionRecommendationAddTable);
}

//callback 함수
var callback_PartitionRecommendationAddTable = function(result) {
	json_string_callback_common(result,'#partitionList',true);
};

function searchDetail(){
	$('#accessPathList').datagrid("loadData", []);
	$('#partitionKeyList').datagrid("loadData", []);
	
	ajaxCall("/PartitionTargetTable/AccessPath",
			$("#submit_form"),
			"POST",
			callback_AccessPathAction);
	
	ajaxCall("/PartitionTargetTable/PartitionKeyRecommendation",
			$("#submit_form"),
			"POST",
			callback_PartitionKeyRecommendationAction);	
}

//callback 함수
var callback_AccessPathAction = function(result) {
	json_string_callback_common(result,'#accessPathList',true);
	$("#accessPathList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

//callback 함수
var callback_PartitionKeyRecommendationAction = function(result) {
	if(result.result){
		var post = result.object;
//		$("#rowCount").html("<b>총 " + post.total_cnt + " 건</b>");
		$(".panel-title").eq(2).append("<span class='h3' style='margin-left:0px;'> (총 "+post.usage_cnt+"건)</span>");

		var data = JSON.parse(result.txtValue);
		$('#partitionKeyList').datagrid("loadData", data);
		$("#partitionKeyList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		//$("#partitionKeyList").parent().select("td").css("cursor","initial");

	}else{
		parent.$.messager.alert('',result.message);
		$("#rowCount").html("<b>총 0 건</b>");
	}
};