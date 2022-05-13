package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.18	이원식	최초작성
 **********************************************************/

@Alias("databaseTuner")
public class DatabaseTuner extends Base implements Jsonable {
    private String tuner_id;
    private String tuner_nm;
    private String tun_start_day;
    private String tun_end_day;
    private String isBase;
    
    private String use_flag;
    
	public String getTuner_id() {
		return tuner_id;
	}
	public void setTuner_id(String tuner_id) {
		this.tuner_id = tuner_id;
	}
	public String getTuner_nm() {
		return tuner_nm;
	}
	public void setTuner_nm(String tuner_nm) {
		this.tuner_nm = tuner_nm;
	}	
	public String getTun_start_day() {
		return tun_start_day;
	}
	public void setTun_start_day(String tun_start_day) {
		this.tun_start_day = tun_start_day;
	}
	public String getTun_end_day() {
		return tun_end_day;
	}
	public void setTun_end_day(String tun_end_day) {
		this.tun_end_day = tun_end_day;
	}
	public String getUse_flag() {
		return use_flag;
	}
	public void setUse_flag(String use_flag) {
		this.use_flag = use_flag;
	}
	
	public String getIsBase() {
		return isBase;
	}
	public void setIsBase(String isBase) {
		this.isBase = isBase;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("tuner_id",this.getTuner_id());
		objJson.put("tuner_nm",this.getTuner_nm());
		objJson.put("tun_start_day",this.getTun_start_day());
		objJson.put("tun_end_day",this.getTun_end_day());
		objJson.put("use_flag",this.getUse_flag());
		objJson.put("isBase",this.getIsBase());

		return objJson;
	}		
}
