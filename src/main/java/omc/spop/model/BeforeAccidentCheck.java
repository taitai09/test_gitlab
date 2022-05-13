package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.10.23	이원식	최초작성
 **********************************************************/

@Alias("beforeAccidentCheck")
public class BeforeAccidentCheck extends Base implements Jsonable {
	
	private String bfac_chk_no;
	private String bfac_chk_request_dt;
	private String bfac_chk_rqtr_id;
	private String bfac_chk_source;
	private String bfac_chk_id;
	private String bfac_chk_dt;
	private String bfac_chk_result_sbst;
	
	private String wrkjob_mgr_nm;
	private String wrkjob_mgr_dept_nm;
	private String tuning_complete_dt;
	private String perfr_id;
	
	private String bfac_chkr_id;
	private String tr_cd;
	private String dbio;
	
	public String getBfac_chk_no() {
		return bfac_chk_no;
	}
	public void setBfac_chk_no(String bfac_chk_no) {
		this.bfac_chk_no = bfac_chk_no;
	}
	public String getBfac_chk_request_dt() {
		return bfac_chk_request_dt;
	}
	public void setBfac_chk_request_dt(String bfac_chk_request_dt) {
		this.bfac_chk_request_dt = bfac_chk_request_dt;
	}
	public String getBfac_chk_rqtr_id() {
		return bfac_chk_rqtr_id;
	}
	public void setBfac_chk_rqtr_id(String bfac_chk_rqtr_id) {
		this.bfac_chk_rqtr_id = bfac_chk_rqtr_id;
	}
	public String getBfac_chk_source() {
		return bfac_chk_source;
	}
	public void setBfac_chk_source(String bfac_chk_source) {
		this.bfac_chk_source = bfac_chk_source;
	}
	public String getBfac_chk_id() {
		return bfac_chk_id;
	}
	public void setBfac_chk_id(String bfac_chk_id) {
		this.bfac_chk_id = bfac_chk_id;
	}
	public String getBfac_chk_dt() {
		return bfac_chk_dt;
	}
	public void setBfac_chk_dt(String bfac_chk_dt) {
		this.bfac_chk_dt = bfac_chk_dt;
	}
	public String getBfac_chk_result_sbst() {
		return bfac_chk_result_sbst;
	}
	public void setBfac_chk_result_sbst(String bfac_chk_result_sbst) {
		this.bfac_chk_result_sbst = bfac_chk_result_sbst;
	}	
	public String getWrkjob_mgr_nm() {
		return wrkjob_mgr_nm;
	}
	public void setWrkjob_mgr_nm(String wrkjob_mgr_nm) {
		this.wrkjob_mgr_nm = wrkjob_mgr_nm;
	}
	public String getWrkjob_mgr_dept_nm() {
		return wrkjob_mgr_dept_nm;
	}
	public void setWrkjob_mgr_dept_nm(String wrkjob_mgr_dept_nm) {
		this.wrkjob_mgr_dept_nm = wrkjob_mgr_dept_nm;
	}
	public String getTuning_complete_dt() {
		return tuning_complete_dt;
	}
	public void setTuning_complete_dt(String tuning_complete_dt) {
		this.tuning_complete_dt = tuning_complete_dt;
	}
	public String getPerfr_id() {
		return perfr_id;
	}
	public void setPerfr_id(String perfr_id) {
		this.perfr_id = perfr_id;
	}	
	public String getBfac_chkr_id() {
		return bfac_chkr_id;
	}
	public void setBfac_chkr_id(String bfac_chkr_id) {
		this.bfac_chkr_id = bfac_chkr_id;
	}	
	public String getTr_cd() {
		return tr_cd;
	}
	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}
	public String getDbio() {
		return dbio;
	}
	public void setDbio(String dbio) {
		this.dbio = dbio;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("bfac_chk_no",this.getBfac_chk_no());
		objJson.put("db_name",this.getDb_name());
		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("bfac_chk_request_dt",this.getBfac_chk_request_dt());
		objJson.put("bfac_chk_rqtr_id",this.getBfac_chk_rqtr_id());
		objJson.put("bfac_chk_source",this.getBfac_chk_source());
		objJson.put("bfac_chk_id",this.getBfac_chk_id());
		objJson.put("bfac_chk_dt",this.getBfac_chk_dt());
		objJson.put("bfac_chk_result_sbst",this.getBfac_chk_result_sbst());
		objJson.put("wrkjob_mgr_nm",this.getWrkjob_mgr_nm());
		objJson.put("wrkjob_mgr_dept_nm",this.getWrkjob_mgr_dept_nm());
		objJson.put("tuning_complete_dt",this.getTuning_complete_dt());
		objJson.put("perfr_id",this.getPerfr_id());
		objJson.put("bfac_chkr_id",this.getBfac_chkr_id());

		return objJson;
	}		
}