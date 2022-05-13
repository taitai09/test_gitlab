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
 * 2019.04.30 홍길동 최초작성
 **********************************************************/
@Alias("reorgTargetCondition")
public class ReorgTargetCondition extends Base implements Jsonable {

	
	private String owner;
	private String table_name;
	private String tablespace_name;
//	private int num_rows;
	private String num_rows;
	private double allocated_space;
	private double used_space;
	private double reclaimable_space;
	private double reclaimable_space_ratio;
	private int cnt;

	private String run_first;
	private String run_second;
	private String run_third;

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

	public String getTablespace_name() {
		return tablespace_name;
	}

	public void setTablespace_name(String tablespace_name) {
		this.tablespace_name = tablespace_name;
	}

	public String getNum_rows() {
		return num_rows;
	}

	public void setNum_rows(String num_rows) {
		this.num_rows = num_rows;
	}

	public double getAllocated_space() {
		return allocated_space;
	}

	public void setAllocated_space(double allocated_space) {
		this.allocated_space = allocated_space;
	}

	public double getUsed_space() {
		return used_space;
	}

	public void setUsed_space(double used_space) {
		this.used_space = used_space;
	}

	public double getReclaimable_space() {
		return reclaimable_space;
	}

	public void setReclaimable_space(double reclaimable_space) {
		this.reclaimable_space = reclaimable_space;
	}

	public double getReclaimable_space_ratio() {
		return reclaimable_space_ratio;
	}

	public void setReclaimable_space_ratio(double reclaimable_space_ratio) {
		this.reclaimable_space_ratio = reclaimable_space_ratio;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public String getRun_first() {
		return run_first;
	}

	public void setRun_first(String run_first) {
		this.run_first = run_first;
	}

	public String getRun_second() {
		return run_second;
	}

	public void setRun_second(String run_second) {
		this.run_second = run_second;
	}

	public String getRun_third() {
		return run_third;
	}

	public void setRun_third(String run_third) {
		this.run_third = run_third;
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