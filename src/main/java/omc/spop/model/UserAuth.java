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
 * 2017.12.12	이원식	최초작성
 **********************************************************/

@Alias("userAuth")
public class UserAuth extends Base implements Jsonable {
	
    private String auth_grp_id;
    private String old_auth_grp_id;
    private String auth_cd;
    private String auth_nm;
    private String auth_start_day;
    private String auth_end_day;
    private String auth_comp_day;
    private String default_auth_yn;
    private String default_auth_grp_id;
    private String default_auth_grp_id_nm;
    private String old_default_auth_yn;
    
	public String getOld_default_auth_yn() {
		return old_default_auth_yn;
	}
	public void setOld_default_auth_yn(String old_default_auth_yn) {
		this.old_default_auth_yn = old_default_auth_yn;
	}
	public String getDefault_auth_grp_id() {
		return default_auth_grp_id;
	}
	public void setDefault_auth_grp_id(String default_auth_grp_id) {
		this.default_auth_grp_id = default_auth_grp_id;
	}
	public String getDefault_auth_grp_id_nm() {
		return default_auth_grp_id_nm;
	}
	public void setDefault_auth_grp_id_nm(String default_auth_grp_id_nm) {
		this.default_auth_grp_id_nm = default_auth_grp_id_nm;
	}
	public String getDefault_auth_yn() {
		return default_auth_yn;
	}
	public void setDefault_auth_yn(String default_auth_yn) {
		this.default_auth_yn = default_auth_yn;
	}
	public String getOld_auth_grp_id() {
		return old_auth_grp_id;
	}
	public void setOld_auth_grp_id(String old_auth_grp_id) {
		this.old_auth_grp_id = old_auth_grp_id;
	}
	public String getAuth_grp_id() {
		return auth_grp_id;
	}
	public void setAuth_grp_id(String auth_grp_id) {
		this.auth_grp_id = auth_grp_id;
	}
	public String getAuth_cd() {
		return auth_cd;
	}
	public void setAuth_cd(String auth_cd) {
		this.auth_cd = auth_cd;
	}
	public String getAuth_nm() {
		return auth_nm;
	}
	public void setAuth_nm(String auth_nm) {
		this.auth_nm = auth_nm;
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
	public String getAuth_comp_day() {
		return auth_comp_day;
	}
	public void setAuth_comp_day(String auth_comp_day) {
		this.auth_comp_day = auth_comp_day;
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
