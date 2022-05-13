package omc.spop.model;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2017.09.20	이원식	최초작성
 **********************************************************/

public class MenuAuth extends Base implements Jsonable {
	
	private String menu1auth = "notin";
	private String menu21auth = "notin";
	private String menu22auth = "notin";
	private String menu23auth = "notin";
	private String menu24auth = "notin";
	private String menu25auth = "notin";
	private String menu26auth = "notin";
	private String menu27auth = "notin";
	private String menu28auth = "notin";
	private String menu29auth = "notin";	
	private String menu31auth = "notin";
	private String menu31_1auth = "notin";
	private String menu31_2auth = "notin";
	private String menu31_3auth = "notin";
	private String menu32auth = "notin";
	private String menu33auth = "notin";
	private String menu34auth = "notin";
	private String menu35auth = "notin";
	private String menu36auth = "notin";
	private String menu4auth = "notin";
	private String menu5auth = "notin";
	private String menu6auth = "notin";
	private String menu7auth = "notin";	

	public String getMenu1auth() {
		return menu1auth;
	}
	public void setMenu1auth(String menu1auth) {
		this.menu1auth = menu1auth;
	}
	public String getMenu21auth() {
		return menu21auth;
	}
	public void setMenu21auth(String menu21auth) {
		this.menu21auth = menu21auth;
	}
	public String getMenu22auth() {
		return menu22auth;
	}
	public void setMenu22auth(String menu22auth) {
		this.menu22auth = menu22auth;
	}
	public String getMenu23auth() {
		return menu23auth;
	}
	public void setMenu23auth(String menu23auth) {
		this.menu23auth = menu23auth;
	}
	public String getMenu24auth() {
		return menu24auth;
	}
	public void setMenu24auth(String menu24auth) {
		this.menu24auth = menu24auth;
	}
	public String getMenu25auth() {
		return menu25auth;
	}
	public void setMenu25auth(String menu25auth) {
		this.menu25auth = menu25auth;
	}
	public String getMenu26auth() {
		return menu26auth;
	}
	public void setMenu26auth(String menu26auth) {
		this.menu26auth = menu26auth;
	}
	public String getMenu27auth() {
		return menu27auth;
	}
	public void setMenu27auth(String menu27auth) {
		this.menu27auth = menu27auth;
	}
	public String getMenu28auth() {
		return menu28auth;
	}
	public void setMenu28auth(String menu28auth) {
		this.menu28auth = menu28auth;
	}
	public String getMenu29auth() {
		return menu29auth;
	}
	public void setMenu29auth(String menu29auth) {
		this.menu29auth = menu29auth;
	}
	public String getMenu31auth() {
		return menu31auth;
	}
	public void setMenu31auth(String menu31auth) {
		this.menu31auth = menu31auth;
	}
	public String getMenu31_1auth() {
		return menu31_1auth;
	}
	public void setMenu31_1auth(String menu31_1auth) {
		this.menu31_1auth = menu31_1auth;
	}
	public String getMenu31_2auth() {
		return menu31_2auth;
	}
	public void setMenu31_2auth(String menu31_2auth) {
		this.menu31_2auth = menu31_2auth;
	}
	public String getMenu31_3auth() {
		return menu31_3auth;
	}
	public void setMenu31_3auth(String menu31_3auth) {
		this.menu31_3auth = menu31_3auth;
	}
	public String getMenu32auth() {
		return menu32auth;
	}
	public void setMenu32auth(String menu32auth) {
		this.menu32auth = menu32auth;
	}
	public String getMenu33auth() {
		return menu33auth;
	}
	public void setMenu33auth(String menu33auth) {
		this.menu33auth = menu33auth;
	}
	public String getMenu34auth() {
		return menu34auth;
	}
	public void setMenu34auth(String menu34auth) {
		this.menu34auth = menu34auth;
	}
	public String getMenu35auth() {
		return menu35auth;
	}
	public void setMenu35auth(String menu35auth) {
		this.menu35auth = menu35auth;
	}
	public String getMenu36auth() {
		return menu36auth;
	}
	public void setMenu36auth(String menu36auth) {
		this.menu36auth = menu36auth;
	}
	public String getMenu4auth() {
		return menu4auth;
	}
	public void setMenu4auth(String menu4auth) {
		this.menu4auth = menu4auth;
	}
	public String getMenu5auth() {
		return menu5auth;
	}
	public void setMenu5auth(String menu5auth) {
		this.menu5auth = menu5auth;
	}
	public String getMenu6auth() {
		return menu6auth;
	}
	public void setMenu6auth(String menu6auth) {
		this.menu6auth = menu6auth;
	}
	public String getMenu7auth() {
		return menu7auth;
	}
	public void setMenu7auth(String menu7auth) {
		this.menu7auth = menu7auth;
	}
	@Override
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
