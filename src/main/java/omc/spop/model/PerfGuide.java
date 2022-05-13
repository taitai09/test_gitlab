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

@Alias("perfGuide")
public class PerfGuide extends Base implements Jsonable {
	
	private String guide_no;
	private String guide_div_cd;
	private String guide_div_nm;
	private String sys_nm;
	private String guide_title_nm;
	private String guide_sbst;
	private String reg_user_id;
	private String reg_user_nm;
	private String reg_dt;
	private String retv_cnt;
	private String download_cnt;
	private String del_yn;
	
	private String dept_nm;
	private String retv_user_id;
	private String retv_dt;
	private String download_yn;
	
	private String upd_id;
	private String upd_nm;
	private String upd_dt;
	private String top_fix_yn;
	private String pinset;
	
	private String impr_sbst;
	private String controversialist;
	
	private String tuning_case_type_cd;
	
	public String getTuning_case_type_cd() {
		return tuning_case_type_cd;
	}
	public void setTuning_case_type_cd(String tuning_case_type_cd) {
		this.tuning_case_type_cd = tuning_case_type_cd;
	}
	public String getImpr_sbst() {
		return impr_sbst;
	}
	public void setImpr_sbst(String impr_sbst) {
		this.impr_sbst = impr_sbst;
	}
	public String getControversialist() {
		return controversialist;
	}
	public void setControversialist(String controversialist) {
		this.controversialist = controversialist;
	}
	public String getPinset() {
		return pinset;
	}
	public void setPinset(String pinset) {
		this.pinset = pinset;
	}
	public String getUpd_nm() {
		return upd_nm;
	}
	public void setUpd_nm(String upd_nm) {
		this.upd_nm = upd_nm;
	}
	public String getUpd_id() {
		return upd_id;
	}
	public void setUpd_id(String upd_id) {
		this.upd_id = upd_id;
	}
	public String getUpd_dt() {
		return upd_dt;
	}
	public void setUpd_dt(String upd_dt) {
		this.upd_dt = upd_dt;
	}
	public String getTop_fix_yn() {
		return top_fix_yn;
	}
	public void setTop_fix_yn(String top_fix_yn) {
		this.top_fix_yn = top_fix_yn;
	}
	public String getGuide_no() {
		return guide_no;
	}
	public void setGuide_no(String guide_no) {
		this.guide_no = guide_no;
	}
	public String getGuide_div_cd() {
		return guide_div_cd;
	}
	public void setGuide_div_cd(String guide_div_cd) {
		this.guide_div_cd = guide_div_cd;
	}
	public String getGuide_div_nm() {
		return guide_div_nm;
	}
	public void setGuide_div_nm(String guide_div_nm) {
		this.guide_div_nm = guide_div_nm;
	}
	public String getSys_nm() {
		return sys_nm;
	}
	public void setSys_nm(String sys_nm) {
		this.sys_nm = sys_nm;
	}
	public String getGuide_title_nm() {
		return guide_title_nm;
	}
	public void setGuide_title_nm(String guide_title_nm) {
		this.guide_title_nm = guide_title_nm;
	}
	public String getGuide_sbst() {
		return guide_sbst;
	}
	public void setGuide_sbst(String guide_sbst) {
		this.guide_sbst = guide_sbst;
	}
	public String getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(String reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getReg_user_nm() {
		return reg_user_nm;
	}
	public void setReg_user_nm(String reg_user_nm) {
		this.reg_user_nm = reg_user_nm;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getRetv_cnt() {
		return retv_cnt;
	}
	public void setRetv_cnt(String retv_cnt) {
		this.retv_cnt = retv_cnt;
	}
	public String getDownload_cnt() {
		return download_cnt;
	}
	public void setDownload_cnt(String download_cnt) {
		this.download_cnt = download_cnt;
	}
	public String getDel_yn() {
		return del_yn;
	}
	public void setDel_yn(String del_yn) {
		this.del_yn = del_yn;
	}	
	public String getDept_nm() {
		return dept_nm;
	}
	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
	}
	public String getRetv_user_id() {
		return retv_user_id;
	}
	public void setRetv_user_id(String retv_user_id) {
		this.retv_user_id = retv_user_id;
	}
	public String getDownload_yn() {
		return download_yn;
	}
	public void setDownload_yn(String download_yn) {
		this.download_yn = download_yn;
	}	
	public String getRetv_dt() {
		return retv_dt;
	}
	public void setRetv_dt(String retv_dt) {
		this.retv_dt = retv_dt;
	}
	
/*	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("guide_no",StringUtil.parseInt(this.getGuide_no(),0));
		objJson.put("guide_div_cd",this.getGuide_div_cd());
		objJson.put("guide_div_nm",this.getGuide_div_nm());
		objJson.put("sys_nm",this.getSys_nm());
		objJson.put("guide_title_nm",this.getGuide_title_nm());
		objJson.put("guide_sbst",this.getGuide_sbst());
		objJson.put("reg_user_id",this.getReg_user_id());
		objJson.put("reg_user_nm",this.getReg_user_nm());
		objJson.put("reg_dt",this.getReg_dt());
		objJson.put("retv_cnt",StringUtil.parseInt(this.getRetv_cnt(),0));
		objJson.put("download_cnt",StringUtil.parseInt(this.getDownload_cnt(),0));
		objJson.put("tuning_no",this.getTuning_no());
		objJson.put("del_yn",this.getDel_yn());
		
		objJson.put("dept_nm",this.getDept_nm());
		objJson.put("retv_user_id",this.getRetv_user_id());
		objJson.put("retv_dt",this.getRetv_dt());
		objJson.put("download_yn",this.getDownload_yn());

		return objJson;
	}*/
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
