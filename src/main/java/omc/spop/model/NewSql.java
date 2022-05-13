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
 * 2017.09.29	이원식	최초작성
 **********************************************************/

@Alias("newSql")
public class NewSql extends Base implements Jsonable {
    
    private String sql_id;
    private String plan_hash_value;
    private String module;
    private String parsing_schema_name;
    private float avg_elapsed_time;
    private float max_elapsed_time;
    private float avg_cpu_time;
    private float avg_buffer_gets;
    private float avg_disk_reads;
    private float avg_rows_processed;
    private float executions;
    private String first_load_time;
    private String last_load_time;    
    private String tuning_tgt_yn;
    private String tuning_tgt_dt;
    private String tuning_tgt_id;
    
    private String choice_div_cd;
    private String choice_div_cd_nm;
    private String tuning_requester_id;
	private String tuning_requester_nm;
	private String tuning_requester_wrkjob_cd;
	private String tuning_requester_wrkjob_nm;
	private String tuning_requester_tel_num;    
	private String tuning_request_dt;
	private String wrkjob_mgr_nm;
	private String wrkjob_mgr_wrkjob_cd;
	private String wrkjob_mgr_wrkjob_nm;
	private String wrkjob_mgr_tel_num;
	private String tr_cd;
	private String dbio;
	private String sql_text;
	
	private String elapsed_time;
	private String buffer_gets;
	
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
	public float getAvg_elapsed_time() {
		return avg_elapsed_time;
	}
	public void setAvg_elapsed_time(float avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}
	public float getMax_elapsed_time() {
		return max_elapsed_time;
	}
	public void setMax_elapsed_time(float max_elapsed_time) {
		this.max_elapsed_time = max_elapsed_time;
	}
	public float getAvg_cpu_time() {
		return avg_cpu_time;
	}
	public void setAvg_cpu_time(float avg_cpu_time) {
		this.avg_cpu_time = avg_cpu_time;
	}
	public float getAvg_buffer_gets() {
		return avg_buffer_gets;
	}
	public void setAvg_buffer_gets(float avg_buffer_gets) {
		this.avg_buffer_gets = avg_buffer_gets;
	}
	public float getAvg_disk_reads() {
		return avg_disk_reads;
	}
	public void setAvg_disk_reads(float avg_disk_reads) {
		this.avg_disk_reads = avg_disk_reads;
	}
	public float getAvg_rows_processed() {
		return avg_rows_processed;
	}
	public void setAvg_rows_processed(float avg_rows_processed) {
		this.avg_rows_processed = avg_rows_processed;
	}
	public float getExecutions() {
		return executions;
	}
	public void setExecutions(float executions) {
		this.executions = executions;
	}
	public String getFirst_load_time() {
		return first_load_time;
	}
	public void setFirst_load_time(String first_load_time) {
		this.first_load_time = first_load_time;
	}
	public String getLast_load_time() {
		return last_load_time;
	}
	public void setLast_load_time(String last_load_time) {
		this.last_load_time = last_load_time;
	}	
	public String getTuning_tgt_yn() {
		return tuning_tgt_yn;
	}
	public void setTuning_tgt_yn(String tuning_tgt_yn) {
		this.tuning_tgt_yn = tuning_tgt_yn;
	}
	public String getTuning_tgt_dt() {
		return tuning_tgt_dt;
	}
	public void setTuning_tgt_dt(String tuning_tgt_dt) {
		this.tuning_tgt_dt = tuning_tgt_dt;
	}
	public String getTuning_tgt_id() {
		return tuning_tgt_id;
	}
	public void setTuning_tgt_id(String tuning_tgt_id) {
		this.tuning_tgt_id = tuning_tgt_id;
	}	
	public String getChoice_div_cd() {
		return choice_div_cd;
	}
	public void setChoice_div_cd(String choice_div_cd) {
		this.choice_div_cd = choice_div_cd;
	}
	public String getChoice_div_cd_nm() {
		return choice_div_cd_nm;
	}
	public void setChoice_div_cd_nm(String choice_div_cd_nm) {
		this.choice_div_cd_nm = choice_div_cd_nm;
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
	public void setTuning_requester_wrkjob_nm(String tuning_requester_wrkjob_nm) {
		this.tuning_requester_wrkjob_nm = tuning_requester_wrkjob_nm;
	}
	public String getTuning_requester_tel_num() {
		return tuning_requester_tel_num;
	}
	public void setTuning_requester_tel_num(String tuning_requester_tel_num) {
		this.tuning_requester_tel_num = tuning_requester_tel_num;
	}
	public String getTuning_request_dt() {
		return tuning_request_dt;
	}
	public void setTuning_request_dt(String tuning_request_dt) {
		this.tuning_request_dt = tuning_request_dt;
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
	public String getTr_cd() {
		return tr_cd;
	}
	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}
	public String getDbio() {
		return dbio;
	}
	public void setDbio(String dbio) {
		this.dbio = dbio;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
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
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//
//		objJson.put("dbid",this.getDbid());
//		objJson.put("db_name",this.getDb_name());
//		objJson.put("tuning_no",this.getTuning_no());
//		objJson.put("gather_day",this.getGather_day());
//		objJson.put("sql_id",this.getSql_id());
//		objJson.put("plan_hash_value",this.getPlan_hash_value());
//		objJson.put("module",this.getModule());
//		objJson.put("parsing_schema_name",this.getParsing_schema_name());
//		objJson.put("avg_elapsed_time",StringUtil.parseFloat(this.getAvg_elapsed_time(),0));
//		objJson.put("max_elapsed_time",StringUtil.parseFloat(this.getMax_elapsed_time(),0));
//		objJson.put("avg_cpu_time",StringUtil.parseFloat(this.getAvg_cpu_time(),0));
//		objJson.put("avg_buffer_gets",StringUtil.parseFloat(this.getAvg_buffer_gets(),0));
//		objJson.put("avg_disk_reads",StringUtil.parseFloat(this.getAvg_disk_reads(),0));
//		objJson.put("avg_rows_processed",StringUtil.parseFloat(this.getAvg_rows_processed(),0));
//		objJson.put("executions",StringUtil.parseFloat(this.getExecutions(),0));
//		objJson.put("first_load_time",this.getFirst_load_time());
//		objJson.put("last_load_time",this.getLast_load_time());
//		objJson.put("tuning_tgt_yn",this.getTuning_tgt_yn());
//		objJson.put("tuning_tgt_dt",this.getTuning_tgt_dt());
//		objJson.put("tuning_tgt_id",this.getTuning_tgt_id());
//		objJson.put("choice_div_cd",this.getChoice_div_cd());
//		objJson.put("choice_div_cd_nm",this.getChoice_div_cd_nm());
//		objJson.put("tuning_requester_id",this.getTuning_requester_id());
//		objJson.put("tuning_requester_nm",this.getTuning_requester_nm());
//		objJson.put("tuning_requester_wrkjob_cd",this.getTuning_requester_wrkjob_cd());
//		objJson.put("tuning_requester_wrkjob_nm",this.getTuning_requester_wrkjob_nm());
//		objJson.put("tuning_requester_tel_num",this.getTuning_requester_tel_num());
//		objJson.put("tuning_request_dt",this.getTuning_request_dt());
//		objJson.put("wrkjob_mgr_id",this.getWrkjob_mgr_id());
//		objJson.put("wrkjob_mgr_nm",this.getWrkjob_mgr_nm());
//		objJson.put("wrkjob_mgr_wrkjob_cd",this.getWrkjob_mgr_wrkjob_cd());
//		objJson.put("wrkjob_mgr_wrkjob_nm",this.getWrkjob_mgr_wrkjob_nm());
//		objJson.put("wrkjob_mgr_tel_num",this.getWrkjob_mgr_tel_num());
//		objJson.put("tr_cd",this.getTr_cd());
//		objJson.put("dbio",this.getDbio());
//		objJson.put("sql_text",this.getSql_text());
//		
//		return objJson;
//	}		
	
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