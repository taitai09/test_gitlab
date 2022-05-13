package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("instanceDsbCheck")
public class InstanceDsbCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String inst_nm;
    private String host_nm;
    private String version;
    private String startup_time;
    private String up_time;
    private String status;
    private String archiver;
    
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
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	public String getHost_nm() {
		return host_nm;
	}
	public void setHost_nm(String host_nm) {
		this.host_nm = host_nm;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStartup_time() {
		return startup_time;
	}
	public void setStartup_time(String startup_time) {
		this.startup_time = startup_time;
	}
	public String getUp_time() {
		return up_time;
	}
	public void setUp_time(String up_time) {
		this.up_time = up_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getArchiver() {
		return archiver;
	}
	public void setArchiver(String archiver) {
		this.archiver = archiver;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("inst_nm",this.getInst_nm());
		objJson.put("host_nm",this.getHost_nm());
		objJson.put("version",this.getVersion());
		objJson.put("startup_time",this.getStartup_time());
		objJson.put("up_time",this.getUp_time());
		objJson.put("status",this.getStatus());
		objJson.put("archiver",this.getArchiver());

		return objJson;
	}		
}
