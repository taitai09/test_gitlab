var chkFixYn = false;
$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	autoSizeTextarea('controversialist','impr_sbst');
	autoSizeTextarea('wrkjob_peculiar_point','request_why');
	
	//개선전 SQL
	/*var clipImprSql = new Clipboard('#sqlCopyBtn',{
    	text: function() {
            return $("#sql_text_h").val();
        }
    });*/
	var clipboard = new Clipboard('#sqlCopyBtn');
    clipboard.on('success', function(e) {
    	parent.$.messager.alert('','개선전 SQL이 복사되었습니다.');
    });
    
    
    
    //개선후 SQL
    var clipImprSql = new Clipboard('#imprSqlCopyBtn',{
    	text: function() {
            return $("#impr_sql_text_h").val();
        }
    });    
    clipImprSql.on('success', function(e) {
    	parent.$.messager.alert('','개선후 SQL이 복사되었습니다.');
    });
    
    
    //개선전 실행계획
    var clipImprbExec = new Clipboard('#imprbExecCopyBtn',{
    	text: function() {
            return $("#imprb_exec_plan_h").val();
        }
    });    
    clipImprbExec.on('success', function(e) {
    	parent.$.messager.alert('','개선전 실행계획이 복사되었습니다.');
    });
    
    //개선후 실행계획
    var clipImpraExec = new Clipboard('#impraExecCopyBtn',{
    	text: function() {
            return $("#impra_exec_plan_h").val();
        }
    });    
    clipImpraExec.on('success', function(e) {
    	parent.$.messager.alert('','개선후 실행계획이 복사되었습니다.');
    });    
    
/*	$("#historyViewTab").tabs({
		plain: true,
		onSelect: function(title,index){
			 탭을 클릭시 화면을 높이 자동 조절 
			var height = $("#container").height();
			parent.resizeTopFrame($("#menu_id").val(), height);  
		}
	});
	*/
	if($("#guide_div_cd").val() == "2"){
	  /*CKEDITOR.replace("sql_text",{
			width:"99%",
			height:"300px",
			contentsCss: "body {font-size:11px;}",
			extraPlugins: 'colorbutton,colordialog,font'
		});*/
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
			height:"520px",
			contentsCss: "body {font-size:12px;}",
		extraPlugins: 'colorbutton,colordialog,font'
		});		
	}
	
	//인덱스 이력 조회
	getSQLTuningIndexHistoryActionTable();
	
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
			ajaxCall("/PrecedentNew/DeleteAction",
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
	
	if( $("#uploadFile").textbox('getValue') == ""){
		formData.append('uploadFile',"");
	}
	
		ajaxMultiCall("/PrecedentNew/UpdateAction",
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
	$("#submit_form").attr("action","/PrecedentNew");
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
	$("#submit_form").attr("action","/PrecedentNew/DownFile");
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
		url: "/PrecedentNew/DeleteGuideFile",
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


//textarea auto size 조절
function autoSizeTextarea(elementId1, elementId2){
	var obj1 = document.getElementById(elementId1);
	var obj2 = document.getElementById(elementId2);

	if(obj1 != undefined && obj1 != 'undefined' && obj1 != null && obj2 != undefined && obj2 != 'undefined' && obj2 != null){
		var size1 = obj1.scrollHeight;
		var size2 = obj2.scrollHeight;
	
		if(size1 > size2){
			startChangesize1(obj1, 30, obj2);
		}else{
			startChangeSize2(obj2, 30, obj1);
		}
	}
}






/*//업무특이사항부터 조절
function startendChangesize1(obj, bsize) { // 객체명, 기본사이즈
    var sTextarea = document.getElementById(obj);
    var csize = (sTextarea.scrollHeight >= bsize) ? sTextarea.scrollHeight+"px" : bsize+"px";
    sTextarea.style.height = bsize+"px"; 
    sTextarea.style.height = csize;

	endChangesize1("request_why", csize);
}

function endChangesize1(obj, bsize) { // 객체명, 기본사이즈
	var sTextarea = document.getElementById(obj);
	sTextarea.style.height = bsize+"px"; 
	sTextarea.style.height = bsize;
	
}

//요청사유 부터 조절
function startendChangeSize2(obj, bsize) { // 객체명, 기본사이즈
    var sTextarea = document.getElementById(obj);
    var csize = (sTextarea.scrollHeight >= bsize) ? sTextarea.scrollHeight+"px" : bsize+"px";
    sTextarea.style.height = bsize+"px"; 
    sTextarea.style.height = csize;

	endChangeSize2("wrkjob_peculiar_point", csize);
}

function endChangeSize2(obj, bsize) { // 객체명, 기본사이즈
	var sTextarea = document.getElementById(obj);
	sTextarea.style.height = bsize+"px"; 
	sTextarea.style.height = bsize;
}
 */





//(1-1)요청사유 부터 조절
function startChangesize1(obj, bsize, obj2) { // 객체명, 기본사이즈
 // var sTextarea = document.getElementById(obj);
  
  var csize = "";
  	csize = (obj.scrollHeight >= bsize) ? obj.scrollHeight+"px" : bsize+"px";
  	obj.style.height = bsize+"px"; 
  	obj.style.height = csize;
  	
  	endChangesize1(obj2, csize);
}
//(2-1)
function endChangesize1(obj, bsize) { // 객체명, 기본사이즈
	//var sTextarea = document.getElementById(obj);
//	var csize = (sTextarea.scrollHeight >= bsize) ? sTextarea.scrollHeight+"px" : bsize+"px";
	obj.style.height = bsize+"px"; 
	obj.style.height = bsize;
	
}


//(2-1)업무특이사항 부터 조절
function startChangeSize2(obj, bsize, obj2) { // 객체명, 기본사이즈
	console.log(obj);
	  //var sTextarea = document.getElementById(obj);
		var csize = "";
		csize = (obj.scrollHeight >= bsize) ? obj.scrollHeight+"px" : bsize+"px";
		obj.style.height = bsize+"px"; 
		obj.style.height = csize;
		
		endChangeSize2(obj2, csize);
}
//(2-2)
function endChangeSize2(obj, bsize) { // 객체명, 기본사이즈
	//var sTextarea = document.getElementById(obj);
//	var csize = (sTextarea.scrollHeight >= bsize) ? sTextarea.scrollHeight+"px" : bsize+"px";
	obj.style.height = bsize+"px"; 
	obj.style.height = bsize;
	
	
}




function getSQLTuningIndexHistoryActionTable(){
	//index 내역
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
		},			
		columns:[[
			{field:'tuning_no',hidden:"true"},
			{field:'index_impr_type_nm',title:'개선유형',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'인덱스명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_column_name',title:'인덱스컬럼',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'before_index_column_name',title:'변경전 인덱스컬럼',width:"30%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

	ajaxCall("/SqlTuningIndexHistoryAction",
		$("#submit_form"),
		"POST",
		callback_SQLTuningIndexHistoryAction2);	
}

//인덱스 이력 콜백 함수
var callback_SQLTuningIndexHistoryAction2 = function(result) {
	//$('#tableList').datagrid('loadData',[]);
	var data = JSON.parse(result);
	var rowCnt = data.totalCount;

	if(rowCnt > 0){
		$("#indexTable").show();
	}else{
		$("#indexTable").hide();
	}
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').datagrid("loadData", data);
	}	
};