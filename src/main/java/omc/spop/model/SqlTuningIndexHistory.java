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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("sqlTuningIndexHistory")
public class SqlTuningIndexHistory extends Base implements Jsonable {
	private String update_dt;
	private String index_tuning_seq;
	private String index_impr_type_cd;
	private String index_impr_type_nm;
	private String table_name;
	private String index_name;
	private String index_column_name;
    private String before_index_column_name;
	
	public String getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}

	public String getIndex_tuning_seq() {
		return index_tuning_seq;
	}

	public void setIndex_tuning_seq(String index_tuning_seq) {
		this.index_tuning_seq = index_tuning_seq;
	}

	public String getIndex_impr_type_cd() {
		return index_impr_type_cd;
	}

	public void setIndex_impr_type_cd(String index_impr_type_cd) {
		this.index_impr_type_cd = index_impr_type_cd;
	}

	public String getIndex_impr_type_nm() {
		return index_impr_type_nm;
	}

	public void setIndex_impr_type_nm(String index_impr_type_nm) {
		this.index_impr_type_nm = index_impr_type_nm;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getIndex_name() {
		return index_name;
	}

	public void setIndex_name(String index_name) {
		this.index_name = index_name;
	}

	public String getIndex_column_name() {
		return index_column_name;
	}

	public void setIndex_column_name(String index_column_name) {
		this.index_column_name = index_column_name;
	}

	public String getBefore_index_column_name() {
		return before_index_column_name;
	}

	public void setBefore_index_column_name(String before_index_column_name) {
		this.before_index_column_name = before_index_column_name;
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