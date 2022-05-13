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

@Alias("sqlDiagnosisReport")
public class SQLDiagnosisReport extends Base implements Jsonable{
	
	private String qty_chk_idt_nm;
	private String err_cnt;
	private String qty_chk_idt_cd;
	private String diag_dt;
	private String gather_term;
	
	private String std_qty_target_dbid;
	private String sql_std_qty_scheduler_no;
	private String job_scheduler_nm;
	private String sql_id;
	
	private String sql_std_qty_program_seq;
	private String sql_std_qty_chkt_id;
	private String sql_cnt;
	private String in_progress_sql_cnt;

	private String exec_status;
	private String exec_status_cd;
	
	
	public String getExec_status() {
		return exec_status;
	}
	public void setExec_status(String exec_status) {
		this.exec_status = exec_status;
	}
	public String getExec_status_cd() {
		return exec_status_cd;
	}
	public void setExec_status_cd(String exec_status_cd) {
		this.exec_status_cd = exec_status_cd;
	}
	public String getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	public String getIn_progress_sql_cnt() {
		return in_progress_sql_cnt;
	}
	public void setIn_progress_sql_cnt(String in_progress_sql_cnt) {
		this.in_progress_sql_cnt = in_progress_sql_cnt;
	}
	public String getSql_std_qty_program_seq() {
		return sql_std_qty_program_seq;
	}
	public void setSql_std_qty_program_seq(String sql_std_qty_program_seq) {
		this.sql_std_qty_program_seq = sql_std_qty_program_seq;
	}
	public String getSql_std_qty_chkt_id() {
		return sql_std_qty_chkt_id;
	}
	public void setSql_std_qty_chkt_id(String sql_std_qty_chkt_id) {
		this.sql_std_qty_chkt_id = sql_std_qty_chkt_id;
	}
	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}
	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}
	public String getErr_cnt() {
		return err_cnt;
	}
	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}
	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}
	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}
	public String getDiag_dt() {
		return diag_dt;
	}
	public void setDiag_dt(String diag_dt) {
		this.diag_dt = diag_dt;
	}
	public String getGather_term() {
		return gather_term;
	}
	public void setGather_term(String gather_term) {
		this.gather_term = gather_term;
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
	public String getJob_scheduler_nm() {
		return job_scheduler_nm;
	}
	public void setJob_scheduler_nm(String job_scheduler_nm) {
		this.job_scheduler_nm = job_scheduler_nm;
	}
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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
}
