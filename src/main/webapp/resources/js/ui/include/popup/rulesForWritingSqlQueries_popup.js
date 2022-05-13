$(document).ready(function() {
	const newLocal = $('#rulesForWritingSqlQueriesPopup').window({
		title:"RULE 작성 규칙",
		top:getWindowTop(600),
		left:getWindowLeft(600)
	});
});

function Btn_ClosePopup() {
	Btn_OnClosePopup("rulesForWritingSqlQueriesPopup");
}