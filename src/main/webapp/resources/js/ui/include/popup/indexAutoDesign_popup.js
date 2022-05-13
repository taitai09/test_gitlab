$(document).ready(function() {
	$('#indexAutoDesignPop').window({
		title : "테이블 인덱스 자동설계",
		top:getWindowTop(700),
		left:getWindowLeft(900)
	});

	$('#selectivity_method1').radiobutton({
		onChange:function(val){
			if(val == true){
				$("#submit_form #selectivity_calc_method").val("STAT");
			}else{
				$("#submit_form #selectivity_calc_method").val("DATA");
			}
		}
	});
	
	let ndvRatioTooltip2 = "통계정보 기반 </br>selectivity 계산";
	let colNullTooltip2 = "데이터 샘플링 기반</br>" +
	"selectivity 계산";

	$('#selectivity_statistics_tooltip').tooltip({
		content : '<span style="color:#fff">' + ndvRatioTooltip2 + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});
	
	$('#selectivity_data_tooltip').tooltip({
		content : '<span style="color:#fff">' + colNullTooltip2 + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});
	
	$("#indexResultList").datagrid({
		columns:[[
			{field:'seq',title:'NO',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"20%",halign:"center",align:'left'},
			{field:'recommend_type',title:'추천 유형',width:"20%",halign:"center",align:'left'},
			{field:'access_path_column_list',title:'인덱스 컬럼',width:"50%",halign:"center",align:'left'},
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});	
});

function Btn_StartIndexAutoDesign(){
	/*if($('#indexAutoDesign_form #ndv_ratio').textbox('getValue') == ""){
		$.messager.alert('','NDV Ratio값을 입력해 주세요.');
		return false;
	}
	
	if($('#indexAutoDesign_form #col_null').textbox('getValue') == ""){
		$.messager.alert('','NULL 처리 기준건수를 입력해 주세요.');
		return false;
	}
	
	let ndv_ratio = $('#indexAutoDesign_form #ndv_ratio').textbox('getValue');
	let col_null = $('#indexAutoDesign_form #col_null').textbox('getValue');
	let pre_ndv_ratio = "범위 검색 조건 선택도";
	let pre_col_null = "인덱스 제외 NDV 기준건수";
	
	if(ndv_ratio == ""){
		parent.errorMessager(pre_ndv_ratio + ' 값을 입력해 주세요.');
		return false;
	}
	
	if(col_null == ""){
		parent.errorMessager(pre_col_null + ' 값를 입력해 주세요.');
		return false;
	}
	
	if(!$.isNumeric(ndv_ratio)){
		parent.errorMessager(pre_ndv_ratio + ' 값은 숫자형만 입력해 주세요.');
		return false;
	}
	
	if(!$.isNumeric(col_null)){
		parent.errorMessager(pre_col_null + ' 값은 숫자형만 입력해 주세요.');
		return false;
	}
	
	if(ndv_ratio < 0 || ndv_ratio > 1){
		parent.errorMessager(pre_ndv_ratio + ' 값은 0과 1 사이를 입력해 주세요.');
		return false;
	}
	
	if(col_null < 1){
		parent.errorMessager(pre_col_null + ' 값은 0 이상으로 입력해 주세요.');
		return false;
	}*/

	
	
	/* Index Auto Design */
	$('#indexResultList').datagrid('loadData',[]);

	var indexTxt = "";
	
	if($("#indexAutoDesign_form #access_path_type").val() == "VSQL"){
		indexTxt = "수집SQL 인덱스 자동 설계 진행중입니다.";
	}else{
		indexTxt = "적재SQL 인덱스 자동 설계 진행중입니다.";
	}
	
	/* modal progress open */
//	openMessageProgress("테이블 인덱스 자동 설계",indexTxt); 
	if(parent.openMessageProgress != undefined){
		parent.openMessageProgress("테이블 인덱스 자동 설계",indexTxt);
	}

	ajaxCall("/IndexDesignMaintenance/StartIndexAutoDesign",
			$("#indexAutoDesign_form"),
			"POST",
			callback_StartIndexAutoDesignAction);		
}

//callback 함수
var callback_StartIndexAutoDesignAction = function(result) {
	if(result.result){
		var data = JSON.parse(result.txtValue);
		$('#indexAutoDesign_form #indexResultList').datagrid("loadData", data);
		
		$("#indexResultList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		$("#indexAutoDesign_form #applyBtn").linkbutton({disabled:false});
	}else{
		parent.infoMessager("추천 인덱스가 없습니다.");
		$("#indexAutoDesign_form #applyBtn").linkbutton({disabled:true});
	}
	
	/* modal progress close */
//	closeMessageProgress();
	if(parent.closeMessageProgress != undefined){
		parent.closeMessageProgress();
	}
};

function Btn_AppendIndexDesignList(){
	var rows = $('#indexAutoDesign_form #indexResultList').datagrid('getRows');

	$.each(rows, function(i, row) {
//		var designRowCnt = eval("if_"+frameName).$('#indexDesignList').datagrid('getRows').length;
//		eval("if_"+frameName).$("#indexDesignList").datagrid('appendRow',{index_seq:(designRowCnt+1),index_column_list:row.access_path_column_list,reg_dt:''});
//		
//		eval("if_"+frameName).$("#indexDesignList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		var designRowCnt = $('#indexDesignList').datagrid('getRows').length;
		$("#indexDesignList").datagrid('appendRow',{index_seq:(designRowCnt+1),index_column_list:row.access_path_column_list,reg_dt:''});
		
		$("#indexDesignList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
	});
	
	Btn_OnClosePopup("indexAutoDesignPop");
}