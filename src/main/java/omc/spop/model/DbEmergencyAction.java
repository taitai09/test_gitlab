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
 * 2018.05.16	이원식	최초작성
 **********************************************************/

@Alias("dbEmergencyAction")
public class DbEmergencyAction extends Base implements Jsonable {
	private String emergency_action_no;
	private String check_day;
	private String check_seq;
	private String check_dt;
	private String check_pref_cd;
	private String check_pref_id;
	private String check_pref_nm;
	
	private String check_tbl;
	private String emergency_action_target_id;
	private String emergency_action_sbst;
	private String emergency_action_yn;
	private String emergency_actor_id;
	private String emergency_actor_nm;
	private String emergency_action_dt;
	
	public String getEmergency_action_no() {
		return emergency_action_no;
	}
	public void setEmergency_action_no(String emergency_action_no) {
		this.emergency_action_no = emergency_action_no;
	}
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
	public String getCheck_dt() {
		return check_dt;
	}
	public void setCheck_dt(String check_dt) {
		this.check_dt = check_dt;
	}
	public String getCheck_pref_cd() {
		return check_pref_cd;
	}
	public void setCheck_pref_cd(String check_pref_cd) {
		this.check_pref_cd = check_pref_cd;
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
	public String getCheck_tbl() {
		return check_tbl;
	}
	public void setCheck_tbl(String check_tbl) {
		this.check_tbl = check_tbl;
	}
	public String getEmergency_action_target_id() {
		return emergency_action_target_id;
	}
	public void setEmergency_action_target_id(String emergency_action_target_id) {
		this.emergency_action_target_id = emergency_action_target_id;
	}
	public String getEmergency_action_sbst() {
		return emergency_action_sbst;
	}
	public void setEmergency_action_sbst(String emergency_action_sbst) {
		this.emergency_action_sbst = emergency_action_sbst;
	}
	public String getEmergency_action_yn() {
		return emergency_action_yn;
	}
	public void setEmergency_action_yn(String emergency_action_yn) {
		this.emergency_action_yn = emergency_action_yn;
	}
	public String getEmergency_actor_id() {
		return emergency_actor_id;
	}
	public void setEmergency_actor_id(String emergency_actor_id) {
		this.emergency_actor_id = emergency_actor_id;
	}
	public String getEmergency_actor_nm() {
		return emergency_actor_nm;
	}
	public void setEmergency_actor_nm(String emergency_actor_nm) {
		this.emergency_actor_nm = emergency_actor_nm;
	}
	public String getEmergency_action_dt() {
		return emergency_action_dt;
	}
	public void setEmergency_action_dt(String emergency_action_dt) {
		this.emergency_action_dt = emergency_action_dt;
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
