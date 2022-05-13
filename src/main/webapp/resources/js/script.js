$(document).ready(function(){
	parent.$.messager.defaults.ok = 'YES';
	parent.$.messager.defaults.cancel = 'NO';
	
	$(".tbl").datagrid({
		singleSelect:true,
		collapsible:false,
		multiSort:false,
		remoteSort:false
	});	

	$(".datapicker").datebox({
		formatter:myformatter,
		parser:myparser
	});
});

// easyui datagrid no records loaded
var myview = $.extend({},$.fn.datagrid.defaults.view,{
	onAfterRender:function(target){
		$.fn.datagrid.defaults.view.onAfterRender.call(this,target);
		var opts = $(target).datagrid('options');
		var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		if (!$(target).datagrid('getRows').length){
			var emptyMsg = "검색된 데이터가 없습니다.";
			var d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
			d.css({
				top:50
			});
		}
	}
});

var emptyview = $.extend({},$.fn.datagrid.defaults.view,{
	onAfterRender:function(target){
		$.fn.datagrid.defaults.view.onAfterRender.call(this,target);
		var opts = $(target).datagrid('options');
		var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		if (!$(target).datagrid('getRows').length){
		}
	}
});

var naview = $.extend({},$.fn.datagrid.defaults.view,{
	onAfterRender:function(target){
		$.fn.datagrid.defaults.view.onAfterRender.call(this,target);
		var opts = $(target).datagrid('options');
		var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		if (!$(target).datagrid('getRows').length){
			var emptyMsg = "N/A";
			var d = $('<div class="datagrid-empty"></div>').html(emptyMsg || 'no records').appendTo(vc);
			d.css({
				top:50
			});
		}
	}
});