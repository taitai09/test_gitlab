package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.23	이원식	최초작성
 **********************************************************/

@Alias("odsHistSnapshot")
public class OdsHistSnapshot extends Base implements Jsonable {
	
	private String snap_id;
	private String instance_number;
	private String begin_interval_time;
	private String end_interval_time;
	private String startup_time;
	private String flush_elapsed;
	private String snap_level;
	private String error_count;
	private String snap_flag;
	private String snap_timezone;
	
	private String choice_tms;
	private String choice_dt;
	private String start_snap_id;
	private String end_snap_id;
	
	public String getSnap_id() {
		return snap_id;
	}
	public void setSnap_id(String snap_id) {
		this.snap_id = snap_id;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getBegin_interval_time() {
		return begin_interval_time;
	}
	public void setBegin_interval_time(String begin_interval_time) {
		this.begin_interval_time = begin_interval_time;
	}
	public String getEnd_interval_time() {
		return end_interval_time;
	}
	public void setEnd_interval_time(String end_interval_time) {
		this.end_interval_time = end_interval_time;
	}
	public String getStartup_time() {
		return startup_time;
	}
	public void setStartup_time(String startup_time) {
		this.startup_time = startup_time;
	}
	public String getFlush_elapsed() {
		return flush_elapsed;
	}
	public void setFlush_elapsed(String flush_elapsed) {
		this.flush_elapsed = flush_elapsed;
	}
	public String getSnap_level() {
		return snap_level;
	}
	public void setSnap_level(String snap_level) {
		this.snap_level = snap_level;
	}
	public String getError_count() {
		return error_count;
	}
	public void setError_count(String error_count) {
		this.error_count = error_count;
	}
	public String getSnap_flag() {
		return snap_flag;
	}
	public void setSnap_flag(String snap_flag) {
		this.snap_flag = snap_flag;
	}
	public String getSnap_timezone() {
		return snap_timezone;
	}
	public void setSnap_timezone(String snap_timezone) {
		this.snap_timezone = snap_timezone;
	}	
	public String getChoice_tms() {
		return choice_tms;
	}
	public void setChoice_tms(String choice_tms) {
		this.choice_tms = choice_tms;
	}
	public String getChoice_dt() {
		return choice_dt;
	}
	public void setChoice_dt(String choice_dt) {
		this.choice_dt = choice_dt;
	}
	public String getStart_snap_id() {
		return start_snap_id;
	}
	public void setStart_snap_id(String start_snap_id) {
		this.start_snap_id = start_snap_id;
	}
	public String getEnd_snap_id() {
		return end_snap_id;
	}
	public void setEnd_snap_id(String end_snap_id) {
		this.end_snap_id = end_snap_id;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("snap_id", StringUtil.parseInt(this.getSnap_id(),0));
		objJson.put("dbid", this.getDbid());
		objJson.put("instance_number", StringUtil.parseInt(this.getInstance_number(),0));
		objJson.put("begin_interval_time", this.getBegin_interval_time());
		objJson.put("end_interval_time", this.getEnd_interval_time());
		objJson.put("startup_time", this.getStartup_time());
		objJson.put("flush_elapsed", this.getFlush_elapsed());
		objJson.put("snap_level", this.getSnap_level());
		objJson.put("error_count", this.getError_count());
		objJson.put("snap_flag", this.getSnap_flag());
		objJson.put("snap_timezone", this.getSnap_timezone());
		
		objJson.put("choice_tms", this.getChoice_tms());
		objJson.put("choice_dt", this.getChoice_dt());
		objJson.put("start_snap_id", this.getStart_snap_id());
		objJson.put("end_snap_id", this.getEnd_snap_id());

		return objJson;
	}		
}