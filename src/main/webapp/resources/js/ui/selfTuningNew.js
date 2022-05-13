var perfCheckDbid = '';

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 업무구분 조회
	$('#ui_wrkjob_cd').combobox({
		url:"/Common/getWrkJob",
		method:"get",
		valueField:'wrkjob_cd',
		textField:'wrkjob_cd_nm',
		onLoadError: function(){
			parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items){
			if ( $("#wrkjob_cd").val() != '' ) {
				$(this).combobox('setValue', $("#wrkjob_cd").val() );
			}
			
			$('#ui_wrkjob_cd').combobox('textbox').attr( 'placeholder' , '선택' );
		},
		onSelect:function(rec){
			$("#wrkjob_cd").val(rec.wrkjob_cd);
			$("#dbid").val(rec.dbid);
			
			getDatabaseOfWrkjobCd(rec.wrkjob_cd);
		}
	});
	
	var dbid = $("#dbid").val();
	if ( dbid != "" ){
		getParsingSchemaName();
	}
	
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
	
	$("#selfTuningTab").tabs({
		plain: true,
		onSelect: function(title,index){
			var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
			var tabIndex = index + 1;
			var tabClickCount = $("#submit_form #tab"+tabIndex+"ClickCount").val();
			
			$("#submit_form #tab"+tabIndex+"ClickCount").val(1);
			
			if ( index == 3 ) {
				if ( Number($('#show_flag').val()) == 0 ) {
					resizeTabbed_default_treePlan();
					
				} else {
					resizeTabbed_expand_treePlan();
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
			
			if ( node.attributes.partition_start != "" ||
					node.attributes.distribution != "" ||
					node.attributes.access_predicates != "" ||
					node.attributes.filter_predicates != "" ){
				
				if ( node.attributes.table_owner == "" && node.attributes.table_name == "" ){
					str += "<a href='javascript:;' class='help easyui-tooltip' ";
					
				}else {
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
				
				if ( text.indexOf("FULL") > -1 || text.indexOf("CARTESIAN") > -1 ){
					text = "<span style='color:red;font-weight:bold'>" + strReplace(strReplace(text,"'","`"),"\"","&quot;") + "</span>";
				}
				str += strReplace(strReplace(text,"'","`"),"\"","&quot;") + "</a>";
				
			}else{
				if ( node.attributes.table_owner == "" && node.attributes.table_name == "" ){
					str += strReplace(strReplace(text,"'","`"),"\"","&quot;");
					
				}else {
					str += "<a href='javascript:showExplainPlanPopup(\""+node.attributes.table_owner+"\",\""+node.attributes.table_name+"\")'>";
					str += strReplace(strReplace(text,"'","`"),"\"","&quot;");
					str += "</a>";
				}
			}
			return str;
		}
	});
	
	$('#selectKey').combobox('setValue',$("#searchKey").val());
	
	if ( $("#wrkjob_cd").val() != "" ){
		$('#ui_wrkjob_cd').combobox('setValue',$("#wrkjob_cd").val());
	}
	
	$("#tr_perf_check_result_basis_why1").hide();
	$("#tr_perf_check_result_basis_why2").hide();
	
	$('#dbioList').datagrid("loadData", []);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("셀프튜닝","SQL식별자(DBIO) 목록을 조회중입니다.");
	
	ajaxCallSelfTuningAction();
	
	setBindValue();
});

// 업무별 Database 조회
function getDatabaseOfWrkjobCd(wrkjob_cd) {
	$('#selectDbOfWrkjobCd').combobox({
		url:"/SelfTuning/getDatabaseOfWrkjobCd?wrkjob_cd="+wrkjob_cd,
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(items) {
			var dbid = $("#dbid").val();
			var isExist = false;
			var defaultIndex = -1;
			if ( items.length ){
				for( let i=0; i < items.length; i++ ){
					if ( dbid == items[i].dbid ){
						isExist = true;
					}
					
					if ( items[i].default_yn == 'Y' ) {
						defaultIndex = i;
					}
				}
				
				if ( isExist ){
					$('#selectDbOfWrkjobCd').combobox("setValue",dbid);
					
				}else {
					var opts = $(this).combobox('options');
					
					if ( defaultIndex > -1 ) {
						$(this).combobox('select', items[defaultIndex][opts.valueField]);
						
					} else {
						$(this).combobox('select', items[0][opts.valueField]);
					}
				}
			}
			$('#selectDbOfWrkjobCd').combobox('textbox').attr( 'placeholder' , '선택' );
		},
		onSelect:function(rec){
			$("#dbid").val(rec.dbid);
			
			$("#parsing_schema_name").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$('#parsing_schema_name').combobox('textbox').attr( 'placeholder' , '선택' );
				},
				onLoadError: function(){
					parent.$.messager.alert('','파싱스키마 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			$('#selectParsingSchemaName').combobox("setValue",$("#parsing_schema_name").val());
		}
	});
}

function getParsingSchemaName(){
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

function ajaxCallSelfTuningAction(){
	ajaxCall("/SelfTuningAction",
			$("#submit_form"),
			"POST",
			callback_SelfTuningAddTable);
}

//callback 함수
var callback_SelfTuningAddTable = function(result) {
	json_string_callback_common(result,'#dbioList',true);
};

//성능점검수행
function ajaxCallSelfTest(){
	//이전 데이터 초기화
	$("#detailCheckResultTable tbody").html("");
	$("#ta_perf_check_result_basis_why1").val("");
	$("#ta_perf_check_result_basis_why2").val("");
	$("#perf_impr_guide_table tbody").html("");
	$("#ta_exec_plan").val("");
	$('#tableList').datagrid("loadData", []);
	
	var data = $("#submit_form #sql_text").val();
	var sqlText = $("#submit_form #sql_text").val();
	
	sqlTextRegularExpression(data,sqlText);
	
	if(parent.parent.openMessageProgress != undefined) parent.parent.openMessageProgress("성능점검을 수행중입니다"," ");
	
	ajaxCall("/SelfTuning/SelfTestNew",
			$("#submit_form"),
			"POST",
			callback_SelfTestAction);
}

function ajaxCallPerfCheckResultList(){
	/* modal progress open */
	ajaxCall("/SelfTuning/PerfCheckResultList",
			$("#submit_form"),
			"POST",
			callback_PerfCheckResultList);
}

// sql에 '[]'일 경우 <![CDATA[[]]]>로 치환
function sqlTextRegularExpression(data,sqlText){
	
	data = data.replace(/\[/g , "<![CDATA[[");
	data = data.replace(/\]/g , "]]]>");
	
	// defaultText에 치환된 sql_text를 저장.
	$("#submit_form #sql_text").val(sqlText);
	$("#submit_form #defaultText").val(data);
}

//callback 함수
var callback_PerfCheckResultList = function(result) {
	try{
		var data = JSON.parse(result);
		if ( data.result != undefined && !data.result ){
			if ( data.message == 'null' || data.message == '' ){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
				
			}else {
				parent.$.messager.alert('',data.message);
			}
			
		}else {
			row = data.rows;
			var dbid = row[0].dbid;
			var selftun_query_seq = row[0].selftun_query_seq;
			var perf_check_result_div_nm = row[0].perf_check_result_div_nm;
			var sql_id = row[0].sql_id;
			
			selectRowPerfCheckResult(dbid, selftun_query_seq, perf_check_result_div_nm, sql_id);
		}
		
	}catch(e){
		if ( e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1 ){
			$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
				setTimeout(function() {
					top.location.href="/auth/login";
				},1000);
			});
			
		}else {
			parent.$.messager.alert('',e.message);
		}
	}
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function selectRowPerfCheckResult(dbid, selftun_query_seq, perf_check_result_div_nm, sql_id){
	$("#submit_form #dbid").val(dbid);
	$("#submit_form #selftun_query_seq").val(selftun_query_seq);
	$("#submit_form #sql_id").val(sql_id);
	
	ajaxCall("/SelfTuning/selectDeployPerfChkDetailResultList",
			$("#submit_form"),
			"POST",
			callback_DeployPerfChkDetailResultList);
	
	ajaxCall("/SelfTuning/selectPerfCheckResultBasisWhy",
			$("#submit_form"),
			"POST",
			callback_PerfCheckResultBasisWhy);
	
	if ( perf_check_result_div_nm == "오류" ){
		$("#tr_perf_check_result_table").hide();
		$("#tr_perf_impr_guide").hide();
		$("#tr_perf_check_result_basis_why1").show();
		$("#tr_perf_check_result_basis_why2").show();
		$("#tr_exec_plan").hide();
		
		$('#perfCheckDbid').val('');
		
	}else if ( perf_check_result_div_nm == "적합" ){
		$("#tr_perf_check_result_table").show();
		$("#tr_perf_impr_guide").hide();
		$("#tr_perf_check_result_basis_why1").hide();
		$("#tr_perf_check_result_basis_why2").hide();
		$("#tr_exec_plan").show();
		
		perfCheckDbid = dbid;
		
	}else {
		$("#tr_perf_check_result_table").show();
		$("#tr_perf_impr_guide").show();
		$("#tr_perf_check_result_basis_why1").hide();
		$("#tr_perf_check_result_basis_why2").hide();
		$("#tr_exec_plan").show();
		
		perfCheckDbid = dbid;
	}
}

function cellStyler(value,row,index){
	var elapsed_time = row.elapsed_time;
	var buffer_gets = row.buffer_gets;
	if ( elapsed_time > 3 || buffer_gets > 1000 ){
		return 'background-color:#f97b7b;color:#ffffff;font-weight:700;';
	}
}

function Btn_OnClick(){
	if ( $('#wrkjob_cd').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if ( $("#dbio_search_yn").val() == "Y" ){
		if ( ($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
				($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "") ){
			
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

function Btn_RequestTuning(){
	if ( $('#wrkjob_cd').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if ( $("#parsing_schema_name").combobox("getValue") == "" ){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if ( $("#sql_text").val() == "" ){
		parent.$.messager.alert('','튜닝요청 할 SQL TEXT를 입력해 주세요.');
		return false;
	}
	
	Btn_RequestTuningStep2();
}

function Btn_RequestTuningStep2(){
	var menuId = $("#menu_id").val() + "1";
	var menuNm = "SQL 성능 셀프 점검 :: 튜닝 요청";
	$("#menu_nm").val(menuNm);
	var menuUrl = "/SelfTuning/RequestTuning";
	
	//※ SQL BIND 값 변환
	var sqlDesc = createSqlDesc();
	$("#submit_form #sql_desc").val(sqlDesc);
	$("#submit_form #wrkjob_cd").val( $('#ui_wrkjob_cd').combobox('getValue') );
	
	var formId = "#submit_form";
	
	openLinkByPost("Y", menuId, menuNm, menuUrl, formId);
}

function createSqlDesc(){
	var bind_var_nm_obj = $("input[name='bind_var_nm']");
	var bind_var_value_obj = $("input[name='bind_var_value']");
	var sqlDesc = "";
	
	for( let i=0; i < bind_var_nm_obj.length; i++ ){
		sqlDesc += $(bind_var_nm_obj[i]).val()+" "+ $(bind_var_value_obj[i]).val()+"\n";
	}
	
	return sqlDesc;
}
/**
 * 성능점검수행 버튼 클릭시
 * @returns
 */
function Btn_SelfTuningTest(){
	if ( $('#wrkjob_cd').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if ( $("#parsing_schema_name").combobox("getValue") == "" ){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if ( $("#sql_text").val() == "" ){
		parent.$.messager.alert('','셀프테스트 진행 할 SQL TEXT를 입력해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();
	
	$('#selfTuningTab').tabs('select', 0);
	
	ajaxCallSelfTest();
}
//성능점검수행, 성능 점검 수행 결과 callback
var callback_SelfTestAction = function(result) {
	parent.$.messager.progress('close');
	
	if ( result.result ){
		//성능점검결과 목록 가져오기
		ajaxCallPerfCheckResultList();
		
	}else {
		var msg = result.message;
		if ( result.message == undefined ){
			msg = "성능 점검 수행중 오류가 발생하였습니다. <br>다시 로그인하여 실행해 주세요.<br>지속적으로 문제가 발생할 경우 관리자에게 문의바랍니다.";
		}
		parent.$.messager.alert('ERROR',msg,'error', function(){});
	}
};

function Btn_TableShowYn(strName, showYn){
	try {
		if ( strName == "dbio" ){
			if ( showYn == "Y" ){
				$('#dbioDiv').panel('expand',true);
				
			}else {
				$('#dbioDiv').panel('collapse',true);
			}
			
		}else{
			if ( showYn == "Y" ){
				$("#testTbl").show();
				$("#testDiv").show();
				
				resizeTabbed_default();
				
				$('#show_flag').val(0);
				
			}else {
				$("#testTbl").hide();
				$("#testDiv").hide();
				
				resizeTabbed_expand();
				
				$('#show_flag').val(1);
			}
		}
	} catch(error) {
		console.err(error);
	}
}

function resizeTabbed_default() {
	$("#selfTuningTab").css('height', '310px');
	$("#selfTuningTab .tabs-panels").css('height', '285px');
	$("#selfTuningTab .panel-body-noheader").css('height', '280px');
	$('#tableList_div').css('height', '270px');
	$('#tableList').datagrid('resize', {
		height: 270
	});
	
	$('#ta_exec_plan').css('height', '250px');
	
	$('#treePlan_div').css('height', '240px');
	$('#treePlan_div2').css('height', '240px');
	$('#treePlan').css('height', '240px');
}

function resizeTabbed_expand() {
	$("#selfTuningTab").css('height', '620px');
	$("#selfTuningTab .tabs-panels").css('height', '590px');
	$("#selfTuningTab .panel-body-noheader").css('height', '580px');
	$('#tableList_div').css('height', '572px');
	$('#tableList').datagrid('resize', {
		height: 572
	});
	
	$('#ta_exec_plan').css('height', '558px');
	
	$('#treePlan_div').css('height', '555px');
	$('#treePlan_div2').css('height', '555px');
	$('#treePlan').css('height', '555px');
}

function resizeTabbed_default_treePlan() {
	$('#treePlan_div').css('height', '240px');
	$('#treePlan_div2').css('height', '240px');
	$('#treePlan').css('height', '240px');
}

function resizeTabbed_expand_treePlan() {
	$('#treePlan_div').css('height', '568px');
	$('#treePlan_div2').css('height', '568px');
	$('#treePlan').css('height', '568px');
}

var bind_var_nm_value_arr = [];
var bind_var_nm_value_arr_length = 0;
var bind_var_value_one = "";

function setBindValue(){
	var resultArr = [];
	
	if ( bind_var_nm_value_arr != undefined ){
		
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
		if ( bind_var_nm_value_arr_length > 0 ){
			
			for( let i=0; i < bind_var_nm_value_arr_length; i++ ){
				var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
				var bind_var_value = $("#bind_var_value"+(i+1)).val();
				
				if ( bind_var_nm != undefined ){
					bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
				}
			}
		}
		bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
	}
	
	var bindArry = pullOutBindArray($("#sql_text").val());
	if ( bindArry != null ){
		$.each(bindArry, function(key, value){
			if ( $.inArray(value, resultArr) === -1 ){
				let regularExp = /\,|\-|\)|\;|\|\|/g;		/* 주석 기호 "--"가 홀수일 때("---") 정규식에서 차지 못하는 문제점 발견 */ 
				value = value.replace(regularExp,'').trim();
				
				var n = false;
				if ( resultArr.indexOf(value) !== -1 ){
					n = true;
				}
				if ( !n ){
					if ( value.lastIndexOf(":") != 0 ) {
						var bindSplit = value.split(":");
						for ( let i = 1; i < bindSplit.length; i++ ) {
							resultArr.push(":"+bindSplit[i] );
						}
						
					} else if ( value.indexOf(':MI:SS') < 0 ) {
						resultArr.push(value);
					}
				}
			}
		});
	}
	
	$("#bindTbl tbody tr").remove();
	var strHtml = "";
	if ( bindArry != null ){
		for( let i = 0 ; i < resultArr.length ; i++ ){
			var bind_var_nm = strExpReplace2(strReplace(resultArr[i],' ',''));
			strHtml += "<tr><td class='ctext txtCenter'>";
			strHtml += "<input type='hidden' id='bind_set_seq"+(i+1)+"' name='bind_set_seq' value='1'/>";
			strHtml += "<input type='hidden' id='mandatory_yn"+(i+1)+"' name='mandatory_yn' value='Y'>";
			strHtml += "<input type='text' id='bind_seq"+(i+1)+"' name='bind_seq' value='"+(i+1)+"' data-options='readonly:true' class='w50 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='bind_var_nm"+(i+1)+"' name='bind_var_nm' value='"+strExpReplace2(strReplace(resultArr[i],' ',''))+"' class='w130 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='w130 easyui-combobox'><option value='string' selected>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";
			
			if ( bind_var_nm_value_arr != undefined ){
				bind_var_nm_value_arr_length = Object.keys(bind_var_nm_value_arr).length;
				
				if ( bind_var_nm_value_arr_length > 0 ){
					for( let j=0;j<bind_var_nm_value_arr_length;j++ ){
						bind_var_value_one = bind_var_nm_value_arr[bind_var_nm];
						if ( bind_var_value_one == undefined ) bind_var_value_one = "";
					}
				}
				
			}else {
				bind_var_nm_value_arr = "";
			}
			
			if ( bind_var_value_one == undefined ) bind_var_value_one = "";
			
			strHtml += "<td class='ctext'><input type='text' id='bind_var_value"+(i+1)+"' name='bind_var_value' value='"+bind_var_value_one+"' class='w130 easyui-textbox'/></td></tr>";
		}
		
		$("#bindTbl tbody").append(strHtml);
		
		for( let i = 0 ; i < bindArry.length ; i++ ){
			$("#bindTbl #bind_seq"+(i+1)).textbox({readonly:true});	
			$("#bindTbl #bind_var_nm"+(i+1)).textbox();
			$("#bindTbl #bind_var_type"+(i+1)).combobox();
			$("#bindTbl #bind_var_value"+(i+1)).textbox();
		}
	}
	
	if ( resultArr.length > 0 ){
		bind_var_nm_value_arr = [];
		
		for( let i=0; i < resultArr.length; i++ ){
			var bind_var_nm = $("#bind_var_nm"+(i+1)).val();
			var bind_var_value = $("#bind_var_value"+(i+1)).val();
			bind_var_nm_value_arr[bind_var_nm] = bind_var_value;
		}
	}
}

function Btn_ReqExplainPlan(){
	if ( $('#wrkjob_cd').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if ( $("#parsing_schema_name").combobox("getValue") == "" ){
		parent.$.messager.alert('','Parsing Schema Name를 선택해 주세요.');
		return false;
	}
	
	if ( $("#sql_text").val() == "" ){
		parent.$.messager.alert('','Explain Plan 진행 할 SQL TEXT를 입력해 주세요.');
		return false;
	}
	
	$('#selfTuningTab').tabs('select', 3);
	
	ajaxCall("/SelfTuning/ExplainPlanTree",
			$("#submit_form"),
			"POST",
			callback_ExplainPlanTreeAction);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("","Explain Plan을 진행중입니다.");
}

//callback 함수
var callback_ExplainPlanTreeAction = function(result) {
	parent.$.messager.progress('close');
	
	$('#treePlan').tree("loadData", []);
	
	if ( result.result ){
		var data = JSON.parse(result.txtValue);
		$('#treePlan').tree("loadData", data);
		
		$('.help').tooltip({
			position: 'right'
		});
		
	}else {
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
	if ( result.result ){
		var selfSql = result.list[0];
		var lines = selfSql.sql_text.split("\n");
		var newText = "";
		
		for ( let i = 1; i < lines.length; i++ ) {
			newText += lines[i] + "\n";
		}
		
		$("#sql_text_pop").val(newText);
		$("#bindCnt").val(result.list[1].length);
		
		for( let i = 0 ; i < result.list[1].length ; i++ ){
			var post = result.list[1][i];
			
			strHtml += "<tr><td class='ctext'>";
			strHtml += "<input type='hidden' id='bind_p_seq"+(i+1)+"' name='bind_p_seq' value='"+(i+1)+"'/>"+ post.bind_seq +"</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_nm"+(i+1)+"' name='bind_p_var_nm' value='"+post.bind_var_nm+"'/>"+post.bind_var_nm+"</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_type"+(i+1)+"' name='bind_p_var_type' value'"+post.bind_var_type+"'/>";
			
			if ( post.bind_var_type == "string" ){
				strHtml += "String타입";
				
			}else if ( post.bind_var_type == "number" ){
				strHtml += "Number타입";
				
			}else if ( post.bind_var_type == "date" ){
				strHtml += "Date타입";
				
			}else if ( post.bind_var_type == "char" ){
				strHtml += "Char타입";
			}
			strHtml += "</td>";
			strHtml += "<td class='ctext'><input type='hidden' id='bind_p_var_value"+(i+1)+"' name='bind_p_var_value' value='"+post.bind_var_value+"'/>"+post.bind_var_value+"</td></tr>";
		}
		
		$("#bindPopTbl tbody").append(strHtml);
		
	}else {
		parent.$.messager.alert('',result.message,'error', function(){
			Btn_OnClosePopup('selftunSqlPop');
		});
	}
};

/* 인덱스 자동설계 팝업 */
function showIndexAutoDesign(){
	if ( $('#wrkjob_cd').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	if ( $("#dbio_search_yn").val() == "Y" ){
		if ( ($('#selectKey').combobox('getValue') != "" && $('#searchValue').textbox('getValue') == "") ||
				($('#selectKey').combobox('getValue') == "" && $('#searchValue').textbox('getValue') != "") ){
			parent.$.messager.alert('','조회 구분값을 입력해주세요.');
			return false;
		}
		$("#searchKey").val($('#selectKey').combobox('getValue'));
	}
	
	if ( $('#sql_text').val() == "" ){
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
	
	$("#explainPlanPop .tableTitle").html(tableOwner + "." + tableName);
	
	$("#explain_form #dbid").val($("#submit_form #dbid").val());
	$("#explain_form #owner").val(tableOwner);
	$("#explain_form #table_name").val(tableName);
	
	$('#explainPlanPop').window("open");
	$('#explainPlanPop').window('move',{
		top:getWindowTop(1350),
		left:getWindowLeft(1000)
	});
	$("#columnInfoTbl tbody tr").remove();
	
	$('#columnInfoList').datagrid('loadData',[]);
	$('#indexInfoList').datagrid('loadData',[]);
	$('#statisticsList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("","테이블 정보를 조회중입니다.");
	
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
	
	if ( result.result ){
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
		
	}else {
		$.messager.alert('',result.message,'error');
	}
	
	parent.$.messager.progress('close');
};

var callback_ExplanPlanDetailPopup2Action = function(result) {
	if ( result.result ){
		var data1 = JSON.parse(result.stringList[0]);
		$('#accessPathList').datagrid("loadData", data1);
		$("#accessPathList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var data2 = JSON.parse(result.stringList[1]);
		$('#columnHistoryList').datagrid("loadData", data2);
		$("#columnHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
		var data3 = JSON.parse(result.stringList[2]);
		$('#indexHistoryList').datagrid("loadData", data3);
		$("#indexHistoryList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
	}else {
		parent.$.messager.alert('',data.message);
		$('#treePlan').tree("loadData", data);
	}
}

//callback 함수
var callback_ExplanPlanStatisticsPopupAction = function(result) {
	var data = JSON.parse(result);
	
	if ( data.result != undefined && !data.result ){
		if ( data.message == 'null' ){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			
		}else {
			parent.$.messager.alert('',data.message);
		}
	}else {
		$('#statisticsList').datagrid("loadData", data);
		$("#statisticsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	}
};


function Btn_ShowIndexGuide(){
	$('#indexGuidePop').window("open");
	$('#indexGuidePop').window('move',{
		top: 0,
		left:getWindowLeft(1200)
	});
}

function WinOpen_ShowIndexGuide(){
	window.open('resources/document/indexGuidePop.html',
				'indexGuidePop',
				'top=180, left=190, width=1200,height=600,scrollbars=yes')
}

function Btn_ShowTuningCheckGuide(){
	$('#tuningCheckPop').window("open");
	$('#tuningCheckPop').window('move',{
		top: 0,
		left:getWindowLeft(1200)
	});
}

function WinOpen_ShowTuningCheckGuide(){
	window.open('resources/document/tuningCheckPop.html',
				'tuningCheckPop',
				'top=180, left=190, width=1200,height=600,scrollbars=yes')
}

function Btn_GoSQLAdvisor(strGb){
	var menuId = $("#menu_id").val() + strGb;
	var menuNm = "";
	var strUrl = "";
	var menuParam = "";
	
	if ( $('#dbid').val() == "" ){
		parent.$.messager.alert('','업무구분을 선택해 주세요.');
		return false;
	}
	
	
	if ( $("#submit_form #sql_id").val() == null || $("#submit_form #sql_id").val() == '' ) {
		parent.warningMessager("성능 점검 수행을 진행되지 못하였거나, 성능 점검대상 SQL 오류로 진행할 수 없습니다.");
		return false;
	}
	
	if ( perfCheckDbid != $('#selectDbOfWrkjobCd').combobox('getValue') ) {
		parent.warningMessager("이전 성능 점검 수행 DB와 현재 선택한 DB가 다릅니다. 선택한 DB에서 먼저 성능 점검을 수행한 후 진행해 주세요.");
		return false;
	}
	
	menuParam = "dbid="+$('#dbid').val()+"&sql_id="+$("#submit_form #sql_id").val()+"&call_from_parent=Y";
	
	if ( strGb == "1" ){
		menuNm = "SQL Tuning Advisor";
		strUrl = "/SQLPerformance/SQLTuningAdvisor";
		
	}else if ( strGb == "2" ){
		menuNm = "SQL Access Advisor";
		strUrl = "/SQLPerformance/SQLAccessAdvisor";
	}
	
	parent.openLink("Y", menuId, menuNm, strUrl, menuParam);
}

//callback 함수
//상세 점검 결과
var callback_DeployPerfChkDetailResultList = function(result) {
	try{
		var data = JSON.parse(result);
		if ( data.result != undefined && !data.result ){
			if ( data.message == 'null' ){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
				
			}else {
				parent.$.messager.alert('',data.message);
			}
			
		}else {
			var html = "";
			jQuery.each(data.rows, function(index, row){
				var perf_check_meth_cd = row.perf_check_meth_cd;
				var perf_check_result_div_cd = row.perf_check_result_div_cd;
				var perf_check_indc_nm = row.perf_check_indc_nm;
				
				html += "<tr>\n";
				html += "	<td style='cursor:help;display:flex;'><input type='text' id='perf_check_id_" + index + "' size='15' class='font11px width100per border0px' value='"+ perf_check_indc_nm +"' readonly style='cursor:help;'>" +
						"		<span>" +
						"			<i id='perf_check_id_" + index + "_tooltip' class='fas fa_question_circle easyui-tooltip' title=''></i>" +
						"		</span>" +
						"\n</td>\n";
				
				var backColor = "";
				if ( row.perf_check_result_div_nm == '적합' ){
					backColor = 'style="background-color:#1A9F55;color:white;"';
				}else if ( row.perf_check_result_div_nm == '부적합' ){
					backColor = 'style="background-color:#E41E2C;color:white;"';
				}else if ( row.perf_check_result_div_nm == '오류' ){
					backColor = 'style="background-color:#ED8C33;color:white;"';
				}else if ( row.perf_check_result_div_nm == '미수행' ){
					backColor = 'style="background-color:#7F7F7F;color:white;"';
				}else if ( row.perf_check_result_div_nm == '수행중' ){
					return 'background-color:#89BD4C;color:white;';
				}else if ( row.perf_check_result_div_nm == '점검제외' ){
					backColor = 'style="background-color:#012753;color:white;"';
				}
				html += "	<td "+backColor+"><input type='text' size='15' class='tac font11px width100per border0px' "+backColor+" value='"+row.perf_check_result_div_nm+"'readonly></td>\n";
				
				if ( perf_check_meth_cd == 1 ){
					html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.indc_pass_max_value+"' readonly></td>\n";
					html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.indc_pass_max_value+" 초과' readonly></td>\n";
					
				}else if ( perf_check_meth_cd == 2 ){
					var chk1 = "";
					var chk2 = "";
					if ( perf_check_result_div_cd == 'A' ){
						var chk1 = "checked='checked'";
						var chk2 = "";
						
					}else if ( perf_check_result_div_cd == 'B' ){
						var chk1 = "";
						var chk2 = "checked='checked'";
					}
					html += "	<td class='tac' colspan='2'>";
				}
				html += "	<td><input type='text' size='15' class='tac font11px width100per border0px' value='"+row.exec_result_value+"'readonly></td>\n";
				html += "</tr>\n";
			});
			$("#detailCheckResultTable tbody").html("");
			$("#detailCheckResultTable tbody").append(html);
			
			//개선가이드
			ajaxCall("/SelfTuning/selectImprovementGuideList",
					$("#submit_form"),
					"POST",
					callback_PerfChkResultImproveGuide);
			
			ajaxCall("/SelfTuning/executionPlan1",
					$("#submit_form"),
					"POST",
					callback_ExecutionPlan1);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

//사유1,사유2
//callback 함수
var callback_PerfCheckResultBasisWhy = function(result) {
	var data;
	
	try{
		data = JSON.parse(result);
		var rows = data.rows;
		var row = data.rows[0];
		
		if ( row != undefined ){
			$("#ta_perf_check_result_basis_why1").val(row.perf_check_result_basis_why1);
			$("#ta_perf_check_result_basis_why2").val(row.perf_check_result_basis_why2);
			$("#ta_exec_plan").val(row.exec_plan)
		}
		
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

//개선가이드
//callback 함수
var callback_PerfChkResultImproveGuide = function(result) {
	var data;
	
	try{
		data = JSON.parse(result);
		var rows = data.rows;
		
		jQuery.each(data.rows, function(index, row){
			var perf_check_indc_nm = nvl(row.perf_check_indc_nm,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			var perf_check_indc_desc = nvl(row.perf_check_indc_desc,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			var perf_check_fail_guide_sbst = nvl(row.perf_check_fail_guide_sbst,"").replace(/(?:\r\n|\r|\n)/g, '<br>');
			
			$('#perf_check_id_' + index).tooltip({
				content: $('<div class="dg"></div>'),
				showEvent: 'mouseenter focusin',
				position: 'right',
				onUpdate: function(content){
					content.datagrid({
						width: 800,
						columns:[[
							{field:'content1', title:'점검지표'},
							{field:'content2', title:'지표설명'},
							{field:'content3', title:'개선가이드'}
						]],
						data: [
							{content1: perf_check_indc_nm, content2: perf_check_indc_desc, content3: perf_check_fail_guide_sbst}
						],
						onLoadSuccess: function(data){
						}
					})
				},
				onShow: function(){
					var t = $(this);
					var dg = t.tooltip('tip').find('.dg');
					dg.datagrid('resize')
					t.tooltip('tip').unbind().bind('mouseenter', function(){
						t.tooltip('show');
					}).bind('mouseleave', function(){
						t.tooltip('hide');
					});
				}
			});
			
			$('#perf_check_id_' + index + '_tooltip').tooltip({
				content: $('<div class="dg"></div>'),
				showEvent: 'mouseenter focusin',
				position: 'right',
				onUpdate: function(content){
					content.datagrid({
						width: 800,
						columns:[[
							{field:'content1', title:'점검지표'},
							{field:'content2', title:'지표설명'},
							{field:'content3', title:'개선가이드'}
						]],
						data: [
							{content1: perf_check_indc_nm, content2: perf_check_indc_desc, content3: perf_check_fail_guide_sbst}
						],
						onLoadSuccess: function(data){
						}
					})
				},
				onShow: function(){
					var t = $(this);
					var dg = t.tooltip('tip').find('.dg');
					dg.datagrid('resize')
					t.tooltip('tip').unbind().bind('mouseenter', function(){
						t.tooltip('show');
					}).bind('mouseleave', function(){
						t.tooltip('hide');
					});
				}
			});
		});
		
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

function createExecutionPlanList() {
	$("#tableList").datagrid({
		view: myview,
		singleSelect: true,
		striped: true,
		columns:[[
			{field:'a_time_activity',title:'수행시간(%)',halign:"center",align:'right',styler:cellStyler1},
			{field:'buffers_activity',title:'블럭수(%)',halign:"center",align:'right',styler:cellStyler2},
			{field:'selftun_id',title:'ID',halign:"center",align:'right'},
			{field:'operation',title:'Operation',halign:"center",align:'left',formatter:toNbspFromWithespace},
			{field:'name',title:'Name',halign:"center",align:'left'},
			{field:'starts',title:'Starts',halign:"center",align:'right'},
			{field:'a_rows',title:'A-Rows',halign:"center",align:'right'},
			{field:'a_time',title:'A-Time',halign:"center",align:'right'},
			{field:'buffers',title:'Buffers',halign:"center",align:'right'},
			{field:'reads',title:'Reads',halign:"center",align:'right'},
			{field:'memory',title:'Memory(Max)',halign:"center",align:'right',formatter:toBlankFromZeroFormat},
			{field:'temp',title:'Temp(Max)',halign:"center",align:'right',formatter:toBlankFromZeroFormat},
			{field:'table_owner',hidden:true},
			{field:'table_name',hidden:true},
			{field:'rank_elapsed_time',hidden:true},
			{field:'rank_buffer_gets',hidden:true},
			{field:'my_elapsed_time',hidden:true},
			{field:'my_buffer_gets',hidden:true},
			{field:'childs_elapsed_time',hidden:true},
			{field:'childs_buffer_gets',hidden:true}
		]],
		onSelect:function(index,row) {
			if ( row.table_owner != null && row.table_owner != '' &&
					row.table_name != null && row.table_name != '' ) {
				showExplainPlanPopup(row.table_owner, row.table_name);
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

var callback_ExecutionPlan1 = function(result) {
	$('#tableList').datagrid("loadData", []);
	
	createExecutionPlanList();
	
	json_string_callback_common(result,'#tableList',true);
	
	/*수행시간 순위 1인 경우 강조*/
	$('[field=rank_elapsed_time]').each(function(){
		let rank = $(this).find('div').text(),
			target = $(this).parent('tr').find('[field=a_time_activity]'),
			value = 100 - target.text();
		
		if ( rank == 1 ) {
			target.css('background','linear-gradient(to right, rgb(255, 255, 255)'+
						value +'%, ' +'rgb(230, 80, 80) 0%, rgb(255, 255, 255))').css('font-weight', 'bold');
		}
	});
	
	/*블럭수 순위 1인 경우 강조*/
	$('[field=rank_buffer_gets]').each(function(){
		let rank = $(this).find('div').text(),
			target = $(this).parent('tr').find('[field=buffers_activity]'),
			value = 100 - target.text();
		
		if ( rank == 1 ) {
			target.css('background','linear-gradient(to right, rgb(255, 255, 255)'+
						value +'%, ' +'rgb(230, 80, 80) 0%, rgb(255, 255, 255))').css('font-weight', 'bold');
		}
	});
};

function cellStyler1(value,row,index){
	var color = '#ffe48d';
	
	if ( value >= 100 ){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);';
	}else if ( value < 100 ){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);';
	}
}

function cellStyler2(value,row,index){
	var color = '#efbd7f';
	
	if ( value >= 100 ){
		return 'background: linear-gradient(to right, #ffffff 0%, ' + color + ' 0%, white);';
	}else if ( value < 100 ){
		var colorVal = 100 - value;
		return 'background: linear-gradient(to right, #ffffff '+ colorVal +'%, ' + color + ' ' + colorVal+'%, white);';
	}
}

function Btn_SetSQLFormatter(){
	$('#sql_text').format({method: 'sql'});
}
