var checkURL = 'BasicCheckConfig';

$(document).ready(function() {
	// DB운영유형 조회			
	$('#select_dbid').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
	    onChange : function(newValue, oldValue){
    		
	    	$('#tableList').datagrid('loadData',[]);

	    	if(newValue ==='' || newValue === 'undefiend'){
	    		checkURL = 'BasicCheckConfig';
	    		getPerformanceList();
	    	}else{
	    		checkURL = 'DBCheckConfig';
	    		getPerformanceList();
	    	}
	    	Btn_ResetField();
	    	
	    },
		onLoadError: function(){	
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
	
	$('#dbid').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});
	
	
	
	$('#check_grade_cd').combobox({
	    url:"/getGradeCd",
	    method:"get",
		valueField:'check_grade_cd',
	    textField:'check_grade_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(result){
			console.log("result", result);
		},
	});
	
	$('#check_class_div_cd').combobox({
		url:"/getClassDivCd",
		method:"get",
		valueField:'check_class_div_cd',
		textField:'check_class_div_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(result){
			console.log("result", result);
		},
	});

	
//	$('#check_pref_id').combobox({
//		url:"/getCheckPrefValue",
//		method:"get",
//		valueField:'check_pref_id',
//		textField:'check_pref_id',
//		onChange: function(newValue, oldValue){
//			$('#check_pref_nm').combobox("setValue", newValue);
//		},
//		onLoadError: function(){
//			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
//			return false;
//		},
//	});
	
	
	
	
	
	Btn_OnClick();
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	$("#check_pref_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
});

//changeList
function getPerformanceList(){
	if(checkURL==='BasicCheckConfig'){
		$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
			},		
			columns:[[
				{field:'check_pref_id',title:'점검설정ID',width:"8%",halign:"center",align:"center",sortable:"true"},
				{field:'check_pref_nm',title:'점검설정명',width:"18%",halign:"center",align:'left'},
				{field:'check_enable_yn',title:'점검활성화여부',width:"8%",halign:"center",align:'center'},
				{field:'check_value_unit',title:'점검값단위',width:"10%",halign:"center",align:'center'},
				{field:'default_threshold_value',title:'기본임계값',width:"8%",halign:"center",align:'center'},
				{field:'check_grade_cd',hidden : 'true'},
				{field:'check_grade_cd_nm',title:'점검등급코드명',width:"14%",halign:"center",align:'center'},
				{field:'check_class_div_cd',hidden : 'true'},
				{field:'check_class_div_cd_nm',title:'점검클래스구분코드명',width:"14%",halign:"center",align:'center'},
				{field:'emergency_action_yn',title:'긴급조치대상여부',width:"8%",halign:"center",align:'center'}
			]],
//	    	fitColumns:true,
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		$('#check_pref_nm').textbox("setValue","");
		
		changeForm();
		$("#div_check_pref_nm2").hide();
		$("#div_check_pref_nm1").show();
//		$("#div_check_pref_nm2").attr('disabled','disabled');
		
	}else{
		$("#tableList").datagrid({
			view: myview,
			onClickRow : function(index,row) {
				setDetailView(row);
				$('#check_pref_nm').textbox({readonly : true});
			},		
			
			columns:[[
				{field:'dbid',hidden : 'true'},
				{field:'db_name',title:'DB',width:"8%",halign:"center",align:"center",sortable:"true"},
				{field:'check_pref_id',title:'점검설정ID',width:"8%",halign:"center",align:'center',sortable:"true"},
				{field:'check_pref_nm',title:'점검설정명',width:"26%",halign:"center",align:'left', sortable:"true"},
				{field:'check_enable_yn',title:'점검활성화여부',width:"8%",halign:"center",align:'center'},
				{field:'threshold_value',title:'임계값',width:"8%",halign:"center",align:'center'}
			]],
//	    	fitColumns:true,
	    	onLoadError:function() {
	    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    	}
		});
		
		
		$('#check_pref_nm2').combobox({
			url:"/getCheckPrefValue",
			method:"get",
			valueField:'check_pref_id',
			textField:'check_pref_nm',
			onChange: function(newValue, oldValue){
				$('#check_pref_id').textbox("setValue", newValue);
			},
			onLoadError: function(){
				parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
				return false;
			},
		});
		
		changeForm();
		
		$("#div_check_pref_nm1").hide();
		$("#div_check_pref_nm2").show();
//		$("#div_check_pref_nm1").attr('disabled','disabled');

	}
	
	
};


function changeForm(){
	
	if(checkURL === 'BasicCheckConfig'){
		$("#dbid").combobox({disabled:true});
		$("#check_grade_cd").combobox({disabled:false});
		$("#check_value_unit").combobox({disabled:false});
		$("#check_class_div_cd").combobox({disabled:false});
		$("#emergency_action_yn").combobox({disabled:false});
		
	}else{
		$("#dbid").combobox({disabled:false});
		$("#check_pref_nm").textbox({readonly : false});
		$("#check_grade_cd").combobox({disabled:true});
		$("#check_value_unit").combobox({disabled:true});
		$("#check_class_div_cd").combobox({disabled:true});
		$("#emergency_action_yn").combobox({disabled:true});
	};
	
}

function Btn_OnClick(){
	if(($('#select_dbid').combobox('getValue') == "" && $("#select_dbid").textbox('getValue') != "") ||
		($('#select_dbid').combobox('getValue') != "" && $("#select_dbid").textbox('getValue') == "")){
		parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();

	$('#tableList').datagrid('loadData',[]);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일예방점검 설정관리"," ");
	
	
	if(checkURL === 'BasicCheckConfig'){
		ajaxCall("/getPerformanceBasicCheckList",
				$("#submit_form"),
				"POST",
				callback_PerformanceAction);
	}else{
		ajaxCall("/getPerformanceDBCheckList",
				$("#submit_form"),
				"POST",
				callback_PerformanceAction);
	}
}

//callback 함수
var callback_PerformanceAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	getPerformanceList();
};

function Btn_SaveSetting(){
	if(checkURL === 'BasicCheckConfig'){
	
		if($("#check_pref_nm").textbox('getValue') == ""){
			parent.$.messager.alert('','점검설정명을 입력해 주세요.');
			return false;
		}
		if($("#check_enable_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','점검활성화여부를 선택해 주세요.');
			return false;
		}	
//		if($("#check_value_unit").combobox('getValue') == ""){
//			parent.$.messager.alert('','점검값단위를 선택해 주세요.');
//			return false;
//		}	
//		if($("#default_threshold_value").textbox('getValue') == ""){
//			parent.$.messager.alert('','임계값을 입력해 주세요.');
//			return false;
//		}	
		if($("#check_grade_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','점검등급코드명을 선택해 주세요.');
			return false;
		}	
		
		if($("#check_class_div_cd").combobox('getValue') == ""){
			parent.$.messager.alert('','점검클래스구분코드명을 선택해 주세요.');
			return false;
		}	
		if($("#emergency_action_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','긴급조치대상여부를 선택해 주세요.');
			return false;
		}	
		
		
	}else{
		
		if($("#dbid").combobox('getValue') == ""){
			parent.$.messager.alert('','DBID를 입력해 주세요.');
			return false;
		}
		if($("#check_pref_id").textbox('getValue') == ""){
			parent.$.messager.alert('','점검설정ID를 입력해 주세요.');
			return false;
		}
		
		if($("#check_pref_nm2").combobox('getValue') == ""){
			parent.$.messager.alert('','점검설정명을 입력해 주세요.');
			return false;
		}
		
		if($("#check_enable_yn").combobox('getValue') == ""){
			parent.$.messager.alert('','점검활성화여부를 선택해 주세요.');
			return false;
		}	
//		if($("#default_threshold_value").textbox('getValue') == ""){
//			parent.$.messager.alert('','임계값을 입력해 주세요.');
//			return false;
//		}	
		
	}//end checkURL
	
	
	
	if(checkURL === 'BasicCheckConfig'){
		ajaxCall("/SaveBasicCheckConfig",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	}else{
		ajaxCall("/SaveDBCheckConfig",
				$("#detail_form"),
				"POST",
				callback_SaveSettingAction);		
	}
	
	
}

//callback 함수
var callback_SaveSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','데이터베이스 저장이 완료 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};






function Btn_DeleteSetting(){
	if(checkURL === 'BasicCheckConfig'){
		
		parent.$.messager.confirm('', '[ 점검설정ID : ' + $("#check_pref_id").textbox("getValue")+ ', 점검설정명 : '+$("#check_pref_nm").textbox("getValue")+ ' ]' + ' 을(를) 삭제할 수 없습니다. 사용을 안하시겠습니까?', function(check) {
			if (check) {
				$("#check_enable_yn").combobox("setValue","N");
				
				ajaxCall("/SaveBasicCheckConfig", 
						$("#detail_form"), 
						"POST",
						callback_SaveSettingAction);
			}
		});

	}else{
		
		parent.$.messager.confirm('', '[ ' + $("#check_pref_nm2").textbox("getValue")+ ' ]' + ' 을(를) 정말로 삭제하시겠습니까?', function(check) {
			if (check) {
				ajaxCall("/deleteDBCheckConfig", 
						$("#detail_form"), 
						"POST",
						callback_DeleteSettingAction);
			}
		});
		
	}
	
	
}

//callback 함수
var callback_DeleteSettingAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
				Btn_ResetField();
			},1000);	
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
//	$("#dbid").combobox("setValue", selRow.dbid);
	$("#check_pref_id").textbox("setValue", selRow.check_pref_id);
	$("#check_enable_yn").combobox("setValue", selRow.check_enable_yn);
	$("#check_value_unit").combobox("setValue", selRow.check_value_unit);

	$("#check_grade_cd").combobox("setValue", selRow.check_grade_cd);
//	$("#check_grade_cd").combobox("setText", selRow.check_grade_cd_nm);
	$("#check_class_div_cd").combobox("setValue", selRow.check_class_div_cd);
//	$("#check_class_div_cd").combobox("setText", selRow.check_class_div_cd_nm);
	$("#emergency_action_yn").combobox("setValue", selRow.emergency_action_yn);
	
	if(checkURL==='BasicCheckConfig'){
		$("#default_threshold_value").textbox("setValue", selRow.default_threshold_value);
		$("#check_pref_nm").textbox("setValue", selRow.check_pref_nm);
	}else{
		
		
		$("#dbid").combobox("setValue",select_dbid = $("#select_dbid").combobox("getValue"));
//		$("#dbid").combobox("setValue",select_dbid);
//		$("#dbid").textbox({readonly : true});
		$("#check_pref_nm2").textbox("setValue", selRow.check_pref_nm);
		$("#check_pref_nm2").textbox({readonly : true});
		$("#default_threshold_value").textbox("setValue", selRow.threshold_value);	
	};
	
	
//	alert($("#check_grade_cd").combobox("getValue"));
//	alert($("#check_class_div_cd").combobox("getValue"));
	
	
	
}

function Btn_ResetField(){
	$("#dbid").combobox("setValue", "");
	$("#check_pref_id").textbox("setValue", "");
	$("#check_pref_nm").textbox("setValue", "");
	$("#check_pref_nm2").textbox("setValue", "");
	$("#check_enable_yn").combobox("setValue", "");
	$("#check_value_unit").combobox("setValue", "");
	$("#check_grade_cd").combobox("setValue", "");
//	$("#check_grade_cd").combobox("setText", "");
	$("#check_class_div_cd").combobox("setValue", "");
//	$("#check_class_div_cd").combobox("setText", "");
	$("#emergency_action_yn").combobox("setValue", "");
	
	if(checkURL==='BasicCheckConfig'){
//		alert(checkURL);
		$("#default_threshold_value").textbox("setValue", "");
		$("#check_pref_nm").textbox({readonly : false });
		$("#check_pref_id").textbox({readonly : true });
		$("#check_pref_id").textbox("textbox").prop('placeholder', '자동으로 입력됩니다.');
	}else{
//		alert(checkURL);
		$("#check_pref_id").textbox({readonly : true });
		$("#check_pref_nm2").combobox("setValue","");
		$("#check_pref_nm2").combobox({readonly : false });
		$("#check_pref_id").textbox("textbox").prop('placeholder', '');
		$("#default_threshold_value").textbox("setValue", "");	
	};
}