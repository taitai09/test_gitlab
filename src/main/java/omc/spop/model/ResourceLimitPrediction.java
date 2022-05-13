package omc.spop.model;

import java.math.BigDecimal;
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

@Alias("resourceLimitPrediction")
public class ResourceLimitPrediction extends Base implements Jsonable {

	private String type;
	//inst_id가 null일 경우가 있어 String으로 한다.
	private String inst_id;
	private String after_month;
	private int cpu;
	private int sequence;
	private int tablespace;
	private float after_1_month;
	private float after_2_month;
	private float after_3_month;
	private float after_6_month;
	private float after_12_month;
	private float threshold;

	private String inst_nm   ;
	private String before_3_month_cpu_usage ;
	private String before_2_month_cpu_usage ;
	private String before_1_month_cpu_usage ;
	private String current_cpu_usage_b ;
	private String after_1_month_cpu_usage ;
	private String after_2_month_cpu_usage ;
	private String after_3_month_cpu_usage ;
	private String before_3_month_mem_usage ;
	private String before_2_month_mem_usage ;
	private String before_1_month_mem_usage ;
	private String current_mem_usage_b ;
	private String after_1_month_mem_usage ;
	private String after_2_month_mem_usage ;
	private String after_3_month_mem_usage ;
	private String cpu_prediction_dt   ;
	private String mem_prediction_dt   ;
	private String rgb_color_value   ;
	
	private String period;
	private String instance;
	private String month_cpu_usage;
	private String month_mem_usage;
	
	
	private int cpu_core_cnt;
	private BigDecimal current_cpu_usage;
	private BigDecimal cpu_increase_ratio;
	private BigDecimal physical_memory_size;
	private BigDecimal current_mem_usage;
	private BigDecimal mem_increase_usage;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getAfter_month() {
		return after_month;
	}

	public void setAfter_month(String after_month) {
		this.after_month = after_month;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getTablespace() {
		return tablespace;
	}

	public void setTablespace(int tablespace) {
		this.tablespace = tablespace;
	}

	public float getAfter_1_month() {
		return after_1_month;
	}

	public void setAfter_1_month(float after_1_month) {
		this.after_1_month = after_1_month;
	}

	public float getAfter_2_month() {
		return after_2_month;
	}

	public void setAfter_2_month(float after_2_month) {
		this.after_2_month = after_2_month;
	}

	public float getAfter_3_month() {
		return after_3_month;
	}

	public void setAfter_3_month(float after_3_month) {
		this.after_3_month = after_3_month;
	}

	public float getAfter_6_month() {
		return after_6_month;
	}

	public void setAfter_6_month(float after_6_month) {
		this.after_6_month = after_6_month;
	}

	public float getAfter_12_month() {
		return after_12_month;
	}

	public void setAfter_12_month(float after_12_month) {
		this.after_12_month = after_12_month;
	}

	public float getThreshold() {
		return threshold;
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	public String getInst_nm() {
		return inst_nm;
	}

	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
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

	public String getCurrent_cpu_usage_b() {
		return current_cpu_usage_b;
	}

	public void setCurrent_cpu_usage_b(String current_cpu_usage_b) {
		this.current_cpu_usage_b = current_cpu_usage_b;
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

	public String getBefore_3_month_mem_usage() {
		return before_3_month_mem_usage;
	}

	public void setBefore_3_month_mem_usage(String before_3_month_mem_usage) {
		this.before_3_month_mem_usage = before_3_month_mem_usage;
	}

	public String getBefore_2_month_mem_usage() {
		return before_2_month_mem_usage;
	}

	public void setBefore_2_month_mem_usage(String before_2_month_mem_usage) {
		this.before_2_month_mem_usage = before_2_month_mem_usage;
	}

	public String getBefore_1_month_mem_usage() {
		return before_1_month_mem_usage;
	}

	public void setBefore_1_month_mem_usage(String before_1_month_mem_usage) {
		this.before_1_month_mem_usage = before_1_month_mem_usage;
	}

	public String getCurrent_mem_usage_b() {
		return current_mem_usage_b;
	}

	public void setCurrent_mem_usage_b(String current_mem_usage_b) {
		this.current_mem_usage_b = current_mem_usage_b;
	}

	public String getAfter_1_month_mem_usage() {
		return after_1_month_mem_usage;
	}

	public void setAfter_1_month_mem_usage(String after_1_month_mem_usage) {
		this.after_1_month_mem_usage = after_1_month_mem_usage;
	}

	public String getAfter_2_month_mem_usage() {
		return after_2_month_mem_usage;
	}

	public void setAfter_2_month_mem_usage(String after_2_month_mem_usage) {
		this.after_2_month_mem_usage = after_2_month_mem_usage;
	}

	public String getAfter_3_month_mem_usage() {
		return after_3_month_mem_usage;
	}

	public void setAfter_3_month_mem_usage(String after_3_month_mem_usage) {
		this.after_3_month_mem_usage = after_3_month_mem_usage;
	}

	public String getCpu_prediction_dt() {
		return cpu_prediction_dt;
	}

	public void setCpu_prediction_dt(String cpu_prediction_dt) {
		this.cpu_prediction_dt = cpu_prediction_dt;
	}

	public String getMem_prediction_dt() {
		return mem_prediction_dt;
	}

	public void setMem_prediction_dt(String mem_prediction_dt) {
		this.mem_prediction_dt = mem_prediction_dt;
	}

	public String getRgb_color_value() {
		return rgb_color_value;
	}

	public void setRgb_color_value(String rgb_color_value) {
		this.rgb_color_value = rgb_color_value;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getMonth_cpu_usage() {
		return month_cpu_usage;
	}

	public void setMonth_cpu_usage(String month_cpu_usage) {
		this.month_cpu_usage = month_cpu_usage;
	}

	public String getMonth_mem_usage() {
		return month_mem_usage;
	}

	public void setMonth_mem_usage(String month_mem_usage) {
		this.month_mem_usage = month_mem_usage;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public int getCpu_core_cnt() {
		return cpu_core_cnt;
	}

	public void setCpu_core_cnt(int cpu_core_cnt) {
		this.cpu_core_cnt = cpu_core_cnt;
	}

	public BigDecimal getCurrent_cpu_usage() {
		return current_cpu_usage;
	}

	public void setCurrent_cpu_usage(BigDecimal current_cpu_usage) {
		this.current_cpu_usage = current_cpu_usage;
	}

	public BigDecimal getCpu_increase_ratio() {
		return cpu_increase_ratio;
	}

	public void setCpu_increase_ratio(BigDecimal cpu_increase_ratio) {
		this.cpu_increase_ratio = cpu_increase_ratio;
	}

	public BigDecimal getPhysical_memory_size() {
		return physical_memory_size;
	}

	public void setPhysical_memory_size(BigDecimal physical_memory_size) {
		this.physical_memory_size = physical_memory_size;
	}

	public BigDecimal getCurrent_mem_usage() {
		return current_mem_usage;
	}

	public void setCurrent_mem_usage(BigDecimal current_mem_usage) {
		this.current_mem_usage = current_mem_usage;
	}

	public BigDecimal getMem_increase_usage() {
		return mem_increase_usage;
	}

	public void setMem_increase_usage(BigDecimal mem_increase_usage) {
		this.mem_increase_usage = mem_increase_usage;
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