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
	
		CKEDITOR.replace("board_contents",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
		});		
	
	
	if($("#submit_form #top_notice_yn").val() =='Y'){
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
				$("#submit_form #top_notice_yn").val("Y");
			}else{
				$("#submit_form #top_notice_yn").val("N");
			}
		}
	})
	
});

function Btn_OnDelete(){
	parent.$.messager.confirm('확인', '게시글을 삭제하시겠습니까?', function(r){
		if (r){
			ajaxCall("/BoardMng/Notice/delete",
					$("#submit_form"),
					"POST",
					callback_deleteAction);
		}
	});	
}

//callback 함수
var callback_deleteAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','게시글이 정상적으로 삭제되었습니다.','info',function(){
			setTimeout(function() {
				Btn_GoList();
			},1000);	
		});	
	}
};

function Btn_OnUpdate(){
	if($('#title').textbox('getValue') == ""){
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	
	var str = CKEDITOR.instances.board_contents.getData();
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/<P>/g,"");
	str = str.replace(/<\/P>/g,"");
	str = str.replace(/\r\n/g,"");
	
	if(str == ""){
		parent.$.messager.alert('','내용을 입력해 주세요.');
		return false;
	}

	$("#board_contents").val( CKEDITOR.instances.board_contents.getData() );
	
	var formData = new FormData($("#submit_form")[0]);

	ajaxMultiCall("/BoardMng/Notice/update",
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
		});		
	}
};

function Btn_GoList(){
	$("#submit_form #call_from_parent").val("Y");
	$("#submit_form").attr("action","/BoardMng/Notice");
	$("#submit_form").submit();	
}
function downGuideFile(file_nm, org_file_nm){
	$("#file_form #file_nm").val(file_nm);
	$("#file_form #org_file_nm").val(org_file_nm);

	
	$("#file_form").attr("action","/BoardMng/Notice/DownBoardFile");
	$("#file_form").submit();	

}


/*function downGuideFile(file_nm, org_file_nm){
	$("#file_form #file_nm").val(file_nm);
	$("#file_form #org_file_nm").val(org_file_nm);
	
	var form = document.file_form;
//	form.action="/Precedent/DownGuideFile";
	form.action="/BoardMng/Notice/DownBoardFile";
	form.target = "downloadFrame";
	form.submit();
	
}*/

function deleteFile(obj,file_seq,file_nm, auth_cd, reg_user_id, user_id){
	
	if(auth_cd == 'ROLE_ITMANAGER' || auth_cd == 'ROLE_TUNER' || auth_cd == 'ROLE_DEV' || auth_cd == 'ROLE_DEV_DEPLOYMANAGER' || auth_cd == 'ROLE_DEPLOYMANAGER'){
		parent.$.messager.alert('','권한이 없습니다.','error');
		return false;
	}
	
	$("#file_form #file_nm").val(file_nm);
	$("#file_form #file_seq").val(file_seq);
	$.ajax({
		type: "POST",
		url: "/BoardMng/Notice/deleteAttachFile",
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