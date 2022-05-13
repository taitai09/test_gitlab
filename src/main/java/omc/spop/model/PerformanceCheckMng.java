package omc.spop.model;

import java.io.Serializable;
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
 * 2019.01.04
 **********************************************************/

@Alias("performanceCheckMng")
public class PerformanceCheckMng extends Base implements Jsonable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 성능점검ID */
	private String perf_check_id;
	/** 업무코드 */
	private String wrkjob_cd;
	private String top_wrkjob_cd_nm;
	private String top_wrkjob_cd;
	private String search_wrkjob_cd;
	private String search_wrkjob_cd_nm;
	/** 업무명 */
	private String wrkjob_cd_nm;
	/** 배포성능점검상태코드 */
	private String deploy_check_status_cd;
	/** 배포성능점검상태명 */
	private String deploy_check_status_nm;
	/** 성능점검단계명 */
	private String perf_check_step_nm;
	/** 성능점검단계id */
	private String perf_check_step_id;
	/** 성능점검자동실행여부 */
	private String perf_check_auto_exec_yn;
	/** 정렬 */
	private String step_ordering;

	private String perf_check_result_div_nm;
	/** 배포ID */
	private String deploy_id;
	private String search_deploy_id;
	/** 배포명 */
	private String deploy_nm;
	/** 배포요청일시 */
	private String deploy_request_dt;
	/** 배포요청일시 검색시작일 */
	private String deploy_request_dt_1;
	/** 배포요청일시 검색종료일 */
	private String deploy_request_dt_2;
	/** 배포예정일 */
	private String deploy_expected_day;
	/** 점검완료일시 */
	private String perf_check_complete_dt;
	private String deploy_update_sbst;
	private String deploy_check_status_chg_dt;
	/** 최종성능점검단계ID */
	private String last_perf_check_step_id;
	private String before_perf_check_id;
	/**성능점검결과구분코드 */
	private String perf_check_result_div_cd;
	/** 배포요청자ID */
	private String deploy_requester_id;
	private String deploy_requester_nm;
	/** 배포업무분류코드 */
	private String deploy_wrkjob_div_cd;
	private String wrkjob_div_cd;

	private int start_num;
	private int end_num;

	private String perf_test_complete_yn;
	private String perf_test_db_name;
	private String perf_check_request_dt;
	private String perf_check_request_type_nm;
	private String total_cnt;
	private String pass_cnt;
	private String fail_cnt;
	private String err_cnt;
	private String test_miss_cnt;
	private String exception_cnt;
	private String perf_check_request_type_cd;
	/** 최종성능점검단계여부 */
	private String last_perf_check_step_yn;
	/** 성능점검완료자ID=로그인사용자ID */
	private String perf_check_completer_id;

	private String deploy_check_status_chg_why;
	/**
	 *  성능점검완료일시
	 *  점검요청일시 
	 */
	private String deploy_check_status_update_dt;

	private String program_div_nm;
	private String dbio;
	/**
	 * tr_cd = 소스파일명(FULL PATH) : DEPLOY_PERF_CHK_ALL_PGM 테이블의 DIR_NM || '/' || FILE_NM
	 */
	private String tr_cd;
	private String program_dvlp_div_nm;

	private String program_id;
	private String program_nm;
	private String parsing_schema_name;

	private String program_execute_tms;
	private String hidden_program_execute_tms;

	private String paging_yn;
	private String paging_cnt;

	private String program_exec_div_nm;
	private String program_exec_div_cd;
	private String program_executer_id;
	private String program_executer_nm;
	private String program_exec_dt;
	private String file_nm;
	private String dir_nm;
	private String exception_processing_yn;

	private String perf_check_auto_pass_yn;
	private String reg_dt;
	private String last_update_dt;
	private String program_source_desc;
	private String program_source_desc2;
	private String program_desc;
	private String improper;
	private String unperformed;
	private String perf_check_result_div_cd_d;

	private String bind_seq;
	private String bind_var;
	private String bind_var_nm;
	private String bind_var_value;
	private String bind_var_type;
	private String hidden_bind_var_value;
	private String perf_check_result_basis_why1;
	private String perf_check_result_basis_why2;
	private String exec_plan;
	private String sql_text;
	/**
	 * 점검지표
	 */
	private String perf_check_indc_nm;
	/**
	 * 지표설명
	 */
	private String perf_check_indc_desc;
	/**
	 * 개선가이드
	 */
	private String perf_check_fail_guide_sbst;

	private String indc_pass_max_value;
	private String exec_result_value;
	private String exception_yn;
	private String indc_yn_decide_div_cd;
	private String perf_check_indc_id;
	private String perf_check_meth_cd;
	private String perf_check_meth_nm;

	private String db_name;
	private String bind_cnt;
	private String elapsed_time;
	private String buffer_gets;
	private String executions;
	private String rows_processed;

	private String compcd;
	private String cmid;
	private String cmpknm;
	private String cmdepday;
	private String cmusrnum;
	private String jobcd;
	private String cmcreateday;
	private String deploy_check_status_updater_id;
	private String perf_check_request_cn_div_cd;
	private String agent_status_div_cd;
	private String sql_id;
	private String snap_time;
	private String last_captured;
	private String exception_button_enable_yn;
	private String impr_guide;
	private String auto_skip;
	
	private String pagingYn;
	private int pagingCnt;
	private int perfCheckId;
	private int perfCheckStepId ;
	private int programId ;
	private int programExecuteTms;
	private String params;
	/**
	 * 성능점검완료방법코드
	 */
	private String perf_check_complete_meth_cd;
	/**
	 * 성능점검완료방법명
	 */
	private String perf_check_complete_meth_nm;
	/**
	 * 성능점검결과설명
	 */
	private String perf_check_result_desc;

	/**
	 * 성능점검대상단계여부
	 */
	private String perf_test_target_step_yn;
	/**
	 * 점검결과통보여부
	 */
	private String check_result_anc_yn;
	/**
	 * 점검결과통보일시
	 */
	private String last_check_result_anc_dt;
	/**
	 * 점검결과통보횟수
	 */
	private String check_result_anc_cnt;
	/**
	 * 점검상태 검색조건
	 */
	private String search_deploy_check_status_cd;
	/**
	 * 최종점검단계 검색조건
	 */
	private String search_last_perf_check_step_id;

	private String pop_perf_check_id;
	private String pop_perf_check_step_id;
	private String pop_program_id;
	private String pop_program_execute_tms;
	private String refresh;
	/**
	 * 점검제외코드
	 */
	private String exception_prc_meth_cd;
	/**
	 * 점검제외명
	 */
	private String exception_prc_meth_nm;

	/**
	 * 최종성능점검단계
	 */
	private String final_perf_check_step;

	private String program_type_cd;
	private String hidden_program_type_cd;
	private String program_type_nm;
	private String sql_command_type_cd;
	private String sql_command_type_nm;
	private String dynamic_sql_yn;
	private String hidden_sql_command_type_cd;

	/**
	 * 현재수행시간
	 */
	private BigDecimal current_elap_time;
	/**
	 * 예상결과건수
	 */
	private String forecast_result_cnt;
	/**
	 * 점검대상건수
	 */
	private String check_tgt_cnt;
	
	// 비지니스 동작 중에 특정 ID가 존재하지 않을 경우를 대비한 에러 메시지
	// From KB 카드
	private String error_message;
	
	private boolean onlyIncorrectYn;
	
	private String avg_exec_result_value;			// 성능 검증 결과 평균
	private String max_exec_result_value;			// 성능 검증 결과 최대값
	private String perf_check_evaluation_meth_cd;	// 성능 검증 평가 코드 : 1-개별, 2-평균 3-최대
	
	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
	}

	public String getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}

	public boolean isOnlyIncorrectYn() {
		return onlyIncorrectYn;
	}

	public void setOnlyIncorrectYn(String onlyIncorrectYn) {
		boolean booleanVal = false;
		
		try {
			booleanVal = Boolean.parseBoolean(onlyIncorrectYn);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		this.onlyIncorrectYn = booleanVal;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
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
	
	public int getPerfCheckId() {
		return perfCheckId;
	}

	public void setPerfCheckId(int perfCheckId) {
		this.perfCheckId = perfCheckId;
	}

	public int getPerfCheckStepId() {
		return perfCheckStepId;
	}

	public void setPerfCheckStepId(int perfCheckStepId) {
		this.perfCheckStepId = perfCheckStepId;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public int getProgramExecuteTms() {
		return programExecuteTms;
	}

	public void setProgramExecuteTms(int programExecuteTms) {
		this.programExecuteTms = programExecuteTms;
	}

	public String getPaging_yn() {
		return paging_yn;
	}

	public void setPaging_yn(String paging_yn) {
		this.paging_yn = paging_yn;
	}

	public String getPaging_cnt() {
		return paging_cnt;
	}

	public void setPaging_cnt(String paging_cnt) {
		this.paging_cnt = paging_cnt;
	}

	public String getPerf_check_id() {
		return perf_check_id;
	}

	public void setPerf_check_id(String perf_check_id) {
		this.perf_check_id = perf_check_id;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getDeploy_check_status_nm() {
		return deploy_check_status_nm;
	}

	public void setDeploy_check_status_nm(String deploy_check_status_nm) {
		this.deploy_check_status_nm = deploy_check_status_nm;
	}

	public String getPerf_check_step_nm() {
		return perf_check_step_nm;
	}

	public void setPerf_check_step_nm(String perf_check_step_nm) {
		this.perf_check_step_nm = perf_check_step_nm;
	}

	public String getPerf_check_result_div_nm() {
		return perf_check_result_div_nm;
	}

	public void setPerf_check_result_div_nm(String perf_check_result_div_nm) {
		this.perf_check_result_div_nm = perf_check_result_div_nm;
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

	public String getDeploy_requester_nm() {
		return deploy_requester_nm;
	}

	public void setDeploy_requester_nm(String deploy_requester_nm) {
		this.deploy_requester_nm = deploy_requester_nm;
	}

	public String getDeploy_request_dt() {
		return deploy_request_dt;
	}

	public void setDeploy_request_dt(String deploy_request_dt) {
		this.deploy_request_dt = deploy_request_dt;
	}

	public String getDeploy_request_dt_1() {
		return deploy_request_dt_1;
	}

	public void setDeploy_request_dt_1(String deploy_request_dt_1) {
		this.deploy_request_dt_1 = deploy_request_dt_1;
	}

	public String getDeploy_request_dt_2() {
		return deploy_request_dt_2;
	}

	public void setDeploy_request_dt_2(String deploy_request_dt_2) {
		this.deploy_request_dt_2 = deploy_request_dt_2;
	}

	public String getDeploy_expected_day() {
		return deploy_expected_day;
	}

	public void setDeploy_expected_day(String deploy_expected_day) {
		this.deploy_expected_day = deploy_expected_day;
	}

	public String getPerf_check_complete_dt() {
		return perf_check_complete_dt;
	}

	public void setPerf_check_complete_dt(String perf_check_complete_dt) {
		this.perf_check_complete_dt = perf_check_complete_dt;
	}

	public String getDeploy_update_sbst() {
		return deploy_update_sbst;
	}

	public void setDeploy_update_sbst(String deploy_update_sbst) {
		this.deploy_update_sbst = deploy_update_sbst;
	}

	public String getDeploy_check_status_chg_dt() {
		return deploy_check_status_chg_dt;
	}

	public void setDeploy_check_status_chg_dt(String deploy_check_status_chg_dt) {
		this.deploy_check_status_chg_dt = deploy_check_status_chg_dt;
	}

	public String getLast_perf_check_step_id() {
		return last_perf_check_step_id;
	}

	public void setLast_perf_check_step_id(String last_perf_check_step_id) {
		this.last_perf_check_step_id = last_perf_check_step_id;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getBefore_perf_check_id() {
		return before_perf_check_id;
	}

	public void setBefore_perf_check_id(String before_perf_check_id) {
		this.before_perf_check_id = before_perf_check_id;
	}

	public String getDeploy_check_status_cd() {
		return deploy_check_status_cd;
	}

	public void setDeploy_check_status_cd(String deploy_check_status_cd) {
		this.deploy_check_status_cd = deploy_check_status_cd;
	}

	public String getPerf_check_result_div_cd() {
		return perf_check_result_div_cd;
	}

	public void setPerf_check_result_div_cd(String perf_check_result_div_cd) {
		this.perf_check_result_div_cd = perf_check_result_div_cd;
	}

	public String getDeploy_requester_id() {
		return deploy_requester_id;
	}

	public void setDeploy_requester_id(String deploy_requester_id) {
		this.deploy_requester_id = deploy_requester_id;
	}

	public int getStart_num() {
		return start_num;
	}

	public void setStart_num(int start_num) {
		this.start_num = start_num;
	}

	public int getEnd_num() {
		return end_num;
	}

	public void setEnd_num(int end_num) {
		this.end_num = end_num;
	}

	public String getPerf_check_step_id() {
		return perf_check_step_id;
	}

	public void setPerf_check_step_id(String perf_check_step_id) {
		this.perf_check_step_id = perf_check_step_id;
	}

	public String getPerf_check_auto_exec_yn() {
		return perf_check_auto_exec_yn;
	}

	public void setPerf_check_auto_exec_yn(String perf_check_auto_exec_yn) {
		this.perf_check_auto_exec_yn = perf_check_auto_exec_yn;
	}

	public String getStep_ordering() {
		return step_ordering;
	}

	public void setStep_ordering(String step_ordering) {
		this.step_ordering = step_ordering;
	}

	public String getPerf_test_complete_yn() {
		return perf_test_complete_yn;
	}

	public void setPerf_test_complete_yn(String perf_test_complete_yn) {
		this.perf_test_complete_yn = perf_test_complete_yn;
	}

	public String getPerf_test_db_name() {
		return perf_test_db_name;
	}

	public void setPerf_test_db_name(String perf_test_db_name) {
		this.perf_test_db_name = perf_test_db_name;
	}

	public String getPerf_check_request_dt() {
		return perf_check_request_dt;
	}

	public void setPerf_check_request_dt(String perf_check_request_dt) {
		this.perf_check_request_dt = perf_check_request_dt;
	}

	public String getPerf_check_request_type_nm() {
		return perf_check_request_type_nm;
	}

	public void setPerf_check_request_type_nm(String perf_check_request_type_nm) {
		this.perf_check_request_type_nm = perf_check_request_type_nm;
	}

	public String getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}

	public String getPass_cnt() {
		return pass_cnt;
	}

	public void setPass_cnt(String pass_cnt) {
		this.pass_cnt = pass_cnt;
	}

	public String getFail_cnt() {
		return fail_cnt;
	}

	public void setFail_cnt(String fail_cnt) {
		this.fail_cnt = fail_cnt;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}

	public String getTest_miss_cnt() {
		return test_miss_cnt;
	}

	public void setTest_miss_cnt(String test_miss_cnt) {
		this.test_miss_cnt = test_miss_cnt;
	}

	public String getPerf_check_request_type_cd() {
		return perf_check_request_type_cd;
	}

	public void setPerf_check_request_type_cd(String perf_check_request_type_cd) {
		this.perf_check_request_type_cd = perf_check_request_type_cd;
	}

	public String getLast_perf_check_step_yn() {
		return last_perf_check_step_yn;
	}

	public void setLast_perf_check_step_yn(String last_perf_check_step_yn) {
		this.last_perf_check_step_yn = last_perf_check_step_yn;
	}

	public String getException_cnt() {
		return exception_cnt;
	}

	public void setException_cnt(String exception_cnt) {
		this.exception_cnt = exception_cnt;
	}

	public String getDeploy_check_status_chg_why() {
		return deploy_check_status_chg_why;
	}

	public void setDeploy_check_status_chg_why(String deploy_check_status_chg_why) {
		this.deploy_check_status_chg_why = deploy_check_status_chg_why;
	}

	public String getPerf_check_completer_id() {
		return perf_check_completer_id;
	}

	public void setPerf_check_completer_id(String perf_check_completer_id) {
		this.perf_check_completer_id = perf_check_completer_id;
	}

	public String getDeploy_check_status_update_dt() {
		return deploy_check_status_update_dt;
	}

	public void setDeploy_check_status_update_dt(String deploy_check_status_update_dt) {
		this.deploy_check_status_update_dt = deploy_check_status_update_dt;
	}

	public String getProgram_div_nm() {
		return program_div_nm;
	}

	public void setProgram_div_nm(String program_div_nm) {
		this.program_div_nm = program_div_nm;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getProgram_nm() {
		return program_nm;
	}

	public void setProgram_nm(String program_nm) {
		this.program_nm = program_nm;
	}

	public String getProgram_dvlp_div_nm() {
		return program_dvlp_div_nm;
	}

	public void setProgram_dvlp_div_nm(String program_dvlp_div_nm) {
		this.program_dvlp_div_nm = program_dvlp_div_nm;
	}

	public String getProgram_id() {
		return program_id;
	}

	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getProgram_exec_div_nm() {
		return program_exec_div_nm;
	}

	public void setProgram_exec_div_nm(String program_exec_div_nm) {
		this.program_exec_div_nm = program_exec_div_nm;
	}

	public String getProgram_executer_nm() {
		return program_executer_nm;
	}

	public void setProgram_executer_nm(String program_executer_nm) {
		this.program_executer_nm = program_executer_nm;
	}

	public String getProgram_exec_dt() {
		return program_exec_dt;
	}

	public void setProgram_exec_dt(String program_exec_dt) {
		this.program_exec_dt = program_exec_dt;
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

	public String getException_processing_yn() {
		return exception_processing_yn;
	}

	public void setException_processing_yn(String exception_processing_yn) {
		this.exception_processing_yn = exception_processing_yn;
	}

	public String getPerf_check_auto_pass_yn() {
		return perf_check_auto_pass_yn;
	}

	public void setPerf_check_auto_pass_yn(String perf_check_auto_pass_yn) {
		this.perf_check_auto_pass_yn = perf_check_auto_pass_yn;
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

	public String getProgram_source_desc() {
		return program_source_desc;
	}

	public void setProgram_source_desc(String program_source_desc) {
		this.program_source_desc = program_source_desc;
	}

	public String getUnperformed() {
		return unperformed;
	}

	public void setUnperformed(String unperformed) {
		this.unperformed = unperformed;
	}

	public String getPerf_check_result_div_cd_d() {
		return perf_check_result_div_cd_d;
	}

	public void setPerf_check_result_div_cd_d(String perf_check_result_div_cd_d) {
		this.perf_check_result_div_cd_d = perf_check_result_div_cd_d;
	}

	public String getProgram_execute_tms() {
		return program_execute_tms;
	}

	public void setProgram_execute_tms(String program_execute_tms) {
		this.program_execute_tms = program_execute_tms;
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

	public String getPerf_check_result_basis_why1() {
		return perf_check_result_basis_why1;
	}

	public void setPerf_check_result_basis_why1(String perf_check_result_basis_why1) {
		this.perf_check_result_basis_why1 = perf_check_result_basis_why1;
	}

	public String getPerf_check_result_basis_why2() {
		return perf_check_result_basis_why2;
	}

	public void setPerf_check_result_basis_why2(String perf_check_result_basis_why2) {
		this.perf_check_result_basis_why2 = perf_check_result_basis_why2;
	}

	public String getExec_plan() {
		return exec_plan;
	}

	public void setExec_plan(String exec_plan) {
		this.exec_plan = exec_plan;
	}

	public String getHidden_program_execute_tms() {
		return hidden_program_execute_tms;
	}

	public void setHidden_program_execute_tms(String hidden_program_execute_tms) {
		this.hidden_program_execute_tms = hidden_program_execute_tms;
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

	public String getException_yn() {
		return exception_yn;
	}

	public void setException_yn(String exception_yn) {
		this.exception_yn = exception_yn;
	}

	public String getIndc_yn_decide_div_cd() {
		return indc_yn_decide_div_cd;
	}

	public void setIndc_yn_decide_div_cd(String indc_yn_decide_div_cd) {
		this.indc_yn_decide_div_cd = indc_yn_decide_div_cd;
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

	public String getPerf_check_meth_nm() {
		return perf_check_meth_nm;
	}

	public void setPerf_check_meth_nm(String perf_check_meth_nm) {
		this.perf_check_meth_nm = perf_check_meth_nm;
	}

	public String getProgram_executer_id() {
		return program_executer_id;
	}

	public void setProgram_executer_id(String program_executer_id) {
		this.program_executer_id = program_executer_id;
	}

	public String getProgram_exec_div_cd() {
		return program_exec_div_cd;
	}

	public void setProgram_exec_div_cd(String program_exec_div_cd) {
		this.program_exec_div_cd = program_exec_div_cd;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getBind_cnt() {
		return bind_cnt;
	}

	public void setBind_cnt(String bind_cnt) {
		this.bind_cnt = bind_cnt;
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

	public String getCompcd() {
		return compcd;
	}

	public void setCompcd(String compcd) {
		this.compcd = compcd;
	}

	public String getCmid() {
		return cmid;
	}

	public void setCmid(String cmid) {
		this.cmid = cmid;
	}

	public String getCmpknm() {
		return cmpknm;
	}

	public void setCmpknm(String cmpknm) {
		this.cmpknm = cmpknm;
	}

	public String getCmdepday() {
		return cmdepday;
	}

	public void setCmdepday(String cmdepday) {
		this.cmdepday = cmdepday;
	}

	public String getCmusrnum() {
		return cmusrnum;
	}

	public void setCmusrnum(String cmusrnum) {
		this.cmusrnum = cmusrnum;
	}

	public String getJobcd() {
		return jobcd;
	}

	public void setJobcd(String jobcd) {
		this.jobcd = jobcd;
	}

	public String getCmcreateday() {
		return cmcreateday;
	}

	public void setCmcreateday(String cmcreateday) {
		this.cmcreateday = cmcreateday;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDeploy_wrkjob_div_cd() {
		return deploy_wrkjob_div_cd;
	}

	public void setDeploy_wrkjob_div_cd(String deploy_wrkjob_div_cd) {
		this.deploy_wrkjob_div_cd = deploy_wrkjob_div_cd;
	}

	public String getWrkjob_div_cd() {
		return wrkjob_div_cd;
	}

	public String getDeploy_check_status_updater_id() {
		return deploy_check_status_updater_id;
	}

	public void setDeploy_check_status_updater_id(String deploy_check_status_updater_id) {
		this.deploy_check_status_updater_id = deploy_check_status_updater_id;
	}

	public String getPerf_check_request_cn_div_cd() {
		return perf_check_request_cn_div_cd;
	}

	public void setPerf_check_request_cn_div_cd(String perf_check_request_cn_div_cd) {
		this.perf_check_request_cn_div_cd = perf_check_request_cn_div_cd;
	}

	public String getAgent_status_div_cd() {
		return agent_status_div_cd;
	}

	public void setAgent_status_div_cd(String agent_status_div_cd) {
		this.agent_status_div_cd = agent_status_div_cd;
	}

	public void setWrkjob_div_cd(String wrkjob_div_cd) {
		this.wrkjob_div_cd = wrkjob_div_cd;
	}

	public String getTop_wrkjob_cd() {
		return top_wrkjob_cd;
	}

	public void setTop_wrkjob_cd(String top_wrkjob_cd) {
		this.top_wrkjob_cd = top_wrkjob_cd;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getSnap_time() {
		return snap_time;
	}

	public void setSnap_time(String snap_time) {
		this.snap_time = snap_time;
	}

	public String getLast_captured() {
		return last_captured;
	}

	public void setLast_captured(String last_captured) {
		this.last_captured = last_captured;
	}

	public String getException_button_enable_yn() {
		return exception_button_enable_yn;
	}

	public void setException_button_enable_yn(String exception_button_enable_yn) {
		this.exception_button_enable_yn = exception_button_enable_yn;
	}

	public String getCheck_result_anc_yn() {
		return check_result_anc_yn;
	}

	public void setCheck_result_anc_yn(String check_result_anc_yn) {
		this.check_result_anc_yn = check_result_anc_yn;
	}

	public String getLast_check_result_anc_dt() {
		return last_check_result_anc_dt;
	}

	public void setLast_check_result_anc_dt(String last_check_result_anc_dt) {
		this.last_check_result_anc_dt = last_check_result_anc_dt;
	}

	public String getCheck_result_anc_cnt() {
		return check_result_anc_cnt;
	}

	public void setCheck_result_anc_cnt(String check_result_anc_cnt) {
		this.check_result_anc_cnt = check_result_anc_cnt;
	}

	public String getSearch_deploy_check_status_cd() {
		return search_deploy_check_status_cd;
	}

	public void setSearch_deploy_check_status_cd(String search_deploy_check_status_cd) {
		this.search_deploy_check_status_cd = search_deploy_check_status_cd;
	}

	public String getSearch_last_perf_check_step_id() {
		return search_last_perf_check_step_id;
	}

	public void setSearch_last_perf_check_step_id(String search_last_perf_check_step_id) {
		this.search_last_perf_check_step_id = search_last_perf_check_step_id;
	}

	public String getImpr_guide() {
		return impr_guide;
	}

	public void setImpr_guide(String impr_guide) {
		this.impr_guide = impr_guide;
	}

	public String getPerf_test_target_step_yn() {
		return perf_test_target_step_yn;
	}

	public void setPerf_test_target_step_yn(String perf_test_target_step_yn) {
		this.perf_test_target_step_yn = perf_test_target_step_yn;
	}

	public String getPerf_check_result_desc() {
		return perf_check_result_desc;
	}

	public void setPerf_check_result_desc(String perf_check_result_desc) {
		this.perf_check_result_desc = perf_check_result_desc;
	}

	public String getPop_perf_check_id() {
		return pop_perf_check_id;
	}

	public void setPop_perf_check_id(String pop_perf_check_id) {
		this.pop_perf_check_id = pop_perf_check_id;
	}

	public String getPop_perf_check_step_id() {
		return pop_perf_check_step_id;
	}

	public void setPop_perf_check_step_id(String pop_perf_check_step_id) {
		this.pop_perf_check_step_id = pop_perf_check_step_id;
	}

	public String getPop_program_id() {
		return pop_program_id;
	}

	public void setPop_program_id(String pop_program_id) {
		this.pop_program_id = pop_program_id;
	}

	public String getPop_program_execute_tms() {
		return pop_program_execute_tms;
	}

	public void setPop_program_execute_tms(String pop_program_execute_tms) {
		this.pop_program_execute_tms = pop_program_execute_tms;
	}

	public String getHidden_bind_var_value() {
		return hidden_bind_var_value;
	}

	public void setHidden_bind_var_value(String hidden_bind_var_value) {
		this.hidden_bind_var_value = hidden_bind_var_value;
	}

	public String getSearch_wrkjob_cd() {
		return search_wrkjob_cd;
	}

	public void setSearch_wrkjob_cd(String search_wrkjob_cd) {
		this.search_wrkjob_cd = search_wrkjob_cd;
	}

	public String getTop_wrkjob_cd_nm() {
		return top_wrkjob_cd_nm;
	}

	public void setTop_wrkjob_cd_nm(String top_wrkjob_cd_nm) {
		this.top_wrkjob_cd_nm = top_wrkjob_cd_nm;
	}

	public String getSearch_wrkjob_cd_nm() {
		return search_wrkjob_cd_nm;
	}

	public void setSearch_wrkjob_cd_nm(String search_wrkjob_cd_nm) {
		this.search_wrkjob_cd_nm = search_wrkjob_cd_nm;
	}

	public String getAuto_skip() {
		return auto_skip;
	}

	public void setAuto_skip(String auto_skip) {
		this.auto_skip = auto_skip;
	}

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public String getFinal_perf_check_step() {
		return final_perf_check_step;
	}

	public void setFinal_perf_check_step(String final_perf_check_step) {
		this.final_perf_check_step = final_perf_check_step;
	}

	public String getException_prc_meth_cd() {
		return exception_prc_meth_cd;
	}

	public void setException_prc_meth_cd(String exception_prc_meth_cd) {
		this.exception_prc_meth_cd = exception_prc_meth_cd;
	}

	public String getException_prc_meth_nm() {
		return exception_prc_meth_nm;
	}

	public void setException_prc_meth_nm(String exception_prc_meth_nm) {
		this.exception_prc_meth_nm = exception_prc_meth_nm;
	}

	public String getProgram_desc() {
		return program_desc;
	}

	public void setProgram_desc(String program_desc) {
		this.program_desc = program_desc;
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

	public String getProgram_type_cd() {
		return program_type_cd;
	}

	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
	}

	public String getProgram_type_nm() {
		return program_type_nm;
	}

	public void setProgram_type_nm(String program_type_nm) {
		this.program_type_nm = program_type_nm;
	}

	public String getSql_command_type_cd() {
		return sql_command_type_cd;
	}

	public void setSql_command_type_cd(String sql_command_type_cd) {
		this.sql_command_type_cd = sql_command_type_cd;
	}

	public String getHidden_sql_command_type_cd() {
		return hidden_sql_command_type_cd;
	}

	public void setHidden_sql_command_type_cd(String hidden_sql_command_type_cd) {
		this.hidden_sql_command_type_cd = hidden_sql_command_type_cd;
	}

	public String getDynamic_sql_yn() {
		return dynamic_sql_yn;
	}

	public void setDynamic_sql_yn(String dynamic_sql_yn) {
		this.dynamic_sql_yn = dynamic_sql_yn;
	}

	public String getSql_command_type_nm() {
		return sql_command_type_nm;
	}

	public void setSql_command_type_nm(String sql_command_type_nm) {
		this.sql_command_type_nm = sql_command_type_nm;
	}

	public String getPagingYn() {
		return pagingYn;
	}

	public void setPagingYn(String pagingYn) {
		this.pagingYn = pagingYn;
	}

	public int getPagingCnt() {
		return pagingCnt;
	}

	public void setPagingCnt(int pagingCnt) {
		this.pagingCnt = pagingCnt;
	}

	public String getHidden_program_type_cd() {
		return hidden_program_type_cd;
	}

	public void setHidden_program_type_cd(String hidden_program_type_cd) {
		this.hidden_program_type_cd = hidden_program_type_cd;
	}

	public String getSearch_deploy_id() {
		return search_deploy_id;
	}

	public void setSearch_deploy_id(String search_deploy_id) {
		this.search_deploy_id = search_deploy_id;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getImproper() {
		return improper;
	}

	public void setImproper(String improper) {
		this.improper = improper;
	}

	public String getPerf_check_complete_meth_cd() {
		return perf_check_complete_meth_cd;
	}

	public void setPerf_check_complete_meth_cd(String perf_check_complete_meth_cd) {
		this.perf_check_complete_meth_cd = perf_check_complete_meth_cd;
	}

	public String getPerf_check_complete_meth_nm() {
		return perf_check_complete_meth_nm;
	}

	public void setPerf_check_complete_meth_nm(String perf_check_complete_meth_nm) {
		this.perf_check_complete_meth_nm = perf_check_complete_meth_nm;
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
	public BigDecimal getCurrent_elap_time() {
		return current_elap_time;
	}
	public String getForecast_result_cnt() {
		return forecast_result_cnt;
	}

	public void setCurrent_elap_time(BigDecimal current_elap_time) {
		this.current_elap_time = current_elap_time;
	}

	public void setForecast_result_cnt(String forecast_result_cnt) {
		this.forecast_result_cnt = forecast_result_cnt;
	}

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getCheck_tgt_cnt() {
		return check_tgt_cnt;
	}

	public void setCheck_tgt_cnt(String check_tgt_cnt) {
		this.check_tgt_cnt = check_tgt_cnt;
	}

	public String getBind_var() {
		return bind_var;
	}

	public void setBind_var(String bind_var) {
		this.bind_var = bind_var;
	}

	public String getProgram_source_desc2() {
		return program_source_desc2;
	}

	public void setProgram_source_desc2(String program_source_desc2) {
		this.program_source_desc2 = program_source_desc2;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

}
