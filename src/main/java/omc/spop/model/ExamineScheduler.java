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

@Alias("examineScheduler")
public class ExamineScheduler extends Base implements Jsonable {
	private String base_day;
	private int scheduler_job_status_cd;
	private int complete_cnt;
	private int error_cnt;
	private String error_yn;
	
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

	public String getBase_day() {
		return base_day;
	}

	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}

	public int getScheduler_job_status_cd() {
		return scheduler_job_status_cd;
	}

	public void setScheduler_job_status_cd(int scheduler_job_status_cd) {
		this.scheduler_job_status_cd = scheduler_job_status_cd;
	}

	public int getComplete_cnt() {
		return complete_cnt;
	}

	public void setComplete_cnt(int complete_cnt) {
		this.complete_cnt = complete_cnt;
	}

	public int getError_cnt() {
		return error_cnt;
	}

	public void setError_cnt(int error_cnt) {
		this.error_cnt = error_cnt;
	}

	public String getError_yn() {
		return error_yn;
	}

	public void setError_yn(String error_yn) {
		this.error_yn = error_yn;
	}
}
