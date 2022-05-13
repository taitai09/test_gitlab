$(document).ready(function() {
	$("body").css("visibility", "visible");
	
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
	
	
	// Database 조회
	$('#autoLoadIndexDesign_form #selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#autoLoadIndexDesign_form #selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			$("#autoLoadIndexDesign_form #selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#autoLoadIndexDesign_form #selectUserName').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}
	});
	
	$("#autoLoadIndexDesign_form #tableList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
//		    {field:'chk',width:"8%",halign:"center",align:"center",checkbox:"true"},
//			{field:'exec_seq',title:'파싱순번',width:"10%",halign:"center",align:"center",sortable:"true"},
//			{field:'file_no',title:'파일번호',width:"14%",halign:"center",align:'center',sortable:"true"},
//			{field:'explain_exec_seq',title:'EXPLAIN EXEC SEQ',width:"14%",halign:"center",align:'right',sortable:"true"},
//			{field:'analysis_sql_cnt',title:'SQL수',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'table_count',title:'테이블수',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'access_path_count',title:'ACCESS-PATH수',width:"15%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
//			{field:'access_path_exec_dt',title:'ACCESS-PATH 파싱일시',width:"15%",halign:"center",align:'center',sortable:"true"}
		    {field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'exec_seq',title:'파싱순번',halign:"center",align:"center",sortable:"true"},
			{field:'file_no',title:'파일번호',halign:"center",align:'center',sortable:"true"},
			{field:'file_nm',title:'파일명',halign:"center",align:'left',sortable:"true"},
			{field:'explain_exec_seq',title:'생성순번',halign:"center",align:'right',sortable:"true"},
			{field:'analysis_sql_cnt',title:'SQL수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'table_count',title:'테이블수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_count',title:'ACCESS-PATH수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'access_path_exec_dt',title:'ACCESS-PATH 파싱일시',halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
        onCheck : function(node, checked) {
        	
        	row = $('#autoLoadIndexDesign_form #tableList').datagrid('getChecked');
        	var rowLength = row.length;

    		if(row.length > 1){
    			parent.$.messager.alert('','다중 선택은 불가합니다.');
   		        $(this).datagrid('uncheckRow',node);
    			return false;
    		}
        }
	});
	
});

function Btn_OnClick_autoLoadIndexDesign(){
	if($('#autoLoadIndexDesign_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	$("#autoLoadIndexDesign_form #dbid").val($('#autoLoadIndexDesign_form #selectCombo').combobox('getValue'));

	$('#autoLoadIndexDesign_form #tableList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재  SQL 인덱스 자동 설계"," "); 
	
	ajaxCall("/AutoLoadIndexDesign",
		$("#autoLoadIndexDesign_form"),
		"POST",
		callback_AutoLoadIndexDesignAddTable);		
}

//callback 함수
var callback_AutoLoadIndexDesignAddTable = function(result) {
	json_string_callback_common(result,'#autoLoadIndexDesign_form #tableList',true);
	$("#autoLoadIndexDesign_form #tableList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
}

function Btn_IndexAutoDesign(){
	if($('#autoLoadIndexDesign_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#autoLoadIndexDesign_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	let pre_ndv_ratio = "범위 검색 조건 선택도";
	let pre_col_null = "인덱스 제외 NDV 기준건수";
	
	rows = $('#autoLoadIndexDesign_form #tableList').datagrid('getSelected');
	
	if(rows != null){
		var exec_seq = rows.exec_seq;
	
		$("#autoLoadIndexDesign_form #dbid").val($('#autoLoadIndexDesign_form #selectCombo').combobox('getValue'));
		$("#autoLoadIndexDesign_form #table_owner").val($('#autoLoadIndexDesign_form #selectUserName').combobox('getValue'));
		$("#autoLoadIndexDesign_form #exec_seq").val(exec_seq);
		
		// 선행 작업 유무 판단
		ajaxCall("/isTaskStartIndexAutoDesign",
				$("#autoLoadIndexDesign_form"),
				"POST",
				callback_isTaskStartIndexAutoDesign);
	}else{
		parent.$.messager.alert('','인덱스 자동설계를 진행할 실행번호를 선택해 주세요.');
	}
}

var callback_isTaskStartIndexAutoDesign = function(result) {
	var data = JSON.parse(result);
	var rcount = data.rows[0].rnum;
	
	if(rcount == 0) {
		ajaxCall("/StartIndexAutoDesign",
				$("#autoLoadIndexDesign_form"),
				"POST",
				null);
		
		callback_StartIndexAutoDesignAction_autoLoadIndexDesign();
		
		return;
	}
	
	var rcount = data.rows[0].rnum;
	var db_name = $("#autoLoadIndexDesign_form #selectCombo").combobox("getText");
	
	if(rcount > 0) {
		var msgStr = "DB " + db_name + "에서 인덱스 자동설계를 수행중인 작업이 있습니다. 작업이 종료된 후 다시 수행하세요";
		parent.$.messager.alert('',msgStr);
		return false;
	}
}	

var callback_StartIndexAutoDesignAction_autoLoadIndexDesign = function() {
	parent.$.messager.alert('','인덱스 자동설계를 요청하였습니다.<br/>작업진행상태는 인텍스자동설계현황에서<br/>확인하세요.','info',function(){
		var menuId = "148";
		var menuNm = "인덱스 자동 설계현황";
		var menuUrl = "/AutoIndexStatus";
		var menuParam = "dbid="+$("#autoLoadIndexDesign_form #dbid").val() + "&call_from_child=Y";
		
		/* 탭 생성 */
		parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
	});
}