package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.11	이원식	최초작성
 **********************************************************/

@Alias("alertConfig")
public class AlertConfig extends Base implements Jsonable {
    private String inst_id;
    private String alert_type_cd;
    private String alert_type_nm;
    private String alert_threshold;
    private String enable_yn;
    private String sms_send_yn;
    
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getAlert_type_cd() {
		return alert_type_cd;
	}
	public void setAlert_type_cd(String alert_type_cd) {
		this.alert_type_cd = alert_type_cd;
	}
	public String getAlert_type_nm() {
		return alert_type_nm;
	}
	public void setAlert_type_nm(String alert_type_nm) {
		this.alert_type_nm = alert_type_nm;
	}
	public String getAlert_threshold() {
		return alert_threshold;
	}
	public void setAlert_threshold(String alert_threshold) {
		this.alert_threshold = alert_threshold;
	}
	public String getEnable_yn() {
		return enable_yn;
	}
	public void setEnable_yn(String enable_yn) {
		this.enable_yn = enable_yn;
	}
	public String getSms_send_yn() {
		return sms_send_yn;
	}
	public void setSms_send_yn(String sms_send_yn) {
		this.sms_send_yn = sms_send_yn;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",StringUtil.parseDouble(this.getDbid(),0));
		objJson.put("inst_id",StringUtil.parseInt(this.getInst_id(),0));
		objJson.put("alert_type_cd",this.getAlert_type_cd());
		objJson.put("alert_type_nm",this.getAlert_type_nm());
		objJson.put("alert_threshold",this.getAlert_threshold());
		objJson.put("enable_yn",this.getEnable_yn());
		objJson.put("sms_send_yn",this.getSms_send_yn());

		return objJson;
	}		
}
