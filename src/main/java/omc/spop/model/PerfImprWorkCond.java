package omc.spop.model;

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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/

@Alias("perfImprWorkCond")
public class PerfImprWorkCond extends Base implements Jsonable {
	
    private String today_add_request;
    private String today_add_receipt;
    private String today_add_tuning;
    private String today_add_apply_wait;
    private String today_add_apply_reject;
    private String today_add_tuning_finish;
    private String today_add_tuning_delay;
    private String today_add_apply_delay;
    private String day_before_accum_request;
    private String day_before_accum_receipt;
    private String day_before_accum_tuning;
    private String day_before_accum_apply_wait;
    private String day_before_accum_apply_reject;
    private String day_before_accum_tuning_finish;
    private String day_before_accum_tuning_delay;
    private String day_before_accum_apply_delay;
    
	public String getToday_add_request() {
		return today_add_request;
	}

	public void setToday_add_request(String today_add_request) {
		this.today_add_request = today_add_request;
	}

	public String getToday_add_receipt() {
		return today_add_receipt;
	}

	public void setToday_add_receipt(String today_add_receipt) {
		this.today_add_receipt = today_add_receipt;
	}

	public String getToday_add_tuning() {
		return today_add_tuning;
	}

	public void setToday_add_tuning(String today_add_tuning) {
		this.today_add_tuning = today_add_tuning;
	}

	public String getToday_add_apply_wait() {
		return today_add_apply_wait;
	}

	public void setToday_add_apply_wait(String today_add_apply_wait) {
		this.today_add_apply_wait = today_add_apply_wait;
	}

	public String getToday_add_apply_reject() {
		return today_add_apply_reject;
	}

	public void setToday_add_apply_reject(String today_add_apply_reject) {
		this.today_add_apply_reject = today_add_apply_reject;
	}

	public String getToday_add_tuning_finish() {
		return today_add_tuning_finish;
	}

	public void setToday_add_tuning_finish(String today_add_tuning_finish) {
		this.today_add_tuning_finish = today_add_tuning_finish;
	}

	public String getToday_add_tuning_delay() {
		return today_add_tuning_delay;
	}

	public void setToday_add_tuning_delay(String today_add_tuning_delay) {
		this.today_add_tuning_delay = today_add_tuning_delay;
	}

	public String getToday_add_apply_delay() {
		return today_add_apply_delay;
	}

	public void setToday_add_apply_delay(String today_add_apply_delay) {
		this.today_add_apply_delay = today_add_apply_delay;
	}

	public String getDay_before_accum_request() {
		return day_before_accum_request;
	}

	public void setDay_before_accum_request(String day_before_accum_request) {
		this.day_before_accum_request = day_before_accum_request;
	}

	public String getDay_before_accum_receipt() {
		return day_before_accum_receipt;
	}

	public void setDay_before_accum_receipt(String day_before_accum_receipt) {
		this.day_before_accum_receipt = day_before_accum_receipt;
	}

	public String getDay_before_accum_tuning() {
		return day_before_accum_tuning;
	}

	public void setDay_before_accum_tuning(String day_before_accum_tuning) {
		this.day_before_accum_tuning = day_before_accum_tuning;
	}

	public String getDay_before_accum_apply_wait() {
		return day_before_accum_apply_wait;
	}

	public void setDay_before_accum_apply_wait(String day_before_accum_apply_wait) {
		this.day_before_accum_apply_wait = day_before_accum_apply_wait;
	}

	public String getDay_before_accum_apply_reject() {
		return day_before_accum_apply_reject;
	}

	public void setDay_before_accum_apply_reject(String day_before_accum_apply_reject) {
		this.day_before_accum_apply_reject = day_before_accum_apply_reject;
	}

	public String getDay_before_accum_tuning_finish() {
		return day_before_accum_tuning_finish;
	}

	public void setDay_before_accum_tuning_finish(String day_before_accum_tuning_finish) {
		this.day_before_accum_tuning_finish = day_before_accum_tuning_finish;
	}

	public String getDay_before_accum_tuning_delay() {
		return day_before_accum_tuning_delay;
	}

	public void setDay_before_accum_tuning_delay(String day_before_accum_tuning_delay) {
		this.day_before_accum_tuning_delay = day_before_accum_tuning_delay;
	}

	public String getDay_before_accum_apply_delay() {
		return day_before_accum_apply_delay;
	}

	public void setDay_before_accum_apply_delay(String day_before_accum_apply_delay) {
		this.day_before_accum_apply_delay = day_before_accum_apply_delay;
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
