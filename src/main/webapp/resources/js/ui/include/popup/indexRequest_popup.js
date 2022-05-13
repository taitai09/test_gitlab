var editIndex = undefined;

$(document).ready(function() {
	$('#indexRequestPop').window({
		title : "인덱스 요청",
		top:getWindowTop(700),
		left:getWindowLeft(900)
	});

	$("#indexsList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : true,		
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true"},
			{field:'rnum',title:'SEQ',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"15%",halign:"center",align:'left'},
			{field:'index_name',title:'인덱스명',width:"20%",halign:"center",align:'left'},
			{field:'index_column',title:'인덱스컬럼',width:"30%",halign:"center",align:'left'},
			{field:'uniqueness',title:'UNIQUE',width:"15%",halign:"center",align:'left'},
			{field:'partitioned',title:'파티션',width:"15%",halign:"center",align:'left'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#indexsRequestList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : false,
		selectOnCheck : true,		
		onClickCell: fnIndexDesignClickCell,
		onClickRow : function(index,row) {
		},			
		columns:[[
			{field:'chk',width:"5%",halign:"center",align:"center",checkbox:"true",readonly:"true"},
			{field:'tuning_no',hidden:"true"},
			{field:'editing',hidden:"true"},
			{field:'index_impr_type_cd',hidden:"true"},
			{field:'index_impr_type_nm',title:'개선유형',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'인덱스명',editor:'textbox',width:"20%",halign:"center",align:'center',sortable:"true"},
			{field:'index_column_name',title:'인덱스컬럼',editor:'textbox',width:"25%",halign:"center",align:'left',sortable:"true"},
			{field:'before_index_column_name',title:'변경전 인덱스컬럼',width:"25%",halign:"center",align:'left',sortable:"true"}
		]],
        onBeforeEdit:function(index,row){
    		//row.editing = true;
        	if(row != undefined){
        		row.editing = true;
        	}
            $(this).datagrid('refreshRow', index);
        },
        onBeginEdit:function(index,row){
//        	console.log("index===>",index);
        	var dg = $(this);
        	var ed = dg.datagrid('getEditors',index)[0];
        	if (!ed){return;}
        	var t = $(ed.target);
        	if (t.hasClass('textbox-f')){
        		t = t.textbox('textbox');
        	};
        	t.bind('keydown', function(e){
//        		console.log("keydown:",e.keyCode);
        		if (e.keyCode == 13){
        			dg.datagrid('endEdit', index);
        		} else if (e.keyCode == 27){
        			dg.datagrid('cancelEdit', index);
        		}
        	});
        	t.bind('keyup', function(e){
     		   var thisValUpper = $(this).val().toUpperCase();
    		   $(this).val(thisValUpper);
        	});

        	var ed1 = dg.datagrid('getEditors',index)[1];
        	if (!ed1){return;}
        	var t1 = $(ed1.target);
        	if (t1.hasClass('textbox-f')){
        		t1 = t1.textbox('textbox');
        	};
        	t1.bind('keydown', function(e){
//        		console.log("keydown:",e.keyCode);
        		if (e.keyCode == 13){
        			dg.datagrid('endEdit', index);
        		} else if (e.keyCode == 27){
        			dg.datagrid('cancelEdit', index);
        		}
        	});
        	t1.bind('keyup', function(e){
     		   var thisValUpper = $(this).val().toUpperCase();
    		   $(this).val(thisValUpper);
        	});

        	var ed2 = dg.datagrid('getEditors',index)[2];
        	if (!ed2){return;}
        	var t2 = $(ed2.target);
        	if (t2.hasClass('textbox-f')){
        		t2 = t2.textbox('textbox');
        	};
        	t2.bind('keydown', function(e){
//        		console.log("keydown:",e.keyCode);
        		if (e.keyCode == 13){
        			dg.datagrid('endEdit', index);
        		} else if (e.keyCode == 27){
        			dg.datagrid('cancelEdit', index);
        		}
        	});
        	t2.bind('keyup', function(e){
     		   var thisValUpper = $(this).val().toUpperCase();
    		   $(this).val(thisValUpper);
        	});
        },
        onEndEdit:function(index,row){
        	var dg = $(this);
        	var ed = dg.datagrid('getEditors',index)[0];
        	if (!ed){return;}
        	var t = $(ed.target);
            
            row.index_name = t.textbox('getText');
        },
        onAfterEdit:function(index,row){
            row.editing = false;
            $(this).datagrid('refreshRow', index);
        },
        onCancelEdit:function(index,row){
            row.editing = false;
            $(this).datagrid('refreshRow', index);
        },
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    }    	
	});

	$("#owner").combobox({
		url:"/Common/getUserName?dbid="+$("#submit_form #dbid").val(),
		method:"get",
		valueField:'username',
		textField:'username',
		onLoadSuccess: function(event) {
			parent.$.messager.progress('close');
		},
		onLoadError: function(){
			parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
			parent.$.messager.progress('close');
		},
	    onSelect:function(rec){
	    }
	});

	// when press ENTER key, accept the inputed value.
	$('#indexRequest_form #table_name').textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){
		   Btn_Search();
	   }else{
		   var thisValUpper = $(this).val().toUpperCase();
		   $(this).val(thisValUpper);
	   }
	});
	
//	$('#indexsRequestList').datagrid({
//	    onBeginEdit:function(index,row){
//	        var dg = $(this);
//	        var ed = dg.datagrid('getEditors',index)[0];
//	        if (!ed){return;}
//	        var t = $(ed.target);
//	        if (t.hasClass('textbox-f')){
//	            t = t.textbox('textbox');
//	        }
//	        t.bind('keydown', function(e){
//	            if (e.keyCode == 13){
//	                dg.datagrid('endEdit', index);
//	            } else if (e.keyCode == 27){
//	                dg.datagrid('cancelEdit', index);
//	            }
//	        })
//	    }
//	}).datagrid('enableCellEditing');
	
	$('#indexRequest_form #db_name').textbox('setValue',$('#submit_form #db_name').val());
	$('#indexRequest_form #dbid').val($('#submit_form #dbid').val());
	Btn_OnClosePopup('indexRequestPop');
});

function fnIndexDesignClickCell(index, field){
//	console.log("index:"+index+" field:"+field);
    if (editIndex != index){
        if (endEditing()){
        	var row = $("#indexsRequestList").datagrid('selectRow', index);
        	
            $("#indexsRequestList").datagrid('selectRow', index).datagrid('beginEdit', index);
            var ed = $('#indexsRequestList').datagrid('getEditor', {index:index,field:field});
            if (ed){
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            editIndex = index;
        } else {
            setTimeout(function(){
                $('#indexsRequestList').datagrid('selectRow', editIndex);
            },0);
        }
    }
}

function Btn_Search(){
//	console.log("Btn_Search");
	if($('#indexRequest_form #owner').combobox('getValue') == ""){
		$.messager.alert('','Owner를 선택해 주세요.');
		return false;
	}
	
	if($('#indexRequest_form #table_name').textbox('getValue') == ""){
		$.messager.alert('','테이블명을 입력해 주세요.');
		return false;
	}
	
	fnUpdateSearchBtnClickFlag();
	
	$('#indexsList').datagrid('loadData',[]);

	/* modal progress open */
	parent.openMessageProgress("인덱스 목록 조회","인덱스 목록을 조회중입니다."); 

	ajaxCall("/IndexDesignMaintenance/Indexs",
			$("#indexRequest_form"),
			"POST",
			callback_Indexs);		
}

//callback 함수
var callback_Indexs = function(result) {
	json_string_callback_common(result,'#indexsList',true);
	$("#indexsList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
};

function Btn_AppendIndexDesignList(){
	var rows = $('#indexRequest_form #indexsList').datagrid('getRows');

	$.each(rows, function(i, row) {
//		var totalRowCnt = eval("if_"+frameName).$('#indexsRequestList').datagrid('getRows').length;
//		eval("if_"+frameName).$("#indexsRequestList").datagrid('appendRow',{index_seq:(totalRowCnt+1),index_column_list:row.access_path_column_list,reg_dt:''});
//		
//		eval("if_"+frameName).$("#indexsRequestList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		var totalRowCnt = $('#indexsRequestList').datagrid('getRows').length;
		$("#indexsRequestList").datagrid('appendRow',{index_seq:(totalRowCnt+1),index_column_list:row.access_path_column_list,reg_dt:''});
		
		$("#indexsRequestList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		
	});
	
	Btn_OnClosePopup("indexRequestPop");
}

//신규
function Btn_IndexAdd(){
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
//	console.log("searchBtnClickCount:",searchBtnClickCount);
	if(searchBtnClickCount == ""||searchBtnClickCount == "0"){
		if($('#indexRequest_form #owner').combobox('getValue') == ""){
			$.messager.alert('','Owner를 선택해 주세요.');
			return false;
		}else if($('#indexRequest_form #table_name').textbox('getValue') == ""){
			$.messager.alert('','테이블명을 입력해 주세요.');
			return false;
		}else{
			parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
			return false;
		}
	}
//	var selectedRows = $('#indexsList').datagrid('getChecked');
//	if(selectedRows.length == 0){
//		parent.$.messager.alert('','선택된 인덱스가 없습니다.');
//		return false;		
//	}
//	for(var i=0;i<selectedRows.length;i++){
//		var selectedRow = selectedRows[i];
//      var selectedRowIndex = $("#indexsList").datagrid("getRowIndex", selectedRow);
	
		if (endEditing()){
			$("#indexsRequestList").datagrid('appendRow',{
				tuning_no:$("#submit_form #tuning_no").val(),
				editing:false,
				index_impr_type_nm:'신규',
				index_impr_type_cd:'1',
				table_name:$("#table_name").textbox('getValue').toUpperCase(),
				index_name:'',
				index_column_name:'',
				before_index_column_name:''
			});
				var totalRowCnt = $("#indexsRequestList").datagrid('getRows').length;
//					console.log("totalRowCnt:"+totalRowCnt);
					editIndex = totalRowCnt-1;
//					console.log("editIndex:"+editIndex);
			$('#indexsRequestList').datagrid('beginEdit', editIndex);
			$("#indexsRequestList").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
			$("#indexsRequestList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
		}
//	}
	
	
}

function endEditing(){
	if (editIndex == undefined){return true}
	if ($("#indexsRequestList").datagrid('validateRow', editIndex)){
		$("#indexsRequestList").datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

function Btn_IndexModify(){

	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	if(searchBtnClickCount == "" || searchBtnClickCount == "0"){
		if($('#indexRequest_form #owner').combobox('getValue') == ""){
			$.messager.alert('','Owner를 선택해 주세요.');
			return false;
		}else if($('#indexRequest_form #table_name').textbox('getValue') == ""){
			$.messager.alert('','테이블명을 입력해 주세요.');
			return false;
		}else{
			parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
			return false;
		}
	}
	
	var selectedRows = $('#indexsList').datagrid('getChecked');
//	console.log("selectedRows:",selectedRows);
	if(selectedRows.length == 0){
		parent.$.messager.alert('','선택된 인덱스가 없습니다.');
		return false;		
	}
	for(var i=0;i<selectedRows.length;i++){
		var selectedRow = selectedRows[i];
//		console.log("selectedRow:",selectedRow);
        var selectedRowIndex = $("#indexsList").datagrid("getRowIndex", selectedRow);
//		console.log("selectedRowIndex:"+selectedRowIndex);
	
		if (endEditing()){
			var totalRowCnt = $("#indexsRequestList").datagrid('getRows').length;
			console.log("totalRowCnt:"+totalRowCnt);
			editIndex = totalRowCnt-1;
//			console.log("editIndex:"+editIndex);
		    $('#indexsRequestList').datagrid('beginEdit', editIndex);
			$("#indexsRequestList").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
			$("#indexsRequestList").datagrid('appendRow',{
				tuning_no:$("#submit_form #tuning_no").val(),
				editing:true,
				index_impr_type_nm:'변경',
				index_impr_type_cd:'2',
				table_name:$("#table_name").textbox('getValue').toUpperCase(),
				index_name:selectedRow.index_name,
				index_column_name:'',
				before_index_column_name:selectedRow.index_column
				});
		}
	}
	$("#indexsRequestList").parent().find(".datagrid-body td" ).css( "cursor", "default" );	
}		

//삭제
function Btn_IndexDelete(){
	
	var searchBtnClickCount = $("#submit_form #searchBtnClickCount").val();
	if(searchBtnClickCount == "" || searchBtnClickCount == "0"){
		if($('#indexRequest_form #owner').combobox('getValue') == ""){
			$.messager.alert('','Owner를 선택해 주세요.');
			return false;
		}else if($('#indexRequest_form #table_name').textbox('getValue') == ""){
			$.messager.alert('','테이블명을 입력해 주세요.');
			return false;
		}else{
			parent.$.messager.alert('','먼저 검색버튼을 클릭해 주세요.');
			return false;
		}
	}
	
	var selectedRows = $('#indexsList').datagrid('getChecked');
//	console.log("selectedRows:",selectedRows);
	if(selectedRows.length == 0){
		parent.$.messager.alert('','선택된 인덱스가 없습니다.');
		return false;		
	}
	for(var i=0;i<selectedRows.length;i++){
		var selectedRow = selectedRows[i];
//		console.log("selectedRow:",selectedRow);
      var selectedRowIndex = $("#indexsList").datagrid("getRowIndex", selectedRow);
//		console.log("selectedRowIndex:"+selectedRowIndex);
	
		if (endEditing()){
			$("#indexsRequestList").datagrid('appendRow',{
				tuning_no:$("#submit_form #tuning_no").val(),
				editing:false,
				index_impr_type_nm:'삭제',
				index_impr_type_cd:'3',
				table_name:$("#table_name").textbox('getValue').toUpperCase(),
				index_name:selectedRow.index_name,
				index_column_name:selectedRow.index_column,
				before_index_column_name:''
			});
				var totalRowCnt = $("#indexsRequestList").datagrid('getRows').length;
//					console.log("totalRowCnt:"+totalRowCnt);
					editIndex = totalRowCnt-1;
//					console.log("editIndex:"+editIndex);
			$('#indexsRequestList').datagrid('beginEdit', editIndex);
			$("#indexsRequestList").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
		}
	}
	$("#indexsRequestList").parent().find(".datagrid-body td" ).css( "cursor", "default" );
	
}

//반영
function Btn_IndexApply(){
//	var selectedRows = $('#indexsRequestList').datagrid('getChecked');
//	console.log("selectedRows:",selectedRows);
//	for(var i=0;i<selectedRows.length;i++){
////		var selectedRow = selectedRows[i];
////        var selectedRowIndex = $("#indexsRequestList").datagrid("getRowIndex", selectedRow);
////		console.log("selectedRowIndex:"+selectedRowIndex);
////		$('#indexsRequestList').datagrid('deleteRow', selectedRowIndex);
//	  if (editIndex == undefined){return}
//	  $("#indexsRequestList").datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
//	  editIndex = undefined;
//	}
//	var selectedRows = $('#indexsRequestList').datagrid('getChecked');
//	console.log("selectedRows:",selectedRows);
//	if(selectedRows.length == 0){
//		parent.$.messager.alert('','반영할 인덱스를 선택하여 주세요.');
//		$("#indexsRequestList").datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
//		return false;		
//	}	
	fnIndexDesignClickCell(); //완료
	
	//반영 버튼 클릭하였을때 인덱스명과 인덱스컬럼의 값이 유효한지 체크한다.
	if(!fnValidationCheckSqlTuningIndexHistory()){
		return false; 
	}
			
	var rows = $('#indexsRequestList').datagrid('getRows');
//	if(rows.length > 0){
		var ndg = $('#tableList').datagrid();
		ndg.datagrid('loadData', $.extend(true,[],rows));
//	}
		
	Btn_OnClosePopup('indexRequestPop');
}
//선택 삭제
function Btn_SelectedIndexDelete(){
	var selectedRows = $('#indexsRequestList').datagrid('getChecked');
//	console.log("selectedRows:",selectedRows);
	if(selectedRows.length == 0){
		parent.$.messager.alert('','먼저 삭제할 인덱스명을 체크하시고 삭제버튼을 눌러주세요.');
		return false;		
	}	
	for(var i=0;i<selectedRows.length;i++){
		var selectedRow = selectedRows[i];
        var selectedRowIndex = $("#indexsRequestList").datagrid("getRowIndex", selectedRow);
//		console.log("selectedRowIndex:"+selectedRowIndex);
		$('#indexsRequestList').datagrid('deleteRow', selectedRowIndex);
	}
}
//반영 버튼 클릭하였을때 인덱스명과 인덱스컬럼의 값이 유효한지 체크한다.
function fnValidationCheckSqlTuningIndexHistory(){
	//index_history 추가
	var rows = $('#indexsRequestList').datagrid('getRows');
	console.log("row.length:"+rows.length);
	if(rows.length > 0){
		for(var i=0;i<rows.length;i++){
			var index_impr_type_cd = rows[i].index_impr_type_cd;
			var table_name = rows[i].table_name;
			var index_name = rows[i].index_name;
			var index_column_name = rows[i].index_column_name;
			
			if(index_name == ""){
				parent.$.messager.alert('',"인덱스명을 입력해 주세요.",'error');
				return false;
			}	
			if(index_column_name == ""){
				parent.$.messager.alert('',"인덱스컬럼명을 입력해 주세요.",'error');
				return false;
			}	
		}
	}		
	return true;
}
