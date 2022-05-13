package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.12.22	이원식	최초작성
 **********************************************************/

@Alias("beforePerfExpect")
public class BeforePerfExpect extends Base implements Jsonable {
    private String before_perf_expect_no;
    private String expect_work_dt;
    private String expect_work_exec_day;
    private String expect_work_exec_time;
    private String top_rank_measure_type_cd;
    private String top_rank_measure_type_nm;
    private String topn_cnt;
    private String plan_change_cnt;
    private String perf_regress_cnt;
    private String work_start_dt;
    private String work_end_dt;
    private String begin_snap_id;
    private String end_snap_id;
    
    private String immediately_yn;
    private String edit_yn;
    
	public String getBefore_perf_expect_no() {
		return before_perf_expect_no;
	}
	public void setBefore_perf_expect_no(String before_perf_expect_no) {
		this.before_perf_expect_no = before_perf_expect_no;
	}
	public String getExpect_work_dt() {
		return expect_work_dt;
	}
	public void setExpect_work_dt(String expect_work_dt) {
		this.expect_work_dt = expect_work_dt;
	}
	public String getExpect_work_exec_day() {
		return expect_work_exec_day;
	}
	public void setExpect_work_exec_day(String expect_work_exec_day) {
		this.expect_work_exec_day = expect_work_exec_day;
	}
	public String getExpect_work_exec_time() {
		return expect_work_exec_time;
	}
	public void setExpect_work_exec_time(String expect_work_exec_time) {
		this.expect_work_exec_time = expect_work_exec_time;
	}
	public String getTop_rank_measure_type_cd() {
		return top_rank_measure_type_cd;
	}
	public void setTop_rank_measure_type_cd(String top_rank_measure_type_cd) {
		this.top_rank_measure_type_cd = top_rank_measure_type_cd;
	}
	public String getTop_rank_measure_type_nm() {
		return top_rank_measure_type_nm;
	}
	public void setTop_rank_measure_type_nm(String top_rank_measure_type_nm) {
		this.top_rank_measure_type_nm = top_rank_measure_type_nm;
	}
	public String getTopn_cnt() {
		return topn_cnt;
	}
	public void setTopn_cnt(String topn_cnt) {
		this.topn_cnt = topn_cnt;
	}
	public String getPlan_change_cnt() {
		return plan_change_cnt;
	}
	public void setPlan_change_cnt(String plan_change_cnt) {
		this.plan_change_cnt = plan_change_cnt;
	}
	public String getPerf_regress_cnt() {
		return perf_regress_cnt;
	}
	public void setPerf_regress_cnt(String perf_regress_cnt) {
		this.perf_regress_cnt = perf_regress_cnt;
	}
	public String getWork_start_dt() {
		return work_start_dt;
	}
	public void setWork_start_dt(String work_start_dt) {
		this.work_start_dt = work_start_dt;
	}
	public String getWork_end_dt() {
		return work_end_dt;
	}
	public void setWork_end_dt(String work_end_dt) {
		this.work_end_dt = work_end_dt;
	}
	public String getBegin_snap_id() {
		return begin_snap_id;
	}
	public void setBegin_snap_id(String begin_snap_id) {
		this.begin_snap_id = begin_snap_id;
	}
	public String getEnd_snap_id() {
		return end_snap_id;
	}
	public void setEnd_snap_id(String end_snap_id) {
		this.end_snap_id = end_snap_id;
	}	
	public String getImmediately_yn() {
		return immediately_yn;
	}
	public void setImmediately_yn(String immediately_yn) {
		this.immediately_yn = immediately_yn;
	}
	public String getEdit_yn() {
		return edit_yn;
	}
	public void setEdit_yn(String edit_yn) {
		this.edit_yn = edit_yn;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("before_perf_expect_no",this.getBefore_perf_expect_no());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("expect_work_dt",this.getExpect_work_dt());
		objJson.put("expect_work_exec_day",this.getExpect_work_exec_day());
		objJson.put("expect_work_exec_time",this.getExpect_work_exec_time());
		objJson.put("top_rank_measure_type_cd",this.getTop_rank_measure_type_cd());
		objJson.put("top_rank_measure_type_nm",this.getTop_rank_measure_type_nm());
		objJson.put("topn_cnt",StringUtil.parseDouble(this.getTopn_cnt(),0));
		objJson.put("plan_change_cnt",StringUtil.parseDouble(this.getPlan_change_cnt(),0));
		objJson.put("perf_regress_cnt",StringUtil.parseDouble(this.getPerf_regress_cnt(),0));
		objJson.put("work_start_dt",this.getWork_start_dt());
		objJson.put("work_end_dt",this.getWork_end_dt());
		objJson.put("begin_snap_id",this.getBegin_snap_id());
		objJson.put("end_snap_id",this.getEnd_snap_id());
		objJson.put("immediately_yn",this.getImmediately_yn());

		return objJson;
	}		
}
