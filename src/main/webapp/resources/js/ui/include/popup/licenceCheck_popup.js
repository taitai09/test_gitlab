var licence_Arry;
$(document).ready(function() {
	licence_Arry = "";
	if ( auth_cd != null ){
		if ( auth_cd == "ROLE_DBMANAGER" || auth_cd == "ROLE_OPENPOPMANAGER") {
			createList();
			ajaxCallTableLicenceList();
		}
		
		if ( auth_cd == "ROLE_OPENPOPMANAGER") {
			createNoLicenceList();
			ajaxCallTableNoLicenceList();
		}
	}
	
	$("#week_yn").checkbox({
		value:"Y",
		checked: false,
		onChange:function( checked ) {
			if ( checked ) {
				let arry = licence_Arry.split(",");
				
				for (var listIdx = 0; listIdx < arry.length-1; listIdx++) {
					
					ajaxCall("/licence/updateCloseLicensePopupForWeek?licence_id="+arry[listIdx],
							null,
							"GET",
							null);
				}
				
				Btn_OnClosePopup('licenceCheckPop');
			}
		}
	});
	
});

function createList() {
	$("#licenceTableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'dbid',title:'DBID',width:'18%',halign:'center',align:'center'},
			{field:'db_name',title:'DB명',width:'18%',halign:'center',align:'center'},
			{field:'inst_id',title:'INSTANCE ID',width:'19%',halign:'center',align:'center'},
			{field:'licence_cpu_cnt',title:'계약 CPU 수',width:'15%',halign:'center',align:'right'},
			{field:'real_cpu_cnt',title:'실제 CPU 수',width:'15%',halign:'center',align:'right'},
			{field:'over_cpu_cnt',title:'초과 CPU 수',width:'15%',halign:'center',align:'right',styler:cellCntStyler}
		]],
		onLoadSuccess:function(data) {
			// datagrid 셀 병합.
			let cnt = 1;
			let idx = 0;
			for( let listIdx=1; listIdx <= data.rows.length; listIdx++ ) {
				
				if ( listIdx == data.rows.length ) {
					$('#licenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'dbid',
						rowspan: cnt
					});
					$('#licenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'db_name',
						rowspan: cnt
					});
					
				} else if ( data.rows[idx].dbid == data.rows[listIdx].dbid ) {
					cnt ++;
				} else {
					
					$('#licenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'dbid',
						rowspan: cnt
					});
					$('#licenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'db_name',
						rowspan: cnt
					});
					
					cnt = 1;
					idx = listIdx;
				}
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

function createNoLicenceList() {
	$("#noLicenceTableList").datagrid({
		view: myview,
		singleSelect: true,
		checkOnSelect : false,
		selectOnCheck : false,
		columns:[[
			{field:'dbid',title:'DBID',width:'20%',halign:'center',align:'center'},
			{field:'db_name',title:'DB명',width:'20%',halign:'center',align:'center'},
			{field:'inst_id',title:'INSTANCE ID',width:'20%',halign:'center',align:'center'},
			{field:'licence_cpu_cnt',title:'계약 CPU 수',width:'20%',halign:'center',align:'right',styler:cellCntStyler},
			{field:'real_cpu_cnt',title:'실제 CPU 수',width:'20%',halign:'center',align:'right'}
			]],
		onLoadSuccess:function(data) {
			// datagrid 셀 병합.
			let cnt = 1;
			let idx = 0;
			
			for( let listIdx=1; listIdx <= data.rows.length; listIdx++ ) {
				
				if ( listIdx == data.rows.length ) {
					$('#noLicenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'dbid',
						rowspan: cnt
					});
					$('#noLicenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'db_name',
						rowspan: cnt
					});
					
				} else if ( data.rows[idx].dbid == data.rows[listIdx].dbid ) {
					cnt ++;
				} else {
					
					$('#noLicenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'dbid',
						rowspan: cnt
					});
					$('#noLicenceTableList').datagrid('mergeCells',{
						index:idx,
						field:'db_name',
						rowspan: cnt
					});
					
					cnt = 1;
					idx = listIdx;
				}
			}
		},
		onLoadError:function() {
			parent.$.messager.alert('검색 에러','데이터 조회 중에 에러가 발생하였습니다.','error');
		}
	});
}

var callback_CloseLicensePopupForWeekAction = function(result) {
	if ( result.result ) {
		parent.$.messager.alert('','완료.','info');
	}
	
}

function ajaxCallTableLicenceList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("라이센스 CPU 조회"," ");

	/* 라이센스 CPU 리스트 */
	ajaxCall("/licence/getLicenseExceeded",
			null,
			"POST",
			callback_getLicenseExceededAction);
}

var callback_getLicenseExceededAction = function(result) {
	
	let data = JSON.parse(result);
	
	if ( data != null && data.rows.length > 0 ) {
		for (var listIdx = 0; listIdx < data.rows.length; listIdx++) {
			licence_Arry += data.rows[listIdx].licence_id+ " , ";
		}
	}
	setTimeout(function(){
		json_string_callback_common( result,'#licenceTableList',true );
	},300);
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}

function ajaxCallTableNoLicenceList() {
	/* modal progress open */
	if ( parent.openMessageProgress != undefined ) parent.openMessageProgress("라이센스 미셋팅 조회"," ");
	
	/* 라이센스 CPU 리스트 */
	ajaxCall("/licence/getNoLicense",
			null,
			"POST",
			callback_getNoLicenseAction);
}

var callback_getNoLicenseAction = function(result) {
	
	json_string_callback_common( result,'#noLicenceTableList',true );
	
	/* modal progress close */
	if ( parent.closeMessageProgress != undefined ) parent.closeMessageProgress();
}


function cellCntStyler( value,row,index ) {
	if ( row.over_cpu_cnt > 0 ) {
		return 'font-size:0px; background-image:url(/resources/images/up_arrow_red.png);background-repeat:no-repeat;background-position: center;background-size: 15px;';
	} else if ( row.licence_cpu_cnt == 0 && auth_cd == "ROLE_OPENPOPMANAGER" ){
		return 'font-size:0px; background-image:url(/resources/images/fail.png);background-repeat:no-repeat;background-position: center;background-size: 20px;';
	} else {
		return '';
	}
}

