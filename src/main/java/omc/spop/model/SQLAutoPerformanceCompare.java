package omc.spop.model;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import oracle.sql.CLOB;

@Alias("sqlAutoPerformanceCompare")
public class SQLAutoPerformanceCompare extends Base implements Jsonable {
	private String project_id;
	private String child_project_id;
	private String project_nm;
	private String project_desc;
	private String original_dbid;
	private String original_db_name;
	private String original_db_abbr_nm;
	private String del_yn;
	private String sql_auto_perf_check_id;
	private String child_sql_auto_perf_check_id;
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
	
	private String perf_check_name;
	private String perf_check_desc;
	private String perf_check_type_cd;
	private String data_yn;
	
	private String owner_list;
	private String module_list;
	private String table_name_list;
	
	private String all_sql_yn;
	private String sql_time_limt_cd;
	private String sql_time_direct_pref_value;
	
	private String perf_check_executer_id;		// Login ID
	
	private String perf_impact_type_nm;
	private String buffer_increase_ratio;
	private String elapsed_time_increase_ratio;
	private String perf_check_result_yn;
	private String perf_check_result;
	private String plan_change_yn;
	private String sql_id;
	private String asis_plan_hash_value;
	private String tobe_plan_hash_value;
	private String asis_executions;
	private String tobe_executions;
	private String asis_rows_processed;
	private String tobe_rows_processed;
	private String asis_elapsed_time;
	private String tobe_elapsed_time;
	private String asis_total_elapsed_time;
	private String tobe_total_elapsed_time;
	private String asis_buffer_gets;
	private String tobe_buffer_gets;
	private String asis_fullscan_yn;
	private String tobe_fullscan_yn;
	private String asis_partition_all_access_yn;
	private String tobe_partition_all_access_yn;
	private String sql_command_type_cd;
	private String err_code;
	private String err_msg;
	private String sql_text;
	private String sql_text_web;
	private CLOB sql_text_excel;
	
	private String buffer_gets_1day;
	private String buffer_gets_regres;
	private String elapsed_time_regres;
	private String elapsed_time_ratio;
	private String total_elapsed_time_percent;
	private String buffer_gets_ratio;
	private String asis_total_buffer_gets;
	private String tobe_total_buffer_gets;
	private String total_buffer_gets_percent;
	private String tablespace_usage;
	
	private String perf_down_yn;
	private String notPerf_yn;
	private String error_yn;
	
	private String select_yn;
	private String dml_yn;
	private String all_dml_yn;
	private String fullScan_yn;
	private String partition_yn;
	private String error_dml_yn;
	
	private String tuning_status_nm;
	private String chkExcept;
	private String sqlExclude;
	
	private String sql_all_cnt;
	private String plan_change_cnt;
	private String tuning_selection_cnt;
	private String elapsed_time_std_cnt;
	private String buffer_std_cnt;
	private String tuning_end_cnt;
	private String tuning_cnt;
	private String elap_time_impr_ratio;
	private String buffer_impr_ratio;
	
	private String impra_elap_time;
	private String impra_buffer_cnt;
	
	private String check_range_period;
	private String sql_time_limt_nm;
	private String perf_check_exec_end_dt;
	private String perf_check_exec_time;
	
	private String verify_project_id;
	private String verify_sql_auto_perf_check_id;
	private String tuning_elap_time_impr_ratio;
	private String tuning_buffer_impr_ratio;
	private String verify_elap_time_impr_ratio;
	private String verify_buffer_impr_ratio;
	
	private String asis_perf_degrade_versus_yn;
	private String tuning_perf_degrade_versus_yn;
	private String parent_project_id;
	private String parent_sql_auto_perf_check_id;
	
	private String verify_tuning_no;
	private String before_tuning_no;
	private String verify_buffer_gets;
	private String verify_elapsed_time;
	private String verify_sql_id;
	private String asis_sql_id ;
	private String tuning_buffer_gets;
	private String tuning_elapsed_time;
	
	private String ordering;
	private String exadata_yn;

	private String sql_id_text;
	private String sql_full_text;
	private String operation_dbid;
	private String operation_plan_hash_value;
	private String operation_elapsed_time;
	private String operation_parsing_schema_name;
	private String elapsed_time_activity;
	private String operation_buffer_gets;
	private String buffer_gets_activity;
	private String operation_executions;
	private String operation_rows_processed;
	private String operation_tuning_no;
	
	private String tuning_requester_id;
	private String tuning_requester_wrkjob_cd;
	private String tuning_requester_tel_num;

	private String module;
	private String parsing_schema_name;
	private String avg_buffer_gets;
	private String max_buffer_gets;
	private String total_buffer_gets;
	private String avg_elapsed_time;
	private String max_elapsed_time;
	private String avg_cpu_time;
	private String avg_disk_reads;
	private String avg_row_processed;
	private String ratio_buffer_get_total;
	private String ratio_cpu_total;
	private String ratio_cpu_per_executions;
	private String tuning_prgrs_step_seq;
	
	private String perf_check_result_desc;
	private String perf_check_indc_nm;
	private String pass_max_value;
	private String exec_result_value;
	private String perf_check_result_div_nm;
	
	private String bind_var_nm;
	private String bind_var_value;
	private String bind_var_type;
	
	private String execution_plan;
	private String db_operate_type_cd;
	
	private String perf_check_exec_begin_dt;
	private String perf_check_sql_source_type_cd;
	
	private String table_name;
	private String table_owner;
	private String index_name;
	private String sql_cnt;

	private String asid_oracle_version_cd;
	private String tobe_oracle_version_cd;
	
	private String condition;
	private String reg_dt;
	private String choice_div_cd;
	
	private List<String> strOwnerList;
	private List<String> strModuleList;
	private List<String> strTableNameList;
	
	private String max_fetch_cnt;
	private String sql_profile_nm;
	private String sql_profile_yn;
	private String not_sql_profile_yn;
	private String sql_review_yn;
	private String not_sql_review_yn;
	
	private String bind_set_seq;
	private String transfer_dbid;
	
	private String sqlIdList;
	private String planHashValueList;
	
	private String asis_db_name;
	private String tobe_db_name;
	private String asis_dbid;
	private String tobe_dbid;
	private String complete_percent;
	private String exec_time;
	
	private String perf_period_start_time;
	private String perf_period_end_time;
	
	private String extra_filter_predication;
	
	private String perf_compare_meth_cd;
	private String parallel_degree;
	private String dml_exec_yn;
	private String multiple_exec_cnt;
	private String multiple_bind_exec_cnt;
	
	private String inProgress;
	private String completion;
	
	private String plan_hash_value;
	private String elapsed_time;
	private String buffer_gets;
	private String rows_processed;
	private String planCompare;
	
	private String search_sql_id;
	private String review_sbst;
	private String review_keyword;
	
	private String line_up_cd;
	private String orderOf;
	private String timeOut_yn;
	private String maxFetch_yn;

	private String exec_status;
	private String table_count;
	private String access_path_count;
	private String access_path_column_list;
	private String table_cnt;
	private String access_path_exec_dt;
	private String index_exec_end_dt;
	private String exec_seq;

	private String idx_selectvity_calc_meth_cd;

	private String acces_path_exec_yn;
	private String acces_path_end_yn;
	private String index_db_create_exec_yn;
	private String index_recommend_exec_yn;
	private String index_recommend_end_yn;

	private String running_table_cnt;
	private String recommend_index_cnt;
	private String recommend_index_add_cnt;
	private String recommend_index_modify_cnt;
	private String recommend_index_unused_cnt;
	private String idx_ad_no;
	private String idx_work_div_cd;

	private String analysis_sql_cnt;
	private String tablespace_name;
	private String database_kinds_cd;
	private String all_idx_cnt;
	private String tot_cnt;

	private String total_cnt_1st;
	private String completed_cnt_1st;
	private String err_cnt_1st;

	private String total_cnt_2nd;
	private String completed_cnt_2nd;
	private String err_cnt_2nd;

	private String perf_check_exec_yn;
	private String perf_check_end_yn;
	private String index_db_drop_end_yn;
	private String index_db_create_end_yn;
	private String index_db_drop_exec_yn;
	private String oneRow;

	private String exec_end_dt;
	private String exec_start_dt;
	private String idx_db_work_cnt;
	private String err_sbst;
	private String idx_db_work_id;


	@Override
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

	public String getTablespace_usage() {
		return tablespace_usage;
	}

	public void setTablespace_usage(String tablespace_usage) {
		this.tablespace_usage = tablespace_usage;
	}

	public String getAsis_total_elapsed_time() {
		return asis_total_elapsed_time;
	}

	public void setAsis_total_elapsed_time(String asis_total_elapsed_time) {
		this.asis_total_elapsed_time = asis_total_elapsed_time;
	}

	public String getTobe_total_elapsed_time() {
		return tobe_total_elapsed_time;
	}

	public void setTobe_total_elapsed_time(String tobe_total_elapsed_time) {
		this.tobe_total_elapsed_time = tobe_total_elapsed_time;
	}

	public String getTotal_elapsed_time_percent() {
		return total_elapsed_time_percent;
	}

	public void setTotal_elapsed_time_percent(String total_elapsed_time_percent) {
		this.total_elapsed_time_percent = total_elapsed_time_percent;
	}

	public String getTotal_buffer_gets_percent() {
		return total_buffer_gets_percent;
	}

	public void setTotal_buffer_gets_percent(String total_buffer_gets_percent) {
		this.total_buffer_gets_percent = total_buffer_gets_percent;
	}

	public String getAsis_total_buffer_gets() {
		return asis_total_buffer_gets;
	}

	public void setAsis_total_buffer_gets(String asis_total_buffer_gets) {
		this.asis_total_buffer_gets = asis_total_buffer_gets;
	}

	public String getTobe_total_buffer_gets() {
		return tobe_total_buffer_gets;
	}

	public void setTobe_total_buffer_gets(String tobe_total_buffer_gets) {
		this.tobe_total_buffer_gets = tobe_total_buffer_gets;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getSql_cnt() {
		return sql_cnt;
	}

	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}

	public String getLine_up_cd() {
		return line_up_cd;
	}

	public void setLine_up_cd(String line_up_cd) {
		this.line_up_cd = line_up_cd;
	}

	public String getOrderOf() {
		return orderOf;
	}

	public void setOrderOf(String orderOf) {
		this.orderOf = orderOf;
	}

	public String getTimeOut_yn() {
		return timeOut_yn;
	}

	public void setTimeOut_yn(String timeOut_yn) {
		this.timeOut_yn = timeOut_yn;
	}

	public String getMaxFetch_yn() {
		return maxFetch_yn;
	}

	public void setMaxFetch_yn(String maxFetch_yn) {
		this.maxFetch_yn = maxFetch_yn;
	}

	public String getOperation_parsing_schema_name() {
		return operation_parsing_schema_name;
	}

	public void setOperation_parsing_schema_name(String operation_parsing_schema_name) {
		this.operation_parsing_schema_name = operation_parsing_schema_name;
	}

	public String getNot_sql_profile_yn() {
		return not_sql_profile_yn;
	}

	public void setNot_sql_profile_yn(String not_sql_profile_yn) {
		this.not_sql_profile_yn = not_sql_profile_yn;
	}

	public String getSql_review_yn() {
		return sql_review_yn;
	}

	public void setSql_review_yn(String sql_review_yn) {
		this.sql_review_yn = sql_review_yn;
	}

	public String getNot_sql_review_yn() {
		return not_sql_review_yn;
	}

	public void setNot_sql_review_yn(String not_sql_review_yn) {
		this.not_sql_review_yn = not_sql_review_yn;
	}

	public String getSelect_yn() {
		return select_yn;
	}

	public void setSelect_yn(String select_yn) {
		this.select_yn = select_yn;
	}

	public String getReview_keyword() {
		return review_keyword;
	}

	public void setReview_keyword(String review_keyword) {
		this.review_keyword = review_keyword;
	}

	public String getReview_sbst() {
		return review_sbst;
	}

	public void setReview_sbst(String review_sbst) {
		this.review_sbst = review_sbst;
	}

	public String getSearch_sql_id() {
		return search_sql_id;
	}

	public void setSearch_sql_id(String search_sql_id) {
		this.search_sql_id = search_sql_id;
	}

	public String getPlanCompare() {
		return planCompare;
	}

	public void setPlanCompare(String planCompare) {
		this.planCompare = planCompare;
	}

	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}

	public String getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public String getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public String getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}

	public String getInProgress() {
		return inProgress;
	}

	public void setInProgress(String inProgress) {
		this.inProgress = inProgress;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
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

	public String getExtra_filter_predication() {
		return extra_filter_predication;
	}

	public void setExtra_filter_predication(String extra_filter_predication) {
		this.extra_filter_predication = extra_filter_predication;
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

	public String getComplete_percent() {
		return complete_percent;
	}

	public void setComplete_percent(String complete_percent) {
		this.complete_percent = complete_percent;
	}

	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}

	public String getAsis_db_name() {
		return asis_db_name;
	}

	public void setAsis_db_name(String asis_db_name) {
		this.asis_db_name = asis_db_name;
	}

	public String getTobe_db_name() {
		return tobe_db_name;
	}

	public void setTobe_db_name(String tobe_db_name) {
		this.tobe_db_name = tobe_db_name;
	}

	public String getAsis_dbid() {
		return asis_dbid;
	}

	public void setAsis_db_id(String asis_dbid) {
		this.asis_dbid = asis_dbid;
	}

	public String getTobe_dbid() {
		return tobe_dbid;
	}

	public void setTobe_dbid(String tobe_dbid) {
		this.tobe_dbid = tobe_dbid;
	}

	public String getSqlIdList() {
		return sqlIdList;
	}

	public void setSqlIdList(String sqlIdList) {
		this.sqlIdList = sqlIdList;
	}

	public String getPlanHashValueList() {
		return planHashValueList;
	}

	public void setPlanHashValueList(String planHashValueList) {
		this.planHashValueList = planHashValueList;
	}

	public String getTransfer_dbid() {
		return transfer_dbid;
	}

	public void setTransfer_dbid(String transfer_dbid) {
		this.transfer_dbid = transfer_dbid;
	}

	public String getBind_set_seq() {
		return bind_set_seq;
	}

	public void setBind_set_seq(String bind_set_seq) {
		this.bind_set_seq = bind_set_seq;
	}

	public String getChoice_div_cd() {
		return choice_div_cd;
	}

	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}

	public String getSql_profile_nm() {
		return sql_profile_nm;
	}

	public void setSql_profile_nm(String sql_profile_nm) {
		this.sql_profile_nm = sql_profile_nm;
	}

	public String getSql_profile_yn() {
		return sql_profile_yn;
	}

	public void setSql_profile_yn(String sql_profile_yn) {
		this.sql_profile_yn = sql_profile_yn;
	}

	public String getMax_fetch_cnt() {
		return max_fetch_cnt;
	}

	public void setMax_fetch_cnt(String max_fetch_cnt) {
		this.max_fetch_cnt = max_fetch_cnt;
	}

	public String getAsid_oracle_version_cd() {
		return asid_oracle_version_cd;
	}

	public void setAsid_oracle_version_cd(String asid_oracle_version_cd) {
		this.asid_oracle_version_cd = asid_oracle_version_cd;
	}

	public String getTobe_oracle_version_cd() {
		return tobe_oracle_version_cd;
	}

	public void setTobe_oracle_version_cd(String tobe_oracle_version_cd) {
		this.tobe_oracle_version_cd = tobe_oracle_version_cd;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getTable_owner() {
		return table_owner;
	}

	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}

	public String getPerf_check_sql_source_type_cd() {
		return perf_check_sql_source_type_cd;
	}

	public void setPerf_check_sql_source_type_cd(String perf_check_sql_source_type_cd) {
		this.perf_check_sql_source_type_cd = perf_check_sql_source_type_cd;
	}

	public String getPerf_check_exec_begin_dt() {
		return perf_check_exec_begin_dt;
	}

	public void setPerf_check_exec_begin_dt(String perf_check_exec_begin_dt) {
		this.perf_check_exec_begin_dt = perf_check_exec_begin_dt;
	}

	public String getDb_operate_type_cd() {
		return db_operate_type_cd;
	}

	public void setDb_operate_type_cd(String db_operate_type_cd) {
		this.db_operate_type_cd = db_operate_type_cd;
	}

	public String getExecution_plan() {
		return execution_plan;
	}

	public void setExecution_plan(String execution_plan) {
		this.execution_plan = execution_plan;
	}

	public String getBind_var_nm() {
		return bind_var_nm;
	}

	public void setBind_var_nm(String bind_var_nm) {
		this.bind_var_nm = bind_var_nm;
	}

	public String getBind_var_value() {
		return bind_var_value;
	}

	public void setBind_var_value(String bind_var_value) {
		this.bind_var_value = bind_var_value;
	}

	public String getBind_var_type() {
		return bind_var_type;
	}

	public void setBind_var_type(String bind_var_type) {
		this.bind_var_type = bind_var_type;
	}

	public String getPerf_check_result_desc() {
		return perf_check_result_desc;
	}

	public void setPerf_check_result_desc(String perf_check_result_desc) {
		this.perf_check_result_desc = perf_check_result_desc;
	}

	public String getPerf_check_indc_nm() {
		return perf_check_indc_nm;
	}

	public void setPerf_check_indc_nm(String perf_check_indc_nm) {
		this.perf_check_indc_nm = perf_check_indc_nm;
	}

	public String getPass_max_value() {
		return pass_max_value;
	}

	public void setPass_max_value(String pass_max_value) {
		this.pass_max_value = pass_max_value;
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

	public String getTuning_requester_id() {
		return tuning_requester_id;
	}

	public void setTuning_requester_id(String tuning_requester_id) {
		this.tuning_requester_id = tuning_requester_id;
	}

	public String getTuning_requester_wrkjob_cd() {
		return tuning_requester_wrkjob_cd;
	}

	public void setTuning_requester_wrkjob_cd(String tuning_requester_wrkjob_cd) {
		this.tuning_requester_wrkjob_cd = tuning_requester_wrkjob_cd;
	}

	public String getTuning_requester_tel_num() {
		return tuning_requester_tel_num;
	}

	public void setTuning_requester_tel_num(String tuning_requester_tel_num) {
		this.tuning_requester_tel_num = tuning_requester_tel_num;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getAvg_buffer_gets() {
		return avg_buffer_gets;
	}

	public void setAvg_buffer_gets(String avg_buffer_gets) {
		this.avg_buffer_gets = avg_buffer_gets;
	}

	public String getMax_buffer_gets() {
		return max_buffer_gets;
	}

	public void setMax_buffer_gets(String max_buffer_gets) {
		this.max_buffer_gets = max_buffer_gets;
	}

	public String getTotal_buffer_gets() {
		return total_buffer_gets;
	}

	public void setTotal_buffer_gets(String total_buffer_gets) {
		this.total_buffer_gets = total_buffer_gets;
	}

	public String getAvg_elapsed_time() {
		return avg_elapsed_time;
	}

	public void setAvg_elapsed_time(String avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}

	public String getMax_elapsed_time() {
		return max_elapsed_time;
	}

	public void setMax_elapsed_time(String max_elapsed_time) {
		this.max_elapsed_time = max_elapsed_time;
	}

	public String getAvg_cpu_time() {
		return avg_cpu_time;
	}

	public void setAvg_cpu_time(String avg_cpu_time) {
		this.avg_cpu_time = avg_cpu_time;
	}

	public String getAvg_disk_reads() {
		return avg_disk_reads;
	}

	public void setAvg_disk_reads(String avg_disk_reads) {
		this.avg_disk_reads = avg_disk_reads;
	}

	public String getAvg_row_processed() {
		return avg_row_processed;
	}

	public void setAvg_row_processed(String avg_row_processed) {
		this.avg_row_processed = avg_row_processed;
	}

	public String getRatio_buffer_get_total() {
		return ratio_buffer_get_total;
	}

	public void setRatio_buffer_get_total(String ratio_buffer_get_total) {
		this.ratio_buffer_get_total = ratio_buffer_get_total;
	}

	public String getRatio_cpu_total() {
		return ratio_cpu_total;
	}

	public void setRatio_cpu_total(String ratio_cpu_total) {
		this.ratio_cpu_total = ratio_cpu_total;
	}

	public String getRatio_cpu_per_executions() {
		return ratio_cpu_per_executions;
	}

	public void setRatio_cpu_per_executions(String ratio_cpu_per_executions) {
		this.ratio_cpu_per_executions = ratio_cpu_per_executions;
	}

	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}

	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}

	public String getPerf_check_result() {
		return perf_check_result;
	}

	public void setPerf_check_result(String perf_check_result) {
		this.perf_check_result = perf_check_result;
	}

	public String getOperation_tuning_no() {
		return operation_tuning_no;
	}

	public void setOperation_tuning_no(String operation_tuning_no) {
		this.operation_tuning_no = operation_tuning_no;
	}

	public String getOperation_plan_hash_value() {
		return operation_plan_hash_value;
	}

	public void setOperation_plan_hash_value(String operation_plan_hash_value) {
		this.operation_plan_hash_value = operation_plan_hash_value;
	}

	public String getOperation_elapsed_time() {
		return operation_elapsed_time;
	}

	public void setOperation_elapsed_time(String operation_elapsed_time) {
		this.operation_elapsed_time = operation_elapsed_time;
	}

	public String getElapsed_time_activity() {
		return elapsed_time_activity;
	}

	public void setElapsed_time_activity(String elapsed_time_activity) {
		this.elapsed_time_activity = elapsed_time_activity;
	}

	public String getOperation_buffer_gets() {
		return operation_buffer_gets;
	}

	public void setOperation_buffer_gets(String operation_buffer_gets) {
		this.operation_buffer_gets = operation_buffer_gets;
	}

	public String getBuffer_gets_activity() {
		return buffer_gets_activity;
	}

	public void setBuffer_gets_activity(String buffer_gets_activity) {
		this.buffer_gets_activity = buffer_gets_activity;
	}

	public String getOperation_executions() {
		return operation_executions;
	}

	public void setOperation_executions(String operation_executions) {
		this.operation_executions = operation_executions;
	}

	public String getOperation_rows_processed() {
		return operation_rows_processed;
	}

	public void setOperation_rows_processed(String operation_rows_processed) {
		this.operation_rows_processed = operation_rows_processed;
	}

	public String getOperation_dbid() {
		return operation_dbid;
	}

	public void setOperation_dbid(String operation_dbid) {
		this.operation_dbid = operation_dbid;
	}

	public String getSql_id_text() {
		return sql_id_text;
	}

	public void setSql_id_text(String sql_id_text) {
		this.sql_id_text = sql_id_text;
	}

	public String getSql_full_text() {
		return sql_full_text;
	}

	public void setSql_full_text(String sql_full_text) {
		this.sql_full_text = sql_full_text;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public String getExadata_yn() {
		return exadata_yn;
	}

	public void setExadata_yn(String exadata_yn) {
		this.exadata_yn = exadata_yn;
	}

	public String getAsis_sql_id() {
		return asis_sql_id;
	}

	public void setAsis_sql_id(String asis_sql_id) {
		this.asis_sql_id = asis_sql_id;
	}

	public String getVerify_tuning_no() {
		return verify_tuning_no;
	}

	public void setVerify_tuning_no(String verify_tuning_no) {
		this.verify_tuning_no = verify_tuning_no;
	}

	public String getBefore_tuning_no() {
		return before_tuning_no;
	}

	public void setBefore_tuning_no(String before_tuning_no) {
		this.before_tuning_no = before_tuning_no;
	}

	public String getVerify_buffer_gets() {
		return verify_buffer_gets;
	}

	public void setVerify_buffer_gets(String verify_buffer_gets) {
		this.verify_buffer_gets = verify_buffer_gets;
	}

	public String getVerify_elapsed_time() {
		return verify_elapsed_time;
	}

	public void setVerify_elapsed_time(String verify_elapsed_time) {
		this.verify_elapsed_time = verify_elapsed_time;
	}

	public String getVerify_sql_id() {
		return verify_sql_id;
	}

	public void setVerify_sql_id(String verify_sql_id) {
		this.verify_sql_id = verify_sql_id;
	}

	public String getTuning_buffer_gets() {
		return tuning_buffer_gets;
	}

	public void setTuning_buffer_gets(String tuning_buffer_gets) {
		this.tuning_buffer_gets = tuning_buffer_gets;
	}

	public String getTuning_elapsed_time() {
		return tuning_elapsed_time;
	}

	public void setTuning_elapsed_time(String tuning_elapsed_time) {
		this.tuning_elapsed_time = tuning_elapsed_time;
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

	public String getAsis_perf_degrade_versus_yn() {
		return asis_perf_degrade_versus_yn;
	}

	public void setAsis_perf_degrade_versus_yn(String asis_perf_degrade_versus_yn) {
		this.asis_perf_degrade_versus_yn = asis_perf_degrade_versus_yn;
	}

	public String getTuning_perf_degrade_versus_yn() {
		return tuning_perf_degrade_versus_yn;
	}

	public void setTuning_perf_degrade_versus_yn(String tuning_perf_degrade_versus_yn) {
		this.tuning_perf_degrade_versus_yn = tuning_perf_degrade_versus_yn;
	}

	public String getTuning_elap_time_impr_ratio() {
		return tuning_elap_time_impr_ratio;
	}

	public void setTuning_elap_time_impr_ratio(String tuning_elap_time_impr_ratio) {
		this.tuning_elap_time_impr_ratio = tuning_elap_time_impr_ratio;
	}

	public String getTuning_buffer_impr_ratio() {
		return tuning_buffer_impr_ratio;
	}

	public void setTuning_buffer_impr_ratio(String tuning_buffer_impr_ratio) {
		this.tuning_buffer_impr_ratio = tuning_buffer_impr_ratio;
	}

	public String getVerify_elap_time_impr_ratio() {
		return verify_elap_time_impr_ratio;
	}

	public void setVerify_elap_time_impr_ratio(String verify_elap_time_impr_ratio) {
		this.verify_elap_time_impr_ratio = verify_elap_time_impr_ratio;
	}

	public String getVerify_buffer_impr_ratio() {
		return verify_buffer_impr_ratio;
	}

	public void setVerify_buffer_impr_ratio(String verify_buffer_impr_ratio) {
		this.verify_buffer_impr_ratio = verify_buffer_impr_ratio;
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

	public String getAsis_partition_all_access_yn() {
		return asis_partition_all_access_yn;
	}

	public void setAsis_partition_all_access_yn(String asis_partition_all_access_yn) {
		this.asis_partition_all_access_yn = asis_partition_all_access_yn;
	}

	public String getCheck_range_period() {
		return check_range_period;
	}

	public void setCheck_range_period(String check_range_period) {
		this.check_range_period = check_range_period;
	}

	public String getSql_time_limt_nm() {
		return sql_time_limt_nm;
	}

	public void setSql_time_limt_nm(String sql_time_limt_nm) {
		this.sql_time_limt_nm = sql_time_limt_nm;
	}

	public String getPerf_check_exec_end_dt() {
		return perf_check_exec_end_dt;
	}

	public void setPerf_check_exec_end_dt(String perf_check_exec_end_dt) {
		this.perf_check_exec_end_dt = perf_check_exec_end_dt;
	}

	public String getPerf_check_exec_time() {
		return perf_check_exec_time;
	}

	public void setPerf_check_exec_time(String perf_check_exec_time) {
		this.perf_check_exec_time = perf_check_exec_time;
	}

	public String getSql_all_cnt() {
		return sql_all_cnt;
	}

	public void setSql_all_cnt(String sql_all_cnt) {
		this.sql_all_cnt = sql_all_cnt;
	}

	public String getPlan_change_cnt() {
		return plan_change_cnt;
	}

	public void setPlan_change_cnt(String plan_change_cnt) {
		this.plan_change_cnt = plan_change_cnt;
	}

	public String getTuning_selection_cnt() {
		return tuning_selection_cnt;
	}

	public void setTuning_selection_cnt(String tuning_selection_cnt) {
		this.tuning_selection_cnt = tuning_selection_cnt;
	}

	public String getElapsed_time_std_cnt() {
		return elapsed_time_std_cnt;
	}

	public void setElapsed_time_std_cnt(String elapsed_time_std_cnt) {
		this.elapsed_time_std_cnt = elapsed_time_std_cnt;
	}

	public String getBuffer_std_cnt() {
		return buffer_std_cnt;
	}

	public void setBuffer_std_cnt(String buffer_std_cnt) {
		this.buffer_std_cnt = buffer_std_cnt;
	}

	public String getTuning_end_cnt() {
		return tuning_end_cnt;
	}

	public void setTuning_end_cnt(String tuning_end_cnt) {
		this.tuning_end_cnt = tuning_end_cnt;
	}

	public String getTuning_cnt() {
		return tuning_cnt;
	}

	public void setTuning_cnt(String tuning_cnt) {
		this.tuning_cnt = tuning_cnt;
	}

	public String getElap_time_impr_ratio() {
		return elap_time_impr_ratio;
	}

	public void setElap_time_impr_ratio(String elap_time_impr_ratio) {
		this.elap_time_impr_ratio = elap_time_impr_ratio;
	}

	public String getBuffer_impr_ratio() {
		return buffer_impr_ratio;
	}

	public void setBuffer_impr_ratio(String buffer_impr_ratio) {
		this.buffer_impr_ratio = buffer_impr_ratio;
	}

	public String getImpra_elap_time() {
		return impra_elap_time;
	}

	public void setImpra_elap_time(String impra_elap_time) {
		this.impra_elap_time = impra_elap_time;
	}

	public String getImpra_buffer_cnt() {
		return impra_buffer_cnt;
	}

	public void setImpra_buffer_cnt(String impra_buffer_cnt) {
		this.impra_buffer_cnt = impra_buffer_cnt;
	}

	public String getSqlExclude() {
		return sqlExclude;
	}

	public void setSqlExclude(String sqlExclude) {
		this.sqlExclude = sqlExclude;
	}

	public String getChkExcept() {
		return chkExcept;
	}

	public void setChkExcept(String chkExcept) {
		this.chkExcept = chkExcept;
	}

	public String getTuning_status_nm() {
		return tuning_status_nm;
	}

	public void setTuning_status_nm(String tuning_status_nm) {
		this.tuning_status_nm = tuning_status_nm;
	}

	public String getDml_yn() {
		return dml_yn;
	}

	public void setDml_yn(String dml_yn) {
		this.dml_yn = dml_yn;
	}

	public String getPerf_impact_type_nm() {
		return perf_impact_type_nm;
	}

	public void setPerf_impact_type_nm(String perf_impact_type_nm) {
		this.perf_impact_type_nm = perf_impact_type_nm;
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

	public String getPerf_check_result_yn() {
		return perf_check_result_yn;
	}

	public void setPerf_check_result_yn(String perf_check_result_yn) {
		this.perf_check_result_yn = perf_check_result_yn;
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

	public String getAsis_plan_hash_value() {
		return asis_plan_hash_value;
	}

	public void setAsis_plan_hash_value(String asis_plan_hash_value) {
		this.asis_plan_hash_value = asis_plan_hash_value;
	}

	public String getTobe_plan_hash_value() {
		return tobe_plan_hash_value;
	}

	public void setTobe_plan_hash_value(String tobe_plan_hash_value) {
		this.tobe_plan_hash_value = tobe_plan_hash_value;
	}

	public String getAsis_executions() {
		return asis_executions;
	}

	public void setAsis_executions(String asis_executions) {
		this.asis_executions = asis_executions;
	}

	public String getTobe_executions() {
		return tobe_executions;
	}

	public void setTobe_executions(String tobe_executions) {
		this.tobe_executions = tobe_executions;
	}

	public String getAsis_rows_processed() {
		return asis_rows_processed;
	}

	public void setAsis_rows_processed(String asis_rows_processed) {
		this.asis_rows_processed = asis_rows_processed;
	}

	public String getTobe_rows_processed() {
		return tobe_rows_processed;
	}

	public void setTobe_rows_processed(String tobe_rows_processed) {
		this.tobe_rows_processed = tobe_rows_processed;
	}

	public String getAsis_elapsed_time() {
		return asis_elapsed_time;
	}

	public void setAsis_elapsed_time(String asis_elapsed_time) {
		this.asis_elapsed_time = asis_elapsed_time;
	}

	public String getTobe_elapsed_time() {
		return tobe_elapsed_time;
	}

	public void setTobe_elapsed_time(String tobe_elapsed_time) {
		this.tobe_elapsed_time = tobe_elapsed_time;
	}

	public String getAsis_buffer_gets() {
		return asis_buffer_gets;
	}

	public void setAsis_buffer_gets(String asis_buffer_gets) {
		this.asis_buffer_gets = asis_buffer_gets;
	}

	public String getTobe_buffer_gets() {
		return tobe_buffer_gets;
	}

	public void setTobe_buffer_gets(String tobe_buffer_gets) {
		this.tobe_buffer_gets = tobe_buffer_gets;
	}

	public String getAsis_fullscan_yn() {
		return asis_fullscan_yn;
	}

	public void setAsis_fullscan_yn(String asis_fullscan_yn) {
		this.asis_fullscan_yn = asis_fullscan_yn;
	}

	public String getTobe_fullscan_yn() {
		return tobe_fullscan_yn;
	}

	public void setTobe_fullscan_yn(String tobe_fullscan_yn) {
		this.tobe_fullscan_yn = tobe_fullscan_yn;
	}

	public String getTobe_partition_all_access_yn() {
		return tobe_partition_all_access_yn;
	}

	public void setTobe_partition_all_access_yn(String tobe_partition_all_access_yn) {
		this.tobe_partition_all_access_yn = tobe_partition_all_access_yn;
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

	public String getBuffer_gets_1day() {
		return buffer_gets_1day;
	}

	public void setBuffer_gets_1day(String buffer_gets_1day) {
		this.buffer_gets_1day = buffer_gets_1day;
	}

	public String getBuffer_gets_regres() {
		return buffer_gets_regres;
	}

	public void setBuffer_gets_regres(String buffer_gets_regres) {
		this.buffer_gets_regres = buffer_gets_regres;
	}

	public String getElapsed_time_regres() {
		return elapsed_time_regres;
	}

	public void setElapsed_time_regres(String elapsed_time_regres) {
		this.elapsed_time_regres = elapsed_time_regres;
	}

	public String getElapsed_time_ratio() {
		return elapsed_time_ratio;
	}

	public void setElapsed_time_ratio(String elapsed_time_ratio) {
		this.elapsed_time_ratio = elapsed_time_ratio;
	}

	public String getBuffer_gets_ratio() {
		return buffer_gets_ratio;
	}

	public void setBuffer_gets_ratio(String buffer_gets_ratio) {
		this.buffer_gets_ratio = buffer_gets_ratio;
	}

	public String getPerf_down_yn() {
		return perf_down_yn;
	}

	public void setPerf_down_yn(String perf_down_yn) {
		this.perf_down_yn = perf_down_yn;
	}

	public String getNotPerf_yn() {
		return notPerf_yn;
	}

	public void setNotPerf_yn(String notPerf_yn) {
		this.notPerf_yn = notPerf_yn;
	}

	public String getError_yn() {
		return error_yn;
	}

	public void setError_yn(String error_yn) {
		this.error_yn = error_yn;
	}

	public String getAll_dml_yn() {
		return all_dml_yn;
	}

	public void setAll_dml_yn(String all_dml_yn) {
		this.all_dml_yn = all_dml_yn;
	}

	public String getFullScan_yn() {
		return fullScan_yn;
	}

	public void setFullScan_yn(String fullScan_yn) {
		this.fullScan_yn = fullScan_yn;
	}

	public String getPartition_yn() {
		return partition_yn;
	}

	public void setPartition_yn(String partition_yn) {
		this.partition_yn = partition_yn;
	}

	public String getError_dml_yn() {
		return error_dml_yn;
	}

	public void setError_dml_yn(String error_dml_yn) {
		this.error_dml_yn = error_dml_yn;
	}

	public String getPerf_check_executer_id() {
		return perf_check_executer_id;
	}
	public void setPerf_check_executer_id(String perf_check_executer_id) {
		this.perf_check_executer_id = perf_check_executer_id;
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

	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getChild_project_id() {
		return child_project_id;
	}
	public void setChild_project_id(String child_project_id) {
		this.child_project_id = child_project_id;
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
	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}
	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}
	public String getChild_sql_auto_perf_check_id() {
		return child_sql_auto_perf_check_id;
	}
	public void setChild_sql_auto_perf_check_id(String child_sql_auto_perf_check_id) {
		this.child_sql_auto_perf_check_id = child_sql_auto_perf_check_id;
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
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
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
	public String getSql_text_web() {
		return sql_text_web;
	}
	public void setSql_text_web(String sql_text_web) {
		this.sql_text_web = sql_text_web;
	}
	public CLOB getSql_text_excel() {
		return sql_text_excel;
	}
	public void setSql_text_excel(CLOB sql_text_excel) {
		this.sql_text_excel = sql_text_excel;
	}

	public String getExec_status() {
		return exec_status;
	}

	public void setExec_status(String exec_status) {
		this.exec_status = exec_status;
	}

	public String getTable_count() {
		return table_count;
	}

	public void setTable_count(String table_count) {
		this.table_count = table_count;
	}

	public String getAccess_path_count() {
		return access_path_count;
	}

	public void setAccess_path_count(String access_path_count) {
		this.access_path_count = access_path_count;
	}

	public String getAccess_path_column_list() {
		return access_path_column_list;
	}

	public void setAccess_path_column_list(String access_path_column_list) {
		this.access_path_column_list = access_path_column_list;
	}

	public String getTable_cnt() {
		return table_cnt;
	}

	public void setTable_cnt(String table_cnt) {
		this.table_cnt = table_cnt;
	}

	public String getAccess_path_exec_dt() {
		return access_path_exec_dt;
	}

	public void setAccess_path_exec_dt(String access_path_exec_dt) {
		this.access_path_exec_dt = access_path_exec_dt;
	}

	public String getIndex_exec_end_dt() {
		return index_exec_end_dt;
	}

	public void setIndex_exec_end_dt(String index_exec_end_dt) {
		this.index_exec_end_dt = index_exec_end_dt;
	}

	public String getExec_seq() {
		return exec_seq;
	}

	public void setExec_seq(String exec_seq) {
		this.exec_seq = exec_seq;
	}

	public String getIdx_selectvity_calc_meth_cd() {
		return idx_selectvity_calc_meth_cd;
	}

	public void setIdx_selectvity_calc_meth_cd(String idx_selectvity_calc_meth_cd) {
		this.idx_selectvity_calc_meth_cd = idx_selectvity_calc_meth_cd;
	}

	public String getAcces_path_exec_yn() {
		return acces_path_exec_yn;
	}

	public void setAcces_path_exec_yn(String acces_path_exec_yn) {
		this.acces_path_exec_yn = acces_path_exec_yn;
	}

	public String getAcces_path_end_yn() {
		return acces_path_end_yn;
	}

	public void setAcces_path_end_yn(String acces_path_end_yn) {
		this.acces_path_end_yn = acces_path_end_yn;
	}

	public String getIndex_db_create_exec_yn() {
		return index_db_create_exec_yn;
	}

	public void setIndex_db_create_exec_yn(String index_db_create_exec_yn) {
		this.index_db_create_exec_yn = index_db_create_exec_yn;
	}

	public String getIndex_recommend_exec_yn() {
		return index_recommend_exec_yn;
	}

	public void setIndex_recommend_exec_yn(String index_recommend_exec_yn) {
		this.index_recommend_exec_yn = index_recommend_exec_yn;
	}

	public String getIndex_recommend_end_yn() {
		return index_recommend_end_yn;
	}

	public void setIndex_recommend_end_yn(String index_recommend_end_yn) {
		this.index_recommend_end_yn = index_recommend_end_yn;
	}

	public String getRunning_table_cnt() {
		return running_table_cnt;
	}

	public void setRunning_table_cnt(String running_table_cnt) {
		this.running_table_cnt = running_table_cnt;
	}

	public String getRecommend_index_cnt() {
		return recommend_index_cnt;
	}

	public void setRecommend_index_cnt(String recommend_index_cnt) {
		this.recommend_index_cnt = recommend_index_cnt;
	}

	public String getRecommend_index_add_cnt() {
		return recommend_index_add_cnt;
	}

	public void setRecommend_index_add_cnt(String recommend_index_add_cnt) {
		this.recommend_index_add_cnt = recommend_index_add_cnt;
	}

	public String getRecommend_index_modify_cnt() {
		return recommend_index_modify_cnt;
	}

	public void setRecommend_index_modify_cnt(String recommend_index_modify_cnt) {
		this.recommend_index_modify_cnt = recommend_index_modify_cnt;
	}

	public String getRecommend_index_unused_cnt() {
		return recommend_index_unused_cnt;
	}

	public void setRecommend_index_unused_cnt(String recommend_index_unused_cnt) {
		this.recommend_index_unused_cnt = recommend_index_unused_cnt;
	}

	public String getIdx_ad_no() {
		return idx_ad_no;
	}

	public void setIdx_ad_no(String idx_ad_no) {
		this.idx_ad_no = idx_ad_no;
	}

	public String getIdx_work_div_cd() {
		return idx_work_div_cd;
	}

	public void setIdx_work_div_cd(String idx_work_div_cd) {
		this.idx_work_div_cd = idx_work_div_cd;
	}

	public String getAnalysis_sql_cnt() {
		return analysis_sql_cnt;
	}

	public void setAnalysis_sql_cnt(String analysis_sql_cnt) {
		this.analysis_sql_cnt = analysis_sql_cnt;
	}

	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	public String getAll_idx_cnt() {
		return all_idx_cnt;
	}

	public void setAll_idx_cnt(String all_idx_cnt) {
		this.all_idx_cnt = all_idx_cnt;
	}

	@Override
	public String getDatabase_kinds_cd() {
		return database_kinds_cd;
	}

	@Override
	public void setDatabase_kinds_cd(String database_kinds_cd) {
		this.database_kinds_cd = database_kinds_cd;
	}

	public String getTot_cnt() {
		return tot_cnt;
	}

	public void setTot_cnt(String tot_cnt) {
		this.tot_cnt = tot_cnt;
	}

	public void setAsis_dbid(String asis_dbid) {
		this.asis_dbid = asis_dbid;
	}

	public String getTotal_cnt_1st() {
		return total_cnt_1st;
	}

	public void setTotal_cnt_1st(String total_cnt_1st) {
		this.total_cnt_1st = total_cnt_1st;
	}

	public String getCompleted_cnt_1st() {
		return completed_cnt_1st;
	}

	public void setCompleted_cnt_1st(String completed_cnt_1st) {
		this.completed_cnt_1st = completed_cnt_1st;
	}

	public String getErr_cnt_1st() {
		return err_cnt_1st;
	}

	public void setErr_cnt_1st(String err_cnt_1st) {
		this.err_cnt_1st = err_cnt_1st;
	}

	public String getTotal_cnt_2nd() {
		return total_cnt_2nd;
	}

	public void setTotal_cnt_2nd(String total_cnt_2nd) {
		this.total_cnt_2nd = total_cnt_2nd;
	}

	public String getCompleted_cnt_2nd() {
		return completed_cnt_2nd;
	}

	public void setCompleted_cnt_2nd(String completed_cnt_2nd) {
		this.completed_cnt_2nd = completed_cnt_2nd;
	}

	public String getErr_cnt_2nd() {
		return err_cnt_2nd;
	}

	public void setErr_cnt_2nd(String err_cnt_2nd) {
		this.err_cnt_2nd = err_cnt_2nd;
	}

	public String getPerf_check_exec_yn() {
		return perf_check_exec_yn;
	}

	public void setPerf_check_exec_yn(String perf_check_exec_yn) {
		this.perf_check_exec_yn = perf_check_exec_yn;
	}

	public String getPerf_check_end_yn() {
		return perf_check_end_yn;
	}

	public void setPerf_check_end_yn(String perf_check_end_yn) {
		this.perf_check_end_yn = perf_check_end_yn;
	}

	public String getIndex_db_drop_end_yn() {
		return index_db_drop_end_yn;
	}

	public void setIndex_db_drop_end_yn(String index_db_drop_end_yn) {
		this.index_db_drop_end_yn = index_db_drop_end_yn;
	}

	public String getIndex_db_create_end_yn() {
		return index_db_create_end_yn;
	}

	public String getOneRow() {
		return oneRow;
	}

	public void setOneRow(String oneRow) {
		this.oneRow = oneRow;
	}

	public void setIndex_db_create_end_yn(String index_db_create_end_yn) {
		this.index_db_create_end_yn = index_db_create_end_yn;
	}

	public String getIndex_db_drop_exec_yn() {
		return index_db_drop_exec_yn;
	}

	public void setIndex_db_drop_exec_yn(String index_db_drop_exec_yn) {
		this.index_db_drop_exec_yn = index_db_drop_exec_yn;
	}

	public String getExec_end_dt() {
		return exec_end_dt;
	}

	public void setExec_end_dt(String exec_end_dt) {
		this.exec_end_dt = exec_end_dt;
	}

	public String getExec_start_dt() {
		return exec_start_dt;
	}

	public void setExec_start_dt(String exec_start_dt) {
		this.exec_start_dt = exec_start_dt;
	}

	public String getIdx_db_work_cnt() {
		return idx_db_work_cnt;
	}

	public void setIdx_db_work_cnt(String idx_db_work_cnt) {
		this.idx_db_work_cnt = idx_db_work_cnt;
	}

	public String getErr_sbst() {
		return err_sbst;
	}

	public void setErr_sbst(String err_sbst) {
		this.err_sbst = err_sbst;
	}

	public String getIdx_db_work_id() {
		return idx_db_work_id;
	}

	public void setIdx_db_work_id(String idx_db_work_id) {
		this.idx_db_work_id = idx_db_work_id;
	}
}
