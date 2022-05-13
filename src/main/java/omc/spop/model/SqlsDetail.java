package omc.spop.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
 * 2017.10.11 이원식 최초작성
 * 2020.06.30 명성태 SQL Information가 분리
 **********************************************************/

@Alias("sqlsDetail")
public class SqlsDetail extends Base implements Jsonable {

	private String sql_id;
	private String selectOrdered;
	private String begin_interval_time;
	private String snap_time;
	private String snap_id;
	private String instance_number;
	private String plan_hash_value;
	private String module;
	private String parsing_schema_name;
	private String elapsed_time;
	private String cpu_time;
	private String buffer_gets;
	private String disk_reads;
	private String rows_processed;
	private String clwait_time;
	private String iowait_time;
	private String apwait_time;
	private String ccwait_time;
	private String cpu_rate;
	private String clwait_rate;
	private String iowait_rate;
	private String apwait_rate;
	private String ccwait_rate;
	private String executions;
	private String parse_calls;
	private String fetches;
	private String optimizer_env_hash_value;

	private String name;
	private String value;
	private String exec_time;
	private String exec_inst_id;
	private String hint;
	private String max_elapsed_time;
	private String sql_text;
	private String sql_filter;
	private String sql_bind;
	private String sql_full_text;

	private String avg_buffer_gets;
	private String max_buffer_gets;
	private String total_buffer_gets;
	private String avg_elapsed_time;
	private String avg_cpu_time;
	private String avg_disk_reads;
	private String avg_rows_processed;
	private String ratio_bget_total;
	private String ratio_cpu_total;
	private String ratio_cpu_per_execution;

	private String start_snap_id;
	private String end_snap_id;
	private String order_div_cd;
	private String before_choice_sql_except_yn;
	private String topn_cnt;

	private String wrkjob_cd;
	private String sql_hash;
	private HashSet<String> name_value;

	private String chk_condition;

	private String excpt_module_list;
	private String excpt_parsing_schema_name_list;
	private String excpt_sql_id_list;

	private List<String> array_parsing_schema_name;
	private List<String> array_module;
	private List<String> array_sql_id;
	private String filter_sql;
	private String seq;
	
	
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getFilter_sql() {
		return filter_sql;
	}

	public void setFilter_sql(String filter_sql) {
		this.filter_sql = filter_sql;
	}

	public String getSql_filter() {
		return sql_filter;
	}

	public void setSql_filter(String sql_filter) {
		this.sql_filter = sql_filter;
	}

	public String getExcpt_module_list() {
		return excpt_module_list;
	}

	public void setExcpt_module_list(String excpt_module_list) {
		this.excpt_module_list = excpt_module_list;
	}

	public String getExcpt_parsing_schema_name_list() {
		return excpt_parsing_schema_name_list;
	}

	public void setExcpt_parsing_schema_name_list(String excpt_parsing_schema_name_list) {
		this.excpt_parsing_schema_name_list = excpt_parsing_schema_name_list;
	}

	public String getExcpt_sql_id_list() {
		return excpt_sql_id_list;
	}

	public void setExcpt_sql_id_list(String excpt_sql_id_list) {
		this.excpt_sql_id_list = excpt_sql_id_list;
	}

	public List<String> getArray_parsing_schema_name() {
		return array_parsing_schema_name;
	}

	public void setArray_parsing_schema_name(List<String> array_parsing_schema_name) {
		this.array_parsing_schema_name = array_parsing_schema_name;
	}

	public List<String> getArray_module() {
		return array_module;
	}

	public void setArray_module(List<String> array_module) {
		this.array_module = array_module;
	}

	public List<String> getArray_sql_id() {
		return array_sql_id;
	}

	public void setArray_sql_id(List<String> array_sql_id) {
		this.array_sql_id = array_sql_id;
	}

	public String getChk_condition() {
		return chk_condition;
	}

	public void setChk_condition(String chk_condition) {
		this.chk_condition = chk_condition;
	}

//	public SqlsDetail() {
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public SqlsDetail(String snap_id, String exec_time, String elapsed_time, String buffer_gets,
//			HashSet name_value) {
//		this.snap_id = snap_id;
//		this.exec_time = exec_time;
//		this.elapsed_time = elapsed_time;
//		this.buffer_gets = buffer_gets;
//		this.name_value = name_value;
//	}

	public HashSet<String> getName_value() {
		return name_value;
	}

	public void setName_value(HashSet<String> name_value) {
		this.name_value = name_value;
	}

	public String getSelectOrdered() {
		return selectOrdered;
	}

	public void setSelectOrdered(String selectOrdered) {
		this.selectOrdered = selectOrdered;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
	}

	public String getBegin_interval_time() {
		return begin_interval_time;
	}

	public void setBegin_interval_time(String begin_interval_time) {
		this.begin_interval_time = begin_interval_time;
	}

	public String getSnap_time() {
		return snap_time;
	}

	public void setSnap_time(String snap_time) {
		this.snap_time = snap_time;
	}

	public String getSnap_id() {
		return snap_id;
	}

	public void setSnap_id(String snap_id) {
		this.snap_id = snap_id;
	}

	public String getInstance_number() {
		return instance_number;
	}

	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
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

	public String getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
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

	public String getClwait_time() {
		return clwait_time;
	}

	public void setClwait_time(String clwait_time) {
		this.clwait_time = clwait_time;
	}

	public String getIowait_time() {
		return iowait_time;
	}

	public void setIowait_time(String iowait_time) {
		this.iowait_time = iowait_time;
	}

	public String getApwait_time() {
		return apwait_time;
	}

	public void setApwait_time(String apwait_time) {
		this.apwait_time = apwait_time;
	}

	public String getCcwait_time() {
		return ccwait_time;
	}

	public void setCcwait_time(String ccwait_time) {
		this.ccwait_time = ccwait_time;
	}

	public String getCpu_rate() {
		return cpu_rate;
	}

	public void setCpu_rate(String cpu_rate) {
		this.cpu_rate = cpu_rate;
	}

	public String getClwait_rate() {
		return clwait_rate;
	}

	public void setClwait_rate(String clwait_rate) {
		this.clwait_rate = clwait_rate;
	}

	public String getIowait_rate() {
		return iowait_rate;
	}

	public void setIowait_rate(String iowait_rate) {
		this.iowait_rate = iowait_rate;
	}

	public String getApwait_rate() {
		return apwait_rate;
	}

	public void setApwait_rate(String apwait_rate) {
		this.apwait_rate = apwait_rate;
	}

	public String getCcwait_rate() {
		return ccwait_rate;
	}

	public void setCcwait_rate(String ccwait_rate) {
		this.ccwait_rate = ccwait_rate;
	}

	public String getExecutions() {
		return executions;
	}

	public void setExecutions(String executions) {
		this.executions = executions;
	}

	public String getParse_calls() {
		return parse_calls;
	}

	public void setParse_calls(String parse_calls) {
		this.parse_calls = parse_calls;
	}

	public String getFetches() {
		return fetches;
	}

	public void setFetches(String fetches) {
		this.fetches = fetches;
	}

	public String getOptimizer_env_hash_value() {
		return optimizer_env_hash_value;
	}

	public void setOptimizer_env_hash_value(String optimizer_env_hash_value) {
		this.optimizer_env_hash_value = optimizer_env_hash_value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExec_time() {
		return exec_time;
	}

	public void setExec_time(String exec_time) {
		this.exec_time = exec_time;
	}

	public String getExec_inst_id() {
		return exec_inst_id;
	}

	public void setExec_inst_id(String exec_inst_id) {
		this.exec_inst_id = exec_inst_id;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getMax_elapsed_time() {
		return max_elapsed_time;
	}

	public void setMax_elapsed_time(String max_elapsed_time) {
		this.max_elapsed_time = max_elapsed_time;
	}

	public String getSql_bind() {
		return sql_bind;
	}

	public void setSql_bind(String sql_bind) {
		this.sql_bind = sql_bind;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public String getSql_full_text() {
		return sql_full_text;
	}

	public void setSql_full_text(String sql_full_text) {
		this.sql_full_text = sql_full_text;
	}

	public String getAvg_buffer_gets() {
		return avg_buffer_gets;
	}

	public void setAvg_buffer_gets(String avg_buffer_gets) {
		this.avg_buffer_gets = avg_buffer_gets;
	}

	public String getMax_buffer_gets() {
		return max_buffer_gets;
	}

	public void setMax_buffer_gets(String max_buffer_gets) {
		this.max_buffer_gets = max_buffer_gets;
	}

	public String getTotal_buffer_gets() {
		return total_buffer_gets;
	}

	public void setTotal_buffer_gets(String total_buffer_gets) {
		this.total_buffer_gets = total_buffer_gets;
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

	public String getRatio_bget_total() {
		return ratio_bget_total;
	}

	public void setRatio_bget_total(String ratio_bget_total) {
		this.ratio_bget_total = ratio_bget_total;
	}

	public String getRatio_cpu_total() {
		return ratio_cpu_total;
	}

	public void setRatio_cpu_total(String ratio_cpu_total) {
		this.ratio_cpu_total = ratio_cpu_total;
	}

	public String getRatio_cpu_per_execution() {
		return ratio_cpu_per_execution;
	}

	public void setRatio_cpu_per_execution(String ratio_cpu_per_execution) {
		this.ratio_cpu_per_execution = ratio_cpu_per_execution;
	}

	public String getStart_snap_id() {
		return start_snap_id;
	}

	public void setStart_snap_id(String start_snap_id) {
		this.start_snap_id = start_snap_id;
	}

	public String getEnd_snap_id() {
		return end_snap_id;
	}

	public void setEnd_snap_id(String end_snap_id) {
		this.end_snap_id = end_snap_id;
	}

	public String getOrder_div_cd() {
		return order_div_cd;
	}

	public void setOrder_div_cd(String order_div_cd) {
		this.order_div_cd = order_div_cd;
	}

	public String getBefore_choice_sql_except_yn() {
		return before_choice_sql_except_yn;
	}

	public void setBefore_choice_sql_except_yn(String before_choice_sql_except_yn) {
		this.before_choice_sql_except_yn = before_choice_sql_except_yn;
	}

	public String getTopn_cnt() {
		return topn_cnt;
	}

	public void setTopn_cnt(String topn_cnt) {
		this.topn_cnt = topn_cnt;
	}

	public String getWrkjob_cd() {
		return wrkjob_cd;
	}

	public void setWrkjob_cd(String wrkjob_cd) {
		this.wrkjob_cd = wrkjob_cd;
	}

	public String getSql_hash() {
		return sql_hash;
	}

	public void setSql_hash(String sql_hash) {
		this.sql_hash = sql_hash;
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