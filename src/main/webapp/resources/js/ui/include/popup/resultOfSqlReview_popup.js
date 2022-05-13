$(document).ready(function() {
	$('#memoBox').window({
		title : 'SQL 검토결과',
		top:getWindowTop(315),
		left:getWindowLeft(1435),
		closable:false
	});
	
	$('#memoBox').window('close');
	
	setPopupPlaceHolder();
});

function setPopupPlaceHolder(){
	$('#memoEdit').focusin(function() {
		if ( $('#memoEdit').val().length <= 0 ) {
			$('#memoEdit').val('');
		}
	});
	$('#memoEdit').focusout(function() {
		if ( $('#memoEdit').val().length == 1 && $('#memoEdit').val() == '' ) {
			$('#memoEdit').val('');
		}
	});
}

function Btn_SetSQLmemo() {
	$('#memoBox').window({
		top:getWindowTop(315),
		left:getWindowLeft(1435)
	});
	
	// sql 검토결과조회
	ajaxCall('/AutoPerformanceCompareBetweenDatabase/Popup/getSqlMemo',
			$('#memo_form'),
			'POST',
			callback_getSqlMemo);
	
	$('#memoBox').window('open');
}

var callback_getSqlMemo = function(result) {
	if ( isNotEmpty( result ) && result.result ) {
		$('#memoEdit').val( result.message );
		$('#review_sbst').val( result.message );
		
	} else {
		$('#memoEdit').val('');
	}
}

/* SQL 검토결과 popup - 저장 */
function Btn_memoSave() {
	if ( $('#memo_form #memoEdit' ).val() == null || $('#memo_form #memoEdit' ).val() == '' ) {
		warningMessager('저장할 SQL검토결과를 입력해 주세요.');
		return false;
		
	} else {
		let param = '확인';
		let msgStr = '저장 하시겠습니까?';
		
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				const maxByte = 4000;
				const totalByte = byteLength( $('#memo_form #memoEdit').val() );
				
				if ( totalByte > maxByte ) {
					warningMessager('최대 4000Byte까지 저장이 가능합니다.<br>현재 : '+totalByte+'byte');
					return false;
					
				} else {
					$('#review_sbst').val( $('#memo_form #memoEdit').val() );
					
					ajaxCall('/AutoPerformanceCompareBetweenDatabase/Popup/updateSqlMemo',
							$('#memo_form'),
							'POST',
							callback_updateSqlMemo);
				}
			}
		});
	}
}
var callback_updateSqlMemo = function(result) {
	if ( isNotEmpty( result ) && result.result ) {
		infoMessager(result.message);
		
	} else {
		warningMessager(result.message);
	}
	
	$('#memoBox').window('close');
}

/* SQL 검토결과 popup - 삭제 */
function Btn_memoDelete() {
	if ( $('#review_sbst').val() == null || $('#review_sbst').val() == '' ) {
		warningMessager('삭제할 SQL검토결과가 없습니다.');
		return false;
		
	} else {
		let param = '확인';
		let msgStr = '삭제 하시겠습니까?';
		
		parent.$.messager.confirm( param,msgStr,function(r) {
			if (r) {
				ajaxCall('/AutoPerformanceCompareBetweenDatabase/Popup/deleteSqlMemo',
						$('#memo_form'),
						'POST',
						callback_deleteSqlMemo);
			}
		});
	}
}
var callback_deleteSqlMemo = function(result) {
	if ( isNotEmpty( result ) && result.result ) {
		infoMessager(result.message);
		
		$('#memoEdit').val( '' );
		
	} else {
		warningMessager(result.message);
		
		$('#memo_form #memoEdit').val( $('#review_sbst').val() );
	}
	$('#memoBox').window('close');
}