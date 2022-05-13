var deleteFileCnt = 0;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('[name=wrkjob_mgr_nm],[name=wrkjob_mgr_wrkjob_nm],[name=wrkjob_mgr_tel_num]')
		.siblings('input').css({'background':'none','color':'#000','width':'150px'});
	$('[name=wrkjob_mgr_nm],[name=wrkjob_mgr_wrkjob_nm],[name=wrkjob_mgr_tel_num]')
		.parents('span').css({'border':'none','color':'#000','width':'168px'});
	
	//튜닝요청 초기화 설정값 세팅 팝업
	$('#saveInitSettingPop').window({
		title : "튜닝요청 초기값 설정",
		top:getWindowTop(550),
		left:getWindowLeft(680)
	});	
	
	/*업무담당자 'X'클릭시 업무담당자,담당업무,담당자연락처 리셋*/
	$('#resetBtn').on('click', function(){
		
		$("#wrkjob_mgr_nm").textbox('setValue','');
		$("#wrkjob_mgr_id").val('');
		$("#wrkjob_mgr_wrkjob_nm").textbox('setValue','');
		$("#wrkjob_mgr_tel_num").textbox('setValue','');
		$("#wrkjob_mgr_wrkjob_cd").val('');
		$('#resetBtn').css("visibility","hidden");
		
	});
	
	// 프로그램 유형 조회
	$('#selectProgram').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1005&isAll=N&isChoice=N",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
			
			$("#selectProgram").combobox("textbox").attr("placeholder","선택");
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
				//$(this).combobox('setValue',$("#batch_work_div_cd").val());
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
			var isExist = false;
			
			$("#selectSystem").combobox("textbox").attr("placeholder","선택");
			
			if (items.length){
				for(var i=0;i<items.length;i++){
					if(dbid == items[i].dbid){
						isExist = true;
					}
				}
				//기존 소스, 업무테이블과 상관없이 db값이 있으면 선택해줌
				//$('#selectSystem').combobox("setValue",dbid);
				//kb카드 요구사항, 업무에 따른 db가 있으면 default로 그 db를 선택
				if(isExist){
					$('#selectSystem').combobox("setValue",dbid);
				}else{
					var opts = $(this).combobox('options');
					$(this).combobox('select', items[0][opts.valueField]);
				}
			}
			
			setInitSetting();
		},
		onSelect:function(rec){
			// parsing_schema_name 조회
//			$('#selectParsingSchemaName').combobox({
//				url:"/SelfTuning/getParsingSchemaName?dbid="+rec.dbid,
//				method:"get",
//				valueField:'username',
//				textField:'username'
//			});
			
			$("#selectParsingSchemaName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					
					$("#selectParsingSchemaName").combobox("textbox").attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			
			$('#selectParsingSchemaName').combobox("setValue",$("#parsing_schema_name").val());
			
		}
	});
	
	$("#selectBatch").combobox({disabled:true});
	
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
	
	// SQL성능분석에서 요청시 업무특이사항에 값을 넣어주기위함
	if($('#call_from_sqlPerformance').val() == 'Y'){
		getWkjobPeculiarPoint();
	}
	//	SQL성능분석에서 요청시 SQL TEXT 와 SQL BIND 값을 가지고 오기 위함.
	if($('#call_from_sqlPerformance').val() == 'Y'){
		getSqlText();
	}
	if($('#call_from_sqlPerformance').val() == 'Y' && $("#bindValueIsNull").val() == 'N'){
		getSQLBind();
	}
	
	setInitSetting();

});

function deleteFile(obj, Idx, Nm) {
	console.log(obj+" , "+Idx+" , "+Nm);
	let deleteFiles = $("#deleteFiles").val();
	
	deleteFileCnt++;
	
	if (deleteFileCnt == 3 || deleteFileCnt == $("#uploadFile")[0].files.length ) {
		$("#uploadFile").val("");
		$("#fileNames").html( "<label style='color:darkgray;margin:0;'>파일을 선택해 주세요.</label>" );
		$("#deleteFiles").val("");
		deleteFileCnt = 0;
		
		return false;
	}
	
	deleteFiles += Nm+" , ";
	
	$("#deleteFiles").val( deleteFiles );
	
	$(obj).parent().remove();
}

function fileUploadChange( data ) {
	let maxUploadSize = $('#maxUploadSize').val();
	let noneFile = $('#noneFileList').val().substring(1 , $('#noneFileList').val().lastIndexOf("]")-1 ).trim().split(",");
	let files = data.files;
	let fileArry = "";
	
	if (files.length > 3) {
		$.messager.alert('ERROR','첨부파일은 최대 3개까지 첨부가능 합니다.','error' );
		$("#uploadFile").val("");
		$("#fileNames").html( "<label style='color:darkgray;margin:0;'>파일을 선택해 주세요.</label>" );
		$("#deleteFiles").val("");
		deleteFileCnt = 0;
		
		return false;
	} else {
		for (let i = 0; i < files.length; i++) {
			let file_ex = files[i].name.substring(files[i].name.lastIndexOf("."));
			let fileNm = '"'+files[i].name+'"';
			
			if ( files[i].size >= maxUploadSize) {
				let fileLength = parseInt((files[i].size/1024)/1024);
				
				$.messager.alert('ERROR', files[i].name +" 파일 용량이 너무 큽니다.<br/>100메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB",'error');
				$("#uploadFile").val("");
				$("#fileNames").html( "<label style='color:darkgray;margin:0;'>파일을 선택해 주세요.</label>" );
				$("#deleteFiles").val("");
				deleteFileCnt = 0;
				
				return false;
			} else {
				for (let extension = 0; extension < noneFile.length; extension++) {
					if ( noneFile[extension].trim() == file_ex ) {
						$.messager.alert('ERROR','[ '+ files[i].name +' ]의 확장자는 업로드할 수 없습니다.','error' );
						$("#uploadFile").val("");
						$("#fileNames").html( "<label style='color:darkgray;margin:0;'>파일을 선택해 주세요.</label>" );
						$("#deleteFiles").val("");
						deleteFileCnt = 0;
						
						return false;
					}
				}
			}
			
			fileArry += "<span id='"+i+"_"+files[i].name+"' style='padding:5px;'>"+files[i].name+" <a href='javascript:;' onClick='deleteFile(this,"+i+","+fileNm+");'><i class='btnIcon fas fa-trash-alt'></i></a></span>&nbsp;";
		}
	}
	
	if( fileArry != null && fileArry != "" && fileArry != "null" ) {
		$("#fileNames").html( fileArry );
	}
}

function setInitSetting() {
//설정초기화 값이 있다면
	if(initValues != null && initValues != ''){
		$('#tuning_requester_tel_num').textbox("setValue",tuning_requester_tel_num);
		$('#selectProgram').combobox("setValue",program_type_cd);
		$('#selectParsingSchemaName').combobox("setValue",parsing_schema_name);
		
		$('#popup_form #tuning_requester_tel_num').textbox("setValue",tuning_requester_tel_num);
		$('#popup_form #program_type_cd').combobox("setValue",program_type_cd);
		$('#popup_form #dbid').combobox("setValue",dbid);
		$('#popup_form #parsing_schema_name').combobox("setValue",parsing_schema_name);
		$('#popup_form #wrkjob_peculiar_point').textbox("setValue",wrkjob_peculiar_point);
		$('#popup_form #request_why').textbox("setValue",request_why);
	}
}
function getWkjobPeculiarPoint() {
	var str = ""
		str = "SQL_ID : " + $("#sqlStats_sql_id").val() + "\n"+ 
				"PLAN_HASH_VALUE : " + $("#sqlStats_plan_hash_value").val() + "\n"+ 
				"ELAPSED_TIME : " + $("#sqlStats_elapsed_time").val() + "\n"+ 
				"BUFFER_GETS : " + $("#sqlStats_buffer_gets").val();
	$("#wrkjob_peculiar_point").val(str);
};

function getSqlText() {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("튜닝요청"," ");
	
	/* 상단 텍스트 박스 */
	ajaxCall("/SQLPerformance/SQLText",
			$("#submit_form"),
			"POST",
			callback_SQLTextAction);
	
//	/* Bind Value */
//	$('#bindValueList').datagrid('loadData',[]);
//	$('#bindValueList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
//	$('#bindValueList').datagrid('loading');
	
//	$("#submit_form #tab1ClickCount").val(1);

}

//callback 함수
var callback_SQLTextAction = function(result) {
	if(result.result){
		var post = result.object;
		//$("#textArea").html(strReplace(post.sql_text,'\n','<br/>'));
		if(post != null){
			$("#sql_text").val(post.sql_text);
			$('#sql_text').format({method: 'sql'});
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function getSQLBind() {
	ajaxCall("/SQLPerformance/SQLBind",
			$("#submit_form"), 
			"POST", 
			callback_SQLBindAction);
}

//callback 함수
var callback_SQLBindAction = function(result) {
	var strTemp = "";
	
	if(result.result){
		var post = result.object;
		//$("#textArea").html(strReplace(post.sql_text,'\n','<br/>'));
		if(post != null){
			for(var i=0; i< post.length; i++){
				strTemp = (strTemp + post[i])+"\n";
			}
			$("#sql_desc").val(strTemp);
		}
	}else{
		parent.$.messager.alert('',result.message);
	}
	
};

function validMsg(objId,msg) {
		parent.$.messager.alert('',msg,'info',function(){
			setTimeout(function() {
				$(objId).textbox('clear').textbox('textbox').focus();
			},100);
		});
}

function Btn_GoSave() {
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
	
	if($('#exec_cnt').textbox('getValue') != ''){
		if($('#exec_cnt').textbox('getValue') != null && $('#exec_cnt').textbox('getValue') <= 0){
			validMsg("#exec_cnt",'수행횟수를 0보다 크게 입력해 주세요.');
			return false;
		}
	}
	
	if($('#selectSystem').combobox('getValue') == ""){
		validMsg("#selectSystem",'DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectParsingSchemaName').combobox('getValue') == ""){
		validMsg("#selectParsingSchemaName","DB접속계정을 선택해 주세요.");
		return false;
	}
	
	if($('#tuning_complete_due_dt').datebox('getValue') == ""){
		validMsg("#tuning_complete_due_dt",'튜닝완료요청일자를 입력해 주세요.');
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
	
	let sql_bind = $('#sql_desc').val();
	if( byteLength(sql_bind) > 4000 ){
		parent.$.messager.alert('','SQL BIND를 4000byte 이하로 입력해 주세요.');
		return false;
	}
	
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
	
	let maxUploadSize = $('#maxUploadSize').val();
	let noneFile = $('#noneFileList').val().substring(1 , $('#noneFileList').val().lastIndexOf("]")-1 ).trim().split(",");
	let files = $("#uploadFile")[0].files;
	
	if ( files[0] != null ) {
		
		if (deleteFileCnt == 3 || files.length == deleteFileCnt) {
			$("#uploadFile").val("");
			$("#fileNames").html( "<label style='color:darkgray;margin: 0;'>파일을 선택해 주세요.</label>" );
			$("#deleteFiles").val("");
			deleteFileCnt = 0;
		}
		if ( files.length > 3 ) {
			$.messager.alert('ERROR','첨부파일은 최대 3개까지 첨부가능 합니다.','error' );
			$("#uploadFile").val("");
			$("#fileNames").html( "<label style='color:darkgray;margin: 0;'>파일을 선택해 주세요.</label>" );
			$("#deleteFiles").val("");
			deleteFileCnt = 0;
			
			return false;
		} else {
			for ( var i = 0; i < files.length; i++ ) {
				var file_ex = files[i].name.substring( files[i].name.lastIndexOf(".") );
				var file_name = files[i].name.substring( files[i].name.lastIndexOf(".")+1 );
				
				if ( files[i].size >= maxUploadSize) {
					var fileLength = parseInt((files[i].size/1024)/1024);
					
					$.messager.alert('ERROR', files[i].name +" 파일 용량이 너무 큽니다.<br/>100메가 이하로 선택해 주세요.<br/>현재용량 :"+fileLength+" MB",'error');
					$("#uploadFile").val("");
					$("#fileNames").html( "<label style='color:darkgray;margin: 0;'>파일을 선택해 주세요.</label>" );
					$("#deleteFiles").val("");
					deleteFileCnt = 0;
					
					return false;
				} else {
					for (var extension = 0; extension < noneFile.length; extension++) {
						if ( noneFile[extension].trim() == file_ex ) {
							$.messager.alert('ERROR','[ '+ file_name +' ]의 확장자는 업로드할 수 없습니다.','error' );
							$("#uploadFile").val("");
							$("#fileNames").html( "<label style='color:darkgray;margin: 0;'>파일을 선택해 주세요.</label>" );
							$("#deleteFiles").val("");
							deleteFileCnt = 0;
							
							return false;
						}
					}
				}
			}
		}
	}
	
	var formData = new FormData($('#submit_form')[0]);
	formData.append(dbid, $('#selectSystem').combobox('getValue'));
	
	ajaxMultiCall("/RequestImprovementMultiAction",
			formData,
			"POST",
			callback_RequestImprovementAction);
	
}

//callback 함수
var callback_RequestImprovementAction = function(result) {
	if(result.result) {
		parent.$.messager.alert('','튜닝요청이 정상적으로 완료되었습니다.','info',function(){
			setTimeout(function() {
				// DBA에게  Alert 전송
//				메시지 전송 삭제 2018-10-29
//				var userName = $("#tuning_requester_nm").textbox("getValue");
//				var userName = $("#tuning_requester_nm").val();
//				var strMsg = "["+userName+"]님이 튜닝요청을 등록하셨습니다.";
//				
//				for(var i = 0 ; i < result.object.length ; i++){
//					var post = result.object[i];					
//					parent.messageSendByUser(post.user_id, "튜닝요청", strMsg);
//				}

				//location.href = "/RequestImprovement?menu_id="+$("#menu_id").val();
				//location.href = "/ImprovementManagement?menu_id="+$("#menu_id").val();

				//상단 요청,접수,튜닝중,적용대기,튜닝반려 메시지 변경
				parent.searchWorkStatusCount();

				menuParam = "menu_id="+$("#menu_id").val();
				parent.openLink("Y","110","성능 개선 관리","/ImprovementManagement",menuParam);
				closeTab();
			},1000);	
		});	
	} else {
		$.messager.alert('ERROR',result.message,'error', function(){
			//location.href = "/RequestImprovement?menu_id="+$("#menu_id").val();
			//menuParam = "menu_id="+$("#menu_id").val();
			//parent.openLink("Y","110","성능 개선 관리","/ImprovementManagement",menuParam);
			//closeTab();
		});
	}
};

//탭닫기
function closeTab() {
	/*** From SQL 성능 분석 ***/
	var menuNm = $('#menu_nm').val();
	
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
				//console.log("value:"+value);
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

//function getTableColumn(){
//	var sqlText = $("#sql_text").val();
////	alert(sqlText);
//	$.ajax({
//		url : "/getTableColumn",
//		data: {
//			sql : sqlText
//		},
//		dataType:"json",
//		type:"POST",
//		success : function(data) {
//			$(data).each(function(idx, item){
////				alert(idx+" , "+item.column);
//			});
//		}
//	});
//}

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

function showWorkTunerPopup(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#workTunerPop').window("open");
}
