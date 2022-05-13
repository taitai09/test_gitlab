package omc.spop.model;

import java.math.BigDecimal;
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
 * 2018.06.11	이원식	최초작성
 **********************************************************/

@Alias("memoryLimitPrediction")
public class MEMORYLimitPrediction extends Base implements Jsonable {
	
	private String inst_id;
	private String inst_nm;
	private String prediction_dt;
	private String prediction_date;
	private String predictioner_id;
	private String slope;
	private String intcpt;
	private BigDecimal physical_memory_size;
	private BigDecimal before_3_month_mem_usage;
	private BigDecimal before_2_month_mem_usage;
	private BigDecimal before_1_month_mem_usage;
	private BigDecimal current_mem_usage;
	private BigDecimal after_1_month_mem_usage;
	private BigDecimal after_2_month_mem_usage;
	private BigDecimal after_3_month_mem_usage;
	private BigDecimal after_6_month_mem_usage;
	private BigDecimal after_12_month_mem_usage;
	private BigDecimal resource_type;
	private BigDecimal predict_standard;
	
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
	public BigDecimal getPhysical_memory_size() {
		return physical_memory_size;
	}
	public void setPhysical_memory_size(BigDecimal physical_memory_size) {
		this.physical_memory_size = physical_memory_size;
	}
	public BigDecimal getBefore_3_month_mem_usage() {
		return before_3_month_mem_usage;
	}
	public void setBefore_3_month_mem_usage(BigDecimal before_3_month_mem_usage) {
		this.before_3_month_mem_usage = before_3_month_mem_usage;
	}
	public BigDecimal getBefore_2_month_mem_usage() {
		return before_2_month_mem_usage;
	}
	public void setBefore_2_month_mem_usage(BigDecimal before_2_month_mem_usage) {
		this.before_2_month_mem_usage = before_2_month_mem_usage;
	}
	public BigDecimal getBefore_1_month_mem_usage() {
		return before_1_month_mem_usage;
	}
	public void setBefore_1_month_mem_usage(BigDecimal before_1_month_mem_usage) {
		this.before_1_month_mem_usage = before_1_month_mem_usage;
	}
	public BigDecimal getCurrent_mem_usage() {
		return current_mem_usage;
	}
	public void setCurrent_mem_usage(BigDecimal current_mem_usage) {
		this.current_mem_usage = current_mem_usage;
	}
	public BigDecimal getAfter_1_month_mem_usage() {
		return after_1_month_mem_usage;
	}
	public void setAfter_1_month_mem_usage(BigDecimal after_1_month_mem_usage) {
		this.after_1_month_mem_usage = after_1_month_mem_usage;
	}
	public BigDecimal getAfter_2_month_mem_usage() {
		return after_2_month_mem_usage;
	}
	public void setAfter_2_month_mem_usage(BigDecimal after_2_month_mem_usage) {
		this.after_2_month_mem_usage = after_2_month_mem_usage;
	}
	public BigDecimal getAfter_3_month_mem_usage() {
		return after_3_month_mem_usage;
	}
	public void setAfter_3_month_mem_usage(BigDecimal after_3_month_mem_usage) {
		this.after_3_month_mem_usage = after_3_month_mem_usage;
	}
	public BigDecimal getAfter_6_month_mem_usage() {
		return after_6_month_mem_usage;
	}
	public void setAfter_6_month_mem_usage(BigDecimal after_6_month_mem_usage) {
		this.after_6_month_mem_usage = after_6_month_mem_usage;
	}
	public BigDecimal getAfter_12_month_mem_usage() {
		return after_12_month_mem_usage;
	}
	public void setAfter_12_month_mem_usage(BigDecimal after_12_month_mem_usage) {
		this.after_12_month_mem_usage = after_12_month_mem_usage;
	}
	
	public BigDecimal getResource_type() {
		return resource_type;
	}
	public void setResource_type(BigDecimal resource_type) {
		this.resource_type = resource_type;
	}
	public BigDecimal getPredict_standard() {
		return predict_standard;
	}
	public void setPredict_standard(BigDecimal predict_standard) {
		this.predict_standard = predict_standard;
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
	
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}