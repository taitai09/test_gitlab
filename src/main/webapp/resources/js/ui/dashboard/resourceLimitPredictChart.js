var resourceLimitPredictChart;

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
    var width8 = $("#resourceLimitPredictChart").width();

    if(resourceLimitPredictChart != "undefined" && resourceLimitPredictChart != undefined){
    	resourceLimitPredictChart.setSize(width8);
	}
});	

$(document).ready(function() {
	OnSearch();
});
//chart 8, 자원한계예측현황
function createResourceLimitPredictChart(jsondata){
	if(resourceLimitPredictChart != "undefined" && resourceLimitPredictChart != undefined){
		resourceLimitPredictChart.destroy();
	}

	resourceLimitPredictChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("resourceLimitPredictChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			legend : {
				docked : 'right'
			},
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
		    innerPadding : '10 5 0 5', // 차트안쪽 여백
			insetPadding : 10, // 차트 밖 여백
			store : {
				data : jsondata
			},
			axes : [{
				type : 'numeric',
				position : 'left',
				minorTickSteps: 0,
        		minimum: 0,
			    grid: {
			        odd: {
			            opacity: 1,
			            fill: '#eee',
			            stroke: '#bbb',
			            lineWidth: 1
			        }
			    },
				title : '(건수)'
			},{
				type : 'category',
				position : 'bottom',
				grid: true,
				label : {
					x : 0,
					y : 0
				}
			}],
			series : {
				type : 'bar',
				style: {
      			  minGapWidth: 25
    			},
				xField : ['type','after_1_month','after_2_month','after_3_month','after_6_month','after_12_month'],
				yField : ['after_1_month','after_2_month','after_3_month','after_6_month','after_12_month'],
				title : ['1개월후','2개월후','3개월후','6개월후','12개월후'],
				highlight: {
      				strokeStyle: 'black',
        			fillStyle: 'gold'
    			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field),
			            sector = item.series.getTitle()[fieldIndex],
			            value = Ext.util.Format.number(record.get(item.field));

						tooltip.setHtml(sector+" : " + value + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {			        	
			        	fieldIndex = Ext.Array.indexOf(item.series.getYField(), item.field);
			        	var object_change_type = item.field;

			        	var menuId = "191";
			        	var menuNm = "오브젝트 변경 분석";
			        	var menuUrl = "/ObjectChangeAnalysis";
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            menuParam = "dbid="+item.record.get("dbid")+"&base_day_gubun=2&object_change_type="+object_change_type;

			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});		
}

function OnSearch(){
	/* 자원한계예측 현황 차트 */
	ajaxCall("/DashboardV2/getResourceLimitPredict",
			$("#submit_form"),
			"POST",
			callback_ResourceLimitPredictChartAction);
	
}

//callback 함수
var callback_ResourceLimitPredictChartAction = function(result) {
	//console.log("resourceLimitPredictChart result",result);
//	chart_callback(result, resourceLimitPredictChart);
	var data = JSON.parse(result);
	createResourceLimitPredictChart(data.rows);	
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