var g_groupDbArray;			// 전체 Group DB를 관리하는 Array
var g_groupDbMap;			// 전체 Group DB를 관리하는 Map
var g_allDbMap;				// DB별 데이터 관리하는 Map
var g_maxDbColumns;			// DB
var g_dataMap = new Map();
var g_eventList;			// id별 그룹들의 event 관리

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	console.log("$('#user_id').val() :" + $('#user_id').val() );
	// DB 그룹 조회
	$('#perfDbGroupCombo').combobox({
		url:"/DailyCheckDb/dbGroupList?user_id"+ $('#user_id').val() +"&&isAll=Y",
		method:"get",
		valueField:'group_id',
		textField:'group_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
		}
	});
	
	// DB 그룹 조회
	$('#perfSeverityCombo').combobox({
		url:"/DailyCheckDb/severityList?isAll=Y",
		method:"get",
		valueField:'cd',
		textField:'cd_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
		}
	});
	
	dbSeverityCount();
	
	Btn_OnClick();
});

function dbSeverityCount() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터를 수집 중입니다"," ");
	
	ajaxCall("/DailyCheckDb/dbSeverityCount",
			$("#submit_form"),
			"POST",
			callback_DbSeverityCountAction);
}

var callback_DbSeverityCountAction = function(rows) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	var data = JSON.parse(rows);
	
	if(data.length == 0) {
		return;
	}
	
	var db_critical_cnt = data[0].db_critical_cnt;
	var db_warning_cnt = data[0].db_warning_cnt;
	var db_info_cnt = data[0].db_info_cnt;
	var db_normal_cnt = data[0].db_normal_cnt;
	
	$("label[for='severity_1']").text('긴급조치(' + db_critical_cnt + ')');
	$("label[for='severity_2']").text('조치필요(' + db_warning_cnt + ')');
	$("label[for='severity_3']").text('확인필요(' + db_info_cnt + ')');
	$("label[for='severity_4']").text('정상(' + db_normal_cnt + ')');
}

function Btn_OnClick() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("2번째 데이터를 수집 중입니다"," ");
	
	$('#choice_db_group_id').val($('#perfDbGroupCombo').combobox('getValue'));
	$('#choice_severity_id').val($('#perfSeverityCombo').combobox('getValue'));
	
	console.log("DB그룹[" + $('#perfDbGroupCombo').combobox('getValue') + "] 심각도[" + $('#perfSeverityCombo').combobox('getValue') + "]");
	
	ajaxCall("/DailyCheckDb/dbMain",
			$("#submit_form"),
			"POST",
			callback_DbMainAction);
}

var callback_DbMainAction = function(rows) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	var data = JSON.parse(rows);
	
	initGlobalVariable();
	
	checkGroupData(data);
	checkDbData(data);
	checkDataMap(data);
	draw();
}

function initGlobalVariable() {
	g_groupDbArray = new Array();
	g_groupDbMap = new Map();
	g_allDbMap = new Map();
	g_maxDbColumns = 0;
	g_dataMap = new Map();
	g_eventList = new Map();

}

function checkGroupData(data) {
	var dataLen = data.length;
	var group_nm;
	var group_cnt;
	var groupHtml = "";
	
	for(var index = 0; index < dataLen; index++) {
		group_nm = data[index].group_nm;
		group_cnt = data[index].group_cnt;
		
		compareMaxDbColumns(group_cnt);
		
		if(g_groupDbArray.indexOf(group_nm) < 0) {
			g_groupDbArray.push(group_nm);
			g_groupDbMap.set(group_nm, group_cnt);
			
////			
////			if(index != 0) {
////				groupHtml += "</div>";
////			}
////			
////			groupHtml += "<div id=group_" + group + " style='outline: #00FF00 dotted thick;'>";
////			groupHtml += "<label for=group_label_'" + group + "'>" + group + "</label>";
//		} else {
//			groupHtml += "<label for=group_label_'" + group + "'>" + group + "</label>";
		}
		
//		if(index == dataLen - 1) {
//			groupHtml += "</div>";
//		}
	}
	
//	$('#canvas').html(groupHtml);
//	$('#canvas').append();
}

function compareMaxDbColumns(dbCount) {
	if(dbCount > g_maxDbColumns) {
		g_maxDbColumns = dbCount;
	}
}

function checkDbData(data) {
	var dataLen = data.length;
	
	for(var index = 0; index < dataLen; index++) {
		var row = data[index];
		
		g_allDbMap.set(row.db_title, addDbArray(row));
	}
}

function checkDataMap(data) {
	var dataLen = data.length;
	var dbMap;
	
	for(var index = 0; index < dataLen; index++) {
		var row = data[index];
		var group_nm = row.group_nm;
		
		if(g_dataMap.has(group_nm) == false) {
			dbMap = new Map();
			
			dbMap.set(row.dbid, addDbArray(row));
			
			g_dataMap.set(group_nm, dbMap);
		} else {
			dbMap = g_dataMap.get(group_nm);
			
			dbMap.set(row.dbid, addDbArray(row));
			
			g_dataMap.set(group_nm, dbMap);
		}
	}
}

function addDbArray(row) {
	var dbArray = new Array();
	var group_nm, group_cnt, db_title, dbid_chekc_grade_cd, check_pref_nm, check_dt, check_cnt, dbid;
	var db_check_grade_cd, instance_check_grade_cd, space_check_grade_cd, object_check_grade_cd, statistics_check_grade_cd, long_running_check_grade_cd, alert_grade_cd;
	
	group_nm = row.group_nm;
	group_cnt = row.group_cnt;
	db_title = row.db_title;
	dbid_chekc_grade_cd = row.dbid_chekc_grade_cd;
	check_pref_nm = row.check_pref_nm;
	check_dt = row.check_dt;
	check_cnt = row.check_cnt;
	dbid = row.dbid;
	
	db_check_grade_cd = row.db_check_grade_cd;
	instance_check_grade_cd = row.instance_check_grade_cd;
	space_check_grade_cd = row.space_check_grade_cd;
	object_check_grade_cd = row.object_check_grade_cd;
	statistics_check_grade_cd = row.statistics_check_grade_cd;
	long_running_check_grade_cd = row.long_running_check_grade_cd;
	alert_grade_cd = row.alert_grade_cd;
	
	dbArray.push(group_nm);
	dbArray.push(group_cnt);
	dbArray.push(db_title);
	dbArray.push(dbid_chekc_grade_cd);
	dbArray.push(check_pref_nm);
	dbArray.push(check_dt);
	dbArray.push(check_cnt);
	dbArray.push(dbid);
	dbArray.push(db_check_grade_cd);
	dbArray.push(instance_check_grade_cd);
	dbArray.push(space_check_grade_cd);
	dbArray.push(object_check_grade_cd);
	dbArray.push(statistics_check_grade_cd);
	dbArray.push(long_running_check_grade_cd);
	dbArray.push(alert_grade_cd);
	
	return dbArray;
}

function draw() {
	console.log(g_groupDbArray);
	var groupDbMapCount = g_groupDbMap.size;
	console.log("group db count[" + groupDbMapCount + "]");
	console.log(g_groupDbMap);
	console.log(g_allDbMap);
	console.log("max db columns[" + g_maxDbColumns + "]");
	
	console.log(g_dataMap);
	
	var html = "";
	var grouDb;
	
//	for(var groupDbIndex = 0; groupDbIndex < groupDbMapCount; groupDbIndex++) {
//		groupDb = g_groupDbArray[groupDbIndex];
//		
//		html += "<h1>" + groupDb + "</h1>";
//	}
	
	const dataMapIter = g_dataMap.entries();
	
//	for(let groupDbMapIterData of groupDbMapIter) {
//		console.log(groupDbMapIterData);
//		console.log(groupDbMapIterData.key);
//		console.log(groupDbMapIterData.value);
//	}
	
	for(let [dataMapKey, dataMapValue] of dataMapIter) {
//		html += "<h1 style='font-size:20px'>" + dataMapKey + "</h1>";
		
		if(g_maxDbColumns < 3) {
			// n by 2 Matrix
			html += nByTwoMatrix(dataMapValue);
		} else {
			// n by 4 Matrix
			
		}
	}
	
	$('#canvas').html(html);
	$('#canvas').append();
//	drawCanvasUnit();
	
//	drawCanvas2Columns('canvas1');
//	drawCanvas2Columns('canvas2');
//	drawCanvas2Columns('canvas3');
//	drawCanvas2Columns('canvas4');
//	
//	drawCanvas2Columns('canvas11');
//	drawCanvas2Columns('canvas22');
//	drawCanvas2Columns('canvas33');
//	drawCanvas2Columns('canvas44');
	
	drawCanvas3Columns('canvas1');
	drawCanvas3Columns('canvas2');
	drawCanvas3Columns('canvas3');
	drawCanvas3Columns('canvas4');
	
	drawCanvas3Columns('canvas11');
	drawCanvas3Columns('canvas22');
	drawCanvas3Columns('canvas33');
	drawCanvas3Columns('canvas44');
}

function drawGroupDb() {
	
}

function nByTwoMatrix(dbDataMap) {
	const dbDataMapIter = dbDataMap.entries();
	var html = "";
	
	for(let [dbDataMapKey, dbDataMapValue] of dbDataMapIter) {
		var db_title = dbDataMapKey;
		var dbMap = dbDataMapValue;
		var randomId = Math.random();
		
		html += "<div id=" + randomId + ">";
//		html += "\t<h2 style='font-size:20px'>" + db_title + "</h2>";
//		drawCanvas(randomId);
		html += "</div>";
		
	}
	
	return html;
}

function drawCanvas2Columns(id) {
	var canvas = document.getElementById(id);
	var red = "#d90015";
	var orange = "#fe852b";
	var blue = "#0070c0";
	var green = "#00b050";
	var white = "#FFF";
	var black = "#000";
	var gray = "#7f7f7f";
	var defaultDbFont = "12px Open Sans";
	var bold18pxDbFont = "Bold 18px Open Sans";
	var bold24pxDbFont = "Bold 24px Open Sans";
	var bold48pxDbFont = "Bold 48px Open Sans";
	
	var yPoint = 0;
	var boxWidth = 550;
	var boxHeight = 170;
	var paddingLeft = 20;
	var dbTitleYPointEnd = 30;
	var boxDbXPoint = paddingLeft;
	var boxDbYPoint = yPoint + 110;
	var xDbPoint = 0;
	var fontDbXPoint = 50;
	var fontDbYPoint = yPoint + 145;
	var fontTitleYPoint = yPoint + 20;
	var fontCheckPrefXPoint = 45;
	var fontCheckPrefYPoint = yPoint + 70;
	var fontCheckDtYPoint = yPoint + 100;
	var fontCheckPlusYPoint = yPoint + 70;
	var fontCheckCountYPoint = yPoint + 95;
	var checkCntBoxPaddingLeft = boxWidth - 60;
	var checkCntBoxFontPaddingLeft = checkCntBoxPaddingLeft + 31;
	var checkDtFontPaddingLeft = checkCntBoxPaddingLeft - 105;
	var lineWidth = 4;
	var dbWidth = 76;
	var dbHeight = 60;
	var fontDbPaddingLeftCollect = 38;
	var dbName = "OPENPOP: OPEN-POP";
	var checkPrefName = "Expired(grace) 계정 외";
	var checkDt = "2020-03-19 17:59";
	var checkCount = "9999";
	var dbStatusName1 = "DB";
	var dbStatusName2 = "INSTANCE";
	var dbStatusName3 = "SPACE";
	var dbStatusName4 = "OBJECT";
	var dbStatusName5 = "STATISTICS";
	var dbStatusName6 = "LONG\nRUNNING\nWORK";
	var dbStatusName7 = "ALERT";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		canvas.addEventListener("click", function(e) {
			console.log({x:e.layerX, y: e.layerY});
		});
		
		var ctx = canvas.getContext("2d");
		
		ctx.fillStyle = orange;
		ctx.fillRect(0, yPoint, boxWidth, boxHeight);
		ctx.font = bold24pxDbFont;
		ctx.fillStyle = white;
		ctx.fillText(checkPrefName, fontCheckPrefXPoint, fontCheckPrefYPoint);
		
		var imgClo = new Image();
		
		imgClo.addEventListener('load', function(){
			var ctxImg = ctx;
			
			ctxImg.drawImage(imgClo, fontCheckPrefXPoint - 42, fontCheckPrefYPoint - 25, 40, 35);
		});
		
		imgClo.src = "/resources/images/daily/severity_e_2.png";
		
		ctx.fillStyle = white;
		ctx.fillRect(paddingLeft, yPoint, boxWidth, dbTitleYPointEnd);
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = black;
		ctx.fillText(dbName, 35, fontTitleYPoint);
		ctx.fillStyle = white;
		ctx.font = defaultDbFont;
		
		if(true) {
			ctx.fillText(checkDt, checkDtFontPaddingLeft, fontCheckDtYPoint);
			
			ctx.fillStyle = white;
			ctx.fillRect(checkCntBoxPaddingLeft - 2, dbTitleYPointEnd, 78, 140);
			ctx.fillStyle = gray;
			ctx.fillRect(checkCntBoxPaddingLeft, dbTitleYPointEnd, 80, 140);
			ctx.font = bold18pxDbFont;
			ctx.fillStyle = white;
			ctx.textAlign = "center";
			ctx.font = bold48pxDbFont;
			ctx.fillText("+", checkCntBoxFontPaddingLeft, fontCheckPlusYPoint);
			ctx.font = bold18pxDbFont;
			ctx.fillText(checkCount, checkCntBoxFontPaddingLeft, fontCheckCountYPoint);
		} else {
			ctx.fillText(checkDt, boxWidth - 105, fontCheckDtYPoint);
		}
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = orange;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName1, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + dbWidth;
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName2, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 2);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = blue;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName3, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 3);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName4, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 4);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName5, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 5);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		var x = xDbPoint + fontDbPaddingLeftCollect;
		var y = fontDbYPoint - 15;
		var lineheight = 15;
		var lines = dbStatusName6.split('\n');
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 6);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName7, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
	}
}

function drawCanvas3Columns(id) {
	var canvas = document.getElementById(id);
	var red = "#d90015";
	var orange = "#fe852b";
	var blue = "#0070c0";
	var green = "#00b050";
	var white = "#FFF";
	var black = "#000";
	var gray = "#7f7f7f";
	var defaultDbFont = "11px Open Sans";
	var bold18pxDbFont = "Bold 18px Open Sans";
	var bold24pxDbFont = "Bold 24px Open Sans";
	var bold48pxDbFont = "Bold 48px Open Sans";
	
	var yPoint = 0;
	var boxWidth = 520;
	var boxHeight = 170;
	var paddingLeft = 20;
	var dbTitleYPointEnd = 30;
	var boxDbXPoint = paddingLeft;
//	var boxDbYPoint = yPoint + 108;
	var boxDbYPoint = yPoint + 110;
	var xDbPoint = 0;
	var fontDbXPoint = 50;
	var fontDbYPoint = yPoint + 145;
	var fontTitleYPoint = yPoint + 20;
	var fontCheckPrefXPoint = 65;
	var fontCheckPrefYPoint = yPoint + 70;
	var fontCheckDtYPoint = yPoint + 100;
	var fontCheckPlusYPoint = yPoint + 70;
	var fontCheckCountYPoint = yPoint + 95;
	var checkCntBoxPaddingLeft = boxWidth - 60;
	var checkCntBoxFontPaddingLeft = checkCntBoxPaddingLeft + 31;
	var checkDtFontPaddingLeft = checkCntBoxPaddingLeft - 105;
	var lineWidth = 4;
//	var dbWidth = 76;
	var dbWidth = 72;
	var dbHeight = 60;
	var fontDbPaddingLeftCollect = 38;
	var dbName = "OPENPOP: OPEN-POP";
	var checkPrefName = "Expired(grace) 계정 외";
	var checkPrefNameNormal = "진단결과 정상입니다.";
	var checkDt = "2020-03-19 17:59";
	var checkCount = "9999";
	var dbStatusName1 = "DB";
	var dbStatusName2 = "INSTANCE";
	var dbStatusName3 = "SPACE";
	var dbStatusName4 = "OBJECT";
	var dbStatusName5 = "STATISTICS";
	var dbStatusName6 = "LONG\nRUNNING\nWORK";
	var dbStatusName7 = "ALERT";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		var elemLeft = canvas.offsetLeft;
		var elemTop = canvas.offsetTop;
		
		var ctx = canvas.getContext("2d");
		
		ctx.fillStyle = orange;
		ctx.fillRect(0, yPoint, boxWidth, boxHeight);
		ctx.font = bold24pxDbFont;
		ctx.fillStyle = white;
		
		if(true) {
			ctx.fillText(checkPrefName, fontCheckPrefXPoint, fontCheckPrefYPoint);
			
			var imgClo = new Image();
			
			imgClo.addEventListener('load', function(){
				var ctxImg = ctx;
				
				ctxImg.drawImage(imgClo, fontCheckPrefXPoint - 42, fontCheckPrefYPoint - 25, 40, 35);
			});
			
			imgClo.src = "/resources/images/daily/severity_e_2.png";
		} else {
			ctx.fillText(checkPrefNameNormal, fontCheckPrefXPoint, fontCheckPrefYPoint);
		}
		
		ctx.fillStyle = white;
		ctx.fillRect(paddingLeft, yPoint, boxWidth, dbTitleYPointEnd);
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = black;
		ctx.fillText(dbName, 35, fontTitleYPoint);
		
		setEventList(id, 'advisor_rec', boxWidth - 29, dbTitleYPointEnd - 30, 29, 30);
		
		var imgClo2 = new Image();
		
		imgClo2.addEventListener('load', function(){
			var ctxImg = ctx;
			
			ctxImg.drawImage(imgClo2, boxWidth - 29, dbTitleYPointEnd - 30, 29, 30);
		});
		
		imgClo2.src = "/resources/images/daily/advisor_recommendation.png";
		
		ctx.fillStyle = white;
		ctx.font = defaultDbFont;
		
		if(true) {
			ctx.fillText(checkDt, checkDtFontPaddingLeft, fontCheckDtYPoint);
			
			ctx.fillStyle = white;
			ctx.fillRect(checkCntBoxPaddingLeft - 2, dbTitleYPointEnd, 78, 140);
			ctx.fillStyle = gray;
			ctx.fillRect(checkCntBoxPaddingLeft, dbTitleYPointEnd, 80, 140);
			ctx.font = bold18pxDbFont;
			ctx.fillStyle = white;
			ctx.textAlign = "center";
			ctx.font = bold48pxDbFont;
			ctx.fillText("+", checkCntBoxFontPaddingLeft, fontCheckPlusYPoint);
			ctx.font = bold18pxDbFont;
			ctx.fillText(checkCount, checkCntBoxFontPaddingLeft, fontCheckCountYPoint);
		} else {
			ctx.fillText(checkDt, boxWidth - 105, fontCheckDtYPoint);
		}
		
		// DB
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = orange;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		setEventList(id, dbStatusName1, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName1, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		// INSTANCE
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + dbWidth;
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		setEventList(id, dbStatusName2, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName2, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		// SPACE
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 2);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = blue;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		setEventList(id, dbStatusName3, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName3, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		// OBJECT
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 3);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		setEventList(id, dbStatusName4, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName4, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		// STATISTICS
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 4);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		setEventList(id, dbStatusName5, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName5, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		// LONG RUNNING WORK
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 5);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		setEventList(id, dbStatusName6, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		var x = xDbPoint + fontDbPaddingLeftCollect;
		var y = fontDbYPoint - 15;
		var lineheight = 15;
		var lines = dbStatusName6.split('\n');
		
		for (var i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// ALERT
		ctx.strokeStyle = white;
		ctx.lineWidth = lineWidth;
		xDbPoint = boxDbXPoint + (dbWidth * 6);
		ctx.strokeRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		ctx.fillStyle = green;
		ctx.fillRect(xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		setEventList(id, dbStatusName7, xDbPoint, boxDbYPoint, dbWidth, dbHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName7, xDbPoint + fontDbPaddingLeftCollect, fontDbYPoint);
		
		addEventListener(id, canvas, elemLeft, elemTop);
	}
}

function addEventListener(id, canvas, elemLeft, elemTop) {
	canvas.addEventListener("click", function(event) {
		console.log({x:event.layerX, y: event.layerY});
		
		var x = event.pageX - elemLeft,
				y = event.pageY - elemTop;
		
		if(g_eventList.has(id)) {
			var _group = g_eventList.get(id);
			const _groupIter = _group.entries();
			
			for(let [key, value] of _groupIter) {
				var _element = value;
				
				if (y > _element.get('top') && y < _element.get('top') + _element.get('height') 
						&& x > _element.get('left') && x < _element.get('left') + _element.get('width')) {
					alert('clicked an _element id[' + id + '] group_id[' + key + ']');
					
					var tabTitle = 'DB 상태 점검 상세(' + canvas + ')';
					var check_day = "";
					var check_seq = "";
					
//					parent.createMinuteDbStatus($("#menu_id").val(), "dailyCheckDbTabs", $("#dbid").val(), check_day, check_seq, tabTitle);
					createMinuteDbStatus($("#menu_id").val(), "dailyCheckDbTabs", $("#dbid").val(), check_day, check_seq, tabTitle);
				}
			}
		}
	}, false);
}

function createMinuteDbStatus(menu_id, tab_id, dbid, check_day, check_seq, tabTitle) {
	parent.parent.createMinuteDbStatusNewTab($("#menu_id").val(), tab_id, dbid, check_day, check_seq, tabTitle);
}

function setEventList(id, groupId, left, top, width, height, color) {
	var _group;
	var _element = new Map();
	
	if(g_eventList.has(id)) {
		_group = g_eventList.get(id);
	} else {
		_group = new Map();
	}
	
	if(_group.has(groupId)) {
//		_group = _evnetList.get(groupId);
		throw new Error('이미 저장된 canvas ID[' + id + '], Group ID[' + groupId + ']를 받았습니다.');
		try {
			node.addEventListener(event, listener, false);
		} catch(err) {
			console.error(err);
		}
	} else {
		_element = new Map();
	}
	
	_element.set('left', left);
	_element.set('top', top);
	_element.set('width', width);
	_element.set('height', height);
	
	if(color) {
		_element.set('color', color);
	}
	
	_group.set(groupId, _element);
	
	g_eventList.set(id, _group);
}
