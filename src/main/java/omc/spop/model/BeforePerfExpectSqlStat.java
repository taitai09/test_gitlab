package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.22	이원식	최초작성
 **********************************************************/

@Alias("beforePerfExpectSqlStat")
public class BeforePerfExpectSqlStat extends Base implements Jsonable {
    private String before_perf_expect_no;
    private String sql_id;
    private String dbio;
    private String before_plan_hash_value;
    private String after_plan_hash_value;
    private String plan_change_yn;
    private String perf_impact_type_cd;
    private String perf_impact_type_nm;
    private String before_executions;
    private String after_executions;
    private String before_rows_processed;
    private String after_rows_processed;
    private String before_elapsed_time;
    private String after_elapsed_time;
    private String before_buffer_gets;
    private String after_buffer_gets;
    private String before_disk_reads;
    private String after_disk_reads;
    private String buffer_increase_ratio;
    private String elapsed_time_increase_ratio;
    private String parsing_schema_name;
    private String module;
    private String sql_text;
    private String sql_profile_yn;
    private String sql_profile_nm;
    
	public String getBefore_perf_expect_no() {
		return before_perf_expect_no;
	}
	public void setBefore_perf_expect_no(String before_perf_expect_no) {
		this.before_perf_expect_no = before_perf_expect_no;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getDbio() {
		return dbio;
	}
	public void setDbio(String dbio) {
		this.dbio = dbio;
	}
	public String getBefore_plan_hash_value() {
		return before_plan_hash_value;
	}
	public void setBefore_plan_hash_value(String before_plan_hash_value) {
		this.before_plan_hash_value = before_plan_hash_value;
	}
	public String getAfter_plan_hash_value() {
		return after_plan_hash_value;
	}
	public void setAfter_plan_hash_value(String after_plan_hash_value) {
		this.after_plan_hash_value = after_plan_hash_value;
	}
	public String getPlan_change_yn() {
		return plan_change_yn;
	}
	public void setPlan_change_yn(String plan_change_yn) {
		this.plan_change_yn = plan_change_yn;
	}
	public String getPerf_impact_type_cd() {
		return perf_impact_type_cd;
	}
	public void setPerf_impact_type_cd(String perf_impact_type_cd) {
		this.perf_impact_type_cd = perf_impact_type_cd;
	}
	public String getPerf_impact_type_nm() {
		return perf_impact_type_nm;
	}
	public void setPerf_impact_type_nm(String perf_impact_type_nm) {
		this.perf_impact_type_nm = perf_impact_type_nm;
	}
	public String getBefore_executions() {
		return before_executions;
	}
	public void setBefore_executions(String before_executions) {
		this.before_executions = before_executions;
	}
	public String getAfter_executions() {
		return after_executions;
	}
	public void setAfter_executions(String after_executions) {
		this.after_executions = after_executions;
	}
	public String getBefore_rows_processed() {
		return before_rows_processed;
	}
	public void setBefore_rows_processed(String before_rows_processed) {
		this.before_rows_processed = before_rows_processed;
	}
	public String getAfter_rows_processed() {
		return after_rows_processed;
	}
	public void setAfter_rows_processed(String after_rows_processed) {
		this.after_rows_processed = after_rows_processed;
	}
	public String getBefore_elapsed_time() {
		return before_elapsed_time;
	}
	public void setBefore_elapsed_time(String before_elapsed_time) {
		this.before_elapsed_time = before_elapsed_time;
	}
	public String getAfter_elapsed_time() {
		return after_elapsed_time;
	}
	public void setAfter_elapsed_time(String after_elapsed_time) {
		this.after_elapsed_time = after_elapsed_time;
	}
	public String getBefore_buffer_gets() {
		return before_buffer_gets;
	}
	public void setBefore_buffer_gets(String before_buffer_gets) {
		this.before_buffer_gets = before_buffer_gets;
	}
	public String getAfter_buffer_gets() {
		return after_buffer_gets;
	}
	public void setAfter_buffer_gets(String after_buffer_gets) {
		this.after_buffer_gets = after_buffer_gets;
	}
	public String getBefore_disk_reads() {
		return before_disk_reads;
	}
	public void setBefore_disk_reads(String before_disk_reads) {
		this.before_disk_reads = before_disk_reads;
	}
	public String getAfter_disk_reads() {
		return after_disk_reads;
	}
	public void setAfter_disk_reads(String after_disk_reads) {
		this.after_disk_reads = after_disk_reads;
	}
	public String getBuffer_increase_ratio() {
		return buffer_increase_ratio;
	}
	public void setBuffer_increase_ratio(String buffer_increase_ratio) {
		this.buffer_increase_ratio = buffer_increase_ratio;
	}
	public String getElapsed_time_increase_ratio() {
		return elapsed_time_increase_ratio;
	}
	public void setElapsed_time_increase_ratio(String elapsed_time_increase_ratio) {
		this.elapsed_time_increase_ratio = elapsed_time_increase_ratio;
	}
	public String getParsing_schema_name() {
		return parsing_schema_name;
	}
	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}	
	public String getSql_profile_yn() {
		return sql_profile_yn;
	}
	public void setSql_profile_yn(String sql_profile_yn) {
		this.sql_profile_yn = sql_profile_yn;
	}
	public String getSql_profile_nm() {
		return sql_profile_nm;
	}
	public void setSql_profile_nm(String sql_profile_nm) {
		this.sql_profile_nm = sql_profile_nm;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("before_perf_expect_no",this.getBefore_perf_expect_no());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("dbio",this.getDbio());
		objJson.put("before_plan_hash_value",this.getBefore_plan_hash_value());
		objJson.put("after_plan_hash_value",this.getAfter_plan_hash_value());
		objJson.put("plan_change_yn",this.getPlan_change_yn());
		objJson.put("perf_impact_type_cd",this.getPerf_impact_type_cd());
		objJson.put("perf_impact_type_nm",this.getPerf_impact_type_nm());
		objJson.put("before_executions",StringUtil.parseFloat(this.getBefore_executions(),0));
		objJson.put("after_executions",StringUtil.parseFloat(this.getAfter_executions(),0));
		objJson.put("before_rows_processed",StringUtil.parseFloat(this.getBefore_rows_processed(),0));
		objJson.put("after_rows_processed",StringUtil.parseFloat(this.getAfter_rows_processed(),0));
		objJson.put("before_elapsed_time",StringUtil.parseFloat(this.getBefore_elapsed_time(),0));
		objJson.put("after_elapsed_time",StringUtil.parseFloat(this.getAfter_elapsed_time(),0));
		objJson.put("before_buffer_gets",StringUtil.parseFloat(this.getBefore_buffer_gets(),0));
		objJson.put("after_buffer_gets",StringUtil.parseFloat(this.getAfter_buffer_gets(),0));
		objJson.put("before_disk_reads",StringUtil.parseFloat(this.getBefore_disk_reads(),0));
		objJson.put("after_disk_reads",StringUtil.parseFloat(this.getAfter_disk_reads(),0));
		objJson.put("buffer_increase_ratio",StringUtil.parseFloat(this.getBuffer_increase_ratio(),0));
		objJson.put("elapsed_time_increase_ratio",StringUtil.parseFloat(this.getElapsed_time_increase_ratio(),0));
		objJson.put("parsing_schema_name",this.getParsing_schema_name());
		objJson.put("module",this.getModule());
		objJson.put("sql_text",this.getSql_text());
		objJson.put("sql_profile_yn",this.getSql_profile_yn());
		objJson.put("sql_profile_nm",this.getSql_profile_nm());

		return objJson;
	}		
}
