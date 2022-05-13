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

@Alias("diagnosisResultDetailFault")
public class DiagnosisResultDetailFault extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

	private String P172 = reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P172\">\n" +
			"\t<h2 class=\"title\" name=\"P172\">&middot; 장애 예측(자원한계점) 분석</h2>\n" +
			"</div>\n";	
	private String P173 = "<div id=\"P173\">\n" +
			"\t<h3 class=\"title\" name=\"P173\">CPU 한계점 예측</h3>\n" +
			"\t<dd class=\"contents3\">과거 특정 시점부터 현재 시점까지의 CPU사용량을 기반으로 CPU사용량이 한계점 10208_var1%에 도달하는 미래 시점을 예측한다.</dd>\n" +
			"</div>\n";
	
	private String P283 = "<div id=\"P283\">\n" +
			"\t<h4 class=\"title\" name=\"P283\">CPU 한계점 예측 현황</h4>\n" + 
			"\t<div id=\"P283_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P284 = "<div id=\"P284\">\n" +
			"\t<h4 class=\"title\" name=\"P284\">CPU 사용률(%)</h4>\n" + 
			"\t<div id=\"P284_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P174 = "<div id=\"P174\">\n" +
			"\t<h3 class=\"title\" name=\"P174\">Sequence 한계점 예측</h3>\n" +
			"\t<dd class=\"contents3\">과거 특정 시점부터 현재 시점까지의 Sequence 사용량을 기반으로 Sequence사용량이 한계점 10215_var1%에 도달하는 미래 시점을 예측한다.</dd>\n" +
			"</div>\n";
	
	private String P285 = "<div id=\"P285\">\n" +
			"\t<h4 class=\"title\" name=\"P285\">1년 이내 한계점에 도달하는 Sequence</h4>\n" + 
			"\t<div id=\"P285_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P286 = "<div id=\"P286\">\n" +
			"\t<h4 class=\"title\" name=\"P286\">Sequence 사용량 증가 추이</h4>\n" + 
			"\t<div id=\"P286_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P175 = "<div id=\"P175\">\n" +
			"\t<h3 class=\"title\" name=\"P175\">Tablespace 한계점 예측</h3>\n" +
			"\t<dd class=\"contents3\">과거 특정 시점부터 현재 시점까지의 Tablespace 사용량을 기반으로 사용량이 한계점 10219_var1%에 도달하는 미래 시점을 예측한다.</dd>\n" +
			"</div>\n";
	
	private String P287 = "<div id=\"P287\">\n" +
			"\t<h4 class=\"title\" name=\"P287\">1개월 이내 한계점에 도달하는 Tablespace</h4>\n" + 
			"\t<div id=\"P287_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P288 = "<div id=\"P288\">\n" +
			"\t<h4 class=\"title\" name=\"P288\">Tablespace 사용량 증가 추이</h4>\n" + 
			"\t<div id=\"P288_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P176 = "<div id=\"P176\">\n" +
			"\t<h3 class=\"title\" name=\"P176\">신규 SQL 타임아웃 예측</h3>\n" +
			"\t<dd class=\"contents3\">이전에 수행되지 않았던 새로운 SQL을 발견하고, 해당 SQL의 성능을 일정기간 모니터링하여 설정된 임계값 10223_var1초를 초과하는 미래 시점을 예측한다.</dd>\n" +
			"</div>\n";
	
	private String P289 = "<div id=\"P289\">\n" +
			"\t<h4 class=\"title\" name=\"P289\">신규 SQL 발생건수</h4>\n" + 
			"\t<div id=\"P289_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P290 = "<div id=\"P290\">\n" +
			"\t<h4 class=\"title\" name=\"P290\">1개월 후 타임아웃 발생 신규 SQL수행시간 추이</h4>\n" + 
			"\t<div id=\"P290_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P291 = "<div id=\"P291\">\n" +
			"\t<h4 class=\"title\" name=\"P291\">1개월 후 타임아웃 발생 신규 SQL 현황</h4>\n" + 
			"\t<div id=\"P291_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P177 = "<div id=\"P177\" onclick='removeHlClass()'>\n" +
			"\t<h2 class=\"title\" name=\"P177\">&middot; SQL Text List</h2>\n" +
			"\t<div id=\"P177_T01\">\n" +
			"\t</div>\n" +
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

	public String getP172() {
		return P172;
	}

	public void setP172(String p172) {
		P172 = p172;
	}

	public String getP173() {
		return P173;
	}

	public void setP173(String p173) {
		P173 = p173;
	}

	public String getP283() {
		return P283;
	}

	public void setP283(String p283) {
		P283 = p283;
	}

	public String getP284() {
		return P284;
	}

	public void setP284(String p284) {
		P284 = p284;
	}

	public String getP174() {
		return P174;
	}

	public void setP174(String p174) {
		P174 = p174;
	}

	public String getP285() {
		return P285;
	}

	public void setP285(String p285) {
		P285 = p285;
	}

	public String getP286() {
		return P286;
	}

	public void setP286(String p286) {
		P286 = p286;
	}

	public String getP175() {
		return P175;
	}

	public void setP175(String p175) {
		P175 = p175;
	}

	public String getP287() {
		return P287;
	}

	public void setP287(String p287) {
		P287 = p287;
	}

	public String getP288() {
		return P288;
	}

	public void setP288(String p288) {
		P288 = p288;
	}

	public String getP176() {
		return P176;
	}

	public void setP176(String p176) {
		P176 = p176;
	}

	public String getP289() {
		return P289;
	}

	public void setP289(String p289) {
		P289 = p289;
	}

	public String getP290() {
		return P290;
	}

	public void setP290(String p290) {
		P290 = p290;
	}

	public String getP291() {
		return P291;
	}

	public void setP291(String p291) {
		P291 = p291;
	}

	public String getP177() {
		return P177;
	}

	public void setP177(String p177) {
		P177 = p177;
	}

}
