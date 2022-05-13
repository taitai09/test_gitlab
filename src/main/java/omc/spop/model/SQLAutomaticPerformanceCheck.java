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

@Alias("sqlAutomaticPerformanceCheck")
public class SQLAutomaticPerformanceCheck extends Base implements Jsonable {
	private String project_nm;
	private String project_desc;
	private String original_dbid;
	private String original_db_name;
	private String original_db_abbr_nm;
	private String del_yn;
	private String project_id;
	private String sql_auto_perf_check_id;
	private String perf_check_target_dbid;
	private String perf_check_target_db_name;
	private String total_cnt;
	private String completed_cnt;
	private String performing_cnt;
	private String err_cnt;
	private String perf_check_force_close_yn;
	private String perf_check_error;
	private String perf_check_range_begin_dt;
	private String perf_check_range_end_dt;
	private String topn_cnt;
	private String literal_except_yn;
	private String plan_change_status;
	
	private String perf_impact_type_cd;
	private String sql_idfy_cond_type_nm;
	private BigDecimal buffer_increase_ratio;
	private BigDecimal elapsed_time_increase_ratio;
	private String plan_change_yn;
	private String sql_id;
	private String before_plan_hash_value;
	private String before_executions;
	private BigDecimal before_rows_processed;
	private BigDecimal before_elapsed_time;
	private BigDecimal before_buffer_gets;
	private BigDecimal before_disk_reads;
	private String before_fullscan_yn;
	private String before_partition_all_access_yn;
	private String after_plan_hash_value;
	private String after_executions;
	private String after_rows_processed;
	private BigDecimal after_elapsed_time;
	private String after_buffer_gets;
	private String after_disk_reads;
	private String alter_fullscan_yn;
	private String after_partition_all_access_yn;
	private String sql_command_type_cd;
	private String err_code;
	private String err_msg;
	private String sql_text;
	private String performance_yn;
	private String error_yn;
	private String select_sql;
	private String select_perf_impact;
	
	private String parsing_schema_name;			// OWNER
	private String perf_check_executer_id;		// Login ID
	
	/* chart Title */
	private String before_elapsed_time_title;
	private String after_elapsed_time_title;
	private String before_buffer_gets_title;
	private String after_buffer_gets_title;

	private String plan_change_y_title;
	private String plan_change_n_title;

	private String sql_command_select_title;
	private String sql_command_insert_title;
	private String sql_command_update_title;
	private String sql_command_delete_title;
	private String sql_command_merge_title;

	private String sql_command_improved_title;
	private String sql_command_regressed_title;
	private String sql_command_unchanged_title;
	private String sql_command_timeout_title;
	
	private String perf_impact_improved_title;
	private String perf_impact_regressed_title;
	private String perf_impact_unchanged_title;
	private String perf_impact_timeout_title;
	private String perf_impact_fetch_exceed_title;
	
	private String perf_chk_indc_n_title;
	private String perf_chk_indc_y_title;
	
	/* 성능 적합/부적합 */
	private String perf_chk_indc_n_chart;
	private String perf_chk_indc_y_chart;
	
	/* Elapsed Time */
	private BigDecimal before_elapsed_time_chart;
	private BigDecimal after_elapsed_time_chart;
	/* Buffer Gets */
	private BigDecimal before_buffer_gets_chart;
	private BigDecimal after_buffer_gets_chart;
	/* PLAN변경 */
	private int plan_change_y_chart;
	private int plan_change_n_chart;
	/* SQL유형 */
	private int sql_command_select_chart;
	private int sql_command_insert_chart;
	private int sql_command_update_chart;
	private int sql_command_delete_chart;
	private int sql_command_merge_chart;
	/* 성능 임팩트 */
	private int perf_impact_improved_chart;
	private int perf_impact_regressed_chart;
	private int perf_impact_unchanged_chart;
	private int perf_impact_timeout_chart;
	private int perf_impact_fetch_exceed_chart;
	
	private String authority_sql;
	private String grant_script;
	
	private String perf_check_name;
	private String perf_check_desc;
	private String perf_check_type_cd;
	private String data_yn;
	
	private String owner_list;
	private String module_list;
	private String table_name_list;
	private String all_sql_yn;
	private String sql_time_limt;
	private String sql_time_limt_cd;
	private String sql_time_direct_pref_value;
	
	private List<String> strOwnerList;
	private List<String> strModuleList;
	private List<String> strTableNameList;
	
	private String parent_project_id;
	private String parent_sql_auto_perf_check_id;
	private String verify_project_id;
	private String verify_sql_auto_perf_check_id;
	
	private String perf_check_sql_source_type_cd;
	private String max_fetch_cnt;
	
	private String perf_period_start_time;
	private String perf_period_end_time;
	private String extra_filter_predication;
	
	private String perf_compare_meth_cd;
	private String parallel_degree;
	private String dml_exec_yn;
	private String multiple_exec_cnt;
	private String multiple_bind_exec_cnt;
	
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

	public String getPerf_impact_fetch_exceed_title() {
		return perf_impact_fetch_exceed_title;
	}

	public void setPerf_impact_fetch_exceed_title(String perf_impact_fetch_exceed_title) {
		this.perf_impact_fetch_exceed_title = perf_impact_fetch_exceed_title;
	}

	public int getPerf_impact_fetch_exceed_chart() {
		return perf_impact_fetch_exceed_chart;
	}

	public void setPerf_impact_fetch_exceed_chart(int perf_impact_fetch_exceed_chart) {
		this.perf_impact_fetch_exceed_chart = perf_impact_fetch_exceed_chart;
	}

	public String getVerify_project_id() {
		return verify_project_id;
	}

	public void setVerify_project_id(String verify_project_id) {
		this.verify_project_id = verify_project_id;
	}

	public String getVerify_sql_auto_perf_check_id() {
		return verify_sql_auto_perf_check_id;
	}

	public void setVerify_sql_auto_perf_check_id(String verify_sql_auto_perf_check_id) {
		this.verify_sql_auto_perf_check_id = verify_sql_auto_perf_check_id;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getProject_desc() {
		return project_desc;
	}

	public void setProject_desc(String project_desc) {
		this.project_desc = project_desc;
	}

	public String getOriginal_dbid() {
		return original_dbid;
	}

	public void setOriginal_dbid(String original_dbid) {
		this.original_dbid = original_dbid;
	}

	public String getOriginal_db_name() {
		return original_db_name;
	}

	public void setOriginal_db_name(String original_db_name) {
		this.original_db_name = original_db_name;
	}

	public String getOriginal_db_abbr_nm() {
		return original_db_abbr_nm;
	}

	public void setOriginal_db_abbr_nm(String original_db_abbr_nm) {
		this.original_db_abbr_nm = original_db_abbr_nm;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}

	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}

	public String getPerf_check_target_dbid() {
		return perf_check_target_dbid;
	}

	public void setPerf_check_target_dbid(String perf_check_target_dbid) {
		this.perf_check_target_dbid = perf_check_target_dbid;
	}

	public String getPerf_check_target_db_name() {
		return perf_check_target_db_name;
	}

	public void setPerf_check_target_db_name(String perf_check_target_db_name) {
		this.perf_check_target_db_name = perf_check_target_db_name;
	}

	public String getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}

	public String getCompleted_cnt() {
		return completed_cnt;
	}

	public void setCompleted_cnt(String completed_cnt) {
		this.completed_cnt = completed_cnt;
	}

	public String getPerforming_cnt() {
		return performing_cnt;
	}

	public void setPerforming_cnt(String performing_cnt) {
		this.performing_cnt = performing_cnt;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}

	public String getPerf_check_force_close_yn() {
		return perf_check_force_close_yn;
	}

	public void setPerf_check_force_close_yn(String perf_check_force_close_yn) {
		this.perf_check_force_close_yn = perf_check_force_close_yn;
	}

	public String getPerf_check_error() {
		return perf_check_error;
	}

	public void setPerf_check_error(String perf_check_error) {
		this.perf_check_error = perf_check_error;
	}

	public String getPerf_check_range_begin_dt() {
		return perf_check_range_begin_dt;
	}

	public void setPerf_check_range_begin_dt(String perf_check_range_begin_dt) {
		this.perf_check_range_begin_dt = perf_check_range_begin_dt;
	}

	public String getPerf_check_range_end_dt() {
		return perf_check_range_end_dt;
	}

	public void setPerf_check_range_end_dt(String perf_check_range_end_dt) {
		this.perf_check_range_end_dt = perf_check_range_end_dt;
	}

	public String getTopn_cnt() {
		return topn_cnt;
	}

	public void setTopn_cnt(String topn_cnt) {
		this.topn_cnt = topn_cnt;
	}

	public String getLiteral_except_yn() {
		return literal_except_yn;
	}

	public void setLiteral_except_yn(String literal_except_yn) {
		this.literal_except_yn = literal_except_yn;
	}

	public String getPlan_change_status() {
		return plan_change_status;
	}

	public void setPlan_change_status(String plan_change_status) {
		this.plan_change_status = plan_change_status;
	}

	public String getPerf_impact_type_cd() {
		return perf_impact_type_cd;
	}

	public void setPerf_impact_type_cd(String perf_impact_type_cd) {
		this.perf_impact_type_cd = perf_impact_type_cd;
	}

	public String getSql_idfy_cond_type_nm() {
		return sql_idfy_cond_type_nm;
	}

	public void setSql_idfy_cond_type_nm(String sql_idfy_cond_type_nm) {
		this.sql_idfy_cond_type_nm = sql_idfy_cond_type_nm;
	}

	public BigDecimal getBuffer_increase_ratio() {
		return buffer_increase_ratio;
	}

	public void setBuffer_increase_ratio(BigDecimal buffer_increase_ratio) {
		this.buffer_increase_ratio = buffer_increase_ratio;
	}

	public BigDecimal getElapsed_time_increase_ratio() {
		return elapsed_time_increase_ratio;
	}

	public void setElapsed_time_increase_ratio(BigDecimal elapsed_time_increase_ratio) {
		this.elapsed_time_increase_ratio = elapsed_time_increase_ratio;
	}

	public String getPlan_change_yn() {
		return plan_change_yn;
	}

	public void setPlan_change_yn(String plan_change_yn) {
		this.plan_change_yn = plan_change_yn;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getBefore_plan_hash_value() {
		return before_plan_hash_value;
	}

	public void setBefore_plan_hash_value(String before_plan_hash_value) {
		this.before_plan_hash_value = before_plan_hash_value;
	}

	public String getBefore_executions() {
		return before_executions;
	}

	public void setBefore_executions(String before_executions) {
		this.before_executions = before_executions;
	}

	public BigDecimal getBefore_rows_processed() {
		return before_rows_processed;
	}

	public void setBefore_rows_processed(BigDecimal before_rows_processed) {
		this.before_rows_processed = before_rows_processed;
	}

	public BigDecimal getBefore_elapsed_time() {
		return before_elapsed_time;
	}

	public void setBefore_elapsed_time(BigDecimal before_elapsed_time) {
		this.before_elapsed_time = before_elapsed_time;
	}

	public BigDecimal getBefore_buffer_gets() {
		return before_buffer_gets;
	}

	public void setBefore_buffer_gets(BigDecimal before_buffer_gets) {
		this.before_buffer_gets = before_buffer_gets;
	}

	public BigDecimal getBefore_disk_reads() {
		return before_disk_reads;
	}

	public void setBefore_disk_reads(BigDecimal before_disk_reads) {
		this.before_disk_reads = before_disk_reads;
	}

	public String getBefore_fullscan_yn() {
		return before_fullscan_yn;
	}

	public void setBefore_fullscan_yn(String before_fullscan_yn) {
		this.before_fullscan_yn = before_fullscan_yn;
	}

	public String getBefore_partition_all_access_yn() {
		return before_partition_all_access_yn;
	}

	public void setBefore_partition_all_access_yn(String before_partition_all_access_yn) {
		this.before_partition_all_access_yn = before_partition_all_access_yn;
	}

	public String getAfter_plan_hash_value() {
		return after_plan_hash_value;
	}

	public void setAfter_plan_hash_value(String after_plan_hash_value) {
		this.after_plan_hash_value = after_plan_hash_value;
	}

	public String getAfter_executions() {
		return after_executions;
	}

	public void setAfter_executions(String after_executions) {
		this.after_executions = after_executions;
	}

	public String getAfter_rows_processed() {
		return after_rows_processed;
	}

	public void setAfter_rows_processed(String after_rows_processed) {
		this.after_rows_processed = after_rows_processed;
	}

	public BigDecimal getAfter_elapsed_time() {
		return after_elapsed_time;
	}

	public void setAfter_elapsed_time(BigDecimal after_elapsed_time) {
		this.after_elapsed_time = after_elapsed_time;
	}

	public String getAfter_buffer_gets() {
		return after_buffer_gets;
	}

	public void setAfter_buffer_gets(String after_buffer_gets) {
		this.after_buffer_gets = after_buffer_gets;
	}

	public String getAfter_disk_reads() {
		return after_disk_reads;
	}

	public void setAfter_disk_reads(String after_disk_reads) {
		this.after_disk_reads = after_disk_reads;
	}

	public String getAlter_fullscan_yn() {
		return alter_fullscan_yn;
	}

	public void setAlter_fullscan_yn(String alter_fullscan_yn) {
		this.alter_fullscan_yn = alter_fullscan_yn;
	}

	public String getAfter_partition_all_access_yn() {
		return after_partition_all_access_yn;
	}

	public void setAfter_partition_all_access_yn(String after_partition_all_access_yn) {
		this.after_partition_all_access_yn = after_partition_all_access_yn;
	}

	public String getSql_command_type_cd() {
		return sql_command_type_cd;
	}

	public void setSql_command_type_cd(String sql_command_type_cd) {
		this.sql_command_type_cd = sql_command_type_cd;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getPerformance_yn() {
		return performance_yn;
	}

	public void setPerformance_yn(String performance_yn) {
		this.performance_yn = performance_yn;
	}

	public String getError_yn() {
		return error_yn;
	}

	public void setError_yn(String error_yn) {
		this.error_yn = error_yn;
	}

	public String getSelect_sql() {
		return select_sql;
	}

	public void setSelect_sql(String select_sql) {
		this.select_sql = select_sql;
	}

	public String getSelect_perf_impact() {
		return select_perf_impact;
	}

	public void setSelect_perf_impact(String select_perf_impact) {
		this.select_perf_impact = select_perf_impact;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getPerf_check_executer_id() {
		return perf_check_executer_id;
	}

	public void setPerf_check_executer_id(String perf_check_executer_id) {
		this.perf_check_executer_id = perf_check_executer_id;
	}

	public String getBefore_elapsed_time_title() {
		return before_elapsed_time_title;
	}

	public void setBefore_elapsed_time_title(String before_elapsed_time_title) {
		this.before_elapsed_time_title = before_elapsed_time_title;
	}

	public String getAfter_elapsed_time_title() {
		return after_elapsed_time_title;
	}

	public void setAfter_elapsed_time_title(String after_elapsed_time_title) {
		this.after_elapsed_time_title = after_elapsed_time_title;
	}

	public String getBefore_buffer_gets_title() {
		return before_buffer_gets_title;
	}

	public void setBefore_buffer_gets_title(String before_buffer_gets_title) {
		this.before_buffer_gets_title = before_buffer_gets_title;
	}

	public String getAfter_buffer_gets_title() {
		return after_buffer_gets_title;
	}

	public void setAfter_buffer_gets_title(String after_buffer_gets_title) {
		this.after_buffer_gets_title = after_buffer_gets_title;
	}

	public String getPlan_change_y_title() {
		return plan_change_y_title;
	}

	public void setPlan_change_y_title(String plan_change_y_title) {
		this.plan_change_y_title = plan_change_y_title;
	}

	public String getPlan_change_n_title() {
		return plan_change_n_title;
	}

	public void setPlan_change_n_title(String plan_change_n_title) {
		this.plan_change_n_title = plan_change_n_title;
	}

	public String getSql_command_select_title() {
		return sql_command_select_title;
	}

	public void setSql_command_select_title(String sql_command_select_title) {
		this.sql_command_select_title = sql_command_select_title;
	}

	public String getSql_command_insert_title() {
		return sql_command_insert_title;
	}

	public void setSql_command_insert_title(String sql_command_insert_title) {
		this.sql_command_insert_title = sql_command_insert_title;
	}

	public String getSql_command_update_title() {
		return sql_command_update_title;
	}

	public void setSql_command_update_title(String sql_command_update_title) {
		this.sql_command_update_title = sql_command_update_title;
	}

	public String getSql_command_delete_title() {
		return sql_command_delete_title;
	}

	public void setSql_command_delete_title(String sql_command_delete_title) {
		this.sql_command_delete_title = sql_command_delete_title;
	}

	public String getSql_command_merge_title() {
		return sql_command_merge_title;
	}

	public void setSql_command_merge_title(String sql_command_merge_title) {
		this.sql_command_merge_title = sql_command_merge_title;
	}

	public String getSql_command_improved_title() {
		return sql_command_improved_title;
	}

	public void setSql_command_improved_title(String sql_command_improved_title) {
		this.sql_command_improved_title = sql_command_improved_title;
	}

	public String getSql_command_regressed_title() {
		return sql_command_regressed_title;
	}

	public void setSql_command_regressed_title(String sql_command_regressed_title) {
		this.sql_command_regressed_title = sql_command_regressed_title;
	}

	public String getSql_command_unchanged_title() {
		return sql_command_unchanged_title;
	}

	public void setSql_command_unchanged_title(String sql_command_unchanged_title) {
		this.sql_command_unchanged_title = sql_command_unchanged_title;
	}

	public String getSql_command_timeout_title() {
		return sql_command_timeout_title;
	}

	public void setSql_command_timeout_title(String sql_command_timeout_title) {
		this.sql_command_timeout_title = sql_command_timeout_title;
	}

	public String getPerf_impact_improved_title() {
		return perf_impact_improved_title;
	}

	public void setPerf_impact_improved_title(String perf_impact_improved_title) {
		this.perf_impact_improved_title = perf_impact_improved_title;
	}

	public String getPerf_impact_regressed_title() {
		return perf_impact_regressed_title;
	}

	public void setPerf_impact_regressed_title(String perf_impact_regressed_title) {
		this.perf_impact_regressed_title = perf_impact_regressed_title;
	}

	public String getPerf_impact_unchanged_title() {
		return perf_impact_unchanged_title;
	}

	public void setPerf_impact_unchanged_title(String perf_impact_unchanged_title) {
		this.perf_impact_unchanged_title = perf_impact_unchanged_title;
	}

	public String getPerf_impact_timeout_title() {
		return perf_impact_timeout_title;
	}

	public void setPerf_impact_timeout_title(String perf_impact_timeout_title) {
		this.perf_impact_timeout_title = perf_impact_timeout_title;
	}

	public String getPerf_chk_indc_n_title() {
		return perf_chk_indc_n_title;
	}

	public void setPerf_chk_indc_n_title(String perf_chk_indc_n_title) {
		this.perf_chk_indc_n_title = perf_chk_indc_n_title;
	}

	public String getPerf_chk_indc_y_title() {
		return perf_chk_indc_y_title;
	}

	public void setPerf_chk_indc_y_title(String perf_chk_indc_y_title) {
		this.perf_chk_indc_y_title = perf_chk_indc_y_title;
	}

	public String getPerf_chk_indc_n_chart() {
		return perf_chk_indc_n_chart;
	}

	public void setPerf_chk_indc_n_chart(String perf_chk_indc_n_chart) {
		this.perf_chk_indc_n_chart = perf_chk_indc_n_chart;
	}

	public String getPerf_chk_indc_y_chart() {
		return perf_chk_indc_y_chart;
	}

	public void setPerf_chk_indc_y_chart(String perf_chk_indc_y_chart) {
		this.perf_chk_indc_y_chart = perf_chk_indc_y_chart;
	}

	public BigDecimal getBefore_elapsed_time_chart() {
		return before_elapsed_time_chart;
	}

	public void setBefore_elapsed_time_chart(BigDecimal before_elapsed_time_chart) {
		this.before_elapsed_time_chart = before_elapsed_time_chart;
	}

	public BigDecimal getAfter_elapsed_time_chart() {
		return after_elapsed_time_chart;
	}

	public void setAfter_elapsed_time_chart(BigDecimal after_elapsed_time_chart) {
		this.after_elapsed_time_chart = after_elapsed_time_chart;
	}

	public BigDecimal getBefore_buffer_gets_chart() {
		return before_buffer_gets_chart;
	}

	public void setBefore_buffer_gets_chart(BigDecimal before_buffer_gets_chart) {
		this.before_buffer_gets_chart = before_buffer_gets_chart;
	}

	public BigDecimal getAfter_buffer_gets_chart() {
		return after_buffer_gets_chart;
	}

	public void setAfter_buffer_gets_chart(BigDecimal after_buffer_gets_chart) {
		this.after_buffer_gets_chart = after_buffer_gets_chart;
	}

	public int getPlan_change_y_chart() {
		return plan_change_y_chart;
	}

	public void setPlan_change_y_chart(int plan_change_y_chart) {
		this.plan_change_y_chart = plan_change_y_chart;
	}

	public int getPlan_change_n_chart() {
		return plan_change_n_chart;
	}

	public void setPlan_change_n_chart(int plan_change_n_chart) {
		this.plan_change_n_chart = plan_change_n_chart;
	}

	public int getSql_command_select_chart() {
		return sql_command_select_chart;
	}

	public void setSql_command_select_chart(int sql_command_select_chart) {
		this.sql_command_select_chart = sql_command_select_chart;
	}

	public int getSql_command_insert_chart() {
		return sql_command_insert_chart;
	}

	public void setSql_command_insert_chart(int sql_command_insert_chart) {
		this.sql_command_insert_chart = sql_command_insert_chart;
	}

	public int getSql_command_update_chart() {
		return sql_command_update_chart;
	}

	public void setSql_command_update_chart(int sql_command_update_chart) {
		this.sql_command_update_chart = sql_command_update_chart;
	}

	public int getSql_command_delete_chart() {
		return sql_command_delete_chart;
	}

	public void setSql_command_delete_chart(int sql_command_delete_chart) {
		this.sql_command_delete_chart = sql_command_delete_chart;
	}

	public int getSql_command_merge_chart() {
		return sql_command_merge_chart;
	}

	public void setSql_command_merge_chart(int sql_command_merge_chart) {
		this.sql_command_merge_chart = sql_command_merge_chart;
	}

	public int getPerf_impact_improved_chart() {
		return perf_impact_improved_chart;
	}

	public void setPerf_impact_improved_chart(int perf_impact_improved_chart) {
		this.perf_impact_improved_chart = perf_impact_improved_chart;
	}

	public int getPerf_impact_regressed_chart() {
		return perf_impact_regressed_chart;
	}

	public void setPerf_impact_regressed_chart(int perf_impact_regressed_chart) {
		this.perf_impact_regressed_chart = perf_impact_regressed_chart;
	}

	public int getPerf_impact_unchanged_chart() {
		return perf_impact_unchanged_chart;
	}

	public void setPerf_impact_unchanged_chart(int perf_impact_unchanged_chart) {
		this.perf_impact_unchanged_chart = perf_impact_unchanged_chart;
	}

	public int getPerf_impact_timeout_chart() {
		return perf_impact_timeout_chart;
	}

	public void setPerf_impact_timeout_chart(int perf_impact_timeout_chart) {
		this.perf_impact_timeout_chart = perf_impact_timeout_chart;
	}

	public String getAuthority_sql() {
		return authority_sql;
	}

	public void setAuthority_sql(String authority_sql) {
		this.authority_sql = authority_sql;
	}

	public String getGrant_script() {
		return grant_script;
	}

	public void setGrant_script(String grant_script) {
		this.grant_script = grant_script;
	}

	public String getPerf_check_name() {
		return perf_check_name;
	}

	public void setPerf_check_name(String perf_check_name) {
		this.perf_check_name = perf_check_name;
	}

	public String getPerf_check_desc() {
		return perf_check_desc;
	}

	public void setPerf_check_desc(String perf_check_desc) {
		this.perf_check_desc = perf_check_desc;
	}

	public String getPerf_check_type_cd() {
		return perf_check_type_cd;
	}

	public void setPerf_check_type_cd(String perf_check_type_cd) {
		this.perf_check_type_cd = perf_check_type_cd;
	}

	public String getData_yn() {
		return data_yn;
	}

	public void setData_yn(String data_yn) {
		this.data_yn = data_yn;
	}

	public String getOwner_list() {
		return owner_list;
	}

	public void setOwner_list(String owner_list) {
		this.owner_list = owner_list;
	}

	public String getModule_list() {
		return module_list;
	}

	public void setModule_list(String module_list) {
		this.module_list = module_list;
	}

	public String getTable_name_list() {
		return table_name_list;
	}

	public void setTable_name_list(String table_name_list) {
		this.table_name_list = table_name_list;
	}

	public String getAll_sql_yn() {
		return all_sql_yn;
	}

	public void setAll_sql_yn(String all_sql_yn) {
		this.all_sql_yn = all_sql_yn;
	}

	public String getSql_time_limt() {
		return sql_time_limt;
	}

	public void setSql_time_limt(String sql_time_limt) {
		this.sql_time_limt = sql_time_limt;
	}

	public String getSql_time_limt_cd() {
		return sql_time_limt_cd;
	}

	public void setSql_time_limt_cd(String sql_time_limt_cd) {
		this.sql_time_limt_cd = sql_time_limt_cd;
	}

	public String getSql_time_direct_pref_value() {
		return sql_time_direct_pref_value;
	}

	public void setSql_time_direct_pref_value(String sql_time_direct_pref_value) {
		this.sql_time_direct_pref_value = sql_time_direct_pref_value;
	}

	public List<String> getStrOwnerList() {
		return strOwnerList;
	}

	public void setStrOwnerList(List<String> strOwnerList) {
		this.strOwnerList = strOwnerList;
	}

	public List<String> getStrModuleList() {
		return strModuleList;
	}

	public void setStrModuleList(List<String> strModuleList) {
		this.strModuleList = strModuleList;
	}

	public List<String> getStrTableNameList() {
		return strTableNameList;
	}

	public void setStrTableNameList(List<String> strTableNameList) {
		this.strTableNameList = strTableNameList;
	}

	public String getParent_project_id() {
		return parent_project_id;
	}

	public void setParent_project_id(String parent_project_id) {
		this.parent_project_id = parent_project_id;
	}

	public String getParent_sql_auto_perf_check_id() {
		return parent_sql_auto_perf_check_id;
	}

	public void setParent_sql_auto_perf_check_id(String parent_sql_auto_perf_check_id) {
		this.parent_sql_auto_perf_check_id = parent_sql_auto_perf_check_id;
	}

	public String getPerf_check_sql_source_type_cd() {
		return perf_check_sql_source_type_cd;
	}

	public void setPerf_check_sql_source_type_cd(String perf_check_sql_source_type_cd) {
		this.perf_check_sql_source_type_cd = perf_check_sql_source_type_cd;
	}

	public String getMax_fetch_cnt() {
		return max_fetch_cnt;
	}

	public void setMax_fetch_cnt(String max_fetch_cnt) {
		this.max_fetch_cnt = max_fetch_cnt;
	}

	public String getPerf_period_start_time() {
		return perf_period_start_time;
	}

	public void setPerf_period_start_time(String perf_period_start_time) {
		this.perf_period_start_time = perf_period_start_time;
	}

	public String getPerf_period_end_time() {
		return perf_period_end_time;
	}

	public void setPerf_period_end_time(String perf_period_end_time) {
		this.perf_period_end_time = perf_period_end_time;
	}

	public String getExtra_filter_predication() {
		return extra_filter_predication;
	}

	public void setExtra_filter_predication(String extra_filter_predication) {
		this.extra_filter_predication = extra_filter_predication;
	}

	public String getPerf_compare_meth_cd() {
		return perf_compare_meth_cd;
	}

	public void setPerf_compare_meth_cd(String perf_compare_meth_cd) {
		this.perf_compare_meth_cd = perf_compare_meth_cd;
	}

	public String getParallel_degree() {
		return parallel_degree;
	}

	public void setParallel_degree(String parallel_degree) {
		this.parallel_degree = parallel_degree;
	}

	public String getDml_exec_yn() {
		return dml_exec_yn;
	}

	public void setDml_exec_yn(String dml_exec_yn) {
		this.dml_exec_yn = dml_exec_yn;
	}

	public String getMultiple_exec_cnt() {
		return multiple_exec_cnt;
	}

	public void setMultiple_exec_cnt(String multiple_exec_cnt) {
		this.multiple_exec_cnt = multiple_exec_cnt;
	}

	public String getMultiple_bind_exec_cnt() {
		return multiple_bind_exec_cnt;
	}

	public void setMultiple_bind_exec_cnt(String multiple_bind_exec_cnt) {
		this.multiple_bind_exec_cnt = multiple_bind_exec_cnt;
	}

}
