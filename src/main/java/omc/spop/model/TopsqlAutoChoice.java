package omc.spop.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.13	이원식	최초작성
 * 2018.05.30	이원식	순번(AUTO_CHOICE_COND_NO) PK 변경
 **********************************************************/

@Alias("topsqlAutoChoice")
public class TopsqlAutoChoice extends Base implements Jsonable {
	
	private String auto_choice_cond_no;
	private String select_auto_choice_cond_no;
	private String auto_choice_cond_no_temp;
    private String choice_tms;
    private String choice_dt;
    private String choice_cnt;
    private String cb_perfr_id_detail;
    private String perfr_auto_assign_yn;
    private String perfr_id;
    private String perfr_nm;
    private String gather_cycle_div_cd;
    private String gather_cycle_div_nm;
    private String gather_range_div_cd;
    private String gather_range_div_nm;
    private String choice_start_day;
    private String choice_end_day;
    private String before_choice_sql_except_yn;
    private String before_tuning_sql_except_yn;
    private String elap_time;
    private String buffer_cnt;
    private String exec_cnt;
    private String topn_cnt;
    private String order_div_cd;
    private String order_div_nm;
    private String module1;
    private String module2;
    private String parsing_schema_name;
    private String sql_text;
    private String choicer_id;
    private String choicer_nm;
    private String update_id;
    private String update_nm;
    private String update_dt;
    
    private String program_type_cd;
    private String program_type_cd_nm;
    private String appl_filter_yn;
    private String del_yn;
    private String use_yn;

    private String project_id;
    private String project_nm;
    private String tuning_prgrs_step_seq;
    private String tuning_prgrs_step_nm;
    private String autoChoiceCondNoArray;
    
	public String getAuto_choice_cond_no_temp() {
		return auto_choice_cond_no_temp;
	}
	public void setAuto_choice_cond_no_temp(String auto_choice_cond_no_temp) {
		this.auto_choice_cond_no_temp = auto_choice_cond_no_temp;
	}
	public String getSelect_auto_choice_cond_no() {
		return select_auto_choice_cond_no;
	}
	public void setSelect_auto_choice_cond_no(String select_auto_choice_cond_no) {
		this.select_auto_choice_cond_no = select_auto_choice_cond_no;
	}
	public String getChoice_tms() {
		return choice_tms;
	}
	public void setChoice_tms(String choice_tms) {
		this.choice_tms = choice_tms;
	}
	public String getChoice_dt() {
		return choice_dt;
	}
	public void setChoice_dt(String choice_dt) {
		this.choice_dt = choice_dt;
	}
	public String getChoice_cnt() {
		return choice_cnt;
	}
	public void setChoice_cnt(String choice_cnt) {
		this.choice_cnt = choice_cnt;
	}
	public String getCb_perfr_id_detail() {
		return cb_perfr_id_detail;
	}
	public void setCb_perfr_id_detail(String cb_perfr_id_detail) {
		this.cb_perfr_id_detail = cb_perfr_id_detail;
	}
	public String getAuto_choice_cond_no() {
		return auto_choice_cond_no;
	}
	public void setAuto_choice_cond_no(String auto_choice_cond_no) {
		this.auto_choice_cond_no = auto_choice_cond_no;
	}
	public String getPerfr_auto_assign_yn() {
		return perfr_auto_assign_yn;
	}
	public void setPerfr_auto_assign_yn(String perfr_auto_assign_yn) {
		this.perfr_auto_assign_yn = perfr_auto_assign_yn;
	}
	public String getPerfr_id() {
		return perfr_id;
	}
	public void setPerfr_id(String perfr_id) {
		this.perfr_id = perfr_id;
	}
	public String getPerfr_nm() {
		return perfr_nm;
	}
	public void setPerfr_nm(String perfr_nm) {
		this.perfr_nm = perfr_nm;
	}
	public String getGather_cycle_div_cd() {
		return gather_cycle_div_cd;
	}
	public void setGather_cycle_div_cd(String gather_cycle_div_cd) {
		this.gather_cycle_div_cd = gather_cycle_div_cd;
	}
	public String getGather_cycle_div_nm() {
		return gather_cycle_div_nm;
	}
	public void setGather_cycle_div_nm(String gather_cycle_div_nm) {
		this.gather_cycle_div_nm = gather_cycle_div_nm;
	}
	public String getGather_range_div_cd() {
		return gather_range_div_cd;
	}
	public void setGather_range_div_cd(String gather_range_div_cd) {
		this.gather_range_div_cd = gather_range_div_cd;
	}
	public String getGather_range_div_nm() {
		return gather_range_div_nm;
	}
	public void setGather_range_div_nm(String gather_range_div_nm) {
		this.gather_range_div_nm = gather_range_div_nm;
	}
	public String getChoice_start_day() {
		return choice_start_day;
	}
	public void setChoice_start_day(String choice_start_day) {
		this.choice_start_day = choice_start_day;
	}
	public String getChoice_end_day() {
		return choice_end_day;
	}
	public void setChoice_end_day(String choice_end_day) {
		this.choice_end_day = choice_end_day;
	}
	public String getBefore_choice_sql_except_yn() {
		return before_choice_sql_except_yn;
	}
	public void setBefore_choice_sql_except_yn(String before_choice_sql_except_yn) {
		this.before_choice_sql_except_yn = before_choice_sql_except_yn;
	}
	public String getBefore_tuning_sql_except_yn() {
		return before_tuning_sql_except_yn;
	}
	public void setBefore_tuning_sql_except_yn(String before_tuning_sql_except_yn) {
		this.before_tuning_sql_except_yn = before_tuning_sql_except_yn;
	}
	public String getElap_time() {
		return elap_time;
	}
	public void setElap_time(String elap_time) {
		this.elap_time = elap_time;
	}
	public String getBuffer_cnt() {
		return buffer_cnt;
	}
	public void setBuffer_cnt(String buffer_cnt) {
		this.buffer_cnt = buffer_cnt;
	}
	public String getExec_cnt() {
		return exec_cnt;
	}
	public void setExec_cnt(String exec_cnt) {
		this.exec_cnt = exec_cnt;
	}
	public String getTopn_cnt() {
		return topn_cnt;
	}
	public void setTopn_cnt(String topn_cnt) {
		this.topn_cnt = topn_cnt;
	}
	public String getOrder_div_cd() {
		return order_div_cd;
	}
	public void setOrder_div_cd(String order_div_cd) {
		this.order_div_cd = order_div_cd;
	}
	public String getOrder_div_nm() {
		return order_div_nm;
	}
	public void setOrder_div_nm(String order_div_nm) {
		this.order_div_nm = order_div_nm;
	}
	public String getModule1() {
		return module1;
	}
	public void setModule1(String module1) {
		this.module1 = module1;
	}
	public String getModule2() {
		return module2;
	}
	public void setModule2(String module2) {
		this.module2 = module2;
	}
	public String getParsing_schema_name() {
		return parsing_schema_name;
	}
	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}	
	public String getChoicer_id() {
		return choicer_id;
	}
	public void setChoicer_id(String choicer_id) {
		this.choicer_id = choicer_id;
	}	
	public String getChoicer_nm() {
		return choicer_nm;
	}
	public void setChoicer_nm(String choicer_nm) {
		this.choicer_nm = choicer_nm;
	}
	public String getUpdate_id() {
		return update_id;
	}
	public void setUpdate_id(String update_id) {
		this.update_id = update_id;
	}
	public String getUpdate_nm() {
		return update_nm;
	}
	public void setUpdate_nm(String update_nm) {
		this.update_nm = update_nm;
	}
	public String getUpdate_dt() {
		return update_dt;
	}
	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}	
	public String getProgram_type_cd() {
		return program_type_cd;
	}
	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
	}
	public String getProgram_type_cd_nm() {
		return program_type_cd_nm;
	}
	public void setProgram_type_cd_nm(String program_type_cd_nm) {
		this.program_type_cd_nm = program_type_cd_nm;
	}
	public String getAppl_filter_yn() {
		return appl_filter_yn;
	}
	public void setAppl_filter_yn(String appl_filter_yn) {
		this.appl_filter_yn = appl_filter_yn;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	
	public String getProject_id() {
		return project_id;
	}
	public String getProject_nm() {
		return project_nm;
	}
	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}
	public String getTuning_prgrs_step_nm() {
		return tuning_prgrs_step_nm;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}
	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}
	public void setTuning_prgrs_step_nm(String tuning_prgrs_step_nm) {
		this.tuning_prgrs_step_nm = tuning_prgrs_step_nm;
	}
	public String getAutoChoiceCondNoArray() {
		return autoChoiceCondNoArray;
	}
	public void setAutoChoiceCondNoArray(String autoChoiceCondNoArray) {
		this.autoChoiceCondNoArray = autoChoiceCondNoArray;
	}
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		//objJson.put("auto_choice_cond_no",StringUtil.parseDouble(this.getAuto_choice_cond_no(),0));
		objJson.put("auto_choice_cond_no",this.getAuto_choice_cond_no());
		
		objJson.put("select_auto_choice_cond_no",this.getSelect_auto_choice_cond_no());
		objJson.put("auto_choice_cond_no_temp",this.getAuto_choice_cond_no_temp());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("choice_tms",this.getChoice_tms());
		objJson.put("choice_dt",this.getChoice_dt());
		objJson.put("choice_cnt",this.getChoice_cnt());
		objJson.put("cb_perfr_id_detail", this.getCb_perfr_id_detail());
		objJson.put("perfr_auto_assign_yn",this.getPerfr_auto_assign_yn());
		objJson.put("perfr_id",this.getPerfr_id());
		objJson.put("perfr_nm",this.getPerfr_nm());
		objJson.put("gather_cycle_div_cd",this.getGather_cycle_div_cd());
		objJson.put("gather_cycle_div_nm",this.getGather_cycle_div_nm());
		objJson.put("gather_range_div_cd",this.getGather_range_div_cd());
		objJson.put("gather_range_div_nm",this.getGather_range_div_nm());
		objJson.put("choice_start_day",this.getChoice_start_day());
		objJson.put("choice_end_day",this.getChoice_end_day());
		objJson.put("before_choice_sql_except_yn",this.getBefore_choice_sql_except_yn());
		objJson.put("before_tuning_sql_except_yn",this.getBefore_tuning_sql_except_yn());
		objJson.put("elap_time",StringUtil.parseFloat(this.getElap_time(),0));
		objJson.put("buffer_cnt",StringUtil.parseFloat(this.getBuffer_cnt(),0));
		objJson.put("exec_cnt",StringUtil.parseFloat(this.getExec_cnt(),0));
		objJson.put("topn_cnt",StringUtil.parseFloat(this.getTopn_cnt(),0));
		objJson.put("order_div_cd",this.getOrder_div_cd());
		objJson.put("order_div_nm",this.getOrder_div_nm());
		objJson.put("module1",this.getModule1());
		objJson.put("module2",this.getModule2());
		objJson.put("parsing_schema_name",this.getParsing_schema_name());
		objJson.put("sql_text",this.getSql_text());
		objJson.put("choicer_id",this.getChoicer_id());
		objJson.put("choicer_nm",this.getChoicer_nm());
		objJson.put("update_id",this.getUpdate_id());
		objJson.put("update_nm",this.getUpdate_nm());
		objJson.put("update_dt",this.getUpdate_dt());
		
		objJson.put("program_type_cd",this.getProgram_type_cd());
		objJson.put("program_type_cd_nm",this.getProgram_type_cd_nm());
		objJson.put("appl_filter_yn",this.getAppl_filter_yn());
		objJson.put("del_yn",this.getDel_yn());
		objJson.put("use_yn",this.getUse_yn());
		
		objJson.put("project_id",this.getProject_id());
		objJson.put("project_nm",this.getProject_nm());
		objJson.put("tuning_prgrs_step_seq",this.getTuning_prgrs_step_seq());
		objJson.put("tuning_prgrs_step_nm",this.getTuning_prgrs_step_nm());
		objJson.put("autoChoiceCondNoArray",this.getAutoChoiceCondNoArray());
		
		return objJson;
	}
}