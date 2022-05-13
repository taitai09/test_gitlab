package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.13	이원식	최초작성
 **********************************************************/

@Alias("userDBPrivilege")
public class UserDBPrivilege extends Base implements Jsonable {
	
    private String privilege_start_day;
    private String privilege_end_day;
    private String old_dbid;
    private String use_flag;

	public String getOld_dbid() {
		return old_dbid;
	}
	public void setOld_dbid(String old_dbid) {
		this.old_dbid = old_dbid;
	}
	public String getPrivilege_start_day() {
		return privilege_start_day;
	}
	public void setPrivilege_start_day(String privilege_start_day) {
		this.privilege_start_day = privilege_start_day;
	}
	public String getPrivilege_end_day() {
		return privilege_end_day;
	}
	public void setPrivilege_end_day(String privilege_end_day) {
		this.privilege_end_day = privilege_end_day;
	}	
	public String getUse_flag() {
		return use_flag;
	}
	public void setUse_flag(String use_flag) {
		this.use_flag = use_flag;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("user_id",this.getUser_id());
		objJson.put("user_nm",this.getUser_nm());
		objJson.put("old_dbid",this.getOld_dbid());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("privilege_start_day",this.getPrivilege_start_day());
		objJson.put("privilege_end_day",this.getPrivilege_end_day());
		objJson.put("use_flag",this.getUse_flag());

		return objJson;
	}		
}
