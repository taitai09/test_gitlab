$(document).ready(function() {
	var height = $("#sqlImprove").height();
	top.resizeTopFrame($("#menu_id").val(), height);
	parent.resizeSqlHistoryIF(height);	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#update_dt").val(row.update_dt);
			$("#tuning_complete_dt").val(row.update_dt);
			getDetailView();
		},			
		columns:[[
			{field:'tuning_no',hidden:"true", rowspan:2},
			{field:'update_dt',title:'변경 일시',width:"10%",halign:"center",align:'center',sortable:"true", rowspan:2},
			{title:'개선전',halign:"center",colspan:3},
			{title:'개선후',halign:"center",colspan:3},
			{title:'개선율(%)',halign:"center",colspan:3}
		],[
			{field:'imprb_elap_time',title:'응답시간(Sec)',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'imprb_buffer_cnt',title:'블럭수',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'imprb_pga_usage',title:'PGA 사용량(MB)',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'impra_elap_time',title:'응답시간(Sec)',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'impra_buffer_cnt',title:'블럭수',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'impra_pga_usage',title:'PGA 사용량(MB)',width:"10%",halign:"center",sortable:"true",align:'right'},			
			{field:'elap_time_impr_ratio',title:'응답시간(Sec)',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'buffer_impr_ratio',title:'블럭수',width:"10%",halign:"center",sortable:"true",align:'right'},
			{field:'buffer_impr_ratio',title:'PGA 사용량(MB)',width:"10%",halign:"center",sortable:"true",align:'right'}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

	ajaxCall("/ImprovementManagement/SQLImproveHistoryAction",
		$("#submit_form"),
		"POST",
		callback_SQLImproveHistoryAddTable);		
});

//callback 함수
var callback_SQLImproveHistoryAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
};

function getDetailView(){
	$("#submit_form").attr("action","/ImprovementManagement/SQLImproveHistoryView");
	$("#submit_form").submit();	
}