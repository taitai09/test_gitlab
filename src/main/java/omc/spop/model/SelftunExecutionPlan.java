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
 * 2019.04.24
 **********************************************************/

@Alias("selftunExecutionPlan")
public class SelftunExecutionPlan extends Base implements Jsonable {

	private String perf_check_result_div_nm;	// 점검결과
	private String program_exec_dt;				// 성능점검시간
	private String parsing_schema_name;			// 파싱스키마
	private String elapsed_time;				// sql수행시간(초)
	private String buffer_gets;					// 블럭수
	private String rows_processed;				// 처리건수
	private String memory_used;					// 메모리사용량(mb)
	private String selftun_query_seq;			// 셀프튜닝쿼리일련번호
	private String sql_id;						// SQL_ID

	public String getPerf_check_result_div_nm() {
		return perf_check_result_div_nm;
	}

	public void setPerf_check_result_div_nm(String perf_check_result_div_nm) {
		this.perf_check_result_div_nm = perf_check_result_div_nm;
	}

	public String getProgram_exec_dt() {
		return program_exec_dt;
	}

	public void setProgram_exec_dt(String program_exec_dt) {
		this.program_exec_dt = program_exec_dt;
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

	public String getBuffer_gets() {
		return buffer_gets;
	}

	public void setBuffer_gets(String buffer_gets) {
		this.buffer_gets = buffer_gets;
	}

	public String getRows_processed() {
		return rows_processed;
	}

	public void setRows_processed(String rows_processed) {
		this.rows_processed = rows_processed;
	}

	public String getMemory_used() {
		return memory_used;
	}

	public void setMemory_used(String memory_used) {
		this.memory_used = memory_used;
	}

	public String getSelftun_query_seq() {
		return selftun_query_seq;
	}

	public void setSelftun_query_seq(String selftun_query_seq) {
		this.selftun_query_seq = selftun_query_seq;
	}

	public String getSql_id() {
		return sql_id;
	}

	public void setSql_id(String sql_id) {
		this.sql_id = sql_id;
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