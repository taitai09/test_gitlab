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
 * 2020.01.17	명성태	최초작성
 **********************************************************/

@Alias("selftunExecutionPlan1")
public class SelftunExecutionPlan1 extends Base implements Jsonable {
	private String selftun_query_seq;// 셀프튜닝쿼리일련번호
	private BigDecimal a_time_activity;
	private BigDecimal buffers_activity;
	private String selftun_id;
	private String operation;
	private String name;
	private String starts;
	private String a_rows;
	private BigDecimal a_time;
	private String buffers;
	private String reads;
	private String memory;
	private String temp;
	private String table_owner;
	private String table_name;
	
	private String delta_elapsed_time;			// 현재 오퍼레이션의 수행시간
	private String delta_buffer_gets;			// 현재 오퍼레이션의 블럭수
	private String rank_elapsed_time;			// 수행시간 순위
	private String rank_buffer_gets;			// 블럭수 순위
	
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

	public String getSelftun_query_seq() {
		return selftun_query_seq;
	}

	public void setSelftun_query_seq(String selftun_query_seq) {
		this.selftun_query_seq = selftun_query_seq;
	}

	public BigDecimal getA_time_activity() {
		return a_time_activity;
	}

	public void setA_time_activity(BigDecimal a_time_activity) {
		this.a_time_activity = a_time_activity;
	}

	public BigDecimal getBuffers_activity() {
		return buffers_activity;
	}

	public void setBuffers_activity(BigDecimal buffers_activity) {
		this.buffers_activity = buffers_activity;
	}

	public String getSelftun_id() {
		return selftun_id;
	}

	public void setSelftun_id(String selftun_id) {
		this.selftun_id = selftun_id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStarts() {
		return starts;
	}

	public void setStarts(String starts) {
		this.starts = starts;
	}

	public String getA_rows() {
		return a_rows;
	}

	public void setA_rows(String a_rows) {
		this.a_rows = a_rows;
	}

	public BigDecimal getA_time() {
		return a_time;
	}

	public void setA_time(BigDecimal a_time) {
		this.a_time = a_time;
	}

	public String getBuffers() {
		return buffers;
	}

	public void setBuffers(String buffers) {
		this.buffers = buffers;
	}

	public String getReads() {
		return reads;
	}

	public void setReads(String reads) {
		this.reads = reads;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTable_owner() {
		return table_owner;
	}

	public void setTable_owner(String table_owner) {
		this.table_owner = table_owner;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getDelta_elapsed_time() {
		return delta_elapsed_time;
	}

	public void setDelta_elapsed_time(String delta_elapsed_time) {
		this.delta_elapsed_time = delta_elapsed_time;
	}

	public String getDelta_buffer_gets() {
		return delta_buffer_gets;
	}

	public void setDelta_buffer_gets(String delta_buffer_gets) {
		this.delta_buffer_gets = delta_buffer_gets;
	}

	public String getRank_elapsed_time() {
		return rank_elapsed_time;
	}

	public void setRank_elapsed_time(String rank_elapsed_time) {
		this.rank_elapsed_time = rank_elapsed_time;
	}

	public String getRank_buffer_gets() {
		return rank_buffer_gets;
	}

	public void setRank_buffer_gets(String rank_buffer_gets) {
		this.rank_buffer_gets = rank_buffer_gets;
	}
}