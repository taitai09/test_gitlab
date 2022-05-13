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

@Alias("diagnosisResultDetailSQL")
public class DiagnosisResultDetailSQL extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();
	private String P155 = 
			reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<script>" +
			"function callFunction3( msg , contents_id) { \n" +
			"\tmsg = '{\"func\":\"callFunction3\",\"param1\":\"'+ msg + '\",\"param2\":\"'+ contents_id + '\"}'; \n" +
//			"\tmsg = 'callFunction3(\"' + msg + '\" , \"' + contents_id + '\")'; \n" +
			"\twindow.parent.postMessage( msg, '*' ); \n " +
			"}\n" +
			"</script>" +
			"<div id=\"P155\">\n" +
			"\t<h2 class=\"title\" name=\"P155\">&middot; SQL 성능 진단</h2>\n" +
			"\t<dd class=\"contents2\">플랜 변경, 악성 신규 SQL, Temporary 과다 사용 등 DB 성능 장애를 유발할 수 있는 다양한 유형의 SQL을 매일 정기적으로 진단한다.</dd>\n" +
			"</div>\n";	
	private String P156 = "<div id=\"P156\">\n" +
			"\t<h3 class=\"title\" name=\"P156\">성능진단 임계값</h3>\n" +
			"\t<div id=\"P156_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P157 = "<div id=\"P157\">\n" +
			"\t<h3 class=\"title\" name=\"P157\">1개월 동안 성능진단대상 SQL 발생 현황</h3>\n" + 
			"\t<div id=\"P157_C01\" class=\"chart3\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P158 = "<div id=\"P158\">\n" +
			"\t<h3 class=\"title\" name=\"P158\">Plan 변경</h3>\n" +
			"\t<dd class=\"contents3\">실행 Plan이 변경되어 설정한 임계값 이상으로 성능이 저하된 SQL을 도출한다.</dd>\n" +
			"</div>\n";
	
	private String P255 = "<div id=\"P255\">\n" +
			"\t<h4 class=\"title\" name=\"P255\">Plan 변경 SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P255_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P256 = "<div id=\"P256\">\n" +
			"\t<h4 class=\"title\" name=\"P256\">Plan 변경 SQL</h4>\n" + 
			"\t<div id=\"P256_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P159 = "<div id=\"P159\">\n" +
			"\t<h3 class=\"title\" name=\"P159\">신규 SQL</h3>\n" +
			"\t<dd class=\"contents3\">신규 SQL 선정 조건에 부합되고 성능 부하가 설정한 임계값을 초과하는 SQL을 도출한다.</dd>\n" +
			"</div>\n";
	
	private String P257 = "<div id=\"P257\">\n" +
			"\t<h4 class=\"title\" name=\"P257\">신규 SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P257_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P258 = "<div id=\"P258\">\n" +
			"\t<h4 class=\"title\" name=\"P258\">신규 SQL</h4>\n" + 
			"\t<div id=\"P258_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P160 = "<div id=\"P160\">\n" +
			"\t<h3 class=\"title\" name=\"P160\">Literal SQL</h3>\n" +
			"\t<dd class=\"contents3\">바인드 처리하지 않는 Literal SQL이 많은 경우 잦은 하드파싱으로 인한 library cache 부하를 발생하여 데이터베이스의 전체 성능을 저하시킨다.</dd>\n" +
			"</div>\n";
	
	private String P259 = "<div id=\"P259\">\n" +
			"\t<h4 class=\"title\" name=\"P259\">Literal SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P259_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P260 = "<div id=\"P260\">\n" +
			"\t<h4 class=\"title\" name=\"P260\">Literal SQL</h4>\n" + 
			"\t<div id=\"P260_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P161 = "<div id=\"P161\">\n" +
			"\t<h3 class=\"title\" name=\"P161\">Temp 과다 사용 SQL</h3>\n" +
			"\t<dd class=\"contents3\">Temporary Tablespace 를 설정한 임계값 이상 사용하는 SQL을 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P261 = "<div id=\"P261\">\n" +
			"\t<h4 class=\"title\" name=\"P261\">Temp 과다 사용 SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P261_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P262 = "<div id=\"P262\">\n" +
			"\t<h4 class=\"title\" name=\"P262\">Temp 과다 사용 SQL</h4>\n" + 
			"\t<div id=\"P262_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P162 = "<div id=\"P162\">\n" +
			"\t<h3 class=\"title\" name=\"P162\">Full Scan SQL</h3>\n" +
			"\t<dd class=\"contents3\">Table Full Scan 또는 Index Full Scan 으로 동작하면서 성능이 설정한 임계값을 초과하는 SQL을 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P263 = "<div id=\"P263\">\n" +
			"\t<h4 class=\"title\" name=\"P263\">Full Scan SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P263_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P264 = "<div id=\"P264\">\n" +
			"\t<h4 class=\"title\" name=\"P264\">Full Scan SQL</h4>\n" + 
			"\t<div id=\"P264_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P163 = "<div id=\"P163\">\n" +
			"\t<h3 class=\"title\" name=\"P163\">조건 없는 Delete</h3>\n" +
			"\t<dd class=\"contents3\">Where 조건이 없는 Delete 문장은 redo 와 archive log를 과도하게 발생하므로 truncate 문장으로 변경을 검토한다.</dd>\n" +
			"</div>\n";
	
	private String P265 = "<div id=\"P265\">\n" +
			"\t<h4 class=\"title\" name=\"P265\">조건 없는 Delete SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P265_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P266 = "<div id=\"P266\">\n" +
			"\t<h4 class=\"title\" name=\"P266\">조건 없는 Delete 문장</h4>\n" + 
			"\t<div id=\"P266_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P164 = "<div id=\"P164\">\n" +
			"\t<h3 class=\"title\" name=\"P164\">OFFLOAD 비효율 SQL</h3>\n" +
			"\t<dd class=\"contents3\">Offload 효율이 10176_var1% 이하인 SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P267 = "<div id=\"P267\">\n" +
			"\t<h4 class=\"title\" name=\"P267\">OFFLOAD 비효율 SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P267_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P268 = "<div id=\"P268\">\n" +
			"\t<h4 class=\"title\" name=\"P268\">OFFLOAD 비효율 SQL</h4>\n" + 
			"\t<div id=\"P268_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P165 = "<div id=\"P165\">\n" +
			"\t<h3 class=\"title\" name=\"P165\">OFFLOAD 효율저하 SQL</h3>\n" +
			"\t<dd class=\"contents3\">전주 대비 Offload 효율이 10179_var1% 이상 저하된 SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P269 = "<div id=\"P269\">\n" +
			"\t<h4 class=\"title\" name=\"P269\">OFFLOAD 효율저하 SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P269_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P270 = "<div id=\"P270\">\n" +
			"\t<h4 class=\"title\" name=\"P270\">OFFLOAD 효율저하 SQL</h4>\n" + 
			"\t<div id=\"P270_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P166 = "<div id=\"P166\">\n" +
			"\t<h3 class=\"title\" name=\"P166\">TOP Elapsed Time SQL</h3>\n" +
			"\t<dd class=\"contents3\">Elapsed Time Resource 점유율이 10182_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P271 = "<div id=\"P271\">\n" +
			"\t<h4 class=\"title\" name=\"P271\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P271_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P272 = "<div id=\"P272\">\n" +
			"\t<h4 class=\"title\" name=\"P272\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P272_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P167 = "<div id=\"P167\">\n" +
			"\t<h3 class=\"title\" name=\"P167\">TOP CPU Time SQL</h3>\n" +
			"\t<dd class=\"contents3\">Elapsed Time Resource 점유율이 10227_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P273 = "<div id=\"P273\">\n" +
			"\t<h4 class=\"title\" name=\"P273\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P273_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P274 = "<div id=\"P274\">\n" +
			"\t<h4 class=\"title\" name=\"P274\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P274_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P168 = "<div id=\"P168\">\n" +
			"\t<h3 class=\"title\" name=\"P168\">TOP Buffer Gets SQL</h3>\n" +
			"\t<dd class=\"contents3\">Buffer Gets Resource 점유율이 10228_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P275 = "<div id=\"P275\">\n" +
			"\t<h4 class=\"title\" name=\"P275\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P275_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P276 = "<div id=\"P276\">\n" +
			"\t<h4 class=\"title\" name=\"P276\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P276_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P169 = "<div id=\"P169\">\n" +
			"\t<h3 class=\"title\" name=\"P169\">TOP Physical Reads SQL</h3>\n" +
			"\t<dd class=\"contents3\">Physical Read Resource 점유율이 10229_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P277 = "<div id=\"P277\">\n" +
			"\t<h4 class=\"title\" name=\"P277\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P277_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P278 = "<div id=\"P278\">\n" +
			"\t<h4 class=\"title\" name=\"P278\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P278_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P170 = "<div id=\"P170\">\n" +
			"\t<h3 class=\"title\" name=\"P170\">TOP Executions SQL</h3>\n" +
			"\t<dd class=\"contents3\">Executions점유율이 10230_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P279 = "<div id=\"P279\">\n" +
			"\t<h4 class=\"title\" name=\"P279\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P279_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P280 = "<div id=\"P280\">\n" +
			"\t<h4 class=\"title\" name=\"P280\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P280_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P171 = "<div id=\"P171\">\n" +
			"\t<h3 class=\"title\" name=\"P171\">TOP Cluster Wait SQL</h3>\n" +
			"\t<dd class=\"contents3\">Cluster Wait 점유율이 10231_var1% 이상인 TOP SQL 목록</dd>\n" +
			"</div>\n";
	
	private String P281 = "<div id=\"P281\">\n" +
			"\t<h4 class=\"title\" name=\"P281\">TOP SQL 발생 현황</h4>\n" + 
			"\t<div id=\"P281_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P282 = "<div id=\"P282\">\n" +
			"\t<h4 class=\"title\" name=\"P282\">TOP SQL 목록</h4>\n" + 
			"\t<div id=\"P282_T01\">\n" +
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

	public String getP155() {
		return P155;
	}

	public void setP155(String p155) {
		P155 = p155;
	}

	public String getP156() {
		return P156;
	}

	public void setP156(String p156) {
		P156 = p156;
	}

	public String getP157() {
		return P157;
	}

	public void setP157(String p157) {
		P157 = p157;
	}

	public String getP158() {
		return P158;
	}

	public void setP158(String p158) {
		P158 = p158;
	}

	public String getP255() {
		return P255;
	}

	public void setP255(String p255) {
		P255 = p255;
	}

	public String getP256() {
		return P256;
	}

	public void setP256(String p256) {
		P256 = p256;
	}

	public String getP159() {
		return P159;
	}

	public void setP159(String p159) {
		P159 = p159;
	}

	public String getP257() {
		return P257;
	}

	public void setP257(String p257) {
		P257 = p257;
	}

	public String getP258() {
		return P258;
	}

	public void setP258(String p258) {
		P258 = p258;
	}

	public String getP160() {
		return P160;
	}

	public void setP160(String p160) {
		P160 = p160;
	}

	public String getP259() {
		return P259;
	}

	public void setP259(String p259) {
		P259 = p259;
	}

	public String getP260() {
		return P260;
	}

	public void setP260(String p260) {
		P260 = p260;
	}

	public String getP161() {
		return P161;
	}

	public void setP161(String p161) {
		P161 = p161;
	}

	public String getP261() {
		return P261;
	}

	public void setP261(String p261) {
		P261 = p261;
	}

	public String getP262() {
		return P262;
	}

	public void setP262(String p262) {
		P262 = p262;
	}

	public String getP162() {
		return P162;
	}

	public void setP162(String p162) {
		P162 = p162;
	}

	public String getP263() {
		return P263;
	}

	public void setP263(String p263) {
		P263 = p263;
	}

	public String getP264() {
		return P264;
	}

	public void setP264(String p264) {
		P264 = p264;
	}

	public String getP163() {
		return P163;
	}

	public void setP163(String p163) {
		P163 = p163;
	}

	public String getP265() {
		return P265;
	}

	public void setP265(String p265) {
		P265 = p265;
	}

	public String getP266() {
		return P266;
	}

	public void setP266(String p266) {
		P266 = p266;
	}

	public String getP164() {
		return P164;
	}

	public void setP164(String p164) {
		P164 = p164;
	}

	public String getP267() {
		return P267;
	}

	public void setP267(String p267) {
		P267 = p267;
	}

	public String getP268() {
		return P268;
	}

	public void setP268(String p268) {
		P268 = p268;
	}

	public String getP165() {
		return P165;
	}

	public void setP165(String p165) {
		P165 = p165;
	}

	public String getP269() {
		return P269;
	}

	public void setP269(String p269) {
		P269 = p269;
	}

	public String getP270() {
		return P270;
	}

	public void setP270(String p270) {
		P270 = p270;
	}

	public String getP166() {
		return P166;
	}

	public void setP166(String p166) {
		P166 = p166;
	}

	public String getP271() {
		return P271;
	}

	public void setP271(String p271) {
		P271 = p271;
	}

	public String getP272() {
		return P272;
	}

	public void setP272(String p272) {
		P272 = p272;
	}

	public String getP167() {
		return P167;
	}

	public void setP167(String p167) {
		P167 = p167;
	}

	public String getP273() {
		return P273;
	}

	public void setP273(String p273) {
		P273 = p273;
	}

	public String getP274() {
		return P274;
	}

	public void setP274(String p274) {
		P274 = p274;
	}

	public String getP168() {
		return P168;
	}

	public void setP168(String p168) {
		P168 = p168;
	}

	public String getP275() {
		return P275;
	}

	public void setP275(String p275) {
		P275 = p275;
	}

	public String getP276() {
		return P276;
	}

	public void setP276(String p276) {
		P276 = p276;
	}

	public String getP169() {
		return P169;
	}

	public void setP169(String p169) {
		P169 = p169;
	}

	public String getP277() {
		return P277;
	}

	public void setP277(String p277) {
		P277 = p277;
	}

	public String getP278() {
		return P278;
	}

	public void setP278(String p278) {
		P278 = p278;
	}

	public String getP170() {
		return P170;
	}

	public void setP170(String p170) {
		P170 = p170;
	}

	public String getP279() {
		return P279;
	}

	public void setP279(String p279) {
		P279 = p279;
	}

	public String getP280() {
		return P280;
	}

	public void setP280(String p280) {
		P280 = p280;
	}

	public String getP171() {
		return P171;
	}

	public void setP171(String p171) {
		P171 = p171;
	}

	public String getP281() {
		return P281;
	}

	public void setP281(String p281) {
		P281 = p281;
	}

	public String getP282() {
		return P282;
	}

	public void setP282(String p282) {
		P282 = p282;
	}

}
