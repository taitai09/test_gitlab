package omc.mqm.model;

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
 * 2019.05.13	임승률	최초작성
 **********************************************************/

@Alias("openmQtyChkSql")
public class OpenmQtyChkSql extends Base implements Jsonable {
	
	private String qty_chk_idt_cd;
	private String qty_chk_idt_nm;
	private String qty_chk_sql;
	private String dml_yn;
	private String project_id;
	private String project_nm;
	private String apply_yn;
	private String mdi_pcs_nm;
	private String qty_chk_idt_cD;
	private String qty_chk_idt_nM;
	private String qty_idt_tp_nm;
	private String qty_chk_tg_yn;
	private String qty_chk_tp_nm;
	private String srt_ord;
	private String mdi_pcs_cd;
	private String qty_idt_tp_cd;
	private String qty_chk_tp_cd;
	private String qty_chk_cont;
	private String slv_rsl_cont;
	
	
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

	public String getApply_yn() {
		return apply_yn;
	}

	public void setApply_yn(String apply_yn) {
		this.apply_yn = apply_yn;
	}

	public String getMdi_pcs_nm() {
		return mdi_pcs_nm;
	}

	public void setMdi_pcs_nm(String mdi_pcs_nm) {
		this.mdi_pcs_nm = mdi_pcs_nm;
	}

	public String getQty_chk_idt_cD() {
		return qty_chk_idt_cD;
	}

	public void setQty_chk_idt_cD(String qty_chk_idt_cD) {
		this.qty_chk_idt_cD = qty_chk_idt_cD;
	}

	public String getQty_chk_idt_nM() {
		return qty_chk_idt_nM;
	}

	public void setQty_chk_idt_nM(String qty_chk_idt_nM) {
		this.qty_chk_idt_nM = qty_chk_idt_nM;
	}

	public String getQty_idt_tp_nm() {
		return qty_idt_tp_nm;
	}

	public void setQty_idt_tp_nm(String qty_idt_tp_nm) {
		this.qty_idt_tp_nm = qty_idt_tp_nm;
	}

	public String getQty_chk_tg_yn() {
		return qty_chk_tg_yn;
	}

	public void setQty_chk_tg_yn(String qty_chk_tg_yn) {
		this.qty_chk_tg_yn = qty_chk_tg_yn;
	}

	public String getQty_chk_tp_nm() {
		return qty_chk_tp_nm;
	}

	public void setQty_chk_tp_nm(String qty_chk_tp_nm) {
		this.qty_chk_tp_nm = qty_chk_tp_nm;
	}

	public String getSrt_ord() {
		return srt_ord;
	}

	public void setSrt_ord(String srt_ord) {
		this.srt_ord = srt_ord;
	}

	public String getMdi_pcs_cd() {
		return mdi_pcs_cd;
	}

	public void setMdi_pcs_cd(String mdi_pcs_cd) {
		this.mdi_pcs_cd = mdi_pcs_cd;
	}

	public String getQty_idt_tp_cd() {
		return qty_idt_tp_cd;
	}

	public void setQty_idt_tp_cd(String qty_idt_tp_cd) {
		this.qty_idt_tp_cd = qty_idt_tp_cd;
	}

	public String getQty_chk_tp_cd() {
		return qty_chk_tp_cd;
	}

	public void setQty_chk_tp_cd(String qty_chk_tp_cd) {
		this.qty_chk_tp_cd = qty_chk_tp_cd;
	}

	// 품질검토 작업용
	private String qty_chk_result_tbl_nm;
	
	public String getQty_chk_idt_cd() {
		return qty_chk_idt_cd;
	}

	public void setQty_chk_idt_cd(String qty_chk_idt_cd) {
		this.qty_chk_idt_cd = qty_chk_idt_cd;
	}

	public String getQty_chk_idt_nm() {
		return qty_chk_idt_nm;
	}

	public void setQty_chk_idt_nm(String qty_chk_idt_nm) {
		this.qty_chk_idt_nm = qty_chk_idt_nm;
	}

	public String getQty_chk_sql() {
		return qty_chk_sql;
	}
	
	public void setQty_chk_sql(String qty_chk_sql) {
		this.qty_chk_sql = qty_chk_sql;
	}

	public String getDml_yn() {
		return dml_yn;
	}

	public void setDml_yn(String dml_yn) {
		this.dml_yn = dml_yn;
	}
	
	public String getQty_chk_result_tbl_nm() {
		return qty_chk_result_tbl_nm;
	}

	public void setQty_chk_result_tbl_nm(String qty_chk_result_tbl_nm) {
		this.qty_chk_result_tbl_nm = qty_chk_result_tbl_nm;
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
