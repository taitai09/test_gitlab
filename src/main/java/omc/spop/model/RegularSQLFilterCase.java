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
 * 2019.04.01 명성태 최초작성 (정규 SQL 필터링 조건)
 **********************************************************/

@Alias("regularSQLFilterCase")
public class RegularSQLFilterCase extends Base implements Jsonable {
	
	// 조건 값
	private String parsing_schema_name;
	
	// 정규 SQL 모듈  필터링 조건 조회
	private String regular_sql_filter_type_nm;		// 필터유형
	private String regular_sql_filter_condition;	// 필터조건
	private String regular_sql_filter_type_cd;		// 필터유형코드
	private String cd_nm;							// 코드명
	private String cd;								// 코드 ID
	
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

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getRegular_sql_filter_type_nm() {
		return regular_sql_filter_type_nm;
	}

	public void setRegular_sql_filter_type_nm(String regular_sql_filter_type_nm) {
		this.regular_sql_filter_type_nm = regular_sql_filter_type_nm;
	}

	public String getRegular_sql_filter_condition() {
		return regular_sql_filter_condition;
	}

	public void setRegular_sql_filter_condition(String regular_sql_filter_condition) {
		this.regular_sql_filter_condition = regular_sql_filter_condition;
	}

	public String getRegular_sql_filter_type_cd() {
		return regular_sql_filter_type_cd;
	}

	public void setRegular_sql_filter_type_cd(String regular_sql_filter_type_cd) {
		this.regular_sql_filter_type_cd = regular_sql_filter_type_cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCm_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}
}
