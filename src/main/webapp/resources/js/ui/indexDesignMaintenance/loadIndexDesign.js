var editIndex = undefined;

$(document).ready(function() {
	// Database 조회			
	$('#selectCombo').combobox({
	    url:"/Common/getDatabase?isChoice=Y",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	$("#exec_seq").val("");
	    	$('#file_nm').textbox('setValue',"");
	    	$('#analysis_sql_cnt').textbox('setValue',"");
	    	$('#access_path_exec_dt').textbox('setValue',"");
	    	$("#dbid").val(rec.dbid);
	    	
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	//$('#selectFileNo').combobox('load',[]);

	    	$("#selectUserName").combobox({
				url:"/Common/getUserName?dbid="+rec.dbid,
				method:"get",
				valueField:'username',
				textField:'username',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','OWNER 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    	
	    	$("#selectFileNo").combobox({
				url:"/Common/getDBIOLoadFile?dbid="+rec.dbid,
				method:"get",
				valueField:'file_info',
				textField:'file_no',
				onSelect:function(rec1){
					$("#exec_seq").val("");
					$('#analysis_sql_cnt').textbox('setValue',"");
					$('#access_path_exec_dt').textbox('setValue',"");
					
					setFileInfo(rec1.file_info);

			    	$("#selectExecSeq").combobox({
						url:"/Common/getAccPathExec?dbid="+rec.dbid+"&access_path_type="+$("#access_path_type").val()+"&file_no="+rec1.file_no,
						method:"get",
						valueField:'access_path_value',
						textField:'exec_seq',
						onSelect:function(rec2){
							setExecValue(rec2.access_path_value);	
						}
			    	});	
			    	
			    	$('#selectExecSeq').combobox("setValue",$("#access_path_value").val());
				},
				onLoadSuccess : function(items){
					if (items.length){
						var opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					}else{
						$(this).combobox('select', '');
					}
				}				
	    	});
	    	
	    	$('#selectFileNo').combobox("setValue",$("#file_no").val());
	    }
	});
	
	$('#selectCombo').combobox("setValue",$("#dbid").val());
	
	$('#selectTableName').textbox({
		inputEvents:$.extend({},$.fn.textbox.defaults.inputEvents,{
			keyup:function(e){
				if(e.keyCode == 13){
					Btn_OnClick();
				}				
			}
		})
	});
	
	$("#odstableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#table_name").val(row.table_name);
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
	$("#odstableList").parent().find(".datagrid-view2 .datagrid-body .datagrid-btable").remove();

	$("#columnList").datagrid({
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
	
	$("#indexsList").datagrid({
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
	
	$("#indexDesignList").datagrid({
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
	
	$("#accPathList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#access_path").val(row.access_path);
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
	
	$("#sqlTextList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#explain_exec_seq").val(row.explain_exec_seq);
			$("#query_seq").val(row.query_seq);
			
			// 신규 탭 생성..
			parent.createSQLLoadNewTab($("#menu_id").val(), "accessPathTabs", row.file_no, row.explain_exec_seq, row.query_seq);
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
	
	$('#accessPathTabs').tabs({
		plain : true
	});	
});

function formValidationCheck() {
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','파싱 순번을 선택해 주세요.');
		return false;
	}
	
	return true;
}

function Btn_OnClick(){
	$("#currentPage").val("1");
	
	$('#accessPathTabs').tabs('select', 0);
	
	$('#odstableList').datagrid('loadData',[]);
	$('#columnList').datagrid('loadData',[]);
	$('#indexsList').datagrid('loadData',[]);
	$('#indexDesignList').datagrid('loadData',[]);
	$('#accPathList').datagrid('loadData',[]);
	$('#sqlTextList').datagrid('loadData',[]);
	
	if(!formValidationCheck()){
		return;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	fnSearch();
}

function fnSearch() {
	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	$("#table_owner").val($("#owner").val());
	$("#table_name").val($('#selectTableName').textbox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("적재SQL 인덱스 설계","테이블 목록을 조회 중입니다."," "); 
	
	ajaxCall("/LoadIndexDesign/OdsTable",
		$("#submit_form"),
		"POST",
		callback_OdsTableAddTable);		
}

//callback 함수
var callback_OdsTableAddTable = function(result) {
	json_string_callback_common(result,'#odstableList',true);
	
	var data = JSON.parse(result);
	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn(dataLength);
};

function getSubTables(){
	$('#accessPathTabs').tabs('select', 1);
	
	$('#columnList').datagrid('loadData',[]);
	$('#indexsList').datagrid('loadData',[]);
	$('#indexDesignList').datagrid('loadData',[]);
	$('#accPathList').datagrid('loadData',[]);
	$('#sqlTextList').datagrid('loadData',[]);
	
	/* 컬럼정보 조회 */
	$('#columnList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#columnList').datagrid('loading'); 	
	ajaxCall("/IndexDesignMaintenance/Columns",
			$("#submit_form"),
			"POST",
			callback_ColumnsAddTable);
	
	/* 인덱스 현황 조회 */
	$('#indexsList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#indexsList').datagrid('loading');	
	ajaxCall("/IndexDesignMaintenance/Indexs",
			$("#submit_form"),
			"POST",
			callback_IndexsAddTable);
	
	/* 인덱스  설계 조회 */
	$('#indexDesignList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#indexDesignList').datagrid('loading');		
	ajaxCall("/LoadIndexDesign/AccessPath",
			$("#submit_form"),
			"POST",
			callback_AccPathAddTable);	
	/* Access Path 조회 */
	$('#accPathList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#accPathList').datagrid('loading');	
	ajaxCall("/IndexDesignMaintenance/AccPathIndexDesign",
			$("#submit_form"),
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
		$('#columnList').datagrid("loadData", data);
		$('#columnList').datagrid('loaded');
	
		$( "#accessPathTabs .tabs-panels:eq(0) .datagrid-view2:eq(1) td" ).css( "cursor", "default" );
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
		$('#indexsList').datagrid("loadData", data);
		$('#indexsList').datagrid('loaded');
		
		$( ".datagrid-view2:eq(3) td" ).css( "cursor", "default" );
	}
};

//callback 함수
var callback_AccPathIndexDesignAddTable = function(result) {
	json_string_callback_common(result,'#indexDesignList',false);
};

//callback 함수
var callback_AccPathAddTable = function(result) {
	json_string_callback_common(result,'#accPathList',false);
};	

function getSqlTextTable(){
	$('#accessPathTabs').tabs('select', 2);
	
	/* SQL 현황 조회 */
	$('#sqlTextList').datagrid('loadData',[]);
	$('#sqlTextList').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#sqlTextList').datagrid('loading');	
	
	ajaxCall("/LoadIndexDesign/SQLStatus",
			$("#submit_form"),
			"POST",
			callback_SQLStatusAddTable);			
}

//callback 함수
var callback_SQLStatusAddTable = function(result) {
	json_string_callback_common(result,'#sqlTextList',false);
};		

function setFileInfo(selValue){
	$('#file_no').val(selValue);
	
	ajaxCall("/Common/getDBIOLoadFileInfo?dbid="+$("#dbid").val()+"&file_no="+selValue,
			null,
			"GET",
			callback_getDBIOLoadFileInfoAction);	
}

//callback 함수
var callback_getDBIOLoadFileInfoAction = function(result) {
	if(result.result){	
		var post = result.object;
		
		$('#file_nm').textbox('setValue',post.file_nm);
	}else{
		parent.$.messager.alert('',result.message);
	}
}

function setExecValue(selValue){
	var txData = [];
	txData = selValue.split("|");
	
	if(selValue != ""){
		$("#exec_seq").val(txData[0]);
		$('#analysis_sql_cnt').textbox('setValue',txData[3]);
		$('#access_path_exec_dt').textbox('setValue',txData[4]);		
	}else{
		$("#exec_seq").val("");
		$("#access_path_value").val("");
		$('#analysis_sql_cnt').textbox('setValue',"");
		$('#access_path_exec_dt').textbox('setValue',"");		
	}
	
	$("#access_path_value").val("");
}

function endEditing(){
	if (editIndex == undefined){return true}
	if ($("#indexDesignList").datagrid('validateRow', editIndex)){
		$("#indexDesignList").datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}	

function onClickCell(index, field){
    if (editIndex != index){
        if (endEditing()){
            $("#indexDesignList").datagrid('selectRow', index).datagrid('beginEdit', index);
            var ed = $('#indexDesignList').datagrid('getEditor', {index:index,field:field});
            if (ed){
                ($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
            }
            editIndex = index;
        } else {
            setTimeout(function(){
                $('#indexDesignList').datagrid('selectRow', editIndex);
            },0);
        }
    }
}

function Btn_AddRow(){
	if (endEditing()){
		var designRowCnt = $("#indexDesignList").datagrid('getRows').length;
		$("#indexDesignList").datagrid('appendRow',{index_seq:(designRowCnt+1),index_column_list:'',reg_dt:''});
		
		editIndex = $("#indexDesignList").datagrid('getRows').length-1;
		$("#indexDesignList").datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
	}			
}

function Btn_DeleteRow(){
    if (editIndex == undefined){return}
    $("#indexDesignList").datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
    editIndex = undefined;
}		

function Btn_SaveIndexDesign(){
	var rows = $('#indexDesignList').datagrid('getRows');
	var strVal = "";
	$.each(rows, function(i, row) {
		$('#indexDesignList').datagrid('endEdit', i);
		
		if($.trim(row.index_column_list) != '') strVal += $.trim(row.index_column_list) + "|";
	});
	
	$("#indexColumnArry").val(strRight(strVal,1));
	
	ajaxCall("/IndexDesignMaintenance/InsertIndexDesign",
			$("#submit_form"),
			"POST",
			callback_InsertIndexDesignAction);		
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
						$("#submit_form"),
						"POST",
						callback_AccPathIndexDesignAddTable);
			},1000);			
		});
	}else{
		parent.$.messager.alert('',result.message);
	}
};

function showIndexAutoDesign(){
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','분석 회차를 선택해 주세요.');
		return false;
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

	$("#indexAutoDesign_form #dbid").val($('#selectCombo').combobox('getValue'));
	$("#indexAutoDesign_form #owner").val($('#selectUserName').combobox('getValue'));	
	$("#indexAutoDesign_form #exec_seq").val($('#exec_seq').val());
	$("#indexAutoDesign_form #table_name").val($('#table_name').val());
	$("#indexAutoDesign_form #access_path_type").val($('#access_path_type').val());
	
	$('#indexResultList').datagrid('loadData',[]);
	
	$('#indexAutoDesignPop').window("open");
}

function Excel_DownClick(){	
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	if($('#selectUserName').combobox('getValue') == ""){
		parent.$.messager.alert('','OWNER를 선택해 주세요.');
		return false;
	}
	
	if($('#selectExecSeq').combobox('getValue') == ""){
		parent.$.messager.alert('','분석 회차를 선택해 주세요.');
		return false;
	}

	$("#dbid").val($('#selectCombo').combobox('getValue'));
	$("#owner").val($('#selectUserName').combobox('getValue'));
	
	$("#submit_form").attr("action","/IndexDesignMaintenance/IndexExcelDown");
	$("#submit_form").submit();
}