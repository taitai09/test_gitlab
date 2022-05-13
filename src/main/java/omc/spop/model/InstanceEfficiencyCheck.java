package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("instanceEfficiencyCheck")
public class InstanceEfficiencyCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;    
    private String threshold_value;
    private String inst_efficiency_type_cd;
    private String inst_efficiency_yn;
    private String inst_efficiency_value;
    
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
	public String getThreshold_value() {
		return threshold_value;
	}
	public void setThreshold_value(String threshold_value) {
		this.threshold_value = threshold_value;
	}
	public String getInst_efficiency_type_cd() {
		return inst_efficiency_type_cd;
	}
	public void setInst_efficiency_type_cd(String inst_efficiency_type_cd) {
		this.inst_efficiency_type_cd = inst_efficiency_type_cd;
	}
	public String getInst_efficiency_yn() {
		return inst_efficiency_yn;
	}
	public void setInst_efficiency_yn(String inst_efficiency_yn) {
		this.inst_efficiency_yn = inst_efficiency_yn;
	}
	public String getInst_efficiency_value() {
		return inst_efficiency_value;
	}
	public void setInst_efficiency_value(String inst_efficiency_value) {
		this.inst_efficiency_value = inst_efficiency_value;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());		
		objJson.put("inst_efficiency_type_cd",this.getInst_efficiency_type_cd());
		objJson.put("threshold_value",StringUtil.parseDouble(this.getThreshold_value(),0));
		objJson.put("inst_efficiency_yn",this.getInst_efficiency_yn());
		objJson.put("inst_efficiency_value",StringUtil.parseFloat(this.getInst_efficiency_value(),0));

		return objJson; 
	}		
}
