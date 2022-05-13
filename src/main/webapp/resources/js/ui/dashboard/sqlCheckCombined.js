$(document).ready(function() {
	$("#tableList").treegrid({
		idField:'wrkjob_cd',
		treeField:'wrkjob_nm',
		rownumbers : true,
		lines : true,
		singleSelect : false,
		onClickRow : function(row) {
        	var menuId = "100";
        	var menuNm = "SQL 진단";
        	var menuUrl = "/SQLDiagnostics/Summary";
        	var menuParam = "";			        	
            
            menuParam = "dbid="+row.dbid+"&wrkjob_cd="+row.wrkjob_cd+"&gather_day="+$("#gather_day").datebox("getValue")+"&call_from_parent=Y";
			
			parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
		},
		onClickCell : function(index,field,value) {
//			console.log("index:",index);
//			console.log("field:",field);
//			console.log("value:",value);
			
//			var afterMonth="";
//			if(field == 'after_1_month'){
//				afterMonth=1;
//			}else if(field == 'after_2_month'){
//				afterMonth=2;
//			}else if(field == 'after_3_month'){
//				afterMonth=3;
//			}else if(field == 'after_6_month'){
//				afterMonth=6;
//			}else if(field == 'after_12_month'){
//				afterMonth=12;
//			} 
//			var row = $(this).treegrid('getRows')[index];
//
//			var menuId = $("#menu_id").val()+1;
//        	var menuNm = "CPU 한계점 예측";
//        	var menuUrl = "/CPULimitPointPrediction";
//        	var menuParam = "dbid="+row.dbid+"&resource_type="+row.type+"&predict_standard="+afterMonth;
//
		},		
		columns:[
			[
				{field:'db_id',hidden:"true",rowspan:2},
				{field:'wrkjob_cd',hidden:"true",rowspan:2},
				{field:'parent_wrkjob_cd',hidden:"true",rowspan:2},
				{field:'db_name',title:'DB',halign:"center",align:"center",sortable:"true",rowspan:2},
				{field:'plan_cnt',title:'플랜변경',halign:"center",align:'right',sortable:"true",rowspan:2},
				{field:'new_cnt',title:'신규',halign:"center",align:'right',sortable:"true",rowspan:2},
				{title:'LITERAL',halign:"center",colspan:2},
				{field:'temp_cnt',title:'TEMP 과다사용',halign:"center",align:'right',sortable:"true",rowspan:2},
				{field:'fullscan_cnt',title:'FULLSCAN',halign:"center",align:'right',sortable:"true",rowspan:2},
				{field:'delete_cnt',title:'조건절 없는 DELETE',halign:"center",align:'right',sortable:"true",rowspan:2},
				{field:'wrkjob_nm',title:'업무',halign:"center",align:"center",sortable:"true",rowspan:2},
				{field:'timeount_cnt',title:'타임아웃',halign:"center",align:'right',sortable:"true",rowspan:2},
				{field:'elpased_time_delay_cnt',title:'응답지연',halign:"center",align:'right',sortable:"true",rowspan:2},
			],
			[
				{field:'literal_sql_text_cnt',title:'SQL_TEXT기반',halign:"center",align:'right',sortable:"true"},
				{field:'literl_plan_hash_value_cnt',title:'PLAN_HASH_VALUE',halign:"center",align:'right',sortable:"true"},
			]
		],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	Btn_OnClick();
});

function Btn_OnClick(){
	$('#tableList').treegrid("loadData", []);
	ajaxCall("/DashboardV2/getSqlCheckCombined", $("#submit_form"), "POST", callback_SqlCheckCombined);
//	ajaxCall("/DashboardV2/getSqlCheckCombinedResult", $("#submit_form"), "POST", callback_callback_SqlCheckCombinedResult);
}

//callback 함수
var callback_SqlCheckCombined = function(result) {
	treegrid_json_string_callback_common(result,'#tableList',false);
};

var callback_callback_SqlCheckCombinedResult = function(result) {
		parent.$.messager.progress('close');
		$('#tableList').treegrid("loadData", []);
		if(result.result){		
			var data = JSON.parse(result.txtValue);
			$('#tableList').treegrid("loadData", data);
			console.log("data1:",data);
			$('#tableList').treegrid('loaded');			

		}else{
			parent.$.messager.alert('ERROR',result.message,'error', function(){
				$('#tableList').treegrid("loadData", []);	
			});
		}
};
