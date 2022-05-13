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
		},
	    onSelect:function(rec){
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	// Instance 조회			
	    	$('#selectInstance').combobox({
	    	    url:"/Common/getMasterInstance?dbid="+rec.dbid,
	    	    method:"get",
	    		valueField:'inst_id',
	    	    textField:'inst_name',
				onLoadSuccess: function(items) {
					parent.$.messager.progress('close');
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}					
				}
	    	});	    	
	    }
	});	
	
	// ALERT 유형 조회			
	$('#searchKey').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1011&isChoice=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm'
	});	
});

function Btn_OnClick(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectInstance').combobox('getValue') == ""){
		parent.$.messager.alert('','INST_ID를 선택해 주세요.');
		return false;
	}	
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#submit_form #inst_id").val($('#selectInstance').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("ALERT 설정"," "); 

	ajaxCall("/AlertSetting",
			$("#submit_form"),
			"POST",
			callback_AlertSettingAction);		
}

//callback 함수
var callback_AlertSettingAction = function(result) {
	var strHtml = "";
	$("#detailT tbody tr").remove();
	
	if(result.result){
		for(var i = 0 ; i < result.object.length ; i++){
			var post = result.object[i];
			
			strHtml += "<tr>";
			strHtml += "<td><b>▶  "+post.alert_type_nm+"</b><input type='hidden' id='"+post.alert_type_cd+"_alert_type_cd' name='alert_type_cd' value='"+post.alert_type_cd+"'></td>";
			if(post.alert_threshold == null){
				var alertThreshold = "";
			}else{
				alertThreshold = post.alert_threshold;
			}
			strHtml += "<td class='ctext'><input type='text' id='"+post.alert_type_cd+"_alert_threshold' name='alert_threshold' value='"+alertThreshold+"' class='w200 easyuit'/></td>";
			strHtml += "<td class='ctext'>";
			strHtml += "<select id='"+post.alert_type_cd+"_enable_yn' name='enable_yn' data-options='panelHeight:\"auto\"' class='w200 easyuic'>";
			strHtml += "<option value='Y' "; if(post.enable_yn == "Y"){ strHtml += "selected"; } strHtml += ">활성화</option>";
			strHtml += "<option value='N' "; if(post.enable_yn == "N"){ strHtml += "selected"; } strHtml += ">비활성화</option>";
			strHtml += "</select>";
			strHtml += "</td>";
			strHtml += "<td class='ctext'>";
			strHtml += "<select id='"+post.alert_type_cd+"_sms_send_yn' name='sms_send_yn' data-options='panelHeight:\"auto\"' class='w200 easyuic'>";
			strHtml += "<option value='Y' "; if(post.sms_send_yn == "Y"){ strHtml += "selected"; } strHtml += ">발송</option>";
			strHtml += "<option value='N' "; if(post.sms_send_yn == "N"){ strHtml += "selected"; } strHtml += ">미발송</option>";
			strHtml += "</select>";
			strHtml += "</td></tr>";			
		}
	}else{
		strHtml = "<tr><td colspan='4' class='ctext'>검색된 데이터가 없습니다.</td></tr>";
	}
	
	$("#detailT tbody").append(strHtml);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
	
	$(".easyuit").textbox();
	$(".easyuic").combobox();	
};

function Btn_SaveSetting(){
	$("#detail_form #dbid").val($('#dbid').val());
	$("#detail_form #inst_id").val($('#submit_form #inst_id').val());
	
	ajaxCall("/AlertSetting/Save",
			$("#detail_form"),
			"POST",
			callback_SaveAlertSettingAction);		
}

//callback 함수
var callback_SaveAlertSettingAction = function(result) {
	if(result.result){
//		parent.$.messager.alert('','ALERT 설정 저장이 완료 되었습니다.','info',function(){
		parent.$.messager.alert('','저장 되었습니다.','info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);
		});		
	}
};