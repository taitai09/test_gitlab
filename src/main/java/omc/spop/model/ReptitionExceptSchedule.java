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
 * 2018.08.30	임호경	최초작성
 **********************************************************/

@Alias("reptitionExceptSchedule")
public class ReptitionExceptSchedule extends Base implements Jsonable {

	private String sched_seq;
	private String prt_except_day;

	public String getSched_seq() {
		return sched_seq;
	}

	public void setSched_seq(String sched_seq) {
		this.sched_seq = sched_seq;
	}

	public String getPrt_except_day() {
		return prt_except_day;
	}

	public void setPrt_except_day(String prt_except_day) {
		this.prt_except_day = prt_except_day;
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
