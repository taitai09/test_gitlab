$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#dbid").val(row.dbid);
			$("#inst_id").val(row.inst_id);
			$("#prediction_dt").val(row.prediction_dt);
			
			getDetailView();
		},			
		columns:[[
			{field:'dbid',hidden:"true",rowspan:2},
			{field:'inst_id',hidden:"true",rowspan:2},
			{field:'prediction_dt',hidden:"true",rowspan:2},
			{field:'db_name',title:'DB',width:"5%",halign:"center",align:'center',sortable:"true", rowspan:2},
			{field:'inst_nm',title:'INSTANCE',width:"5%",halign:"center",sortable:"true",align:'center', rowspan:2},
			{field:'prediction_date',title:'예측기준일',width:"5%",halign:"center",align:'center', rowspan:2},
			{field:'physical_memory_size',title:'Physical Memory',width:"10%",halign:"center",align:'center', rowspan:2},
			{title:'SGA+PGA Memory Size(GB)',halign:"center",colspan:9}
		],[
			{field:'before_3_month_mem_usage',title:'3개월전',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'before_2_month_mem_usage',title:'2개월전',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'before_1_month_mem_usage',title:'1개월전',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'current_mem_usage',title:'현재',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'after_1_month_mem_usage',title:'1개월후',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'after_2_month_mem_usage',title:'2개월후',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'after_3_month_mem_usage',title:'3개월후',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'after_6_month_mem_usage',title:'6개월후',width:"8%",halign:"center",sortable:"true",align:'right'},
			{field:'after_12_month_mem_usage',title:'12개월후',width:"8%",halign:"center",sortable:"true",align:'right'}
		]],

		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	var call_from_parent = $("#call_from_parent").val();
	console.log("call_from_parent :"+call_from_parent);
	if(call_from_parent == "Y"){
		$("#selectPredictStandard").combobox("setValue",3);
		Btn_OnClick();
	}
});

function Btn_OnClick(){
	if($('#selectPredictStandard').combobox('getValue') == ""){
		parent.$.messager.alert('','예측 기준을 선택해 주세요.');
		return false;
	}

	$('#tableList').datagrid("loadData", []);
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("MEMORY 한계점 예측"," "); 
	
	$("#predict_standard").val($("#selectPredictStandard").combobox("getValue"));

	ajaxCall("/MEMORYLimitPointPrediction",
			$("#submit_form"),
			"POST",
			callback_MEMORYLimitPointPredictionAddTable);
}

//callback 함수
var callback_MEMORYLimitPointPredictionAddTable = function(result) {
	json_string_callback_common(result, '#tableList', "Y");	
};

function getDetailView(){
    menuParam = "predict_standard="+$("#selectPredictStandard").combobox('getValue')+"&dbid="+$("#dbid").val()+"&inst_id="+$("#inst_id").val()+"&prediction_dt="+$("#prediction_dt").val();

	/* 탭 생성 */
	parent.openLink("Y", "1861", "MEMORY 한계점 예측 상세", "/MEMORYLimitPointPredictionDetail", menuParam);	
}