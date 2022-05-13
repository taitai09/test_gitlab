package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.14	이원식	최초작성
 **********************************************************/

@Alias("jobSchedulerExecDetailLog")
public class JobSchedulerExecDetailLog extends Base implements Jsonable {
	
    private String job_exec_no;
    private String job_scheduler_type_cd;
    private String job_scheduler_type_nm;
    private String job_scheduler_detail_type_cd;
    private String job_scheduler_detail_type_nm;
    private String job_exec_dt;
    private String job_target_cnt;
    private String job_err_yn;
    private String job_err_code;
    private String job_err_sbst;
    private String hndop_job_exec_yn;
    private String hndop_worker_id;
    private String hndop_worker_nm;
    
    private String list_job_scheduler_type_cd;
    
	public String getJob_exec_no() {
		return job_exec_no;
	}
	public void setJob_exec_no(String job_exec_no) {
		this.job_exec_no = job_exec_no;
	}
	public String getJob_scheduler_type_cd() {
		return job_scheduler_type_cd;
	}
	public void setJob_scheduler_type_cd(String job_scheduler_type_cd) {
		this.job_scheduler_type_cd = job_scheduler_type_cd;
	}
	public String getJob_scheduler_type_nm() {
		return job_scheduler_type_nm;
	}
	public void setJob_scheduler_type_nm(String job_scheduler_type_nm) {
		this.job_scheduler_type_nm = job_scheduler_type_nm;
	}
	public String getJob_scheduler_detail_type_cd() {
		return job_scheduler_detail_type_cd;
	}
	public void setJob_scheduler_detail_type_cd(String job_scheduler_detail_type_cd) {
		this.job_scheduler_detail_type_cd = job_scheduler_detail_type_cd;
	}
	public String getJob_scheduler_detail_type_nm() {
		return job_scheduler_detail_type_nm;
	}
	public void setJob_scheduler_detail_type_nm(String job_scheduler_detail_type_nm) {
		this.job_scheduler_detail_type_nm = job_scheduler_detail_type_nm;
	}
	public String getJob_exec_dt() {
		return job_exec_dt;
	}
	public void setJob_exec_dt(String job_exec_dt) {
		this.job_exec_dt = job_exec_dt;
	}
	public String getJob_target_cnt() {
		return job_target_cnt;
	}
	public void setJob_target_cnt(String job_target_cnt) {
		this.job_target_cnt = job_target_cnt;
	}
	public String getJob_err_yn() {
		return job_err_yn;
	}
	public void setJob_err_yn(String job_err_yn) {
		this.job_err_yn = job_err_yn;
	}
	public String getJob_err_code() {
		return job_err_code;
	}
	public void setJob_err_code(String job_err_code) {
		this.job_err_code = job_err_code;
	}
	public String getJob_err_sbst() {
		return job_err_sbst;
	}
	public void setJob_err_sbst(String job_err_sbst) {
		this.job_err_sbst = job_err_sbst;
	}
	public String getHndop_job_exec_yn() {
		return hndop_job_exec_yn;
	}
	public void setHndop_job_exec_yn(String hndop_job_exec_yn) {
		this.hndop_job_exec_yn = hndop_job_exec_yn;
	}
	public String getHndop_worker_id() {
		return hndop_worker_id;
	}
	public void setHndop_worker_id(String hndop_worker_id) {
		this.hndop_worker_id = hndop_worker_id;
	}
	public String getHndop_worker_nm() {
		return hndop_worker_nm;
	}
	public void setHndop_worker_nm(String hndop_worker_nm) {
		this.hndop_worker_nm = hndop_worker_nm;
	}	
	public String getList_job_scheduler_type_cd() {
		return list_job_scheduler_type_cd;
	}
	public void setList_job_scheduler_type_cd(String list_job_scheduler_type_cd) {
		this.list_job_scheduler_type_cd = list_job_scheduler_type_cd;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("job_exec_no",StringUtil.parseDouble(this.getJob_exec_no(),0));
		objJson.put("job_scheduler_type_cd",this.getJob_scheduler_type_cd());
		objJson.put("job_scheduler_type_nm",this.getJob_scheduler_type_nm());
		objJson.put("job_scheduler_detail_type_cd",this.getJob_scheduler_detail_type_cd());
		objJson.put("job_scheduler_detail_type_nm",this.getJob_scheduler_detail_type_nm());
		objJson.put("job_exec_dt",this.getJob_exec_dt());
		objJson.put("job_target_cnt",StringUtil.parseDouble(this.getJob_target_cnt(),0));
		objJson.put("job_err_yn",this.getJob_err_yn());
		objJson.put("job_err_code",this.getJob_err_code());
		objJson.put("job_err_sbst",this.getJob_err_sbst());
		objJson.put("hndop_job_exec_yn",this.getHndop_job_exec_yn());
		objJson.put("hndop_worker_id",this.getHndop_worker_id());
		objJson.put("hndop_worker_nm",this.getHndop_worker_nm());

		return objJson;
	}		
}
