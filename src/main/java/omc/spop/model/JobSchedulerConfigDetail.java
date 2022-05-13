package omc.spop.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.05.31	이원식	최초작성
 * 2021.05.06	황예지	필드추가
 **********************************************************/

@Alias("jobSchedulerConfigDetail")
public class JobSchedulerConfigDetail extends Base implements Jsonable {
	
    private String job_scheduler_type_cd;
    private String job_scheduler_type_nm;
    private String job_scheduler_wrk_target_id;
    private String exec_cycle;
    private String use_yn;
    private String upd_dt;
    private String upd_id;
    private String exec_start_dt;
    private String exec_end_dt;
    
	private String sql_std_qty_scheduler_no;
	private String project_id;
	private String job_scheduler_nm;
	private String job_scheduler_desc;
	private String dir_nm;
	private String exec_cycle_div_cd;
	private String exec_day_of_week;
	private String exec_day;
	private String exec_hour;
	private String exec_minute;
	
	private String std_qty_target_db_name;
	private String std_qty_target_db_user_id;
	private String parse_code;
	private String svn_if_meth_cd;
	private String svn_dir_nm;
	private String svn_ip;
	private String svn_port;
	private String svn_os_user_id;
	private String svn_os_user_password;
	
	private String std_qty_target_dbid;
	private String sql_std_qty_div_cd;
	private String sql_source_type_cd;
	private String gather_term_type_cd;
	private String gather_range_div_cd;
	private String gather_term_start_day;
	private String gather_term_end_day;
	private String owner_list;
	private String module_list;
	private String extra_filter_predication;
	private String std_qty_scheduler_div_cd;
	
	public String getOwner_list() {
		return owner_list;
	}
	public void setOwner_list(String owner_list) {
		this.owner_list = owner_list;
	}
	public String getSql_source_type_cd() {
		return sql_source_type_cd;
	}
	public void setSql_source_type_cd(String sql_source_type_cd) {
		this.sql_source_type_cd = sql_source_type_cd;
	}
	public String getGather_term_type_cd() {
		return gather_term_type_cd;
	}
	public void setGather_term_type_cd(String gather_term_type_cd) {
		this.gather_term_type_cd = gather_term_type_cd;
	}
	public String getGather_range_div_cd() {
		return gather_range_div_cd;
	}
	public void setGather_range_div_cd(String gather_range_div_cd) {
		this.gather_range_div_cd = gather_range_div_cd;
	}
	public String getGather_term_start_day() {
		return gather_term_start_day;
	}
	public void setGather_term_start_day(String gather_term_start_day) {
		this.gather_term_start_day = gather_term_start_day;
	}
	public String getGather_term_end_day() {
		return gather_term_end_day;
	}
	public void setGather_term_end_day(String gather_term_end_day) {
		this.gather_term_end_day = gather_term_end_day;
	}
	public String getModule_list() {
		return module_list;
	}
	public void setModule_list(String module_list) {
		this.module_list = module_list;
	}
	public String getExtra_filter_predication() {
		return extra_filter_predication;
	}
	public void setExtra_filter_predication(String extra_filter_predication) {
		this.extra_filter_predication = extra_filter_predication;
	}
	public String getStd_qty_scheduler_div_cd() {
		return std_qty_scheduler_div_cd;
	}
	public void setStd_qty_scheduler_div_cd(String std_qty_scheduler_div_cd) {
		this.std_qty_scheduler_div_cd = std_qty_scheduler_div_cd;
	}
	public String getSql_std_qty_div_cd() {
		return sql_std_qty_div_cd;
	}
	public void setSql_std_qty_div_cd(String sql_std_qty_div_cd) {
		this.sql_std_qty_div_cd = sql_std_qty_div_cd;
	}
	public String getStd_qty_target_dbid() {
		return std_qty_target_dbid;
	}
	public void setStd_qty_target_dbid(String std_qty_target_dbid) {
		this.std_qty_target_dbid = std_qty_target_dbid;
	}
	public String getStd_qty_target_db_name() {
		return std_qty_target_db_name;
	}
	public void setStd_qty_target_db_name(String std_qty_target_db_name) {
		this.std_qty_target_db_name = std_qty_target_db_name;
	}
	public String getStd_qty_target_db_user_id() {
		return std_qty_target_db_user_id;
	}
	public void setStd_qty_target_db_user_id(String std_qty_target_db_user_id) {
		this.std_qty_target_db_user_id = std_qty_target_db_user_id;
	}
	public String getParse_code() {
		return parse_code;
	}
	public void setParse_code(String parse_code) {
		this.parse_code = parse_code;
	}
	public String getSvn_if_meth_cd() {
		return svn_if_meth_cd;
	}
	public void setSvn_if_meth_cd(String svn_if_meth_cd) {
		this.svn_if_meth_cd = svn_if_meth_cd;
	}
	public String getSvn_dir_nm() {
		return svn_dir_nm;
	}
	public void setSvn_dir_nm(String svn_dir_nm) {
		this.svn_dir_nm = svn_dir_nm;
	}
	public String getSvn_ip() {
		return svn_ip;
	}
	public void setSvn_ip(String svn_ip) {
		this.svn_ip = svn_ip;
	}
	public String getSvn_port() {
		return svn_port;
	}
	public void setSvn_port(String svn_port) {
		this.svn_port = svn_port;
	}
	public String getSvn_os_user_id() {
		return svn_os_user_id;
	}
	public void setSvn_os_user_id(String svn_os_user_id) {
		this.svn_os_user_id = svn_os_user_id;
	}
	public String getSvn_os_user_password() {
		return svn_os_user_password;
	}
	public void setSvn_os_user_password(String svn_os_user_password) {
		this.svn_os_user_password = svn_os_user_password;
	}
	public String getSql_std_qty_scheduler_no() {
		return sql_std_qty_scheduler_no;
	}
	public void setSql_std_qty_scheduler_no(String sql_std_qty_scheduler_no) {
		this.sql_std_qty_scheduler_no = sql_std_qty_scheduler_no;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getJob_scheduler_nm() {
		return job_scheduler_nm;
	}
	public void setJob_scheduler_nm(String job_scheduler_nm) {
		this.job_scheduler_nm = job_scheduler_nm;
	}
	public String getJob_scheduler_desc() {
		return job_scheduler_desc;
	}
	public void setJob_scheduler_desc(String job_scheduler_desc) {
		this.job_scheduler_desc = job_scheduler_desc;
	}
	public String getDir_nm() {
		return dir_nm;
	}
	public void setDir_nm(String dir_nm) {
		this.dir_nm = dir_nm;
	}
	public String getExec_cycle_div_cd() {
		return exec_cycle_div_cd;
	}
	public void setExec_cycle_div_cd(String exec_cycle_div_cd) {
		this.exec_cycle_div_cd = exec_cycle_div_cd;
	}
	public String getExec_day_of_week() {
		return exec_day_of_week;
	}
	public void setExec_day_of_week(String exec_day_of_week) {
		this.exec_day_of_week = exec_day_of_week;
	}
	public String getExec_day() {
		return exec_day;
	}
	public void setExec_day(String exec_day) {
		this.exec_day = exec_day;
	}
	public String getExec_hour() {
		return exec_hour;
	}
	public void setExec_hour(String exec_hour) {
		this.exec_hour = exec_hour;
	}
	public String getExec_minute() {
		return exec_minute;
	}
	public void setExec_minute(String exec_minute) {
		this.exec_minute = exec_minute;
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
	public String getJob_scheduler_wrk_target_id() {
		return job_scheduler_wrk_target_id;
	}
	public void setJob_scheduler_wrk_target_id(String job_scheduler_wrk_target_id) {
		this.job_scheduler_wrk_target_id = job_scheduler_wrk_target_id;
	}
	public String getExec_cycle() {
		return exec_cycle;
	}
	public void setExec_cycle(String exec_cycle) {
		this.exec_cycle = exec_cycle;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	public String getUpd_dt() {
		return upd_dt;
	}
	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}
	public String getUpd_id() {
		return upd_id;
	}
	public void setUpd_id(String upd_id) {
		this.upd_id = upd_id;
	}
	public String getExec_start_dt() {
		return exec_start_dt;
	}
	public void setExec_start_dt(String exec_start_dt) {
		this.exec_start_dt = exec_start_dt;
	}
	public String getExec_end_dt() {
		return exec_end_dt;
	}
	public void setExec_end_dt(String exec_end_dt) {
		this.exec_end_dt = exec_end_dt;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("job_scheduler_type_cd",this.getJob_scheduler_type_cd());
		objJson.put("job_scheduler_type_nm",this.getJob_scheduler_type_nm());
		objJson.put("job_scheduler_wrk_target_id",this.getJob_scheduler_wrk_target_id());
		objJson.put("exec_cycle",this.getExec_cycle());
		objJson.put("use_yn",this.getUse_yn());
		objJson.put("upd_dt",this.getUpd_dt());
		objJson.put("upd_id",this.getUpd_id());
		objJson.put("exec_start_dt",this.getExec_start_dt());
		objJson.put("exec_end_dt",this.getExec_end_dt());

		return objJson;
	}		
}
