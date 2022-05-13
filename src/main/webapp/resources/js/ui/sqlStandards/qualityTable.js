$(document).ready(function() {
	$("body").css("visibility", "visible");
	
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	$('#project_id').combobox({
		url:"/sqlStandardOperationDesign/getProjectList",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},onLoadSuccess: function(){
			$('#project_id').combobox('textbox').attr('placeholder','선택');
		}
	});
	
	$('#search_wrkjob_cd').combotree({
		url:"/Common/getWrkJobCd?deploy_check_target_yn=N&isAll=Y",
		method:'get',
		valueField:'id',
		textField:'text',
		onChange:function(newval,oldval){
			$('#wrkjob_cd').val(newval);
		},
		onLoadSuccess: function(rec) {
//			Btn_OnClick();
		}
	});
	
	var t1 = $("#search_wrkjob_cd");
	
	t1.textbox('textbox').bind('keyup', function(e){
		if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
			Btn_OnClick();
		}
	});
});

function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}

function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#project_nm").textbox("setValue", row.project_nm);
	
	$("#project_id").combobox("setValue",row.project_id);
}

function formValidationCheck(){
	return true;
}

function fnSearch(){
	ajaxCallTableList();
}

function Btn_OnClick(){
	if($('#project_id').combobox("getValue") == '' || $('#project_id').combobox("getValue") == null ) {
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	$('#qty_chk_idt_cd').val('001');
	
	$('#tableList').datagrid("loadData", []);
	
	if($('#search_wrkjob_cd').combotree('getValue') == "" ||
			$('#search_wrkjob_cd').combotree('getText') == ""){
		$('#wrkjob_cd').val(0);
	}
	
	ajaxCallTableList();
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
}

function ajaxCallTableList(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 품질 검토 작업/집계표"," ");
	
	/* Tablespace 한계점 예측 - 상세 리스트 */
	ajaxCall("/loadProjectQualityTable",
			$("#submit_form"),
			"POST",
			callback_TableListAction);
}

var callback_TableListAction = function(result) {
	try {
		var data = JSON.parse(result);
		var rows = data.rows;
		var dataLength = data.dataCount4NextBtn;
		var rowsLen = rows.length;
		var headArray = new Array();
		
		if(rowsLen > 0) {
			var headRow = rows.splice(rowsLen - 1, 1);
			var head = headRow[0].HEAD;
			
			headArray = head.split('\;');
		}
		
		var pureData = rows;
		getByGrid(headArray);
		
		data.rows = pureData;
		
		json_string_callback_common(JSON.stringify(data), '#tableList', true);
		
		fnEnableDisablePagingBtn(dataLength);
	} catch(err) {
		if(err.message.indexOf('Unexpected') == 0) {
			parent.$.messager.alert('검색 에러',result.substring(result.indexOf('message') + 10, result.length - 2),'error');
		} else {
			parent.$.messager.alert('검색 에러',err.message,'error');
		}
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function getByGrid(headArray){
	var jsonArray = new Array();
	var jsonMap = new Object();
	var jsonArrayColumnGroup = new Array();
	var jsonMap2 = new Object();
	var jsonMap3 = new Object();
	
//	jsonMap2.title='메뉴';
//	jsonMap2.colspan=2;
//	jsonMap3.title='전회차';
//	jsonMap3.colspan=2;
//	jsonArrayColumnGroup.push(jsonMap2);
//	jsonArrayColumnGroup.push(jsonMap3);
	
	if(headArray.length == 0) {
		jsonMap = new Object();  //맵객체를 새로만듬.
		//jsonMap.field='coulmn_'+i;
		jsonMap.field='null';
		jsonMap.title='';
		jsonMap.width='100%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	}
	
	for(var i = 0; i < headArray.length; i++){
		var head = headArray[i];
		var options = head.split('\/');
		jsonMap = new Object();  //맵객체를 새로만듬.
		//jsonMap.field='coulmn_'+i;
		jsonMap.field=options[0];
		jsonMap.title=options[0];
		jsonMap.width=options[2];
		jsonMap.halign='center';
		jsonMap.align=options[1];
		jsonArray.push(jsonMap);
	}
	
	$("#tableList").datagrid({
		view: myview,
		//nowrap : false,
		fitColumns:true,
		singleSelect : true,
		//columns:[jsonArrayColumnGroup, jsonArray],
		columns:[jsonArray],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

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
}

function Excel_Download(){
	if($('#project_id').combobox("getValue") == '') {
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;
	}
	
	if($('#search_wrkjob_cd').combotree('getValue') == "" ||
			$('#search_wrkjob_cd').combotree('getText') == ""){
		$('#wrkjob_cd').val(0);
	}
	
	$('#qty_chk_idt_cd').val('001');
	$("#submit_form").attr("action","/excelDownQualityTableMultiDynamic");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
	openMessageProgress();
}

function getCookie(name) {
	var parts = document.cookie.split(name + "=");
	if(parts.length == 2)
		return parts.pop().split(";").shift();
}

function expireCookie(name) {
	document.cookie = encodeURIComponent(name) + "=deleted; expires=" + new Date(0).toUTCString();
}

var intId;
var time = 1000;
var count;

function openMessageProgress() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 품질 검토 집계표 Excel 생성"," ");
	
	count = 0;
	
	invoke();
}

function invoke() {
	intId = setInterval(function() {
		var token = getCookie("excelDownToken");
		
		if(count <= 5) {
			count += 1;
		}
		
		if(typeof token != "undefined" && token == "TRUE") {
			closeMessageProgress();
		} else {
			if(count == 5) {
				time = 5000;
				intId = clearInterval(intId);
				invoke();
			}
		}
	}, time);
}

function closeMessageProgress() {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	clearInterval(intId);
	
	expireCookie( "excelDownToken" );
}
