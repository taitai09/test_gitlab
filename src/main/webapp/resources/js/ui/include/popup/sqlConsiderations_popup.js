$(document).ready(function() {
	$('#sqlConsiderationsPop').window({
		title : "DB 변경 SQL 고려사항",
		top:0,
		left:getWindowLeft(1200)
	});
	
});


function Btn_ShowSqlConsiderationPop() {
	$('#sqlConsiderationsPop').window("open");
	$('#sqlConsiderationsPop').window('move',{
		top: 30,
		left:getWindowLeft(1200)
	});
	
	location.href='#sqlConsideration';
}