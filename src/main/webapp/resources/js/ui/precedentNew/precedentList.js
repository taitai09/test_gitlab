var guideOrPrecedent=1;

$(document).ready(function() {
	$("body").css("visibility", "visible");
//	Btn_OnClick(1);
	$('#submit_form2 #tuning_case_type_cd').combobox({
	    url:"/Common/getCommonCode?grp_cd_id=1079&isAll=Y",
	    method:"get",
		valueField:'cd',
	    textField:'cd_nm',
	});
	
	
	$("#tableList0").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#submit_form1 #guide_no").val(row.guide_no);
			$("#submit_form1 #tuning_no").val(row.tuning_no);
			$("#submit_form1 #reg_user_id").val(row.reg_user_id);
			$("#submit_form1 #guide_div_cd").val(row.guide_div_cd);
			getDetailView1();
		},		
		columns:[[
			{field:'pinset',title:'',width:"2%",halign:"center",align:"center",styler:cellStyler},
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'guide_sbst',hidden:true},
			{field:'guide_div_cd',hidden:true}
		]],
			onLoadSuccess:function(data){
					var cells = $('#tableList0').datagrid('getPanel').find('div.datagrid-body td[field] div.datagrid-cell:not(:empty)');
					$(this).datagrid('getPanel').find('tr.datagrid-row').bind('mouseover',function(e){
						var index = parseInt($(this).attr('datagrid-row-index'));
						var tp = data.rows[index].guide_sbst;
						if( tp == "" || tp == null || tp == undefined || ( tp != null && typeof tp == "object" && !Object.keys(tp).length ) ) ;
						else {
							//tp = tp.replace(/(<([^>]+)>)/ig,"");
							if(tp.length > 900){
								/*if(tp.indexOf('.') != -1) tp = tp.substring(0,tp.indexOf('.')+1) + '<br/>'+ tp.substring(tp.indexOf('.')+1, 150) + '...'
								else if(tp.indexOf("-") != -1)  tp = tp.substring(0,tp.indexOf('-')+1) + '<br/>'+ tp.substring(tp.indexOf('-')+1, 150) + '...'
								else tp = tp.substring(0,50)+'<br/>'+tp.substring(50,100)+'<br/>'+tp.substring(100,150)+'<br/>'+tp.substring(150,200)+" ...";*/
								tp = tp.substring(0,900);
							}
						}
						cells.tooltip({
							position:'bottom',
							content: function() {
								//console.log("전:"+tp);
								//tp = $(this).html();
								//console.log("후:"+tp);
								return tp;
							},
						});
					
					})
				  
				},
    	onLoadError:function() {
    		parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
    	} 
	});
	
	$("#tableList1").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#submit_form2 #guide_no").val(row.guide_no);
			$("#submit_form2 #tuning_no").val(row.tuning_no);
			$("#submit_form2 #reg_user_id").val(row.reg_user_id);
			$("#submit_form2 #guide_div_cd").val(row.guide_div_cd);
			getDetailView2();
		},		
		columns:[[
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			//{field:'tuning_no',title:'튜닝번호',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			//{field:'sys_nm',title:'DB명',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			//{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'controversialist',hidden:true},
			{field:'impr_sbst',hidden:true},
			{field:'guide_div_cd',hidden:true}
			]],
			onLoadSuccess:function(data){
				var cells = $('#tableList1').datagrid('getPanel').find('div.datagrid-body td[field] div.datagrid-cell:not(:empty)');
				$(this).datagrid('getPanel').find('tr.datagrid-row').bind('mouseover',function(e){
					var index = parseInt($(this).attr('datagrid-row-index'));
					
					var tp = data.rows[index].controversialist;
					var tp2 = data.rows[index].impr_sbst;
					if( tp == "" || tp == null || tp == undefined || ( tp != null && typeof tp == "object" && !Object.keys(tp).length ) ) {
						tp == '내용없음';
					} else {
						tp = tp.replace(/(<([^>]+)>)/ig,"");
						if(tp.length > 50){
							if(tp.indexOf('.') != -1) tp = tp.substring(0,tp.indexOf('.')+1) + '<br/>'+ tp.substring(tp.indexOf('.')+1, 150) + '...'
							else tp = tp.substring(0,80)+'<br/>'+tp.substring(80,150)+" ...";
						}
					}
					if(tp == ''|| tp2 == 'null' || tp == null || tp == undefined) tp = '내용없음';

					if( tp2 == "" || tp2 == null || tp2 == undefined || ( tp2 != null && typeof tp2 == "object" && !Object.keys(tp).length ) ){
						 tp2 == '';	
					} else {
						tp2 = tp2.replace(/(<([^>]+)>)/ig,"");
						if(tp2.length > 50){
							if(tp2.indexOf('.') != -1) tp2 = tp2.substring(0,tp2.indexOf('.')+1) + '<br/>'+ tp2.substring(tp2.indexOf('.')+1, 150) + '...'
							else tp2 = tp2.substring(0,80)+'<br/>'+tp2.substring(80,150)+" ...";
						}
					}
					if(tp2 == ''|| tp2 == 'null' || tp2 == null || tp2 == undefined) tp2 = '내용없음';
					
					cells.tooltip({
						position:'bottom',
						content: function() {
							//tp = $(this).html();
							var contents = '<b>문제점</b>' + '<br/>' + tp + '<br/><br/>' + '<b>개선내역</b>' + '<br/>' + tp2;
							return contents;
						},
					});
				
				})
			  
			},
			/* onLoadSuccess:function(data) {
					var cells = $('#tableList0').datagrid('getPanel').find('div.datagrid-body td[field] div.datagrid-cell:not(:empty)');
					$(this).datagrid('getPanel').find('tr.datagrid-row').bind('mouseover',function(e){
						var index = parseInt($(this).attr('datagrid-row-index'));
						
						var controversialist = data.rows[index].controversialist;
							
							if(controversialist != null && controversialist.length > 50){
								controversialist = controversialist.substring(0,50)+" ...";
							}else if(controversialist != null && controversialist.length <= 50){
								controversialist = controversialist.substring(0,50);
							}

							var impr_sbst = data.rows[index].impr_sbst;
							if(impr_sbst != null && impr_sbst.length > 50){
								impr_sbst = impr_sbst.substring(0,50)+" ...";
							}else if(impr_sbst != null && impr_sbst.length <= 50){
								impr_sbst = impr_sbst.substring(0,50);
							}
							
						$('.datagrid-body .datagrid-cell').tooltip({
				            content: function() {return $('<div class="dg"></div>')},
				            showEvent: 'mouseenter focusin',
				            position: 'right',
				            onShow: function(){
				               var t = $(this);
				               var dg = t.tooltip('tip').find('.dg');
				               dg.datagrid('resize');
				               t.tooltip('tip').unbind().bind('mouseenter', function(){
				                  t.tooltip('show');
				               }).bind('mouseleave', function(){
				                  t.tooltip('hide');
				               });
				            },
				            onUpdate:function(content){
				               //var opts = $(this).tooltip('options');
				               //var id_ticket = opts.param;
				               content.datagrid({
				                  width: 800,
				                  nowrap:false,
				                  columns:[[
				                     {field:'content1', title:'문제점', width:'50%'},
				                     {field:'content2', title:'개선내역', width:'50%'},
				                  ]],
				                  data: [
				                     {content1: controversialist, content2: impr_sbst}
				                  ],
				                  onLoadSuccess: function(data){
				                  //   console.log(data)
				                  }
				               });
				            }
				         });
					})
		      },*/

			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});
	
	
	
/*	$("#tableList1").datagrid({
		view: myview,
		onClickRow : function(index,row) {
			$("#submit_form2 #guide_no").val(row.guide_no);
			$("#submit_form2 #tuning_no").val(row.tuning_no);
			$("#submit_form2 #reg_user_id").val(row.reg_user_id);
			$("#submit_form2 #guide_div_cd").val(row.guide_div_cd);
			getDetailView2();
		},		
		columns:[[
			{field:'guide_no',title:'번호',width:"5%",halign:"center",align:"center",sortable:"true"},
			{field:'tuning_no',title:'튜닝번호',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'sys_nm',title:'DB명',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
			{field:'guide_title_nm',title:'제목',width:"30%",halign:"center",align:'left',sortable:"true"},
			{field:'reg_user_id',hidden:"true"},
			{field:'reg_user_nm',title:'등록자',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'reg_dt',title:'등록일시',width:"10%",halign:"center",align:'center',sortable:"true"},
//			{field:'upd_nm',title:'수정자',width:"5%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'upd_dt',title:'수정일시',width:"10%",halign:"center",align:'center',sortable:"true",sorter:sorterDatetime},
//			{field:'top_fix_yn',title:'상위공지여부',width:"5%",halign:"center",align:'center',sortable:"true"},
			{field:'retv_cnt',title:'조회수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			//{field:'download_cnt',title:'다운로드수',width:"5%",halign:"center",align:'center',formatter:getNumberFormat,sortable:"true"},
			{field:'controversialist',hidden:true},
			{field:'impr_sbst',hidden:true},
			{field:'guide_div_cd',hidden:true}
			]],
			onLoadSuccess:function(data) {
				var cells = $('#tableList0').datagrid('getPanel').find('div.datagrid-body td[field] div.datagrid-cell:not(:empty)');
				$(this).datagrid('getPanel').find('tr.datagrid-row').bind('mouseover',function(e){
					var index = parseInt($(this).attr('datagrid-row-index'));
					
					var controversialist = data.rows[index].controversialist;
					
					if(controversialist != null && controversialist.length > 50){
						controversialist = controversialist.substring(0,50)+" ...";
					}else if(controversialist != null && controversialist.length <= 50){
						controversialist = controversialist.substring(0,50);
					}
					
					var impr_sbst = data.rows[index].impr_sbst;
					if(impr_sbst != null && impr_sbst.length > 50){
						impr_sbst = impr_sbst.substring(0,50)+" ...";
					}else if(impr_sbst != null && impr_sbst.length <= 50){
						impr_sbst = impr_sbst.substring(0,50);
					}
					
					$('.datagrid-body .datagrid-cell').tooltip({
						content: function() {return $('<div class="dg"></div>')},
						showEvent: 'mouseenter focusin',
						position: 'right',
						onShow: function(){
							var t = $(this);
							var dg = t.tooltip('tip').find('.dg');
							dg.datagrid('resize');
							t.tooltip('tip').unbind().bind('mouseenter', function(){
								t.tooltip('show');
							}).bind('mouseleave', function(){
								t.tooltip('hide');
							});
						},
						onUpdate:function(content){
							//var opts = $(this).tooltip('options');
							//var id_ticket = opts.param;
							content.datagrid({
								width: 800,
								nowrap:false,
								columns:[[
									{field:'content1', title:'문제점', width:'50%'},
									{field:'content2', title:'개선내역', width:'50%'},
									]],
									data: [
										{content1: controversialist, content2: impr_sbst}
										],
										onLoadSuccess: function(data){
											//   console.log(data)
										}
							});
						}
					});
				})
			},
			
			onLoadError:function() {
				parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
			} 
	});
*/	 
	$.extend($.fn.validatebox.defaults.rules, {
		greaterThan:{
			validator: function(value,param){
				var v1 = $(param[0]).datebox('getValue');
				var d1 = myparser(v1);
				var d2 = myparser(value);
				if(d2<d1){
					parent.$.messager.alert('','검색조건의 종료일은 시작일보다 커야 합니다.');
				}
				return d2 > d1;
			},
			message: 'Please select a greater date.'
		}
	});
	
	$("#submit_form1 #selectValue").combobox('setValue',$("#submit_form1 #searchKey").val());	
	var call_from_parent = $("#submit_form1 #call_from_parent").val();
	var call_from_child = $("#submit_form1 #call_from_child").val();

	var searchBtnClickCount1 = $("#submit_form1 #searchBtnClickCount1").val();
	var searchBtnClickCount2 = $("#submit_form1 #searchBtnClickCount2").val();
	
	guideOrPrecedent = $("#submit_form1 #guide_div_cd").val();
	
	if(guideOrPrecedent == 1){
		$("#sqlTuningTab").tabs('select',0);
	}else if(guideOrPrecedent == 2){
		$("#sqlTuningTab").tabs('select',1);
	}
	
	if(call_from_parent == "Y" || call_from_child == "Y" || searchBtnClickCount1 == "1" || searchBtnClickCount2 == "1"){
		if(searchBtnClickCount1 == "1"){
			fnSearch2(1);
		}
		
		if(searchBtnClickCount2 == "1"){
			fnSearch2(2);
		}
	}
	
	var t = $('#searchValue');
	t.textbox('textbox').bind('keyup', function(e){
	   if (e.keyCode == 13){   // when press ENTER key, accept the inputed value.
		   Btn_OnClick();
	   }
	});

	$("#sqlTuningTab").tabs({
		plain: true,
		onSelect: function(title,index){
			if(index == 0){
				if($("#tab0ClickCount").val() != 1){
					Btn_OnClick(1);
				}
				guideOrPrecedent = 1;
			}else if(index == 1){
				if($("#tab1ClickCount").val() != 1){
					Btn_OnClick(2);
				}
				guideOrPrecedent = 2;
			}
		}		
	});
	
	//이전, 다음 처리
	$("#submit_form1 #prevBtn").click(function(){
		if(formValidationCheck1()){
			fnGoPrevOrNext1("PREV");
		}
	});
	$("#submit_form1 #nextBtn").click(function(){
		if(formValidationCheck1()){
			fnGoPrevOrNext1("NEXT");
		}
	});
	
	//이전, 다음 처리
	$("#submit_form2 #prevBtn").click(function(){
		if(formValidationCheck2()){
			fnGoPrevOrNext2("PREV");
		}
	});
	$("#submit_form2 #nextBtn").click(function(){
		if(formValidationCheck2()){
			fnGoPrevOrNext2("NEXT");
		}
	});
	
	var guideDivCd = $('#guide_div_cd').val();
	
	if(guideDivCd == '') {
		Btn_OnClick(1);
	} else {
		Btn_OnClick(guideDivCd);
	}
});

function fnGoPrevOrNext1(direction){
	
	if(direction == "PREV"){
		if($('#submit_form1 #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form1 #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#submit_form1 #currentPage").val();
	if(currentPage != null && currentPage != ""){
		var iCurrentPage = parseInt(currentPage);
		if(direction == "PREV"){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			iCurrentPage++;
		}
		
		$("#submit_form1 #currentPage").val(iCurrentPage);
	}else{
		$("#submit_form1 #currentPage").val("1");
	}
	fnSearch();
}

function fnGoPrevOrNext2(direction){
	
	if(direction == "PREV"){
		if($('#submit_form2 #prevBtn').linkbutton('options').disabled) return;
	}else if(direction == "NEXT"){
		if($('#submit_form2 #nextBtn').linkbutton('options').disabled) return;
	}
	
	var currentPage = $("#submit_form2 #currentPage").val();
	if(currentPage != null && currentPage != ""){
		var iCurrentPage = parseInt(currentPage);
		if(direction == "PREV"){
			if(iCurrentPage > 1){
				iCurrentPage--;
			}else{
				return;
			}
		}else if(direction == "NEXT"){
			iCurrentPage++;
		}
		
		$("#submit_form2 #currentPage").val(iCurrentPage);
	}else{
		$("#submit_form2 #currentPage").val("1");
	}
	fnSearch();
}

function myparser(s){
	if (!s) return new Date();
	var ss = s.split('-');
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

function fnSearch(){
	if(guideOrPrecedent == 1){
		$('#tableList0').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 가이드"," "); 
		
		ajaxCall("/SqlTuningGuideNewAction", $("#submit_form1"), "POST", callback_SqlTuningGuideAddTable);		
	}else if(guideOrPrecedent == 2){
		$('#tableList1').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 사례"," "); 
		
		ajaxCall("/PrecedentNewAction", $("#submit_form2"), "POST", callback_PrecedentAddTable);			
	}
	
}

function fnSearch2(arg1){
	if(arg1 == 1){
		$("#guide_div_cd").val("1");
		$('#tableList0').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 가이드"," "); 
		
		ajaxCall("/SqlTuningGuideNewAction", $("#submit_form1"), "POST", callback_SqlTuningGuideAddTable);		
	}else if(arg1 == 2){
		$("#guide_div_cd").val("2");
		$('#tableList1').datagrid("loadData", []);
		/* modal progress open */
		if(parent.openMessageProgress != undefined) parent.openMessageProgress("SQL 튜닝 사례"," "); 
		
		ajaxCall("/PrecedentNewAction", $("#submit_form2"), "POST", callback_PrecedentAddTable);			
	}
	
}

//callback 함수
var callback_SqlTuningGuideAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList0').datagrid("loadData", data);
	
	/* modal progress close */
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		
	
	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn1(dataLength);
};

//callback 함수
var callback_PrecedentAddTable = function(result) {
	var data = JSON.parse(result);
	$('#tableList1').datagrid("loadData", data);
	
	/* modal progress close */ 
	if(parent.closeMessageProgress != undefined) parent.closeMessageProgress();		

	var dataLength=0;
	dataLength = data.dataCount4NextBtn;
	fnEnableDisablePagingBtn2(dataLength);
};

function formValidationCheck1(){
	if($('#submit_form1 #selectValue').combobox('getValue') != "" && $('#submit_form1 #searchValue').textbox('getValue') == ""){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
	}
	if(($('#submit_form1 #selectValue').combobox('getValue') == "" && $('#submit_form1 #searchValue').textbox('getValue') != "")){
		parent.$.messager.alert('','검색조건을 선택해 주세요.');
		return false;
	}
	return true;
}

function formValidationCheck2(){
	if($('#submit_form2 #selectValue').combobox('getValue') != "" && $('#submit_form2 #searchValue').textbox('getValue') == ""){
		parent.$.messager.alert('','검색조건을 입력해 주세요.');
		return false;
	}
	if(($('#submit_form2 #selectValue').combobox('getValue') == "" && $('#submit_form2 #searchValue').textbox('getValue') != "")){
		parent.$.messager.alert('','검색조건을 선택해 주세요.');
		return false;
	}
	return true;
}

function Btn_OnClick(gubun){
	if(gubun == 1){
		$("#tab0ClickCount").val(1);
		guideOrPrecedent = 1;
		$("#submit_form1 #guide_div_cd").val("1");
		$("#submit_form1 #searchKey").val($('#submit_form1 #selectValue').combobox('getValue'));
		$("#submit_form1 #currentPage").val("1");
		$("#submit_form1 #searchBtnClickCount1").val("1");
		$("#submit_form2 #searchBtnClickCount1").val("1");
		if(!formValidationCheck1()){
			return;
		}
	}else if(gubun == 2){
		$("#tab1ClickCount").val(1);
		guideOrPrecedent = 2;
		$("#submit_form2 #guide_div_cd").val("2");
		$("#submit_form2 #searchKey").val($('#submit_form2 #selectValue').combobox('getValue'));
		$("#submit_form2 #currentPage").val("1");
		$("#submit_form1 #searchBtnClickCount2").val("1");
		$("#submit_form2 #searchBtnClickCount2").val("1");
		if(!formValidationCheck2()){
			return;
		}
	}

	/* iframe name에 사용된 menu_id를 상단 frameName에 설정 */
	parent.frameName = $("#submit_form1 #menu_id").val();
	
	fnSearch();
}

function Btn_InsertGuide(){
	location.href = "/PrecedentNew/Insert?" 
			+ "menu_id=" + $("#submit_form1 #menu_id").val() 
			+ "&searchBtnClickCount1=" + $("#submit_form1 #searchBtnClickCount1").val() 
			+ "&searchBtnClickCount2=" + $("#submit_form1 #searchBtnClickCount2").val() 
			+ "&guide_div_cd=1";
}

function getDetailView1(){
	$("#submit_form1").attr("action","/PrecedentNew/Update");
	$("#submit_form1").submit();	
}

function getDetailView2(){
	$("#submit_form2").attr("action","/PrecedentNew/Update");
	$("#submit_form2").submit();	
}

function Excel_DownClick(gubun){
	if(gubun == 1){
		if(($('#submit_form1 #selectValue').combobox('getValue') != "" && $('#submit_form1 #searchValue').textbox('getValue') == "") ||
				($('#submit_form1 #selectValue').combobox('getValue') == "" && $('#submit_form1 #searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
		}
		$("#submit_form1 #guide_div_cd").val("1");
		$("#submit_form1 #searchKey").val($('#submit_form1 #selectValue').combobox('getValue'));
		
		$("#submit_form1").attr("action","/PrecedentNew/SqlTuningGuideExcelDown");
		$("#submit_form1").submit();
	}else if(gubun == 2){
		if(($('#submit_form2 #selectValue').combobox('getValue') != "" && $('#submit_form2 #searchValue').textbox('getValue') == "") ||
				($('#submit_form2 #selectValue').combobox('getValue') == "" && $('#submit_form2 #searchValue').textbox('getValue') != "")){
			parent.$.messager.alert('','검색조건을 입력해 주세요.');
			return false;
		}
		$("#submit_form2 #guide_div_cd").val("2");
		$("#submit_form2 #searchKey").val($('#submit_form2 #selectValue').combobox('getValue'));
		
		$("#submit_form2").attr("action","/PrecedentNew/ExcelDown");
		$("#submit_form2").submit();
	}
}


function cellStyler(value,row,index){
		return 'color:#474038;';
}

function fnEnableDisablePagingBtn1(dataLength){
	//페이징 처리
	var currentPage = $("#submit_form1 #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form1 #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$('#submit_form1 #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form1 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form1 #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#submit_form1 #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form1 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form1 #nextBtn').linkbutton('disable');
		}
	}
}


function fnEnableDisablePagingBtn2(dataLength){
	//페이징 처리
	var currentPage = $("#submit_form2 #currentPage").val();
	var iCurrentPage = parseInt(currentPage);
	var pagePerCount = $("#submit_form2 #pagePerCount").val();
	var iPagePerCount = parseInt(pagePerCount);
	
	if(iCurrentPage > 1){
		$('#submit_form2 #prevBtn').linkbutton('enable');

		if(dataLength > pagePerCount){
			$('#submit_form2 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form2 #nextBtn').linkbutton('disable');
		}
	}
	if(iCurrentPage == 1){
		$('#submit_form2 #prevBtn').linkbutton('disable');
		if(dataLength > iPagePerCount){
			$('#submit_form2 #nextBtn').linkbutton('enable');
		}else{
			$('#submit_form2 #nextBtn').linkbutton('disable');
		}
	}
}

