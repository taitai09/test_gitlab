var allScript = "";
	
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
    var clipboardAll = new Clipboard('#allCopyBtn',{
    	text: function() {
            return $("#allScript").val();
        }
    });
    
    clipboardAll.on('success', function(e) {
    	parent.$.messager.alert('','전체 SCRIPT가 복사되었습니다.');
    });
    
    var clipboard = new Clipboard('.scriptCopy');
    clipboard.on('success', function(e) {
    	parent.$.messager.alert('','SCRIPT가 복사되었습니다.');
    });
    
	fnCreateDbidCombobox();	

	$("#strStartDt").datebox({
		onSelect: function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			var selDate = y+(m<10?('0'+m):m+"")+(d<10?('0'+d):d+"");
			
			// 점검회차 설정
			setCheckSeq(selDate);
		}
	});
	
	// 점검회차 설정
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	setCheckSeq($("#check_day").val());

	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'owner',title:'OWNER',width:"10%",halign:"center",align:"left",sortable:"true"},
			{field:'table_name',title:'TABLE_NAME',width:"19%",halign:"center",align:"left",sortable:"true"},
			{field:'index_name',title:'INDEX_NAME',width:"20%",halign:"center",align:"left",sortable:"true"},
			{field:'script',title:'SCRIPT',width:"55%",halign:"center",align:"left",sortable:"true"},			
			{field:'script_btn',title:'SCRIPT 복사',width:"8%",halign:"center",align:"center",formatter:scriptCopyBtn}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});
	
	$("#registExceptionBtn").click(function(){
		fnRegistException();
	});
	
	$('#selectDbid').combobox("setValue",$("#dbid").val());
	$('#selectCheckSeq').combobox("setValue",$("#check_seq").val());	
	Btn_OnClick();
});

function Btn_OnClick(){
	if($('#selectDbid').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectCheckSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','점검 회차를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectDbid').combobox('getValue'));	
	$("#check_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));
	$("#check_seq").val($('#selectCheckSeq').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("일 예방 점검 - UNUSABLE INDEX"," "); 

	ajaxCall("/PreventiveCheck/UnusableIndex",
			$("#submit_form"),
			"POST",
			callback_UnusableIndexAction);
}

//callback 함수
var callback_UnusableIndexAction = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

/* 스크립트 복사 버튼 생성 */
function scriptCopyBtn(val, row) {
	allScript += row.script + "\n";
	$("#allScript").val(allScript);
	
    return "<a href='javascript:;' class='scriptCopy' data-clipboard-action='copy' data-clipboard-text='"+row.script+"'><i class='btnIcon far fa-copy fa-fw'></i></a>";
}

/**
 * 장애예방>일예방점검>예외등록
 * @returns
 */
function fnRegistException(){
	var checkedRows = $('#tableList').datagrid('getChecked');
	
	var dbid = $("#selectDbid").combobox('getValue');
	
	var exceptObjectNameArry = "";
	
	var rows = $('#tableList').datagrid('getSelections');

	if(rows != null && rows != ""){
		$.messager.confirm('', "선택한 점검대상을 예외등록하시겠습니까?", function(r){
		
			if(r){
				for(var i = 0; i < rows.length; i++){
					exceptObjectNameArry += nullStringToBlank(rows[i].owner)+"^" +nullStringToBlank(rows[i].index_name) + "|";
				}		
				$("#submit_form #check_except_object").val(strRight(exceptObjectNameArry,1));
		
				ajaxCall("/ExceptionManagement/RegistException", $("#submit_form"), "POST", callback_RegistExceptionAction);
			}
		
		});
	}else{
		parent.$.messager.alert('','예외등록할 데이터를 선택해 주세요.');
	}
}
