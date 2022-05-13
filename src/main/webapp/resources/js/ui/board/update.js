var index = 0;
$(document).ready(function() {
	CKEDITOR.replace("board_contents",{
		width:"99%",
		height:"400px",
		contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
	});	
	
	var checked = false;
	
	if($("#top_notice_yn").val() == "Y") checked = true;
	
	$('#chkTopNotice').switchbutton({
		checked: checked,
		onText:"Yes",
		offText:"No",
		onChange: function(checked){
			if(checked) $("#top_notice_yn").val("Y"); 
			else $("#top_notice_yn").val("N");
		}
	});
	
	if ( $("#defaultText").val() != null && $("#defaultText").val() != '' ) {
		parent.$.messager.alert('', $("#defaultText").val() ,'error');
	}
	
	
});

function Btn_OnClick() {
	var str = CKEDITOR.instances.board_contents.getData();
	let totMaxSize = 100 * 1024 * 1024;
	let maxSize = 0;
	let totSize = 0;
	let fileSize = 0;
	let max = 0;
	
	if($("#subTitle").textbox('getValue') == "") {
		parent.$.messager.alert('','제목을 입력해 주세요.');
		return false;
	}
	if($('#subTitle').textbox('getValue').length > 50) {
		parent.$.messager.alert('','제목은 50자 이내로 입력해 주세요.');
		return false;
	}
	
	let tit = $('#subTitle').textbox('getValue');
	tit = tit.replace(/&nbsp;/g,"");
	
	tit = tit.replace(/<(\/a|a[^>]*)>/gi,"");
	tit = tit.replace(/<(\/p|p[^>]*)>/gi,"");
	tit = tit.replace(/<(\/span|span[^>]*)>/gi,"");
	tit = tit.replace(/<(\/script|script[^>]*)>/gi,"");
	tit = tit.replace(/</gi,"&lt;");
	tit = tit.replace(/>/gi,"&gt;");
//	tit = tit.replace(/&lt;/g,"");
//	tit = tit.replace(/&gt;/g,"");
	$('#title').val(tit);
	
	if(str.length <= 0){
		parent.$.messager.alert('','내용을 등록해주세요.');
		return false;
	}
	
	str = str.replace(/&nbsp;/g,"");
	str = str.replace(/&lt;/gi,"<<");
	str = str.replace(/&gt;/gi,">>");
	
	str = str.replace(/<<(\/a|a[^>>]*)>>/gi,"");
	str = str.replace(/<<(\/p|p[^>>]*)>>/gi,"");
	str = str.replace(/<<(\/span|span[^>>]*)>>/gi,"");
	str = str.replace(/<<(\/script|script[^>>]*)>>/gi,"");
	str = str.replace(/<</gi,"&lt; ");
	str = str.replace(/>>/gi," &gt;");
	str = str.replace(/<(\/a|a[^>]*)>/gi,"");
	str = str.replace(/<(\/p|p[^>]*)>/gi,"");
	str = str.replace(/<(\/span|span[^>]*)>/gi,"");
	str = str.replace(/<(\/script|script[^>]*)>/gi,"");
//	str = str.replace(/</gi,"< ");
//	str = str.replace(/>/gi," >");
	str = str.replace(/\r\n/g,"");
	$("#board_contents").val(str);
	
	if ( $("#board_mgmt_no").val() == '2' ) {
		max = 10;
	} else {
		max = 100;
	}
	maxSize = max * 1024 * 1024;
	
	let file_seq = $("#file_form #file_seq").val();
	let file_nm = $("#file_form #file_nm").val();
	let org_file_nm = $("#file_form #org_file_nm").val();
	
	if ( $("#uploadFile").textbox("getValue") != null && $("#uploadFile").textbox("getValue") != '' ) {
		let fileAddr = document.submit_form.uploadFile[1].files;
		let files = "";
		
		if ( $("#files").val() != null && $("#files").val() != '' ){
			files = $("#files").val().split(",");
			files.pop();
		}
		
		let fileIdx = null;
		let fileSeq = null;
		let fileNm = null;
		let orgFileNm = null;
		
		if ( index.length > 0 ){
			fileIdx = index.split(",");
			fileSeq = file_seq.split(",");
			fileNm = file_nm.split(",");
			orgFileNm = org_file_nm.split(",");
			for (var comIdx = 0; comIdx < orgFileNm.length; comIdx++) {
				orgFileNm[comIdx] = orgFileNm[comIdx].replace(/\:/g," ");
			}
			for (var delIdx = 0; delIdx < orgFileNm.length; delIdx++) {
				for (var subIdx = 0; subIdx < files.length; subIdx++) {
					if ( orgFileNm[delIdx] == files[subIdx] ) {
						files.splice(subIdx,1);
					}
				}
			}
		}
		
		if( fileAddr.length > 3 || (fileAddr.length+files.length) > 3) {
			parent.$.messager.alert('','첨부파일은 최대 3개까지 첨부가능 합니다.','error');
			
			if( fileIdx != null && fileIdx.length > 0 ) {
				
				for (var fileCnt = 0; fileCnt < fileIdx.length; fileCnt++) {
					
					$("#file_area").append("<span id=fileArea"+fileIdx[fileCnt]+">" +
							"<a href='javascript:;' onClick=Btn_downFile('"+fileNm[fileCnt]+"','"+orgFileNm[fileCnt].replace(/ /g,":").replace(/\,/g,"*")+"');>"+orgFileNm[fileCnt].replace(/\:/g," ").replace(/\*/g,",")+"</a>&nbsp;"+
							"<a href='javascript:;' onClick=deleteFile(this,'"+fileSeq[fileCnt]+"','"+fileNm[fileCnt]+"','"+orgFileNm[fileCnt].replace(/ /g,":").replace(/\,/g,"*")+"','"+$('#auth_cd').val()+"','"+$('#reg_id').val()+
							"','"+$('#user_id').val()+"','"+fileIdx[fileCnt]+"');><i class='btnIcon fas fa-trash-alt'></i></a></span>");
					$("#file_area").append(" ");
				}
				
				$("#file_form #file_nm").val("");
				$("#file_form #file_seq").val("");
				$("#file_form #org_file_nm").val("");
				index = 0;
			}
			
			return false;
		}
		
		for ( var sizeIdx = 0; sizeIdx < fileAddr.length; sizeIdx++ ) {
			totSize += fileAddr[sizeIdx].size;
			
			console.log(fileAddr[sizeIdx].name+" = 파일사이즈 : "+ fileAddr[sizeIdx].size + ", MAX : "+maxSize +" , totSize : "+totSize);
			if ( fileAddr[sizeIdx].size >= maxSize ) {
				fileSize = fileAddr[sizeIdx].size/1024/1024;
				
				parent.$.messager.alert('','[ '+fileAddr[sizeIdx].name+' ]<br>파일의 용량이 너무 큽니다.<br>현재용량 : '+fileSize.toFixed(2)+' MB, 최대용량 : '+max+' MB','error');
				return false;
			}
		}
		
		if ( totSize >= totMaxSize ) {
			parent.$.messager.alert('','파일의 총용량이 100MB보다 큽니다.<br>100MB 이하로 선택해 주세요.<br>현재용량 : '+(totSize/1024/1024).toFixed(2)+' MB','error');
			totSize = 0;
			return false;
		} else {
			totSize = 0;
		}
		
	}
	
	var formData = new FormData($("#submit_form")[0]);
	
	if ( file_seq != null && file_seq != '' ) {
		let fileIdx = index.split(",");
		let fileSeq = file_seq.split(",");
		let fileNm = file_nm.split(",");
		let orgFileNm = org_file_nm.split(",");
		// 파일 삭제
		$.ajax({
			type: "POST",
			url: "/BoardMng/Notice/deleteAttachFile",
			data: $("#file_form").serialize(),
			success: function(result) {
				if( result.result == false ) {
					// 삭제할 파일이 없을 시 다시생성.
					parent.$.messager.alert('',result.message,'error');
					
					for (var fileCnt = 0; fileCnt < fileIdx.length; fileCnt++) {
						
						$("#file_area").append("<span id=fileArea"+fileIdx[fileCnt]+">" +
								"<a href='javascript:;' onClick=Btn_downFile('"+fileNm[fileCnt]+"','"+orgFileNm[fileCnt].replace(/ /g,":").replace(/\,/g,"*")+"');>"+orgFileNm[fileCnt].replace(/\:/g," ").replace(/\*/g,",")+"</a>&nbsp;"+
								"<a href='javascript:;' onClick=deleteFile(this,'"+fileSeq[fileCnt]+"','"+fileNm[fileCnt]+"','"+orgFileNm[fileCnt].replace(/ /g,":").replace(/\,/g,"*")+"','"+$('#auth_cd').val()+"','"+$('#reg_id').val()+
								"','"+$('#user_id').val()+"','"+fileIdx[fileCnt]+"');><i class='btnIcon fas fa-trash-alt'></i></a></span>");
						$("#file_area").append(" ");
					}
					
					$("#file_form #file_nm").val("");
					$("#file_form #file_seq").val("");
					$("#file_form #org_file_nm").val("");
					index = 0;
					
					return false;
				} else {
					//parent.$.messager.alert('',result.message,'info');
					
					ajaxMultiCall("/Board/UpdateAction",
							formData,
							"POST",
							callback_updateAction);
				}
			}
		});
	} else {
		ajaxMultiCall("/Board/UpdateAction",
				formData,
				"POST",
				callback_updateAction);
	}
}

//callback 함수
var callback_updateAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 수정되었습니다','info',function(){
			setTimeout(function() {
				Btn_goView();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message,'error',function(){
			
		});
	}
};	


function Btn_downFile(fileNm, orgFileNm){
	$("#file_nm").val(fileNm);
	
	$("#org_file_nm").val(orgFileNm.replace(/\:/g," ").replace(/\*/g,","));
	
	$("#submit_form").attr("action","/Board/DownFile");
	$("#submit_form").submit();
}

function deleteFile(obj,file_seq,file_nm, org_file_nm, auth_cd, reg_user_id, user_id, idx){
	let orgFileNm = "";
	
	if(auth_cd == 'ROLE_ITMANAGER' || auth_cd == 'ROLE_TUNER' || auth_cd == 'ROLE_DEV' || auth_cd == 'ROLE_DEV_DEPLOYMANAGER' || auth_cd == 'ROLE_DEPLOYMANAGER'){
		parent.$.messager.alert('','권한이 없습니다.','error');
		return false;
	}
	
	orgFileNm = org_file_nm.replace(/ /g,":").replace(/\,/g,"*");
	
	console.log(org_file_nm+" , "+orgFileNm);
	
	if ( $("#file_form #file_nm").val() != null && $("#file_form #file_nm").val() != '' ) {
		$("#file_form #file_nm").val($("#file_form #file_nm").val()+","+file_nm);
		$("#file_form #file_seq").val($("#file_form #file_seq").val()+","+file_seq);
		$("#file_form #org_file_nm").val($("#file_form #org_file_nm").val()+","+orgFileNm);
		
		index = index+","+idx
	} else {
		$("#file_form #file_nm").val(file_nm);
		$("#file_form #file_seq").val(file_seq);
		$("#file_form #org_file_nm").val(orgFileNm);
		index = idx;
	}
	console.log("index = "+index +" , "+$("#file_form #file_nm").val()+" , "+$("#file_form #file_seq").val() +" , "+$("#file_form #org_file_nm").val());
	$(obj).parent().remove();
	
}
function Btn_goList(){
	$("#submit_form #uploadFile").filebox('clear');
	$("#submit_form").attr("action","/Board/List");
	$("#submit_form").submit();	
}

function Btn_goView(){
	$("#submit_form #uploadFile").filebox('clear');
	$("#submit_form").attr("action","/Board/View");
	$("#submit_form").submit();	
}