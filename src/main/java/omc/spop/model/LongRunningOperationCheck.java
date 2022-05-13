package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("longRunningOperationCheck")
public class LongRunningOperationCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String sid;
    private String serial;
    private String start_time;
    private String last_update_time;
    private String elapsed_minute;
    private String remaining_minute;
    private String done_percent;
    private String message;
    private String sql_id;
    private String sql_plan_hash_value;
    private String sql_text;
    
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getLast_update_time() {
		return last_update_time;
	}
	public void setLast_update_time(String last_update_time) {
		this.last_update_time = last_update_time;
	}
	public String getElapsed_minute() {
		return elapsed_minute;
	}
	public void setElapsed_minute(String elapsed_minute) {
		this.elapsed_minute = elapsed_minute;
	}
	public String getRemaining_minute() {
		return remaining_minute;
	}
	public void setRemaining_minute(String remaining_minute) {
		this.remaining_minute = remaining_minute;
	}
	public String getDone_percent() {
		return done_percent;
	}
	public void setDone_percent(String done_percent) {
		this.done_percent = done_percent;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getSql_plan_hash_value() {
		return sql_plan_hash_value;
	}
	public void setSql_plan_hash_value(String sql_plan_hash_value) {
		this.sql_plan_hash_value = sql_plan_hash_value;
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
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("sid",this.getSid());
		objJson.put("serial",this.getSerial());
		objJson.put("start_time",this.getStart_time());
		objJson.put("last_update_time",this.getLast_update_time());
		objJson.put("elapsed_minute",this.getElapsed_minute());
		objJson.put("remaining_minute",this.getRemaining_minute());
		objJson.put("done_percent",this.getDone_percent());
		objJson.put("message",this.getMessage());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("sql_plan_hash_value",this.getSql_plan_hash_value());
		objJson.put("sql_text",this.getSql_text());

		return objJson; 
	}		
}
