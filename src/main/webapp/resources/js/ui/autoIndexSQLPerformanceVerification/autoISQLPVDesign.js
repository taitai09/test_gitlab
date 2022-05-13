$(document).ready(function(){
	$('#autoISQLBVTab').tabs({
		plain: true,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				parent.openMessageProgress('인덱스 자동 분석',' ');
				
				let autoIndexAnalys = document.getElementById('autoIndexAnalys').src;
				if ( autoIndexAnalys == undefined || autoIndexAnalys == '' ) {
					document.getElementById('autoIndexAnalys').src='/AISQLPVDesign/autoIndexAnalys';
					
				} else {
					parent.closeMessageProgress();
				}
				break;
				
			case 1:
				parent.openMessageProgress('인덱스 검증',' ');
				
				let indexRecommend = document.getElementById('IndexRecommend').src;
				if ( indexRecommend == undefined || indexRecommend == '' ) {
					document.getElementById('IndexRecommend').src='/AISQLPVDesign/IndexRecommend';
					
				} else {
					parent.closeMessageProgress();
				}
				break;
				
			case 2:
				parent.openMessageProgress('인덱스별 성능 영향도 분석 결과',' ');
				
				let perfImpactByIndex = document.getElementById('perfImpactByIndex').src;
				if ( perfImpactByIndex == undefined || perfImpactByIndex == '' ) {
					document.getElementById('perfImpactByIndex').src='/AISQLPVDesign/perfImpactByIndex';
					
				} else {
					parent.closeMessageProgress();
				}
				break;
				
			case 3:
				parent.openMessageProgress('SQL점검팩',' ');
				
				let sqlCheck = document.getElementById('sqlCheck').src;
				if ( sqlCheck == undefined || sqlCheck == '' ) {
					document.getElementById('sqlCheck').src='/AISQLPVDesign/SqlCheck';
					
				} else {
					parent.closeMessageProgress();
				}
				break;
				
			default:
				console.log('Not exist Tab: index[' + index + '] title[' + title + ']');
				break;
			}
		}
	});
});

function moveToOtherTab( tabIndex, tabId, obj, timeOutSec, reTry ) {
	try{
		isFromParent = true;
		$('#autoISQLBVTab').tabs('select', tabIndex);
		
		if( isEmpty(timeOutSec) ){
			let existYn = document.getElementById( tabId ).contentWindow.document.getElementById('project_id') != null;
			timeOutSec = existYn ? 300 : 3500;
		}
		callMoveFromOtherTab(tabId, obj, timeOutSec, reTry);
		
	}catch(err){
		isFromParent = false;
		console.log('Error Occured', err);
	}
}
function moveToOtherTab2( tabIndex, tabId, parameter ) {
	let tabUrl = '/AISQLPVDesign/' + tabId + '?' + parameter;
	document.getElementById( tabId ).src = tabUrl;
	
	$('#autoISQLBVTab').tabs('select',tabIndex);
}
function callMoveFromOtherTab(tabId, obj, timeOutSec, reTry){
	try{
		let func = document.getElementById( tabId ).contentWindow.moveFromOtherTab
		
		if ( isEmpty(reTry) ){
			reTry = 3;
		}
		
		if(reTry > 0){
			let isEmptyFunc = isEmpty(func);
			
			console.log('is function loaded',!isEmptyFunc);
			console.log('reTry',reTry);
			
			setTimeout(function() {
				if(isEmptyFunc){
					callMoveFromOtherTab(tabId, obj, timeOutSec, reTry - 1);
					
				}else{
					func( obj );
				}
			}, timeOutSec)
			
		}else {
			isFromParent = false;
		}
		
	}catch(err){
		isFromParent = false;
		console.log('Error Occured', err);
	}
}

function reloadSqlPacCombo( projectId, sqlPerfId ) {
	document.getElementById('autoIndexAnalys').contentWindow.sqlPerfPacReload( projectId, sqlPerfId );
	
	if ( document.getElementById('IndexRecommend').contentWindow.document.getElementById('project_id') != null ) {
		document.getElementById('IndexRecommend').contentWindow.sqlPerfPacReload( projectId, sqlPerfId );
	}
	if ( document.getElementById('perfImpactByIndex').contentWindow.document.getElementById('project_id') != null ) {
		document.getElementById('perfImpactByIndex').contentWindow.sqlPerfPacReload( projectId, sqlPerfId );
	}
}