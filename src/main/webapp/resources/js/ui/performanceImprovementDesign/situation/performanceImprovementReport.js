$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid();

	$('#tableList').datagrid("loadData", []);


	//프로젝트 조회
	$('#submit_form #project_id').combobox({
		url:"/Common/getProject?isAll=Y",
		method:"get",
		valueField:'project_id',
		textField:'project_nm',
		onSelect: function(rec){
			if(rec.project_id == ''){
				$("#tuning_prgrs_step_seq").combobox('setValue','');
			}
				
			project_id = rec.project_id;
			
			if(project_id != null && project_id != ''){
				//튜닝진행단계 조회
				$('#submit_form #tuning_prgrs_step_seq').combobox({
					url:"/ProjectTuningPrgrsStep/getTuningPrgrsStep?isAll=Y&project_id="+project_id,
					method:"get",
					valueField:'tuning_prgrs_step_seq',
					textField:'tuning_prgrs_step_nm',
//					onSelect: function(rec){
//						tuning_prgrs_step_seq = rec.tuning_prgrs_step_seq;
//					},
					onLoadError: function(){
						parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
						return false;
					}
				});	
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
	});	
	
	
	//ajaxCallPerformanceImprovementReport();
	
});

function Btn_OnClick(){
	if($('#strStartDt').textbox('getValue') == ""){
		parent.$.messager.alert('','기준일자를 선택해 주세요.');
		return false;
	} else {
		if($('#strEndDt').textbox('getValue') == ""){
			parent.$.messager.alert('','종료일자를 선택해 주세요.');
			return false;
		}
	}
	
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	

//	$("#submit_form").attr("action","/PerformanceImprovementReport");
//	$("#submit_form").submit();			
	ajaxCallPerformanceImprovementReport();
}

function ajaxCallPerformanceImprovementReport(){
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("성능개선현황 보고서"," "); 
	
	ajaxCall("/PerformanceImprovementReportAction",
			$("#submit_form"),
			"POST",
			callback_PerformanceMngStatusAddTable);		
}
//callback 함수
var callback_PerformanceMngStatusAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);
	
	$( ".datagrid-view2:eq(0) td" ).css( "cursor", "default" );

	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
};

function Excel_DownClick() {
	$("#submit_form").attr("action","/PerformanceImprovementReport/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
};

//var Excel_DownClick = (function (e) {
//	console.log("Excel_DownClick");
//	if($('#strStartDt').textbox('getValue') == ""){
//		parent.$.messager.alert('','기준일자를 선택해 주세요.');
//		return false;
//	}
//	var excelExportHtml = $('<div>').append($('.datagrid-view2').clone()).html();
//	
//	var a = document.createElement('a');
//	//getting data from our div that contains the HTML table
//	var data_type = 'data:application/vnd.ms-excel';
//	a.href = data_type + ', ' + encodeURIComponent(excelExportHtml);
//	//setting the file name
//	a.download = '성능개선현황 보고서.xls';
//	//triggering the function
//	a.click();
//	return (a);	
//});

//	window.open('data:application/vnd.ms-excel,' + encodeURIComponent(excelExportHtml));
//	e.preventDefault(); 
//	
//	$("#submit_form").attr("action","/PerformanceImprovementReport/ExcelDown");
//	$("#submit_form").submit();		

//$.extend($.fn.datagrid.methods, {
//    toExcel: function(jq, filename){
//    	console.log("toExcel");
//    	console.log("jq:"+jq);
//    	console.log("filename:"+filename);
//        return jq.each(function(){
//            var uri = 'data:application/vnd.ms-excel;base64,'
//            , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>'
//            , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
//            , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
//
//            var alink = $('<a style="display:none"></a>').appendTo('body');
//            var table = $(this).datagrid('tableList').find('div.datagrid-view2 table.datagrid-btable');
//            var ctx = { worksheet: name || 'Worksheet', table: table.html()||'' };
//            alink[0].href = uri + base64(format(template, ctx));
//            alink[0].download = filename;
//            alink[0].click();
//            alink.remove();
//        })
//    }
//});


function Excel_DownClick2(){
	$('#tableList').datagrid('toExcel','dg.xls'); // export to excel
}