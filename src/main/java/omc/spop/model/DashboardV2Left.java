package omc.spop.model;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("dashboardV2Left")
public class DashboardV2Left extends Base implements Jsonable {

	private String user_id;
	private String check_day;
	private int cnt;
	private String rgb_color_value;
	private int total_cnt;
	private int fatal_cnt;
	private int critical_cnt;
	private int warning_cnt;
	private int info_cnt;
	private String grade;
	private String check_pref_nm;
	private String check_value1;
	private String check_grade_cd;
	private String check_seq;
	private String check_pref_id;
	private String base_day;
	private String diag_type_nm;
	private int d_1;
	private int increase;
	private int d_2;
	private int last_week;
	private String wrkjob_cd;
	private String diag_type_cd;
	private String grp_cd_id;
	private String sql_id;
	private BigDecimal elapsed_time;
	private BigInteger buffer_gets;
	private BigInteger executions;
	private BigDecimal ratio_buffer_gets;
	private int start_snap_id;
	private int end_snap_id;
	private BigInteger elapsed_time_threshold;
	private BigInteger buffer_gets_threshold;
	private BigInteger executions_threshold;
	private int topsql_cnt;
	private String plan_hash_value;
	private String gather_day_dash;
	private String check_date_topsql_diag_summary;
	
	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCheck_day() {
		return check_day;
	}

	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getRgb_color_value() {
		return rgb_color_value;
	}

	public void setRgb_color_value(String rgb_color_value) {
		this.rgb_color_value = rgb_color_value;
	}

	public int getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(int total_cnt) {
		this.total_cnt = total_cnt;
	}

	public int getFatal_cnt() {
		return fatal_cnt;
	}

	public void setFatal_cnt(int fatal_cnt) {
		this.fatal_cnt = fatal_cnt;
	}

	public int getCritical_cnt() {
		return critical_cnt;
	}

	public void setCritical_cnt(int critical_cnt) {
		this.critical_cnt = critical_cnt;
	}

	public int getWarning_cnt() {
		return warning_cnt;
	}

	public void setWarning_cnt(int warning_cnt) {
		this.warning_cnt = warning_cnt;
	}

	public int getInfo_cnt() {
		return info_cnt;
	}

	public void setInfo_cnt(int info_cnt) {
		this.info_cnt = info_cnt;
	}
	
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getCheck_pref_nm() {
		return check_pref_nm;
	}

	public void setCheck_pref_nm(String check_pref_nm) {
		this.check_pref_nm = check_pref_nm;
	}

	public String getCheck_value1() {
		return check_value1;
	}

	public void setCheck_value1(String check_value1) {
		this.check_value1 = check_value1;
	}

	public String getCheck_grade_cd() {
		return check_grade_cd;
	}

	public void setCheck_grade_cd(String check_grade_cd) {
		this.check_grade_cd = check_grade_cd;
	}

	public String getCheck_seq() {
		return check_seq;
	}

	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}

	public String getCheck_pref_id() {
		return check_pref_id;
	}

	public void setCheck_pref_id(String check_pref_id) {
		this.check_pref_id = check_pref_id;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getDiag_type_nm() {
		return diag_type_nm;
	}

	public void setDiag_type_nm(String diag_type_nm) {
		this.diag_type_nm = diag_type_nm;
	}

	public int getD_1() {
		return d_1;
	}

	public void setD_1(int d_1) {
		this.d_1 = d_1;
	}

	public int getIncrease() {
		return increase;
	}

	public void setIncrease(int increase) {
		this.increase = increase;
	}

	public int getD_2() {
		return d_2;
	}

	public void setD_2(int d_2) {
		this.d_2 = d_2;
	}

	public int getLast_week() {
		return last_week;
	}

	public void setLast_week(int last_week) {
		this.last_week = last_week;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getDiag_type_cd() {
		return diag_type_cd;
	}

	public void setDiag_type_cd(String diag_type_cd) {
		this.diag_type_cd = diag_type_cd;
	}

	public String getGrp_cd_id() {
		return grp_cd_id;
	}

	public void setGrp_cd_id(String grp_cd_id) {
		this.grp_cd_id = grp_cd_id;
	}
	
	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public BigDecimal getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(BigDecimal elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public BigInteger getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(BigInteger buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public BigInteger getExecutions() {
		return executions;
	}

	public void setExecutions(BigInteger executions) {
		this.executions = executions;
	}

	public BigDecimal getRatio_buffer_gets() {
		return ratio_buffer_gets;
	}

	public void setRatio_buffer_gets(BigDecimal ratio_buffer_gets) {
		this.ratio_buffer_gets = ratio_buffer_gets;
	}

	public int getStart_snap_id() {
		return start_snap_id;
	}

	public void setStart_snap_id(int start_snap_id) {
		this.start_snap_id = start_snap_id;
	}

	public int getEnd_snap_id() {
		return end_snap_id;
	}

	public void setEnd_snap_id(int end_snap_id) {
		this.end_snap_id = end_snap_id;
	}

	public BigInteger getElapsed_time_threshold() {
		return elapsed_time_threshold;
	}

	public void setElapsed_time_threshold(BigInteger elapsed_time_threshold) {
		this.elapsed_time_threshold = elapsed_time_threshold;
	}

	public BigInteger getBuffer_gets_threshold() {
		return buffer_gets_threshold;
	}

	public void setBuffer_gets_threshold(BigInteger buffer_gets_threshold) {
		this.buffer_gets_threshold = buffer_gets_threshold;
	}

	public BigInteger getExecutions_threshold() {
		return executions_threshold;
	}

	public void setExecutions_threshold(BigInteger executions_threshold) {
		this.executions_threshold = executions_threshold;
	}

	public int getTopsql_cnt() {
		return topsql_cnt;
	}

	public void setTopsql_cnt(int topsql_cnt) {
		this.topsql_cnt = topsql_cnt;
	}

	public String getGather_day_dash() {
		return gather_day_dash;
	}

	public void setGather_day_dash(String gather_day_dash) {
		this.gather_day_dash = gather_day_dash;
	}

	public String getCheck_date_topsql_diag_summary() {
		return check_date_topsql_diag_summary;
	}

	public void setCheck_date_topsql_diag_summary(String check_date_topsql_diag_summary) {
		this.check_date_topsql_diag_summary = check_date_topsql_diag_summary;
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
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
