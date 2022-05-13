package omc.spop.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2020.03.17 명성태		최초작성
 **********************************************************/

@Alias("dailyCheckDb")
public class DailyCheckDb extends Base implements Jsonable {
	private String choice_db_group_id;
	private String choice_severity_id;
	
	private String severity_color_1;
	private String severity_color_2;
	private String severity_color_3;
	private String severity_color_4;
	
	private String db_status_name;
	
	private String db_status_tabs_severity;
	
	private String check_day;
	private String check_seq;
	
	private String html;
	
	private String cd;
	private String cd_nm;
	private String color;
	
	// 1. 긴급조치, 조치필요, 확인필요, 정상 DB 건수
	private int db_critical_cnt;
	private int db_warning_cnt;
	private int db_info_cnt;
	private int db_normal_cnt;
	private int db_unchecked_cnt;
	
	// 2,3,4,5,6,7, 8, 9, 10, 25 // 11-1. DB그룹
	private String group_id;
	private String group_nm;
	private int group_cnt;
	private String db_title;
	private String dbid_check_grade_cd;
	private String check_pref_nm;
	private String check_dt;
	private int check_cnt;
	private String db_check_grade_cd;
	private String instance_check_grade_cd;
	private String space_check_grade_cd;
	private String object_check_grade_cd;
	private String statistics_check_grade_cd;
	private String long_running_check_grade_cd;
	private String alert_grade_cd;
	
	// 17-1. DB 점검결과 요약
	private String database_status_grade;
	private String expired_account_grade;
	private String modified_parameter_grade;
	private String new_created_object_grade;
	
	// 17-2. INSTANCE 점검결과 요약
	private String instance_status_grade;
	private String listener_status_grade;
	private String db_files_grade;
	private String library_cache_hit_grade;
	private String dch_grade;
	private String bch_grade;
	private String latch_hit_grade;
	private String pctp_elapsd_grade;
	private String disk_sort_grade;
	private String spu_grade;
	private String rlr_grade;
	
	// 17-3. SPACE 점검결과 요약
	private String bdsu_grade;
	private String archive_lsu_grade;
	private String alert_lsu_grade;
	private String fra_ste_grade;
	private String asm_dste_grade;
	private String tablespace_te_grade;
	private String recyclebin_object_grade;
	
	// 17-4. OBJECT 점검결과 요약
	private String invalid_object_grade;
	private String nologging_object_grade;
	private String parallel_object_grade;
	private String unusable_index_grade;
	private String chained_rows_grade;
	private String corrupt_block_grade;
	private String sequence_te_grade;
	private String foreign_kwi_grade;
	private String disabled_constraint_grade;
	
	// 17-5. STATISTICS 점검결과 요약
	private String missing_oss_grade;
	private String statistics_lt_grade;
	
	// 17-6. LONG RUNNING WORS 점검결과 요약
	private String long_running_operation_grade;
	private String long_running_job_grade;
	
	// 17-7. ALERT 점검결과 요약
	private String alert_log_error_grade;
	private String active_incident_grade;
	private String outstanding_alert_grade;
	private String dbms_scheduler_failed_grade;
	
	// --[DB상태점검-DATABASE_STATUS]
	private String inst_id;
	private String log_mode;
	private String open_mode;
	private String platform_name;
	
	// --[DB상태점검-EXPIRED_GRACE_ACCOUNT]
	private String username;
	private String account_status;
	private String expiry_date;
	private String created_date;
	private String password_expiry_remain_time;
	private String password_grace_time;
	
	// --[DB상태점검-MODIFIED_PARAMETER]
	private String num;
	private String name;
	private String before_value;
	private String after_value;
	private String update_date;
	
	// --[DB상태점검-NEW_CREATED_OBJECT]
	private String owner;
	private String object_name;
	private String subobject_name;
	private String object_type;
	private String last_ddl_time;
	
	// --[DB상태점검-INSTANCE_STATUS]
	private String inst_nm;
	private String host_nm;
	private String version;
	private String startup_time;
	private String up_time;
	private String status;
	private String archiver;
	
	// --[DB상태점검-LISTENER_STATUS]
	private String listener_nm;
	
	// --[DB상태점검-DBFILES
	private String param_db_file_cnt;
	private String create_db_file_cnt;
	private BigDecimal created_percent;
	
	// --[DB상태점검-RESOURCE_LIMIT]
	private String resource_nm;
	private String max_utilization;
	private String limit_value;
	private BigDecimal utilization_percent;
	
	// --[DB상태점검-LIBRARY_CACHE_HIT] // --[DB상태점검-DICTIONARY_CACHE_HIT] // --[DB상태점검-BUFFER_CACHE_HIT
	// --[DB상태점검-LATCH_HIT] // --[DB상태점검-PARSE_CPU_TO_PARSE_ELAPSD] // --[DB상태점검-DISK_SORT]
	// --[DB상태점검-MEMORY_USAGE]
	private BigDecimal inst_efficiency_value;
	private BigDecimal threshold_value;
	
	// --[DB상태점검-BACKGROUND_DUMP_SPACE] // --[DB상태점검-ARCHIVE_LOG_SPACE // --[DB상태점검-ALERT_LOG_SPACE]
	private BigDecimal total_space_size;
	private BigDecimal space_used;
	private BigDecimal space_used_percent;
	private BigDecimal threshold_percent;
	
	// --[DB상태점검-FRA_SPACE]
	private BigDecimal number_of_files;
	private BigDecimal total_space;
	private BigDecimal space_reclaimable;
	private BigDecimal claim_before_usage_percent;
	private BigDecimal claim_after_usage_percent;
	
	// --[DB상태점검-FRA_SPACE-FRA USAGE]
	private String file_type;
	private BigDecimal percent_space_used;
	private BigDecimal percent_space_reclaimable;
	
	// --[DB상태점검-ASM_DISKGROUP_SPACE]
	private String group_number;
	private String state;
	private BigDecimal free_space;
	
	// --[DB상태점검-TABLESPACE]
	private String tablespace_name;
	private BigDecimal tablespace_size;
	
	// --[DB상태점검-RECYCLEBIN_OBJECT]
	private String original_name;
	private String operation;
	private String type;
	private String ts_name;
	private String createtime;
	private String droptime;
	private String blocks;
	
	// --[DB상태점검-INVALID_OBJECT]
	private String script;
	
	// --[DB상태점검-NOLOGGING_OBJECT] // --[DB상태점검-PARALLEL_OBJECT]
	private String partition_name;
	private String subpartition_name;
	
	// --[DB상태점검-UNUSABLE_INDEX]
	private String table_name;
	private String index_name;
	
	// --[DB상태점검-CORRUPT_BLOCK]
	private String file_number;
	private String file_name;
	private String block_number;
	private String corruption_change_number;
	private String corruption_type;
	
	// --[DB상태점검-SEQUENCE]
	private String sequence_owner;
	private String sequence_name;
	private String min_value;
	private String max_value;
	private String increment_by;
	private String cycle_flag;
	private String order_flag;
	private String cache_size;
	private String last_number;
	
	// --[DB상태점검-FOREIGNKEYS_WITHOUT_INDEX]
	private String constraint_name;
	private String index_column_name;
	
	// --[DB상태점검-DISABLED_CONSTRAINT]
	private String constraint_type;
	
	// --[DB상태점검-CHAINED_ROWS]
	private String num_rows;
	private String chain_cnt;
	private BigDecimal chain_percent;
	
	// --[DB상태점검-MISSING_OR_STALE_STATISTICS]
	private String partitioned;
	private String inserts;
	private String updates;
	private String deletes;
	private String truncated;
	private String timestamp;
	private BigDecimal change_percent;
	private String last_analyzed;
	
	// --[DB상태점검-STATISTICS_LOCKED_TABLE]
	private String stattype_locked;
	
	// --[DB상태점검-LONG_RUNNING_OPERATION]
	private String sid;
	private String serial_number;
	private String start_time;
	private String last_update_time;
	private String elapsed_minute;
	private String remaining_minute;
	private BigDecimal done_percent;
	private String message;
	private String sql_id;
	private String sql_plan_hash_value;
	private String sql_text;
	
	// --[DB상태점검-LONG_RUNNING_JOB]
	private String session_id;
	private String job_name;
	private String elapsed_time;
	private String cpu_used;
	private String slave_process_id;
	private String running_instance;
	
	// --[DB상태점검-ALERT_LOG_ERROR]
	private String error_cd;
	private String error_cnt;
	
	// --[DB상태점검-ACTIVE_INCIDENT-PROBLEM]
	private String problem_id;
	private String problem_key;
	private String first_incident;
	private String firstinc_time;
	private String last_incident;
	private String lastinc_time;
	private String impact1;
	private String impact2;
	private String impact3;
	private String impact4;
	private String service_request;
	private String bug_number;
	
	// --[DB상태점검-ACTIVE_INCIDENT-INCIDENT]
	private String incident_id;
	private String create_time;
	private String close_time;
	private String flood_controlled;
	private String error_facility;
	private String error_number;
	private String error_arg1;
	private String error_arg2;
	private String error_arg3;
	private String error_arg4;
	private String error_arg5;
	private String error_arg6;
	private String error_arg7;
	private String error_arg8;
	private String error_arg9;
	private String error_arg10;
	private String error_arg11;
	private String error_arg12;
	private String signalling_component;
	private String signalling_subcomponent;
	private String suspect_component;
	private String suspect_subcomponent;
	private String ecid;
	private String impact;
	
	// --[DB상태점검-OUTSTANDING_ALERT]
	private String sequence_id;
	private String reason_id;
	private String reason;
	private String time_suggested;
	private String creation_time;
	private String suggested_action;
	private String advisor_name;
	private String metric_value;
	private String message_type;
	private String message_group;
	private String message_level;
	private String hosting_client_id;
	private String module_id;
	private String process_id;
	private String host_id;
	private String host_nw_addr;
	private String instance_name;
	private String instance_number;
	private String execution_context_id;
	private String error_instance_id;
	
	// --[DB상태점검-DBMS_SCHEDULER_JOB_FAILED]
	private String log_id;
	private String log_date;
	private String job_subname;
	private String req_start_date;
	private String actual_start_date;
	private String run_duration;
	private String slave_pid;
	private String credential_owner;
	private String credential_name;
	private String destination_owner;
	private String destination;
	private String additional_info;
	private String errors;
	private String output;
	
	// DB 상태 점검 현황
	private String start_first_analysis_day;
	private String end_first_analysis_day;
	private String start_first_analysis_day_bottom;
	private String end_first_analysis_day_bottom;
	private int base_period_value;
	private int isLargerThanBeginDate;
	
	private String db_full_name;
	private String dbid_check_grade_cd_situation;
	private String day;
	private String dbid_situation;
	private String check_day_situation;
	private String check_class_div_nm;
	private String check_class_div_cd;
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	public String getChoice_db_group_id() {
		return choice_db_group_id;
	}

	public void setChoice_db_group_id(String choice_db_group_id) {
		this.choice_db_group_id = choice_db_group_id;
	}

	public String getChoice_severity_id() {
		return choice_severity_id;
	}

	public void setChoice_severity_id(String choice_severity_id) {
		this.choice_severity_id = choice_severity_id;
	}

	public String getSeverity_color_1() {
		return severity_color_1;
	}

	public void setSeverity_color_1(String severity_color_1) {
		this.severity_color_1 = severity_color_1;
	}

	public String getSeverity_color_2() {
		return severity_color_2;
	}

	public void setSeverity_color_2(String severity_color_2) {
		this.severity_color_2 = severity_color_2;
	}

	public String getSeverity_color_3() {
		return severity_color_3;
	}

	public void setSeverity_color_3(String severity_color_3) {
		this.severity_color_3 = severity_color_3;
	}

	public String getSeverity_color_4() {
		return severity_color_4;
	}

	public void setSeverity_color_4(String severity_color_4) {
		this.severity_color_4 = severity_color_4;
	}

	public String getDb_status_name() {
		return db_status_name;
	}

	public void setDb_status_name(String db_status_name) {
		this.db_status_name = db_status_name;
	}

	public String getDb_status_tabs_severity() {
		return db_status_tabs_severity;
	}

	public void setDb_status_tabs_severity(String db_status_tabs_severity) {
		this.db_status_tabs_severity = db_status_tabs_severity;
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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getDb_critical_cnt() {
		return db_critical_cnt;
	}
	public void setDb_critical_cnt(int db_critical_cnt) {
		this.db_critical_cnt = db_critical_cnt;
	}
	public int getDb_warning_cnt() {
		return db_warning_cnt;
	}
	public void setDb_warning_cnt(int db_warning_cnt) {
		this.db_warning_cnt = db_warning_cnt;
	}
	public int getDb_info_cnt() {
		return db_info_cnt;
	}
	public void setDb_info_cnt(int db_info_cnt) {
		this.db_info_cnt = db_info_cnt;
	}
	public int getDb_normal_cnt() {
		return db_normal_cnt;
	}
	public void setDb_normal_cnt(int db_normal_cnt) {
		this.db_normal_cnt = db_normal_cnt;
	}
	public int getDb_unchecked_cnt() {
		return db_unchecked_cnt;
	}
	public void setDb_unchecked_cnt(int db_unchecked_cnt) {
		this.db_unchecked_cnt = db_unchecked_cnt;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_nm() {
		return group_nm;
	}
	public void setGroup_nm(String group_nm) {
		this.group_nm = group_nm;
	}
	public int getGroup_cnt() {
		return group_cnt;
	}
	public void setGroup_cnt(int group_cnt) {
		this.group_cnt = group_cnt;
	}
	public String getDb_title() {
		return db_title;
	}
	public void setDb_title(String db_title) {
		this.db_title = db_title;
	}
	public String getDbid_check_grade_cd() {
		return dbid_check_grade_cd;
	}
	public void setDbid_check_grade_cd(String dbid_check_grade_cd) {
		this.dbid_check_grade_cd = dbid_check_grade_cd;
	}
	public String getCheck_pref_nm() {
		return check_pref_nm;
	}
	public void setCheck_pref_nm(String check_pref_nm) {
		this.check_pref_nm = check_pref_nm;
	}
	public String getCheck_dt() {
		return check_dt;
	}
	public void setCheck_dt(String check_dt) {
		this.check_dt = check_dt;
	}
	public int getCheck_cnt() {
		return check_cnt;
	}
	public void setCheck_cnt(int check_cnt) {
		this.check_cnt = check_cnt;
	}
	public String getDb_check_grade_cd() {
		return db_check_grade_cd;
	}
	public void setDb_check_grade_cd(String db_check_grade_cd) {
		this.db_check_grade_cd = db_check_grade_cd;
	}
	public String getInstance_check_grade_cd() {
		return instance_check_grade_cd;
	}
	public void setInstance_check_grade_cd(String instance_check_grade_cd) {
		this.instance_check_grade_cd = instance_check_grade_cd;
	}
	public String getSpace_check_grade_cd() {
		return space_check_grade_cd;
	}
	public void setSpace_check_grade_cd(String space_check_grade_cd) {
		this.space_check_grade_cd = space_check_grade_cd;
	}
	public String getObject_check_grade_cd() {
		return object_check_grade_cd;
	}
	public void setObject_check_grade_cd(String object_check_grade_cd) {
		this.object_check_grade_cd = object_check_grade_cd;
	}
	public String getStatistics_check_grade_cd() {
		return statistics_check_grade_cd;
	}
	public void setStatistics_check_grade_cd(String statistics_check_grade_cd) {
		this.statistics_check_grade_cd = statistics_check_grade_cd;
	}
	public String getLong_running_check_grade_cd() {
		return long_running_check_grade_cd;
	}
	public void setLong_running_check_grade_cd(String long_running_check_grade_cd) {
		this.long_running_check_grade_cd = long_running_check_grade_cd;
	}
	public String getAlert_grade_cd() {
		return alert_grade_cd;
	}
	public void setAlert_grade_cd(String alert_grade_cd) {
		this.alert_grade_cd = alert_grade_cd;
	}
	public String getDatabase_status_grade() {
		return database_status_grade;
	}
	public void setDatabase_status_grade(String database_status_grade) {
		this.database_status_grade = database_status_grade;
	}
	public String getExpired_account_grade() {
		return expired_account_grade;
	}
	public void setExpired_account_grade(String expired_account_grade) {
		this.expired_account_grade = expired_account_grade;
	}
	public String getModified_parameter_grade() {
		return modified_parameter_grade;
	}
	public void setModified_parameter_grade(String modified_parameter_grade) {
		this.modified_parameter_grade = modified_parameter_grade;
	}
	public String getNew_created_object_grade() {
		return new_created_object_grade;
	}
	public void setNew_created_object_grade(String new_created_object_grade) {
		this.new_created_object_grade = new_created_object_grade;
	}
	public String getInstance_status_grade() {
		return instance_status_grade;
	}
	public void setInstance_status_grade(String instance_status_grade) {
		this.instance_status_grade = instance_status_grade;
	}
	public String getListener_status_grade() {
		return listener_status_grade;
	}
	public void setListener_status_grade(String listener_status_grade) {
		this.listener_status_grade = listener_status_grade;
	}
	public String getDb_files_grade() {
		return db_files_grade;
	}
	public void setDb_files_grade(String db_files_grade) {
		this.db_files_grade = db_files_grade;
	}
	public String getLibrary_cache_hit_grade() {
		return library_cache_hit_grade;
	}
	public void setLibrary_cache_hit_grade(String library_cache_hit_grade) {
		this.library_cache_hit_grade = library_cache_hit_grade;
	}
	public String getDch_grade() {
		return dch_grade;
	}
	public void setDch_grade(String dch_grade) {
		this.dch_grade = dch_grade;
	}
	public String getBch_grade() {
		return bch_grade;
	}
	public void setBch_grade(String bch_grade) {
		this.bch_grade = bch_grade;
	}
	public String getLatch_hit_grade() {
		return latch_hit_grade;
	}
	public void setLatch_hit_grade(String latch_hit_grade) {
		this.latch_hit_grade = latch_hit_grade;
	}
	public String getPctp_elapsd_grade() {
		return pctp_elapsd_grade;
	}
	public void setPctp_elapsd_grade(String pctp_elapsd_grade) {
		this.pctp_elapsd_grade = pctp_elapsd_grade;
	}
	public String getDisk_sort_grade() {
		return disk_sort_grade;
	}
	public void setDisk_sort_grade(String disk_sort_grade) {
		this.disk_sort_grade = disk_sort_grade;
	}
	public String getSpu_grade() {
		return spu_grade;
	}
	public void setSpu_grade(String spu_grade) {
		this.spu_grade = spu_grade;
	}
	public String getRlr_grade() {
		return rlr_grade;
	}
	public void setRlr_grade(String rlr_grade) {
		this.rlr_grade = rlr_grade;
	}
	public String getBdsu_grade() {
		return bdsu_grade;
	}
	public void setBdsu_grade(String bdsu_grade) {
		this.bdsu_grade = bdsu_grade;
	}
	public String getArchive_lsu_grade() {
		return archive_lsu_grade;
	}
	public void setArchive_lsu_grade(String archive_lsu_grade) {
		this.archive_lsu_grade = archive_lsu_grade;
	}
	public String getAlert_lsu_grade() {
		return alert_lsu_grade;
	}
	public void setAlert_lsu_grade(String alert_lsu_grade) {
		this.alert_lsu_grade = alert_lsu_grade;
	}
	public String getFra_ste_grade() {
		return fra_ste_grade;
	}
	public void setFra_ste_grade(String fra_ste_grade) {
		this.fra_ste_grade = fra_ste_grade;
	}
	public String getAsm_dste_grade() {
		return asm_dste_grade;
	}
	public void setAsm_dste_grade(String asm_dste_grade) {
		this.asm_dste_grade = asm_dste_grade;
	}
	public String getTablespace_te_grade() {
		return tablespace_te_grade;
	}
	public void setTablespace_te_grade(String tablespace_te_grade) {
		this.tablespace_te_grade = tablespace_te_grade;
	}
	public String getRecyclebin_object_grade() {
		return recyclebin_object_grade;
	}
	public void setRecyclebin_object_grade(String recyclebin_object_grade) {
		this.recyclebin_object_grade = recyclebin_object_grade;
	}
	public String getInvalid_object_grade() {
		return invalid_object_grade;
	}
	public void setInvalid_object_grade(String invalid_object_grade) {
		this.invalid_object_grade = invalid_object_grade;
	}
	public String getNologging_object_grade() {
		return nologging_object_grade;
	}
	public void setNologging_object_grade(String nologging_object_grade) {
		this.nologging_object_grade = nologging_object_grade;
	}
	public String getParallel_object_grade() {
		return parallel_object_grade;
	}
	public void setParallel_object_grade(String parallel_object_grade) {
		this.parallel_object_grade = parallel_object_grade;
	}
	public String getUnusable_index_grade() {
		return unusable_index_grade;
	}
	public void setUnusable_index_grade(String unusable_index_grade) {
		this.unusable_index_grade = unusable_index_grade;
	}
	public String getChained_rows_grade() {
		return chained_rows_grade;
	}
	public void setChained_rows_grade(String chained_rows_grade) {
		this.chained_rows_grade = chained_rows_grade;
	}
	public String getCorrupt_block_grade() {
		return corrupt_block_grade;
	}
	public void setCorrupt_block_grade(String corrupt_block_grade) {
		this.corrupt_block_grade = corrupt_block_grade;
	}
	public String getSequence_te_grade() {
		return sequence_te_grade;
	}
	public void setSequence_te_grade(String sequence_te_grade) {
		this.sequence_te_grade = sequence_te_grade;
	}
	public String getForeign_kwi_grade() {
		return foreign_kwi_grade;
	}
	public void setForeign_kwi_grade(String foreign_kwi_grade) {
		this.foreign_kwi_grade = foreign_kwi_grade;
	}
	public String getDisabled_constraint_grade() {
		return disabled_constraint_grade;
	}
	public void setDisabled_constraint_grade(String disabled_constraint_grade) {
		this.disabled_constraint_grade = disabled_constraint_grade;
	}
	public String getMissing_oss_grade() {
		return missing_oss_grade;
	}
	public void setMissing_oss_grade(String missing_oss_grade) {
		this.missing_oss_grade = missing_oss_grade;
	}
	public String getStatistics_lt_grade() {
		return statistics_lt_grade;
	}
	public void setStatistics_lt_grade(String statistics_lt_grade) {
		this.statistics_lt_grade = statistics_lt_grade;
	}
	public String getLong_running_operation_grade() {
		return long_running_operation_grade;
	}
	public void setLong_running_operation_grade(String long_running_operation_grade) {
		this.long_running_operation_grade = long_running_operation_grade;
	}
	public String getLong_running_job_grade() {
		return long_running_job_grade;
	}
	public void setLong_running_job_grade(String long_running_job_grade) {
		this.long_running_job_grade = long_running_job_grade;
	}
	public String getAlert_log_error_grade() {
		return alert_log_error_grade;
	}
	public void setAlert_log_error_grade(String alert_log_error_grade) {
		this.alert_log_error_grade = alert_log_error_grade;
	}
	public String getActive_incident_grade() {
		return active_incident_grade;
	}
	public void setActive_incident_grade(String active_incident_grade) {
		this.active_incident_grade = active_incident_grade;
	}
	public String getOutstanding_alert_grade() {
		return outstanding_alert_grade;
	}
	public void setOutstanding_alert_grade(String outstanding_alert_grade) {
		this.outstanding_alert_grade = outstanding_alert_grade;
	}
	public String getDbms_scheduler_failed_grade() {
		return dbms_scheduler_failed_grade;
	}
	public void setDbms_scheduler_failed_grade(String dbms_scheduler_failed_grade) {
		this.dbms_scheduler_failed_grade = dbms_scheduler_failed_grade;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getLog_mode() {
		return log_mode;
	}
	public void setLog_mode(String log_mode) {
		this.log_mode = log_mode;
	}
	public String getOpen_mode() {
		return open_mode;
	}
	public void setOpen_mode(String open_mode) {
		this.open_mode = open_mode;
	}
	public String getPlatform_name() {
		return platform_name;
	}
	public void setPlatform_name(String platform_name) {
		this.platform_name = platform_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getPassword_expiry_remain_time() {
		return password_expiry_remain_time;
	}
	public void setPassword_expiry_remain_time(String password_expiry_remain_time) {
		this.password_expiry_remain_time = password_expiry_remain_time;
	}
	public String getPassword_grace_time() {
		return password_grace_time;
	}
	public void setPassword_grace_time(String password_grace_time) {
		this.password_grace_time = password_grace_time;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBefore_value() {
		return before_value;
	}
	public void setBefore_value(String before_value) {
		this.before_value = before_value;
	}
	public String getAfter_value() {
		return after_value;
	}
	public void setAfter_value(String after_value) {
		this.after_value = after_value;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getObject_name() {
		return object_name;
	}
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}
	public String getSubobject_name() {
		return subobject_name;
	}
	public void setSubobject_name(String subobject_name) {
		this.subobject_name = subobject_name;
	}
	public String getObject_type() {
		return object_type;
	}
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}
	public String getLast_ddl_time() {
		return last_ddl_time;
	}
	public void setLast_ddl_time(String last_ddl_time) {
		this.last_ddl_time = last_ddl_time;
	}
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	public String getHost_nm() {
		return host_nm;
	}
	public void setHost_nm(String host_nm) {
		this.host_nm = host_nm;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStartup_time() {
		return startup_time;
	}
	public void setStartup_time(String startup_time) {
		this.startup_time = startup_time;
	}
	public String getUp_time() {
		return up_time;
	}
	public void setUp_time(String up_time) {
		this.up_time = up_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getArchiver() {
		return archiver;
	}
	public void setArchiver(String archiver) {
		this.archiver = archiver;
	}
	public String getListener_nm() {
		return listener_nm;
	}
	public void setListener_nm(String listener_nm) {
		this.listener_nm = listener_nm;
	}
	public String getParam_db_file_cnt() {
		return param_db_file_cnt;
	}
	public void setParam_db_file_cnt(String param_db_file_cnt) {
		this.param_db_file_cnt = param_db_file_cnt;
	}
	public String getCreate_db_file_cnt() {
		return create_db_file_cnt;
	}
	public void setCreate_db_file_cnt(String create_db_file_cnt) {
		this.create_db_file_cnt = create_db_file_cnt;
	}
	public BigDecimal getCreated_percent() {
		return created_percent;
	}
	public void setCreated_percent(BigDecimal created_percent) {
		this.created_percent = created_percent;
	}

	public String getResource_nm() {
		return resource_nm;
	}
	public void setResource_nm(String resource_nm) {
		this.resource_nm = resource_nm;
	}
	public String getMax_utilization() {
		return max_utilization;
	}
	public void setMax_utilization(String max_utilization) {
		this.max_utilization = max_utilization;
	}
	public String getLimit_value() {
		return limit_value;
	}
	public void setLimit_value(String limit_value) {
		this.limit_value = limit_value;
	}
	public BigDecimal getUtilization_percent() {
		return utilization_percent;
	}
	public void setUtilization_percent(BigDecimal utilization_percent) {
		this.utilization_percent = utilization_percent;
	}
	public BigDecimal getInst_efficiency_value() {
		return inst_efficiency_value;
	}
	public void setInst_efficiency_value(BigDecimal inst_efficiency_value) {
		this.inst_efficiency_value = inst_efficiency_value;
	}
	public BigDecimal getThreshold_value() {
		return threshold_value;
	}
	public void setThreshold_value(BigDecimal threshold_value) {
		this.threshold_value = threshold_value;
	}
	public BigDecimal getTotal_space_size() {
		return total_space_size;
	}
	public void setTotal_space_size(BigDecimal total_space_size) {
		this.total_space_size = total_space_size;
	}
	public BigDecimal getSpace_used() {
		return space_used;
	}
	public void setSpace_used(BigDecimal space_used) {
		this.space_used = space_used;
	}
	public BigDecimal getSpace_used_percent() {
		return space_used_percent;
	}
	public void setSpace_used_percent(BigDecimal space_used_percent) {
		this.space_used_percent = space_used_percent;
	}
	public BigDecimal getThreshold_percent() {
		return threshold_percent;
	}
	public void setThreshold_percent(BigDecimal threshold_percent) {
		this.threshold_percent = threshold_percent;
	}
	public BigDecimal getNumber_of_files() {
		return number_of_files;
	}
	public void setNumber_of_files(BigDecimal number_of_files) {
		this.number_of_files = number_of_files;
	}
	public BigDecimal getTotal_space() {
		return total_space;
	}
	public void setTotal_space(BigDecimal total_space) {
		this.total_space = total_space;
	}
	public BigDecimal getSpace_reclaimable() {
		return space_reclaimable;
	}
	public void setSpace_reclaimable(BigDecimal space_reclaimable) {
		this.space_reclaimable = space_reclaimable;
	}
	public BigDecimal getClaim_before_usage_percent() {
		return claim_before_usage_percent;
	}
	public void setClaim_before_usage_percent(BigDecimal claim_before_usage_percent) {
		this.claim_before_usage_percent = claim_before_usage_percent;
	}
	public BigDecimal getClaim_after_usage_percent() {
		return claim_after_usage_percent;
	}
	public void setClaim_after_usage_percent(BigDecimal claim_after_usage_percent) {
		this.claim_after_usage_percent = claim_after_usage_percent;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public BigDecimal getPercent_space_used() {
		return percent_space_used;
	}
	public void setPercent_space_used(BigDecimal percent_space_used) {
		this.percent_space_used = percent_space_used;
	}
	public BigDecimal getPercent_space_reclaimable() {
		return percent_space_reclaimable;
	}
	public void setPercent_space_reclaimable(BigDecimal percent_space_reclaimable) {
		this.percent_space_reclaimable = percent_space_reclaimable;
	}
	public String getGroup_number() {
		return group_number;
	}
	public void setGroup_number(String group_number) {
		this.group_number = group_number;
	}
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getFree_space() {
		return free_space;
	}
	public void setFree_space(BigDecimal free_space) {
		this.free_space = free_space;
	}
	public String getTablespace_name() {
		return tablespace_name;
	}
	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}
	public BigDecimal getTablespace_size() {
		return tablespace_size;
	}
	public void setTablespace_size(BigDecimal tablespace_size) {
		this.tablespace_size = tablespace_size;
	}
	public String getOriginal_name() {
		return original_name;
	}
	public void setOriginal_name(String original_name) {
		this.original_name = original_name;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTs_name() {
		return ts_name;
	}
	public void setTs_name(String ts_name) {
		this.ts_name = ts_name;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDroptime() {
		return droptime;
	}

	public void setDroptime(String droptime) {
		this.droptime = droptime;
	}

	public String getBlocks() {
		return blocks;
	}
	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getPartition_name() {
		return partition_name;
	}
	public void setPartition_name(String partition_name) {
		this.partition_name = partition_name;
	}
	public String getSubpartition_name() {
		return subpartition_name;
	}
	public void setSubpartition_name(String subpartition_name) {
		this.subpartition_name = subpartition_name;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getIndex_name() {
		return index_name;
	}
	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}
	public String getFile_number() {
		return file_number;
	}
	public void setFile_number(String file_number) {
		this.file_number = file_number;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getBlock_number() {
		return block_number;
	}
	public void setBlock_number(String block_number) {
		this.block_number = block_number;
	}
	public String getCorruption_change_number() {
		return corruption_change_number;
	}
	public void setCorruption_change_number(String corruption_change_number) {
		this.corruption_change_number = corruption_change_number;
	}
	public String getCorruption_type() {
		return corruption_type;
	}
	public void setCorruption_type(String corruption_type) {
		this.corruption_type = corruption_type;
	}
	public String getSequence_owner() {
		return sequence_owner;
	}
	public void setSequence_owner(String sequence_owner) {
		this.sequence_owner = sequence_owner;
	}
	public String getSequence_name() {
		return sequence_name;
	}
	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}
	public String getMin_value() {
		return min_value;
	}
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}
	public String getMax_value() {
		return max_value;
	}
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}
	public String getIncrement_by() {
		return increment_by;
	}
	public void setIncrement_by(String increment_by) {
		this.increment_by = increment_by;
	}
	public String getCycle_flag() {
		return cycle_flag;
	}
	public void setCycle_flag(String cycle_flag) {
		this.cycle_flag = cycle_flag;
	}
	public String getOrder_flag() {
		return order_flag;
	}
	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}
	public String getCache_size() {
		return cache_size;
	}
	public void setCache_size(String cache_size) {
		this.cache_size = cache_size;
	}
	public String getLast_number() {
		return last_number;
	}
	public void setLast_number(String last_number) {
		this.last_number = last_number;
	}
	public String getConstraint_name() {
		return constraint_name;
	}
	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}
	public String getIndex_column_name() {
		return index_column_name;
	}
	public void setIndex_column_name(String index_column_name) {
		this.index_column_name = index_column_name;
	}
	public String getConstraint_type() {
		return constraint_type;
	}
	public void setConstraint_type(String constraint_type) {
		this.constraint_type = constraint_type;
	}
	public String getNum_rows() {
		return num_rows;
	}
	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}
	public String getChain_cnt() {
		return chain_cnt;
	}
	public void setChain_cnt(String chain_cnt) {
		this.chain_cnt = chain_cnt;
	}
	public BigDecimal getChain_percent() {
		return chain_percent;
	}
	public void setChain_percent(BigDecimal chain_percent) {
		this.chain_percent = chain_percent;
	}
	public String getPartitioned() {
		return partitioned;
	}
	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}
	public String getInserts() {
		return inserts;
	}
	public void setInserts(String inserts) {
		this.inserts = inserts;
	}
	public String getUpdates() {
		return updates;
	}
	public void setUpdates(String updates) {
		this.updates = updates;
	}
	public String getDeletes() {
		return deletes;
	}
	public void setDeletes(String deletes) {
		this.deletes = deletes;
	}
	public String getTruncated() {
		return truncated;
	}
	public void setTruncated(String truncated) {
		this.truncated = truncated;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public BigDecimal getChange_percent() {
		return change_percent;
	}
	public void setChange_percent(BigDecimal change_percent) {
		this.change_percent = change_percent;
	}
	public String getLast_analyzed() {
		return last_analyzed;
	}
	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
	}
	public String getStattype_locked() {
		return stattype_locked;
	}
	public void setStattype_locked(String stattype_locked) {
		this.stattype_locked = stattype_locked;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
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
	public BigDecimal getDone_percent() {
		return done_percent;
	}
	public void setDone_percent(BigDecimal done_percent) {
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
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getJob_name() {
		return job_name;
	}
	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}
	public String getElapsed_time() {
		return elapsed_time;
	}
	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	public String getCpu_used() {
		return cpu_used;
	}
	public void setCpu_used(String cpu_used) {
		this.cpu_used = cpu_used;
	}
	public String getSlave_process_id() {
		return slave_process_id;
	}
	public void setSlave_process_id(String slave_process_id) {
		this.slave_process_id = slave_process_id;
	}
	public String getRunning_instance() {
		return running_instance;
	}
	public void setRunning_instance(String running_instance) {
		this.running_instance = running_instance;
	}
	public String getError_cd() {
		return error_cd;
	}
	public void setError_cd(String error_cd) {
		this.error_cd = error_cd;
	}
	public String getError_cnt() {
		return error_cnt;
	}
	public void setError_cnt(String error_cnt) {
		this.error_cnt = error_cnt;
	}
	public String getProblem_id() {
		return problem_id;
	}
	public void setProblem_id(String problem_id) {
		this.problem_id = problem_id;
	}
	public String getProblem_key() {
		return problem_key;
	}
	public void setProblem_key(String problem_key) {
		this.problem_key = problem_key;
	}
	public String getFirst_incident() {
		return first_incident;
	}
	public void setFirst_incident(String first_incident) {
		this.first_incident = first_incident;
	}
	public String getFirstinc_time() {
		return firstinc_time;
	}
	public void setFirstinc_time(String firstinc_time) {
		this.firstinc_time = firstinc_time;
	}
	public String getLast_incident() {
		return last_incident;
	}
	public void setLast_incident(String last_incident) {
		this.last_incident = last_incident;
	}
	public String getLastinc_time() {
		return lastinc_time;
	}
	public void setLastinc_time(String lastinc_time) {
		this.lastinc_time = lastinc_time;
	}
	public String getImpact1() {
		return impact1;
	}
	public void setImpact1(String impact1) {
		this.impact1 = impact1;
	}
	public String getImpact2() {
		return impact2;
	}
	public void setImpact2(String impact2) {
		this.impact2 = impact2;
	}
	public String getImpact3() {
		return impact3;
	}
	public void setImpact3(String impact3) {
		this.impact3 = impact3;
	}
	public String getImpact4() {
		return impact4;
	}
	public void setImpact4(String impact4) {
		this.impact4 = impact4;
	}
	public String getService_request() {
		return service_request;
	}
	public void setService_request(String service_request) {
		this.service_request = service_request;
	}
	public String getBug_number() {
		return bug_number;
	}
	public void setBug_number(String bug_number) {
		this.bug_number = bug_number;
	}
	public String getIncident_id() {
		return incident_id;
	}
	public void setIncident_id(String incident_id) {
		this.incident_id = incident_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getFlood_controlled() {
		return flood_controlled;
	}
	public void setFlood_controlled(String flood_controlled) {
		this.flood_controlled = flood_controlled;
	}
	public String getError_facility() {
		return error_facility;
	}
	public void setError_facility(String error_facility) {
		this.error_facility = error_facility;
	}
	public String getError_number() {
		return error_number;
	}
	public void setError_number(String error_number) {
		this.error_number = error_number;
	}
	public String getError_arg1() {
		return error_arg1;
	}
	public void setError_arg1(String error_arg1) {
		this.error_arg1 = error_arg1;
	}
	public String getError_arg2() {
		return error_arg2;
	}
	public void setError_arg2(String error_arg2) {
		this.error_arg2 = error_arg2;
	}
	public String getError_arg3() {
		return error_arg3;
	}
	public void setError_arg3(String error_arg3) {
		this.error_arg3 = error_arg3;
	}
	public String getError_arg4() {
		return error_arg4;
	}
	public void setError_arg4(String error_arg4) {
		this.error_arg4 = error_arg4;
	}
	public String getError_arg5() {
		return error_arg5;
	}
	public void setError_arg5(String error_arg5) {
		this.error_arg5 = error_arg5;
	}
	public String getError_arg6() {
		return error_arg6;
	}
	public void setError_arg6(String error_arg6) {
		this.error_arg6 = error_arg6;
	}
	public String getError_arg7() {
		return error_arg7;
	}
	public void setError_arg7(String error_arg7) {
		this.error_arg7 = error_arg7;
	}
	public String getError_arg8() {
		return error_arg8;
	}
	public void setError_arg8(String error_arg8) {
		this.error_arg8 = error_arg8;
	}
	public String getError_arg9() {
		return error_arg9;
	}
	public void setError_arg9(String error_arg9) {
		this.error_arg9 = error_arg9;
	}
	public String getError_arg10() {
		return error_arg10;
	}
	public void setError_arg10(String error_arg10) {
		this.error_arg10 = error_arg10;
	}
	public String getError_arg11() {
		return error_arg11;
	}
	public void setError_arg11(String error_arg11) {
		this.error_arg11 = error_arg11;
	}
	public String getError_arg12() {
		return error_arg12;
	}
	public void setError_arg12(String error_arg12) {
		this.error_arg12 = error_arg12;
	}
	public String getSignalling_component() {
		return signalling_component;
	}
	public void setSignalling_component(String signalling_component) {
		this.signalling_component = signalling_component;
	}
	public String getSignalling_subcomponent() {
		return signalling_subcomponent;
	}
	public void setSignalling_subcomponent(String signalling_subcomponent) {
		this.signalling_subcomponent = signalling_subcomponent;
	}
	public String getSuspect_component() {
		return suspect_component;
	}
	public void setSuspect_component(String suspect_component) {
		this.suspect_component = suspect_component;
	}
	public String getSuspect_subcomponent() {
		return suspect_subcomponent;
	}
	public void setSuspect_subcomponent(String suspect_subcomponent) {
		this.suspect_subcomponent = suspect_subcomponent;
	}
	public String getEcid() {
		return ecid;
	}
	public void setEcid(String ecid) {
		this.ecid = ecid;
	}
	public String getImpact() {
		return impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
	}
	public String getSequence_id() {
		return sequence_id;
	}
	public void setSequence_id(String sequence_id) {
		this.sequence_id = sequence_id;
	}
	public String getReason_id() {
		return reason_id;
	}
	public void setReason_id(String reason_id) {
		this.reason_id = reason_id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTime_suggested() {
		return time_suggested;
	}
	public void setTime_suggested(String time_suggested) {
		this.time_suggested = time_suggested;
	}
	public String getCreation_time() {
		return creation_time;
	}
	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}
	public String getSuggested_action() {
		return suggested_action;
	}
	public void setSuggested_action(String suggested_action) {
		this.suggested_action = suggested_action;
	}
	public String getAdvisor_name() {
		return advisor_name;
	}
	public void setAdvisor_name(String advisor_name) {
		this.advisor_name = advisor_name;
	}
	public String getMetric_value() {
		return metric_value;
	}
	public void setMetric_value(String metric_value) {
		this.metric_value = metric_value;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getMessage_group() {
		return message_group;
	}
	public void setMessage_group(String message_group) {
		this.message_group = message_group;
	}
	public String getMessage_level() {
		return message_level;
	}
	public void setMessage_level(String message_level) {
		this.message_level = message_level;
	}
	public String getHosting_client_id() {
		return hosting_client_id;
	}
	public void setHosting_client_id(String hosting_client_id) {
		this.hosting_client_id = hosting_client_id;
	}
	public String getModule_id() {
		return module_id;
	}
	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}
	public String getProcess_id() {
		return process_id;
	}
	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}
	public String getHost_id() {
		return host_id;
	}
	public void setHost_id(String host_id) {
		this.host_id = host_id;
	}
	public String getHost_nw_addr() {
		return host_nw_addr;
	}
	public void setHost_nw_addr(String host_nw_addr) {
		this.host_nw_addr = host_nw_addr;
	}
	public String getInstance_name() {
		return instance_name;
	}
	public void setInstance_name(String instance_name) {
		this.instance_name = instance_name;
	}
	public String getInstance_number() {
		return instance_number;
	}
	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}
	public String getExecution_context_id() {
		return execution_context_id;
	}
	public void setExecution_context_id(String execution_context_id) {
		this.execution_context_id = execution_context_id;
	}
	public String getError_instance_id() {
		return error_instance_id;
	}
	public void setError_instance_id(String error_instance_id) {
		this.error_instance_id = error_instance_id;
	}
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getLog_date() {
		return log_date;
	}
	public void setLog_date(String log_date) {
		this.log_date = log_date;
	}
	public String getJob_subname() {
		return job_subname;
	}
	public void setJob_subname(String job_subname) {
		this.job_subname = job_subname;
	}
	public String getReq_start_date() {
		return req_start_date;
	}
	public void setReq_start_date(String req_start_date) {
		this.req_start_date = req_start_date;
	}
	public String getActual_start_date() {
		return actual_start_date;
	}
	public void setActual_start_date(String actual_start_date) {
		this.actual_start_date = actual_start_date;
	}
	public String getRun_duration() {
		return run_duration;
	}
	public void setRun_duration(String run_duration) {
		this.run_duration = run_duration;
	}
	public String getSlave_pid() {
		return slave_pid;
	}
	public void setSlave_pid(String slave_pid) {
		this.slave_pid = slave_pid;
	}
	public String getCredential_owner() {
		return credential_owner;
	}
	public void setCredential_owner(String credential_owner) {
		this.credential_owner = credential_owner;
	}
	public String getCredential_name() {
		return credential_name;
	}
	public void setCredential_name(String credential_name) {
		this.credential_name = credential_name;
	}
	public String getDestination_owner() {
		return destination_owner;
	}
	public void setDestination_owner(String destination_owner) {
		this.destination_owner = destination_owner;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getAdditional_info() {
		return additional_info;
	}
	public void setAdditional_info(String additional_info) {
		this.additional_info = additional_info;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	
	public String getStart_first_analysis_day() {
		return start_first_analysis_day;
	}

	public void setStart_first_analysis_day(String start_first_analysis_day) {
		this.start_first_analysis_day = start_first_analysis_day;
	}

	public String getEnd_first_analysis_day() {
		return end_first_analysis_day;
	}

	public void setEnd_first_analysis_day(String end_first_analysis_day) {
		this.end_first_analysis_day = end_first_analysis_day;
	}

	public String getStart_first_analysis_day_bottom() {
		return start_first_analysis_day_bottom;
	}

	public void setStart_first_analysis_day_bottom(String start_first_analysis_day_bottom) {
		this.start_first_analysis_day_bottom = start_first_analysis_day_bottom;
	}

	public String getEnd_first_analysis_day_bottom() {
		return end_first_analysis_day_bottom;
	}

	public void setEnd_first_analysis_day_bottom(String end_first_analysis_day_bottom) {
		this.end_first_analysis_day_bottom = end_first_analysis_day_bottom;
	}

	public int getBase_period_value() {
		return base_period_value;
	}

	public void setBase_period_value(int base_period_value) {
		this.base_period_value = base_period_value;
	}

	public int getIsLargerThanBeginDate() {
		return isLargerThanBeginDate;
	}

	public void setIsLargerThanBeginDate(int isLargerThanBeginDate) {
		this.isLargerThanBeginDate = isLargerThanBeginDate;
	}

	public String getDb_full_name() {
		return db_full_name;
	}

	public void setDb_full_name(String db_full_name) {
		this.db_full_name = db_full_name;
	}

	public String getDbid_check_grade_cd_situation() {
		return dbid_check_grade_cd_situation;
	}

	public void setDbid_check_grade_cd_situation(String dbid_check_grade_cd_situation) {
		this.dbid_check_grade_cd_situation = dbid_check_grade_cd_situation;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDbid_situation() {
		return dbid_situation;
	}

	public void setDbid_situation(String dbid_situation) {
		this.dbid_situation = dbid_situation;
	}

	public String getCheck_day_situation() {
		return check_day_situation;
	}

	public void setCheck_day_situation(String check_day_situation) {
		this.check_day_situation = check_day_situation;
	}

	public String getCheck_class_div_nm() {
		return check_class_div_nm;
	}

	public void setCheck_class_div_nm(String check_class_div_nm) {
		this.check_class_div_nm = check_class_div_nm;
	}

	public String getCheck_class_div_cd() {
		return check_class_div_cd;
	}

	public void setCheck_class_div_cd(String check_class_div_cd) {
		this.check_class_div_cd = check_class_div_cd;
	}

	public String outlineButton(DailyCheckDb dailyCheckDb, String fieldName, int type) {
		StringBuffer outlineHtml = new StringBuffer("<div class='outline_type_" + type + "'>\n");
		
		switch(type) {
		case 1:
			outlineHtml.append(exceptionRegistrationTag(dailyCheckDb, fieldName, type));
			break;
		case 2:
			outlineHtml.append(exceptionRegistrationTag(dailyCheckDb, fieldName, type));
			outlineHtml.append(scriptCopyTag(dailyCheckDb, fieldName, type));
			break;
		}
		
		outlineHtml.append("</div>\n");
		
		return outlineHtml.toString();
	}
	
	private String exceptionRegistrationTag(DailyCheckDb dailyCheckDb, String fieldName, int type) {
		return "\t<button class=\"button_type_" + type + "\" onclick=\"exceptionRegistration(" + 
					dailyCheckDb.getDbid() + "," + dailyCheckDb.getCheck_day() + "," + dailyCheckDb.getCheck_seq() + ",'" + fieldName +
					"'); return false;\">예외 등록</button>\n";
	}
	
	private String scriptCopyTag(DailyCheckDb dailyCheckDb, String fieldName, int type) {
		return "\t<button class=\"button_type_" + type + "\" onclick=\"exceptionRegistration(" + 
					dailyCheckDb.getDbid() + "," + dailyCheckDb.getCheck_day() + "," + dailyCheckDb.getCheck_seq() + ",'" + fieldName +
					"'); return false;\">SCRIPT 복사</button>\n";
	}
	
	/** Table Information ****************************************************/
	public String tableStyle1(int level, boolean isWidth) {
		return "\n\t\t<table class=\"om" + level + (isWidth ? "_width" : "") + "\" >\n" +
				"\t\t\t<thead>\n\t\t\t\t<tr>\n";
	}
	
	/**
	 * 
	 * @param position
	 * @param value
	 * @param align 0 : left, 1 : center, 2 : right
	 * @return
	 */
	public String tableStyle1Body(int position, String value, int width, int align) {
		StringBuffer html = new StringBuffer();
		StringBuffer alignText = new StringBuffer();
		
		switch(align) {
		case 0: alignText.append(" style=").append("\"text-align:left;").append("white-space:pre-line;"); break;
		case 1: alignText.append(" style=").append("\"text-align:center;"); break;
		case 2: alignText.append(" style=").append("\"text-align:right;"); break;
		}
		
		if(width > 0) {
//			alignText.append("word-break:inherit;");
			alignText.append("width:" + width + "px;");
		} else {
			alignText.append("word-break:break-all;");
		}
		
		alignText.append("\"");
		
		if(value == null || value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n");
			break;
		}
		
		return html.toString();
	}
	
	/**
	 * 
	 * @param position
	 * @param value
	 * @param align 0 : left, 1 : center, 2 : right
	 * @return
	 */
	public String tableStyle1Body(int position, BigDecimal value, int width, int align) {
		StringBuffer html = new StringBuffer();
		StringBuffer alignText = new StringBuffer();
		
		switch(align) {
		case 0: alignText.append(" style=").append("\"text-align:left;").append("white-space:pre-line;"); break;
		case 1: alignText.append(" style=").append("\"text-align:center;"); break;
		case 2: alignText.append(" style=").append("\"text-align:right;"); break;
		}
		
		if(width > 0) {
//			alignText.append("word-break:inherit;");
			alignText.append("width:" + width + "px;");
		} else {
			alignText.append("word-break:break-all;");
		}
		
		alignText.append("\"");
		
		if(value == null) {
			value = BigDecimal.ZERO;
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n");
			break;
		}
		
		return html.toString();
	}
	
	/**
	 * @param colspan	style
	 * @param align
	 * @return			html
	 */
	public String tableStyleNoDataClose(int colspan) {
		StringBuffer html = new StringBuffer();
		String value = "점검결과 정상입니다.";
		
		html.append("\t\t\t<td colspan=\"").append(colspan).append("\" class=\"");
		html.append("om-colspan");
		html.append("\"");
		html.append(">").append(value).append("</td>\n\t\t</table>");
		
		return html.toString();
	}
	
	public String tableStyle1BodyClose(int position, String value, int width, int align) {
		StringBuffer html = new StringBuffer();
		StringBuffer alignText = new StringBuffer();
		
		switch(align) {
		case 0: alignText.append(" style=").append("\"text-align:left;").append("white-space:pre-line;"); break;
		case 1: alignText.append(" style=").append("\"text-align:center;"); break;
		case 2: alignText.append(" style=").append("\"text-align:right;"); break;
		}
		
		if(width > 0) {
//			alignText.append("word-break:inherit;");
			alignText.append("width:" + width + "px;");
		} else {
			alignText.append("word-break:break-all;");
		}
		
		alignText.append("\"");
		
		if(value == null || value.equalsIgnoreCase("NULL")) {
			value = "";
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n\t\t</table>\n");
			break;
		}
		
		return html.toString();
	}
	
	public String tableStyle1BodyClose(int position, BigDecimal value, int width, int align) {
		StringBuffer html = new StringBuffer();
		StringBuffer alignText = new StringBuffer();
		
		switch(align) {
		case 0: alignText.append(" style=").append("\"text-align:left;").append("white-space:pre-line;"); break;
		case 1: alignText.append(" style=").append("\"text-align:center;"); break;
		case 2: alignText.append(" style=").append("\"text-align:right;"); break;
		}
		
		if(width > 0) {
//			alignText.append("word-break:inherit;");
			alignText.append("width:" + width + "px;");
		} else {
			alignText.append("word-break:break-all;");
		}
		
		alignText.append("\"");
		
		if(value == null) {
			value = BigDecimal.ZERO;
		}
		
		switch(position) {
		case 1:	// Left
			html.append("\t\t\t<tr>\n\t\t\t\t<td class=\"");
			html.append("om-left-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 2:	// Center
			html.append("\t<td class=\"");
			html.append("om-center-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>");
			break;
		case 3:	// Right
			html.append("\t<td class=\"");
			html.append("om-right-close");
			html.append("\"");
			html.append(alignText);
			html.append(">").append(value).append("</td>\n\t\t\t</tr>\n\t\t</table>\n");
			break;
		}
		
		return html.toString();
	}
	
	/** End Table Information ************************************************/
}
