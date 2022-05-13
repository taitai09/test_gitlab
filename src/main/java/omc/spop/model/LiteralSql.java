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
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.29	이원식	최초작성
 **********************************************************/

@Alias("literalSql")
public class LiteralSql extends Base implements Jsonable {
    
    private String literal_type_cd;
    private String literal_seq="0";
    private String plan_hash_value="0";
    private String sql_text;
    private String sql_cnt;
    private String sql_id;

    
	public String getSql_id() {
		return sql_id;
	}
	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}
	public String getLiteral_type_cd() {
		return literal_type_cd;
	}
	public void setLiteral_type_cd(String literal_type_cd) {
		this.literal_type_cd = literal_type_cd;
	}
	public String getLiteral_seq() {
		return literal_seq;
	}
	public void setLiteral_seq(String literal_seq) {
		this.literal_seq = literal_seq;
	}
	public String getPlan_hash_value() {
		return plan_hash_value;
	}
	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}
	public String getSql_cnt() {
		return sql_cnt;
	}
	public void setSql_cnt(String sql_cnt) {
		this.sql_cnt = sql_cnt;
	}
	
	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//		
//		objJson.put("dbid",this.getDbid());
//		objJson.put("gather_day",this.getGather_day());		
//		objJson.put("literal_type_cd",this.getLiteral_type_cd());
//		objJson.put("literal_seq",StringUtil.parseDouble(this.getLiteral_seq(),0));
//		objJson.put("plan_hash_value",StringUtil.parseDouble(this.getPlan_hash_value(),0));
//		objJson.put("sql_text",this.getSql_text());
//		objJson.put("sql_cnt",this.getSql_cnt());
//
//		return objJson;
//	}		
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