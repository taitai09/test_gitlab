<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ page session="false" %>
<%
/***********************************************************
 * 2018.01.29	이원식	V2 화면 최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<title>:: OPEN-POP ::</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />    
    <%@include file="/WEB-INF/jsp/include/sub_include.jsp" %>
    <link rel="stylesheet" href="/resources/js/lib/extjs/classic/theme-triton/resources/theme-triton-all.css">
    <link rel="stylesheet" href="/resources/js/lib/extjs/packages/charts/classic/triton/resources/charts-all.css">
    <script type="text/javascript" src="/resources/js/lib/extjs/ext-all.js"></script>    
    <script type="text/javascript" src="/resources/js/lib/extjs/packages/charts/classic/charts.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			createWaitChart();

			// Work Job 조회			
			$('#selectCombo').combobox({
			    url:"/Common/getWrkJob",
			    method:"get",
				valueField:'wrkjob_cd',
			    textField:'wrkjob_cd_nm',
				onLoadError: function(){
					parent.$.messager.alert('','업무구분 조회중 오류가 발생하였습니다.');
				}
			});	
			
			$("#chartTab").tabs({
				plain: true,
				onSelect: function(title,index){
					if(index == 0){
						$("#day_gubun").val("D");
						createWaitChart();
					}else if(index == 1){
						$("#day_gubun").val("W");
						createTopWaitChart();
					}
					
					Btn_OnClick();
				}
			});
		});	
		
		function createDayChart(){
			if(dayChart == "undefined" || dayChart == undefined){
				dayChart = Ext.create("Ext.panel.Panel",{
					width : '100%',
					height : '100%',
					border : false,
					renderTo : document.getElementById("waitClassChart"),
					layout : 'fit',
					items : [{
						xtype : 'cartesian',
						plugins: {
					        chartitemevents: {
					            moveEvents: true
					        }
					    },
						innerPadding : '0 5 0 0', // 차트안쪽 여백
						insetPadding : '10 20 10 10', // 차트 밖 여백
						store : {
							data : []
						},
						axes : [{
							type : 'numeric',
							position : 'left',
							majorTickSteps : 20,
							minorTickSteps: 0,
	                		minimum: 0,
	                		maximum: 20,
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
								textAlign : 'right'
							}
						}],
						series : {
							type : 'line',
							smooth : true,
							marker : {
								radius : 2
							},
							xField : 'tr_perf_indc_type_nm',
							yField : 'total_cnt',
							tooltip : {
								trackMouse : true,
								renderer : function(tooltip, record){
									tooltip.setHtml(record.get("tr_perf_indc_type_nm")+" : " + record.get("total_cnt") + " 건");
								}
							},
							listeners: {
						        itemclick: function (chart, item, event) {
						            console.log('itemclick', item.record.get("tr_perf_indc_type_nm"), item.record.get("total_cnt"));
						        }
						    }
						}
					}]
		    	});
			}
		}

	    function Btn_OnClick(){
	    	if($('#selectCombo').combobox('getValue') == ""){
	    		parent.$.messager.alert('','DB를 선택해 주세요.');
	    		return false;
	    	}

	    	$("#wrkjob_cd").val($('#selectCombo').combobox('getValue'));
	    	$("#base_day").val(strReplace($('#strStartDt').textbox('getValue'),"-",""));

	    	// 차트 데이터 조회
	    	searchSummaryChart();
	    	// 요약 데이터 조회
	    	//searchSummary();	    	
	    }
	    
	    function searchSummaryChart(){	
	    	ajaxCall("/ApplicationDiagnostics/SummaryChart",
	    			$("#submit_form"),
	    			"POST",
	    			callback_SummaryChartAction);	
	    }
	    
	    var callback_SummaryChartAction = function(result) {
	    	var store;
	    	var data = JSON.parse(result);
	    	var diagCntArray = [];
	    	var maxValue = 0;
	    	var maxCnt = 0;

	    	if($("#day_gubun").val() == "D"){
	    		for(var i = 0 ; i < data.rows.length ; i++){
	    			diagCntArray.push(parseInt(data.rows[i].diag_cnt));
	    		}
	    		
	    		maxValue = Math.max.apply(null,diagCntArray);		
	    		maxCnt = maxValue + (10 - maxValue%10);

	    		store = dayChart.down("chart").getStore();
	    		
	    		dayChart.down("chart").getAxes()[0].setMajorTickSteps(maxCnt);
	    		dayChart.down("chart").getAxes()[0].setMaximum(maxCnt);
	    	}else if($("#day_gubun").val() == "W"){
	    		store = weekChart.down("chart").getStore();
	    	}else {
	    		store = monthChart.down("chart").getStore();
	    	}
	    	
	    	store.loadData(data.rows);
	    };
	</script>
</head>
<body>
<!-- container START -->
<div id="container">	
	<!-- contents START -->
	<form:form method="post" id="submit_form" name="submit_form" class="form-inline">
		<%@include file="/WEB-INF/jsp/include/hidden.jsp" %>
		<div class="well">						
			<input type="hidden" id="wrkjob_cd" name="wrkjob_cd"/>
			<input type="hidden" id="day_gubun" name="day_gubun" value="D"/>
			<input type="hidden" id="base_day" name="base_day"/>
			<label>DB</label>
			<select id="selectCombo" name="selectCombo" data-options="editable:false" class="w130 easyui-combobox"></select>
			<label>기준일</label>
			<input type="text" id="strStartDt" name="strStartDt" value="${nowDate}" class="w130 datapicker easyui-datebox"/>
			<span class="searchBtnLeft">
				<a href="javascript:;" class="w80 easyui-linkbutton" onClick="Btn_OnClick();"><i class="btnIcon fas fa-search fa-lg fa-fw"></i> 검색</a>
			</span>
		</div>	
		<div id="contents" style="margin-top:10px;">
			<div id="chartTab" class="easyui-tabs" data-options="border:true" style="width:100%;height:300px;">
				<div id="waitClassChart" title="Wait Class" style="padding:5px;">
				</div>
				<div id="topWaitChart" title="Top Wait Event" style="padding:5px">
				</div>
			</div>
		</div>
	</form:form>
</div>
</body>
</html>