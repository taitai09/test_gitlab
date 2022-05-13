var appPerfCheckChart;

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
    var width4 = $("#appPerfCheckChart").width();

    if(appPerfCheckChart != "undefined" && appPerfCheckChart != undefined){
    	appPerfCheckChart.setSize(width4);
	}

});	

$(document).ready(function() {
	OnSearch();
});
//chart 4, APP성능진단
function createAppPerfCheckChart(jsondata){
	if(appPerfCheckChart != "undefined" && appPerfCheckChart != undefined){
		appPerfCheckChart.destroy();
	}
	var newLegend = Ext.create("Ext.chart.Legend",{
		docked : 'right'
	});	
	appPerfCheckChart = Ext.create("Ext.panel.Panel",{
		width : '100%',
		height : '100%',
		flex : 1,
		border : false,		
		renderTo : document.getElementById("appPerfCheckChart"),
		layout : 'fit',
		items : [{
			xtype : 'cartesian',
			border : false,
			width : '100%',
			height : '100%',
			plugins: {
		        chartitemevents: {
		            moveEvents: true
		        }
		    },
			legend : newLegend,
		    innerPadding : '0 3 0 0', // 차트안쪽 여백
			insetPadding : 20, // 차트 밖 여백
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
      			  minGapWidth: 25,
      			  cursor:'hand'
    			},
				xField : 'wrkjob_cd_nm',
				yField : ['timeout_cnt','replytimeinc_cnt'],
				title : ['TIMEOUT','응답시간증가'],
				stacked: true,
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

			        	var menuId = "110";
			        	var menuNm = "성능 개선 관리";
			        	var menuUrl = "/ImprovementManagement";
			        	var strCd = "";
			        	var menuParam = "";			        	
			        	
			            if(fieldIndex == 0){
			            	strCd = "2";
			            }else if(fieldIndex == 1){
			            	strCd = "3";
			            }else if(fieldIndex == 2){
			            	strCd = "5";
			            }else if(fieldIndex == 3){
			            	strCd = "6";
			            }else if(fieldIndex == 4){
			            	strCd = "7";
			            }else if(fieldIndex == 5){
			            	strCd = "8";
			            }
			            
			            menuParam = "dbid="+item.record.get("dbid")+"&day_gubun=ALL&tuning_status_cd="+strCd;
			            
			            createNewTab(menuId, menuNm, menuUrl, menuParam);
			        }
			    }
			}
		}]
	});		
}

function OnSearch(){
	/* App성능진단 리스트 */
	ajaxCall("/DashboardV2/getAppPerfCheck",
			$("#submit_form"),
			"POST",
			callback_AppPerfCheckChartAction);	
}
//callback 함수
var callback_AppPerfCheckChartAction = function(result) {
	//console.log("appPerfCheckChart result",result);
//	chart_callback(result, appPerfCheckChart);
	var data = JSON.parse(result);
	createAppPerfCheckChart(data.rows);	
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