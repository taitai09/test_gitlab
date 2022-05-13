$(document).ready(function() {
	$('#selftunSqlPop').window({
		title : "셀프테스트 상세",
		top:getWindowTop(480),
		left:getWindowLeft(1000)
	});
});

function Btn_SetSqlInfo(){
//	eval("if_"+frameName).$("#sql_text").val($("#sql_text_pop").val());
	$("#sql_text").val($("#sql_text_pop").val());
	var rowCnt = strParseInt($("#bindCnt").val(),0);
//	eval("if_"+frameName).$("#bindTbl tbody tr").remove();
	$("#bindTbl tbody tr").remove();
	var strHtml = "";
	
	if(rowCnt > 0){
		for(var i = 0 ; i < rowCnt ; i++){
			var bindSeq = $("#bind_p_seq"+(i+1)).val();
			var bindVarNm = $("#bind_p_var_nm"+(i+1)).val();			
			var bindVarValue = $("#bind_p_var_value"+(i+1)).val();
			var bindVarType = $("#bind_p_var_type"+(i+1)).val();
			
			strHtml += "<tr><td class='ctext'>";
			strHtml += "<input type='hidden' id='bind_set_seq"+(i+1)+"' name='bind_set_seq' value='1'/>";
			strHtml += "<input type='hidden' id='mandatory_yn"+(i+1)+"' name='mandatory_yn' value='Y'>";
			strHtml += "<input type='text' id='bind_seq"+(i+1)+"' name='bind_seq' value='"+bindSeq+"' data-options='readonly:true' class='w50 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><input type='text' id='bind_var_nm"+(i+1)+"' name='bind_var_nm' value='"+bindVarNm+"' class='w130 easyui-textbox'/></td>";
			strHtml += "<td class='ctext'><select id='bind_var_type"+(i+1)+"' name='bind_var_type' class='w130 easyui-combobox'><option value='string'>String타입</option><option value='number'>Number타입</option><option value='date'>Date타입</option><option value='char'>Char타입</option></select></td>";			
			strHtml += "<td class='ctext'><input type='text' id='bind_var_value"+(i+1)+"' name='bind_var_value' value='"+bindVarValue+"' class='w130 easyui-textbox'/></td></tr>";
			
//			eval("if_"+frameName).$("#bindTbl #bind_var_type"+(i+1)).combobox("setValue", bindVarType);
			$("#bindTbl #bind_var_type"+(i+1)).combobox("setValue", bindVarType);
		}

//		eval("if_"+frameName).$("#bindTbl tbody").append(strHtml);
		$("#bindTbl tbody").append(strHtml);
		
		for(var i = 0 ; i < rowCnt ; i++){
//			eval("if_"+frameName).$("#bindTbl #bind_seq"+(i+1)).textbox({readonly:true});	
//			eval("if_"+frameName).$("#bindTbl #bind_var_nm"+(i+1)).textbox();
//			eval("if_"+frameName).$("#bindTbl #bind_var_type"+(i+1)).combobox();			
//			eval("if_"+frameName).$("#bindTbl #bind_var_value"+(i+1)).textbox();		
			$("#bindTbl #bind_seq"+(i+1)).textbox({readonly:true});	
			$("#bindTbl #bind_var_nm"+(i+1)).textbox();
			$("#bindTbl #bind_var_type"+(i+1)).combobox();			
			$("#bindTbl #bind_var_value"+(i+1)).textbox();		
		}
	}
	
	Btn_OnClosePopup('selftunSqlPop');
}