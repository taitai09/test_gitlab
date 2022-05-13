package omc.spop.model;

import java.math.BigDecimal;
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
 * 2017.10.18 이원식 최초작성 2018.05.29 이원식 접수취소 관련 필드 추가
 **********************************************************/

@Alias("tuningTargetSql")
public class TuningTargetSql extends Base implements Jsonable {

	private String choice_tms;

	private String sql_id;
	private String choice_div_cd;
	private String choice_div_cd_nm;
	private String tuning_status;
	private String tuning_status_cd;
	private String tuning_status_nm;
	private String first_tuning_status_cd;
	private String before_tuning_status_cd;
	private String perfr_id;
	private String tuning_requester_id;
	private String tuning_requester_nm;
	private String tuning_requester_wrkjob_cd;
	private String tuning_requester_wrkjob_nm;
	private String tuning_requester_tel_num;
	private String tuning_requester_dt;
	private String tuning_rcess_dt;
	private String tuning_rcess_why;
	private String tuning_apply_rcess_why;
	private String wrkjob_mgr_id;
	private String program_type_cd;
	private String program_type_nm;
	private String program_type_cd_nm;
	private String batch_work_div_cd;
	private String tuning_complete_due_dt;
	private BigDecimal current_elap_time;
	private String forecast_result_cnt;
	private String goal_elap_time;
	private String wrkjob_peculiar_point;
	private String request_why;
	private String sql_desc;
	private String exec_cnt;
	private String dbio;
	private String temp_dbio;
	private String tr_cd;
	private String plan_hash_value;
	private String module;
	private String parsing_schema_name;
	private String executions;
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
	private String sql_text;
	private String flag_sql_text;
	private String sql_bind;
	private String bindValue_name;
	private String bindValue_value;

	private String auto_share;
	private String sqlIdArry;
	private String planHashValueArry;
	private String asisPlanHashValueArry;
	private String gatherDayArry;
	private String tuningNoArry;
	private String ChoiceDivArry;
	private String tuningStatusArry;
	private String table_name;
	private String field_name;
	private String tuning_status_change_why;
	private String tuning_status_changer_id;

	private String tuning_request_dt;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String wrkjob_mgr_nm;
	private String wrkjob_mgr_wrkjob_cd;
	private String wrkjob_mgr_wrkjob_nm;
	private String wrkjob_mgr_tel_num;
	private String batch_work_div_cd_nm;
	private String perfr_nm;
	private String tuning_complete_dt;
	private String tuning_completer_id;
	private String start_tuning_complete_dt;
	private String end_tuning_complete_dt;
	private String tuning_complete_why_nm;
	private String tuning_end_why_nm;
	private String imprb_elap_time;
	private String imprb_buffer_cnt;
	private String imprb_pga_usage;
	private String impra_elap_time;
	private String impra_buffer_cnt;
	private String impra_pga_usage;
	private String elap_time_impr_ratio;
	private String buffer_impr_ratio;
	private String tuning_apply_dt;
	private String pga_impr_ratio;
	private String controversialist;
	private String impr_sbst;
	private String impr_sql_text;
	private String imprb_exec_plan;
	private String impra_exec_plan;

	private String gubun;
	private String bfac_chk_no;

	private String start_snap_id;
	private String end_snap_id;

	private String process_all; /* 전체 */
	private String process_1; /* 선정/요청 */
	private String process_2; /* 접수 */
	private String process_3; /* 튜닝중 */
	private String process_4; /* 튜닝적용 */
	private String process_5; /* 튜닝완료 */
	private String process_6; /* 튜닝반려 */
	private String process_7; /* 적용반려 */
	private String process_8; /* 튜닝종료 */
	private String is_completed;/* 완료여부 2018-06-21 */
	private String tuning_end_why_cd;
	private String tuning_end_why;
	private String except_target_yn;
	private String tuning_case_posting_yn;
	private String tuning_case_posting_title;
	private String tuning_complete_why_cd;
	private String tuning_complete_why;
	private String bfac_chk_source;
	private String all_tuning_end_yn;
	

	private String tuning_end_dt;
	private String bfac_chk_request_dt;
	private String bfac_chk_dt;

	private String chart_title;
	private String all_cnt;
	private String tuning_cnt;
	private String knowledge_cnt;

	private String day_gubun;

	private String tuning_no_array;
	private String perfr_id_cnt;

	private String tuning_why_nm;
	private String leader_yn;

	private String completeArry;
	private String dba_name;
	private String tuning_status_change_dt;
	private String tuning_elapsed_time;
	private String tuning_delay_time;
	private String request_elapsed_time;
	private String complete_over_time;

	private String tuning_receipt_cancel_why;
	private String tuning_receipt_canceler_id;
	private String tuning_receipt_cancel_dt;
	private String sql_hash;
	private String auto_choice_cond_no;
	private String temporary_save_dt;
	private String temporary_save_yn;

	private String update_dt;
	private String index_tuning_seq;
	private String index_impr_type_cd;
	private String index_name;
	private String index_column_name;
	private String before_index_column_name;

	private String call_from_sqlPerformance;
	private String bindValueIsNull;

	private String sqlStats_sql_id;
	private String sqlStats_plan_hash_value;
	private String sqlStats_elapsed_time;
	private String sqlStats_buffer_gets;
	private String rerequest;
	private String search_tuning_no;
	private String tuning_case_type_cd;
	
	private String chkExcept;
	
	private String headerBtnIsClicked;  //상단 헤더 튜닝요청, 튜닝대기, 튜닝중, 적용대기 클릭시 
	
	/**
	 * 성능점검결과에서 튜닝요청할때 넘어가는 파라미터들...
	 */
	private String perf_check_id;
	private String perf_check_step_id;
	private String program_id;
	private String bind_set_seq;
	private String program_execute_tms;
	
	private String tuning_prgrs_step_nm;
	private String tuning_prgrs_step_seq;
	private String project_id;
	private String project_nm;
	
	private String strStartDt;
	private String strEndDt;
	
	private String deploy_id;
	private String deploy_requester_id;
	private String deploy_request_dt;
	private String sql_auto_perf_check_id;
	private String perf_check_name;
	private String sqlExclude;
	private String asis_elapsed_time;
	private String asis_buffer_gets;
	private String asis_plan_hash_value;
	
	private String bind_seq;
	private String bind_var_nm;
	private String bind_var_value;
	private String bind_var_type;
	private String mandatory_yn;
	
	private String verify_project_id;
	private String verify_sql_auto_perf_check_id;
	private String asis_sql_id;
	private String perf_check_sql_source_type_cd;	//AWR or VSQL
	
	private String operation_plan_hash_value;
	private String operation_executions;
	private String operation_buffer_gets;
	private String operation_elapsed_time;
	private String operation_rows_processed;
	private String operation_tuning_no;
	private String operation_dbid;
	private String operation_parsing_schema_name;
	
	private String error_msg;
	private String deleteFiles;
	
	public String getDeleteFiles() {
		return deleteFiles;
	}

	public void setDeleteFiles(String deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public String getTuning_end_why_nm() {
		return tuning_end_why_nm;
	}

	public void setTuning_end_why_nm(String tuning_end_why_nm) {
		this.tuning_end_why_nm = tuning_end_why_nm;
	}

	public String getAsisPlanHashValueArry() {
		return asisPlanHashValueArry;
	}

	public void setAsisPlanHashValueArry(String asisPlanHashValueArry) {
		this.asisPlanHashValueArry = asisPlanHashValueArry;
	}

	public String getPerf_check_sql_source_type_cd() {
		return perf_check_sql_source_type_cd;
	}

	public void setPerf_check_sql_source_type_cd(String perf_check_sql_source_type_cd) {
		this.perf_check_sql_source_type_cd = perf_check_sql_source_type_cd;
	}

	public String getOperation_parsing_schema_name() {
		return operation_parsing_schema_name;
	}

	public void setOperation_parsing_schema_name(String operation_parsing_schema_name) {
		this.operation_parsing_schema_name = operation_parsing_schema_name;
	}

	public String getOperation_plan_hash_value() {
		return operation_plan_hash_value;
	}

	public void setOperation_plan_hash_value(String operation_plan_hash_value) {
		this.operation_plan_hash_value = operation_plan_hash_value;
	}

	public String getOperation_buffer_gets() {
		return operation_buffer_gets;
	}

	public void setOperation_buffer_gets(String operation_buffer_gets) {
		this.operation_buffer_gets = operation_buffer_gets;
	}

	public String getOperation_elapsed_time() {
		return operation_elapsed_time;
	}

	public void setOperation_elapsed_time(String operation_elapsed_time) {
		this.operation_elapsed_time = operation_elapsed_time;
	}

	public String getOperation_dbid() {
		return operation_dbid;
	}

	public void setOperation_dbid(String operation_dbid) {
		this.operation_dbid = operation_dbid;
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

	public String getOperation_tuning_no() {
		return operation_tuning_no;
	}

	public void setOperation_tuning_no(String operation_tuning_no) {
		this.operation_tuning_no = operation_tuning_no;
	}

	public String getAsis_sql_id() {
		return asis_sql_id;
	}

	public void setAsis_sql_id(String asis_sql_id) {
		this.asis_sql_id = asis_sql_id;
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

	public String getBind_seq() {
		return bind_seq;
	}

	public void setBind_seq(String bind_seq) {
		this.bind_seq = bind_seq;
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

	public String getMandatory_yn() {
		return mandatory_yn;
	}

	public void setMandatory_yn(String mandatory_yn) {
		this.mandatory_yn = mandatory_yn;
	}

	public String getAsis_plan_hash_value() {
		return asis_plan_hash_value;
	}

	public void setAsis_plan_hash_value(String asis_plan_hash_value) {
		this.asis_plan_hash_value = asis_plan_hash_value;
	}

	public String getAsis_elapsed_time() {
		return asis_elapsed_time;
	}

	public void setAsis_elapsed_time(String asis_elapsed_time) {
		this.asis_elapsed_time = asis_elapsed_time;
	}

	public String getAsis_buffer_gets() {
		return asis_buffer_gets;
	}

	public void setAsis_buffer_gets(String asis_buffer_gets) {
		this.asis_buffer_gets = asis_buffer_gets;
	}

	public String getPerf_check_name() {
		return perf_check_name;
	}

	public void setPerf_check_name(String perf_check_name) {
		this.perf_check_name = perf_check_name;
	}

	public String getSqlExclude() {
		return sqlExclude;
	}

	public void setSqlExclude(String sqlExclude) {
		this.sqlExclude = sqlExclude;
	}

	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}

	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}

	public String getChkExcept() {
		return chkExcept;
	}

	public void setChkExcept(String chkExcept) {
		this.chkExcept = chkExcept;
	}

	public String getDeploy_id() {
		return deploy_id;
	}

	public void setDeploy_id(String deploy_id) {
		this.deploy_id = deploy_id;
	}

	public String getDeploy_requester_id() {
		return deploy_requester_id;
	}

	public void setDeploy_requester_id(String deploy_requester_id) {
		this.deploy_requester_id = deploy_requester_id;
	}

	public String getDeploy_request_dt() {
		return deploy_request_dt;
	}

	public void setDeploy_request_dt(String deploy_request_dt) {
		this.deploy_request_dt = deploy_request_dt;
	}

	public String getTuning_case_type_cd() {
		return tuning_case_type_cd;
	}

	public void setTuning_case_type_cd(String tuning_case_type_cd) {
		this.tuning_case_type_cd = tuning_case_type_cd;
	}

	public String getAll_tuning_end_yn() {
		return all_tuning_end_yn;
	}

	public void setAll_tuning_end_yn(String all_tuning_end_yn) {
		this.all_tuning_end_yn = all_tuning_end_yn;
	}

	public String getStrStartDt() {
		return strStartDt;
	}

	public void setStrStartDt(String strStartDt) {
		this.strStartDt = strStartDt;
	}

	public String getStrEndDt() {
		return strEndDt;
	}

	public void setStrEndDt(String strEndDt) {
		this.strEndDt = strEndDt;
	}

	public String getTuning_prgrs_step_nm() {
		return tuning_prgrs_step_nm;
	}

	public void setTuning_prgrs_step_nm(String tuning_prgrs_step_nm) {
		this.tuning_prgrs_step_nm = tuning_prgrs_step_nm;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}

	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}

	public String getHeaderBtnIsClicked() {
		return headerBtnIsClicked;
	}

	public void setHeaderBtnIsClicked(String headerBtnIsClicked) {
		this.headerBtnIsClicked = headerBtnIsClicked;
	}

	public String getBindValueIsNull() {
		return bindValueIsNull;
	}

	public void setBindValueIsNull(String bindValueIsNull) {
		this.bindValueIsNull = bindValueIsNull;
	}

	public String getCall_from_sqlPerformance() {
		return call_from_sqlPerformance;
	}

	public void setCall_from_sqlPerformance(String call_from_sqlPerformance) {
		this.call_from_sqlPerformance = call_from_sqlPerformance;
	}

	public String getChoice_tms() {
		return choice_tms;
	}

	public void setChoice_tms(String choice_tms) {
		this.choice_tms = choice_tms;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getChoice_div_cd() {
		return choice_div_cd;
	}

	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}

	public String getTuning_status_cd() {
		return tuning_status_cd;
	}

	public String getChoice_div_cd_nm() {
		return choice_div_cd_nm;
	}

	public void setChoice_div_cd_nm(String choice_div_cd_nm) {
		this.choice_div_cd_nm = choice_div_cd_nm;
	}

	public String getTuning_status() {
		return tuning_status;
	}

	public void setTuning_status(String tuning_status) {
		this.tuning_status = tuning_status;
	}

	public void setTuning_status_cd(String tuning_status_cd) {
		this.tuning_status_cd = tuning_status_cd;
	}

	public String getTuning_status_nm() {
		return tuning_status_nm;
	}

	public void setTuning_status_nm(String tuning_status_nm) {
		this.tuning_status_nm = tuning_status_nm;
	}

	public String getFirst_tuning_status_cd() {
		return first_tuning_status_cd;
	}

	public void setFirst_tuning_status_cd(String first_tuning_status_cd) {
		this.first_tuning_status_cd = first_tuning_status_cd;
	}

	public String getPerfr_id() {
		return perfr_id;
	}

	public void setPerfr_id(String perfr_id) {
		this.perfr_id = perfr_id;
	}

	public String getTuning_requester_id() {
		return tuning_requester_id;
	}

	public void setTuning_requester_id(String tuning_requester_id) {
		this.tuning_requester_id = tuning_requester_id;
	}

	public String getTuning_requester_nm() {
		return tuning_requester_nm;
	}

	public void setTuning_requester_nm(String tuning_requester_nm) {
		this.tuning_requester_nm = tuning_requester_nm;
	}

	public String getTuning_requester_wrkjob_cd() {
		return tuning_requester_wrkjob_cd;
	}

	public void setTuning_requester_wrkjob_cd(String tuning_requester_wrkjob_cd) {
		this.tuning_requester_wrkjob_cd = tuning_requester_wrkjob_cd;
	}

	public String getTuning_requester_wrkjob_nm() {
		return tuning_requester_wrkjob_nm;
	}

	public String getSearch_tuning_no() {
		return search_tuning_no;
	}

	public void setSearch_tuning_no(String search_tuning_no) {
		this.search_tuning_no = search_tuning_no;
	}

	public void setTuning_requester_wrkjob_nm(String tuning_requester_wrkjob_nm) {
		this.tuning_requester_wrkjob_nm = tuning_requester_wrkjob_nm;
	}

	public String getTuning_requester_tel_num() {
		return tuning_requester_tel_num;
	}

	public void setTuning_requester_tel_num(String tuning_requester_tel_num) {
		this.tuning_requester_tel_num = tuning_requester_tel_num;
	}

	public String getTuning_requester_dt() {
		return tuning_requester_dt;
	}

	public void setTuning_requester_dt(String tuning_requester_dt) {
		this.tuning_requester_dt = tuning_requester_dt;
	}

	public String getTuning_rcess_dt() {
		return tuning_rcess_dt;
	}

	public void setTuning_rcess_dt(String tuning_rcess_dt) {
		this.tuning_rcess_dt = tuning_rcess_dt;
	}

	public String getTuning_rcess_why() {
		return tuning_rcess_why;
	}

	public void setTuning_rcess_why(String tuning_rcess_why) {
		this.tuning_rcess_why = tuning_rcess_why;
	}

	public String getWrkjob_mgr_id() {
		return wrkjob_mgr_id;
	}

	public void setWrkjob_mgr_id(String wrkjob_mgr_id) {
		this.wrkjob_mgr_id = wrkjob_mgr_id;
	}

	public String getTuning_apply_rcess_why() {
		return tuning_apply_rcess_why;
	}

	public void setTuning_apply_rcess_why(String tuning_apply_rcess_why) {
		this.tuning_apply_rcess_why = tuning_apply_rcess_why;
	}

	public String getProgram_type_cd() {
		return program_type_cd;
	}

	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
	}

	public String getBatch_work_div_cd() {
		return batch_work_div_cd;
	}

	public void setBatch_work_div_cd(String batch_work_div_cd) {
		this.batch_work_div_cd = batch_work_div_cd;
	}

	public String getTuning_complete_due_dt() {
		return tuning_complete_due_dt;
	}

	public void setTuning_complete_due_dt(String tuning_complete_due_dt) {
		this.tuning_complete_due_dt = tuning_complete_due_dt;
	}

	public BigDecimal getCurrent_elap_time() {
		return current_elap_time;
	}

	public void setCurrent_elap_time(BigDecimal current_elap_time) {
		this.current_elap_time = current_elap_time;
	}

	public String getForecast_result_cnt() {
		return forecast_result_cnt;
	}

	public void setForecast_result_cnt(String forecast_result_cnt) {
		this.forecast_result_cnt = forecast_result_cnt;
	}

	public String getGoal_elap_time() {
		return goal_elap_time;
	}

	public void setGoal_elap_time(String goal_elap_time) {
		this.goal_elap_time = goal_elap_time;
	}

	public String getWrkjob_peculiar_point() {
		return wrkjob_peculiar_point;
	}

	public void setWrkjob_peculiar_point(String wrkjob_peculiar_point) {
		this.wrkjob_peculiar_point = wrkjob_peculiar_point;
	}

	public String getRequest_why() {
		return request_why;
	}

	public void setRequest_why(String request_why) {
		this.request_why = request_why;
	}

	public String getSql_desc() {
		return sql_desc;
	}

	public void setSql_desc(String sql_desc) {
		this.sql_desc = sql_desc;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
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

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
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

	public String getSql_bind() {
		return sql_bind;
	}

	public void setSql_bind(String sql_bind) {
		this.sql_bind = sql_bind;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getFlag_sql_text() {
		return flag_sql_text;
	}

	public void setFlag_sql_text(String flag_sql_text) {
		this.flag_sql_text = flag_sql_text;
	}

	public String getAuto_share() {
		return auto_share;
	}

	public void setAuto_share(String auto_share) {
		this.auto_share = auto_share;
	}

	public String getSqlIdArry() {
		return sqlIdArry;
	}

	public void setSqlIdArry(String sqlIdArry) {
		this.sqlIdArry = sqlIdArry;
	}

	public String getPlanHashValueArry() {
		return planHashValueArry;
	}

	public void setPlanHashValueArry(String planHashValueArry) {
		this.planHashValueArry = planHashValueArry;
	}

	public String getGatherDayArry() {
		return gatherDayArry;
	}

	public void setGatherDayArry(String gatherDayArry) {
		this.gatherDayArry = gatherDayArry;
	}

	public String getTuningNoArry() {
		return tuningNoArry;
	}

	public void setTuningNoArry(String tuningNoArry) {
		this.tuningNoArry = tuningNoArry;
	}

	public String getChoiceDivArry() {
		return ChoiceDivArry;
	}

	public void setChoiceDivArry(String choiceDivArry) {
		ChoiceDivArry = choiceDivArry;
	}

	public String getTuningStatusArry() {
		return tuningStatusArry;
	}

	public void setTuningStatusArry(String tuningStatusArry) {
		this.tuningStatusArry = tuningStatusArry;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getField_name() {
		return field_name;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getTuning_status_change_why() {
		return tuning_status_change_why;
	}

	public void setTuning_status_change_why(String tuning_status_change_why) {
		this.tuning_status_change_why = tuning_status_change_why;
	}

	public String getTuning_status_changer_id() {
		return tuning_status_changer_id;
	}

	public void setTuning_status_changer_id(String tuning_status_changer_id) {
		this.tuning_status_changer_id = tuning_status_changer_id;
	}

	public String getTuning_request_dt() {
		return tuning_request_dt;
	}

	public void setTuning_request_dt(String tuning_request_dt) {
		this.tuning_request_dt = tuning_request_dt;
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

	public String getWrkjob_mgr_nm() {
		return wrkjob_mgr_nm;
	}

	public void setWrkjob_mgr_nm(String wrkjob_mgr_nm) {
		this.wrkjob_mgr_nm = wrkjob_mgr_nm;
	}

	public String getWrkjob_mgr_wrkjob_cd() {
		return wrkjob_mgr_wrkjob_cd;
	}

	public void setWrkjob_mgr_wrkjob_cd(String wrkjob_mgr_wrkjob_cd) {
		this.wrkjob_mgr_wrkjob_cd = wrkjob_mgr_wrkjob_cd;
	}

	public String getWrkjob_mgr_wrkjob_nm() {
		return wrkjob_mgr_wrkjob_nm;
	}

	public void setWrkjob_mgr_wrkjob_nm(String wrkjob_mgr_wrkjob_nm) {
		this.wrkjob_mgr_wrkjob_nm = wrkjob_mgr_wrkjob_nm;
	}

	public String getWrkjob_mgr_tel_num() {
		return wrkjob_mgr_tel_num;
	}

	public void setWrkjob_mgr_tel_num(String wrkjob_mgr_tel_num) {
		this.wrkjob_mgr_tel_num = wrkjob_mgr_tel_num;
	}

	public String getProgram_type_cd_nm() {
		return program_type_cd_nm;
	}

	public void setProgram_type_cd_nm(String program_type_cd_nm) {
		this.program_type_cd_nm = program_type_cd_nm;
	}

	public String getBatch_work_div_cd_nm() {
		return batch_work_div_cd_nm;
	}

	public void setBatch_work_div_cd_nm(String batch_work_div_cd_nm) {
		this.batch_work_div_cd_nm = batch_work_div_cd_nm;
	}

	public String getPerfr_nm() {
		return perfr_nm;
	}

	public void setPerfr_nm(String perfr_nm) {
		this.perfr_nm = perfr_nm;
	}

	public String getTuning_complete_dt() {
		return tuning_complete_dt;
	}

	public void setTuning_complete_dt(String tuning_complete_dt) {
		this.tuning_complete_dt = tuning_complete_dt;
	}

	public String getTuning_complete_why_nm() {
		return tuning_complete_why_nm;
	}

	public void setTuning_complete_why_nm(String tuning_complete_why_nm) {
		this.tuning_complete_why_nm = tuning_complete_why_nm;
	}

	public String getStart_tuning_complete_dt() {
		return start_tuning_complete_dt;
	}

	public void setStart_tuning_complete_dt(String start_tuning_complete_dt) {
		this.start_tuning_complete_dt = start_tuning_complete_dt;
	}

	public String getEnd_tuning_complete_dt() {
		return end_tuning_complete_dt;
	}

	public void setEnd_tuning_complete_dt(String end_tuning_complete_dt) {
		this.end_tuning_complete_dt = end_tuning_complete_dt;
	}

	public String getImprb_elap_time() {
		return imprb_elap_time;
	}

	public void setImprb_elap_time(String imprb_elap_time) {
		this.imprb_elap_time = imprb_elap_time;
	}

	public String getImprb_buffer_cnt() {
		return imprb_buffer_cnt;
	}

	public void setImprb_buffer_cnt(String imprb_buffer_cnt) {
		this.imprb_buffer_cnt = imprb_buffer_cnt;
	}

	public String getImprb_pga_usage() {
		return imprb_pga_usage;
	}

	public void setImprb_pga_usage(String imprb_pga_usage) {
		this.imprb_pga_usage = imprb_pga_usage;
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

	public String getImpra_pga_usage() {
		return impra_pga_usage;
	}

	public void setImpra_pga_usage(String impra_pga_usage) {
		this.impra_pga_usage = impra_pga_usage;
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

	public String getTuning_apply_dt() {
		return tuning_apply_dt;
	}

	public void setTuning_apply_dt(String tuning_apply_dt) {
		this.tuning_apply_dt = tuning_apply_dt;
	}

	public String getPga_impr_ratio() {
		return pga_impr_ratio;
	}

	public void setPga_impr_ratio(String pga_impr_ratio) {
		this.pga_impr_ratio = pga_impr_ratio;
	}

	public String getControversialist() {
		return controversialist;
	}

	public void setControversialist(String controversialist) {
		this.controversialist = controversialist;
	}

	public String getImpr_sbst() {
		return impr_sbst;
	}

	public void setImpr_sbst(String impr_sbst) {
		this.impr_sbst = impr_sbst;
	}

	public String getImpr_sql_text() {
		return impr_sql_text;
	}

	public void setImpr_sql_text(String impr_sql_text) {
		this.impr_sql_text = impr_sql_text;
	}

	public String getImprb_exec_plan() {
		return imprb_exec_plan;
	}

	public void setImprb_exec_plan(String imprb_exec_plan) {
		this.imprb_exec_plan = imprb_exec_plan;
	}

	public String getImpra_exec_plan() {
		return impra_exec_plan;
	}

	public void setImpra_exec_plan(String impra_exec_plan) {
		this.impra_exec_plan = impra_exec_plan;
	}

	public String getGubun() {
		return gubun;
	}

	public void setGubun(String gubun) {
		this.gubun = gubun;
	}

	public String getBfac_chk_no() {
		return bfac_chk_no;
	}

	public void setBfac_chk_no(String bfac_chk_no) {
		this.bfac_chk_no = bfac_chk_no;
	}

	public String getStart_snap_id() {
		return start_snap_id;
	}

	public void setStart_snap_id(String start_snap_id) {
		this.start_snap_id = start_snap_id;
	}

	public String getEnd_snap_id() {
		return end_snap_id;
	}

	public void setEnd_snap_id(String end_snap_id) {
		this.end_snap_id = end_snap_id;
	}

	public String getProcess_all() {
		return process_all;
	}

	public void setProcess_all(String process_all) {
		this.process_all = process_all;
	}

	public String getProcess_1() {
		return process_1;
	}

	public void setProcess_1(String process_1) {
		this.process_1 = process_1;
	}

	public String getProcess_2() {
		return process_2;
	}

	public void setProcess_2(String process_2) {
		this.process_2 = process_2;
	}

	public String getProcess_3() {
		return process_3;
	}

	public void setProcess_3(String process_3) {
		this.process_3 = process_3;
	}

	public String getProcess_4() {
		return process_4;
	}

	public void setProcess_4(String process_4) {
		this.process_4 = process_4;
	}

	public String getProcess_5() {
		return process_5;
	}

	public void setProcess_5(String process_5) {
		this.process_5 = process_5;
	}

	public String getProcess_6() {
		return process_6;
	}

	public void setProcess_6(String process_6) {
		this.process_6 = process_6;
	}

	public String getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(String is_completed) {
		this.is_completed = is_completed;
	}

	public String getTuning_end_why_cd() {
		return tuning_end_why_cd;
	}

	public void setTuning_end_why_cd(String tuning_end_why_cd) {
		this.tuning_end_why_cd = tuning_end_why_cd;
	}

	public String getTuning_end_why() {
		return tuning_end_why;
	}

	public void setTuning_end_why(String tuning_end_why) {
		this.tuning_end_why = tuning_end_why;
	}

	public String getExcept_target_yn() {
		return except_target_yn;
	}

	public void setExcept_target_yn(String except_target_yn) {
		this.except_target_yn = except_target_yn;
	}

	public String getTuning_case_posting_yn() {
		return tuning_case_posting_yn;
	}

	public void setTuning_case_posting_yn(String tuning_case_posting_yn) {
		this.tuning_case_posting_yn = tuning_case_posting_yn;
	}

	public String getTuning_case_posting_title() {
		return tuning_case_posting_title;
	}

	public void setTuning_case_posting_title(String tuning_case_posting_title) {
		this.tuning_case_posting_title = tuning_case_posting_title;
	}

	public String getTuning_complete_why_cd() {
		return tuning_complete_why_cd;
	}

	public void setTuning_complete_why_cd(String tuning_complete_why_cd) {
		this.tuning_complete_why_cd = tuning_complete_why_cd;
	}

	public String getTuning_complete_why() {
		return tuning_complete_why;
	}

	public void setTuning_complete_why(String tuning_complete_why) {
		this.tuning_complete_why = tuning_complete_why;
	}

	public String getBfac_chk_source() {
		return bfac_chk_source;
	}

	public void setBfac_chk_source(String bfac_chk_source) {
		this.bfac_chk_source = bfac_chk_source;
	}

	public String getTuning_end_dt() {
		return tuning_end_dt;
	}

	public void setTuning_end_dt(String tuning_end_dt) {
		this.tuning_end_dt = tuning_end_dt;
	}

	public String getBfac_chk_request_dt() {
		return bfac_chk_request_dt;
	}

	public void setBfac_chk_request_dt(String bfac_chk_request_dt) {
		this.bfac_chk_request_dt = bfac_chk_request_dt;
	}

	public String getBfac_chk_dt() {
		return bfac_chk_dt;
	}

	public void setBfac_chk_dt(String bfac_chk_dt) {
		this.bfac_chk_dt = bfac_chk_dt;
	}

	public String getChart_title() {
		return chart_title;
	}

	public void setChart_title(String chart_title) {
		this.chart_title = chart_title;
	}

	public String getAll_cnt() {
		return all_cnt;
	}

	public void setAll_cnt(String all_cnt) {
		this.all_cnt = all_cnt;
	}

	public String getTuning_cnt() {
		return tuning_cnt;
	}

	public void setTuning_cnt(String tuning_cnt) {
		this.tuning_cnt = tuning_cnt;
	}

	public String getKnowledge_cnt() {
		return knowledge_cnt;
	}

	public void setKnowledge_cnt(String knowledge_cnt) {
		this.knowledge_cnt = knowledge_cnt;
	}

	public String getDay_gubun() {
		return day_gubun;
	}

	public void setDay_gubun(String day_gubun) {
		this.day_gubun = day_gubun;
	}

	public String getTuning_no_array() {
		return tuning_no_array;
	}

	public void setTuning_no_array(String tuning_no_array) {
		this.tuning_no_array = tuning_no_array;
	}

	public String getPerfr_id_cnt() {
		return perfr_id_cnt;
	}

	public void setPerfr_id_cnt(String perfr_id_cnt) {
		this.perfr_id_cnt = perfr_id_cnt;
	}

	public String getTuning_why_nm() {
		return tuning_why_nm;
	}

	public void setTuning_why_nm(String tuning_why_nm) {
		this.tuning_why_nm = tuning_why_nm;
	}

	public String getLeader_yn() {
		return leader_yn;
	}

	public void setLeader_yn(String leader_yn) {
		this.leader_yn = leader_yn;
	}

	public String getCompleteArry() {
		return completeArry;
	}

	public void setCompleteArry(String completeArry) {
		this.completeArry = completeArry;
	}

	public String getDba_name() {
		return dba_name;
	}

	public void setDba_name(String dba_name) {
		this.dba_name = dba_name;
	}

	public String getTuning_status_change_dt() {
		return tuning_status_change_dt;
	}

	public void setTuning_status_change_dt(String tuning_status_change_dt) {
		this.tuning_status_change_dt = tuning_status_change_dt;
	}

	public String getTuning_elapsed_time() {
		return tuning_elapsed_time;
	}

	public void setTuning_elapsed_time(String tuning_elapsed_time) {
		this.tuning_elapsed_time = tuning_elapsed_time;
	}

	public String getTuning_delay_time() {
		return tuning_delay_time;
	}

	public void setTuning_delay_time(String tuning_delay_time) {
		this.tuning_delay_time = tuning_delay_time;
	}

	public String getRequest_elapsed_time() {
		return request_elapsed_time;
	}

	public void setRequest_elapsed_time(String request_elapsed_time) {
		this.request_elapsed_time = request_elapsed_time;
	}

	public String getComplete_over_time() {
		return complete_over_time;
	}

	public void setComplete_over_time(String complete_over_time) {
		this.complete_over_time = complete_over_time;
	}

	public String getTuning_receipt_cancel_why() {
		return tuning_receipt_cancel_why;
	}

	public void setTuning_receipt_cancel_why(String tuning_receipt_cancel_why) {
		this.tuning_receipt_cancel_why = tuning_receipt_cancel_why;
	}

	public String getTuning_receipt_canceler_id() {
		return tuning_receipt_canceler_id;
	}

	public void setTuning_receipt_canceler_id(String tuning_receipt_canceler_id) {
		this.tuning_receipt_canceler_id = tuning_receipt_canceler_id;
	}

	public String getTuning_receipt_cancel_dt() {
		return tuning_receipt_cancel_dt;
	}

	public void setTuning_receipt_cancel_dt(String tuning_receipt_cancel_dt) {
		this.tuning_receipt_cancel_dt = tuning_receipt_cancel_dt;
	}

	public String getSql_hash() {
		return sql_hash;
	}

	public void setSql_hash(String sql_hash) {
		this.sql_hash = sql_hash;
	}

	public String getAuto_choice_cond_no() {
		return auto_choice_cond_no;
	}

	public void setAuto_choice_cond_no(String auto_choice_cond_no) {
		this.auto_choice_cond_no = auto_choice_cond_no;
	}

	public String getProcess_7() {
		return process_7;
	}

	public void setProcess_7(String process_7) {
		this.process_7 = process_7;
	}

	public String getProcess_8() {
		return process_8;
	}

	public void setProcess_8(String process_8) {
		this.process_8 = process_8;
	}

	public String getProgram_type_nm() {
		return program_type_nm;
	}

	public void setProgram_type_nm(String program_type_nm) {
		this.program_type_nm = program_type_nm;
	}

	public String getTemporary_save_dt() {
		return temporary_save_dt;
	}

	public void setTemporary_save_dt(String temporary_save_dt) {
		this.temporary_save_dt = temporary_save_dt;
	}

	public String getTemporary_save_yn() {
		return temporary_save_yn;
	}

	public void setTemporary_save_yn(String temporary_save_yn) {
		this.temporary_save_yn = temporary_save_yn;
	}

	public String getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}

	public String getIndex_tuning_seq() {
		return index_tuning_seq;
	}

	public void setIndex_tuning_seq(String index_tuning_seq) {
		this.index_tuning_seq = index_tuning_seq;
	}

	public String getIndex_impr_type_cd() {
		return index_impr_type_cd;
	}

	public void setIndex_impr_type_cd(String index_impr_type_cd) {
		this.index_impr_type_cd = index_impr_type_cd;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getIndex_column_name() {
		return index_column_name;
	}

	public void setIndex_column_name(String index_column_name) {
		this.index_column_name = index_column_name;
	}

	public String getBefore_index_column_name() {
		return before_index_column_name;
	}

	public void setBefore_index_column_name(String before_index_column_name) {
		this.before_index_column_name = before_index_column_name;
	}

	public String getTuning_completer_id() {
		return tuning_completer_id;
	}

	public void setTuning_completer_id(String tuning_completer_id) {
		this.tuning_completer_id = tuning_completer_id;
	}

	public String getSqlStats_sql_id() {
		return sqlStats_sql_id;
	}

	public void setSqlStats_sql_id(String sqlStats_sql_id) {
		this.sqlStats_sql_id = sqlStats_sql_id;
	}

	public String getSqlStats_plan_hash_value() {
		return sqlStats_plan_hash_value;
	}

	public void setSqlStats_plan_hash_value(String sqlStats_plan_hash_value) {
		this.sqlStats_plan_hash_value = sqlStats_plan_hash_value;
	}

	public String getSqlStats_elapsed_time() {
		return sqlStats_elapsed_time;
	}

	public void setSqlStats_elapsed_time(String sqlStats_elapsed_time) {
		this.sqlStats_elapsed_time = sqlStats_elapsed_time;
	}

	public String getSqlStats_buffer_gets() {
		return sqlStats_buffer_gets;
	}

	public void setSqlStats_buffer_gets(String sqlStats_buffer_gets) {
		this.sqlStats_buffer_gets = sqlStats_buffer_gets;
	}

	public String getExec_cnt() {
		return exec_cnt;
	}

	public void setExec_cnt(String exec_cnt) {
		this.exec_cnt = exec_cnt;
	}

	public String getBefore_tuning_status_cd() {
		return before_tuning_status_cd;
	}

	public void setBefore_tuning_status_cd(String before_tuning_status_cd) {
		this.before_tuning_status_cd = before_tuning_status_cd;
	}

	public String getRerequest() {
		return rerequest;
	}

	public void setRerequest(String rerequest) {
		this.rerequest = rerequest;
	}

	public String getTemp_dbio() {
		return temp_dbio;
	}

	public void setTemp_dbio(String temp_dbio) {
		this.temp_dbio = temp_dbio;
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

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getBind_set_seq() {
		return bind_set_seq;
	}

	public void setBind_set_seq(String bind_set_seq) {
		this.bind_set_seq = bind_set_seq;
	}

	public String getProgram_execute_tms() {
		return program_execute_tms;
	}

	public void setProgram_execute_tms(String program_execute_tms) {
		this.program_execute_tms = program_execute_tms;
	}

	/*
	 * @SuppressWarnings("unchecked") public JSONObject toJSONObject() {
	 * JSONObject objJson = new JSONObject();
	 * 
	 * objJson.put("tuning_no",StringUtil.parseInt(this.getTuning_no(),0));
	 * objJson.put("dbid",this.getDbid());
	 * objJson.put("choice_tms",this.getChoice_tms());
	 * objJson.put("gather_day",this.getGather_day());
	 * objJson.put("sql_id",this.getSql_id());
	 * objJson.put("choice_div_cd",this.getChoice_div_cd());
	 * objJson.put("choice_div_cd_nm",this.getChoice_div_cd_nm());
	 * objJson.put("tuning_status_cd",this.getTuning_status_cd());
	 * objJson.put("tuning_status_nm",this.getTuning_status_nm());
	 * objJson.put("perfr_id",this.getPerfr_id());
	 * objJson.put("tuning_requester_id",this.getTuning_requester_id());
	 * objJson.put("tuning_requester_nm",this.getTuning_requester_nm());
	 * objJson.put("tuning_requester_wrkjob_cd",this.
	 * getTuning_requester_wrkjob_cd());
	 * objJson.put("tuning_requester_wrkjob_nm",this.
	 * getTuning_requester_wrkjob_nm());
	 * objJson.put("tuning_requester_tel_num",this.getTuning_requester_tel_num()
	 * ); objJson.put("tuning_requester_dt",this.getTuning_requester_dt());
	 * objJson.put("tuning_rcess_dt",this.getTuning_rcess_dt());
	 * objJson.put("tuning_rcess_why",this.getTuning_rcess_why());
	 * objJson.put("tuning_apply_rcess_why",this.getTuning_apply_rcess_why());
	 * objJson.put("program_type_cd",this.getProgram_type_cd());
	 * objJson.put("batch_work_div_cd",this.getBatch_work_div_cd());
	 * objJson.put("tuning_complete_due_dt",this.getTuning_complete_due_dt());
	 * objJson.put("current_elap_time",this.getCurrent_elap_time());
	 * objJson.put("forecast_result_cnt",this.getForecast_result_cnt());
	 * objJson.put("goal_elap_time",this.getGoal_elap_time());
	 * objJson.put("wrkjob_peculiar_point",this.getWrkjob_peculiar_point());
	 * objJson.put("request_why",this.getRequest_why());
	 * objJson.put("sql_desc",this.getSql_desc());
	 * objJson.put("dbio",this.getDbio()); objJson.put("tr_cd",this.getTr_cd());
	 * objJson.put("plan_hash_value",this.getPlan_hash_value());
	 * objJson.put("module",this.getModule());
	 * objJson.put("parsing_schema_name",this.getParsing_schema_name());
	 * objJson.put("executions",StringUtil.parseFloat(this.getExecutions(),0));
	 * objJson.put("avg_buffer_gets",StringUtil.parseFloat(this.
	 * getAvg_buffer_gets(),0));
	 * objJson.put("max_buffer_gets",StringUtil.parseFloat(this.
	 * getMax_buffer_gets(),0));
	 * objJson.put("total_buffer_gets",StringUtil.parseFloat(this.
	 * getTotal_buffer_gets(),0));
	 * objJson.put("avg_elapsed_time",StringUtil.parseFloat(this.
	 * getAvg_elapsed_time(),0));
	 * objJson.put("max_elapsed_time",StringUtil.parseFloat(this.
	 * getMax_elapsed_time(),0));
	 * objJson.put("avg_cpu_time",StringUtil.parseFloat(this.getAvg_cpu_time(),0
	 * ));
	 * objJson.put("avg_disk_reads",StringUtil.parseFloat(this.getAvg_disk_reads
	 * (),0)); objJson.put("avg_row_processed",StringUtil.parseFloat(this.
	 * getAvg_row_processed(),0));
	 * objJson.put("ratio_buffer_get_total",StringUtil.parseFloat(this.
	 * getRatio_buffer_get_total(),0));
	 * objJson.put("ratio_cpu_total",StringUtil.parseFloat(this.
	 * getRatio_cpu_total(),0));
	 * objJson.put("ratio_cpu_per_executions",StringUtil.parseFloat(this.
	 * getRatio_cpu_per_executions(),0));
	 * objJson.put("sql_text",this.getSql_text());
	 * 
	 * objJson.put("tuning_request_dt",this.getTuning_request_dt());
	 * objJson.put("wrkjob_cd",this.getWrkjob_cd());
	 * objJson.put("wrkjob_cd_nm",this.getWrkjob_cd_nm());
	 * objJson.put("wrkjob_mgr_id",this.getWrkjob_mgr_id());
	 * objJson.put("wrkjob_mgr_nm",this.getWrkjob_mgr_nm());
	 * objJson.put("wrkjob_mgr_wrkjob_cd",this.getWrkjob_mgr_wrkjob_cd());
	 * objJson.put("wrkjob_mgr_wrkjob_nm",this.getWrkjob_mgr_wrkjob_nm());
	 * objJson.put("wrkjob_mgr_tel_num",this.getWrkjob_mgr_tel_num());
	 * objJson.put("program_type_cd_nm",this.getProgram_type_cd_nm());
	 * objJson.put("batch_work_div_cd_nm",this.getBatch_work_div_cd_nm());
	 * objJson.put("db_name",this.getDb_name());
	 * objJson.put("perfr_nm",this.getPerfr_nm());
	 * objJson.put("tuning_complete_dt",this.getTuning_complete_dt());
	 * objJson.put("tuning_complete_why_nm",this.getTuning_complete_why_nm());
	 * objJson.put("imprb_elap_time",StringUtil.parseFloat(this.
	 * getImprb_elap_time(),0));
	 * objJson.put("imprb_buffer_cnt",StringUtil.parseFloat(this.
	 * getImprb_buffer_cnt(),0));
	 * objJson.put("imprb_pga_usage",StringUtil.parseFloat(this.
	 * getImprb_pga_usage(),0));
	 * objJson.put("impra_elap_time",StringUtil.parseFloat(this.
	 * getImpra_elap_time(),0));
	 * objJson.put("impra_buffer_cnt",StringUtil.parseFloat(this.
	 * getImpra_buffer_cnt(),0));
	 * objJson.put("impra_pga_usage",StringUtil.parseFloat(this.
	 * getImpra_pga_usage(),0));
	 * objJson.put("elap_time_impr_ratio",StringUtil.parseFloat(this.
	 * getElap_time_impr_ratio(),0));
	 * objJson.put("buffer_impr_ratio",StringUtil.parseFloat(this.
	 * getBuffer_impr_ratio(),0));
	 * objJson.put("pga_impr_ratio",StringUtil.parseFloat(this.getPga_impr_ratio
	 * (),0)); objJson.put("controversialist",this.getControversialist());
	 * objJson.put("impr_sbst",this.getImpr_sbst());
	 * objJson.put("impr_sql_text",this.getImpr_sql_text());
	 * objJson.put("imprb_exec_plan",this.getImprb_exec_plan());
	 * objJson.put("impra_exec_plan",this.getImpra_exec_plan());
	 * 
	 * objJson.put("tuning_why_nm",this.getTuning_why_nm());
	 * objJson.put("dba_name",this.getDba_name());
	 * objJson.put("tuning_status_change_dt",this.getTuning_status_change_dt());
	 * objJson.put("tuning_elapsed_time",this.getTuning_elapsed_time());
	 * objJson.put("tuning_delay_time",this.getTuning_delay_time());
	 * objJson.put("request_elapsed_time",this.getRequest_elapsed_time());
	 * objJson.put("complete_over_time",this.getComplete_over_time());
	 * objJson.put("tuning_cnt",StringUtil.parseDouble(this.getTuning_cnt(),0));
	 * objJson.put("knowledge_cnt",StringUtil.parseDouble(this.getKnowledge_cnt(
	 * ),0));
	 * 
	 * objJson.put("process_1",StringUtil.parseDouble(this.getProcess_1(),0));
	 * objJson.put("process_2",StringUtil.parseDouble(this.getProcess_2(),0));
	 * objJson.put("process_3",StringUtil.parseDouble(this.getProcess_3(),0));
	 * objJson.put("process_4",StringUtil.parseDouble(this.getProcess_4(),0));
	 * objJson.put("process_5",StringUtil.parseDouble(this.getProcess_5(),0));
	 * objJson.put("process_6",StringUtil.parseDouble(this.getProcess_6(),0));
	 * 
	 * objJson.put("tuning_receipt_cancel_why",this.getTuning_receipt_cancel_why
	 * ()); objJson.put("tuning_receipt_canceler_id",this.
	 * getTuning_receipt_canceler_id());
	 * objJson.put("tuning_receipt_cancel_dt",this.getTuning_receipt_cancel_dt()
	 * );
	 * 
	 * return objJson; }
	 */
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