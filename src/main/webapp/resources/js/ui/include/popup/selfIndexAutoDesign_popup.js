$(document).ready(function() {
	
	$('#selectivity_method1').radiobutton({
		onChange:function(val){
			if( val == true ){
				$("#submit_form #selectivity_calc_method").val("STAT");
				
			} else {
				$("#submit_form #selectivity_calc_method").val("DATA");
			}
		}
	});
	
	$('#selfIndexAutoDesignPop').window({
		title : "셀프 성능 점검 인덱스 자동설계",
		top:getWindowTop(1000),
		left:getWindowLeft(900)
	});
	
	let ndvRatioTooltip = "통계정보 기반 </br>selectivity 계산";
	let colNullTooltip = "데이터 샘플링 기반</br>" +
	"selectivity 계산";
	
	$('#selectivity_statistics_tooltip').tooltip({
		content : '<span style="color:#fff">' + ndvRatioTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});
	
	$('#selectivity_data_tooltip').tooltip({
		content : '<span style="color:#fff">' + colNullTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});
	
	$("#selfIndexResultList").datagrid({
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

function Btn_StartSelfIndexAutoDesign(){
	if( isBeforeInspection() ){
		ajaxCall_selfIndexAutoDesign();
	}
}

function isBeforeInspection(){
	let defaultText = $("#submit_form #defaultText").val();
	let sqlText = $("#submit_form #sql_text").val();
	
	if( isEmpty(defaultText) ){
		warningMessager('성능 점검 수행 전입니다.<br>성능 점검을 수행한 후 인덱스 자동설계를 진행해 주세요.');
		return false;
		
	}else if( defaultText !== sqlText ){
		let message = 'SQL Text가 수정되었습니다. 성능 점검을 수행한 SQL Text로 인덱스 자동설계를 진행하시겠습니까?';
		
		$.messager.confirm('', message, function(check) {
			if(check){
				ajaxCall_selfIndexAutoDesign();
			}
		});
		return false;
		
	}else {
		return true;
	}
}

function ajaxCall_selfIndexAutoDesign(){
	$("#submit_form #dbid").val($('#selectDbOfWrkjobCd').combobox('getValue'));

	/* Index Auto Design */
	$('#selfIndexResultList').datagrid('loadData',[]);
	
	/* modal progress open */
	parent.openMessageProgress("셀프튜닝 인덱스 자동 설계","셀프튜닝 인덱스 자동 설계 진행중입니다."); 	
	
	ajaxCall("/SelfTuning/StartSelfIndexAutoDesign",
			$("#submit_form"),
			"POST",
			callback_StartSelfIndexAutoDesignAction);
}

//callback 함수
var callback_StartSelfIndexAutoDesignAction = function(result) {
	
	if( result.result ){
		var data = JSON.parse(result.txtValue);	
		$('#selfIndexResultList').datagrid("loadData", data);
		$("#selfIndexResultList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var post = result.object[result.object.length-1];
		$('#idx_ad_no').val(post.idx_ad_no);
		
	} else {
		let msg = "";
		let lastIndex = result.message.lastIndexOf("ORA-");
		
		if( lastIndex != -1 ) {
			parent.errorMessager(result.message.substring(lastIndex));
			
		} else {
			parent.infoMessager("추천 인덱스가 없습니다.");
		}
	}
	
	/* modal progress close */
	parent.closeMessageProgress();
};

function Btn_SelfIndexExcelDown(){
	$("#submit_form").attr("action","/SelfTuning/SelfIndexAutoDesign/ExcelDown");
	$("#submit_form").submit();	
}