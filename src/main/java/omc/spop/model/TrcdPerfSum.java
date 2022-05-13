package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.12 이원식 최초작성
 **********************************************************/

@Alias("trcdPerfSum")
public class TrcdPerfSum extends Base implements Jsonable {
	
	private String day_gubun;
	private String base_day;
	private String min_day;
	private String max_day;
	private String day_name;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String tr_cd;
	private String tr_cd_nm;
	private String tr_perf_indc_type_cd;
	private String tr_perf_indc_type_nm;
	private String exec_cnt;
	private String total_cnt;
	private String timeout_cnt;
	private String elapsed_time_delay_cnt;

	private String prev_elap_time;
	private String cur_elap_time;
	private String delay_elap_time;
	private String elap_time_increase_ratio;

	public String getDay_gubun() {
		return day_gubun;
	}

	public void setDay_gubun(String day_gubun) {
		this.day_gubun = day_gubun;
	}

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public String getMin_day() {
		return min_day;
	}

	public void setMin_day(String min_day) {
		this.min_day = min_day;
	}

	public String getMax_day() {
		return max_day;
	}

	public void setMax_day(String max_day) {
		this.max_day = max_day;
	}

	public String getDay_name() {
		return day_name;
	}

	public void setDay_name(String day_name) {
		this.day_name = day_name;
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

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getTr_cd_nm() {
		return tr_cd_nm;
	}

	public void setTr_cd_nm(String tr_cd_nm) {
		this.tr_cd_nm = tr_cd_nm;
	}

	public String getTr_perf_indc_type_cd() {
		return tr_perf_indc_type_cd;
	}

	public void setTr_perf_indc_type_cd(String tr_perf_indc_type_cd) {
		this.tr_perf_indc_type_cd = tr_perf_indc_type_cd;
	}

	public String getTr_perf_indc_type_nm() {
		return tr_perf_indc_type_nm;
	}

	public void setTr_perf_indc_type_nm(String tr_perf_indc_type_nm) {
		this.tr_perf_indc_type_nm = tr_perf_indc_type_nm;
	}

	public String getExec_cnt() {
		return exec_cnt;
	}

	public void setExec_cnt(String exec_cnt) {
		this.exec_cnt = exec_cnt;
	}

	public String getTotal_cnt() {
		return total_cnt;
	}

	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}

	public String getTimeout_cnt() {
		return timeout_cnt;
	}

	public void setTimeout_cnt(String timeout_cnt) {
		this.timeout_cnt = timeout_cnt;
	}

	public String getElapsed_time_delay_cnt() {
		return elapsed_time_delay_cnt;
	}

	public void setElapsed_time_delay_cnt(String elapsed_time_delay_cnt) {
		this.elapsed_time_delay_cnt = elapsed_time_delay_cnt;
	}

	public String getPrev_elap_time() {
		return prev_elap_time;
	}

	public void setPrev_elap_time(String prev_elap_time) {
		this.prev_elap_time = prev_elap_time;
	}

	public String getCur_elap_time() {
		return cur_elap_time;
	}

	public void setCur_elap_time(String cur_elap_time) {
		this.cur_elap_time = cur_elap_time;
	}

	public String getDelay_elap_time() {
		return delay_elap_time;
	}

	public void setDelay_elap_time(String delay_elap_time) {
		this.delay_elap_time = delay_elap_time;
	}

	public String getElap_time_increase_ratio() {
		return elap_time_increase_ratio;
	}

	public void setElap_time_increase_ratio(String elap_time_increase_ratio) {
		this.elap_time_increase_ratio = elap_time_increase_ratio;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("day_gubun", this.getDay_gubun());
		objJson.put("base_day", this.getBase_day());
		objJson.put("min_day", this.getMin_day());
		objJson.put("max_day", this.getMax_day());
		objJson.put("day_name", this.getDay_name());
		objJson.put("wrkjob_cd", this.getWrkjob_cd());
		objJson.put("wrkjob_cd_nm", this.getWrkjob_cd_nm());
		objJson.put("tr_cd", this.getTr_cd());
		objJson.put("tr_cd_nm", this.getTr_cd_nm());
		objJson.put("user_nm", this.getUser_nm());
		objJson.put("tr_perf_indc_type_cd", this.getTr_perf_indc_type_cd());
		objJson.put("tr_perf_indc_type_nm", this.getTr_perf_indc_type_nm());
		objJson.put("exec_cnt", StringUtil.parseInt(this.getExec_cnt(), 0));
		objJson.put("total_cnt", StringUtil.parseInt(this.getTotal_cnt(), 0));
		objJson.put("timeout_cnt", StringUtil.parseInt(this.getTimeout_cnt(), 0));
		objJson.put("elapsed_time_delay_cnt", StringUtil.parseInt(this.getElapsed_time_delay_cnt(), 0));

		objJson.put("prev_elap_time", StringUtil.parseFloat(this.getPrev_elap_time(), 0));
		objJson.put("cur_elap_time", StringUtil.parseFloat(this.getCur_elap_time(), 0));
		objJson.put("delay_elap_time", StringUtil.parseFloat(this.getDelay_elap_time(), 0));
		objJson.put("elap_time_increase_ratio", StringUtil.parseDouble(this.getElap_time_increase_ratio(), 0));

		return objJson;
	}
}