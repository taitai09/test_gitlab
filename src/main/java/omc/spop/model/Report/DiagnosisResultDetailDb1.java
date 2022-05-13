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

@Alias("diagnosisResultDetailDb1")
public class DiagnosisResultDetailDb1 extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();

	private String P030  = "<div id=\"P030\">\n" +
			"\t<h1 class=\"title\" name=\"P030\">DB 진단 결과 상세 내용</h1>" +
			"</div>\n";
	
	private String P112  = reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P112\">\n" +
			"\t<h2 class=\"title\" name=\"P112\">&middot; DB 진단</h2>" +
			"</div>\n";
	
	private String P113 = "<div id=\"P113\">\n" +
			"\t<h3 class=\"title\" name=\"P113\">Expired(grace) 계정</h3>\n" + 
			"\t<dd class=\"contents3\">계정 상태가 expired(grace)인 계정을 진단한다. 비밀번호를 변경하지 않으면 계정이 잠기게 되어 로그인 장애가 발생할 수 있다.</dd>\n" +
			"\t<div id=\"P113_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P114 = "<div id=\"P114\">\n" +
			"\t<h3 class=\"title\" name=\"P114\">Expired(grace) 계정 발생 추이</h3>\n" + 
			"\t<div id=\"P114_C01\" class=\"chart3\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P115 = "<div id=\"P115\">\n" +
			"\t<h3 class=\"title\" name=\"P115\">파라미터 변경</h3>\n" +
			"\t<dd class=\"contents3\">인스턴스 기동 이후에 ALTER SYSTEM에 의해 변경된 파라미터를 진단한다. 변경된 파라미터 값과 변경 시점 정보를 통해 파라미터 변경으로 인한 DB이슈에 신속한 대응을 하도록 한다.</dd>\n" +
			"</div>\n";
	
	private String P178 = "<div id=\"P178\">\n" +
			"\t<h4 class=\"title\" name=\"P178\">전일  파라미터 변경</h4>\n" + 
			"\t<div id=\"P178_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P179 = "<div id=\"P179\">\n" +
			"\t<h4 class=\"title\" name=\"P179\">진단기간 파라미터 변경</h4>\n" + 
			"\t<div id=\"P179_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P116 = "<div id=\"P116\">\n" +
			"\t<h3 class=\"title\" name=\"P116\">DB File 생성률</h3>\n" +
			"\t<dd class=\"contents3\">DB Files 파라미터 값과 실제 생성된 파일수를 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P180 = "<div id=\"P180\">\n" +
			"\t<h4 class=\"title\" name=\"P180\">DB File 생성률</h4>\n" + 
			"\t<div id=\"P180_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P181 = "<div id=\"P181\">\n" +
			"\t<h4 class=\"title\" name=\"P181\">DB File 생성률 추이</h4>\n" + 
			"\t<div id=\"P181_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P117 = "<div id=\"P117\">\n" +
			"\t<h3 class=\"title\" name=\"P117\">Library Cache Hit</h3>\n" +
			"\t<dd class=\"contents3\">Library Cache는 SQL의 파싱 정보와 SQL문장을 저장하는 메모리영역으로 Library Cache 를 효율적으로 사용하기 위해서는 잦은 하드 파싱이 일어나지 않도록 한다.</dd>\n" +
			"</div>\n";
	
	private String P182 = "<div id=\"P182\">\n" +
			"\t<h4 class=\"title\" name=\"P182\">Library Cache Hit 율</h4>\n" + 
			"\t<div id=\"P182_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P183 = "<div id=\"P183\">\n" +
			"\t<h4 class=\"title\" name=\"P183\">Library Cache Hit 변화</h4>\n" + 
			"\t<div id=\"P183_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P118 = "<div id=\"P118\">\n" +
			"\t<h3 class=\"title\" name=\"P118\">Dictionary Cache Hit</h3>\n" +
			"\t<dd class=\"contents3\">Dictionary Cache는 SQL문 파싱에 필요한 객체와 객체의 권한, 객체의 구조, 사용자가 정의한 테이블, 뷰, 인덱스 등에 대한 참조 정보를 저장하는 공간으로 히트율이 낮을 경우 SHARED_POOL_SIZE 를 조정한다.</dd>\n" +
			"</div>\n";
	
	private String P184 = "<div id=\"P184\">\n" +
			"\t<h4 class=\"title\" name=\"P184\">Dictionary Cache Hit 율</h4>\n" + 
			"\t<div id=\"P184_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P185 = "<div id=\"P185\">\n" +
			"\t<h4 class=\"title\" name=\"P185\">Dictionary Cache Hit 변화</h4>\n" + 
			"\t<div id=\"P185_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P119 = "<div id=\"P119\">\n" +
			"\t<h3 class=\"title\" name=\"P119\">Buffer Cache Hit</h3>\n" +
			"\t<dd class=\"contents3\">Buffer Cache는 Data File 로부터 읽은 Data Block의 복사본을 담고 있는 메모리 영역으로, Hit율이 낮을 경우 Physical I/O에 의한 SQL 응답시간이 저하되므로 DB_CACHE_SIZE 파라미터를 통해 조정한다.</dd>\n" +
			"</div>\n";
	
	private String P186 = "<div id=\"P186\">\n" +
			"\t<h4 class=\"title\" name=\"P186\">Buffer Cache Hit 율</h4>\n" + 
			"\t<div id=\"P186_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P187 = "<div id=\"P187\">\n" +
			"\t<h4 class=\"title\" name=\"P187\">Buffer Cache Hit 변화</h4>\n" + 
			"\t<div id=\"P187_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P120 = "<div id=\"P120\">\n" +
			"\t<h3 class=\"title\" name=\"P120\">Latch Hit</h3>\n" +
			"\t<dd class=\"contents3\">Latch Hit율은 latch 를 대기없이 바로 획득한 비율로써 latch경합이 많이 발생할 경우 latch히트율이 낮아진다. Latch히트율이 낮을 경우 TOP Wait Event를 진단하여 문제점을 해결해야한다.</dd>\n" +
			"</div>\n";
	
	private String P188 = "<div id=\"P188\">\n" +
			"\t<h4 class=\"title\" name=\"P188\">Latch Hit 율</h4>\n" + 
			"\t<div id=\"P188_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P189 = "<div id=\"P189\">\n" +
			"\t<h4 class=\"title\" name=\"P189\">Latch Hit 변화</h4>\n" + 
			"\t<div id=\"P189_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P121 = "<div id=\"P121\">\n" +
			"\t<h3 class=\"title\" name=\"P121\">Parse CPU To Parse Elapsed</h3>\n" +
			"\t<dd class=\"contents3\">파싱 총 소요 시간 중 CPU Time이 차지한 비율이다. 이 비율이 낮으면 파싱 시에 대기하는 시간이 길다는 의미로 shared pool latch 나 library cache latch 경합이 발생하고 있는지 확인한다.</dd>\n" +
			"</div>\n";
	
	private String P190 = "<div id=\"P190\">\n" +
			"\t<h4 class=\"title\" name=\"P190\">Parse CPU To Parse Elapsed 율</h4>\n" + 
			"\t<div id=\"P190_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P191 = "<div id=\"P191\">\n" +
			"\t<h4 class=\"title\" name=\"P191\">Parse CPU To Parse Elapsed 율 변화</h4>\n" + 
			"\t<div id=\"P191_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P122 = "<div id=\"P122\">\n" +
			"\t<h3 class=\"title\" name=\"P122\">Disk Sort</h3>\n" +
			"\t<dd class=\"contents3\">Disk Sort는 데이터 정렬 작업을 메모리가 아닌 디스크에서 수행한 비율이다. 이 비율이 높으면 sort_area_size와 sort_area_retained_size를 조정한다.</dd>\n" +
			"</div>\n";
	
	private String P192 = "<div id=\"P192\">\n" +
			"\t<h4 class=\"title\" name=\"P192\">Disk Sort 율</h4>\n" + 
			"\t<div id=\"P192_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P193 = "<div id=\"P193\">\n" +
			"\t<h4 class=\"title\" name=\"P193\">Disk Sort 율 변화</h4>\n" + 
			"\t<div id=\"P193_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P123 = "<div id=\"P123\">\n" +
			"\t<h3 class=\"title\" name=\"P123\">Shared Pool Usage</h3>\n" +
			"\t<dd class=\"contents3\">Shared Pool Usage는 Shared Pool 내에서 현재 사용 중인 메모리 비중이다. 데이터베이스가 오랜 시간 실행 된 후에 70% 가까이 있어야 하고 90%이면 shared pool 메모리가 거의 모두 소비되었음을 의미한다.</dd>\n" +
			"</div>\n";
	
	private String P194 = "<div id=\"P194\">\n" +
			"\t<h4 class=\"title\" name=\"P194\">Shared Pool 사용률</h4>\n" + 
			"\t<div id=\"P194_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P195 = "<div id=\"P195\">\n" +
			"\t<h4 class=\"title\" name=\"P195\">Shared Pool 사용률 변화</h4>\n" + 
			"\t<div id=\"P195_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P124 = "<div id=\"P124\">\n" +
			"\t<h3 class=\"title\" name=\"P124\">Resource Limit</h3>\n" +
			"\t<dd class=\"contents3\">Resource Limit 은 오라클이 사용하는 자원에 대한 제한을 설정하는 파라미터이다. LIMIT_VALUE 가 UNLIMITED을 제외한 나머지 리소스에 대한 사용 비율을 진단하여 Max Utilization 이 90% 이상인 경우 파라미터 변경을 검토한다.</dd>\n" +
			"\t<div id=\"P124_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P125 = "<div id=\"P125\">\n" +
			"\t<h3 class=\"title\" name=\"P125\">Background Dump Space</h3>\n" +
			"\t<dd class=\"contents3\">Background Dump(bdump)는 백그라운드 프로세스의 trace파일이 저장되는 공간으로, 이 공간이 가득차면 오라클 장애(Hang)가 발생할 수 있으므로 충분한 여유공간이 있는지 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P196 = "<div id=\"P196\">\n" +
			"\t<h4 class=\"title\" name=\"P196\">Background Dump Space 사용률</h4>\n" + 
			"\t<div id=\"P196_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P197 = "<div id=\"P197\">\n" +
			"\t<h4 class=\"title\" name=\"P197\">Background Dump Space 사용량 변화</h4>\n" + 
			"\t<div id=\"P197_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P126 = "<div id=\"P126\">\n" +
			"\t<h3 class=\"title\" name=\"P126\">Alert Log Space</h3>\n" +
			"\t<dd class=\"contents3\">Alert log가 저장되는 공간을 진단한다. 이 공간이 가득차면 오라클 접속 장애(Hang)가 발생할 수 있으므로 충분한 여유공간이 있는지 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P198 = "<div id=\"P198\">\n" +
			"\t<h4 class=\"title\" name=\"P198\">Alert Log Space 사용률</h4>\n" + 
			"\t<div id=\"P198_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P199 = "<div id=\"P199\">\n" +
			"\t<h4 class=\"title\" name=\"P199\">Alert Log Space 사용량 변화</h4>\n" + 
			"\t<div id=\"P199_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P127 = "<div id=\"P127\">\n" +
			"\t<h3 class=\"title\" name=\"P127\">Archive Log Space</h3>\n" +
			"\t<dd class=\"contents3\">Archive Log가 저장되는 공간을 진단한다. 이 공간이 가득차면 오라클 장애(Hang)가 발생할 수 있으므로 충분한 여유공간이 있는지 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P200 = "<div id=\"P200\">\n" +
			"\t<h4 class=\"title\" name=\"P200\">Archive Log Space 사용률</h4>\n" + 
			"\t<div id=\"P200_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P201 = "<div id=\"P201\">\n" +
			"\t<h4 class=\"title\" name=\"P201\">Archive Log Space 사용량 변화</h4>\n" + 
			"\t<div id=\"P201_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P128 = "<div id=\"P128\">\n" +
			"\t<h3 class=\"title\" name=\"P128\">FRA Space</h3>\n" +
			"\t<dd class=\"contents3\">FRA(Fast Recovery Area) Space 와 파일 유형별 사용량을 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P202 = "<div id=\"P202\">\n" +
			"\t<h4 class=\"title\" name=\"P202\">FRA Space 사용량</h4>\n" + 
			"\t<div id=\"P202_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P203 = "<div id=\"P203\">\n" +
			"\t<h4 class=\"title\" name=\"P203\">FRA Space 사용량 변화</h4>\n" + 
			"\t<div id=\"P203_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P204 = "<div id=\"P204\">\n" +
			"\t<h4 class=\"title\" name=\"P204\">FRA 유형별 사용량</h4>\n" + 
			"\t<div id=\"P204_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P205 = "<div id=\"P205\">\n" +
			"\t<h4 class=\"title\" name=\"P205\">FRA 유형별 사용량 변화</h4>\n" + 
			"\t<div id=\"P205_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P129 = "<div id=\"P129\">\n" +
			"\t<h3 class=\"title\" name=\"P129\">ASM Diskgroup Space</h3>\n" +
			"\t<dd class=\"contents3\">ASM(Automatic Storage Management) Disk Group Space를 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P206 = "<div id=\"P206\">\n" +
			"\t<h4 class=\"title\" name=\"P206\">ASM Diskgroup Space 사용률</h4>\n" + 
			"\t<div id=\"P206_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P207 = "<div id=\"P207\">\n" +
			"\t<h4 class=\"title\" name=\"P207\">ASM Diskgroup Space 사용률 변화</h4>\n" + 
			"\t<div id=\"P207_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P130 = "<div id=\"P130\">\n" +
			"\t<h3 class=\"title\" name=\"P130\">Tablespace Usage</h3>\n" +
			"\t<dd class=\"contents3\">사용량이 90%를 초과하는 테이블스페이스를 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P208 = "<div id=\"P208\">\n" +
			"\t<h4 class=\"title\" name=\"P208\">Tablespace 사용률</h4>\n" + 
			"\t<div id=\"P208_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P209 = "<div id=\"P209\">\n" +
			"\t<h4 class=\"title\" name=\"P209\">Tablespace 사용량 변화</h4>\n" + 
			"\t<div id=\"P209_C01\" class=\"chart4\">\n" +
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

	public String getP030() {
		return P030;
	}

	public void setP030(String p030) {
		P030 = p030;
	}

	public String getP112() {
		return P112;
	}

	public void setP112(String p112) {
		P112 = p112;
	}

	public String getP113() {
		return P113;
	}

	public void setP113(String p113) {
		P113 = p113;
	}

	public String getP114() {
		return P114;
	}

	public void setP114(String p114) {
		P114 = p114;
	}

	public String getP115() {
		return P115;
	}

	public void setP115(String p115) {
		P115 = p115;
	}

	public String getP178() {
		return P178;
	}

	public void setP178(String p178) {
		P178 = p178;
	}

	public String getP179() {
		return P179;
	}

	public void setP179(String p179) {
		P179 = p179;
	}

	public String getP116() {
		return P116;
	}

	public void setP116(String p116) {
		P116 = p116;
	}

	public String getP180() {
		return P180;
	}

	public void setP180(String p180) {
		P180 = p180;
	}

	public String getP181() {
		return P181;
	}

	public void setP181(String p181) {
		P181 = p181;
	}

	public String getP117() {
		return P117;
	}

	public void setP117(String p117) {
		P117 = p117;
	}

	public String getP182() {
		return P182;
	}

	public void setP182(String p182) {
		P182 = p182;
	}

	public String getP183() {
		return P183;
	}

	public void setP183(String p183) {
		P183 = p183;
	}

	public String getP118() {
		return P118;
	}

	public void setP118(String p118) {
		P118 = p118;
	}

	public String getP184() {
		return P184;
	}

	public void setP184(String p184) {
		P184 = p184;
	}

	public String getP185() {
		return P185;
	}

	public void setP185(String p185) {
		P185 = p185;
	}

	public String getP119() {
		return P119;
	}

	public void setP119(String p119) {
		P119 = p119;
	}

	public String getP186() {
		return P186;
	}

	public void setP186(String p186) {
		P186 = p186;
	}

	public String getP187() {
		return P187;
	}

	public void setP187(String p187) {
		P187 = p187;
	}

	public String getP120() {
		return P120;
	}

	public void setP120(String p120) {
		P120 = p120;
	}

	public String getP188() {
		return P188;
	}

	public void setP188(String p188) {
		P188 = p188;
	}

	public String getP189() {
		return P189;
	}

	public void setP189(String p189) {
		P189 = p189;
	}

	public String getP121() {
		return P121;
	}

	public void setP121(String p121) {
		P121 = p121;
	}

	public String getP190() {
		return P190;
	}

	public void setP190(String p190) {
		P190 = p190;
	}

	public String getP191() {
		return P191;
	}

	public void setP191(String p191) {
		P191 = p191;
	}

	public String getP122() {
		return P122;
	}

	public void setP122(String p122) {
		P122 = p122;
	}

	public String getP192() {
		return P192;
	}

	public void setP192(String p192) {
		P192 = p192;
	}

	public String getP193() {
		return P193;
	}

	public void setP193(String p193) {
		P193 = p193;
	}

	public String getP123() {
		return P123;
	}

	public void setP123(String p123) {
		P123 = p123;
	}

	public String getP194() {
		return P194;
	}

	public void setP194(String p194) {
		P194 = p194;
	}

	public String getP195() {
		return P195;
	}

	public void setP195(String p195) {
		P195 = p195;
	}

	public String getP124() {
		return P124;
	}

	public void setP124(String p124) {
		P124 = p124;
	}

	public String getP125() {
		return P125;
	}

	public void setP125(String p125) {
		P125 = p125;
	}

	public String getP196() {
		return P196;
	}

	public void setP196(String p196) {
		P196 = p196;
	}

	public String getP197() {
		return P197;
	}

	public void setP197(String p197) {
		P197 = p197;
	}

	public String getP126() {
		return P126;
	}

	public void setP126(String p126) {
		P126 = p126;
	}

	public String getP198() {
		return P198;
	}

	public void setP198(String p198) {
		P198 = p198;
	}

	public String getP199() {
		return P199;
	}

	public void setP199(String p199) {
		P199 = p199;
	}

	public String getP127() {
		return P127;
	}

	public void setP127(String p127) {
		P127 = p127;
	}

	public String getP200() {
		return P200;
	}

	public void setP200(String p200) {
		P200 = p200;
	}

	public String getP201() {
		return P201;
	}

	public void setP201(String p201) {
		P201 = p201;
	}

	public String getP128() {
		return P128;
	}

	public void setP128(String p128) {
		P128 = p128;
	}

	public String getP202() {
		return P202;
	}

	public void setP202(String p202) {
		P202 = p202;
	}

	public String getP203() {
		return P203;
	}

	public void setP203(String p203) {
		P203 = p203;
	}

	public String getP204() {
		return P204;
	}

	public void setP204(String p204) {
		P204 = p204;
	}

	public String getP205() {
		return P205;
	}

	public void setP205(String p205) {
		P205 = p205;
	}

	public String getP129() {
		return P129;
	}

	public void setP129(String p129) {
		P129 = p129;
	}

	public String getP206() {
		return P206;
	}

	public void setP206(String p206) {
		P206 = p206;
	}

	public String getP207() {
		return P207;
	}

	public void setP207(String p207) {
		P207 = p207;
	}

	public String getP130() {
		return P130;
	}

	public void setP130(String p130) {
		P130 = p130;
	}

	public String getP208() {
		return P208;
	}

	public void setP208(String p208) {
		P208 = p208;
	}

	public String getP209() {
		return P209;
	}

	public void setP209(String p209) {
		P209 = p209;
	}

}
