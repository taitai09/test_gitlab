package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.24	이원식	최초작성
 **********************************************************/

@Alias("monitoringGroup")
public class MonitoringGroup extends Base implements Jsonable {
	
    private String group_id;
    private String group_nm;
    private String group_desc;
    
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_nm() {
		return group_nm;
	}
	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}
	public String getGroup_desc() {
		return group_desc;
	}
	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("group_id",this.getGroup_id());
		objJson.put("user_id",this.getUser_id());
		objJson.put("group_nm",this.getGroup_nm());
		objJson.put("group_desc",this.getGroup_desc());

		return objJson;
	}		
}
