package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

@Alias("vsqlText")
public class VsqlText extends Base implements Jsonable {
	
	private String owner;
	private String exec_seq;
	private String table_name;
	private String access_path;
	private String sql_id;
	private String plan_hash_value;
	private String sql_text;
	private String sql_fulltext;
	private String snap_no;
	private String query_seq;
	private String rnum;
	
	public String getRnum() {
		return rnum;
	}
	public void setRnum(String rnum) {
		this.rnum = rnum;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getExec_seq() {
		return exec_seq;
	}
	public void setExec_seq(String exec_seq) {
		this.exec_seq = exec_seq;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getAccess_path() {
		return access_path;
	}
	public void setAccess_path(String access_path) {
		this.access_path = access_path;
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
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}
	public String getSql_fulltext() {
		return sql_fulltext;
	}
	public void setSql_fulltext(String sql_fulltext) {
		this.sql_fulltext = sql_fulltext;
	}
	public String getSnap_no() {
		return snap_no;
	}
	public void setSnap_no(String snap_no) {
		this.snap_no = snap_no;
	}
	public String getQuery_seq() {
		return query_seq;
	}
	public void setQuery_seq(String query_seq) {
		this.query_seq = query_seq;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("rnum",this.getRnum());
		objJson.put("dbid",this.getDbid());
		objJson.put("owner",this.getOwner());
		objJson.put("exec_seq",this.getExec_seq());
		objJson.put("table_name",this.getTable_name());
		objJson.put("access_path",this.getAccess_path());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("plan_hash_value",this.getPlan_hash_value());
		objJson.put("sql_text",this.getSql_text());
		objJson.put("sql_fulltext",this.getSql_fulltext());
		objJson.put("exec_seq",this.getExec_seq());
		objJson.put("snap_no",this.getSnap_no());
		objJson.put("query_seq",this.getQuery_seq());
		
		return objJson;
	}		
}