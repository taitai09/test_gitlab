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

/***********************************************************
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Alias("projectSqlQtyChkRule")
public class ProjectSqlQtyChkRule extends Base implements Jsonable {

	private String project_id;
	private String project_nm;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String sql_std_qty_target_yn;
	
	private String apply_yn;
	private String dml_yn;
	private String qty_chk_idt_cd;
	private String qty_chk_idt_cd_nm;
	private String qty_chk_sql;
	private String qty_chk_idt_nm;
	private String qty_chk_idt_yn;
	private String srt_ord;
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
	
	private String count_create_plan;	/* 품질 점검 지표 코드 100 - 0 : 미적용, 1 : 적용 */

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getSql_std_qty_target_yn() {
		return sql_std_qty_target_yn;
	}

	public void setSql_std_qty_target_yn(String sql_std_qty_target_yn) {
		this.sql_std_qty_target_yn = sql_std_qty_target_yn;
	}

	public String getApply_yn() {
		return apply_yn;
	}

	public void setApply_yn(String apply_yn) {
		this.apply_yn = apply_yn;
	}

	public String getDml_yn() {
		return dml_yn;
	}

	public void setDml_yn(String dml_yn) {
		this.dml_yn = dml_yn;
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

	public String getSrt_ord() {
		return srt_ord;
	}

	public void setSrt_ord(String srt_ord) {
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

	public String getCount_create_plan() {
		return count_create_plan;
	}

	public void setCount_create_plan(String count_create_plan) {
		this.count_create_plan = count_create_plan;
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
