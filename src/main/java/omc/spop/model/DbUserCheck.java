package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("dbUserCheck")
public class DbUserCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String username;
    private String account_status;
    private String expiry_date;
    private String created;
    private String password_expiry_remain_time;
    private String password_grace_time;
    
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccount_status() {
		return account_status;
	}
	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}
	public String getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getPassword_expiry_remain_time() {
		return password_expiry_remain_time;
	}
	public void setPassword_expiry_remain_time(String password_expiry_remain_time) {
		this.password_expiry_remain_time = password_expiry_remain_time;
	}
	public String getPassword_grace_time() {
		return password_grace_time;
	}
	public void setPassword_grace_time(String password_grace_time) {
		this.password_grace_time = password_grace_time;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("username",this.getUsername());
		objJson.put("account_status",this.getAccount_status());
		objJson.put("expiry_date",this.getExpiry_date());
		objJson.put("created",this.getCreated());
		objJson.put("password_expiry_remain_time",this.getPassword_expiry_remain_time());
		objJson.put("password_grace_time",this.getPassword_grace_time());
		
		return objJson;
	}		
}
