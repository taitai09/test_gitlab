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

@Alias("planCompareResult")
public class PlanCompareResult extends Base implements Jsonable {
	/* 파라미터 받을 값 */
	private String dbid;
	private String sql_id;
	private String project_id;
	private String plan_hash_value;
	private String sql_command_type_cd;				// SELECT, DML
	private String sql_auto_perf_check_id;
	private String perf_check_sql_source_type_cd;	// 1:AWR 2:전체
	private String tobe_executions;
	
	private String asis_plan_hash_value;
	private String tobe_plan_hash_value;
	
	/* 화면으로 리턴해줄 값 */
	String originLine;
	String newLine;
	String tag;
	
	public String getTobe_plan_hash_value() {
		return tobe_plan_hash_value;
	}
	public void setTobe_plan_hash_value(String tobe_plan_hash_value) {
		this.tobe_plan_hash_value = tobe_plan_hash_value;
	}
	public String getTobe_executions() {
		return tobe_executions;
	}
	public void setTobe_executions(String tobe_executions) {
		this.tobe_executions = tobe_executions;
	}
	public String getAsis_plan_hash_value() {
		return asis_plan_hash_value;
	}
	public void setAsis_plan_hash_value(String asis_plan_hash_value) {
		this.asis_plan_hash_value = asis_plan_hash_value;
	}
	public String getDbid() {
		return dbid;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getPlan_hash_value() {
		return plan_hash_value;
	}
	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}
	public String getSql_command_type_cd() {
		return sql_command_type_cd;
	}
	public void setSql_command_type_cd(String sql_command_type_cd) {
		this.sql_command_type_cd = sql_command_type_cd;
	}
	public String getSql_auto_perf_check_id() {
		return sql_auto_perf_check_id;
	}
	public void setSql_auto_perf_check_id(String sql_auto_perf_check_id) {
		this.sql_auto_perf_check_id = sql_auto_perf_check_id;
	}
	public String getPerf_check_sql_source_type_cd() {
		return perf_check_sql_source_type_cd;
	}
	public void setPerf_check_sql_source_type_cd(String perf_check_sql_source_type_cd) {
		this.perf_check_sql_source_type_cd = perf_check_sql_source_type_cd;
	}
	public void setOriginLine(String originLine) {
		this.originLine = originLine;
	}
	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}
	public String getOriginLine() {
		return originLine;
	}
	public String getNewLine() {
		return newLine;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
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
}
