$(document).ready(function(){
	if( parent.subTabTitle != "" ){
		document.getElementById('requestException').src="/execSqlPerfCheck/requestException?menu_id="+$("#menu_id").val();
		
	}else {
		document.getElementById('perfInspectMng').src="/execSqlPerfCheck/perfInspectMng?menu_id="+$("#menu_id").val();
	}
	
	$("#execSqlPerfCheckTab").tabs({
		plain: true,
		selected: 0,
		onSelect: function(title,index){
			switch(index) {
			case 0:
				let perfInspectMng = document.getElementById('perfInspectMng').src;
				if ( perfInspectMng == undefined || perfInspectMng == "" ) {
					document.getElementById('perfInspectMng').src="/execSqlPerfCheck/perfInspectMng?menu_id="+$("#menu_id").val();
				}
				break;
				
			case 1:
				try{
					let requestException = document.getElementById('requestException').src;
					if ( requestException == undefined || requestException == "" ) {
						document.getElementById('requestException').src="/execSqlPerfCheck/requestException";
					}
				}catch(e) {
					console.log("requestException tab is not exist");
				}
				break;
				
			case 2:
				let deleteException = document.getElementById('deleteException').src;
				if ( deleteException == undefined || deleteException == "" ) {
					document.getElementById('deleteException').src="/execSqlPerfCheck/deleteException";
				}
				break;
				
			case 3:
				let exceptionProcessHistory = document.getElementById('exceptionProcessHistory').src;
				if ( exceptionProcessHistory == undefined || exceptionProcessHistory == "" ) {
					document.getElementById('exceptionProcessHistory').src="/execSqlPerfCheck/exceptionProcessHistory";
				}
				break;
				
			default:
				break;
			}
		}
	});
	
	if( parent.subTabTitle != "" ){
		$("#execSqlPerfCheckTab").tabs({ selected: 1 });
		
	}else {
		$("#execSqlPerfCheckTab").tabs({ selected: 0 });
	}
});