package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("resourceLimitCheck")
public class ResourceLimitCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String resource_nm;
    private String max_utilization;
    private String limit_value;
    private String utilization_percent;
    
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getResource_nm() {
		return resource_nm;
	}
	public void setResource_nm(String resource_nm) {
		this.resource_nm = resource_nm;
	}
	public String getMax_utilization() {
		return max_utilization;
	}
	public void setMax_utilization(String max_utilization) {
		this.max_utilization = max_utilization;
	}
	public String getLimit_value() {
		return limit_value;
	}
	public void setLimit_value(String limit_value) {
		this.limit_value = limit_value;
	}
	public String getUtilization_percent() {
		return utilization_percent;
	}
	public void setUtilization_percent(String utilization_percent) {
		this.utilization_percent = utilization_percent;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("resource_nm",this.getResource_nm());
		objJson.put("max_utilization",StringUtil.parseDouble(this.getMax_utilization(),0));
		objJson.put("limit_value",StringUtil.parseDouble(this.getLimit_value(),0));
		objJson.put("utilization_percent",StringUtil.parseFloat(this.getUtilization_percent(),0));

		return objJson; 
	}		
}
