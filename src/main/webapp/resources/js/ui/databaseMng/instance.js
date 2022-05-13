var colorIdArry = new Array();
var colorValArry = new Array(); 
var pageId = "Instance";

$(document).ready(function() {
	
	$("body").css("visibility", "visible");
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			setDetailView(row);
		},		
		columns:[[
			{field:'dbid',title:'DBID',width:"7%",halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB명',width:"7%",halign:"center",align:"center",sortable:"true"},
			{field:'inst_id',title:'인스턴스 번호',width:"5%",halign:"center",align:'center'},
			{field:'inst_name',title:'인스턴스명',width:"7%",halign:"center",align:'center'},
			{field:'display_nm',title:'화면 표시명',width:"7%",halign:"center",align:'center'},
			{field:'host_nm',title:'호스트명',width:"7%",halign:"center",align:'left'},
			{field:'instance_desc',title:'인스턴스 설명',width:"11%",halign:"center",align:'left'},
			{field:'rgb_color_value',title:'컬러',width:"5%",halign:"center",align:'center', styler:cellStyler},
			{field:'agent_ip',title:'COLLECT IP',width:"7%",halign:"center",align:'center'},
			{field:'agent_port',title:'COLLECT PORT',width:"6%",halign:"center",align:'center'},
			{field:'dplx_agent_ips',title:'COLLECT 이중화 IP 그룹',width:"11%",halign:"center",align:'center'},
			{field:'dplx_gather_agent_ports',title:'COLLECT 이중화 PORT 그룹',width:"11%",halign:"center",align:'center'},
			{field:'collect_agent_id',title:'COLLECT AGENT ID',width:"11%",halign:"center",align:'center'},
			{field:'collect_agent_path',title:'COLLECT PATH',width:"16%",halign:"center",align:'left'},
			{field:'collect_instance_seq',title:'COLLECT 인스턴스 순번',width:"8%",halign:"center",align:'center'},
			{field:'gather_agent_port',title:'GATHER PORT',width:"6%",halign:"center",align:'center'},
			{field:'dplx_agent_ports',title:'GATHER 이중화 PORT 그룹',width:"11%",halign:"center",align:'center'},
			{field:'rgb_color_id',hidden:"true"}
		]],
		
/*		columns:[[
			{field:'dbid',title:'DBID',width:"7%",halign:"center",align:"center",sortable:"true"},
			{field:'db_name',title:'DB명',width:"7%",halign:"center",align:"center",sortable:"true"},
			{field:'inst_id',title:'인스턴스 번호',width:"5%",halign:"center",align:'center'},
			{field:'inst_name',title:'인스턴스명',width:"7%",halign:"center",align:'center'},
			{field:'display_nm',title:'화면 표시명',width:"7%",halign:"center",align:'center'},
			{field:'host_nm',title:'호스트명',width:"7%",halign:"center",align:'left'},
			{field:'instance_desc',title:'인스턴스 설명',width:"11%",halign:"center",align:'left'},
			{field:'rgb_color_value',title:'컬러',width:"5%",halign:"center",align:'center', styler:cellStyler},
			{field:'agent_ip',title:'AGENT IP',width:"7%",halign:"center",align:'center'},
			{field:'agent_port',title:'COLLECT PORT',width:"6%",halign:"center",align:'center'},
			{field:'gather_agent_port',title:'GATHER PORT',width:"6%",halign:"center",align:'center'},
			{field:'dplx_agent_ips',title:'AGENT 이중화 IP 그룹',width:"11%",halign:"center",align:'center'},
			{field:'dplx_gather_agent_ports',title:'COLLECT PORT 그룹',width:"11%",halign:"center",align:'center'},
			{field:'dplx_agent_ports',title:'GATHER PORT 그룹',width:"11%",halign:"center",align:'center'},
			{field:'rgb_color_id',hidden:"true"}
			]],
*/
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	// Database 조회			
	$('#selectDbid').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onSelect: function(rec){
			$("#dbid").val(rec.dbid);
			$("#db_name").val(rec.db_name);
		},
		onLoadSuccess: function(){
			$('#selectDbid').combobox('textbox').attr("placeholder","선택");
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});	
	
	//이전, 다음 처리
	$("#prevBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("PREV");
		}
	});
	$("#nextBtnEnabled").click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext("NEXT");
		}
	});
	
	$("#prevBtnEnabled").hide();
	$("#nextBtnEnabled").hide();
	
	$('#colorDiv').textbox('readonly');
	$('#searchValue').textbox({readonly:true});

	$('#searchKey').combobox({
		onChange : function(newValue,oldValue){
			if(newValue == "") {
				$('#searchValue').textbox("setValue", "");
				$('#searchValue').textbox({readonly:true});
			}else{
				$('#searchValue').textbox({readonly:false});
			}
		}
	});
});


function cellStyler(value, row, index) {
	if(!value == "") {
		return 'background-color:' + value + ';color:#fff;text-shadow:-1px -1px #000, 1px -1px #000, -1px 1px #000, 1px 1px #000';
	}
}

function Btn_SaveSetting(){
	if($("#selectDbid").combobox('getValue') == "" || $("#selectDbid").combobox('getValue') == '선택'){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($("#ui_inst_id").textbox('getValue') == ""){
		parent.$.messager.alert('','인스턴스 번호를 입력해 주세요.');
		return false;
	}
	if($("#inst_name").textbox('getValue') == ""){
		parent.$.messager.alert('','인스턴스명을 입력해 주세요.');
		return false;
	}
	
	if($("#display_nm").textbox('getValue') == ""){
		$("#display_nm").textbox('setValue', '');
	}
	
	if($("#display_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','화면표시명을 입력해 주세요.');
		return false;
	}
	
	if($("#host_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','호스트명을 입력해 주세요.');
		return false;
	}
	
	if($("#colorDiv").textbox('getValue') == ""){
		parent.$.messager.alert('','RGB 컬러를 선택해 주세요.');
		return false;
	}
	if($('#collect_agent_id').textbox('getValue') == ""){
		parent.$.messager.alert('','COLLECT AGENT ID를 입력해 주세요.');
		return false;
	}
	
	if($("#agent_ip").textbox('getValue') == ""){
		parent.$.messager.alert('','COLLECT IP를 입력해 주세요.');
		return false;
	}
	
	if($("#agent_port").textbox('getValue') == ""){
		parent.$.messager.alert('','COLLECT PORT를 입력해 주세요.');
		return false;
	}
	
	if( $('#collect_instance_seq').textbox('getValue') != null && $('#collect_instance_seq').textbox('getValue') < 0) {
		$('#collect_instance_seq').textbox('setValue',0);
		parent.$.messager.alert('','COLLECT 인스턴스 순번을 0이상으로 입력해 주세요.');
		return false;
	}
	
	if($('#gather_agent_port').textbox('getValue') == ""){
		parent.$.messager.alert('','GATHER PORT를 입력해 주세요.');
		return false;
	}

	
	if($("#instance_desc").textbox('getValue') == ""){
		$("#instance_desc").textbox('setValue', '');
	}
	
	if($("#dplx_agent_ips").textbox('getValue') == ""){
		$("#dplx_agent_ips").textbox('setValue', '');
	}
	
	if($("#dplx_gather_agent_ports").textbox('getValue') == ""){
		$("#dplx_gather_agent_ports").textbox('setValue', '');
	}
	
	if($("#dplx_agent_ports").textbox('getValue') == ""){
		$("#dplx_agent_ports").textbox('setValue', '');
	}
	
	

	/*if($('#collect_agent_path').textbox('getValue') == ""){
		parent.$.messager.alert('','COLLECT PATH를 입력해 주세요.');
		return false;
	}*/
//	if($('#collect_instance_seq').textbox('getValue') == ""){
//		parent.$.messager.alert('','COLLECT 인스턴스 순번을 입력해 주세요.');
//		return false;
//	}
	
	if(!validationNumberType()) return false;
	
//	$("#dbid").val($("#selectDbid").combobox('getValue'));
	$('#inst_id').val($('#ui_inst_id').textbox('getValue'));
	
	ajaxCall("/Instance/Save",
			$("#detail_form"),
			"POST",
			callback_SaveInstanceAction);
	
//	ajaxCall("/Instance/Save",
//			$("#detail_form"),
//			"POST",
//			callback_SaveInstanceAction);		
}

function validationNumberType() {
	if(!$.isNumeric($('#ui_inst_id').val())) {
		parent.$.messager.alert('입력형식 오류','인스턴스 번호는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if(!$.isNumeric($('#agent_port').val())) {
		parent.$.messager.alert('입력형식 오류','COLLECT PORT는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if(!$.isNumeric($('#gather_agent_port').val())) {
		parent.$.messager.alert('입력형식 오류','GATHER PORT는 숫자만 입력 가능합니다', 'error');
		return false;
	}
	
	if($('#ui_inst_id').val() < 1) {
		parent.$.messager.alert('입력형식 오류','인스턴스 번호는 0 이상의 값으로 입력하세요', 'error');
		return false;
	}
	
	return true;
}

//callback 함수
var callback_SaveInstanceAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_DeleteInstance(){
	if($("#selectDbid").combobox('getValue') == ""){
		parent.$.messager.alert('','DBID를 선택해 주세요.');
		return false;
	}
	
	if($("#ui_inst_id").textbox('getValue') == ""){
		parent.$.messager.alert('','인스턴스 번호를 입력해 주세요.');
		return false;
	}
	
	parent.$.messager.confirm('','[ DB명: '+$("#selectDbid").combobox('getText')+', 인스턴스 번호 : '+ $('#ui_inst_id').textbox('getValue')+' ] 을(를) 삭제하시겠습니까?',function(r){
		console.log( $("#detail_form").serialize());
	    if (r){
	        ajaxCall("/Instance/Delete",
	        		$("#detail_form"),
	        		"POST",
	        		callback_DeleteInstanceAction);		
	    }
	});

}

//callback 함수
var callback_DeleteInstanceAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','삭제 되었습니다.','info',function(){		
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};


function setDetailView(selRow){
	$("#detail_form #crud_flag").val("U");
	$("#selectDbid").combobox("setValue", selRow.dbid);
	$("#dbid").val(selRow.dbid);
	$("#db_name").val(selRow.db_name);
	$("#selectDbid").textbox({disabled: true});
	$("#ui_inst_id").textbox("setValue", selRow.inst_id);
	$("#ui_inst_id").textbox({disabled: true});
	$("#inst_id").val(selRow.inst_id);
	$("#inst_name").textbox("setValue", selRow.inst_name);
	$("#old_inst_name").val(selRow.inst_name);
	$("#display_nm").textbox("setValue", selRow.display_nm);
	$("#host_nm").textbox("setValue", selRow.host_nm);
	$("#instance_desc").textbox("setValue", selRow.instance_desc);
	$("#agent_ip").textbox("setValue", selRow.agent_ip);
	$("#agent_port").textbox("setValue", selRow.agent_port);
	$("#gather_agent_port").textbox("setValue", selRow.gather_agent_port);
	$("#dplx_agent_ips").textbox("setValue", selRow.dplx_agent_ips);
	$("#dplx_gather_agent_ports").textbox("setValue", selRow.dplx_gather_agent_ports);
	$("#dplx_agent_ports").textbox("setValue", selRow.dplx_agent_ports);

	$("#collect_agent_id").textbox("setValue", selRow.collect_agent_id);
	$("#collect_agent_path").textbox("setValue", selRow.collect_agent_path);
	$("#collect_instance_seq").textbox("setValue", selRow.collect_instance_seq);
	
	var colorValue = selRow.rgb_color_value;
	
	if(colorValue == null || typeof(colorValue) == 'undefined'){
		$("#colorDiv").textbox('setValue','');
		$("#colorDiv").textbox('textbox').css("background-color", '#FFFFFF');
		return;
	}
	$("#rgb_color_id").val(selRow.rgb_color_id);
	$("#colorDiv").textbox('setValue',colorValue);
	$("#colorDiv").textbox('textbox').css("background-color", colorValue);
	setTextShadow($('#colorDiv'));
}

function Btn_ResetField(){
	$("#detail_form #crud_flag").val("C");
	$("#selectDbid").combobox("setValue", "");	
	$("#selectDbid").textbox({disabled: false});
	$("#dbid").val("");
	$("#db_name").val("");
	$("#ui_inst_id").textbox("setValue", "");
	$("#ui_inst_id").textbox({disabled: false});
	$("#inst_id").val("");
	$("#inst_name").textbox("setValue", "");
	$("#old_inst_name").val("");
	$("#display_nm").textbox("setValue", "");
	$("#host_nm").textbox("setValue", "");
	$("#instance_desc").textbox("setValue", "");
	$("#agent_ip").textbox("setValue", "");
	$("#agent_port").textbox("setValue", "");
	$("#gather_agent_port").textbox("setValue", "");
	$("#dplx_agent_ips").textbox("setValue", "");
	$("#dplx_gather_agent_ports").textbox("setValue", "");
	$("#dplx_agent_ports").textbox("setValue", "");
	$("#colorDiv").textbox("setValue", "");
	$("#colorDiv").textbox("textbox").css("background-color", "#FFF");
	$("#tableList").datagrid('clearSelections');
	
	$("#collect_agent_id").textbox("setValue", "");
	$("#collect_agent_path").textbox("setValue", "");
	$("#collect_instance_seq").textbox("setValue", "");
}

function setTextShadow(divId) {
	divId.textbox('textbox').css("color", '#FFF');
	divId.textbox('textbox').css("text-shadow", '-1px -1px #000, 1px -1px #000, -1px 1px #000, 1px 1px #000');
}


/*페이징처리시작*/
function fnSetCurrentPage(direction){
	console.log("direction : "+direction);
	var currentPage = $("#submit_form #currentPage").val();
	
	console.log("currentPage : "+currentPage);
	if(currentPage != null && currentPage != ""){
		if(direction == "PREV"){
			currentPage--;
		}else if(direction == "NEXT"){
			currentPage++;
		}
		console.log("currentPage2 : "+currentPage);
		
		$("#submit_form #currentPage").val(currentPage);
	}else{
		$("#submit_form #currentPage").val("1");
	}
}

function fnGoPrevOrNext(direction){
	fnSetCurrentPage(direction);  //
	
	var currentPage = $("#submit_form #currentPage").val();  //현재 설정한 커런트 페이지 값을 세팅
	currentPage = parseInt(currentPage);
	if(currentPage <= 0){
		$("#submit_form #currentPage").val("1");
		return;
	}
	Btn_OnClick('P');
}

function Btn_OnClick(val){
	if(!formValidationCheck()){  //현재 없음.
		return;
	}
	if(val != 'P'){ //페이징으로 검색을 하지않는는경우
		$("#submit_form #currentPage").val('1');
		$("#submit_form #pagePerCount").val('10');
	}
	
	fnSearch();
}


function fnSearch(){

		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		
		$('#tableList').datagrid('loadData',[]);

		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("인스턴스 관리"," "); 
		
		ajaxCall("/Instance",
				$("#submit_form"),
				"POST",
				callback_InstanceAction);	
	
}

//callback 함수
var callback_InstanceAction = function(result) {
	Btn_ResetField();
	
	json_string_callback_common(result,'#tableList',true);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

	fnControlPaging(result);
};

var fnControlPaging = function(result) {
	//페이징 처리
	var currentPage = $("#submit_form #currentPage").val();
	currentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form #pagePerCount").val();
	pagePerCount = parseInt(pagePerCount);

	var data;
	var dataLength=0;
	try{
		data = JSON.parse(result);
		dataLength = data.dataCount4NextBtn; //totalcount를 가지고옴, dataCount4NextBtn 이전,다음여부확인
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	//페이지를 보여줄지말지 여부를 결정
	if(currentPage > 1){
		$("#prevBtnDisabled").hide();
		$("#prevBtnEnabled").show();
		
		if(dataLength > 10){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}else{
			$("#nextBtnDisabled").show();
			$("#nextBtnEnabled").hide();
		}
	}
	if(currentPage == 1){
		$("#prevBtnDisabled").show();
		$("#prevBtnEnabled").hide();
		$("#nextBtnDisabled").show();
		$("#nextBtnEnabled").hide();
		if(dataLength > pagePerCount){
			$("#nextBtnDisabled").hide();
			$("#nextBtnEnabled").show();
		}
	}	
};

function formValidationCheck(){
	
	if(($('#searchKey').combobox('getValue') == "" && $("#searchValue").textbox('getValue') != "") ||
			($('#searchKey').combobox('getValue') != "" && $("#searchValue").textbox('getValue') == "")){
			parent.$.messager.alert('','검색 조건을 정확히 입력해 주세요.');
			return false;
		}
	
	return true;
}
/*페이징처리끝*/


function Excel_Download(){
	
	if(!formValidationCheck()){  //현재 없음.
		return false;
	}

	$("#submit_form").attr("action","/Instance/ExcelDown");
	$("#submit_form").submit();
}

function update() {
	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	$('#colorPopup').window("open");
	
	//checkRGBColor();
	ajaxCallGetRGBColor();
}
