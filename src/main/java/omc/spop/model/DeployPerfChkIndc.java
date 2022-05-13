package omc.spop.model;

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

@Alias("deployPerfChkIndc")
public class DeployPerfChkIndc extends Base implements Jsonable {

	private String perf_check_indc_id;
	private String perf_check_meth_cd;
	private String perf_check_meth_cd_nm;
	private String perf_check_indc_desc;
	private String perf_check_fail_guide_sbst;
	private String indc_use_yn;
	private String old_indc_use_yn;
	private String perf_check_indc_nm;
	private String ranger_wheter_cd;
	private String ranger_wheter_nm;
	private String user_auth_id;

	private String reg_div;
	private String top_wrkjob_cd;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;

	private String perf_check_program_div_cd;
	private String perf_check_program_div_cd_nm;
	private String program_div_cd_nm;
	private String pass_max_value;
	private String not_pass_pass;
	private String yn_decide_div_cd;
	private String yn_decide_div_cd_nm;
	private String indc_apply_yn;

	private String update_dt;
	private String old_perf_check_meth_cd;

	// ProgramPerformaceExceptionRequest
	private String temporary_exception_yn;
	private String exception_indc_apply_close;
	private String exception_request_id;
	private String last_exception_request_id;
	private String deploy_check_status_cd;
	private String deploy_check_status_nm;
	private String deploy_check_status_cd_nm;
	private String exception_prc_status_cd;
	private String exception_requester_id;
	private String exception_requester;
	private String excepter_id;
	private String program_div_cd;
	private String program_execute_tms;
	private String deploy_requester_id;
	private String perf_check_result_div_cd;
	private String program_type_cd;
	private String program_type_cd_nm;
	private String sql_command_type_cd;
	private String dynamic_sql_yn;
	
	private String perf_check_id;
	private String program_id;
	private String program_nm;
	private String dbio;
	private String deploy_id;
	private String deploy_nm;
	private String deploy_request_dt;
	private String deploy_expected_day;

	private String perf_check_step_nm;
	private String file_nm;
	private String dir_nm;
	private String perf_check_auto_pass_yn;
	private String perf_check_result_div_cd_nm;
	private String exception_request_dt;
	private String exception_request_why_cd_nm;
	private String exception_request_why_cd;
	private String exception_prc_meth_cd_nm;
	private String exception_prc_dt;
	private String exception_prc_why;
	private String exception_prc_meth_cd;
	private String exception_request_why;

	private String howToWay;
	private String search_wrkjob_cd;
	private String search_wrkjob_cd_nm;
	private String search_indc_apply_yn;
	private String search_chk;
	private String search_perf_check_step_id;
	private String search2_perf_check_step_id;
	private String search_from_deploy_request_dt;
	private String search_perf_check_result_div_cd;
	private String search_to_deploy_request_dt;
	private String search_exception_prc_meth_cd;
	private String search_exception_prc_status_cd;
	private String search_dbio;
	private String search_program_nm;
	private String search_program_desc;
	private String search_login_user_id;
	private String search_deploy_id;
	private String search_deploy_requester;
	private String search_user_id;
	private String search_program_id;
	private String search_perf_check_id;
	private String search_perf_check_step_nm;
	private String search_del_yn;
	private String search_exception_request_why_chk;
	private String search_date_chk;
	private String search_program_execute_tms;

	private String deploy_requester;
	private String step_deploy_check_status_cd;
	private String exception_prc_status_cd_nm;
	private String exception__requester;
	private String exception_request_detail_why;
	private String except_processor;
	private String last_perf_check_step_id;
	private String perf_check_step_id;
	private String program_source_desc;
	private String perf_check_auto_pass_del_yn;
	private String program_desc;

	private String bind_seq;
	private String bind_var_nm;
	private String bind_var_value;
	private String bind_var_type;

	private String indc_pass_max_value;
	private String indc_yn_decide_div_cd_nm;
	private String exec_result_value;
	private String except_reg_yn;
	private String indc_yn_decide_div_cd;
	private String perf_check_result_desc;

	private String excpt_pass_max_value;
	private String excpt_not_pass_pass;
	private String excpt_yn_decide_div_cd;
	private String excpt_yn_decide_div_cd_nm;

	private String perf_check_auto_exec_yn;
	private String perf_check_step_desc;
	private String step_ordering;
	private String old_step_ordering;
	private String del_yn;
	private String old_del_yn;
	private String dbid;
	private String parsing_schema_name;
	private String old_parsing_schema_name;
	private String chk_info;

	// 선택된 체크박스에의한 로우값 데이터 저장
	private String perf_check_indc_id_chk;
	private String indc_yn_decide_div_cd_chk;
	private String perf_check_result_div_cd_chk;
	private String perf_check_meth_cd_chk;
	private String change_indc_pass_max_value_chk;
	private String change_not_pass_pass_chk;
	private String except_reg_yn_chk;
	private String change_except_reg_yn_chk;
	private String indc_pass_max_value_chk;
	private String change_yn_decide_div_cd_chk;
	private String change_yn_decide_div_cd;
	private String change_indc_pass_max_value;
	private String change_not_pass_pass;
	private String is_checked;
	private String acceptUpdate;

	private String dbio_chk;
	private String excpt_pass_max_value_chk;
	private String excpt_yn_decide_div_cd_chk;

	private String exception_del_request_dt;
	private String exception_del_request_why;
	private String exception_del_prc_why;
	private String except_del_processor;

	private String total_excpt_cnt;
	private String sql_auto_pass_cnt;
	private String sql_indc_unit_cnt;
	private String program_auto_pass_cnt;
	private String tr_auto_pass_cnt;

	private String total_cnt;
	private String exception_request_why_cd01;
	private String exception_request_why_cd02;
	private String exception_request_why_cd03;
	private String exception_request_why_cd04;
	private String exception_request_why_cd05;
	private String exception_request_why_cd06;
	private String exception_request_why_cd07;
	private String exception_request_why_cd08;
	private String exception_request_why_cd09;
	private String exception_request_why_cd10;
	private String exception_request_why_cd_etc;

	private String request_cm_cnt;
	private String complete_cm_cnt;
	private String none_complete_cm_cnt;
	private String pass_cm_cnt;
	private String fail_cm_cnt;
	private String all_pgm_cnt;
	private String pass_pgm_cnt;
	private String fail_pgm_cnt;
	private String error_pgm_cnt;
	private String none_exec_pgm_cnt;
	private String auto_pass_cnt;

	private String elaspsed_time;
	private String buffer_gets;
	private String row_processed;
	private String pga;
	private String fullscan;
	private String partition_range_all;
	private String program_executer_id;

	private String array_perf_check_id;
	private String array_program_id; 
	
	private String dev_confirm_cm_cnt;// 개발확정
	private String checking_cm_cnt;// 점검중
	private String dev_confirm_cancel_cm_cnt;// 개발확정취소
	private String delete_cm_cnt;// 배포삭제
	
	private String new_pgm_cnt;// 신규 프로그램
	private String change_pgm_cnt;// 변경 프로그램
	private String same_pgm_cnt;// 동일 프로그램
	private String pagingYn;
	private String pagingCnt;
	private String reg_dt;
	private String last_update_dt;
	
	private String avg_exec_result_value;
	private String max_exec_result_value;
	private String perf_check_evaluation_meth_nm;
	private String perf_check_evaluation_meth_cd;
	
	private String search_from_exception_reqeust_dt; //예외 요청 일시
	private String search_to_exception_reqeust_dt; //예외 요청 일시

	public String getSearch_to_exception_reqeust_dt() {
		return search_to_exception_reqeust_dt;
	}

	public void setSearch_to_exception_reqeust_dt(String search_to_exception_reqeust_dt) {
		this.search_to_exception_reqeust_dt = search_to_exception_reqeust_dt;
	}

	public String getSearch_from_exception_reqeust_dt() {
		return search_from_exception_reqeust_dt;
	}

	public void setSearch_from_exception_reqeust_dt(String search_from_exception_reqeust_dt) {
		this.search_from_exception_reqeust_dt = search_from_exception_reqeust_dt;
	}

	public String getTop_wrkjob_cd() {
		return top_wrkjob_cd;
	}

	public void setTop_wrkjob_cd(String top_wrkjob_cd) {
		this.top_wrkjob_cd = top_wrkjob_cd;
	}

	public String getPerf_check_evaluation_meth_nm() {
		return perf_check_evaluation_meth_nm;
	}

	public void setPerf_check_evaluation_meth_nm(String perf_check_evaluation_meth_nm) {
		this.perf_check_evaluation_meth_nm = perf_check_evaluation_meth_nm;
	}

	public String getPerf_check_evaluation_meth_cd() {
		return perf_check_evaluation_meth_cd;
	}

	public void setPerf_check_evaluation_meth_cd(String perf_check_evaluation_meth_cd) {
		this.perf_check_evaluation_meth_cd = perf_check_evaluation_meth_cd;
	}

	public String getAvg_exec_result_value() {
		return avg_exec_result_value;
	}

	public void setAvg_exec_result_value(String avg_exec_result_value) {
		this.avg_exec_result_value = avg_exec_result_value;
	}

	public String getMax_exec_result_value() {
		return max_exec_result_value;
	}

	public void setMax_exec_result_value(String max_exec_result_value) {
		this.max_exec_result_value = max_exec_result_value;
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

	public String getPagingYn() {
		return pagingYn;
	}

	public void setPagingYn(String pagingYn) {
		this.pagingYn = pagingYn;
	}

	public String getPagingCnt() {
		return pagingCnt;
	}

	public void setPagingCnt(String pagingCnt) {
		this.pagingCnt = pagingCnt;
	}

	public String getProgram_type_cd_nm() {
		return program_type_cd_nm;
	}

	public void setProgram_type_cd_nm(String program_type_cd_nm) {
		this.program_type_cd_nm = program_type_cd_nm;
	}

	public String getProgram_type_cd() {
		return program_type_cd;
	}

	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
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

	public String getHowToWay() {
		return howToWay;
	}

	public void setHowToWay(String howToWay) {
		this.howToWay = howToWay;
	}

	public String getArray_perf_check_id() {
		return array_perf_check_id;
	}

	public void setArray_perf_check_id(String array_perf_check_id) {
		this.array_perf_check_id = array_perf_check_id;
	}

	public String getArray_program_id() {
		return array_program_id;
	}

	public void setArray_program_id(String array_program_id) {
		this.array_program_id = array_program_id;
	}

	public String getUser_auth_id() {
		return user_auth_id;
	}

	public void setUser_auth_id(String user_auth_id) {
		this.user_auth_id = user_auth_id;
	}

	public String getProgram_desc() {
		return program_desc;
	}

	public void setProgram_desc(String program_desc) {
		this.program_desc = program_desc;
	}

	public String getSearch_program_desc() {
		return search_program_desc;
	}

	public void setSearch_program_desc(String search_program_desc) {
		this.search_program_desc = search_program_desc;
	}

	public String getElaspsed_time() {
		return elaspsed_time;
	}

	public void setElaspsed_time(String elaspsed_time) {
		this.elaspsed_time = elaspsed_time;
	}

	public String getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public String getRow_processed() {
		return row_processed;
	}

	public void setRow_processed(String row_processed) {
		this.row_processed = row_processed;
	}

	public String getPga() {
		return pga;
	}

	public void setPga(String pga) {
		this.pga = pga;
	}

	public String getFullscan() {
		return fullscan;
	}

	public void setFullscan(String fullscan) {
		this.fullscan = fullscan;
	}

	public String getPartition_range_all() {
		return partition_range_all;
	}

	public void setPartition_range_all(String partition_range_all) {
		this.partition_range_all = partition_range_all;
	}

	public String getRequest_cm_cnt() {
		return request_cm_cnt;
	}

	public void setRequest_cm_cnt(String request_cm_cnt) {
		this.request_cm_cnt = request_cm_cnt;
	}

	public String getComplete_cm_cnt() {
		return complete_cm_cnt;
	}

	public void setComplete_cm_cnt(String complete_cm_cnt) {
		this.complete_cm_cnt = complete_cm_cnt;
	}

	public String getNone_complete_cm_cnt() {
		return none_complete_cm_cnt;
	}

	public void setNone_complete_cm_cnt(String none_complete_cm_cnt) {
		this.none_complete_cm_cnt = none_complete_cm_cnt;
	}

	public String getPass_cm_cnt() {
		return pass_cm_cnt;
	}

	public void setPass_cm_cnt(String pass_cm_cnt) {
		this.pass_cm_cnt = pass_cm_cnt;
	}

	public String getFail_cm_cnt() {
		return fail_cm_cnt;
	}

	public void setFail_cm_cnt(String fail_cm_cnt) {
		this.fail_cm_cnt = fail_cm_cnt;
	}

	public String getAll_pgm_cnt() {
		return all_pgm_cnt;
	}

	public void setAll_pgm_cnt(String all_pgm_cnt) {
		this.all_pgm_cnt = all_pgm_cnt;
	}

	public String getPass_pgm_cnt() {
		return pass_pgm_cnt;
	}

	public void setPass_pgm_cnt(String pass_pgm_cnt) {
		this.pass_pgm_cnt = pass_pgm_cnt;
	}

	public String getFail_pgm_cnt() {
		return fail_pgm_cnt;
	}

	public void setFail_pgm_cnt(String fail_pgm_cnt) {
		this.fail_pgm_cnt = fail_pgm_cnt;
	}

	public String getError_pgm_cnt() {
		return error_pgm_cnt;
	}

	public void setError_pgm_cnt(String error_pgm_cnt) {
		this.error_pgm_cnt = error_pgm_cnt;
	}

	public String getNone_exec_pgm_cnt() {
		return none_exec_pgm_cnt;
	}

	public void setNone_exec_pgm_cnt(String none_exec_pgm_cnt) {
		this.none_exec_pgm_cnt = none_exec_pgm_cnt;
	}

	public String getAuto_pass_cnt() {
		return auto_pass_cnt;
	}

	public void setAuto_pass_cnt(String auto_pass_cnt) {
		this.auto_pass_cnt = auto_pass_cnt;
	}

	public String getAcceptUpdate() {
		return acceptUpdate;
	}

	public void setAcceptUpdate(String acceptUpdate) {
		this.acceptUpdate = acceptUpdate;
	}

	public String getOld_del_yn() {
		return old_del_yn;
	}

	public void setOld_del_yn(String old_del_yn) {
		this.old_del_yn = old_del_yn;
	}

	public String getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}

	public String getException_request_why_cd01() {
		return exception_request_why_cd01;
	}

	public void setException_request_why_cd01(String exception_request_why_cd01) {
		this.exception_request_why_cd01 = exception_request_why_cd01;
	}

	public String getException_request_why_cd02() {
		return exception_request_why_cd02;
	}

	public void setException_request_why_cd02(String exception_request_why_cd02) {
		this.exception_request_why_cd02 = exception_request_why_cd02;
	}

	public String getException_request_why_cd03() {
		return exception_request_why_cd03;
	}

	public void setException_request_why_cd03(String exception_request_why_cd03) {
		this.exception_request_why_cd03 = exception_request_why_cd03;
	}

	public String getException_request_why_cd04() {
		return exception_request_why_cd04;
	}

	public void setException_request_why_cd04(String exception_request_why_cd04) {
		this.exception_request_why_cd04 = exception_request_why_cd04;
	}

	public String getException_request_why_cd05() {
		return exception_request_why_cd05;
	}

	public void setException_request_why_cd05(String exception_request_why_cd05) {
		this.exception_request_why_cd05 = exception_request_why_cd05;
	}

	public String getException_request_why_cd06() {
		return exception_request_why_cd06;
	}

	public void setException_request_why_cd06(String exception_request_why_cd06) {
		this.exception_request_why_cd06 = exception_request_why_cd06;
	}

	public String getException_request_why_cd07() {
		return exception_request_why_cd07;
	}

	public void setException_request_why_cd07(String exception_request_why_cd07) {
		this.exception_request_why_cd07 = exception_request_why_cd07;
	}

	public String getException_request_why_cd08() {
		return exception_request_why_cd08;
	}

	public void setException_request_why_cd08(String exception_request_why_cd08) {
		this.exception_request_why_cd08 = exception_request_why_cd08;
	}

	public String getException_request_why_cd09() {
		return exception_request_why_cd09;
	}

	public void setException_request_why_cd09(String exception_request_why_cd09) {
		this.exception_request_why_cd09 = exception_request_why_cd09;
	}

	public String getException_request_why_cd10() {
		return exception_request_why_cd10;
	}

	public void setException_request_why_cd10(String exception_request_why_cd10) {
		this.exception_request_why_cd10 = exception_request_why_cd10;
	}

	public String getException_request_why_cd_etc() {
		return exception_request_why_cd_etc;
	}

	public void setException_request_why_cd_etc(String exception_request_why_cd_etc) {
		this.exception_request_why_cd_etc = exception_request_why_cd_etc;
	}

	public String getTotal_excpt_cnt() {
		return total_excpt_cnt;
	}

	public void setTotal_excpt_cnt(String total_excpt_cnt) {
		this.total_excpt_cnt = total_excpt_cnt;
	}

	public String getSql_auto_pass_cnt() {
		return sql_auto_pass_cnt;
	}

	public void setSql_auto_pass_cnt(String sql_auto_pass_cnt) {
		this.sql_auto_pass_cnt = sql_auto_pass_cnt;
	}

	public String getSql_indc_unit_cnt() {
		return sql_indc_unit_cnt;
	}

	public void setSql_indc_unit_cnt(String sql_indc_unit_cnt) {
		this.sql_indc_unit_cnt = sql_indc_unit_cnt;
	}

	public String getProgram_auto_pass_cnt() {
		return program_auto_pass_cnt;
	}

	public void setProgram_auto_pass_cnt(String program_auto_pass_cnt) {
		this.program_auto_pass_cnt = program_auto_pass_cnt;
	}

	public String getTr_auto_pass_cnt() {
		return tr_auto_pass_cnt;
	}

	public void setTr_auto_pass_cnt(String tr_auto_pass_cnt) {
		this.tr_auto_pass_cnt = tr_auto_pass_cnt;
	}

	public String getSearch_program_id() {
		return search_program_id;
	}

	public void setSearch_program_id(String search_program_id) {
		this.search_program_id = search_program_id;
	}

	public String getSearch_perf_check_id() {
		return search_perf_check_id;
	}

	public void setSearch_perf_check_id(String search_perf_check_id) {
		this.search_perf_check_id = search_perf_check_id;
	}

	public String getStep_deploy_check_status_cd() {
		return step_deploy_check_status_cd;
	}

	public void setStep_deploy_check_status_cd(String step_deploy_check_status_cd) {
		this.step_deploy_check_status_cd = step_deploy_check_status_cd;
	}

	public String getSearch2_perf_check_step_id() {
		return search2_perf_check_step_id;
	}

	public void setSearch2_perf_check_step_id(String search2_perf_check_step_id) {
		this.search2_perf_check_step_id = search2_perf_check_step_id;
	}

	public String getSearch_perf_check_step_id() {
		return search_perf_check_step_id;
	}

	public void setSearch_perf_check_step_id(String search_perf_check_step_id) {
		this.search_perf_check_step_id = search_perf_check_step_id;
	}

	public String getSearch_chk() {
		return search_chk;
	}

	public void setSearch_chk(String search_chk) {
		this.search_chk = search_chk;
	}

	public String getExcpt_yn_decide_div_cd_nm() {
		return excpt_yn_decide_div_cd_nm;
	}

	public void setExcpt_yn_decide_div_cd_nm(String excpt_yn_decide_div_cd_nm) {
		this.excpt_yn_decide_div_cd_nm = excpt_yn_decide_div_cd_nm;
	}

	public String getLast_exception_request_id() {
		return last_exception_request_id;
	}

	public void setLast_exception_request_id(String last_exception_request_id) {
		this.last_exception_request_id = last_exception_request_id;
	}

	public String getException_del_request_dt() {
		return exception_del_request_dt;
	}

	public void setException_del_request_dt(String exception_del_request_dt) {
		this.exception_del_request_dt = exception_del_request_dt;
	}

	public String getException_del_request_why() {
		return exception_del_request_why;
	}

	public void setException_del_request_why(String exception_del_request_why) {
		this.exception_del_request_why = exception_del_request_why;
	}

	public String getException_del_prc_why() {
		return exception_del_prc_why;
	}

	public void setException_del_prc_why(String exception_del_prc_why) {
		this.exception_del_prc_why = exception_del_prc_why;
	}

	public String getExcept_del_processor() {
		return except_del_processor;
	}

	public void setExcept_del_processor(String except_del_processor) {
		this.except_del_processor = except_del_processor;
	}

	public String getDbio_chk() {
		return dbio_chk;
	}

	public void setDbio_chk(String dbio_chk) {
		this.dbio_chk = dbio_chk;
	}

	public String getExcpt_pass_max_value_chk() {
		return excpt_pass_max_value_chk;
	}

	public void setExcpt_pass_max_value_chk(String excpt_pass_max_value_chk) {
		this.excpt_pass_max_value_chk = excpt_pass_max_value_chk;
	}

	public String getExcpt_yn_decide_div_cd_chk() {
		return excpt_yn_decide_div_cd_chk;
	}

	public void setExcpt_yn_decide_div_cd_chk(String excpt_yn_decide_div_cd_chk) {
		this.excpt_yn_decide_div_cd_chk = excpt_yn_decide_div_cd_chk;
	}

	public String getExcpt_pass_max_value() {
		return excpt_pass_max_value;
	}

	public void setExcpt_pass_max_value(String excpt_pass_max_value) {
		this.excpt_pass_max_value = excpt_pass_max_value;
	}

	public String getExcpt_not_pass_pass() {
		return excpt_not_pass_pass;
	}

	public void setExcpt_not_pass_pass(String excpt_not_pass_pass) {
		this.excpt_not_pass_pass = excpt_not_pass_pass;
	}

	public String getExcpt_yn_decide_div_cd() {
		return excpt_yn_decide_div_cd;
	}

	public void setExcpt_yn_decide_div_cd(String excpt_yn_decide_div_cd) {
		this.excpt_yn_decide_div_cd = excpt_yn_decide_div_cd;
	}

	public String getPerf_check_auto_pass_del_yn() {
		return perf_check_auto_pass_del_yn;
	}

	public void setPerf_check_auto_pass_del_yn(String perf_check_auto_pass_del_yn) {
		this.perf_check_auto_pass_del_yn = perf_check_auto_pass_del_yn;
	}

	public String getProgram_div_cd_nm() {
		return program_div_cd_nm;
	}

	public void setProgram_div_cd_nm(String program_div_cd_nm) {
		this.program_div_cd_nm = program_div_cd_nm;
	}

	public String getSearch_exception_request_why_chk() {
		return search_exception_request_why_chk;
	}

	public void setSearch_exception_request_why_chk(String search_exception_request_why_chk) {
		this.search_exception_request_why_chk = search_exception_request_why_chk;
	}

	public String getIs_checked() {
		return is_checked;
	}

	public void setIs_checked(String is_checked) {
		this.is_checked = is_checked;
	}

	public String getPerf_check_result_desc() {
		return perf_check_result_desc;
	}

	public void setPerf_check_result_desc(String perf_check_result_desc) {
		this.perf_check_result_desc = perf_check_result_desc;
	}

	public String getChange_yn_decide_div_cd_chk() {
		return change_yn_decide_div_cd_chk;
	}

	public void setChange_yn_decide_div_cd_chk(String change_yn_decide_div_cd_chk) {
		this.change_yn_decide_div_cd_chk = change_yn_decide_div_cd_chk;
	}

	public String getChange_yn_decide_div_cd() {
		return change_yn_decide_div_cd;
	}

	public void setChange_yn_decide_div_cd(String change_yn_decide_div_cd) {
		this.change_yn_decide_div_cd = change_yn_decide_div_cd;
	}

	public String getSearch_perf_check_result_div_cd() {
		return search_perf_check_result_div_cd;
	}

	public void setSearch_perf_check_result_div_cd(String search_perf_check_result_div_cd) {
		this.search_perf_check_result_div_cd = search_perf_check_result_div_cd;
	}

	public String getSearch_date_chk() {
		return search_date_chk;
	}

	public void setSearch_date_chk(String search_date_chk) {
		this.search_date_chk = search_date_chk;
	}

	public String getSearch_exception_prc_meth_cd() {
		return search_exception_prc_meth_cd;
	}

	public void setSearch_exception_prc_meth_cd(String search_exception_prc_meth_cd) {
		this.search_exception_prc_meth_cd = search_exception_prc_meth_cd;
	}

	public String getChange_not_pass_pass() {
		return change_not_pass_pass;
	}

	public void setChange_not_pass_pass(String change_not_pass_pass) {
		this.change_not_pass_pass = change_not_pass_pass;
	}

	public String getChange_indc_pass_max_value() {
		return change_indc_pass_max_value;
	}

	public void setChange_indc_pass_max_value(String change_indc_pass_max_value) {
		this.change_indc_pass_max_value = change_indc_pass_max_value;
	}

	public String getSearch_user_id() {
		return search_user_id;
	}

	public void setSearch_user_id(String search_user_id) {
		this.search_user_id = search_user_id;
	}

	public String getIndc_pass_max_value_chk() {
		return indc_pass_max_value_chk;
	}

	public void setIndc_pass_max_value_chk(String indc_pass_max_value_chk) {
		this.indc_pass_max_value_chk = indc_pass_max_value_chk;
	}

	public String getExcept_reg_yn_chk() {
		return except_reg_yn_chk;
	}

	public void setExcept_reg_yn_chk(String except_reg_yn_chk) {
		this.except_reg_yn_chk = except_reg_yn_chk;
	}

	public String getPerf_check_indc_id_chk() {
		return perf_check_indc_id_chk;
	}

	public void setPerf_check_indc_id_chk(String perf_check_indc_id_chk) {
		this.perf_check_indc_id_chk = perf_check_indc_id_chk;
	}

	public String getIndc_yn_decide_div_cd_chk() {
		return indc_yn_decide_div_cd_chk;
	}

	public void setIndc_yn_decide_div_cd_chk(String indc_yn_decide_div_cd_chk) {
		this.indc_yn_decide_div_cd_chk = indc_yn_decide_div_cd_chk;
	}

	public String getPerf_check_result_div_cd_chk() {
		return perf_check_result_div_cd_chk;
	}

	public void setPerf_check_result_div_cd_chk(String perf_check_result_div_cd_chk) {
		this.perf_check_result_div_cd_chk = perf_check_result_div_cd_chk;
	}

	public String getPerf_check_meth_cd_chk() {
		return perf_check_meth_cd_chk;
	}

	public void setPerf_check_meth_cd_chk(String perf_check_meth_cd_chk) {
		this.perf_check_meth_cd_chk = perf_check_meth_cd_chk;
	}

	public String getChange_indc_pass_max_value_chk() {
		return change_indc_pass_max_value_chk;
	}

	public void setChange_indc_pass_max_value_chk(String change_indc_pass_max_value_chk) {
		this.change_indc_pass_max_value_chk = change_indc_pass_max_value_chk;
	}

	public String getChange_not_pass_pass_chk() {
		return change_not_pass_pass_chk;
	}

	public void setChange_not_pass_pass_chk(String change_not_pass_pass_chk) {
		this.change_not_pass_pass_chk = change_not_pass_pass_chk;
	}

	public String getChange_except_reg_yn_chk() {
		return change_except_reg_yn_chk;
	}

	public void setChange_except_reg_yn_chk(String change_except_reg_yn_chk) {
		this.change_except_reg_yn_chk = change_except_reg_yn_chk;
	}

	public String getChk_info() {
		return chk_info;
	}

	public void setChk_info(String chk_info) {
		this.chk_info = chk_info;
	}

	public String getException_request_why_cd() {
		return exception_request_why_cd;
	}

	public void setException_request_why_cd(String exception_request_why_cd) {
		this.exception_request_why_cd = exception_request_why_cd;
	}

	public String getException_requester() {
		return exception_requester;
	}

	public void setException_requester(String exception_requester) {
		this.exception_requester = exception_requester;
	}

	public String getOld_parsing_schema_name() {
		return old_parsing_schema_name;
	}

	public void setOld_parsing_schema_name(String old_parsing_schema_name) {
		this.old_parsing_schema_name = old_parsing_schema_name;
	}

	public String getOld_step_ordering() {
		return old_step_ordering;
	}

	public void setOld_step_ordering(String old_step_ordering) {
		this.old_step_ordering = old_step_ordering;
	}

	public String getPerf_check_auto_exec_yn() {
		return perf_check_auto_exec_yn;
	}

	public void setPerf_check_auto_exec_yn(String perf_check_auto_exec_yn) {
		this.perf_check_auto_exec_yn = perf_check_auto_exec_yn;
	}

	public String getPerf_check_step_desc() {
		return perf_check_step_desc;
	}

	public void setPerf_check_step_desc(String perf_check_step_desc) {
		this.perf_check_step_desc = perf_check_step_desc;
	}

	public String getStep_ordering() {
		return step_ordering;
	}

	public void setStep_ordering(String step_ordering) {
		this.step_ordering = step_ordering;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getSearch_perf_check_step_nm() {
		return search_perf_check_step_nm;
	}

	public void setSearch_perf_check_step_nm(String search_perf_check_step_nm) {
		this.search_perf_check_step_nm = search_perf_check_step_nm;
	}

	public String getSearch_del_yn() {
		return search_del_yn;
	}

	public void setSearch_del_yn(String search_del_yn) {
		this.search_del_yn = search_del_yn;
	}

	public String getPerf_check_step_id() {
		return perf_check_step_id;
	}

	public void setPerf_check_step_id(String perf_check_step_id) {
		this.perf_check_step_id = perf_check_step_id;
	}

	public String getIndc_pass_max_value() {
		return indc_pass_max_value;
	}

	public void setIndc_pass_max_value(String indc_pass_max_value) {
		this.indc_pass_max_value = indc_pass_max_value;
	}

	public String getIndc_yn_decide_div_cd_nm() {
		return indc_yn_decide_div_cd_nm;
	}

	public void setIndc_yn_decide_div_cd_nm(String indc_yn_decide_div_cd_nm) {
		this.indc_yn_decide_div_cd_nm = indc_yn_decide_div_cd_nm;
	}

	public String getExec_result_value() {
		return exec_result_value;
	}

	public void setExec_result_value(String exec_result_value) {
		this.exec_result_value = exec_result_value;
	}

	public String getExcept_reg_yn() {
		return except_reg_yn;
	}

	public void setExcept_reg_yn(String except_reg_yn) {
		this.except_reg_yn = except_reg_yn;
	}

	public String getIndc_yn_decide_div_cd() {
		return indc_yn_decide_div_cd;
	}

	public void setIndc_yn_decide_div_cd(String indc_yn_decide_div_cd) {
		this.indc_yn_decide_div_cd = indc_yn_decide_div_cd;
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

	public String getProgram_source_desc() {
		return program_source_desc;
	}

	public void setProgram_source_desc(String program_source_desc) {
		this.program_source_desc = program_source_desc;
	}

	public String getLast_perf_check_step_id() {
		return last_perf_check_step_id;
	}

	public void setLast_perf_check_step_id(String last_perf_check_step_id) {
		this.last_perf_check_step_id = last_perf_check_step_id;
	}

	public String getSearch_deploy_requester() {
		return search_deploy_requester;
	}

	public void setSearch_deploy_requester(String search_deploy_requester) {
		this.search_deploy_requester = search_deploy_requester;
	}

	public String getDeploy_requester() {
		return deploy_requester;
	}

	public void setDeploy_requester(String deploy_requester) {
		this.deploy_requester = deploy_requester;
	}

	public String getException_prc_status_cd_nm() {
		return exception_prc_status_cd_nm;
	}

	public void setException_prc_status_cd_nm(String exception_prc_status_cd_nm) {
		this.exception_prc_status_cd_nm = exception_prc_status_cd_nm;
	}

	public String getException__requester() {
		return exception__requester;
	}

	public void setException__requester(String exception__requester) {
		this.exception__requester = exception__requester;
	}

	public String getException_request_detail_why() {
		return exception_request_detail_why;
	}

	public void setException_request_detail_why(String exception_request_detail_why) {
		this.exception_request_detail_why = exception_request_detail_why;
	}

	public String getExcept_processor() {
		return except_processor;
	}

	public void setExcept_processor(String except_processor) {
		this.except_processor = except_processor;
	}

	public String getSearch_deploy_id() {
		return search_deploy_id;
	}

	public void setSearch_deploy_id(String search_deploy_id) {
		this.search_deploy_id = search_deploy_id;
	}

	public String getSearch_to_deploy_request_dt() {
		return search_to_deploy_request_dt;
	}

	public void setSearch_to_deploy_request_dt(String search_to_deploy_request_dt) {
		this.search_to_deploy_request_dt = search_to_deploy_request_dt;
	}

	public String getSearch_from_deploy_request_dt() {
		return search_from_deploy_request_dt;
	}

	public void setSearch_from_deploy_request_dt(String search_from_deploy_request_dt) {
		this.search_from_deploy_request_dt = search_from_deploy_request_dt;
	}

	public String getSearch_exception_prc_status_cd() {
		return search_exception_prc_status_cd;
	}

	public void setSearch_exception_prc_status_cd(String search_exception_prc_status_cd) {
		this.search_exception_prc_status_cd = search_exception_prc_status_cd;
	}

	public String getSearch_dbio() {
		return search_dbio;
	}

	public void setSearch_dbio(String search_dbio) {
		this.search_dbio = search_dbio;
	}

	public String getSearch_program_nm() {
		return search_program_nm;
	}

	public void setSearch_program_nm(String search_program_nm) {
		this.search_program_nm = search_program_nm;
	}

	public String getSearch_login_user_id() {
		return search_login_user_id;
	}

	public void setSearch_login_user_id(String search_login_user_id) {
		this.search_login_user_id = search_login_user_id;
	}

	public String getPerf_check_id() {
		return perf_check_id;
	}

	public void setPerf_check_id(String perf_check_id) {
		this.perf_check_id = perf_check_id;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getProgram_nm() {
		return program_nm;
	}

	public void setProgram_nm(String program_nm) {
		this.program_nm = program_nm;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getDeploy_id() {
		return deploy_id;
	}

	public void setDeploy_id(String deploy_id) {
		this.deploy_id = deploy_id;
	}

	public String getDeploy_nm() {
		return deploy_nm;
	}

	public void setDeploy_nm(String deploy_nm) {
		this.deploy_nm = deploy_nm;
	}

	public String getDeploy_request_dt() {
		return deploy_request_dt;
	}

	public void setDeploy_request_dt(String deploy_request_dt) {
		this.deploy_request_dt = deploy_request_dt;
	}

	public String getDeploy_expected_day() {
		return deploy_expected_day;
	}

	public void setDeploy_expected_day(String deploy_expected_day) {
		this.deploy_expected_day = deploy_expected_day;
	}

	public String getPerf_check_step_nm() {
		return perf_check_step_nm;
	}

	public void setPerf_check_step_nm(String perf_check_step_nm) {
		this.perf_check_step_nm = perf_check_step_nm;
	}

	public String getDeploy_check_status_cd_nm() {
		return deploy_check_status_cd_nm;
	}

	public void setDeploy_check_status_cd_nm(String deploy_check_status_cd_nm) {
		this.deploy_check_status_cd_nm = deploy_check_status_cd_nm;
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

	public String getPerf_check_auto_pass_yn() {
		return perf_check_auto_pass_yn;
	}

	public void setPerf_check_auto_pass_yn(String perf_check_auto_pass_yn) {
		this.perf_check_auto_pass_yn = perf_check_auto_pass_yn;
	}

	public String getPerf_check_result_div_cd_nm() {
		return perf_check_result_div_cd_nm;
	}

	public void setPerf_check_result_div_cd_nm(String perf_check_result_div_cd_nm) {
		this.perf_check_result_div_cd_nm = perf_check_result_div_cd_nm;
	}

	public String getException_request_dt() {
		return exception_request_dt;
	}

	public void setException_request_dt(String exception_request_dt) {
		this.exception_request_dt = exception_request_dt;
	}

	public String getException_request_why_cd_nm() {
		return exception_request_why_cd_nm;
	}

	public void setException_request_why_cd_nm(String exception_request_why_cd_nm) {
		this.exception_request_why_cd_nm = exception_request_why_cd_nm;
	}

	public String getException_prc_meth_cd_nm() {
		return exception_prc_meth_cd_nm;
	}

	public void setException_prc_meth_cd_nm(String exception_prc_meth_cd_nm) {
		this.exception_prc_meth_cd_nm = exception_prc_meth_cd_nm;
	}

	public String getException_prc_dt() {
		return exception_prc_dt;
	}

	public void setException_prc_dt(String exception_prc_dt) {
		this.exception_prc_dt = exception_prc_dt;
	}

	public String getException_prc_why() {
		return exception_prc_why;
	}

	public void setException_prc_why(String exception_prc_why) {
		this.exception_prc_why = exception_prc_why;
	}

	public String getException_prc_meth_cd() {
		return exception_prc_meth_cd;
	}

	public void setException_prc_meth_cd(String exception_prc_meth_cd) {
		this.exception_prc_meth_cd = exception_prc_meth_cd;
	}

	public String getException_request_why() {
		return exception_request_why;
	}

	public void setException_request_why(String exception_request_why) {
		this.exception_request_why = exception_request_why;
	}

	public String getTemporary_exception_yn() {
		return temporary_exception_yn;
	}

	public void setTemporary_exception_yn(String temporary_exception_yn) {
		this.temporary_exception_yn = temporary_exception_yn;
	}

	public String getException_indc_apply_close() {
		return exception_indc_apply_close;
	}

	public void setException_indc_apply_close(String exception_indc_apply_close) {
		this.exception_indc_apply_close = exception_indc_apply_close;
	}

	public String getException_request_id() {
		return exception_request_id;
	}

	public void setException_request_id(String exception_request_id) {
		this.exception_request_id = exception_request_id;
	}

	public String getDeploy_check_status_cd() {
		return deploy_check_status_cd;
	}

	public void setDeploy_check_status_cd(String deploy_check_status_cd) {
		this.deploy_check_status_cd = deploy_check_status_cd;
	}

	public String getException_prc_status_cd() {
		return exception_prc_status_cd;
	}

	public void setException_prc_status_cd(String exception_prc_status_cd) {
		this.exception_prc_status_cd = exception_prc_status_cd;
	}

	public String getException_requester_id() {
		return exception_requester_id;
	}

	public void setException_requester_id(String exception_requester_id) {
		this.exception_requester_id = exception_requester_id;
	}

	public String getExcepter_id() {
		return excepter_id;
	}

	public void setExcepter_id(String excepter_id) {
		this.excepter_id = excepter_id;
	}

	public String getProgram_div_cd() {
		return program_div_cd;
	}

	public void setProgram_div_cd(String program_div_cd) {
		this.program_div_cd = program_div_cd;
	}

	public String getProgram_execute_tms() {
		return program_execute_tms;
	}

	public void setProgram_execute_tms(String program_execute_tms) {
		this.program_execute_tms = program_execute_tms;
	}

	public String getDeploy_requester_id() {
		return deploy_requester_id;
	}

	public void setDeploy_requester_id(String deploy_requester_id) {
		this.deploy_requester_id = deploy_requester_id;
	}

	public String getPerf_check_result_div_cd() {
		return perf_check_result_div_cd;
	}

	public void setPerf_check_result_div_cd(String perf_check_result_div_cd) {
		this.perf_check_result_div_cd = perf_check_result_div_cd;
	}

	public String getOld_perf_check_meth_cd() {
		return old_perf_check_meth_cd;
	}

	public void setOld_perf_check_meth_cd(String old_perf_check_meth_cd) {
		this.old_perf_check_meth_cd = old_perf_check_meth_cd;
	}

	public String getSearch_indc_apply_yn() {
		return search_indc_apply_yn;
	}

	public void setSearch_indc_apply_yn(String search_indc_apply_yn) {
		this.search_indc_apply_yn = search_indc_apply_yn;
	}

	public String getSearch_wrkjob_cd() {
		return search_wrkjob_cd;
	}

	public void setSearch_wrkjob_cd(String search_wrkjob_cd) {
		this.search_wrkjob_cd = search_wrkjob_cd;
	}

	public String getIndc_apply_yn() {
		return indc_apply_yn;
	}

	public void setIndc_apply_yn(String indc_apply_yn) {
		this.indc_apply_yn = indc_apply_yn;
	}

	public String getReg_div() {
		return reg_div;
	}

	public void setReg_div(String reg_div) {
		this.reg_div = reg_div;
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

	public String getPerf_check_program_div_cd() {
		return perf_check_program_div_cd;
	}

	public void setPerf_check_program_div_cd(String perf_check_program_div_cd) {
		this.perf_check_program_div_cd = perf_check_program_div_cd;
	}

	public String getPerf_check_program_div_cd_nm() {
		return perf_check_program_div_cd_nm;
	}

	public void setPerf_check_program_div_cd_nm(String perf_check_program_div_cd_nm) {
		this.perf_check_program_div_cd_nm = perf_check_program_div_cd_nm;
	}

	public String getPass_max_value() {
		return pass_max_value;
	}

	public void setPass_max_value(String pass_max_value) {
		this.pass_max_value = pass_max_value;
	}

	public String getNot_pass_pass() {
		return not_pass_pass;
	}

	public void setNot_pass_pass(String not_pass_pass) {
		this.not_pass_pass = not_pass_pass;
	}

	public String getYn_decide_div_cd() {
		return yn_decide_div_cd;
	}

	public void setYn_decide_div_cd(String yn_decide_div_cd) {
		this.yn_decide_div_cd = yn_decide_div_cd;
	}

	public String getYn_decide_div_cd_nm() {
		return yn_decide_div_cd_nm;
	}

	public void setYn_decide_div_cd_nm(String yn_decide_div_cd_nm) {
		this.yn_decide_div_cd_nm = yn_decide_div_cd_nm;
	}

	public String getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}

	public String getRanger_wheter_cd() {
		return ranger_wheter_cd;
	}

	public void setRanger_wheter_cd(String ranger_wheter_cd) {
		this.ranger_wheter_cd = ranger_wheter_cd;
	}

	public String getRanger_wheter_nm() {
		return ranger_wheter_nm;
	}

	public void setRanger_wheter_nm(String ranger_wheter_nm) {
		this.ranger_wheter_nm = ranger_wheter_nm;
	}

	public String getPerf_check_indc_nm() {
		return perf_check_indc_nm;
	}

	public void setPerf_check_indc_nm(String perf_check_indc_nm) {
		this.perf_check_indc_nm = perf_check_indc_nm;
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

	public String getPerf_check_meth_cd_nm() {
		return perf_check_meth_cd_nm;
	}

	public void setPerf_check_meth_cd_nm(String perf_check_meth_cd_nm) {
		this.perf_check_meth_cd_nm = perf_check_meth_cd_nm;
	}

	public String getPerf_check_indc_desc() {
		return perf_check_indc_desc;
	}

	public void setPerf_check_indc_desc(String perf_check_indc_desc) {
		this.perf_check_indc_desc = perf_check_indc_desc;
	}

	public String getPerf_check_fail_guide_sbst() {
		return perf_check_fail_guide_sbst;
	}

	public void setPerf_check_fail_guide_sbst(String perf_check_fail_guide_sbst) {
		this.perf_check_fail_guide_sbst = perf_check_fail_guide_sbst;
	}

	public String getOld_indc_use_yn() {
		return old_indc_use_yn;
	}

	public void setOld_indc_use_yn(String old_indc_use_yn) {
		this.old_indc_use_yn = old_indc_use_yn;
	}

	public String getIndc_use_yn() {
		return indc_use_yn;
	}

	public void setIndc_use_yn(String indc_use_yn) {
		this.indc_use_yn = indc_use_yn;
	}

	public String getSearch_wrkjob_cd_nm() {
		return search_wrkjob_cd_nm;
	}

	public void setSearch_wrkjob_cd_nm(String search_wrkjob_cd_nm) {
		this.search_wrkjob_cd_nm = search_wrkjob_cd_nm;
	}

	public String getSearch_program_execute_tms() {
		return search_program_execute_tms;
	}

	public void setSearch_program_execute_tms(String search_program_execute_tms) {
		this.search_program_execute_tms = search_program_execute_tms;
	}

	public String getDeploy_check_status_nm() {
		return deploy_check_status_nm;
	}

	public void setDeploy_check_status_nm(String deploy_check_status_nm) {
		this.deploy_check_status_nm = deploy_check_status_nm;
	}

	public String getProgram_executer_id() {
		return program_executer_id;
	}

	public void setProgram_executer_id(String program_executer_id) {
		this.program_executer_id = program_executer_id;
	}

	public String getDev_confirm_cm_cnt() {
		return dev_confirm_cm_cnt;
	}

	public void setDev_confirm_cm_cnt(String dev_confirm_cm_cnt) {
		this.dev_confirm_cm_cnt = dev_confirm_cm_cnt;
	}

	public String getChecking_cm_cnt() {
		return checking_cm_cnt;
	}

	public void setChecking_cm_cnt(String checking_cm_cnt) {
		this.checking_cm_cnt = checking_cm_cnt;
	}

	public String getDev_confirm_cancel_cm_cnt() {
		return dev_confirm_cancel_cm_cnt;
	}

	public void setDev_confirm_cancel_cm_cnt(String dev_confirm_cancel_cm_cnt) {
		this.dev_confirm_cancel_cm_cnt = dev_confirm_cancel_cm_cnt;
	}

	public String getDelete_cm_cnt() {
		return delete_cm_cnt;
	}

	public void setDelete_cm_cnt(String delete_cm_cnt) {
		this.delete_cm_cnt = delete_cm_cnt;
	}

	public String getNew_pgm_cnt() {
		return new_pgm_cnt;
	}

	public void setNew_pgm_cnt(String new_pgm_cnt) {
		this.new_pgm_cnt = new_pgm_cnt;
	}

	public String getChange_pgm_cnt() {
		return change_pgm_cnt;
	}

	public void setChange_pgm_cnt(String change_pgm_cnt) {
		this.change_pgm_cnt = change_pgm_cnt;
	}

	public String getSame_pgm_cnt() {
		return same_pgm_cnt;
	}

	public void setSame_pgm_cnt(String same_pgm_cnt) {
		this.same_pgm_cnt = same_pgm_cnt;
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
