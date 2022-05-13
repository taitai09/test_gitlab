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

@Alias("diagnosisResultDetailObject")
public class DiagnosisResultDetailObject extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

	private String P149 =  reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P149\">\n" +
			"\t<h2 class=\"title\" name=\"P149\">&middot; 오브젝트 진단</h2>\n" +
			"</div>\n";
	
	private String P150 = "<div id=\"P150\">\n" +
			"\t<h3 class=\"title\" name=\"P150\">Reorg 대상 진단</h3>\n" +
			"\t<dd class=\"contents3\">운영중인 DB에 잦은 DML(Insert / Update / Delete) 발생으로 인해 오브젝트에 단편화가 많이 생기면 Disk I/O가 증가되어 SQL 성능이 저하된다.\r\n" + 
			"단편화 된 오브젝트를 조각나지 않은 연속된 페이지로 재 빌드함으로써 불필요하게 공간을 차지하고 있는 테이블 및 인덱스를 최적화 할 수 있으며, 이로 인해 엑세스되는 블록도 감소시켜 SQL의 성능을 향상시킬 수 있다. Reorg 작업 후에 재사용 공간 반환율이 10195_var1% 이상인 오브젝트를 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P248 = "<div id=\"P248\">\n" +
			"\t<h4 class=\"title\" name=\"P248\">Reorg Object 목록</h4>\n" + 
			"\t<div id=\"P248_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P249 = "<div id=\"P249\">\n" +
			"\t<h4 class=\"title\" name=\"P249\">Reorg 후 Owner 별 재사용 가능 공간(GB)</h4>\n" +
			"\t<div id=\"P249_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P250 = "<div id=\"P250\">\n" +
			"\t<h4 class=\"title\" name=\"P250\">Owner별 Reorg 대상 건수</h4>\n" + 
			"\t<div id=\"P250_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P151 = "<div id=\"P151\">\n" +
			"\t<h3 class=\"title\" name=\"P151\">파티셔닝 대상 진단</h3>\n" +
			"\t<dd class=\"contents3\">대용량 테이블을 보다 효율적으로 관리하기 위해 테이블을 작은 단위로 나눔으로써 데이터 액세스 작업의 성능을 향상시키고 데이터 관리를 보다 용이하게 하기 위해서 테이블 파티션이 필요하다. 테이블 사이즈와 사이즈 증가 추이를 고려하여 파티션 대상을 추천한다.</dd>\n" +
			"</div>\n";
	
	private String P251 = "<div id=\"P251\">\n" +
			"\t<h4 class=\"title\" name=\"P251\">파티셔닝 대상</h4>\n" + 
			"\t<div id=\"P251_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P152 = "<div id=\"P152\">\n" +
			"\t<h3 class=\"title\" name=\"P152\">인덱스 사용 진단</h3>\n" +
			"\t<dd class=\"contents3\">수행된 SQL의 Plan정보를 통해 인덱스 사용을 진단한다. 인덱스가 많아서 DML성능이 좋지 못한 테이블을 대상으로 인덱스를 정비(삭제)해야 할 때 사용횟수가 0인 미 사용 인덱스를 정리대상으로 선정할 수 있다.</dd>\n" +
			"</div>\n";
	
	private String P252 = "<div id=\"P252\">\n" +
			"\t<h4 class=\"title\" name=\"P252\">미사용 인덱스</h4>\n" + 
			"\t<div id=\"P252_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P253 = "<div id=\"P253\">\n" +
			"\t<h4 class=\"title\" name=\"P253\">Owner 별 사용/미사용 현황</h4>\n" + 
			"\t<div id=\"P253_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"\t<div id=\"P253_T01\">\n" +
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

	public String getP149() {
		return P149;
	}

	public void setP149(String p149) {
		P149 = p149;
	}

	public String getP150() {
		return P150;
	}

	public void setP150(String p150) {
		P150 = p150;
	}

	public String getP248() {
		return P248;
	}

	public void setP248(String p248) {
		P248 = p248;
	}

	public String getP249() {
		return P249;
	}

	public void setP249(String p249) {
		P249 = p249;
	}

	public String getP250() {
		return P250;
	}

	public void setP250(String p250) {
		P250 = p250;
	}

	public String getP151() {
		return P151;
	}

	public void setP151(String p151) {
		P151 = p151;
	}

	public String getP251() {
		return P251;
	}

	public void setP251(String p251) {
		P251 = p251;
	}

	public String getP152() {
		return P152;
	}

	public void setP152(String p152) {
		P152 = p152;
	}

	public String getP252() {
		return P252;
	}

	public void setP252(String p252) {
		P252 = p252;
	}

	public String getP253() {
		return P253;
	}

	public void setP253(String p253) {
		P253 = p253;
	}

}
