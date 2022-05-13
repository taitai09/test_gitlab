package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.21	이원식	최초작성
 **********************************************************/

@Alias("vsqlSnapshot")
public class VsqlSnapshot extends Base implements Jsonable {
	
    private String snap_no;
    private String snap_s_no;
    private String snap_e_no;
    private String parsing_schema_no;
    private String parsing_schema;
    private String global_view_yn;
    private String instance_number;
    private String snap_dt;
    private String strEndTime;
    private String strStartTime;
    
    
    
	public String getStrEndTime() {
		return strEndTime;
	}
	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}
	public String getStrStartTime() {
		return strStartTime;
	}
	public void setStrStartTime(String strStartTime) {
		this.strStartTime = strStartTime;
	}
	public String getSnap_no() {
		return snap_no;
	}
	public void setSnap_no(String snap_no) {
		this.snap_no = snap_no;
	}
	public String getSnap_s_no() {
		return snap_s_no;
	}
	public void setSnap_s_no(String snap_s_no) {
		this.snap_s_no = snap_s_no;
	}
	public String getSnap_e_no() {
		return snap_e_no;
	}
	public void setSnap_e_no(String snap_e_no) {
		this.snap_e_no = snap_e_no;
	}
	public String getParsing_schema_no() {
		return parsing_schema_no;
	}
	public void setParsing_schema_no(String parsing_schema_no) {
		this.parsing_schema_no = parsing_schema_no;
	}
	public String getParsing_schema() {
		return parsing_schema;
	}
	public void setParsing_schema(String parsing_schema) {
		this.parsing_schema = parsing_schema;
	}
	public String getGlobal_view_yn() {
		return global_view_yn;
	}
	public void setGlobal_view_yn(String global_view_yn) {
		this.global_view_yn = global_view_yn;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getSnap_dt() {
		return snap_dt;
	}
	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("snap_no",StringUtil.parseLong(this.getSnap_no(),0));
		objJson.put("parsing_schema_no",StringUtil.parseInt(this.getParsing_schema_no(),0));
		objJson.put("parsing_schema",this.getParsing_schema());
		objJson.put("global_view_yn",this.getGlobal_view_yn());
		objJson.put("instance_number",StringUtil.parseInt(this.getInstance_number(),0));
		objJson.put("snap_dt",this.getSnap_dt());

		return objJson;
	}		
}
