package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("tableUseSql")
public class TableUseSql extends Base implements Jsonable {
	
    private String owner;
    private String table_name;
    private String sql_id;
    private String plan_hash_value;
    private String module;
    private String action;
    private String avg_elap;
    private String executions;
    private String avg_cpu;
    private String avg_bget;
    private String avg_drds;
    private String avg_rows;
    private String sql_text;
    
    private String index_name;
    
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
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
	public String getAvg_elap() {
		return avg_elap;
	}
	public void setAvg_elap(String avg_elap) {
		this.avg_elap = avg_elap;
	}
	public String getExecutions() {
		return executions;
	}
	public void setExecutions(String executions) {
		this.executions = executions;
	}
	public String getAvg_cpu() {
		return avg_cpu;
	}
	public void setAvg_cpu(String avg_cpu) {
		this.avg_cpu = avg_cpu;
	}
	public String getAvg_bget() {
		return avg_bget;
	}
	public void setAvg_bget(String avg_bget) {
		this.avg_bget = avg_bget;
	}
	public String getAvg_drds() {
		return avg_drds;
	}
	public void setAvg_drds(String avg_drds) {
		this.avg_drds = avg_drds;
	}
	public String getAvg_rows() {
		return avg_rows;
	}
	public void setAvg_rows(String avg_rows) {
		this.avg_rows = avg_rows;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}	
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("owner",this.getOwner());
		objJson.put("table_name",this.getTable_name());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("plan_hash_value",this.getPlan_hash_value());
		objJson.put("module",this.getModule());
		objJson.put("action",this.getAction());
		objJson.put("avg_elap",StringUtil.parseFloat(this.getAvg_elap(),0));
		objJson.put("executions",StringUtil.parseFloat(this.getExecutions(),0));
		objJson.put("avg_cpu",StringUtil.parseFloat(this.getAvg_cpu(),0));
		objJson.put("avg_bget",StringUtil.parseFloat(this.getAvg_bget(),0));
		objJson.put("avg_drds",StringUtil.parseFloat(this.getAvg_drds(),0));
		objJson.put("avg_rows",StringUtil.parseFloat(this.getAvg_rows(),0));
		objJson.put("sql_text",this.getSql_text());

		return objJson;
	}		
}