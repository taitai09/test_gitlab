$(document).ready(function() {
	fnGetMyAuthMenuList();
//	fnGetMyMenuList();
});

var fnGetMyAuthMenuList = function(){
	$('#authMenuList').tree({
		url:"/Common/UserAuthMenuList",
		method:"get",
		animate:true,
		lines:true,
		onClick: function(node){
			console.log("id:"+node.id);
			console.log("text:"+node.text);
			console.log("parent_id:"+node.parent_id);
			node.checked = true;
			$('#tt').tree(node.checked, true);
		},
        checkbox : true,
        onCheck : function(node, checked) {

            var arrayLayerName = [];

            var nodes = $('#authMenuList').tree('getChecked');

            $(nodes).each(function() {
                if (this.attributes && this.attributes.url) {
                    arrayLayerName.push(this.attributes.url);
                }
            });

        }
	});	
}

//var fnGetMyMenuList = function() {
//	$('#myMenuList').tree({
//		url:"/Common/MyMenuList",
//		method:"get",
//		animate:true,
//		lines:true,
//		onClick: function(node){
//			console.log("id:"+node.id);
//			console.log("text:"+node.text);
//			console.log("parent_id:"+node.parent_id);
//			node.checked = true;
//			//setUserAddTable(node.id);
//		}
//	});	
//};

function Btn_AddApplyTarget(){
	//rows = $('#authMenuList').treegrid('getSelections');
//	rows = $('#authMenuList').tree('getSelected');
//	console.log("rows.length:"+row.length);
	
    var nodes = $('#authMenuList').tree('getChecked');

	var menu_id_array = "";
	var cnt = 1;
    $(nodes).each(function() {
    	menu_id_array += this.id;
    	if(cnt < nodes.length){
    		menu_id_array += "^";
    	}
    	cnt++;
    });

    $("#submit_form #menu_id").val(menu_id_array);
    
    ajaxCall("/Common/insertMyMenuAction", $("#submit_form"), "POST", callback_afterInsertOrDeleteMyMenu);	    		
    
//    fnGetMyMenuList();	
}

function Btn_RemoveApplyTarget(){
    var nodes = $('#myMenuList').tree('getChecked');
	console.log("nodes:",nodes);
	console.log("nodes.length:"+nodes.length);

    $(nodes).each(function() {
    		console.log("onCheck id::", this.id);
    		console.log("onCheck text::", this.text);
    		console.log("onCheck parent_id::", this.parent_id);
    		$("#submit_form #menu_id").val(this.id);
    		ajaxCall("/Common/deleteMyMenuAction", $("#submit_form"), "POST", callback_afterInsertOrDeleteMyMenu);	    		
    });	
    

}

var callback_afterInsertOrDeleteMyMenu = function(result){
	console.log("result",result);
	if(result.result){
		parent.$.messager.alert('',result.message+"<BR/>재접속시 마이메뉴가 반영됩니다.");
		//$('#authMenuList').tree('reload',$('#authMenuList').tree('getRoot').target);
		document.location.reload();
	}else{
		parent.$.messager.alert('error',result.message);
	}
//    fnGetMyMenuList();	
}
