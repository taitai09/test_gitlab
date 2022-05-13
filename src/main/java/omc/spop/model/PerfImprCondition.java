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

/***********************************************************
 * 2019.04.30 홍길동 최초작성
 **********************************************************/

@Alias("perfImprCondition")
public class PerfImprCondition extends Base implements Jsonable {
	
    private String type;
    private String tot_sum;
    private String this_year;
    private String this_month;
    private String note;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTot_sum() {
		return tot_sum;
	}

	public void setTot_sum(String tot_sum) {
		this.tot_sum = tot_sum;
	}

	public String getThis_year() {
		return this_year;
	}

	public void setThis_year(String this_year) {
		this.this_year = this_year;
	}

	public String getThis_month() {
		return this_month;
	}

	public void setThis_month(String this_month) {
		this.this_month = this_month;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
