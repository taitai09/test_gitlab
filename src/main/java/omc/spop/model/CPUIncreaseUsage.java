package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.06.07 이원식 최초작성 (장예예측분석 - CPU사용량 증가에서 사용)
 **********************************************************/

@Alias("cpuIncreaseUsage")
public class CPUIncreaseUsage extends Base implements Jsonable {
	
	private String inst_id;
	private String inst_nm;
	private String baseline_cpu_usage;
	private String current_cpu_usage;
	private String cpu_increase_ratio;
	private String snap_time;
	private String baseline_user_time;
	private String current_user_time;
	private String baseline_sys_time;
	private String current_sys_time;
	private String stat_name;
	private String baseline_time_model;
	private String current_time_model;
	private String sql_id;
	private String cnt;
	private String activity;
	private String sql_text;

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

	public String getBaseline_cpu_usage() {
		return baseline_cpu_usage;
	}

	public void setBaseline_cpu_usage(String baseline_cpu_usage) {
		this.baseline_cpu_usage = baseline_cpu_usage;
	}

	public String getCurrent_cpu_usage() {
		return current_cpu_usage;
	}

	public void setCurrent_cpu_usage(String current_cpu_usage) {
		this.current_cpu_usage = current_cpu_usage;
	}

	public String getCpu_increase_ratio() {
		return cpu_increase_ratio;
	}

	public void setCpu_increase_ratio(String cpu_increase_ratio) {
		this.cpu_increase_ratio = cpu_increase_ratio;
	}

	public String getSnap_time() {
		return snap_time;
	}

	public void setSnap_time(String snap_time) {
		this.snap_time = snap_time;
	}

	public String getBaseline_user_time() {
		return baseline_user_time;
	}

	public void setBaseline_user_time(String baseline_user_time) {
		this.baseline_user_time = baseline_user_time;
	}

	public String getCurrent_user_time() {
		return current_user_time;
	}

	public void setCurrent_user_time(String current_user_time) {
		this.current_user_time = current_user_time;
	}

	public String getBaseline_sys_time() {
		return baseline_sys_time;
	}

	public void setBaseline_sys_time(String baseline_sys_time) {
		this.baseline_sys_time = baseline_sys_time;
	}

	public String getCurrent_sys_time() {
		return current_sys_time;
	}

	public void setCurrent_sys_time(String current_sys_time) {
		this.current_sys_time = current_sys_time;
	}

	public String getStat_name() {
		return stat_name;
	}

	public void setStat_name(String stat_name) {
		this.stat_name = stat_name;
	}

	public String getBaseline_time_model() {
		return baseline_time_model;
	}

	public void setBaseline_time_model(String baseline_time_model) {
		this.baseline_time_model = baseline_time_model;
	}

	public String getCurrent_time_model() {
		return current_time_model;
	}

	public void setCurrent_time_model(String current_time_model) {
		this.current_time_model = current_time_model;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid", this.getDbid());
		objJson.put("db_name", this.getDb_name());
		objJson.put("inst_id", this.getInst_id());
		objJson.put("inst_nm", this.getInst_nm());
		objJson.put("baseline_cpu_usage", StringUtil.parseDouble(this.getBaseline_cpu_usage(), 0));
		objJson.put("current_cpu_usage", StringUtil.parseDouble(this.getCurrent_cpu_usage(), 0));
		objJson.put("cpu_increase_ratio", StringUtil.parseDouble(this.getCpu_increase_ratio(), 0));
		objJson.put("snap_time", this.getSnap_time());
		objJson.put("baseline_user_time", StringUtil.parseDouble(this.getBaseline_user_time(), 0));
		objJson.put("current_user_time", StringUtil.parseDouble(this.getCurrent_user_time(), 0));
		objJson.put("baseline_sys_time", StringUtil.parseDouble(this.getBaseline_sys_time(), 0));
		objJson.put("current_sys_time", StringUtil.parseDouble(this.getCurrent_sys_time(), 0));
		objJson.put("stat_name", this.getStat_name());
		objJson.put("baseline_time_model", StringUtil.parseDouble(this.getBaseline_time_model(), 0));
		objJson.put("current_time_model", StringUtil.parseDouble(this.getCurrent_time_model(), 0));
		objJson.put("sql_id", this.getSql_id());
		objJson.put("cnt", StringUtil.parseDouble(this.getCnt(), 0));
		objJson.put("activity", StringUtil.parseDouble(this.getActivity(), 0));
		objJson.put("sql_text", this.getSql_text());

		return objJson;
	}
}
