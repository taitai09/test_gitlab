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
 * 2017.09.28 이원식 최초작성
 **********************************************************/

@Alias("topSqlUnionOffloadSql")
public class TopSqlUnionOffloadSql extends Base implements Jsonable {

	private String gather_day;
	private String choice_div_cd_nm;
	private String tuning_requester_id;
	private String tuning_requester_nm;
	private String tuning_requester_wrkjob_cd;
	private String tuning_requester_wrkjob_nm;
	private String tuning_requester_tel_num;
	private String wrkjob_mgr_nm;
	private String wrkjob_mgr_wrkjob_nm;
	private String wrkjob_mgr_tel_num;
	private String db_name;
	private String sql_id;
	private String plan_hash_value;
	private String first_load_time;
	private String last_load_time;
	private String parsing_schema_name;
	private String avg_elapsed_time;
	private String avg_cpu_time;
	private String avg_buffer_gets;
	private String avg_disk_reads;
	private String avg_rows_processed;
	private String executions;
	private String ratio_elapsed_time;
	private String ratio_cpu_time;
	private String ratio_buffer_gets;
	private String parallel_servers;
	private String offload_yn;
	private String io_saved;
	private String before_1_week_io_saved;
	private String offload_io_saved_decrease;
	private String module;
	private String tr_cd;
	private String dbio;
	private String sql_text;
	
	


	public String getGather_day() {
		return gather_day;
	}

	public void setGather_day(String gather_day) {
		this.gather_day = gather_day;
	}

	public String getChoice_div_cd_nm() {
		return choice_div_cd_nm;
	}

	public void setChoice_div_cd_nm(String choice_div_cd_nm) {
		this.choice_div_cd_nm = choice_div_cd_nm;
	}

	public String getTuning_requester_id() {
		return tuning_requester_id;
	}

	public void setTuning_requester_id(String tuning_requester_id) {
		this.tuning_requester_id = tuning_requester_id;
	}

	public String getTuning_requester_nm() {
		return tuning_requester_nm;
	}

	public void setTuning_requester_nm(String tuning_requester_nm) {
		this.tuning_requester_nm = tuning_requester_nm;
	}

	public String getTuning_requester_wrkjob_cd() {
		return tuning_requester_wrkjob_cd;
	}

	public void setTuning_requester_wrkjob_cd(String tuning_requester_wrkjob_cd) {
		this.tuning_requester_wrkjob_cd = tuning_requester_wrkjob_cd;
	}

	public String getTuning_requester_wrkjob_nm() {
		return tuning_requester_wrkjob_nm;
	}

	public void setTuning_requester_wrkjob_nm(String tuning_requester_wrkjob_nm) {
		this.tuning_requester_wrkjob_nm = tuning_requester_wrkjob_nm;
	}

	public String getTuning_requester_tel_num() {
		return tuning_requester_tel_num;
	}

	public void setTuning_requester_tel_num(String tuning_requester_tel_num) {
		this.tuning_requester_tel_num = tuning_requester_tel_num;
	}

	public String getWrkjob_mgr_nm() {
		return wrkjob_mgr_nm;
	}

	public void setWrkjob_mgr_nm(String wrkjob_mgr_nm) {
		this.wrkjob_mgr_nm = wrkjob_mgr_nm;
	}

	public String getWrkjob_mgr_wrkjob_nm() {
		return wrkjob_mgr_wrkjob_nm;
	}

	public void setWrkjob_mgr_wrkjob_nm(String wrkjob_mgr_wrkjob_nm) {
		this.wrkjob_mgr_wrkjob_nm = wrkjob_mgr_wrkjob_nm;
	}

	public String getWrkjob_mgr_tel_num() {
		return wrkjob_mgr_tel_num;
	}

	public void setWrkjob_mgr_tel_num(String wrkjob_mgr_tel_num) {
		this.wrkjob_mgr_tel_num = wrkjob_mgr_tel_num;
	}

	public String getDb_name() {
		return db_name;
	}

	public void setDb_name(String db_name) {
		this.db_name = db_name;
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

	public String getFirst_load_time() {
		return first_load_time;
	}

	public void setFirst_load_time(String first_load_time) {
		this.first_load_time = first_load_time;
	}

	public String getLast_load_time() {
		return last_load_time;
	}

	public void setLast_load_time(String last_load_time) {
		this.last_load_time = last_load_time;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getAvg_elapsed_time() {
		return avg_elapsed_time;
	}

	public void setAvg_elapsed_time(String avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}

	public String getAvg_cpu_time() {
		return avg_cpu_time;
	}

	public void setAvg_cpu_time(String avg_cpu_time) {
		this.avg_cpu_time = avg_cpu_time;
	}

	public String getAvg_buffer_gets() {
		return avg_buffer_gets;
	}

	public void setAvg_buffer_gets(String avg_buffer_gets) {
		this.avg_buffer_gets = avg_buffer_gets;
	}

	public String getAvg_disk_reads() {
		return avg_disk_reads;
	}

	public void setAvg_disk_reads(String avg_disk_reads) {
		this.avg_disk_reads = avg_disk_reads;
	}

	public String getAvg_rows_processed() {
		return avg_rows_processed;
	}

	public void setAvg_rows_processed(String avg_rows_processed) {
		this.avg_rows_processed = avg_rows_processed;
	}

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
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

	public String getRatio_buffer_gets() {
		return ratio_buffer_gets;
	}

	public void setRatio_buffer_gets(String ratio_buffer_gets) {
		this.ratio_buffer_gets = ratio_buffer_gets;
	}

	public String getParallel_servers() {
		return parallel_servers;
	}

	public void setParallel_servers(String parallel_servers) {
		this.parallel_servers = parallel_servers;
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

	public String getOffload_io_saved_decrease() {
		return offload_io_saved_decrease;
	}

	public void setOffload_io_saved_decrease(String offload_io_saved_decrease) {
		this.offload_io_saved_decrease = offload_io_saved_decrease;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getTr_cd() {
		return tr_cd;
	}

	public void setTr_cd(String tr_cd) {
		this.tr_cd = tr_cd;
	}

	public String getDbio() {
		return dbio;
	}

	public void setDbio(String dbio) {
		this.dbio = dbio;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
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
