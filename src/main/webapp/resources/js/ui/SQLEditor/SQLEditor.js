var perfCheckDbid = '';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	let bindTooltip = "&#45; 변수명 앞에 특수문자(':'-Colon)가 존재하면 Bind 변수로 인식한다.";
	
	$('#bind_tooltip').tooltip({
		content : '<span style="color:#fff">' + bindTooltip + '</span>',
		onShow : function() {
			$(this).tooltip('tip').css({
				backgroundColor : '#5b5b5b',
				borderColor : '#5b5b5b'
			});
		}
	});

	$('#base_retrieve').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBaseCommand(Number($(this).val()));
			}
		}
	});
	
	$('#base_select').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBaseCommand(Number($(this).val()));
			}
		}
	});
	
	$('#base_update').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBaseCommand(Number($(this).val()));
			}
		}
	});
	
	$('#base_ddl').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBaseCommand(Number($(this).val()));
			}
		}
	});
});

function resetBaseCommand(base_command_type) {
	$('#base_command_type').val(base_command_type);
	
	switch(base_command_type) {
	case 0:
		$('#base_retrieve').radiobutton({
			checked: true
		});
		
		$('#base_select').radiobutton({
			checked: false
		});
		
		$('#base_update').radiobutton({
			checked: false
		});
		
		$('#base_ddl').radiobutton({
			checked: false
		});
		break;
	case 1:
		$('#base_retrieve').radiobutton({
			checked: false
		});
		
		$('#base_select').radiobutton({
			checked: true
		});
		
		$('#base_update').radiobutton({
			checked: false
		});
		
		$('#base_ddl').radiobutton({
			checked: false
		});
		break;
	case 2:
		$('#base_retrieve').radiobutton({
			checked: false
		});
		
		$('#base_select').radiobutton({
			checked: false
		});
		
		$('#base_update').radiobutton({
			checked: true
		});
		
		$('#base_ddl').radiobutton({
			checked: false
		});
		break;
	case 3:
		$('#base_retrieve').radiobutton({
			checked: false
		});
		
		$('#base_select').radiobutton({
			checked: false
		});
		
		$('#base_update').radiobutton({
			checked: false
		});
		
		$('#base_ddl').radiobutton({
			checked: true
		});
		break;
	}
}

function createSqlDesc(){
	var bind_var_nm_obj = $("input[name='bind_var_nm']");
	var bind_var_value_obj = $("input[name='bind_var_value']");
	var sqlDesc = "";
	for(var i=0;i<bind_var_nm_obj.length;i++){
		sqlDesc += $(bind_var_nm_obj[i]).val()+" "+ $(bind_var_value_obj[i]).val()+"\n";
	}
//	console.log("sqlDesc:"+sqlDesc);
	return sqlDesc;
}

var bind_var_nm_value_arr = [];
var bind_var_nm_value_arr_length = 0;
var bind_var_value_one = "";
var bind_var_value_arr = [];

function setBindValue(){
	var resultArr = [];
	
	if(bind_var_nm_value_arr != undefined){
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
//		console.log("bind_var_nm_value_arr_length1:",bind_var_nm_value_arr_length);
		if(bind_var_nm_value_arr_length > 0){
			for(var i=0;i<bind_var_nm_value_arr_length;i++){
				var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
				var bind_var_value = $("#bind_var_value"+(i+1)).val();
//				console.log(bind_var_nm+"=",bind_var_value);
				if(bind_var_nm != undefined){
					bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
				}
//				console.log("bind_var_nm_value_arr:",bind_var_nm_value_arr);
			}
		}
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
//		console.log("bind_var_nm_value_arr_length2:",bind_var_nm_value_arr_length);
	}
	
	var bindArry = pullOutBindArray($("#sql_editor").val());
	
	if(bindArry != null){
		$.each(bindArry, function(key, value){
			if($.inArray(value, resultArr) === -1){
				var regularExp = new RegExp(/\,|\)|\;|\|\|/g);
				value = value.replace(regularExp,'').trim();
				var n = false;
				if(resultArr.indexOf(value) !== -1){
					n = true;
				}
				if(!n){
//					console.log("value:"+value+" index:"+value.indexOf(':MI:SS'));
					if(value.indexOf(':MI:SS') < 0){
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
			var bind_var_nm = strExpReplace2(strReplace(resultArr[i],' ',''));
			//console.log("resultArr@@@:"+strExpReplace2(strReplace(resultArr[i],' ','')));
			strHtml += "<tr><td class='ctext'>";
			strHtml += "<input type='hidden' id='bind_set_seq"+(i+1)+"' name='bind_set_seq' value='1'/>";
			strHtml += "<input type='hidden' id='mandatory_yn"+(i+1)+"' name='mandatory_yn' value='Y'>";
			strHtml += "<input type='text' id='bind_seq"+(i+1)+"' name='bind_seq' value='"+(i+1)+"' data-options='readonly:true' class='easyui-textbox' style='text-align:right;width:25px;' /></td>";
			strHtml += "<td class='ctext'><input type='text' id='bind_var_nm"+(i+1)+"' name='bind_var_nm' value='"+strExpReplace2(strReplace(resultArr[i],' ',''))+"' class='w130 easyui-textbox'/></td>";
			
//			console.log("bind_var_nm_value_arr1:",bind_var_nm_value_arr);
//			console.log("bind_var_nm_value_arr2:",bind_var_nm_value_arr);
			if(bind_var_nm_value_arr != undefined){
				bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
				//console.log("bind_var_nm_value_arr_length:",bind_var_nm_value_arr_length);
				if(bind_var_nm_value_arr_length > 0){
					for(var j=0;j<bind_var_nm_value_arr_length;j++){
						
						bind_var_value_one = bind_var_nm_value_arr[bind_var_nm];
						//console.log("bind_var_value_one:"+bind_var_value_one);
						if(bind_var_value_one == undefined) bind_var_value_one = "";
					}
				}
			}else{
				bind_var_nm_value_arr = "";
			}
			if(bind_var_value_one == undefined) bind_var_value_one = "";
			
			strHtml += "<td class='ctext'><input type='text' id='bind_var_value"+(i+1)+"' name='bind_var_value' value='"+bind_var_value_one+"' class='w130 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='w130 easyui-combobox'><option value='string' selected>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td></tr>";
		}
		
		$("#bindTbl tbody").append(strHtml);
		
		for(var i = 0 ; i < bindArry.length ; i++){
			$("#bindTbl #bind_seq"+(i+1)).textbox({readonly:true});
			$("#bindTbl #bind_var_nm"+(i+1)).textbox();
			$("#bindTbl #bind_var_type"+(i+1)).combobox();
			$("#bindTbl #bind_var_value"+(i+1)).textbox();
		}
	}
	
	if(resultArr.length > 0){
		//console.log("resultArr.length:"+resultArr.length);
		bind_var_nm_value_arr = [];
		for(var i=0;i<resultArr.length;i++){
			var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
			var bind_var_value = $("#bind_var_value"+(i+1)).val();
			bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
			//console.log("bind_var_nm_value_arr:",bind_var_nm_value_arr);
		}
	}
	//console.log("bind_var_nm_value_arr.length:",Object.keys(bind_var_nm_value_arr).length);
}

function validateFunction() {
	var retFlag = false;
	var sqlText = $('#sql_editor').val();
	
	if(sqlText == '') {
		parent.warningMessager("SQL을 입력해 주세요.");
		return retFlag;
	}
	
	var bindArray = pullOutBindArray(sqlText);
	var resultArr = [];
	
	if(bindArray != null && bindArray.length > 0) {
		$.each(bindArray, function(key, value){
			if($.inArray(value, resultArr) === -1){
				let regularExp = /\,|\)|\;/g;
				value = value.replace(regularExp,'').trim();
				var n = false;
				if(resultArr.indexOf(value) !== -1){
					n = true;
				}
				if(!n){
//					console.log("value:"+value+" index:"+value.indexOf(':MI:SS'));
					if(value.indexOf(':MI:SS') < 0){
						resultArr.push(value);
					}
				}
			}
		});
		
		var names = findBindNames();
		var values = findBindValues();
		var types = findBindTypes();
		
		for(var i=0;i<names.length;i++){
			var bind_var_nm = names[i];
			var bind_var_value = values[i];
			var bind_var_type = types[i];
			
			if(bind_var_value == 'undefined' || bind_var_value.length == 0) {
				continue;
			}
			
			sqlText = replaceAll(sqlText, bind_var_nm, bind_var_value, bind_var_type);
		}
	}
	
	if(validateSQL(sqlText) != null && validateSQL(sqlText).length > 0) {
		parent.warningMessager("변수 정보가 정의되지 않았습니다.");
		return retFlag;
	}
	
	if($('#rownum').numberbox("getValue") != null && $('#rownum').numberbox("getValue") == 0) {
		parent.warningMessager("ROWNUM 정보가 정의되지 않았습니다.");
		return retFlag;
	}
	
	sqlText = sqlText.replace(/;/g, '');
	
//	console.log("sql_text\n" + sqlText);
	$('#sql_text').val(sqlText);
	
	return true;
}

function Btn_OnClick(){
	if(validateFunction() == false) {
		return;
	}
//	$("#rownum").val( $("#rownum_input").val() );
	var url = '/SQLEditor/Retrieve';
	
	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_OnClick);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("","조회중입니다.");
}

function findBindNames() {
	var _this = $('#bindTbl tr:gt(0)');
	var _this_len = _this.length;
	var names = new Array();
	
	for(var i=0;i<_this_len;i++){
		names.push($('#bindTbl tr:gt(' + i + ') > td')[1].childNodes[0].value);
	}
	
//	console.log('names:' + names);
	
	return names;
}

function findBindValues() {
	var _this = $('#bindTbl tr:gt(0)');
	var _this_len = _this.length;
	var values = new Array();
	
	for(var i=0;i<_this_len;i++){
		values.push($('#bindTbl tr:gt(' + i + ') > td')[2].childNodes[0].value);
	}
	
//	console.log('values:' + values);
	
	return values;
}

function findBindTypes() {
	var _this = $('#bindTbl tr:gt(0)');
	var _this_len = _this.length;
	var types = new Array();
	
	for(var i=0;i<_this_len;i++){
		types.push($('#bindTbl tr:gt(' + i + ') > td')[3].childNodes[0].value);
	}
	
//	console.log('types:' + types);
	
	return types;
}

function Dis_Conn() {
	var url = '/SQLEditor/DisConn';
	
	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_DisConn);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("","조회중입니다.");
}

var callback_DisConn = function(result) {
	parent.$.messager.progress('close');
	
	var headArray = new Array();
	
	try {
		var message = result.message;
		
		$('#tableList').datagrid("loadData", [])
		getByGrid(headArray, !Boolean(result.result));
		$('#tableList').datagrid({rownumbers:false});
		$('#tableList').datagrid('reload');
		$('#tableList').datagrid('appendRow',{info:message});
	} catch(err) {
		if(result.result) {
			$('#connInfo').textbox('setValue', '');
		}
		
		errorFlow(err, result);
	}
}

function transaction(isCommit) {
	var sqlText = '';
	
	if(isCommit) {
		sqlText = 'commit';
	} else {
		sqlText = 'rollback';
	}
	
	$('#sql_text').val(sqlText);
	
	var url = '/SQLEditor/Retrieve';
	
	ajaxCall(url,
			$("#submit_form"),
			"POST",
			callback_OnClick);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("","조회중입니다.");
}

function Excel_Download(){
	let data = $("#tableList").datagrid("getData");
	
	if ( data.rows.length == 0 ) {
		parent.warningMessager("SQL을 실행후 엑셀다운로드 진행하여주십시요.");
		return false;
	}
	
	if(validateFunction() == false) {
		return;
	}
	
	let original_rowNum = $("#rownum").numberbox("getValue");
	$("#rownum").numberbox("setValue", data.rows.length);
	console.log( data.rows.length +" , "+$("#rownum").numberbox("getValue") );
	
	var url = '/SQLEditor/ExcelRetrieve';
	
	$("#submit_form").attr("action",url);
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
	$("#rownum").numberbox("setValue", original_rowNum);
	openMessageProgress();
}

function replaceAll(str, searchStr, replaceStr, type) {
	var tempReplaceStr = replaceStr;
	
	if(type != 'number') {
		tempReplaceStr = "'" + replaceStr + "'";
	}
	
	return str.split(searchStr).join(tempReplaceStr);
}

function validateSQL(sqlText) {
	return pullOutBindArray(sqlText);
}

//callback 함수
var callback_OnClick = function(result) {
	parent.$.messager.progress('close');
	
	$('#tableList').datagrid("loadData", []);
	
	callback_Retrieve(result);
}

function callback_Retrieve(result) {
	var data;
	var headArray = new Array();
	
	try {
		data = JSON.parse(result);
		var rows = data.rows;
		var dataLength = rows.length;
		
		if(dataLength > 0) {
			var connInfoRow = rows.splice(rows.length - 1, 1);
			$('#connInfo').textbox('setValue', connInfoRow[0].CONN_INFO);
			
			var headRow = rows.splice(rows.length - 1, 1);
//			console.log(headRow);
			
			for(var headIndex = 0; headIndex < headRow.length; headIndex++) {
				var head = headRow[0].HEAD;
				
				headArray = head.split('\;');
				
//				console.log(headArray);
			}
		}
		
		var pureData = rows;
		getByGrid(headArray, false);
		
//		data.rows = pureData;
//		var jsonData = JSON.stringify(data);
//		json_string_callback_common(jsonData, '#tableList', true);
		
		$('#tableList').datagrid('loadData', pureData);
	} catch(err) {
		errorFlow(err, result);
	}
}

function callback_ExecuteQuery(result) {
	var data;
	var headArray = new Array();
	
	try {
		data = JSON.parse(result);
		var rows = data.rows;
		var dataLength = rows.length;
		
		if(dataLength > 0) {
			var headRow = rows.splice(dataLength - 1, 1);
//			console.log(headRow);
			
			for(var headIndex = 0; headIndex < headRow.length; headIndex++) {
				var head = headRow[0].HEAD;
				
				headArray = head.split('\;');
				
//				console.log(headArray);
			}
		}
		
		var pureData = rows;
		getByGrid(headArray, false);
		
		data.rows = pureData;
		
		json_string_callback_common(JSON.stringify(data), '#tableList', true);
	} catch(err) {
		errorFlow(err, result);
	}
}

function callback_ExecuteUpdate(result) {
	try {
		infoFlow(result.message);
	} catch(err) {
		errorFlow(err);
	}
}

function callback_Execute(result) {
	try {
		infoFlow(result.message);
	} catch(err) {
		errorFlow(err);
	}
}


function infoFlow(message) {
	var headArray = new Array();
	
	getByGrid(headArray, false);
	$('#tableList').datagrid('appendRow',{info:message});
}

function errorFlow(err, result) {
	var message = '';
	var headArray = new Array();
	
	try {
		if(err.message.indexOf('Unexpected') == 0) {
			message = result.substring(result.indexOf('message') + 10, result.length - 2);
		} else {
			var data = JSON.parse(result);
			
			if(data.result == false) {
				message = data.message;
			} else {
				message = err.message;
			}
		}
	} catch(err) {
		message = result.message;
	}
	
	$('#tableList').datagrid("loadData", [])
	getByGrid(headArray, true);
	$('#tableList').datagrid({rownumbers:false});
	$('#tableList').datagrid('reload');
	$('#tableList').datagrid('appendRow',{error:message});
}

function getByGrid(headArray, isError){
	var jsonArray = new Array();
	var jsonMap = new Object();
	var jsonArrayColumnGroup = new Array();
	var jsonMap2 = new Object();
	var jsonMap3 = new Object();
	
	if(isError) {
		jsonMap = new Object();  //맵객체를 새로만듬.
		jsonMap.field='error';
		jsonMap.title='에러';
		jsonMap.width='100%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	} else {
		if(headArray.length == 0) {
			jsonMap = new Object();  //맵객체를 새로만듬.
			//jsonMap.field='coulmn_'+i;
			jsonMap.field='info';
			jsonMap.title='정보';
			jsonMap.width='100%';
			jsonMap.halign='center';
			jsonMap.align='center';
			jsonMap.fixed = 'true';
			jsonArray.push(jsonMap);
		}
		
		for(var i = 0; i < headArray.length; i++){
			var head = headArray[i];
			var options = head.split('\/');
			jsonMap = new Object();  //맵객체를 새로만듬.
			jsonMap.field=options[0];
			jsonMap.title=options[0];
			/*
			if(i == headArray.length - 1) {
				jsonMap.width='100%';
			}
			*/
			jsonMap.halign='center';
//			jsonMap.align=options[1];
			jsonMap.align=setAlign(options[1]);
			jsonArray.push(jsonMap);
		}
	}
	
	$("#tableList").datagrid({
		view:scrollview,
		nowrap : true,
		//fitColumns:true,
		striped:true,
		autoRowHeight:false,
		singleSelect:true,
		rownumbers:true,
		pageSize:1000,
		columns:[jsonArray],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data){
			if(data.rows.length > 1){
				$("#tableList").datagrid('autoSizeColumn');
			}
		}
	});
	
}

function setAlign(typeName) {
	var align = '';
	
	if(typeName.toUpperCase() === 'NUMBER') {
		align = 'right';
	} else if(typeName.toUpperCase() === 'VARCHAR' || typeName.toUpperCase() === 'VARCHAR2' ||
			typeName.toUpperCase() === 'CLOB') {
		align = 'left';
	} else {
		align = 'center';
	}
	
	return align;
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
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL Editor Excel 생성"," ");
	
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