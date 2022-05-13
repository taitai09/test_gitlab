$(document).ready(function() {

	//튜닝요청 초기화 설정값 세팅 팝업
	$('#saveInitSettingPop').window({
		title : "튜닝요청 초기값 설정",
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	// 프로그램 유형 조회			
	$('#selectProgram').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1005",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				$(this).combobox('setValue',$("#program_type_cd").val());
			}
			
			$('#selectProgram').combobox('textbox').attr("placeholder","선택");
		},		
	    onSelect:function(rec){
	    	if(rec.cd == "2"){
	    		$("#selectBatch").combobox({disabled:false});
	    	}else{
	    		$("#selectBatch").combobox({disabled:true});
	    	}
	    }	    
	});
	
	// 배치작업주기 조회			
	$('#selectBatch').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1006",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				$(this).combobox('setValue',$("#batch_work_div_cd").val());
			}
		}
	});	
	
	// Database 조회			
	$('#selectSystem').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items) {
			if (items.length){
				$(this).combobox('setValue',$("#dbid").val());
			}
			
			$('#selectSystem').combobox('textbox').attr("placeholder","선택");
			
			setInitSetting();
		},		
		onSelect:function(rec){
	    	// parsing_schema_name 조회
//	    	$('#selectParsingSchemaName').combobox({
//	    	    url:"/SelfTuning/getParsingSchemaName?dbid="+rec.dbid,
//	    	    method:"get",
//	    		valueField:'username',
//	    	    textField:'username'
//	    	});
	    	
	    	$("#selectParsingSchemaName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					
					$('#selectParsingSchemaName').combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$('#selectParsingSchemaName').combobox("setValue",$("#parsing_schema_name").val());
	    }
	});	
	
	if($("#batch_work_div_cd").val() != "2"){
		$("#selectBatch").combobox({disabled:true});
	}
	
	$("#current_elap_time").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}
		}
	});
	
	$("#forecast_result_cnt").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}
		}
	});
	
	$("#goal_elap_time").textbox({
		onChange: function(value){
			if(!onlyNumChk(value)){
				$(this).textbox("setValue","");
			}
		}
	});
	$("#tuning_complete_due_dt").datebox({
		onChange: function(value){
			var today = $("#nowDate").val();
			today = parseInt(today.replace(/-/gi,""));
			var due_dt = parseInt(value.replace(/-/gi,""));
			if(due_dt <= today){
				parent.$.messager.alert('','튜닝완료기한일을 미래로 설정해주세요.');
				$(this).datebox("setValue","");
			}
		}
	});
	
	//튜닝요청 초기화값 설정
	setInitSetting();
});

function setInitSetting(){
	//설정초기화 값이 있다면
		if(initValues != null && initValues != ''){
			$('#tuning_requester_tel_num').textbox("setValue",tuning_requester_tel_num);
			$('#selectProgram').combobox("setValue",program_type_cd);
			$('#selectSystem').combobox("setValue",dbid);
			$('#selectParsingSchemaName').combobox("setValue",parsing_schema_name);
			
			$('#popup_form #tuning_requester_tel_num').textbox("setValue",tuning_requester_tel_num);
			$('#popup_form #program_type_cd').combobox("setValue",program_type_cd);
			$('#popup_form #dbid').combobox("setValue",dbid);
			$('#popup_form #parsing_schema_name').combobox("setValue",parsing_schema_name);
			$('#popup_form #wrkjob_peculiar_point').textbox("setValue",wrkjob_peculiar_point);
			$('#popup_form #request_why').textbox("setValue",request_why);
		}
	}
function validMsg(objId,msg){
		parent.$.messager.alert('',msg,'info',function(){
			setTimeout(function() {
				$(objId).textbox('clear').textbox('textbox').focus();
			},100);
		});
}

function Btn_GoUpdate(){
	if($('#selectProgram').combobox('getValue') == ""){
		validMsg("#selectProgram",'프로그램 유형을 선택해 주세요.');
		return false;
	}
	
//	if($('#selectProgram').combobox('getValue') == "2" && $('#selectBatch').combobox('getValue') == ""){
//		validMsg("#selectBatch",'배치작업주기를 선택해 주세요.');
//		return false;
//	}
//	if($('#exec_cnt').textbox('getValue') != null && $('#exec_cnt').textbox('getValue') == ""){
//		validMsg("#exec_cnt",'수행횟수를 입력해 주세요.');
//		return false;
//	}
	if($('#selectSystem').combobox('getValue') == ""){
		validMsg("#selectSystem",'DB를 선택해 주세요.');
		return false;
	}
	if($('#selectParsingSchemaName').combobox('getValue') == ""){
		validMsg("#selectParsingSchemaName","DB접속계정을 선택해 주세요.");
		return false;
	}
	if($('#tuning_complete_due_dt').datebox('getValue') == ""){
		validMsg("#tuning_complete_due_dt",'튜닝완료일자를 입력해 주세요.');
		return false;
	}	
	
	if($('#exec_cnt').textbox('getValue') != ''){
		if($('#exec_cnt').textbox('getValue') != null && $('#exec_cnt').textbox('getValue') <= 0){
			validMsg("#exec_cnt",'수행횟수를 0보다 크게 입력해 주세요.');
			return false;
		}
	}
	if($('#current_elap_time').textbox('getValue') != ''){
		if($('#current_elap_time').textbox('getValue') <= 0){
			validMsg("#current_elap_time",'현재처리시간(초)을 0보다 크게 입력해 주세요.');
			return false;
		}
	}
	if($('#forecast_result_cnt').textbox('getValue') != ''){
		if($('#forecast_result_cnt').textbox('getValue') <= 0){
			validMsg("#forecast_result_cnt",'결과건수를 0보다 크게 입력해 주세요.');
			return false;
		}
	}
	if($('#goal_elap_time').textbox('getValue') != ''){
		if($('#goal_elap_time').textbox('getValue') <= 0){
			validMsg("#goal_elap_time",'목표처리시간(초)을 0보다 크게 입력해 주세요.');
			return false;
		}
	}
	if($('#sql_text').val() == '' || $('#sql_text').val().trim().length < 10){
		parent.$.messager.alert('','SQL Text를 10자 이상 입력해 주세요.');
		return false;
	}
	
//	if($('#current_elap_time').textbox('getValue') == ""){
//		validMsg("#current_elap_time",'현재처리시간을 입력해 주세요.');
//		return false;
//	}
//	if($('#forecast_result_cnt').textbox('getValue') == ""){
//		validMsg("#forecast_result_cnt",'결과건수를 입력해 주세요.');
//		return false;
//	}
//	if($('#goal_elap_time').textbox('getValue') == ""){
//		validMsg("#goal_elap_time",'목표처리시간을 입력해 주세요.');
//		return false;
//	}
//	if($('#tr_cd').textbox('getValue') == ""){
//		validMsg("#tr_cd",'소스파일명(Full Path)을 입력해 주세요.');
//		return false;
//	}	
//	if($('#dbio').textbox('getValue') == ""){
//		validMsg("#dbio",'SQL식별자(DBIO)를 입력해 주세요.');
//		return false;
//	}	

	
	$("#program_type_cd").val($('#selectProgram').combobox('getValue'));
	$("#batch_work_div_cd").val($('#selectBatch').combobox('getValue'));
	$("#parsing_schema_name").val($('#selectParsingSchemaName').combobox('getValue'));
	$("#dbid").val($('#selectSystem').combobox('getValue'));
	
	ajaxCall("/UpdateImprovementAction",
			$("#submit_form"),
			"POST",
			callback_UpdateImprovementAction);
}

//callback 함수
var callback_UpdateImprovementAction = function(result) {	
	if(result.result){
		parent.$.messager.alert('','튜닝재요청이  정상적으로 완료되었습니다.','info',function(){
			setTimeout(function() {
				// Tuner에게  Alert 재전송
				//메시지 전송 삭제 2018-10-29
//				var userId = $("#perfr_id").val();
//				var userName = $("#tuning_requester_nm").textbox("getValue");
//				var strMsg = "["+userName+"]님이 튜닝요청을 변경 재등록하셨습니다.";
//
//				parent.messageSendByUser(userId, "튜닝요청변경", strMsg);

				//Btn_GoBack();
				
				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();
				
				menuParam = "menu_id="+$("#menu_id").val();
				parent.openLink("Y","110","성능 개선 관리","/ImprovementManagement",menuParam);
				closeTab();
			},1000);	
		});	
	}else{
		$.messager.alert('ERROR',result.message,'error', function(){
			//Btn_GoBack();
		});	
	}
};

//탭닫기
function closeTab(){
	
	var menuNm="튜닝재요청";
	
	if(parent.$("#mainTab").tabs("exists",menuNm)){
		parent.$("#mainTab").tabs("select",menuNm);
		var tab = parent.$("#mainTab").tabs("getSelected");
		var index = tab.panel("options").index;
		parent.$("#mainTab").tabs("close",index);
	}
			
}

function Btn_addBindTable(){
	var strHtml = "";
	var totalRow = $("#bindTable > tbody > tr").length;
	
	strHtml += "<tr><th>바인드 "+(totalRow+1)+"</th>";
	strHtml += "<td><table id='bindValueTable"+(totalRow+1)+"' class='detailT' style='margin-left:3px;margin-top:3px;width:100%'>";
	strHtml += "<colgroup><col style='width:13%;'><col style='width:23%;'><col style='width:23%;'><col style='width:24%;'><col style='width:17%;'></colgroup>";
	strHtml += "<thead><tr><th>순번</th><th>변수명</th><th>변수값</th><th>변수타입</th><th>필수여부</th></tr></thead>";	
	strHtml += "<tbody><tr>";
	strHtml += "<td class='ctext'><input type='hidden' id='bind_set_seq"+(totalRow+1)+"' name='bind_set_seq' value='"+(totalRow+1)+"'/><input type='text' id='bind_seq1' name='bind_seq' value='1' class='w40'/></td>";
	strHtml += "<td class='ctext'><input type='text' id='bind_var_nm1' name='bind_var_nm' class='w80'/></td>";
	strHtml += "<td class='ctext'><input type='text' id='bind_var_value1' name='bind_var_value' class='w70'/></td>";
	strHtml += "<td class='ctext'><select id='bind_var_type1' name='bind_var_type' class='w80 easyui-combobox'><option value='string'>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";
	strHtml += "<td class='ctext'><select id='mandatory_yn1' name='mandatory_yn' class='w60 easyui-combobox'><option value='Y'>필수</option><option value='N'>필수아님</option></select></td></tr></tbody></table>";
	strHtml += "<div class='dtlBtn' style='margin-bottom:5px;'>";
	strHtml += "<a href='javascript:;' id='add"+(totalRow+1)+"' class='w80' onClick='Btn_addBindValue(\""+(totalRow+1)+"\");'>변수추가</a>&nbsp;";
	strHtml += "<a href='javascript:;' id='remove"+(totalRow+1)+"' class='w80' onClick='Btn_removeBindValue(\""+(totalRow+1)+"\");'>변수삭제</a>";
	strHtml += "</div></td></tr>";
	
	$("#bindTable > tbody:last").append(strHtml);
	
	$("#bindValueTable"+(totalRow+1)+" #bind_seq1").textbox({readonly:true});	
	$("#bindValueTable"+(totalRow+1)+" #bind_var_nm1").textbox();
	$("#bindValueTable"+(totalRow+1)+" #bind_var_value1").textbox();
	$("#bindValueTable"+(totalRow+1)+" #bind_var_type1").combobox({panelHeight:'auto'});
	$("#bindValueTable"+(totalRow+1)+" #mandatory_yn1").combobox({panelHeight:'auto'});
	
	$("#add"+(totalRow+1)).linkbutton({
		iconCls: 'icon-add'
	});
	
	$("#remove"+(totalRow+1)).linkbutton({
		iconCls: 'icon-remove'
	});
}

function Btn_removeBindTable(){
	var totalRow = $("#bindTable > tbody > tr").length;
	$("#bindTable > tbody:last > tr:last").remove();
}

function Btn_addBindValue(index){
	var strHtml = "";
	var totalRow = $("#bindValueTable"+index+" > tbody > tr").length;

	strHtml += "<tr><td class='ctext'><input type='hidden' id='bind_set_seq"+(totalRow+1)+"' name='bind_set_seq' value='"+index+"'/><input type='text' id='bind_seq"+(totalRow+1)+"' name='bind_seq' value='"+(totalRow+1)+"' class='w40 easyui-textbox'/></td>";
	strHtml += "<td class='ctext'><input type='text' id='bind_var_nm"+(totalRow+1)+"' name='bind_var_nm' class='w80 easyui-textbox'/></td>";
	strHtml += "<td class='ctext'><input type='text' id='bind_var_value"+(totalRow+1)+"' name='bind_var_value' class='w70 easyui-textbox'/></td>";
	strHtml += "<td class='ctext'><select id='bind_var_type"+(totalRow+1)+"' name='bind_var_type' class='w80 easyui-combobox'><option value='string'>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";
	strHtml += "<td class='ctext'><select id='mandatory_yn"+(totalRow+1)+"' name='mandatory_yn' class='w60 easyui-combobox'><option value='Y'>필수</option><option value='N'>필수아님</option></select></td></tr>";
	
	$("#bindValueTable"+index+" > tbody:last").append(strHtml);	
	
	$("#bindValueTable"+index+" #bind_seq"+(totalRow+1)).textbox({readonly:true});	
	$("#bindValueTable"+index+" #bind_var_nm"+(totalRow+1)).textbox();
	$("#bindValueTable"+index+" #bind_var_value"+(totalRow+1)).textbox();
	$("#bindValueTable"+index+" #bind_var_type"+(totalRow+1)).combobox({panelHeight:'auto'});
	$("#bindValueTable"+index+" #mandatory_yn"+(totalRow+1)).combobox({panelHeight:'auto'});	
}

function Btn_removeBindValue(index){
	var totalRow = $("#bindValueTable"+index+" > tbody > tr").length;
	$("#bindValueTable"+index+" > tbody:last > tr:last").remove();
}

//변수추출
function setBindValue(){
	var resultArr = [];
	var bindArry2Txt = "";
	var bindArry = pullOutBindArray($("#sql_text").val());
	
	if(bindArry != null){
		$.each(bindArry, function(index, value){
			if($.inArray(value, resultArr) === -1){
				value = value.replace(/\)/g,'').trim();
				value = value.replace(/,/g,'').trim();
				var n = false;
				if(resultArr.indexOf(value) !== -1){
					n = true;
				}
				if(!n){
					if(value.indexOf(':MI:SS') < 0){
						resultArr.push(value);
						bindArry2Txt += value+"\t\n";
					}
				}
			}
		});
	}

	$("#sql_desc").val(bindArry2Txt);
	
}

function Btn_GoBack(){
	$("#tuning_status_cd").val($("#list_tuning_status_cd").val());
	$("#tr_cd").textbox('setValue',$("#list_tr_cd").val());
	$("#dbio").textbox('setValue',$("#list_dbio").val());
	
	$("#submit_form").attr("action","/ImprovementManagementView");
	$("#submit_form").submit();		
}


/* 팝업 호출 */
function showInitSaveSetting_popup(){

	// iframe name에 사용된 menu_id를 상단 frameName에 설정 
	parent.frameName = $("#menu_id").val();
	
	// 위치 초기화
	$('#saveInitSettingPop').window({
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});
	
	$('#saveInitSettingPop').window("open");
}
function Btn_SettingText(val){
	$('#submit_form #wrkjob_peculiar_point').val(val); 
}
function Btn_SettingText2(val){
	$('#submit_form #request_why').val(val); 
}
