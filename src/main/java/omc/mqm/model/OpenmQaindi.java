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

@Alias("openmQaindi")
public class OpenmQaindi extends Base implements Jsonable {
	
	private String project_id;
	private String qty_chk_idt_cd;
	private String qty_chk_idt_nm;
	private String qty_chk_idt_cd_nm;
	private String mdi_pcs_cd;
	private String qty_idt_tp_cd;
	private String qty_chk_tg_yn;
	private String qty_chk_tp_cd;
	private int    srt_ord;
	private String qty_chk_cont;
	private String slv_rsl_cont;
	private String qty_chk_result_tbl_nm;
	private String excel_output_yn;
	private int    output_start_row;
	private int    output_start_col;
	
	// 품질검토작업용 추가
	private String qty_chk_idt;
	private String qty_result_sheet_nm;
	private String dml_yn;
	private int    qty_inspection_cnt;
	private String qty_chk_sql;
	private int    mdi_pcs_cd_cnt;
	
	
	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

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

	public String getQty_chk_idt_cd_nm() {
		return qty_chk_idt_cd_nm;
	}

	public void setQty_chk_idt_cd_nm(String qty_chk_idt_cd_nm) {
		this.qty_chk_idt_cd_nm = qty_chk_idt_cd_nm;
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

	public String getQty_chk_tg_yn() {
		return qty_chk_tg_yn;
	}

	public void setQty_chk_tg_yn(String qty_chk_tg_yn) {
		this.qty_chk_tg_yn = qty_chk_tg_yn;
	}

	public String getQty_chk_tp_cd() {
		return qty_chk_tp_cd;
	}

	public void setQty_chk_tp_cd(String qty_chk_tp_cd) {
		this.qty_chk_tp_cd = qty_chk_tp_cd;
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

	public String getQty_chk_result_tbl_nm() {
		return qty_chk_result_tbl_nm;
	}

	public void setQty_chk_result_tbl_nm(String qty_chk_result_tbl_nm) {
		this.qty_chk_result_tbl_nm = qty_chk_result_tbl_nm;
	}

	public String getExcel_output_yn() {
		return excel_output_yn;
	}

	public void setExcel_output_yn(String excel_output_yn) {
		this.excel_output_yn = excel_output_yn;
	}

	public int getOutput_start_row() {
		return output_start_row;
	}

	public void setOutput_start_row(int output_start_row) {
		this.output_start_row = output_start_row;
	}
	
	public int getOutput_start_col() {
		return output_start_col;
	}

	public void setOutput_start_col(int output_start_col) {
		this.output_start_col = output_start_col;
	}

	public String getQty_chk_idt() {
		return qty_chk_idt;
	}

	public void setQty_chk_idt(String qty_chk_idt) {
		this.qty_chk_idt = qty_chk_idt;
	}

	public String getQty_result_sheet_nm() {
		return qty_result_sheet_nm;
	}

	public void setQty_result_sheet_nm(String qty_result_sheet_nm) {
		this.qty_result_sheet_nm = qty_result_sheet_nm;
	}

	public String getDml_yn() {
		return dml_yn;
	}

	public void setDml_yn(String dml_yn) {
		this.dml_yn = dml_yn;
	}

	public int getQty_inspection_cnt() {
		return qty_inspection_cnt;
	}

	public void setQty_inspection_cnt(int qty_inspection_cnt) {
		this.qty_inspection_cnt = qty_inspection_cnt;
	}

	public String getQty_chk_sql() {
		return qty_chk_sql;
	}

	public void setQty_chk_sql(String qty_chk_sql) {
		this.qty_chk_sql = qty_chk_sql;
	}
	
	public int getMdi_pcs_cd_cnt() {
		return mdi_pcs_cd_cnt;
	}

	public void setMdi_pcs_cd_cnt(int mdi_pcs_cd_cnt) {
		this.mdi_pcs_cd_cnt = mdi_pcs_cd_cnt;
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
