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
 * 2018.08.30 	임호경 	최초작성
 **********************************************************/

@Alias("apmConnection")
public class ApmConnection extends Base implements Jsonable {

	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String apm_operate_type_cd;
	private String apm_operate_type_cd_nm;
	private String db_connect_ip;
	private String db_connect_port;
	private String db_user_id;
	private String db_user_password;
	private String updateIsAll;
	
	private String old_wrkjob_cd;
	private String old_apm_operate_type_cd;
	private String old_db_connect_ip;
	private String old_db_connect_port;
	private String old_db_user_id;
	
	
	

	public String getUpdateIsAll() {
		return updateIsAll;
	}

	public void setUpdateIsAll(String updateIsAll) {
		this.updateIsAll = updateIsAll;
	}

	public String getApm_operate_type_cd_nm() {
		return apm_operate_type_cd_nm;
	}

	public void setApm_operate_type_cd_nm(String apm_operate_type_cd_nm) {
		this.apm_operate_type_cd_nm = apm_operate_type_cd_nm;
	}

	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}

	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getApm_operate_type_cd() {
		return apm_operate_type_cd;
	}

	public void setApm_operate_type_cd(String apm_operate_type_cd) {
		this.apm_operate_type_cd = apm_operate_type_cd;
	}

	public String getDb_connect_ip() {
		return db_connect_ip;
	}

	public void setDb_connect_ip(String db_connect_ip) {
		this.db_connect_ip = db_connect_ip;
	}

	public String getDb_user_id() {
		return db_user_id;
	}

	public void setDb_user_id(String db_user_id) {
		this.db_user_id = db_user_id;
	}

	public String getDb_user_password() {
		return db_user_password;
	}

	public void setDb_user_password(String db_user_password) {
		this.db_user_password = db_user_password;
	}

	public String getOld_wrkjob_cd() {
		return old_wrkjob_cd;
	}

	public void setOld_wrkjob_cd(String old_wrkjob_cd) {
		this.old_wrkjob_cd = old_wrkjob_cd;
	}

	public String getOld_apm_operate_type_cd() {
		return old_apm_operate_type_cd;
	}

	public void setOld_apm_operate_type_cd(String old_apm_operate_type_cd) {
		this.old_apm_operate_type_cd = old_apm_operate_type_cd;
	}

	public String getOld_db_connect_ip() {
		return old_db_connect_ip;
	}

	public void setOld_db_connect_ip(String old_db_connect_ip) {
		this.old_db_connect_ip = old_db_connect_ip;
	}

	public String getDb_connect_port() {
		return db_connect_port;
	}

	public void setDb_connect_port(String db_connect_port) {
		this.db_connect_port = db_connect_port;
	}

	public String getOld_db_connect_port() {
		return old_db_connect_port;
	}

	public void setOld_db_connect_port(String old_db_connect_port) {
		this.old_db_connect_port = old_db_connect_port;
	}

	public String getOld_db_user_id() {
		return old_db_user_id;
	}

	public void setOld_db_user_id(String old_db_user_id) {
		this.old_db_user_id = old_db_user_id;
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
