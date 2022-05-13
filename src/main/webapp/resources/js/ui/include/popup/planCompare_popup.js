$(document).ready(function() {
	$('.scrollLeft').scroll(function(){
		$('.scrollRight').scrollTop( $('.scrollLeft').scrollTop() );
		$('.scrollRight').scrollLeft( $('.scrollLeft').scrollLeft() );
	})
	$('.scrollRight').scroll(function(){
		$('.scrollLeft').scrollTop( $('.scrollRight').scrollTop() );
		$('.scrollLeft').scrollLeft( $('.scrollRight').scrollLeft() );
	})
});

function drawDiffTable(result){
	$('.scrollLeft').scrollTop(0);
	$('.scrollRight').scrollTop(0);
	
	$('.scrollLeft').scrollLeft(0);
	$('.scrollRight').scrollLeft(0);
	
	$('#planComparePop').window({
		title : "PLAN 비교",
		top:getWindowTop(580),
		left:getWindowLeft(1600)
	});
	
	let compareResult = JSON.parse(result);
	
	let originLine = '';
	let newLine = '';
	
	if( result ){
		
		let emptyLine = '<tr class="EMPTY" style="height: 22px;">'
					  + '	<th>0</th>'
					  + '	<td> 해당 정보가 없습니다. </td>'
					  + '</tr>';
		
		if( Object.keys(compareResult).length > 0 ){
			let originTxtIsEmpty = 0;
			let newTxtIsEmpty = 0;
			
			for ( var i in compareResult ) {
				originLine += '<tr class="' + compareResult[i].tag + '1">';
				originLine += '		<th>'+ i +'</th>';
				originLine += '		<td>'+ strReplace( compareResult[i].originLine,' ','&nbsp;&nbsp;' ) +'</td>';
				originLine += '</tr>';
				
				newLine += '<tr class="' + compareResult[i].tag + '2">';
				newLine += '	<th>'+ i +'</th>';
				newLine += '	<td>'+ strReplace( compareResult[i].newLine,' ','&nbsp;&nbsp;' ) +'</td>';
				newLine += '</tr>';
				
				if(compareResult[i].tag == 'INSERT'){
					originTxtIsEmpty++;
				}
				if(compareResult[i].tag == 'DELETE'){
					newTxtIsEmpty++;
				}
				
				i++;
			}	//end of for
			
			if( Object.keys(compareResult).length == originTxtIsEmpty ) {
				// ASIS PLAN 전체가 공백인 경우
				originLine = emptyLine;
			}
			if( Object.keys(compareResult).length == newTxtIsEmpty ) {
				// TOBE PLAN 전체가 공백인 경우
				newLine = emptyLine;
			}
			
		}else if ( Object.keys(compareResult).length == 0 ) {
			originLine = emptyLine;
			newLine = emptyLine;
		}	//end of inner if
		
	} else if ( result != undefined || result == null) {
		parent.$.messager.alert('','데이터 조회중 오류가 발생하였습니다.');
		
		originLine += '<tr class="EMPTY" style="height: 22px;">'
		originLine += '		<th>0</th>';
		originLine += '		<td> 데이터 조회중 오류가 발생하였습니다. </td>';
		originLine += '</tr>';
		
		newLine += '<tr class="EMPTY" style="height: 22px;">'
		newLine += '	<th>0</th>';
		newLine += '	<td> 데이터 조회중 오류가 발생하였습니다. </td>';
		newLine += '</tr>';
		
		return false;
	}	//end of outer if
	
	$('.scrollLeft table tbody').html(originLine);
	$('.scrollRight table tbody').html(newLine);
	
	$('#planComparePop').window('open');  // open a window
}

function Btn_ClosePlanComparePop() {
	
	Btn_OnClosePopup('planComparePop');
}

