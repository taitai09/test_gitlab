package omc.spop.model;

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

@Alias("sqlStandards")
public class SQLStandards extends Base implements Jsonable {
	//private int wrkjob_cd = -1;
	private String project_nm;
	private String project_desc;
	private String del_yn;
	private String project_id;
	
	private String wrkjob_cd;
	private String developer_id;
	
	private String dml_yn;
	private String project_by_mgmt_yn;
	private String qty_chk_idt_cd;
	private String qty_chk_idt_cd_nm;
	private String qty_chk_sql;
	private String qty_chk_idt_nm;
	private String qty_chk_idt_yn;
	private int srt_ord;
	private String qty_chk_cont;
	private String slv_rsl_cont;
	private String rowStatus;
	private String dir_nm;
	private String dbio;
	private String sql_hash;
	private int sql_length = -1;
	private String except_sbst;
	private String requester;
	private String reg_dt;
	private String hard_work_type;
	private String std_qty_target_db_name;
	
	private String paramter_list;
	
	private String err_cnt;
	private String process1;
	private String process2;
	private String wrk_process_yn;
	private String wrk_complete_yn;
	
	private String sql_std_gather_dt;
	private String wrk_start_dt;
	private String wrk_end_dt;
	private String err_yn;
	private String err_sbst;
	private String err_table_name;
	private String force_close_yn;
	private String std_qty_scheduler_div_cd;
	private String sql_std_qty_div_cd;
	private String sql_id;
	private String db_name;					// rule-019에 사용
	private String sql_std_gather_day;		// rule-029에 사용
	private String diag_day;				// rule-029에 사용
	private String tot_err_cnt;				// rule-029에 사용
	private String start_day;				// rule-027에 사용
	private String end_day;					// rule-027에 사용
	
	private String std_qty_target_dbid;			//rule-021 김원재 추가
	private String sql_std_qty_scheduler_no;	//rule-021 김원재 추가
	private String user_id;						//rule-023 김원재 추가
	private String exec_status;						//진행 상태 김원재 추가
	
	public String getDeveloper_id() {
		return developer_id;
	}

	public void setDeveloper_id(String developer_id) {
		this.developer_id = developer_id;
	}

	public String getSql_std_gather_day() {
		return sql_std_gather_day;
	}

	public void setSql_std_gather_day(String sql_std_gather_day) {
		this.sql_std_gather_day = sql_std_gather_day;
	}

	public String getDiag_day() {
		return diag_day;
	}

	public void setDiag_day(String diag_day) {
		this.diag_day = diag_day;
	}

	public String getTot_err_cnt() {
		return tot_err_cnt;
	}

	public void setTot_err_cnt(String tot_err_cnt) {
		this.tot_err_cnt = tot_err_cnt;
	}

	public String getStart_day() {
		return start_day;
	}

	public void setStart_day(String start_day) {
		this.start_day = start_day;
	}

	public String getEnd_day() {
		return end_day;
	}

	public void setEnd_day(String end_day) {
		this.end_day = end_day;
	}

	public String getExec_status() {
		return exec_status;
	}

	public void setExec_status(String exec_status) {
		this.exec_status = exec_status;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStd_qty_target_dbid() {
		return std_qty_target_dbid;
	}

	public void setStd_qty_target_dbid(String std_qty_target_dbid) {
		this.std_qty_target_dbid = std_qty_target_dbid;
	}

	public String getSql_std_qty_scheduler_no() {
		return sql_std_qty_scheduler_no;
	}

	public void setSql_std_qty_scheduler_no(String sql_std_qty_scheduler_no) {
		this.sql_std_qty_scheduler_no = sql_std_qty_scheduler_no;
	}

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
	
	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getStd_qty_target_db_name() {
		return std_qty_target_db_name;
	}

	public void setStd_qty_target_db_name(String std_qty_target_db_name) {
		this.std_qty_target_db_name = std_qty_target_db_name;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getSql_std_qty_div_cd() {
		return sql_std_qty_div_cd;
	}

	public void setSql_std_qty_div_cd(String sql_std_qty_div_cd) {
		this.sql_std_qty_div_cd = sql_std_qty_div_cd;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

//	public int getWrkjob_cd() {
//		return wrkjob_cd;
//	}
//
//	public void setWrkjob_cd(int wrkjob_cd) {
//		this.wrkjob_cd = wrkjob_cd;
//	}
	
	public String getProject_nm() {
		return project_nm;
	}

	public String getStd_qty_scheduler_div_cd() {
		return std_qty_scheduler_div_cd;
	}

	public void setStd_qty_scheduler_div_cd(String std_qty_scheduler_div_cd) {
		this.std_qty_scheduler_div_cd = std_qty_scheduler_div_cd;
	}

	public String getProject_desc() {
		return project_desc;
	}

	public String getDel_yn() {
		return del_yn;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public void setProject_desc(String project_desc) {
		this.project_desc = project_desc;
	}

	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
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

	public String getDml_yn() {
		return dml_yn;
	}

	public void setDml_yn(String dml_yn) {
		this.dml_yn = dml_yn;
	}

	public String getProject_by_mgmt_yn() {
		return project_by_mgmt_yn;
	}

	public void setProject_by_mgmt_yn(String project_by_mgmt_yn) {
		this.project_by_mgmt_yn = project_by_mgmt_yn;
	}

	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getQty_chk_idt_cd_nm() {
		return qty_chk_idt_cd_nm;
	}

	public void setQty_chk_idt_cd_nm(String qty_chk_idt_cd_nm) {
		this.qty_chk_idt_cd_nm = qty_chk_idt_cd_nm;
	}

	public String getQty_chk_sql() {
		return qty_chk_sql;
	}

	public void setQty_chk_sql(String qty_chk_sql) {
		this.qty_chk_sql = qty_chk_sql;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public String getQty_chk_idt_yn() {
		return qty_chk_idt_yn;
	}

	public void setQty_chk_idt_yn(String qty_chk_idt_yn) {
		this.qty_chk_idt_yn = qty_chk_idt_yn;
	}

	public int getSrt_ord() {
		return srt_ord;
	}

	public void setSrt_ord(int srt_ord) {
		this.srt_ord = srt_ord;
	}

	public String getQty_chk_cont() {
		return qty_chk_cont;
	}

	public void setQty_chk_cont(String qty_chk_cont) {
		this.qty_chk_cont = qty_chk_cont;
	}

	public String getSlv_rsl_cont() {
		return slv_rsl_cont;
	}

	public void setSlv_rsl_cont(String slv_rsl_cont) {
		this.slv_rsl_cont = slv_rsl_cont;
	}

	public String getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

	public String getDir_nm() {
		return dir_nm;
	}

	public void setDir_nm(String dir_nm) {
		this.dir_nm = dir_nm;
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

	public int getSql_length() {
		return sql_length;
	}

	public void setSql_length(int sql_length) {
		this.sql_length = sql_length;
	}

	public String getExcept_sbst() {
		return except_sbst;
	}

	public void setExcept_sbst(String except_sbst) {
		this.except_sbst = except_sbst;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getHard_work_type() {
		return hard_work_type;
	}

	public void setHard_work_type(String hard_work_type) {
		this.hard_work_type = hard_work_type;
	}

	public String getParamter_list() {
		return paramter_list;
	}

	public void setParamter_list(String paramter_list) {
		this.paramter_list = paramter_list;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}

	public String getProcess1() {
		return process1;
	}

	public String getProcess2() {
		return process2;
	}

	public void setProcess1(String process1) {
		this.process1 = process1;
	}

	public void setProcess2(String process2) {
		this.process2 = process2;
	}

	public String getWrk_process_yn() {
		return wrk_process_yn;
	}

	public void setWrk_process_yn(String wrk_process_yn) {
		this.wrk_process_yn = wrk_process_yn;
	}

	public String getWrk_complete_yn() {
		return wrk_complete_yn;
	}

	public void setWrk_complete_yn(String wrk_complete_yn) {
		this.wrk_complete_yn = wrk_complete_yn;
	}

	public String getSql_std_gather_dt() {
		return sql_std_gather_dt;
	}

	public String getWrk_start_dt() {
		return wrk_start_dt;
	}

	public String getWrk_end_dt() {
		return wrk_end_dt;
	}

	public void setSql_std_gather_dt(String sql_std_gather_dt) {
		this.sql_std_gather_dt = sql_std_gather_dt;
	}

	public void setWrk_start_dt(String wrk_start_dt) {
		this.wrk_start_dt = wrk_start_dt;
	}

	public void setWrk_end_dt(String wrk_end_dt) {
		this.wrk_end_dt = wrk_end_dt;
	}

	public String getErr_yn() {
		return err_yn;
	}

	public String getErr_sbst() {
		return err_sbst;
	}

	public String getErr_table_name() {
		return err_table_name;
	}

	public void setErr_yn(String err_yn) {
		this.err_yn = err_yn;
	}

	public void setErr_sbst(String err_sbst) {
		this.err_sbst = err_sbst;
	}

	public void setErr_table_name(String err_table_name) {
		this.err_table_name = err_table_name;
	}

	public String getForce_close_yn() {
		return force_close_yn;
	}

	public void setForce_close_yn(String force_close_yn) {
		this.force_close_yn = force_close_yn;
	}

}
