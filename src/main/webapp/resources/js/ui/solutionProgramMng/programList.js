var selectedRow = "";
var old_contents_id = "";
var contents_id = "";
$(document).ready(function() {
	$("body").css("visibility", "visible");

/*	$('#parent_contents_id').combotree({
		idField:'id',
		treeField:'text',
		url:"/SolutionProgramMng/ProgramListTree?isChoice=menu",
		method:"POST",
		onSelect : function (rows){
			selectedRow = rows;
		},
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});*/

	
	$('#submit_form #select_slt_program_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1074&isChoice=X",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect: function(rec){
			var slt_program_div_cd = rec.cd;
			console.log(rec);
			$("#submit_form #slt_program_div_cd").val(slt_program_div_cd);
			if(slt_program_div_cd != undefined && slt_program_div_cd != ''){
				
				$('#select_contents_tree').combotree({      //메뉴관리에서 콤보메뉴트리
					idField:'id',
					treeField:'text',
					url:"/SolutionProgramMng/ProgramListTree?slt_program_div_cd="+slt_program_div_cd,
					method:"POST",
					onSelect : function (rows){
						selectedRow = rows;
					},
					onLoadError:function() {
						$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
					},
					onLoadSuccess: function() {
						$("#select_contents_tree").combobox("textbox").attr("placeholder","선택");
						$("#select_contents_tree").combobox("textbox").attr("required","true");
					}
				});
				
				Btn_SearchProgramListMenuTree(slt_program_div_cd);
				Btn_TableListTree();
			}
		},
		onLoadSuccess: function(data){
			$('#submit_form #select_slt_program_div_cd').combobox('textbox').attr("placeholder","선택");
		},
		onLoadError: function(){
			$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		}
	});
	
	
	//솔루션프로그램구분
	$('#detail_form #slt_program_div_cd').combobox({
		url:"/Common/getCommonCode?grp_cd_id=1074&isChoice=X",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onSelect : function(rec){
			Btn_SearchProgramListMenuTree(rec.cd);
		},
		onLoadError: function(){
			$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#detail_form #slt_program_div_cd').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #exadata_contents_yn').combobox('textbox').attr("placeholder","선택");
			$('#detail_form #use_yn').combobox('textbox').attr("placeholder","선택");
		}
	});
	
	
	$("#tableList").treegrid({
		idField:'id',
		treeField:'text',
		rownumbers: true,
		lines: true,	
		selectOnCheck : false,
		checkOnSelect : false,
		onCheckAll : function(){
			setCheckDetail();
		},
		onUncheckAll : function(){
			setCheckDetail();
		},
		onCheck:function(index, row){
			setCheckDetail();
		},
		onUncheck:function(){
			setCheckDetail();
		},
		onClickCell : function(index,row) {
			setDetailView(row);
//			ajaxCallSelectedMenuAuth(row.menu_id);
		},	
//		onLoadSuccess: function(row){
			/*$("#detail_form #menu_id").textbox({disabled: true});
			$(this).treegrid('enableDnd', row?row.id:null);*/
//		},
		columns:[[		
			{field:'rnum',hidden:"true"},
			{field:'id',hidden:"true"}, //contents_id
			{field:'level',hidden:"true"},
			{field:'contents_name',hidden:"true"}, //text, contents_name
			{field:'parent_contents_id',hidden:"true"},
			{field:'slt_program_div_cd',hidden:"true"},
			{field:'chk',checkbox:"true"}, 
			{field:'slt_program_div_id',hidden:true},
			{field:'slt_program_div_nm',title:'솔루션프로그램구분',width:"12%",halign:"center",align:'center'},
			{field:'contents_id',title:'목차ID',width:"6%",halign:"center",align:'center'},
			{field:'text',title:'목차명',width:"28%",halign:"center",align:'left'},
			{field:'contents_desc',title:'설명',width:"16%",halign:"center",align:'left'},
			{field:'contents_url_addr',title:'호출 URL주소',width:"16%",halign:"center",align:'left'},
			{field:'exadata_contents_yn',title:'Exadata여부',width:"8%",halign:"center",align:'center'},
			{field:'contents_ordering',title:'정렬순서',width:"6%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"6%",halign:"center",align:'center'}
		]],
    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onLoadSuccess:function(data,data2){
    		
    	}
	});
	

	$("#detail_form #contents_id").textbox({disabled:true});
	/*$("#detail_form #slt_program_div_nm").textbox({disabled:true});
	$("#detail_form #parent_contents_id").textbox({disabled:true});
	$("#detail_form #contents_name").textbox({disabled:true});
	$("#detail_form #contents_desc").textbox({disabled:true});
	$("#detail_form #contents_url_addr").textbox({disabled:true});
	$("#detail_form #exadata_contents_yn").textbox({disabled:true});
	$("#detail_form #contents_url_addr").textbox({disabled:true});
	$("#detail_form #contents_ordering").textbox({disabled:true});*/
	
});

function setCheckDetail(){
	var rows = $('#tableList').datagrid('getChecked');
	if(rows.length > 0){  //여러개 체크 됐을 시 승인버튼만 활성화
		$("#detail_form #contents_id").textbox({disabled:true});
		$("#detail_form #contents_name").textbox({disabled:true});
		$("#detail_form #contents_desc").textbox({disabled:true});
		$("#detail_form #contents_url_addr").textbox({disabled:true});
		$("#detail_form #exadata_contents_yn").combobox({disabled:true});
		$("#detail_form #contents_url_addr").textbox({disabled:true});
		$("#detail_form #contents_ordering").textbox({disabled:true});
		$("#detail_form #deleteButton").linkbutton({disabled:true});

	}else{  //체크를 하지않았을시
		$("#detail_form #contents_id").textbox("textbox").css("background-color","#F2F3EF");
		$("#detail_form #contents_id").textbox("textbox").css("color","#a7a7a6");
		$("#detail_form #contents_id").textbox({disabled:false});
		$("#detail_form #contents_name").textbox({disabled:false});
		$("#detail_form #contents_desc").textbox({disabled:false});
		$("#detail_form #contents_url_addr").textbox({disabled:false});
		$("#detail_form #exadata_contents_yn").combobox({disabled:false});
		$("#detail_form #contents_url_addr").textbox({disabled:false});
		$("#detail_form #contents_ordering").textbox({disabled:false});
		$("#detail_form #deleteButton").linkbutton({disabled:false});


	}

//	$("#checkUserId").linkbutton({disabled:true});
};

function Btn_OnClick(){
	if(($('#select_slt_program_div_cd').combobox('getValue') == "")){
		$.messager.alert('','솔루션프로그램구분을 선택해 주세요.');
		return false;
	}
	if(($('#select_contents_tree').combotree('getValue') == "")){
		$.messager.alert('','솔루션프로그램 목차를 선택해 주세요.');
		return false;
	}
	
	changeRowCss(selectedRow.contents_id);
	
	setDetailView(selectedRow);
//	ajaxCallSelectedMenuAuth(selectedRow.menu_id);
};

function changeRowCss(newid){
		contents_id = newid;
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+contents_id).css("background-color","#E7E7E7");
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+contents_id).css("color","#165CBB");
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+old_contents_id).css("background-color","#FFFFFF");
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+old_contents_id).css("color","#000000");
		old_contents_id = contents_id;

};
/*//메뉴아이디를 파라미터로 전달
function ajaxCallSelectedMenuAuth(menu_id){
	$("#detail_form #menu_id").textbox("setValue", menu_id);

	ajaxCall("/getAuthNm",
			$("#detail_form"),
			"POST",
			callback_SelectedMenuAuthAction);
}
var callback_SelectedMenuAuthAction = function(result) {
	var store;
	var data = JSON.parse(result);
	for(var i=0;i<data.length;i++){
		console.log("data.menu_id=============>"+data[i].menu_id);
		if(data[i].menu_id != ""){
			$("input[id='auth_id"+data[i].auth_id+"']:checkbox").prop("checked", "checked");
		}
	}
}
*/

function Btn_SearchProgramListMenuTree(slt_program_div_cd){
	$('#parent_contents_id').combotree({      //오른쪽 메뉴관리에서 콤보메뉴트리
		url:"/SolutionProgramMng/ProgramListTree?isChoice=menu&slt_program_div_cd="+slt_program_div_cd,
		method:"POST"
	});
	
}


function Btn_TableListTree(){
	$('#tableList').treegrid('loadData',[]);

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	/* modal progress open */
	if(openMessageProgress != undefined) openMessageProgress("솔루션 프로그램 목차 관리"," ");
	//if(parent.openMessageProgress != undefined) parent.openMessageProgress("솔루션 프로그램 목차 관리"," ");
	
	ajaxCall("/SolutionProgramMng/ProgramListTree",
			$("#submit_form"),
			"POST",
			callback_SolutionProgramMngTreeAction);	
}

//callback 함수
var callback_SolutionProgramMngTreeAction = function(result) {
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').treegrid("loadData", data);
	}
	
	/* modal progress close */
	if(closeMessageProgress != undefined) closeMessageProgress();	
};

function Btn_SaveSettingAction(){
	var isPassed = false;
	var rows = $('#tableList').datagrid('getChecked');
	
		//단일 메뉴 수정/저장
		if(rows.length == 0){
			var crud_flag = $("#detail_form #crud_flag").val();
			
			
			if($("#detail_form #slt_program_div_cd").combobox('getValue') == ""){
				$.messager.alert('','솔루션프로그램구분을 선택해 주세요.');
				return false;
			}
//			if($("#contents_id").textbox('getValue') == ""){
//				parent.$.messager.alert('','목차ID를 입력해 주세요.');
//				return false;
//			}
			if($("#contents_name").textbox('getValue') == ""){
				$.messager.alert('','목차명 입력해 주세요.');
				return false;
			}
			/*if($("#contents_desc").textbox('getValue') == ""){
				parent.$.messager.alert('','설명을 입력해 주세요.');
				return false;
			}
			if($("#contents_url_addr").textbox('getValue') == ""){
				parent.$.messager.alert('','호출URL주소를 입력해 주세요.');
				return false;
			}*/
			
			if($("#exadata_contents_yn").combobox('getValue') == ""){
				$.messager.alert('','Exadata목차여부를 선택해 주세요.');
				return false;
			}
			if($("#contents_ordering").textbox('getValue') == ""){
				$.messager.alert('','정렬순서를 입력해 주세요.');
				return false;
			}
			if($("#use_yn").textbox('getValue') == ""){
				$.messager.alert('','사용여부를 선택해 주세요.');
				return false;
			}

			//최상위 메뉴 확인
			if($("#detail_form #parent_contents_id").combobox('getValue') == ''){
				$.messager.confirm('', '최상위목차로 변경합니다. 변경하시겠습니까?', function(check) {
					if (!check) {
						isPassed = false;
						return false;
						
					}else{
//						$("#detail_form #parent_contents_id").val($("#detail_form #parent_contents_id").combotree("getValue"));
						ajaxCall("/SolutionProgramMng/ProgramList/Save",$("#detail_form"),"POST",callback_SaveSolutionProgramListMngAction);
					}
				});
				
			}else{
				isPassed = true;
			}
			
			if(isPassed){
				$("#detail_form #parent_contents_id").val($("#detail_form #parent_contents_id").combotree("getValue"));
				ajaxCall("/SolutionProgramMng/ProgramList/Save",$("#detail_form"),"POST",callback_SaveSolutionProgramListMngAction);
			}
			
			
		}else{
		//체크박스 1개이상 여러개의 메뉴 수정
			
			if($("#detail_form #slt_program_div_cd").combobox('getValue') == ""){
				$.messager.alert('','솔루션프로그램구분을 선택해 주세요.');
				return false;
			}
			if($("#use_yn").textbox('getValue') == ""){
				$.messager.alert('','사용여부를 선택해 주세요.');
				return false;
			}
			
			var contents_id_list = new Array();
			var contents_ordering_list = new Array();
			
			for(var i = 0; i < rows.length; i++){
				contents_id_list.push(rows[i].contents_id);
				contents_ordering_list.push(rows[i].contents_ordering);
			}
			$.messager.confirm('', '선택된 체크박스목차 모두 업데이트 하시겠습니까?', function(check) {
				if (check) {
					
					//최상위 메뉴 확인
					if($("#detail_form #parent_contents_id").combobox('getValue') == ''){
						$.messager.confirm('', '최상위목차로 변경합니다. 변경하시겠습니까?', function(check) {
							if (!check) {
								isPassed = false;
								return false;
								
							}else{
								$("#detail_form #contents_id_list").val(contents_id_list.join(","));
								$("#detail_form #contents_ordering_list").val(contents_ordering_list.join(","));
								ajaxCall("/SolutionProgramMng/ProgramList/SaveMulti",$("#detail_form"),"POST",callback_SolutionProgramListMng);
							}
						});
						
					}else{
						isPassed = true;
					}
					
					if(isPassed){
						$("#detail_form #contents_id_list").val(contents_id_list.join(","));
						$("#detail_form #contents_ordering_list").val(contents_ordering_list.join(","));
						ajaxCall("/SolutionProgramMng/ProgramList/SaveMulti",$("#detail_form"),"POST",callback_SolutionProgramListMng);		
					}
				}
			});
		}
}
var callback_SaveSolutionProgramListMngAction = function(result) {

	if(result.result){
		$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_TableListTree();
				Btn_SearchProgramListMenuTree();
				},1000);
		});
	}else{
		$.messager.alert('',result.message);
	}
};

var callback_SolutionProgramListMng = function(result) {
//	var data = JSON.parse(result);

	$("#detail_form #crud_flag").val("U");
	
	if(result.result){
		$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_TableListTree();
				Btn_SearchProgramListMenuTree();
			},1000);
		});
	}else{
		$.messager.alert('',result.message);
	}
};

function Btn_DeleteSettingAction(){
//	var rows = $('#tableList').datagrid('getChecked');

	if($("#contents_id").textbox('getValue') == ""){
		$.messager.alert('','목차를 선택해 주세요.');
		return false;
	}
	
		$.messager.confirm('','[ 목차명 : '+$("#contents_name").textbox('getValue')+' ] 을(를)'+' 삭제하시겠습니까?',function(check){
			if(check){
				ajaxCall("/SolutionProgramMng/ProgramList/Delete",
						$("#detail_form"),
						"POST",
						callback_DeleteSolutionListMng);			
			}
		});
	
	
	
}
//callback 함수
var callback_DeleteSolutionListMng = function(result) {
	if(result.result){
		$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_TableListTree();
				Btn_SearchProgramListMenuTree();
				},1000);
		});
	}else{
		$.messager.alert('',result.message);
	}
};





function setDetailView(selRow){
	
	console.log(selRow);
	setCheckDetail();
	$("#detail_form #slt_program_div_cd").combobox("setValue", selRow.slt_program_div_cd);
	$("#detail_form #slt_program_div_cd").combobox("readonly", true);
	$("#detail_form #contents_name").textbox("setValue", selRow.contents_name);	
	$("#detail_form #contents_id").textbox("setValue", selRow.contents_id);
	
	if(selRow.parent_contents_id != "-1"){ //상위메뉴가 있다면
		$("#detail_form #parent_contents_id").combotree("setValue", selRow.parent_contents_id);
	}else{
		$("#detail_form #parent_contents_id").combotree("setValue", "");
	}

	$("#detail_form #contents_desc").textbox("setValue", selRow.contents_desc);
	$("#detail_form #contents_url_addr").textbox("setValue", selRow.contents_url_addr);
	$("#detail_form #exadata_contents_yn").combobox("setValue", selRow.exadata_contents_yn);
	$("#detail_form #contents_ordering").textbox("setValue", selRow.contents_ordering);
	$("#detail_form #use_yn").combobox("setValue", selRow.use_yn);
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #contents_id").textbox({readonly:true});
	$("#detail_form #contents_id").textbox({disabled: false});

	$("#detail_form #contents_id").textbox("textbox").css("background-color","#F2F3EF");
	$("#detail_form #contents_id").textbox("textbox").css("color","#a7a7a6");

}



function Btn_ResetField(){
	$("#detail_form #slt_program_div_cd").combobox("setValue", "");
	$("#detail_form #slt_program_div_cd").combobox("readonly", false);
	$("#detail_form #contents_name").textbox("setValue", "");	
	$("#detail_form #contents_id").textbox("setValue", "");
	$("#detail_form #parent_contents_id").combotree("setValue", "");
	$("#detail_form #contents_desc").textbox("setValue", "");
	$("#detail_form #contents_url_addr").textbox("setValue", "");
	$("#detail_form #exadata_contents_yn").combobox("setValue", "");
	$("#detail_form #contents_ordering").textbox("setValue", "");
	$("#detail_form #use_yn").combobox("setValue", "");
	$("#detail_form #crud_flag").val("U");
	$("#detail_form #contents_id").textbox({readonly:true});
	$("#detail_form #contents_id").textbox({disabled: false});

	$("#detail_form #contents_id").textbox("textbox").css("background-color","#F2F3EF");
	$("#detail_form #contents_id").textbox("textbox").css("color","#a7a7a6");

	
	$('#tableList').datagrid('clearSelections');
	$('#tableList').datagrid('clearChecked');

	
	$("#detail_form #crud_flag").val("C");
	setCheckDetail();

	$(".datagrid-btable").find("#datagrid-row-r3-2-"+contents_id).css("background-color","#FFFFFF");
	$(".datagrid-btable").find("#datagrid-row-r3-2-"+contents_id).css("color","#000000");
}
