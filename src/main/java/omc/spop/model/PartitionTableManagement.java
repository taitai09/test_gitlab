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
 * 2018.09.07 	임호경 	최초작성
 *******************************/

@Alias("partitionTableManagement")
public class PartitionTableManagement extends Base implements Jsonable {

	private String table_name;
	private String partition_work_type_cd;
	private String partition_work_type_cd_nm;
	// private String partition_type;
	private int partition_interval;
	private String partition_interval_type_cd;
	private String partition_interval_type_cd_nm;
	private String compress_yn;
	private int shelf_life_cnt;
	private String shelf_life_type_cd;
	private String shelf_life_type_cd_nm;
	private String partition_key_composite_value;
	private int spare_partition_cnt;
	private String cd;
	private String cd_nm;

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getCd_nm() {
		return cd_nm;
	}

	public void setCd_nm(String cd_nm) {
		this.cd_nm = cd_nm;
	}

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getPartition_work_type_cd() {
		return partition_work_type_cd;
	}

	public void setPartition_work_type_cd(String partition_work_type_cd) {
		this.partition_work_type_cd = partition_work_type_cd;
	}

	public String getPartition_work_type_cd_nm() {
		return partition_work_type_cd_nm;
	}

	public void setPartition_work_type_cd_nm(String partition_work_type_cd_nm) {
		this.partition_work_type_cd_nm = partition_work_type_cd_nm;
	}

	public int getPartition_interval() {
		return partition_interval;
	}

	public void setPartition_interval(int partition_interval) {
		this.partition_interval = partition_interval;
	}

	public String getPartition_interval_type_cd() {
		return partition_interval_type_cd;
	}

	public void setPartition_interval_type_cd(String partition_interval_type_cd) {
		this.partition_interval_type_cd = partition_interval_type_cd;
	}

	public String getPartition_interval_type_cd_nm() {
		return partition_interval_type_cd_nm;
	}

	public void setPartition_interval_type_cd_nm(String partition_interval_type_cd_nm) {
		this.partition_interval_type_cd_nm = partition_interval_type_cd_nm;
	}

	public String getCompress_yn() {
		return compress_yn;
	}

	public void setCompress_yn(String compress_yn) {
		this.compress_yn = compress_yn;
	}

	public int getShelf_life_cnt() {
		return shelf_life_cnt;
	}

	public void setShelf_life_cnt(int shelf_life_cnt) {
		this.shelf_life_cnt = shelf_life_cnt;
	}

	public String getShelf_life_type_cd() {
		return shelf_life_type_cd;
	}

	public String getShelf_life_type_cd_nm() {
		return shelf_life_type_cd_nm;
	}

	public void setShelf_life_type_cd_nm(String shelf_life_type_cd_nm) {
		this.shelf_life_type_cd_nm = shelf_life_type_cd_nm;
	}

	public void setShelf_life_type_cd(String shelf_life_type_cd) {
		this.shelf_life_type_cd = shelf_life_type_cd;
	}

	public String getPartition_key_composite_value() {
		return partition_key_composite_value;
	}

	public void setPartition_key_composite_value(String partition_key_composite_value) {
		this.partition_key_composite_value = partition_key_composite_value;
	}

	public int getSpare_partition_cnt() {
		return spare_partition_cnt;
	}

	public void setSpare_partition_cnt(int spare_partition_cnt) {
		this.spare_partition_cnt = spare_partition_cnt;
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
