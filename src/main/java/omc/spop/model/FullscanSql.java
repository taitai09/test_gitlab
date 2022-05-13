package omc.spop.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.29	이원식	최초작성
 **********************************************************/

@Alias("fullscanSql")
public class FullscanSql extends Base implements Jsonable {
    
    private String sql_id;
    private String plan_hash_value;
    private String operations;
    private String parsing_schema_name;
    private String module;
    private String action;
    private String executions;
    private String elapsed_time;
    private String cpu_time;
    private String rows_processed;
    private String disk_reads;
    private String buffer_gets;
    private String sql_text;
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
	private String exception_list;
	
	
	public String getException_list() {
		return exception_list;
	}
	public void setException_list(String exception_list) {
		this.exception_list = exception_list;
	}
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
	public String getOperations() {
		return operations;
	}
	public void setOperations(String operations) {
		this.operations = operations;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getExecutions() {
		return executions;
	}
	public void setExecutions(String executions) {
		this.executions = executions;
	}
	public String getElapsed_time() {
		return elapsed_time;
	}
	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	public String getCpu_time() {
		return cpu_time;
	}
	public void setCpu_time(String cpu_time) {
		this.cpu_time = cpu_time;
	}
	public String getRows_processed() {
		return rows_processed;
	}
	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}
	public String getDisk_reads() {
		return disk_reads;
	}
	public void setDisk_reads(String disk_reads) {
		this.disk_reads = disk_reads;
	}
	public String getBuffer_gets() {
		return buffer_gets;
	}
	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
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
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("gather_day",this.getGather_day());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("plan_hash_value",this.getPlan_hash_value());
		objJson.put("operations",this.getOperations());
		objJson.put("parsing_schema_name",this.getParsing_schema_name());
		objJson.put("module",this.getModule());
		objJson.put("action",this.getAction());
		objJson.put("executions",StringUtil.parseFloat(this.getExecutions(),0));
		objJson.put("elapsed_time",StringUtil.parseFloat(this.getElapsed_time(),0));
		objJson.put("cpu_time",StringUtil.parseFloat(this.getCpu_time(),0));
		objJson.put("rows_processed",StringUtil.parseFloat(this.getRows_processed(),0));
		objJson.put("disk_reads",StringUtil.parseFloat(this.getDisk_reads(),0));
		objJson.put("buffer_gets",StringUtil.parseFloat(this.getBuffer_gets(),0));
		objJson.put("sql_text",this.getSql_text());
		objJson.put("tuning_tgt_yn",this.getTuning_tgt_yn());
		objJson.put("tuning_tgt_dt",this.getTuning_tgt_dt());
		objJson.put("tuning_tgt_id",this.getTuning_tgt_id());
		objJson.put("choice_div_cd",this.getChoice_div_cd());
		objJson.put("choice_div_cd_nm",this.getChoice_div_cd_nm());		
		objJson.put("tuning_requester_id",this.getTuning_requester_id());
		objJson.put("tuning_requester_nm",this.getTuning_requester_nm());
		objJson.put("tuning_requester_wrkjob_cd",this.getTuning_requester_wrkjob_cd());
		objJson.put("tuning_requester_wrkjob_nm",this.getTuning_requester_wrkjob_nm());
		objJson.put("tuning_requester_tel_num",this.getTuning_requester_tel_num());
		objJson.put("tuning_request_dt",this.getTuning_request_dt());
		objJson.put("wrkjob_mgr_id",this.getWrkjob_mgr_id());
		objJson.put("wrkjob_mgr_nm",this.getWrkjob_mgr_nm());
		objJson.put("wrkjob_mgr_wrkjob_cd",this.getWrkjob_mgr_wrkjob_cd());
		objJson.put("wrkjob_mgr_wrkjob_nm",this.getWrkjob_mgr_wrkjob_nm());
		objJson.put("wrkjob_mgr_tel_num",this.getWrkjob_mgr_tel_num());
		objJson.put("tr_cd",this.getTr_cd());
		objJson.put("dbio",this.getDbio());
		
		return objJson;
	}		
	
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//        // object -> Map
//        ObjectMapper oMapper = new ObjectMapper();
//        Map<String, Object> map = oMapper.convertValue(this, Map.class);
//        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
//        String strJson = gson.toJson(map);
//        try {
//			objJson = (JSONObject) new JSONParser().parse(strJson);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return objJson;
//	}
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}