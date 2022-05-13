$(document).ready(function() {

	$('#loadSqlProjectUnitPop').window({
		title : "SQL 적재",
		top:getWindowTop(190),
		left:getWindowLeft(400)
	});

});

function Btn_ShowProjectListPop() {
	$('#pop_projectList_form #project_nm').textbox('setValue', '');
	$('#pop_projectList_form #del_yn').combobox('setValue','N');
	$('#pop_projectList_form #projectList').datagrid('loadData',[]);
	
	$('#popProjectListPop').window("open");
	
	$("#pop_projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}

function setProjectRowPop(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#loadSQLProjectUnit_form #project_nm").textbox("setValue", row.project_nm);
	
	$("#loadSQLProjectUnit_form #project_id").val(row.project_id);
}

function loadSQLAction(){
	if($('#loadSQLProjectUnit_form #project_nm').textbox('getValue') == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요','warning');
		return false;
	}
	
	if($('#uploadFile').textbox('getValue') == ""){
		$.messager.alert('','업로드할 SQL 파일을 선택해 주세요.');
		return false;
	}
	
	var objFile = $('#loadSQLProjectUnit_form #uploadFile').textbox('getValue');
	
	if(!checkExtentionFile1(objFile)) {
		$.messager.alert('',' .txt .sql 확장자 파일만 업로드가 가능합니다.');
		$('#loadSQLProjectUnit_form #uploadFile').textbox('setValue',"");
		return false;
	}
	
	var formData = new FormData($("#loadSQLProjectUnit_form")[0]);

	ajaxMultiCall("/ProjectUnitLoadSQLFile", formData, "POST", callback_LoadSQLFile);
//	performAjaxSubmit();
	
	/* modal progress open */
//	openMessageProgress('SQL 적재','SQL을 DB에 적재중입니다.');
	parent.openMessageProgress('SQL 적재','SQL을 DB에 적재중입니다.');
    window.status="SQL을 DB에 적재중입니다."
}

function performAjaxSubmit() {
	var formData = new FormData($("#loadSQLProjectUnit_form")[0]);
    var xhr = new XMLHttpRequest();  
    xhr.onreadystatechange = function () {
    	if(xhr.status == 0){
    		$.messager.progress(); 
    	}
        if (xhr.readyState == 4 && xhr.status == 200) {
    		$.messager.progress('close'); 
        	console.log("onreadystatechange...xhr.readyState :"+xhr.readyState +" xhr.status:"+xhr.status);
            callback_LoadSQLFile(JSON.parse(this.responseText));
        }
    };    
    xhr.upload.addEventListener("progress", function(e) {
        var pc = parseInt(e.loaded / e.total * 100);
        window.status=pc + "%"
    }, false);    
    xhr.open("POST","/LoadSQLFile", true);
    xhr.send(formData);
    xhr.onload = function(e) {
    	console.log("onload...this.status:"+this.status);
    	console.log("onload...this.responseText:"+this.responseText);
        if (this.status == 200) {
           //callback_LoadSQLFile(JSON.parse(this.responseText));
        }
    };                    
}   

//callback 함수
var callback_LoadSQLFile = function(result) {
	if(result.result){
		$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				Btn_OnClosePopup("loadSqlProjectUnitPop");
				/* iframe에 함수 호출 */
//				eval("if_"+frameName).Btn_OnClick();
				try {
					Btn_OnClick();
				} catch(error) {
					;
				}
				
				try {
					Btn_OnClick_loadSQL();	// 적재SQL의  SQL 적재를 CS 구조로 변경하여 추가함
				} catch(error) {
					
				}
			},1000);			
		});
	}else{
		$.messager.alert('',result.message,'error',function(){
			closePopup();	
		});		
	}
	
	/* modal progress close */
//	closeMessageProgress();
	parent.closeMessageProgress();
};