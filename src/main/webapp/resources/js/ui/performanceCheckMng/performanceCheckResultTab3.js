var resultArr = [];
var bindArry;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	var program_execute_tms = $("#program_execute_tms").val();
	parent.$("#submit_form #hidden_program_execute_tms").val(program_execute_tms);

	//익스플로러에서는 이 소스가 작동됨
	Btn_OnClosePopup('bindSearchDialog');

	// 수행유형
	$('#program_exec_div_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1054&isAll=N&isChoice=N",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
		onLoadSuccess: function(items){
			if (items.length){
				$(this).combobox('setValue',2);
				//나중에 기능 구현되면 풀을것
				$(this).combobox('readonly',true);
			}
		},
	    onChange:function(newValue, oldValue){
	    }
	});

	// 페이징
	$('#pagingCnt').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1069&isAll=N&isChoice=Y",
		method:"get",
		disabled:"true",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items){
//			var defaultPagingCnt = $("#defaultPagingCnt").val();
//			if (items.length){
//				$(this).combobox('setValue',defaultPagingCnt);
//			}
			
//			alert( $("#pagingCnt_chk").val() +","+ $("#pagingYn_chk").val());
			if ( $("#pagingCnt_chk").val() == "0" ) {
				$("#pagingCnt_chk").val("");
			}
			
			if ( $("#pagingYn_chk").val() != "" && $("#pagingCnt_chk").val() != "" ) {
				$("#pagingCnt").combobox( "setValue", $("#pagingCnt_chk").val() );
			} else {
				$("#pagingCnt").combobox( "setValue", "" );
//				$("#pagingYn").combobox( "uncheck" );
//				$("#pagingYn_Chk").val( "N" );
			}
		},
		onChange:function(newValue, oldValue){
			
		}
	});
	
	$("#pagingYn").checkbox({
		checked:false,
		onChange:function(checked){
			if(checked){
				$("#pagingCnt").combobox({disabled:false});
			}else{
				$("#pagingCnt").combobox({disabled:true});
			}
		}
	});
	
	if ( $("#pagingYn_chk").val() != "" && $("#pagingYn_chk").val() != "N" ) {
		$("#pagingYn").checkbox("check");
	} else {
		$("#pagingYn").checkbox("uncheck");
	}

	$("#parsing_schema_name").combobox({
		url:"/Common/getUserName?dbid="+$("#dbid").val(),
		method:"get",
		valueField:'username',
		textField:'username',
		onChange:function(newval,oldval){
		},
		onLoadSuccess: function(event) {
			$(this).combobox("setValue",$("#default_parsing_schema_name").val());	
		},
		onLoadError: function(){
			parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
		}
	});
	//SQL명령 유형이 SELECT일경우에만 바인드값 추출함
	//SQL명령 유형이 INSERT, UPDATE, DELETE, MERGE일 경우는 바인드값 추출안함
	var sql_command_type_cd = $("#sql_command_type_cd").val();
	if(sql_command_type_cd == "SELECT"){
		//바인드값 세팅
		setBindValue();
	}

});

function Btn_SetSQLFormatter(){
	$('#sql_text').format({method: 'sql'});
}

/* 바인드검색*/
var fnBindSearchBtnClick = function(){
	if ( $("#pagingYn").checkbox("options").checked == true && 
		 $('#pagingCnt').combobox("getValue") == "" ) {
		parent.parent.$.messager.alert('',"페이징처리 에서 값을 선택해주세요." , 'warning');
		return false;
	}
	
	$('#bindSearchDialog').dialog('open');
//	$("#bindSearchDialog").parent().css({"position":"absolute", "top":"500px", "left":"30px"});
//	$(".window-shadow").css({"position":"absolute", "top":"500px", "left":"30px"});
	$("#bindSearchDialog").parent().css({"position":"absolute", "top":"0px", "left":"500px"});
	$(".window-shadow").css({"position":"absolute", "top":"0px", "left":"500px"});
	fnBindSearchList1();
}

/* 성능점검수행*/
var fnPerfChkExecuteBtnClick = function(){
	
	/*if(bindArry != null){
		for(var i = 0 ; i < resultArr.length ; i++){
			//바인드값 자동 세팅
			var compare_bind_var_value = $("#compare_bind_var_value"+i).val();
			console.log("#### compare_bind_var_value #### :"+compare_bind_var_value);
			var bind_var_nm = strExpReplace3(strReplace(resultArr[i],' ',''));
			alert("bind_var_nm"+bind_var_nm);
			if(bind_var_nm.indexOf("STRING_IN") != -1){
				
					var cnt = countMatches(compare_bind_var_value,"'");	
					alert("들어옴 카운터 :"+cnt);
					if(cnt == 0 || cnt == 1 || cnt%2 == 1){
						parent.$.messager.alert('',"STRING_IN 다이나믹 변수의 입력값을 작은 따옴표로 묶어주세요!<br/>예)<br/>'abc'<br/>'abc', 'cde'");
						return false;
					}
			};

		}
	};*/
	if ( $("#sql_command_type_cd").val() == "SELECT" ) {
		if ( $("#pagingYn").checkbox("options").checked == true &&
				$('#pagingCnt').combobox("getValue") == "" ) {
			parent.parent.$.messager.alert('',"페이징처리 에서 값을 선택해주세요." , 'warning');
			return false;
		}
	}
	
	console.log("fnPerfChkExecuteBtnClick function started");
	var msg = "";
	var msg1 = "성능점검을 수행하시겠습니까?";
	var msg2 = "바인드값이 모두 없습니다. 성능점검을 수행하시겠습니까?";
	
	var bind_var_values = $("input[name='bind_var_value']");
	var bind_var_values_length = bind_var_values.length;
	var cnt = 0;
	for(var i=0;i<bind_var_values.length;i++){
		var bind_var_nm = resultArr[i];
		var bind_var_value = $(bind_var_values).eq(i).val();
		if(bind_var_value != "") cnt ++;
//		console.log("bind_var_values["+i+"]:"+$(bind_var_values).eq(i).val());

		if(bind_var_nm.indexOf("STRING_IN") != -1){
			var cnt = countMatches(bind_var_value,"'");	
			if(cnt == 0 || cnt == 1 || cnt%2 == 1){
				parent.parent.$.messager.alert('',"STRING_IN 다이나믹 변수의 입력값을 작은 따옴표로 묶어주세요!<br/>예)<br/>'abc'<br/>'abc', 'cde'");
				return false;
			}
		}
	
		bind_var_value = nvl(bind_var_value,"").replace(/,/g,'@~!');
		$("input[name='hidden_bind_var_value']").eq(i).val(bind_var_value);
	}
	console.log("bind_var_values_length:"+bind_var_values_length);
	console.log("cnt:"+cnt);
//	if(bind_var_values_length == cnt) msg = msg1;
	if(cnt == 0) msg = msg2;
	else msg = msg1;
	

//	parent.parent.$.messager.confirm('', msg, function(check) {
//		if (check) {
//			ajaxCall("/PerformanceCheckExecute", $("#tab_submit_form"), "POST", callback_PerformanceCheckExecute);
//		}
//	});
	
	var dlg = parent.parent.$.messager.confirm({
		title: 'Confirm',
		msg: msg,
		buttons:[{
			text: '확인',
			onClick: function(){
				if(parent.parent.openMessageProgress != undefined) parent.parent.openMessageProgress("성능점검을 수행중입니다"," ");	
				ajaxCall("/PerformanceCheckExecute", $("#tab_submit_form"), "POST", callback_PerformanceCheckExecute);
				dlg.dialog('destroy')
			}
		},{
			text: '취소',
			onClick: function(){
				dlg.dialog('destroy')
			}
		}
		]
	});	
	dlg.window('move',{
		left:810,
		top:410
	});	
}

//성능 점검 수행 callback 함수
var callback_PerformanceCheckExecute = function(result) {
	/* modal progress close */
	if(parent.parent.closeMessageProgress != undefined) parent.parent.closeMessageProgress();
	console.log("result:",result);
	console.log("result.result:",result.result);
	if(result.result){
//		console.log("성능점검을 수행하였습니다.");
		var dlg = parent.parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
					parent.$("#perfChkResultTabs").tabs('select',3);
					parent.fnSearch();
				},500);
		});
		dlg.window('move',{
			left:810,
			top:410
		});
	}else{
//		console.log("성능점검수행중 오류가 발생하였습니다.");
		//"현재 배포성능점검상태가 ㅁㅁㅁㅁ이므로 성능점검을 수행할 수 없습니다."
		var dlg = parent.parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
					parent.$("#perfChkResultTabs").tabs('select',3);
					parent.fnSearch();
				},500);
		});
		dlg.window('move',{
			left:810,
			top:410
		});
	}
};

var bind_var_nm_value_arr = [];
var bind_var_nm_value_arr_length = 0;
var bind_var_value_one = "";

function setBindValue(){
	
	if(bind_var_nm_value_arr != undefined){
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
		console.log("bind_var_nm_value_arr_length1:",bind_var_nm_value_arr_length);
		if(bind_var_nm_value_arr_length > 0){
			for(var i=0;i<bind_var_nm_value_arr_length;i++){
				var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
				var bind_var_value = $("#bind_var_value"+(i+1)).val();
				console.log(bind_var_nm+"=",bind_var_value);
				if(bind_var_nm != undefined){
					bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
				}
				console.log("bind_var_nm_value_arr:",bind_var_nm_value_arr);
			}
		}
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
		console.log("bind_var_nm_value_arr_length2:",bind_var_nm_value_arr_length);
	}
	
	bindArry = pullOutBindArray($("#sql_text").val());
	console.log("bindArry:",bindArry);
	if(bindArry != null){
		$.each(bindArry, function(key, value){
			console.log("resultArr:",resultArr.toString());
			if($.inArray(value, resultArr) === -1){
				let regularExp = /\,|\)|\;/g;
				value = value.replace(regularExp,'').trim();
				var n = false;
				if(resultArr.indexOf(value) !== -1){
					n = true;
				}
				if(!n){
					if(value.indexOf("${") != -1 || value.indexOf("#{") != -1){
						resultArr.push(value+"}");
					}else{
						resultArr.push(value);
					}
				}				
			}
		});
	}

	$("#bindTbl tbody tr").remove();
	var strHtml = "";
	if(bindArry != null){
		for(var i = 0 ; i < resultArr.length ; i++){
			//바인드값 자동 세팅
			var compare_bind_var_value = $("#compare_bind_var_value"+i).val();
			console.log("compare_bind_var_value :"+compare_bind_var_value);
			if(compare_bind_var_value != undefined){
				compare_bind_var_value = nvl(compare_bind_var_value,"").replace(/'/g,'&apos;');
			}
			var bind_var_nm = strExpReplace3(strReplace(resultArr[i],' ',''));
			
			if(bind_var_nm.substring(0,1) == ":"){
				bind_var_nm = bind_var_nm.replace(":","");
				bind_var_nm = "${"+bind_var_nm+"}";
			}
			//console.log("bind_var_nm===>"+bind_var_nm);
			strHtml += "<tr>";
			strHtml += "<td class='ctext'>";
			strHtml += "	<input type='hidden' id='hidden_bind_set_seq"+(i+1)+"' name='bind_set_seq' value='1'/>";
			strHtml += "	<input type='hidden' id='hidden_mandatory_yn"+(i+1)+"' name='mandatory_yn' value='Y'>";
			strHtml += "	<input type='hidden' id='hidden_bind_seq"+(i+1)+"' name='bind_seq' value='"+(i+1)+"'/>";
			strHtml += (i+1);
			strHtml += "</td>";

//			strHtml += "<td class='ctext'>";
//			strHtml += "	<input type='hidden' id='hidden_bind_var_nm"+(i+1)+"' name='bind_var_nm' value='"+bind_var_nm+"'/>";
//			strHtml += bind_var_nm;
//			strHtml += "</td>";
			strHtml += "<td class='ctext'>";
			strHtml += '	<span class="textbox tac" style="width: 100%;border:0px !important;">';
			strHtml += '		<input type="text" id="bind_var_nm'+(i+1)+'" name="bind_var_nm" value="'+bind_var_nm+'" class="tac textbox-text" autocomplete="off" readonly style="margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px;width:85%;">';
			strHtml += '	</span>';			
			strHtml += "</td>";
			
//			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='w130 easyui-combobox'><option value='string' selected>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";
//			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' data-options=\"panelHeight:'auto',editable:false\" class='w80 easyui-combobox'><option value='string' selected>String</option><option value='number'>Number</option><option value='date'>Date</option><option value='char'>Char</option></select></td>";

			strHtml += "<td class='ctext'>";
			strHtml += '	<span class="textbox combo">';
			strHtml += "		<select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='textbox-text validatebox-text w80'><option value='string' selected>String</option><option value='number'>Number</option><option value='date'>Date</option><option value='char'>Char</option></select>";
			strHtml += '	<span>';
			strHtml += "</td>";
			
			console.log("bind_var_nm_value_arr:",bind_var_nm_value_arr);
			if(bind_var_nm_value_arr != undefined){
				bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
				//console.log("bind_var_nm_value_arr_length:",bind_var_nm_value_arr_length);
				if(bind_var_nm_value_arr_length > 0){
					for(var j=0;j<bind_var_nm_value_arr_length;j++){
						
						bind_var_value_one = bind_var_nm_value_arr[bind_var_nm];
						if(bind_var_value_one == undefined) bind_var_value_one = "";
	//					if(bind_var_nm == bind_var_nm_arr_one){
	//						bind_var_value_one = bind_var_value_arr[j];
	//					}
					}
				}
			}else{
				bind_var_nm_value_arr = "";
			}
			if(bind_var_value_one == undefined) bind_var_value_one = "";
//			if(compare_bind_var_value != undefined && compare_bind_var_value != "") bind_var_value_one = compare_bind_var_value;
			if(compare_bind_var_value != undefined) bind_var_value_one = compare_bind_var_value;
			
			strHtml += "<td class='ctext'>";
			strHtml += '	<span class="textbox tac" style="width: 100%;">';
//			strHtml += "		<input type='text' id='bind_var_value"+(i+1)+"' style='width:100%' name='bind_var_value' value='"+bind_var_value_one+"' class='w130 easyui-textbox'/>";
			strHtml += '		<input type="text" id="bind_var_value'+(i+1)+'" name="bind_var_value" value="'+bind_var_value_one+'" class="tac textbox-text validatebox-text" autocomplete="off" style="margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 23px; line-height: 23px;width:85%;">';
			strHtml += "		<input type='hidden' id='hidden_bind_var_value"+(i+1)+"' name='hidden_bind_var_value' value='"+bind_var_value_one+"' />";
			strHtml += '	</span>';
			strHtml += "</td>";
			
			strHtml += "</tr>";
		}

		$("#bindTbl tbody").append(strHtml);
		
	}
	
	//console.log("resultArr.length:"+resultArr.length);
	bind_var_nm_value_arr = [];
	for(var i=0;i<resultArr.length;i++){
		var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
		var bind_var_value = $("#bind_var_value"+(i+1)).val();
		bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
	}
	
}
