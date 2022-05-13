var WindowHeight;
var WindowWidth;
var frameName;

/* Window popup Top 위치값 리턴 */
function getWindowTop(winHeight){
	var Top = (WindowHeight - parseInt(winHeight)) / 2;
	if (Top < 0) Top = 0;
	
	return Top;
}

/* Window popup Left 위치값 리턴 */
function getWindowLeft(winWidth){
	var Left = (WindowWidth - parseInt(winWidth)) / 2; 
	if (Left < 0) Left = 0;
	
	return Left;
}
/* Window popup close */
function Btn_OnClosePopup(winName){
	if(winName == 'userExcelUploadPop'){
		$("#userExcelUpload_form #uploadFile").textbox("setValue","");
	}
	if(winName == 'applicationCodeExcelUploadPop'){
		$("#applicationCodeExcelUpload_form #uploadFile").textbox("setValue","");
	}
	$('#'+winName).window("close");	
}

$(window).resize(function(){
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
});

$(document).ready(function(){
	
	WindowWidth = $(window).width();
	WindowHeight = $(window).height();
	
    window.history.pushState(null, "", window.location.href);        
    window.onpopstate = function() {
        window.history.pushState(null, "", window.location.href);
    };	
	
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

});


