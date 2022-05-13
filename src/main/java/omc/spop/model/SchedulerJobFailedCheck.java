package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("schedulerJobFailedCheck")
public class SchedulerJobFailedCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String log_id;
    private String log_date;
    private String owner;
    private String job_name;
    private String job_subname;
    private String status;
    private String error;
    private String req_start_date;
    private String actual_start_date;
    private String run_duration;
    private String session_id;
    private String slave_pid;
    private String cpu_used;
    private String credential_owner;
    private String credential_name;
    private String destination_owner;
    private String destination;
    private String additional_info;
    private String errors;
    private String output;
    
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
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getLog_date() {
		return log_date;
	}
	public void setLog_date(String log_date) {
		this.log_date = log_date;
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
	public String getJob_subname() {
		return job_subname;
	}
	public void setJob_subname(String job_subname) {
		this.job_subname = job_subname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getReq_start_date() {
		return req_start_date;
	}
	public void setReq_start_date(String req_start_date) {
		this.req_start_date = req_start_date;
	}
	public String getActual_start_date() {
		return actual_start_date;
	}
	public void setActual_start_date(String actual_start_date) {
		this.actual_start_date = actual_start_date;
	}
	public String getRun_duration() {
		return run_duration;
	}
	public void setRun_duration(String run_duration) {
		this.run_duration = run_duration;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getSlave_pid() {
		return slave_pid;
	}
	public void setSlave_pid(String slave_pid) {
		this.slave_pid = slave_pid;
	}
	public String getCpu_used() {
		return cpu_used;
	}
	public void setCpu_used(String cpu_used) {
		this.cpu_used = cpu_used;
	}
	public String getCredential_owner() {
		return credential_owner;
	}
	public void setCredential_owner(String credential_owner) {
		this.credential_owner = credential_owner;
	}
	public String getCredential_name() {
		return credential_name;
	}
	public void setCredential_name(String credential_name) {
		this.credential_name = credential_name;
	}
	public String getDestination_owner() {
		return destination_owner;
	}
	public void setDestination_owner(String destination_owner) {
		this.destination_owner = destination_owner;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getAdditional_info() {
		return additional_info;
	}
	public void setAdditional_info(String additional_info) {
		this.additional_info = additional_info;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("log_id",this.getLog_id());
		objJson.put("log_date",this.getLog_date());
		objJson.put("owner",this.getOwner());
		objJson.put("job_name",this.getJob_name());
		objJson.put("job_subname",this.getJob_subname());
		objJson.put("status",this.getStatus());
		objJson.put("error",this.getError());
		objJson.put("req_start_date",this.getReq_start_date());
		objJson.put("actual_start_date",this.getActual_start_date());
		objJson.put("run_duration",this.getRun_duration());
		objJson.put("session_id",this.getSession_id());
		objJson.put("slave_pid",this.getSlave_pid());
		objJson.put("cpu_used",this.getCpu_used());
		objJson.put("credential_owner",this.getCredential_owner());
		objJson.put("credential_name",this.getCredential_name());
		objJson.put("destination_owner",this.getDestination_owner());
		objJson.put("destination",this.getDestination());
		objJson.put("additional_info",this.getAdditional_info());
		objJson.put("errors",this.getErrors());
		objJson.put("output",this.getOutput());

		return objJson; 
	}		
}
