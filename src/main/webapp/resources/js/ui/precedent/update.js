var chkFixYn = false;
$(document).ready(function() {
	var clipboard = new Clipboard('#sqlCopyBtn');
    clipboard.on('success', function(e) {
    	parent.$.messager.alert('','복사 되었습니다.');
    });
    
    var clipImprSql = new Clipboard('#imprSqlCopyBtn',{
    	text: function() {
            return $("#impr_sql_text_h").val();
        }
    });    
    clipImprSql.on('success', function(e) {
    	parent.$.messager.alert('','개선 SQL TEXT가 복사되었습니다.');
    });
    
    var clipImprbExec = new Clipboard('#imprbExecCopyBtn',{
    	text: function() {
            return $("#imprb_exec_plan_h").val();
        }
    });    
    clipImprbExec.on('success', function(e) {
    	parent.$.messager.alert('','개선전 실행계획이 복사되었습니다.');
    });
    
    var clipImpraExec = new Clipboard('#impraExecCopyBtn',{
    	text: function() {
            return $("#impra_exec_plan_h").val();
        }
    });    
    clipImpraExec.on('success', function(e) {
    	parent.$.messager.alert('','개선후 실행계획이 복사되었습니다.');
    });    
    
	$("#historyViewTab").tabs({
		plain: true,
		onSelect: function(title,index){
			/* 탭을 클릭시 화면을 높이 자동 조절 */
			var height = $("#container").height();
			parent.resizeTopFrame($("#menu_id").val(), height);  
		}
	});
	
	if($("#guide_div_cd").val() == "2"){
		CKEDITOR.replace("impr_sql_text",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
		
		CKEDITOR.replace("imprb_exec_plan",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-family:'굴림체';font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});
		
		CKEDITOR.replace("impra_exec_plan",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-family:'굴림체';font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});	
		
		/* html 태크 제거 및 공백 제거*/
		var impr_sql_text_h = $("#impr_sql_text_h").val();
		$("#impr_sql_text_h").val(parent.formatHTML(impr_sql_text_h));

		var imprb_exec_plan_h = $("#imprb_exec_plan_h").val();
		$("#imprb_exec_plan_h").val(parent.formatHTML(imprb_exec_plan_h));

		var impra_exec_plan_h = $("#impra_exec_plan_h").val();
		$("#impra_exec_plan_h").val(parent.formatHTML(impra_exec_plan_h));		
	}else{
		CKEDITOR.replace("guide_sbst",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
		});		
	}
	
	//인덱스 이력 조회
	createSqlTuningIndexHistoryTable();
	
	if($("#submit_form #top_fix_yn").val() =='Y'){
		chkFixYn = true;
	}else{
		chkFixYn = false;
	}
	
	$('#chkTopFixYn').switchbutton({
		checked: chkFixYn,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked){
				$("#submit_form #top_fix_yn").val("Y");
			}else{
				$("#submit_form #top_fix_yn").val("N");
			}
		}
	})
	
});

function Btn_OnDelete(){
	parent.$.messager.confirm('확인', '정말로 삭제하시겠습니까?', function(r){
		if (r){
			ajaxCall("/Precedent/DeleteAction",
					$("#submit_form"),
					"POST",
					callback_deleteAction);
		}
	});	
}

//callback 함수
var callback_deleteAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 삭제되었습니다.','info',function(){
			setTimeout(function() {
				Btn_GoList();
			},1000);	
		});	
	}
};

function Btn_OnUpdate(){
	if($('#guide_title_nm').textbox('getValue') == ""){
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	var str = CKEDITOR.instances.guide_sbst.getData();
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if(str == ""){
		parent.$.messager.alert('','내용을 입력해 주세요.');
		return false;
	}

	
	
	$("#guide_sbst").val( CKEDITOR.instances.guide_sbst.getData() );
	
	var formData = new FormData($("#submit_form")[0]);

	ajaxMultiCall("/Precedent/UpdateAction",
			formData,
			"POST",
			callback_updateAction);	
}

//callback 함수
var callback_updateAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 수정되었습니다.','info',function(){
			setTimeout(function() {
				Btn_GoList();
			},1000);	
		});	
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			//오류가 났을 경우 리스트로 가는 것을 주석처리
			//Btn_GoList();	
		});		
	}
};

function Btn_GoList(){
	$("#submit_form #call_from_child").val("Y");
	$("#submit_form #menu_nm").val("SQL 튜닝 사례/가이드");
	$("#submit_form").attr("action","/Precedent");
	$("#submit_form").submit();	
}
//function downGuideFile(file_nm, org_file_nm){
//	$("#submit_form").attr("action","/Precedent/DownGuideFile");
//	$("#submit_form").submit();	
//}
function downGuideFile(file_nm, org_file_nm){
	$("#file_nm").val(file_nm);
	$("#org_file_nm").val(org_file_nm);
	console.log(file_nm);
	console.log(org_file_nm);
	//$("#submit_form").attr("action","/Precedent/DownGuideFile");
	$("#submit_form").attr("action","/Precedent/DownFile");
	$("#submit_form").submit();
}

function deleteGuideFile(obj,file_seq,file_nm, auth_cd, reg_user_id, user_id){
	
	if(auth_cd == 'ROLE_DEV'){
		parent.$.messager.alert('','권한이 없습니다.','error');
		return false;
	}
	
	$("#file_form #file_nm").val(file_nm);
	$("#file_form #file_seq").val(file_seq);
	$.ajax({
		type: "POST",
		url: "/Precedent/DeleteGuideFile",
		data: $("#file_form").serialize(),
		success: function(result) {
			if(!result.result){
				parent.$.messager.alert('',result.message,'error');
			}else{
				parent.$.messager.alert('',result.message,'info');
				$(obj).parent().remove();
			}
		}
	});	
}

function Btn_GoNext(strIndex){
	$('#historyViewTab').tabs('select', strIndex);
}