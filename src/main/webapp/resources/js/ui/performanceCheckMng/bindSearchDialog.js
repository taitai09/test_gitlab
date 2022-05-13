var totalCount = 0;

$(document).ready(function() {
	$('#bindSearchDialog').window({
		title : "바인드 검색",
		top:getWindowTop(700),
		left:getWindowLeft(900),
		width:'650px',
		height:'390px'
	});

	Btn_OnClosePopup('bindSearchDialog');
	
	/**바인드 검색 탭 1, 2 */
	$("#bindSearchTabs").tabs({
		plain: true,
		onSelect: function(title,index){
			$('#bindSearchList2Table tbody').html("");
			$('#bindSearchList4Table tbody').html("");
			if(index == 0){
				fnBindSearchList1();
			}else if(index == 1){
				fnBindSearchList3();
			}
		}		
	});
	
});
//바인드검색을 누르면 팝업창에서 실행됨.
function fnBindSearchList1(){
	$("#bindSearchTabs").tabs('select',0);
	$('#bindSearchList1').html("");

//	console.log("tab_submit_form:"+$("#tab_submit_form").serialize());
	ajaxCall("/PerfCheckResultList/selectDeployPerfChkExecBindListPop", $("#tab_submit_form"), "POST", callback_BindSearch1);		
}

function fnBindSearchList2(perf_check_id, perf_check_step_id, program_id, program_execute_tms){
	console.log("perf_check_id:"+perf_check_id);
	console.log("perf_check_step_id:"+perf_check_step_id);
	console.log("program_id:"+program_id);
	console.log("program_execute_tms:"+program_execute_tms);
	
	$("#tab_submit_form #pop_perf_check_id").val(perf_check_id);
	$("#tab_submit_form #pop_perf_check_step_id").val(perf_check_step_id);
	$("#tab_submit_form #pop_program_id").val(program_id);
	$("#tab_submit_form #pop_program_execute_tms").val(program_execute_tms);
//	console.log("tab_submit_form:"+$("#tab_submit_form").serialize());
	totalCount = 0;
//	$('#bindSearchList2').html("");
//	console.log($('#bindSearchList2Table tbody').html());
	$('#bindSearchList2Table tbody').html("");

	ajaxCall("/PerfCheckResultList/selectDeployPerfChkExecBindValue", $("#tab_submit_form"), "POST", callback_BindSearch2);		
}
function fnBindSearchList3(){
	$('#bindSearchList3').html("");

//	console.log("tab_submit_form:"+$("#tab_submit_form").serialize());
	ajaxCall("/PerfCheckResultList/selectDeployPerfChkAllPgmList", $("#tab_submit_form"), "POST", callback_BindSearch3);		
}

function fnBindSearchList4(index){
	console.log("bindSearchDialog fnBindSearchList4("+index+")");
	totalCount = 0;
//	$('#bindSearchList4').html("");
//	console.log($('#bindSearchList4Table tbody').html());
	$('#bindSearchList4Table tbody').html("");

	console.log("sql_id :"+$("#grid_sql_id"+index).val());
	console.log("snap_time :"+$("#grid_snap_time"+index).val());
	console.log("last_captured :"+$("#grid_last_captured"+index).val());
	
	$("#tab_submit_form #sql_id").val($("#grid_sql_id"+index).val());
	$("#tab_submit_form #snap_time").val($("#grid_snap_time"+index).val());
	$("#tab_submit_form #last_captured").val($("#grid_last_captured"+index).val());

	console.log("sql_id :"+$("#tab_submit_form #sql_id").val());
	console.log("snap_time :"+$("#tab_submit_form #snap_time").val());
	console.log("last_captured :"+$("#tab_submit_form #last_captured").val());
//	console.log("tab_submit_form:"+$("#tab_submit_form").serialize());
	ajaxCall("/PerfCheckResultList/selectVsqlBindCaptureList", $("#tab_submit_form"), "POST", callback_BindSearch4);		
}

function fnBindingByVarNm(){
	
	var pp = $('#bindSearchTabs').tabs('getSelected');
	var index = pp.panel('options').index;
	//console.log("index:"+index);

	var rows;
	var rowsLength = 0;
	if(index == 0){
		rows = $("#bindSearchList2 input[name='pop_bind_seq']");
	}else{
		rows = $("#bindSearchList4 input[name='pop_bind_seq']");
	}
	console.log("rows:",rows);
	rowsLength = rows.length;
	console.log("row.length:",rows.length);
	
	var bindTarget = $("#bindTbl input[name='bind_seq']");
	var bindTargetLength = bindTarget.length;
	console.log("bindTargetLength:",bindTargetLength);
	
	$("#bindSearchList2 input[name='pop_bind_seq']").each(function() {
		var aValue = $(this).val();
		console.log("aValue:",aValue);
	});
	
	if(index == 0){
		for(var i=0;i<rowsLength;i++){
			//순번
			var bind_seq = $('#bindSearchList2 input[name="pop_bind_seq"]').eq(i).val();
			//변수명
			var bind_var_nm = $('#bindSearchList2 input[name="pop_bind_var_nm"]').eq(i).val();
			//변수타입
			var bind_var_type = $('#bindSearchList2 input[name="pop_bind_var_type"]').eq(i).val();
			//변수값
			var bind_var_value = $('#bindSearchList2 input[name="pop_bind_var_value"]').eq(i).val();
			for(var j=0;j<bindTargetLength;j++){
				var bind_var_nm2 = $("#bindTbl #bind_var_nm"+(j+1)).val();
				console.log("bind_var_nm:"+bind_var_nm+" bind_var_nm2:"+bind_var_nm2);
				if(bind_var_nm == bind_var_nm2){
					$("#bindTbl #bind_var_type"+(j+1)).combobox("setValue",bind_var_type);
					$("#bindTbl #bind_var_value"+(j+1)).textbox("setValue",bind_var_value);
				}
			}
		}	
	}else{
		for(var i=0;i<rowsLength;i++){
			//순번
			var bind_seq = $('#bindSearchList4 input[name="pop_bind_seq"]').eq(i).val();
			//변수명
			var bind_var_nm = $('#bindSearchList4 input[name="pop_bind_var_nm"]').eq(i).val();
			//변수타입
			var bind_var_type = $('#bindSearchList4 input[name="pop_bind_var_type"]').eq(i).val();
			//변수값
			var bind_var_value = $('#bindSearchList4 input[name="pop_bind_var_value"]').eq(i).val();
			for(var j=0;j<bindTargetLength;j++){
				var bind_var_nm2 = $("#bindTbl #bind_var_nm"+(j+1)).textbox("getValue");
				console.log("bind_var_nm:"+bind_var_nm+" bind_var_nm2:"+bind_var_nm2);
				if(bind_var_nm == bind_var_nm2){
					$("#bindTbl #bind_var_type"+(j+1)).combobox("setValue",bind_var_type);
					$("#bindTbl #bind_var_value"+(j+1)).textbox("setValue",bind_var_value);
				}
			}
		}
	}
	$("#bindSearchList2Table tbody").html("");
	$("#bindSearchList4Table tbody").html("");
	Btn_OnClosePopup('bindSearchDialog');
}
//바인딩 버튼 클릭시
function fnBinding(){
	
	var pp = $('#bindSearchTabs').tabs('getSelected');
	var index = pp.panel('options').index;
	console.log("index:"+index);

	var rows;
	var rowsLength = 0;
	if(index == 0){
		rows = $("#bindSearchList2 input[name='pop_bind_seq']");
	}else{
		rows = $("#bindSearchList4 input[name='pop_bind_seq']");
	}
	console.log("rows:",rows);
	rowsLength = rows.length;
	
	var bindTarget = $("#bindTbl input[name='bind_seq']");
	var bindTargetLength = bindTarget.length;
	console.log("bindTargetLength:",bindTargetLength);
	
	$("#bindSearchList2 input[name='pop_bind_seq']").each(function() {
		var aValue = $(this).val();
		console.log("aValue:",aValue);
	});
	
	if(index == 0){
		for(var i=0;i<rowsLength;i++){
			//순번
			var bind_seq = $('#bindSearchList2 input[name="pop_bind_seq"]').eq(i).val();
			//변수명
			var bind_var_nm = $('#bindSearchList2 input[name="pop_bind_var_nm"]').eq(i).val();
			//변수타입
			var bind_var_type = $('#bindSearchList2 input[name="pop_bind_var_type"]').eq(i).val();
			//변수값
			var bind_var_value = $('#bindSearchList2 input[name="pop_bind_var_value"]').eq(i).val();
//			$("#bindTbl #bind_var_type"+(i+1)).combobox("setValue",bind_var_type);
//			$("#bindTbl #bind_var_value"+(i+1)).textbox("setValue",bind_var_value);
			$("#bindTbl #bind_var_type"+(i+1)).val(bind_var_type.toLowerCase());
			$("#bindTbl #bind_var_value"+(i+1)).val(bind_var_value);
		}	
	}else{
		for(var i=0;i<rowsLength;i++){
			//순번
			var bind_seq = $('#bindSearchList4 input[name="pop_bind_seq"]').eq(i).val();
			//변수명
			var bind_var_nm = $('#bindSearchList4 input[name="pop_bind_var_nm"]').eq(i).val();
			//변수타입
			var bind_var_type = $('#bindSearchList4 input[name="pop_bind_var_type"]').eq(i).val();
			
			if(bind_var_type.indexOf("(") > 0){
				bind_var_type = bind_var_type.substring(0,bind_var_type.indexOf("("));
			}

			if(bind_var_type == "CHAR" || bind_var_type == "NCHAR" || bind_var_type == "VARCHAR2" || bind_var_type == "NVARCHAR2"){
				bind_var_type = "String";
			}else if(bind_var_type == "NUMBER"){
				bind_var_type = "Number";
			}else if(bind_var_type == "DATE" || bind_var_type == "TIMESTAMP"){
				bind_var_type = "Date";
			}else{
				bind_var_type = "String";
			}
			
			//변수값
			var bind_var_value = $('#bindSearchList4 input[name="pop_bind_var_value"]').eq(i).val();
//			$("#bindTbl #bind_var_type"+(i+1)).combobox("setValue",bind_var_type);
//			$("#bindTbl #bind_var_value"+(i+1)).textbox("setValue",bind_var_value);
			$("#bindTbl #bind_var_type"+(i+1)).val(bind_var_type.toLowerCase());
			$("#bindTbl #bind_var_value"+(i+1)).val(bind_var_value);
		}
	}
	$("#bindSearchList2Table tbody").html("");
	$("#bindSearchList4Table tbody").html("");
	Btn_OnClosePopup('bindSearchDialog');
}

var topHtml1 = "";
topHtml1 += "<table class=\"detailT\" style=\"margin-top:5px;margin-bottom:5px;width:98%;\">\n";
topHtml1 += "<colgroup>\n";
topHtml1 += "	<col style=\"width:5%\">";
topHtml1 += "	<col style=\"width:19%\">";
topHtml1 += "	<col style=\"width:24%\">";
topHtml1 += "	<col style=\"width:17%\">";
topHtml1 += "	<col style=\"width:17%\">";
topHtml1 += "	<col style=\"width:17%\">";
topHtml1 += "</colgroup>\n";
topHtml1 += "<tr>\n";
topHtml1 += "	<th class='tar font11px'>NO</th>\n";
topHtml1 += "	<th class='tar font11px'>수행DB</th>\n";
topHtml1 += "	<th class='tar font11px'>수행일시</th>\n";
topHtml1 += "	<th class='tar font11px'>바인드수</th>\n";
topHtml1 += "	<th class='tar font11px'>Elapsed Time</th>\n";
topHtml1 += "	<th class='tar font11px'>Buffer Gets</th>\n";
topHtml1 += "</tr>\n";

var topHtml2 = "";
topHtml2 += "<table class=\"detailT\" style=\"margin-top:5px;margin-bottom:5px;width:98%;\">\n";
topHtml2 += "<colgroup>\n";
topHtml2 += "	<col style=\"width:5%\">";
topHtml2 += "	<col style=\"width:21%\">";
topHtml2 += "	<col style=\"width:24%\">";
topHtml2 += "	<col style=\"width:15%\">";
topHtml2 += "	<col style=\"width:17%\">";
topHtml2 += "	<col style=\"width:17%\">";
topHtml2 += "</colgroup>\n";
topHtml2 += "<tr>\n";
topHtml2 += "	<th class='tar font11px'>NO</th>\n";
topHtml2 += "	<th class='tar font11px'>SQL_ID</th>\n";
topHtml2 += "	<th class='tar font11px'>수행일시</th>\n";
topHtml2 += "	<th class='tar font11px'>바인드수</th>\n";
topHtml2 += "	<th class='tar font11px'>Elapsed Time</th>\n";
topHtml2 += "	<th class='tar font11px'>Buffer Gets</th>\n";
topHtml2 += "</tr>\n";

var bottomHtml = "";
//bottomHtml += "<table class=\"detailT\" style=\"margin-top:5px;margin-bottom:5px;width:98%;\">\n";
//bottomHtml += "<colgroup>\n";
//bottomHtml += "	<col style=\"width:10%\">";
//bottomHtml += "	<col style=\"width:38%\">";
//bottomHtml += "	<col style=\"width:14%\">";
//bottomHtml += "	<col style=\"width:38%\">";
//bottomHtml += "</colgroup>\n";
//bottomHtml += "<tr>\n";
//bottomHtml += "	<th class='tar font11px'>순번</th>\n";
//bottomHtml += "	<th class='tar font11px'>변수명</th>\n";
//bottomHtml += "	<th class='tar font11px'>변수타입</th>\n";
//bottomHtml += "	<th class='tar font11px'>변수값</th>\n";
//bottomHtml += "</tr>\n";

var callback_BindSearch1 = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var strHtml = topHtml1;
	        jQuery.each(data.rows, function(index, row){
	            strHtml += "<tr style='cursor:pointer' onclick='fnBindSearchList2("+row.perf_check_id+","+row.perf_check_step_id+","+row.program_id+","+row.program_execute_tms+")'>\n";
	            strHtml += "	<td class='tac font11px'>"+row.no+"</td>\n";
	            strHtml += "	<td class='tac font11px'>"+row.db_name+"</td>\n";
	            strHtml += "	<td class='tac font11px'>"+row.program_exec_dt+"</td>\n";
	            strHtml += "	<td class='tar font11px'>"+row.bind_cnt+"</td>\n";
	            strHtml += "	<td class='tar font11px'>"+row.elapsed_time+"</td>\n";
	            strHtml += "	<td class='tar font11px'>";
	            strHtml += 		row.buffer_gets;
	            
	            strHtml += "	<input type='hidden' name='temp_perf_check_id"+index+"' id='temp_perf_check_id' value='"+row.perf_check_id+"' \n";
	            strHtml += "	<input type='hidden' name='temp_perf_check_step_id"+index+"' id='temp_perf_check_step_id' value='"+row.perf_check_step_id+"' \n";
	            strHtml += "	<input type='hidden' name='temp_program_id"+index+"' id='temp_program_id' value='"+row.program_id+"' \n";
	            strHtml += "	<input type='hidden' name='temp_program_execute_tms"+index+"' id='temp_program_execute_tms' value='"+row.program_execute_tms+"' \n";
	            strHtml += "	</td>\n";
	            strHtml += "</tr>\n";
	        });
	        if(data.rows.length == 0){
	        	strHtml += "<tr><td colspan='6' class='tac'>검색된 데이터가 없습니다.</td></tr>\n";
	        }

	        strHtml += "</table>\n";
	    	$("#bindSearchList1").html(strHtml);
	    	//$("#bindSearchList2").html(bottomHtml+"</table>");
	    	//$("#bindSearchList4").html(bottomHtml+"</table>");
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

var callback_BindSearch2 = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var strHtml = bottomHtml;
	        jQuery.each(data.rows, function(index, row){
	        	var bind_var_type = row.bind_var_type;
	        	if(bind_var_type == null || bind_var_type == "null"){
	        		bind_var_type = "";
	        	}
	        	var bind_var_value = row.bind_var_value;
	        	bind_var_value = nvl(bind_var_value,"").replace(/'/g,'&apos;');
	        	if(bind_var_value == null || bind_var_value == "null"){
		        	bind_var_value = "";
	        	}
	        	
	            strHtml += "<tr>\n";
	            strHtml += "	<td class='ctext'><input type='hidden' id='pop_bind_seq"+index+"' name='pop_bind_seq' value='"+row.bind_seq+"'/>"+row.bind_seq+"</td>\n";
	            strHtml += "	<td class='ctext'><input type='text' id='pop_bind_var_nm"+index+"' name='pop_bind_var_nm' value='"+row.bind_var_nm+"' class='font11px tac' style='width:100%;border:0px;height:23px;' readonly/></td>\n";
	            strHtml += "	<td class='ctext'><input type='hidden' id='pop_bind_var_type"+index+"' name='pop_bind_var_type' value='"+bind_var_type+"'/>"+bind_var_type+"</td>\n";
	            strHtml += "	<td class='ctext'><input type='text' id='pop_bind_var_value"+index+"' name='pop_bind_var_value' value='"+bind_var_value+"' class='font11px tac' style='width:100%;border:0px;height:23px;' readonly/></td>\n";

	            strHtml += "</tr>\n";
	        });
	        if(data.rows.length == 0){
	        	strHtml += "<tr><td colspan='4' class='tac'>검색된 데이터가 없습니다.</td></tr>\n";
	        }
//	        strHtml += "</table>\n";
//	    	$("#bindSearchList2").html(strHtml);
//	    	$("#bindSearchList2").append(strHtml);
	    	$("#bindSearchList2Table tbody").html(strHtml);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

var callback_BindSearch3 = function(result) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var strHtml = topHtml2;
	        jQuery.each(data.rows, function(index, row){
	            strHtml += "<tr style='cursor:pointer' onclick='fnBindSearchList4("+index+")'>\n";
	            strHtml += "	<td class='tac font11px'>"+row.no+"</td>\n";
	            strHtml += "	<td class='tac font11px'>"+row.sql_id+"</td>\n";
	            strHtml += "	<td class='tac font11px'>"+row.program_exec_dt+"</td>\n";
	            strHtml += "	<td class='tar font11px'>"+row.bind_cnt+"</td>\n";
	            strHtml += "	<td class='tar font11px'>"+row.elapsed_time+"</td>\n";
	            strHtml += "	<td class='tar font11px'>";
	            strHtml += row.buffer_gets;
	            strHtml += "	<input type='hidden' name='grid_sql_id' id='grid_sql_id"+index+"' value='"+row.sql_id+"'/>";
	            strHtml += "	<input type='hidden' name='grid_snap_time' id='grid_snap_time"+index+"' value='"+row.snap_time+"'/>";
	            strHtml += "	<input type='hidden' name='grid_last_captured' id='grid_last_captured"+index+"' value='"+row.last_captured+"'/>";
	            strHtml += "	</td>\n";
	            strHtml += "</tr>\n";
	        });
	        if(data.rows.length == 0){
	        	strHtml += "<tr><td colspan='6' class='tac'>검색된 데이터가 없습니다.</td></tr>\n";
	        }
//	        strHtml += "</table>\n";
	    	$("#bindSearchList3").html(strHtml);
//	    	$("#bindSearchList2").html(bottomHtml+"</table>");
//	    	$("#bindSearchList4").html(bottomHtml+"</table>");
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

var callback_BindSearch4 = function(result) {
	console.log("callback_BindSearch4 callback_BindSearch4");
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			var strHtml = bottomHtml;
	        jQuery.each(data.rows, function(index, row){
	        	var bind_var_type = row.bind_var_type;
	        	if(bind_var_type == null || bind_var_type == "null"){
		        	console.log("bind_var_type is null Object or null String");
	        		bind_var_type = "";
	        	}
	        	var bind_var_value = row.bind_var_value;
	        	if(bind_var_value == null || bind_var_value == "null"){
		        	console.log("bind_var_value is null Object or null String");
		        	bind_var_value = "";
	        	}	        	
	            strHtml += "<tr>\n";
	            strHtml += "	<td class='ctext'><input type='hidden' id='pop_bind_seq"+index+"' name='pop_bind_seq' value='"+row.bind_seq+"'/>"+row.bind_seq+"</td>\n";
	            strHtml += "	<td class='ctext'><input type='text' id='pop_bind_var_nm"+index+"' name='pop_bind_var_nm' value='"+row.bind_var_nm+"' class='font11px tac' style='width:100%;border:0px;height:23px;' readonly/></td>\n";
	            strHtml += "	<td class='ctext'><input type='hidden' id='pop_bind_var_type"+index+"' name='pop_bind_var_type' value='"+bind_var_type+"'/>"+bind_var_type+"</td>\n";
	            strHtml += "	<td class='ctext'><input type='text' id='pop_bind_var_value"+index+"' name='pop_bind_var_value' value='"+bind_var_value+"' class='font11px tac' style='width:100%;border:0px;height:23px;' readonly/></td>\n";

	            strHtml += "</tr>\n";
	        });
	        if(data.rows.length == 0){
	        	strHtml += "<tr><td colspan='4' class='tac'>검색된 데이터가 없습니다.</td></tr>\n";
	        }	        
//	        strHtml += "</table>\n";
//	    	$("#bindSearchList4").html(strHtml);
//	    	$("#bindSearchList4").append(strHtml);
	    	$("#bindSearchList4Table tbody").append(strHtml);
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
};

function fnClose(){
//	$("#bindSearchList2Table tbody").html("");
//	$("#bindSearchList4Table tbody").html("");
	$("#bindSearchList2Table tbody tr").remove();
	$("#bindSearchList4Table tbody tr").remove();
	Btn_OnClosePopup('bindSearchDialog');
}
