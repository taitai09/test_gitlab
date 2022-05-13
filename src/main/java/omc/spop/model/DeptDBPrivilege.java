package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.12	이원식	최초작성
 **********************************************************/

@Alias("deptDBPrivilege")
public class DeptDBPrivilege extends Base implements Jsonable {
    private String dept_cd;
    private String dept_nm;
    private String privilege_start_day;
    private String privilege_end_day;
    
    private String use_flag;

	public String getDept_cd() {
		return dept_cd;
	}
	public void setDept_cd(String dept_cd) {
		this.dept_cd = dept_cd;
	}
	public String getDept_nm() {
		return dept_nm;
	}
	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
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
		
		objJson.put("dept_cd",this.getDept_cd());
		objJson.put("dept_nm",this.getDept_nm());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("privilege_start_day",this.getPrivilege_start_day());
		objJson.put("privilege_end_day",this.getPrivilege_end_day());
		objJson.put("use_flag",this.getUse_flag());

		return objJson;
	}		
}
