package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.12	이원식	최초작성
 **********************************************************/

@Alias("userDept")
public class UserDept extends Base implements Jsonable {
	
    private String dept_cd;
    private String dept_nm;
    private String work_start_day;
    private String work_end_day;
    
    private String work_comp_day;
    
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
	public String getWork_start_day() {
		return work_start_day;
	}
	public void setWork_start_day(String work_start_day) {
		this.work_start_day = work_start_day;
	}
	public String getWork_end_day() {
		return work_end_day;
	}
	public void setWork_end_day(String work_end_day) {
		this.work_end_day = work_end_day;
	}
	public String getWork_comp_day() {
		return work_comp_day;
	}
	public void setWork_comp_day(String work_comp_day) {
		this.work_comp_day = work_comp_day;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("user_id",this.getUser_id());
		objJson.put("dept_cd",this.getDept_cd());
		objJson.put("dept_nm",this.getDept_nm());
		objJson.put("work_start_day",this.getWork_start_day());
		objJson.put("work_end_day",this.getWork_end_day());
		
		return objJson;
	}		
}
