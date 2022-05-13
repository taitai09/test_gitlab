var g_groupDbArray;			// 전체 Group DB를 관리하는 Array
var g_groupDbMap;			// 전체 Group DB를 관리하는 Map
var g_allDbMap;				// DB별 데이터 관리하는 Map
var g_maxDbColumns;			// DB
var g_dataMap = new Map();
var g_eventList;			// id별 그룹들의 event 관리

$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$('#dailyCheckDb_form #dailyCheckDbTabs').tabs({
		plain : true,
		onSelect: function(title, index) {
			if(index == 0) {
				$('#choice_db_group_id').val('');
			} else if(index == 1) {
				$('#choice_db_group_id').val('');
				dailyCheckDbSituation();
			}
		}
	});
	
	// DB 그룹 조회
	$('#perfDbGroupCombo').combobox({
		url:"/DailyCheckDb/dbGroupList?user_id="+ $('#user_id').val() +"&&isAll=Y",
		method:"get",
		valueField:'group_id',
		textField:'group_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				let opts = $(this).combobox('options');
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
				let opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
				
				for(var index = 0; index < items.length; index++) {
					if(index == 0) {
						let opts = $(this).combobox('options');
						$(this).combobox('select', items[0][opts.valueField]);
					} else {
						$('#severity_color_' + (index - 1)).val(items[index].color);
					}
				}
			}
			
		}
	});
	
	Btn_OnClick();
});

function dbSeverityCount() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("데이터를 수집 중입니다"," ");
	
	ajaxCall("/DailyCheckDb/dbSeverityCount",
			$("#dailyCheckDb_form"),
			"POST",
			callback_DbSeverityCountAction);
}

var callback_DbSeverityCountAction = function(rows) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	let data;
	
	if ( rows != "" && rows != null ) {
		data = JSON.parse(rows);
	} else {
		return;
	}
	
	let db_critical_cnt = data[0].db_critical_cnt;
	let db_warning_cnt = data[0].db_warning_cnt;
	let db_info_cnt = data[0].db_info_cnt;
	let db_normal_cnt = data[0].db_normal_cnt;
	let db_unchecked_cnt = data[0].db_unchecked_cnt;
	
	if(db_unchecked_cnt > 0) {
		$('#severity_div_4').addClass('severity_4');
		$("label[for='severity_text_4']").text('미수행 (' + db_unchecked_cnt + ')');
	} else {
		$('#severity_div_4').removeClass('severity_4');
		$('#severity_div_4').css('display','inline');
		$("label[for='severity_text_4']").text('');
	}
	
	$("label[for='severity_text_0']").text('긴급조치(' + db_critical_cnt + ')');
	$("label[for='severity_text_1']").text('조치필요(' + db_warning_cnt + ')');
	$("label[for='severity_text_2']").text('확인필요(' + db_info_cnt + ')');
	$("label[for='severity_text_3']").text('정상(' + db_normal_cnt + ')');
}

function Btn_OnClick() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("2번째 데이터를 수집 중입니다"," ");
	
	$('#choice_db_group_id').val($('#perfDbGroupCombo').combobox('getValue'));
	$('#choice_severity_id').val($('#perfSeverityCombo').combobox('getValue'));
	
	ajaxCall("/DailyCheckDb/dbMain",
			$("#dailyCheckDb_form"),
			"POST",
			callback_DbMainAction);
}

var callback_DbMainAction = function(rows) {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	let data;
	
	if ( rows != "" && rows != null ) {
		data = JSON.parse(rows);
	} else {
		if ( $("#perfSeverityCombo").combobox('getValue') == "" ) {
			parent.$.messager.alert('',"<label style='font-size:15px;'>일예방점검 내역이 없습니다.</label>","warning");
			return;
		} else {
			return;
		}
	}
	
	initGlobalVariable();
	
	checkGroupData(data);
	checkDbData(data);
	checkDataMap(data);
	draw();
	
	dbSeverityCount();
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
	let dataLen = data.length;
	let group_nm;
	let group_cnt;
	let groupHtml = "";
	
	for(let index = 0; index < dataLen; index++) {
		group_nm = data[index].group_nm;
		group_cnt = data[index].group_cnt;
		
		compareMaxDbColumns(group_cnt);
		
		if(g_groupDbArray.indexOf(group_nm) < 0) {
			g_groupDbArray.push(group_nm);
			g_groupDbMap.set(group_nm, group_cnt);
		}
	}
}

function compareMaxDbColumns(dbCount) {
	if(dbCount > g_maxDbColumns) {
		g_maxDbColumns = dbCount;
	}
}

function checkDbData(data) {
	let dataLen = data.length;
	
	for(let index = 0; index < dataLen; index++) {
		let row = data[index];
		
		g_allDbMap.set(row.db_title, addDbArray(row));
	}
}

function checkDataMap(data) {
	let dataLen = data.length;
	let dbMap;
	
	for(let index = 0; index < dataLen; index++) {
		let row = data[index];
		let group_nm = row.group_nm;
		
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
	let dbArray = new Array();
	let group_nm, group_cnt, db_title, dbid_chekc_grade_cd, check_pref_nm, check_dt, check_cnt, dbid, check_day, check_seq;
	let db_check_grade_cd, instance_check_grade_cd, space_check_grade_cd, object_check_grade_cd, statistics_check_grade_cd, long_running_check_grade_cd, alert_grade_cd;
	
	group_nm = row.group_nm;
	group_cnt = row.group_cnt;
	db_title = row.db_title;
	dbid_check_grade_cd = row.dbid_check_grade_cd;
	check_pref_nm = row.check_pref_nm;
	check_dt = row.check_dt;
	check_cnt = row.check_cnt;
	dbid = row.dbid;
	check_day = row.check_day;
	check_seq = row.check_seq;
	
	db_check_grade_cd = row.db_check_grade_cd;
	instance_check_grade_cd = row.instance_check_grade_cd;
	space_check_grade_cd = row.space_check_grade_cd;
	object_check_grade_cd = row.object_check_grade_cd;
	statistics_check_grade_cd = row.statistics_check_grade_cd;
	long_running_check_grade_cd = row.long_running_check_grade_cd;
	alert_grade_cd = row.alert_grade_cd;
	
	dbArray.push(group_nm);						// Index 0
	dbArray.push(group_cnt);
	dbArray.push(db_title);
	dbArray.push(dbid_check_grade_cd);
	dbArray.push(check_pref_nm);
	dbArray.push(check_dt);						// Index 5
	dbArray.push(check_cnt);
	dbArray.push(dbid);
	dbArray.push(db_check_grade_cd);
	dbArray.push(instance_check_grade_cd);
	dbArray.push(space_check_grade_cd);			// Index 10
	dbArray.push(object_check_grade_cd);
	dbArray.push(statistics_check_grade_cd);
	dbArray.push(long_running_check_grade_cd);
	dbArray.push(alert_grade_cd);
	dbArray.push(check_day);					// Index 15
	dbArray.push(check_seq);
	
	return dbArray;
}

function draw() {
	console.log(g_groupDbArray);
	let groupDbMapCount = g_groupDbMap.size;
	console.log("group db count[" + groupDbMapCount + "]");
	console.log(g_groupDbMap);
	console.log(g_allDbMap);
	console.log("max db columns[" + g_maxDbColumns + "]");
	
	console.log(g_dataMap);
	
	let html = "";
	let grouDb;
	
	let dataMapIter = g_dataMap.entries();
	
	/***** Real Data Area *************************************************************/
	// write to HTML
	for(let [dataMapKey, dataMapValue] of dataMapIter) {
		html += "\r<div style='padding:10px 0px 5px 0px;'><label class='group_db_title'>&#x25b6; " + dataMapKey + "</label></div>";
		
		if(g_maxDbColumns <= 2) {
			// n by 2 Matrix
			html += nByTwoMatrix(dataMapValue);
		} else {
			// n by 3 Matrix
			html += nByThreeMatrix(dataMapValue);
		}
		
		html += "\r</div>";
	}
	
	
	$('#canvas').html(html);
	$('#canvas').append();
	
	dataMapIter = g_dataMap.entries();
	
	// write to draw
	for(let [dataMapKey, dataMapValue] of dataMapIter) {
		drawMetrix(dataMapValue);
	}
	/***** End Real Data Area *********************************************************/
	
	/***** Test Area ************/
//	drawCanvas2Columns('canvas1');
//	drawCanvas2Columns('canvas2');
//	drawCanvas2Columns('canvas3');
//	drawCanvas2Columns('canvas4');
//	
//	drawCanvas2Columns('canvas11');
//	drawCanvas2Columns('canvas22');
//	drawCanvas2Columns('canvas33');
//	drawCanvas2Columns('canvas44');
	
//	drawCanvas3Columns('canvas1');
//	drawCanvas3Columns('canvas2');
//	drawCanvas3Columns('canvas3');
//	drawCanvas3Columns('canvas4');
//	
//	drawCanvas3Columns('canvas11');
//	drawCanvas3Columns('canvas22');
//	drawCanvas3Columns('canvas33');
//	drawCanvas3Columns('canvas44');
	/***** End Test Area *********/
}

function nByTwoMatrix(dbDataMap) {
	let dbDataMapIter = dbDataMap.entries();
	let html = "";
	
	for(let [dbDataMapKey, dbDataMapValue] of dbDataMapIter) {
		let db_title = dbDataMapKey;
		let dbMap = dbDataMapValue;
		
		html += "\r\t<canvas id='canvas_" + db_title + "' width='540' height='160' style='padding:5px;border:1px solid #9f9f9f;'></canvas>";
	}
	
	return html;
}

function nByThreeMatrix(dbDataMap) {
	let dbDataMapIter = dbDataMap.entries();
	let html = "";
	
	for(let [dbDataMapKey, dbDataMapValue] of dbDataMapIter) {
		let db_title = dbDataMapKey;
		let dbMap = dbDataMapValue;
		
		html += "\r\t<canvas id='canvas_" + db_title + "' width='510' height='160' style='padding:5px;border:1px solid #9f9f9f;'></canvas>";
	}
	
	return html;
}

function drawMetrix(dbDataMap) {
	let dbDataMapIter = dbDataMap.entries();
	
	for(let [dbDataMapKey, dbDataMapValue] of dbDataMapIter) {
		let db_title = dbDataMapKey;
		let dbMap = dbDataMapValue;
		let id = "canvas_" + db_title;
		
		drawColumn(id, dbMap, g_maxDbColumns);
	}
}

function drawColumn(id, dbMap, column_length) {
	let canvas = document.getElementById(id);
	let red = "#d90015";
	let orange = "#fe852b";
	let blue = "#0070c0";
	let green = "#00b050";
	let white = "#FFF";
	let black = "#000";
	let gray = "#7f7f7f";
	let yellow = "#020202";
	let defaultDbFont = "12px Open Sans";
	
	if(column_length > 2) {
		defaultDbFont = "11px Open Sans";
	}
	
	let bold18pxDbFont = "Bold 18px Open Sans";
	let checkPrefNameboldFont = "Bold 24px Open Sans";
	
	if(column_length > 2) {
		checkPrefNameboldFont = "Bold 22px Open Sans";
	}
	
	let bold48pxDbFont = "Bold 48px Open Sans";
	
	let yPoint = 0;
	let canvasRectangleWidth = 550;									// canvas Rectangle Width(canvas element의 width보다 +1로 설정해야 정확함)
	
	if(column_length > 2) {
		canvasRectangleWidth = 520;
	}
	
	let canvasRectangleHeight = 170;								// canvas Rectangle Height(canvas element의 height보다 +1로 설정해야 정확함)
	let paddingLeft = 20;											// 기본적인 좌측 패딩 길이
	let dbTitleRectangleHeight = 30;								// DB Title Rectangle Height
	let dbTitleTextPointX = 55;										// DB Title Text X축 시작점
	let dbTitleTextPointY = yPoint + 20;							// DB Title Text Y축 시작점
	let checkPrefTextPointX = 65;									// Check Pref Text X축 시작점
	let checkPrefTextPointY = yPoint + 70;							// Check Pref Text Y축 시작점
	let checkDtTextPointY = yPoint + 100;							// Check Dt Text Y축 시작점
	let checkPlusTextPointY = yPoint + 70;							// Check + Text Y축 시작점
	let checkCountTextPointY = yPoint + 95;							// Check Count Text Y축 시작점
	let checkCountRectanglePointX = canvasRectangleWidth - 60;		// Check Count Rectangle X축 시작점
	let checkCountTextPointX = checkCountRectanglePointX + 31;		// Check Count Text X축 시작점
	let checkDtTextPointX = checkCountRectanglePointX - 120;		// Check Dt Text X축 시작점
	let advisorRecImagePointX = canvasRectangleWidth - 29;			// Advisor Recommendation Image X축 시작점
	let advisorRecImagePointY = dbTitleRectangleHeight - 30;		// Advisor Recommendation Image Y축 시작점
	let dbStatusRectangleThickess = 4;								// DB Status Rectangle Thickess
	let dbStatusRectangleWidth = 76;								// DB Status Rectangle Width
	
	if(column_length > 2) {
		dbStatusRectangleWidth = 72;
	}
	
	let dbStatusRectangleHeight = 60;								// DB Status Rectangle Height
	let dbStatusRectangleBasePointX = paddingLeft;					// DB Status Rectangle X축 기준 시작점
	let dbStatusRectanglePointY = yPoint + 110;						// DB Status Rectangle Y축 시작점
	let dbStatusRectanglePointX = 0;								// DB Status Rectangle X축 시작점
	let dbStatusTextPointX = 50;									// DB Status Text X축 시작점. 사용하지 않음
	let dbStatusTextAddPointX = 38;									// DB Status Text X축 추가 시작점
	let dbStatusTextPointY = yPoint + 145;							// DB Status Text Y축 시작점
	let titleName = dbMap[2];
	let checkPrefName = dbMap[4].length > 40 ? dbMap[4].substring(0, 40) + " ..." : dbMap[4];
	let checkPrefNameNormal = "진단결과 정상입니다.";
	let checkDt = dbMap[5];
	let checkCount = dbMap[6];
	let dbStatusName1 = "DB";
	let dbStatusName2 = "INSTANCE";
	let dbStatusName3 = "SPACE";
	let dbStatusName4 = "OBJECT";
	let dbStatusName5 = "STATISTICS";
	let dbStatusName6 = "LONG\nRUNNING\nWORK";
	let dbStatusName7 = "ALERT";
	let dbName = titleName.substring(0, titleName.indexOf(":"));
	
	let dbDelegateAlarm = dbMap[3];
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		let elemLeft = canvas.offsetLeft;
		let elemTop = canvas.offsetTop;
		
		let ctx = canvas.getContext("2d");
		
		// checkPref
		ctx.fillStyle = checkDelegateAlarm(dbMap[3]);
		ctx.fillRect(0, yPoint, canvasRectangleWidth, canvasRectangleHeight);
//		ctx.font = checkPrefNameboldFont;
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = white;
		ctx.fillText(checkPrefName, checkPrefTextPointX, checkPrefTextPointY);
		
		let imgClo = new Image();
		
		imgClo.src = checkDelegateAlarmImage(dbDelegateAlarm);
		imgClo.addEventListener('load', function(){
			let ctxImg = ctx;
			
			ctxImg.drawImage(imgClo, checkPrefTextPointX - 42, checkPrefTextPointY - 25, 40, 35);
		});
		
		// DB Title
		let imgClo3 = new Image();
		
		imgClo3.src = "/resources/images/daily/db.png";
		imgClo3.addEventListener('load', function(){
			let ctxImg = ctx;
			
			ctxImg.drawImage(imgClo3, dbTitleTextPointX - 30, dbTitleTextPointY - 20, 25, 25);
		});
		
		
		ctx.fillStyle = white;
		ctx.fillRect(paddingLeft, yPoint, canvasRectangleWidth, dbTitleRectangleHeight);
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = black;
		ctx.fillText(titleName, dbTitleTextPointX, dbTitleTextPointY);
		
		setEventList(id, 'advisor_rec', advisorRecImagePointX, advisorRecImagePointY, 29, 26, dbName, dbMap[7]);
		
		let imgClo2 = new Image();
		
		imgClo2.addEventListener('load', function(){
			let ctxImg = ctx;
			
			ctxImg.drawImage(imgClo2, advisorRecImagePointX, advisorRecImagePointY, 29, 26);
		});
		
		imgClo2.src = "/resources/images/daily/advisor_recommendation.png";
		
		ctx.fillStyle = white;
		ctx.font = defaultDbFont;
		
		if(dbDelegateAlarm != "N" && dbDelegateAlarm != "U") {
			ctx.fillText(checkDt, checkDtTextPointX, checkDtTextPointY);
			
			ctx.fillStyle = white;
			ctx.fillRect(checkCountRectanglePointX - 2, dbTitleRectangleHeight, 78, 140);
			ctx.fillStyle = gray;
			ctx.fillRect(checkCountRectanglePointX, dbTitleRectangleHeight, 80, 140);
			ctx.font = bold18pxDbFont;
			ctx.fillStyle = white;
			ctx.textAlign = "center";
			ctx.font = bold48pxDbFont;
			ctx.fillText("+", checkCountTextPointX, checkPlusTextPointY);
			ctx.font = bold18pxDbFont;
			ctx.fillText(checkCount, checkCountTextPointX, checkCountTextPointY);
		} else if(dbDelegateAlarm == 'N'){
			ctx.fillText(checkDt, canvasRectangleWidth - 120, checkDtTextPointY );
		}
		
		// DB
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[8]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName1, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName1, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// INSTANCE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + dbStatusRectangleWidth;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[9]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName2, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName2, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// SPACE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 2);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[10]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName3, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName3, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// OBJECT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 3);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[11]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName4, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName4, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// STATISTICS
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 4);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbMap[7]);
		ctx.fillStyle = checkDelegateAlarm(dbMap[12]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName5, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName5, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// LONG RUNNING WORK
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 5);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[13]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName6, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		let x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		let y = dbStatusTextPointY - 15;
		let lineheight = 15;
		let lines = dbStatusName6.split('\n');
		
		for (let i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// ALERT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 6);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = checkDelegateAlarm(dbMap[14]);
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName7, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight, dbName);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName7, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		const delimiter = ";";
		let db_status_tabs_severity = dbMap[8] + delimiter + dbMap[9] + delimiter + dbMap[10] + delimiter + dbMap[11] +
				delimiter + dbMap[12] + delimiter + dbMap[13] + delimiter + dbMap[14];
		
		addEventListener(id, canvas, elemLeft, elemTop, dbMap[7], dbMap[15], dbMap[16], db_status_tabs_severity, dbDelegateAlarm);
	}
}

function checkDelegateAlarm(gradeCd) {
	let red = $('#severity_color_0').val();		// "#e21b30";
	let orange = $('#severity_color_1').val();	// "#fe852b";
	let blue = $('#severity_color_2').val();	// "#0070c0";
	let green = $('#severity_color_3').val();	// "#00b050";
	let gray = "#bfbfbf";
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

function checkDelegateAlarmImage(gradeCd) {
	const critical_image =		"/resources/images/daily/severity_e_0.png";
	const warning_image =		"/resources/images/daily/severity_e_1.png";
	const information_image =	"/resources/images/daily/severity_e_2.png";
	const normal_image =		"/resources/images/daily/severity_e_3.png";
	const unperformed_image =	"/resources/images/daily/severity_e_4.png";
	const helf_image =			"/resources/images/help.png";
	let image;
	
	switch(gradeCd) {
	case 'C':
		image = critical_image;
		break;
	case 'W':
		image = warning_image;
		break;
	case 'I':
		image = information_image;
		break;
	case 'N':
		image = normal_image;
		break;
	case 'U':
		image = unperformed_image;
		break;
	default:
		console.log("gradeCd[" + gradeCd + "]");
		image = helf_image;
		break;
	}
	
	return image;
}

function addEventListener(id, canvas, elemLeft, elemTop, dbid, check_day, check_seq, db_status_tabs_severity, dbDelegateAlarm) {
	canvas.addEventListener("click", function(event) {
		console.log({x:event.layerX, y: event.layerY});
		
//		let x = event.pageX - elemLeft,
//				y = event.pageY - elemTop;
		
		let x = event.layerX - 5;
		let y = event.layerY - 5;
		
		if(g_eventList.has(id)) {
			let _group = g_eventList.get(id);
			const _groupIter = _group.entries();
			
			for(let [key, value] of _groupIter) {
				var _element = value;
				
				if (y > _element.get('top') && y < _element.get('top') + _element.get('height') 
						&& x > _element.get('left') && x < _element.get('left') + _element.get('width')) {
					if(dbDelegateAlarm == 'U') {
						let message = '점검이 수행되지않았습니다. 관리자에게 문의바랍니다.';
						parent.parent.parent.$.messager.alert('',message);
						
						return;
					}
					
					if(_element.get('db_status_name') == 'advisor_rec') {
						createAdvisorRecommendation(dbid, $("#menu_id").val(), $("#menu_nm").val(), check_day, check_seq);
						
						return;
					}
					
					var tabTitle = 'DB 상태 점검 상세(' + _element.get('db_name')+ ')';
					
					createMinuteDbStatus($("#menu_id").val(), $("#menu_nm").val(), "dailyCheckDbTabs", dbid, check_day, check_seq, tabTitle,
							$('#severity_color_0').val(),  $('#severity_color_1').val(),  $('#severity_color_2').val(),  $('#severity_color_3').val(),
							_element.get('db_status_name'), db_status_tabs_severity);
				}
			}
		}
	}, false);
	
	canvas.addEventListener("mousemove", function(event) {
		let x = event.layerX - 5;
		let y = event.layerY - 5;
		let found = false;
		
		if(g_eventList.has(id)) {
			let _group = g_eventList.get(id);
			const _groupIter = _group.entries();
			
			for(let [key, value] of _groupIter) {
				var _element = value;
				
				if (y > _element.get('top') && y < _element.get('top') + _element.get('height') 
						&& x > _element.get('left') && x < _element.get('left') + _element.get('width')) {
					canvas.style.cursor = 'pointer';
					found = true;
					break;
				}
			}
		}
		
		if(!found) {
			canvas.style.cursor = 'default';
		}
	}, false);
}

function createAdvisorRecommendation(dbid, menu_id, menu_nm, check_day, check_seq) {
	var menuUrl = "/PreventiveCheck/DetailCheckInfo";
	var menuParam = "dbid="+dbid
					+"&strStartDt="+getDateFormat(check_day)
					+"&check_item_name=advisorRecommendation"
					+"&check_day="+check_day
					+"&check_seq="+check_seq;
	
	menuNm = menu_nm + ' - ADVISOR RECOMMENDATION';
		/* 탭 생성 */
	parent.parent.openLink("Y", $('#menu_id').val(), menuNm, menuUrl, menuParam);
}

function createMinuteDbStatus(menu_id, menu_nm, tab_id, dbid, check_day, check_seq, tabTitle,
		severity_color_0, severity_color_1, severity_color_2, severity_color_3, db_status_name, db_status_tabs_severity) {
	parent.parent.createMinuteDbStatusNewTab(menu_id, menu_nm, tab_id, dbid, check_day, check_seq, tabTitle,
			severity_color_0, severity_color_1, severity_color_2, severity_color_3, db_status_name, db_status_tabs_severity, $('#user_id').val());
}

function setEventList(id, db_status_name, left, top, width, height, dbName) {
	let _group;
	let _element = new Map();
	
	if(g_eventList.has(id)) {
		_group = g_eventList.get(id);
	} else {
		_group = new Map();
	}
	
	if(_group.has(db_status_name)) {
//		_group = _evnetList.get(groupId);
		throw new Error('이미 저장된 canvas ID[' + id + '], db_status_name[' + db_status_name + ']를 받았습니다.');
		try {
			node.addEventListener(event, listener, false);
		} catch(err) {
			console.error(err);
		}
	} else {
		_element = new Map();
	}
	
	_element.set('db_status_name', db_status_name);
	_element.set('left', left);
	_element.set('top', top);
	_element.set('width', width);
	_element.set('height', height);
	_element.set('db_name', dbName)
	
//	if(color) {
//		_element.set('color', color);
//	}
	
	_group.set(db_status_name, _element);
	
	g_eventList.set(id, _group);
}

function drawCanvas2Columns(id) {				// Use Test
	let canvas = document.getElementById(id);
	let red = "#d90015";
	let orange = "#fe852b";
	let blue = "#0070c0";
	let green = "#00b050";
	let white = "#FFF";
	let black = "#000";
	let gray = "#7f7f7f";
	let defaultDbFont = "12px Open Sans";
	let bold18pxDbFont = "Bold 18px Open Sans";
	let bold24pxDbFont = "Bold 24px Open Sans";
	let bold48pxDbFont = "Bold 48px Open Sans";
	
	let yPoint = 0;
	let canvasRectangleWidth = 550;
	let canvasRectangleHeight = 170;
	let paddingLeft = 20;
	let dbTitleRectangleHeight = 30;
	let dbStatusRectangleBasePointX = paddingLeft;
	let dbStatusRectanglePointY = yPoint + 110;
	let dbStatusRectanglePointX = 0;
	let dbStatusTextPointX = 50;
	let dbStatusTextPointY = yPoint + 145;
	let dbTitleTextPointY = yPoint + 20;
	let checkPrefTextPointX = 65;
	let checkPrefTextPointY = yPoint + 72;
	let checkDtTextPointY = yPoint + 100;
	let checkPlusTextPointY = yPoint + 70;
	let checkCountTextPointY = yPoint + 95;
	let checkCountRectanglePointX = canvasRectangleWidth - 60;
	let checkCountTextPointX = checkCountRectanglePointX + 31;
	let checkDtTextPointX = checkCountRectanglePointX - 120;
	let dbStatusRectangleThickess = 4;
	let dbStatusRectangleWidth = 76;
	let dbStatusRectangleHeight = 60;
	let dbStatusTextAddPointX = 38;
	let dbName = "OPENPOP: OPEN-POP";
	let checkPrefName = "Expired(grace) 계정 외";
	let checkDt = "2020-03-19 17:59";
	let checkCount = "9999";
	let dbStatusName1 = "DB";
	let dbStatusName2 = "INSTANCE";
	let dbStatusName3 = "SPACE";
	let dbStatusName4 = "OBJECT";
	let dbStatusName5 = "STATISTICS";
	let dbStatusName6 = "LONG\nRUNNING\nWORK";
	let dbStatusName7 = "ALERT";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		canvas.addEventListener("click", function(e) {
			console.log({x:e.layerX, y: e.layerY});
		});
		
		let ctx = canvas.getContext("2d");
		
		ctx.fillStyle = green;
		ctx.fillRect(0, yPoint, canvasRectangleWidth, canvasRectangleHeight);
		ctx.font = bold24pxDbFont;
		ctx.fillStyle = white;
		ctx.fillText(checkPrefName, checkPrefTextPointX, checkPrefTextPointY);
		
		let imgClo = new Image();
		
		imgClo.addEventListener('load', function(){
			let ctxImg = ctx;
			
//			ctxImg.drawImage(imgClo, checkPrefTextPointX - 42, checkPrefTextPointY - 25, 40, 35);
			ctxImg.drawImage(imgClo, checkPrefTextPointX - 42, checkPrefTextPointY - 25, 30, 30);
		});
		
		imgClo.src = "/resources/images/daily/severity_e_4.png";
		
		ctx.fillStyle = white;
		ctx.fillRect(paddingLeft, yPoint, canvasRectangleWidth, dbTitleRectangleHeight);
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = black;
		ctx.fillText(dbName, 35, dbTitleTextPointY);
		ctx.fillStyle = white;
		ctx.font = defaultDbFont;
		
		if(true) {
			ctx.fillText(checkDt, checkDtTextPointX, checkDtTextPointY);
			
			ctx.fillStyle = white;
			ctx.fillRect(checkCountRectanglePointX - 2, dbTitleRectangleHeight, 78, 140);
			ctx.fillStyle = gray;
			ctx.fillRect(checkCountRectanglePointX, dbTitleRectangleHeight, 80, 140);
			ctx.font = bold18pxDbFont;
			ctx.fillStyle = white;
			ctx.textAlign = "center";
			ctx.font = bold48pxDbFont;
			ctx.fillText("+", checkCountTextPointX, checkPlusTextPointY);
			ctx.font = bold18pxDbFont;
			ctx.fillText(checkCount, checkCountTextPointX, checkCountTextPointY);
		} else {
			ctx.fillText(checkDt, canvasRectangleWidth - 105, checkDtTextPointY);
		}
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = orange;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName1, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + dbStatusRectangleWidth;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName2, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 2);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = blue;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName3, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 3);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName4, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 4);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName5, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 5);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		let x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		let y = dbStatusTextPointY - 15;
		let lineheight = 15;
		let lines = dbStatusName6.split('\n');
		
		for (let i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 6);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName7, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
	}
}

function drawCanvas3Columns(id) {
	let canvas = document.getElementById(id);
	let red = "#d90015";
	let orange = "#fe852b";
	let blue = "#0070c0";
	let green = "#00b050";
	let white = "#FFF";
	let black = "#000";
	let gray = "#7f7f7f";
	let defaultDbFont = "11px Open Sans";
	let bold18pxDbFont = "Bold 18px Open Sans";
	let bold24pxDbFont = "Bold 24px Open Sans";
//	let bold48pxDbFont = "Bold 48px Open Sans";
	
	let yPoint = 0;
	let canvasRectangleWidth = 520;
	let canvasRectangleHeight = 170;
	let paddingLeft = 20;
	let dbTitleRectangleHeight = 30;
	let dbStatusRectangleBasePointX = paddingLeft;
//	let dbStatusRectanglePointY = yPoint + 108;
	let dbStatusRectanglePointY = yPoint + 110;
	let dbStatusRectanglePointX = 0;
	let dbStatusTextPointX = 50;
	let dbStatusTextPointY = yPoint + 145;
	let dbTitleTextPointY = yPoint + 20;
	let checkPrefTextPointX = 65;
	let checkPrefTextPointY = yPoint + 70;
	let checkDtTextPointY = yPoint + 100;
	let checkPlusTextPointY = yPoint + 70;
	let checkCountTextPointY = yPoint + 95;
	let checkCountRectanglePointX = canvasRectangleWidth - 60;
	let checkCountTextPointX = checkCountRectanglePointX + 31;
	let checkDtTextPointX = checkCountRectanglePointX - 120;
	let advisorRecImagePointX = canvasRectangleWidth - 29;
	let advisorRecImagePointY = dbTitleRectangleHeight - 30;
	let dbStatusRectangleThickess = 4;
//	let dbStatusRectangleWidth = 76;
	let dbStatusRectangleWidth = 72;
	let dbStatusRectangleHeight = 60;
	let dbStatusTextAddPointX = 38;
	let dbName = "OPENPOP: OPEN-POP";
	let checkPrefName = "Expired(grace) 계정 외";
	let checkPrefNameNormal = "진단결과 정상입니다.";
	let checkDt = "2020-03-19 17:59";
	let checkCount = "9999";
	let dbStatusName1 = "DB";
	let dbStatusName2 = "INSTANCE";
	let dbStatusName3 = "SPACE";
	let dbStatusName4 = "OBJECT";
	let dbStatusName5 = "STATISTICS";
	let dbStatusName6 = "LONG\nRUNNING\nWORK";
	let dbStatusName7 = "ALERT";
	
	if(canvas.getContext) {
		canvas.width=canvas.clientWidth;
		canvas.height=canvas.clientHeight;
		
		let elemLeft = canvas.offsetLeft;
		let elemTop = canvas.offsetTop;
		
		let ctx = canvas.getContext("2d");
		
		ctx.fillStyle = orange;
		ctx.fillRect(0, yPoint, canvasRectangleWidth, canvasRectangleHeight);
		ctx.font = bold24pxDbFont;
		ctx.fillStyle = white;
		
		if(true) {
			ctx.fillText(checkPrefName, checkPrefTextPointX, checkPrefTextPointY);
			
			let imgClo = new Image();
			
			imgClo.addEventListener('load', function(){
				var ctxImg = ctx;
				
				ctxImg.drawImage(imgClo, checkPrefTextPointX - 42, checkPrefTextPointY - 25, 40, 35);
			});
			
			imgClo.src = "/resources/images/daily/severity_e_2.png";
		} else {
			ctx.fillText(checkPrefNameNormal, checkPrefTextPointX, checkPrefTextPointY);
		}
		
		ctx.fillStyle = white;
		ctx.fillRect(paddingLeft, yPoint, canvasRectangleWidth, dbTitleRectangleHeight);
		ctx.font = bold18pxDbFont;
		ctx.fillStyle = black;
		ctx.fillText(dbName, 35, dbTitleTextPointY);
		
		setEventList(id, 'advisor_rec', advisorRecImagePointX, advisorRecImagePointY, 29, 30);
		
		let imgClo2 = new Image();
		
		imgClo2.addEventListener('load', function(){
			let ctxImg = ctx;
			
			ctxImg.drawImage(imgClo2, advisorRecImagePointX, advisorRecImagePointY, 29, 30);
		});
		
		imgClo2.src = "/resources/images/daily/advisor_recommendation.png";
		
		ctx.fillStyle = white;
		ctx.font = defaultDbFont;
		
		if(true) {
			ctx.fillText(checkDt, checkDtTextPointX, checkDtTextPointY);
			
			ctx.fillStyle = white;
			ctx.fillRect(checkCountRectanglePointX - 2, dbTitleRectangleHeight, 78, 140);
			ctx.fillStyle = gray;
			ctx.fillRect(checkCountRectanglePointX, dbTitleRectangleHeight, 80, 140);
			ctx.font = bold18pxDbFont;
			ctx.fillStyle = white;
			ctx.textAlign = "center";
			ctx.font = bold48pxDbFont;
			ctx.fillText("+", checkCountTextPointX, checkPlusTextPointY);
			ctx.font = bold18pxDbFont;
			ctx.fillText(checkCount, checkCountTextPointX, checkCountTextPointY);
		} else {
			ctx.fillText(checkDt, canvasRectangleWidth - 105, checkDtTextPointY);
		}
		
		// DB
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = orange;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName1, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName1, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// INSTANCE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + dbStatusRectangleWidth;
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName2, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName2, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// SPACE
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 2);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = blue;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName3, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName3, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// OBJECT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 3);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		setEventList(id, dbStatusName4, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName4, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// STATISTICS
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 4);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		setEventList(id, dbStatusName5, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName5, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		// LONG RUNNING WORK
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 5);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName6, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		let x = dbStatusRectanglePointX + dbStatusTextAddPointX;
		let y = dbStatusTextPointY - 15;
		let lineheight = 15;
		let lines = dbStatusName6.split('\n');
		
		for (let i = 0; i<lines.length; i++) {
			ctx.font = defaultDbFont;
			ctx.textAlign = "center";
			ctx.fillStyle = white;
			ctx.fillText(lines[i], x, y + (i*lineheight) );
		}
		
		// ALERT
		ctx.strokeStyle = white;
		ctx.lineWidth = dbStatusRectangleThickess;
		dbStatusRectanglePointX = dbStatusRectangleBasePointX + (dbStatusRectangleWidth * 6);
		ctx.strokeRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		ctx.fillStyle = green;
		ctx.fillRect(dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		setEventList(id, dbStatusName7, dbStatusRectanglePointX, dbStatusRectanglePointY, dbStatusRectangleWidth, dbStatusRectangleHeight);
		
		ctx.font = defaultDbFont;
		ctx.textAlign = "center";
		ctx.fillStyle = white;
		ctx.fillText(dbStatusName7, dbStatusRectanglePointX + dbStatusTextAddPointX, dbStatusTextPointY);
		
		addEventListener(id, canvas, elemLeft, elemTop);
	}
}

function fnOpenExceptionManageTab() {
	let menuId = "1181";
	let menuNm = "일 예방 점검 예외 관리";
	let menuUrl = "/ExceptionManagement/ExceptionManagement";
	let dbid = $('#dbid').val();
	let check_day = $('#check_day').val();
	let check_seq = $('#check_seq').val();
	
	if(dbid == undefined) {
		dbid = '';
	}
	
	if(check_day == undefined) {
		check_day = '';
	}
	
	if(check_seq == undefined) {
		check_seq = '';
	}
	
	var menuParam = "dbid="+dbid
					+"&strStartDt="+getDateFormat(String(check_day))
					+"&check_item_name=''"+
					+"&check_day="+check_day
					+"&check_seq="+check_seq;
	
	parent.parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}

/* DB 상태 점검 */
function dailyCheckDbSituation() {
	// DB 그룹 조회
	$('#perfDbGroupComboSituation').combobox({
		url:"/DailyCheckDb/dbGroupList?user_id="+ $('#user_id').val() +"&&isAll=Y",
		method:"get",
		valueField:'group_id',
		textField:'group_nm',
		onLoadSuccess: function(items) {
			if (items.length){
				let opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
			}
		}
	});
	
	$('#start_first_analysis_day').datebox('clear');
	$('#end_first_analysis_day').datebox('clear');
	
	resetBasePeriod(2);
	
	$('#base_weekly').radiobutton({
		onChange:function(val){
			if(val == true){
//				$("#base_period_value").val();
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
	
	$('#base_monthly').radiobutton({
		onChange:function(val){
			if(val == true){
				resetBasePeriod(Number($(this).val()));
			}
		}
	});
}

function resetBasePeriod(base_period_value) {
	$('#base_period_value').val(base_period_value);
	
	switch(base_period_value) {
	case 1:
		$('#base_weekly').radiobutton({
			checked: true
		});
		
		$('#base_monthly').radiobutton({
			checked: false
		});
		break;
	case 2:
		$('#base_weekly').radiobutton({
			checked: false
		});
		
		$('#base_monthly').radiobutton({
			checked: true
		});
		break;
	}
	
	ajaxCall("/DailyCheckDb/baseDate",
			$("#dailyCheckDb_form"),
			"POST",
			callback_BaseDateAction);
}

var callback_BaseDateAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.alert('','기준 일자를 조회하지 못하였습니다.');
		return false;
	}
	
	$('#start_first_analysis_day').datebox('setValue', data[0].start_first_analysis_day);
	$('#end_first_analysis_day').datebox('setValue', data[0].end_first_analysis_day);
	
	Btn_OnClickSituation();
}

function Btn_OnClickSituation() {
	if(!compareAnBDate($('#start_first_analysis_day').datebox('getValue'), $('#end_first_analysis_day').datebox('getValue'))) {
		let msg = "기준일자를 확인해 주세요.<br>시작일자[" + $('#start_first_analysis_day').datebox('getValue') + "] 종료일자[" + $('#end_first_analysis_day').datebox('getValue') + "]";
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	ajaxCall("/DailyCheckDb/compareBaseDate",
			$("#dailyCheckDb_form"),
			"POST",
			callback_compareBaseDateAction);
}

var callback_compareBaseDateAction = function(result) {
	var data = JSON.parse(result);
	
	if(data.length <= 0) {
		$.messager.error('','기준일자를 비교하지 못하였습니다.');
		return false;
	}
	
	let isLargerThanBeginDate = data[0].isLargerThanBeginDate;
	
	if(isLargerThanBeginDate != 0) {
		let msg = '기준일자를 1개월 이내로 선택해 주세요.';
		parent.$.messager.alert('경고',msg,'warning');
		return false;
	}
	
	$('#start_first_analysis_day_bottom').val($('#start_first_analysis_day').datebox('getValue'));
	$('#end_first_analysis_day_bottom').val($('#end_first_analysis_day').datebox('getValue'));
	$('#choice_db_group_id').val($('#perfDbGroupComboSituation').combobox('getValue'));
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("DB 상태 점검 현황");
	
	ajaxCall("/DailyCheckDb/dailyCheckDbSituationTop",
			$("#dailyCheckDb_form"),
			"POST",
			callback_dailyCheckDbSituationTopAction);
}

var callback_dailyCheckDbSituationTopAction = function(result) {
	try {
		let data = JSON.parse(result);
		let rows = data.rows;
		let rowsLen = rows.length;
		
		if(rowsLen > 0) {
			let headRow = rows.splice(rowsLen - 1, 1);
			let head = headRow[0].HEAD;
			
			headArray = head.split('\;');
		}
		
		let pureData = rows;
		
		getByGridTop(headArray, false);
		
		data.rows = changeValueTop(pureData);
		
		$('#tableList1').datagrid("loadData", []);
		
		$('#bottom').panel('setTitle', '');
		
		$('#tableList2').datagrid('loadData', {"total":0,"rows":[]});
		
		json_string_callback_common(JSON.stringify(data), '#tableList1', true);
	} catch(err) {
		if(err.message.indexOf('Unexpected') == 0) {
			parent.$.messager.alert('검색 에러',result.substring(result.indexOf('message') + 10, result.length - 2),'error');
		} else {
			parent.$.messager.alert('검색 에러',err.message,'error');
		}
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function changeValueTop(pureData) {
	for(var index = 0; index < pureData.length; index++) {
		var row = pureData[index];
		
		for(var key in row) {
			var value = row[key];
			
			if(key !== 'db_full_name' && key.indexOf('check_day') !== 0 && key.indexOf('dbid') !== 0) {
				row[key] = getStatusImg(value);
			} else {
				row[key] = value;
			}
		}
		
		pureData[index] = row;
	}
	
	return pureData
}

function getByGridTop(headArray, isError){
	var jsonArray = new Array();
	var jsonMap = new Object();
	var jsonArrayColumnGroup = new Array();
	var jsonMap2 = new Object();
	var jsonMap3 = new Object();
	
	if(isError) {
		jsonMap = new Object();  //맵객체를 새로만듬.
		jsonMap.field='error';
		jsonMap.title='에러';
		jsonMap.width='100%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	} else {
		if(headArray.length == 0) {
			jsonMap = new Object();  //맵객체를 새로만듬.
			//jsonMap.field='coulmn_'+i;
			jsonMap.field='info';
			jsonMap.title='정보';
			jsonMap.width='100%';
			jsonMap.halign='center';
			jsonMap.align='center';
			jsonArray.push(jsonMap);
		}
		
		for(var i = 0; i < headArray.length; i++){
			var head = headArray[i];
			var options = head.split('\:');
			
			if(i == 1) {
				_second_field = options[0];
			}
			
			jsonMap = new Object();  //맵객체를 새로만듬.
			jsonMap.field = options[0];
			jsonMap.title = options[1];
			if(options[2] != 0) {
				jsonMap.width = options[2];	// controller단의 정보가 아닌 웹에서 재정의한다.
			} else {
				jsonMap.width = '64px';		// width가 정의되어 있지 않으면 성능에 문제가 발생한다.
			}
			jsonMap.halign = 'center';
			jsonMap.align = setAlign(options[3]);
			jsonMap.hidden = setHidden(options[4]);
			
			if(i > 0) {
				jsonMap.formatter = getStatusImg();
			}
			
			jsonArray.push(jsonMap);
		}
	}
	
	$('#tableList1').datagrid({
		view: emptyview,
		nowrap : true,
		fitColumns:false,
		autoRowHeight:false,
		striped:true,
		singleSelect:true,
		rownumbers:false,
		resizable:false,
		columns:[jsonArray],
		onClickRow:function(index,row) {
			$('#dbid_situation').val(row.dbid);
			$('#bottom').panel('setTitle', row.db_full_name);
			
			dailyCheckDbSituationBottom();
		},
		onSelect:function(index,row) {
			$('#dbid_situation').val(row.dbid);
			$('#bottom').panel('setTitle', row.db_full_name);
			
			dailyCheckDbSituationBottom();
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data) {
			var opts = $(this).datagrid('options');
			if(data.rows.length > 0){
				dataRow = data.rows[0];
				
				$(this).datagrid('selectRow', 0);
			}
		}
	});
}

function setAlign(typeName) {
	var align = '';
	
	if(typeName.toUpperCase() === 'NUMBER') {
		align = 'right';
	} else if(typeName.toUpperCase() === 'VARCHAR' || typeName.toUpperCase() === 'VARCHAR2' ||
			typeName.toUpperCase() === 'CLOB') {
		align = 'left';
	} else {
		align = 'center';
	}
	
	return align;
}

function setHidden(hidden) {
	if(typeof hidden != 'undefined') {
		return eval(hidden);
	}
	
	return eval('false');
}

function getStatusImg(val) {
	if(Number(val) >= 0 && Number(val) <= 4) {		// 정상
		return "<img src='/resources/images/daily/severity_s_" + val + ".png' style='vertical-align:middle;'/>";
	}
}

function dailyCheckDbSituationBottom() {
	$('#tableList2').datagrid("loadData", []);
	$('#tableList2').datagrid('options').loadMsg = '데이터를 불러오는 중입니다.';
	$('#tableList2').datagrid('loading');
	
	ajaxCall("/DailyCheckDb/dailyCheckDbSituationBottom",
			$("#dailyCheckDb_form"),
			"POST",
			callback_dailyCheckDbSituationBottomAction);
}

var callback_dailyCheckDbSituationBottomAction = function(result) {
	try {
		let data = JSON.parse(result);
		let rows = data.rows;
		let rowsLen = rows.length;
		
		if(rowsLen > 0) {
			let headRow = rows.splice(rowsLen - 1, 1);
			let head = headRow[0].HEAD;
			
			headArray = head.split('\;');
		}
		
		let pureData = rows;
		
		getByGridBottom(headArray, false);
		
		data.rows = changeValueBottom(pureData);
		
		json_string_callback_common(JSON.stringify(data), '#tableList2', false);
	} catch(err) {
		if(err.message.indexOf('Unexpected') == 0) {
			parent.$.messager.alert('검색 에러',result.substring(result.indexOf('message') + 10, result.length - 2),'error');
		} else {
			parent.$.messager.alert('검색 에러',err.message,'error');
		}
	}
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
}

function getByGridBottom(headArray, isError){
	var jsonArray = new Array();
	var jsonMap = new Object();
	var jsonArrayColumnGroup = new Array();
	var jsonMap2 = new Object();
	var jsonMap3 = new Object();
	
	if(isError) {
		jsonMap = new Object();  //맵객체를 새로만듬.
		jsonMap.field='error';
		jsonMap.title='에러';
		jsonMap.width='100%';
		jsonMap.halign='center';
		jsonMap.align='center';
		jsonArray.push(jsonMap);
	} else {
		if(headArray.length == 0) {
			jsonMap = new Object();  //맵객체를 새로만듬.
			//jsonMap.field='coulmn_'+i;
			jsonMap.field='info';
			jsonMap.title='정보';
			jsonMap.width='100%';
			jsonMap.halign='center';
			jsonMap.align='center';
			jsonArray.push(jsonMap);
		}
		
		for(var i = 0; i < headArray.length; i++){
			var head = headArray[i];
			var options = head.split('\:');
			
			if(i == 1) {
				_second_field = options[0];
			}
			
			jsonMap = new Object();  //맵객체를 새로만듬.
			jsonMap.field = options[0];
			jsonMap.title = options[1];
			if(options[2] != 0) {
				jsonMap.width = options[2];	// controller단의 정보가 아닌 웹에서 재정의한다.
			} else {
				jsonMap.width = '64px';		// width가 정의되어 있지 않으면 성능에 문제가 발생한다.
			}
			jsonMap.halign = 'center';
			jsonMap.align = setAlign(options[3]);
			jsonMap.hidden = setHidden(options[4]);
			
			if(i > 0) {
				jsonMap.formatter = getStatusImg();
			}
			
			jsonArray.push(jsonMap);
		}
	}
	
	$('#tableList2').datagrid({
		view: emptyview,
		nowrap : false,
		fitColumns:false,
		autoRowHeight:false,
		striped:true,
		singleSelect:false,
		rownumbers:false,
		resizable:false,
		columns:[jsonArray],
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		},
		onLoadSuccess:function(data) {
		},
		onSelect: function(index, row) {
			$(this).datagrid('unselectRow', index);
		}
	});
}

function changeValueBottom(pureData) {
	for(var index = 0; index < pureData.length; index++) {
		var row = pureData[index];
		
		for(var key in row) {
			var value = row[key];
			
			if(key !== 'check_class_div_nm' && key.indexOf('check_day') !== 0 && key.indexOf('dbid') !== 0 && key.indexOf('check_class_div_cd') !== 0) {
				row[key] = getStatusImg(value);
			} else {
				row[key] = value;
			}
		}
		
		pureData[index] = row;
	}
	
	return pureData
}