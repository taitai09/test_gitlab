$(document).ready(function() {
	$('#explainPlanPop').window({
		title : "Table Info",
		top:getWindowTop(1350),
		left:getWindowLeft(1000)
	});
	
	$("#explainPlanTab").tabs({
		plain: true
	});
	
	$("#columnInfoList").datagrid({
		view: myview,
		columns:[[
			{field:'column_name',title:'COLUMN NAME',width:"15%",halign:"center",align:'left',sortable:"true"},
			{field:'datatype',title:'DATATYPE',width:"19%",halign:"center",align:'left'},
			{field:'notnull',title:'NOT NULL',width:"8%",halign:"center",align:'left'},
			{field:'num_distinct',title:'NUM DISTINCT',width:"9%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'num_nulls',title:'NUM NULLS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'num_buckets',title:'NUM BUCKETS',width:"9%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'sample_size',title:'SAMPLE SIZE',width:"9%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'avg_col_len',title:'AVG COL LEN',width:"9%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'comments',title:'COMMENTS',width:"14%",halign:"center",align:'left'}
		]],
		onLoadError:function() {
			$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function() {
			$(".datagrid").css("border-top","1px solid");
		}
	});
	
	$("#indexInfoList").datagrid({
		view: myview,
		columns:[[
			{field:'owner',title:'INDEX OWNER',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'INDEX NAME',width:"15%",halign:"center",align:'left',sortable:"true"},
			{field:'index_column',title:'INDEX COLUMN',width:"20%",halign:"center",align:'left',sortable:"true"},
			{field:'index_type',title:'INDEX TYPE',width:"8%",halign:"center",align:'center'},
			{field:'uniquness',title:'UNIQUENESS',width:"9%",halign:"center",align:'left'},
			{field:'partitioned',title:'PARTITIONED',width:"9%",halign:"center",align:'center'},
			{field:'distinct',title:'DISTINCT',width:"8%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'num_rows',title:'NUM ROWS',width:"8%",halign:"center",align:'right',formatter:getNumberFormat},
			{field:'last_analyzed',title:'LAST ANALYZED',width:"13%",halign:"center",align:'center'}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#statisticsList").datagrid({
		view: myview,
		columns:[[
			{field:'owner',title:'OWNER',width:"11%",halign:"center",align:'center',sortable:"true"},
			{field:'analyzetime',title:'ANALYZETIME',width:"21%",halign:"center",align:'center',sortable:"true"},
			{field:'rowcnt',title:'ROWCNT',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'blkcnt',title:'BLKCNT',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'avgrln',title:'AVGRLN',width:"12%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'samplesize',title:'SAMPLESIZE',width:"11%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"},
			{field:'savtime',title:'SAVETIME',width:"21%",halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#accessPathList").datagrid({
		view: myview,
		columns:[[
			{field:'rnum',title:'NO',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'access_path_value',title:'ACCESS PATH',width:"70%",halign:"center",align:'left',sortable:"true"},
			{field:'access_path_count',title:'SQL수',width:"20%",halign:"center",align:'right',formatter:getNumberFormat,sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#columnHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'change_type',title:'CHANGE_TYPE',width:"12%",halign:"center",align:'center',sortable:"true"},
			{field:'change_day',title:'CHANGE_DAY',width:"12%",halign:"center",align:'center',sortable:"true"},
			{field:'column_name',title:'COLUMN_NAME',width:"16%",halign:"center",align:'left',sortable:"true"},
			{field:'data_type',title:'DATA_TYPE',width:"11%",halign:"center",align:'center',sortable:"true"},
			{field:'data_length',title:'DATA_LENGTH',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'data_precision',title:'DATA_PRECISION',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'data_scale',title:'DATA_SCALE',width:"12%",halign:"center",align:'right',sortable:"true"},
			{field:'nullable',title:'NULLABLE',width:"12%",halign:"center",align:'center',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
	
	$("#indexHistoryList").datagrid({
		view: myview,
		columns:[[
			{field:'change_type',title:'CHANGE_TYPE',width:"15%",halign:"center",align:'center',sortable:"true"},
			{field:'base_day',title:'BASE_DAY',width:"15%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'INDEX_NAME',width:"20%",halign:"center",align:'left',sortable:"true"},
			{field:'index_type',title:'INDEX_TYPE',width:"15%",halign:"center",align:'center',sortable:"true"},
			{field:'tablespace_name',title:'TABLESPACE_NAME',width:"15%",halign:"center",align:'center',sortable:"true"},
			{field:'index_column',title:'INDEX_COLUMN',width:"20%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	}
	});	
});