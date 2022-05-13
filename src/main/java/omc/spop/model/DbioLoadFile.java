package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.10.19	이원식	최초작성
 **********************************************************/

@Alias("dbioLoadFile")
public class DbioLoadFile extends Base implements Jsonable {
	private String file_no;
	
	private String file_nm;
	private String query_load_cnt;
	private String file_load_note;
	private String reg_dt;
	private String file_info;
	
	private String explain_exec_seq;
	private String plan_desc;
	private String access_path_exec_yn;
	private String sql_cnt;
	private String explain_exec_yn;
	
	public String getFile_no() {
		return file_no;
	}
	public void setFile_no(String file_no) {
		this.file_no = file_no;
	}
	public String getFile_nm() {
		return file_nm;
	}
	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}
	public String getQuery_load_cnt() {
		return query_load_cnt;
	}
	public void setQuery_load_cnt(String query_load_cnt) {
		this.query_load_cnt = query_load_cnt;
	}
	public String getFile_load_note() {
		return file_load_note;
	}
	public void setFile_load_note(String file_load_note) {
		this.file_load_note = file_load_note;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getFile_info() {
		return file_info;
	}
	public void setFile_info(String file_info) {
		this.file_info = file_info;
	}	
	public String getExplain_exec_seq() {
		return explain_exec_seq;
	}
	public void setExplain_exec_seq(String explain_exec_seq) {
		this.explain_exec_seq = explain_exec_seq;
	}
	public String getPlan_desc() {
		return plan_desc;
	}
	public void setPlan_desc(String plan_desc) {
		this.plan_desc = plan_desc;
	}
	public String getAccess_path_exec_yn() {
		return access_path_exec_yn;
	}
	public void setAccess_path_exec_yn(String access_path_exec_yn) {
		this.access_path_exec_yn = access_path_exec_yn;
	}	
	public String getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	public String getExplain_exec_yn() {
		return explain_exec_yn;
	}
	public void setExplain_exec_yn(String explain_exec_yn) {
		this.explain_exec_yn = explain_exec_yn;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("file_no", StringUtil.parseDouble(this.getFile_no(),0));
		objJson.put("dbio", this.getDbid());
		objJson.put("db_name", this.getDb_name());
		objJson.put("file_nm", this.getFile_nm());
		objJson.put("query_load_cnt", StringUtil.parseInt(this.getQuery_load_cnt(),0));
		objJson.put("file_load_note", this.getFile_load_note());
		objJson.put("reg_dt", this.getReg_dt());
		objJson.put("file_info", this.getFile_info());
		
		objJson.put("explain_exec_seq", StringUtil.parseInt(this.getExplain_exec_seq(),0));
		objJson.put("plan_desc", this.getPlan_desc());
		objJson.put("sql_cnt", StringUtil.parseInt(this.getSql_cnt(),0));
		objJson.put("explain_exec_yn", this.getExplain_exec_yn());
		objJson.put("access_path_exec_yn", this.getAccess_path_exec_yn());

		return objJson;
	}		
}