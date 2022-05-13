package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.01.10	이원식	최초작성
 **********************************************************/

@Alias("sqlPerfImpluenceAnalysis")
public class SqlPerfImpluenceAnalysis extends Base implements Jsonable {
	private String sql_perf_impl_anal_no;
	
	private String begin_snap_id;
	private String end_snap_id;
	private String begin_snap_time;
	private String end_snap_time;	
	private String immediately_yn;
	private String anal_work_exec_day;
	private String anal_work_exec_time;
	private String anal_work_exec_dt;
	private String work_start_dt;
	private String work_end_dt;
	
	private String edit_yn;
	private String table_cnt;
	private String tableOwnerArry;
	private String tableNameArry;
	
	public String getSql_perf_impl_anal_no() {
		return sql_perf_impl_anal_no;
	}
	public void setSql_perf_impl_anal_no(String sql_perf_impl_anal_no) {
		this.sql_perf_impl_anal_no = sql_perf_impl_anal_no;
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
	public String getBegin_snap_time() {
		return begin_snap_time;
	}
	public void setBegin_snap_time(String begin_snap_time) {
		this.begin_snap_time = begin_snap_time;
	}
	public String getEnd_snap_time() {
		return end_snap_time;
	}
	public void setEnd_snap_time(String end_snap_time) {
		this.end_snap_time = end_snap_time;
	}
	public String getImmediately_yn() {
		return immediately_yn;
	}
	public void setImmediately_yn(String immediately_yn) {
		this.immediately_yn = immediately_yn;
	}
	public String getAnal_work_exec_day() {
		return anal_work_exec_day;
	}
	public void setAnal_work_exec_day(String anal_work_exec_day) {
		this.anal_work_exec_day = anal_work_exec_day;
	}
	public String getAnal_work_exec_time() {
		return anal_work_exec_time;
	}
	public void setAnal_work_exec_time(String anal_work_exec_time) {
		this.anal_work_exec_time = anal_work_exec_time;
	}
	public String getAnal_work_exec_dt() {
		return anal_work_exec_dt;
	}
	public void setAnal_work_exec_dt(String anal_work_exec_dt) {
		this.anal_work_exec_dt = anal_work_exec_dt;
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
	public String getEdit_yn() {
		return edit_yn;
	}
	public void setEdit_yn(String edit_yn) {
		this.edit_yn = edit_yn;
	}	
	public String getTable_cnt() {
		return table_cnt;
	}
	public void setTable_cnt(String table_cnt) {
		this.table_cnt = table_cnt;
	}
	public String getTableOwnerArry() {
		return tableOwnerArry;
	}
	public void setTableOwnerArry(String tableOwnerArry) {
		this.tableOwnerArry = tableOwnerArry;
	}
	public String getTableNameArry() {
		return tableNameArry;
	}
	public void setTableNameArry(String tableNameArry) {
		this.tableNameArry = tableNameArry;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("sql_perf_impl_anal_no", StringUtil.parseInt(this.getSql_perf_impl_anal_no(),0));
		objJson.put("dbid", this.getDbid());
		objJson.put("db_name", this.getDb_name());
		objJson.put("begin_snap_id", this.getBegin_snap_id());
		objJson.put("end_snap_id", this.getEnd_snap_id());
		objJson.put("begin_snap_time", this.getBegin_snap_time());
		objJson.put("end_snap_time", this.getEnd_snap_time());		
		objJson.put("immediately_yn", this.getImmediately_yn());
		objJson.put("anal_work_exec_day", this.getAnal_work_exec_day());
		objJson.put("anal_work_exec_time", this.getAnal_work_exec_time());
		objJson.put("anal_work_exec_dt", this.getAnal_work_exec_dt());
		objJson.put("work_start_dt", this.getWork_start_dt());
		objJson.put("work_end_dt", this.getWork_end_dt());
		objJson.put("table_cnt", StringUtil.parseInt(this.getTable_cnt(),0));

		return objJson;
	}		
}