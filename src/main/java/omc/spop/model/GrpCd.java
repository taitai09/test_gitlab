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
 * 2017.11.10	이원식	최초작성
 **********************************************************/

@Alias("grpCd")
public class GrpCd extends Base implements Jsonable {
	
    private String up_grp_cd_id;
    private String grp_cd_nm;
    private String grp_cd_desc;
    private String use_yn;
    
	public String getUp_grp_cd_id() {
		return up_grp_cd_id;
	}
	public void setUp_grp_cd_id(String up_grp_cd_id) {
		this.up_grp_cd_id = up_grp_cd_id;
	}
	public String getGrp_cd_nm() {
		return grp_cd_nm;
	}
	public void setGrp_cd_nm(String grp_cd_nm) {
		this.grp_cd_nm = grp_cd_nm;
	}
	public String getGrp_cd_desc() {
		return grp_cd_desc;
	}
	public void setGrp_cd_desc(String grp_cd_desc) {
		this.grp_cd_desc = grp_cd_desc;
	}
	public String getUse_yn() {
		return use_yn;
	}
	public void setUse_yn(String use_yn) {
		this.use_yn = use_yn;
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
