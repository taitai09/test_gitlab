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
 * 2017.08.10 이원식 최초작성
 **********************************************************/

@Alias("projectWrkjob")
public class ProjectWrkjob extends Base implements Jsonable {

	private String project_id;
	private String project_nm;
	private String wrkjob_cd;
	private String wrkjob_cd_nm;
	private String sql_std_qty_target_yn;

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
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

	public String getSql_std_qty_target_yn() {
		return sql_std_qty_target_yn;
	}

	public void setSql_std_qty_target_yn(String sql_std_qty_target_yn) {
		this.sql_std_qty_target_yn = sql_std_qty_target_yn;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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
