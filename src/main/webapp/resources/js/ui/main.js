var clickMenuGubun;
var menuGubunArry = new Array;
var menuNameArry = new Array;
var WindowHeight;
var WindowWidth;
var frameName;
var tabCount = 1;
var strGb = "";
var vSqlId = "";
var isOpenLink = false;
var iFrameHeight = 0;
var tabTitle="";
var subTabTitle="";
var tabHref="";
var deleteCheckCnt = 0;
var withScrollMenuUrl = "/PreventiveCheck/DetailCheckInfo;/CPUIncreaseUsageDetail;";
var noModifyDocSize = false;	

$(window).resize(function(){
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
});

$(window).on('unload', function(e) {
	deleteCookie("MYID");
});

$(document).ready(function(){
	$("body").css("visibility", "visible");
	saveLogin(loginUserId);
	
	$('#mainTab').tabs({
	    onSelect:function(title){
	        console.log(title+' is selected');
	    }		
	});
	
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
	
    window.history.pushState(null, "", window.location.href);        
    window.onpopstate = function() {
        window.history.pushState(null, "", window.location.href);
    };	
	
	/* dashboard width 자동 변동 처리 */
	$("#mainLayout").layout('panel','west').panel({
		onCollapse:function(){
			resetDashBoardWidth('H');
		}
	});
	
//	$('.dept1').mouseover(function() {
////		$(this).attr("style","color:#df2e02;background-color:#ffffff;");  
//		$(this).attr("style","color:#1f1c19;background-color:#fab232");  //메인 메뉴 마우스 오버시 변동됨.
//	}).mouseout(function() {
//		$(this).attr("style","color:#ffffff");
//	});
	$('.dept1').click(function() {
		$('.dept1').removeClass("active");
		$(this).addClass("active");
	});

	$(".popWin").window({
		modal:true,	
		collapsible:false,
		minimizable:false,
		maximizable:false,
		closable:true,
		closed:true,
		constrain:false,
		zIndex:9999
	});	

	$("#mainTab").tabs({
		plain:true,
		pill:true,
		onBeforeClose: function(title, index){
			/* 탭 모두 삭제했을때 dashboard 선택되게 처리 */
			var count = $('#mainTab .panel').length;
			if((count - 1) == 1){
				$('#mainTab').tabs('select', 0);
			}
			
			/* 탭배열에서 삭제 */
			removeMenuTab(title);
			
			tabCount--;
		},
		onSelect : function(title, index){
			if(index == 0){
				// My Menu
				//setLeftMenu("000","My Menu");
			}else{
				var menuGubun = getMenuGubun(title);
				var menuName = "";
				
				/* 선택된 탭과 현재 메뉴가 다를 경우 메뉴리스트를 다시 불러온다. */
				if(menuGubun != "" && menuGubun != clickMenuGubun){
					if(menuGubun == "000"){ menuName = "My Menu"; }
					else if(menuGubun == "010"){ menuName = "성능 진단"; }
					else if(menuGubun == "020"){ menuName = "성능 분석"; }
					else if(menuGubun == "030"){ menuName = "성능 개선"; }
					else if(menuGubun == "040"){ menuName = "튜닝 KM"; }
					else if(menuGubun == "090"){ menuName = "설정"; }
					
					//setLeftMenu(menuGubun, menuName);
				}
			}
			
			if (deleteCheckCnt != 0 && title == "성능 개선 관리" ) {
				setTimeout(function() {
					document.getElementById("if_110").contentWindow.selectOnSearch();
					deleteCheckCnt = 0;
				},500);
			};
		}
	});
	
	fnReloadDashboard = function(){
		var agent = navigator.userAgent.toLowerCase();
		subTabTitle = "";
		
		if(agent.indexOf('chrome') > -1 || agent.indexOf('sasfari') > -1 ||
				agent.indexOf('edge') > -1) {
			dashboard.location.reload();
		} else {
			dashboard.location.replace(dashboard.location.pathname);
		}
	}
	
	if(deploy_id != ""){
		tabTitle="성능 점검 결과";
		tabHref="/PerformanceCheckResult?deploy_id="+deploy_id+"&menu_nm="+encodeURIComponent(tabTitle);
	}else{
		if(auth_cd == "ROLE_DEV_DEPLOYMANAGER"){
			if( customer == "kbcd" ){
				tabTitle="성능 점검 예외 요청";
				tabHref="/PerformanceCheckIndex/ProPerfExcReq?menu_nm="+encodeURIComponent(tabTitle);
				
			}else {
				tabTitle = '배포전 자동 성능 검증';
				subTabTitle = '예외 요청';
				tabHref="/execSqlPerfCheck?menu_nm="+encodeURIComponent(tabTitle);
			}
		}else if(auth_cd == "ROLE_DEPLOYMANAGER"){
			if( customer == "kbcd" ){
				tabTitle="성능 점검 예외 요청";
				tabHref="/PerformanceCheckIndex/ProPerfExcReq?menu_nm="+encodeURIComponent(tabTitle);
			}
			else {
				tabTitle = '배포전 자동 성능 검증';
				subTabTitle = '예외 요청';
				tabHref="/execSqlPerfCheck?menu_nm="+encodeURIComponent(tabTitle);
			}
			
		}else if(auth_cd == "ROLE_DA"){
			tabTitle="구조 품질검토 작업";
			tabHref="/Mqm/QualityInspectionJob?menu_nm="+encodeURIComponent(tabTitle);
		}else if(auth_cd == "ROLE_DIAGNOSTIC"){
			tabTitle="데이터 수집 현황";
			tabHref="/SolutionProgramMng/DataCollectionStatus?menu_nm="+encodeURIComponent(tabTitle);
		}else{
			tabTitle="<i class='fas fa-chart-pie fa-lg fa-fw'></i> DashBoard";
			tabHref="/Dashboard";
		}
	}
	// 데쉬보드
	$('#mainTab').tabs('add',{
		id:"000",
//		title:"<i class='fas fa-chart-pie fa-lg fa-fw'></i> dashboard <i onclick='fnReloadDashboard()' class='fas fa-redo fa-lg fa-fw cursor-hand'></i>",
//		title:"<i class='fas fa-chart-pie fa-lg fa-fw'></i> DashBoard",
		title:tabTitle,
//		closable:true,
//		href:"/Dashboard",
//		href:tabHref,
	    tools:[{
//	        iconCls:'icon-mini-refresh',
	        iconCls:'fas fa-redo fa-lg fa-fw',
	        handler:function(){
	        	fnReloadDashboard();
	        }
	    }]
		,content: '<iframe id="dashboard" name="dashboard" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+tabHref+'" style="width:100%;height:99%;"></iframe>'
	});
	
	// 데쉬보드 구분배열에 추가
	menuGubunArry.push("000");	
	// 데쉬보드명 배열에 추가
	menuNameArry.push("My Menu");
	
	// My Menu 리스트 조회
	setLeftMenu("000","My Menu");

	/* 상단 작업현황 조회 */
	searchWorkStatusCount();
	/* 상단 메시지 현황 */
	searchMessageCount();
	
	if ( auth_cd != null && auth_cd == "ROLE_DBMANAGER" ) {
		licenceCheck();
	} else if( auth_cd != null && auth_cd == "ROLE_OPENPOPMANAGER" ) {
		noLicenceCheck();
	}
});

function searchWorkStatusCount(){
	/* 상단 작업현황 조회 */
	ajaxCall("/Common/WorkStatusCount",
			null,
			"GET",
			callback_WorkStatusCountAction);
}

//callback 함수
var callback_WorkStatusCountAction = function(result) {	
	$("#requestSpan").html("");
	$("#receiptSpan").html("");
	$("#tuningSpan").html("");
	$("#applySpan").html("");
	$("#cancelSpan").html("");
	
	if(result.result){
		var post = result.object;
		
		$("#requestSpan").html(post.process_1);
		$("#receiptSpan").html(post.process_3);
		$("#tuningSpan").html(post.process_5);
		$("#applySpan").html(post.process_6);
		$("#cancelSpan").html(post.process_4);
	}
};

function goMain(){
	$('#mainTab').tabs('select',0);
	
	//var tab = $('#mainTab').tabs('getSelected');
	//tab.panel('refresh', '/Dashboard');
	//setLeftMenu("000","My Menu");
}

function searchMessageCount(){	
	/* 개인 쪽지건수 조회 */
	ajaxCall("/Common/MessageCount?recv_user_id="+loginUserId,
			null,
			"GET",
			callback_MessageCountAction);
}

//callback 함수
var callback_MessageCountAction = function(result) {	
	if(result.result){
		var post = result.object;
		var recvCnt = "";
		if(post.recv_cnt.length = 1){
//			recvCnt = "0" + post.recv_cnt;
			//0 삭제, 2018-10-22
			recvCnt = post.recv_cnt;
		}else{
			recvCnt = post.recv_cnt;
		}
		
		$("#messageSpan").html(recvCnt);
	}
};

/* Left 메뉴 조회 */
function setLeftMenu(menuGubun, strTitle){
	$('#mainLayout').layout('expand','west');
	$("#leftArea").html("");
	$("#leftImgBar").html("");
	
	if(menuGubun == "000"){ 
		// My Favorite Menu 리스트 조회
		ajaxCall("/Common/MyMenu", null, "GET", callback_MyMenuAction);
	}else{
		// 선택된 메뉴 리스트 조회
		ajaxCall("/Common/Menu?menu_id="+menuGubun,
				null,
				"GET",
				callback_MenuAction);		
	}
	
	// 클릭된 메뉴의 구분값을 저장.
	clickMenuGubun = menuGubun;

	$('#mainLayout').layout('panel','west').panel('setTitle',strTitle);
}

/* Workplace 초기화 */
function resetWorkplace(){
	$.messager.confirm('', "WorkPlace를 초기화 하시겠습니까?", function(r){
		if(r){
			var count = $('#mainTab').tabs('tabs').length;
			for(var i = count; i >= 1; i--){
				$('#mainTab').tabs('close',i);
			}

			// dashboard reload
			$('#mainTab').tabs('select', 0);
			var tab = $('#mainTab').tabs('getSelected');
			$('#mainTab').tabs('update', {
				tab: tab,
				title:tabTitle,
//				closable:true,
//				href:tabHref,
				options: {
					content: '<iframe id="dashboard" name="dashboard" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+tabHref+'" style="width:100%;"></iframe>'
				}
			});
			
			// Mymenu load
//			setLeftMenu("000","My Menu");
		}
	});
}

//callback 함수
var callback_MyMenuAction = function(result) {
	var strImgHtml = "";

	if(result.result != undefined){
		if(result.result){
			var strHtml = "";
			strImgHtml = "<a href=\"javascript:;\" title=\"메뉴로 돌아가기\" class=\"easyui-linkbutton easyui-tooltip linkBtn btnIcon layout-button-right\" style=\"width:90%\" data-options=\"position:'right'\" onclick=\"resetDashBoardWidth('S');\"><i class='btnIcon layout-button-right'></i></a>";
			
			for(var i = 0 ; i < result.object.length ; i++){
				var fMenu = result.object[i];			
				strHtml += "<div class='myMenuTit'>"+fMenu.menu_nm+"</div>";
	
				if(fMenu.childMenu.length > 0){
					strHtml += "<div style='width:100%;'>";
					strHtml += "<ul id='myMenu"+fMenu.menu_id+"' class='myMenu left_nav'>";
					
					for(var j = 0 ; j < fMenu.childMenu.length ; j++){
						var sMenu = fMenu.childMenu[j];
						
						if(sMenu.childMenu.length > 0){
							strHtml += "<li><a class='amenu' href='javascript:;'><i class='menuIcon " + sMenu.menu_image_nm + "' ></i> "+sMenu.menu_nm+"</a>";
							strHtml += "<ul>";
							
							for(var k = 0 ; k < sMenu.childMenu.length ; k++){
								var tMenu = sMenu.childMenu[k];
								strHtml += "<li><a class='amenu' href='javascript:;' onClick='openLink(\"N\", \""+tMenu.menu_id+"\", \""+tMenu.menu_nm+"\", \""+tMenu.menu_url_addr+"\", \"\");'><i class='menuIcon " + tMenu.menu_image_nm + "'></i> "+tMenu.menu_nm+"</a></li>";
								strImgHtml += "<a href=\"javascript:;\" onClick='openLink(\"N\", \""+tMenu.menu_id+"\", \""+tMenu.menu_nm+"\", \""+tMenu.menu_url_addr+"\", \"\");' title=\"<b>"+sMenu.menu_nm+"</b> > "+tMenu.menu_nm+"\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\"><i class='btnIcon " + tMenu.menu_image_nm + "'></i></a>";
							}
							
							strHtml += "</ul></li>";
						}else{
							strHtml += "<li><a class='amenu' href='javascript:;' onClick='openLink(\"N\", \""+sMenu.menu_id+"\", \""+sMenu.menu_nm+"\", \""+sMenu.menu_url_addr+"\", \"\");'><i class='menuIcon " + sMenu.menu_image_nm + "'></i> "+sMenu.menu_nm+"</a></li>";
							strImgHtml += "<a href=\"javascript:;\" onClick='openLink(\"N\", \""+sMenu.menu_id+"\", \""+sMenu.menu_nm+"\", \""+sMenu.menu_url_addr+"\", \"\");' title=\"<b>"+sMenu.menu_nm+"</b>\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\"><i class='btnIcon " + sMenu.menu_image_nm + "'></i></a>";
						}
					}
					
					strHtml += "</ul></div>";
				}
			}
	
			$("#leftArea").html(strHtml);
	
			// 왼쪽 메뉴 Tree	
			$(".myMenu").navgoco({
				caretHtml: '',
				accordion: false,
				openClass: 'open',
				save: true,
				cookie: {
					name: 'leftMenu',
					expires: false,
					path: '/'
				},
				slide: {
					duration: 400,
					easing: 'swing'
				},
				onClickAfter: function(e, submenu) {
					e.preventDefault();
					$(".myMenu").find('li').removeClass('active');
					$(".myMenu").find('li').children("a").find("i").removeClass("menuOverIcon");
					$(".myMenu").find('li').children("a").find("i").addClass("menuIcon");
	
					var li =  $(this).parent();
					var lis = li.parents('li');
					li.addClass('active');
					lis.addClass('active');
					
					li.children("a").find("i").addClass("menuOverIcon");
					lis.children("a").find("i").addClass("menuOverIcon");
				},
			});	
	
			$(".myMenu").navgoco('toggle', true);	
			
			if(deploy_id == ""){
				$('#mainLayout').layout('panel','west').panel('open');
			}else{
				$('#mainLayout').layout('panel','west').panel('close');
			}
			$('#mainLayout').layout('resize');
			
			$('.amenu').mouseover(function() {
				$(this).find("i").removeClass("menuIcon");
				$(this).find("i").addClass("menuOverIcon");
			}).mouseout(function() {
				if(!$(this).closest("li").hasClass("active")){
					$(this).find("i").removeClass("menuOverIcon");
					$(this).find("i").addClass("menuIcon");	
				}
			});
			
			$('.amenu').click(function() {
				$(this).parent().parents('li').children("a").find("i").removeClass("menuIcon");
				$(this).parent().parents('li').children("a").find("i").addClass("menuOverIcon");
	
				$(this).find("i").removeClass("menuIcon");
				$(this).find("i").addClass("menuOverIcon");
			})
		}else{
			$.messager.alert('','My메뉴가 없습니다.<br/>My메뉴를 설정해 주세요.');
			strImgHtml = "<a href=\"javascript:;\" title=\"메뉴로 돌아가기\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\" onclick=\"resetDashBoardWidth('S');\"><i class='btnIcon fas fa-arrow-right fa-lg'></i></a>";
		}
	}else{
		//$.messager.alert('',result.message);
		//alert("세션이 종료되어 로그인화면으로 이동합니다.");
		//$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.");
		//top.location.href="/auth/login";
		$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
			setTimeout(function() {
				top.location.href="/auth/login";
			},1000);	
		});		
	}	
	$("#leftImgBar").html(strImgHtml);
	$(".linkBtn").linkbutton();
	$(".linkBtn").tooltip();	
}

//callback 함수
var callback_MenuAction = function(result) {
	if(result.result != undefined){
		if(result.result){
			var strHtml = "";
			var strImgHtml = "";
			strHtml = "<ul id='leftMenu' class='left_nav'>";
			strImgHtml = "<a href=\"javascript:;\" title=\"메뉴로 돌아가기\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\" onclick=\"resetDashBoardWidth('S');\"><i class='fas fa-arrow-right fa-lg'></i></a>";
			
			for(var i = 0 ; i < result.object.length ; i++){
				var menu = result.object[i];
	
				if(menu.childMenu.length > 0){
					strHtml += "<li><a class='amenu' href='javascript:;'><i class='menuIcon " + menu.menu_image_nm + "'></i> "+menu.menu_nm+"</a>";
					strHtml += "<ul>";
					
					for(var j = 0 ; j < menu.childMenu.length ; j++){
						var subMenu = menu.childMenu[j];
						strHtml += "<li><a class='amenu' href='javascript:;' onClick='openLink(\"N\", \""+subMenu.menu_id+"\", \""+subMenu.menu_nm+"\", \""+subMenu.menu_url_addr+"\", \"\");'><i class='menuIcon " + subMenu.menu_image_nm + "'></i> "+subMenu.menu_nm+"</a></li>";
						strImgHtml += "<a href=\"javascript:;\" onClick='openLink(\"N\", \""+subMenu.menu_id+"\", \""+subMenu.menu_nm+"\", \""+subMenu.menu_url_addr+"\", \"\");' title=\"<b>"+menu.menu_nm+"</b> > "+subMenu.menu_nm+"\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\"><i class='" + subMenu.menu_image_nm + "'></i></a>";
					}
					
					strHtml += "</ul></li>";	
				}else{
					if($.trim(menu.menu_url_addr) != null && $.trim(menu.menu_url_addr) != '') {
						strHtml += "<li><a class='amenu' href='javascript:;' onClick='openLink(\"N\", \""+menu.menu_id+"\", \""+menu.menu_nm+"\", \""+menu.menu_url_addr+"\", \"\");'><i class='menuIcon " + menu.menu_image_nm + "'></i> "+menu.menu_nm+"</a></li>";
						strImgHtml += "<a href=\"javascript:;\" onClick='openLink(\"N\", \""+menu.menu_id+"\", \""+menu.menu_nm+"\", \""+menu.menu_url_addr+"\", \"\");' title=\"<b>"+menu.menu_nm+"</b>\" class=\"easyui-linkbutton easyui-tooltip linkBtn\" style=\"width:90%\" data-options=\"position:'right'\"><i class='" + menu.menu_image_nm + "'></i> </a>";
					}
				}
			}		
			strHtml += "</ul>";
	
			$("#leftArea").html(strHtml);
			$("#leftImgBar").html(strImgHtml);
			
			$(".linkBtn").linkbutton();
			$(".linkBtn").tooltip();
			
			if(deploy_id == ""){
				$('#mainLayout').layout('panel','west').panel('open');
			}else{
				$('#mainLayout').layout('panel','west').panel('close');
			}
			$('#mainLayout').layout('resize');
			
			
			// 왼쪽 메뉴 Tree	
			$("#leftMenu").navgoco({
				caretHtml: '',
				accordion: false,
				openClass: 'open',
				save: true,
				cookie: {
					name: 'leftMenu',
					expires: false,
					path: '/'
				},
				slide: {
					duration: 400,
					easing: 'swing'
				},
				onClickAfter: function(e, submenu) {
					e.preventDefault();
					$('#leftMenu').find('li').removeClass('active');
					$("#leftMenu").find('li').children("a").find("i").removeClass("menuOverIcon");
					$("#leftMenu").find('li').children("a").find("i").addClass("menuIcon");
					
					var li =  $(this).parent();
					var lis = li.parents('li');
					li.addClass('active');
					lis.addClass('active');
					
					li.children("a").find("i").addClass("menuOverIcon");
					lis.children("a").find("i").addClass("menuOverIcon");
				},
			});	
	
			$("#leftMenu").navgoco('toggle', true);
			
			$('.amenu').mouseover(function() {
				$(this).find("i").removeClass("menuIcon");
				$(this).find("i").addClass("menuOverIcon");
			}).mouseout(function() {
				if(!$(this).closest("li").hasClass("active")){
					$(this).find("i").removeClass("menuOverIcon");
					$(this).find("i").addClass("menuIcon");	
				}
			});
			
			$('.amenu').click(function() {
				$(this).parent().parents('li').children("a").find("i").removeClass("menuIcon");
				$(this).parent().parents('li').children("a").find("i").addClass("menuOverIcon");
	
				$(this).find("i").removeClass("menuIcon");
				$(this).find("i").addClass("menuOverIcon");
			})	
		}
	}else{
		//$.messager.alert('',result.message);
		//alert("세션이 종료되어 로그인화면으로 이동합니다.");
		//$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.");
		//top.location.href="/auth/login";
		$.messager.alert('',"세션이 종료되어 로그인화면으로 이동합니다.",'info',function(){
			setTimeout(function() {
				top.location.href="/auth/login";
			},1000);	
		});		
	}
};

/* DashBoard Contents Width 조정 */
function resetDashBoardWidth(showGb){
	if(showGb == "S"){
		$("#mainLayout").layout("expand","west");
	}
}


/* Use Function List : openLink, resizeFrame */
function isWithScrollMenuUrl(target) {
	let flag = false;
	let withScrollMenuUrlSplit = withScrollMenuUrl.split(";");
	let withScrollMenuUrlSplitLen = withScrollMenuUrlSplit.length;
	
	for(let withScrollMenuUrlSplitIndex = 0; withScrollMenuUrlSplitIndex < withScrollMenuUrlSplitLen; withScrollMenuUrlSplitIndex++) {
		let scrollMenuUrl = withScrollMenuUrlSplit[withScrollMenuUrlSplitIndex];
		
		if(scrollMenuUrl.length > 0 && target.indexOf(scrollMenuUrl) >= 0) {
			flag = true;
			break;
		}
	}
	
	return flag;
}

/* 탭 링크 함수 */
function openLink(updateYn, menuId, menuNm, menuUrl, menuParam){
	isOpenLink = true;
	/* 상단 작업현황 조회 */
	searchWorkStatusCount();
	/* 상단 메시지 현황 */
	searchMessageCount();	
	
	/* 파라미터가 존재하는 경우 처리 */
	if(menuParam == ""){
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}else{
			menuParam = "?menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);	
		}		
	}else{
		//menuParam = encodeURIComponentMenuParam(menuParam);
		
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&"+menuParam+"&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}else{
			menuParam = "?"+menuParam+"&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}			
	}
	
	let style = "width:100%;height:780px;";
	
	if(isWithScrollMenuUrl(menuUrl)) {
		style = "width:100%;";
	}
	
	if ($('#mainTab').tabs('exists',menuNm)){
		$('#mainTab').tabs('select',menuNm);
		
		if(updateYn == "Y"){ // 탭의 Content Update
			var tab = $('#mainTab').tabs('getSelected');
			let contentString = '';
			
			if( noModifyDocSize ){
				contentString = '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;"></iframe>';
				noModifyDocSize = false;	// 조건문 실행 후 원상복구
				
			}else {
				contentString = '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;height:99%"></iframe>';
			}
			
			$('#mainTab').tabs('update', {
				tab: tab,
				options: {
					content: contentString
				}
			});
		}
	} else {
		
		/* tab생성을 10개로 제한한다. */
		if(tabCount > 10){
			$.messager.alert('','생성된 탭의 갯수가 많습니다.<br/>작업 완료한 탭을 닫고 다른 탭을 열어주세요.','info');
		}else{
			$('#mainTab').tabs('add',{
				id:menuId,
				title:menuNm,
				closable:true,
				tools:[{
//					iconCls:'icon-mini-refresh',
					iconCls:'fas fa-redo fa-lg fa-fw',
					handler:function(){
						//console.log("menuId===>"+menuId);
						if(document.getElementById('if_'+menuId) != undefined){
							//console.log("document.getElementById('if_"+menuId+"').src:"+document.getElementById('if_'+menuId).src);
						}
						//184 : 성능개선결과 산출물
						if(menuId == '110'||menuId == '114'||menuId == '184'||menuId == '117' || menuId == '300' || menuId == '168' || menuId == '169'){
							//eval('if_'+menuId).document.forms[0].submit();
							document.getElementById('if_'+menuId).src = document.getElementById('if_'+menuId).src;
//			        	}else if(menuId == '212'){
//			        		var action = eval('if_'+menuId).document.forms[0].action;
//			        		action = action.substring(0, action.indexOf("?"));
//			        		console.log("menuid:"+menuId+" action :"+action);
//			        		eval('if_'+menuId).document.forms[0].action = action;
//			        		eval('if_'+menuId).document.forms[0].method = "GET";
//			        		eval('if_'+menuId).document.forms[0].submit();
						}else if(menuId == '801' || menuId == '318') {
							document.getElementById('if_'+menuId).src = document.getElementById('if_'+menuId).src;
//							var action = eval('if_'+menuId).document.forms[0].action;
//							action = action.substring(0, action.indexOf("?"));
//							console.log("menuid:"+menuId+" action :"+action);
//							eval('if_'+menuId).document.forms[0].action = action;
//							eval('if_'+menuId).document.forms[0].method = "POST";
//							eval('if_'+menuId).document.forms[0].submit();
						}else{
							if(menuId.substring(0,3) == '110'){
//		        			if(menuId == '110' || menuId.substring(0,3) == '110'){
								
								var action = eval('if_'+menuId).document.forms[0].action;
								action = action.substring(0, action.indexOf("?"));
								console.log("menuid:"+menuId+" action :"+action);
								eval('if_'+menuId).document.forms[0].action = action;
								eval('if_'+menuId).document.forms[0].method = "POST";
								eval('if_'+menuId).document.forms[0].submit();
							}else{
								var agent = navigator.userAgent.toLowerCase();
								
								if(agent.indexOf('chrome') > -1 || agent.indexOf('sasfari') > -1 ||
										agent.indexOf('edge') > -1) {
									eval('if_'+menuId).location.reload();
								} else {
									eval('if_'+menuId).location = eval('if_'+menuId).location;
									
									eval('if_'+menuId).location.href = eval('if_'+menuId).location.href;
								}
							}
						}
					}
				}],
//				content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;"></iframe>'
//				content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;height:780px;"></iframe>'
				content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="' + style + '"></iframe>'
			});
			
			// 메뉴구분배열에 추가
			menuGubunArry.push(clickMenuGubun);
			
			// 메뉴명 배열에 추가
			menuNameArry.push(menuNm);
			
			tabCount++;
		}
	}
	$(".tabs-p-tool a").css("font-size","10px");
	$(".tabs-p-tool a").css("margin-top","3px");
}

function encodeURIComponentMenuParam(menuParam) {
	let paramSplit = menuParam.split('&');
	let paramSplitLen = paramSplit.length;
	let retParams = '';
	//console.log("params:\n" + params);
	
	for(var index = 0; index < paramSplitLen; index++) {
		let param = paramSplit[index];
		let valueIndex = param.indexOf('=');
		let value = param.substring(valueIndex + 1);
		
		let encodeValue = encodeURIComponent( ( decodeURIComponent(value) ) );
		
		if(index == 0) {
			retParams = param.substring(0, valueIndex) + '=' + encodeValue;
		} else {
			retParams = retParams + '&' + param.substring(0, valueIndex) + '=' + encodeValue;
		}
	}
	
	//console.log("retParams:\n" + retParams);
	return retParams;
}

/* 하위탭 SQL정보 생성 함수 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 * dbId : 파라미터 1
 * sqlId : 파라미터 2
 * planHashValue : 파라미터 3
 */
function createSQLNewTab(menuId, tabId, dbId, sqlId, planHashValue, tabHeight){
	vSqlId = sqlId;
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue;

	makeNewTab(objTab, tabId, tabName, tabUrl, tabHeight);
}

/* 하위탭 VSQL기반 SQL정보 생성 함수 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 * dbId : 파라미터 1
 * sqlId : 파라미터 2
 * planHashValue : 파라미터 3
 */
function createVSQLNewTab(menuId, tabId, dbId, sqlId, planHashValue){
	vSqlId = sqlId;
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue+"&isvsql=Y";
	
	makeNewTab(objTab, tabId, tabName, tabUrl);
}

/* 하위탭 SQL정보 생성 함수 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 * dbId : 파라미터 1
 * sqlId : 파라미터 2
 * planHashValue : 파라미터 3
 */
function createSQLInfoAWRTab(menuId, tabId, dbId, sqlId, planHashValue) {
	vSqlId = sqlId;
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = sqlId + " / " + planHashValue;
	var tabUrl = "/SQLInformation?dbid="+dbId+"&sql_id="+sqlId+"&plan_hash_value="+planHashValue+"&rcount=1";

	makeNewTab(objTab, tabId, tabName, tabUrl);
}

/* 하위탭 적재SQL SQL정보 생성 함수 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 * fileNo : 파라미터 1
 * explainExecSeq : 파라미터 2
 * querySeq : 파라미터 3
 */
function createSQLLoadNewTab(menuId, tabId, fileNo, explainExecSeq, querySeq){
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = fileNo + " / " + explainExecSeq + " / " + querySeq;
	var tabUrl = "/SQLInformation/Load?file_no="+fileNo+"&explain_exec_seq="+explainExecSeq+"&query_seq="+querySeq;
	
	makeNewTab(objTab, tabId, tabName, tabUrl);
}

function goToExceptionTab(menuId, tabId, tabName, menuName, tabUrl){
	if ($('#mainTab').tabs('exists',menuName)){
		$('#mainTab').tabs("select", menuName);
		
	}else {
		openLink("Y", menuId, menuName, "/execSqlPerfCheck", "");
	}
	
	var objTab = eval("if_"+menuId).$('#'+tabId);
	
	if( auth_cd == "ROLE_DEPLOYMANAGER" ){
		objTab = eval("dashboard").$('#'+tabId);
	}
	
	makeNewTab(objTab, tabId, tabName, tabUrl, 680);
}

function createSqlStdChkTab(menuId, menuName, menuUrl, param){
	openLink('Y', menuId, menuName, menuUrl, "");
}

function createMinuteDbStatusNewTab(menuId, menuNm, tabId, dbId, check_day, check_seq, tabTitle,
		severity_color_0, severity_color_1, severity_color_2, severity_color_3, db_status_name, db_status_tabs_severity, user_id) {
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = tabTitle;
	var tabUrl = "/DailyCheckDb/minuteDbStatus?dbid=" + dbId + "&menu_nm=" + menuNm + "&check_day=" + check_day + "&check_seq=" + check_seq +
			"&severity_color_0=" + removeSpecialCharacter(severity_color_0) + "&severity_color_1=" + removeSpecialCharacter(severity_color_1) +
			"&severity_color_2=" + removeSpecialCharacter(severity_color_2) + "&severity_color_3=" + removeSpecialCharacter(severity_color_3) +
			"&db_status_name=" + db_status_name + "&db_status_tabs_severity=" + db_status_tabs_severity+"&user_id="+user_id;
	
	makeNewTab(objTab, tabId, tabName, tabUrl, 690);
}

/* 탭 SqlsDetail 정보 생성 함수 */
/*
 * menuId : 호출된 자식의 구분
 * tabId : 호출된 자식창의 탭구분
 */
function createSqlsDetailNewTab(menuId, tabId, dbid, sql_id, plan_hash_value, begin_dt){
	vSqlId = sql_id;
	var objTab = eval("if_"+menuId).$('#'+tabId);
	var tabName = sql_id;
	var tabUrl = "/PerformanceCheckSqlDesign/sqlsDetail?dbid="+dbid+"&sql_id="+sql_id+"&plan_hash_value="+plan_hash_value+"&begin_dt="+begin_dt;
	
	makeNewTab(objTab, tabId, tabName, tabUrl, 690);
}

function removeSpecialCharacter(tmp) {
	var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-+<>@\#$%&\\\=\(\'\"]/gi;
	 
	if(regExp.test(tmp)){
	var t = tmp.replace(regExp, "");
	tmp = t;
	}
	
	return tmp;
}

/* 탭 생성 함수 */
function makeNewTab(objTab, tabId, tabName, tabUrl, tabHeight){
	let content;
	
	if (objTab.tabs('exists',tabName)){
		
		if(tabHeight != null && tabHeight > 0) {
			content = '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;height:' + tabHeight + 'px;"></iframe>';
		} else {
			content = '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>';
		}
		
		objTab.tabs('select',tabName);
		var tab = objTab.tabs('getSelected');
		objTab.tabs('update', {
			tab: tab,
			options: {
//				content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>'
				content: content
			}
		});
	} else {
		if(tabHeight != null && tabHeight > 0) {
			content = '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;height:' + tabHeight + 'px;"></iframe>';
		} else {
			content = '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>';
		}
		
		objTab.tabs('add',{
			id:vSqlId,
			title:tabName,
			closable:true,
//			content: '<iframe id="sub_'+tabName+'" name="sub_'+tabName+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="top.resizeFrame(this);" src="'+tabUrl+'" style="width:100%;"></iframe>'
			content: content
		});
	}	
}

/* 메뉴명으로 메뉴구분 조회 */
function getMenuGubun(menuName){
	var menuGubun = "";
	
	for(var i = 0 ; i < menuNameArry.length ; i++){
		if(menuName == menuNameArry[i]){
			menuGubun = menuGubunArry[i];
			break;
		}
	}
	
	return menuGubun; 
}

/* 메뉴탭 삭제할 경우 배열에서 삭제 처리 */
function removeMenuTab(menuName){
	var menuIndex;
	
	for(var i = 0 ; i < menuNameArry.length ; i++){
		if(menuName == menuNameArry[i]){
			menuIndex = i;
			break;
		}
	}	
	
	// 메뉴구분배열에서 삭제
	menuGubunArry.splice(menuIndex, 1);
	
	// 메뉴명 배열에사 삭제
	menuNameArry.splice(menuIndex, 1);
}

/* 상단 상태값 클릭시 링크 - 신규 탭 생성 */
function Btn_goImprovementStatus(strGubun, strCd, isClick){
	clickMenuGubun = "040";
	var menuId = "110";
	var menuNm = "성능 개선 관리";
	var menuUrl = "/ImprovementManagement";
	var menuParam = "day_gubun="+strGubun+"&tuning_status_cd="+strCd+"&headerBtnIsClicked="+isClick;
	
	/* 탭 생성 */
	openLink("Y", menuId, menuNm, menuUrl, menuParam);
	
	//setLeftMenu(clickMenuGubun, menuNm);
}

/* 도움말 modal창 열기*/
function openGuide(){
	window.open("/resources/document/manual.html");
}
/*리뉴얼 도움말 창 열기*/
function newGuideOpen(){
	window.open("/resources/document/manual_renewal.html");
}
/* 상단 공통메뉴 클릭시 링크 - 신규 탭 생성 */
function Btn_goCommonLink(strGubun, strParam){
	var menuId = "";
	var menuNm = "";
	var menuUrl = "";
	var menuParam = "";
	
	if(strGubun == "message"){
		menuId = "800";
		menuNm = "쪽지함 관리";
		menuUrl = "/Message/List";
	}else if(strGubun == "notice"){
		menuId = "801";
		if ( strParam != '2' ) {
			menuNm = "공지사항";
		} else {
			menuNm = "DBA게시판";
		}
		menuUrl = "/Board/List";
		menuParam = "board_mgmt_no="+strParam;
	}else if(strGubun == "schedule"){
		menuId = "802";
		menuNm = "일정 관리";
		menuUrl = "/Schedule/List";
	}else if(strGubun == "dashboard"){
		menuId = "803";
		menuNm = "ToDo Dashboard";
		menuUrl = "/Dashboard/dbmanager";
	}else if(strGubun == "dashboard2"){
		menuId = "804";
		menuNm = "Dashboard";
		menuUrl = "/Dashboard/dashboard";
	}else if(strGubun == "mypage"){
		menuId = "805";
		menuNm = "사용자 정보 변경";
		menuUrl = "/auth/UserInfo";
	}else if(strGubun == "defaultLoginRole"){
		menuId = "defaultLoginRole";
		menuNm = " 로그인 권한 변경";
		menuUrl = "/auth/defaultLoginRole";
	} else if ( strGubun == "precedentNew") {
		menuId = "precedentNew";
		menuNm = "SQL 튜닝 사례/가이드";
		menuUrl = "/PrecedentNew";
	}
	
	/* 탭 생성 */
	openLink("N", menuId, menuNm, menuUrl, menuParam);
}

function Btn_goNoticeLink( strGubun, strParam, strNo ) {
	var menuId = "";
	var menuNm = "";
	var menuUrl = "";
	var menuParam = "";
	
	if ( strGubun == "notice" ) {
		menuId = "801";
		if ( strParam != '2' ) {
			menuNm = "공지사항";
		} else {
			menuNm = "DBA게시판";
		}
		menuUrl = "/Board/View";
		menuParam = "board_mgmt_no="+strParam+"&board_no="+strNo;
	}
	
	openLink("N", menuId, menuNm, menuUrl, menuParam);
}

function resizeFrame(iframeObj){
	if(iframeObj == null){
		return false;
	}
	
	try {
		iFrameHeight = $(iframeObj).get(0).contentWindow.top.WindowHeight;
		
		if(isWithScrollMenuUrl(iframeObj.src)) {
			iFrameHeight = iframeObj.contentWindow.document.body.scrollHeight;
		}
	}catch(err) {
		iFrameHeight = $(iframeObj).get(0).contentWindow.top.WindowHeight;
	}
	(iframeObj).height = iFrameHeight-10;
}

/* 탭 클릭시 자식창 container 사이즈 계산 */
function resizeTopFrame(menuId, dynheight){
	if(document.getElementById("if_"+menuId) != null){
		document.getElementById("if_"+menuId).height = 0;
		document.getElementById("if_"+menuId).height = parseInt(dynheight);
	}
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

var winTypeB;

/* 데이터 로딩 modal 처리 */
function openMessageProgressTypeB(title, msgTitle, interval){
	var strText = "데이터를 불러오는 중입니다.";
	
	winTypeB = $.messager.progress({
		title:title,
		msg:msgTitle,
		interval:interval
	});
}

function updateMessageProgressTypeB(percentage, text){
	$.messager.progress('bar').progressbar('setValue', percentage);
	
	if(text) {
		winTypeB.find('.messager-p-msg').html(text);
	}
}

/* 데이터 로딩 modal close */
function closeMessageProgressTypeB(){
	$.messager.progress('close');
	if(winTypeB) {
		winTypeB = null;
	}
}

/* Window popup Left 위치값 리턴 */
function getWindowLeft(winWidth){
	var Left = (WindowWidth - parseInt(winWidth)) / 2; 
	if (Left < 0) Left = 0;
	
	return Left;
}

/* Window popup Top 위치값 리턴 */
function getWindowTop(winHeight){
	var Top = (WindowHeight - parseInt(winHeight)) / 2;
	if (Top < 0) Top = 0;
	
	return Top;
}

/* Window popup close */
function Btn_OnClosePopup(winName){
	$('#'+winName).window("close");	
}

/* textarea 중 에디터 기능을 사용하는 경우 html 제거 함수 */
function formatHTML(strText){
	var returnValue = "";
	
	returnValue = strText;
	
	//앞뒤공백추가제거 2020-02-12
	returnValue = returnValue.replace(/^\s*/, "");
	returnValue = returnValue.replace(/\s*$/, "");
	
	returnValue = returnValue.replace(/\s/ig," ");
	returnValue = returnValue.replace(/&nbsp;/ig," ");
	returnValue = returnValue.replace(/&#39;/ig,"'");
	returnValue = returnValue.replace(/<>/ig,"@@");
	returnValue = returnValue.replace(/(<br>)|(<br\/>)|(<br \/>)/ig,"\n");
	returnValue = returnValue.replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig,"");
	returnValue = returnValue.replace(/@@/ig,"<>");
	returnValue = returnValue.replace(/&lt;/ig,"<");
	returnValue = returnValue.replace(/&gt;/ig,">");
	returnValue = returnValue.replace(/&quot;/ig,'"');
	returnValue = returnValue.replace(/&amp;/ig,'&');
	
	return returnValue;
}

function fnMyMenuSetting(user_id){
	fnMyMenuSetting1("Y","001","My Menu 설정","/Common/MyMenuSetting","")
}

function fnMyMenuSetting1(updateYn, menuId, menuNm, menuUrl, menuParam){
	isOpenLink = true;
	/* 파라미터가 존재하는 경우 처리 */
	if(menuParam == ""){
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}else{
			menuParam = "?menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);	
		}		
	}else{
		if(menuUrl.indexOf("?") > -1){
			menuParam = "&"+menuParam+"&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}else{
			menuParam = "?"+menuParam+"&menu_id="+menuId+"&menu_nm="+encodeURIComponent(menuNm);
		}			
	}

	if ($('#mainTab').tabs('exists',menuNm)){
		$('#mainTab').tabs('select',menuNm);
		
		if(updateYn == "Y"){ // 탭의 Content Update
			var tab = $('#mainTab').tabs('getSelected');
			$('#mainTab').tabs('update', {
				tab: tab,
				options: {
					content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;"></iframe>'
				}
			});
		}
	} else {
		
		/* tab생성을 10개로 제한한다. */
		if(tabCount > 10){
			$.messager.alert('','생성된 탭의 갯수가 많습니다.<br/>작업 완료한 탭을 닫고 다른 탭을 열어주세요.','info');
		}else{
			$('#mainTab').tabs('add',{
				id:menuId,
				title:menuNm,
				closable:true,
			    tools:[{
//			        iconCls:'icon-mini-refresh',
			        iconCls:'fas fa-redo fa-lg fa-fw',
			        handler:function(){
			    		eval('if_'+menuId).location.reload();
			        }
			    }],				
				content: '<iframe id="if_'+menuId+'" name="if_'+menuId+'" frameborder="0" framespacing="0" marginheight="0" marginwidth="0" scrolling="no" vspace="0" onLoad="resizeFrame(this);" src="'+menuUrl+menuParam+'" style="width:100%;"></iframe>'
			});
			
			// 메뉴구분배열에 추가
			menuGubunArry.push(clickMenuGubun);
			
			// 메뉴명 배열에 추가
			menuNameArry.push(menuNm);
			
			tabCount++;
		}
	}	
}

function createSelectRoleTab(){
	var menuId = "";
	var menuNm = "";
	var menuUrl = "";
	var menuParam = "";
	
	menuId = "select_role_tab";
	menuNm = "기본 권한 설정";
	menuUrl = "/Message/List";
	
	/* 탭 생성 */
	openLink("N", menuId, menuNm, menuUrl, menuParam);	
}


function warningMessager(message) {
	$.messager.alert('경고',message,'warning');
}

function infoMessager(message) {
	$.messager.alert('정보',message,'info');
}

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

function saveWatchLoginUsers(cnt){
	
		ajaxCall("/auth/SaveWatchLoginUsers",$("#detail_form"),"POST",callback_SaveWatchLoginUsersAction);
}

//callback 함수
var callback_SaveWatchLoginUsersAction = function(result) {
	//$.messager.alert('','관리자가 승인 처리중입니다.<br/>관리자의 승인 후 로그인해 주세요.','info');
	
	if(result.result){
		console.log("RESULT : 로그인실패 / 로그저장");
	}else{
		console.log("RESULT : 로그인실패 / 로그저장실패");
	}
}

function licenceCheck(){
	/* 상단 작업현황 조회 */
		ajaxCall("/licence/Check",
				null,
				"GET",
				callback_LicenceCheckAction);
}

function noLicenceCheck(){
	/* 상단 작업현황 조회 */
		ajaxCall("/licence/Check",
				null,
				"GET",
				callback_NoLicenceCheckAction);
}

var callback_LicenceCheckAction = function( result ) {
	var data = JSON.parse(result);
	
	if ( data != null && data.rows.length > 0
			&& data.rows[0].over_licence_set_cnt > 0 && data.rows[0].unreg_licence_cpu_cnt == 0	// 초과cpu수 > 0 && 계약cpu수가 0이 아닌 수 == 0
			&& ( (data.rows[0].rcount == 0 && data.rows[0].popup_licence_cpu_cnt == 0 )		// && ( (LICENCE_OVER_USER_CHECK 에 등록된 수 == 0 && 라이선스 7일 초과 된 수 == 0 )
			|| data.rows[0].popup_licence_cpu_cnt > 0 										// || 라이선스 7일 초과 된 수 > 0
			|| ( data.rows[0].popup_licence_cpu_cnt == 0 && data.rows[0].rnum > 0 ))) {		// || ( 라이선스 7일 초과 된 수 == 0 && 새로 추가된 Licence 수 > 0) )
		$('#licenceCheckPop').window("open");
	}
}

var callback_NoLicenceCheckAction = function( result ) {
	var data = JSON.parse(result);
	
	if ( data != null && data.rows.length > 0 ) {
		// View3
		if ( data.rows[0].unreg_licence_cpu_cnt > 0 ) {
			$('#noLicenceCheckPop').window("open");
		}
		// View2
		if ( data.rows[0].reg_licence_cpu_cnt == 0 ) {
			$.messager.alert('Licence','라이선스가 등록되지 않았습니다. <br/>확인 후 등록 바랍니다.','warning');
		}
		// View1
		if ( data.rows[0].over_licence_set_cnt > 0 && data.rows[0].unreg_licence_cpu_cnt == 0	// 초과cpu수 > 0 && 계약cpu수가 0이 아닌 수 == 0
				&& ( (data.rows[0].rcount == 0 && data.rows[0].popup_licence_cpu_cnt == 0 )		// && ( (LICENCE_OVER_USER_CHECK 에 등록된 수 == 0 && 라이선스 7일 초과 된 수 == 0 )
				|| data.rows[0].popup_licence_cpu_cnt > 0 										// || 라이선스 7일 초과 된 수 > 0
				|| ( data.rows[0].popup_licence_cpu_cnt == 0 && data.rows[0].rnum > 0 ))) {		// || ( 라이선스 7일 초과 된 수 == 0 && 새로 추가된 Licence 수 > 0) )
			$('#licenceCheckPop').window("open");
		}
	}
	
}