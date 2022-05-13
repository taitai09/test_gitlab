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

/***********************************************************
 * 2017.10.13	이원식	최초작성
 **********************************************************/

@Alias("topsqlHandopChoice")
public class TopsqlHandopChoice extends Base implements Jsonable {
    private String elapsed_time;
    private String buffer_gets;
    private String selectOrdered;
    private String choice_tms;
    private String choice_id;
    private String handop_type_div_cd;
    private String choice_dt;
    private String choice_cnt;
    private String start_snap_id;
    private String end_snap_id;
    private String perfr_auto_assign_yn;
    private String perfr_id;
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
    private String executions;
    private String module;
    
    private String excpt_module_list;
    private String excpt_parsing_schema_name_list;
    private String excpt_sql_id_list;
    
    private String project_nm;
    private String tuning_prgrs_step_nm;
    private String project_id;
    private String tuning_prgrs_step_seq;
    
    private String extra_filter_predication;
    
	public String getExtra_filter_predication() {
		return extra_filter_predication;
	}
	public void setExtra_filter_predication(String extra_filter_predication) {
		this.extra_filter_predication = extra_filter_predication;
	}
	public String getProject_nm() {
		return project_nm;
	}
	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
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
	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}
	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}
	public String getExcpt_module_list() {
		return excpt_module_list;
	}
	public void setExcpt_module_list(String excpt_module_list) {
		this.excpt_module_list = excpt_module_list;
	}
	public String getExcpt_parsing_schema_name_list() {
		return excpt_parsing_schema_name_list;
	}
	public void setExcpt_parsing_schema_name_list(String excpt_parsing_schema_name_list) {
		this.excpt_parsing_schema_name_list = excpt_parsing_schema_name_list;
	}
	public String getExcpt_sql_id_list() {
		return excpt_sql_id_list;
	}
	public void setExcpt_sql_id_list(String excpt_sql_id_list) {
		this.excpt_sql_id_list = excpt_sql_id_list;
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
	public String getSelectOrdered() {
		return selectOrdered;
	}
	public void setSelectOrdered(String selectOrdered) {
		this.selectOrdered = selectOrdered;
	}
	public String getChoice_tms() {
		return choice_tms;
	}
	public void setChoice_tms(String choice_tms) {
		this.choice_tms = choice_tms;
	}
	public String getChoice_id() {
		return choice_id;
	}
	public void setChoice_id(String choice_id) {
		this.choice_id = choice_id;
	}
	public String getHandop_type_div_cd() {
		return handop_type_div_cd;
	}
	public void setHandop_type_div_cd(String handop_type_div_cd) {
		this.handop_type_div_cd = handop_type_div_cd;
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
	public String getExecutions() {
		return executions;
	}
	public void setExecutions(String executions) {
		this.executions = executions;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
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
	
}