$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	getByTypeList();
//	Btn_OnClick();
	
	
	//프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/Common/getProject?isAll=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(rec){
			if(rec.project_id == ''){
				$("#tuning_prgrs_step_seq").combobox('setValue','');
			}
				
			project_id = rec.project_id;
			
			if(project_id != null && project_id != ''){
				//튜닝진행단계 조회
				$('#submit_form #tuning_prgrs_step_seq').combobox({
					url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?isAll=Y&project_id="+project_id,
					method:"get",
					valueField:'tuning_prgrs_step_seq',
					textField:'tuning_prgrs_step_nm',
//					onSelect: function(rec){
//						tuning_prgrs_step_seq = rec.tuning_prgrs_step_seq;
//					},
					onLoadError: function(){
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
				});	
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});	
	
});


function getByTypeList(){
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'base_day', title:'기준일',width:"10%",halign:"center",align:'center'},
			{field:'online_cnt',title:'온라인',width:"10%",halign:"center",align:'center'},
			{field:'batch_cnt',title:'배치',width:"10%",halign:"center",align:'center'},
			{field:'etc_cnt',title:'기타',width:"10%",halign:"center",align:'center'},
			{field:'all_cnt',title:'전체',width:"10%",halign:"center",align:'center'}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
};



function Btn_OnClick(){
	if($('#search_StartDate').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	} else {
		if($('#search_endDate').textbox('getValue') == ""){
			parent.$.messager.alert('','종료일자를 선택해 주세요.');
			return false;
		}
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
//	getByTypeList();
	
	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("프로그램 유형별 성능개선현황"," ");
	
		ajaxCall("/PerformanceImprovementStatus/ByProgramType",
				$("#submit_form"),
				"POST",
				callback_ByTypeListAction);
}


//callback 함수
var callback_ByTypeListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getByTypeList();

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Excel_Download(){
	$("#submit_form").attr("action","/PerformanceImprovementStatus/ByProgramType/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}


