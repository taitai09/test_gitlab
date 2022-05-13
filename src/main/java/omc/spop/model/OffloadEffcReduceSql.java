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
 * 2017.09.28 이원식 최초작성
 **********************************************************/

@Alias("offloadEffcReduceSql")
public class OffloadEffcReduceSql extends Base implements Jsonable {

	private String gather_day;
	private String sql_id;
	private String plan_hash_value;
	private String executions;
	private String avg_elapsed_time;
	private String offload_yn;
	private String io_saved;
	private String before_1_week_io_saved;
	private String io_saved_decrease;
	private String sql_text;
	private String tuning_tgt_yn;
	private String project_id;
	private String tuning_prgrs_step_seq;

	
	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getTuning_prgrs_step_seq() {
		return tuning_prgrs_step_seq;
	}

	public void setTuning_prgrs_step_seq(String tuning_prgrs_step_seq) {
		this.tuning_prgrs_step_seq = tuning_prgrs_step_seq;
	}
	public String getGather_day() {
		return gather_day;
	}

	public void setGather_day(String gather_day) {
		this.gather_day = gather_day;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getPlan_hash_value() {
		return plan_hash_value;
	}

	public void setPlan_hash_value(String plan_hash_value) {
		this.plan_hash_value = plan_hash_value;
	}

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
	}

	public String getAvg_elapsed_time() {
		return avg_elapsed_time;
	}

	public void setAvg_elapsed_time(String avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}

	public String getOffload_yn() {
		return offload_yn;
	}

	public void setOffload_yn(String offload_yn) {
		this.offload_yn = offload_yn;
	}

	public String getIo_saved() {
		return io_saved;
	}

	public void setIo_saved(String io_saved) {
		this.io_saved = io_saved;
	}

	public String getBefore_1_week_io_saved() {
		return before_1_week_io_saved;
	}

	public void setBefore_1_week_io_saved(String before_1_week_io_saved) {
		this.before_1_week_io_saved = before_1_week_io_saved;
	}

	public String getIo_saved_decrease() {
		return io_saved_decrease;
	}

	public void setIo_saved_decrease(String io_saved_decrease) {
		this.io_saved_decrease = io_saved_decrease;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getTuning_tgt_yn() {
		return tuning_tgt_yn;
	}

	public void setTuning_tgt_yn(String tuning_tgt_yn) {
		this.tuning_tgt_yn = tuning_tgt_yn;
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
