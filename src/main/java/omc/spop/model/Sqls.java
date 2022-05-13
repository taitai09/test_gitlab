package omc.spop.model;

import java.math.BigDecimal;
import java.util.List;
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
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2020.05.12 	명성태 	최초작성
 * 2021.01.22	황예지	exception_prc_meth_nm,children 필드 추가
 **********************************************************/

@Alias("sqls")
public class Sqls extends Base implements Jsonable {
	private String isHandOff;					// SQL 성능 추적 현황 탭의 성능 점검 SQL 탭과 예외 처리 SQL 탭에서 넘어온 데이터일 때 "Y"로 처리
	private String begin_dt;
	private String end_dt;
	private int base_period_value;
	
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	
	private String isCheckHighestRank;				// 최상위
	
	private String selectSearchType;			// 0: 전체, 1: 성능 점검 SLQ 탭, 2: 예외 처리 SQL 탭
	private String isRegressYn;					// 성능 저하 - Y / N		// 전달받는 정보
	private String is_regress;					// 성능 저하 - Y / N		// 실제 정보
	private String selectPerfRegressedMetric;	// 성능점검SQL - 콤보 : '2XLT' -- < 2X,   '5XLT' -- < 5X,   '10XLT' -- < 10X,
												//                  '30XLT' -- < 30X, '50XLT' -- < 50X, '100XLT' -- < 100X, '100XMT' -- >= 100X
	private String selectSqlPerfTrace;			// 예외처리방법
	private String selectElapsedTimeMetirc;		// 예외처리 수행시간 콤보 : 0:전체, 1:0.1XLT, 2:0.3XLT, 3:1XLT, 4:3XLT, 5:10XLT, 6:60XLT, 7:60XMT
	private String isCheckFail;					// 부적합
	private String isCheckPass;					// 적합 체크 박스 판단여부
	private String strSqlId;					// SQL ID
	private String strDbio;						// SQL 식별자
	
	// 조회 컬럼
	private String after_prd_sql_id;			// SQL ID
	private String dbio;						// SQL식별자
	private String program_nm;					// 프로그램
	private String after_prd_plan_hash_value;	// PLAN_HASH_VALUE
	private String prd_plan_change_yn;			// PLAN 변경 여부
	private BigDecimal test_elapsed_time;			// 배포 평균  수행시간
	private BigDecimal after_prd_elapsed_time;		// 운영 평균 수행시간
	private BigDecimal elapsed_time_activity;	// 수행시간 Activity(%)
	private String test_buffer_gets;			// 배포 평균 블럭수
	private String after_prd_buffer_gets;		// 운영 평균 블럭수
	private BigDecimal buffer_gets_activity;	// 블럭수 Activity(%)
	private String test_rows_processed;			// 배포 평균  처리건수
	private String after_prd_rows_processed;	// 운영 평균 처리건수
	private String after_executions;			// 운영 수행횟수
	private String except_yn;					// 예외여부
	private String after_fail_yn;				// 부적합 여부(Y:부적합, N:적합)
	private String before_fail_yn;				// 이전 부적합 여부(Y:부적합, N:적합)
	private String program_exec_dt;				// 성능점검일자
	private String deploy_complete_dt;			// 배포일자
	private String perf_regressed_metric;		// 성능저하 율 메트릭스
	private String elapsed_time_metirc;			// 수행시간 구간 메트릭스
	private String exception_prc_meth_cd;		// 예외처리방법코드
	private String test_sql_id;					// 배포 SQL_ID
	private String test_plan_hash_value;		// 배포 PLAN_HASH_VALUE
	private String before_prd_sql_id;			// 이전 운영 SQL_ID
	private String before_prd_plan_hash_value;	// 이전 운영 PLAN_HASH_VALUE
	private String elapsed_time_regress_yn;		// 수행시간 성능저하
	private String buffer_gets_regress_yn;		// 블럭수 성능저하 (Y:성능향상, N:성능저하)
	private BigDecimal prd_buffer_gets_increase_ratio;	// 운영 블럭 증감율
	private BigDecimal prd_elap_time_increase_ratio;	// 운영 수행시간 증감율
	private BigDecimal prd_rows_proc_increase_ratio;	// 운영 처리건수 증감율
	private String top_wrkjob_cd;					// 최상위 업무코드
	private String program_id;						// PROGRAM_ID
	private String perf_check_id;					// 성능점검ID
	private String perf_check_step_id;				// 성능점검 단계
	private String program_execute_tms;				// 성능점검 수행회차
	private String after_fail_yn_condition;			// 부적합 조건
	
	// Tuning 요청
	private String auto_share;
	private String tuning_no_array;
	private String dbid_array;
	private String after_prd_sql_id_array;
	private String after_prd_plan_hash_value_array;
	private String perf_check_step_id_array;
	private String wrkjob_cd_array;
	private String executions_array;
	private String avg_buffer_gets_array;
	private String avg_elapsed_time_array;
	private String avg_row_processed_array;
	private String perf_check_id_array;
	private String program_id_array;
	private String dbio_array;
	private String perfr_id;
	
	private String project_id;
	private String tuning_prgrs_step_seq;
	private String parsing_schema_name;
	
	private String bindValue_name;
	private String bindValue_value;
	
	private BigDecimal elapsed_time;				// SQL 성능 추적
	private BigDecimal buffer_gets;
	private BigDecimal rows_processed;
	private String last_active_time;
	private String sql_id;
	private String plan_hash_value;
	
	private BigDecimal before_prd_elapsed_time;
	private String before_prd_buffer_gets;
	private String before_prd_rows_processed;
	
	private String program_source_desc;				// 성능 점검 SQL > sql_text
	private String name;
	private String value_string;
	private BigDecimal cost_percent;
	private BigDecimal cpu_cost_percent;
	private BigDecimal io_cost_percent;
	private String id;
	private String operation;
	private String cost;
	private String cpu_cost;
	private String io_cost;
	
	private String sql_fulltext;					// 운용 SQL > sql_text
	
	private String big_table_threshold_cnt;
	
	private String program_div_nm;			// 프로그램 구분
	private String program_type_nm;			// 프로그램유형
	private String program_type_cd;
	private String sql_command_type_nm;		// SQL 명령 유형
	private String sql_command_type_cd;
	private String dynamic_sql_yn;			// 다이나믹 SQL 여부
	private String program_desc;			// 프로그램설명(with br tag)
	private String file_nm;					// 파일명
	private String dir_nm;					// 디렉토리명
	private String reg_dt;					// 등록일시
	private String last_update_dt;			// 변경일시

	private String sql_text;
	private String snap_id;
	private String value;
	private BigDecimal exec_time;
	private String exec_inst_id;
	private String seq;
	private String execution_plan;
	private String text;
	private String parent_id;
	private String imid;
	private String object_node;
	private String object;
	private String object_owner;
	private String object_name;
	private String object_type;
	private String optimizer;
	private String cardinality;
	private String bytes;
	private String other_tag;
	private String partition_start;
	private String partition_stop;
	private String access_predicates;
	private String filter_predicates;
	private String projection;
	private String time;
	private String qblock_name;
	private String timestamp;
	private String hint;
	private String log_dt;
	private String executions;
	private String disk_reads;
	private String module;
	private BigDecimal cpu_time;
	private BigDecimal clwait_time;
	private BigDecimal iowait_time ;
	private BigDecimal apwait_time;
	private BigDecimal ccwait_time;
	private BigDecimal cpu_rate;
	private BigDecimal clwait_rate;
	private BigDecimal iowait_rate;
	private BigDecimal apwait_rate;
	private BigDecimal ccwait_rate;
	private String parse_calls;
	private String fetches;
	private String inst_id;
	private String choice_div_cd;
	
	/* 성능 점검 결과 */
	private String perf_check_indc_nm;					// 점검지표
	private String indc_pass_max_value;					// 지표여부값판정구분코드
	private String exec_result_value;					// 배포전 성능점검결과값
	private String perf_check_result_div_nm;			// 배포전 성능점검결과
	private String prd_exec_result_value;				// 배포후 성능점검결과값
	private String prd_perf_check_result_div_nm;		// 배포후 성능점검결과
	private String exception_yn;						// 예외등록여부
	private String perf_check_result_desc;				// 성능점검 결과내용
	private String perf_check_result_div_cd;			// 배포전 성능점검결과
	private String prd_perf_check_result_div_cd;		// 배포후 성능점검결과
	private String perf_check_indc_id;					// 성능점검지표ID
	private String perf_check_meth_cd;
	private String exception_prc_meth_nm;				//예외처리방법	영구/한시
	private Object children;							//treegrid 하위 리스트
	
	private String diffTag;
	
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

	public String getChoice_div_cd() {
		return choice_div_cd;
	}

	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}

	public void setChildren(Object children) {
		this.children = children;
	}

	public String getIsHandOff() {
		return isHandOff;
	}

	public void setIsHandOff(String isHandOff) {
		this.isHandOff = isHandOff;
	}

	public String getDeploy_complete_dt() {
		return deploy_complete_dt;
	}

	public void setDeploy_complete_dt(String deploy_complete_dt) {
		this.deploy_complete_dt = deploy_complete_dt;
	}

	public String getBegin_dt() {
		return begin_dt;
	}

	public void setBegin_dt(String begin_dt) {
		this.begin_dt = begin_dt;
	}

	public String getEnd_dt() {
		return end_dt;
	}

	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}

	public int getBase_period_value() {
		return base_period_value;
	}

	public void setBase_period_value(int base_period_value) {
		this.base_period_value = base_period_value;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getIsCheckHighestRank() {
		return isCheckHighestRank;
	}

	public void setIsCheckHighestRank(String isCheckHighestRank) {
		this.isCheckHighestRank = isCheckHighestRank;
	}

	public String getSelectSearchType() {
		return selectSearchType;
	}

	public void setSelectSearchType(String selectSearchType) {
		this.selectSearchType = selectSearchType;
	}

	public String getIsRegressYn() {
		return isRegressYn;
	}

	public void setIsRegressYn(String isRegressYn) {
		this.isRegressYn = isRegressYn;
	}

	public String getIs_regress() {
		return is_regress;
	}

	public void setIs_regress(String is_regress) {
		this.is_regress = is_regress;
	}

	public String getException_prc_meth_cd() {
		return exception_prc_meth_cd;
	}

	public void setException_prc_meth_cd(String exception_prc_meth_cd) {
		this.exception_prc_meth_cd = exception_prc_meth_cd;
	}

	public String getElapsed_time_regress_yn() {
		return elapsed_time_regress_yn;
	}

	public void setElapsed_time_regress_yn(String elapsed_time_regress_yn) {
		this.elapsed_time_regress_yn = elapsed_time_regress_yn;
	}

	public String getAfter_fail_yn() {
		return after_fail_yn;
	}

	public void setAfter_fail_yn(String after_fail_yn) {
		this.after_fail_yn = after_fail_yn;
	}

	public String getBuffer_gets_regress_yn() {
		return buffer_gets_regress_yn;
	}

	public void setBuffer_gets_regress_yn(String buffer_gets_regress_yn) {
		this.buffer_gets_regress_yn = buffer_gets_regress_yn;
	}

	public String getSelectPerfRegressedMetric() {
		return selectPerfRegressedMetric;
	}

	public void setSelectPerfRegressedMetric(String selectPerfRegressedMetric) {
		this.selectPerfRegressedMetric = selectPerfRegressedMetric;
	}

	public String getIsCheckPass() {
		return isCheckPass;
	}

	public void setIsCheckPass(String isCheckPass) {
		this.isCheckPass = isCheckPass;
	}

	public String getElapsed_time_metirc() {
		return elapsed_time_metirc;
	}

	public void setElapsed_time_metirc(String elapsed_time_metirc) {
		this.elapsed_time_metirc = elapsed_time_metirc;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getSelectElapsedTimeMetirc() {
		return selectElapsedTimeMetirc;
	}

	public void setSelectElapsedTimeMetirc(String selectElapsedTimeMetirc) {
		this.selectElapsedTimeMetirc = selectElapsedTimeMetirc;
	}

	public String getProgram_nm() {
		return program_nm;
	}

	public void setProgram_nm(String program_nm) {
		this.program_nm = program_nm;
	}

	public String getAfter_prd_plan_hash_value() {
		return after_prd_plan_hash_value;
	}

	public void setAfter_prd_plan_hash_value(String after_prd_plan_hash_value) {
		this.after_prd_plan_hash_value = after_prd_plan_hash_value;
	}

	public String getPrd_plan_change_yn() {
		return prd_plan_change_yn;
	}

	public void setPrd_plan_change_yn(String prd_plan_change_yn) {
		this.prd_plan_change_yn = prd_plan_change_yn;
	}

	public BigDecimal getTest_elapsed_time() {
		return test_elapsed_time;
	}

	public void setTest_elapsed_time(BigDecimal test_elapsed_time) {
		this.test_elapsed_time = test_elapsed_time;
	}

	public BigDecimal getAfter_prd_elapsed_time() {
		return after_prd_elapsed_time;
	}

	public void setAfter_prd_elapsed_time(BigDecimal after_prd_elapsed_time) {
		this.after_prd_elapsed_time = after_prd_elapsed_time;
	}

	public BigDecimal getElapsed_time_activity() {
		return elapsed_time_activity;
	}

	public void setElapsed_time_activity(BigDecimal elapsed_time_activity) {
		this.elapsed_time_activity = elapsed_time_activity;
	}

	public String getTest_buffer_gets() {
		return test_buffer_gets;
	}

	public void setTest_buffer_gets(String test_buffer_gets) {
		this.test_buffer_gets = test_buffer_gets;
	}

	public String getAfter_prd_buffer_gets() {
		return after_prd_buffer_gets;
	}

	public void setAfter_prd_buffer_gets(String after_prd_buffer_gets) {
		this.after_prd_buffer_gets = after_prd_buffer_gets;
	}

	public BigDecimal getBuffer_gets_activity() {
		return buffer_gets_activity;
	}

	public void setBuffer_gets_activity(BigDecimal buffer_gets_activity) {
		this.buffer_gets_activity = buffer_gets_activity;
	}

	public String getTest_rows_processed() {
		return test_rows_processed;
	}

	public void setTest_rows_processed(String test_rows_processed) {
		this.test_rows_processed = test_rows_processed;
	}

	public String getAfter_prd_rows_processed() {
		return after_prd_rows_processed;
	}

	public void setAfter_prd_rows_processed(String after_prd_rows_processed) {
		this.after_prd_rows_processed = after_prd_rows_processed;
	}

	public String getAfter_executions() {
		return after_executions;
	}

	public void setAfter_executions(String after_executions) {
		this.after_executions = after_executions;
	}

	public String getExcept_yn() {
		return except_yn;
	}

	public void setExcept_yn(String except_yn) {
		this.except_yn = except_yn;
	}

	public String getBefore_fail_yn() {
		return before_fail_yn;
	}

	public void setBefore_fail_yn(String before_fail_yn) {
		this.before_fail_yn = before_fail_yn;
	}

	public String getProgram_exec_dt() {
		return program_exec_dt;
	}

	public void setProgram_exec_dt(String program_exec_dt) {
		this.program_exec_dt = program_exec_dt;
	}

	public String getPerf_regressed_metric() {
		return perf_regressed_metric;
	}

	public void setPerf_regressed_metric(String perf_regressed_metric) {
		this.perf_regressed_metric = perf_regressed_metric;
	}

	public String getTest_sql_id() {
		return test_sql_id;
	}

	public void setTest_sql_id(String test_sql_id) {
		this.test_sql_id = test_sql_id;
	}

	public String getTest_plan_hash_value() {
		return test_plan_hash_value;
	}

	public void setTest_plan_hash_value(String test_plan_hash_value) {
		this.test_plan_hash_value = test_plan_hash_value;
	}

	public String getBefore_prd_sql_id() {
		return before_prd_sql_id;
	}

	public void setBefore_prd_sql_id(String before_prd_sql_id) {
		this.before_prd_sql_id = before_prd_sql_id;
	}

	public String getBefore_prd_plan_hash_value() {
		return before_prd_plan_hash_value;
	}

	public void setBefore_prd_plan_hash_value(String before_prd_plan_hash_value) {
		this.before_prd_plan_hash_value = before_prd_plan_hash_value;
	}

	public BigDecimal getPrd_buffer_gets_increase_ratio() {
		return prd_buffer_gets_increase_ratio;
	}

	public void setPrd_buffer_gets_increase_ratio(BigDecimal prd_buffer_gets_increase_ratio) {
		this.prd_buffer_gets_increase_ratio = prd_buffer_gets_increase_ratio;
	}

	public BigDecimal getPrd_elap_time_increase_ratio() {
		return prd_elap_time_increase_ratio;
	}

	public void setPrd_elap_time_increase_ratio(BigDecimal prd_elap_time_increase_ratio) {
		this.prd_elap_time_increase_ratio = prd_elap_time_increase_ratio;
	}

	public BigDecimal getPrd_rows_proc_increase_ratio() {
		return prd_rows_proc_increase_ratio;
	}

	public void setPrd_rows_proc_increase_ratio(BigDecimal prd_rows_proc_increase_ratio) {
		this.prd_rows_proc_increase_ratio = prd_rows_proc_increase_ratio;
	}

	public String getTop_wrkjob_cd() {
		return top_wrkjob_cd;
	}

	public void setTop_wrkjob_cd(String top_wrkjob_cd) {
		this.top_wrkjob_cd = top_wrkjob_cd;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getPerf_check_id() {
		return perf_check_id;
	}

	public void setPerf_check_id(String perf_check_id) {
		this.perf_check_id = perf_check_id;
	}

	public String getPerf_check_step_id() {
		return perf_check_step_id;
	}

	public void setPerf_check_step_id(String perf_check_step_id) {
		this.perf_check_step_id = perf_check_step_id;
	}

	public String getProgram_execute_tms() {
		return program_execute_tms;
	}

	public void setProgram_execute_tms(String program_execute_tms) {
		this.program_execute_tms = program_execute_tms;
	}

	public String getAfter_prd_sql_id() {
		return after_prd_sql_id;
	}

	public void setAfter_prd_sql_id(String after_prd_sql_id) {
		this.after_prd_sql_id = after_prd_sql_id;
	}

	public String getStrSqlId() {
		return strSqlId;
	}

	public void setStrSqlId(String strSqlId) {
		this.strSqlId = strSqlId;
	}

	public String getStrDbio() {
		return strDbio;
	}

	public void setStrDbio(String strDbio) {
		this.strDbio = strDbio;
	}

	public String getSelectSqlPerfTrace() {
		return selectSqlPerfTrace;
	}

	public void setSelectSqlPerfTrace(String selectSqlPerfTrace) {
		this.selectSqlPerfTrace = selectSqlPerfTrace;
	}

	public String getIsCheckFail() {
		return isCheckFail;
	}

	public void setIsCheckFail(String isCheckFail) {
		this.isCheckFail = isCheckFail;
	}

	public String getAfter_fail_yn_condition() {
		return after_fail_yn_condition;
	}

	public void setAfter_fail_yn_condition(String after_fail_yn_condition) {
		this.after_fail_yn_condition = after_fail_yn_condition;
	}

	public String getAuto_share() {
		return auto_share;
	}

	public void setAuto_share(String auto_share) {
		this.auto_share = auto_share;
	}

	public String getTuning_no_array() {
		return tuning_no_array;
	}

	public void setTuning_no_array(String tuning_no_array) {
		this.tuning_no_array = tuning_no_array;
	}

	public String getAfter_prd_sql_id_array() {
		return after_prd_sql_id_array;
	}

	public void setAfter_prd_sql_id_array(String after_prd_sql_id_array) {
		this.after_prd_sql_id_array = after_prd_sql_id_array;
	}

	public String getAfter_prd_plan_hash_value_array() {
		return after_prd_plan_hash_value_array;
	}

	public void setAfter_prd_plan_hash_value_array(String after_prd_plan_hash_value_array) {
		this.after_prd_plan_hash_value_array = after_prd_plan_hash_value_array;
	}

	public String getPerf_check_step_id_array() {
		return perf_check_step_id_array;
	}

	public void setPerf_check_step_id_array(String perf_check_step_id_array) {
		this.perf_check_step_id_array = perf_check_step_id_array;
	}

	public String getDbid_array() {
		return dbid_array;
	}

	public void setDbid_array(String dbid_array) {
		this.dbid_array = dbid_array;
	}

	public String getPerfr_id() {
		return perfr_id;
	}

	public void setPerfr_id(String perfr_id) {
		this.perfr_id = perfr_id;
	}

	public String getWrkjob_cd_array() {
		return wrkjob_cd_array;
	}

	public void setWrkjob_cd_array(String wrkjob_cd_array) {
		this.wrkjob_cd_array = wrkjob_cd_array;
	}

	public String getExecutions_array() {
		return executions_array;
	}

	public void setExecutions_array(String executions_array) {
		this.executions_array = executions_array;
	}

	public String getAvg_buffer_gets_array() {
		return avg_buffer_gets_array;
	}

	public void setAvg_buffer_gets_array(String avg_buffer_gets_array) {
		this.avg_buffer_gets_array = avg_buffer_gets_array;
	}

	public String getAvg_elapsed_time_array() {
		return avg_elapsed_time_array;
	}

	public void setAvg_elapsed_time_array(String avg_elapsed_time_array) {
		this.avg_elapsed_time_array = avg_elapsed_time_array;
	}

	public String getAvg_row_processed_array() {
		return avg_row_processed_array;
	}

	public void setAvg_row_processed_array(String avg_row_processed_array) {
		this.avg_row_processed_array = avg_row_processed_array;
	}

	public String getPerf_check_id_array() {
		return perf_check_id_array;
	}

	public void setPerf_check_id_array(String perf_check_id_array) {
		this.perf_check_id_array = perf_check_id_array;
	}

	public String getProgram_id_array() {
		return program_id_array;
	}

	public void setProgram_id_array(String program_id_array) {
		this.program_id_array = program_id_array;
	}

	public String getDbio_array() {
		return dbio_array;
	}

	public void setDbio_array(String dbio_array) {
		this.dbio_array = dbio_array;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}

	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public BigDecimal getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(BigDecimal elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public BigDecimal getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(BigDecimal buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public BigDecimal getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(BigDecimal rows_processed) {
		this.rows_processed = rows_processed;
	}

	public String getLast_active_time() {
		return last_active_time;
	}

	public void setLast_active_time(String last_active_time) {
		this.last_active_time = last_active_time;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}

	public String getBindValue_name() {
		return bindValue_name;
	}

	public void setBindValue_name(String bindValue_name) {
		this.bindValue_name = bindValue_name;
	}

	public String getBindValue_value() {
		return bindValue_value;
	}

	public void setBindValue_value(String bindValue_value) {
		this.bindValue_value = bindValue_value;
	}

	public String getProgram_source_desc() {
		return program_source_desc;
	}

	public void setProgram_source_desc(String program_source_desc) {
		this.program_source_desc = program_source_desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue_string() {
		return value_string;
	}

	public void setValue_string(String value_string) {
		this.value_string = value_string;
	}

	public BigDecimal getCost_percent() {
		return cost_percent;
	}

	public void setCost_percent(BigDecimal cost_percent) {
		this.cost_percent = cost_percent;
	}

	public BigDecimal getCpu_cost_percent() {
		return cpu_cost_percent;
	}

	public void setCpu_cost_percent(BigDecimal cpu_cost_percent) {
		this.cpu_cost_percent = cpu_cost_percent;
	}

	public BigDecimal getIo_cost_percent() {
		return io_cost_percent;
	}

	public void setIo_cost_percent(BigDecimal io_cost_percent) {
		this.io_cost_percent = io_cost_percent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getCpu_cost() {
		return cpu_cost;
	}

	public void setCpu_cost(String cpu_cost) {
		this.cpu_cost = cpu_cost;
	}

	public String getIo_cost() {
		return io_cost;
	}

	public void setIo_cost(String io_cost) {
		this.io_cost = io_cost;
	}

	public String getSql_fulltext() {
		return sql_fulltext;
	}

	public void setSql_fulltext(String sql_fulltext) {
		this.sql_fulltext = sql_fulltext;
	}

	public String getBig_table_threshold_cnt() {
		return big_table_threshold_cnt;
	}

	public void setBig_table_threshold_cnt(String big_table_threshold_cnt) {
		this.big_table_threshold_cnt = big_table_threshold_cnt;
	}

	public String getProgram_div_nm() {
		return program_div_nm;
	}

	public void setProgram_div_nm(String program_div_nm) {
		this.program_div_nm = program_div_nm;
	}

	public String getProgram_type_nm() {
		return program_type_nm;
	}

	public void setProgram_type_nm(String program_type_nm) {
		this.program_type_nm = program_type_nm;
	}

	public String getProgram_type_cd() {
		return program_type_cd;
	}

	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
	}

	public String getSql_command_type_nm() {
		return sql_command_type_nm;
	}

	public void setSql_command_type_nm(String sql_command_type_nm) {
		this.sql_command_type_nm = sql_command_type_nm;
	}

	public String getSql_command_type_cd() {
		return sql_command_type_cd;
	}

	public void setSql_command_type_cd(String sql_command_type_cd) {
		this.sql_command_type_cd = sql_command_type_cd;
	}

	public String getDynamic_sql_yn() {
		return dynamic_sql_yn;
	}

	public void setDynamic_sql_yn(String dynamic_sql_yn) {
		this.dynamic_sql_yn = dynamic_sql_yn;
	}

	public String getProgram_desc() {
		return program_desc;
	}

	public void setProgram_desc(String program_desc) {
		this.program_desc = program_desc;
	}

	public String getFile_nm() {
		return file_nm;
	}

	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}

	public String getDir_nm() {
		return dir_nm;
	}

	public void setDir_nm(String dir_nm) {
		this.dir_nm = dir_nm;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getLast_update_dt() {
		return last_update_dt;
	}

	public void setLast_update_dt(String last_update_dt) {
		this.last_update_dt = last_update_dt;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getSnap_id() {
		return snap_id;
	}

	public void setSnap_id(String snap_id) {
		this.snap_id = snap_id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BigDecimal getExec_time() {
		return exec_time;
	}

	public void setExec_time(BigDecimal exec_time) {
		this.exec_time = exec_time;
	}

	public String getExec_inst_id() {
		return exec_inst_id;
	}

	public void setExec_inst_id(String exec_inst_id) {
		this.exec_inst_id = exec_inst_id;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getExecution_plan() {
		return execution_plan;
	}

	public void setExecution_plan(String execution_plan) {
		this.execution_plan = execution_plan;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getImid() {
		return imid;
	}

	public void setImid(String imid) {
		this.imid = imid;
	}

	public String getObject_node() {
		return object_node;
	}

	public void setObject_node(String object_node) {
		this.object_node = object_node;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getObject_owner() {
		return object_owner;
	}

	public void setObject_owner(String object_owner) {
		this.object_owner = object_owner;
	}

	public String getObject_name() {
		return object_name;
	}

	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}

	public String getObject_type() {
		return object_type;
	}

	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}

	public String getOptimizer() {
		return optimizer;
	}

	public void setOptimizer(String optimizer) {
		this.optimizer = optimizer;
	}

	public String getCardinality() {
		return cardinality;
	}

	public void setCardinality(String cardinality) {
		this.cardinality = cardinality;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getOther_tag() {
		return other_tag;
	}

	public void setOther_tag(String other_tag) {
		this.other_tag = other_tag;
	}

	public String getPartition_start() {
		return partition_start;
	}

	public void setPartition_start(String partition_start) {
		this.partition_start = partition_start;
	}

	public String getPartition_stop() {
		return partition_stop;
	}

	public void setPartition_stop(String partition_stop) {
		this.partition_stop = partition_stop;
	}

	public String getAccess_predicates() {
		return access_predicates;
	}

	public void setAccess_predicates(String access_predicates) {
		this.access_predicates = access_predicates;
	}

	public String getFilter_predicates() {
		return filter_predicates;
	}

	public void setFilter_predicates(String filter_predicates) {
		this.filter_predicates = filter_predicates;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQblock_name() {
		return qblock_name;
	}

	public void setQblock_name(String qblock_name) {
		this.qblock_name = qblock_name;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getLog_dt() {
		return log_dt;
	}

	public void setLog_dt(String log_dt) {
		this.log_dt = log_dt;
	}

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
	}

	public String getDisk_reads() {
		return disk_reads;
	}

	public void setDisk_reads(String disk_reads) {
		this.disk_reads = disk_reads;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public BigDecimal getCpu_time() {
		return cpu_time;
	}

	public void setCpu_time(BigDecimal cpu_time) {
		this.cpu_time = cpu_time;
	}

	public BigDecimal getClwait_time() {
		return clwait_time;
	}

	public void setClwait_time(BigDecimal clwait_time) {
		this.clwait_time = clwait_time;
	}

	public BigDecimal getIowait_time() {
		return iowait_time;
	}

	public void setIowait_time(BigDecimal iowait_time) {
		this.iowait_time = iowait_time;
	}

	public BigDecimal getApwait_time() {
		return apwait_time;
	}

	public void setApwait_time(BigDecimal apwait_time) {
		this.apwait_time = apwait_time;
	}

	public BigDecimal getCcwait_time() {
		return ccwait_time;
	}

	public void setCcwait_time(BigDecimal ccwait_time) {
		this.ccwait_time = ccwait_time;
	}

	public BigDecimal getCpu_rate() {
		return cpu_rate;
	}

	public void setCpu_rate(BigDecimal cpu_rate) {
		this.cpu_rate = cpu_rate;
	}

	public BigDecimal getClwait_rate() {
		return clwait_rate;
	}

	public void setClwait_rate(BigDecimal clwait_rate) {
		this.clwait_rate = clwait_rate;
	}

	public BigDecimal getIowait_rate() {
		return iowait_rate;
	}

	public void setIowait_rate(BigDecimal iowait_rate) {
		this.iowait_rate = iowait_rate;
	}

	public BigDecimal getApwait_rate() {
		return apwait_rate;
	}

	public void setApwait_rate(BigDecimal apwait_rate) {
		this.apwait_rate = apwait_rate;
	}

	public BigDecimal getCcwait_rate() {
		return ccwait_rate;
	}

	public void setCcwait_rate(BigDecimal ccwait_rate) {
		this.ccwait_rate = ccwait_rate;
	}

	public String getParse_calls() {
		return parse_calls;
	}

	public void setParse_calls(String parse_calls) {
		this.parse_calls = parse_calls;
	}

	public String getFetches() {
		return fetches;
	}

	public void setFetches(String fetches) {
		this.fetches = fetches;
	}

	public String getPerf_check_indc_nm() {
		return perf_check_indc_nm;
	}

	public void setPerf_check_indc_nm(String perf_check_indc_nm) {
		this.perf_check_indc_nm = perf_check_indc_nm;
	}

	public String getIndc_pass_max_value() {
		return indc_pass_max_value;
	}

	public void setIndc_pass_max_value(String indc_pass_max_value) {
		this.indc_pass_max_value = indc_pass_max_value;
	}

	public String getExec_result_value() {
		return exec_result_value;
	}

	public void setExec_result_value(String exec_result_value) {
		this.exec_result_value = exec_result_value;
	}

	public String getPerf_check_result_div_nm() {
		return perf_check_result_div_nm;
	}

	public void setPerf_check_result_div_nm(String perf_check_result_div_nm) {
		this.perf_check_result_div_nm = perf_check_result_div_nm;
	}

	public String getPrd_exec_result_value() {
		return prd_exec_result_value;
	}

	public void setPrd_exec_result_value(String prd_exec_result_value) {
		this.prd_exec_result_value = prd_exec_result_value;
	}

	public String getPrd_perf_check_result_div_nm() {
		return prd_perf_check_result_div_nm;
	}

	public void setPrd_perf_check_result_div_nm(String prd_perf_check_result_div_nm) {
		this.prd_perf_check_result_div_nm = prd_perf_check_result_div_nm;
	}

	public String getException_yn() {
		return exception_yn;
	}

	public void setException_yn(String exception_yn) {
		this.exception_yn = exception_yn;
	}

	public String getPerf_check_result_desc() {
		return perf_check_result_desc;
	}

	public void setPerf_check_result_desc(String perf_check_result_desc) {
		this.perf_check_result_desc = perf_check_result_desc;
	}

	public String getPerf_check_result_div_cd() {
		return perf_check_result_div_cd;
	}

	public void setPerf_check_result_div_cd(String perf_check_result_div_cd) {
		this.perf_check_result_div_cd = perf_check_result_div_cd;
	}

	public String getPrd_perf_check_result_div_cd() {
		return prd_perf_check_result_div_cd;
	}

	public void setPrd_perf_check_result_div_cd(String prd_perf_check_result_div_cd) {
		this.prd_perf_check_result_div_cd = prd_perf_check_result_div_cd;
	}

	public String getPerf_check_indc_id() {
		return perf_check_indc_id;
	}

	public void setPerf_check_indc_id(String perf_check_indc_id) {
		this.perf_check_indc_id = perf_check_indc_id;
	}

	public String getPerf_check_meth_cd() {
		return perf_check_meth_cd;
	}

	public void setPerf_check_meth_cd(String perf_check_meth_cd) {
		this.perf_check_meth_cd = perf_check_meth_cd;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public BigDecimal getBefore_prd_elapsed_time() {
		return before_prd_elapsed_time;
	}

	public void setBefore_prd_elapsed_time(BigDecimal before_prd_elapsed_time) {
		this.before_prd_elapsed_time = before_prd_elapsed_time;
	}

	public String getBefore_prd_buffer_gets() {
		return before_prd_buffer_gets;
	}

	public void setBefore_prd_buffer_gets(String before_prd_buffer_gets) {
		this.before_prd_buffer_gets = before_prd_buffer_gets;
	}

	public String getBefore_prd_rows_processed() {
		return before_prd_rows_processed;
	}

	public void setBefore_prd_rows_processed(String before_prd_rows_processed) {
		this.before_prd_rows_processed = before_prd_rows_processed;
	}

	public String getException_prc_meth_nm() {
		return exception_prc_meth_nm;
	}

	public void setException_prc_meth_nm(String exception_prc_meth_nm) {
		this.exception_prc_meth_nm = exception_prc_meth_nm;
	}

	public void setChildren(List<Sqls> children) {
		this.children = children;
	}
	
	public Object getChildren() {
		return children;
	}

	public String getDiffTag() {
		return diffTag;
	}

	public void setDiffTag(String diffTag) {
		this.diffTag = diffTag;
	}
	
}
