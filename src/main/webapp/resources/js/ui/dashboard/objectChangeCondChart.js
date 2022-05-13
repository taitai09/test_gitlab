var objectChangeCondChart;

var risk_jsp_page = [[ '001','databaseStatus' ],
	[ '004','instanceStatus' ],
	[ '005','listenerStatus' ],
	[ '006','dbfiles' ],
	[ '019','backgroundDumpSpace' ],
	[ '020','archiveLogSpace' ],
	[ '021','alertLogSpace' ],
	[ '022','fraSpace' ],
	[ '023','asmDiskgroupSpace' ],
	[ '024','tablespace' ],
	[ '026','invalidObject' ],
	[ '029','unusableIndex' ],
	[ '032','sequence']
	];

Ext.EventManager.onWindowResize(function () {
    var width7 = $("#objectChangeCondChart").width();

    if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
    	objectChangeCondChart.setSize(width7);
	}
});	

Ext.require([
    'Ext.chart.*',
    'Ext.Window', 
    'Ext.fx.target.Sprite', 
    'Ext.layout.container.Fit', 
    'Ext.window.MessageBox'
]);

$(document).ready(function() {
	OnSearch();
});

//chart 7, Object 변경현황 
function createObjectChangeCondChart1(jsondata){
	if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
		objectChangeCondChart.destroy();
	}

	objectChangeCondChart = Ext.create('Ext.chart.PolarChart', {
		renderTo : document.getElementById("objectChangeCondChart"),
	    width:600,
	    height:600,

	    store: {
	        fields: ['name', 'g1'],
	        data: [
	            {"name": "0", "g1": 18.34},
	            {"name": "1", "g1": 2.67},
	            {"name": "2", "g1": 1.90},
	            {"name": "3", "g1": 21.37},
	            {"name": "4", "g1": 2.67},
	            {"name": "5", "g1": 18.22},
	            {"name": "6", "g1": 28.51},
	            {"name": "7", "g1": 34.43},
	            {"name": "8", "g1": 21.65},
	            {"name": "9", "g1": 12.98},
	            {"name": "10", "g1": 22.96},
	            {"name": "11", "g1": 0.49},
	            {"name": "12", "g1": 20.87},
	            {"name": "13", "g1": 25.10},
	            {"name": "14", "g1": 16.87}
	        ]
	    },

	    // Set rotation
	    interactions: ['rotate'],

	    // Define radial and angular axis for the radar chart.
	    axes: [
	        {
	            type: 'numeric',
	            position: 'radial',
	            fields: 'g1',
	            grid: true,
	            label: {
	                fill: 'black',
	                y: -8
	            }
	        }, {
	            type: 'category',
	            position: 'angular',
	            fields: 'name',
	            grid: true,
	            label: {
	                fill: 'black'
	            }
	        }
	    ],


	    series: [
	        {
	            type: 'radar',
	            xField: 'name',
	            yField: 'g1'
	        }
	    ]
	});
	
}

//chart 7, Object 변경현황 
function createObjectChangeCondChart(jsondata){
	var newLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right'
	});	
	if(objectChangeCondChart != "undefined" && objectChangeCondChart != undefined){
		objectChangeCondChart.destroy();
	}
	
	Ext.onReady(function () {
	    
	    var seriesConfig = function(field,legend) {
	        return {
	            type: 'radar',
	            xField: 'type',
	            yField: field,
	            title:legend,
	            showInLegend: true,
	            showMarkers: true,
	            markerConfig: {
	                radius: 5,
	                size: 5
	            },
	            tips: {
	                trackMouse: true,
	                renderer: function(storeItem, item) {
	                	console.log("type:"+storeItem.get('type'));
	                	console.log("field:"+storeItem.get(field));
	                	this.setTitle(storeItem.get('name') + ': ' + storeItem.get(field));
	                }
	            },
	            style: {
	                'stroke-width': 2,
	                fill: 'none'
	            }
	        }
	    },

//	    chart = Ext.create('Ext.chart.Chart', {
//	        style: 'background:#fff',
//	        theme: 'Category2',
//	        animate: true,
//	        store: store1,
//	        insetPadding: 20,
//	        legend: {
//	            position: 'right'
//	        },
//	        axes: [{
//	            type: 'Radial',
//	            position: 'radial',
//	            label: {
//	                display: true
//	            }
//	        }],
//	        series: [
//	            seriesConfig('data1'),
//	            seriesConfig('data2'),
//	            seriesConfig('data3')
//	        ]
//	    });
	    
	    
	    objectChangeCondChart = Ext.create('Ext.chart.PolarChart', {
		    width:'100%',
		    height:270,
			//title : 'Object 변경현황',
			titleAlign:'center',
//			renderTo : document.getElementById("objectChangeCondChart"),
	        style: 'background:#fff',
	        theme: 'Category2',
	        animate: true,
	        insetPadding: 20,
	        legend: {
	            docked: 'right'
	        },
		    store: {
//		        fields: ['name', 'g1', 'g2'],
		        fields: ['type','yesterday','recent_one_week','recent_one_month'],
				data : jsondata,
		    },

		    // Set rotation
		    interactions: ['rotate'],

		    // Define radial and angular axis for the radar chart.
		    axes: [
		        {
		            type: 'Radial',
		            type: 'numeric',
		            position: 'radial',
		            fields: ['yesterday','recent_one_week','recent_one_month'],
		            grid: true,
		            label: {
		                fill: 'black',
		                y: -8,
		                display:true
		            }
		        }, {
		            type: 'category',
		            position: 'angular',
		            fields: 'type',
		            grid: true,
		            label: {
		                fill: 'black',
		                display:true
		            }
		        }
		    ],
		    series: [
	            seriesConfig('yesterday','전일'),
	            seriesConfig('recent_one_week','최근1주일'),
	            seriesConfig('recent_one_month','최근1개월')
		    ]
		    
		});	    

	    var win = Ext.create('Ext.Window', {
	        width: 800,
	        height: 600,
	        minHeight: 400,
	        minWidth: 550,
	        hidden: false,
	        shadow: false,
	        maximizable: true,
	        titleAlign: 'center',
	        style: 'overflow: hidden;',
	        title: 'Radar Chart',
	        constrain: true,
//	        renderTo: Ext.getBody(),
			renderTo : document.getElementById("objectChangeCondChart"),
	        layout: 'fit',
	        items: objectChangeCondChart
	    }); 
	});
}

function OnSearch(){
	/* 오브젝트 변경 현황 차트 */
	ajaxCall("/DashboardV2/getObjectChangeCondition",
			$("#submit_form"),
			"POST",
			callback_ObjectChangeCondChartAction);
	
}
//callback 함수
var callback_ObjectChangeCondChartAction = function(result) {
	//console.log("objectChangeCondChart result",result);
//	chart_callback(result, objectChangeCondChart);
	var data = JSON.parse(result);
	//console.log("data.rows:",data.rows);
	for(var i=0;i<data.rows.length;i++){
		//console.log(JSON.stringify(data.rows[i]));
	}
	createObjectChangeCondChart(data.rows);	
};

/* 조치여부 업데이트 */
function actionBtn(val, row) {
	if(row.emergency_action_yn == "N"){
		return "<a href='javascript:;' class='w80 easyui-linkbutton' onClick='Btn_UpdateUrgentAction(\""+row.dbid+"\",\""+row.emergency_action_no+"\");'><i class='btnIcon fas fa-wrench fa-1x'></i> 조치완료</a>";	
	}else{
		return "";
	}    
}

function Btn_UpdateUrgentAction(dbid, emergencyActionNo){
	//console.log("emergencyActionNo:"+emergencyActionNo+" dbid:"+dbid);
	$("#emergency_action_no").val(emergencyActionNo);
	$("#dbid").val(dbid);
	
	ajaxCall("/DashboardV2/UpdateUrgentAction",
			$("#submit_form"),
			"POST",
			callback_UpdateUrgentActionAction);		
}

//callback 함수
var callback_UpdateUrgentActionAction = function(result) {
	if(result.result){
		parent.$.messager.alert({
			msg : '긴급 조치 처리가 완료되었습니다.',
			fn :function(){
				Btn_UrgentActionSearch();
			} 
		});
	}else{
		parent.$.messager.alert('error','긴급 조치 처리가 실패하였습니다.');
	}
};

var menuNm = "";
var menu_id = "";
function setNewTabInfo(check_pref_id,check_tbl){
	for(var i=0;i<risk_jsp_page.length;i++){
		var array = risk_jsp_page[i];
		for(var j=0;j<array.length;j++){
			var k = array[0];
			var v = array[1];
			if(check_pref_id == k){
				menuId = $("#menu_id").val() + j;
				break;
			}
		}
	}	
	menuNm = strReplace(check_tbl, "_"," ").toUpperCase(); 
}

function getJspPage(check_pref_id){
	//console.log("check_pref_id :"+check_pref_id);
	for(var i=0;i<risk_jsp_page.length;i++){
		var array = risk_jsp_page[i];
		for(var j=0;j<array.length;j++){
			var k = array[0];
			var v = array[1];
			if(check_pref_id == k){
				return v;
			}
		}
	}
	return "";
}