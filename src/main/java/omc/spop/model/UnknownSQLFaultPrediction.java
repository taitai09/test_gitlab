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
 * 2019.03.27 명성태 최초작성 (Unknown SQL 장애 예측)
 **********************************************************/

@Alias("unknownSQLFaultPrediction")
public class UnknownSQLFaultPrediction extends Base implements Jsonable {
	
	// 조건 값
	private String inst_id;
	private String start_first_analysis_day;
	private String end_first_analysis_day;
	
	// 컬럼 정보
	private String begin_interval_time;
	private String cpu_usage;
	
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
	
	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getStart_first_analysis_day() {
		return start_first_analysis_day;
	}

	public void setStart_first_analysis_day(String start_first_analysis_day) {
		this.start_first_analysis_day = start_first_analysis_day;
	}

	public String getEnd_first_analysis_day() {
		return end_first_analysis_day;
	}

	public void setEnd_first_analysis_day(String end_first_analysis_day) {
		this.end_first_analysis_day = end_first_analysis_day;
	}

	public String getBegin_interval_time() {
		return begin_interval_time;
	}

	public void setBegin_interval_time(String begin_interval_time) {
		this.begin_interval_time = begin_interval_time;
	}

	public String getCpu_usage() {
		return cpu_usage;
	}

	public void setCpu_usage(String cpu_usage) {
		this.cpu_usage = cpu_usage;
	}
	
}
