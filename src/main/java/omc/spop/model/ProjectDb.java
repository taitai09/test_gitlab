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

@Alias("projectDb")
public class ProjectDb extends Base implements Jsonable {

	private String project_id;
	private String perf_check_original_dbid;
	private String perf_check_target_dbid;
	private String parsing_schema_name;
	private String perf_check_target_yn;

	private String project_nm;
	private String perf_check_original_db_name;
	private String perf_check_target_db_name;

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getPerf_check_original_dbid() {
		return perf_check_original_dbid;
	}

	public void setPerf_check_original_dbid(String perf_check_original_dbid) {
		this.perf_check_original_dbid = perf_check_original_dbid;
	}

	public String getPerf_check_target_dbid() {
		return perf_check_target_dbid;
	}

	public void setPerf_check_target_dbid(String perf_check_target_dbid) {
		this.perf_check_target_dbid = perf_check_target_dbid;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getPerf_check_target_yn() {
		return perf_check_target_yn;
	}

	public void setPerf_check_target_yn(String perf_check_target_yn) {
		this.perf_check_target_yn = perf_check_target_yn;
	}

	public String getProject_nm() {
		return project_nm;
	}

	public void setProject_nm(String project_nm) {
		this.project_nm = project_nm;
	}

	public String getPerf_check_original_db_name() {
		return perf_check_original_db_name;
	}

	public void setPerf_check_original_db_name(String perf_check_original_db_name) {
		this.perf_check_original_db_name = perf_check_original_db_name;
	}

	public String getPerf_check_target_db_name() {
		return perf_check_target_db_name;
	}

	public void setPerf_check_target_db_name(String perf_check_target_db_name) {
		this.perf_check_target_db_name = perf_check_target_db_name;
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
