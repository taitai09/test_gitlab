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
 * 2017.11.02	이원식	최초작성
 **********************************************************/

@Alias("selftunSql")
public class SelftunSql extends Base implements Jsonable {
	
	private String selftun_query_seq;
	private String sql_text;
	private String reg_dt;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String reg_id;
	
	public String getSelftun_query_seq() {
		return selftun_query_seq;
	}
	public void setSelftun_query_seq(String selftun_query_seq) {
		this.selftun_query_seq = selftun_query_seq;
	}
	public String getSql_text() {
		return sql_text;
	}
	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}
	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}
	public String getWrkjob_cd_nm() {
		return wrkjob_cd_nm;
	}
	public void setWrkjob_cd_nm(String wrkjob_cd_nm) {
		this.wrkjob_cd_nm = wrkjob_cd_nm;
	}
	public String getReg_id() {
		return reg_id;
	}
	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
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