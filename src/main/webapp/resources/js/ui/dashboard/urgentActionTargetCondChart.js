var urgentActionTargetCondChart;

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
    var width2 = $("#urgentActionTargetCondChart").width();

    if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
    	urgentActionTargetCondChart.setSize(width2);	
	}
});	

$(document).ready(function() {
	
	OnSearch();
});


//chart 2, 긴급조치대상현황
function createUrgentActionTargetCondChart(jsondata){
	if(urgentActionTargetCondChart != "undefined" && urgentActionTargetCondChart != undefined){
		urgentActionTargetCondChart.destroy();
	}
	
	urgentActionTargetCondChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		//title:{color:"#000000",text:"긴급조치대상현황",textAlign:"left"},
		flex : 1,
		border : false,
		renderTo : document.getElementById("urgentActionTargetCondChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
	        captions: {
	            title: {
	                text: '긴급조치대상현황('+ maxCheckDay +')',
	                align: 'left',
	                style: {
	                	color: "#000000",
	                	font: 'bold 12px Arial',
	                	fill:"#000000"
	                }
	            }
	        },			
			border : false,
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			flipXY : true, // 가로/세로 축 변경
			innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
			store : {
				data : jsondata
			},
			axes : [{
				type : 'numeric',
				position : 'bottom',
				majorTickSteps : 10,
				minorTickSteps: 0,
      		minimum: 0,
      		//maximum: 10,
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
				position : 'left',
				label : {
					textAlign : 'left',
					x : 0,
					y : 0					
				}
			}],
			series : {
//				type : 'bar3d',
				type : 'bar',
				stacked : false, // stack 차트 해제
				style: {
	      			  minGapWidth: 25
  			},
//				xField : 'cnt',
//				yField : 'check_pref_nm',				
				xField : 'check_pref_nm',
				yField : 'cnt',				
				highlight: {
    				strokeStyle: 'black',
      			fillStyle: 'gold'
  			},
				tooltip : {
					trackMouse : true,
					renderer : function(tooltip, record, item){
						tooltip.setHtml(item.record.get('check_pref_nm') + " : " + item.record.get('cnt') + " 건");
					}
				},
				listeners: {
			        itemclick: function (chart, item, event) {
			        	var menuId = $("#menu_id").val()+Math.round(Math.random()*100);
			        	var menuNm = "긴급조치대상현황";
			        	var menuUrl = "/DashboardV2/urgentActionTarget";
			        	var menuParam = "check_pref_id="+item.record.get("check_pref_id")+"&check_day="+maxCheckDayDash;

			        	createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});
}

var win = Ext.create('Ext.Window', {
	width: '99%',
	height:'99%',
	minHeight: 240,
	minWidth: 300,
	hidden: false,
	shadow: false,
	maximizable: true,
	titleAlign: 'center',
	style: 'overflow: hidden;',
	title: '긴급조치대상현황',
	constrain: true,
//        renderTo: Ext.getBody(),
	renderTo : document.getElementById("urgentActionTargetCondChart"),
	layout: 'fit',
	items: urgentActionTargetCondChart
});    

function OnSearch(){
	
	/* 긴급조치대상 현황 차트 */
	ajaxCall("/DashboardV2/getUrgentActionCondition",
			$("#submit_form"),
			"POST",
			callback_UrgentActionTargetCondChartAction);
}
//callback 함수
var callback_UrgentActionTargetCondChartAction = function(result) {
	//console.log("urgentActionTargetCondChart result",result);
//	chart_callback(result, urgentActionTargetCondChart);
	var data = JSON.parse(result);
	console.log(data.rows);
	createUrgentActionTargetCondChart(data.rows);	
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