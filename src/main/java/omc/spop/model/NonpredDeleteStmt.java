package omc.spop.model;

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

/***********************************************************
 * 2017.09.28	이원식	최초작성
 **********************************************************/

@Alias("nonpredDeleteStmt")
public class NonpredDeleteStmt extends Base implements Jsonable {
    
    private String sql_id;
    private String plan_hash_value;
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
