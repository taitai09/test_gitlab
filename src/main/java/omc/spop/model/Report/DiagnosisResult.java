package omc.spop.model.Report;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.controller.DiagnosisReportController;
import omc.spop.model.ReportHtml;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2019.10.23 명성태 최초작성
 **********************************************************/

@Alias("diagnosisResult")
public class DiagnosisResult extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();
	
	public String P020  = reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<script> \n" + 
			"$(document).ready (function(){" +
			"\t$('body').css('overflow-x','hidden');\n" +
			"\t$('body').css('height','calc( 100% - 30px)');\n" +
			"\t$('.om2').css('width','calc(100% - 50px)');\n" +
			"\t$('.om2-b20').css('width','calc(100% - 50px)');\n" +
			"});\n" +
			"function rowFilter(obj , text){\n" +
			"\tlet objArr = $(\"tbody tr td:nth-child(2) font\");\n" +
			"\tlet length = objArr.length\n" +
			
			"\tif(obj.className === 'result_status_on') {\n" +
			"\t\tobj.className = 'result_status_off';\n" +
			"\t}else{\n" +
			"\t\tobj.className = 'result_status_on'\n" +
			"\t}\n" +
			"\tfor(let iter = 0 ; iter < length ; iter++){ \n" +
			"\t\tif(objArr[iter].innerHTML === text){\n" +
			"\t\t\tif(obj.className === 'result_status_off'){\n" +
			"\t\t\t\tobjArr[iter].parentNode.parentNode.parentNode.hidden=true;\n" +
			"\t\t\t}else{\n" +
			"\t\t\t\tobjArr[iter].parentNode.parentNode.parentNode.hidden=false;\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}\n" +
			"function callFunction2( msg ) { \n" +
			"\tmsg = '{\"func\":\"callFunction2\",\"param1\":\"'+ msg + '\"}'; \n" +
			"\twindow.parent.postMessage( msg, '*' ); \n " +
			"}\n" +
			"</script>\n" +
			"<div id=\"P020\">\n" +
			"\t<h1 class=\"title\" name=\"P020\">진단 결과 요약</h1>" +
//			reportHtml.tableNoLineStyle(1) +
//			reportHtml.tableNoLineStyleBody(1, "우수("+ reportHtml.getIconExcellent() + ")") +
//			reportHtml.tableNoLineStyleBody(2, ":") +
//			reportHtml.tableNoLineStyleBody(3, "조치 사항 없음") +
//			reportHtml.tableNoLineStyleBody(1, "양호("+ reportHtml.getIconGood() + ")") +
//			reportHtml.tableNoLineStyleBody(2, ":") +
//			reportHtml.tableNoLineStyleBody(3, "금일 조치 사항이 없지만 BASE_PERIOD_VALUE 동안 조치 사항 있음") +
//			reportHtml.tableNoLineStyleBody(1, "조치필요("+ reportHtml.getIconAction() + ")") +
//			reportHtml.tableNoLineStyleBody(2, ":") +
//			reportHtml.tableNoLineStyleBody(3, "비 긴급성 조치 필요") +
//			reportHtml.tableNoLineStyleBody(1, "긴급조치(" + reportHtml.getIconEmergency() + ")") +
//			reportHtml.tableNoLineStyleBody(2, ":") +
//			reportHtml.tableNoLineStyleBody(3, "긴급성 조치 필요") +
//			reportHtml.tableNoLineStyleBodyClose(1, "확인필요(" + reportHtml.getIconConfirm() + ")") +
//			reportHtml.tableNoLineStyleBodyClose(2, ":") +
//			reportHtml.tableNoLineStyleBodyClose(3, "진단결과 확인 필요") +

			"\t<div style=\"text-align:center;\">\n" +
//			"\t\t<span style=\"padding-left:100px;\">\n" +
			"\t\t<div class=\"result_total_count_1\">\n" +
			"\t\t\t<div class=\"rtc_title\">긴급조치</div>\n" +
			"\t\t\t<div id=\"rtc_1\" class=\"rtc_value\"></div>\n" +
			"\t\t</div>\n" +
			"\t\t\t<span style=\"padding-left:40px;\">\n" +
			"\t\t<div class=\"result_total_count_2\">\n" +
			"\t\t\t<div class=\"rtc_title\">조치필요</div>\n" +
			"\t\t\t<div id=\"rtc_2\" class=\"rtc_value\"></div>\n" +
			"\t\t</div>" +
			"\t\t\t<span style=\"padding-left:40px;\">\n" +
			"\t\t<div class=\"result_total_count_3\">\n" +
			"\t\t\t<div class=\"rtc_title\">확인필요</div>\n" +
			"\t\t\t<div id=\"rtc_3\" class=\"rtc_value\"></div>\n" +
			"\t\t</div>" +
			"\t</div>" +
			"</div>\n";
	
	private String P106 = "<div id=\"P106\">\n" +
//			"\t<h2 class=\"title\" name=\"P106\">&middot; DB 진단</h2>\n" + 
//			"\t<h2 class=\"title\" name=\"P106\">&middot; DB 진단<span class=\"result_status\"/></h2>\n" + 
			"\t<h2 class=\"title\" name=\"P106\">&middot; DB 진단<span class=\"result_status\">"
			+"<a style='cursor:pointer' onclick='javascript:rowFilter(this,\"◎\");' class='result_status_on'><img src = '../images/result_status_1.jpg'></img></a>"
			+"<a style='cursor:pointer' onclick='javascript:rowFilter(this,\"○\");' class='result_status_on'><img src = '../images/result_status_2.jpg'></img></a>"
			+"<a style='cursor:pointer' onclick='javascript:rowFilter(this,\"△\");' class='result_status_on'><img src = '../images/result_status_3.jpg'></img></a>"
			+"<a style='cursor:pointer' onclick='javascript:rowFilter(this,\"▽\");' class='result_status_on'><img src = '../images/result_status_4.jpg'></img></a>"
			+"<a style='cursor:pointer' onclick='javascript:rowFilter(this,\"□\");' class='result_status_on'><img src = '../images/result_status_5.jpg'></img></a>"

			+"</span>"
			+ "</h2>\n" + 
			
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P113';\" style=\"cursor: pointer\">" + "Expired(grace) 계정" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P113'); return false;\" style=\"cursor: pointer\">" + "Expired(grace) 계정" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10006_var2", 1) +
			reportHtml.tableStyle1Body(2, "계정 상태가 expired(grace)인 계정이 존재하는지 진단", 0) +
			reportHtml.tableStyle1Body(3, "Expired(grace) 계정 : " + "10006_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P115';\" style=\"cursor: pointer\">" + "파라미터 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P115'); return false;\" style=\"cursor: pointer\">" + "파라미터 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10007_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 동안 변경된 파라미터가 존재하는지 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 변경된 파라미터 : " + "10007_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P116';\" style=\"cursor: pointer\">" + "DB File 생성률" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P116'); return false;\" style=\"cursor: pointer\">" + "DB File 생성률" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10008_var2", 1) +
			reportHtml.tableStyle1Body(2, "DB Files 파라미터 값과 실제 생성된 파일의 생성률(%) 진단", 0) +
			reportHtml.tableStyle1Body(3, "DB File 생성률 : " + "10008_var1" + " %", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P117';\" style=\"cursor: pointer\">" + "Library Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P117'); return false;\" style=\"cursor: pointer\">" + "Library Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10009_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE Library Cache Hit율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Hit율 초과 인스턴스 : " + "10009_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P118';\" style=\"cursor: pointer\">" + "Dictionary Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P118'); return false;\" style=\"cursor: pointer\">" + "Dictionary Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10010_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE Dictionary Cache Hit율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Hit율 초과 인스턴스 : " + "10010_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P119';\" style=\"cursor: pointer\">" + "Buffer Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P119'); return false;\" style=\"cursor: pointer\">" + "Buffer Cache Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10011_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE  Buffer Cache Hit율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Hit율 초과 인스턴스 : " + "10011_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P120';\" style=\"cursor: pointer\">" + "Latch Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P120'); return false;\" style=\"cursor: pointer\">" + "Latch Hit Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10012_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE Latch Hit율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Hit율 초과 인스턴스 : " + "10012_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P121';\" style=\"cursor: pointer\">" + "Parse CPU To Parse Elapsed Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P121'); return false;\" style=\"cursor: pointer\">" + "Parse CPU To Parse Elapsed Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10013_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 파싱 총 소요 시간 중 CPU time이 차지한 비율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 사용률 초과 인스턴스 : " + "10013_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P122';\" style=\"cursor: pointer\">" + "Disk Sort Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P122'); return false;\" style=\"cursor: pointer\">" + "Disk Sort Ratio" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10014_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE의 Disk Sort율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Sort율 초과 인스턴스 : " + "10014_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P123';\" style=\"cursor: pointer\">" + "Shared Pool Usage" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P123'); return false;\" style=\"cursor: pointer\">" + "Shared Pool Usage" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10015_var2", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 동안 Shared Pool 내에서 현재 사용 중인 메모리 비율 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 사용률 초과 인스턴스 : " + "10015_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P124';\" style=\"cursor: pointer\">" + "Resource Limit" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P124'); return false;\" style=\"cursor: pointer\">" + "Resource Limit" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10016_var2", 1) +
			reportHtml.tableStyle1Body(2, "Processes, Session 등의 Resource 사용량 진단", 0) +
			reportHtml.tableStyle1Body(3, "임계값 초과 Resource : " + "10016_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P202';\" style=\"cursor: pointer\">" + "FRA Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P202'); return false;\" style=\"cursor: pointer\">" + "FRA Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10020_var5", 1) +
			reportHtml.tableStyle1Body(2, "FRA Space 진단", 0) +
			reportHtml.tableStyle1Body(3, "현재 사용량 : 10020_var1 GB / 10020_var2 GB( 10020_var3 % )<br>" + "BASE_PERIOD_VALUE 최대 사용량 : 10020_var4 %", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P204';\" style=\"cursor: pointer\">" + "FRA Usage Detail" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P204'); return false;\" style=\"cursor: pointer\">" + "FRA Usage Detail" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10021_var2", 1) +
			reportHtml.tableStyle1Body(2, "FRA Usage Detail 진단", 0) +
			reportHtml.tableStyle1Body(3, "임계값 초과 FRA Component : 10021_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P129';\" style=\"cursor: pointer\">" + "ASM Diskgroup Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P129'); return false;\" style=\"cursor: pointer\">" + "ASM Diskgroup Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10022_var2", 1) +
			reportHtml.tableStyle1Body(2, "ASM Diskgroup Space 진단", 0) +
			reportHtml.tableStyle1Body(3, "임계값 초과 Diskgroup : 10022_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P130';\" style=\"cursor: pointer\">" + "Tablespace Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P130'); return false;\" style=\"cursor: pointer\">" + "Tablespace Space" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10023_var2", 1) +
			reportHtml.tableStyle1Body(2, "테이블스페이스 사용량 진단", 0) +
			reportHtml.tableStyle1Body(3, "임계값 초과 Tablespace : 10023_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P131';\" style=\"cursor: pointer\">" + "Recyclebin Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P131'); return false;\" style=\"cursor: pointer\">" + "Recyclebin Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10024_var3", 1) +
			reportHtml.tableStyle1Body(2, "Recyclebin Object 사용량 진단", 0) +
			reportHtml.tableStyle1Body(3, "Recycled Object 수 : 10024_var1 개<br>" + "Recycled Object 사이즈 : 10024_var2 GB", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P132';\" style=\"cursor: pointer\">" + "Invalid Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P132'); return false;\" style=\"cursor: pointer\">" + "Invalid Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10025_var2", 1) +
			reportHtml.tableStyle1Body(2, "Invalid Object 진단", 0) +
			reportHtml.tableStyle1Body(3, "Invalid Object : 10025_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P133';\" style=\"cursor: pointer\">" + "Nologging Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P133'); return false;\" style=\"cursor: pointer\">" + "Nologging Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10026_var2", 1) +
			reportHtml.tableStyle1Body(2, "Nologging Object 진단", 0) +
			reportHtml.tableStyle1Body(3, "Nologging Object : 10026_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P134';\" style=\"cursor: pointer\">" + "Parallel Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P134'); return false;\" style=\"cursor: pointer\">" + "Parallel Object" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10027_var2", 1) +
			reportHtml.tableStyle1Body(2, "Parallel Object를 진단", 0) +
			reportHtml.tableStyle1Body(3, "Parallel Object : 10027_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P135';\" style=\"cursor: pointer\">" + "Unusable Index" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P135'); return false;\" style=\"cursor: pointer\">" + "Unusable Index" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10028_var2", 1) +
			reportHtml.tableStyle1Body(2, "Unusable Index를 진단", 0) +
			reportHtml.tableStyle1Body(3, "Unusable Index : 10028_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P136';\" style=\"cursor: pointer\">" + "Chained Rows Table" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P136'); return false;\" style=\"cursor: pointer\">" + "Chained Rows Table" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10029_var2", 1) +
			reportHtml.tableStyle1Body(2, "1% 이상 Chain 발생 테이블 진단", 0) +
			reportHtml.tableStyle1Body(3, "Chain 테이블 : 10029_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P137';\" style=\"cursor: pointer\">" + "Corrupt Block" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P137'); return false;\" style=\"cursor: pointer\">" + "Corrupt Block" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10030_var2", 1) +
			reportHtml.tableStyle1Body(2, "마지막 백업 후에 Corrupt 된 블록 진단", 0) +
			reportHtml.tableStyle1Body(3, "Corrupt 블럭 : 10030_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P138';\" style=\"cursor: pointer\">" + "Sequence Threshold  Exceeded" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P138'); return false;\" style=\"cursor: pointer\">" + "Sequence Threshold  Exceeded" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10031_var2", 1) +
			reportHtml.tableStyle1Body(2, "Sequence cur_val 사용량 진단", 0) +
			reportHtml.tableStyle1Body(3, "임계값 초과 Sequence : 10031_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P139';\" style=\"cursor: pointer\">" + "Foreign Keys Without Index" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P139'); return false;\" style=\"cursor: pointer\">" + "Foreign Keys Without Index" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10032_var2", 1) +
			reportHtml.tableStyle1Body(2, "Foreign Key에 인덱스가 없는 테이블 진단", 0) +
			reportHtml.tableStyle1Body(3, "인덱스 없는 Foreign Key  : 10032_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P140';\" style=\"cursor: pointer\">" + "Disabled Constraint" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P140'); return false;\" style=\"cursor: pointer\">" + "Disabled Constraint" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10033_var2", 1) +
			reportHtml.tableStyle1Body(2, "Disabled 된 제약조건 진단<br>" + "- Primary key, Unique key, Referential Integrity Constraint, Check", 0) +
			reportHtml.tableStyle1Body(3, "Disabled 된 제약조건  : 10033_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P141';\" style=\"cursor: pointer\">" + "Stale Statistics" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P141'); return false;\" style=\"cursor: pointer\">" + "Stale Statistics" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10034_var2", 1) +
			reportHtml.tableStyle1Body(2, "데이터와 통계정보 불일치  테이블 진단", 0) +
			reportHtml.tableStyle1Body(3, "통계정보 불일치(Missing or Stale) 테이블 : 10034_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P142';\" style=\"cursor: pointer\">" + "Statistics Locked Table" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P142'); return false;\" style=\"cursor: pointer\">" + "Statistics Locked Table" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10035_var2", 1) +
			reportHtml.tableStyle1Body(2, "통계정보  Lock 테이블 진단", 0) +
			reportHtml.tableStyle1Body(3, "통계정보 Lock 테이블 : 10035_var1 개", 0) +

//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P143';\" style=\"cursor: pointer\">" + "Long Running Operation" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P143'); return false;\" style=\"cursor: pointer\">" + "Long Running Work" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10036_var3", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 동안 업무 시작 전(08시)까지 쿼리가 종료되지 않을 것으로 예상되는 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 발생 건수 : 10036_var1 건<br>" + "BASE_PERIOD_VALUE 일별 최대 발생 건수 : 10036_var2 건", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P144';\" style=\"cursor: pointer\">" + "Long Running Job" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P144'); return false;\" style=\"cursor: pointer\">" + "Long Running Job" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10037_var3", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 동안 오라클 스케줄러에서 실행되는 JOB 중 1일 이상 수행하는 JOB이 존재하는지 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 발생 건수 : 10037_var1 건<br>" + "BASE_PERIOD_VALUE  일별 최대 발생 건수 : 10037_var2 건", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P142';\" style=\"cursor: pointer\">" + "Statistics Locked Table" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P240'); return false;\" style=\"cursor: pointer\">" + "Scheduler Job 실패" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10319_var3", 1) +
			reportHtml.tableStyle1Body(2, "진단기간 동안 오라클 스케줄러에서 실행되는 JOB이 실패했는지 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 발생 건수 : 10319_var1 건<br>" + "BASE_PERIOD_VALUE 일별 최대 발생 건수 : 10319_var2 건", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P146';\" style=\"cursor: pointer\">" + "Alert Log Error" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P146'); return false;\" style=\"cursor: pointer\">" + "Alert Log Error" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10038_var3", 1) +
			reportHtml.tableStyle1Body(2, "BASE_PERIOD_VALUE 동안 Alert log 에 기록되는 오라클 오류 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 발생 건수 : 10038_var1 건<br>" + "BASE_PERIOD_VALUE 일별 최대 발생 건수 : 10038_var2 건", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P147';\" style=\"cursor: pointer\">" + "Active Incident" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P147'); return false;\" style=\"cursor: pointer\">" + "Active Incident" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10039_var3", 1) +
			reportHtml.tableStyle1Body(2, "Alert로그에 기록되는 Error를 Problem(문제)와 Incident(사건)으로 구분하여 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE Problem 발생 건수 : 10039_var1 일<br>" + "BASE_PERIOD_VALUE Incident 발생 건수 : 10039_var2 건", 0) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P148';\" style=\"cursor: pointer\">" + "Outstanding Alert" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P148'); return false;\" style=\"cursor: pointer\">" + "Outstanding Alert" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10040_var3", 1) +
			reportHtml.tableStyle1BodyClose(2, "DB에서 발생한 Outstanding Alert 이슈를 진단", 0) +
			reportHtml.tableStyle1BodyClose(3, "BASE_PERIOD_VALUE 발생 건수 : 10040_var1 건<br>" + "BASE_PERIOD_VALUE 일별 최대 발생 건수 : 10040_var2 건", 0) +
			
			"</div>\n";
	
	private String P107 = "<div id=\"P107\">\n" +
			"\t<h2 class=\"title\" name=\"P107\">&middot; 오브젝트 진단</h2>\n" + 
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P150';\" style=\"cursor: pointer\">" + "Reorg 대상 진단" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P150'); return false;\" style=\"cursor: pointer\">" + "Reorg 대상 진단" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10041_var5", 1) +
			reportHtml.tableStyle1Body(2, "단편화가 발생하여 Reorg 작업 후에 재사용 공간 반환율이 20% 이상인 오브젝트 진단", 0) +
			reportHtml.tableStyle1Body(3, "Reorg 대상 오브젝트 수 : 10041_var1 개<br>" + "Reorg 전 전체 사이즈 : 10041_var2 GB<br>" + 
					"Reorg 후 공간 반환 사이즈 : 10041_var3 GB<br>" + "Reorg 후 공간 반환율 : 10041_var4 %", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P151';\" style=\"cursor: pointer\">" + "파티셔닝 대상 진단" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P151'); return false;\" style=\"cursor: pointer\">" + "파티셔닝 대상 진단" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10042_var2", 1) +
			reportHtml.tableStyle1Body(2, "테이블 사이즈/데이터 건수 증가 추이를 고려하여 파티셔닝이 필요한 테이블 진단", 0) +
			reportHtml.tableStyle1Body(3, "파티셔닝 대상 테이블  : 10042_var1" + " 개", 0) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P152';\" style=\"cursor: pointer\">" + "인덱스 사용 진단" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P152'); return false;\" style=\"cursor: pointer\">" + "인덱스 사용 진단" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10043_var2", 1) +
			reportHtml.tableStyle1BodyClose(2, "DB에서 실제 수행된 SQL의 Plan정보를 통해 미사용 인덱스 진단", 0) +
			reportHtml.tableStyle1BodyClose(3, "BASE_PERIOD_VALUE 미사용 인덱스 : 10043_var1 개", 0) +
			
			"</div>\n";
	
	private String P108 = "<div id=\"P108\">\n" +
			"\t<h2 class=\"title\" name=\"P108\">&middot; 파라미터 진단</h2>\n" + 
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P154';\" style=\"cursor: pointer\">" + "RAC 인스턴스 간 다른 파라미터" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P154'); return false;\" style=\"cursor: pointer\">" + "RAC 인스턴스 간 다른 파라미터" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10044_var2", 1) +
			reportHtml.tableStyle1BodyClose(2, "RAC 인스턴스 간에 다르게 설정한 파라미터가 존재하는지 진단", 0) +
			reportHtml.tableStyle1BodyClose(3, "인스턴스 간에 다른 파라미터 : 10044_var1" + " 개", 0) +
			
			"</div>\n";
	
	private String P109 = "<div id=\"P109\">\n" +
			"\t<h2 class=\"title\" name=\"P109\">&middot; SQL 성능 진단</h2>\n" + 
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P158';\" style=\"cursor: pointer\">" + "Plan 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P158'); return false;\" style=\"cursor: pointer\">" + "Plan 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10045_var2", 1) +
			reportHtml.tableStyle1Body(2, "Plan이 변경되어 설정한 임계값 이상으로 성능이 저하된 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Plan 변경 SQL수  : 10045_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P159';\" style=\"cursor: pointer\">" + "신규 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P159'); return false;\" style=\"cursor: pointer\">" + "신규 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10046_var2", 1) +
			reportHtml.tableStyle1Body(2, "설정한 성능 임계값을 초과하는 신규SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 신규 SQL수  : 10046_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P160';\" style=\"cursor: pointer\">" + "Literal SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P160'); return false;\" style=\"cursor: pointer\">" + "Literal SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10047_var2", 1) +
			reportHtml.tableStyle1Body(2, "바인드 처리하지 않는 Literal SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Literal SQL수  : 10047_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P161';\" style=\"cursor: pointer\">" + "Temp 과다 사용 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P161'); return false;\" style=\"cursor: pointer\">" + "Temp 과다 사용 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10048_var2", 1) +
			reportHtml.tableStyle1Body(2, "Temporary Tablespace 를 많이 사용하는 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Temp 과다 사용 SQL수 : 10048_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P162';\" style=\"cursor: pointer\">" + "Full Scan SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P162'); return false;\" style=\"cursor: pointer\">" + "Full Scan SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10049_var2", 1) +
			reportHtml.tableStyle1Body(2, "Table Full Scan 또는 Index Full Scan을 하면서 성능과 테이블 데이터 건수가 설정한 임계값을 초과하는 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Full Scan SQL수  : 10049_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P163';\" style=\"cursor: pointer\">" + "조건 없는 Delete" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P163'); return false;\" style=\"cursor: pointer\">" + "조건 없는 Delete" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10050_var2", 1) +
			reportHtml.tableStyle1Body(2, "Where 조건이 없는 delete SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 조건절 없는 SQL수  : 10050_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P164';\" style=\"cursor: pointer\">" + "Offload 비효율 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P164'); return false;\" style=\"cursor: pointer\">" + "Offload 비효율 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10051_var3", 1) +
			reportHtml.tableStyle1Body(2, "Offload 효율이 10051_var1% 이하인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Offload비효율 SQL수  : 10051_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P165';\" style=\"cursor: pointer\">" + "Offload 효율저하 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P165'); return false;\" style=\"cursor: pointer\">" + "Offload 효율저하 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10052_var3", 1) +
			reportHtml.tableStyle1Body(2, "전주 대비 Offload 효율이 10052_var1% 이상 저하된 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Offload효율저하 SQL수  : 10052_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P166';\" style=\"cursor: pointer\">" + "TOP Elapsed Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P166'); return false;\" style=\"cursor: pointer\">" + "TOP Elapsed Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10053_var3", 1) +
			reportHtml.tableStyle1Body(2, "Elapsed Time 기준 Activity가 10053_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10053_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P167';\" style=\"cursor: pointer\">" + "TOP CPU Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P167'); return false;\" style=\"cursor: pointer\">" + "TOP CPU Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10054_var3", 1) +
			reportHtml.tableStyle1Body(2, "CPU Time 기준 Activity가 10054_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10054_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P168';\" style=\"cursor: pointer\">" + "TOP Buffer Gets SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P168'); return false;\" style=\"cursor: pointer\">" + "TOP Buffer Gets SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10055_var3", 1) +
			reportHtml.tableStyle1Body(2, "Buffer Gets 기준 Activity가 10055_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10055_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P169';\" style=\"cursor: pointer\">" + "TOP Physical Reads SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P169'); return false;\" style=\"cursor: pointer\">" + "TOP Physical Reads SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10056_var3", 1) +
			reportHtml.tableStyle1Body(2, "Physical Reads 기준 Activity가 10056_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10056_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P170';\" style=\"cursor: pointer\">" + "TOP Executions SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P170'); return false;\" style=\"cursor: pointer\">" + "TOP Executions SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10057_var3", 1) +
			reportHtml.tableStyle1Body(2, "Executions 기준 Activity가 10057_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10057_var2 개", 0) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P171';\" style=\"cursor: pointer\">" + "TOP Cluster Wait SQL" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P171'); return false;\" style=\"cursor: pointer\">" + "TOP Cluster Wait SQL" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10058_var3", 1) +
			reportHtml.tableStyle1BodyClose(2, "Cluster Wait 기준 Activity가 10058_var1% 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1BodyClose(3, "BASE_PERIOD_VALUE 동안 발생 SQL수 : 10058_var2 개", 0) +
			
			"</div>\n";
	
	private String P109_noexadata = "<div id=\"P109\">\n" +
			"\t<h2 class=\"title\" name=\"P109\">&middot; SQL 성능 진단</h2>\n" + 
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P158';\" style=\"cursor: pointer\">" + "Plan 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P158'); return false;\" style=\"cursor: pointer\">" + "Plan 변경" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10045_var2", 1) +
			reportHtml.tableStyle1Body(2, "Plan이 변경되어 설정한 임계값 이상으로 성능이 저하된 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Plan 변경 SQL : 10045_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P159';\" style=\"cursor: pointer\">" + "신규 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P159'); return false;\" style=\"cursor: pointer\">" + "신규 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10046_var2", 1) +
			reportHtml.tableStyle1Body(2, "설정한 성능 임계값을 초과하는 신규SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 신규 SQL : 10046_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P160';\" style=\"cursor: pointer\">" + "Literal SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P160'); return false;\" style=\"cursor: pointer\">" + "Literal SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10047_var2", 1) +
			reportHtml.tableStyle1Body(2, "바인드 처리하지 않는 Literal SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Literal SQL : 10047_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P161';\" style=\"cursor: pointer\">" + "Temp 과다 사용 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P161'); return false;\" style=\"cursor: pointer\">" + "Temp 과다 사용 SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10048_var2", 1) +
			reportHtml.tableStyle1Body(2, "Temporary Tablespace 를 많이 사용하는 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Temp 과다 사용 SQL : 10048_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P162';\" style=\"cursor: pointer\">" + "Full Scan SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P162'); return false;\" style=\"cursor: pointer\">" + "Full Scan SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10049_var2", 1) +
			reportHtml.tableStyle1Body(2, "Table Full Scan 또는 Index Full Scan을 하면서 성능과 테이블 데이터 건수가 설정한 임계값을 초과하는 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 Full Scan SQL : 10049_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P163';\" style=\"cursor: pointer\">" + "조건 없는 Delete" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P163'); return false;\" style=\"cursor: pointer\">" + "조건 없는 Delete" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10050_var2", 1) +
			reportHtml.tableStyle1Body(2, "Where 조건이 없는 delete SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생한 조건절 없는 SQL : 10050_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P166';\" style=\"cursor: pointer\">" + "TOP Elapsed Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P166'); return false;\" style=\"cursor: pointer\">" + "TOP Elapsed Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10053_var3", 1) +
			reportHtml.tableStyle1Body(2, "Elapsed Time 기준 Activity가 10053_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10053_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P167';\" style=\"cursor: pointer\">" + "TOP CPU Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P167'); return false;\" style=\"cursor: pointer\">" + "TOP CPU Time SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10054_var3", 1) +
			reportHtml.tableStyle1Body(2, "CPU Time 기준 Activity가 10054_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10054_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P168';\" style=\"cursor: pointer\">" + "TOP Buffer Gets SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P168'); return false;\" style=\"cursor: pointer\">" + "TOP Buffer Gets SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10055_var3", 1) +
			reportHtml.tableStyle1Body(2, "Buffer Gets 기준 Activity가 10055_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10055_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P169';\" style=\"cursor: pointer\">" + "TOP Physical Reads SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P169'); return false;\" style=\"cursor: pointer\">" + "TOP Physical Reads SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10056_var3", 1) +
			reportHtml.tableStyle1Body(2, "Physical Reads 기준 Activity가 10056_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10056_var2 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P170';\" style=\"cursor: pointer\">" + "TOP Executions SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P170'); return false;\" style=\"cursor: pointer\">" + "TOP Executions SQL" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10057_var3", 1) +
			reportHtml.tableStyle1Body(2, "Executions 기준 Activity가 10057_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1Body(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10057_var2 개", 0) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P171';\" style=\"cursor: pointer\">" + "TOP Cluster Wait SQL" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P171'); return false;\" style=\"cursor: pointer\">" + "TOP Cluster Wait SQL" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10058_var3", 1) +
			reportHtml.tableStyle1BodyClose(2, "Cluster Wait 기준 Activity가 10058_var1 % 이상인 SQL 진단", 0) +
			reportHtml.tableStyle1BodyClose(3, "BASE_PERIOD_VALUE 동안 발생 SQL : 10058_var2 개", 0) +
			
			"</div>\n";
	
	private String P110 = "<div id=\"P110\">\n" +
			"\t<h2 class=\"title\" name=\"P110\">&middot; 장애 예측(자원한계점) 분석</h2>\n" + 
			"<dd class=\"contents2\">과거 특정 시점부터 현재 시점까지의 자원 사용량을 기반으로 미래 어느 시점에 해당 자원이 한계점에 도달하는지를 예측하여, 한계점에 도달하기 전에 선제적으로 조치함으로써 장애를 미연에 예방할 수 있도록 한다.</dd>\n" +
			reportHtml.tableStyle2Fixed(2) +
			reportHtml.tableStyle1Head(1, "a", "진단 항목", 20) +
			reportHtml.tableStyle1Head(2, "d", "결과 평가", 10) +
			reportHtml.tableStyle1Head(2, "b", "진단 내용", 40) +
			reportHtml.tableStyle1Head(3, "c", "진단 결과", 30) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P173';\" style=\"cursor: pointer\">" + "CPU 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P173'); return false;\" style=\"cursor: pointer\">" + "CPU 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10059_var2", 1) +
			reportHtml.tableStyle1Body(2, "과거 특정 시점부터 현재 시점까지의 CPU사용량을 기반으로 CPU사용량이 한계점 도달하는 미래 시점을 예측", 0) +
			reportHtml.tableStyle1Body(3, "현재 > 1개월후 > 3개월후 > 6개월후 > 12개월후 CPU 사용률(%)<br>" + "10059_var1", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P174';\" style=\"cursor: pointer\">" + "Sequence 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P174'); return false;\" style=\"cursor: pointer\">" + "Sequence 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10060_var2", 1) +
			reportHtml.tableStyle1Body(2, "과거 특정 시점부터 현재 시점까지의 Sequence 사용량을 기반으로 Sequence사용량이 한계점 도달하는 미래 시점을 예측", 0) +
			reportHtml.tableStyle1Body(3, "1개월 후 한계점 도달 : 10060_var1 개", 0) +
			
//			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"location.href='#P175';\" style=\"cursor: pointer\">" + "Tablespace 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(1, "<a class=\"om\" onclick=\"callFunction2('P175'); return false;\" style=\"cursor: pointer\">" + "Tablespace 한계점 예측" + "</a>", 0) +
			reportHtml.tableStyle1Body(2, "10061_var2", 1) +
			reportHtml.tableStyle1Body(2, "과거 특정 시점부터 현재 시점까지의 Tablespace 사용량을 기반으로 사용량이 한계점 도달하는 미래 시점을 예측", 0) +
			reportHtml.tableStyle1Body(3, "1개월 후 한계점 도달 : 10061_var1 개", 0) +
			
//			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"location.href='#P176';\" style=\"cursor: pointer\">" + "신규 SQL 타임아웃 예측" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(1, "<a class=\"om\" onclick=\"callFunction2('P176'); return false;\" style=\"cursor: pointer\">" + "신규 SQL 타임아웃 예측" + "</a>", 0) +
			reportHtml.tableStyle1BodyClose(2, "10062_var2", 1) +
			reportHtml.tableStyle1BodyClose(2, "이전에 수행되지 않았던 새로운 SQL 성능을 일정기간 모니터링하여 설정된 임계 값을 초과하는 시점을 예측", 0) +
			reportHtml.tableStyle1BodyClose(3, "1개월 후 타임아웃 발생: 10062_var1 개", 0) +
			
			"</div>\n";
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}

	public String getP020() {
		return P020;
	}

	public void setP020(String P020) {
		this.P020 = P020;
	}

	public String getP106() {
		return P106;
	}

	public void setP106(String p106) {
		P106 = p106;
	}

	public String getP107() {
		return P107;
	}

	public void setP107(String p107) {
		P107 = p107;
	}

	public String getP108() {
		return P108;
	}

	public void setP108(String p108) {
		P108 = p108;
	}

	public String getP109() {
		return P109;
	}

	public void setP109(String p109) {
		P109 = p109;
	}

	public String getP109_noexadata() {
		return P109_noexadata;
	}

	public void setP109_noexadata(String p109_noexadata) {
		P109_noexadata = p109_noexadata;
	}

	public String getP110() {
		return P110;
	}

	public void setP110(String p110) {
		P110 = p110;
	}
}
