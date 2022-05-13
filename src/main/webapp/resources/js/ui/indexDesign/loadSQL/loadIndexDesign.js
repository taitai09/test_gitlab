var editIndex = undefined;

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#loadIndexDesign_form #selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess : function(items){
			$("#loadIndexDesign_form #selectCombo").combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			$("#loadIndexDesign_form #exec_seq").val("");
			$('#loadIndexDesign_form #file_nm').textbox('setValue',"");
			$('#loadIndexDesign_form #analysis_sql_cnt').textbox('setValue',"");
			$('#loadIndexDesign_form #access_path_exec_dt').textbox('setValue',"");
			$("#loadIndexDesign_form #dbid").val(rec.dbid);
			$("#loadIndexDesign_form #file_no").val('');
			
			var win = parent.$.messager.progress({
				title:'Please waiting',
				text:'데이터를 불러오는 중입니다.'
			});
			
			//$('#selectFileNo').combobox('load',[]);
			
			$("#loadIndexDesign_form #selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
					$("#loadIndexDesign_form #selectUserName").combobox('textbox').attr("placeholder","선택");
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
			});
			
			$("#loadIndexDesign_form #selectFileNo").combobox({
				url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
				method:"get",
				valueField:'file_info',
				textField:'file_no',
				panelHeight: 500,
				onSelect:function(rec1){
					$("#loadIndexDesign_form #exec_seq").val("");
					$('#loadIndexDesign_form #analysis_sql_cnt').textbox('setValue',"");
					$('#loadIndexDesign_form #access_path_exec_dt').textbox('setValue',"");
					
					setFileInfo_loadIndexDesign(rec1.file_info);

					$("#loadIndexDesign_form #selectExecSeq").combobox({
						url:"/Common/getAccPathExec?dbid="+rec.dbid+"&access_path_type="+$("#loadIndexDesign_form #access_path_type").val()+"&file_no="+rec1.file_no,
						method:"get",
						valueField:'access_path_value',
						textField:'exec_seq',
						onSelect:function(rec2){
							setExecValue(rec2.access_path_value);
						},
						onLoadSuccess : function(items) {
							$("#loadIndexDesign_form #selectExecSeq").combobox('textbox').attr("placeholder","없음");
							
							if (items.length){
								var opts = $(this).combobox('options');
								
								if($('#loadIndexDesign_form #beforeExecSeq').val() != '') {
									for(var index = 0; index < items.length; index++) {
										if(items[index].exec_seq == $('#loadIndexDesign_form #beforeExecSeq').val()) {
											$(this).combobox('select', items[index][opts.valueField]);
											$('#loadIndexDesign_form #beforeExecSeq').val('');
											break;
										}
									}
								} else {
									$(this).combobox('select', items[0][opts.valueField]);
								}
							}
						}
					});	
					
					$('#loadIndexDesign_form #selectExecSeq').combobox("setValue",$("#loadIndexDesign_form #access_path_value").val());
				},
				onShowPanel: function() {
					// 파일번호 재조회
					$("#loadIndexDesign_form #selectFileNo").combobox({
						url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
						method:"get",
						valueField:'file_info',
						textField:'file_no',
						panelHeight: 500,
					});
					$(".textbox").removeClass("textbox-focused");
					$(".textbox-text").removeClass("tooltip-f");
				},
				onHidePanel: function() {
					$(".tooltip ").hide();
				},
				onLoadSuccess : function(items){
					$("#loadIndexDesign_form #selectFileNo").combobox('textbox').attr("placeholder","선택");
//					if (items.length){
//						var opts = $(this).combobox('options');
////						$(this).combobox('select', items[0][opts.valueField]);
//						
//						if($('#loadIndexDesign_form #file_no').val() != '') {
//							$(this).combobox('select', $('#loadIndexDesign_form #file_no').val());
//						} else {
//							$(this).combobox('select', items[0][opts.valueField]);
//						}
//					}else{
//						$(this).combobox('select', '');
//					}
				}
			});
			
			if($("#loadIndexDesign_form #file_no").val() != '') {
				$('#loadIndexDesign_form #selectFileNo').combobox("setValue",$("#loadIndexDesign_form #file_no").val());
			}
		}
	});
	
	$('#loadIndexDesign_form #selectCombo').combobox("setValue",$("#loadIndexDesign_form #dbid").val());
	
	$('#loadIndexDesign_form #selectTableName').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick_loadIndexDesign();
				}				
			}
		})
	});
	
	$("#loadIndexDesign_form #odstableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#loadIndexDesign_form #table_name").val(row.table_name);
			getSubTables();
		},				
		columns:[[
			{field:'table_name',title:'TABLE_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'acc_cnt',title:'ACCESS-PATH수',halign:"center",align:"right",formatter:getNumberFormat,sortable:"true"},			
			{field:'last_analyzed',title:'LAST_ANALYZED',halign:"center",align:"center",sortable:"true"},
			{field:'num_rows',title:'NUM_ROWS',halign:"center",align:"right",formatter:getNumberFormat,sortable:"true"},					
			{field:'blocks',title:'BLOCKS',halign:"center",align:"right",formatter:getNumberFormat,sortable:"true"},
			{field:'partitioned',title:'PARTITIONED',halign:"center",align:"center",sortable:"true"},
			{field:'part_key_column',title:'PART_KEY_COLUMNS',halign:"center",align:"left",sortable:"true"},
			{field:'subpart_key_column',title:'SUBPART_KEY_COLUMNS',halign:"center",align:"left",sortable:"true"},
			{field:'partitioning_type',title:'PARTITION_TYPE',halign:"center",align:"left",sortable:"true"},
			{field:'table_h_name',title:'COMMENTS',width:'400px',halign:"center",align:"left"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	$("#loadIndexDesign_form #odstableList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable").remove();

	$("#loadIndexDesign_form #columnList").datagrid({
		view: myview,
		columns:[[
			{field:'column_id',title:'COLUMN_ID',halign:"center",align:"center",sortable:"true"},
			{field:'column_name',title:'COLUMN_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'datatype',title:'DATA_TYPE',halign:"center",align:"left"},					
			{field:'notnull',title:'NOT_NULL',halign:"center",align:"center"},
			{field:'num_distinct',title:'NUM_DISTINCT',halign:"center",align:"right",formatter:getNumberFormat},
			{field:'num_nulls',title:'NUM_NULLS',halign:"center",align:"right",formatter:getNumberFormat},
			{field:'comments',title:'COMMENTS',width:'400px',halign:"center",align:"left"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#loadIndexDesign_form #indexsList").datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'index_name',title:'INDEX_NAME',halign:"center",align:"left",sortable:"true"},
			{field:'index_column',title:'INDEX_COLUMNS',halign:"center",align:"left"},					
			{field:'uniqueness',title:'UNIQUENESS',halign:"center",align:"center",sortable:"true"},
			{field:'partitioned',title:'PARTITIONED',halign:"center",align:"center",sortable:"true"},
			{field:'last_analyzed',title:'LAST_ANALYZED',halign:"center",align:"center",sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});			
	
	$("#loadIndexDesign_form #indexDesignList").datagrid({
		onClickCell: onClickCell,
		columns:[[
			{field:'index_seq',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'index_column_list',title:'INDEX_COLUMNS',editor:'textbox',halign:"center",align:"left",sortable:"true"},
			{field:'reg_dt',title:'등록일시',halign:"center",align:"center",sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#loadIndexDesign_form #accPathList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#loadIndexDesign_form #access_path").val(row.access_path);
			getSqlTextTable();
		},					
		columns:[[
			{field:'rnum',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'access_path',title:'ACCESS-PATH',halign:"center",align:"left",sortable:"true"},
			{field:'access_path_count',title:'ACCESS-PATH수',halign:"center",align:"right",formatter:getNumberFormat,sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$("#loadIndexDesign_form #sqlTextList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#loadIndexDesign_form #explain_exec_seq").val(row.explain_exec_seq);
			$("#loadIndexDesign_form #query_seq").val(row.query_seq);
			
			// 신규 탭 생성..
			parent.createSQLLoadNewTab($("#loadIndexDesign_form #menu_id").val(), "accessPathTabs", row.file_no, row.explain_exec_seq, row.query_seq);
		},			
		columns:[[
			{field:'rnum',title:'NO',halign:"center",align:"center",sortable:"true"},
			{field:'exec_seq',title:'EXEC_SEQ',halign:"center",align:"right",sortable:"true"},
			{field:'file_no',title:'FILE_NO',halign:"center",align:"center",sortable:"true"},
			{field:'explain_exec_seq',title:'EXPLAIN_EXEC_SEQ',halign:"center",align:"right",sortable:"true"},					
			{field:'query_seq',title:'QUERY SEQ',halign:"center",align:"right",sortable:"true"},
			{field:'sql_text',title:'SQL_TEXT',halign:"center",align:"left",sortable:"true"}
		]],

		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
	
	$('#loadIndexDesign_form #accessPathTabs').tabs({
		plain : true
	});	
	
//	setMarster_form_selector($('#loadIndexDesign_form').selector);
	setMarster_form_selector('#loadIndexDesign_form');
	
	$('#loadIndexDesign_form #prevBtn').click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext('PREV');
		}
	});
	$('#loadIndexDesign_form #nextBtn').click(function(){
		if(formValidationCheck()){
			fnGoPrevOrNext('NEXT');
		}
	});	
});

function formValidationCheck(){
	return true;
}

function formValidationCheck_loadIndexDesign() {
	if($('#loadIndexDesign_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱 순번을 선택해 주세요.');
		return false;
	}
	
	return true;
}

function Btn_OnClick_loadIndexDesign(){
	$("#loadIndexDesign_form #currentPage").val('1');
	$("#loadIndexDesign_form #pagePerCount").val('20');
	
	$('#loadIndexDesign_form #accessPathTabs').tabs('select', 0);
	
	$('#loadIndexDesign_form #odstableList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #columnList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #indexsList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #indexDesignList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #accPathList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #sqlTextList').datagrid('loadData',[]);
	
	if(!formValidationCheck_loadIndexDesign()){
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#loadIndexDesign_form #menu_id").val();
	
	fnSearchOdsTableAddTable();
}

function fnSearchOdsTableAddTable() {
	$("#loadIndexDesign_form #dbid").val($('#loadIndexDesign_form #selectCombo').combobox('getValue'));
	$("#loadIndexDesign_form #owner").val($('#loadIndexDesign_form #selectUserName').combobox('getValue'));
	$("#loadIndexDesign_form #table_owner").val($("#loadIndexDesign_form #owner").val());
	$("#loadIndexDesign_form #table_name").val($('#loadIndexDesign_form #selectTableName').textbox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 설계","테이블 목록을 조회 중입니다."," "); 
	
	ajaxCall("/LoadIndexDesign/OdsTable",
		$("#loadIndexDesign_form"),
		"POST",
		callback_OdsTableAddTable);
}

//callback 함수
var callback_OdsTableAddTable = function(result) {
	var data = JSON.parse(result);
	var dataLength = 0;
	
	dataLength = data.dataCount4NextBtn;
	
	json_string_callback_common(result,'#loadIndexDesign_form #odstableList',true);
	
	setMarster_form_selector('#loadIndexDesign_form');
	
	fnEnableDisablePagingBtn(dataLength);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
};

function getSubTables(){
	$('#loadIndexDesign_form #accessPathTabs').tabs('select', 1);
	
	$('#loadIndexDesign_form #tbl_title').html('<span class="h3">' + '※ 컬럼 정보( ' + $("#loadIndexDesign_form #table_name").val() + ' )' + '</span>');
	
	$('#loadIndexDesign_form #columnList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #indexsList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #indexDesignList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #accPathList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #sqlTextList').datagrid('loadData',[]);
	
	/* 컬럼정보 조회 */
	$('#loadIndexDesign_form #columnList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#loadIndexDesign_form #columnList').datagrid('loading'); 	
	ajaxCall("/IndexDesignMaintenance/Columns",
			$("#loadIndexDesign_form"),
			"POST",
			callback_ColumnsAddTable);
	
	/* 인덱스 현황 조회 */
	$('#loadIndexDesign_form #indexsList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#loadIndexDesign_form #indexsList').datagrid('loading');	
	ajaxCall("/IndexDesignMaintenance/Indexs",
			$("#loadIndexDesign_form"),
			"POST",
			callback_IndexsAddTable);
	
	/* 인덱스  설계 조회 */
	$('#loadIndexDesign_form #indexDesignList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#loadIndexDesign_form #indexDesignList').datagrid('loading');		
	ajaxCall("/LoadIndexDesign/AccessPath",
			$("#loadIndexDesign_form"),
			"POST",
			callback_AccPathAddTable);	
	/* Access Path 조회 */
	$('#loadIndexDesign_form #accPathList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#loadIndexDesign_form #accPathList').datagrid('loading');	
	ajaxCall("/IndexDesignMaintenance/AccPathIndexDesign",
			$("#loadIndexDesign_form"),
			"POST",
			callback_AccPathIndexDesignAddTable);
}

//callback 함수
var callback_ColumnsAddTable = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#loadIndexDesign_form #columnList').datagrid("loadData", data);
		$('#loadIndexDesign_form #columnList').datagrid('loaded');
	
		$( "#loadIndexDesign_form #accessPathTabs .tabs-panels:eq(0) .datagrid-view2:eq(1) td" ).css( "cursor", "default" );
	}
};		

//callback 함수
var callback_IndexsAddTable = function(result) {
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#loadIndexDesign_form #indexsList').datagrid("loadData", data);
		$('#loadIndexDesign_form #indexsList').datagrid('loaded');
		
		$( "#loadIndexDesign_form .datagrid-view2:eq(3) td" ).css( "cursor", "default" );
	}
};

//callback 함수
var callback_AccPathIndexDesignAddTable = function(result) {
	json_string_callback_common(result,'#loadIndexDesign_form #indexDesignList',false);
};

//callback 함수
var callback_AccPathAddTable = function(result) {
	json_string_callback_common(result,'#loadIndexDesign_form #accPathList',false);
};	

function getSqlTextTable(){
	$('#loadIndexDesign_form #accessPathTabs').tabs('select', 2);
	
	/* SQL 현황 조회 */
	$('#loadIndexDesign_form #sqlTextList').datagrid('loadData',[]);
	$('#loadIndexDesign_form #sqlTextList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#loadIndexDesign_form #sqlTextList').datagrid('loading');	
	
	ajaxCall("/LoadIndexDesign/SQLStatus",
			$("#loadIndexDesign_form"),
			"POST",
			callback_SQLStatusAddTable);			
}

//callback 함수
var callback_SQLStatusAddTable = function(result) {
	json_string_callback_common(result,'#loadIndexDesign_form #sqlTextList',false);
};		

function setFileInfo_loadIndexDesign(selValue){
	$('#loadIndexDesign_form #file_no').val(selValue);
	
	ajaxCall("/Common/getDBIOLoadFileInfo?dbid="+$("#loadIndexDesign_form #dbid").val()+"&file_no="+selValue,
			null,
			"GET",
			callback_getDBIOLoadFileInfoAction_loadIndexDesign);	
}

//callback 함수
var callback_getDBIOLoadFileInfoAction_loadIndexDesign = function(result) {
	if(result.result){	
		var post = result.object;
		
		$('#loadIndexDesign_form #file_nm').textbox('setValue',post.file_nm);
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function setExecValue(selValue){
	var txData = [];
	txData = selValue.split("|");
	
	if(selValue != ""){
//		$("#loadIndexDesign_form #selectExecSeq").combobox('setValue', txData[0]);
		$("#loadIndexDesign_form #exec_seq").val(txData[0]);
		$('#loadIndexDesign_form #analysis_sql_cnt').textbox('setValue',txData[3]);
		$('#loadIndexDesign_form #access_path_exec_dt').textbox('setValue',txData[4]);		
	}else{
		$("#loadIndexDesign_form #exec_seq").val("");
		$("#loadIndexDesign_form #access_path_value").val("");
		$('#loadIndexDesign_form #analysis_sql_cnt').textbox('setValue',"");
		$('#loadIndexDesign_form #access_path_exec_dt').textbox('setValue',"");		
	}
	
	$("#loadIndexDesign_form #access_path_value").val("");
}

function endEditing(){
	if (editIndex == undefined){return true}
	if ($("#loadIndexDesign_form #indexDesignList").datagrid('validateRow', editIndex)){
		$("#loadIndexDesign_form #indexDesignList").datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}	

function onClickCell(index, field){
    if (editIndex != index){
        if (endEditing()){
            $("#loadIndexDesign_form #indexDesignList").datagrid('selectRow', index).datagrid('beginEdit', index);
            var ed = $('#loadIndexDesign_form #indexDesignList').datagrid('getEditor', {index:index,field:field});
            if (ed){
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            editIndex = index;
        } else {
            setTimeout(function(){
                $('#loadIndexDesign_form #indexDesignList').datagrid('selectRow', editIndex);
            },0);
        }
    }
}

function Btn_AddRow(){
	if (endEditing()){
		var designRowCnt = $("#loadIndexDesign_form #indexDesignList").datagrid('getRows').length;
		$("#loadIndexDesign_form #indexDesignList").datagrid('appendRow',{index_seq:(designRowCnt+1),index_column_list:'',reg_dt:''});
		
		editIndex = $("#loadIndexDesign_form #indexDesignList").datagrid('getRows').length-1;
		$("#loadIndexDesign_form #indexDesignList").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
	}			
}

function Btn_DeleteRow(){
    if (editIndex == undefined){return}
    $("#loadIndexDesign_form #indexDesignList").datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
}		

function Btn_SaveIndexDesign(){
	var rows = $('#loadIndexDesign_form #indexDesignList').datagrid('getRows');
	
	var strVal = "";
//	$.each(rows, function(i, row) {
//		$('#indexDesignList').datagrid('endEdit', i);
//		
//		if($.trim(row.index_column_list) != '') strVal += row.index_column_list + "|";
//	});
	
	if(rows.length > 0) {
		strVal = checkedDuplicate(rows);
		
		$("#loadIndexDesign_form #indexColumnArry").val(strRight(strVal,1));
	}
	
	ajaxCall("/IndexDesignMaintenance/InsertIndexDesign",
			$("#loadIndexDesign_form"),
			"POST",
			callback_InsertIndexDesignAction);
}

function checkedDuplicate(rows) {
	var tempArray = new Array();
	var strVal = "";
	
	for(var index = 0; index < rows.length; index++) {
		$('#loadIndexDesign_form #indexDesignList').datagrid('endEdit', index);
		
		if(rows[index].index_column_list == null) {
			continue;
		}
		
		var row = rows[index].index_column_list.replace(/(\s*)/g,"");
		
		if(row == '') {
			continue;
		}
		
		if(tempArray.length == 0) {
			tempArray.push(row);
			strVal += row + "|";
		} else {
			if(tempArray.indexOf(row) == -1) {
				tempArray.push(row);
				strVal += row + "|";
			}
		}
	}
	
	return strVal;
}

//callback 함수
var callback_InsertIndexDesignAction = function(result) {
	if(result.result){
		parent.$.messager.alert('','정상적으로 인덱스가 저장되었습니다.','info',function(){
			setTimeout(function() {
				$('#indexDesignList').datagrid("loadData", []);
				$('#indexDesignList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
				$('#indexDesignList').datagrid('loading');	
				
				ajaxCall("/IndexDesignMaintenance/AccPathIndexDesign",
						$("#loadIndexDesign_form"),
						"POST",
						callback_AccPathIndexDesignAddTable);
			},1000);			
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function showIndexAutoDesign(){
	if($('#loadIndexDesign_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','분석 회차를 선택해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#loadIndexDesign_form #menu_id").val();	

	$("#indexAutoDesign_form #dbid").val($('#loadIndexDesign_form #selectCombo').combobox('getValue'));
	$("#indexAutoDesign_form #owner").val($('#loadIndexDesign_form #selectUserName').combobox('getValue'));	
	$("#indexAutoDesign_form #exec_seq").val($('#loadIndexDesign_form #exec_seq').val());
	$("#indexAutoDesign_form #table_name").val($('#loadIndexDesign_form #table_name').val());
	$("#indexAutoDesign_form #access_path_type").val($('#loadIndexDesign_form #access_path_type').val());
	
	$('#loadIndexDesign_form #indexResultList').datagrid('loadData',[]);
	
	$('#indexAutoDesignPop').window("open");
}

function Excel_DownClick_loadIndexDesign(){	
	if($('#loadIndexDesign_form #selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#loadIndexDesign_form #selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','분석 회차를 선택해 주세요.');
		return false;
	}

	$("#loadIndexDesign_form #dbid").val($('#loadIndexDesign_form #selectCombo').combobox('getValue'));
	$("#loadIndexDesign_form #owner").val($('#loadIndexDesign_form #selectUserName').combobox('getValue'));
	
	$("#loadIndexDesign_form").attr("action","/IndexDesignMaintenance/IndexExcelDown");
	$("#loadIndexDesign_form").submit();
}