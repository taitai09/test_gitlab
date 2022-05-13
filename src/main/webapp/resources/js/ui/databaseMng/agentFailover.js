/*
 * 2018-07-10 기능삭제(메뉴삭제)
 */
$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#detail_form #dbid").val($('#selectCombo').combobox('getValue'));

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("에이전트 Failover 관리"," "); 
	
	ajaxCall("/AgentFailover",
			$("#submit_form"),
			"POST",
			callback_AgentFailoverAction);		
}

//callback 함수
var callback_AgentFailoverAction = function(result) {
	var strHtml = "";
	$("#detailT tbody tr").remove();
	
	if(result.result){
		strHtml += "<tr>";		
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			strHtml += "<th>▶ "+post.agent_type_nm+"</th><td>";
			strHtml += "<input type='hidden' id='agent_type_cd"+i+"' name='agent_type_cd' value='"+post.agent_type_cd+"'>";
			strHtml += "<input type='hidden' id='firstInstId"+i+"' name='firstInstId' value='"+post.first_inst_id+"'>";
			strHtml += "<input type='hidden' id='secondaryInstId"+i+"' name='secondaryInstId' value='"+post.secondary_inst_id+"'>";
			strHtml += "<b>※ 1차 인스턴스 번호</b>&nbsp;&nbsp;:&nbsp;&nbsp;<select id='first_inst_id"+i+"' name='first_inst_id' data-options='panelHeight:\"auto\"' class='w100 easyuic easyui-combobox'></select>&nbsp;&nbsp;&nbsp;&nbsp;";
			strHtml += "<b>※ 2차 인스턴스 번호</b>&nbsp;&nbsp;:&nbsp;&nbsp;<select id='secondary_inst_id"+i+"' name='secondary_inst_id' data-options='panelHeight:\"auto\"' class='w100 easyuic easyui-combobox'></select>";
			strHtml += "</td>";
		}
		strHtml += "</tr>";
	}else{
		strHtml = "<tr><td colspan='4' class='ctext'>검색된 데이터가 없습니다.</td></tr>";
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	$("#detailT tbody").append(strHtml);

	$('.easyuic').combobox({
	    url:"/Common/getMasterInstance?dbid="+$("#detail_form #dbid").val(),
	    method:"get",
		valueField:'inst_id',
	    textField:'inst_name'
	});	
	
	for(var i = 0 ; i < result.object.length ; i++){
		$("#first_inst_id"+i).combobox("setValue",$("#firstInstId"+i).val());
		$("#secondary_inst_id"+i).combobox("setValue",$("#secondaryInstId"+i).val());
	}
};

function Btn_SaveSetting(){
	ajaxCall("/AgentFailover/Save",
			$("#detail_form"),
			"POST",
			callback_SaveAgentFailoverAction);		
}

//callback 함수
var callback_SaveAgentFailoverAction = function(result) {
	if(result.result){
//		parent.$.messager.alert('','에이전트 Failover 정보 저장이 완료 되었습니다.','info',function(){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});
	}
};