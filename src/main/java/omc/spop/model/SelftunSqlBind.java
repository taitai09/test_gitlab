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
 * 2018.01.16	이원식	최초작성
 **********************************************************/

@Alias("selftunSqlBind")
public class SelftunSqlBind extends Base implements Jsonable {
	
    private String selftun_query_seq;
    private String bind_seq;
    private String bind_var_nm;
    private String bind_var_value;
    private String bind_var_type;
    
	public String getSelftun_query_seq() {
		return selftun_query_seq;
	}
	public void setSelftun_query_seq(String selftun_query_seq) {
		this.selftun_query_seq = selftun_query_seq;
	}
	public String getBind_seq() {
		return bind_seq;
	}
	public void setBind_seq(String bind_seq) {
		this.bind_seq = bind_seq;
	}
	public String getBind_var_nm() {
		return bind_var_nm;
	}
	public void setBind_var_nm(String bind_var_nm) {
		this.bind_var_nm = bind_var_nm;
	}
	public String getBind_var_value() {
		return bind_var_value;
	}
	public void setBind_var_value(String bind_var_value) {
		this.bind_var_value = bind_var_value;
	}
	public String getBind_var_type() {
		return bind_var_type;
	}
	public void setBind_var_type(String bind_var_type) {
		this.bind_var_type = bind_var_type;
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