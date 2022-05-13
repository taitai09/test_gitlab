package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.28	이원식	최초작성
 **********************************************************/

@Alias("sqlTuningStatusHistory")
public class SqlTuningStatusHistory extends Base implements Jsonable {
	
    private String tuning_status_change_dt;
    private String tuning_status_cd;
    private String tuning_status_nm;
    private String tuning_status_change_why;
    private String tuning_status_changer_id;
    private String tuning_status_changer_nm;
    
	public String getTuning_status_change_dt() {
		return tuning_status_change_dt;
	}
	public void setTuning_status_change_dt(String tuning_status_change_dt) {
		this.tuning_status_change_dt = tuning_status_change_dt;
	}
	public String getTuning_status_cd() {
		return tuning_status_cd;
	}
	public void setTuning_status_cd(String tuning_status_cd) {
		this.tuning_status_cd = tuning_status_cd;
	}
	public String getTuning_status_nm() {
		return tuning_status_nm;
	}
	public void setTuning_status_nm(String tuning_status_nm) {
		this.tuning_status_nm = tuning_status_nm;
	}
	public String getTuning_status_change_why() {
		return tuning_status_change_why;
	}
	public void setTuning_status_change_why(String tuning_status_change_why) {
		this.tuning_status_change_why = tuning_status_change_why;
	}
	public String getTuning_status_changer_id() {
		return tuning_status_changer_id;
	}
	public void setTuning_status_changer_id(String tuning_status_changer_id) {
		this.tuning_status_changer_id = tuning_status_changer_id;
	}
	public String getTuning_status_changer_nm() {
		return tuning_status_changer_nm;
	}
	public void setTuning_status_changer_nm(String tuning_status_changer_nm) {
		this.tuning_status_changer_nm = tuning_status_changer_nm;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("tuning_status_change_dt",this.getTuning_status_change_dt());
		objJson.put("tuning_status_cd",this.getTuning_status_cd());
		objJson.put("tuning_status_nm",this.getTuning_status_nm());
		objJson.put("tuning_status_change_why",this.getTuning_status_change_why());
		objJson.put("tuning_status_changer_id",this.getTuning_status_changer_id());
		objJson.put("tuning_status_changer_nm",this.getTuning_status_changer_nm());
		
		return objJson;
	}    
}