package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.11.03	이원식	최초작성
 **********************************************************/

@Alias("schedule")
public class Schedule extends Base implements Jsonable {
	
    private String sched_seq;
    private String sched_type_cd;
    private String sched_type_nm;
    private String sched_title;
    private String sched_sbst;
    private String sched_start_dt;
    private String sched_start_date;
    private String sched_start_time;
    private String sched_end_dt;
    private String sched_end_date;
    private String sched_end_time;
    private String allday_yn;
    private String rpt_yn;
    private String rpt_freq_div_cd;
    private String rpt_freq_div_nm;
    private String rpt_std_div_cd;
    private String rpt_std_div_nm;
    private String rpt_cycle;
    private String rpt_dayofweek;
    private String rpt_start_day;
    private String rpt_end_day;
    private String reg_dt;
    
	public String getSched_seq() {
		return sched_seq;
	}
	public void setSched_seq(String sched_seq) {
		this.sched_seq = sched_seq;
	}
	public String getSched_type_cd() {
		return sched_type_cd;
	}
	public void setSched_type_cd(String sched_type_cd) {
		this.sched_type_cd = sched_type_cd;
	}
	public String getSched_type_nm() {
		return sched_type_nm;
	}
	public void setSched_type_nm(String sched_type_nm) {
		this.sched_type_nm = sched_type_nm;
	}
	public String getSched_title() {
		return sched_title;
	}
	public void setSched_title(String sched_title) {
		this.sched_title = sched_title;
	}
	public String getSched_sbst() {
		return sched_sbst;
	}
	public void setSched_sbst(String sched_sbst) {
		this.sched_sbst = sched_sbst;
	}
	public String getSched_start_dt() {
		return sched_start_dt;
	}
	public void setSched_start_dt(String sched_start_dt) {
		this.sched_start_dt = sched_start_dt;
	}
	public String getSched_start_date() {
		return sched_start_date;
	}
	public void setSched_start_date(String sched_start_date) {
		this.sched_start_date = sched_start_date;
	}
	public String getSched_start_time() {
		return sched_start_time;
	}
	public void setSched_start_time(String sched_start_time) {
		this.sched_start_time = sched_start_time;
	}
	public String getSched_end_dt() {
		return sched_end_dt;
	}
	public void setSched_end_dt(String sched_end_dt) {
		this.sched_end_dt = sched_end_dt;
	}
	public String getSched_end_date() {
		return sched_end_date;
	}
	public void setSched_end_date(String sched_end_date) {
		this.sched_end_date = sched_end_date;
	}
	public String getSched_end_time() {
		return sched_end_time;
	}
	public void setSched_end_time(String sched_end_time) {
		this.sched_end_time = sched_end_time;
	}
	public String getAllday_yn() {
		return allday_yn;
	}
	public void setAllday_yn(String allday_yn) {
		this.allday_yn = allday_yn;
	}
	public String getRpt_yn() {
		return rpt_yn;
	}
	public void setRpt_yn(String rpt_yn) {
		this.rpt_yn = rpt_yn;
	}
	public String getRpt_freq_div_cd() {
		return rpt_freq_div_cd;
	}
	public void setRpt_freq_div_cd(String rpt_freq_div_cd) {
		this.rpt_freq_div_cd = rpt_freq_div_cd;
	}
	public String getRpt_freq_div_nm() {
		return rpt_freq_div_nm;
	}
	public void setRpt_freq_div_nm(String rpt_freq_div_nm) {
		this.rpt_freq_div_nm = rpt_freq_div_nm;
	}
	public String getRpt_std_div_cd() {
		return rpt_std_div_cd;
	}
	public void setRpt_std_div_cd(String rpt_std_div_cd) {
		this.rpt_std_div_cd = rpt_std_div_cd;
	}
	public String getRpt_std_div_nm() {
		return rpt_std_div_nm;
	}
	public void setRpt_std_div_nm(String rpt_std_div_nm) {
		this.rpt_std_div_nm = rpt_std_div_nm;
	}
	public String getRpt_cycle() {
		return rpt_cycle;
	}
	public void setRpt_cycle(String rpt_cycle) {
		this.rpt_cycle = rpt_cycle;
	}
	public String getRpt_dayofweek() {
		return rpt_dayofweek;
	}
	public void setRpt_dayofweek(String rpt_dayofweek) {
		this.rpt_dayofweek = rpt_dayofweek;
	}
	public String getRpt_start_day() {
		return rpt_start_day;
	}
	public void setRpt_start_day(String rpt_start_day) {
		this.rpt_start_day = rpt_start_day;
	}
	public String getRpt_end_day() {
		return rpt_end_day;
	}
	public void setRpt_end_day(String rpt_end_day) {
		this.rpt_end_day = rpt_end_day;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("user_id",this.getUser_id());
		objJson.put("user_nm",this.getUser_nm());
		objJson.put("sched_seq",this.getSched_seq());
		objJson.put("sched_type_cd",this.getSched_type_cd());
		objJson.put("sched_type_nm",this.getSched_type_nm());
		objJson.put("sched_title",this.getSched_title());
		objJson.put("sched_sbst",this.getSched_sbst());
		objJson.put("sched_start_dt",this.getSched_start_dt());
		objJson.put("sched_start_date",this.getSched_start_date());
		objJson.put("sched_start_time",this.getSched_start_time());
		objJson.put("sched_end_dt",this.getSched_end_dt());
		objJson.put("sched_end_date",this.getSched_end_date());
		objJson.put("sched_end_time",this.getSched_end_time());
		objJson.put("allday_yn",this.getAllday_yn());
		objJson.put("rpt_yn",this.getRpt_yn());
		objJson.put("rpt_freq_div_cd",this.getRpt_freq_div_cd());
		objJson.put("rpt_freq_div_nm",this.getRpt_freq_div_nm());
		objJson.put("rpt_std_div_cd",this.getRpt_std_div_cd());
		objJson.put("rpt_std_div_nm",this.getRpt_std_div_nm());
		objJson.put("rpt_cycle",this.getRpt_cycle());
		objJson.put("rpt_dayofweek",this.getRpt_dayofweek());
		objJson.put("rpt_start_day",this.getRpt_start_day());
		objJson.put("rpt_end_day",this.getRpt_end_day());
		objJson.put("reg_dt",this.getReg_dt());

		return objJson;
	}		
}