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
 * 2017.02.01 홍길동 최초작성
 **********************************************************/

@Alias("appCheckCombined")
public class AppCheckCombined extends Base implements Jsonable {
	
	private String wrkjob_cd;
	private String wrkjob_nm;
	private String parent_wrkjob_cd;
	private String timeount_cnt;
	private String elpased_time_delay_cnt;
	private String plan_cnt;
	private String new_cnt;
	private String literal_sql_text_cnt;
	private String literl_plan_hash_value_cnt;
	private String temp_cnt;
	private String fullscan_cnt;
	private String delete_cnt;
	
	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getWrkjob_nm() {
		return wrkjob_nm;
	}

	public void setWrkjob_nm(String wrkjob_nm) {
		this.wrkjob_nm = wrkjob_nm;
	}

	public String getParent_wrkjob_cd() {
		return parent_wrkjob_cd;
	}

	public void setParent_wrkjob_cd(String parent_wrkjob_cd) {
		this.parent_wrkjob_cd = parent_wrkjob_cd;
	}

	public String getTimeount_cnt() {
		return timeount_cnt;
	}

	public void setTimeount_cnt(String timeount_cnt) {
		this.timeount_cnt = timeount_cnt;
	}

	public String getElpased_time_delay_cnt() {
		return elpased_time_delay_cnt;
	}

	public void setElpased_time_delay_cnt(String elpased_time_delay_cnt) {
		this.elpased_time_delay_cnt = elpased_time_delay_cnt;
	}

	public String getPlan_cnt() {
		return plan_cnt;
	}

	public void setPlan_cnt(String plan_cnt) {
		this.plan_cnt = plan_cnt;
	}

	public String getNew_cnt() {
		return new_cnt;
	}

	public void setNew_cnt(String new_cnt) {
		this.new_cnt = new_cnt;
	}

	public String getLiteral_sql_text_cnt() {
		return literal_sql_text_cnt;
	}

	public void setLiteral_sql_text_cnt(String literal_sql_text_cnt) {
		this.literal_sql_text_cnt = literal_sql_text_cnt;
	}

	public String getLiterl_plan_hash_value_cnt() {
		return literl_plan_hash_value_cnt;
	}

	public void setLiterl_plan_hash_value_cnt(String literl_plan_hash_value_cnt) {
		this.literl_plan_hash_value_cnt = literl_plan_hash_value_cnt;
	}

	public String getTemp_cnt() {
		return temp_cnt;
	}

	public void setTemp_cnt(String temp_cnt) {
		this.temp_cnt = temp_cnt;
	}

	public String getFullscan_cnt() {
		return fullscan_cnt;
	}

	public void setFullscan_cnt(String fullscan_cnt) {
		this.fullscan_cnt = fullscan_cnt;
	}

	public String getDelete_cnt() {
		return delete_cnt;
	}

	public void setDelete_cnt(String delete_cnt) {
		this.delete_cnt = delete_cnt;
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
