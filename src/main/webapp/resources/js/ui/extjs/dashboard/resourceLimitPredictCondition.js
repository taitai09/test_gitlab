$(document).ready(function() {
	Ext.define('KitchenSink.view.grid.MultipleSortingController', {
	    extend: 'Ext.app.ViewController',

	    alias: 'controller.multi-sort-grid',

	    updateSort: function () {
	        var store = this.getView().store,
	            details = [],
	            vm = this.getViewModel();

	        store.getSorters().each(function(sorter) {
	            details.push(sorter.getProperty() + ' ' + sorter.getDirection());
	        });

	        vm.set({
	            sortOrder: details.join(', ')
	        });
	    }
	});
	
	/**
	 * This example shows how to sort a grid by more than a single field.
	 *
	 * The store is initially sorted by joinDate DESC then by Salary ASC,
	 * as indicated in the headers. The `multiColumnSort` config allows
	 * clicking on a header to either add that field as the primary sorter,
	 * or if already sorted, it flips direction and moves that field to be
	 * the primary sort key.
	 */
	Ext.define('KitchenSink.view.grid.MultipleSorting', {
	    extend: 'Ext.grid.Panel',
	    xtype: 'multi-sort-grid',
	    controller: 'multi-sort-grid',

	    title: 'Multiple Sort Grid',
	    height: 350,
	    width : 600,

	    bind: '{store}',
	    multiColumnSort: true,

	    bbar: {
	        items: [{
	            xtype: 'component',
	            bind: 'Sorted By: {sortOrder}'
	        }]
	    },

	    columns: [{
	        text: 'Name',
	        dataIndex: 'name',

	        flex: 1
	    }, {
	        xtype: 'datecolumn',
	        text: 'Join Date',
	        dataIndex: 'joinDate',

	        width: 125
	    }, {
	        text: 'Salary',
	        dataIndex: 'salary',

	        width: 125,
	        align: 'right',
	        formatter: 'usMoney'
	    }],

	    viewModel: {
	        data: {
	            sortOrder: null
	        },

	        stores: {
	            store: {
	                type: 'big-data',

	                sorters: [
	                    { property: 'joinDate', direction: 'DESC' },
	                    'salary'
	                ],

	                listeners: {
	                    sort: 'updateSort',
	                    refresh: 'updateSort',
	                    buffer: 10
	                }
	            }
	        }
	    }
	});
	
	
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
		},
		onClickCell : function(index,field,value) {
//			console.log("index:",index);
//			console.log("field:",field);
//			console.log("value:",value);
			
			var afterMonth="";
			if(field == 'after_1_month'){
				afterMonth=1;
			}else if(field == 'after_2_month'){
				afterMonth=2;
			}else if(field == 'after_3_month'){
				afterMonth=3;
			}else if(field == 'after_6_month'){
				afterMonth=6;
			}else if(field == 'after_12_month'){
				afterMonth=12;
			} 
			var row = $(this).datagrid('getRows')[index];

			var menuId = $("#menu_id").val()+1;
        	var menuNm = "CPU 한계점 예측";
        	var menuUrl = "/CPULimitPointPrediction";
        	var menuParam = "dbid="+row.dbid+"&resource_type="+row.type+"&predict_standard="+afterMonth;

        	createNewTab(menuId, menuNm, menuUrl, menuParam);
		},		
		columns:[
		[
			{field:'dbid',title:'DB',halign:"center",align:"center",sortable:"true"},
			{field:'db_name',hidden:"true"},
			{field:'threshold',hidden:"true"},
			{field:'type',title:'자원',halign:"center",align:"center",sortable:"true"},
			{field:'inst_id',title:'INST_ID',halign:"center",align:'right',sortable:"true"},
			{field:'after_1_month',title:'1개월후',halign:"center",align:"center",sortable:"true",styler:cellStyler,formatter:getPercentRound},
			{field:'after_2_month',title:'2개월후',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getPercentRound},
			{field:'after_3_month',title:'3개월후',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getPercentRound},
			{field:'after_6_month',title:'6개월후',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getPercentRound},
			{field:'after_12_month',title:'12개월후',halign:"center",align:'right',sortable:"true",styler:cellStyler,formatter:getPercentRound}
		]],
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

	Btn_OnClick();
});

function Btn_OnClick(){
	ajaxCall("/DashboardV2/getResourceLimitPredictCombined",
			$("#submit_form"),
			"POST",
			callback_TablespaceAnalysisAddTable);
}

//callback 함수
var callback_TablespaceAnalysisAddTable = function(result) {
	json_string_callback_common(result,'#tableList',false);
};

function cellStyler(value,row,index){
	if(row.type == 'CPU'){
		if(parseInt(nvl(value,"0")) >= parseInt(row.threshold)){
			return 'background-color:#FF0000;';
		}else{
			return 'background-color:#00FF00;';		
		}
		
	}
}
