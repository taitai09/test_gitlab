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
 * 2018.04.18	이원식	최초작성
 **********************************************************/

@Alias("dbCheckSummary")
public class DbCheckSummary extends Base implements Jsonable {
	private String check_day;
	private String check_seq;
	
	private String check_pref_id;
    private String check_pref_nm;
    private String check_value1;
    private String check_value2;
    
	public String getCheck_day() {
		return check_day;
	}
	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}
	public String getCheck_seq() {
		return check_seq;
	}
	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}
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
	public String getCheck_value1() {
		return check_value1;
	}
	public void setCheck_value1(String check_value1) {
		this.check_value1 = check_value1;
	}
	public String getCheck_value2() {
		return check_value2;
	}
	public void setCheck_value2(String check_value2) {
		this.check_value2 = check_value2;
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
