var selectedRow = "";
var old_menu_id = "";
var menu_id = "";
$(document).ready(function() {

	$("body").css("visibility", "visible");
	$('#selectMenu').combotree({		//메뉴관리에서 콤보메뉴트리
		idField:'id',
		treeField:'text',
		url:"/MenuTree",
		method:"POST",
		onSelect : function (rows){
			selectedRow = rows;
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(rows, asdf){
//			console.log("success::",asdf);
			$('#selectMenu').combobox("textbox").attr("placeholder","선택");
		}
	});
	
	$('#use_yn').combobox("textbox").attr("placeholder","선택");
	var crud_flag = $("#detail_form #crud_flag").val();
//	if(crud_flag == "U"){
//		$("#menu_id").attr("required","true");
//	}
	
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
			ajaxCallSelectedMenuAuth(row.menu_id);
		},	
//		onLoadSuccess: function(row){
			/*$("#detail_form #menu_id").textbox({disabled: true});
			$(this).treegrid('enableDnd', row?row.id:null);*/
//		},
		columns:[[		
			{field:'rnum',hidden:"true"},
			{field:'id',hidden:"true"},
			{field:'parent_id',hidden:"true"},
			{field:'chk',checkbox:"true"}, 
			{field:'auth_id',hidden:"true"},
			{field:'menu_id',title:'메뉴코드',width:"6%",halign:"center",align:'center'},
			{field:'text',title:'메뉴명',width:"28%",halign:"center",align:'left'},
			{field:'menu_desc',title:'설명',width:"15%",halign:"center",align:'left'},
			{field:'menu_url_addr',title:'호출 URL주소',width:"28%",halign:"center",align:'left'},
			{field:'menu_image_nm',title:'이미지 정보',width:"10%",halign:"center",align:'left'},
			{field:'menu_ordering',title:'정렬순서',width:"6%",halign:"center",align:'center'},
			{field:'use_yn',title:'사용여부',width:"6%",halign:"center",align:'center'}
		]],
//    	fitColumns:true,
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onLoadSuccess:function(data,data2){
    		
//    		 var rows = $(this).datagrid('getChecked');
// 		    for(i=0; i<rows.length; i++){
// 		        $(this).datagrid('checkRow',i);
// 		        $(this).datagrid('uncheckRow',i);
// 		        $(this).datagrid('uncheckRow',i+1);
// 		        $(this).datagrid('uncheckRow',i-1);
// 		    }
    		/*$('#tableList').datagrid('clearSelections');
    		$('#tableList').datagrid('clearChecked');*/
    	}
	});
	
	Btn_SearchMenuTree();
	Btn_SearchMenu();
//	$("#detail_form #menu_id").textbox("textbox").css("background-color","#F2F3EF");

	$("#detail_form #menu_id").textbox({disabled:true});
	$("#detail_form #menu_nm").textbox({disabled:true});
	$("#detail_form #menu_desc").textbox({disabled:true});
	$("#detail_form #menu_url_addr").textbox({disabled:true});
	$("#detail_form #menu_image_nm").textbox({disabled:true});
	$("#detail_form #menu_url_addr").textbox({disabled:true});
	$("#detail_form #menu_ordering").textbox({disabled:true});
//	$("#detail_form #deleteButton").linkbutton({disabled:true});
	
});

function setCheckDetail(){
	var rows = $('#tableList').datagrid('getChecked');
	if(rows.length > 0){  //여러개 체크 됐을 시 승인버튼만 활성화
		$("#detail_form #menu_id").textbox({disabled:true});
		$("#detail_form #menu_nm").textbox({disabled:true});
		$("#detail_form #menu_desc").textbox({disabled:true});
		$("#detail_form #menu_url_addr").textbox({disabled:true});
		$("#detail_form #menu_image_nm").textbox({disabled:true});
		$("#detail_form #menu_url_addr").textbox({disabled:true});
		$("#detail_form #menu_ordering").textbox({disabled:true});
		$("#detail_form #deleteButton").linkbutton({disabled:true});

	}else{  //체크를 하지않았을시
		$("#detail_form #menu_id").textbox({disabled:false});
		$("#detail_form #menu_id").textbox("textbox").css("background-color","#F2F3EF");
		$("#detail_form #menu_id").textbox("textbox").css("color","#a7a7a6");
		$("#detail_form #menu_nm").textbox({disabled:false});
		$("#detail_form #menu_desc").textbox({disabled:false});
		$("#detail_form #menu_url_addr").textbox({disabled:false});
		$("#detail_form #menu_image_nm").textbox({disabled:false});
		$("#detail_form #menu_url_addr").textbox({disabled:false});
		$("#detail_form #menu_ordering").textbox({disabled:false});
		$("#detail_form #deleteButton").linkbutton({disabled:false});


	}

	$("#checkUserId").linkbutton({disabled:true});
};

function Btn_OnClick(){
	if(($('#selectMenu').combotree('getValue') == "")){
		parent.$.messager.alert('','검색란을 선택해 주세요.');
		return false;
	}

	//	  $("#tableList").treegrid('unselectRow', clickedIndex);
	
	changeRowCss(selectedRow.menu_id);
	
	setDetailView(selectedRow);
	ajaxCallSelectedMenuAuth(selectedRow.menu_id);
};

function changeRowCss(newid){
	 	menu_id = newid; 
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+menu_id).css("background-color","#E7E7E7");
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+menu_id).css("color","#165CBB")
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+old_menu_id).css("background-color","#FFFFFF");
		$(".datagrid-btable").find("#datagrid-row-r3-2-"+old_menu_id).css("color","#000000")
		old_menu_id = menu_id;

};
//메뉴아이디를 파라미터로 전달
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
		//console.log("data.menu_id=============>"+data[i].menu_id);
		if(data[i].menu_id != ""){
			$("input[id='auth_id"+data[i].auth_id+"']:checkbox").prop("checked", "checked");
			
		}else {
			$("input[id='auth_id"+data[i].auth_id+"']:checkbox").prop("checked", false);
		}
	}
}

function Btn_SearchMenuTree(){
	
	$('#parent_menu_id').combotree({      //메뉴관리에서 콤보메뉴트리
		url:"/getMenuList",
		method:"get"
	});
	
}


function Btn_SearchMenu(){
	$('#tableList').treegrid('loadData',[]);

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("메뉴 관리"," ");
	
	ajaxCall("/MenuTree",
			$("#submit_form"),
			"POST",
			callback_MenuTreeAction);	
}

//callback 함수
var callback_MenuTreeAction = function(result) { 
	var data = JSON.parse(result);
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').treegrid("loadData", data);
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
};

function Btn_saveMenuInfo(){
	var isPassed = false;
	var rows = $('#tableList').datagrid('getChecked');
	
		//단일 메뉴 수정/저장
		if(rows.length == 0){
			var crud_flag = $("#detail_form #crud_flag").val();
			
			if(crud_flag == "U"){
				if($("#detail_form #menu_id").textbox('getValue') == ""){
					parent.$.messager.alert('','메뉴코드를 입력해 주세요.');
					return false;
				}
			}
			
			if($("#menu_nm").textbox('getValue') == ""){
				parent.$.messager.alert('','메뉴명 입력해 주세요.');
				return false;
			}
			if($("#menu_desc").textbox('getValue') == ""){
				parent.$.messager.alert('','설명을 입력해 주세요.');
				return false;
			}
			
			if($("#menu_ordering").textbox('getValue') == ""){
				parent.$.messager.alert('','정렬순서를 입력해 주세요.');
				return false;
			}
			if($("#menu_ordering").textbox('getValue') == ""){
				parent.$.messager.alert('','정렬순서를 입력해 주세요.');
				return false;
			}
			if($("#use_yn").combobox('getValue') == ""){
				parent.$.messager.alert('','사용여부를 선택해 주세요.');
				return false;
			}

			//최상위 메뉴 확인
			if($("#detail_form #parent_menu_id").combobox('getValue') == ''){
				parent.$.messager.confirm('', '최상위메뉴로 변경합니다. 변경하시겠습니까?', function(check) {
					if (!check) {
						isPassed = false;
						return false;
						
					}else{
						$("#detail_form #parent_menu_id").val($("#detail_form #parent_menu_id").combotree("getValue"));
						ajaxCall("/Menu/Save",$("#detail_form"),"POST",callback_saveMenuInfo);
					}
				});
				
			}else{
				isPassed = true;
			}
			
			if(isPassed){
				$("#detail_form #parent_menu_id").val($("#detail_form #parent_menu_id").combotree("getValue"));
				ajaxCall("/Menu/Save",$("#detail_form"),"POST",callback_saveMenuInfo);
			}
			
			
		}else{
			
		//체크박스 1개이상 여러개의 메뉴 수정
			var menu_id_list = new Array();
			var menu_ordering_list = new Array();
			
			for(var i = 0; i < rows.length; i++){
				menu_id_list.push(rows[i].menu_id);
				menu_ordering_list.push(rows[i].menu_ordering);
			}
			parent.$.messager.confirm('', '선택된 체크박스메뉴 모두 업데이트 하시겠습니까?', function(check) {
				if (check) {
					
					//최상위 메뉴 확인
					if($("#detail_form #parent_menu_id").combobox('getValue') == ''){
						parent.$.messager.confirm('', '최상위메뉴로 변경합니다. 변경하시겠습니까?', function(check) {
							if (!check) {
								isPassed = false;
								return false;
								
							}else{
								$("#detail_form #menu_id_list").val(menu_id_list.join(","));
								$("#detail_form #menu_ordering_list").val(menu_ordering_list.join(","));
								ajaxCall("/Menu/MultiSave",$("#detail_form"),"POST",callback_multiSaveMenuInfo);
							}
						});
						
					}else{
						isPassed = true;
					}
					
					if(isPassed){
						$("#detail_form #menu_id_list").val(menu_id_list.join(","));
						$("#detail_form #menu_ordering_list").val(menu_ordering_list.join(","));
						ajaxCall("/Menu/MultiSave",$("#detail_form"),"POST",callback_multiSaveMenuInfo);		
					}
				}
			});
		}
}
var callback_saveMenuInfo = function(result) {

	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_SearchMenu();
				Btn_SearchMenuTree();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

var callback_multiSaveMenuInfo = function(result) {
//	var data = JSON.parse(result);

	$("#detail_form #crud_flag").val("U");
	
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_SearchMenu();
				Btn_SearchMenuTree();
			},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function Btn_deleteMenuInfo(){
	var rows = $('#tableList').datagrid('getChecked');

	if($("#menu_nm").textbox('getValue') == ""){
		parent.$.messager.alert('','메뉴를 선택해 주세요.');
		return false;
	}
	
//	if(rows.length == 0){
		parent.$.messager.confirm('','[ 메뉴명 : '+$("#menu_nm").textbox('getValue')+' ] 을(를)'+' 삭제하시겠습니까?',function(check){
			if(check){
				ajaxCall("/Menu/Delete",
						$("#detail_form"),
						"POST",
						callback_deleteMenuInfo);			
			}
		});
		
/*	}else{
		
		//체크박스 1개이상 여러개의 메뉴 수정
		var menu_id_list = new Array();
		
		for(var i = 0;
		i < rows.length; i++){
			menu_id_list.push(rows[i].menu_id);
		}
		$("#detail_form #menu_id_list").val(menu_id_list.join(","));
		parent.$.messager.confirm('','선택된 체크박스메뉴 모두를 정말로 삭제하시겠습니까?',function(check){
			if(check){
				ajaxCall("/Menu/MultiDelete",
						$("#detail_form"),
						"POST",
						callback_deleteMenuInfo);			
			}
		});
	}*/
	
	
	
}
//callback 함수
var callback_deleteMenuInfo = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {			
				Btn_ResetField();
				Btn_SearchMenu();
				Btn_SearchMenuTree();
				},1000);
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};





function setDetailView(selRow){
	
//	var rows = $('#tableList').datagrid('getChecked');
		
		setCheckDetail();
		$("input[name='auth_id']:checkbox").removeProp("checked");
		
		$("#detail_form #menu_nm").textbox("setValue", selRow.text);	
		$("#detail_form #menu_id").textbox("setValue", selRow.menu_id);
		
		if(selRow.parent_id != "-1"){ //상위메뉴가 있다면
	//		$("#detail_form #upper_wrkjob_cd").combotree("setValue", selRow.parent_id);
			$("#detail_form #parent_menu_id").combotree("setValue", selRow.parent_id);
		}else{
			$("#detail_form #parent_menu_id").combotree("setValue", "");
		}
	
	//	$("#detail_form #auth_id").datalist("setValue", selRow.auth_nm);
		$("#detail_form #menu_desc").textbox("setValue", selRow.menu_desc);
		$("#detail_form #menu_url_addr").textbox("setValue", selRow.menu_url_addr);
		$("#detail_form #menu_image_nm").textbox("setValue", selRow.menu_image_nm);
		$("#detail_form #menu_ordering").textbox("setValue", selRow.menu_ordering);
		$("#detail_form #use_yn").combobox("setValue", selRow.use_yn);
		$("#detail_form #crud_flag").val("U");
		$("#detail_form #menu_id").textbox({readonly:true});
		$("#detail_form #menu_id").textbox({disabled: false});
	
//	}
	$("#detail_form #menu_id").textbox("textbox").css("background-color","#F2F3EF");
	$("#detail_form #menu_id").textbox("textbox").css("color","#a7a7a6");

//	$("#detail_form #auth_id").combobox("setValue", selRow.auth_id);
	
	//$('#detail_form').textbox('textbox').css('background','gray');


//	setMenuInfo();
}



function Btn_ResetField(){
	$("#detail_form #parent_menu_id").combotree("setValue","");
	$("#detail_form #menu_id").textbox("setValue","");
	$("#detail_form #menu_nm").textbox("setValue","");
	$("#detail_form #menu_desc").textbox("setValue", "");
	$("#detail_form #menu_url_addr").textbox("setValue", "");
	$("#detail_form #menu_image_nm").textbox("setValue", "");
	$("#detail_form #menu_ordering").textbox("setValue", "");
	$("#detail_form #use_yn").combobox("setValue", "");
	$('#tableList').datagrid('clearSelections');
	$('#tableList').datagrid('clearChecked');
//	for(var i = 0; i < 4; i++){
//		$("input[id='auth_id"+i+"']:checkbox").removeProp("checked");
//	}
	for(var i = 0; i < detail_form.auth_id.length; i++){
		detail_form.auth_id[i].checked=false;
	}
	$("#detail_form #crud_flag").val("C");
	setCheckDetail();


	//	$("#detail_form #use_yn").combobox("setValue", "");	
}
