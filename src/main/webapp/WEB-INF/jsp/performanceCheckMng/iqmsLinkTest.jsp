<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page session="false"%>
<%
	String requestURL = request.getRequestURL().toString();
	String requestURI = request.getRequestURI();
	String url1= requestURL.substring(0,requestURL.indexOf(requestURI));
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<title>CM</title>
<meta charset="utf-8" />
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<meta http-equiv="cleartype" content="on" />
<%@include file="/WEB-INF/jsp/include/sub_include.jsp"%>
<script>
	var userpassword="<spring:eval expression="@serverConfig['userpassword']"/>";
	var iqmsip="<spring:eval expression="@defaultConfig['iqmsip']"/>";
	var iqmsport="<spring:eval expression="@defaultConfig['iqmsport']"/>";
	var url1="<%=url1%>";
	var status;
	
	$(document).ready(function() {
		var date = new Date();
		var currentDate = date.getFullYear() + '' + leadingZeros(date.getMonth() + 1, 2) + '' + leadingZeros(date.getDate(), 2);
		var currentTime = leadingZeros(date.getHours(), 2) + '' + leadingZeros(date.getMinutes(), 2) + '' + leadingZeros(date.getSeconds(), 2);
		
		$('#cmdepday').val(currentDate);
		$('#cmcreateday').val(currentDate+currentTime);
		
// 		$("#getTest").click(function() {
// 			console.log("GET Test");
// 			test("GET");
// 		});
// 		$("#postTest").click(function() {
// 			console.log("POST Test");
// 			test("POST");
// 		});
		$("#cmConfirm").click(function() {
			console.log("확정");
			$("#status").val("1");
			cmCall('1');
		});
		$("#cmComplete").click(function() {
			console.log("완료");
			$("#status").val("3");
			cmCall('3');
		});
		$("#cmCancel").click(function() {
			console.log("취소");
			$("#status").val("4");
			cmCall('4');
		});
		$("#cmDelete").click(function() {
			console.log("삭제");
			$("#status").val("5");
			cmCall('5');
		});
		$("#cmRequest1").click(function() {
			console.log($(this).val());
			cmRequest1();
		});
		$("#cmRequest2").click(function() {
			console.log($(this).val());
			cmRequest2();
		});
		$("#cmSync1").click(function() {
			console.log($(this).val());
			cmSync1();
		});
		$("#cmSync2").click(function() {
			console.log($(this).val());
			cmSync2();
		});
		$("#perfChkAutoFinishBtn").click(function() {
			console.log($(this).val());
			var url2 = "/auth/PerfChkAutoFinish";
			$("#linkspan").html("<a target=_new href=\""+url1+url2+"\">"+url1+url2+"</a>");
			ajaxCall(url2, $("#submit_form3"), "POST", callback_PerfChkAutoFinishAjaxCall);

		});
		$("#btn_test").on('click',function() {
			console.log("확정");
			$("#status").val("1");							// status 값을 확정으로 변수 지정
			let resultStr = {};								// 빈 JSON 객체 생성
			
			$("#submit_form1 input").each(function(index){	//input Element 각각에 대해 함수 실행
				let value = $(this).val();					// 현재 input의 value
				let name = $(this).attr('name');			// 현재 input의 name
				
				resultStr[name] = value;					// 현재 input 의 name을 key로, 해당 value 넣기
			});
			
			// JSON 서버로 전송
			//ajaxCallWithJson("/iqms/ConfirmTestLink", resultStr, 'POST', callback_JsonParamAjaxCall);
			ajaxCallWithJson("/iqms/CmConfirm", resultStr, 'POST', callback_JsonParamAjaxCall);
		});
// 		//업무 동기화
// 		$("#wrkjobcdSyncBtn").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/syncWrkJobCd", $("#submit_form4"), "POST", callback_syncAjaxCall);
// 		});
// 		//사용자 업무 동기화
// 		$("#userSyncBtn").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/syncUsers", $("#submit_form4"), "POST", callback_syncAjaxCall);
// 		});
// 		//사용자 업무 동기화
// 		$("#userWrkjobSyncBtn").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/syncUserWrkjob", $("#submit_form4"), "POST", callback_syncAjaxCall);
// 		});
// 		//전체(업무코드,사용자,사용자 업무) 동기화
// 		$("#syncAllBtn").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/syncWrkRelatedTable", $("#submit_form4"), "POST", callback_syncAjaxCall);
// 		});
		
// 		//업무 동기화
// 		$("#wrkjobcdSyncBtn2").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/UserWorkSyncJdbc/syncWrkJobCd", $("#submit_form5"), "POST", callback_syncAjaxCall2);
// 		});
// 		//사용자 업무 동기화
// 		$("#userSyncBtn2").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/UserWorkSyncJdbc/syncUsers", $("#submit_form5"), "POST", callback_syncAjaxCall2);
// 		});
// 		//사용자 업무 동기화
// 		$("#userWrkjobSyncBtn2").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/UserWorkSyncJdbc/syncUserWrkjob", $("#submit_form5"), "POST", callback_syncAjaxCall2);
// 		});
// 		//전체(업무코드,사용자,사용자 업무) 동기화
// 		$("#syncAllBtn2").click(function() {
// 			console.log($(this).val());
// 			ajaxCall("/UserWorkSyncJdbc/syncWrkRelatedTable", $("#submit_form5"), "POST", callback_syncAjaxCall2);
// 		});

	});
	
	function leadingZeros(n, digits) {
		var zero = '';
		n = n.toString();
		
		if (n.length < digits) {
			for (i = 0; i < digits - n.length; i++)
				zero += '0';
		}
		
		return zero + n;
	}
	
	function test(method){
		var url = "/iqms/Test";
		console.log("url==>"+url);
		ajaxCall(url, $("#submit_form1"), method, callback_test);
	}

	var callback_test=function(result){
		console.log("result:",result);
		var jsonString = JSON.stringify(result);
		$("#testResult1").text(jsonString);
	}
	//p_systemgb:WBN0041007
	//p_servicegb:1001 / 1002
	//p_cmid:63
	//p_resultyn:Y
	function cmRequest1(){
		var url = "http://"+iqmsip+":"+iqmsport+"/wbn/Wbn9001I.jsp?systemgb=WBN0041007&p_servicegb=1001&p_cmid=SKI180023&p_resultyn=Y";
		console.log("url==>"+url);
		document.location.href=url;
		//ajaxCall(url, $("#submit_form1"), "GET", callback_test);
	}
	function cmSync1(){
		var url = "http://"+iqmsip+":"+iqmsport+"/wbn/Wbn9001I.jsp?systemgb=WBN0041007&p_servicegb=1002";
		console.log("url==>"+url);
		document.location.href=url;
		//ajaxCall(url, $("#submit_form1"), "GET", callback_test);
	}

	function cmRequest2(){
		var url = "/wbn/Wbn9001I";
		$("#p_servicegb").val("1001");
		console.log("url==>"+url);
		console.log("submit_form2==>"+$("#submit_form2").serialize());
		ajaxCall(url, $("#submit_form2"), "POST", callback_test2);
	}
	function cmSync2(){
		var url = "/iqms/sync/wbn/Wbn9001I";
		$("#p_servicegb").val("1002");
		console.log("url==>"+url);
		ajaxCall(url, $("#submit_form2"), "POST", callback_test2);
	}

	var callback_test2=function(result){
		console.log("result:",result);
		var jsonString = JSON.stringify(result);
		$("#testResult2").text(jsonString);
	}

	function cmCall(status){
		var url = "";
		$("#testResult1").text("");
		
		let resultStr = {};								// 빈 JSON 객체 생성
		
		$("#submit_form1 input").each(function(index){	//input Element 각각에 대해 함수 실행
			let value = $(this).val();					// 현재 input의 value
			let name = $(this).attr('name');			// 현재 input의 name
			
			resultStr[name] = value;					// 현재 input 의 name을 key로, 해당 value 넣기
		});
		
		if(status == "1"){
			url = "/iqms/CmConfirm";
		}else if(status == "3"){
			url = "/iqms/CmComplete";
		}else if(status == "4"){
			url = "/iqms/CmCancel";
		}else if(status == "5"){
			url = "/iqms/CmDelete";
		}else{
			url = "/iqms/CmConfirm";
		} 
		console.log("url==>"+url);
		//ajaxCall(url, $("#submit_form1"), "POST", callback_JsonParamAjaxCall);
		ajaxCallWithJson(url, resultStr, 'POST', callback_JsonParamAjaxCall);
	}

	//callback 함수
	var callback_JsonParamAjaxCall = function(result) {
		$("#testResult1").text(JSON.stringify(result));
	};

	var callback_PerfChkAutoFinishAjaxCall = function(result) {
		if(result.result){
			$("#testResult3").text(result.message);
		}
	};
	var callback_syncAjaxCall = function(result) {
		console.log("callback_syncAjaxCall result:",result);
		if(result.result){
			$("#testResult4").text(result.message);
		}
	};
	var callback_syncAjaxCall2 = function(result) {
		console.log("callback_syncAjaxCall2 result:",result);
		if(result.result){
			$("#testResult5").text(result.message);
		}
	};

	function getFormData($form){
		var unindexed_array = $form.serializeArray();
		var indexed_array = {};

		$.map(unindexed_array, function(n, i){
			indexed_array[n['name']] = n['value'];
		});

		return indexed_array;
	}

</script>
</head>
<body style="padding-left:10px;">
	<div style="font-size:2.5em">IQMS 연동 TEST</div>
	<br/>
	<form id="submit_form1" method="post">
		<table>
<!-- 			<tr> -->
<!-- 				<td>시스템구분</td> -->
<!-- 				<td><input type="text" name="p_systemgb" id="p_systemgb" size="20" value="WBN0041007" /></td> -->
<!-- 			</tr> -->
			<tr>
				<td>회사코드</td>
				<td><input type="text" name="compcd" size="40" value="rF+Kpw650Lg12ccmqaliMFhGdSW54iH9" readonly="true" style="background-color:#ebebe4;" /></td>
			</tr>
			<tr>
				<td>CM번호</td>
				<td>
					<input type="text" name="cmid" value="SKI180010" />
					<input type="hidden" name="p_cmid" id="p_cmid" value="SKI180010" />
					<input type="hidden" name="status" id="status" value="1" />
				</td>
			</tr>
			<tr>
				<td>CM패키지명</td>
				<td><input type="text" name="cmpknm" value="KB 카드 배포 패키지" /></td>
			</tr>
			<tr>
				<td>CM예정년월일(CM배포예정일)</td>
<!-- 				<td><input type="text" name="cmdepday" value="20181010" /></td> -->
				<td><input type="text" id="cmdepday" name="cmdepday" /></td>
			</tr>
			<tr>
				<td>CM담당자번호</td>
				<td><input type="text" name="cmusrnum" value="dev" /></td>
			</tr>
			<tr>
				<td>업무코드(WRKJOB_DIV_CD)</td>
				<td><input type="text" name="jobcd" value="GS" /></td>
			</tr>
			<tr>
				<td>CM생성일시</td>
<!-- 				<td><input type="text" name="cmcreateday" value="20181010101010" /></td> -->
				<td><input type="text" id="cmcreateday" name="cmcreateday" /></td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td>상태</td> -->
<!-- 				<td><input type="text" name="status" id="status" value="Y" /></td> -->
<!-- 			</tr> -->
<!-- 			<tr> -->
<!-- 				<td>패스여부</td> -->
<!-- 				<td><input type="text" name="p_resultyn" id="p_resultyn" value="Y" /></td> -->
<!-- 			</tr> -->
		</table>
		<div style="margin-top:10px;border-top:#ffaa55 5px solid;width:400px;"></div>
		<table>
			<tr>
				<td colspan="2" style="padding-top:10px;">
					<table>
						<colgroup>
							<col style="width:150px"/>
							<col/>
						</colgroup>
						<tr>
							<td>테스트결과 : </td>
							<td><font color="red"><b><span id="testResult1"></span></b></font></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br/>
<!-- 		<input type="button" name="getTest" id="getTest" value="GET Test" /> -->
<!-- 		<input type="button" name="postTest" id="postTest" value="POST Test" /> -->
		<input type="button" name="cmConfirm" id="cmConfirm" value="CM확정" />
 		<input type="button" name="cmComplete" id="cmComplete" value="CM완료(상태값 3)" /> 
 		<input type="button" name="cmCancel" id="cmCancel" value="CM취소(상태값 4)" /> 
 		<input type="button" name="cmDelete" id="cmDelete" value="CM삭제(상태값 5)" /> 
		<br/>
 		<input type="button" name="cmRequest1" id="cmRequest1" value="성능점검 결과 통보1" /> 
 		<input type="button" name="cmSync1" id="cmSync1" value="동기화1" /> 
		<button type="button" id="btn_test">CONFIRM_TEST_WITH_JSON</button>
		<div id="resultDiv1"></div>
	</form>
	<br/><br/>
	<div style="font-size:2.5em">성능점검 결과 통보/동기화</div>
	<form id="submit_form2" method="post">
		<table>
			<tr>
<!-- 				<td>시스템구분</td> -->
				<td><input type="hidden" name="p_systemgb" id="p_systemgb" size="20" value="WBN0041007" /></td>
			</tr>
			<tr>
<!-- 				<td>회사코드</td> -->
				<td><input type="hidden" name="compcd" size="40" value="rF+Kpw650Lg12ccmqaliMFhGdSW54iH9" /></td>
			</tr>
			<tr>
				<td>CM번호</td>
				<td>
<!-- 					<input type="text" name="cmid" value="SKI180023" /> -->
					<input type="text" name="p_cmid" id="p_cmid" value="AB000001_13801" />
<!-- 					<input type="hidden" name="p_cmid" id="p_cmid" value="SKI180023" /> -->
				</td>
			</tr>
			<tr>
<!-- 				<td>서비스구분</td> -->
				<td><input type="hidden" name="p_servicegb" id="p_servicegb" value="1001" /></td>
			</tr>
			<tr>
<!-- 				<td>패스여부</td> -->
				<td><input type="hidden" name="p_resultyn" id="p_resultyn" value="Y" /></td>
			</tr>
		</table>
		<div style="margin-top:10px;border-top:#ffaa55 5px solid;width:400px;"></div>
		<table>
			<tr>
				<td colspan="2" style="padding-top:10px;">
					<table>
						<colgroup>
							<col style="width:150px"/>
							<col/>
						</colgroup>
						<tr>
							<td>테스트결과 : </td>
							<td><font color="red"><b><span id="testResult2"></span></b></font></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<br/>
		<input type="button" name="cmRequest2" id="cmRequest2" value="성능점검 결과 통보" />
<!-- 		<input type="button" name="cmSync2" id="cmSync2" value="동기화" /> -->
		<br/>
<!-- 		<input type="button" name="cmRequest1" id="cmRequest1" value="성능점검 결과 통보1" /> -->
<!-- 		<input type="button" name="cmSync1" id="cmSync1" value="동기화1" /> -->
		<div id="resultDiv2"></div>
	</form>
<!-- 	<br/><br/> -->
<!-- 	<div style="font-size:2.5em">성능점검 자동완료</div> -->
<%-- 	<form id="submit_form3" method="POST" action="/auth/PerfChkAutoFinish"> --%>
<!-- 		<table id="perfChkAutoFinishTbl"> -->
<!-- 		<tr> -->
<!-- 				<td>CM번호(배포ID)</td> -->
<!-- 				<td> -->
<!-- 					<input type="text" id="deploy_id" name="deploy_id" value="AC000001_12702" /> -->
<!-- 				</td> -->
<!-- 		</tr> -->
<!-- 		<tr> -->
<!-- 				<td>성능점검ID</td> -->
<!-- 				<td> -->
<!-- 					<input type="text" id="perf_check_id" name="perf_check_id" value="12702" /> -->
<!-- 				</td> -->
<!-- 		</tr> -->
<!-- 		</table> -->
<!-- 		<br> -->
<!-- 		<input type="button" name="perfChkAutoFinishBtn" id="perfChkAutoFinishBtn" value="성능점검 자동완료" /> -->
<!-- 		<span id="linkspan"></span> -->
<!-- 		<div style="margin-top:10px;border-top:#ffaa55 5px solid;width:400px;"></div> -->
<!-- 		<table> -->
<!-- 			<tr> -->
<!-- 				<td colspan="2" style="padding-top:10px;"> -->
<!-- 					<table> -->
<%-- 						<colgroup> --%>
<%-- 							<col style="width:150px"/> --%>
<%-- 							<col/> --%>
<%-- 						</colgroup> --%>
<!-- 						<tr> -->
<!-- 							<td>테스트결과 : </td> -->
<!-- 							<td><font color="red"><b><span id="testResult3"></span></b></font></td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<%-- 	</form> --%>
	<br/><br/>
<!-- 	<div style="font-size:2.5em">IQMS JDBC TEST1(POOL)</div> -->
<%-- 	<form id="submit_form4" method="POST" action="/searchWorkJobCd"> --%>
<!-- 		<input type="button" name="wrkjobcdSyncBtn" id="wrkjobcdSyncBtn" value="업무코드 동기화" /> -->
<!-- 		<input type="button" name="userSyncBtn" id="userSyncBtn" value="사용자 동기화" /> -->
<!-- 		<input type="button" name="userWrkjobSyncBtn" id="userWrkjobSyncBtn" value="사용자 업무 동기화" /> -->
<!-- 		<input type="button" name="syncAllBtn" id="syncAllBtn" value="전체(업무코드,사용자,사용자 업무) 동기화" /> -->
<!-- 		<span id="linkspan"></span> -->
<!-- 		<div style="margin-top:10px;border-top:#ffaa55 5px solid;width:400px;"></div> -->
<!-- 		<table> -->
<!-- 			<tr> -->
<!-- 				<td colspan="2" style="padding-top:10px;"> -->
<!-- 					<table> -->
<%-- 						<colgroup> --%>
<%-- 							<col style="width:150px"/> --%>
<%-- 							<col/> --%>
<%-- 						</colgroup> --%>
<!-- 						<tr> -->
<!-- 							<td>테스트결과 : </td> -->
<!-- 							<td><font color="red"><b><span id="testResult4"></span></b></font></td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<%-- 	</form> --%>
<!-- 	<div style="font-size:2.5em">IQMS JDBC TEST2(JDBC)</div> -->
<%-- 	<form id="submit_form5" method="POST" action="/searchWorkJobCd"> --%>
<!-- 		<input type="button" name="wrkjobcdSyncBtn2" id="wrkjobcdSyncBtn2" value="업무코드 동기화" /> -->
<!-- 		<input type="button" name="userSyncBtn2" id="userSyncBtn2" value="사용자 동기화" /> -->
<!-- 		<input type="button" name="userWrkjobSyncBtn2" id="userWrkjobSyncBtn2" value="사용자 업무 동기화" /> -->
<!-- 		<input type="button" name="syncAllBtn2" id="syncAllBtn2" value="전체(업무코드,사용자,사용자 업무) 동기화" /> -->
<!-- 		<span id="linkspan"></span> -->
<!-- 		<div style="margin-top:10px;border-top:#ffaa55 5px solid;width:400px;"></div> -->
<!-- 		<table> -->
<!-- 			<tr> -->
<!-- 				<td colspan="2" style="padding-top:10px;"> -->
<!-- 					<table> -->
<%-- 						<colgroup> --%>
<%-- 							<col style="width:150px"/> --%>
<%-- 							<col/> --%>
<%-- 						</colgroup> --%>
<!-- 						<tr> -->
<!-- 							<td>테스트결과 : </td> -->
<!-- 							<td><font color="red"><b><span id="testResult5"></span></b></font></td> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<%-- 	</form> --%>

</body>
</html>

