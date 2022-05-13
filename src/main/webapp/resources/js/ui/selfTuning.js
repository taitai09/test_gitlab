$(document).ready(function() {
	// 업무구분 조회			
	$('#selectWrkJob').combobox({
		url:"/Common/getWrkJobDev",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onSelect:function(rec){
			$("#wrkjob_cd").val(rec.wrkjob_cd);
			$("#dbid").val(rec.dbid);
			
			// parsing_schema_name 조회			
//			$('#parsing_schema_name').combobox({
//				url:"/SelfTuning/getParsingSchemaName?dbid="+$("#dbid").val(),
//				method:"get",
//				valueField:'username',
//				textField:'username'
//			});	
			
			$("#parsing_schema_name").combobox({
				url:"/Common/getUserName?dbid="+$("#dbid").val(),
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
		}
//		,
//		onChange:function() {
//			$("#selectWrkJob").combobox('textbox').css("background-color","beige");
//		}
	
	});	
	
	$("#dbioList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#tr_cd").val(row.appl_name);
			$("#dbio").val(row.dbio);
			$("#current_elap_time").val(row.elapsed_time);
			$("#sql_text").val(row.sql_text);
			
			// bind값 셋팅
			setBindValue();
		},		
		columns:[[
			{field:'appl_name',title:'소스파일명(Full Path)',width:'350px',halign:"center",align:"left",sortable:"true"},
			{field:'dbio',title:'SQL식별자(DBIO)',width:'350px',halign:"center",align:'center',sortable:"true"},
			{field:'exec_cnt',title:'수행횟수',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'sql_text',title:'SQL TEXT',width:"500px",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#testResultList").datagrid({
		view: myview,
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,		
		onClickRow : function(index,row) {
			$("#selftun_query_seq").val(row.selftun_query_seq);
			showSelfTuningSqlPopup();
		},		
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'selftun_query_seq',hidden:"true"},
			{field:'last_active_time',title:'수행시간',halign:"center",align:"center",sortable:"true",styler:cellStyler},
			{field:'sql_id',title:'SQL_ID',width:"150px",halign:"center",align:"left",styler:cellStyler},
			{field:'plan_hash_value',title:'PLAN_HASH_VALUE',halign:"center",align:'center',styler:cellStyler},
			{field:'elapsed_time',title:'ELAPSED_TIME',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},
			{field:'cpu_time',title:'CPU_TIME',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},
			{field:'buffer_gets',title:'BUFFER_GETS',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},
			{field:'disk_reads',title:'DISK READS',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},
			{field:'rows_processed',title:'ROWS_PROCESSED',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},			
			{field:'parse_calls',title:'PARSE_CALLS',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},			
			{field:'user_io_wait_time',title:'USER_IO_WAIT_TIME',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},
			{field:'fetches',title:'FETCHES',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat},			
			{field:'direct_writes',title:'DIRECT WRITES',halign:"center",align:'right',styler:cellStyler,formatter:getNumberFormat}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#selfTuningTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			
			if(tabClickCount < searchBtnClickCount){
				$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
				if(index == 0){
				}else if(index == 1){
				}else if(index == 2){
					Btn_ReqExplainPlan();
				}
			}
		}
	});	
	
	$('#treePlan').tree({
		animate:true,
		lines:true,
		formatter:function(node){
			var str = "";
			var text = node.text;

			if (node.attributes.partition_start != "" || node.attributes.distribution != "" || node.attributes.access_predicates != "" || node.attributes.filter_predicates != ""){
				if(node.attributes.table_owner == "" && node.attributes.table_name == ""){
					str += "<a href='javascript:;' class='help easyui-tooltip' ";	
				}else{
					str += "<a href='javascript:showExplainPlanPopup(\""+node.attributes.table_owner+"\",\""+node.attributes.table_name+"\")' class='help easyui-tooltip' ";
				}
				str += "data-options=\"content:$('<div></div>'),";
				str += "onUpdate: function(cc){";
				str += "cc.panel({";
				str += "width: 500,";
				str += "height: 'auto',";
				str += "border: false,";
				str += "content:'<b>PARTITION : " + node.attributes.partition_start + "<br/>";
				str += "DISTRIBUTION : " + node.attributes.distribution + "<br/>";
				str += "ACCESS_PREDICATES : " + strReplace(strReplace(node.attributes.access_predicates,"'","`"),"\"","&quot;") + "<br/>";
				str += "FILTER_PREDICATES : " + strReplace(strReplace(node.attributes.filter_predicates,"'","`"),"\"","&quot;") + "<br/></b>'})}";
				str += "\">";
				
				if(text.indexOf("FULL") > -1 || text.indexOf("CARTESIAN") > -1){
					text = "<span style='color:red;font-weight:bold'>" + strReplace(strReplace(text,"'","`"),"\"","&quot;") + "</span>";
				}
				str += strReplace(strReplace(text,"'","`"),"\"","&quot;") + "</a>";
			}else{
				if(node.attributes.table_owner == "" && node.attributes.table_name == ""){
					str += strReplace(strReplace(text,"'","`"),"\"","&quot;");
				}else{
					str += "<a href='javascript:showExplainPlanPopup(\""+node.attributes.table_owner+"\",\""+node.attributes.table_name+"\")'>";
					str += strReplace(strReplace(text,"'","`"),"\"","&quot;");
					str += "</a>";
				}
			}
			
			return str;
		}
	});
	
	$('#selectKey').combobox('setValue',$("#searchKey").val());
	if($("#wrkjob_cd").val() != ""){
//		$('#selectWrkJob').combobox('setValue',$("#wrkjob_cd").val()+"|"+$("#dbid").val());	
		$('#selectWrkJob').combobox('setValue',$("#wrkjob_cd").val());
	}

	$('#dbioList').datagrid("loadData", []);

	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("셀프튜닝","SQL식별자(DBIO) 목록을 조회중입니다.");

	ajaxCall("/SelfTuningAction",
		$("#submit_form"),
		"POST",
		callback_SelfTuningAddTable);

	setBindValue();
});

function cellStyler(value,row,index){
	var elapsed_time = row.elapsed_time;
	var buffer_gets = row.buffer_gets;
	if(elapsed_time > 3 || buffer_gets > 1000){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';
	}
}

function Btn_OnClick(){
	if($('#wrkjob_cd').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if($("#dbio_search_yn").val() == "Y"){
		if(($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
			($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','조회 구분값을 입력해주세요.');
			return false;
		}
		$("#searchKey").val($('#selectKey').combobox('getValue'));
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#testResultList').datagrid("loadData", []);
	
	$("#submit_form").attr("action","/SelfTuning");
	$("#submit_form").submit();
}

//callback 함수
var callback_SelfTuningAddTable = function(result) {
	json_string_callback_common(result,'#dbioList',true);
};

function Btn_RequestTuning(){
	if($('#wrkjob_cd').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if($("#parsing_schema_name").combobox("getValue") == ""){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if($("#sql_text").val() == ""){
		parent.$.messager.alert('','튜닝요청 할 SQL TEXT를 입력해 주세요.');
		return false;
	}

//	ajaxCall("/RequestTuningAction", $("#submit_form"), "POST", callback_RequestTuningAction);
	Btn_RequestTuningStep2();
}

//var callback_RequestTuningAction = function(result) {
//	try{
//		var data = JSON.parse(result);
//		if(data.result != undefined && !data.result){
//			if(data.message == 'null'){
//				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
//			}else{
//				parent.$.messager.alert('',data.message);
//			}
//		}else{
//			Btn_RequestTuningStep2();
//		}
//	}catch(e){
//		parent.$.messager.alert('',e.message);
//	}
//
//};

function Btn_RequestTuningStep2(){
	var menuId = $("#menu_id").val() + "1";
	var menuNm = "SQL 셀프 성능 점검 :: 튜닝요청";
	var menuUrl = "/SelfTuning/RequestTuning";
	
	//※ SQL BIND 값 변환
	var sqlDesc = createSqlDesc();
	$("#submit_form #sql_desc").val(sqlDesc);
	$("#submit_form #wrkjob_cd").val($('#selectWrkJob').combobox('getText'));
	var temp_sql_text = $("#sql_text").val();
	$("#sql_text").val("");
	var menuParam = $("#submit_form").serialize();
	$("#sql_text").val(temp_sql_text);
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

function createSqlDesc(){
	var bind_var_nm_obj = $("input[name='bind_var_nm']");
	var bind_var_value_obj = $("input[name='bind_var_value']");
	var sqlDesc = "";
	for(var i=0;i<bind_var_nm_obj.length;i++){
		sqlDesc += $(bind_var_nm_obj[i]).val()+" "+ $(bind_var_value_obj[i]).val()+"\n";
	}
	console.log("sqlDesc:"+sqlDesc);
	return sqlDesc;
}
/**
 * 성능점검수행 버튼 클릭시
 * @returns
 */
function Btn_SelfTuningTest(){
	if($('#wrkjob_cd').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if($("#parsing_schema_name").combobox("getValue") == ""){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if($("#sql_text").val() == ""){
		parent.$.messager.alert('','셀프테스트 진행 할 SQL TEXT를 입력해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();
	
	$('#selfTuningTab').tabs('select', 0);

	ajaxCall("/SelfTuning/SelfTest", $("#submit_form"), "POST", callback_SelfTestAction);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("셀프 튜닝","셀프 테스트를 진행중입니다.");
}

//callback 함수
var callback_SelfTestAction = function(result) {
	parent.$.messager.progress('close');
	
	if(result.result){
		var strHtml = "";
		$("#textPlan li").remove();		
		strHtml += "<li><b>ExecutionPlan</b></li>";
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		for(var j = 1; j < result.list[0].length ; j++){ // ExecutionPlan
			var post = result.list[0][j];		
			strHtml += "<li class='selfLi'>"+strReplace(post.execution_plan,' ','&nbsp;')+"</li>";
		}
		strHtml += "<li>---------------------------------------------------------------------------------------------</li>";
		$("#textPlan").append(strHtml);
		
		var data = result.list[1];
		$('#testResultList').datagrid("appendRow", {
			selftun_query_seq : data.selftun_query_seq,
			last_active_time : data.last_active_time,
			sql_id : data.sql_id,
			plan_hash_value : data.plan_hash_value,
			parse_calls : data.parse_calls,
			buffer_gets : data.buffer_gets,
			rows_processed : data.rows_processed,
			elapsed_time : strParseFloat(data.elapsed_time,0),
			cpu_time : strParseFloat(data.cpu_time,0),
			user_io_wait_time : data.user_io_wait_time,
			fetches : data.fetches,
			disk_reads : data.disk_reads,
			direct_writes : data.direct_writes
		});

		$('#testResultList').datagrid('loaded');		
	}else{
		parent.$.messager.alert('ERROR',result.message,'error', function(){
			$('#testResultList').datagrid('loaded');
			$("#textPlan li").remove();
		});		
	}
};

function Btn_TableShowYn(strName, showYn){
	if(strName == "dbio"){
		if(showYn == "Y"){
			$('#dbioDiv').panel('expand',true);
		}else{
			$('#dbioDiv').panel('collapse',true);
		}
	}else{
		if(showYn == "Y"){
			$("#testTbl").show();
			$("#testDiv").show();
		}else{
			$("#testTbl").hide();
			$("#testDiv").hide();
		}
	}
}

var bind_var_nm_value_arr = [];
var bind_var_nm_value_arr_length = 0;
var bind_var_value_one = "";

function setBindValue(){
	var resultArr = [];

	strText = $("#sql_text").val() + " \n";
	strText = strText.replace(/--.*\n/g,'');
	strText = strText.replace(/\}/g,'');
	strText = strText.replace(/\$\{/g,':');

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
	
	var patten = /\:.+?(\s|,|;|\))/g; /* ':' 문자부터 공백이나 ',' 이나 ';' 이나 ')' 이전 까지의 문자 패턴 */
	strText = strText.replace(/\r/g, '');  /* /r 제거 */ 
	strText = strText.replace(/\n/g, ' '); /* /n => 공백 */	
	//console.log("strText2: " + strText);
	
	//var bindArry = strText.replace(/\-.+?(\n|,|;|\))/g, '').match(patten);
	var bindArry = strText.replace(/(\/\*).+?(\*\/)/g, '').match(patten); /* 주석/힌트 제거, pattern match */
	
	if(bindArry != null){
		$.each(bindArry, function(key, value){
			if($.inArray(value, resultArr) === -1){
				value = value.replace(/\)/g,'').trim();
				//var n = resultArr.includes(value);
				var n = false;
				if(resultArr.indexOf(value) !== -1){
					n = true;
				}
				if(!n){
					console.log("value:"+value+" index:"+value.indexOf(':MI:SS'));
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
			strHtml += "<input type='text' id='bind_seq"+(i+1)+"' name='bind_seq' value='"+(i+1)+"' data-options='readonly:true' class='w50 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='bind_var_nm"+(i+1)+"' name='bind_var_nm' value='"+strExpReplace2(strReplace(resultArr[i],' ',''))+"' class='w130 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='w130 easyui-combobox'><option value='string' selected>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";
			
			console.log("bind_var_nm_value_arr1:",bind_var_nm_value_arr);
			console.log("bind_var_nm_value_arr2:",bind_var_nm_value_arr);
			if(bind_var_nm_value_arr != undefined){
				bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
				//console.log("bind_var_nm_value_arr_length:",bind_var_nm_value_arr_length);
				if(bind_var_nm_value_arr_length > 0){
					for(var j=0;j<bind_var_nm_value_arr_length;j++){
						
						bind_var_value_one = bind_var_nm_value_arr[bind_var_nm];
						//console.log("bind_var_value_one:"+bind_var_value_one);
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
			strHtml += "<td class='ctext'><input type='text' id='bind_var_value"+(i+1)+"' name='bind_var_value' value='"+bind_var_value_one+"' class='w130 easyui-textbox'/></td></tr>";
			
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

function Btn_ReqExplainPlan(){
	if($('#wrkjob_cd').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if($("#parsing_schema_name").combobox("getValue") == ""){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if($("#sql_text").val() == ""){
		parent.$.messager.alert('','Explain Plan 진행 할 SQL TEXT를 입력해 주세요.');
		return false;
	}
	
	$('#selfTuningTab').tabs('select', 2);
	ajaxCall("/SelfTuning/ExplainPlanTree",
			$("#submit_form"),
			"POST",
			callback_ExplainPlanTreeAction);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("셀프 튜닝","Explain Plan을 진행중입니다.");
}

//callback 함수
var callback_ExplainPlanTreeAction = function(result) {
	parent.$.messager.progress('close');
	$('#treePlan').tree("loadData", []);
	
	if(result.result){
		var data = JSON.parse(result.txtValue);
		$('#treePlan').tree("loadData", data);

		$('.help').tooltip({
			position: 'right'
		});
	}else{
		parent.$.messager.alert('ERROR',result.message,'error', function(){
			$('#treePlan').tree("loadData", []);
		});
	}
};

/*셀프 테스트 상세 팝업 */
function showSelfTuningSqlPopup(){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#sql_text_pop").val("");
	$("#bindPopTbl tbody tr").remove();
	$('#selftunSqlPop').window("open");
	
	ajaxCall("/SelfTuning/Popup/SelfTuningSqlInfo",
			$("#submit_form"),
			"POST",
			callback_SelfTuningSqlInfoPopupAction);
}

//callback 함수
var callback_SelfTuningSqlInfoPopupAction = function(result) {
	var strHtml = "";
	if(result.result){
		var selfSql = result.list[0];
		
		var lines = selfSql.sql_text.split("\n");
		var newText = "";
		for (var i = 1; i < lines.length; i++) {
			newText += lines[i] + "\n";
		}

		$("#sql_text_pop").val(newText);
		$("#bindCnt").val(result.list[1].length);
		
		for(var i = 0 ; i < result.list[1].length ; i++){
			var post = result.list[1][i];
			
			strHtml += "<tr><td class='ctext'>";
			strHtml += "<input type='hidden' id='bind_p_seq"+(i+1)+"' name='bind_p_seq' value='"+(i+1)+"'/>"+ post.bind_seq +"</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_nm"+(i+1)+"' name='bind_p_var_nm' value='"+post.bind_var_nm+"'/>"+post.bind_var_nm+"</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_type"+(i+1)+"' name='bind_p_var_type' value'"+post.bind_var_type+"'/>";
			if(post.bind_var_type == "string"){
				strHtml += "String타입";	
			}else if(post.bind_var_type == "number"){
				strHtml += "Number타입";
			}else if(post.bind_var_type == "date"){
				strHtml += "Date타입";
			}else if(post.bind_var_type == "char"){
				strHtml += "Char타입";
			}
			strHtml += "</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_value"+(i+1)+"' name='bind_p_var_value' value='"+post.bind_var_value+"'/>"+post.bind_var_value+"</td></tr>";
		}

		$("#bindPopTbl tbody").append(strHtml);
	}else{
		parent.$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('selftunSqlPop');
		});
	}
};

/* 인덱스 자동설계 팝업 */
function showIndexAutoDesign(){
	if($('#wrkjob_cd').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if($("#dbio_search_yn").val() == "Y"){
		if(($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
			($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','조회 구분값을 입력해주세요.');
			return false;
		}
		$("#searchKey").val($('#selectKey').combobox('getValue'));
	}
	
	if($('#sql_text').val() == ""){
		parent.$.messager.alert('','인덱스 자동설계를 진행할 쿼리를 입력해 주세요.');
		return false;
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$('#selfIndexResultList').datagrid('loadData',[]);
	$('#selfIndexAutoDesignPop').window("open");
}

/* Explain Plan 정보 팝업 */

function showExplainPlanPopup(tableOwner, tableName){
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$(".tableTitle").html(tableOwner + "." + tableName);
	
	$("#explain_form #dbid").val($("#submit_form #dbid").val());
	$("#explain_form #owner").val(tableOwner);
	$("#explain_form #table_name").val(tableName);
	
	$('#explainPlanPop').window("open");
	$("#columnInfoTbl tbody tr").remove();
	
	$('#columnInfoList').datagrid('loadData',[]);
	$('#indexInfoList').datagrid('loadData',[]);
	$('#statisticsList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("셀프 튜닝","Explain Plan 정보를 조회중입니다.");	

	ajaxCall("/SelfTuning/Popup/ExplanPlanDetail",
			$("#explain_form"),
			"POST",
			callback_ExplanPlanDetailPopupAction);
	
	ajaxCall("/SelfTuning/Popup/ExplanPlanDetail2",
			$("#explain_form"),
			"POST",
			callback_ExplanPlanDetailPopup2Action);
	
	ajaxCall("/SelfTuning/Popup/ExplanPlanStatistics",
			$("#explain_form"),
			"POST",
			callback_ExplanPlanStatisticsPopupAction);
}

//callback 함수
var callback_ExplanPlanDetailPopupAction = function(result) {
	var strHtml = "";
	$("#columnInfoTbl tbody tr").remove();
	
	if(result.result){
		var post = result.object;
		
		strHtml += "<tr><th>COMMENTS</th>";
		strHtml += "<th colspan='3'>: "+ nvl(post.comments,"") +"</th></tr>";
		strHtml += "<tr><th>PARTITIONED</th>";
		strHtml += "<th colspan='3'>: "+ nvl(post.partitioned,"") +"</th></tr>";
		strHtml += "<tr><th>PARTITIONING_TYPE</th>";
		strHtml += "<th colspan='3'>: "+ nvl(post.partitioning_type,"") +"</th></tr>";
		strHtml += "<tr><th>PART_KEY_COLUMN</th>";
		strHtml += "<th colspan='3'>: "+ nvl(post.part_key_column,"") +"</th></tr>";
		strHtml += "<tr><th>SUBPART_KEY_COLUMN</th>";
		strHtml += "<th colspan='3'>: "+ nvl(post.subpart_key_column,"") +"</th></tr>";
		strHtml += "<tr><th>NUM_ROWS</th>";
		strHtml += "<th>: "+ formatComma(post.num_rows) +"</th>";
		strHtml += "<th>SAMPLE_SIZE</th>";
		strHtml += "<th>: "+ formatComma(post.sample_size) +"</th></tr>";
		strHtml += "<tr><th>BLOCKS</th>";
		strHtml += "<th>: "+ formatComma(post.blocks) +"</th>";
		strHtml += "<th>EMPTY_BLOCKS</th>";
		strHtml += "<th>: "+ formatComma(post.empty_blocks) +"</th></tr>";
		strHtml += "<tr><th>AVG_SPACE</th>";
		strHtml += "<th>: "+ formatComma(post.avg_space) +"</th>";
		strHtml += "<th>CHAIN_CNT</th>";
		strHtml += "<th>: "+ formatComma(post.chain_cnt) +"</th></tr>";
		strHtml += "<tr><th>AVG_ROW_LEN</th>";
		strHtml += "<th>: "+ formatComma(post.avg_row_len) +"</th>";
		strHtml += "<th>AVG_SPACE_FREELIST_BLOCKS</th>";
		strHtml += "<th>: "+ formatComma(post.avg_space_freelist_blocks) +"</th></tr>";
		strHtml += "<tr><th>DEGREE</th>";
		strHtml += "<th>: "+ formatComma(post.degree) +"</th>";
		strHtml += "<th>LAST_ANALYZED</th>";
		strHtml += "<th>: "+ post.last_analyzed +"</th></tr>";
		
		$("#columnInfoTbl tbody").append(strHtml);
		
		var data1 = JSON.parse(result.stringList[0]);
		$('#columnInfoList').datagrid("loadData", data1);
		$("#columnInfoList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var data2 = JSON.parse(result.stringList[1]);
		$('#indexInfoList').datagrid("loadData", data2);
		$("#indexInfoList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}else{
		$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};

var callback_ExplanPlanDetailPopup2Action = function(result) {
	if(result.result){
		var data1 = JSON.parse(result.stringList[0]);
		$('#accessPathList').datagrid("loadData", data1);
		$("#accessPathList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var data2 = JSON.parse(result.stringList[1]);
		$('#columnHistoryList').datagrid("loadData", data2);
		$("#columnHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var data3 = JSON.parse(result.stringList[2]);
		$('#indexHistoryList').datagrid("loadData", data3);
		$("#indexHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}else{
		parent.$.messager.alert('',data.message);
		$('#treePlan').tree("loadData", data);
	}
}

//callback 함수
var callback_ExplanPlanStatisticsPopupAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#statisticsList').datagrid("loadData", data);
		$("#statisticsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}
};


function Btn_ShowIndexGuide(){
	$('#indexGuidePop').window("open");
}

function WinOpen_ShowIndexGuide(){
	window.open('resources/document/indexGuidePop.html','indexGuidePop','top=180, left=190, width=1200,height=600,scrollbars=yes')
}

function Btn_ShowTuningCheckGuide(){
	$('#tuningCheckPop').window("open");
}

function WinOpen_ShowTuningCheckGuide(){
	window.open('resources/document/tuningCheckPop.html','tuningCheckPop','top=180, left=190, width=1200,height=600,scrollbars=yes')
}

function Btn_GoSQLAdvisor(strGb){
	var menuId = $("#menu_id").val() + strGb;
	var menuNm = "";
	var strUrl = "";
	var menuParam = "";
	
	if($('#dbid').val() == ""){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}

	rows = $('#testResultList').datagrid('getChecked');
	
	if(rows.length < 1){
		parent.$.messager.alert('','SQL Statistics의 row를 선택해 주세요.');
		return false;
	}

	menuParam = "dbid="+$('#dbid').val()+"&sql_id="+rows[0].sql_id+"&call_from_parent=Y";
	
	if(strGb == "1"){
		menuNm = "SQL Tuning Advisor";
		strUrl = "/SQLPerformance/SQLTuningAdvisor";
	}else if(strGb == "2"){
		menuNm = "SQL Access Advisor";
		strUrl = "/SQLPerformance/SQLAccessAdvisor";
	}
	
	parent.openLink("Y", menuId, menuNm, strUrl, menuParam);
}