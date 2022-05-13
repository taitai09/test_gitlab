package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.01.16	이원식	최초작성
 **********************************************************/

@Alias("selftunSqlStatistics")
public class SelftunSqlStatistics extends Base implements Jsonable {
	
    private String selftun_query_seq;
    private String sql_id;
    private String plan_hash_value;
    private String parse_calls;
    private String buffer_gets;
    private String rows_processed;
    private String elapsed_time;
    private String cpu_time;
    private String user_io_wait_time;
    private String fetches;
    private String disk_reads;
    private String direct_writes;
    
	public String getSelftun_query_seq() {
		return selftun_query_seq;
	}
	public void setSelftun_query_seq(String selftun_query_seq) {
		this.selftun_query_seq = selftun_query_seq;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getPlan_hash_value() {
		return plan_hash_value;
	}
	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}
	public String getParse_calls() {
		return parse_calls;
	}
	public void setParse_calls(String parse_calls) {
		this.parse_calls = parse_calls;
	}
	public String getBuffer_gets() {
		return buffer_gets;
	}
	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}
	public String getRows_processed() {
		return rows_processed;
	}
	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}
	public String getElapsed_time() {
		return elapsed_time;
	}
	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	public String getCpu_time() {
		return cpu_time;
	}
	public void setCpu_time(String cpu_time) {
		this.cpu_time = cpu_time;
	}
	public String getUser_io_wait_time() {
		return user_io_wait_time;
	}
	public void setUser_io_wait_time(String user_io_wait_time) {
		this.user_io_wait_time = user_io_wait_time;
	}
	public String getFetches() {
		return fetches;
	}
	public void setFetches(String fetches) {
		this.fetches = fetches;
	}
	public String getDisk_reads() {
		return disk_reads;
	}
	public void setDisk_reads(String disk_reads) {
		this.disk_reads = disk_reads;
	}
	public String getDirect_writes() {
		return direct_writes;
	}
	public void setDirect_writes(String direct_writes) {
		this.direct_writes = direct_writes;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		objJson.put("dbid",this.getDbid());
		objJson.put("selftun_query_seq",this.getSelftun_query_seq());
		objJson.put("sql_id",this.getSql_id());
		objJson.put("plan_hash_value",this.getPlan_hash_value());
		objJson.put("parse_calls",this.getParse_calls());
		objJson.put("buffer_gets",this.getBuffer_gets());
		objJson.put("rows_processed",this.getRows_processed());
		objJson.put("elapsed_time",this.getElapsed_time());
		objJson.put("cpu_time",this.getCpu_time());
		objJson.put("user_io_wait_time",this.getUser_io_wait_time());
		objJson.put("fetches",this.getFetches());
		objJson.put("disk_reads",this.getDisk_reads());
		objJson.put("direct_writes",this.getDirect_writes());

		return objJson;
	}		
}