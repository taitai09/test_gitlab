/**
 * autoSelectionDesign.js 참조
 * @returns
 */

$(document).ready(function(){
	console.log("index :",index);
	/** 일 종합 진단 탭 */
	$("#dailyFullCheckDesignTab").tabs({
		plain: true,
		onSelect: function(title,index){
			console.log("index :",index);
			if(index == 0){
				console.log("dailyFullCheckDesignIF1.src :["+document.getElementById('dailyFullCheckDesignIF1').src+"]");
				var dailyFullCheckDesignIF1Src = document.getElementById('dailyFullCheckDesignIF1').src;
				if(dailyFullCheckDesignIF1Src == undefined || dailyFullCheckDesignIF1Src == ""){
					document.getElementById('dailyFullCheckDesignIF1').src="/DailyCheckDb";
				}
			}
		}
	});
});
