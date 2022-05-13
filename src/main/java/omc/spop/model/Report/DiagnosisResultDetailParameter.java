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

@Alias("diagnosisResultDetailParameter")
public class DiagnosisResultDetailParameter extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

	private String P153 = reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P153\">\n" +
			"\t<h2 class=\"title\" name=\"P153\">&middot; 파라미터 진단</h2>\n" +
			"</div>\n";

	
	private String P154 = "<div id=\"P154\">\n" +
			"\t<h3 class=\"title\" name=\"P154\">RAC 인스턴스 간 다른 파라미터 진단</h3>\n" +
			"\t<dd class=\"contents3\">오라클을 RAC 으로 운영하는 경우 인스턴스에 종속적인 파라미터를 제외한 나머지는 인스턴스 간에 파라미터를 일치시킨다.</dd>\n" +
			"</div>\n";
	
	private String P254 = "<div id=\"P254\">\n" +
			"\t<h4 class=\"title\" name=\"P254\">RAC 인스턴스 간 다른 파라미터</h4>\n" + 
			"\t<div id=\"P254_T01\">\n" +
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

	public String getP153() {
		return P153;
	}

	public void setP153(String p153) {
		P153 = p153;
	}

	public String getP154() {
		return P154;
	}

	public void setP154(String p154) {
		P154 = p154;
	}

	public String getP254() {
		return P254;
	}

	public void setP254(String p254) {
		P254 = p254;
	}

}
