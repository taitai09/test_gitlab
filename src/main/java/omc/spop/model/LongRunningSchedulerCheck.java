package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("longRunningSchedulerCheck")
public class LongRunningSchedulerCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String session_id;
    private String owner;
    private String job_name;
    private String elapsed_time;
    private String cpu_used;
    private String slave_process_id;
    private String running_instance;
    
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
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getElapsed_time() {
		return elapsed_time;
	}
	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	public String getCpu_used() {
		return cpu_used;
	}
	public void setCpu_used(String cpu_used) {
		this.cpu_used = cpu_used;
	}
	public String getSlave_process_id() {
		return slave_process_id;
	}
	public void setSlave_process_id(String slave_process_id) {
		this.slave_process_id = slave_process_id;
	}
	public String getRunning_instance() {
		return running_instance;
	}
	public void setRunning_instance(String running_instance) {
		this.running_instance = running_instance;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("session_id",this.getSession_id());
		objJson.put("owner",this.getOwner());
		objJson.put("job_name",this.getJob_name());
		objJson.put("elapsed_time",this.getElapsed_time());
		objJson.put("cpu_used",this.getCpu_used());
		objJson.put("slave_process_id",this.getSlave_process_id());
		objJson.put("running_instance",this.getRunning_instance());

		return objJson; 
	}		
}
