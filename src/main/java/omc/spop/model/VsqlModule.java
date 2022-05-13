package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.16	이원식	최초작성
 **********************************************************/

@Alias("vsqlModule")
public class VsqlModule extends Base implements Jsonable {
	
    private String sql_id;
    private String module;
    private String module_hash;
    
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getModule_hash() {
		return module_hash;
	}
	public void setModule_hash(String module_hash) {
		this.module_hash = module_hash;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("module",this.getModule());
		objJson.put("module_hash",this.getModule_hash());
		
		return objJson;
	}		
}