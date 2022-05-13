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
 * 2021.09.16	이재우	최초작성
 **********************************************************/

@Alias("licence")
public class Licence extends Base implements Jsonable {

	private String licence_id;
	private String auth_cd;
	private String dbid;
	private String db_name;
	private String inst_id;
	private String reg_licence_cpu_cnt;
	private String unreg_licence_cpu_cnt;
	private String over_licence_set_cnt;
	private String popup_licence_cpu_cnt;
	private String over_cpu_cnt;
	private String real_cpu_cnt;
	private String licence_cpu_cnt;
	private String licence_chk_action_db_yn;
	

	public String getLicence_id() {
		return licence_id;
	}

	public void setLicence_id(String licence_id) {
		this.licence_id = licence_id;
	}

	public String getAuth_cd() {
		return auth_cd;
	}

	public void setAuth_cd(String auth_cd) {
		this.auth_cd = auth_cd;
	}

	public String getDbid() {
		return dbid;
	}

	public void setDbid(String dbid) {
		this.dbid = dbid;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getReg_licence_cpu_cnt() {
		return reg_licence_cpu_cnt;
	}

	public void setReg_licence_cpu_cnt(String reg_licence_cpu_cnt) {
		this.reg_licence_cpu_cnt = reg_licence_cpu_cnt;
	}

	public String getUnreg_licence_cpu_cnt() {
		return unreg_licence_cpu_cnt;
	}

	public void setUnreg_licence_cpu_cnt(String unreg_licence_cpu_cnt) {
		this.unreg_licence_cpu_cnt = unreg_licence_cpu_cnt;
	}

	public String getOver_licence_set_cnt() {
		return over_licence_set_cnt;
	}

	public void setOver_licence_set_cnt(String over_licence_set_cnt) {
		this.over_licence_set_cnt = over_licence_set_cnt;
	}

	public String getPopup_licence_cpu_cnt() {
		return popup_licence_cpu_cnt;
	}

	public void setPopup_licence_cpu_cnt(String popup_licence_cpu_cnt) {
		this.popup_licence_cpu_cnt = popup_licence_cpu_cnt;
	}

	public String getOver_cpu_cnt() {
		return over_cpu_cnt;
	}

	public void setOver_cpu_cnt(String over_cpu_cnt) {
		this.over_cpu_cnt = over_cpu_cnt;
	}

	public String getReal_cpu_cnt() {
		return real_cpu_cnt;
	}

	public void setReal_cpu_cnt(String real_cpu_cnt) {
		this.real_cpu_cnt = real_cpu_cnt;
	}

	public String getLicence_cpu_cnt() {
		return licence_cpu_cnt;
	}

	public void setLicence_cpu_cnt(String licence_cpu_cnt) {
		this.licence_cpu_cnt = licence_cpu_cnt;
	}

	public String getLicence_chk_action_db_yn() {
		return licence_chk_action_db_yn;
	}

	public void setLicence_chk_action_db_yn(String licence_chk_action_db_yn) {
		this.licence_chk_action_db_yn = licence_chk_action_db_yn;
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
