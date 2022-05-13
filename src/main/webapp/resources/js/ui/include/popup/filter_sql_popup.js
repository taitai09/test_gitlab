var tuning_prgrs_step_seq = "";
var project_id = "";
var params ="";
var filter_sql = "";
$(document).ready(function() {
	
//	var Top = (WindowHeight - 150) / 2;
//	var Left = (WindowWidth - 270) / 2; 
//	if (Top < 0) Top = 0;
//	if (Left < 0) Left = 0;
	
	$('#filterSqlPopup').window({
		title : "Filter SQL",
		top:getWindowTop(800),
		left:getWindowLeft(440)
	});
	
	
	//프로젝트 조회
	$('#filter_sql_form #condition_1').combobox({
		onSelect: function(rec){

			project_id = rec.project_id;
			
			if(project_id != null && project_id != ''){
				//튜닝진행단계 조회
				$('#assign_form #tuning_prgrs_step_seq').combobox({
					url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?isChoice=Y&project_id="+project_id,
					method:"get",
					valueField:'tuning_prgrs_step_seq',
					textField:'tuning_prgrs_step_nm',
					onSelect: function(rec){
						tuning_prgrs_step_seq = rec.tuning_prgrs_step_seq;
					},
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
	
	//filter_sql
	$('#filter_sql_form #filter_sql').textbox({
		onChange: function(newval, oldval){
			filter_sql = newval;
		},
	});	
	
	
	
});

function addSql(){

	if($('#condition_1').combobox('getValue') == "" || $('#condition_2').combobox('getValue') == ""){
		parent.$.messager.alert('','Filter Sql을 정확히 선택해 주세요.');
		return false;
	}
	
	filter_sql = $("#filter_sql_form #filter_sql").textbox("getValue") + "\n";
	
	var condition_1 = $("#condition_1").combobox("getValue");
	var condition_2 = $("#condition_2").combobox("getValue");
	if(condition_2 == 'LIKE' || condition_2 == 'NOT LIKE'){
		filter_sql += "AND " + condition_1 + " " +condition_2+" "+"\n ";
	}else{
		filter_sql += "AND " + condition_1 + " " +condition_2+"( "+ " )"+"\n ";
	}
	
	$("#filter_sql_form #filter_sql").textbox("setValue",filter_sql);
}

function Btn_OnCloseSqlPopup(){
	
	$("#filter_sql_form #filter_sql").textbox("setValue",old_filter_sql);
	$("#submit_form #extra_filter_predication").textbox("setValue",old_filter_sql);
	Btn_OnClosePopup('filterSqlPopup');
}

function Btn_SaveSql(){	

	$("#submit_form #extra_filter_predication").textbox("setValue",filter_sql);
	Btn_OnClosePopup('filterSqlPopup');
}

function Btn_OnAssignAllPopClose(){
	$('#tuningAssignAllPop').window("close");
}