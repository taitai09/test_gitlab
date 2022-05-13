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

@Alias("structuralQualityStatusTrend")
public class StructuralQualityStatusTrend extends Base implements Jsonable {

	//미준수건수, 표준화율 추이
	private String project_id;
	private String std_inspect_day;
	private String err_cnt;
	private String std_rate; 
	
	//모델별 현황
	private String lib_nm;
	private String model_nm;
	private String last_ent_cnt;  //엔터티 수
	private String last_att_cnt;  //속성수 
	private String last_err_101_cnt;  // 비표준 속성 수
	private String last_std_rate;  // 표준화율
	private String incre_ent_cnt;  // 엔터티 증감건수
	private String incre_ent_cnt_sign;  //엔터티 증감기호
	private String incre_att_cnt;  // 속성 증감건수
	private String incre_att_cnt_sign; // 속성 증감기호
	private String incre_std_rate;    // 표준화율 증감률
	private String incre_std_rate_sign;  //표준화율 증감기호
	private String last_extrac_dt;  //금회차
	private String pre_extrac_dt;  //전회차
	private String incre_err_cnt;  //비표준 속성 증감건
	private String incre_err_sign; //비표준 속성 증감 기호
	private String startDate;
	private String endDate;
	
	private String last_err_cnt; //미준수 건수
	private String pre_std_rate; //이전회차 표준화율
	
	private String qty_chk_idt_cd;   //품질지표코드
	private String qty_chk_idt_nm;   
	
	private String extrac_dt; //회차;
	
	public String getExtrac_dt() {
		return extrac_dt;
	}

	public void setExtrac_dt(String extrac_dt) {
		this.extrac_dt = extrac_dt;
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

	public String getStd_inspect_day() {
		return std_inspect_day;
	}

	public void setStd_inspect_day(String std_inspect_day) {
		this.std_inspect_day = std_inspect_day;
	}

	public String getErr_cnt() {
		return err_cnt;
	}

	public void setErr_cnt(String err_cnt) {
		this.err_cnt = err_cnt;
	}

	public String getStd_rate() {
		return std_rate;
	}

	public void setStd_rate(String std_rate) {
		this.std_rate = std_rate;
	}

	public String getLast_err_cnt() {
		return last_err_cnt;
	}

	public void setLast_err_cnt(String last_err_cnt) {
		this.last_err_cnt = last_err_cnt;
	}

	public String getPre_std_rate() {
		return pre_std_rate;
	}

	public void setPre_std_rate(String pre_std_rate) {
		this.pre_std_rate = pre_std_rate;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIncre_err_cnt() {
		return incre_err_cnt;
	}

	public void setIncre_err_cnt(String incre_err_cnt) {
		this.incre_err_cnt = incre_err_cnt;
	}

	public String getIncre_err_sign() {
		return incre_err_sign;
	}

	public void setIncre_err_sign(String incre_err_sign) {
		this.incre_err_sign = incre_err_sign;
	}

	public String getLast_ent_cnt() {
		return last_ent_cnt;
	}

	public void setLast_ent_cnt(String last_ent_cnt) {
		this.last_ent_cnt = last_ent_cnt;
	}

	public String getLast_att_cnt() {
		return last_att_cnt;
	}

	public void setLast_att_cnt(String last_att_cnt) {
		this.last_att_cnt = last_att_cnt;
	}

	public String getLast_err_101_cnt() {
		return last_err_101_cnt;
	}

	public void setLast_err_101_cnt(String last_err_101_cnt) {
		this.last_err_101_cnt = last_err_101_cnt;
	}

	public String getLast_std_rate() {
		return last_std_rate;
	}

	public void setLast_std_rate(String last_std_rate) {
		this.last_std_rate = last_std_rate;
	}

	public String getIncre_ent_cnt() {
		return incre_ent_cnt;
	}

	public void setIncre_ent_cnt(String incre_ent_cnt) {
		this.incre_ent_cnt = incre_ent_cnt;
	}

	public String getIncre_ent_cnt_sign() {
		return incre_ent_cnt_sign;
	}

	public void setIncre_ent_cnt_sign(String incre_ent_cnt_sign) {
		this.incre_ent_cnt_sign = incre_ent_cnt_sign;
	}

	public String getIncre_att_cnt() {
		return incre_att_cnt;
	}

	public void setIncre_att_cnt(String incre_att_cnt) {
		this.incre_att_cnt = incre_att_cnt;
	}

	public String getIncre_att_cnt_sign() {
		return incre_att_cnt_sign;
	}

	public void setIncre_att_cnt_sign(String incre_att_cnt_sign) {
		this.incre_att_cnt_sign = incre_att_cnt_sign;
	}

	public String getIncre_std_rate() {
		return incre_std_rate;
	}

	public void setIncre_std_rate(String incre_std_rate) {
		this.incre_std_rate = incre_std_rate;
	}

	public String getIncre_std_rate_sign() {
		return incre_std_rate_sign;
	}

	public void setIncre_std_rate_sign(String incre_std_rate_sign) {
		this.incre_std_rate_sign = incre_std_rate_sign;
	}

	public String getLast_extrac_dt() {
		return last_extrac_dt;
	}

	public void setLast_extrac_dt(String last_extrac_dt) {
		this.last_extrac_dt = last_extrac_dt;
	}

	public String getPre_extrac_dt() {
		return pre_extrac_dt;
	}

	public void setPre_extrac_dt(String pre_extrac_dt) {
		this.pre_extrac_dt = pre_extrac_dt;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
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
