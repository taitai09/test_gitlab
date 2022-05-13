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
 * 2017.09.22	이원식	최초작성
 **********************************************************/

@Alias("odsUsers")
public class OdsUsers extends Base implements Jsonable {
	
    private String username;
    private String base_day;
    private String password;
    private String account_status;
    private String lock_date;
    private String expiry_date;
    private String default_tablespace;
    private String temporary_tablespace;
    private String created;
    private String profile;
    private String initial_rsrc_consumer_group;
    private String external_name;
    private String table_name;
    private String ods_table_name;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBase_day() {
		return base_day;
	}
	public void setBase_day(String base_day) {
		this.base_day = base_day;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	public String getLock_date() {
		return lock_date;
	}
	public void setLock_date(String lock_date) {
		this.lock_date = lock_date;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getDefault_tablespace() {
		return default_tablespace;
	}
	public void setDefault_tablespace(String default_tablespace) {
		this.default_tablespace = default_tablespace;
	}
	public String getTemporary_tablespace() {
		return temporary_tablespace;
	}
	public void setTemporary_tablespace(String temporary_tablespace) {
		this.temporary_tablespace = temporary_tablespace;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getInitial_rsrc_consumer_group() {
		return initial_rsrc_consumer_group;
	}
	public void setInitial_rsrc_consumer_group(String initial_rsrc_consumer_group) {
		this.initial_rsrc_consumer_group = initial_rsrc_consumer_group;
	}
	public String getExternal_name() {
		return external_name;
	}
	public void setExternal_name(String external_name) {
		this.external_name = external_name;
	}
	
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("dbid",this.getDbid());
//		objJson.put("username",this.getUsername());
//		objJson.put("base_day",this.getBase_day());
//		objJson.put("user_id",this.getUser_id());
//		objJson.put("password",this.getPassword());
//		objJson.put("account_status",this.getAccount_status());
//		objJson.put("lock_date",this.getLock_date());
//		objJson.put("expiry_date",this.getExpiry_date());
//		objJson.put("default_tablespace",this.getDefault_tablespace());
//		objJson.put("temporary_tablespace",this.getTemporary_tablespace());
//		objJson.put("created",this.getCreated());
//		objJson.put("profile",this.getProfile());
//		objJson.put("initial_rsrc_consumer_group",this.getInitial_rsrc_consumer_group());
//		objJson.put("external_name",this.getExternal_name());
//
//		return objJson;
//	}		
	
	public String getOds_table_name() {
		return ods_table_name;
	}
	public void setOds_table_name(String ods_table_name) {
		this.ods_table_name = ods_table_name;
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