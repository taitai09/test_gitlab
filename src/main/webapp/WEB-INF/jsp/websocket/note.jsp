<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page session="false" %>
<sec:authorize access="isAuthenticated()">
	<sec:authentication var="loginId" property="principal.username"></sec:authentication>
	<sec:authentication var="users" property="principal.users"></sec:authentication>
	<sec:authentication var="menuAuth" property="principal.menuAuth"></sec:authentication>
	<sec:authentication var="uuid" property="principal.uuid"></sec:authentication>
</sec:authorize>
<%
/***********************************************************
 * 2017.08.10	이원식	최초작성
 **********************************************************/
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>Socket.IO chat</title>
	<meta charset="utf-8" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
    <meta http-equiv="cleartype" content="on" />
    <style>
      * { margin: 0; padding: 0; box-sizing: border-box; }
      body { font: 13px Helvetica, Arial; }
      form { background: #000; padding: 3px; position: fixed; bottom: 0; width: 100%; }
      form select { border: 0; padding: 10px; width: 12%; margin-right: .5%; }
      #mtxt { border: 0; padding: 10px; width: 49%; margin-right: .5%; }
      #btnsubmit { width: 9%; background: rgb(130, 224, 255); border: none; padding: 10px; }
      #messages { list-style-type: none; margin: 0; padding: 0; }
      #messages li { padding: 5px 10px; }
      #messages li:nth-child(odd) { background: #eee; }
    </style>    
    <script type="text/javascript" src="/resources/js/lib/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/js/lib/jquery-migrate-3.1.0.min.js"></script>
<!--     <script type="text/javascript" src="/resources/js/lib/sockjs.js"></script> -->
    <script>
	var webSock;
    $(document).ready(function(){    
    	sock = new SockJS("/Message");
    	sock.onmessage = function(evt) {
    		var msg = JSON.parse(evt.data);
    		
    		alert(msg.send_type);
    		alert(msg.send_gubun);
    		alert(msg.title);
    		alert(msg.message);
    		
    		$('#messages').append($('<li>').text(msg.message));
    	};
    	
    	$("#aform #btnsubmit").on("click",function(){
    		if($("#seluser").val() != ""){
	    		if($("#seluser").val() == "all"){
	    			sendMsg("ALL", "", "", $('#mtxt').val());
	    		}else{
	    			sendMsg("USER", $('#seluser').val(), "", $('#mtxt').val());
	    		}
    		}
    		
    		if($("#selRole").val() != ""){
    			sendMsg("AUTH",$('#selRole').val(),"",$('#mtxt').val());
    		}
    		
    		if($("#selJob").val() != ""){
    			sendMsg("WRKJOB",$('#selJob').val(),"",$('#mtxt').val());
    		}

			$('#mtxt').val('');
			return false;
		});
    });
    
    function sendMsg(strType, strGubun, strTitle, strMsg){
    	var msg = {
			send_type : strType,
			send_gubun : strGubun,
			title : strTitle,
			message : strMsg
		};
    	
    	sock.send(JSON.stringify(msg));
    }
    </script>      
</head>
<body>
    <ul id="messages"></ul>
    <form id="aform" action="">
	   <select id="selJob" name="selJob">
	    <option value="">업무선택</option>
      	<option value="10000">계정계</option>
      	<option value="10001">여신</option>
      	<option value="10002">수신</option>
      	<option value="10003">외환</option>
      	<option value="20000">BIZHUP</option>
      	<option value="20001">마케팅</option>
      	<option value="20002">세일즈</option>
      </select>
	   <select id="selRole" name="selRole">
      	<option value="">롤선택</option>
      	<option value="ROLE_ITMANAGER">IT관리자</option>
      	<option value="ROLE_DBMANAGER">성능관리자</option>
      	<option value="ROLE_TUNER">튜닝담당자</option>
      	<option value="ROLE_DEV">업무개발자</option>
      </select>      
      <select id="seluser" name="seluser">
      	<option value="">사용자선택</option>
      	<option value="all">전체</option>
      	<option value="dbmanager">dbmanager</option>
      	<option value="dev">dev</option>
      	<option value="tuner1">tuner1</option>
      </select>
      <input id="mtxt" autocomplete="off" /><input id="btnsubmit" type="button" value="Send"/>
    </form>
</body>
</html>