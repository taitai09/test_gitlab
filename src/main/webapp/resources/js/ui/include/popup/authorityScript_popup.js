var checkList = new Array();
var text = "";
var checkCnt = 0;

$(document).ready(function() {
	$('#authorityScriptPop').window({
		title : "권한스크립트",
		top:getWindowTop(870),
		left:getWindowLeft(950)
	});
	
	text = 
"/* :num : 권한스크립트 생성 */\n" +
"SELECT 'GRANT'||\n" +
"       (CASE \n" +
"           WHEN OBJECT_TYPE IN ('TABLE') THEN ' SELECT, UPDATE, INSERT, DELETE '\n" +
"           WHEN OBJECT_TYPE IN ('VIEW', 'SEQUENCE') THEN ' SELECT '\n" +
"           WHEN OBJECT_TYPE IN ('PACKAGE', 'PROCEDURE', 'FUNCTION') THEN ' EXECUTE '\n" +
"       END) ||\n" +
"       'ON ' || OWNER || '.' || OBJECT_NAME || ' TO ' ||\n" +
"       (SELECT NVL(MAX(UPPER(ORACLE_USER_ID)), 'OPENMON') FROM DATABASE WHERE DBID = :dbid) || ';'\n" +
"       AS GRANT_SCRIPT\n" +
"FROM ODS_OBJECTS\n" +
"WHERE DBID = :dbid \n" +
"AND OWNER = :owner\n" +
"AND OBJECT_TYPE = upper(:object_type) \n" +
"AND BASE_DAY = (SELECT MAX(BASE_DAY) FROM ODS_OBJECTS WHERE DBID = :dbid)";
	
	$('#authorityScript_form #checkTable').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkTable').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	$('#authorityScript_form #checkView').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkView').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	$('#authorityScript_form #checkSequence').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkSequence').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	$('#authorityScript_form #checkFunction').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkFunction').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	$('#authorityScript_form #checkProcedure').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkProcedure').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	$('#authorityScript_form #checkPackage').checkbox({
		onChange: function(checked) {
			let label = $('#authorityScript_form #checkPackage').checkbox('options').label;
			
			if(checked) checkList.push(label);
			else spliceCheckList(label);
		}
	});
	
	var clipboard = new Clipboard('#authorityScript_form #scriptCopyBtn');
	
	clipboard.on('success', function(e) {
		if ( $("#authorityScript_form #scriptView").val().trim() == "" ){
			parent.$.messager.alert('경고','복사할 권한 스크립트가 없습니다.','warning');
			return false;
		} else {
			parent.$.messager.alert('','복사 되었습니다.');
		}
	});
});

function spliceCheckList(element) {
	var idx = checkList.indexOf(element);
	
	if(idx > -1) {
		checkList.splice(idx, 1);
	}
}

function LoadUserName() {
	$("#authorityScript_form #authorityUserName").combobox("setValue",'');
	$("#authorityScript_form #authorityUserName").combobox("textbox").attr("placeholder",'선택');
	
	$("#authorityScript_form #authorityUserName").combobox({
		url:"/Common/getUserName?dbid="+$('#authorityScript_form #dbid').val(),
		method:"get",
		valueField:'username',
		textField:'username',
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
			$("#authorityScript_form #authorityUserName").combobox("textbox").attr("placeholder",'선택');
		},
		onLoadError: function(){
			parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
			parent.$.messager.progress('close');
		}
	});
}

function Btn_OnCreate() {
	var dbid = $('#authorityScript_form #dbid').val();
	var owner = $("#authorityScript_form #authorityUserName").combobox("getText");
	$("#authorityScript_form #scriptView").val("");
	
	if ( owner == null || owner == "" ) {
		parent.$.messager.alert('경고','Object Owner를 선택해주세요.','warning');
		return false;
	} else {
		if ( dbid.indexOf('-') == 0 ) { // tibero DBID는 '-'로 시작하여 '-'로 구분
			
			if ( checkList.length > 0 ) {
				
				for ( let typeIdx = 0; typeIdx < checkList.length; typeIdx++ ) {
					$.ajax({
						type: "POST",
						url: "/DBChangePerformanceImpactAnalysisForTibero/getGrantScriptsTibero",
						datatype: 'json',
						data: {
							DBID : dbid ,
							obOwner : $("#authorityScript_form #authorityUserName").combobox('getValue'),
							obType : checkList[typeIdx]
						} ,
						success: function( result ) {
							let data = JSON.parse(result);
							let jData = data.message;
							checkCnt++;
							
							if ( data.is_error == "false" && checkList.length == 1 && jData.length == 0 ) {
								checkCnt = 0;
								console.log(jData);
								parent.$.messager.alert('경고','권한을 생성할 Object 가 없습니다.','warning');
								return false;
								
							} else {
								let scriptView = $("#authorityScript_form #scriptView");
								
								if ( ( checkList == 1 || checkList.length == checkCnt ) &&
										jData.length == 0 &&
										scriptView.val() == "" ) {
									parent.$.messager.alert('경고','권한을 생성할 Object 가 없습니다.','warning');
									return false;
								} else {
									for (let dataIdx = 0; dataIdx < jData.length; dataIdx++) {
										jData[dataIdx] = JSON.parse(jData[dataIdx]);
										if ( scriptView.val() == '' ) {
											scriptView.val( jData[dataIdx].grant_scripts );
										} else {
											let tempValue = scriptView.val();
											scriptView.val( tempValue + "\r\n" + jData[dataIdx].grant_scripts );
										}
									}
								}
								console.log(jData);
							}
						},
						error:function ( result ) {
							checkCnt = 0;
							console.log(reulst);
							parent.$.messager.alert('정보','데이터를 불러오지 못했습니다.' , 'info');
							return false;
						}
					});
				}
				
				checkCnt = 0;
			} else {
				checkCnt = 0;
				parent.$.messager.alert('경고','권한스크립트 생성대상 Object 를 선택하세요.','warning');
				return false;
			}
		} else {
			if ( checkList.length > 0 ) {
				for ( let index = 0; index < checkList.length; index++ ) {
					let tempText = text;
					let object_type = checkList[index];
					
					tempText = tempText.replace(/:num/gi, index + "-" + object_type);
					tempText = tempText.replace(/:dbid/gi, "'" + dbid + "'");
					tempText = tempText.replace(/:owner/gi, "'" + owner + "'");
					
					$('#authorityScript_form #authority_sql').val(tempText.replace(/:object_type/gi, "'" + object_type + "'"));
					
					ajaxCall("/SQLAutomaticPerformanceCheck/getAuthoritySQL",
							$("#authorityScript_form"),
							"POST",
							callback_GetAuthoritySQL);
				}
				
				checkCnt = 0;
			} else {
				checkCnt = 0;
				parent.$.messager.alert('경고','권한스크립트 생성대상 Object 를 선택하세요.','warning');
				return false;
			}
		}
		
	}
}

var callback_GetAuthoritySQL = function(result) {
	let rows = JSON.parse(result).rows;
	let scriptView = $("#authorityScript_form #scriptView");
	checkCnt++;
	
	if ( ( checkList.length == 1 || checkCnt == checkList.length ) && 
			rows.length == 0 && scriptView.val() == "" ) {
		checkCnt = 0;
		parent.$.messager.alert('경고','권한을 생성할 Object 가 없습니다.','warning');
		return false;
		
	} else {
		for ( let index = 0; index < rows.length; index++ ) {
			if ( scriptView.val() == '' ) {
				scriptView.val( rows[index].grant_script );
			} else {
				let tempValue = scriptView.val();
				scriptView.val( tempValue + "\r\n" + rows[index].grant_script );
				tempValue = "";
			}
		}
	}
};


