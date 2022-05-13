package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.16	이원식	최초작성
 **********************************************************/

@Alias("vsqlPlan")
public class VsqlPlan extends Base implements Jsonable {
	
    private String id;
    private String pid;
    private String level;
    private String statement_id;
    private String snap_no;
    private String query_seq;
    private String query_output;
    
    private String sql_id;
    private String plan_hash_value;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatement_id() {
		return statement_id;
	}
	public void setStatement_id(String statement_id) {
		this.statement_id = statement_id;
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
	public String getQuery_output() {
		return query_output;
	}
	public void setQuery_output(String query_output) {
		this.query_output = query_output;
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
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("id",this.getId());
		objJson.put("pid",this.getPid());
		objJson.put("level",this.getLevel());
		objJson.put("statement_id",this.getStatement_id());
		objJson.put("snap_no",this.getSnap_no());
		objJson.put("query_seq",this.getQuery_seq());
		objJson.put("query_output",this.getQuery_output());
		
		return objJson;
	}		
}