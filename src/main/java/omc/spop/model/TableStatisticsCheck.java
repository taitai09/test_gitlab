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
 * 2018.04.19 이원식 최초작성
 **********************************************************/

@Alias("tableStatisticsCheck")
public class TableStatisticsCheck extends Base implements Jsonable {
	
	private String check_day;
	private String check_seq;
	private String owner;
	private String table_name;
	private String partition_name;
	private String partitioned;
	private String inserts;
	private String updates;
	private String deletes;
	private String truncated;
	private String timestamp;
	private String change_percent;
	private String num_rows;
	private String last_analyzed;

	public String getCheck_day() {
		return check_day;
	}

	public void setCheck_day(String check_day) {
		this.check_day = check_day;
	}

	public String getCheck_seq() {
		return check_seq;
	}

	public void setCheck_seq(String check_seq) {
		this.check_seq = check_seq;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getPartition_name() {
		return partition_name;
	}

	public void setPartition_name(String partition_name) {
		this.partition_name = partition_name;
	}

	public String getPartitioned() {
		return partitioned;
	}

	public void setPartitioned(String partitioned) {
		this.partitioned = partitioned;
	}

	public String getInserts() {
		return inserts;
	}

	public void setInserts(String inserts) {
		this.inserts = inserts;
	}

	public String getUpdates() {
		return updates;
	}

	public void setUpdates(String updates) {
		this.updates = updates;
	}

	public String getDeletes() {
		return deletes;
	}

	public void setDeletes(String deletes) {
		this.deletes = deletes;
	}

	public String getTruncated() {
		return truncated;
	}

	public void setTruncated(String truncated) {
		this.truncated = truncated;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getChange_percent() {
		return change_percent;
	}

	public void setChange_percent(String change_percent) {
		this.change_percent = change_percent;
	}

	public String getNum_rows() {
		return num_rows;
	}

	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}

	public String getLast_analyzed() {
		return last_analyzed;
	}

	public void setLast_analyzed(String last_analyzed) {
		this.last_analyzed = last_analyzed;
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
