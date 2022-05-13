$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// Database 조회
	$('#selectCombo').combobox({
		url:"/Common/getDatabase?isChoice=Y",
		method:"get",
		valueField:'dbid',
		textField:'db_name',
		onLoadError: function(){
			parent.$.messager.alert('','DB 조회중 오류가 발생하였습니다.');
			return false;
		},
		onLoadSuccess: function() {
			$('#selectCombo').combobox('textbox').attr("placeholder","선택");
		},
		onSelect:function(rec){
			$('#dbid').val(rec.dbid);
			$('#exadata_yn').val(rec.exadata_yn);
		}
	});
	
	$('#base_weekly').radiobutton({
		onChange:function(val){
			if(val == true){
				$("base_period_value").val();
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
	
//	$('#box2').checkbox({
//		onChange: function(checked) {
//			if(checked) {
//				$('ul.nested a.level3').css('display', '');
//				$('ul.nested a.level3 span').css('display', 'none');
//			} else {
//				$('ul.nested a.level3').css('display', 'none');
//			}
//		}
//	});
});

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
}

var callback_loadAction = function(result) {
	try {
		console.log(result);
		if(result){
//			parent.$.messager.alert('','정상적으로 등록되었습니다','info',function(){
//				setTimeout(function() {
//					Btn_goList();
//				},1000);
//			});
			$('#reportView').html(result);
		}else{
			parent.$.messager.alert('',result.message);
		}
	} catch(ex) {
		console.error(ex.message);
	} finally {
		/* modal progress close */
		if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	}
};

function Btn_OnClick() {
	if($("#dbid").val() == '') {
		parent.$.messager.alert('경고','DB를 선택해 주세요.','warning');
		return false;
	}
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val(), "보고서 작성 중입니다.");
	
//	$('#submit_form #strStartDt').val($('#startDate').val().replace(/-/gi, ''));
	$('#submit_form #strEndDt').val($('#nowDate').val());
	
//	var settingDate = new Date($('#nowDate').val().substring(0, 4), $('#nowDate').val().substring(5, 7), $('#nowDate').val().substring(8, 10))
//	settingDate.setMonth(settingDate.getMonth()-1); //한달 전
//	$("#startDate").val(settingDate.asString());
//	console.log("settingDate[" + settingDate.asString() + "]");
	
	ajaxCall("/DiagnosisReport/view",
			$("#submit_form"),
			"POST",
			callback_loadAction);
}

function Btn_OnDownload() {
	if($("#dbid").val() == '') {
		parent.$.messager.alert('경고','DB를 선택해 주세요.','warning');
		return false;
	}
	
	if($('#reportView').html().length == 0) {
		parent.$.messager.alert('경고','진단 실행을 진행해 주세요.','warning');
		return false;
	}
	
	$("#submit_form").attr("action","/DiagnosisReport/download");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
}

function Excel_Download(slt_program_sql_number) {
	console.log("push Excel ["+ slt_program_sql_number + "]");
}

function Btn_OnClickWord() {
	/* modal progress open */
//	if(parent.openMessageProgress != undefined) parent.openMessageProgress($('#menu_nm').val(), "보고서 작성 중입니다.");
	
//	$('#submit_form #strStartDt').val($('#startDate').val().replace(/-/gi, ''));
	$('#submit_form #strEndDt').val($('#nowDate').val());
	
	ajaxCall("/DiagnosisReport/word",
			$("#submit_form"),
			"POST",
			callback_loadAction);
}

function Btn_OnClickPDF() {
	  //pdf_wrap을 canvas객체로 변환
//	  html2canvas($('#section')[0]).then(function(canvas) {
//	    var doc = new jsPDF('p', 'mm', 'a4'); //jspdf객체 생성
//	    var imgData = canvas.toDataURL('image/png'); //캔버스를 이미지로 변환
//	    doc.addImage(imgData, 'PNG', 0, 0); //이미지를 기반으로 pdf생성
//	    doc.save('sample-file.pdf'); //pdf저장
//	  });
	
	
//	html2canvas($('#section')[0]).then(function(canvas) {
	
	$my_view = $('#section');
	var useHeight = $('#my-view').prop('scrollHeight');
	
	html2canvas($my_view[0]).then(function(canvas) {
		var pdf = new jsPDF('landscape', 'mm', 'a4'); //jspdf객체 생성-가로
		var imgData = canvas.toDataURL('image/png'); //캔버스를 이미지로 변환
//		alert(typeof pdf.processPNG);
//		alert(canvas.height +'-' + canvas.width);
		pdf.addImage(imgData, 'PNG', 0, 0); //이미지를 기반으로 pdf생성
		pdf.save('sample-file.pdf'); //pdf저장
	});
	
//	html2canvas(document.body, {
//		onrendered: function(canvas) {
//		    var pdf = new jsPDF('landscape', 'mm', 'a4');
//		    var marginLeft=20;
//		    var marginRight=20
//		    pdf.addImage(canvas.toDataURL("image/jpeg"),"jpeg",marginLeft,marginRight)
////		    window.location=pdf.output("datauristring")
//		    pdf.save('sample-file.pdf'); //pdf저장
//		}
//	});
};

function Btn_OnClickPDF2() {
//	$('.element').css('position','initial'); // Change absolute to initial
	$my_view = $('#section');
	var useHeight = $('#section').prop('scrollHeight');
	html2canvas($my_view[0], {
//		height: useHeight,
//		useCORS: true,
//		allowTaint: true,
//		proxy: "your proxy url",
		onrendered: function (canvas) {
//			var imgSrc = canvas.toDataURL();
//			var popup = window.open(imgSrc);
//			$('.element').css('position','absolute');
			
			var data = canvas.toDataURL('image/png');
			var file = dataURLtoBlob(data);
			
			var formObjects = new FormData();
			formObjects.append('file', file);
			
			$.ajax({
				url: 'ajax_preview',
				type: 'POST',
				data: formObjects,
				processData: false,
				contentType: false,
			}).done(function(response){
				console.log(response);
				//window.open(response, '_blank');  
			});
		}
	});
}

//function Btn_OnClickPDF2() {
//	var element = $('#section');
//	html2pdf(element);
//};

function Btn_OnClickPDF3() {
	html2canvas($('#section')[0], {
		backgroundColor: "#FFFFFF"
	}).then(function(canvas) {
//		var base64image = canvas.toDataURL("image/png");
//		window.open(base64image, "_blank");
		
		var img = canvas.toDataURL();
		console.log(img);
		var iframe = '<iframe src="' + img + '" frameborder="0" style="border:0; top:0px; left:0px; bottom:0px; right:0px; width:100%; height:100%;" allowfullscreen></iframe>';
		var x = window.open();
		x.document.open();
		x.document.write(iframe);
		x.document.close();
		
		var pdf = new jsPDF('landscape', 'mm', 'a4'); //jspdf객체 생성-가로
		pdf.addImage(img, 'PNG', 0, 0); //이미지를 기반으로 pdf생성
//		pdf.save('sample-file.pdf'); //pdf저장
		
		var data = pdf.output();
		var buffer = new ArrayBuffer(data.length);
		var array = new Uint8Array(buffer);
		for (var i = 0; i < data.length; i++) {
			array[i] = data.charCodeAt(i);
		}

		var blob = new Blob(
				[array],
				{type: 'application/pdf', encoding: 'raw'}
		);
		saveAs(blob, "test.pdf");
	});
}

function Btn_OnClickPDF4() {
	var partsec = $("section");
	var scrollHeight = $('#section').prop('scrollHeight');
	var height = $('#section').height();
	var width = $('#section').width();
	html2canvas($("section")[0],
			{
		logging: true,
		profile: true,
		allowTaint: true,
		letterRendering: true,
		onrendered: function(canvas) {
			var sectionHeight = height;
			var sectionWidth = width;
			
			var doc = new jsPDF();
			var image = new Image();
			var imageData = canvas.toDataURL("image/jpeg");
			image = Canvas2Image.convertToJPEG(canvas);
			doc.addImage(imageData,'JPEG', -115, 5, 440, 875);
			
			doc.addPage();
			
			var canvas1 = document.createElement('canvas');
			canvas1.setAttribute('height', sectionHeight);
			canvas1.setAttribute('width', sectionWidth);
			var ctx = canvas1.getContext("2d");
			ctx.drawImage(image, 0, 1025, sectionWidth, 1250, 0, 0, 1800, 950);
			var image2 = new Image();
			image2 = Canvas2Image.convertToJPEG(canvas1);
			image2Data = image2.src;
			doc.addImage(image2Data, 'JPEG', -105, 5, 440, 325);
			
			doc.addPage();
			var canvas2 = document.createElement('canvas');
			canvas2.setAttribute('height', sectionHeight);
			canvas2.setAttribute('width', sectionWidth);
			var ctx1 = canvas2.getContext("2d");
			ctx1.drawImage(image, 0, 2050, sectionWidth, 1250, 0, 0, 1800, 1000);
			var image3 = new Image();
			image3 = Canvas2Image.convertToJPEG(canvas2);
			image2Data = image3.src;
			doc.addImage(image2Data, 'JPEG', -105, 5, 440, 325);
			
			var data = doc.output();
			var buffer = new ArrayBuffer(data.length);
			var array = new Uint8Array(buffer);
			for (var i = 0; i < data.length; i++) {
				array[i] = data.charCodeAt(i);
			}
			
			var blob = new Blob(
					[array],
					{type: 'application/pdf', encoding: 'raw'}
			);
			saveAs(blob, "test.pdf");
		}
	});
}

function Btn_OnClickPDF5() {
	var scrollHeight = $('#section').prop('scrollHeight');
	var height = $('#section').height();
	var width = $('#section').width();
	
	html2canvas($('#section')[0], {
		backgroundColor: "#FFFFFF"
	}).then(function(canvas) {
////		var img = canvas.toDataURL();
//		var imageData = canvas.toDataURL("image/jpeg");
//		var img = Canvas2Image.convertToJPEG(canvas);
////		downloadURI(img, "down.png");
//		downloadURI(img, "down.pdf");
		
//		link2(canvas, scrollHeight, width);
		link2(canvas, height, width);
	});
}

function downloadURI(uri, name) {
	var link = document.createElement("a");
	
	link.download = name;
	link.href = uri;
	document.body.appendChild(link);
	link.click();
}

function link(canvas, height, width) {
	var sectionHeight = height;
	var sectionWidth = width;
	
//	var doc = new jsPDF();
//	var doc = new jsPDF('landscape', 'mm', 'a4');
	var doc = new jsPDF('landscape', 'px', 'a4');
	var image = new Image();
	var imageData = canvas.toDataURL("image/jpeg");
	image = Canvas2Image.convertToJPEG(canvas);
//	pdf.addImage(imgData, 'PNG', 0, 0); //이미지를 기반으로 pdf생성
//	doc.addImage(imageData,'JPEG', -115, 5, 440, 875);
//	doc.addImage(imageData,'JPEG', 0, 0, 500, 300);
	doc.addImage(imageData,'JPEG', 0, 0);
	
	doc.addPage();
	
	var canvas1 = document.createElement('canvas');
	canvas1.setAttribute('height', sectionHeight);
	canvas1.setAttribute('width', sectionWidth);
	var ctx = canvas1.getContext("2d");
//	ctx.drawImage(image, 0, 1025, sectionWidth, 1250, 0, 0, 1800, 950);
	ctx.drawImage(image, 0, 0, sectionWidth, 1250, 0, 0, 1800, 950);
	var image2 = new Image();
//	image2 = Canvas2Image.convertToJPEG(canvas1);
	image2 = Canvas2Image.convertToJPEG(canvas);
	image2Data = image2.src;
//	doc.addImage(image2Data, 'JPEG', -105, 5, 440, 325);
	doc.addImage(imageData, 'JPEG', 0, -400);
	
	doc.addPage();
	var canvas2 = document.createElement('canvas');
	canvas2.setAttribute('height', sectionHeight);
	canvas2.setAttribute('width', sectionWidth);
	var ctx1 = canvas2.getContext("2d");
	ctx1.drawImage(image, 0, 2050, sectionWidth, 1250, 0, 0, 1800, 1000);
	var image3 = new Image();
	image3 = Canvas2Image.convertToJPEG(canvas2);
	image2Data = image3.src;
//	doc.addImage(image2Data, 'JPEG', -105, 5, 440, 325);
	doc.addImage(imageData, 'JPEG', 0, -800);
	
	var data = doc.output();
	var buffer = new ArrayBuffer(data.length);
	var array = new Uint8Array(buffer);
	for (var i = 0; i < data.length; i++) {
		array[i] = data.charCodeAt(i);
	}
	
	var blob = new Blob(
			[array],
			{type: 'application/pdf', encoding: 'raw'}
	);
	saveAs(blob, "test.pdf");
}

function link2(canvas, height, width) {
	var sectionHeight = height;
	var sectionWidth = width;
	
	var doc = new jsPDF('l', 'px', 'a4');
	
	var escapeFlag = true;
	var currentHeight = 0;
	
	var image = new Image();
	var imageData = canvas.toDataURL("image/jpeg");
	image = Canvas2Image.convertToJPEG(canvas);
//	doc.addImage(imageData,'JPEG', 0, 0);
	doc.addImage(image.src,'JPEG', 0, 0);
	var cnt = 0;
	
	while(parseInt(sectionHeight / currentHeight) != -0) {
		console.log(++cnt);
		currentHeight -= 445;
		
		doc.addPage();
//		doc.addImage(imageData, 'JPEG', 0, currentHeight);
		doc.addImage(image.src, 'JPEG', 0, currentHeight);
	}
	
	var data = doc.output();
	var buffer = new ArrayBuffer(data.length);
	var array = new Uint8Array(buffer);
	for (var i = 0; i < data.length; i++) {
		array[i] = data.charCodeAt(i);
	}
	
	var blob = new Blob(
			[array],
			{type: 'application/pdf', encoding: 'raw'}
	);
	saveAs(blob, "test.pdf");
}

function exportHTML(){
	var html = "<div id=\"source-html\">" +
    "<h1>" +
        "<center>Artificial Intelligence</center>" +
    "</h1>" +
    "<h2>Overview</h2>" +
    "<p>" +
        "Artificial Intelligence(AI) is an emerging technology" +
        "demonstrating machine intelligence. The sub studies like <u><i>Neural" +
                "Networks</i>, <i>Robatics</i> or <i>Machine Learning</i></u> are" +
        "the parts of AI. This technology is expected to be a prime part" +
        "of the real world in all levels." +

    "</p>" +
//"</div>" +
//"<div class=\"content-footer\">" +
//    "<button id=\"btn-export\" onclick=\"exportHTML();\">Export to" +
//        "word doc</button>" +
"</div>;"
	var header = "<html xmlns:o='urn:schemas-microsoft-com:office:office' "+
			"xmlns:w='urn:schemas-microsoft-com:office:word' "+
			"xmlns='http://www.w3.org/TR/REC-html40'>"+
			"<head><meta charset='utf-8'><title>Export HTML to Word Document with JavaScript</title>" +
//			css +
//			lib +
			"</head><body>";
	var footer = "</body></html>";
	
//	var sourceHTML = header+document.getElementById("section").innerHTML+footer;
//	var sourceHTML = header+document.getElementById("section")+footer;
//	var sourceHTML = header+html+footer;
	var sourceHTML = header+$('#section').html()+footer;
	
	var source = 'data:application/vnd.ms-word;charset=utf-8,' + encodeURIComponent(sourceHTML);
	var fileDownload = document.createElement("a");
	document.body.appendChild(fileDownload);
	fileDownload.href = source;
	fileDownload.download = 'document.doc';
	fileDownload.click();
	document.body.removeChild(fileDownload);
}

function callFunction(contents_id) {
	console.log(contents_id);
	
//	$(location).attr('href', '#' + contents_id);
	location.href = "#" + contents_id;
//	location.href = "#tab-1 #" + contents_id;
}

/*
function callFunction2(contents_id) {
	var id = Number(contents_id.substring(1));
	
//	if(contents_id == 'P010') {
//		$('ul.tabbed li').removeClass('current');
//		$('.tab-content').removeClass('current');
//		$("#li-tab-8").addClass('current');
//		$("#tab-8").addClass('current');
//		$(location).attr('href', '#' + contents_id);
//	} else if(contents_id == 'P020') {
//		$('ul.tabbed li').removeClass('current');
//		$('.tab-content').removeClass('current');
//		$("#li-tab-2").addClass('current');
//		$("#tab-2").addClass('current');
//		$(location).attr('href', '#' + contents_id);
//	} else {
//		console.log("else contents_id:" + contents_id);
//	}
	
	$('ul.tabbed li').removeClass('current');
	$('.tab-content').removeClass('current');
	
	if(id == 10 || (id >= 101 && id <= 104)) {
		$("#li-tab-8").addClass('current');
		$("#tab-8").addClass('current');
	} else if(id == 20 || (id >= 106 && id <= 110)) {
		$("#li-tab-1").addClass('current');
		$("#tab-1").addClass('current');
	} else if(id == 30 || (id >= 112 && id <= 148) || (id >= 178 && id <= 247)) {
		if(id == 30) {
			contents_id = "P112";
		}
		
		$("#li-tab-2").addClass('current');
		$("#tab-2").addClass('current');
	} else if((id >= 149 && id <= 152) || (id >= 248 && id <= 253)) {
		$("#li-tab-3").addClass('current');
		$("#tab-3").addClass('current');
	} else if((id >= 153 && id <= 154) || (id == 254)) {
		$("#li-tab-4").addClass('current');
		$("#tab-4").addClass('current');
	} else if((id >= 155 && id <= 171) || (id >= 255 && id <= 282)) {
		$("#li-tab-5").addClass('current');
		$("#tab-5").addClass('current');
	} else if((id >= 172 && id <= 176) || (id >= 283 && id <= 291)) {
		$("#li-tab-6").addClass('current');
		$("#tab-6").addClass('current');
	} else if(id == 177) {
		$("#li-tab-7").addClass('current');
		$("#tab-7").addClass('current');
	}
	
	$(location).attr('href', '#' + contents_id);
	
	reverseTreeMenu(contents_id);
}

function reverseTreeMenu(id) {
	$('ul.nested a').removeClass('reverse');
	$("#menu_a_"+id).addClass('reverse');
}
*/
function fnMove(id, level){
	reverseTreeMenu(id);
	
//	var offset = $("#" + seq).offset();
//	$('html, body, html, body, nav').animate({scrollTop : offset.top}, 400);
	location.href='#menu_' + id;
	
	callFunction2(id);
}

function eventTreeMenu(box) {
	if(box.checked) {
//		$('ul.nested a.level3').css('display', '');
//		$('ul.nested a.level3 span').css('display', 'none');
		
		$('ul.nested a.level3').css('display', 'none');
		$('ul.nested a.level3 span').parent().css('display', '');
	} else {
		$('ul.nested a.level3').css('display', '');
	}
}

var lib = "<script type=\"text/javascript\" src=\"./lib/chartjs/Chart.min.js\"></script>" +
"<script type=\"text/javascript\" src=\"./lib/chartjs/chartjs-plugin-labels.min.js\"></script>";

var css = "<style type=\"text/css\">\n" + 
"body.om			{font:bold 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-2px;}\n" + 
"h1.top				{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:0px; margin-bottom:30px; letter-spacing:-2px;}\n" + 
"h1.title			{font:bold 30px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:50px; margin-bottom:30px; letter-spacing:-2px;}\n" + 
"h2.title			{font:bold 20px Arial,Helvetica,Geneva,sans-serif; color:#383838; background-color:White; margin-top:30px; margin-bottom:5px; margin-left:15px; letter-spacing:-2px;}\n" + 
"h3.title			{font:bold 14px Arial,Helvetica,Geneva,sans-serif; color:#004370; background-color:White; margin-top:20px; margin-bottom:0px; margin-left:25px; letter-spacing:-1px;}\n" + 
"h4.title			{font:bold 13px Arial,Helvetica,Geneva,sans-serif;color:#383838;background-color:White; margin-bottom:0px; margin-left:25px; letter-spacing:-1px;}\n" + 
"t1.title			{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background-color:White; margin-top:20px; margin-bottom:0px; letter-spacing:-2px;}\n" + 
"li.title			{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White;letter-spacing:-1px;}\n" + 
"a.om				{font:bold 12px Arial,Helvetica,sans-serif; color:#663300; vertical-align:top; margin-top:0px; margin-bottom:0px;}" +
"li.contents		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; letter-spacing:-1px;}\n" + 
"dd.contents2		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px;}\n" + 
"dd.contents2-b5	{font: 14px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:5px; margin-left:25px; letter-spacing:0px;}\n" + 
"dd.contents3		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px;}\n" + 
"dd.contents4		{font: 12px Arial,Helvetica,Geneva,sans-serif; color:black; background:White; margin-top:0px; margin-bottom:15px; margin-left:25px; letter-spacing:0px;}\n" + 
"table.awr			{border-spacing:0px !important;padding-bottom:30px;}\n" + 
"table.om			{border-spacing:0px !important; margin-bottom:10px;}\n" + 
"table.om2			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;}\n" + 
"table.om2-b20		{border-spacing:0px !important; margin-top:0px; margin-bottom:20px; margin-left:25px; width:975px;}\n" + 
"table.om3			{border-spacing:0px !important; margin-top:0px; margin-bottom:10px; margin-left:25px; width:975px;}\n" + 
"table.om4			{border-spacing:0px !important; margin-top:-5px; margin-bottom:10px; margin-left:35px; width:965px;}\n" + 
"th.om-left			{font:bold 12px Dotum,Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #146298; border-right:1px solid #146298; border-bottom:1px solid #146298; border-left:1px solid #FFFFFF; }\n" + 
"th.om-center		{font:bold 12px Dotum,Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #146298; border-right:1px solid #146298; border-bottom:1px solid #146298;}\n" + 
"th.om-right		{font:bold 12px Dotum,Arial,Helvetica,Geneva,sans-serif; color:#146298; background:#EEF9FF; padding:5px 5px 5px 5px; text-align:center; border-top:2px solid #146298; border-right:1px solid #FFFFFF; border-bottom:1px solid #146298;}\n" + 
"td.om-left			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696; border-left:1px solid #FFFFFF;}\n" + 
"td.om-center		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:1px solid #969696;}\n" + 
"td.om-right		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:1px solid #969696;}\n" + 
"td.om-left-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #146298; border-left:1px solid #FFFFFF;}\n" + 
"td.om-center-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #969696; border-bottom:2px solid #146298;}\n" + 
"td.om-right-close	{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #146298;}\n" + 
"td.om-colspan		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black; background:White; vertical-align:middle; padding:5px 5px 5px 5px; border-right:1px solid #FFFFFF; border-bottom:2px solid #146298; text-align:center;}\n" + 

"table.nl1			{border-spacing:0px !important;margin-top:0px; margin-bottom:10px;}\n" + 
"table.nl2			{border-spacing:0px !important;margin-top:0px; margin-bottom:20px;margin-left:25px;}\n" + 
"table.nl3			{border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:25px;}\n" + 
"table.nl4			{border-spacing:0px !important;margin-top:-5px; margin-bottom:10px;margin-left:25px;}\n" + 
"td.nl-left			{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
"td.nl-center		{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 
"td.nl-right		{font:14px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;padding:0px 0px 5px 0px;border-top:1px solid #FFFFFF;border-right:1px solid #FFFFFF;border-bottom:1px solid #FFFFFF;border-left:1px solid #FFFFFF;}\n" + 

".chart3				{padding-left:25px;}" +
".chart4				{padding-left:40px;}" +
".button {" +
"    width:50px;\n" + 
"    height:22px;\n" +
"    background-color: #FFF;\n" + 
"    border: 1px solid #217346;\n" + 
"    color:#217346;\n" + 
"    text-align: center;\n" + 
"    text-decoration: none;\n" + 
"    display: inline-block;\n" + 
"    font-size: 15px;\n" + 
"    margin: 4px;\n" + 
"    cursor: pointer;\n" + 
"    border-radius:7px;\n" +
"    float: right;\n" +
"}\n" + 
".button:hover {background-color: #217346;color:#FFF;}\n" + 

"td.awrc			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;}\n" + 
"td.awrnclb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-left: thin solid black;}\n" + 
"td.awrncbb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-left: thin solid black;border-right: thin solid black;}\n" + 
"td.awrncrb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-right: thin solid black;}\n" + 
"td.awrcrb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-right: thin solid black;}\n" + 
"td.awrclb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-left: thin solid black;}\n" + 
"td.awrcbb			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-left: thin solid black;border-right: thin solid black;}\n" + 
"a.awr				{font:bold 12px Arial,Helvetica,sans-serif;color:#663300; vertical-align:top;margin-top:0px; margin-bottom:0px;}\n" + 
"td.awrnct			{font:12px Arial,Helvetica,Geneva,sans-serif;border-top: thin solid black;color:black;background:White;vertical-align:top;}\n" + 
"td.awrct			{font:12px Arial,Helvetica,Geneva,sans-serif;border-top: thin solid black;color:black;background:#FFFFCC; vertical-align:top;}\n" + 
"td.awrnclbt		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-top: thin solid black;border-left: thin solid black;}\n" + 
"td.awrncbbt		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-left: thin solid black;border-right: thin solid black;border-top: thin solid black;}\n" + 
"td.awrncrbt		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:White;vertical-align:top;border-top: thin solid black;border-right: thin solid black;}\n" + 
"td.awrcrbt			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-top: thin solid black;border-right: thin solid black;}\n" + 
"td.awrclbt			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-top: thin solid black;border-left: thin solid black;}\n" + 
"td.awrcbbt			{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFFFCC; vertical-align:top;border-top: thin solid black;border-left: thin solid black;border-right: thin solid black;}\n" + 
"td.noborder		{font:12px Arial,Helvetica,Geneva,sans-serif;color:black;background:#FFF;}\n" + 
"table.tdiff		{border_collapse: collapse;}\n" + 
"</style>\n";