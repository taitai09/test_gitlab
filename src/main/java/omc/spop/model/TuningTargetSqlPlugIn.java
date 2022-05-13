package omc.spop.model;

import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/***********************************************************
 * 2021.10.18	명성태	OPENPOP V2 최초작업
 **********************************************************/

@Alias("tuningTargetSqlPlugIn")
//public class TuningTargetSqlPlugIn extends TuningTargetSql implements Jsonable {
public class TuningTargetSqlPlugIn extends TuningTargetSql {
	
	LinkedList<String> bind_set_set_array;
	LinkedList<String> bind_seq_array;
	LinkedList<String> bind_var_nm_array;
	LinkedList<String> bind_var_type_array;
	LinkedList<String> bind_var_value_array;
	LinkedList<String> mandatory_yn_array;
	
	public LinkedList<String> getBind_set_set_array() {
		return bind_set_set_array;
	}

	public void setBind_set_set_array(LinkedList<String> bind_set_set_array) {
		this.bind_set_set_array = bind_set_set_array;
	}

	public LinkedList<String> getBind_seq_array() {
		return bind_seq_array;
	}

	public void setBind_seq_array(LinkedList<String> bind_seq_array) {
		this.bind_seq_array = bind_seq_array;
	}

	public LinkedList<String> getBind_var_nm_array() {
		return bind_var_nm_array;
	}

	public void setBind_var_nm_array(LinkedList<String> bind_var_nm_array) {
		this.bind_var_nm_array = bind_var_nm_array;
	}

	public LinkedList<String> getBind_var_type_array() {
		return bind_var_type_array;
	}

	public void setBind_var_type_array(LinkedList<String> bind_var_type_array) {
		this.bind_var_type_array = bind_var_type_array;
	}

	public LinkedList<String> getBind_var_value_array() {
		return bind_var_value_array;
	}

	public void setBind_var_value_array(LinkedList<String> bind_var_value_array) {
		this.bind_var_value_array = bind_var_value_array;
	}

	public LinkedList<String> getMandatory_yn_array() {
		return mandatory_yn_array;
	}

	public void setMandatory_yn_array(LinkedList<String> mandatory_yn_array) {
		this.mandatory_yn_array = mandatory_yn_array;
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