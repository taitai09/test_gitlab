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

@Alias("memoryLimitPredictionDetail")
public class MEMORYLimitPredictionDetail extends Base implements Jsonable {
	private String prediction_dt;
	
	private String inst_id;
	private String inst_nm;
	private String snap_dt;
	private BigDecimal physical_memory_size;
	private BigDecimal mem_limit;
	private BigDecimal mem_usage;
	private BigDecimal past_mem_usage_trend;
	private BigDecimal future_mem_usage_trend;
	
	public String getPrediction_dt() {
		return prediction_dt;
	}
	public void setPrediction_dt(String prediction_dt) {
		this.prediction_dt = prediction_dt;
	}
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
	public String getSnap_dt() {
		return snap_dt;
	}
	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}
	public BigDecimal getPhysical_memory_size() {
		return physical_memory_size;
	}
	public void setPhysical_memory_size(BigDecimal physical_memory_size) {
		this.physical_memory_size = physical_memory_size;
	}
	public BigDecimal getMem_limit() {
		return mem_limit;
	}
	public void setMem_limit(BigDecimal mem_limit) {
		this.mem_limit = mem_limit;
	}
	public BigDecimal getMem_usage() {
		return mem_usage;
	}
	public void setMem_usage(BigDecimal mem_usage) {
		this.mem_usage = mem_usage;
	}
	public BigDecimal getPast_mem_usage_trend() {
		return past_mem_usage_trend;
	}
	public void setPast_mem_usage_trend(BigDecimal past_mem_usage_trend) {
		this.past_mem_usage_trend = past_mem_usage_trend;
	}
	public BigDecimal getFuture_mem_usage_trend() {
		return future_mem_usage_trend;
	}
	public void setFuture_mem_usage_trend(BigDecimal future_mem_usage_trend) {
		this.future_mem_usage_trend = future_mem_usage_trend;
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