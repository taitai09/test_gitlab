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
 * 2017.11.15	이원식	최초작성
 **********************************************************/

@Alias("auth")
public class Auth extends Base implements Jsonable {
    private String user_ids;
    private String auth_id;
    private String auth_nm;
    private String auth_cd;
    private String use_yn;
    private String auth_grp_id;
    private String auth_start_day;
    private String auth_end_day;
    private String auth_cnt;
    private String user_auth_id;
    
	public String getUser_auth_id() {
		return user_auth_id;
	}
	public void setUser_auth_id(String user_auth_id) {
		this.user_auth_id = user_auth_id;
	}
	public String getUser_ids() {
		return user_ids;
	}
	public void setUser_ids(String user_ids) {
		this.user_ids = user_ids;
	}
	public String getAuth_id() {
		return auth_id;
	}
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	public String getAuth_nm() {
		return auth_nm;
	}
	public void setAuth_nm(String auth_nm) {
		this.auth_nm = auth_nm;
	}
	public String getAuth_cd() {
		return auth_cd;
	}
	public void setAuth_cd(String auth_cd) {
		this.auth_cd = auth_cd;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
	}
	
	public String getAuth_grp_id() {
		return auth_grp_id;
	}
	public void setAuth_grp_id(String auth_grp_id) {
		this.auth_grp_id = auth_grp_id;
	}
	public String getAuth_start_day() {
		return auth_start_day;
	}
	public void setAuth_start_day(String auth_start_day) {
		this.auth_start_day = auth_start_day;
	}
	public String getAuth_end_day() {
		return auth_end_day;
	}
	public void setAuth_end_day(String auth_end_day) {
		this.auth_end_day = auth_end_day;
	}
public String getAuth_cnt() {
		return auth_cnt;
	}
	public void setAuth_cnt(String auth_cnt) {
		this.auth_cnt = auth_cnt;
	}
	//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("auth_id",this.getAuth_id());
//		objJson.put("auth_nm",this.getAuth_nm());
//		objJson.put("auth_cd",this.getAuth_cd());
//		objJson.put("use_yn",this.getUse_yn());
//		
//		return objJson;
//	}		
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
