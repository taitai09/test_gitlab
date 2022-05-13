$(document).ready(function() {
	$("body").css("visibility", "visible");
	
	// 프로젝트ID 조회
//	$('#project_nm').textbox({
//		editable:false,
//		icons:[{
//			iconCls:'icon-search',
//			handler:function() {
//				Btn_ShowProjectList();
//			}
//		}]
//	});
	
	
//	HeadTitleSet();
	
	//Btn_OnClick();
	/*
	$("#tableList").datagrid({
		view: myview,
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : true,
		onLoadSuccess: function(data){
		},
		columns:[[
			{field:'qty_chk_tg_yn',halign:"center",align:'center',checkbox:'true'},
			{field:'qty_chk_idt',title:'품질점검지표',width:"10%",halign:"center",align:"left"},
			{field:'qty_chk_idt_nm',title:'품질점검지표명',width:"20%",halign:"center",align:'left'},
			{field:'qty_inspection_cnt',title:'작업결과(건수)',width:"05%",halign:"center",align:'right'},
			{field:'output_start_row',title:'출력시작행',width:"05%",halign:"center",align:'center'},
			{field:'qty_result_sheet_nm',title:'Sheet명',width:"20%",halign:"center",align:'left'},
			{field:'qty_chk_idt_cd',hidden:true},
			{field:'mdi_pcs_cd',hidden:true},
			{field:'qty_chk_result_tbl_nm',hidden:true},
			{field:'excel_output_yn',hidden:true},
			{field:'dml_yn',hidden:true}
			]],

	    onLoadError:function() {
	    	parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
	    }
	});
	*/
		
	
});


function Btn_ShowProjectList() {
	$('#projectList_form #project_nm').textbox('setValue', '');
	$('#projectList_form #del_yn').combobox('setValue','N');
	$('#projectList_form #projectList').datagrid('loadData',[]);
	
	$('#projectListPop').window("open");
	
	$("#projectList_form #projectList").datagrid("resize",{
		width: 900
	});
}
function setProjectRow(row) {
	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#menu_id").val();
	
	$("#submit_form #project_nm").textbox("setValue", row.project_nm);
//	$("#detail_form #project_nm").textbox("setValue", row.project_nm);
	$("#submit_form #project_id").val(row.project_id);
	
	settingComboBox(row.project_id);
}

function settingComboBox(project_id){
	// 구조품질집계표 - 라이브러리명
	$('#submit_form #lib_nm').combobox({
	    url:"/Mqm/GetOpenqErrCntLibNm?project_id="+project_id,
	    method:"get",
	    textField:'lib_nm',
	    valueField:'lib_cd',
		onLoadError: function(){
			parent.$.messager.alert('','라이브러리명 조회중 오류가 발생하였습니다.');
			return false;
		},
	    onSelect:function(rec){
	    	//if (rec.lib_nm == "") return;
	    	
	    	var win = parent.$.messager.progress({
	    		title:'Please waiting',
	    		text:'데이터를 불러오는 중입니다.'
	    	});
	    	
	    	$("#submit_form #model_nm").combobox({
				url:"/Mqm/GetOpenqErrCntModelNm?project_id="+project_id+"&lib_nm="+rec.lib_nm,
				method:"get",
				textField:'model_nm',
				valueField:'model_cd',
				onLoadSuccess: function(event) {
					parent.$.messager.progress('close');
				},
				onLoadError: function(){
					parent.$.messager.alert('','모델명 조회중 오류가 발생하였습니다.');
					parent.$.messager.progress('close');
				}
	    	});
	    }
	
	});
	
}
function HeadTitleSet()
{
	$("#currentPage").val("1");
	//fnSearch();
	ajaxCall("/Mqm/QualityInspectionJobSheet/GetQualityInspectionJobSheetHeadTitleList", $("#submit_form"), "POST", callback_QualityInspectionJobSheetHeadTitleAction);
}

function Btn_OnClick(val){
	if(!formValidationCheck()){
		return;
	}

	HeadTitleSet();
	/*
	$("#currentPage").val("1");
	//fnSearch();
	ajaxCall("/Mqm/QualityInspectionJobSheet/getQualityInspectionJobSheetHeadTitleList", $("#submit_form"), "POST", callback_QualityInspectionJobSheetHeadTitleAction);
	*/
	fnSearch();
}

//callback 함수
var callback_QualityInspectionJobSheetHeadTitleAction = function(result) {
	try{
		
		var data = JSON.parse(result);
		console.log("data.rows.length :",data.rows.length);

		var headArrayHead1 = new Array();
		var headArrayHead2 = new Array();
		
		var headArrayFixed1 = new Array();
		var headArrayFixed2 = new Array();

		var ldm_colspan = 0;
		var pdm_colspan = 0;
		var mng_colspan = 0;
		
		if(data.rows.length == 0) {
			$('#tableList').datagrid("loadData", []);
			return;
		}

		for(var i=0;i<data.rows.length;i++){
			//console.log("mdi_pcs_cd : ",data.rows[i].mdi_pcs_cd);
			//console.log("mdi_pcs_cd_cnt : ",data.rows[i].mdi_pcs_cd_cnt);
			if (data.rows[i].mdi_pcs_cd == 'LDM'){
				ldm_colspan = data.rows[i].mdi_pcs_cd_cnt;
			} else if (data.rows[i].mdi_pcs_cd == 'PDM'){
				pdm_colspan = data.rows[i].mdi_pcs_cd_cnt;
			} else if (data.rows[i].mdi_pcs_cd == 'MNG'){
				mng_colspan = data.rows[i].mdi_pcs_cd_cnt;
			}
			
		}
		
		headArrayFixed1.push({field:'STDD',title: data.rows[0].gather_day +' 기준',halign:"center", colspan:4});
		headArrayHead1.push({title: ' ',halign:"center"});
		headArrayHead1.push({title: ' ',halign:"center"});

		// LDM Column Header
		if (ldm_colspan > 0){
			headArrayHead1.push({field:'LOGIC',title:'논리모델',halign:"center", align:"center", colspan:ldm_colspan});
		}
		
		// PDM Column Header
		if (pdm_colspan > 0){
			headArrayHead1.push({field:'PHYSICS',title:'물리모델',halign:"center", align:"center", colspan:pdm_colspan});
		}
		
		// MNG Column Header
		if (mng_colspan > 0){
			headArrayHead1.push({field:'MAPPING',title:'매핑',halign:"center", align:"center", colspan:mng_colspan});
		}
		
		headArrayFixed2.push({field:'LIB_NM',title:'라이브러리명<br><br>',width:"6%",halign:"center",align:"left"});
		headArrayFixed2.push({field:'MODEL_NM',title:'모델<br><br>',width:"8%",halign:"center",align:"left"});
		headArrayFixed2.push({field:'ENT_CNT',title:'<br><br>유효<br>엔터티수<br><br><br>',width:"4%",halign:"center", formatter:getNumberFormat, align:"right"});
		headArrayFixed2.push({field:'ATT_CNT',title:'유효<br>속성수<br>',width:"4%",halign:"center", formatter:getNumberFormat, align:"right"});

		headArrayHead2.push({field:'NON_STD_ENT_CNT',title:'비표준<br>엔터티수<br>',width:"4%",halign:"center", formatter:getNumberFormat, align:"right"});
		headArrayHead2.push({field:'NON_STD_ENT_ATT_CNT',title:'<br><br>비표준<br>엔터티내<br>속성수<br><br>',width:"4%",halign:"center", formatter:getNumberFormat, align:"right"});
		
		var totrows = data.rows.length;
		totrows = totrows -3; 
		for(var i=0;i<data.rows.length;i++){
			
			var head_title_cd = data.rows[i].qty_chk_idt_cd;
			var head_title_nm = data.rows[i].qty_chk_idt_nm;
			
			if (data.rows[i].qty_chk_tg_yn == 'N'){
				continue;
				//headArrayHead2.push({field:head_title_cd, hidden:true});
			}
			else{
					headArrayHead2.push({field:head_title_cd,title:head_title_nm,width:"4%",halign:"center", formatter:getNumberFormat, align:"right",
						styler: function(value,row,index){
							if (row.LIB_NM == '전체') return;
							if (value > 0){
								return 'background-color:#ffcc00;';
							}
						}
					});
			}
		}

		//console.log(headArrayHead);
		//console.log(headArrayHead1);
		$('#tableList').datagrid("loadData", []);
		
		$("#tableList").datagrid({
			frozenColumns:[headArrayFixed1,headArrayFixed2],

			columns:[headArrayHead1, headArrayHead2],
		
			rowStyler:function(index,row){
			      if (row.LIB_NM == '전체'){
			            return 'background-color:#dae9f3;font-weight:bold;';
			      }
			}

		});

		//fnSearch();

	}catch(e){
		console.log("e.message:"+e.message);
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

	$(".datagrid-header td").css("vertical-align","top");
	
	var datagridHeaderTds = $(".datagrid-header td");
	var datagridHeaderTd;
	for(var i=0;i<datagridHeaderTds.length;i++){
		datagridHeaderTd = datagridHeaderTds[i];
		var field = $(datagridHeaderTd).attr("field");
		if(field == "STDD" ||field == "LOGIC" ||field == "PHYSICS" ||field == "MAPPING"){
			$(datagridHeaderTd).css("vertical-align","middle");
		}
		if(field == "LIB_NM" ||field == "MODEL_NM" ||field == "ENT_CNT" ||field == "ATT_CNT" || field == "NON_STD_ENT_CNT" || field == "NON_STD_ENT_ATT_CNT" ){
			$(datagridHeaderTd).css("vertical-align","middle");
		}
	}
};

function fnSearch(){

	if($('#submit_form #project_id').val() == null || $('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
		//var current_datetime = new Date()
		//var extrac_dt = current_datetime.getFullYear() + "" + leftpad(current_datetime.getMonth() + 1,2,'0') + leftpad(current_datetime.getDate(),2,'0') + "" + leftpad(current_datetime.getHours(),2,'0') + leftpad(current_datetime.getMinutes(),2,'0')  + leftpad(current_datetime.getSeconds(),2,'0')
		//console.log("current_datetime :" + current_datetime);
		//console.log("formatted_date :" + extrac_dt);
		//$('#submit_form #extrac_dt').val(extrac_dt);
		
		/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
		parent.frameName = $("#menu_id").val();
		$('#tableList').datagrid('loadData',[]);

		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질 집계표"," "); 
		
		ajaxCall("/Mqm/QualityInspectionJobSheet",
				$("#submit_form"),
				"POST",
				callback_QualityInspectionJobAction);	
}

//callback 함수
var callback_QualityInspectionJobAction = function(result) {
	json_string_callback_common(result,'#tableList',true);
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();

};


function formValidationCheck(){
	if($('#submit_form #project_id').val() == null || $('#submit_form #project_id').val() == ""){
		parent.$.messager.alert('경고','프로젝트를 선택해 주세요.','warning');
		return false;
	}
	
	return true;
};


function Excel_Download(){

	var current_datetime = new Date()
	var extrac_dt = current_datetime.getFullYear() + "" + leftpad(current_datetime.getMonth() + 1,2,'0') + leftpad(current_datetime.getDate(),2,'0') + "" + leftpad(current_datetime.getHours(),2,'0') + leftpad(current_datetime.getMinutes(),2,'0')  + leftpad(current_datetime.getSeconds(),2,'0')
	console.log("current_datetime :" + current_datetime);
	console.log("formatted_date :" + extrac_dt);
	
	$('#submit_form #extrac_dt').val(extrac_dt);	
	
	var rows = $('#tableList').datagrid('getRows');
	if(rows.length <= 0){
		parent.$.messager.alert('','다운로드할 데이터가 없습니다.');
		return false;	
	}

	$("#submit_form").attr("action","/Mqm/QualityInspectionJobSheet/ExcelDown");
	$("#submit_form").submit();
	$("#submit_form").attr("action","");
	
	openMessageProgress();
}
function getCookie(name) {
	var parts = document.cookie.split(name + "=");
	if(parts.length == 2)
		return parts.pop().split(";").shift();
}

function expireCookie(name) {
	document.cookie = encodeURIComponent(name) + "=deleted; expires=" + new Date(0).toUTCString();
}

var intId;
var time = 1000;
var count;
var count_max = 0;

function openMessageProgress() {
	/* modal progress open */
	if(parent.openMessageProgress != undefined) parent.openMessageProgress("구조 품질 집계표 Excel 생성"," ");
	
	count = 0;
	
	invoke();
}

function invoke() {
	intId = setInterval(function() {
		var token = getCookie("excelDownToken" + $('#submit_form #extrac_dt').val());
		
		if(count <= 5) {
			count += 1;
		}
		
		count_max += 1;
		// 60초가 초과되면 강제로 닫음
		//console.log("count_max :" + count_max);
		if (count_max > 60) closeMessageProgress();
		
		if(typeof token != "undefined" && token == "TRUE") {
			closeMessageProgress();
		} else {
			if(count > 5) {
				count = 0;
				time = 1000;
				intId = clearInterval(intId);
				invoke();
			}
		}
	}, time);
}

function closeMessageProgress() {
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();
	
	clearInterval(intId);
	
	expireCookie("excelDownToken" + $('#submit_form #extrac_dt').val());
}

function leftpad(str, len, ch) {
	  str = String(str);
	  var i = -1;
	  if (!ch && ch !== 0) ch = ' ';
	  len = len - str.length;
	  while (++i < len) {
	    str = ch + str;
	  }
	  return str;
}