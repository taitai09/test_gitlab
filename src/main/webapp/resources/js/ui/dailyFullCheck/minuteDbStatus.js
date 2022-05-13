var g_eventList;			// id별 그룹들의 event 관리

$(document).ready(function() {
	$("body").css("visibility", "visible");
	console.log("menu_id[" + $('#menu_id').val() + "] menu_nm[" + $('#menu_nm').val() + "]");
	
//	test();
	
	initGlobalVariable();
	
	$('#statusTabs').tabs({
		tabWidth: 160,
		onSelect: function(title, index) {
			let tab = $(this).tabs('getSelected');
			let hest = $(this).tabs('getTabIndex', tab);
			
			proceedDiagnosis(title, index);
		},
		onLoad: function(panel) {
			let db_status_name = $('#db_status_name').val().replace('\n', ' ');
			console.log(panel.panel('options').title + ' is loaded');
		}
	});
	
	setTabsSeverityColor();
	
	if($('#db_status_name').val() == 'DB') {
		diagnosisResultSummary();
	} else {
		selectTab();
	}
});

function initGlobalVariable() {
	g_eventList = new Map();
}

function selectTab() {
	switch($('#db_status_name').val()) {
	case 'DB':
		$('#statusTabs').tabs('select', 0);
		break;
	case 'INSTANCE':
		$('#statusTabs').tabs('select', 1);
		break;
	case 'SPACE':
		$('#statusTabs').tabs('select', 2);
		break;
	case 'OBJECT':
		$('#statusTabs').tabs('select', 3);
		break;
	case 'STATISTICS':
		$('#statusTabs').tabs('select', 4);
		break;
	case 'LONGRUNNINGWORK':
		$('#statusTabs').tabs('select', 5);
		break;
	case 'ALERT':
		$('#statusTabs').tabs('select', 6);
		break;
	}
}

function proceedDiagnosis(title, index) {
	switch(index) {
	case 0:
		$('#db_status_name').val('DB');
		break;
	case 1:
		$('#db_status_name').val('INSTANCE');
		break;
	case 2:
		$('#db_status_name').val('SPACE');
		break;
	case 3:
		$('#db_status_name').val('OBJECT');
		break;
	case 4:
		$('#db_status_name').val('STATISTICS');
		break;
	case 5:
		$('#db_status_name').val('LONGRUNNINGWORK');
		break;
	case 6:
		$('#db_status_name').val('ALERT');
		break;
	}
	
	diagnosisResultSummary();
}

function setTabsSeverityColor() {
	let db_status_tabs_severity = $('#db_status_tabs_severity').val();
	let tabsSeverity = db_status_tabs_severity.split(';');
	
	for(var index = 0; index < tabsSeverity.length; index++) {
		setTabsColor(index, tabsSeverity[index]);
	}
}

function setTabsColor(index, _color) {
//	var tab = $('#statusTabs').tabs('getTab', index);
//	$('#statusTabs').tabs('update', {
//		tab: tab,
//		type: 'header',
//		options: {
//			color: checkDelegateAlarm(_color)
//		}
//	});
	
	$('ul.tabs > li > a')[index].style.background = checkDelegateAlarm(_color);
	$('ul.tabs > li > a')[index].style.color = '#fff';
	$('ul.tabs > li > a')[index].style.borderColor = '#fff';
}

function test() {
	let html = "";
	
	html += "\r<div><label class='minute_title_1'>▉ DB 점검결과 요약</label></div>";
	
	$('#object_title').html(html);
	$('#object_title').append();
	
	drawObject('object_canvas');
	
	drawMinute();
}

function drawObject(id) {
	var canvas = document.getElementById(id);
	var red = "#d90015";
	var orange = "#fe852b";
	var blue = "#0070c0";
	var green = "#00b050";
	var white = "#FFF";
	var black = "#000";
	var gray = "#7f7f7f";
	var yellow = "#020202";
	var defaultDbFont = "12px Open Sans";
	var bold18pxDbFont = "Bold 18px Open Sans";
	var bold24pxDbFont = "Bold 24px Open Sans";
	var bold48pxDbFont = "Bold 48px Open Sans";
	
	var yPoint = 0;
	var canvasRectangleWidth = 1500;								// canvas Rectangle Width(canvas element의 width보다 +1로 설정해야 정확함)
	var canvasRectangleHeight = 70;									// canvas Rectangle Height(canvas element의 height보다 +1로 설정해야 정확함)
	var paddingLeft = 65;											// 기본적인 좌측 패딩 길이
	var dbStatusRectangleThickess = 4;								// DB Status Rectangle Thickess
	var dbStatusRectangleWidth = 96;								// DB Status Rectangle Width
	var dbStatusRectangleHeight = 70;								// DB Status Rectangle Height
	var dbStatusRectangleBasePointX = paddingLeft;					// DB Status Rectangle X축 기준 시작점
	var dbStatusRectangleBasePointXPlus = 5;
	var dbStatusRectanglePointY = yPoint + 0;						// DB Status Rectangle Y축 시작점
	var dbStatusRectanglePointX = 0;								// DB Status Rectangle X축 시작점
	var dbStatusTextPointX = 50;									// DB Status Text X축 시작점. 사용하지 않음
	var dbStatusTextAddPointX = 48;									// DB Status Text X축 추가 시작점
	var dbStatusTextPointY_1 = yPoint + 41;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_2 = yPoint + 33;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_3 = yPoint + 25;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_4 = yPoint + 18;							// DB Status Text Y축 시작점. Row 4
	var dbStatusName1 = "DBFILES";
	var dbStatusName2 = "EXPIRED\nGRACE\nACCOUNT";
	var dbStatusName3 = "LIBRARY\nCACHE HIT(%)";
	var dbStatusName4 = "PARSE CPU\nTO\nPARSE\nELAPSED";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		var elemLeft = canvas.offsetLeft;
		var elemTop = canvas.offsetTop;
		
		var ctx = canvas.getContext("2d");
		
		// DB
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm('C');
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName1, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName1.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// INSTANCE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + dbStatusRectangleBasePointXPlus + dbStatusRectangleWidth;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm('W');
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName2, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName2.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// SPACE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleBasePointXPlus * 2) + (dbStatusRectangleWidth * 2);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm('I');
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName3, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName3.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// OBJECT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleBasePointXPlus * 3) + (dbStatusRectangleWidth * 3);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm('N');
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName4, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName4.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
	}
}

function checkDelegateAlarm(gradeCd) {
	var red = '#' + $('#severity_color_0').val();		// "#e21b30";
	var orange = '#' + $('#severity_color_1').val();	// "#fe852b";
	var blue = '#' + $('#severity_color_2').val();	// "#0070c0";
	var green = '#' + $('#severity_color_3').val();	// "#00b050";
	var gray = "#7f7f7f";
	let color;
	
	switch(gradeCd) {
	case 'C':
		color = red;
		break;
	case 'W':
		color = orange;
		break;
	case 'I':
		color = blue;
		break;
	case 'N':
		color = green;
		break;
	default:
		color = gray;
		break;
	}
	
	return color;
}

function drawMinute() {
	let html = "<label class='minute_title_1'>▉ DATABASE 점검결과 상세</label>";
	
	$('#minute_title').html(html);
	$('#minute_title').append();
	
	html = "";
	
	html += "<div><label class='minute_title_2'>▪&nbsp;&nbsp;DATABASE STATUS</label></div>\n";
	html += "<div id='DB_1'>\n";
	html += "<table class='minute'>\n";
	html += "<thead>\n";
	html += "<tr>\n";
	html += "<th width='20%' class='om-left'>DBID</th>\n";
	html += "<th width='20%' class='om-center'>INST_ID</th>\n";
	html += "<th width='20%' class='om-center'>DB_NAME</th>\n";
	html += "<th width='20%' class='om-center'>LOG_MODE</th>\n";
	html += "<th width='20%' class='om-center'>OPEN_MODE</th>\n";
	html += "<th width='20%' class='om-right'>PLATFORM_NAME</th>\n";
	html += "</tr>\n"
	html += "</thead>\n";
	html += "<tr>\n";
	html += "<td class='om-left-close' style='text-align:center;word-break:break-all;'>252008649</td>\n";
	html += "<td class='om-left-close' style='text-align:center;word-break:break-all;'>2</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>COLLECT1</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>NOARCHIVELOG</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>READ</td>\n";
	html += "<td class='om-right-close' style='text-align:left;word-break:break-all;'>Linux x86 64-bit</td>\n";
	html += "</tr>\n";
	html += "</table>\n";
	html += "</div>";
	
	html += "<div><label class='minute_title_3'>▪&nbsp;&nbsp;EXPIRED GRACE ACCPOUNT Test</label></div>";
html += "<div id='INCIDENT_T2'>";
html +=	"<table class='om3' style='margin-right: 50px;padding-right: 1000px;'>";
html += "			<thead>";
html += "				<tr>";
html += "					</tr><tr>";
html += "						<th class='om-left'>INST_ID</th>";
html += "						<th class='om-center'>INCIDENT_ID</th>";
html += "						<th class='om-center'>PROBLEM_ID</th>";
html += "						<th class='om-center'>CREATE_TIME</th>";
html += "						<th class='om-center'>CLOSE_TIME</th>";
html += "						<th class='om-center'>STATUS</th>";
html += "						<th class='om-center'>FLOOD_CONTROLLED</th>";
html += "						<th class='om-center'>ERROR_FACILITY</th>";
html += "						<th class='om-center'>ERROR_NUMBER</th>";
html += "						<th class='om-center'>ERROR_ARG1</th>";
html += "						<th class='om-center'>ERROR_ARG2</th>";
html += "						<th class='om-center'>ERROR_ARG3</th>";
html += "						<th class='om-center'>ERROR_ARG4</th>";
html += "						<th class='om-center'>ERROR_ARG5</th>";
html += "						<th class='om-center'>ERROR_ARG6</th>";
html += "						<th class='om-center'>ERROR_ARG7</th>";
html += "						<th class='om-center'>ERROR_ARG8</th>";
html += "						<th class='om-center'>ERROR_ARG9</th>";
html += "						<th class='om-center'>ERROR_ARG10</th>";
html += "						<th class='om-center'>ERROR_ARG11</th>";
html += "						<th class='om-center'>ERROR_ARG12</th>";
html += "						<th class='om-center'>SIGNALLING_COMPONENT</th>";
html += "						<th class='om-center'>SIGNALLING_SUBCOMPONENT</th>";
html += "						<th class='om-center'>SUBSPECT_COMPONENT</th>";
html += "						<th class='om-center'>SUBSPECT_SUBCOMPONENT</th>";
html += "						<th class='om-center'>ECID</th>";
html += "						<th class='om-center'>IMPACT</th>";
html += "						<th class='om-center'>INST_ID</th>";
html += "						<th class='om-center'>INCIDENT_ID</th>";
html += "						<th class='om-center'>PROBLEM_ID</th>";
html += "						<th class='om-center'>CREATE_TIME</th>";
html += "						<th class='om-center'>CLOSE_TIME</th>";
html += "						<th class='om-center'>STATUS</th>";
html += "						<th class='om-center'>FLOOD_CONTROLLED</th>";
html += "						<th class='om-center'>ERROR_FACILITY</th>";
html += "						<th class='om-center'>ERROR_NUMBER</th>";
html += "						<th class='om-center'>ERROR_ARG1</th>";
html += "						<th class='om-center'>ERROR_ARG2</th>";
html += "						<th class='om-center'>ERROR_ARG3</th>";
html += "						<th class='om-center'>ERROR_ARG4</th>";
html += "						<th class='om-center'>ERROR_ARG5</th>";
html += "						<th class='om-center'>ERROR_ARG6</th>";
html += "						<th class='om-center'>ERROR_ARG7</th>";
html += "						<th class='om-center'>ERROR_ARG8</th>";
html += "						<th class='om-center'>ERROR_ARG9</th>";
html += "						<th class='om-center'>ERROR_ARG10</th>";
html += "						<th class='om-center'>ERROR_ARG11</th>";
html += "						<th class='om-center'>ERROR_ARG12</th>";
html += "						<th class='om-center'>SIGNALLING_COMPONENT</th>";
html += "						<th class='om-center'>SIGNALLING_SUBCOMPONENT</th>";
html += "						<th class='om-center'>SUBSPECT_COMPONENT</th>";
html += "						<th class='om-center'>SUBSPECT_SUBCOMPONENT</th>";
html += "						<th class='om-center'>ECID</th>";
html += "						<th class='om-right'>IMPACT</th>";
html += "					</tr>";
html += "			</thead>";
html += "			<tbody><tr><td colspan='54' class='om-colspan'>점검결과 정상입니다.</td>";
html += "		</tr></tbody></table>";
html += "</div>";
	
	html += "<div><label class='minute_title_3'>▪&nbsp;&nbsp;EXPIRED GRACE ACCPOUNT</label></div>\n";
	html += "<div id='DB_2'>\n";
	html += "<table class='minute'>\n";
	html += "<thead>\n";
	html += "<tr>\n";
	html += "<th width='20%' class='om-left'>USERNAME</th>\n";
	html += "<th width='20%' class='om-center'>ACCOUNT_STATUS</th>\n";
	html += "<th width='20%' class='om-center'>EXPIRY_DATE</th>\n";
	html += "<th width='20%' class='om-center'>CREATED</th>\n";
	html += "<th width='20%' class='om-center'>PASSWORD_EXPIRY_REMAIN_TIME</th>\n";
	html += "<th width='20%' class='om-right'>PASSWORD_GRACE_TIME</th>\n";
	html += "</tr>\n"
	html += "</thead>\n";
	html += "<tr>\n";
	html += "<td class='om-left-close' style='text-align:center;word-break:break-all;'>SYSTEM</td>\n";
	html += "<td class='om-left-close' style='text-align:center;word-break:break-all;'>EXPIRED(GRACE)</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>2019-08-15 18:28:39</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>2019-08-15 00:17:11</td>\n";
	html += "<td class='om-left-close' style='text-align:left;word-break:break-all;'>0일 -9시 -4분 -2초 남음</td>\n";
	html += "<td class='om-right-close' style='text-align:left;word-break:break-all;'>7</td>\n";
	html += "</tr>\n";
	html += "</table>\n";
	html += "</div>";
	
	$('#minute_area').html(html);
	$('#minute_area').append();
}

function diagnosisResultSummary() {
	ajaxCall("/DailyCheckDb/diagnosisResultSummary",
			$("#minute_form"),
			"POST",
			callback_DiagnosisResultSummary);
	
	ajaxCall("/DailyCheckDb/diagnosisResultMinute",
			$("#minute_form"),
			"POST",
			callback_DiagnosisResultMinute);
}

var callback_DiagnosisResultSummary = function(result) {
	var data = JSON.parse(result);
	
	divideDiagnosisResultSummary(data[0])
}

function divideDiagnosisResultSummary(row) {
	let html = "";
	let statusNameArray = new Array();
	let statusGradeArray = new Array();
//	let statusGradeArray = new Array();
	
	switch($('#db_status_name').val()) {
	case 'DB':
		html = "\r<div><label class='minute_title_1'>▉ DB 점검결과 요약</label></div>";
		
		$('#title_db').html(html);
		$('#title_db').append();
		
		statusNameArray.push("DATABASE\nSTATUS");
		statusNameArray.push("EXPIRED\nGRACE\nACCOUNT");
		statusNameArray.push("MODIFIED\nPARAMETER");
		statusNameArray.push("NEW\nCREATED\nOBJECT");
		
		statusGradeArray.push("database_status_grade");
		statusGradeArray.push("expired_account_grade");
		statusGradeArray.push("modified_parameter_grade");
		statusGradeArray.push("new_created_object_grade");
		
		drawDiagnosisResultSummary('canvas_db', row, statusNameArray, statusGradeArray);
		break;
	case 'INSTANCE':
		html = "\r<div><label class='minute_title_1'>▉ INSTANCE 점검결과 요약</label></div>";
		
		$('#title_instance').html(html);
		$('#title_instance').append();
		
		statusNameArray.push("INSTANCE\nSTATUS");
		statusNameArray.push("LISTENER\nSTATUS");
		statusNameArray.push("DBFILES");
		statusNameArray.push("RESOURCE\nLIMIT");
		statusNameArray.push("LIBRARY\nCACHE HIT");
		statusNameArray.push("DICTIONARY\nCACHE HIT");
		statusNameArray.push("BUFFER\nCACHE HIT");
		statusNameArray.push("LATCH HIT");
		statusNameArray.push("PARSE CPU\nTO\nPARSE\nELAPSED");
		statusNameArray.push("DISK\nSORT");
		statusNameArray.push("MEMORY\nUSAGE");
		
		statusGradeArray.push("instance_status_grade");
		statusGradeArray.push("listener_status_grade");
		statusGradeArray.push("db_files_grade");
		statusGradeArray.push("rlr_grade");
		statusGradeArray.push("library_cache_hit_grade");
		statusGradeArray.push("dch_grade");
		statusGradeArray.push("bch_grade");
		statusGradeArray.push("latch_hit_grade");
		statusGradeArray.push("pctp_elapsd_grade");
		statusGradeArray.push("disk_sort_grade");
		statusGradeArray.push("spu_grade");
		
		drawDiagnosisResultSummary('canvas_instance', row, statusNameArray, statusGradeArray);
		break;
	case 'SPACE':
		html = "\r<div><label class='minute_title_1'>▉ SPACE 점검결과 요약</label></div>";
		
		$('#title_space').html(html);
		$('#title_space').append();

		statusNameArray.push("FRA\nSPACE");
		statusNameArray.push("ASM\nDISKGROUP\nSPACE");
		statusNameArray.push("TABLESPACE");
		statusNameArray.push("RECYCLEBIN\nOBJECT");
		
		statusGradeArray.push("fra_ste_grade");
		statusGradeArray.push("asm_dste_grade");
		statusGradeArray.push("tablespace_te_grade");
		statusGradeArray.push("recyclebin_object_grade");
		
		drawDiagnosisResultSummary('canvas_space', row, statusNameArray, statusGradeArray);
		break;
	case 'OBJECT':
		html = "\r<div><label class='minute_title_1'>▉ OBJECT 점검결과 요약</label></div>";
		
		$('#title_object').html(html);
		$('#title_object').append();
		
		statusNameArray.push("INVALID\nOBJECT");
		statusNameArray.push("NOLOGGING\nOBJECT");
		statusNameArray.push("PARALLEL\nOBJECT");
		statusNameArray.push("UNUSABLE\nINDEX");
		statusNameArray.push("CORRUPT\nBLOCK");
		statusNameArray.push("SEQUENCE");
		statusNameArray.push("FOREIGNKEYS\nWITHOUT\nINDEX");
		statusNameArray.push("DISABLED\nCONSTRAINT");
		statusNameArray.push("CHAINED\nROWS");
		
		statusGradeArray.push("invalid_object_grade");
		statusGradeArray.push("nologging_object_grade");
		statusGradeArray.push("parallel_object_grade");
		statusGradeArray.push("unusable_index_grade");
		statusGradeArray.push("corrupt_block_grade");
		statusGradeArray.push("sequence_te_grade");
		statusGradeArray.push("foreign_kwi_grade");
		statusGradeArray.push("disabled_constraint_grade");
		statusGradeArray.push("chained_rows_grade");
		
		drawDiagnosisResultSummary('canvas_object', row, statusNameArray, statusGradeArray);
		break;
	case 'STATISTICS':
		html = "\r<div><label class='minute_title_1'>▉ STATISTICS 점검결과 요약</label></div>";
		
		$('#title_statistics').html(html);
		$('#title_statistics').append();
		
		statusNameArray.push("MISSING\nOR\nSTALE\nSTATISTICS");
		statusNameArray.push("STATISTICS\nLOCKED\nTABLE");
		
		statusGradeArray.push("missing_oss_grade");
		statusGradeArray.push("statistics_lt_grade");
		
		drawDiagnosisResultSummary('canvas_statistics', row, statusNameArray, statusGradeArray);
		break;
	case 'LONGRUNNINGWORK':
		html = "\r<div><label class='minute_title_1'>▉ LONG RUNNING WORK 점검결과 요약</label></div>";
		
		$('#title_longrunningwork').html(html);
		$('#title_longrunningwork').append();
		
		statusNameArray.push("LONG\nRUNNING\nOPERATION");
		statusNameArray.push("LONG\nRUNNING\nJOB");
		
		statusGradeArray.push("long_running_operation_grade");
		statusGradeArray.push("long_running_job_grade");
		
		drawDiagnosisResultSummary('canvas_longrunningwork', row, statusNameArray, statusGradeArray);
		break;
	case 'ALERT':
		html = "\r<div><label class='minute_title_1'>▉ ALERT 점검결과 요약</label></div>";
		
		$('#title_alert').html(html);
		$('#title_alert').append();
		
		statusNameArray.push("ALERT\nLOG\nERROR");
		statusNameArray.push("ACTIVE\nINCIDENT");
		statusNameArray.push("OUTSTANDING\nALERT");
		statusNameArray.push("DBMS\nSCHEDULER\nJOB\nFAILED");
		
		statusGradeArray.push("alert_log_error_grade");
		statusGradeArray.push("active_incident_grade");
		statusGradeArray.push("outstanding_alert_grade");
		statusGradeArray.push("dbms_scheduler_failed_grade");
		
		drawDiagnosisResultSummary('canvas_alert', row, statusNameArray, statusGradeArray);
		break;
	}
}

function drawDiagnosisResultSummary(id, row, statusNameArray, statusGradeArray) {
	var canvas = document.getElementById(id);
	var red = "#d90015";
	var orange = "#fe852b";
	var blue = "#0070c0";
	var green = "#00b050";
	var white = "#FFF";
	var black = "#000";
	var gray = "#7f7f7f";
	var yellow = "#020202";
	var defaultDbFont = "12px Open Sans";
	var bold18pxDbFont = "Bold 18px Open Sans";
	var bold24pxDbFont = "Bold 24px Open Sans";
	var bold48pxDbFont = "Bold 48px Open Sans";
	
	var yPoint = 0;
	var canvasRectangleWidth = 1500;								// canvas Rectangle Width(canvas element의 width보다 +1로 설정해야 정확함)
	var canvasRectangleHeight = 70;									// canvas Rectangle Height(canvas element의 height보다 +1로 설정해야 정확함)
	var paddingLeft = 65;											// 기본적인 좌측 패딩 길이
	var dbStatusRectangleThickess = 4;								// DB Status Rectangle Thickess
	var dbStatusRectangleWidth = 96;								// DB Status Rectangle Width
	var dbStatusRectangleHeight = 70;								// DB Status Rectangle Height
	var dbStatusRectangleBasePointX = paddingLeft;					// DB Status Rectangle X축 기준 시작점
	var dbStatusRectangleBasePointXPlus = 5;
	var dbStatusRectanglePointY = yPoint + 0;						// DB Status Rectangle Y축 시작점
	var dbStatusRectanglePointX = 0;								// DB Status Rectangle X축 시작점
	var dbStatusTextPointX = 50;									// DB Status Text X축 시작점. 사용하지 않음
	var dbStatusTextAddPointX = 48;									// DB Status Text X축 추가 시작점
	var dbStatusTextPointY_1 = yPoint + 41;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_2 = yPoint + 33;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_3 = yPoint + 25;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_4 = yPoint + 18;							// DB Status Text Y축 시작점. Row 4
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		var elemLeft = canvas.offsetLeft;
		var elemTop = canvas.offsetTop;
		
		var ctx = canvas.getContext("2d");
		
		statusNameArray.forEach(function(element, index){
			ctx.strokeStyle = white;
			ctx.lineWidth = dbStatusRectangleThickess;
			
			if(index == 0) {
				dbStatusRectanglePointX = dbStatusRectangleBasePointX;
			} else {
				dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleBasePointXPlus * index) + (dbStatusRectangleWidth * index);
			}
			
			ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
			ctx.fillStyle = checkDelegateAlarm(eval("row." + statusGradeArray[index]));
			ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
			
			setEventList(id, element, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, index);
			
			var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
			var lineheight = 15;
			var lines = element.split('\n');
			var y = eval("dbStatusTextPointY_" + lines.length);
			
			for (var i = 0; i<lines.length; i++) {
				ctx.font = defaultDbFont;
				ctx.textAlign = "center";
				ctx.fillStyle = white;
				ctx.fillText(lines[i], x, y + (i*lineheight) );
			}
		});
		
//		addEventListener(id, canvas, elemLeft, elemTop);
	}
}

function setEventList(id, statusName, left, top, width, height, index) {
	var _group;
	var _element = new Map();
	
	if(g_eventList.has(id)) {
		_group = g_eventList.get(id);
	} else {
		_group = new Map();
	}
	
	if(_group.has(statusName)) {
//		_group = _evnetList.get(groupId);
		//throw new Error('이미 저장된 canvas ID[' + id + '], statusName[' + statusName + ']를 받았습니다.');
		
		return;
	} else {
		_element = new Map();
	}
	
	_element.set('statusName', statusName);
	_element.set('left', left);
	_element.set('top', top);
	_element.set('width', width);
	_element.set('height', height);
	_element.set('index', index + 1);
	
	_group.set(statusName, _element);
	
	g_eventList.set(id, _group);
}

function addEventListener(id, canvas) {
	canvas.addEventListener("click", function(event) {
//		var x = event.pageX - elemLeft,
//				y = event.pageY - elemTop;
		
		var x = event.layerX;
		var y = event.layerY;
		
		if(g_eventList.has(id)) {
			var _group = g_eventList.get(id);
			const _groupIter = _group.entries();
			
			for(let [key, value] of _groupIter) {
				let _element = value;
				
				if (y > _element.get('top') && y < _element.get('top') + _element.get('height') 
						&& x > _element.get('left') && x < _element.get('left') + _element.get('width')) {
//					alert('clicked an _element id[' + id + '] group_id[' + key + '] statusName[' + _element.get('statusName') + ']');
					
					fnMove(id.substring(7), _element.get('index'));
					
					break;
				}
			}
		}
	}, false);
}

function fnMove(id, index) {
	location.href = '#' + id.toUpperCase() + '_' + index;
//	$('#' + id).scrollTop(100);
	
//	let select = '#' + id + '_' + index;
//	
//	$('#minute_area').animate({scrollTop : $(select).offset().top}, 400);
}

function drawDiagnosisResultSummaryDb(id, row) {
	var canvas = document.getElementById(id);
	var red = "#d90015";
	var orange = "#fe852b";
	var blue = "#0070c0";
	var green = "#00b050";
	var white = "#FFF";
	var black = "#000";
	var gray = "#7f7f7f";
	var yellow = "#020202";
	var defaultDbFont = "12px Open Sans";
	var bold18pxDbFont = "Bold 18px Open Sans";
	var bold24pxDbFont = "Bold 24px Open Sans";
	var bold48pxDbFont = "Bold 48px Open Sans";
	
	var yPoint = 0;
	var canvasRectangleWidth = 1500;								// canvas Rectangle Width(canvas element의 width보다 +1로 설정해야 정확함)
	var canvasRectangleHeight = 70;									// canvas Rectangle Height(canvas element의 height보다 +1로 설정해야 정확함)
	var paddingLeft = 65;											// 기본적인 좌측 패딩 길이
	var dbStatusRectangleThickess = 4;								// DB Status Rectangle Thickess
	var dbStatusRectangleWidth = 96;								// DB Status Rectangle Width
	var dbStatusRectangleHeight = 70;								// DB Status Rectangle Height
	var dbStatusRectangleBasePointX = paddingLeft;					// DB Status Rectangle X축 기준 시작점
	var dbStatusRectangleBasePointXPlus = 5;
	var dbStatusRectanglePointY = yPoint + 0;						// DB Status Rectangle Y축 시작점
	var dbStatusRectanglePointX = 0;								// DB Status Rectangle X축 시작점
	var dbStatusTextPointX = 50;									// DB Status Text X축 시작점. 사용하지 않음
	var dbStatusTextAddPointX = 48;									// DB Status Text X축 추가 시작점
	var dbStatusTextPointY_1 = yPoint + 41;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_2 = yPoint + 33;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_3 = yPoint + 25;							// DB Status Text Y축 시작점. Row 4
	var dbStatusTextPointY_4 = yPoint + 18;							// DB Status Text Y축 시작점. Row 4
	var dbStatusName1 = "DATABASE\nSTATUS";
	var dbStatusName2 = "EXPIRED\nGRACE\nACCOUNT";
	var dbStatusName3 = "MODIFIED\nPARAMETER";
	var dbStatusName4 = "NEW\nCREATED\nOBJECT";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		var elemLeft = canvas.offsetLeft;
		var elemTop = canvas.offsetTop;
		
		var ctx = canvas.getContext("2d");
		
		// DB
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(row.database_status_grade);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName1, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName1.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// INSTANCE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + dbStatusRectangleBasePointXPlus + dbStatusRectangleWidth;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(row.expired_account_grade);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName2, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName2.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// SPACE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleBasePointXPlus * 2) + (dbStatusRectangleWidth * 2);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(row.modified_parameter_grade);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName3, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName3.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// OBJECT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleBasePointXPlus * 3) + (dbStatusRectangleWidth * 3);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(row.new_created_object_grade);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
//		setEventList(id, dbStatusName4, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		var x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		var lineheight = 15;
		var lines = dbStatusName4.split('\n');
		var y = eval("dbStatusTextPointY_" + lines.length);
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
	}
}

var html =
	"<div id='DB_1'>"
	+"<h2 class='title' name='DB_1'>▪&nbsp;&nbsp;DATABASE STATUS</h2>"
	+"<div id='DB_T1'>"
	+"	<table class='om2' >"
	+"		<thead>"
	+"			<tr>"
	+"				<tr>"
	+"					<th class='om-left'>INST_ID</th>"
	+"					<th class='om-center'>LOG_MODE</th>"
	+"					<th class='om-center'>OPEN_MODE</th>"
	+"					<th class='om-right'>PLATFORM_NAME</th>"
	+"				</tr>"
	+"			</tr>"
	+"		</thead>"
	+"		<td colspan='11' class='om-colspan'>점검결과 정상입니다.</td>"
	+"	</table>"
	+"</div>"
+"</div>"

var callback_DiagnosisResultMinute = function(result) {
	var data = JSON.parse(result);
	var row = data[0];
	
	if(row == null || row == undefined) {
		return;
	}
	
	var lines = row.db_status_name.toLowerCase().split('\n');
	var status_name = "";
	
	for (var i = 0; i<lines.length; i++) {
		if(i == 0) {
			status_name = lines[i];
		} else {
			status_name = status_name + "_" + lines[i];
		}
	}
	
	var select = '#' + status_name + ' #minute_area';
	
	$(select).html(row.html);
	$(select).append();
}

var menuArray = ["database_status","expired_grace_account","modified_parameter","new_created_object","instance_status","listener_status","dbfiles","library_cache_hit",
	"dictionary_cache_hit","buffer_cache_hit","latch_hit","parse_cpu_to_parse_elapsd","disk_sort","memory_usage","resource_limit","background_dump_space",
	"archive_log_space","alert_log_space","fra_space","asm_diskgroup_space","tablespace","recyclebin_object","invalid_object","nologging_object","parallel_object",
	"unusable_index","chained_rows","corrupt_block","sequence","foreignkeys_without_index","disabled_constraint","missing_or_stale_statistics","statistics_locked_table",
	"long_running_operation","long_running_job","alert_log_error","active_incident","outstanding_alert","dbms_scheduler_job_failed"];

function exceptionRegistration(dbid, check_day, check_seq, field_name) {
	var menuUrl = "/PreventiveCheck/DetailCheckInfo";
	var menuParam = "dbid="+dbid
					+"&strStartDt="+getDateFormat(String(check_day))
					+"&check_item_name="+setJspName(field_name)
					+"&check_day="+String(check_day)
					+"&check_seq="+check_seq;
	
	parent.parent.openLink("Y", setMenuId(field_name), setMenuNm(field_name), menuUrl, menuParam);
}

function setJspName(fileName){
	var	fileArry = fileName.split('_');
	var newFileName = "";
	
	for(var i = 0 ; i < fileArry.length ; i++){
		if(i == 0){
			newFileName = fileArry[i];
		}else{
			newFileName += fileArry[i].substr(0,1).toUpperCase() + fileArry[i].substring(1, fileArry[i].length);
		}
	}
	
	return newFileName;
}

function setMenuId(fieldName){
	let menuId = "";
	
	for(var i = 0 ; i < menuArray.length ; i++){
		if(fieldName == menuArray[i]){
			menuId = $("#menu_id").val() + i;
			break;
		}
	}
	
	return menuId;
}

function setMenuNm(fieldName){
	return $('#menu_nm').val() + ' - ' + strReplace(fieldName, "_"," ").toUpperCase();
}
