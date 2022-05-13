package omc.spop.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
 * 2020.08.10 명성태 최초작성
 **********************************************************/
@Alias("sqlStandardOperationPlugIn")
public class SqlStandardOperationPlugIn extends Base implements Jsonable {
	private String sql_std_qty_chkt_id;
	private String sql_std_qty_program_seq;
	private String project_id;
	private String wrkjob_cd;
	private String program_div_cd;
	private String program_nm;
	private String program_desc;
	private String dbio;
	private String sql_hash;
	private String sql_length;
	private String program_source_desc;
	private String dir_nm;
	private String file_nm;
	private String program_type_cd;
	private String sql_command_type_cd;
	private String dynamic_sql_yn;
	private String err_msg;
	private String reg_dt;
	
	private String max_sql_std_qty_chkt_id;
	private String db_user;
	private String developer_id;
	private String developer_nm;
	
	private List<LinkedHashMap> fileData;
	private LinkedList<JSONObject> json_list;
	
	private int check_Plan_generation_count;
	
	// SELFSQL_STD_QTY_TABLES
	private String object_owner;
	private String table_name;
	private String table_effect;
	
	// SELFSQL_STD_QTY_TAB_COLS
	private String sql_std_original_div_cd;
	private String sp_name;
	private String table_alias;
	private String column_name;
	private String column_location;
	private String column_coordinate;
	
	private String qty_chk_sql;
	
	private String dir_err_cnt;
	private String program_err_cnt;
	private String qty_chk_idt_nm;
	
	private String file_nm_table;
	private String program_nm_table;
	private String dir_nm_table;
	
	private int first_time;
	private int sql_cnt;
	private int box_cnt;
	
	private String qty_chk_idt_cd;
	
	private String abs_dir_nm;
	
	private String count_create_plan;			/* 품질 점검 지표 코드 100 - 0 : 미적용, 1 : 적용 */
	
	private String sql_std_qty_div_cd;			/* '1': 셀프, '2': 일괄   */
	private String sql_std_qty_scheduler_no;	/* 일괄점검 시 입력. 셀프 시 null */
	private String file_cnt;					/* 일괄점검 시 입력. 셀프 시 null */
	private boolean is_last;
	private String force_completion;			/* 강제 완료 */
	private String std_qty_agent_status_cd;		/* 표준점검에이전트상태코드 */
												/* > 그룹코드 : 1090
												 * > 코드값
												 * 01 : CI-AGENT장애
												 * 02 : MASTER장애
												 * 03 : CI-AGENT/MASTER장애
												 * 99 : 기타장애			*/
	
	private String parser_code;
	
	private int in_progress_sql_cnt = -1;
	
	private long plug_in_sleep_time = -1L;
	private int sql_cnt_list = 0;
	
	private long ci_total_work_time;
	
	private String sql_source_type_cd = "";
	private String gather_term_type_cd = "";
	private String gather_range_div_cd = "";
	private String gather_term_start_day = "";
	private String gather_term_end_day = "";
	private String owner_list = "";
	private String module_list = "";
	private String extra_filter_predication = "";
	private String std_qty_target_dbid = "";
	private List<String> strModuleList = null;
	private List<String> strOwnerList = null;
	
	private HashMap<String, String> wrkJobCdMap = null;
	
	private int sql_std_qty_error_seq = -1;
	private String err_sbst = "";
	
	private String sql_std_qty_status_cd = "";
	
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

	public String getSql_std_qty_chkt_id() {
		return sql_std_qty_chkt_id;
	}

	public void setSql_std_qty_chkt_id(String sql_std_qty_chkt_id) {
		this.sql_std_qty_chkt_id = sql_std_qty_chkt_id;
	}

	public String getSql_std_qty_program_seq() {
		return sql_std_qty_program_seq;
	}

	public void setSql_std_qty_program_seq(String sql_std_qty_program_seq) {
		this.sql_std_qty_program_seq = sql_std_qty_program_seq;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getProgram_div_cd() {
		return program_div_cd;
	}

	public void setProgram_div_cd(String program_div_cd) {
		this.program_div_cd = program_div_cd;
	}

	public String getProgram_nm() {
		return program_nm;
	}

	public void setProgram_nm(String program_nm) {
		this.program_nm = program_nm;
	}

	public String getProgram_desc() {
		return program_desc;
	}

	public void setProgram_desc(String program_desc) {
		this.program_desc = program_desc;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getSql_hash() {
		return sql_hash;
	}

	public void setSql_hash(String sql_hash) {
		this.sql_hash = sql_hash;
	}

	public String getSql_length() {
		return sql_length;
	}

	public void setSql_length(String sql_length) {
		this.sql_length = sql_length;
	}

	public String getProgram_source_desc() {
		return program_source_desc;
	}

	public void setProgram_source_desc(String program_source_desc) {
		this.program_source_desc = program_source_desc;
	}

	public String getDir_nm() {
		return dir_nm;
	}

	public void setDir_nm(String dir_nm) {
		this.dir_nm = dir_nm;
	}

	public String getFile_nm() {
		return file_nm;
	}

	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}

	public String getProgram_type_cd() {
		return program_type_cd;
	}

	public void setProgram_type_cd(String program_type_cd) {
		this.program_type_cd = program_type_cd;
	}

	public String getSql_command_type_cd() {
		return sql_command_type_cd;
	}

	public void setSql_command_type_cd(String sql_command_type_cd) {
		this.sql_command_type_cd = sql_command_type_cd;
	}

	public String getDynamic_sql_yn() {
		return dynamic_sql_yn;
	}

	public void setDynamic_sql_yn(String dynamic_sql_yn) {
		this.dynamic_sql_yn = dynamic_sql_yn;
	}

	public String getErr_msg() {
		return err_msg;
	}

	public void setErr_msg(String err_msg) {
		this.err_msg = err_msg;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getMax_sql_std_qty_chkt_id() {
		return max_sql_std_qty_chkt_id;
	}

	public void setMax_sql_std_qty_id(String max_sql_std_qty_chkt_id) {
		this.max_sql_std_qty_chkt_id = max_sql_std_qty_chkt_id;
	}

	public String getDb_user() {
		return db_user;
	}

	public void setDb_user(String db_user) {
		this.db_user = db_user;
	}
	
	public String getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(String developer_id) {
		this.developer_id = developer_id;
	}

	public String getDeveloper_nm() {
		return developer_nm;
	}

	public void setDeveloper_nm(String developer_nm) {
		this.developer_nm = developer_nm;
	}

	public List<LinkedHashMap> getFileData() {
		return fileData;
	}

	public void setFileData(List<LinkedHashMap> fileData) {
		this.fileData = fileData;
	}

	public LinkedList<JSONObject> getJson_list() {
		return json_list;
	}

	public void setJson_list(LinkedList<JSONObject> json_list) {
		this.json_list = json_list;
	}

	public int getCheck_Plan_generation_count() {
		return check_Plan_generation_count;
	}

	public void setCheck_Plan_generation_count(int check_Plan_generation_count) {
		this.check_Plan_generation_count = check_Plan_generation_count;
	}

	public String getObject_owner() {
		return object_owner;
	}

	public void setObject_owner(String object_owner) {
		this.object_owner = object_owner;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getTable_effect() {
		return table_effect;
	}

	public void setTable_effect(String table_effect) {
		this.table_effect = table_effect;
	}

	public String getSql_std_original_div_cd() {
		return sql_std_original_div_cd;
	}

	public void setSql_std_original_div_cd(String sql_std_original_div_cd) {
		this.sql_std_original_div_cd = sql_std_original_div_cd;
	}

	public String getSp_name() {
		return sp_name;
	}

	public void setSp_name(String sp_name) {
		this.sp_name = sp_name;
	}

	public String getTable_alias() {
		return table_alias;
	}

	public void setTable_alias(String table_alias) {
		this.table_alias = table_alias;
	}

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getColumn_location() {
		return column_location;
	}

	public void setColumn_location(String column_location) {
		this.column_location = column_location;
	}

	public String getColumn_coordinate() {
		return column_coordinate;
	}

	public void setColumn_coordinate(String column_coordinate) {
		this.column_coordinate = column_coordinate;
	}

	public String getQty_chk_sql() {
		return qty_chk_sql;
	}

	public void setQty_chk_sql(String qty_chk_sql) {
		this.qty_chk_sql = qty_chk_sql;
	}

	public String getDir_err_cnt() {
		return dir_err_cnt;
	}

	public void setDir_err_cnt(String dir_err_cnt) {
		this.dir_err_cnt = dir_err_cnt;
	}

	public String getProgram_err_cnt() {
		return program_err_cnt;
	}

	public void setProgram_err_cnt(String program_err_cnt) {
		this.program_err_cnt = program_err_cnt;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public String getFile_nm_table() {
		return file_nm_table;
	}

	public void setFile_nm_table(String file_nm_table) {
		this.file_nm_table = file_nm_table;
	}

	public String getProgram_nm_table() {
		return program_nm_table;
	}

	public void setProgram_nm_table(String program_nm_table) {
		this.program_nm_table = program_nm_table;
	}

	private void setMax_sql_std_qty_chkt_id(String max_sql_std_qty_chkt_id) {
		this.max_sql_std_qty_chkt_id = max_sql_std_qty_chkt_id;
	}

	public String getDir_nm_table() {
		return dir_nm_table;
	}

	public void setDir_nm_table(String dir_nm_table) {
		this.dir_nm_table = dir_nm_table;
	}

	public int getFirst_time() {
		return first_time;
	}

	public void setFirst_time(int first_time) {
		this.first_time = first_time;
	}

	public int getSql_cnt() {
		return sql_cnt;
	}

	public void setSql_cnt(int sql_cnt) {
		this.sql_cnt = sql_cnt;
	}

	public int getBox_cnt() {
		return box_cnt;
	}

	public void setBox_cnt(int box_cnt) {
		this.box_cnt = box_cnt;
	}

	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getAbs_dir_nm() {
		return abs_dir_nm;
	}

	public void setAbs_dir_nm(String abs_dir_nm) {
		this.abs_dir_nm = abs_dir_nm;
	}

	public String getCount_create_plan() {
		return count_create_plan;
	}

	public void setCount_create_plan(String count_create_plan) {
		this.count_create_plan = count_create_plan;
	}

	public String getSql_std_qty_div_cd() {
		return sql_std_qty_div_cd;
	}

	public void setSql_std_qty_div_cd(String sql_std_qty_div_cd) {
		this.sql_std_qty_div_cd = sql_std_qty_div_cd;
	}

	public String getSql_std_qty_scheduler_no() {
		return sql_std_qty_scheduler_no;
	}

	public void setSql_std_qty_scheduler_no(String sql_std_qty_scheduler_no) {
		this.sql_std_qty_scheduler_no = sql_std_qty_scheduler_no;
	}

	public String getFile_cnt() {
		return file_cnt;
	}

	public void setFile_cnt(String file_cnt) {
		this.file_cnt = file_cnt;
	}

	public boolean isIs_last() {
		return is_last;
	}

	public void setIs_last(boolean is_last) {
		this.is_last = is_last;
	}

	public String getForce_completion() {
		return force_completion;
	}

	public void setForce_completion(String force_completion) {
		this.force_completion = force_completion;
	}

	public String getStd_qty_agent_status_cd() {
		return std_qty_agent_status_cd;
	}

	public void setStd_qty_agent_status_cd(String std_qty_agent_status_cd) {
		this.std_qty_agent_status_cd = std_qty_agent_status_cd;
	}

	public String getParser_code() {
		return parser_code;
	}

	public void setParser_code(String parser_code) {
		this.parser_code = parser_code;
	}

	public int getIn_progress_sql_cnt() {
		return in_progress_sql_cnt;
	}

	public void setIn_progress_sql_cnt(int in_progress_sql_cnt) {
		this.in_progress_sql_cnt = in_progress_sql_cnt;
	}

	public long getPlug_in_sleep_time() {
		return plug_in_sleep_time;
	}

	public void setPlug_in_sleep_time(long plug_in_sleep_time) {
		this.plug_in_sleep_time = plug_in_sleep_time;
	}

	public int getSql_cnt_list() {
		return sql_cnt_list;
	}

	public void setSql_cnt_list(int sql_cnt_list) {
		this.sql_cnt_list = sql_cnt_list;
	}

	public long getCi_total_work_time() {
		return ci_total_work_time;
	}

	public void setCi_total_work_time(long ci_total_work_time) {
		this.ci_total_work_time = ci_total_work_time;
	}

	public String getSql_source_type_cd() {
		return sql_source_type_cd;
	}

	public void setSql_source_type_cd(String sql_source_type_cd) {
		this.sql_source_type_cd = sql_source_type_cd;
	}

	public String getGather_term_type_cd() {
		return gather_term_type_cd;
	}

	public void setGather_term_type_cd(String gather_term_type_cd) {
		this.gather_term_type_cd = gather_term_type_cd;
	}

	public String getGather_range_div_cd() {
		return gather_range_div_cd;
	}

	public void setGather_range_div_cd(String gather_range_div_cd) {
		this.gather_range_div_cd = gather_range_div_cd;
	}

	public String getGather_term_start_day() {
		return gather_term_start_day;
	}

	public void setGather_term_start_day(String gather_term_start_day) {
		this.gather_term_start_day = gather_term_start_day;
	}

	public String getGather_term_end_day() {
		return gather_term_end_day;
	}

	public void setGather_term_end_day(String gather_term_end_day) {
		this.gather_term_end_day = gather_term_end_day;
	}

	public String getOwner_list() {
		return owner_list;
	}

	public void setOwner_list(String owner_list) {
		this.owner_list = owner_list;
	}

	public String getModule_list() {
		return module_list;
	}

	public void setModule_list(String module_list) {
		this.module_list = module_list;
	}

	public String getExtra_filter_predication() {
		return extra_filter_predication;
	}

	public void setExtra_filter_predication(String extra_filter_predication) {
		this.extra_filter_predication = extra_filter_predication;
	}

	public String getStd_qty_target_dbid() {
		return std_qty_target_dbid;
	}

	public void setStd_qty_target_dbid(String std_qty_target_dbid) {
		this.std_qty_target_dbid = std_qty_target_dbid;
	}

	public List<String> getStrModuleList() {
		return strModuleList;
	}

	public void setStrModuleList(List<String> strModuleList) {
		this.strModuleList = strModuleList;
	}

	public List<String> getStrOwnerList() {
		return strOwnerList;
	}

	public void setStrOwnerList(List<String> strOwnerList) {
		this.strOwnerList = strOwnerList;
	}

	public HashMap<String, String> getWrkJobCdMap() {
		return wrkJobCdMap;
	}

	public void setWrkJobCdMap(HashMap<String, String> wrkJobCdMap) {
		this.wrkJobCdMap = wrkJobCdMap;
	}

	public int getSql_std_qty_error_seq() {
		return sql_std_qty_error_seq;
	}

	public void setSql_std_qty_error_seq(int sql_std_qty_error_seq) {
		this.sql_std_qty_error_seq = sql_std_qty_error_seq;
	}

	public String getErr_sbst() {
		return err_sbst;
	}

	public void setErr_sbst(String err_sbst) {
		this.err_sbst = err_sbst;
	}

	public String getSql_std_qty_status_cd() {
		return sql_std_qty_status_cd;
	}

	public void setSql_std_qty_status_cd(String sql_std_qty_status_cd) {
		this.sql_std_qty_status_cd = sql_std_qty_status_cd;
	}

}
