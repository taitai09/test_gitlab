const emptyData = [];
const chartTextStyle = '10px "Lucida Grande", "Lucida Sans Unicode", Verdana, Arial, Helvetica, sans-serif';
var chartPerfImpactPanel;
var chartElapsedTimePanel;
var chartBufferGetsPanel;
var chartPerfFitPanel;

$(document).ready(function(){
	$('body').css('visibility', 'visible');
	
	createProjectCombo();
	createSqlPackCombo();
	
	createCheckBoxes();
	setPlaceholder();
	initPerfCheckResult();
	
	createPerfImpactChart( emptyData );
	createElapsedTimeChart( emptyData );
	createBufferGetsChart( emptyData );
	createPerfFitChart( emptyData );
	
	createList();
	
	createPopUp();
	createAsisTree();
	createBindList();
	
	/* modal progress close */
	if ( parent.parent.closeMessageProgress != undefined ) parent.parent.closeMessageProgress();
});

function createProjectCombo(){
	$('#project_combo').combobox({
		url:'/AutoPerformanceCompareBetweenDatabase/getProjectList',
		method:'get',
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(data) {
			$('[name=project_id]').val(data.project_id);
			$('#sqlPack_combo').combobox('clear');
		},
		onLoadError: function(){
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}

function createSqlPackCombo(){
	$('#sqlPack_combo').combobox({
		valueField:'sql_auto_perf_check_id',
		textField:'perf_check_name',
		onChange: function(){
			$('[name=sql_auto_perf_check_id]').val('');
		},
		onShowPanel: function(){
			ajaxCall_loadSqlPCombo();
		},
		onLoadError: function(){
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
			return false;
		}
	});
}

function createCheckBoxes(){
	$('#select_yn').checkbox({
		checked: true,
		onChange:function( checked ) {
			if ( checked == false ){
				if ( $('#dml_yn').checkbox('options').checked == false ) {
					$('#select_yn').checkbox('check');
				}
			} else {
				return;
			}
		}
	});
	
	$('#dml_yn').checkbox({
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				if ( $('#select_yn').checkbox('options').checked == false ) {
					$('#dml_yn').checkbox('check');
				}
			} else {
				return;
			}
		}
	});
	
	$('#all_sql_yn').checkbox({
		checked: true,
		onChange:function( checked ) {
			if ( checked ) {
				$('#perf_down_yn').checkbox('uncheck');
				$('#notPerf_yn').checkbox('uncheck');
				$('#fullScan_yn').checkbox('uncheck');
				$('#partition_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#perf_down_yn').checkbox({	// 성능저하 check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#notPerf_yn').checkbox({	// 성능 부적합 check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#error_yn').checkbox({	// 오류 check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#perf_down_yn').checkbox('uncheck');
				$('#notPerf_yn').checkbox('uncheck');
				$('#fullScan_yn').checkbox('uncheck');
				$('#partition_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				$('#error_yn').checkbox('check');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false &&
						$('#notPerf_yn').checkbox('options').checked == false &&
						$('#fullScan_yn').checkbox('options').checked == false &&
						$('#partition_yn').checkbox('options').checked == false &&
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#all_sql_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
				
				$('#error_yn').checkbox('uncheck');
			}
		}
	});
	
	$('#fullScan_yn').checkbox({	// FULL SCAN check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#partition_yn').checkbox({	// PARTITION ALL ACCESS check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#timeOut_yn').checkbox({		// TIME-OUT check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#perf_down_yn').checkbox('uncheck');
				$('#notPerf_yn').checkbox('uncheck');
				$('#fullScan_yn').checkbox('uncheck');
				$('#partition_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('check');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
	
	$('#maxFetch_yn').checkbox({	// MAX FETCH 초과 check
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				$('#all_sql_yn').checkbox('uncheck');
				$('#error_yn').checkbox('uncheck');
				$('#perf_down_yn').checkbox('uncheck');
				$('#notPerf_yn').checkbox('uncheck');
				$('#fullScan_yn').checkbox('uncheck');
				$('#partition_yn').checkbox('uncheck');
				$('#timeOut_yn').checkbox('uncheck');
				$('#maxFetch_yn').checkbox('check');
				
			} else {
				if ( $('#perf_down_yn').checkbox('options').checked == false && 
						$('#notPerf_yn').checkbox('options').checked == false && 
						$('#fullScan_yn').checkbox('options').checked == false && 
						$('#partition_yn').checkbox('options').checked == false && 
						$('#timeOut_yn').checkbox('options').checked == false && 
						$('#maxFetch_yn').checkbox('options').checked == false && 
						$('#error_yn').checkbox('options').checked == false ) {
					
					$('#all_sql_yn').checkbox('check');
				}
			}
		}
	});
}

function setPlaceholder(){
	$('#buffer_gets_1day').textbox('textbox').focus(function(){
		$('#buffer_gets_1day').numberbox('textbox').attr('placeholder','');
	});
	$('#buffer_gets_1day').textbox('textbox').blur(function(){
		$('#buffer_gets_1day').numberbox('textbox').attr('placeholder','1000');
	});
	
	$('#asis_elapsed_time').textbox('textbox').focus(function(){
		$('#asis_elapsed_time').textbox('textbox').attr('placeholder','');
		
		if ( $('#asis_elapsed_time').textbox('getValue') < 0 ) {
			$('#asis_elapsed_time').textbox('setValue','');
		}
	});
	$('#asis_elapsed_time').textbox('textbox').blur(function(){
		$('#asis_elapsed_time').textbox('textbox').attr('placeholder','3');
		
		if ( $('#asis_elapsed_time').textbox('getValue') < 0 ) {
			$('#asis_elapsed_time').textbox('setValue','');
		}
	});
	$('.asis_elapsed_time .textbox').keyup(function(){
		if ( $('#asis_elapsed_time').textbox('getValue') < 0 ) {
			$('#asis_elapsed_time').textbox('setValue','');
		}
	});
	
	$('#buffer_gets_regres').textbox('textbox').focus(function(){
		$('#buffer_gets_regres').numberbox('textbox').attr('placeholder','');
	});
	$('#buffer_gets_regres').textbox('textbox').blur(function(){
		$('#buffer_gets_regres').numberbox('textbox').attr('placeholder','10');
	});
	
	$('#elapsed_time_regres').textbox('textbox').focus(function(){
		$('#elapsed_time_regres').textbox('textbox').attr('placeholder','');
		if ( $('#elapsed_time_regres').textbox('getValue') < 0 ) {
			$('#elapsed_time_regres').textbox('setValue','');
		}
	});
	$('#elapsed_time_regres').textbox('textbox').blur(function(){
		$('#elapsed_time_regres').textbox('textbox').attr('placeholder','10');
		
		if ( $('#elapsed_time_regres').textbox('getValue') < 0 ) {
			$('#elapsed_time_regres').textbox('setValue','');
		}
	});
	$('.elapsed_time_regres .textbox').keyup(function(){
		if ( $('#elapsed_time_regres').textbox('getValue') < 0 ) {
			$('#elapsed_time_regres').textbox('setValue','');
		}
	});
}

function createPerfImpactChart( data ){
	if ( isNotEmpty(chartPerfImpactPanel) ) {
		chartPerfImpactPanel.destroy();
	}
	
	Ext.define('chartPerfImpactPanel.colors', {
		singleton: true,
		COLOR_01: '#01b0f1',
		COLOR_02: '#fe0000',
		COLOR_03: '#0170c1',
		COLOR_04: '#00af50',
		COLOR_05: '#ffd700'
	});
	
	chartPerfImpactPanel = Ext.create('Ext.panel.Panel',{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById('chartPerfImpactPanel'),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				label: {
					font: chartTextStyle
				}
			},{
				type : 'category',
				position : 'bottom',
				label: {
					font: chartTextStyle
				}
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_improved_title',
				yField : 'perf_impact_improved_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_01],
				label: {
					field: 'perf_impact_improved_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_regressed_title',
				yField : 'perf_impact_regressed_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_02],
				label: {
					field: 'perf_impact_regressed_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_unchanged_title',
				yField : 'perf_impact_unchanged_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_03],
				label: {
					field: 'perf_impact_unchanged_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_timeout_title',
				yField : 'perf_impact_timeout_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_04],
				label: {
					field: 'perf_impact_timeout_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_impact_fetch_exceed_title',
				yField : 'perf_impact_fetch_exceed_chart',
				colors: [chartPerfImpactPanel.colors.COLOR_05],
				label: {
					field: 'perf_impact_fetch_exceed_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			}]
		}]
	});
}

function createElapsedTimeChart( data ){
	if ( isNotEmpty(chartElapsedTimePanel) ) {
		chartElapsedTimePanel.destroy();
	}
	
	Ext.define('chartElapsedTimePanel.colors', {
		singleton: true,
		COLOR_01: '#00af50',
		COLOR_02: '#0170c1'
	});
	
	chartElapsedTimePanel = Ext.create('Ext.panel.Panel',{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById('chartElapsedTimePanel'),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				label: {
					font: chartTextStyle
				}
			},{
				type : 'category',
				position : 'bottom',
				label: {
					font: chartTextStyle
				}
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'before_elapsed_time_title',
				yField : 'before_elapsed_time_chart',
				colors: [chartElapsedTimePanel.colors.COLOR_01],
				label: {
					field: 'before_elapsed_time_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'after_elapsed_time_title',
				yField : 'after_elapsed_time_chart',
				colors: [chartElapsedTimePanel.colors.COLOR_02],
				label: {
					field: 'after_elapsed_time_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			}]
		}]
	});
}

function createBufferGetsChart( data ){
	if ( isNotEmpty(chartBufferGetsPanel) ) {
		chartBufferGetsPanel.destroy();
	}
	
	Ext.define('chartBufferGetsPanel.colors', {
		singleton: true,
		COLOR_01: '#00af50',
		COLOR_02: '#0170c1'
	});
	
	chartBufferGetsPanel = Ext.create('Ext.panel.Panel',{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById('chartBufferGetsPanel'),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				label: {
					font: chartTextStyle
				}
			},{
				type : 'category',
				position : 'bottom',
				label: {
					font: chartTextStyle
				}
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'before_buffer_gets_title',
				yField : 'before_buffer_gets_chart',
				colors: [chartBufferGetsPanel.colors.COLOR_01],
				label: {
					field: 'before_buffer_gets_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'after_buffer_gets_title',
				yField : 'after_buffer_gets_chart',
				colors: [chartBufferGetsPanel.colors.COLOR_02],
				label: {
					field: 'after_buffer_gets_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			}]
		}]
	});
}

function createPerfFitChart( data ){
	if ( isNotEmpty(chartPerfFitPanel) ) {
		chartPerfFitPanel.destroy();
	}
	
	Ext.define('chartPerfFitPanel.colors', {
		singleton: true,
		COLOR_01: '#fe0000',
		COLOR_02: '#00af50',
		COLOR_03: '#01b0f1',
		COLOR_04: '#0170c1',
		COLOR_05: '#7030a0'
	});
	
	chartPerfFitPanel = Ext.create('Ext.panel.Panel',{
		width : '100%',
		height : '100%',
		border : false,
		renderTo : document.getElementById('chartPerfFitPanel'),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			store : {
				data : data
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minimum: 0,
				label: {
					font: chartTextStyle
				}
			},{
				type : 'category',
				position : 'bottom',
				label: {
					font: chartTextStyle
				}
			}],
			series : [{
				type : 'bar',
				axis: 'left',
				xField : 'perf_chk_indc_n_title',
				yField : 'perf_chk_indc_n_chart',
				colors: [chartPerfFitPanel.colors.COLOR_01],
				label: {
					field: 'perf_chk_indc_n_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			},{
				type : 'bar',
				axis: 'left',
				xField : 'perf_chk_indc_y_title',
				yField : 'perf_chk_indc_y_chart',
				colors: [chartPerfFitPanel.colors.COLOR_02],
				label: {
					field: 'perf_chk_indc_y_chart',
					display: 'insideEnd',
					orientation: 'horizontal',
					font: chartTextStyle
				}
			}]
		}]
	});
}

function createList(){
	$('#tableList').datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'perf_impact_type_nm',title:'성능임팩트<br>유형',width:'5%',halign:'center',align:'center',rowspan:'2'},
			{field:'buffer_increase_ratio',title:'버퍼<br>임팩트(배)',width:'4%',halign:'center',align:'right', formatter:getNumberFormatNullChk,sortable:true,rowspan:'2',sorter:numberSorter},
			{field:'elapsed_time_increase_ratio',title:'수행시간<br>임팩트(배)',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,rowspan:'2',sorter:numberSorter},
			{field:'perf_check_result_yn',title:'성능점검<br>결과',width:'3%',halign:'center',align:'center',styler:cellStyler,rowspan:'2'},
			{field:'sql_id',title:'SQL ID',width:'6%',halign:'center',align:'left',rowspan:'2'},
			
			{title:'PLAN HASH VALUE',halign:'center',align:'right',colspan:2},
			{title:'EXECUTIONS',halign:'center',align:'right',colspan:2},
			{title:'ROWS PROCESSED',halign:'center',align:'right',colspan:2},
			{title:'ELAPSED TIME',halign:'center',align:'right',colspan:2},
			{title:'BUFFER GETS',halign:'center',align:'right',colspan:2},
			{title:'FULLSCAN YN',halign:'center',align:'right',colspan:2},
			{title:'PARTITION ALL ACCESS YN',halign:'center',align:'right',colspan:2},
			
			{field:'sql_command_type_cd',title:'SQL 명령 유형',width:'5%',halign:'center',align:'center',rowspan:'2'},
			{field:'parsing_schema_name',title:'PARSING<br>SCHEMA NAME',width:'6%',halign:'center',align:'left',rowspan:'2'},
			{field:'err_code',title:'에러코드',width:'5%',halign:'center',align:'left',rowspan:'2'},
			{field:'err_msg',title:'에러메시지',width: '10%',halign:'center',align:'left',rowspan:'2'},
			{field:'sql_text_web',title:'SQL TEXT',width: '15%',halign:'center',align:'left',rowspan:'2'},
			{field:'project_nm',title:'프로젝트명',width:'10%',halign:'center',align:'left',rowspan:'2'},
			{field:'perf_check_name',title:'SQL<br>점검팩명',width:'12%',halign:'center',align:'left',rowspan:'2'},
			],[
			{field:'asis_plan_hash_value',title:'ASIS',width:'5%',halign:'center',align:'right'},
			{field:'tobe_plan_hash_value',title:'TOBE',width:'5%',halign:'center',align:'right'},
			
			{field:'asis_executions',title:'ASIS',width:'3%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_executions',title:'TOBE',width:'3%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_rows_processed',title:'ASIS',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_rows_processed',title:'TOBE',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_elapsed_time',title:'ASIS',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_elapsed_time',title:'TOBE',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_buffer_gets',title:'ASIS',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			{field:'tobe_buffer_gets',title:'TOBE',width:'4%',halign:'center',align:'right',formatter:getNumberFormatNullChk,sortable:true,sorter:numberSorter},
			
			{field:'asis_fullscan_yn',title:'ASIS',width:'4%',halign:'center',align:'center'},
			{field:'tobe_fullscan_yn',title:'TOBE',width:'4%',halign:'center',align:'center'},
			
			{field:'asis_partition_all_access_yn',title:'ASIS',width:'5%',halign:'center',align:'center'},
			{field:'tobe_partition_all_access_yn',title:'TOBE',width:'5%',halign:'center',align:'center'}
		]],
		onSelect:function( index, row ) {
			showSqlTextPopup(row);
		},
		onLoadError:function(){
			errorMessager('데이터 조회 중 에러가 발생하였습니다.');
		},
	});
}

function ajaxCall_loadSqlPCombo(){
	$('#sqlPack_combo').combobox('loadData',[]);
	
	if ( $('#project_combo' ).combobox('getValue') != '' ){
		ajaxCall('/AISQLPV/getSqlPerformancePacList',
				$('#submit_form'), 
				'GET',
				callback_loadSqlPCombo);
	} else {
		return;
	}
}
var callback_loadSqlPCombo = function(result){
	try {
		let data = JSON.parse(result);
		$('#sqlPack_combo').combobox('loadData', data);
		
	} catch(err) {
		console.log("Error Occured", err.message);
	}
}

function Btn_OnClick(){
	if ( formValidationCheck() ) {
		$('#currentPage').val('1');
		$('#pagePerCount').val('40');
		
		setSearchData();
		
		$('#tableList').datagrid('loadData', []);
		
		ajaxCall_loadResultCount();
		ajaxCall_loadNumberOfSearch();
		ajaxCall_loadChartData();
		ajaxCall_loadResultList();
		
	}else {
		return;
	}
}

function fnSearch(){
	ajaxCall_loadResultList();
}

//수행 결과 조회
function ajaxCall_loadResultCount(){
	ajaxCall('/AISQLPV/loadResultCount',
			$('#submit_form'),
			'GET',
			callback_loadResultCount);
}
var callback_loadResultCount = function(rows) {
	initPerfCheckResult();
	
	try{
		if( isNotEmpty(rows) ){
			let data = JSON.parse(rows)[0];
			
			if( isNotEmpty(data) ){
				$('.perf_check_result_blue').val(data.total_cnt);
				$('.perf_check_result_green').val(data.completed_cnt);
				$('.perf_check_result_orange').val(data.err_cnt);
			}
		}else {
			return;
		}
		
	} catch(err) {
		console.log("Error Occured", err.message);
	}
}

//검색결과 건수
function ajaxCall_loadNumberOfSearch(){
	ajaxCall('/AISQLPV/loadNumberOfSearch',
			$('#submit_form'),
			'GET',
			callback_ajaxCall_loadNumberOfSearchAction);
}
var callback_ajaxCall_loadNumberOfSearchAction = function( result ) {
	let totCnt = 0;
	
	if( isNotEmpty(result) ){
		totCnt = result.txtValue;
	}
	$('.performanceResultCount').val('검색결과 건수 : '+parseInt(totCnt).toLocaleString());
}

// chart 조회
function ajaxCall_loadChartData(){
	ajaxCall('/AISQLPV/loadPerfChartData',
			$('#submit_form'),
			'GET',
			callback_ChartDataAction);
}
var callback_ChartDataAction = function(result) {
	try{
		let data;
		
		if ( isNotEmpty(result) ){
			data = JSON.parse(result);
			data = data.rows;
				
		} else {
			data = [];
		}
		
		createPerfImpactChart( data );
		createElapsedTimeChart( data );
		createBufferGetsChart( data );
		createPerfFitChart( data );
		
	}catch(err) {
		console.log('Error Occured', err.message);
	}
};

function ajaxCall_loadResultList(){
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress('성능 영향도 분석 결과 조회',' ');
	
	ajaxCall('/AISQLPV/loadResultList',
			$('#submit_form'),
			'GET',
			callback_loadResultList);
}
var callback_loadResultList = function(result) {
	try{
		json_string_callback_common(result,'#tableList',true);
		
		let dataLength = JSON.parse(result).dataCount4NextBtn;
		fnEnableDisablePagingBtn(dataLength);
		
	}catch(err) {
		console.log("Error Occured", err.message);
		
	}finally{
		/* modal progress close */
		if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
	}
}

// 엑셀 다운
function Excel_Download(){
	if ( $('#sql_auto_perf_check_id').val() != '' ) {
		$('#currentPage').val('1');
		$('#pagePerCount').val('20');
		
		let src = '/AISQLPV/loadResultList/excelDownload';
		$('#submit_form').attr('action', src);
		$('#submit_form').submit();
		
		showDownloadProgress( src );
		
		$('#submit_form').attr('action','');
		
	}else {
		warningMessager('다운로드할 데이터가 없습니다.');
		return false;
	}
}

function formValidationCheck(){
	if ( $('#project_combo').combobox('getValue') == '' ) {
		warningMessager('프로젝트를 선택해 주세요.');
		return false;
	}
	
	if ( $('#sqlPack_combo').combobox('getValue') == '' ) {
		warningMessager('SQL점검팩을 선택해 주세요.');
		return false;
	}
	return true;
}

//수행결과 초기화
function initPerfCheckResult(){
	$('.perf_check_result_blue').val('전체: 0');
	$('.perf_check_result_green').val('수행완료: 0');
	$('.perf_check_result_orange').val('오류: 0');
}

function setSearchData(){
	$('[name=sql_auto_perf_check_id]').val( $('#sqlPack_combo').combobox('getValue') );
	$('[name=line_up_cd]').val( $('#line_up_combo').combobox('getValue') );
	$('[name=orderOf]').val( $('#order_combo').combobox('getValue') );
	$('[name=select_yn]').val( isChecked('#select_yn') );
	$('[name=dml_yn]').val( isChecked('#dml_yn') );
	
	$('[name=all_sql_yn]').val( isChecked('#all_sql_yn') );
	$('[name=perf_down_yn]').val( isChecked('#perf_down_yn') );
	$('[name=notPerf_yn]').val( isChecked('#notPerf_yn') );
	$('[name=error_yn]').val( isChecked('#error_yn') );
	$('[name=fullScan_yn]').val( isChecked('#fullScan_yn') );
	$('[name=partition_yn]').val( isChecked('#partition_yn') );
	$('[name=timeOut_yn]').val( isChecked('#timeOut_yn') );
	$('[name=maxFetch_yn]').val( isChecked('#maxFetch_yn') );
	
	$('[name=buffer_gets_regres]').val( $('#buffer_gets_regres').numberbox('getValue') );
	$('[name=elapsed_time_regres]').val( $('#elapsed_time_regres').numberbox('getValue') );
	$('[name=buffer_gets_1day]').val( $('#buffer_gets_1day').numberbox('getValue') );
	$('[name=asis_elapsed_time]').val( $('#asis_elapsed_time').numberbox('getValue') );
	
	$('[name=search_sql_id]').val( $('#search_sql_id').textbox('getValue') );
}

function isChecked( chkBoxId ){
	return $( chkBoxId ).checkbox('options').checked ? 'Y' : 'N';
}

function extendArea( $this ) {
	let extendedYn = $($this).data('extended');
	
	let display		  = extendedYn ? 'none' : 'block';
	let wrapperHeight = extendedYn ? '628px' : '285px';
	let resizeHeight  = extendedYn ? 628 : 285;
	
	document.getElementById('searchWrapper').style.display = display;
	document.getElementById('chartWrapper').style.display = display;
	
	$('#tableListHeight, #tableListHeight > div > .panel-body').css('height', wrapperHeight);
	$('#tableList').datagrid('resize', { height: resizeHeight });
	
	$($this).data('extended', !extendedYn);
}