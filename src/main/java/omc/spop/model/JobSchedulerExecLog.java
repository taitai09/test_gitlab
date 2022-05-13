package omc.spop.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.12.14	이원식	최초작성
 * 2019.12.27	명성태	메뉴 통합
 **********************************************************/

@Alias("jobSchedulerExecLog")
public class JobSchedulerExecLog extends Base implements Jsonable {
	private String job_exec_no;
	private String job_scheduler_type_cd;
	private String job_scheduler_type_nm;
	private String base_day;
	private String job_start_dt;
	private String job_end_dt;
	private String job_status_cd;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	
	private int scheduler_job_status_cd;
	private String error_yn;
	private int complete_cnt;
	private int error_cnt;
	
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
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getJob_start_dt() {
		return job_start_dt;
	}
	public void setJob_start_dt(String job_start_dt) {
		this.job_start_dt = job_start_dt;
	}
	public String getJob_end_dt() {
		return job_end_dt;
	}
	public void setJob_end_dt(String job_end_dt) {
		this.job_end_dt = job_end_dt;
	}
	public String getJob_status_cd() {
		return job_status_cd;
	}
	public void setJob_status_cd(String job_status_cd) {
		this.job_status_cd = job_status_cd;
	}
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}	
	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}
	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public int getScheduler_job_status_cd() {
		return scheduler_job_status_cd;
	}
	public void setScheduler_job_status_cd(int scheduler_job_status_cd) {
		this.scheduler_job_status_cd = scheduler_job_status_cd;
	}
	public String getError_yn() {
		return error_yn;
	}
	public void setError_yn(String error_yn) {
		this.error_yn = error_yn;
	}
	public int getComplete_cnt() {
		return complete_cnt;
	}
	public void setComplete_cnt(int complete_cnt) {
		this.complete_cnt = complete_cnt;
	}
	public int getError_cnt() {
		return error_cnt;
	}
	public void setError_cnt(int error_cnt) {
		this.error_cnt = error_cnt;
	}
}
