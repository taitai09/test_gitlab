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

@Alias("diagnosisResultDetailDb2")
public class DiagnosisResultDetailDb2 extends Base implements Jsonable {
	ReportHtml reportHtml = new ReportHtml();
	DiagnosisReportController diagnosisReportController = new DiagnosisReportController();
	
	private String P131 = reportHtml.getWrapper_css() +
			diagnosisReportController.referenceCSS_include() +
			diagnosisReportController.referenceLib_include() +
			"<div id=\"P131\">\n" +
			"\t<h3 class=\"title\" name=\"P131\">Recyclebin Object</h3>\n" +
			"\t<dd class=\"contents3\">Recyclebin은 사용자 실수로 테이블을 잘못 삭제하였을 때 테이블을 완전히 삭제하지 않고 복구할 수 있도록 BIN$로 시작하는 이름으로 테이블(flashback table)을 보관하는 오브젝트이다. Recyclebin 오브젝트가 많은 경우 DBA는 Tablespace의 공간이 가득 찬 것 처럼 인지할 수 있으므로 Recyclebin 오브젝트의 공간사용량을 모니터링하고 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P210 = "<div id=\"P210\">\n" +
			"\t<h4 class=\"title\" name=\"P210\">Recyclebin Object 현황</h4>\n" +
			reportHtml.tableNoLineStyle(4) +
			reportHtml.tableNoLineStyleBody(1, "– Object 건수") +
			reportHtml.tableNoLineStyleBody(2, ":") +
			reportHtml.tableNoLineStyleBody(3, "10103_var1 개") +
			reportHtml.tableNoLineStyleBodyClose(1, "– 전체 사이즈") +
			reportHtml.tableNoLineStyleBodyClose(2, ":") +
			reportHtml.tableNoLineStyleBodyClose(3, "10103_var2 GB") +
			"</div>\n";
	
	private String P211 = "<div id=\"P211\">\n" +
			"\t<h4 class=\"title\" name=\"P211\">Recyclebin Object 건수 추이</h4>\n" + 
			"\t<div id=\"P211_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P212 = "<div id=\"P212\">\n" +
			"\t<h4 class=\"title\" name=\"P212\">Recyclebin 사이즈 추이(GB)</h4>\n" + 
			"\t<div id=\"P212_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P213 = "<div id=\"P213\">\n" +
			"\t<h4 class=\"title\" name=\"P213\">Recycle Object</h4>\n" + 
			"\t<div id=\"P213_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P132 = "<div id=\"P132\">\n" +
			"\t<h3 class=\"title\" name=\"P132\">Invalid Object</h3>\n" +
			"\t<dd class=\"contents3\">Invalid 상태가 된 원인을 분석하고 Valid 상태로 유지하거나 제거한다. Valid 상태로 변경되지 않는 경우 오브젝트 참조 관계를 검토한다.</dd>\n" +
			"</div>\n";
	
	private String P214 = "<div id=\"P214\">\n" +
			"\t<h4 class=\"title\" name=\"P214\">Invalid Object</h4>\n" + 
			"\t<div id=\"P214_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P215 = "<div id=\"P215\">\n" +
			"\t<h4 class=\"title\" name=\"P215\">Invalid Object 건수 추이</h4>\n" + 
			"\t<div id=\"P215_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P133 = "<div id=\"P133\">\n" +
			"\t<h3 class=\"title\" name=\"P133\">Nologging Object</h3>\n" +
			"\t<dd class=\"contents3\">Nologging Object를 진단한 결과이며 목록과 함께 조치 스크립트를 함께 제공한다. Archive Log 을 생성하지 않기 때문에 복구 누락이 될 수 있으므로 Logging 상태로 변경해야 한다.  성능 측면을 고려하여 정책적으로 Nologging 으로 설정한 경우가 아니면 Logging 상태로 변경한다.</dd>\n" +
			"</div>\n";
	
	private String P216 = "<div id=\"P216\">\n" +
			"\t<h4 class=\"title\" name=\"P216\">Nologging Object</h4>\n" + 
			"\t<div id=\"P216_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P217 = "<div id=\"P217\">\n" +
			"\t<h4 class=\"title\" name=\"P217\"> Nologging Object 건수 추이</h4>\n" + 
			"\t<div id=\"P217_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P134 = "<div id=\"P134\">\n" +
			"\t<h3 class=\"title\" name=\"P134\">Parallel Object</h3>\n" +
			"\t<dd class=\"contents3\">오브젝트 수준에서 Parallel Degree 가 지정된 경우 해당 오브젝트를 액세스하는 SQL이 의도하지 않게 병렬로 수행되어 DB의 자원을 과소비하여 DB장애를 발생시킬 수 있으므로 noparallel 로 변경한다.</dd>\n" +
			"</div>\n";
	
	private String P218 = "<div id=\"P218\">\n" +
			"\t<h4 class=\"title\" name=\"P218\">Parallel Object</h4>\n" + 
			"\t<div id=\"P218_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P219 = "<div id=\"P219\">\n" +
			"\t<h4 class=\"title\" name=\"P219\">Parallel Object 건수 추이</h4>\n" + 
			"\t<div id=\"P219_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P135 = "<div id=\"P135\">\n" +
			"\t<h3 class=\"title\" name=\"P135\">Unusable Index</h3>\n" +
			"\t<dd class=\"contents3\">Unusable Index는 테이블의 물리적 변경(move, partition drop 등)으로 인해 인덱스가 사용할 수 없는 상태이다. 이 인덱스를 사용하는 SQL의 플랜이 비효율적으로 수립되어 성능이 저하될 수 있으므로 Rebuild를 통해 Usable상태로 변경한다.</dd>\n" +
			"</div>\n";
	
	private String P220 = "<div id=\"P220\">\n" +
			"\t<h4 class=\"title\" name=\"P220\">Unusable Index</h4>\n" + 
			"\t<div id=\"P220_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P221 = "<div id=\"P221\">\n" +
			"\t<h4 class=\"title\" name=\"P221\">Unusable Index 건수 추이</h4>\n" + 
			"\t<div id=\"P221_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P136 = "<div id=\"P136\">\n" +
			"\t<h3 class=\"title\" name=\"P136\">Chained Rows Table</h3>\n" +
			"\t<dd class=\"contents3\">Chain 건수가 전체 데이터 건수 대비 1% 이상인 테이블을 진단한다. Chain이 많이 발생한 경우 I/O 가 증가함으로 Reorg작업을 수행하는 것을 권고한다.</dd>\n" +
			"</div>\n";
	
	private String P222 = "<div id=\"P222\">\n" +
			"\t<h4 class=\"title\" name=\"P222\">Chained Rows Table</h4>\n" + 
			"\t<div id=\"P222_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P223 = "<div id=\"P223\">\n" +
			"\t<h4 class=\"title\" name=\"P223\">Chained Rows 테이블 발생 추이</h4>\n" + 
			"\t<div id=\"P223_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P137 = "<div id=\"P137\">\n" +
			"\t<h3 class=\"title\" name=\"P137\">Corrupt Block</h3>\n" +
			"\t<dd class=\"contents3\">마지막 백업 이후에 발생된 Corrupt 된 블록을 진단한다. 데이터가 저장되는 Block에 corruption이  발생할 경우 dbverity, dbms_repair 패키지를 통해 해당 블록을 복구한다.</dd>\n" +
			"</div>\n";
	
	private String P224 = "<div id=\"P224\">\n" +
			"\t<h4 class=\"title\" name=\"P224\">Corrupt Block 목록</h4>\n" + 
			"\t<div id=\"P224_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P225 = "<div id=\"P225\">\n" +
			"\t<h4 class=\"title\" name=\"P225\">Corrupt Block 발생 추이</h4>\n" + 
			"\t<div id=\"P225_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P138 = "<div id=\"P138\">\n" +
			"\t<h3 class=\"title\" name=\"P138\">Sequence Threshold Exceeded</h3>\n" +
			"\t<dd class=\"contents3\">Sequence의 현재 값이 max값의 10125_var1%이상 사용하는지 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P226 = "<div id=\"P226\">\n" +
			"\t<h4 class=\"title\" name=\"P226\">Sequence 목록</h4>\n" + 
			"\t<div id=\"P226_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P227 = "<div id=\"P227\">\n" +
			"\t<h4 class=\"title\" name=\"P227\">Sequence Usage 증가 추이</h4>\n" + 
			"\t<div id=\"P227_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P139 = "<div id=\"P139\">\n" +
			"\t<h3 class=\"title\" name=\"P139\">Foreign Keys Without Index</h3>\n" +
			"\t<dd class=\"contents3\">Foreign Key에 인덱스가 없는 테이블을 진단한다. Foreign Key에 인덱스가 없으면 DML작업시 TM락(테이블락) 발생으로 동시성이 저하된다.</dd>\n" +
			"</div>\n";
	
	private String P228 = "<div id=\"P228\">\n" +
			"\t<h4 class=\"title\" name=\"P228\">인덱스 없는 Foreign Key</h4>\n" + 
			"\t<div id=\"P228_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P229 = "<div id=\"P229\">\n" +
			"\t<h4 class=\"title\" name=\"P229\">Owner 별 인덱스 없는 Foreign Key 건수</h4>\n" + 
			"\t<div id=\"P229_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P140 = "<div id=\"P140\">\n" +
			"\t<h3 class=\"title\" name=\"P140\">Disabled Constraint</h3>\n" +
			"\t<dd class=\"contents3\">테이블에서 check, primary key, unique key, referential integrity Constraint 가 Disabled 되었는지를 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P230 = "<div id=\"P230\">\n" +
			"\t<h4 class=\"title\" name=\"P230\">Disabled Constraint</h4>\n" + 
			"\t<div id=\"P230_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P231 = "<div id=\"P231\">\n" +
			"\t<h4 class=\"title\" name=\"P231\">Owner 별 Disabled Constraint 건수</h4>\n" + 
			"\t<div id=\"P231_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P141 = "<div id=\"P141\">\n" +
			"\t<h3 class=\"title\" name=\"P141\">Missing or Stale Statistics</h3>\n" +
			"\t<dd class=\"contents3\">잦은 DML로 인해 테이블 데이터가 전체 건수 대비 10%를 초과 변경되면 통계정보 갱신이 필요하다(Stale Statistics). \r\n" + 
			"통계정보는 오라클 옵티마이저가 최적의 실행계획을 수립하는데 중요한 정보이기 때문에 최신 상태로 유지하는 것이 좋다.</dd>\n" +
			"</div>\n";
	
	private String P232 = "<div id=\"P232\">\n" +
			"\t<h4 class=\"title\" name=\"P232\">Missing or Stale Statistics Table</h4>\n" + 
			"\t<div id=\"P232_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P233 = "<div id=\"P233\">\n" +
			"\t<h4 class=\"title\" name=\"P233\">Owner 별 Missing or Stale Statistics Table 건수</h4>\n" + 
			"\t<div id=\"P233_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P142 = "<div id=\"P142\">\n" +
			"\t<h3 class=\"title\" name=\"P142\">Statistics Locked Table</h3>\n" +
			"\t<dd class=\"contents3\">통계정보가 수집되지 못하도록 잠금(Lock)이 걸린 테이블을 진단한다. 정책적으로 통계정보 수집이 안되도록 Lock을 걸 수 있지만, 그렇지 않은 경우 Lock 상태에서는 통계정보 수집이 안되므로 Lock을 해제해야 한다.</dd>\n" +
			"</div>\n";
	
	private String P234 = "<div id=\"P234\">\n" +
			"\t<h4 class=\"title\" name=\"P234\">Statistics Locked Table</h4>\n" + 
			"\t<div id=\"P234_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P235 = "<div id=\"P235\">\n" +
			"\t<h4 class=\"title\" name=\"P235\">Owner 별 Statistics Lock Table 건수</h4>\n" + 
			"\t<div id=\"P235_C11\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P143 = "<div id=\"P143\">\n" +
			"\t<h3 class=\"title\" name=\"P143\">Long Running Work</h3>\n" +
			"\t<dd class=\"contents3\">업무 시작 전까지 쿼리가 종료되지 않을 것으로 예상되는 과부하 SQL을 진단한다. Long running 작업은 DB자원을 많이 소비하는 배치 성격의 작업일 가능성이 있으므로 작업을 유지할지 강제 종료할지를 검토해야 한다.</dd>\n" +
			"</div>\n";
	
	private String P236 = "<div id=\"P236\">\n" +
			"\t<h4 class=\"title\" name=\"P236\">Long Running Work</h4>\n" + 
			"\t<div id=\"P236_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P237 = "<div id=\"P237\">\n" +
			"\t<h4 class=\"title\" name=\"P237\">Long Running Work 발생 현황</h4>\n" + 
			"\t<div id=\"P237_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P144 = "<div id=\"P144\">\n" +
			"\t<h3 class=\"title\" name=\"P144\">Long Running Job</h3>\n" +
			"\t<dd class=\"contents3\">오라클 스케줄러에서 실행되는 JOB 중 24시간 이상 수행하는 JOB이 존재하는지를 진단한다. 장시간 수행되는 스케줄러 JOB을 진단하여 업무시간에 DB부하를 주지 않도록 한다.</dd>\n" +
			"</div>\n";
	
	private String P238 = "<div id=\"P238\">\n" +
			"\t<h4 class=\"title\" name=\"P238\">Long Running Job</h4>\n" + 
			"\t<div id=\"P238_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P239 = "<div id=\"P239\">\n" +
			"\t<h4 class=\"title\" name=\"P239\">Long Running Job 발생 현황</h4>\n" + 
			"\t<div id=\"P239_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P145 = "<div id=\"P145\">\n" +
			"\t<h3 class=\"title\" name=\"P145\">Scheduler Job 실패</h3>\n" +
			"\t<dd class=\"contents3\">1일 동안 DB 스케쥴러 실패 작업을 진단한다.</dd>\n" +
			"</div>\n";
	
	private String P240 = "<div id=\"P240\">\n" +
			"\t<h4 class=\"title\" name=\"P240\">Scheduler Job 실패</h4>\n" + 
			"\t<div id=\"P240_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P241 = "<div id=\"P241\">\n" +
			"\t<h4 class=\"title\" name=\"P241\">Scheduler Job 실패 발생 추이</h4>\n" + 
			"\t<div id=\"P241_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P146 = "<div id=\"P146\">\n" +
			"\t<h3 class=\"title\" name=\"P146\">Alert Log Error</h3>\n" +
			"\t<dd class=\"contents3\">Alert log 에 기록되는 오라클 오류를 진단한다.  ORA-600, ORA-7445 와 같이 심각한 오류가 발생할 경우 오류 원인 분석이 필요하다.</dd>\n" +
			"</div>\n";
	
	private String P242 = "<div id=\"P242\">\n" +
			"\t<h4 class=\"title\" name=\"P242\">Alert Log Error</h4>\n" + 
			"\t<div id=\"P242_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P243 = "<div id=\"P243\">\n" +
			"\t<h4 class=\"title\" name=\"P243\">Alert Log Error 발생 현황</h4>\n" + 
			"\t<div id=\"P243_C01\" class=\"chart4\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P147 = "<div id=\"P147\">\n" +
			"\t<h3 class=\"title\" name=\"P147\">Active Incident</h3>\n" +
			"\t<dd class=\"contents3\">Alert로그에 기록되는 Error를 Incident(사건)과 Problem(문제) 로 구분하여 진단한다.</dd>\n" +
			reportHtml.tableNoLineStyle(3) +
			reportHtml.tableNoLineStyleBody(1, "– Problem") +
			reportHtml.tableNoLineStyleBody(2, ":") +
			reportHtml.tableNoLineStyleBody(3, "\"incident\"의 원인이 되는 근본적인 문제이며, 일반적으로 하나의 problem는 여러 incidents를 가진다.") +
			reportHtml.tableNoLineStyleBodyClose(1, "– Incident") +
			reportHtml.tableNoLineStyleBodyClose(2, ":") +
			reportHtml.tableNoLineStyleBodyClose(3, "alert log에 기록되는 error(problem)때문에 발생하는 event 하나하나를 의미한다.") +
			"</div>\n";
	
	private String P244 = "<div id=\"P244\">\n" +
			"\t<h4 class=\"title\" name=\"P244\">Problem</h4>\n" + 
			"\t<div id=\"P244_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P245 = "<div id=\"P245\">\n" +
			"\t<h4 class=\"title\" name=\"P245\">Incident</h4>\n" + 
			"\t<div id=\"P245_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P148 = "<div id=\"P148\">\n" +
			"\t<h3 class=\"title\" name=\"P148\">Outstanding Alert</h3>\n" +
			"\t<dd class=\"contents3\">DB에서 발생한 Outstanding Alert 이슈를 진단한다. 확인할 수 있는 대표적인 이슈는 아래와 같다.</dd>\n" +
			reportHtml.tableNoLineNoHeadStyle(3) +
			reportHtml.tableNoLineStyleBody(4, "– Tablespace Full") +
			reportHtml.tableNoLineStyleBody(4, "– Ora-01555 Snapshot too old") +
			reportHtml.tableNoLineStyleBodyClose(4, "– Ora-01555 Snapshot too old") +
			"</div>\n";
	
	private String P246 = "<div id=\"P246\">\n" +
			"\t<h4 class=\"title\" name=\"P246\">Outstanding Alert</h4>\n" + 
			"\t<div id=\"P246_T01\">\n" +
			"\t</div>\n" +
			"</div>\n";
	
	private String P247 = "<div id=\"P247\">\n" +
			"\t<h4 class=\"title\" name=\"P247\">Outstanding Alert 발생 현황</h4>\n" + 
			"\t<div id=\"P247_C01\" class=\"chart4\">\n" +
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

	public String getP131() {
		return P131;
	}

	public void setP131(String p131) {
		P131 = p131;
	}

	public String getP210() {
		return P210;
	}

	public void setP210(String p210) {
		P210 = p210;
	}

	public String getP211() {
		return P211;
	}

	public void setP211(String p211) {
		P211 = p211;
	}

	public String getP212() {
		return P212;
	}

	public void setP212(String p212) {
		P212 = p212;
	}

	public String getP213() {
		return P213;
	}

	public void setP213(String p213) {
		P213 = p213;
	}

	public String getP132() {
		return P132;
	}

	public void setP132(String p132) {
		P132 = p132;
	}

	public String getP214() {
		return P214;
	}

	public void setP214(String p214) {
		P214 = p214;
	}

	public String getP215() {
		return P215;
	}

	public void setP215(String p215) {
		P215 = p215;
	}

	public String getP133() {
		return P133;
	}

	public void setP133(String p133) {
		P133 = p133;
	}

	public String getP216() {
		return P216;
	}

	public void setP216(String p216) {
		P216 = p216;
	}

	public String getP217() {
		return P217;
	}

	public void setP217(String p217) {
		P217 = p217;
	}

	public String getP134() {
		return P134;
	}

	public void setP134(String p134) {
		P134 = p134;
	}

	public String getP218() {
		return P218;
	}

	public void setP218(String p218) {
		P218 = p218;
	}

	public String getP219() {
		return P219;
	}

	public void setP219(String p219) {
		P219 = p219;
	}

	public String getP135() {
		return P135;
	}

	public void setP135(String p135) {
		P135 = p135;
	}

	public String getP220() {
		return P220;
	}

	public void setP220(String p220) {
		P220 = p220;
	}

	public String getP221() {
		return P221;
	}

	public void setP221(String p221) {
		P221 = p221;
	}

	public String getP136() {
		return P136;
	}

	public void setP136(String p136) {
		P136 = p136;
	}

	public String getP222() {
		return P222;
	}

	public void setP222(String p222) {
		P222 = p222;
	}

	public String getP223() {
		return P223;
	}

	public void setP223(String p223) {
		P223 = p223;
	}

	public String getP137() {
		return P137;
	}

	public void setP137(String p137) {
		P137 = p137;
	}

	public String getP224() {
		return P224;
	}

	public void setP224(String p224) {
		P224 = p224;
	}

	public String getP225() {
		return P225;
	}

	public void setP225(String p225) {
		P225 = p225;
	}

	public String getP138() {
		return P138;
	}

	public void setP138(String p138) {
		P138 = p138;
	}

	public String getP226() {
		return P226;
	}

	public void setP226(String p226) {
		P226 = p226;
	}

	public String getP227() {
		return P227;
	}

	public void setP227(String p227) {
		P227 = p227;
	}

	public String getP139() {
		return P139;
	}

	public void setP139(String p139) {
		P139 = p139;
	}

	public String getP228() {
		return P228;
	}

	public void setP228(String p228) {
		P228 = p228;
	}

	public String getP229() {
		return P229;
	}

	public void setP229(String p229) {
		P229 = p229;
	}

	public String getP140() {
		return P140;
	}

	public void setP140(String p140) {
		P140 = p140;
	}

	public String getP230() {
		return P230;
	}

	public void setP230(String p230) {
		P230 = p230;
	}

	public String getP231() {
		return P231;
	}

	public void setP231(String p231) {
		P231 = p231;
	}

	public String getP141() {
		return P141;
	}

	public void setP141(String p141) {
		P141 = p141;
	}

	public String getP232() {
		return P232;
	}

	public void setP232(String p232) {
		P232 = p232;
	}

	public String getP233() {
		return P233;
	}

	public void setP233(String p233) {
		P233 = p233;
	}

	public String getP142() {
		return P142;
	}

	public void setP142(String p142) {
		P142 = p142;
	}

	public String getP234() {
		return P234;
	}

	public void setP234(String p234) {
		P234 = p234;
	}

	public String getP235() {
		return P235;
	}

	public void setP235(String p235) {
		P235 = p235;
	}

	public String getP143() {
		return P143;
	}

	public void setP143(String p143) {
		P143 = p143;
	}

	public String getP236() {
		return P236;
	}

	public void setP236(String p236) {
		P236 = p236;
	}

	public String getP237() {
		return P237;
	}

	public void setP237(String p237) {
		P237 = p237;
	}

	public String getP144() {
		return P144;
	}

	public void setP144(String p144) {
		P144 = p144;
	}

	public String getP238() {
		return P238;
	}

	public void setP238(String p238) {
		P238 = p238;
	}

	public String getP239() {
		return P239;
	}

	public void setP239(String p239) {
		P239 = p239;
	}

	public String getP145() {
		return P145;
	}

	public void setP145(String p145) {
		P145 = p145;
	}

	public String getP240() {
		return P240;
	}

	public void setP240(String p240) {
		P240 = p240;
	}

	public String getP241() {
		return P241;
	}

	public void setP241(String p241) {
		P241 = p241;
	}

	public String getP146() {
		return P146;
	}

	public void setP146(String p146) {
		P146 = p146;
	}

	public String getP242() {
		return P242;
	}

	public void setP242(String p242) {
		P242 = p242;
	}

	public String getP243() {
		return P243;
	}

	public void setP243(String p243) {
		P243 = p243;
	}

	public String getP147() {
		return P147;
	}

	public void setP147(String p147) {
		P147 = p147;
	}

	public String getP244() {
		return P244;
	}

	public void setP244(String p244) {
		P244 = p244;
	}

	public String getP245() {
		return P245;
	}

	public void setP245(String p245) {
		P245 = p245;
	}

	public String getP148() {
		return P148;
	}

	public void setP148(String p148) {
		P148 = p148;
	}

	public String getP246() {
		return P246;
	}

	public void setP246(String p246) {
		P246 = p246;
	}

	public String getP247() {
		return P247;
	}

	public void setP247(String p247) {
		P247 = p247;
	}

}
