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
 * 2018.08.30	이원식	최초작성
 **********************************************************/

@Alias("dbCheckConfig")
public class DbCheckConfig extends Base implements Jsonable {
	
	private String check_pref_id;
    private String check_pref_nm;
    private String check_enable_yn;
    private String threshold_value;
    private int cnt;
    private String check_day;
    
	public String getCheck_pref_id() {
		return check_pref_id;
	}
	public void setCheck_pref_id(String check_pref_id) {
		this.check_pref_id = check_pref_id;
	}
	public String getCheck_pref_nm() {
		return check_pref_nm;
	}
	public void setCheck_pref_nm(String check_pref_nm) {
		this.check_pref_nm = check_pref_nm;
	}
	public String getCheck_enable_yn() {
		return check_enable_yn;
	}
	public void setCheck_enable_yn(String check_enable_yn) {
		this.check_enable_yn = check_enable_yn;
	}
	public String getThreshold_value() {
		return threshold_value;
	}
	public void setThreshold_value(String threshold_value) {
		this.threshold_value = threshold_value;
	}
	
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
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
