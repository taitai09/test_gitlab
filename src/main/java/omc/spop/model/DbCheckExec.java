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

@Alias("dbCheckExec")
public class DbCheckExec extends Base implements Jsonable {
	
	private String check_day;
	private String check_seq;
	private String auto_manual_check_div_cd;
    private String check_dt;
    private String check_text;
    private String checker_id;
    private String checker_nm;
    
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
	public String getAuto_manual_check_div_cd() {
		return auto_manual_check_div_cd;
	}
	public void setAuto_manual_check_div_cd(String auto_manual_check_div_cd) {
		this.auto_manual_check_div_cd = auto_manual_check_div_cd;
	}
	public String getCheck_dt() {
		return check_dt;
	}
	public void setCheck_dt(String check_dt) {
		this.check_dt = check_dt;
	}
	public String getCheck_text() {
		return check_text;
	}
	public void setCheck_text(String check_text) {
		this.check_text = check_text;
	}
	public String getChecker_id() {
		return checker_id;
	}
	public void setChecker_id(String checker_id) {
		this.checker_id = checker_id;
	}
	public String getChecker_nm() {
		return checker_nm;
	}
	public void setChecker_nm(String checker_nm) {
		this.checker_nm = checker_nm;
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
