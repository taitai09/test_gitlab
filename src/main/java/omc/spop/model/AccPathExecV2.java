package omc.spop.model;

import java.util.Map;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import omc.spop.utils.StringUtil;

/***********************************************************
 * 2017.09.20	이원식	최초작성
 **********************************************************/

@Alias("accPathExecV2")
public class AccPathExecV2 extends AccPathExec {
	private String file_nm;
	private String start_collect_dt;
	private String end_collect_dt;
	
	public String getFile_nm() {
		return file_nm;
	}
	public void setFile_nm(String file_nm) {
		this.file_nm = file_nm;
	}
	public String getStart_collect_dt() {
		return start_collect_dt;
	}
	public void setStart_collect_dt(String start_collect_dt) {
		this.start_collect_dt = start_collect_dt;
	}
	public String getEnd_collect_dt() {
		return end_collect_dt;
	}
	public void setEnd_collect_dt(String end_collect_dt) {
		this.end_collect_dt = end_collect_dt;
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
