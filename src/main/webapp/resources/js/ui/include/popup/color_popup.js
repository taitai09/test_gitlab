$(document).ready(function() {
	const newLocal = $('#colorPopup').window({
		title:"색상 선택",
		top:getWindowTop(600),
		left:getWindowLeft(600)
	});
	
	ajaxCallGetRGBColor();
});

function checkRGBColor() {
	ajaxCall("/checkRGBColor" + pageId,
			null,
			"GET",
			callback_setRGBColor);
}

var rgbColorArr = null;

var callback_setRGBColor = function(result) {
	if(!result.result) {
		return false;
	}
	
	rgbColorArr = new Array();
	var post = null, id = null, value = null, dbid;
	var objLen = result.object.length;
	
	for(var i = 0 ; i < objLen; i++){
		post = result.object[i];
		id = post.rgb_color_id;
		value = post.rgb_color_value;
		
		rgbColorArr.push(value);
	}
	
	console.log(rgbColorArr);
}

function ajaxCallGetRGBColor(){
	ajaxCall("/getRGBColor",
			null,
			"GET",
			callback_RGBColor);
}

var callback_RGBColor = function(result) {
	if(result.result){
		var objLen = result.object.length;
		var html = "";
		var post = null, id = null, value = null;
		var str01 = "    " + "<td id='td_";
		var str02 = "' style='cursor:pointer;background-color:";
		var str03 = "; width:100px;height:30px;' onclick=\'setColor(\"";
		var str04 = "\");\'>&nbsp;</td>" + "\n";
		var row = 0;
		
		for(var i = 0 ; i < objLen; i++){
			if(i > 1 && i % 5 == 0) {
				html = html.concat("\n</tr>\n");
			}
			
			if(i % 5 == 0) {
				html = html.concat("<tr>\n");
				row++;
			}
			
			post = result.object[i];
			id = post.rgb_color_id;
			value = post.rgb_color_value;
			
			html = html.concat(str01).concat(id).concat(str02).concat(value).concat(str03).concat(value).concat("\"").concat(",").concat("\"").concat(id).concat(str04);
		}
		
		$("#tableTest tbody").html(html);
	}else{
		parent.$.messager.alert('', result.message);
	}
}

function setColor(c, id){
	this.c = c;
	this.id = id;
	
	ajaxCall("/checkRGBColor" + pageId,
			null,
			"GET",
			callback_checkRGBColor);
}

var callback_checkRGBColor = function(result) {
	if(result.result) {
		var objLen = result.object.length;
		var ERROR_MSG = "선택한 색상은 사용중입니다. 다른 색상으로 선택하십시요";
		
		for(var i = 0 ; i < objLen; i++){
			var rgbColorId = result.object[i].rgb_color_id;
			
			if(this.id == rgbColorId) {
				parent.$.messager.alert('', ERROR_MSG);
				return;
			}
		}
	}
	
	$("#colorDiv").textbox('setValue',this.c);
	$("#colorDiv").textbox('textbox').css("background-color", this.c);
	setTextShadow($('#colorDiv'));
	$("#rgb_color_id").val(this.id);
	
	c = "";
	id = "";
	
	Btn_OnClosePopup("colorPopup");
}

function setTextShadow(divId) {
	divId.textbox('textbox').css("color", '#FFF');
	divId.textbox('textbox').css("text-shadow", '-1px -1px #000, 1px -1px #000, -1px 1px #000, 1px 1px #000');
}
