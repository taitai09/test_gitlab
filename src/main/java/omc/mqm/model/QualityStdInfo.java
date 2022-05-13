package omc.mqm.model;

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
 * 2019.05.22 	임호경 	최초작성
 **********************************************************/

@Alias("qualityStdInfo")
public class QualityStdInfo extends Base implements Jsonable {

	private String cd;
	private String cd_nm;
	/*엔터티유형관리*/
	private String ent_type_cd;
	private String ref_ent_type_nm;
	private String ent_type_nm;
	private String tbl_type_nm;
	private String tbl_type_cd;
	private String ent_type_desc;

	/*품질검토 예외 대상 관리*/
	private String obj_type_cd;
    private String obj_type;
    private String lib_nm;
    private String model_nm;
    private String sub_nm;
    private String ent_nm;
    private String att_nm;
    private String remark;
    private String rqpn;
    private String rgdtti;
    private String err_type_cd;
	
	/*품질점검지표관리*/
	private String mdi_pcs_nm;
	private String qty_chk_idt_cd;
	private String qty_chk_idt_nm;
	private String qty_idt_tp_nm;
	private String qty_chk_tg_yn;
	private String qty_chk_tp_nm;
	private String srt_ord;
	private String qty_chk_cont;
	private String slv_rsl_cont;
	private String qty_chk_result_tbl_nm;
	private String excel_output_yn;
	private String output_start_row;
	private String output_start_col;
	private String mdi_pcs_cd;
	private String qty_idt_tp_cd;
	private String qty_chk_tp_cd;
	
	
	
	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getObj_type_cd() {
		return obj_type_cd;
	}

	public void setObj_type_cd(String obj_type_cd) {
		this.obj_type_cd = obj_type_cd;
	}

	public String getObj_type() {
		return obj_type;
	}

	public void setObj_type(String obj_type) {
		this.obj_type = obj_type;
	}

	public String getLib_nm() {
		return lib_nm;
	}

	public void setLib_nm(String lib_nm) {
		this.lib_nm = lib_nm;
	}

	public String getModel_nm() {
		return model_nm;
	}

	public void setModel_nm(String model_nm) {
		this.model_nm = model_nm;
	}

	public String getSub_nm() {
		return sub_nm;
	}

	public void setSub_nm(String sub_nm) {
		this.sub_nm = sub_nm;
	}

	public String getEnt_nm() {
		return ent_nm;
	}

	public void setEnt_nm(String ent_nm) {
		this.ent_nm = ent_nm;
	}

	public String getAtt_nm() {
		return att_nm;
	}

	public void setAtt_nm(String att_nm) {
		this.att_nm = att_nm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRqpn() {
		return rqpn;
	}

	public void setRqpn(String rqpn) {
		this.rqpn = rqpn;
	}

	public String getRgdtti() {
		return rgdtti;
	}

	public void setRgdtti(String rgdtti) {
		this.rgdtti = rgdtti;
	}

	public String getErr_type_cd() {
		return err_type_cd;
	}

	public void setErr_type_cd(String err_type_cd) {
		this.err_type_cd = err_type_cd;
	}

	public String getMdi_pcs_nm() {
		return mdi_pcs_nm;
	}

	public void setMdi_pcs_nm(String mdi_pcs_nm) {
		this.mdi_pcs_nm = mdi_pcs_nm;
	}

	public String getQty_idt_tp_nm() {
		return qty_idt_tp_nm;
	}

	public void setQty_idt_tp_nm(String qty_idt_tp_nm) {
		this.qty_idt_tp_nm = qty_idt_tp_nm;
	}

	public String getQty_chk_tp_nm() {
		return qty_chk_tp_nm;
	}

	public void setQty_chk_tp_nm(String qty_chk_tp_nm) {
		this.qty_chk_tp_nm = qty_chk_tp_nm;
	}

	public String getSlv_rsl_cont() {
		return slv_rsl_cont;
	}

	public void setSlv_rsl_cont(String slv_rsl_cont) {
		this.slv_rsl_cont = slv_rsl_cont;
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
	
	public String getOutput_start_col() {
		return output_start_col;
	}

	public void setOutput_start_col(String output_start_col) {
		this.output_start_col = output_start_col;
	}

	public String getOutput_start_row() {
		return output_start_row;
	}

	public void setOutput_start_row(String output_start_row) {
		this.output_start_row = output_start_row;
	}

	public String getEnt_type_cd() {
		return ent_type_cd;
	}

	public void setEnt_type_cd(String ent_type_cd) {
		this.ent_type_cd = ent_type_cd;
	}

	public String getRef_ent_type_nm() {
		return ref_ent_type_nm;
	}

	public void setRef_ent_type_nm(String ref_ent_type_nm) {
		this.ref_ent_type_nm = ref_ent_type_nm;
	}

	public String getEnt_type_nm() {
		return ent_type_nm;
	}

	public void setEnt_type_nm(String ent_type_nm) {
		this.ent_type_nm = ent_type_nm;
	}

	public String getTbl_type_nm() {
		return tbl_type_nm;
	}

	public void setTbl_type_nm(String tbl_type_nm) {
		this.tbl_type_nm = tbl_type_nm;
	}

	public String getTbl_type_cd() {
		return tbl_type_cd;
	}

	public void setTbl_type_cd(String tbl_type_cd) {
		this.tbl_type_cd = tbl_type_cd;
	}

	public String getEnt_type_desc() {
		return ent_type_desc;
	}

	public void setEnt_type_desc(String ent_type_desc) {
		this.ent_type_desc = ent_type_desc;
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
