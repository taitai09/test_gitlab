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

@Alias("diagnosisOverview")
public class DiagnosisOverview extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

	public String P010  =  reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P010\">\n" +
			"\t<h1 class=\"top\" name=\"P010\">진단 개요</h1>\n" +
			"</div>\n";
	
	private String P101 = "<div id=\"P101\">\n" +
			"\t<h2 class=\"title\" name=\"P101\">&middot; 진단 일시</h2>\n" + 
			"\t\t<dd class=\"contents2\">P101_value</dd>\n" +
			"</div>\n";
	
	private String P102 = "<div id=\"P102\">\n" +
			"\t<h2 class=\"title\" name=\"P102\">&middot; 진단 기간</h2>\n" + 
			"\t<dd class=\"contents2\">P102_value</dd>\n" +
			"</div>\n";
	
	private String P103 = "<div id=\"P103\">\n" +
			"\t<h2 class=\"title\" name=\"P103\">&middot; 진단 대상 DB</h2>\n" + 
			"\t<div id=\"P103_T01\">\n" +
			"\t</div>\n" +
			"\t<div id=\"P103_T02\">\n" +
			"\t</div>\n" +
			"\t<div id=\"P103_T03\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P103_01T = "<";
	
	private String P103_02T = "";
	
	private String P103_03T = "";
	
	private String P104 = "<div id=\"P104\">\n" +
			"\t<h2 class=\"title\" name=\"P104\">&middot; 진단 항목</h2>\n" + 
//			"\t<dd class=\"contents2-b5\"><a class=\"om\" href=\"#P112\">- DB 점검</a></li>\n" +
//			"\t<dd class=\"contents2-b5\"><a class=\"om\" href=\"#P149\">- 오브젝트 진단</a></li>\n" +
//			"\t<dd class=\"contents2-b5\"><a class=\"om\" href=\"#P153\">- 파라미터 진단</a></li>\n" +
//			"\t<dd class=\"contents2-b5\"><a class=\"om\" href=\"#P155\">- SQL 성능 진단</a></li>\n" +
//			"\t<dd class=\"contents2-b5\"><a class=\"om\" href=\"#P172\">- 장애 예측 분석</a></li>\n" +
			"\t<dd class=\"contents2-b5\"><a class=\"om\" onclick=\"location.href='#P112';\" style=\"cursor: pointer\">- DB 진단</a></li>\n" +
			"\t<dd class=\"contents2-b5\"><a class=\"om\" onclick=\"location.href='#P149';\" style=\"cursor: pointer\">- 오브젝트 진단 </a></li>\n" +
			"\t<dd class=\"contents2-b5\"><a class=\"om\" onclick=\"location.href='#P153';\" style=\"cursor: pointer\">- 파라미터 진단 </a></li>\n" +
			"\t<dd class=\"contents2-b5\"><a class=\"om\" onclick=\"location.href='#P155';\" style=\"cursor: pointer\">- SQL 성능 진단</a></li>\n" +
			"\t<dd class=\"contents2-b5\"><a class=\"om\" onclick=\"location.href='#P172';\" style=\"cursor: pointer\">- 장애 예측 분석</a></li>\n" +
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

	public String getP010() {
		return P010;
	}

	public void setP010(String P010) {
		this.P010 = P010;
	}

	public String getP101() {
		return P101;
	}

	public void setP101(String P101) {
		this.P101 = P101;
	}

	public String getP102() {
		return P102;
	}

	public void setP102(String P102) {
		this.P102 = P102;
	}

	public String getP103() {
		return P103;
	}

	public void setP103(String P103) {
		this.P103 = P103;
	}

	public String getP103_01T() {
		return P103_01T;
	}

	public void setP103_01T(String p103_01t) {
		P103_01T = p103_01t;
	}

	public String getP103_02T() {
		return P103_02T;
	}

	public String getP103_03T() {
		return P103_03T;
	}

	public void setP103_02T(String p103_02t) {
		P103_02T = p103_02t;
	}

	public void setP103_03T(String p103_03t) {
		P103_03T = p103_03t;
	}

	public String getP104() {
		return P104;
	}

	public void setP104(String p104) {
		this.P104 = p104;
	}
}
