package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.06.11	이원식	최초작성
 **********************************************************/

@Alias("cpuLimitPrediction")
public class CPULimitPrediction extends Base implements Jsonable {
	
    private String inst_id;
    private String inst_nm;
    private String prediction_dt;
    private String prediction_date;
    private String predictioner_id;
    private String slope;
    private String intcpt;
    private String before_3_month_cpu_usage;
    private String before_2_month_cpu_usage;
    private String before_1_month_cpu_usage;
    private String current_cpu_usage;
    private String after_1_month_cpu_usage;
    private String after_2_month_cpu_usage;
    private String after_3_month_cpu_usage;
    private String after_6_month_cpu_usage;
    private String after_12_month_cpu_usage;
    private String resource_type;
    private String predict_standard;
    
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	public String getPrediction_dt() {
		return prediction_dt;
	}
	public void setPrediction_dt(String prediction_dt) {
		this.prediction_dt = prediction_dt;
	}
	public String getPrediction_date() {
		return prediction_date;
	}
	public void setPrediction_date(String prediction_date) {
		this.prediction_date = prediction_date;
	}
	public String getPredictioner_id() {
		return predictioner_id;
	}
	public void setPredictioner_id(String predictioner_id) {
		this.predictioner_id = predictioner_id;
	}
	public String getSlope() {
		return slope;
	}
	public void setSlope(String slope) {
		this.slope = slope;
	}
	public String getIntcpt() {
		return intcpt;
	}
	public void setIntcpt(String intcpt) {
		this.intcpt = intcpt;
	}
	public String getBefore_3_month_cpu_usage() {
		return before_3_month_cpu_usage;
	}
	public void setBefore_3_month_cpu_usage(String before_3_month_cpu_usage) {
		this.before_3_month_cpu_usage = before_3_month_cpu_usage;
	}
	public String getBefore_2_month_cpu_usage() {
		return before_2_month_cpu_usage;
	}
	public void setBefore_2_month_cpu_usage(String before_2_month_cpu_usage) {
		this.before_2_month_cpu_usage = before_2_month_cpu_usage;
	}
	public String getBefore_1_month_cpu_usage() {
		return before_1_month_cpu_usage;
	}
	public void setBefore_1_month_cpu_usage(String before_1_month_cpu_usage) {
		this.before_1_month_cpu_usage = before_1_month_cpu_usage;
	}
	public String getCurrent_cpu_usage() {
		return current_cpu_usage;
	}
	public void setCurrent_cpu_usage(String current_cpu_usage) {
		this.current_cpu_usage = current_cpu_usage;
	}
	public String getAfter_1_month_cpu_usage() {
		return after_1_month_cpu_usage;
	}
	public void setAfter_1_month_cpu_usage(String after_1_month_cpu_usage) {
		this.after_1_month_cpu_usage = after_1_month_cpu_usage;
	}
	public String getAfter_2_month_cpu_usage() {
		return after_2_month_cpu_usage;
	}
	public void setAfter_2_month_cpu_usage(String after_2_month_cpu_usage) {
		this.after_2_month_cpu_usage = after_2_month_cpu_usage;
	}
	public String getAfter_3_month_cpu_usage() {
		return after_3_month_cpu_usage;
	}
	public void setAfter_3_month_cpu_usage(String after_3_month_cpu_usage) {
		this.after_3_month_cpu_usage = after_3_month_cpu_usage;
	}
	public String getAfter_6_month_cpu_usage() {
		return after_6_month_cpu_usage;
	}
	public void setAfter_6_month_cpu_usage(String after_6_month_cpu_usage) {
		this.after_6_month_cpu_usage = after_6_month_cpu_usage;
	}
	public String getAfter_12_month_cpu_usage() {
		return after_12_month_cpu_usage;
	}
	public void setAfter_12_month_cpu_usage(String after_12_month_cpu_usage) {
		this.after_12_month_cpu_usage = after_12_month_cpu_usage;
	}
	
	public String getResource_type() {
		return resource_type;
	}
	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}
	public String getPredict_standard() {
		return predict_standard;
	}
	public void setPredict_standard(String predict_standard) {
		this.predict_standard = predict_standard;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("inst_nm",this.getInst_nm());
		objJson.put("prediction_dt",this.getPrediction_dt());
		objJson.put("prediction_date",this.getPrediction_date());
		objJson.put("predictioner_id",this.getPredictioner_id());
		objJson.put("slope",this.getSlope());
		objJson.put("intcpt",this.getIntcpt());
		objJson.put("predict_standard",this.getPredict_standard());
		objJson.put("before_3_month_cpu_usage",StringUtil.parseDouble(this.getBefore_3_month_cpu_usage(),0));
		objJson.put("before_2_month_cpu_usage",StringUtil.parseDouble(this.getBefore_2_month_cpu_usage(),0));
		objJson.put("before_1_month_cpu_usage",StringUtil.parseDouble(this.getBefore_1_month_cpu_usage(),0));
		objJson.put("current_cpu_usage",StringUtil.parseDouble(this.getCurrent_cpu_usage(),0));
		objJson.put("after_1_month_cpu_usage",StringUtil.parseDouble(this.getAfter_1_month_cpu_usage(),0));
		objJson.put("after_2_month_cpu_usage",StringUtil.parseDouble(this.getAfter_2_month_cpu_usage(),0));
		objJson.put("after_3_month_cpu_usage",StringUtil.parseDouble(this.getAfter_3_month_cpu_usage(),0));
		objJson.put("after_6_month_cpu_usage",StringUtil.parseDouble(this.getAfter_6_month_cpu_usage(),0));
		objJson.put("after_12_month_cpu_usage",StringUtil.parseDouble(this.getAfter_12_month_cpu_usage(),0));

		return objJson;
	}
}