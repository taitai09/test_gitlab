var day = new Date(); 
var nowDate = formatDate(day,"Ymd");
var shortMonths = new Array('Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec');
var longMonths = new Array('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December');
var shortDays = new Array('SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT');
var shortHanDays = new Array('일', '월', '화', '수', '목', '금', '토');
var longDays = new Array('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday');

// ← Backspace키의 키코드는 8, F5키의 키코드는 116이다.
window.onkeydown = function() {
	var kcode = event.keyCode;
	if(kcode == 116) event.returnValue = false;
}
// 마우스 오른쪽 버튼을 막는 소스(ie에서 동작 안함)
//document.addEventListener('contextmenu', event => event.preventDefault());
// 마우스 오른쪽 버튼을 막는 소스(ie에서 동작)
document.addEventListener('contextmenu', function(event) { event.preventDefault(); }, true); 

function saveLogin(strId){
	var expdate = new Date();
	if(strId != ""){
		expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30);		
		setCookie("MYID",strId,expdate);
	}else{
		deleteCookie("MYID");
	}
}	

function logout() {
	top.location.href="/auth/logout";
}

function goPage(url, auth) {
	if(auth == "in"){
		location.href = url;
	}
}

function goPopupPage(url, auth) {
	if(auth == "in"){
		openPopup(url,1600,1200);
	}
}

//모달리스 팝업을 뛰운다.
function openPopup(url, popupwidth, popupheight)
{		
	var Top = (window.screen.height - popupheight) / 2;
	var Left = (window.screen.width - popupwidth) / 2; 
	if (Top < 0) Top = 0;
	if (Left < 0) Left = 0;
	var Feature = "fullscreen=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,left=" + Left + ",top=" + Top + ",width=" + popupwidth + ",height=" + popupheight;
	
	window.open(url , popupwidth , Feature);
}

//모달리스 팝업을 뛰운다.
function openPopupDtl(url, popupwidth, popupheight, Top, Left)
{		
	var Feature = "fullscreen=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,left=" + Left + ",top=" + Top + ",width=" + popupwidth + ",height=" + popupheight;
	
	window.open(url , popupwidth , Feature);
}

//------------------------------------------------------------------------------
//문자열의끝에서 주어진 길이만큼 분리합니다.
//@param str  원본 문자열
//@param length 길이
//------------------------------------------------------------------------------
strRight = function(str, length){
 return (str.length >= length) ? str.substring(0, str.length - length) : str;
};

//------------------------------------------------------------------------------
//문자열의 시작부분을 잘라냅니다. 
//
//@param str 원본문자열 
//@param length 길이    
//------------------------------------------------------------------------------
strLeft = function(str, length){
 return str.length >= length ? str.substring(0, length) : str;
};

/*  
 * 쿠키 값을 얻습니다.  
 * getCookie(쿠키명); 형식으로 사용합니다.  
*/  
function getCookie(name) {   
  var value=null, search=name+"=";   
  if (document.cookie.length > 0) {   
    var offset = document.cookie.indexOf(search);   
    if (offset != -1) {   
      offset += search.length;   
      var end = document.cookie.indexOf(";", offset);   
      if (end == -1) end = document.cookie.length;   
      value = unescape(document.cookie.substring(offset, end));   
    }   
  }   
  return value;   
}   
  
/*  
 * 쿠키를 삭제합니다.  
 * deleteCookie(쿠키명); 형식으로 사용합니다.  
*/  
function deleteCookie(name) {
	if(getCookie(name)) {
		document.cookie = name + '=' + '; path=/; expires=' + new Date(0).toGMTString();
		return true;
		
	}else {
		return false;
	}
}
/*  
 * 쿠키를 생성합니다.  
 * setCookie(쿠키명,값[,시간[,경로]]); 형식으로 사용합니다.  
*/  
function setCookie(name,value,expire) {   
  document.cookie = name + '=' + escape(value) + ((!expire) ? '':('; expires=' + expire.toUTCString())) + '; path=/;';   
}   

//------------------------------------------------------------------------------
//, 를 제거한다.
//------------------------------------------------------------------------------
trimComma = function(obj){
	if (obj){
 	if (obj.length){
	    	for (idx=0; idx < obj.length ; idx++){
	    		obj[idx].value = strReplace(obj[idx].value, ",", "");
	    	}
 	}else{
 		obj.value = strReplace(obj.value, ",", "");
 	}
	}
	
	$(obj).select();
};

strReplace = function(str, findStr, replaceStr){
    if (!str) 
        return str;
    return str.replace(new RegExp(findStr, "g"), replaceStr);
};


strExpReplace = function(str){
	var regExp = /[\{\}\[\]\/?.,;|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi
    
	return str.replace(regExp, "");
};
/*
 * _ 언더바는 뺌
 */
strExpReplace2 = function(str){
	var regExp = /[\{\}\[\]\/?.,;|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi
	return str.replace(regExp, "");
};
/*
 * _ #${}는 뺌
 */
strExpReplace3 = function(str){
	var regExp = /[\[\]\/?.,;|\)*~`!^\-+<>@%&\\\=\(\'\"]/gi
	return str.replace(regExp, "");
};
//------------------------------------------------------------------------------
//, 를 넣어 준다.
//------------------------------------------------------------------------------
setComma = function(obj){
	for (idx=0; idx < obj.length ; idx++){
		obj[idx].value = formatComma(strReplace(obj[idx].value,",",""));
	}
};

chkComma = function(obj){
	obj.value = formatComma(strReplace(obj.value,",",""));
}

//------------------------------------------------------------------------------ 
//입력된 숫자형식의 문자열에 콤마를 넣습니다.
//@param str 숫자형식의 문자열 
//@return 
//	콤마가 삽입된 문자열 
//------------------------------------------------------------------------------
formatComma = function(str){
	 str += '';
	 x = str.split('.');
	 x1 = x[0];
	 if(x1 == '') x1 = "0";
	 x2 = x.length > 1 ? '.' + x[1] : '';
	 var rgx = /(\d+)(\d{3})/;
	 while (rgx.test(x1)) {
	     x1 = x1.replace(rgx, '$1' + ',' + '$2');
	 }
 
 return x1 + x2;
};

//숫자만 입력 허용
function onlyNum(event) {
	
	event = event || window.event;

	if(((event.keyCode != 8 && event.keyCode != 9 && event.keyCode != 13 && event.keyCode != 37 && event.keyCode != 39 && event.keyCode != 46 && event.keyCode != 45) && event.keyCode < 48)
	|| (event.keyCode > 57))
	{
		event.target.value = event.target.value.replace(/[^0-9\-\.]/g, "");
	}
}

//textbox 숫자만 입력 허용
function onlyNumChk(strValue) {
	regNumber = /[^0-9\-\.]/g;
	
	if(!regNumber.test(strValue)) {	    
	    return true;
	}else{
		return false;
	}
}

function replaceHtmlTag(strString){
	var returnString = strString;

	returnString = strReplace(returnString,"&","&amp;");
	returnString = strReplace(returnString,"<","&lt;");
	returnString = strReplace(returnString,">","&gt;");
	returnString = strReplace(returnString,"'","&apos;");
	returnString = strReplace(returnString,"\"","&quot;");
	
	return returnString;
}

function replaceHtmlTagToNormal(strString){
	var returnString = strString;

	returnString = strReplace(returnString,"&amp;","&");
	returnString = strReplace(returnString,"&lt;","<");
	returnString = strReplace(returnString,"&gt;",">");
	returnString = strReplace(returnString,"&apos;","'");
	returnString = strReplace(returnString,"&quot;","\"");
	
	return returnString;
}

//이전 날짜들은 선택막기 
function noBefore(date){
	var day = new Date();
	day.setDate(day.getDate()-1);
	
	if (date < day) 
		return [false]; 
	return [true]; 
} 

function formatDate(date, format) {
	var returnStr = '';
	
	for (var i = 0; i < format.length; i++) {
		var curChar = format.charAt(i);
		var res = replaceChars(curChar, date);	

		if(res != ''){
			returnStr += res;
		}else{
			returnStr += curChar;
		}
	}
	
	return returnStr;	
}

//날짜 졍규식 형식 : 2019-04-22 -> true
function checkDate(date){
	var str = date;
	var pattern = /^(19|20|21|99)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/; 

	if(pattern.test(str)) {
	    return true;
	} else {
		return false;
	}
}

function formatDateString(dateStr, format) {
	var returnStr = '';
	var newDate = new Date(dateStr.substr(0,4),dateStr.substr(4,2)-1,dateStr.substr(6,2));
	
	for (var i = 0; i < format.length; i++) {
		var curChar = format.charAt(i);
		var res = replaceChars(curChar, newDate);	

		if(res != ''){
			returnStr += res;
		}else{
			returnStr += curChar;
		}
	}
	
	return returnStr;	
}

// 날짜와 시간까지 Date로 생성
function getNewDate(strDate){
	var newDate = new Date(strDate.substr(0,4),strDate.substr(4,2)-1,strDate.substr(6,2), strDate.substr(8,2), strDate.substr(10,2), strDate.substr(12,2));
	return newDate;
}

function replaceChars(str, date){
	var returnStr = '';
	
	switch (str) {
		case 'd' : returnStr = (date.getDate() < 10 ? '0' : '') + date.getDate();break;
		case 'D' : returnStr = shortDays[date.getDay()];break;
		case 'G' : returnStr = shortHanDays[date.getDay()];break;
		case 'L' : returnStr = longDays[date.getDay()];break;
		case 'm' : returnStr = (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1);break;
		case 'M' : returnStr = shortMonths[date.getMonth()];break;
		case 'F' : returnStr = longMonths[date.getMonth()];break;
		case 'y' : returnStr = ('' + date.getFullYear()).substr(2);break;
		case 'Y' : returnStr = date.getFullYear();break;
		case 'a' : returnStr = date.getHours() < 12 ? 'am' : 'pm';break;
		case 'A' : returnStr = date.getHours() < 12 ? 'AM' : 'PM';break;
		case 'h' : returnStr = ((date.getHours() % 12 || 12) < 10 ? '0' : '') + (date.getHours() % 12 || 12);break;
		case 'H' : returnStr = (date.getHours() < 10 ? '0' : '') + date.getHours();break;
		case 'i' : returnStr = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();break;
		case 's' : returnStr = (date.getSeconds() < 10 ? '0' : '') + date.getSeconds();break;
	}

	return returnStr;
}

function makeZero(s) {
	if ((""+s).length < 2) {
		s = "0" + s;
	}
	return s;
}

function formatReserDate(strDate,token) {
	var yf = "-";
	var ym = "-";
	var yd = "";
	
	if (strDate.length > 7) {
		if (token == "han") {
			yf = "년";
			ym = "월";
			yd = "일";		
		}
		
		strDate = strDate.split("-").join("").split(" ").join("").split(".").join("");
		strDate = strDate.substr(0,4) + yf + strDate.substr(4,2) + ym + strDate.substr(6,2) + yd;
	}
	
	return strDate;
}

function formatReserTime(strTime,token) {
	var yh = ":";
	var hm = "";
	if (strTime.length > 3) {
		if (token == "han") {
			yh = "시";
			hm = "분";			
		}
		strTime = strTime.split("-").join("").split(" ").join("").split(":").join("");
		strTime = strTime.substr(0,2) + yh + strTime.substr(2,2) + hm;
	}
	
	return strTime;
}



//아이디 특수문자 한글 체크
function checkIdSpecial(Objectname) {
	 var intErr
	 var strValue = Objectname
	 var retCode = 0
	 var re = /[~!@\#$%<>^&*\()\-=+_\']/gi; //특수문자 정규식 변수 선언
	 var go = /[\s]/g;  //공백체크
	 
	 for (i = 0; i < strValue.length; i++) {
	  var retCode = strValue.charCodeAt(i)
	  var retChar = strValue.substr(i,1).toUpperCase()
	  retCode = parseInt(retCode)
	  
	  //입력받은 값중에 한글이 있으면 에러
	  if ( (retChar < "0" || retChar > "9") && (retChar < "A" || retChar > "Z") && ((retCode > 255) || (retCode < 0)) ) {
	   intErr = -1;
	   break;
	  //입력받은 값중에 특수문자가 있으면 에러
	  } else if(re.test(strValue)) {
	   intErr = -1;
	   break;
	  } else if(go.test(strValue)){
		  intErr = -1;
		  break;
	  }
	 }
	 return (intErr);
}

function checkHangle(word){
	check = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
	if(check.test(word))
		return false;
	else
		return true;
}
		
// 아이디 유효성 체크
function checkId(word) {
	var alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	var number = "1234567890";
	var alphaCheck = false;
	var numberCheck = false;
	
	if(6 <= word.length || word.length <= 16){
		for(var i=0; i < word.length; i++){
			if(alpha.indexOf(word.charAt(i)) != -1){
				alphaCheck = true;
			}
			if(number.indexOf(word.charAt(i)) != -1){
				numberCheck = true;
			}
		}//for
		
		if(alphaCheck != true || numberCheck != true){
			return false;
		}//if	
	}else{
		return false;
	}
	return true;
}

//이메일 유효성 체크
function checkEmail(strEmail) {   
	var regex=/([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
	
	return (strEmail != '' && strEmail != 'undefined' && regex.test(strEmail));
}

//비밀번호 유효성 체크
function checkPwd(upw) {
	var alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	var number = "1234567890";
	var sChar = "-_=+\|()*&^%$#@!~`?></;,.:'";
	
	var sChar_Count = 0;
	var alphaCheck = false;
	var numberCheck = false;
	
	if(6 <= upw.length || upw.length <= 16){
		for(var i=0; i < upw.length; i++){
			if(sChar.indexOf(upw.charAt(i)) != -1){
				sChar_Count++;
			}
			if(alpha.indexOf(upw.charAt(i)) != -1){
				alphaCheck = true;
			}
			if(number.indexOf(upw.charAt(i)) != -1){
				numberCheck = true;
			}
		}//for
		
		if(sChar_Count < 1 || alphaCheck != true || numberCheck != true){
			return false;
		}//if	
	}else{
		return false;
	}
	return true;
}

//전화번호 유효성 체크
function checkTelno(strTelno)
{
	var tempTelno = strReplace(strTelno, "-", "");
	
	if(tempTelno.length == 8){
		return true;
	}else{
		var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

		if(regExp.test(strTelno) == false)
			return false;
		else
			return true;		
	}
}

//input Enter막기
function captureReturnKey(e) {
    if(e.keyCode==13 && e.srcElement.type != 'textarea')
    return false;
}

//두 날짜간의 날수를 계산(형식 : 20110301)
function getDays(sDate,eDate) {  
    var date1 = new Date(sDate.substr(0,4),sDate.substr(4,2),sDate.substr(6,2));
    var date2 = new Date(eDate.substr(0,4),eDate.substr(4,2),eDate.substr(6,2));

    var interval = date2 - date1;
    var day = 1000*60*60*24;
    
    return parseInt(interval/day);
}

//날짜차이
function diffDayMonth(pStartDate,pEndDate,pType){
	//param : pStartDate - 시작일 YYYYMMDD
	//param : pEndDate  - 마지막일 YYYYMMDD
	//param : pType       - 'D':일수, 'M':개월수
	var strSDT = new Date(pStartDate.substring(0,4),pStartDate.substring(4,6)-1,pStartDate.substring(6,8));
	var strEDT = new Date(pEndDate.substring(0,4),pEndDate.substring(4,6)-1,pEndDate.substring(6,8));
	var strGapDT = 0;
	 
	if(pType == 'D') {  //일수 차이
	    strGapDT = (strEDT.getTime()-strSDT.getTime())/(1000*60*60*24);
	} else {            //개월수 차이
	    //년도가 같으면 단순히 월을 마이너스 한다.
	    // => 20090301-20090201 의 경우 아래 else의 로직으로는 정상적인 1이 리턴되지 않는다.
	    if(pEndDate.substring(0,4) == pStartDate.substring(0,4)) {
	        strGapDT = pEndDate.substring(4,6) * 1 - pStartDate.substring(4,6) * 1;
	    } else {
	        strGapDT = Math.floor((strEDT.getTime()-strSDT.getTime())/(1000*60*60*24*365.25/12));
	    }
	}
	return strGapDT;
}

// 둘날짜의 초차이
function diffDaySecond(pStartDate, pStartTime, pEndDate, pEndTime){
	//param : pStartDate - 시작일 YYYYMMDD
	//param : pStartTime - 시작시간 HHMMSS
	//param : pEndDate  - 마지막일 YYYYMMDD
	//param : pEndTime  - 마지막시간 HHMMSS 

	var strSDT = new Date(pStartDate.substr(0,4),pStartDate.substr(4,2)-1,pStartDate.substr(6,2), pStartTime.substr(0,2), pStartTime.substr(2,2), pStartTime.substr(4,2));
	var strEDT = new Date(pEndDate.substr(0,4),pEndDate.substr(4,2)-1,pEndDate.substr(6,2), pEndTime.substr(0,2), pEndTime.substr(2,2), pEndTime.substr(4,2));
	var strGapDT = 0;

	strGapDT = (strEDT.getTime() - strSDT.getTime()) / 1000;
	
	return strGapDT;
}

// 초(Second) 더하기
/*
 * P : 더하기, M : 빼기
 * 기준날짜
 * 기준시간
 * 더하는초 
 */
function dateAddSecond(addGubun, nowDate, nowTime, addSecond){
	var addDate = new Date(nowDate.substr(0,4),nowDate.substr(4,2)-1,nowDate.substr(6,2), nowTime.substr(0,2), nowTime.substr(2,2), nowTime.substr(4,2));	
	
	if(addGubun == "P"){
		addDate.setSeconds(addDate.getSeconds() + addSecond);
	}else{
		addDate.setSeconds(addDate.getSeconds() + (-1)*addSecond);
	}
	
	return formatDate(addDate,"Y-m-d H:i:s")
}

// 시작날짜와 종료날짜 비교
function compareAnBDate(startDate, endDate) {
	var startDateArr = startDate.split('-');
	var endDateArr = endDate.split('-');
	
	var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1]) - 1, startDateArr[2]);
	var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1]) - 1, endDateArr[2]);
	
	if(startDateCompare.getTime() > endDateCompare.getTime()) {
		console.warn("시작일과 종료일을 확인해 주세요. start[" + startDate + "] end[" + endDate + "]");
		
		return false;
	}
	
	return true;
}

/**
 * 이전 날짜 계산 함수
 * @param {Object} year : 기준 년도
 * @param {Object} month : 기준 달
 * @param {Object} date : 기준 일
 * @param {Object} type : 계산 날짜 구분 (D = 날짜 계산 / M = 달 계산 / Y = 년 계산)
 * @param {Object} calDays : 계산할 날 수
 * return {String} 날짜 : yyyy-MM-dd 형식
 */ 
function dateCalculator(year, month, date, type, calDays){
	let resultDate = '';
	
	year = parseInt(year);
	month = parseInt(month);
	date = parseInt(date);
	
	if(type == 'D') {
		resultDate = new Date(year, month - 1, date + calDays);
		
	}else if (type == 'M') {
		resultDate = new Date(year, month - 1, date + (calDays * 31));
		
	}else if (type == 'Y') {
		resultDate = new Date(year + calDays, month - 1, date);
	}
	
	year = resultDate.getFullYear();
	month = resultDate.getMonth() + 1;
	month = (month < 10) ? '0' + month : month;
	date = resultDate.getDate();
	date = (date < 10) ? '0' + date : date;
	
	return '' + year + '-' +  month  + '-' + date;
}

// 시간 정규식 패턴
function validateHH24MMSS(inputField) {
	var isValid = /^([0-1]?[0-9]|2[0-3]):([0-5][0-9])(:[0-5][0-9])?$/.test(inputField);
	
	return isValid;
}

//시작날짜시간과 종료날짜시간 비교
function compareAnBDatatime(startDate, startTime, endDate, endTime) {
	var startDateArr = startDate.split('-');
	var endDateArr = endDate.split('-');
	var startTimeArr = startTime.split(':');
	var endTimeArr = endTime.split(':');
	
	var timeLen = startTimeArr.length;
	
	var startDatetime = null;
	var endDatetime = null;
	
	if(timeLen < 3) {
		startDatetime = new Date(parseInt(startDateArr[0], 10), parseInt(startDateArr[1], 10) -1, parseInt(startDateArr[2], 10),
				parseInt(startTimeArr[0], 10), parseInt(startTimeArr[1], 10) );
		endDatetime = new Date(parseInt(endDateArr[0], 10), parseInt(endDateArr[1], 10) -1, parseInt(endDateArr[2], 10),
				parseInt(endTimeArr[0], 10), parseInt(endTimeArr[1], 10) );
	} else {
		startDatetime = new Date(parseInt(startDateArr[0], 10), parseInt(startDateArr[1], 10) -1, parseInt(startDateArr[2], 10),
				parseInt(startTimeArr[0], 10), parseInt(startTimeArr[1], 10), parseInt(startTimeArr[2], 10) );
		endDatetime = new Date(parseInt(endDateArr[0], 10), parseInt(endDateArr[1], 10) -1, parseInt(endDateArr[2], 10),
				parseInt(endTimeArr[0], 10), parseInt(endTimeArr[1], 10), parseInt(endTimeArr[2], 10) );
	}
	
	// 두 일자 간의 차이 계산 - 분 단위로 변경
	var diffSec = (endDatetime.getTime() - startDatetime.getTime() ) / 1000;
	
	console.log("diffSec:" + diffSec + "\n startDate:" + startDate + " startTime:" + startTime + " endDate:" + endDate + " endTime:" + endTime);
	
	return diffSec;
}

function setEmail(target, objID){
	var selVal = $("#"+objID+" option:selected").val();
	
	if(selVal == ""){
		$("#"+target).attr("readonly", false);
		$("#"+target).val("");
		$("#"+target).focus();
	}else{
		$("#"+target).attr("readonly", true);
		$("#"+target).val(selVal);
	}
}

/** 타이머 **/
function Timer(in_secs, in_mins, in_hours, in_days) {
	day = in_days;
	hour = in_hours;
	min = in_mins;
	sec = in_secs;

	checkTimer();
}

function checkTimer() {
	sec = sec - 1;
	
	if(sec == -1){
		sec = 59;
		min = min - 1;
	}
	
	if(min == -1){
		min = 59;
		hour = hour - 1;
	}
	
	if(hour == -1){
		hour = 23;
		day = day - 1;	
	}
	
	if(sec == 0 && min == 0 && hour == 0 && day == 0){
		$("#timeGb").val("N");
		$("#timerTxt").html("시간종료");	
		return;
	}
	
	$("#timerTxt").html(min + "분" + sec + "초");	
	
	window.setTimeout("checkTimer()",1000);
}

// checkbox 전제 클릭시 해당 name 모두체크 및 해제 function
function checkBoxAllChecked(allChk, frm, chkName) {
	if($("#"+allChk).prop("checked")){
		$("#"+frm+" input[name="+chkName+"]").prop("checked", true);
	} else {
		$("#"+frm+" input[name="+chkName+"]").prop("checked", false);
	}
}

function myformatter(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}

function myparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

// 문자열 숫자에 대한 정렬
function sorterInt(a,b) {
	return parseInt(a) > parseInt(b) ? 1 : -1;
}

// 인자값 null이 존재하는 date 또는 datetime 정보에 대한 정렬
function sorterDatetime(a,b) {
	if(a == null) {
		return -1;
	}
	if(b == null) {
		return 1;
	}
	
	let dateA = new Date(a).getTime();
	let dateB = new Date(b).getTime();
	
	return dateA > dateB ? 1 : -1;
}

// 문자열에 대한 정렬
function sorterString(a,b) {
	if(a == null) {
		return -1;
	}
	if(b == null) {
		return 1;
	}
	
	if(a > b) return -1;
	if(b > a) return 1;
	
	return 0;
}

//날짜형식 변경
function getDateFormat(val, row) {
    var newDate = "";
    if(val != "" && val != null){
    	newDate = val.substr(0,4) + "-" + val.substr(4,2) + "-" + val.substr(6,2)
    } 
    return newDate;
}

// 숫자 천단위 콤마
// Table number null value to zero. 
// Add a comma to a numeric denomination.
function getNumberFormat(val, row) {
	var newNumber = "0";
	if(val != "" && val != null && val !='0' && val != 0){
		newNumber = formatComma(val);
	}
    return newNumber;
}

// Table number null value to null value.
function getNumberFormatNullChk(val, row) {
	var newNumber = "0";
	if ( val != "" && val != null && val !='0' && val != 0 ) {
		newNumber = formatComma(val);
	} else if ( val == null ) {
		newNumber = null;
	}
	return newNumber;
}

// Y And N Check to "O" when 'Y'
function formatYnChk( val, row ) {
	if ( val == 'Y' ) {
		return '○';
	} else {
		return '';
	}
}

function formatPrice(val,row){
	console.log("val :",val);
	var num = "";
	if(val != undefined){
		var num = val.toString();
		return num.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");   
	}else{
		return num;
	}
}

function toBlankFromZeroFormat(val) {
	var text = val;
	
	if(val == 0) {
		text = '';
	}
	
	return text;
}

function toNbspFromWithespace(val) {
	var text = '';
	
	if(val == '') {
		return text;
	}
	
	for(var index = 0; index < val.length; index++) {
		if(val.substring(index, index + 1) === ' ') {
			text += '&nbsp';
		} else {
			text += val.substring(index, val.length);
			break;
		}
	}
	
	return text;
}

//백분율 소수2째자리까지...
function getPercentRound(val, row) {
	var newNumber = "0";
	if(val != "" && val != null){
		newNumber = parseInt(val*100)/100;
	}	
    return newNumber;
}

// 값이 정수이면 자리수만큼 소수점 이하에 0을 포함항 문자열로 돌려준다.
function fillDigit(val, digitNumber) {
	if(/^(\-|\+)?([0-9]+)$/.test(val) && parseInt(val ) > 0){
		let retValue = val + ".";
		
		for(let index = 0; index < digitNumber; index++) {
			retValue += '0';
		}
		
		return retValue;
	} else {
		return val;
	}

}

//해당 문장의 개행 처리
function getStringNewLine(val, row) {
	var newString = "0";
	if(val != "" && val != null){
		newString = strReplace(strReplace(strReplace(val,">","&gt;"),"<","&lt;"),"\n","<br/>");
	}	
    return newString;
}

//컬러 적용
function formatColor(val, row) { 
	return "<div style='float:left;width:13px;height:15px;margin-right:3px;margin-top:2px;background-color:"+val+";'></div>" + val;
}

// 백분율 "%" 붙이기
function dataRatio(val, row) { 
	return val + "%";
}

function cellStylerCursorDefault(val, row) {
	return 'cursor:default';
}

//FLOAT로 변환
function strParseFloat(str, default_num){
	var strFloat = 0;
	
	if (str != null && str != "") {
		strFloat = parseFloat(str.trim());
    } else {
    	strFloat = default_num;
    }
	
	return strFloat;
}

//DOUBLE로 변환
function strParseDouble(str, default_num){
	var strDouble = 0;
	
	if (str != null && str != "") {
		strDouble = parseDouble(str.trim());
    } else {
    	strDouble = default_num;
    }
	
	return strDouble;
}

//LONG으로 변환
function strParseLong(str, default_num){
	var strLong = 0;
	
	if (str != null && str != "") {
		strLong = parseLong(str.trim());
    } else {
    	strLong = default_num;
    }
	
	return strLong;
}

//INT으로 변환
function strParseInt(str, default_num){
	var strInt = 0;
	
	if (str != null && str != "") {
		strInt = parseInt(str.trim());
    } else {
    	strInt = default_num;
    }
	
	return strInt;
}

function nvl(str, defaultVal) {
    var defaultValue = "";
    
    if (typeof defaultVal != 'undefined') {
        defaultValue = defaultVal;
    }
     
    if (typeof str == "undefined" || str == null || str == '' || str == "undefined") {
        return defaultValue;
    }
     
    return str;
}

function strRound(str, roundVal) {
	return str.toFixed(roundVal); 
}

/**
 * 다용도 AJAX 함수
 * @param {Object} url 대상 URL
 * @param {Object} form FORM을 submit 할 경우
 * @param {Object} callback 콜백 함수
 */ 

ajaxCall = function(url, form, typeString, callback){
//		if(form != null){
//		var $form = form;
//		var data = getFormData($form);
//		var queryString = JSON.stringify(data);		
//	}
	var queryString = '';

	if(form != null){
		queryString = form.formSerialize();
	}

    var options = {
    	beforeSend: function (xhr) {
    		xhr.setRequestHeader("AJAX", true);
    	},
		data : queryString,
        url: url, // override for form's 'action' attribute  
        success:  //callback,// post-submit callback
        function(result){
//        	아직은 테스트 중입니다.
        	if(typeof result == 'string' && result.indexOf('<!DOCTYPE html>') > 0) {
        		if(top.closeMessageProgress != undefined) top.closeMessageProgress();
				
        		top.$.messager.confirm('',"세션 연결이 끊어져서 로그인 페이지로 이동합니다.", function(check) {
        			if(check) {
        				top.location.href="/auth/logout";
        			}
        		});
        	}
        	
        	if(callback != 'undefined' && callback != null && callback != '')
        	{
        		callback(result);
        	}
    	},
        type: typeString,
        error: function(xhr, error){
        	if (xhr.status == 401) {
				top.location.href="/auth/logout";
        	}else if (xhr.status == 500) {
					top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 서버 에러가 발생했습니다. 관리자에게 문의하세요.");
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
					if(closeMessageProgress != undefined) closeMessageProgress();	
			}else if(xhr.status == 403 || xhr.status == 405){
				  console.error('error occured : '+ xhr.status);
//                location.href="/auth/denied";
				top.location.href="/auth/logout";
			}else if(xhr.status == 404){
				top.$.messager.alert('',"페이지를 표시할 수 없습니다. 브라우저를 새로고침한 후 재접속을 해 주십시오.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else if(xhr.status == 0){
				if(error == 'error') {
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();
					
					console.error("error > logout");
					top.location.href="/auth/logout";
					return;
				}
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"[net::ERR_CONNECTION_REFUSED] 서버 연결에 실패하였습니다. 관리자에게 문의하세요.");
				//parent.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else{
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				//location.href="/auth/logout";
			}
    	},
//        timeout: 3600000  // 210902 김원재 #278 [건보에서 SQL종합진단 실행시 오류] 타임아웃 시간 12시간으로 변경
        timeout: 43200000

    };
    
	$.ajax(options);
};

/**
 * 다용도 AJAX 함수 callback 함수 실행시 callbackParam도 같이 전달함. callback(result , callbackParam)
 * @param {Object} url 대상 URL
 * @param {Object} form FORM을 submit 할 경우
 * @param {Object} callback 콜백 함수
 * @param {Object} callbackParam 콜백 함수에 같이 전달 할 파라미터.

 */
ajaxCallWithCallbackParam = function(url, form, typeString, callback, callbackParam){
//		if(form != null){
//		var $form = form;
//		var data = getFormData($form);
//		var queryString = JSON.stringify(data);
//	}
	var queryString = '';

	if(form != null){
		queryString = form.formSerialize();
	}

    var options = {
    	beforeSend: function (xhr) {
    		xhr.setRequestHeader("AJAX", true);
    	},
		data : queryString,
        url: url, // override for form's 'action' attribute
        success:  //callback,// post-submit callback
        function(result){
//        	아직은 테스트 중입니다.
//        	if(typeof result == 'string' && result.indexOf('<!DOCTYPE html>') > 0) {
//        		if(top.closeMessageProgress != undefined) top.closeMessageProgress();
//
//        		top.$.messager.confirm('',"세션 연결이 끊어져서 로그인 페이지로 이동합니다.", function(check) {
//        			if(check) {
//        				top.location.href="/auth/logout";
//        			}
//        		});
//        	}

        	if(callback != 'undefined' && callback != null && callback != '')
        	{
        		callback(result, callbackParam);
        	}
    	},
        type: typeString,
        error: function(xhr, error){
        	if (xhr.status == 401) {
				top.location.href="/auth/logout";
        	}else if (xhr.status == 500) {
					top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 서버 에러가 발생했습니다. 관리자에게 문의하세요.");
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
					if(closeMessageProgress != undefined) closeMessageProgress();
			}else if(xhr.status == 403 || xhr.status == 405){
				  console.error('error occured : '+ xhr.status);
//                location.href="/auth/denied";
				top.location.href="/auth/logout";
			}else if(xhr.status == 404){
				top.$.messager.alert('',"페이지를 표시할 수 없습니다. 브라우저를 새로고침한 후 재접속을 해 주십시오.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();
			}else if(xhr.status == 0){
				if(error == 'error') {
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();

					console.error("error > logout");
					top.location.href="/auth/logout";
					return;
				}
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"[net::ERR_CONNECTION_REFUSED] 서버 연결에 실패하였습니다. 관리자에게 문의하세요.");
				//parent.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();
			}else{
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				//location.href="/auth/logout";
			}
    	},
//        timeout: 3600000  // 210902 김원재 #278 [건보에서 SQL종합진단 실행시 오류] 타임아웃 시간 12시간으로 변경
        timeout: 43200000

    };

	$.ajax(options);
};

/**
 * 다용도 AJAX 함수
 * @param {Object} url 대상 URL
 * @param {Object} form FORM을 submit 할 경우
 * @param {Object} callback 콜백 함수
 * @param {Object} timeOut timeOut (mils)
 */

ajaxCallWithTimeout = function(url, form, typeString, callback, timeOut){
//		if(form != null){
//		var $form = form;
//		var data = getFormData($form);
//		var queryString = JSON.stringify(data);
//	}
	var queryString = '';

	if(isEmpty(timeOut)){
		console.log('function ajaxCallWithTimeout\'s need TimeOut');
	}

	if(form != null){
		queryString = form.formSerialize();
	}

    var options = {
    	beforeSend: function (xhr) {
    		xhr.setRequestHeader("AJAX", true);
    	},
		data : queryString,
        url: url, // override for form's 'action' attribute
        success:  //callback,// post-submit callback
        function(result){
//        	아직은 테스트 중입니다.
//        	if(typeof result == 'string' && result.indexOf('<!DOCTYPE html>') > 0) {
//        		if(top.closeMessageProgress != undefined) top.closeMessageProgress();
//
//        		top.$.messager.confirm('',"세션 연결이 끊어져서 로그인 페이지로 이동합니다.", function(check) {
//        			if(check) {
//        				top.location.href="/auth/logout";
//        			}
//        		});
//        	}

        	if(callback != 'undefined' && callback != null && callback != '')
        	{
        		callback(result);
        	}
    	},
        type: typeString,
        error: function(xhr, error){
        	if (xhr.status == 401) {
				top.location.href="/auth/logout";
        	}else if (xhr.status == 500) {
					top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 서버 에러가 발생했습니다. 관리자에게 문의하세요.");
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
					if(closeMessageProgress != undefined) closeMessageProgress();
			}else if(xhr.status == 403 || xhr.status == 405){
				  console.error('error occured : '+ xhr.status);
//                location.href="/auth/denied";
				top.location.href="/auth/logout";
			}else if(xhr.status == 404){
				top.$.messager.alert('',"페이지를 표시할 수 없습니다. 브라우저를 새로고침한 후 재접속을 해 주십시오.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();
			}else if(xhr.status == 0){
				if(error == 'error') {
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();

					console.error("error > logout");
					top.location.href="/auth/logout";
					return;
				}
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"[net::ERR_CONNECTION_REFUSED] 서버 연결에 실패하였습니다. 관리자에게 문의하세요.");
				//parent.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();
			}else{
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				//location.href="/auth/logout";
			}
    	},
//        timeout: 3600000  // 210902 김원재 #278 [건보에서 SQL종합진단 실행시 오류] 타임아웃 시간 12시간으로 변경
        timeout: timeOut

    };

	$.ajax(options);
};


/**
 * Json형태의 데이터를 전송하는 AJAX 함수
 * @param {Object} url 대상 URL
 * @param {Object} jData JsonObject
 * @param {Object} typeString 데이터 전송 타입(get / post)
 * @param {Object} callback 콜백 함수
 */
ajaxCallWithJson = function(url, jData, typeString, callback){
	
	var options = {
		
		url: url,
		type: typeString,
		contentType: 'application/json; charset=utf-8',
		data : JSON.stringify(jData),
		success:
			function(result){
			if(callback != 'undefined' && callback != null && callback != '')
			{
				callback(result);
			}
		},
		error: function(xhr, error){
			if (xhr.status == 401) {
				top.location.href="/auth/logout";
			}else if (xhr.status == 500) {
					top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 보안 에러가 발생했습니다. 관리자에게 문의하세요.");
					if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
					if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();	
					if(closeMessageProgress != undefined) closeMessageProgress();	
			}else if(xhr.status == 403 || xhr.status == 405){
				top.location.href="/auth/logout";
			}else if(xhr.status == 404){
				top.$.messager.alert('',"페이지를 표시할 수 없습니다. 브라우저를 새로고침한 후 재접속을 해 주십시오.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else if(xhr.status == 0){
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"[net::ERR_CONNECTION_REFUSED] 서버 연결에 실패하였습니다. 관리자에게 문의하세요.");
				//parent.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				if(top.closeMessageProgress != undefined) top.closeMessageProgress();	
			}else{
				//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				top.$.messager.alert('',"["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
				//location.href="/auth/logout";
			}
		},
		timeout: 3600000
	};
	
	$.ajax(options);
};

/**
 * 다용도 multipart AJAX 함수
 * @param {Object} url 대상 URL
 * @param {Object} form FORM을 submit 할 경우
 * @param {Object} callback 콜백 함수
 */ 

ajaxMultiCall = function(url, form, typeString, callback){
    var options = {
		beforeSend: function (xhr) {
    		xhr.setRequestHeader("AJAX", true);
    	},
    	processData: false,
		contentType: false,
		data : form,
        url: url,  
        success:
        function(result){

        	if(callback != 'undefined' && callback != null && callback != '')
        	{
        		callback(result);
        	}
    	},
        type: typeString,
        error: function(xhr, error){
        	//alert("["+ xhr.status +" : " + error + "] 예외가 발생했습니다. 관리자에게 문의하세요.");
    	},
        timeout: 1800000
    }
    
	$.ajax(options);
};


/* 테스트를 위해 dashboard(main) 없이 개별 화면으로 동작 가능하도록 처리*/
/* 차후에 불필요하다고 판단되면 삭제 */
/* 2018-06-19 */
/* 탭 링크 함수 */
function openLinkTest(updateYn, menuId, menuNm, menuUrl, menuParam){	
	/* 파라미터가 존재하는 경우 처리 */
	if(menuParam == ""){
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&menu_id="+menuId;
		}else{
			menuParam = "?menu_id="+menuId;	
		}		
	}else{
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&"+menuParam+"&menu_id="+menuId;
		}else{
			menuParam = "?"+menuParam+"&menu_id="+menuId;	
		}			
	}
	document.location.href = menuUrl+menuParam;
}

ExcelDownClick = function(index,sheetname,filename){
	var options = {
        	beforeSend: function (xhr) {
        		xhr.setRequestHeader("AJAX", true);
        	},
    		data : "",
            url: "/getDate", // override for form's 'action' attribute  
            success:  //callback,// post-submit callback
            function(result){
            	ExcelDownClick1(index,sheetname,filename,result);
        	},
            type: "GET",
            error: function(xhr, error){
            	var date = getDate();
            	ExcelDownClick1(index,sheetname,filename,date);
        	}
        };
    	$.ajax(options);	
}

var ExcelDownClick1 = (function () {
	  var uri = 'data:application/vnd.ms-excel;base64,'
	  , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body>{table}</body></html>'
	  , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
	  , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
	  return function (index, sheetname, filename, date) {
		  
			var tableClassName = "datagrid-view2";
			var hTableClassName = "datagrid-htable";
			var bTableClassName = "datagrid-btable";
			var worksheetname =  'data sheet';
			var excelfilename = 'excel_data_'+date+'.xls';
			if(sheetname != undefined && sheetname != ''){
				worksheetname = sheetname;
			}
			if(filename != undefined && filename != ''){
				if(date != ""){
					excelfilename = filename+"_"+date+'.xls';
				}else{
					excelfilename = filename+'.xls';
				}
			}
			var a = document.createElement('a');
			
			var excelExportHtml = "<meta http-equiv='Content-Type' content='application/vnd.ms-excel; charset=utf-8'/>"; //엑셀다운로드 한글 깨짐 현상 해결
			excelExportHtml += $('<div>').append($($('.'+tableClassName)[index]).clone()).html();
//			console.log("excelExportHtml",excelExportHtml);
			var doc = null;
			try{
				doc = document.implementation.createHTMLDocument();
			}catch(e){
				doc = document.implementation.createHTMLDocument('title');
			}
			doc.body.innerHTML = excelExportHtml;

			var tableObject = doc.getElementsByTagName("table");
			for(var i=0;i<tableObject.length;i++){
//				tableObject[i].removeAttribute("class");
//				tableObject[i].removeAttribute("style");
				//tableObject[i].style.borderStyle="thin solid green";
				tableObject[i].style.border="thin solid #000000";
				tableObject[i].style.textAlign="center";
			}		
			var tds = doc.getElementsByTagName("td");
			for(var i=0;i<tds.length;i++){
//				tds[i].removeAttribute("class");
//				tds[i].removeAttribute("style");
//				tds[i].removeAttribute("field");
				//tds[i].style.borderStyle="thin solid green";
				tds[i].style.border="thin solid #000000";
				tds[i].style.textAlign="center";
			}

			var header_rows = doc.getElementsByClassName('datagrid-header-row');
			for(var i=0;i<header_rows.length;i++){
				var header_row = header_rows[i];
				header_row.setAttribute("style", "color: rgb(0, 0, 0); background: rgb(0, 255, 255);");

				header_row.style.color="#000000";
				header_row.style.background="#ff0732";

				$(header_row).css( "background-color", "#000000" );
				$(header_row).css( "color", "#F5D03A" );

				var header_columns = header_row.childNodes;
				for (var j = 0; j < header_columns.length; j++) {
					$(header_columns[j]).css( "background-color", "#fdd296" );
					$(header_columns[j]).css( "color", "#000000" );
				}
			}

		    //excelExportHtml = doc.documentElement.outerHTML;
			excelExportHtml = doc.body.innerHTML;
			var ua = window.navigator.userAgent;
			var msie = ua.indexOf("MSIE ");
			if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
			{
				  if (typeof Blob !== "undefined") {
					    //use blobs if we can
					    tab_text = [excelExportHtml];
					    //convert to array
					    var blob1 = new Blob(tab_text, {
					      type: "text/html"
					    });
					    window.navigator.msSaveBlob(blob1, excelfilename);
				  } else {
//					excelDownIf.document.open("application/vnd.ms-excel","replace");
					excelDownIf.document.open("text/html","replace");
					excelDownIf.document.write(excelExportHtml);
					excelDownIf.document.close();
					excelDownIf.focus();
					sa=excelDownIf.document.execCommand("SaveAs",true,excelfilename);
					return (sa);
				    
				  }
			}else{
				var ctx = { worksheet: worksheetname || 'Worksheet', table: excelExportHtml };
				a.href = uri + base64(format(template, ctx));
				a.download = excelfilename;
				a.click();
				return (a);
			}
	  }
})();

Date.prototype.YYYYMMDDHHMMSS = function () {
    var yyyy = this.getFullYear().toString();
    var MM = pad(this.getMonth() + 1,2);
    var dd = pad(this.getDate(), 2);
    var hh = pad(this.getHours(), 2);
    var mm = pad(this.getMinutes(), 2)
    var ss = pad(this.getSeconds(), 2)

    return yyyy + MM + dd+  hh + mm + ss;
};

function getDate() {
    d = new Date();
    return d.YYYYMMDDHHMMSS();
}

function pad(number, length) {

    var str = '' + number;
    while (str.length < length) {
        str = '0' + str;
    }

    return str;

}
//엔터키를 치면 조회를 한다.
//버튼을 눌렀을때만 조회하는 것으려 통일함으로 사용안함
function fnCheckEnter(fnName){
	var keycode = event.keyCode;
	if(keycode == 13){
		//eval(fnName+"();");
	}
}

/**
 * JSON STRING 콜백 공통 함수 
 */
var json_string_callback_common = function(result, gridid, progressExist) {
	try{
		var data = JSON.parse(result);
		//console.log("json_string_callback_common data:",data);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'||data.message == ''){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
			$(gridid).datagrid("loadData", data);
			$(gridid).datagrid('loaded');
		}
	}catch(e){
			var tempResult = result;
			console.error("json_string_callback_common Error[" + result + "] gridid[" + gridid + "] e[" + e + "]");
			
			if(tempResult.indexOf('{') == 0 && tempResult.indexOf('}') == tempResult.length - 1) {
				tempResult = tempResult.substring(1, tempResult.length - 1);
			}
			
			var resultStartPosition = tempResult.indexOf('result');
			
			if(resultStartPosition >= 0) {
				var resultEndPosition = tempResult.indexOf(',', resultStartPosition);
				var resultStr = tempResult.substring(resultStartPosition + 8, resultEndPosition);
				var messageStartPosition = -1;
				
				if(resultStr == 'false') {
					messageStartPosition = tempResult.indexOf('message', resultEndPosition);
					
					if(messageStartPosition >= 0) {
						errorMessager(tempResult.substring(messageStartPosition + 9));
					}
				}
				
				if(resultStr == 'true' || messageStartPosition == -1) {
					parent.$.messager.alert('',e.message);
				}
			} else {
				if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
					$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
						setTimeout(function() {
							top.location.href="/auth/login";
						},1000);
					});
				}else{
					parent.$.messager.alert('',e.message);
				}
			}
	}finally{
		/* modal progress close */
		if(progressExist){
			if(parent.closeMessageProgress != undefined) {
				parent.closeMessageProgress();
				closeMessageProgress();
			}
		}
	}
}

/**
 * JSON STRING 콜백 공통 함수
 *  * 데이터가 없을때 문구 파라미터 추가.

 */
var json_string_callback_with_emptyMsg_common = function(result, gridid, progressExist, emptyMsg) {
	try{
		var data = JSON.parse(result);
		//console.log("json_string_callback_common data:",data);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'||data.message == ''){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else if (data.rows.length === 0){
			var opts = $(gridid).datagrid('options');
			var vc = $(gridid).datagrid('getPanel').children('div.datagrid-view');

			vc.children('div.datagrid-empty').remove();

			if (!$(gridid).treegrid('getRows').length){
				let d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);

				d.css({
					top:50
				});
			}
		}else{
			$(gridid).datagrid("loadData", data);
			$(gridid).datagrid('loaded');
		}
	}catch(e){
			var tempResult = result;
			console.error("json_string_callback_common Error[" + result + "] gridid[" + gridid + "] e[" + e + "]");

			if(tempResult.indexOf('{') == 0 && tempResult.indexOf('}') == tempResult.length - 1) {
				tempResult = tempResult.substring(1, tempResult.length - 1);
			}

			var resultStartPosition = tempResult.indexOf('result');

			if(resultStartPosition >= 0) {
				var resultEndPosition = tempResult.indexOf(',', resultStartPosition);
				var resultStr = tempResult.substring(resultStartPosition + 8, resultEndPosition);
				var messageStartPosition = -1;

				if(resultStr == 'false') {
					messageStartPosition = tempResult.indexOf('message', resultEndPosition);

					if(messageStartPosition >= 0) {
						errorMessager(tempResult.substring(messageStartPosition + 9));
					}
				}

				if(resultStr == 'true' || messageStartPosition == -1) {
					parent.$.messager.alert('',e.message);
				}
			} else {
				if(e.message.indexOf("Unexpected token") != -1 || e.message.indexOf("유효하지 않은 문자입니다.") != -1){
					$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
						setTimeout(function() {
							top.location.href="/auth/login";
						},1000);
					});
				}else{
					parent.$.messager.alert('',e.message);
				}
			}
	}finally{
		/* modal progress close */
		if(progressExist){
			if(parent.closeMessageProgress != undefined) {
				parent.closeMessageProgress();
				closeMessageProgress();
			}
		}
	}
}

/**
 * JSON STRING treegrid 콜백 함수 
 */
var json_string_treegrid_callback_common = function(result, treegridid, progressExist) {
	try{
		let target = $(treegridid);
		let data = JSON.parse(result);
		
		if(data.rows == undefined){
			if(data.resultMsg == 'null'||data.resultMsg == ''){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.resultMsg);
			}
			
			return;
		}
		
		if(data.rows.length == 0) {
			var opts = $(target).treegrid('options');
			var vc = $(target).treegrid('getPanel').children('div.datagrid-view');
			
			vc.children('div.datagrid-empty').remove();
			
			if (!$(target).treegrid('getRows').length){
				let emptyMsg = "검색된 데이터가 없습니다.";
				let d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
				
				d.css({
					top:50
				});
			}
		} else {
			let opts = $(target).treegrid('options');
			let vc = $(target).treegrid('getPanel').children('div.datagrid-view');
			
			vc.children('div.datagrid-empty').remove();
			
			$(target).treegrid('loadData', data);
		}
	}catch(err){
		parent.$.messager.alert('',err.message);
	}finally{
		/* modal progress close */
		if(progressExist){
			if(parent.closeMessageProgress != undefined) {
				parent.closeMessageProgress();
				closeMessageProgress();
			}
		}
	}
}

var chart_callback = function(result, chartid) {
	var data = JSON.parse(result);
	//console.log("chartid:",chartid);
	//console.log("chartid.container:",chartid.container);
	if(chartid.container != undefined){
		//console.log("chartid.container.id:",chartid.container.id);
		//console.log(chartid.container.id+" data:",data);
	}else{
		//console.log(chartid.id+" data:",data);
	}

	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','차트 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		var store;
		store = chartid.down("chart").getStore();
		store.loadData(data.rows);
		chartid.down("chart").redraw();
	}
}

/**
 *  dbid 콤보박스 생성 
 */
function fnCreateDbidCombobox(){
	// Database 조회			
	$('#selectDbid').combobox({
	    url:"/Common/getDatabase",
	    method:"get",
		valueField:'dbid',
	    textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function(){
//			$("#_easyui_combobox_i1_0").css("height","14px");
		},
	    onSelect:function(rec){
//	    	var win = parent.$.messager.progress({
//	    		title:'Please waiting',
//	    		text:'점검회차를 불러오는 중입니다.'
//	    	});
	    	
	    	$("#dbid").val(rec.dbid);
			var selDate = $("#check_day").val();
			
			// 점검회차 설정
			setCheckSeq(selDate);
	    }
	});
}

//점검 회차 조회			
function setCheckSeq(strDate){
	var win = parent.$.messager.progress({
		title:'Please waiting',
		text:'점검회차를 불러오는 중입니다.'
	});

	var url = "/PreventiveCheck/GetCheckSeq?check_day="+strDate+"&dbid="+$('#dbid').val();
	$('#selectCheckSeq').combobox({
	    url:url,
	    method:"get",
		valueField:'check_seq',
	    textField:'check_text',
		onLoadSuccess: function(items) {
			parent.$.messager.progress('close');
			
			if (items.length){
				var opts = $(this).combobox('options');
				$(this).combobox('select', items[0][opts.valueField]);
				var callFromParent = $("#call_from_parent").val();
				if(callFromParent == "Y"){
					Btn_OnClick();
				}
				//if($('#selectDbid').combobox('getValue') != ""){
				//검색 버튼을 눌렀을 때 조회
				//	Btn_OnClick();
				//}				
			}
		},
		onLoadError: function(){
			parent.$.messager.alert('','점검회차 조회중 오류가 발생하였습니다.');
			parent.$.messager.progress('close');
		}	    	
	});
}

/* SNAP_ID 선택 팝업 */
//function fnShowSnapPopup(gubun){
//	parent.strGb = gubun;
//	
//	if($('#selectCombo').combobox('getValue') == ""){
//		parent.$.messager.alert('','DB를 선택해 주세요.');
//		return false;
//	}
//	
//	$("#dbid").val($('#selectCombo').combobox('getValue'));
//	
//	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
//	parent.frameName = $("#menu_id").val();	
//	
//	parent.$('#snapList_form #snap_id').val("0");
//	parent.$('#snapList_form #startSnapIdList').datagrid('loadData',[]);
//	parent.$('#snapList_form #endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$('#snapList_form #dbid').val($('#selectCombo').combobox('getValue'));
//	parent.$('#snapList_form #dbName').textbox('setValue',$('#selectCombo').combobox('getText'));
//	parent.$('#snapList_form #instance_number').val($('#selectInstance').combobox('getValue'));
//	
//	parent.$('#startSnapIdList').datagrid('loadData',[]);
//	parent.$('#endSnapIdList').datagrid('loadData',[]);
//	
//	parent.$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
//	parent.$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);	
//	
//	parent.$('#snapListPop').window("open");
//}

function fnShowSnapPopup(gubun){
	strGb = gubun;
	if($('#selectCombo').combobox('getValue') == ""){
		parent.$.messager.alert('','DB를 선택해 주세요.');
		return false;
	}
	
	$("#dbid").val($('#selectCombo').combobox('getValue'));

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();	
	
	$('#snapList_form #snap_id').val("0");
	$('#snapList_form #startSnapIdList').datagrid('loadData',[]);
	$('#snapList_form #endSnapIdList').datagrid('loadData',[]);
	
	$('#snapList_form #dbid').val($('#selectCombo').combobox('getValue'));
	$('#snapList_form #dbName').textbox('setValue',$('#selectCombo').combobox('getText'));
	$('#snapList_form #instance_number').val($('#selectInstance').combobox('getValue'));
	
	$('#startSnapIdList').datagrid('loadData',[]);
	$('#endSnapIdList').datagrid('loadData',[]);

	$("#snapList_form #strStartDt").datebox("setValue", parent.startDate);
	$("#snapList_form #strEndDt").datebox("setValue", parent.nowDate);	

	$('#snapListPop').window("open");
}

/**
 * JSON STRING 콜백 공통 함수 
 */
var treegrid_json_string_callback_common = function(result, gridid, progressExist) {
	try{
		var data = JSON.parse(result);
		if(data.result != undefined && !data.result){
			if(data.message == 'null'){
				parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
			}else{
				parent.$.messager.alert('',data.message);
			}
		}else{
//			console.log("data1",data);
			$(gridid).treegrid("loadData", data);
			$(gridid).treegrid('loaded');			
		}
	}catch(e){
		parent.$.messager.alert('',e.message);
	}
	
	/* modal progress close */
	if(progressExist){
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
}

var fnInitSearchBtnClickFlag = function(){
	$("#submit_form #searchBtnClickCount").val("0");
	
	$("#submit_form #tab1ClickCount").val("0");
	$("#submit_form #tab2ClickCount").val("0");
	$("#submit_form #tab3ClickCount").val("0");
	$("#submit_form #tab4ClickCount").val("0");
	$("#submit_form #tab5ClickCount").val("0");
	$("#submit_form #tab6ClickCount").val("0");
	$("#submit_form #tab7ClickCount").val("0");
	$("#submit_form #tab8ClickCount").val("0");
	$("#submit_form #tab9ClickCount").val("0");
	$("#submit_form #tab10ClickCount").val("0");
	$("#submit_form #tab11ClickCount").val("0");
	$("#submit_form #tab12ClickCount").val("0");
	$("#submit_form #tab13ClickCount").val("0");
	$("#submit_form #tab14ClickCount").val("0");
	$("#submit_form #tab15ClickCount").val("0");
	$("#submit_form #tab16ClickCount").val("0");
	$("#submit_form #tab17ClickCount").val("0");
	$("#submit_form #tab18ClickCount").val("0");
	$("#submit_form #tab19ClickCount").val("0");
	$("#submit_form #tab20ClickCount").val("0");
	$("#submit_form #tab21ClickCount").val("0");
	$("#submit_form #tab22ClickCount").val("0");


}; 

var fnUpdateSearchBtnClickFlag = function(){
	$("#submit_form #searchBtnClickCount").val("1");
	
	$("#submit_form #tab1ClickCount").val("0");
	$("#submit_form #tab2ClickCount").val("0");
	$("#submit_form #tab3ClickCount").val("0");
	$("#submit_form #tab4ClickCount").val("0");
	$("#submit_form #tab5ClickCount").val("0");
	$("#submit_form #tab6ClickCount").val("0");
	$("#submit_form #tab7ClickCount").val("0");
	$("#submit_form #tab8ClickCount").val("0");
	$("#submit_form #tab9ClickCount").val("0");
	$("#submit_form #tab10ClickCount").val("0");
	$("#submit_form #tab11ClickCount").val("0");
	$("#submit_form #tab12ClickCount").val("0");
	$("#submit_form #tab13ClickCount").val("0");
	$("#submit_form #tab14ClickCount").val("0");
	$("#submit_form #tab15ClickCount").val("0");
	$("#submit_form #tab16ClickCount").val("0");
	$("#submit_form #tab17ClickCount").val("0");
	$("#submit_form #tab18ClickCount").val("0");
	$("#submit_form #tab19ClickCount").val("0");
	$("#submit_form #tab20ClickCount").val("0");
	$("#submit_form #tab21ClickCount").val("0");
	$("#submit_form #tab22ClickCount").val("0");

}; 

function createSqlTuningIndexHistoryTable(){
	//index 내역
	$("#tableList").datagrid({
		view: myview,
		onClickRow : function(index,row) {
		},			
		columns:[[
			{field:'tuning_no',hidden:"true"},
			{field:'index_impr_type_nm',title:'개선유형',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'table_name',title:'테이블명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_name',title:'인덱스명',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'index_column_name',title:'인덱스컬럼',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'before_index_column_name',title:'변경전 인덱스컬럼',width:"30%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});

	ajaxCall("/SqlTuningIndexHistoryAction",
		$("#submit_form"),
		"POST",
		callback_SQLTuningIndexHistory);	
}

//인덱스 이력 콜백 함수
var callback_SQLTuningIndexHistory = function(result) {
	//$('#tableList').datagrid('loadData',[]);
	var data = JSON.parse(result);
	
	if(data.result != undefined && !data.result){
		if(data.message == 'null'){
			parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		}else{
			parent.$.messager.alert('',data.message);
		}
	}else{
		$('#tableList').datagrid("loadData", data);
	}	
};

var nullStringToBlank = function(str){
	if(str == "null"){
		return "";
	}else if(str == null){
		return "";
	}
	else return str;
}

//callback 함수
var callback_RegistExceptionAction = function(result) {
	if(result.result){
		parent.$.messager.alert('',result.message,'info',function(){
			setTimeout(function() {
				Btn_OnClick();
			},1000);			
		});		
	}else{
		parent.$.messager.alert('',result.message,'error');
	}
}

//문자열 byte 수 계산
function byteLength(str) {
	var lenght= 0;
	
	for(var idx=0; idx < str.length; idx++) {
		var c = escape(str.charAt(idx));
		
		if( c.length==1 ) lenght ++;
		else if( c.indexOf("%u")!=-1 ) lenght += 2;
		else if( c.indexOf("%")!=-1 ) lenght += c.length / 3;
	}
	
	return lenght;
}

function openLinkByPost(updateYn, menuId, menuNm, menuUrl, formId){
	
	if (parent.$('#mainTab').tabs('exists',menuNm)){
		parent.$('#mainTab').tabs('select',menuNm);
		
		if(updateYn == "Y"){ // 탭의 Content Update
			var tab = parent.$('#mainTab').tabs('getSelected');
			parent.$('#mainTab').tabs('update', {
				tab: tab,
				options: {
					content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" style="width:100%;height:99%;"></iframe>'
				}
			});
			
			// 메뉴명 배열에 추가
//			parent.menuNameArry.push(menuNm);
			
			$(formId).attr("method", "post");
			$(formId).attr("target", "if_"+menuId);
			$(formId).attr("action",menuUrl);
			$(formId).submit();
		}
	} else {
		
		/* tab생성을 10개로 제한한다. */
		if(parent.tabCount > 10){
			$.messager.alert('','생성된 탭의 갯수가 많습니다.<br/>작업 완료한 탭을 닫고 다른 탭을 열어주세요.','info');
		}else{
			parent.$('#mainTab').tabs('add',{
				id:menuId,
				title:menuNm,
				closable:true,
				tools:[{
//			        iconCls:'icon-mini-refresh',
					iconCls:'fas fa-redo fa-lg fa-fw',
					handler:function(){
						//console.log("menuId===>"+menuId);
						if(document.getElementById('if_'+menuId) != undefined){
							//console.log("document.getElementById('if_"+menuId+"').src:"+document.getElementById('if_'+menuId).src);
						}
						//184 : 성능개선결과 산출물
						if(menuId == '110'|| menuId == '114'|| menuId == '184'|| menuId == '117'|| menuId == '300'){
							//eval('if_'+menuId).document.forms[0].submit();
							document.getElementById('if_'+menuId).src = document.getElementById('if_'+menuId).src;
						}else if(menuId == '212'){
							var action = eval('if_'+menuId).document.forms[0].action;
							action = action.substring(0, action.indexOf("?"));
							console.log("menuid:"+menuId+" action :"+action);
							eval('if_'+menuId).document.forms[0].action = action;
							eval('if_'+menuId).document.forms[0].method = "GET";
							eval('if_'+menuId).document.forms[0].submit();
						}else{
							if(menuId.substring(0,3) == '110'){
								var action = eval('if_'+menuId).document.forms[0].action;
								action = action.substring(0, action.indexOf("?"));
								console.log("menuid:"+menuId+" action :"+action);
								eval('if_'+menuId).document.forms[0].action = action;
								eval('if_'+menuId).document.forms[0].method = "POST";
								eval('if_'+menuId).document.forms[0].submit();
							} else if(menuId.substring(0,4) == '1121') {
								var action = parent.eval('if_'+menuId).document.forms[0].action;
								
								parent.eval('if_'+menuId).document.forms[0].action = action;
								parent.eval('if_'+menuId).document.forms[0].method = "POST";
								parent.eval('if_'+menuId).document.forms[0].submit();
//								parent.eval('if_'+menuId).document.location.href = parent.eval('if_'+menuId).document.location.href;
								
							} else {
								//eval('if_'+menuId).location.reload();
								location.href = location.href;
							}
						}
					}
				}],				
				content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" style="width:100%;height:99%;"></iframe>'
			});
			
			// 메뉴명 배열에 추가
			parent.menuNameArry.push(menuNm);
			
			parent.tabCount++;

			$(formId).attr("method", "post");
			$(formId).attr("target", "if_"+menuId);
			$(formId).attr("action",menuUrl);
			$(formId).submit();
		}
	}
	$(".tabs-p-tool a").css("font-size","10px");
	$(".tabs-p-tool a").css("margin-top","3px");
	$(".tabs-p-tool a").css("color","black");

}

function pullOutBindArray(sql_text) {
		var pattern = /\:.+?(\s|,|;|\))/g; 						/* ':' 문자부터 공백이나 ',' 이나 ';' 이나 ')' 이전 까지의 문자 패턴 */
		var pattern2 = new RegExp('//.*\n', 'gm');				/* //시작해서 \n까지 주석 구문 삭제  */
		var pattern3 = /\--.*|---.*(\:|\$|\#).*\n/g;			/****************************************************************
																 * 이전 버전 : --시작해서  $,:,# \n까지 주석 구문 삭제
																 * 수정 버전 : 이전 버전(Revision.7225)까지 "--" 주석 기호 앞 또는 뒤로 븉거나 한칸 이상 띄워졌을 때 정규식에서 놓치는 문제점 발견
																 * 
																 * -- 1. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100    -- sdf
																 *
																 * -- 2. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100    --sdf
																 *
																 * -- 3. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100    --     sdf
																 *
																 * -- 4. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100-- sdf
																 *
																 * -- 5. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100--      sdf
																 *
																 * -- 6. Example missing from previous version regular expressions
																 * select * from orders
																 * where rownum < 100--sdf
																 *****************************************************************/
	var bindArray = null;
	
	if(sql_text.length == 0) {
		return bindArray;
	}
	
	var strText = sql_text + " \n";
	
	strText = strText.replace(/http.*\:\/\//g,'');				/* http:// or https:// 제거*/
	
	strText = strText.replace(/\/\*[\s\S]*?\*\//g, '');			/* /* *'/ 주석 안에 들어간 문자 제거 */
	
	strText = strText.replace(/(\<\!\-).*?(\-\>)/g,'');			/* <!- -> 주석 제거 */
	
	strText = strText.replace(/(\')+?(\')|(\")+?(\")/g,'');		/* 연속된 ' 나 연속된 " 특수문자 제거 
																   ' or "가 1회이상 연속으로 반복될때 공백으로 치환
																*/
	
	strText = strText.replace(/(\').+?(\')/g, '');				/* single quotation mark로 둘러싸여진 구문을 공백으로 치환 */
	
	strText = strText.replace(pattern2,'');						/* // 주석 제거 */
	strText = strText.replace(pattern3,'');						/* --로 시작하는 #,$,: 주석 제거 */
	strText = strText.replace(/(\--|\---.*|\--\s.*)/g,'');		/* --로 시작하는 문자 제거 */
	strText = strText.replace(/--.*\n/g,'');					/* 주석 구문 삭제  */
	
	strText = strText.replace(/\}/g,' ');						/* "}" 문자를 space로 치환하여 변수추출 오류 방지. */
	strText = strText.replace(/(\$|\#)\{/g,':');				/* "${" 또는 "#{"인 문자를 ":"으로 치환 */
	
	strText = strText.replace(/\r/g, '');						/* /r 제거      */ 
	strText = strText.replace(/\n/g, ' ');						/* /n => 공백 */	
	
	strText = strText.replace(/\|\|/g, ' ');					/* ex) ${A}||'/'||${B}||'/'||${C}||'/'||${D}
																 * oracle에서 ||는 값은 연결해주는 문자기 때문에 
																 * ${A}/${B}/${C}/${D} 이런 결과값이 나온다.
																 * 
																 * bind값 은 white space하나이상 잇어야 한개의 바인드값으로 인식하기때문에 
																 * ||'/'||값을 ' '로 치환해야 한다.
																 * ||를 white space로 치환하여 바인드값만 남도록한다. */
	
	strText = strText.replace(/\'|\"/g,'');						/* "SELECT * FROM DUAL WHERE ID = ${abc}" 
	 															 * 변수추출 할 시 :abc" 이런식으로 추출됨	*/
	
//	var bindArry = strText.replace(/(\/\*).+?(\*\/)/g, '').match(pattern); 	/* 주석/힌트 제거, pattern match */
	bindArray = strText.match(pattern);							/* 힌트 제거 */
	
	return bindArray
}

//보류 테스트해야함.
/*function pullOutBindArray(sql_text) {
	var pattern = /\:.+?(\s|,|;|\))/g;  ':' 문자부터 공백이나 ',' 이나 ';' 이나 ')' 이전 까지의 문자 패턴 
	var bindArray = null;
	
	if(sql_text.length == 0) {
		return bindArray;
	}
	
	var strText = sql_text + " \n";
	
	strText = strText.replace(/\'\:/g, '\'');							 // "':" 구분을 "'" 로 치환 - 20191205수정 
	strText = strText.replace(/\:\'/g, '\'');							 // "':" 구분을 "'" 로 치환 - 20191205수정 
	
	strText = strText.replace(/--.*\n/g,'');							 // 주석 구문 삭제                                                      
	strText = strText.replace(/\}/g,' ');								 //  "${"와 "}"로 둘러싸여진 구문에서 "}"을 공백으로 치환   
	strText = strText.replace(/\$\{/g,' :');	 					 	 //  "${"와 "}"로 둘러싸여진 구문에서 "${"을 ":"으로 치환 
	
	strText = strText.replace(/\r/g, '');		 	                     //  /r 제거       
	strText = strText.replace(/\n/g, ' ');								 //  /n => 공백 	
	
//	var bindArry = strText.replace(/(\/\*).+?(\*\/)/g, '').match(pattern); // 주석/힌트 제거, pattern match 
	
	strText = strText.replace(/(\').*?(\')/g, '');						// 	 single quotation mark로 둘러싸여진 구문을 공백으로 치환 
	
	bindArray = strText.replace(/(\/\*).+?(\*\/)/g, '').match(pattern);  //  주석/힌트 제거 
	
	console.log("bindArray:::"+bindArray);
	return bindArray
}*/


function createNewTab(menuId, menuNm, menuUrl, menuParam){
	/* 탭 생성 */
	parent.openLink("Y", menuId, menuNm, menuUrl, menuParam);
}


function createSQLInfoTab(tabId, dbId, sqlId, planHashValue){
	vSqlId = sqlId;
	var objTab = $('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue;

	if (objTab.tabs('exists',tabName)){
		objTab.tabs('select',tabName);
		var tab = objTab.tabs('getSelected');
		objTab.tabs('update', {
			tab: tab,
			options: {
				content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>'
			}
		});
	} else {
		objTab.tabs('add',{
			id:vSqlId,
			title:tabName,
			closable:true,
			content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>'
		});
	}	
}


function createSQLInfoVSQLAllTab2(tabId, dbId, sqlId, planHashValue, sqlYnType) {
	vSqlId = sqlId;
	var objTab = $('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue+"&isvsql=Y&rcount="+sqlYnType;
	
	if (objTab.tabs('exists',tabName)){
		objTab.tabs('select',tabName);
		var tab = objTab.tabs('getSelected');
		objTab.tabs('update', {
			tab: tab,
			options: {
				content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>'
			}
		});
	} else {
		objTab.tabs('add',{
			id:vSqlId,
			title:tabName,
			closable:true,
			content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%; "></iframe>'
		});
	}	
}

function createSQLInfoVSQLAllTab(tabId, dbId, sqlId, planHashValue){
	
	vSqlId = sqlId;
	var objTab = $('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue+"&isvsql=Y";
	
	if (objTab.tabs('exists',tabName)){
		objTab.tabs('select',tabName);
		var tab = objTab.tabs('getSelected');
		objTab.tabs('update', {
			tab: tab,
			options: {
				content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;height:99.5%;"></iframe>'
			}
		});
	} else {
		objTab.tabs('add',{
			id:vSqlId,
			title:tabName,
			closable:true,
			content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;height:99.5%;"></iframe>'
		});
	}	
}

/* 기능수행에 필요한 필수 요구사항을 전달할 경우 사용 
 * ex) XX 항목을 선택 해 주세요.
 */
function warningMessager(message) {
	$.messager.alert('경고',message,'warning');
}
/* 기능수행 결과를 사용자에게 확인시켜줄 때 사용
 * ex) XX 작업이 완료되었습니다.
 */
function infoMessager(message) {
	$.messager.alert('정보',message,'info');
}
/* 기능수행 중 발생한 에러에 대한 정보를 나타낼 때 사용 */
function errorMessager(message) {
	$.messager.alert('오류',message,'error');
}

function confirmMessager(message, callValue, form, requestMapping, callback) {
	$.messager.confirm('', message, function(check) {
		if (check) {
			ajaxCall(callValue, 
					form, 
					requestMapping,
					callback);
		}
	});
}

/* 데이터 로딩 modal 처리 */
function openMessageProgressTypeB(title, msgTitle, interval){
	var strText = "데이터를 불러오는 중입니다.";
	
	winTypeB = $.messager.progress({
		title:title,
		msg:msgTitle,
		interval:interval
	});
}

/* 데이터 로딩 modal 처리 */
function openMessageProgress(msgTitle, msgText){
	var strText = "데이터를 불러오는 중입니다.";
	if(msgText == ""){
		msgText = strText;
	}
	
	$.messager.progress({
		title:'Please waiting',
		msg:msgTitle,
		text:msgText,
		interval:100
	});
//	var bar = $.messager.progress('bar');  // get the progressbar object
//	bar.progressbar('setValue', 60);  // set new progress value	
}

/* 데이터 로딩 modal close */
function closeMessageProgress(){
	$.messager.progress('close');
}



/* 특정문자 카운터 세기 (문자열,찾을값) */
function countMatches(str, substr) {
    var index, counter, sublength;

    sublength = substr.length;
    counter = 0;
    for (index = str.indexOf(substr);
         index >= 0;
         index = str.indexOf(substr, index + sublength))
    {
        ++counter;
    }
    return counter;
};

/* 확장자 검사 .text, .sql */
function checkExtentionFile1(fileName) {
	if(!/\.(txt|sql)$/i.test(fileName)) {
		return false;
	} else {
		return true;
	}
}

/* 
 * 다운로드 progress modal 처리
 * 다운로드 완료 체크하는 함수도 함께 호출됨
 */
function showDownloadProgress( searchCookie ){
	let cookieName = 'excelDownToken';
	if( searchCookie != null && searchCookie != '' ){
		cookieName = searchCookie;
	}
	
	let anyTrash = deleteCookie(cookieName);
	if(anyTrash == true){
		console.log('Throwing away old cookies.');
	}
	
	$.messager.progress({
		title:'Please waiting',
		msg:'',
		text:'Downloading',
		interval:100
	});
	
	downloadChecker( cookieName );
}
/* 
 * 다운로드 완료여부 확인하여 쿠키 삭제
 * 쿠키가 삭제되지 않아도 해당 쿠키를 찾게되면 progress 모달은 닫힘.
 * 위의 경우 콘솔창에서 메세지 확인 가능
 */
function downloadChecker( cookieName ){
	let checkDownloadComplete;
	let isComplete = false;
	let token;
	
	checkDownloadComplete = setInterval(function() {
		token = getCookie( cookieName );
		if(token == "TRUE") {
			isComplete = deleteCookie( cookieName );
				
			if(isComplete == true){
				console.log('Document Download Complete.');
			}else {
				console.log('Error Occurred.');
			}
			
			closeMessageProgress();
			clearInterval(checkDownloadComplete);
		}
	}, 1000 );
}

/* 
 * number sort 기능(tableList)
 * easyui - datagrid 사용 시 sort기능이 제대로 동작안함.
 * sorter:numberSorter 추가하면 작동.
 */
function numberSorter( a , b ) {
	if ( a == null ){
		return -1;
	} else if ( b == null ){
		return 1;
	}
	let v1 = parseFloat(a);
	let v2 = parseFloat(b);
	
	return v1==v2?0:(v1>v2?1:-1);
}

/* 김원재 추가 . check Empty Data
 * Array = []
 * jQuery = $("#id")
 * HTMLCollection = document.getElementById("*");
 * Object = {}
 */
function isEmpty(obj){

	if(obj === null || obj === undefined || obj === ''){
			return true;
	}

	let objConstructor = obj.constructor;

	if(objConstructor === Array){
		return obj.length === 0;
	}
	else if(objConstructor === jQuery){
		return obj.length === 0;
	}
	else if(objConstructor === HTMLCollection){
		return obj.length === 0;
	}
	else if(objConstructor.constructor === Object){
		return Object.keys(obj).length  === 0;
	}

	return false;
}

/* 김원재 추가 . check Empty Data
 * Array = []
 * jQuery = $("#id")
 * HTMLCollection = document.getElementById("*");
 * Object = {}
 */
function isNotEmpty(obj){

	if(obj === null || obj === undefined || obj === ''){
			return false;
	}
	let objConstructor = obj.constructor;

	if(objConstructor === Array){
		return !(obj.length === 0);
	}
	else if(objConstructor === jQuery){
		return !(obj.length === 0);
	}
	else if(objConstructor === HTMLCollection){
		return !(obj.length === 0);
	}
	else if(objConstructor.constructor === Object){
		return !(Object.keys(obj).length  === 0);
	}
	return true;
}

/* 김원재 추가 . Dynamically create form ( return jQuery Obj )
 */
function createDynamicJqueryForm(jsonData){
	let keys = Object.keys(jsonData);

	let form = document.createElement('form');
	form.setAttribute("charset","UTF-8");

	keys.forEach(function(key){
		let hiddenField = document.createElement("input");

		hiddenField.setAttribute("type", "hidden");
		hiddenField.setAttribute("name", key);
		hiddenField.setAttribute("value", jsonData[key]);
		form.appendChild(hiddenField);
	});

	return jQuery(form)
}
/* 김원재 추가 . easyuiField 일 경우 id로 해당 객체의 타입을 찾아 param을 넣어줌
 */
function setEasyUiFieldValue(id , param){

	let type = getEasyUiFieldType(id);

	if(isNotEmpty(type)){

		if(type === 'checkbox' ||
			type === 'switchbutton'){
			$("#"+id)[type](param);
		}

		$('#'+id)[type]('setValue',param);
	}

}
/* 김원재 추가 . easyuiField 일 경우 id로 해당 객체의 타입을 찾고 값을 리턴함
참조 ) autoIndexAnalys.checkRequiredFields
 */

function getEasyUiFieldValue(id){

	let type = getEasyUiFieldType(id);

	if(isNotEmpty(type)){

		if(type === 'checkbox' ||
			type === 'switchbutton'){
			return $('#'+id)[type]('options').checked;
		}

		return $('#'+id)[type]('getValue');
	}

}
/* 김원재 추가 . easyuiField 일 경우 타입을 리턴받음
 */
function getEasyUiFieldType(id){

	let plugins = $.parser.plugins;

	for(let iter=plugins.length-1; iter>=0; iter--){
		if ($('#'+id).data(plugins[iter])){
			return plugins[iter];
		}
	}
}
