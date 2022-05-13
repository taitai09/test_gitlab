var coulmnValues ="";  
var post = "";
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?isChoice=X",
		method:'get',
		valueField:'id',
		textField:'text',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$("#search_wrkjob_cd").combobox("textbox").attr("placeholder","선택");
		}
	});
	
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
	
	
	makeCoulmnValues();
	
	
});


function makeCoulmnValues(){
	ajaxCall("/PerformanceImprovementStatus/makeColumnsValues?grp_cd_id=1003",
			null,
			"POST",
			callback_makeCloumnValuesAction);
};

//callback 함수
var callback_makeCloumnValuesAction = function(result) {
	resultObj = result.object;
	getByTypeListGrid(resultObj);
	
};



function getByTypeListGrid(resultObj){
	

	var jsonArray = new Array();
	var jsonMap = new Object();
	jsonMap.field='wrkjob_cd_nm';
	jsonMap.title='업무';
	jsonMap.width='8%';
	jsonMap.halign='center';
	jsonMap.align='center';
	
	jsonArray.push(jsonMap);

	for(var i = 0; i < resultObj.length; i++){
		jsonMap = new Object();  //맵객체를 새로만듬.
		jsonMap.field='coulmn_'+i;
		jsonMap.title=resultObj[i].wrkjob_cd_nm;
		jsonMap.width='8%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	 }
	 
	console.log(jsonArray[0].title);
	
	$("#tableList").datagrid({
			view: myview,
			columns:[jsonArray],
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
};

function getByTypeListGrid_old(){
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'wrkjob_cd_nm',title:'업무',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_0',title:'자동선정',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_1',title:'수동선정',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_2',title:'요청',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_3',title:'FULLSCAN',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_4',title:'플랜변경',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_5',title:'신규SQL',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_6',title:'TEMP과다사용',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_7',title:'APP타임아웃',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_8',title:'APP응답시간지연',width:"10%",halign:"center",align:'center'},
			{field:'coulmn_9',title:'성능분석',width:"10%",halign:"center",align:'center'}
			]],
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			}
	});
};



function Btn_OnClick(){
	if($("#search_wrkjob_cd").combotree('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
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
	
	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("요청 유형별 성능개선현황"," ");
	
		ajaxCall("/PerformanceImprovementStatus/ByRequestType",
				$("#submit_form"),
				"POST",
				callback_ByTypeListAction);
}


//callback 함수
var callback_ByTypeListAction = function(result) {
	json_string_callback_common(result,'#tableList',true);

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
};

function Excel_Download(){
	
	if($("#search_wrkjob_cd").combotree('getValue') == ""){
		parent.$.messager.alert('','업무를 선택해 주세요.');
		return false;
	}
	
	$("#submit_form").attr("action","/PerformanceImprovementStatus/ByRequestType/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}


