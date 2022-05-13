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

@Alias("topSql")
public class TopSql extends Base implements Jsonable {

	private String gather_day;
	private String sql_id;
	private String plan_hash_value;
	private String module;
	private String parsing_schema_name;
	private String executions;
	private String buffer_gets;
	private String elapsed_time;
	private String cpu_time;
	private String disk_reads;
	private String rows_processed;
	private String ratio_buffer_gets;
	private String ratio_elapsed_time;
	private String ratio_cpu_time;
	private String ratio_disk_reads;
	private String ratio_executions;
	private String ratio_clwait;
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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
	}

	public String getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public String getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public String getCpu_time() {
		return cpu_time;
	}

	public void setCpu_time(String cpu_time) {
		this.cpu_time = cpu_time;
	}

	public String getDisk_reads() {
		return disk_reads;
	}

	public void setDisk_reads(String disk_reads) {
		this.disk_reads = disk_reads;
	}

	public String getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}

	public String getRatio_buffer_gets() {
		return ratio_buffer_gets;
	}

	public void setRatio_buffer_gets(String ratio_buffer_gets) {
		this.ratio_buffer_gets = ratio_buffer_gets;
	}

	public String getRatio_elapsed_time() {
		return ratio_elapsed_time;
	}

	public void setRatio_elapsed_time(String ratio_elapsed_time) {
		this.ratio_elapsed_time = ratio_elapsed_time;
	}

	public String getRatio_cpu_time() {
		return ratio_cpu_time;
	}

	public void setRatio_cpu_time(String ratio_cpu_time) {
		this.ratio_cpu_time = ratio_cpu_time;
	}

	public String getRatio_disk_reads() {
		return ratio_disk_reads;
	}

	public void setRatio_disk_reads(String ratio_disk_reads) {
		this.ratio_disk_reads = ratio_disk_reads;
	}

	public String getRatio_executions() {
		return ratio_executions;
	}

	public void setRatio_executions(String ratio_executions) {
		this.ratio_executions = ratio_executions;
	}

	public String getRatio_clwait() {
		return ratio_clwait;
	}

	public void setRatio_clwait(String ratio_clwait) {
		this.ratio_clwait = ratio_clwait;
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
