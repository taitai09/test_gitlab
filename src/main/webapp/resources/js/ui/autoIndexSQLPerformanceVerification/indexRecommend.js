/**
 *
 */
var autoProcessForcePop;
var performanceAnalysisForcePop;
var autoCreateList;

var autoProcessTimer;
var performanceAnalysisTimer;
var bef_Selected_ProjectId;
var bef_Selected_Perf_Check_Id;
var isCheckedAll = false;

var bef_auto_create_drop_history;

var bef_performance_analyze;
var OK_PERF_CHECK_END_YN_POP = false

const CREATE_INDEX_SCRIPT = 1;
const AUTO_CREATE_INDEX = 2;
const DROP_INDEX_SCRIPT = 3;
const AUTO_DROP_INDEX = 4;
const EXCUTE_ANALYZE_CONSTRAINT = 5;
const FORCE_COMPLET_AUTO = 6;
const VISIBLE_INDEX = 7;
const ADD_DEL_ERR_POP = 8;

const CREATE_HISTORY_ALL = 'C';
const DROP_HISTORY_ALL = 'D';

const FORCE_DROP = 'D';
const FORCE_CREATE = 'C';

const ADD_LIST_IDX = 0;
const DEL_LIST_IDX = 1;

$(document).ready(function(){
	initializeElements();
});

function initializeElements(){
	$("body").css("visibility", "visible");
	$("#currentPage").val("1");
	$("#pagePerCount").val("20");
	$("#search_currentPage").val($('#currentPage').val());
	$("#search_pagePerCount").val($('#pagePerCount').val());

	setNumberBoxAttrByName('autoProcessRefresh_val','min',3);
	setNumberBoxAttrByName('performanceAnalysisTimer_value','min',3);

	setNumberBoxAttrByName('parallel_degree','min',1);
	setNumberBoxAttrByName('multiple_exec_cnt','min',1);
	setNumberBoxAttrByName('multiple_bind_exec_cnt','min',1);
	setNumberBoxAttrByName('max_fetch_cnt','min',1);




	let styleSheet =  $("link[rel='stylesheet']");
	let len = styleSheet.length;

	for(let idx = 0 ; idx < len ; idx ++){
		if(styleSheet[idx].href.indexOf('theme-triton-all')>-1){
			styleSheet[idx].parentElement.removeChild(styleSheet[idx]);
		}
	}

	createList();
	createRecommendIdxAddList(false);
	createPerfAnalysisList();
	initializeProjectIdCombobox()

	initializeAddDelTab();
	initializeSqlPerformancePCombobox();
	initializeRecommendTypeCombobox();
	initializeIndexAddCombobox();
	initializeSpsRecommendIndexCombobox();

	initialAutoProcessRefresh();
	initialPerformanceAnalysisRefresh();
	initializePerformanceAnalyze();

	let obj = $("#perfAnalysisList").parent().find('.datagrid-view2 .datagrid-body');
	obj.css('overflow','hidden');

	closeMessageProgress();
}

function initializeSpsRecommendIndexCombobox(){

	$('#spsRecommendIndex').combobox({
		url : "/Common/getCommonCode?isAll=Y&grp_cd_id=1100",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
	});

}


function initializeIndexAddCombobox(){
	let data = [{
		"indexAdd_id":'',
		"indexAdd_text":"??????",
		"selected":true
	},{
		"indexAdd_id":'Y',
		"indexAdd_text":"Y"
	},{
		"indexAdd_id":'N',
		"indexAdd_text":"N"
	}];

	$('#indexAdd').combobox({
		valueField:'indexAdd_id',
		textField:'indexAdd_text',
		data : data
	});

}


function initializeRecommendTypeCombobox(){
	let url = "/Common/getCommonCode?isAll=Y&grp_cd_id=1099"

	$('#recommendType').combobox({
		url : url,
		method : "get",
		valueField : 'cd',
		textField : 'cd_nm',
	});

}

function initializeSqlPerformancePCombobox(){

	$("#sqlPerformanceP").combobox({
		onShowPanel : function(){
			getSqlPerformancePacList();

		}
	});

}
function initializeProjectIdCombobox(){
	$('#project_id').combobox({
		onShowPanel : function(){
			setProjectIdCombobox();
		}
	});
	$("#project_id").combobox("textbox").attr("placeholder",'??????');
}

function initializePerformanceAnalyze(){
	initializeperformanceAnalyzeSwitchButtons('parallel_degree_yn', 'parallel_degree', false);
	initializeDmlExecSwitch();
	initializeperformanceAnalyzeSwitchButtons('multi_execution', 'multiple_exec_cnt', false);
	initializeperformanceAnalyzeSwitchButtons('multi_bind_execution', 'multiple_bind_exec_cnt', false);

	initialzeSqlTimeLimitCombo();

}

function setProjectIdCombobox(parentParam){

	$('#project_id').combobox({
		url : "/AISQLPVAnalyze/getProjectList",
		method : "get",
		valueField : 'project_id',
		textField : 'project_nm',
		onLoadError: function(){
			closeMessageProgress();
			parent.$.messager.alert('??????','???????????? ????????? ?????? ??? ????????? ?????????????????????.');
			return false;
		},
		onLoadSuccess: function(data) {
			if(isEmpty(data)){
				return false;
			}
			if(parentParam){
				setEasyUiFieldValue("project_id",parentParam.project_id);
				getSqlPerformancePacList(parentParam.sql_auto_perf_check_id);
				bef_Selected_ProjectId = parentParam.project_id;

				$(".textbox").removeClass("textbox-focused");
				$(".textbox-text").removeClass("tooltip-f");
				$(".textbox-text").removeClass("textbox-prompt");

				$('#sqlPerformanceP').combobox('readonly', false);
				$("#sqlPerformanceP").combobox("textbox").attr("placeholder",'??????');
				resetElements(true);

			}else if(isNotEmpty(bef_Selected_ProjectId)){
				setEasyUiFieldValue('project_id',bef_Selected_ProjectId);
			}
			$('#project_id').combobox('textbox').attr("placeholder","??????");
		},
		onClick:function(row){

			if(isEmpty(bef_Selected_ProjectId) || bef_Selected_ProjectId !== row.project_id){
				bef_Selected_ProjectId = row.project_id;
				bef_Selected_Perf_Check_Id = '';
			}else if(isNotEmpty(bef_Selected_ProjectId) && bef_Selected_ProjectId === row.project_id){
				return false;
			}

			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
			$(".textbox-text").removeClass("textbox-prompt");

			$('#sqlPerformanceP').combobox('readonly', false);
			$("#sqlPerformanceP").combobox("textbox").attr("placeholder",'??????');
			resetElements(true);
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		}
	});

}

function resetElements(isProjectId) {

	if(isProjectId){
		setEasyUiFieldValue('sqlPerformanceP','');
	}
	setEasyUiFieldValue('owner','');
	setEasyUiFieldValue('tableName','');
	setEasyUiFieldValue('recommendType','');
	setEasyUiFieldValue('indexAdd','');
	setEasyUiFieldValue('spsRecommendIndex','');


}

function getSqlPerformancePacList(sql_auto_perf_check_id){

	let project_id = $("#project_id").combobox('getValue');

	if(isEmpty(project_id)){
		return false;
	}

	let url = "/AISQLPVAnalyze/getSqlPerformancePacList?project_id="+project_id;
	url += "&perf_check_type_cd=4";
	url += "&database_kinds_cd=ORACLE";

	$('#sqlPerformanceP').combobox({
		url:url,
		method:"get",
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		onLoadError: function(ex){
			closeMessageProgress();
			parent.$.messager.alert('??????','SQL ????????? ????????? ?????? ??? ????????? ?????????????????????.');
			return false;
		},
		onClick:function(row){
			if(isEmpty(bef_Selected_Perf_Check_Id) || bef_Selected_Perf_Check_Id !== row.sql_auto_perf_check_id){
				bef_Selected_Perf_Check_Id = row.sql_auto_perf_check_id;
			}else if (isNotEmpty(bef_Selected_Perf_Check_Id) || bef_Selected_Perf_Check_Id === row.sql_auto_perf_check_id){
				return false;
			}

			$(".textbox").removeClass("textbox-focused");
			$(".textbox-text").removeClass("tooltip-f");
			$(".textbox-text").removeClass("textbox-prompt");

			resetElements(false);
			// getSqlPerformancePacList(row);
		},
		onHidePanel: function() {
			$(".tooltip ").hide();
		},
		onLoadSuccess: function(data){
			if(getEasyUiFieldValue())
			if(isEmpty(data)){
				return false;
			}
			$("#sqlPerformanceP").combobox("textbox").attr("placeholder",'??????');

			if(isNotEmpty(sql_auto_perf_check_id)){
				setEasyUiFieldValue("sqlPerformanceP",sql_auto_perf_check_id);
				bef_Selected_Perf_Check_Id = sql_auto_perf_check_id;
				getIndexRecommend();
				closeMessageProgress();
			}else if(isNotEmpty(bef_Selected_Perf_Check_Id)){
				setEasyUiFieldValue('sqlPerformanceP',bef_Selected_Perf_Check_Id);
			}


		}

	});
}

function initialAutoProcessRefresh() {
	$('#autoProcessRefresh').switchbutton({
		checked: false,
		onChange: function (checked) {

			setTimeout(function () {
				if (checked && $("#autoProcessRefreshYn").val() == "N") {
					$("#autoProcessRefreshYn").val("Y");

					if(checkRequiredFields(false) === false){
						$("#autoProcessRefreshYn").val("N");
						$('#autoProcessRefresh').switchbutton("uncheck");
						return;
					}else if (isEmpty(getEasyUiFieldValue("autoProcessRefresh_val"))){
						setEasyUiFieldValue("autoProcessRefresh_val","60")
					}

					$("#autoProcessRefresh_val").textbox({disabled: true});

					autoProcessRefresh_timeOut();
				} else {
					$("#autoProcessRefreshYn").val("N");
					$("#autoProcessRefresh_val").textbox({disabled: false});

					window.clearTimeout(autoProcessTimer);
				}
				setNumberBoxAttrByName('autoProcessRefresh_val','min',3);

			}, 500);
		}
	});
}

function autoProcessRefresh_timeOut(){
	let intSec = 0;

	// getCurrentCreatedIndexes($("#indexList").datagrid('getData'));
	getAutoIndexCreateHistory();

	if ( $("#autoProcessRefreshYn").val() == "Y" ) {
		intSec = strParseInt( getEasyUiFieldValue('autoProcessRefresh_val'),0 );
		autoProcessTimer = window.setTimeout("autoProcessRefresh_timeOut()",(intSec*1000));
	} else {
		window.clearTimeout(autoProcessTimer);
	}

}

function initialPerformanceAnalysisRefresh() {
	$('#performanceAnalysisRefresh').switchbutton({
		checked: false,
		onChange: function (checked) {

			setTimeout(function () {
				if (checked && $("#performanceAnalysisRefreshYn").val() == "N") {
					$("#performanceAnalysisRefreshYn").val("Y");

					if (isEmpty($("#search_projectId").val())) {
						$("#performanceAnalysisRefreshYn").val("N");
						$('#performanceAnalysisRefresh').switchbutton("uncheck");
						parent.$.messager.alert('??????', "????????? ?????? ?????? ?????? ??? ???????????????.");
						return;
					}

					$("#performanceAnalysisTimer_value").textbox({disabled: true});

					performanceAnalysisRefresh_timeOut();
				} else {
					$("#performanceAnalysisRefreshYn").val("N");
					$("#performanceAnalysisTimer_value").textbox({disabled: false});

					window.clearTimeout(performanceAnalysisTimer);
				}
				setNumberBoxAttrByName('performanceAnalysisTimer_value','min',3);
			}, 500);
		}
	});
}

function performanceAnalysisRefresh_timeOut(){
	let intSec = 0;

	getPerformanceAnalysis();

	if ( $("#performanceAnalysisRefreshYn").val() == "Y" ) {
		intSec = strParseInt( getEasyUiFieldValue('performanceAnalysisTimer_value'),0 );
		performanceAnalysisTimer = window.setTimeout("performanceAnalysisRefresh_timeOut()",(intSec*1000));
	} else {
		window.clearTimeout(performanceAnalysisTimer);
	}

}

function initializeAddDelTab(){

	let addTable = "<div style='padding-top:0px;height:80px;'><table id='addList' class='tbl easyui-datagrid' data-options='fit:true,border:true' style='height:80px;width:99%;'><tbody></tbody></table></div>";

	$("#addDelTab").tabs({
		headerWidth:"35",
		headerHeight:"120px",
		tabPosition:'left',
		tabWidth:"49px"
	});

	$("#addDelTab").tabs('add',{
		title:'<p style="height:12px">???</p><p>???</p><p>&nbsp/</p><p>???</p><p>???</p>',
		content:addTable,
		colsable:true
	});


	createAddList();
}

function createList() {
	$("#indexList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'chk',halign:"center",align:"center",checkbox:"true",rowspan:'2'},
			{field:'INDEX_CREATE_YN',title:'???????????????',width:'4%',halign:'center',align:'center',rowspan:'2'},
			{field:'LAST_RECOMMEND_TYPE_NM',title:'?????? ?????? ??????<br>?????? ??????',width:'8%',halign:'center',align:'left',rowspan:'2'},
			{title:'DB',width:'12%',halign:'center',align:'left',colspan:2},
			{field:'TABLE_OWNER',title:'OWNER',width:'6%',halign:'center',align:'left',rowspan:'2'},
			{field:'TABLE_NAME',title:'????????????',width:'15%',halign:'center',align:'left',rowspan:'2'},
			{field:'RECOMMEND_TYPE',title:'????????????',width:'6%',halign:'center',align:'center',rowspan:'2'},
			{field:'ACCESS_PATH_COLUMN_LIST',title:'?????? ????????? ??????',width:'15%',halign:'center',align:'left',rowspan:'2',formatter:getNumberFormatNullChk},
			{field:'ACCESS_PATH_USED_CNT',title:'ACCESS<br>PATH ???',width:'5%',halign:'center',align:'right',rowspan:'2'},
			{field:'SOURCE_INDEX_NAME',title:'ASIS ????????? ???',width:'15%',halign:'center',align:'left',rowspan:'2'},
			{field:'SOURCE_INDEX_COLUMN_LIST',title:'ASIS ????????? ??????',width:'10%',halign:'center',align:'left',rowspan:'2'},
			],[
			{field:'ASIS_DB_NAME',title:'ASIS',width:'6%',halign:'center',align:'left'},
			{field:'TOBE_DB_NAME',title:'TOBE',width:'6%',halign:'center',align:'left'},
			{field:'IDX_AD_NO',hidden:'true'},
			{field:'DBID',hidden:'true'},
			{field:'EXEC_SEQ',hidden:'true'},
			{field:'ASIS_DBID',hidden:'true'},
			{field:'TOBE_DBID',hidden:'true'},
			{field:'IDX_RECOMMEND_SEQ',hidden:'true'},
			{field:'INDEX_NAME',hidden:'true'},
			{field:'PERF_CHECK_RANGE_BEGIN_DT',hidden:'true'},
			{field:'PERF_CHECK_RANGE_END_DT',hidden:'true'},
			{field:'SOURCE_INDEX_OWNER',hidden:'true'},
		]],
		onCheckAll:function (rows) {
			let obj = this;
			rows.forEach(function (data, idx){

				let opts = $(obj).datagrid('options');
				let disabled = opts.finder.getTr(obj,idx).find('input[type=checkbox]').attr('disabled');
				if(disabled === 'disabled'){
					$("#indexList").datagrid('uncheckRow',idx);
				}

			})
			$('.datagrid-header-check > input').prop('checked','check');
			isCheckedAll = true;
		},
		onUncheckAll:function (rows){
			isCheckedAll = false;
		},
		onBeforeCheck:function (index,row){
			if(row.RECOMMEND_TYPE === "UNUSED"){
				return false;
			}
		},
		onUncheck:function(rows){
			isCheckedAll = false;
		},
		onCheck:function (rows){
			let checkedAll = $('.datagrid-header-check > input').prop('checked');
			if(checkedAll === true){
				$('.datagrid-header-check > input').prop('checked',false);
			}
		},
		onSelect:function( index, row ) {
			let opts = $(this).datagrid('options');
			let disabled = opts.finder.getTr(this,index).find('input[type=checkbox]').attr('disabled');
			if(disabled === 'disabled'){
				$("#indexList").datagrid('uncheckRow',index);
			}
		},
		onClickRow: function(index,row){
			if(isEmpty(row.RECOMMEND_TYPE) || row.RECOMMEND_TYPE === 'UNUSED'){
				return false;
			}
			let jsonData = makeJsonData(row);
			openSqlInfoPopup($('#search_projectId').val(), $('#search_sqlPerformanceP').val(), jsonData);
		},
		onLoadSuccess:function(data) {

			if(isEmpty(data)||isEmpty(data.rows)||isEmpty(data.rows[0].INDEX_CREATE_YN)){
			}else{
				disableUnusedRow(this,data);
			}

			if( isNotEmpty($('.datagrid-header-check > input')) === true ){
				if( isCheckedAll === true ){
					$('.datagrid-header-check > input').click();
				}
			}

		},
		onLoadError:function() {
			closeMessageProgress();
			parent.$.messager.alert('??????','????????? ?????? ?????? ????????? ?????????????????????.','error');
		}
	});
}

function disableUnusedRow(obj, data){
	let opts = $(obj).datagrid('options');
	let len = data.rows.length;

	for(let iter = 0 ; iter < len ; iter ++){
		if(data.rows[iter].RECOMMEND_TYPE === "UNUSED"){
			opts.finder.getTr(obj,iter).find('input[type=checkbox]').attr('disabled','disabled');
		}
	}

}

function makeJsonData(row){
	let jsonData;
	
	if( isNotEmpty(row) ){
		jsonData = {
			  "table_owner": row.TABLE_OWNER
			, "table_name": row.TABLE_NAME
			, "asis_dbid": row.ASIS_DBID	// ASIS_DBID ??? ?????? ??????
			, "exec_seq": row.EXEC_SEQ
			, "access_path": row.ACCESS_PATH_COLUMN_LIST
			, "perf_check_range_begin_dt": row.PERF_CHECK_RANGE_BEGIN_DT
			, "perf_check_range_end_dt": row.PERF_CHECK_RANGE_END_DT
		};
		
	}else {
		jsonData = null;
	}
	
	return jsonData;
}

function createRecommendIdxAddList(isInitialized) {
	$("#recommendIdxAddList").datagrid({
		view: myview,
		singleSelect: false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{title:'?????? ????????? ?????? ??????(UNUSED ??????)',halign:'center',align:'center',colspan:4},
			],[
			{field:'total',title:'??????',width:'23%',halign:'center',align:'right'},
			{field:'created',title:'??????',width:'23%',halign:'center',align:'right'},
			{field:'not_created',title:'?????????',width:'23%',halign:'center',align:'right'},
			{field:'tablespace_used',title:'????????? ???????????? ?????????(GB)',width:'33%',halign:'center',align:'right'},
		]],
		onSelect:function( index, row ) {
		},
		onLoadSuccess:function(data) {
			if(isEmpty(data.rows)){
				return false;
			}
			if(isInitialized === false){

			}else{
				getAutoIndexCreateHistory();
			}
		},
		onLoadError:function() {
			closeMessageProgress();
			parent.$.messager.alert('??????','????????? ?????? ?????? ????????? ?????????????????????.','error');
		}
	});
}

function createAddList() {
	$("#addList").datagrid({
		view: myview,
		singleSelect: false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'exec_status',title:'????????????',width:'22%',halign:'center',align:'center',rowspan:2, styler:cellStyler},
			{title:'????????????(????????? ???)',halign:'center',align:'center',colspan:3},
			{field:'complete_percent',title:'?????????(%)',width:'7%',halign:'center',align:'center',rowspan:2, styler:style_Gradient},
			{field:'exec_start_dt',title:'??????????????????',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_end_dt',title:'??????????????????',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'exec_time',title:'????????????',width:'10%',halign:'center',align:'center',rowspan:2},
			{field:'user_nm',title:'?????????',width:'11%',halign:'center',align:'center',rowspan:2},
			{field:'history',title:'????????????',width:'7%',halign:'center',align:'center',rowspan:2},
			],[
			{field:'idx_db_work_cnt',title:'??????',width:'7%',halign:'center',align:'right'},
			{field:'completed_cnt',title:'??????',width:'7%',halign:'center',align:'right'},
			{field:'err_cnt',title:'??????',width:'7%',halign:'center',align:'right'},
			{field:'idx_db_work_id',hidden:'true'},
			]],
			onSelect:function( index, row ) {
			},
			onLoadSuccess:function(data) {
				if(isEmpty(data.rows)){
					return false;
				}
			},
			onLoadError:function() {
				closeMessageProgress();
				parent.$.messager.alert('??????','????????? ?????? ?????? ????????? ?????????????????????.','error');
			}
	});
}


function cellStyler( value,row,index ) {
	if(isNotEmpty(value)) {
		if (row.exec_status.indexOf('????????????') > -1) {
			return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
		} else if (row.exec_status.indexOf('??????') > -1) {
			return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
		} else if (isEmpty(row.exec_status)) {
			return '';
		} else {
			return 'background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
		}
	}
}

function cellStyler2( value,row,index ) {
	if(isNotEmpty(value)) {
		if (row.exec_status.indexOf('????????????') > -1) {
			return 'color:red; background-image:url(/resources/images/forceperformence.png);background-repeat: no-repeat;background-position-x: right;';
		} else if ( row.exec_status === '??????' ) {
			return 'color:blue; background-image:url(/resources/images/success.png);background-repeat: no-repeat;background-position-x: right;';
		} else if (isEmpty(row.exec_status)) {
			return '';
		} else {
			return 'background-image:url(/resources/images/performing.png);background-repeat: no-repeat;background-position-x: right;';
		}
	}
}


function style_Gradient(value,row,index){
	let color = '#ffe48d';
	return 'background: linear-gradient(to right, #ffffff 0%,'+ color + ' ' + '0%, white ' + value + '%)';
}
function createPerfAnalysisList() {
	$("#perfAnalysisList").datagrid({
		view: myview,
		singleSelect: false,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'exec_status',title:'????????????',width:'11%',halign:'center',align:'center',rowspan:2 , styler:cellStyler2},
			{title:'1??? ??????(SQL ???) - ??????',halign:'center',align:'center',colspan:3},
			{title:'2??? ??????(SQL ???) - ??????',halign:'center',align:'center',colspan:3},
			{field:'complete_percent',title:'????????? (%)',width:'6%',halign:'center',align:'center',rowspan:2,styler:style_Gradient},
			{field:'perf_check_exec_begin_dt',title:'??????????????????',width:'8%',halign:'center',align:'center',rowspan:2},
			{field:'perf_check_exec_end_dt',title:'??????????????????',width:'8%',halign:'center',align:'center',rowspan:2},
			{field:'exec_time',title:'????????????',width:'8%',halign:'center',align:'center',rowspan:2},
			{field:'ERR_MSG',title:'??????',width:'19%',halign:'center',align:'center',rowspan:2},
			{field:'moveToOtherTab',title:'????????????',width:'5%',halign:'center',align:'center',rowspan:2},
			],[
				{field:'total_cnt_1st',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'completed_cnt_1st',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'err_cnt_1st',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'total_cnt_2nd',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'completed_cnt_2nd',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'err_cnt_2nd',title:'??????',width:'6%',halign:'center',align:'right'},
				{field:'sql_auto_perf_check_id',hidden:'true'},
				{field:'project_id',hidden:'true'},

				{field:'parallel_degree',hidden:'true'},
				{field:'dml_exec_yn',hidden:'true'},
				{field:'multiple_exec_cnt',hidden:'true'},
				{field:'multiple_bind_exec_cnt',hidden:'true'},
				{field:'sql_time_limt_cd',hidden:'true'},
				{field:'sql_time_direct_pref_value',hidden:'true'},
				{field:'max_fetch_cnt',hidden:'true'}

			]],
			onResizeColumn:function(){
				let div = $("#perfAnalysisList_datagrid-row-r9-2-0 td:last div");
				if(isNotEmpty(div)){
					div = div[0];
					if(div.scrollWidth > div.offsetWidth){
						if(isNotEmpty($("#perfAnalysisList").datagrid('getData').rows[0])){
						div.setAttribute('title',$("#perfAnalysisList").datagrid('getData').rows[0].ERR_MSG);
						}
					}else{
						div.removeAttribute('title');
					}
				}
			},
			onSelect:function( index, row ) {
			},
			onLoadSuccess:function(data) {
				if(isEmpty(data.rows)){
					return false;
				}

				let row = data.rows[0];

				// {field:'parallel_degree',hidden:'true'},
				// {field:'dml_exec_yn',hidden:'true'},
				// {field:'multiple_exec_cnt',hidden:'true'},
				// {field:'multiple_bind_exec_cnt',hidden:'true'},
				// {field:'sql_time_limt_cd',hidden:'true'},
				// {field:'sql_time_direct_pref_value',hidden:'true'},
				// {field:'max_fetch_cnt',hidden:'true'}

				if(isNotEmpty(row.parallel_degree)){
					if(row.parallel_degree === '1'){
						setEasyUiFieldValue('parallel_degree_yn','uncheck');
					}else{
						setEasyUiFieldValue('parallel_degree_yn','check');
						setEasyUiFieldValue('parallel_degree',row.parallel_degree);
					}
				}
				if(isNotEmpty(row.dml_exec_yn)){
					if(row.dml_exec_yn === 'Y'){
						setEasyUiFieldValue('dml_exec_yn','check');
					}else{
						setEasyUiFieldValue('dml_exec_yn','uncheck');
					}
				}
				if(isNotEmpty(row.multiple_exec_cnt)){
					if(row.multiple_exec_cnt === '1'){
						setEasyUiFieldValue('multi_execution','uncheck');
					}else{
						setEasyUiFieldValue('multi_execution','check');
						setEasyUiFieldValue('multiple_exec_cnt',row.multiple_exec_cnt);

					}
				}
				if(isNotEmpty(row.multiple_bind_exec_cnt)){
					if(row.multiple_bind_exec_cnt === '1'){
						setEasyUiFieldValue('multi_bind_execution','uncheck');
					}else{
						setEasyUiFieldValue('multi_bind_execution','check');
						setEasyUiFieldValue('multiple_bind_exec_cnt',row.multiple_bind_exec_cnt);

					}
				}
				if(isNotEmpty(row.sql_time_limt_cd)){
					if(row.sql_time_limt_cd === '99'){
						$('#sql_time_limt_cd').combobox('select','');
						$('#sql_time_limt_cd').combobox('setText',row.sql_time_direct_pref_value);

					}else{
						$('#sql_time_limt_cd').combobox('select',row.sql_time_limt_cd);
					}
				}
				if(isEmpty(row.max_fetch_cnt)){
					setEasyUiFieldValue('max_fetch_cnt','100000');
				}else{
					setEasyUiFieldValue('max_fetch_cnt',row.max_fetch_cnt);
				}

				let div = $("#perfAnalysisList_datagrid-row-r9-2-0 td:last div");
				if(isNotEmpty(div)){
					div = div[0];
					if(div.scrollWidth > div.offsetWidth){
						div.setAttribute('title',row.ERR_MSG);
					}
				}


			},
			onLoadError:function() {
				closeMessageProgress();

				parent.$.messager.alert('??????','????????? ?????? ?????? ????????? ?????????????????????.','error');
			}
	});
}

function getIndexRecommend(){
	isCheckedAll = false;
	$("#currentPage").val("1");

	if(checkRequiredFields(true) === false){
		return false;
	}

	resetPerformanceAnalysisSwitches();
	resetSearchResult();
	resetRefresh();
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("????????? ?????? ?????? ?????? ??? ?????????."," ");
	setSearchData();
	ajaxCall("/AISQLPVIndexRecommend/getIndexRecommend",
				$("#searchForm"),
				"POST",
				callback_GetIndexRecommend);
}

function callback_GetIndexRecommend(result){
	$('#currentPage').val(1);
	$('#pagePerCount').val(20);

	$("#search_currentPage").val($('#currentPage').val());
	$("#search_pagePerCount").val($('#pagePerCount').val());

	if(isEmpty(result)){
		closeMessageProgress();
		parent.$.messager.alert('??????', "?????? ????????? ?????? ?????? ??? ????????? ?????????????????????.");
		return false;
	}

	let jsonData = JSON.parse(result);

	setIndexRecommendData(jsonData.rows[0]);

	let temp = {};
	temp.rows = new Array();

	if(isEmpty(jsonData.rows[0])){
		closeMessageProgress();
		fnEnableDisablePagingBtn(0);
		json_string_callback_with_emptyMsg_common(JSON.stringify(temp), '#indexList', true, '????????? ????????? ?????? ?????? ????????? ????????????.');
		return false;
	}else if(isEmpty(jsonData.rows[0]) || jsonData.rows[0].recommend_index_cnt === '0'){
		closeMessageProgress();
		json_string_callback_with_emptyMsg_common(JSON.stringify(temp), '#indexList', true, '????????? ?????? ?????? ?????? ?????? ???????????? ????????????.');
		return false;
	}

	if(checkIndexRecommendProgress(result)){
		getIndexDataList(false);
	}
}

function getCurrentCreatedIndexes(callbackFunc, type){
	if(isEmpty(type)){
		$("#recommendIdxAddList").datagrid("loading");
	}

	let jsonData = {};

	jsonData.perf_check_target_dbid = $("#perf_check_target_dbid").val();
	jsonData.idx_ad_no = $('#idx_ad_no').val();

	form = createDynamicJqueryForm(jsonData);

	ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/getCurrentCreatedIndexes",
			form,
			"POST",
		callbackFunc,
		type);
}

function callback_GetCurrentCreatedIndexes(result){

	if(checkErr(result, '?????? ????????? ?????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		$("#recommendIdxAddList").datagrid("loaded");
		return false;
	}

	let jsonData = JSON.parse(result);

	let resultJson = {};
	resultJson.rows = [];

	let data = {};
	data.total = jsonData.recommendIndexState.total;
	data.created = jsonData.recommendIndexState.created;
	data.not_created = jsonData.recommendIndexState.not_created;
	data.tablespace_used = jsonData.recommendIndexState.tablespace_used;

	resultJson.rows[0] = data;
	resultJson.result = true;

	json_string_callback_common(JSON.stringify(resultJson),'#recommendIdxAddList',true);
	$("#recommendIdxAddList").datagrid("loaded");

}

function checkIndexRecommendProgress(result){
	let jsonData = JSON.parse(result);
	if( isEmpty(jsonData.rows[0]) ){
		closeMessageProgress();
		json_string_callback_with_emptyMsg_common(result, '#indexList', true, '????????? ????????? ?????? ?????? ????????? ????????????.');
		return false;
	}else if( jsonData.rows[0].recommend_index_cnt === '0' ){
		closeMessageProgress();
		json_string_callback_with_emptyMsg_common(result, '#indexList', true, '????????? ?????? ?????? ?????? ?????? ???????????? ????????????.');
		return false;
	}
	return true;
}

//?????? ????????? ?????? ?????????
function getIndexDataList(paging){
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("????????? ?????? ?????? ?????? ??? ?????????."," ");
	ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/getIndexDataList",
		$("#searchForm"),
		"POST",
		callback_GetIndexDataList,
		paging);
}

function callback_GetIndexDataList(result, paging){

	if(checkErr(result, '????????? ?????? ?????? ?????? ?????? ?????? ??? ????????? ?????????????????????.') === false){
		return false;
	}

	let jsonData = JSON.parse(result);

	let dataLength = 0;
	if(isEmpty(jsonData.recommendIndexList)){
		dataLength = 0
		$("#indexList").datagrid("loadData",[]);
		closeMessageProgress();
		return false;
	}else{
		dataLength = jsonData.recommendIndexList.length;
	}

	fnEnableDisablePagingBtn(dataLength);

	let data = getJsonParameter(jsonData);

	if(data.rows.length > $("#search_pagePerCount").val()) {
		data.rows.splice(data.rows.length -1 , 1)
	}

	json_string_callback_common(JSON.stringify(data), '#indexList', true);
	if(paging === false) {
		getCurrentCreatedIndexes(callback_GetCurrentCreatedIndexes, null);
		getAutoIndexCreateHistory();
		getPerformanceAnalysis();
	}
	closeMessageProgress();

}

function formValidationCheck(){return true;}

function fnSearch(){

	$("#search_currentPage").val($('#currentPage').val());
	$("#search_pagePerCount").val($('#pagePerCount').val());

	getIndexDataList(true);
}
function getJsonParameter(jsonData){
	let rows = jsonData.recommendIndexList;
	let retJson = {};
	retJson.result = true;
	retJson.rows = rows;
	return retJson;
}


function setIndexRecommendData(jsonData){

	let running_table_cnt = 0;
	let recommend_index_cnt = 0;
	let recommend_index_add_cnt = 0;
	let recommend_index_modify_cnt = 0;
	let recommend_index_unused_cnt = 0;

	let idx_ad_no = 0;
	let perf_check_target_dbid = 0;
	let original_dbid = 0;
	let exec_seq = 0;
	let tobe_db_name = '';

	if(isNotEmpty(jsonData)){
		running_table_cnt = jsonData.running_table_cnt;
		recommend_index_cnt = jsonData.recommend_index_cnt;
		recommend_index_add_cnt = jsonData.recommend_index_add_cnt;
		recommend_index_modify_cnt = jsonData.recommend_index_modify_cnt;
		recommend_index_unused_cnt = jsonData.recommend_index_unused_cnt;
		idx_ad_no = jsonData.idx_ad_no;
		perf_check_target_dbid = jsonData.perf_check_target_dbid;
		original_dbid = jsonData.original_dbid;
		exec_seq = jsonData.exec_seq;
		tobe_db_name = jsonData.tobe_db_name;
	}

	$("#running_table_cnt").text(running_table_cnt);
	$("#recommend_index_cnt").text(recommend_index_cnt);
	$("#recommend_index_add_cnt").text(recommend_index_add_cnt);
	$("#recommend_index_modify_cnt").text(recommend_index_modify_cnt);
	$("#recommend_index_unused_cnt").text(recommend_index_unused_cnt);
	$("#idx_ad_no").val(idx_ad_no);
	$("#perf_check_target_dbid").val(perf_check_target_dbid);
	$('#original_dbid').val(original_dbid);
	$('#exec_seq').val(exec_seq);
	$('#tobe_db_name').val(tobe_db_name);
}

function gridWide(){
	let wideMod = $('.wideMod');
	let narrowMod = $('.narrowMod');
	let mainGrid = $('.mainGrid');
	let childrenEle = mainGrid.children()[0];
	if(isNotEmpty(wideMod)){
		let className = wideMod.attr('class');
		let bottomHeight = 0 ;
		let height = 0;

		bottomHeight = $(".bottomGrid")[0].offsetHeight;
		bottomHeight = bottomHeight + $(".bottomGrid")[1].offsetHeight;

		height = (jQuery(childrenEle).css('height').replace('px','') ) * 1 ;
		jQuery(childrenEle).css('min-height' , (height * 1 + bottomHeight) + 'px');

		wideMod.attr('class',className.replace('wideMod','narrowMod'));
		$(".bottomGrid").css('display','none');

		$('#indexList').datagrid('resize')

	}
	else {
		let className = narrowMod.attr('class');
		$(".bottomGrid").css('display','block');
		jQuery(childrenEle).css('min-height' ,  '0px');
		narrowMod.attr('class',className.replace('narrowMod','wideMod'));
		$('#indexList').datagrid('resize')
	}

	$("#addDelTab").tabs('resize');

}

function getAutoIndexCreateHistory(){
	$("#addList").datagrid("loading");

	let url = "/AISQLPVIndexRecommend/getAutoIndexCreateHistory";

	ajaxCall(url,
			$("#searchForm"),
			"POST",
			callback_GetAutoIndexCreateHistory);

}

function callback_GetAutoIndexCreateHistory(result){

	if(checkErr(result, '?????? ????????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		$("#indexList").datagrid("loaded");
		setEasyUiFieldValue('autoProcessRefresh','uncheck');
		return false;
	}

	let jsonData = JSON.parse(result);

	if(isNotEmpty(jsonData.rows)){
		if(jsonData.rows[0].tot_cnt > 1){
			jsonData.rows[0].history =
				'<a href="javascript:;" onclick="setAddOrDelHistoryList(CREATE_HISTORY_ALL);"><img src="/resources/images/report.png" style="height:15px;vertical-align:middle;"/></a>';
		}
		if(jsonData.rows[0].err_cnt > 0){
			jsonData.rows[0].err_cnt = '<div style="cursor:pointer" onclick="setAutoErrorPop_addList()">' + jsonData.rows[0].err_cnt + '</div>';
			// jsonData.rows[0].err_cnt = '<div onclick="setIndexRecommendPopup('+ ADD_DEL_ERR_POP +')">' + jsonData.rows[0].err_cnt + '<a onclick="alert(1)"> (????????????)</a></div>';

		}
	}
	json_string_callback_with_emptyMsg_common(JSON.stringify(jsonData),'#addList',true,'?????? ????????? ??????/?????? ????????? ????????????.');
	$("#addList").datagrid("loaded");

	if($("#autoProcessRefreshYn").val() == "Y") {
		if (isNotEmpty(jsonData.rows) && isNotEmpty(jsonData.rows[0]) && isNotEmpty(jsonData.rows[0].exec_status)) {
			if(bef_auto_create_drop_history !== jsonData.rows[0].exec_status){
				if(isNotEmpty(jsonData.rows[0].exec_end_dt) && isNotEmpty(bef_auto_create_drop_history)) {
					$("#autoProcessRefresh").switchbutton('uncheck');
					if(autoProcessForcePop){
						autoProcessForcePop = null;
					}else{
						let param = "??????";
						let msgStr = jsonData.rows[0].exec_status.indexOf('??????')>-1 ?
							'????????????????????? ????????? ?????????????????????.' :
							'????????????????????? ????????? ?????????????????????.'
						parent.$.messager.alert(param, msgStr,'',function(){getIndexRecommend()});
					}
				}
			}
			bef_auto_create_drop_history = jsonData.rows[0].exec_status;
		}
	}
}

function showIndexRecommendPopup(type){

	let checkedAll = $('.datagrid-header-check > input').prop('checked');
	let isCreate = true;
	let text = "";
	let action = '';
	let msg = '????????? ???????????? ????????? ?????????';
	let msg2 = '';
	let recommendType = $('#search_recommendType').val();
	let indexAdd = $('#search_indexAdd').val();

	let checkedRow = $('#indexList').datagrid('getChecked');

	if(checkedAll === false && isEmpty(checkedRow)){
		$('#indexList').datagrid('checkAll');
		// closeMessageProgress();
		// parent.$.messager.alert('??????','????????? ???????????? ????????????.');
		// return false;
	}

	switch (type){
		case CREATE_INDEX_SCRIPT :
			text = "????????? ?????? ???????????? ????????? ?????? ?????? ?????? ????????????.";
			action = '??????????????? ????????????'
			break;
		case AUTO_CREATE_INDEX :
			text = "????????? ?????? ?????? ????????? ?????? ?????? ?????? ????????????.";
			action = '????????? ?????? ??????'
			break;
		case DROP_INDEX_SCRIPT :
			text = "????????? ?????? ???????????? ????????? ?????? ?????? ?????? ????????????.";
			msg = '????????? ???????????? ????????? ?????????';
			action = '????????? ?????? ????????????'
			isCreate = false;
			break;
		case AUTO_DROP_INDEX :
			text = "????????? ?????? ?????? ????????? ?????? ?????? ?????? ????????????.";
			msg = '????????? ???????????? ????????? ?????????';
			action = '????????? ?????? ??????'
			isCreate = false;
			break;

		default : return false;
	}


	if (parent.openMessageProgress != undefined) parent.openMessageProgress(text," ");
	if(checkRequiredFields(false) === false){
		return false;
	}

	// if(isEmpty($('#indexList').datagrid('getData').rows)){
	// 	closeMessageProgress();
	// 	parent.$.messager.alert('??????', msg);
	// 	return false;
	// }
	//
	// if(checkedAll === false) {
	// 	if (isEmpty($("#indexList").datagrid('getChecked'))) {
	// 		closeMessageProgress();
	// 		parent.$.messager.alert('??????', msg);
	// 		return false;
	// 	}
	// }


	if(isCreate) {
		if(isNotEmpty(recommendType) && recommendType === 'UNUSED') {
			msg2 = '?????? ?????? [????????????] ??? "UNUSED"??? ?????? ' + action + '??? ????????? ??? ????????????.';
		}else if( isNotEmpty(indexAdd) && indexAdd === 'Y') {
			msg2 = '?????? ?????? [???????????????] ??? "Y"??? ?????? ' + action + '??? ????????? ??? ????????????.';
		}else{
			getExcuteAnalyzeConstraint(callback_CheckCreateIndexScript, type);
			return false;
		}

	}else{
		if(isNotEmpty(recommendType) && recommendType === 'UNUSED') {
			msg2 = '?????? ?????? [????????????] ??? "UNUSED"??? ?????? ' + action + '??? ????????? ??? ????????????.';
		}else if( isNotEmpty(indexAdd) && indexAdd === 'N') {
			msg2 = '?????? ?????? [???????????????] ??? "N"??? ?????? ' + action +'??? ????????? ??? ????????????.';
		}else{
			getExcuteAnalyzeConstraint(checkDropIndexScript, type);
			return false;
		}
	}
	closeMessageProgress();
	parent.$.messager.alert('??????', msg2);
	return false;
}


function checkDropIndexScript(result, type) {

	if( checkExcuteConstarint(result, type) ){
		getCurrentCreatedIndexes( checkCurrentCreatedIndexes_CreatedIndexCount, type);

	}
}

function checkCurrentCreatedIndexes_CreatedIndexCount(result ,type){

	let jsonData = JSON.parse(result);

	let total = jsonData.recommendIndexState.total

	if(total === 0){
		closeMessageProgress();
		parent.$.messager.alert('??????','????????? ?????? ?????? ?????? ?????? ???????????? ????????????.');
		return false;
	}
	if(checkErr(result, '?????? ????????? ?????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		return false;

	}
	let cnt = jsonData.recommendIndexState.created;

	 if(isNotEmpty(cnt) && cnt > 0){
		 checkIndexCreate_getIndexRecommed(type);
	 }else{
	 	closeMessageProgress();
		parent.$.messager.alert('??????', "????????? ???????????? ????????????.");
		return false;
	 }
}

function callback_CheckCreateIndexScript(result, type) {
	if( checkExcuteConstarint(result, type) ){
		checkIndexCreate_getIndexRecommed(type);
	}
}

function checkIndexCreate_getIndexRecommed(type){
	let param = "??????";
	let msgStr = "???????????????????????? ?????????????????????????";

	if(type === AUTO_DROP_INDEX){
		parent.$.messager.confirm({
			title :  param,
			msg : msgStr,
			onClose:function(){
				closeMessageProgress();
				return false;
			},
			fn : function (r) {
				if (r) {
					ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/getIndexRecommend",
								$("#searchForm"),
								"POST",
								callback_CheckIndex_getIndexRecommed,
								type);
					return false;
				}else{
					closeMessageProgress();
					return false;
				}
			}
		});
	}else{
		ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/getIndexRecommend",
					$("#searchForm"),
					"POST",
					callback_CheckIndex_getIndexRecommed,
					type);
		return false;
	}
	return false;

}

function callback_CheckIndex_getIndexRecommed(result, type){
	let checkedAll = $('.datagrid-header-check > input').prop('checked');

	let param = "??????";
	let msgStr ="?????? ???????????? ?????????????????????. ???????????? ?????? ????????? ???????????? ?????? ????????? ????????? ?????? ????????????. ?????? ?????????????????????????";

	if(type === DROP_INDEX_SCRIPT || type === AUTO_DROP_INDEX) {
		msgStr ="?????? ???????????? ?????????????????????. ?????? ?????? ????????? ?????????????????? ?????? ????????? ????????? ?????? ????????????. ?????? ?????????????????????????";
	}

	let jsonData = JSON.parse(result);

	if( jsonData.rows[0].recommend_index_cnt === '0' ){
		closeMessageProgress();
		parent.$.messager.alert('??????','????????? ?????? ?????? ?????? ?????? ???????????? ????????????.');
		return false;
	}

	closeMessageProgress();

	if ( checkedAll === false) {
		if ( isEmpty($("#indexList").datagrid('getChecked')) === false) {
			closeMessageProgress();
			parent.$.messager.confirm({
				title : param,
				msg : msgStr,
				onClose:function(){
					closeMessageProgress();
					return false;
				},
				fn :function (r) {
					if (r) {
						if( type === DROP_INDEX_SCRIPT ) {
							CURRENT_POP = DROP_INDEX_SCRIPT;
							getScript(type);
							return false;
						}else if( type !== AUTO_DROP_INDEX){
							setIndexRecommendPopup(type);
							return false;
						} else{
							excuteAutoIndexDrop(type);
							return false;
						}
					} else {
					}
				}
			});
		}
	} else {
		if( type === DROP_INDEX_SCRIPT ) {
			CURRENT_POP = DROP_INDEX_SCRIPT;
			getScript(type);
			return false;
		}else if( type !== AUTO_DROP_INDEX){
			setIndexRecommendPopup(type);
			return false;
		} else{
			excuteAutoIndexDrop(type);
			return false;
		}
	}
}

function excuteAutoIndexDrop(){

	let rows = $("#indexList").datagrid('getChecked');

	let form = setAutoGenerateORDropIndexForm(rows, 'D');

	let url = '/AISQLPVIndexRecommend/autoIndexDrop';

	ajaxCall(url,
				form,
				"POST",
				callback_excuteAutoIndexDrop);

}

function callback_excuteAutoIndexDrop(result){

	if(checkErr(result, '????????? ?????? ?????? ????????? ????????? ??????????????????.') === false){
				return false;
	}

	let jsonData = JSON.parse(result);

	if(isNotEmpty(jsonData.target_list_yn) && jsonData.target_list_yn === 'N'){
		closeMessageProgress();
		parent.$.messager.alert('??????','?????? ????????? ???????????? ????????????.');
		return false;
	}

	closeMessageProgress();

	setTimeout(function() {
		if(getEasyUiFieldValue('autoProcessRefresh')){
			getAutoIndexCreateHistory();
		}else{
			setEasyUiFieldValue('autoProcessRefresh','check')
		}
	},350);


}
function forceCompleteAuto(){
	if (parent.openMessageProgress != undefined) parent.openMessageProgress("????????????????????? ???????????? ?????? ????????? ?????? ????????????."," ");

	getExcuteAnalyzeConstraint(executeForceCompleteAuto, FORCE_COMPLET_AUTO);
}

function executeForceCompleteAuto(result, type){

	if(checkRequiredFields(false) === false){
		return false;
	}

	if (isEmpty($("#search_projectId").val())) {
		$("#performanceAnalysisRefreshYn").val("N");
		$('#performanceAnalysisRefresh').switchbutton("uncheck");
		parent.$.messager.alert('??????', "????????? ?????? ?????? ?????? ??? ???????????????.");
		$("#perfAnalysisList").datagrid("loaded");
		return;
	}

	if( checkExcuteConstarint(result , type) === true){

		closeMessageProgress();
		parent.$.messager.alert('??????','???????????? ????????? ????????????.');
		return false;

	}else{

		let jsonData = {};

		jsonData.perf_check_target_dbid = $('#perf_check_target_dbid').val();
		jsonData.exec_seq = $('#exec_seq').val();
		jsonData.original_dbid = $('#original_dbid').val();
		let rows = JSON.parse(result).rows[0];

		type = rows.index_db_drop_exec_yn === 'Y' ?
				FORCE_DROP : FORCE_CREATE;
		jsonData.type = type;

		form = createDynamicJqueryForm(jsonData);

		let url = '/AISQLPVIndexRecommend/forceCompleteAuto';
		ajaxCall(url,
					form,
					"POST",
					callback_ExecuteForceCompleteAuto);
	}

}

function callback_ExecuteForceCompleteAuto(result){
	closeMessageProgress();
	if(checkErr(result, '?????? ?????? ????????? ????????? ??????????????????.') === false){
		return false;
	}else{
		closeMessageProgress();
		autoProcessForcePop = parent.$.messager.alert('??????','?????? SQL???????????? ?????? ?????? ?????? ????????? ?????????????????????.','info',function(){getIndexRecommend();});
	}
}

function checkExcuteConstarint(result , type) {
	result = JSON.parse(result);
	let key = '';

	if (result.result === 'false') {
		closeMessageProgress();
		parent.$.messager.alert('??????', "?????? ????????? ?????? ??? ????????? ?????????????????????.");
	}

	let rows = result.rows[0];

	if (isEmpty(rows)) {
		closeMessageProgress();
		parent.$.messager.alert('??????', "????????? ????????? ?????? ?????? ????????? ????????????.");
	} else {

		switch (type){
			case AUTO_CREATE_INDEX :
			case CREATE_INDEX_SCRIPT :
				if (isNotEmpty(rows.index_recommend_end_yn) && rows.index_recommend_end_yn === 'N') {
					key = 'index_recommend_end_yn';
					break;
				} else if (isNotEmpty(rows.acces_path_exec_yn) && rows.acces_path_exec_yn === 'Y') {
					key = 'acces_path_exec_yn';
					break;
				}
			case EXCUTE_ANALYZE_CONSTRAINT:
			case AUTO_DROP_INDEX :
			case DROP_INDEX_SCRIPT :
				if (isNotEmpty(rows.index_recommend_exec_yn) && rows.index_recommend_exec_yn === 'Y') {
					key = 'index_recommend_exec_yn';
					break;
				}
			case FORCE_COMPLET_AUTO :
				if (isNotEmpty(rows.index_db_create_exec_yn) && rows.index_db_create_exec_yn === 'Y') {
					key = 'index_db_create_exec_yn';
					break;
				} if (type !== FORCE_COMPLET_AUTO &&
							isNotEmpty(rows.perf_check_exec_yn) && rows.perf_check_exec_yn === 'Y') {
					key = 'perf_check_exec_yn';
					break;
				} if (type !== EXCUTE_ANALYZE_CONSTRAINT &&
							isNotEmpty(rows.index_db_drop_exec_yn) && rows.index_db_drop_exec_yn === 'Y') {
					key = 'index_db_drop_exec_yn';
					break;
				}
		}
		if( isNotEmpty(key) ){
			if(type !== FORCE_COMPLET_AUTO){
				closeMessageProgress();
				parent.$.messager.alert('??????', getConstraintErrMsg(type)[key]);
			}
			return false;
		}
		return true;
	}
}

function sqlPerfPacReload( projectId, sqlPerfId ) {
	let currentSqlPackId = $('#sqlPerformanceP').combobox('getValue');

	if(projectId === getEasyUiFieldValue('project_id') && sqlPerfId === getEasyUiFieldValue('sqlPerformanceP')) {
			setEasyUiFieldValue('sqlPerformanceP', '');
	}

	if ( currentSqlPackId == sqlPerfId ) {
		$('#sqlPerformanceP').combobox('clear');

	}else {
		$('#sqlPerformanceP').combobox('setValue', currentSqlPackId);
	}
}

function setSearchData(){
	$("#search_projectId").val(getEasyUiFieldValue('project_id'));
	$("#search_sqlPerformanceP").val(getEasyUiFieldValue('sqlPerformanceP'));
	$("#search_sqlPerformanceP_Name").val($('#sqlPerformanceP').combobox('getText'));

	$("#search_owner").val(getEasyUiFieldValue('owner'));
	$("#search_tableName").val(getEasyUiFieldValue('tableName'));
	$("#search_recommendType").val(getEasyUiFieldValue('recommendType'));
	$("#search_spsRecommendIndex").val(getEasyUiFieldValue('spsRecommendIndex'));
	$("#search_indexAdd").val(getEasyUiFieldValue('indexAdd'));

	$("#search_currentPage").val($('#currentPage').val());
	$("#search_pagePerCount").val($('#pagePerCount').val());

	$("#idx_ad_no").val('');
	$("#perf_check_target_dbid").val('');
	$("#package_tableName").val('');
	$('#original_dbid').val('');
	$('#exec_seq').val('');
	$('#tobe_db_name').val('');

}

function moveFromOtherTab( obj ) {
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("????????? ?????? ????????? ?????? ????????????."," ");
	setProjectIdCombobox(obj);
}

function closeMessageProgress() {
	if (isNotEmpty(parent.closeMessageProgress)) {
		parent.closeMessageProgress();
	}
	if (isNotEmpty(parent.parent.closeMessageProgress)) {
		parent.parent.closeMessageProgress();
	}
}

function moveToOtherTab(project_id, sql_auto_perf_check_id, database_kinds_cd, owner, tableName) {

	let parameter =  '';
		parameter += 'project_id=' + project_id;
		parameter += '&sql_auto_perf_check_id=' + sql_auto_perf_check_id;
		parameter += '&database_kinds_cd=' + database_kinds_cd;
		parameter += '&owner_list=' + owner;
		parameter += '&table_name_list=' + tableName;
		
	setTimeout(function() {
		parent.moveToOtherTab2( 2, 'perfImpactByIndex', parameter);
	},150);
}

function checkRequiredFields(isSearch){
	let fields = $('#submit_form .required');
	let len = fields.length;
	if(isSearch === true) {
		for (let idx = 0; idx < len; idx++) {
			if (isEmpty(getEasyUiFieldValue(fields[idx].id))) {
				if (jQuery(fields[idx])[getEasyUiFieldType(fields[idx].id)]('options').readonly === false) {
					closeMessageProgress();
					parent.$.messager.alert('??????', fields[idx].getAttribute('requiredMsg'));
					return false;
				}
			}
		}
	}else{
		if(isEmpty($("#search_projectId").val())){
			closeMessageProgress();
			parent.$.messager.alert('??????', "????????? ?????? ?????? ?????? ??? ???????????????.");
			return false;
		}
	}

	return true;
}


function getPerformanceAnalysis(){

	if(checkRequiredFields(false) === false){
		$("#performanceAnalysisRefreshYn").val("N");
		$('#performanceAnalysisRefresh').switchbutton("uncheck");
		$("#perfAnalysisList").datagrid("loaded");
		return false;
	}

	$("#perfAnalysisList").datagrid("loading");

	ajaxCall("/AISQLPVIndexRecommend/getPerformanceAnalysis",
			$("#searchForm"),
			"POST",
			callback_GetPerformanceAnalysis);
}

function callback_GetPerformanceAnalysis(result){
	let isEnd = false;
	let projectId = $('#search_projectId').val();
	let sql_auto_perf_check_id = $('#search_sqlPerformanceP').val();
	let database_kinds_cd = $('#database_kinds_cd').val();

	let moveToParam = projectId + ',' + sql_auto_perf_check_id + ',\'' + database_kinds_cd + '\',\'\',\'\'';

	if(checkErr(result, '?????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		$("#addList").datagrid("loaded");
		$("#perfAnalysisList").datagrid("loaded");
		setEasyUiFieldValue('performanceAnalysisRefresh','uncheck');
		return false;
	}
	let jsonData = JSON.parse(result);

	if($("#performanceAnalysisRefreshYn").val() == "Y" && isNotEmpty(jsonData.rows[0])) {
		let status = jsonData.rows[0].exec_status;

		if(isNotEmpty(bef_performance_analyze) && bef_performance_analyze !== status){
			if(isNotEmpty(jsonData.rows[0].perf_check_exec_end_dt)){
				isEnd = true;
				$("#performanceAnalysisRefresh").switchbutton('uncheck');
				$("#performanceAnalysisRefreshYn").val("N");
				if(performanceAnalysisForcePop){
					performanceAnalysisForcePop = null;
		}else{
					let param = "??????";
					let msgStr ="?????? ?????? ????????? ?????????????????????.";

					closeMessageProgress();
					parent.$.messager.alert(param, msgStr,'',function(){
							getIndexRecommend();
							if(status.indexOf('??????') < 0 &&
								(isNotEmpty(jsonData.rows[0].total_cnt_2nd) && jsonData.rows[0].total_cnt_2nd * 1 !== 0) ) {
								setTimeout(function () {
									moveToOtherTab(projectId, sql_auto_perf_check_id, database_kinds_cd);
								}, 350)
							}
						}
					);
				}
			}
		}
		bef_performance_analyze = jsonData.rows[0].exec_status;
	}

	if(isEnd === false){

		if(isNotEmpty(jsonData.rows) && isNotEmpty(jsonData.rows[0].perf_check_exec_end_dt)){
			jsonData.rows[0].moveToOtherTab =
				'<a href="javascript:;" ' +
				'onclick="moveToOtherTab(' + moveToParam + ');">' +
				'<img src="/resources/images/report.png" style="height:15px;vertical-align:middle;"/></a>';
			if(isNotEmpty(jsonData.rows[0]) && isNotEmpty(jsonData.rows[0].complete_percent)){
				jsonData.rows[0].complete_percent = Number(jsonData.rows[0].complete_percent);
			}
			result = JSON.stringify(jsonData);
		}

		json_string_callback_with_emptyMsg_common(result, '#perfAnalysisList', true, '?????? ?????? ????????? ????????????.');
		$("#perfAnalysisList").datagrid("loaded");
	}
}

function excutePerformanceAnalysis(){

	autoCreateList = '';
	if (parent.openMessageProgress != undefined) parent.openMessageProgress("?????? ????????? ?????? ????????? ??????????????????.", " ");

	if(checkRequiredFields(false) === false){
		$("#performanceAnalysisRefreshYn").val("N");
		$('#performanceAnalysisRefresh').switchbutton("uncheck");
		return;
	}

	getExcuteAnalyzeConstraint(callback_ExcutePerformanceAnalysis);

}

function callback_ExcutePerformanceAnalysis(result){

	if( checkExcuteConstarint(result, EXCUTE_ANALYZE_CONSTRAINT) ) {
		let resultJson = JSON.parse(result);

		if(isNotEmpty(resultJson.rows[0]) && resultJson.rows[0].perf_check_end_yn === 'Y'){
			let param = "??????";
			let msgStr = "????????? ?????? ????????? ?????? ?????? ????????? ????????????. ???????????? ?????? ????????? ???????????????. ?????? ?????????????????????????";

			parent.$.messager.confirm({
				title :  param,
				msg : msgStr,
				onClose:function(){
					closeMessageProgress();
					return false;
				},
				fn : function (r) {
					if (r) {
						OK_PERF_CHECK_END_YN_POP = true;
						callCurrentCreatedIndexes()
					}else{
						closeMessageProgress();
						return false;
					}
				}
			});
		}else{
			callCurrentCreatedIndexes()
		}
	}
}

function callCurrentCreatedIndexes(){
	let jsonData = {};

	jsonData.perf_check_target_dbid = $("#perf_check_target_dbid").val();
	jsonData.idx_ad_no = $('#idx_ad_no').val();

	let form = createDynamicJqueryForm(jsonData);

	ajaxCall("/AISQLPVIndexRecommend/getCurrentCreatedIndexes",
			form,
			"POST",
			excutePerformanceAnalysis_createdIndexCheck);
}

function callback_GetVisibleIndexInfo(result, showConfirm){

	if(checkErr(result, 'VISIBLE ????????? ?????? ??? ????????? ??????????????????.') === false){
		$("#recommendIdxAddList").datagrid("loaded");
		OK_PERF_CHECK_END_YN_POP = false;
		return false;
	}

	let jsonData = JSON.parse(result);

	if(isNotEmpty(jsonData.invisible_index_list)){
		autoCreateList = jsonData.invisible_index_list;
		setIndexRecommendPopup(VISIBLE_INDEX);
		let str = '';

		str = jsonData.invisible_index_list.join(';\n');

		setEasyUiFieldValue("scriptArea",str + ';');
	}else{
		if(showConfirm && OK_PERF_CHECK_END_YN_POP === false){
			let param = "??????";
			let msgStr = "SQL ????????? - [" +$('#search_sqlPerformanceP_Name').val() + "] ????????? ?????? ????????? ?????? ???????????????????";
			OK_PERF_CHECK_END_YN_POP = false;

			parent.$.messager.confirm({
				title :  param,
				msg : msgStr,
				onClose:function(){
					closeMessageProgress();
					return false;
				},
				fn : function (r) {
					if (r) {
						setExcuteAnalyzeSqlAutoPerfChk();
					}
				}
			});
		}else{
			setExcuteAnalyzeSqlAutoPerfChk();
		}
	}

}

function callback_SetCreateIndexYN(result){
	if(checkErr(result, '?????? ????????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		$("#recommendIdxAddList").datagrid("loaded");
		return false;
	}
	let jsonData = JSON.parse(result);

	let row = $("#indexList").datagrid('getData').rows[0]

	jsonData.project_id = $('#search_projectId').val();
	jsonData.sql_auto_perf_check_id = $('#search_sqlPerformanceP').val();
	jsonData.idx_ad_no = $('#idx_ad_no').val();
	jsonData.perf_check_target_dbid = $('#perf_check_target_dbid').val();

	let form = createDynamicJqueryForm(jsonData);

	ajaxCallWithTimeout("/AISQLPVIndexRecommend/getPerfChkAutoIndexingV2",
		form,
		"POST",
		function (){return false;},
		0
	);
	$("#recommendIdxAddList").datagrid("loaded");

	setTimeout(function() {
		if(getEasyUiFieldValue('performanceAnalysisRefresh')){
			getPerformanceAnalysis();
		}else{
			$(' #performanceAnalysisRefresh').switchbutton("check");
		}
	},350);
}
//
// function callback_GetPerfChkAutoIndexingV2(result){
// 	if(checkErr(result, '?????? ????????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
// 		$("#recommendIdxAddList").datagrid("loaded");
// 		return false;
// 	}
// }

function excelDownload(){
	let url = '/AISQLPVIndexRecommend/excelDownload';

	if(checkRequiredFields(false) === false){
		return;
	}

	$("#searchForm").attr("action",url);
	$("#searchForm").submit();
	$("#searchForm").attr("action","");

}
function resetRefresh(){
	setEasyUiFieldValue('autoProcessRefresh','uncheck');
	setEasyUiFieldValue('performanceAnalysisRefresh','uncheck');
	setEasyUiFieldValue('autoProcessRefresh_val','60');
	setEasyUiFieldValue('performanceAnalysisTimer_value','60');
}
function resetSearchResult(){
	$("#running_table_cnt").text('');
	$("#recommend_index_cnt").text('');
	$("#recommend_index_add_cnt").text('');
	$("#recommend_index_modify_cnt").text('');
	$("#recommend_index_unused_cnt").text('');

	$("#indexList").datagrid('loadData',[]);
	$("#addList").datagrid('loadData',[]);
	$("#delList").datagrid('loadData',[]);

	$("#recommendIdxAddList").datagrid('loadData',[]);
	$("#perfAnalysisList").datagrid('loadData',[]);

}

function getExcuteAnalyzeConstraint(callback, type){
	let url = '/AISQLPVAnalyze/getExcuteAnalyzeConstraint';
	ajaxCallWithCallbackParam(url,
		$("#searchForm"),
		"POST",
		callback,
		type);
}

// function getRecommendIndexDbYn(result, type){
// 	if( checkExcuteConstarint(result , type) ){
//
// 		ajaxCall("/AISQLPVAnalyze/getRecommendIndexDbYn",
// 		$("#searchForm"),
// 				"POST",
// 			callback_GetRecommendIndexDbYn);
// 	}
// }
//
// function callback_GetRecommendIndexDbYn(result){
// 	if(checkErr(result, '????????? ?????? ?????? ?????? ??? ????????? ?????????????????????.') === false){
// 		return false;
// 	}
//
// 	let jsonData = JSON.parse(result);
//
// 	if(jsonData.use_yn === 'Y'){
// 		parent.$.messager.alert('??????', "????????? ?????? ?????? ????????? ???????????? 1??? ????????? ?????? ???????????? DB??? ?????????????????????. ?????? ???????????? ????????? ?????? ?????? ??? ??? ????????????.");
// 		closeMessageProgress();
// 		return false;
// 	}
//
// 	getPerformanceAalnysis();
//
// }

function Btn_ForceUpdateSqlAutoPerformanceCheck() {
	if(checkRequiredFields(false) === false){
		return;
	}
	ajaxCall("/AISQLPVIndexRecommend/countExecuteTms",
			$("#searchForm"),
			"POST",
			callback_ForceCountExecuteTmsAction);
}

var callback_ForceCountExecuteTmsAction = function(result) {

	if( isEmpty(result) ){
		closeMessageProgress();
		parent.$.messager.alert('??????', "?????? ?????? ?????? ?????? ?????? ??? ????????? ??????????????????.");
		return false;
	}

	if ( result.txtValue == 'false' ) {

		// ???????????? ??????
		let param = "??????";
		let msgStr = "SQL ????????? - [" +$('#search_sqlPerformanceP_Name').val() + "] ?????? ?????? ?????? ????????? ?????????????????? ???????????????????";

		parent.$.messager.confirm({
			title : param,
			msg : msgStr,
			onClose:function(){
				closeMessageProgress();
				return false;
			},
			fn : function(r) {
				if (r) {
					/* modal progress open */
					if (parent.openMessageProgress != undefined) parent.openMessageProgress("??????????????????", "?????? ?????? ?????? ????????????.");

					ajaxCall("/AISQLPVIndexRecommend/forceUpdateSqlAutoPerformance",
						$("#searchForm"),
						"POST",
						callback_ForceUpdateSqlAutomaticPerformanceCheckAction);
				}
			}
		});
	} else {
		parent.$.messager.alert('??????','?????? SQL??????????????? ?????? ?????? ????????? ????????????.','info');
	}
}

var callback_ForceUpdateSqlAutomaticPerformanceCheckAction = function(result) {
	if(result.result === false){
		return false;
	}
	if ( result.result === true) {
		closeMessageProgress();
		performanceAnalysisForcePop = parent.$.messager.alert('??????','?????? SQL???????????? ?????? ?????? ?????? ????????? ?????????????????????.','info',function(){getIndexRecommend()});
	} else {
		closeMessageProgress();
		parent.$.messager.alert('??????','?????????????????? ?????? ???????????????.' , 'info');
		return false;
	}

	getPerformanceAnalysis();
}

function Btn_AuthorityScript() {
	if( checkRequiredFields(false) === false ){
		return false;
	}

	setEasyUiFieldValue( 'db', $('#tobe_db_name').val() );
	$('#dbid').val( $('#perf_check_target_dbid').val() );

	$('#checkTable').checkbox('reset');
	$('#checkView').checkbox('reset');
	$('#checkSequence').checkbox('reset');
	$('#checkFunction').checkbox('reset');
	$('#checkProcedure').checkbox('reset');
	$('#checkPackage').checkbox('reset');

	$('#scriptView').val('');
	LoadUserName();

	$('#authorityScriptPop').window({
		title : "??????????????????",
		top:getWindowTop(400),
		left:getWindowLeft(500)
	});

	$('#authorityScriptPop').window("open");

}


function setExcuteAnalyzeSqlAutoPerfChk(){
	let url = "/AISQLPVIndexRecommend/setExcuteAnalyzeSqlAutoPerfChk";
	let jsonData = {};

	jsonData.parallel_degree = getEasyUiFieldValue('parallel_degree');
	if(getEasyUiFieldValue('dml_exec_yn') === false){
		jsonData.dml_exec_yn = 'N';
	}else{
		jsonData.dml_exec_yn = 'Y';
	}

	jsonData.multiple_exec_cnt = getEasyUiFieldValue('multiple_exec_cnt');
	jsonData.multiple_bind_exec_cnt = getEasyUiFieldValue('multiple_bind_exec_cnt');
	jsonData.sql_time_limt_cd = getEasyUiFieldValue('sql_time_limt_cd');
	jsonData.sql_time_direct_pref_value = $('#sql_time_limt_cd').combobox('textbox').val();
	jsonData.max_fetch_cnt = getEasyUiFieldValue('max_fetch_cnt');
	jsonData.project_id = $('#search_projectId').val();
	jsonData.sql_auto_perf_check_id = $('#search_sqlPerformanceP').val();

	let form = createDynamicJqueryForm(jsonData);

	ajaxCall(url,
			form,
			"POST",
			callback_SetExcuteAnalyzeSqlAutoPerfChk);
}

function callback_SetExcuteAnalyzeSqlAutoPerfChk(result){

	if(checkErr(result, '?????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		return false;
	}

	let jsonData = {}

	let row = $("#indexList").datagrid('getData').rows[0]

	jsonData.perf_check_target_dbid = $("#perf_check_target_dbid").val();
	jsonData.idx_ad_no = $("#idx_ad_no").val();

	let form = createDynamicJqueryForm(jsonData);

	ajaxCall("/AISQLPVIndexRecommend/setCreateIndexYN",
			form,
			"POST",
		callback_SetCreateIndexYN);

}

function excutePerformanceAnalysis_createdIndexCheck(result){

	if(checkErr(result, '????????? ?????? ?????? ?????? ??? ????????? ??????????????????.') === false){
		OK_PERF_CHECK_END_YN_POP = false;
		return false;
	}

	let jsonData = JSON.parse(result);


	let created ='';
	let not_created = '';

	created = jsonData.recommendIndexState.created;
	not_created = jsonData.recommendIndexState.not_created;

	if( isEmpty(created) || created * 1 === 0 ){
		closeMessageProgress();
		parent.$.messager.alert('??????', "????????? ???????????? ????????????. ?????? ???????????? ?????? ?????? ????????? ?????? ??? ?????? ?????????, ?????? ?????? ?????? ????????? ????????? ??? ????????????.");

		if(isNotEmpty(jsonData.err_msg)){
			console.log(jsonData.err_msg);
		}
		OK_PERF_CHECK_END_YN_POP = false;

		return false;

	}else if( not_created > 0 ) {
		let param = "??????";
		let msgStr = "?????? ????????? ????????? ???????????? ???????????????. ???????????? ?????? ????????? ???????????? ?????? ????????? ?????? ????????? ?????? ????????????. ?????? ?????????????????????????";

		parent.$.messager.confirm({
			title : param,
			msg : msgStr,
			onClose:function(){
				closeMessageProgress();
				OK_PERF_CHECK_END_YN_POP = false;
				return false;
			},
			fn : function (r) {
				if (r === true) {
					getVisibleIndexInfo(false);
					return false;
				} else {
					closeMessageProgress();
					OK_PERF_CHECK_END_YN_POP = false;
					return false;
				}
			}
		});
	}else{
		getVisibleIndexInfo(true);
	}
}

function getVisibleIndexInfo(showConfirm){
	closeMessageProgress();

	let formData = {};
	let row = $("#indexList").datagrid('getData').rows[0];

	formData.perf_check_target_dbid = row.TOBE_DBID;
	formData.idx_ad_no = row.IDX_AD_NO;
	formData.table_owner = row.TABLE_OWNER;
	formData.table_name = row.TABLE_NAME;

	let form = createDynamicJqueryForm(formData);

	ajaxCallWithCallbackParam("/AISQLPVIndexRecommend/getVisibleIndexInfo",
		form,
		"POST",
		callback_GetVisibleIndexInfo,
		showConfirm);
}
function initializeDmlExecSwitch(){
	// DML ??????
	$('#dml_exec_yn').switchbutton({
		checked: true,
		onText:'YES',
		offText:'NO',
		value:'Y'
	});
}

function initializeperformanceAnalyzeSwitchButtons(switchEle, textEle, isReadOnly){

	let switchObj = $('#' + switchEle);
	let textObj = $('#' + textEle);

	switchObj.switchbutton({
		checked: true,
		onText:'YES',
		offText:'NO',
		onChange: function( checked ) {
			setTimeout(function() {

				let readOnly = true;
				let val = "1";

				if ( checked ) {
					readOnly = false;
					val = "4";
				}

				textObj.textbox("readonly",readOnly);

				if(readOnly === false && textObj.textbox('getValue') !== '1' ){
					return false;
				}

				textObj.textbox("setValue",val);

				},100);
		}
	});

	textObj.textbox("readonly",isReadOnly);

}

function initialzeSqlTimeLimitCombo(){

	$("#sql_time_limt_cd").combobox({
		url:"/Common/getCommonCode?grp_cd_id=1082",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess:function( item ) {
			$('#sql_time_limt_cd').combobox('select',item[0].cd);
		},
		onChange:function( newValue, oldValue ) {
			changeSqlTimeLimitCodeCombo(newValue, oldValue);
		},
		onClick:function(row){
			if(row.cd ==='99'){
				row.cd_nm = '';
			}
		}
	});

	setSqlTimeLimitCodeComboConstaint();
}

function setSqlTimeLimitCodeComboConstaint(){
	$(".sql_time_limt_cd .textbox").keyup(function() {
		if (floatCheck($(".sql_time_limt_cd .textbox-text").val()) == false) {
			parent.$.messager.alert('??????', 'SQL Time Limit??? ???????????? ????????? ??? ????????????.', 'warning');
			$("#sql_time_limt_cd").combobox("setValue", "99");
			return false;
		}

		if ($("#sql_time_limt_cd").combobox("getText") != "?????????" &&
				$("#sql_time_limt_cd").combobox("getText") != "" &&
				/^(\-|\+)?([0-9]+)$/.test($("#sql_time_limt_cd").combobox("getText")) == false) {

			parent.$.messager.alert('??????', 'SQL Time Limit??? 1?????? ????????? ????????? ??? ????????????.', 'warning');
			$("#sql_time_limt_cd").combobox("setValue", "");
			return false;
		}
	});
}

function changeSqlTimeLimitCodeCombo(newValue, oldValue){
	if ( newValue === "99" ) {
		oldValue === "99" ?
			$("#sql_time_limt_cd").combobox("setText","99") :
			$('#sql_time_limt_cd').select('select','');

		$("#sql_time_direct_pref_value").val("99");
		$('#sql_time_limt_cd').combobox('textbox').attr('readonly',false);
		$('#sql_time_limt_cd').combobox('textbox').val('');
		return ;
	} else if( newValue == "98" ) {
		setEasyUiFieldValue('select','98');
		$("#sql_time_direct_pref_value").val( "" );
	}
	$('#sql_time_limt_cd').combobox('textbox').attr('readonly',true)
}

function floatCheck( obj ) {
	let fCheck = /^[0-9]{1,3}[\.][0-9]{0,3}?$/;

	if ( fCheck.test( obj ) ) {
		return false;
	} else {
		return true;
	}
}

function getConstraintErrMsg(type){

	let errMsg =
				{
					'index_recommend_end_yn' : '????????? ?????? ?????? ????????? ???????????? ???????????????. ?????? ?????? ??? ?????? ????????????' ,
					'acces_path_exec_yn' : '?????? ????????? ?????? ?????? ????????? ??????(ACCESS PATH ??????) ??? ?????????. ?????? ?????? ??? ?????? ????????????.' ,
					'index_recommend_exec_yn' :
						type === AUTO_CREATE_INDEX || CREATE_INDEX_SCRIPT ?
							'?????? ????????? ?????? ?????? ????????? ??????(????????? ?????? ??????) ??? ?????????. ?????? ?????? ??? ?????? ????????????.' :
							'????????? ?????? ?????? ?????? ????????????. ?????? ?????? ??? ?????? ????????????.'
					,
					'index_db_create_exec_yn' :
						type === AUTO_CREATE_INDEX || CREATE_INDEX_SCRIPT ?
							'????????? ?????? ?????? ????????? ?????? ????????????.' :
							'????????? ?????? ?????? ?????? ????????????. ?????? ?????? ??? ?????? ????????????.'
					,
					'index_db_drop_exec_yn' :
						type === AUTO_CREATE_INDEX || CREATE_INDEX_SCRIPT ?
							'????????? ?????? ?????? ????????? ?????? ????????????. ?????? ?????? ??? ?????? ????????????.' :
							'????????? ?????? ?????? ?????? ????????????.'
					 ,
					'perf_check_exec_yn' : '?????? ????????? ?????? ????????? ?????? ????????????. ?????? ?????? ??? ?????? ????????????.' ,
				};

	return errMsg;
}

function checkErr(result, msg){
	try{
		if( isEmpty(result) ){
			closeMessageProgress();
			parent.$.messager.alert('??????', msg);
			return false;
		}

		let jsonData = JSON.parse(result);

		if(( isNotEmpty(jsonData.is_error) && jsonData.is_error.toString() === 'true' ) ||
			( isNotEmpty(jsonData.result) && jsonData.result.toString() === 'false' ) ){

			closeMessageProgress();
			parent.$.messager.alert('??????', msg);

			if(isNotEmpty(jsonData.err_msg)){
				console.log(jsonData.err_msg);
			}

			return false;
		}
	}catch (ex){
		closeMessageProgress();
		parent.$.messager.alert('??????', msg);
		return false;
	}

	return true;
}
function resetPerformanceAnalysisSwitches(){
	setEasyUiFieldValue('parallel_degree_yn','check');
	setEasyUiFieldValue('multi_execution','check');
	setEasyUiFieldValue('multi_bind_execution','check');
	setEasyUiFieldValue('dml_exec_yn','check');
	$('#sql_time_limt_cd').combobox('select','01');
	setEasyUiFieldValue('max_fetch_cnt','100000');
}


function setNumberBoxAttrByName(name, str, value){
	let obj = document.getElementsByName(name);

	if(isNotEmpty(obj)){
		obj = obj[0];
	}else{
		return false;
	}

	if(isNotEmpty(obj.parentElement) && isNotEmpty(obj.parentElement.firstChild)){
		obj.parentElement.firstChild.setAttribute(str,value);
	}
}