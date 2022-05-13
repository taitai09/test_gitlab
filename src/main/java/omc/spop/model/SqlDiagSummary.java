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
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.11	이원식	최초작성
 **********************************************************/

@Alias("sqlDiagSummary")
public class SqlDiagSummary extends Base implements Jsonable {
	
    private String day_gubun;
    
    private String day_name;
    private String min_day;
    private String max_day;
    private String sql_diag_type_cd;
    private String sql_diag_type_nm;
    private String diag_cnt="0";
    private String plan_change_sql="0";
    private String new_sql="0";
    private String literal_sql_text="0";
    private String literal_plan_hash_value="0";
    private String temp_usage_sql="0";
    private String fullscan_sql="0";
    private String delete_sql="0";

    private String topsql="0";
    private String offload_effc_sql="0";
    private String offload_effc_reduce_sql="0";
    
	public String getTopsql() {
		return topsql;
	}
	public void setTopsql(String topsql) {
		this.topsql = topsql;
	}
	public String getOffload_effc_sql() {
		return offload_effc_sql;
	}
	public void setOffload_effc_sql(String offload_effc_sql) {
		this.offload_effc_sql = offload_effc_sql;
	}
	public String getOffload_effc_reduce_sql() {
		return offload_effc_reduce_sql;
	}
	public void setOffload_effc_reduce_sql(String offload_effc_reduce_sql) {
		this.offload_effc_reduce_sql = offload_effc_reduce_sql;
	}
	public String getDay_gubun() {
		return day_gubun;
	}
	public void setDay_gubun(String day_gubun) {
		this.day_gubun = day_gubun;
	}
	public String getDay_name() {
		return day_name;
	}
	public void setDay_name(String day_name) {
		this.day_name = day_name;
	}
	public String getMin_day() {
		return min_day;
	}
	public void setMin_day(String min_day) {
		this.min_day = min_day;
	}
	public String getMax_day() {
		return max_day;
	}
	public void setMax_day(String max_day) {
		this.max_day = max_day;
	}
	public String getSql_diag_type_cd() {
		return sql_diag_type_cd;
	}
	public void setSql_diag_type_cd(String sql_diag_type_cd) {
		this.sql_diag_type_cd = sql_diag_type_cd;
	}
	public String getSql_diag_type_nm() {
		return sql_diag_type_nm;
	}
	public void setSql_diag_type_nm(String sql_diag_type_nm) {
		this.sql_diag_type_nm = sql_diag_type_nm;
	}
	public String getDiag_cnt() {
		return diag_cnt;
	}
	public void setDiag_cnt(String diag_cnt) {
		this.diag_cnt = diag_cnt;
	}
	public String getPlan_change_sql() {
		return plan_change_sql;
	}
	public void setPlan_change_sql(String plan_change_sql) {
		this.plan_change_sql = plan_change_sql;
	}
	public String getNew_sql() {
		return new_sql;
	}
	public void setNew_sql(String new_sql) {
		this.new_sql = new_sql;
	}
	public String getLiteral_sql_text() {
		return literal_sql_text;
	}
	public void setLiteral_sql_text(String literal_sql_text) {
		this.literal_sql_text = literal_sql_text;
	}
	public String getLiteral_plan_hash_value() {
		return literal_plan_hash_value;
	}
	public void setLiteral_plan_hash_value(String literal_plan_hash_value) {
		this.literal_plan_hash_value = literal_plan_hash_value;
	}
	public String getTemp_usage_sql() {
		return temp_usage_sql;
	}
	public void setTemp_usage_sql(String temp_usage_sql) {
		this.temp_usage_sql = temp_usage_sql;
	}
	public String getFullscan_sql() {
		return fullscan_sql;
	}
	public void setFullscan_sql(String fullscan_sql) {
		this.fullscan_sql = fullscan_sql;
	}
	public String getDelete_sql() {
		return delete_sql;
	}
	public void setDelete_sql(String delete_sql) {
		this.delete_sql = delete_sql;
	}	
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//
//		objJson.put("dbid",this.getDbid());
//		objJson.put("db_name",this.getDb_name());
//		objJson.put("day_gubun",this.getDay_gubun());
//		objJson.put("gather_day",this.getGather_day());
//		objJson.put("day_name",this.getDay_name());
//		objJson.put("min_day",this.getMin_day());
//		objJson.put("max_day",this.getMax_day());
//		objJson.put("sql_diag_type_cd",this.getSql_diag_type_cd());
//		objJson.put("sql_diag_type_nm",this.getSql_diag_type_nm());
//		objJson.put("diag_cnt",StringUtil.parseDouble(this.getDiag_cnt(),0));
//		objJson.put("plan_change_sql",StringUtil.parseDouble(this.getPlan_change_sql(),0));
//		objJson.put("new_sql",StringUtil.parseDouble(this.getNew_sql(),0));
//		objJson.put("literal_sql_text",StringUtil.parseDouble(this.getLiteral_sql_text(),0));
//		objJson.put("literal_plan_hash_value",StringUtil.parseDouble(this.getLiteral_plan_hash_value(),0));
//		objJson.put("temp_usage_sql",StringUtil.parseDouble(this.getTemp_usage_sql(),0));
//		objJson.put("fullscan_sql",StringUtil.parseDouble(this.getFullscan_sql(),0));
//		objJson.put("delete_sql",StringUtil.parseDouble(this.getDelete_sql(),0));
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
}