package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2019.09.24	명성태	최초작성
 * DbioLoadSql 클래스에서 파생
 * 특징 : 프로젝트 ID 별로 정의
 **********************************************************/

@Alias("projectUnitLoadSql")
public class ProjectUnitLoadSql extends Base implements Jsonable {
	private String file_no;
	private String exec_seq;
	private String explain_exec_seq;
	private String query_seq;
	private String sql_text;
	private String reg_dt;
	private String plan_yn;
	private String note;
	
	private String owner;
	private String table_name;
	private String access_path;
	private String current_schema;
	
	private String executing_cnt;
	
	private String project_id;
	private String sql_hash;
	private String sql_length;
	private String program_source_desc;
	
	private String update10235;
	
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}
	public String getExec_seq() {
		return exec_seq;
	}
	public void setExec_seq(String exec_seq) {
		this.exec_seq = exec_seq;
	}
	public String getExplain_exec_seq() {
		return explain_exec_seq;
	}
	public void setExplain_exec_seq(String explain_exec_seq) {
		this.explain_exec_seq = explain_exec_seq;
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
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getPlan_yn() {
		return plan_yn;
	}
	public void setPlan_yn(String plan_yn) {
		this.plan_yn = plan_yn;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}	
	public String getAccess_path() {
		return access_path;
	}
	public void setAccess_path(String access_path) {
		this.access_path = access_path;
	}
	public String getCurrent_schema() {
		return current_schema;
	}
	public void setCurrent_schema(String current_schema) {
		this.current_schema = current_schema;
	}
	public String getExecuting_cnt() {
		return executing_cnt;
	}
	public void setExecuting_cnt(String executing_cnt) {
		this.executing_cnt = executing_cnt;
	}		
	
	public String getProject_id() {
		return project_id;
	}
	public String getSql_hash() {
		return sql_hash;
	}
	public String getSql_length() {
		return sql_length;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public void setSql_hash(String sql_hash) {
		this.sql_hash = sql_hash;
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
	public String getUpdate10235() {
		return update10235;
	}
	public void setUpdate10235(String update10235) {
		this.update10235 = update10235;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("rnum", StringUtil.parseInt(this.getRnum(),0));
		objJson.put("file_no", StringUtil.parseDouble(this.getFile_no(),0));
		objJson.put("exec_seq", this.getExec_seq());
		objJson.put("explain_exec_seq", this.getExplain_exec_seq());
		objJson.put("query_seq", StringUtil.parseInt(this.getQuery_seq(),0));
		objJson.put("sql_text", this.getSql_text());
		objJson.put("reg_dt", this.getReg_dt());
		objJson.put("plan_yn", this.getPlan_yn());
		objJson.put("note", this.getNote());
		objJson.put("executing_cnt", this.getExecuting_cnt());
		objJson.put("project_id", this.getProject_id());
		objJson.put("sql_hash", this.getSql_hash());
		objJson.put("sql_length", this.getSql_length());
		objJson.put("program_source_desc", this.getProgram_source_desc());

		return objJson;
	}
}