package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("instanceAlert")
public class InstanceAlert extends Base implements Jsonable {
	
    private String inst_id;
    private String alert_type_cd;
    private String alert_type_nm;
    private String alert_occur_dt;
    private String alert_grade_cd;
    private String alert_grade_nm;
    private String alert_contents;
    private String alert_cnt;
    
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
	public String getAlert_occur_dt() {
		return alert_occur_dt;
	}
	public void setAlert_occur_dt(String alert_occur_dt) {
		this.alert_occur_dt = alert_occur_dt;
	}
	public String getAlert_grade_cd() {
		return alert_grade_cd;
	}
	public void setAlert_grade_cd(String alert_grade_cd) {
		this.alert_grade_cd = alert_grade_cd;
	}
	public String getAlert_grade_nm() {
		return alert_grade_nm;
	}
	public void setAlert_grade_nm(String alert_grade_nm) {
		this.alert_grade_nm = alert_grade_nm;
	}
	public String getAlert_contents() {
		return alert_contents;
	}
	public void setAlert_contents(String alert_contents) {
		this.alert_contents = alert_contents;
	}	
	public String getAlert_cnt() {
		return alert_cnt;
	}
	public void setAlert_cnt(String alert_cnt) {
		this.alert_cnt = alert_cnt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("alert_type_cd",this.getAlert_type_cd());
		objJson.put("alert_type_nm",this.getAlert_type_nm());
		objJson.put("alert_occur_dt",this.getAlert_occur_dt());
		objJson.put("alert_grade_cd",this.getAlert_grade_cd());
		objJson.put("alert_grade_nm",this.getAlert_grade_nm());
		objJson.put("alert_contents",this.getAlert_contents());

		return objJson;
	}		
}