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
 * 2018.08.30 	임호경	 최초작성
 **********************************************************/

@Alias("notUseHint")
public class NotUseHint extends Base implements Jsonable {

	private String old_dbid;
	private String old_db_name;
	private String old_hint_nm;
	private String hint_nm;
	private String hint_reg_dt;
	private String hint_reg_id;
	private String db_abbr_nm;
	private String user_yn;

	
	public String getOld_db_name() {
		return old_db_name;
	}

	public void setOld_db_name(String old_db_name) {
		this.old_db_name = old_db_name;
	}

	public String getOld_dbid() {
		return old_dbid;
	}

	public void setOld_dbid(String old_dbid) {
		this.old_dbid = old_dbid;
	}


	public String getOld_hint_nm() {
		return old_hint_nm;
	}

	public void setOld_hint_nm(String old_hint_nm) {
		this.old_hint_nm = old_hint_nm;
	}

	public String getUser_yn() {
		return user_yn;
	}

	public void setUser_yn(String user_yn) {
		this.user_yn = user_yn;
	}

	public String getDb_abbr_nm() {
		return db_abbr_nm;
	}

	public void setDb_abbr_nm(String db_abbr_nm) {
		this.db_abbr_nm = db_abbr_nm;
	}

	public String getHint_nm() {
		return hint_nm;
	}

	public void setHint_nm(String hint_nm) {
		this.hint_nm = hint_nm;
	}

	public String getHint_reg_dt() {
		return hint_reg_dt;
	}

	public void setHint_reg_dt(String hint_reg_dt) {
		this.hint_reg_dt = hint_reg_dt;
	}

	public String getHint_reg_id() {
		return hint_reg_id;
	}

	public void setHint_reg_id(String hint_reg_id) {
		this.hint_reg_id = hint_reg_id;
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
