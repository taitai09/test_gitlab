$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	$("#tableList").datagrid({
		view: myview,
		columns:[[
		    {field:'rnum',title:'순번',width:"5%",halign:"center",align:"center",sortable:"true"},
		    {field:'seq',hidden:'true'},
		    {field:'modify_column',hidden:'true'},
		    {field:'table_name',title:'테이블명',width:"12%",halign:"center",align:"center",sortable:"true"},			
			{field:'recommend_type',title:'추천유형',width:"6%",halign:"center",align:'center',sortable:"true"},
			{field:'access_path_column_list',title:'인덱스컬럼',width:"34%",halign:"center",align:'left', formatter:cellFormat,sortable:"true"},
			{field:'source_index_name',title:'변경대상 인덱스명',width:"11%",halign:"center",align:'left',sortable:"true"},
			{field:'source_index_column_list',title:'변경전 인덱스 컬럼',width:"32%",halign:"center",align:'left',sortable:"true"}
		]],

    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	},
    	onClickRow:function(index,row){
    		//console.log("onClickRow index:"+index+" row:"+row);
    		$("table tr td div").css("cursor","default");
    		$("div").css("cursor","default");
    	},
    	onClickCell:function(index,field,value){
    		//console.log("onClickCell index:"+index+" field:"+field+" value:"+value);
    		$("table tr td div").css("cursor","default");
    		$("div").css("cursor","default");
    	}
	});
	
	$('#tableList').datagrid('loadData',[]);
	
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("인덱스 검증 현황"," "); 
	
	ajaxCall("/IndexRecommendStatusAction",
		$("#submit_form"),
		"POST",
		callback_IndexRecommendStatusActionAddTable);		
});

function Btn_ReturnList(){
	$("#call_from_child").val("Y");
	$("#submit_form").attr("action","/AutoIndexStatus");
	$("#submit_form").submit();		
}

//callback 함수
var callback_IndexRecommendStatusActionAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList').datagrid("loadData", data);

	$(".datagrid-btable td").css("cursor","default");
	//$( ".tabs-panels:eq(0) .panel-htop:eq( 3 ) td" ).css( "cursor", "default" );
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
};

function Excel_DownClick(){	
	$("#submit_form").attr("action","/IndexRecommendStatus/ExcelDown");
	$("#submit_form").submit();
}

function cellFormat(val,row, index){
	
	if(row.recommend_type == "MODIFY"){
		return row.source_index_column_list + ", <span style='color:#f76e6e;font-weight:bold;'>" + row.modify_column + "</span>";	
	}else{
		return val;
	}
}