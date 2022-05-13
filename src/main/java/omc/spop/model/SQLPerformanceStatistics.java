package omc.spop.model;

import java.math.BigDecimal;
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
 * 2019.08.28 	임호경 	최초작성
 **********************************************************/

@Alias("sqlPerformanceStatistics")
public class SQLPerformanceStatistics extends Base implements Jsonable {


	private String inst_id;
	private String inst_nm;
	private String action;
	private String action2;
	private String module;
	private String module2;
	private String parsing_schema_name;
	private String parsing_schema_name2;
	private BigDecimal cpu_time_ratio;
	private BigDecimal elapsed_time_ratio;
	private BigDecimal disk_reads_ratio;
	private BigDecimal buffer_gets_ratio;
	private BigDecimal executions_ratio;
	private BigDecimal parse_calls_ratio;
	private BigDecimal wait_ratio;
	private BigDecimal avg_cpu_time;
	private BigDecimal avg_elapsed_time;
	private BigDecimal avg_disk_reads;
	private BigDecimal avg_buffer_gets;
	private BigDecimal avg_wait;
	private String snap_dt;
	private String instance_number;
	private String cnt;
	private String activity;
	private String top;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private String whatChartIs;
	private String whatGridIs;
	private String whatTime;
	private String sql_id;
	private String plan_hash_value;
	private BigDecimal elapsed_time;
	private BigDecimal cpu_time;
	private BigDecimal executions;
	private BigDecimal buffer_gets;
	private BigDecimal disk_reads;
	private BigDecimal rows_processed;
	private String sql_text;
	private BigDecimal rows_processed_ratio;
	private String excel_inst_nm;
	
	
	public String getExcel_inst_nm() {
		return excel_inst_nm;
	}

	public void setExcel_inst_nm(String excel_inst_nm) {
		this.excel_inst_nm = excel_inst_nm;
	}

	public String getInst_id() {
		return inst_id;
	}

	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}

	public String getInst_nm() {
		return inst_nm;
	}

	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction2() {
		return action2;
	}

	public void setAction2(String action2) {
		this.action2 = action2;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModule2() {
		return module2;
	}

	public void setModule2(String module2) {
		this.module2 = module2;
	}

	public String getParsing_schema_name() {
		return parsing_schema_name;
	}

	public void setParsing_schema_name(String parsing_schema_name) {
		this.parsing_schema_name = parsing_schema_name;
	}

	public String getParsing_schema_name2() {
		return parsing_schema_name2;
	}

	public void setParsing_schema_name2(String parsing_schema_name2) {
		this.parsing_schema_name2 = parsing_schema_name2;
	}

	public BigDecimal getCpu_time_ratio() {
		return cpu_time_ratio;
	}

	public void setCpu_time_ratio(BigDecimal cpu_time_ratio) {
		this.cpu_time_ratio = cpu_time_ratio;
	}

	public BigDecimal getElapsed_time_ratio() {
		return elapsed_time_ratio;
	}

	public void setElapsed_time_ratio(BigDecimal elapsed_time_ratio) {
		this.elapsed_time_ratio = elapsed_time_ratio;
	}

	public BigDecimal getDisk_reads_ratio() {
		return disk_reads_ratio;
	}

	public void setDisk_reads_ratio(BigDecimal disk_reads_ratio) {
		this.disk_reads_ratio = disk_reads_ratio;
	}

	public BigDecimal getBuffer_gets_ratio() {
		return buffer_gets_ratio;
	}

	public void setBuffer_gets_ratio(BigDecimal buffer_gets_ratio) {
		this.buffer_gets_ratio = buffer_gets_ratio;
	}

	public BigDecimal getExecutions_ratio() {
		return executions_ratio;
	}

	public void setExecutions_ratio(BigDecimal executions_ratio) {
		this.executions_ratio = executions_ratio;
	}

	public BigDecimal getParse_calls_ratio() {
		return parse_calls_ratio;
	}

	public void setParse_calls_ratio(BigDecimal parse_calls_ratio) {
		this.parse_calls_ratio = parse_calls_ratio;
	}

	public BigDecimal getWait_ratio() {
		return wait_ratio;
	}

	public void setWait_ratio(BigDecimal wait_ratio) {
		this.wait_ratio = wait_ratio;
	}

	public BigDecimal getAvg_cpu_time() {
		return avg_cpu_time;
	}

	public void setAvg_cpu_time(BigDecimal avg_cpu_time) {
		this.avg_cpu_time = avg_cpu_time;
	}

	public BigDecimal getAvg_elapsed_time() {
		return avg_elapsed_time;
	}

	public void setAvg_elapsed_time(BigDecimal avg_elapsed_time) {
		this.avg_elapsed_time = avg_elapsed_time;
	}

	public BigDecimal getAvg_disk_reads() {
		return avg_disk_reads;
	}

	public void setAvg_disk_reads(BigDecimal avg_disk_reads) {
		this.avg_disk_reads = avg_disk_reads;
	}

	public BigDecimal getAvg_buffer_gets() {
		return avg_buffer_gets;
	}

	public void setAvg_buffer_gets(BigDecimal avg_buffer_gets) {
		this.avg_buffer_gets = avg_buffer_gets;
	}

	public BigDecimal getAvg_wait() {
		return avg_wait;
	}

	public void setAvg_wait(BigDecimal avg_wait) {
		this.avg_wait = avg_wait;
	}

	public String getSnap_dt() {
		return snap_dt;
	}

	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}

	public String getInstance_number() {
		return instance_number;
	}

	public void setInstance_number(String instance_number) {
		this.instance_number = instance_number;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getWhatChartIs() {
		return whatChartIs;
	}

	public void setWhatChartIs(String whatChartIs) {
		this.whatChartIs = whatChartIs;
	}

	public String getWhatGridIs() {
		return whatGridIs;
	}

	public void setWhatGridIs(String whatGridIs) {
		this.whatGridIs = whatGridIs;
	}

	public String getWhatTime() {
		return whatTime;
	}

	public void setWhatTime(String whatTime) {
		this.whatTime = whatTime;
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

	public BigDecimal getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(BigDecimal elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public BigDecimal getCpu_time() {
		return cpu_time;
	}

	public void setCpu_time(BigDecimal cpu_time) {
		this.cpu_time = cpu_time;
	}

	public BigDecimal getExecutions() {
		return executions;
	}

	public void setExecutions(BigDecimal executions) {
		this.executions = executions;
	}

	public BigDecimal getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(BigDecimal buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public BigDecimal getDisk_reads() {
		return disk_reads;
	}

	public void setDisk_reads(BigDecimal disk_reads) {
		this.disk_reads = disk_reads;
	}

	public BigDecimal getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(BigDecimal rows_processed) {
		this.rows_processed = rows_processed;
	}

	public String getSql_text() {
		return sql_text;
	}

	public void setSql_text(String sql_text) {
		this.sql_text = sql_text;
	}

	public BigDecimal getRows_processed_ratio() {
		return rows_processed_ratio;
	}

	public void setRows_processed_ratio(BigDecimal rows_processed_ratio) {
		this.rows_processed_ratio = rows_processed_ratio;
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
