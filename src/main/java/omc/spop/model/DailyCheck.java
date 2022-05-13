package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.18	이원식	최초작성
 **********************************************************/

@Alias("dailyCheck")
public class DailyCheck extends Base implements Jsonable {
	private String check_day;
	private String check_seq;	
	
	private String check_dt;
	private String health;
	private String ordering;
	private String advisor_recommendation;
	private String database_status;
	private String expired_grace_account;
	private String modified_parameter;
	private String new_created_object;
	private String instance_status;
	private String listener_status;
	private String dbfiles;
	private String library_cache_hit;
	private String dictionary_cache_hit;
	private String buffer_cache_hit;
	private String latch_hit;
	private String parse_cpu_to_parse_elapsd;
	private String disk_sort;
	private String memory_usage;
	private String resource_limit;
	private String alert_log_error;
	private String active_incident;
	private String outstanding_alert;
	private String dbms_scheduler_job_failed;
	private String background_dump_space;
	private String archive_log_space;
	private String alert_log_space;
	private String fra_space;
	private String asm_diskgroup_space;
	private String tablespace;
	private String recyclebin_object;
	private String invalid_object;
	private String nologging_object;
	private String parallel_object;
	private String unusable_index;
	private String chained_rows;
	private String corrupt_block;
	private String sequence;
	private String foreignkeys_without_index;
	private String disabled_constraint;
	private String missing_or_stale_statistics;
	private String statistics_locked_table;
	private String long_running_operation;
	private String long_running_job;
	
	private String check_item_name;
	
	
	public String getCheck_dt() {
		return check_dt;
	}
	public void setCheck_dt(String check_dt) {
		this.check_dt = check_dt;
	}
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
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getOrdering() {
		return ordering;
	}
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}
	public String getAdvisor_recommendation() {
		return advisor_recommendation;
	}
	public void setAdvisor_recommendation(String advisor_recommendation) {
		this.advisor_recommendation = advisor_recommendation;
	}
	public String getDatabase_status() {
		return database_status;
	}
	public void setDatabase_status(String database_status) {
		this.database_status = database_status;
	}
	public String getExpired_grace_account() {
		return expired_grace_account;
	}
	public void setExpired_grace_account(String expired_grace_account) {
		this.expired_grace_account = expired_grace_account;
	}
	public String getModified_parameter() {
		return modified_parameter;
	}
	public void setModified_parameter(String modified_parameter) {
		this.modified_parameter = modified_parameter;
	}
	public String getNew_created_object() {
		return new_created_object;
	}
	public void setNew_created_object(String new_created_object) {
		this.new_created_object = new_created_object;
	}
	public String getInstance_status() {
		return instance_status;
	}
	public void setInstance_status(String instance_status) {
		this.instance_status = instance_status;
	}
	public String getListener_status() {
		return listener_status;
	}
	public void setListener_status(String listener_status) {
		this.listener_status = listener_status;
	}
	public String getDbfiles() {
		return dbfiles;
	}
	public void setDbfiles(String dbfiles) {
		this.dbfiles = dbfiles;
	}
	public String getLibrary_cache_hit() {
		return library_cache_hit;
	}
	public void setLibrary_cache_hit(String library_cache_hit) {
		this.library_cache_hit = library_cache_hit;
	}
	public String getDictionary_cache_hit() {
		return dictionary_cache_hit;
	}
	public void setDictionary_cache_hit(String dictionary_cache_hit) {
		this.dictionary_cache_hit = dictionary_cache_hit;
	}
	public String getBuffer_cache_hit() {
		return buffer_cache_hit;
	}
	public void setBuffer_cache_hit(String buffer_cache_hit) {
		this.buffer_cache_hit = buffer_cache_hit;
	}
	public String getLatch_hit() {
		return latch_hit;
	}
	public void setLatch_hit(String latch_hit) {
		this.latch_hit = latch_hit;
	}
	public String getParse_cpu_to_parse_elapsd() {
		return parse_cpu_to_parse_elapsd;
	}
	public void setParse_cpu_to_parse_elapsd(String parse_cpu_to_parse_elapsd) {
		this.parse_cpu_to_parse_elapsd = parse_cpu_to_parse_elapsd;
	}
	public String getDisk_sort() {
		return disk_sort;
	}
	public void setDisk_sort(String disk_sort) {
		this.disk_sort = disk_sort;
	}
	public String getMemory_usage() {
		return memory_usage;
	}
	public void setMemory_usage(String memory_usage) {
		this.memory_usage = memory_usage;
	}
	public String getResource_limit() {
		return resource_limit;
	}
	public void setResource_limit(String resource_limit) {
		this.resource_limit = resource_limit;
	}
	public String getAlert_log_error() {
		return alert_log_error;
	}
	public void setAlert_log_error(String alert_log_error) {
		this.alert_log_error = alert_log_error;
	}
	public String getActive_incident() {
		return active_incident;
	}
	public void setActive_incident(String active_incident) {
		this.active_incident = active_incident;
	}
	public String getOutstanding_alert() {
		return outstanding_alert;
	}
	public void setOutstanding_alert(String outstanding_alert) {
		this.outstanding_alert = outstanding_alert;
	}
	public String getDbms_scheduler_job_failed() {
		return dbms_scheduler_job_failed;
	}
	public void setDbms_scheduler_job_failed(String dbms_scheduler_job_failed) {
		this.dbms_scheduler_job_failed = dbms_scheduler_job_failed;
	}
	public String getBackground_dump_space() {
		return background_dump_space;
	}
	public void setBackground_dump_space(String background_dump_space) {
		this.background_dump_space = background_dump_space;
	}
	public String getArchive_log_space() {
		return archive_log_space;
	}
	public void setArchive_log_space(String archive_log_space) {
		this.archive_log_space = archive_log_space;
	}
	public String getAlert_log_space() {
		return alert_log_space;
	}
	public void setAlert_log_space(String alert_log_space) {
		this.alert_log_space = alert_log_space;
	}
	public String getFra_space() {
		return fra_space;
	}
	public void setFra_space(String fra_space) {
		this.fra_space = fra_space;
	}
	public String getAsm_diskgroup_space() {
		return asm_diskgroup_space;
	}
	public void setAsm_diskgroup_space(String asm_diskgroup_space) {
		this.asm_diskgroup_space = asm_diskgroup_space;
	}
	public String getTablespace() {
		return tablespace;
	}
	public void setTablespace(String tablespace) {
		this.tablespace = tablespace;
	}
	public String getRecyclebin_object() {
		return recyclebin_object;
	}
	public void setRecyclebin_object(String recyclebin_object) {
		this.recyclebin_object = recyclebin_object;
	}
	public String getInvalid_object() {
		return invalid_object;
	}
	public void setInvalid_object(String invalid_object) {
		this.invalid_object = invalid_object;
	}
	public String getNologging_object() {
		return nologging_object;
	}
	public void setNologging_object(String nologging_object) {
		this.nologging_object = nologging_object;
	}
	public String getParallel_object() {
		return parallel_object;
	}
	public void setParallel_object(String parallel_object) {
		this.parallel_object = parallel_object;
	}
	public String getUnusable_index() {
		return unusable_index;
	}
	public void setUnusable_index(String unusable_index) {
		this.unusable_index = unusable_index;
	}
	public String getChained_rows() {
		return chained_rows;
	}
	public void setChained_rows(String chained_rows) {
		this.chained_rows = chained_rows;
	}
	public String getCorrupt_block() {
		return corrupt_block;
	}
	public void setCorrupt_block(String corrupt_block) {
		this.corrupt_block = corrupt_block;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getForeignkeys_without_index() {
		return foreignkeys_without_index;
	}
	public void setForeignkeys_without_index(String foreignkeys_without_index) {
		this.foreignkeys_without_index = foreignkeys_without_index;
	}
	public String getDisabled_constraint() {
		return disabled_constraint;
	}
	public void setDisabled_constraint(String disabled_constraint) {
		this.disabled_constraint = disabled_constraint;
	}
	public String getMissing_or_stale_statistics() {
		return missing_or_stale_statistics;
	}
	public void setMissing_or_stale_statistics(String missing_or_stale_statistics) {
		this.missing_or_stale_statistics = missing_or_stale_statistics;
	}
	public String getStatistics_locked_table() {
		return statistics_locked_table;
	}
	public void setStatistics_locked_table(String statistics_locked_table) {
		this.statistics_locked_table = statistics_locked_table;
	}
	public String getLong_running_operation() {
		return long_running_operation;
	}
	public void setLong_running_operation(String long_running_operation) {
		this.long_running_operation = long_running_operation;
	}
	public String getLong_running_job() {
		return long_running_job;
	}
	public void setLong_running_job(String long_running_job) {
		this.long_running_job = long_running_job;
	}	
	public String getCheck_item_name() {
		return check_item_name;
	}
	public void setCheck_item_name(String check_item_name) {
		this.check_item_name = check_item_name;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_dt",this.getCheck_dt());
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());		
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("health",this.getHealth());
		objJson.put("ordering",this.getOrdering());		
		objJson.put("advisor_recommendation", this.getAdvisor_recommendation());
		objJson.put("database_status", this.getDatabase_status());
		objJson.put("expired_grace_account", this.getExpired_grace_account());
		objJson.put("modified_parameter", this.getModified_parameter());
		objJson.put("new_created_object", this.getNew_created_object());
		objJson.put("instance_status", this.getInstance_status());
		objJson.put("listener_status", this.getListener_status());
		objJson.put("dbfiles", this.getDbfiles());
		objJson.put("library_cache_hit", this.getLibrary_cache_hit());
		objJson.put("dictionary_cache_hit", this.getDictionary_cache_hit());
		objJson.put("buffer_cache_hit", this.getBuffer_cache_hit());
		objJson.put("latch_hit", this.getLatch_hit());
		objJson.put("parse_cpu_to_parse_elapsd", this.getParse_cpu_to_parse_elapsd());
		objJson.put("disk_sort", this.getDisk_sort());
		objJson.put("memory_usage", this.getMemory_usage());
		objJson.put("resource_limit", this.getResource_limit());
		objJson.put("alert_log_error", this.getAlert_log_error());
		objJson.put("active_incident", this.getActive_incident());
		objJson.put("outstanding_alert", this.getOutstanding_alert());
		objJson.put("dbms_scheduler_job_failed", this.getDbms_scheduler_job_failed());
		objJson.put("background_dump_space", this.getBackground_dump_space());
		objJson.put("archive_log_space", this.getArchive_log_space());
		objJson.put("alert_log_space", this.getAlert_log_space());
		objJson.put("fra_space", this.getFra_space());
		objJson.put("asm_diskgroup_space", this.getAsm_diskgroup_space());
		objJson.put("tablespace", this.getTablespace());
		objJson.put("recyclebin_object", this.getRecyclebin_object());
		objJson.put("invalid_object", this.getInvalid_object());
		objJson.put("nologging_object", this.getNologging_object());
		objJson.put("parallel_object", this.getParallel_object());
		objJson.put("unusable_index", this.getUnusable_index());
		objJson.put("chained_rows", this.getChained_rows());
		objJson.put("corrupt_block", this.getCorrupt_block());
		objJson.put("sequence", this.getSequence());
		objJson.put("foreignkeys_without_index", this.getForeignkeys_without_index());
		objJson.put("disabled_constraint", this.getDisabled_constraint());
		objJson.put("missing_or_stale_statistics", this.getMissing_or_stale_statistics());
		objJson.put("statistics_locked_table", this.getStatistics_locked_table());
		objJson.put("long_running_operation", this.getLong_running_operation());
		objJson.put("long_running_job", this.getLong_running_job());

		return objJson;
	}		
}
