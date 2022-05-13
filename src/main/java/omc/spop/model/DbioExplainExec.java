package omc.spop.model;

import java.util.Map;

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
 * 2017.10.19	이원식	최초작성
 **********************************************************/

@Alias("dbioExplainExec")
public class DbioExplainExec extends Base implements Jsonable {
	
	private String file_no;
	private String explain_exec_seq;
	private String explain_exec_note;
	private String plan_create_cnt;
	private String plan_error_cnt;
	private String plan_no_exec_cnt;
	private String reg_dt;
	private String explain_info;
	
	private String query_seq;
	private String sql_text;
	private String note;
	
	private String suc_cnt;
	private String err_cnt;
	
	private String sql_cnt;
	private String plan_exec_cnt;
	private String complete_yn;
	
	private String exec_end_dt;
	
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}
	public String getExplain_exec_seq() {
		return explain_exec_seq;
	}
	public void setExplain_exec_seq(String explain_exec_seq) {
		this.explain_exec_seq = explain_exec_seq;
	}
	public String getExplain_exec_note() {
		return explain_exec_note;
	}
	public void setExplain_exec_note(String explain_exec_note) {
		this.explain_exec_note = explain_exec_note;
	}
	public String getPlan_create_cnt() {
		return plan_create_cnt;
	}
	public void setPlan_create_cnt(String plan_create_cnt) {
		this.plan_create_cnt = plan_create_cnt;
	}
	public String getPlan_error_cnt() {
		return plan_error_cnt;
	}
	public void setPlan_error_cnt(String plan_error_cnt) {
		this.plan_error_cnt = plan_error_cnt;
	}
	public String getPlan_no_exec_cnt() {
		return plan_no_exec_cnt;
	}
	public void setPlan_no_exec_cnt(String plan_no_exec_cnt) {
		this.plan_no_exec_cnt = plan_no_exec_cnt;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getExplain_info() {
		return explain_info;
	}
	public void setExplain_info(String explain_info) {
		this.explain_info = explain_info;
	}	
	public String getQuery_seq() {
		return query_seq;
	}
	public void setQuery_seq(String query_seq) {
		this.query_seq = query_seq;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}	
	public String getSuc_cnt() {
		return suc_cnt;
	}
	public void setSuc_cnt(String suc_cnt) {
		this.suc_cnt = suc_cnt;
	}
	public String getErr_cnt() {
		return err_cnt;
	}
	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}
	public String getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	public String getPlan_exec_cnt() {
		return plan_exec_cnt;
	}
	public void setPlan_exec_cnt(String plan_exec_cnt) {
		this.plan_exec_cnt = plan_exec_cnt;
	}
	public String getComplete_yn() {
		return complete_yn;
	}
	public void setComplete_yn(String complete_yn) {
		this.complete_yn = complete_yn;
	}
	public String getExec_end_dt() {
		return exec_end_dt;
	}
	public void setExec_end_dt(String exec_end_dt) {
		this.exec_end_dt = exec_end_dt;
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