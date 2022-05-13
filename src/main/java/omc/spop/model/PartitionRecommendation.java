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
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.05.04	이원식	최초작성
 **********************************************************/

@Alias("partitionRecommendation")
public class PartitionRecommendation extends Base implements Jsonable {
	
    
    private String owner;
    private String table_name;
    private String cur_size="0";
    private String cur_num_rows="0";
    private String month_1_ago_size="0";
    private String month_2_ago_size="0";
    private String month_3_ago_size="0";
    private String month_4_ago_size="0";
    private String month_5_ago_size="0";
    private String month_6_ago_size="0";
    private String month_1_ago_num_rows="0";
    private String month_2_ago_num_rows="0";
    private String month_3_ago_num_rows="0";
    private String month_4_ago_num_rows="0";
    private String month_5_ago_num_rows="0";
    private String month_6_ago_num_rows="0";
    private String reads_activity;
    private String recommend_part_type;
    
    private String access_path;
    private String access_path_count="0";
    private String total_cnt="0";
    private String column_name;
    private String usage_cnt="0";
    private String partition_key_recommend_rank="0";
    
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
	public String getCur_size() {
		return cur_size;
	}
	public void setCur_size(String cur_size) {
		this.cur_size = cur_size;
	}
	public String getCur_num_rows() {
		return cur_num_rows;
	}
	public void setCur_num_rows(String cur_num_rows) {
		this.cur_num_rows = cur_num_rows;
	}
	public String getMonth_1_ago_size() {
		return month_1_ago_size;
	}
	public void setMonth_1_ago_size(String month_1_ago_size) {
		this.month_1_ago_size = month_1_ago_size;
	}
	public String getMonth_2_ago_size() {
		return month_2_ago_size;
	}
	public void setMonth_2_ago_size(String month_2_ago_size) {
		this.month_2_ago_size = month_2_ago_size;
	}
	public String getMonth_3_ago_size() {
		return month_3_ago_size;
	}
	public void setMonth_3_ago_size(String month_3_ago_size) {
		this.month_3_ago_size = month_3_ago_size;
	}
	public String getMonth_4_ago_size() {
		return month_4_ago_size;
	}
	public void setMonth_4_ago_size(String month_4_ago_size) {
		this.month_4_ago_size = month_4_ago_size;
	}
	public String getMonth_5_ago_size() {
		return month_5_ago_size;
	}
	public void setMonth_5_ago_size(String month_5_ago_size) {
		this.month_5_ago_size = month_5_ago_size;
	}
	public String getMonth_6_ago_size() {
		return month_6_ago_size;
	}
	public void setMonth_6_ago_size(String month_6_ago_size) {
		this.month_6_ago_size = month_6_ago_size;
	}
	public String getMonth_1_ago_num_rows() {
		return month_1_ago_num_rows;
	}
	public void setMonth_1_ago_num_rows(String month_1_ago_num_rows) {
		this.month_1_ago_num_rows = month_1_ago_num_rows;
	}
	public String getMonth_2_ago_num_rows() {
		return month_2_ago_num_rows;
	}
	public void setMonth_2_ago_num_rows(String month_2_ago_num_rows) {
		this.month_2_ago_num_rows = month_2_ago_num_rows;
	}
	public String getMonth_3_ago_num_rows() {
		return month_3_ago_num_rows;
	}
	public void setMonth_3_ago_num_rows(String month_3_ago_num_rows) {
		this.month_3_ago_num_rows = month_3_ago_num_rows;
	}
	public String getMonth_4_ago_num_rows() {
		return month_4_ago_num_rows;
	}
	public void setMonth_4_ago_num_rows(String month_4_ago_num_rows) {
		this.month_4_ago_num_rows = month_4_ago_num_rows;
	}
	public String getMonth_5_ago_num_rows() {
		return month_5_ago_num_rows;
	}
	public void setMonth_5_ago_num_rows(String month_5_ago_num_rows) {
		this.month_5_ago_num_rows = month_5_ago_num_rows;
	}
	public String getMonth_6_ago_num_rows() {
		return month_6_ago_num_rows;
	}
	public void setMonth_6_ago_num_rows(String month_6_ago_num_rows) {
		this.month_6_ago_num_rows = month_6_ago_num_rows;
	}
	public String getReads_activity() {
		return reads_activity;
	}
	public void setReads_activity(String reads_activity) {
		this.reads_activity = reads_activity;
	}
	public String getRecommend_part_type() {
		return recommend_part_type;
	}
	public void setRecommend_part_type(String recommend_part_type) {
		this.recommend_part_type = recommend_part_type;
	}
	public String getAccess_path() {
		return access_path;
	}
	public void setAccess_path(String access_path) {
		this.access_path = access_path;
	}
	public String getAccess_path_count() {
		return access_path_count;
	}
	public void setAccess_path_count(String access_path_count) {
		this.access_path_count = access_path_count;
	}
	public String getTotal_cnt() {
		return total_cnt;
	}
	public void setTotal_cnt(String total_cnt) {
		this.total_cnt = total_cnt;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getUsage_cnt() {
		return usage_cnt;
	}
	public void setUsage_cnt(String usage_cnt) {
		this.usage_cnt = usage_cnt;
	}
	public String getPartition_key_recommend_rank() {
		return partition_key_recommend_rank;
	}
	public void setPartition_key_recommend_rank(String partition_key_recommend_rank) {
		this.partition_key_recommend_rank = partition_key_recommend_rank;
	}
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//
//		objJson.put("rnum",StringUtil.parseDouble(this.getRnum(),0));
//		objJson.put("dbid",this.getDbid());
//		objJson.put("gather_day",this.getGather_day());
//		objJson.put("owner",this.getOwner());
//		objJson.put("table_name",this.getTable_name());
//		objJson.put("cur_size",StringUtil.parseDouble(this.getCur_size(),0));
//		objJson.put("cur_num_rows",StringUtil.parseDouble(this.getCur_num_rows(),0));
//		objJson.put("month_1_ago_size",StringUtil.parseDouble(this.getMonth_1_ago_size(),0));
//		objJson.put("month_2_ago_size",StringUtil.parseDouble(this.getMonth_2_ago_size(),0));
//		objJson.put("month_3_ago_size",StringUtil.parseDouble(this.getMonth_3_ago_size(),0));
//		objJson.put("month_4_ago_size",StringUtil.parseDouble(this.getMonth_4_ago_size(),0));
//		objJson.put("month_5_ago_size",StringUtil.parseDouble(this.getMonth_5_ago_size(),0));
//		objJson.put("month_6_ago_size",StringUtil.parseDouble(this.getMonth_6_ago_size(),0));
//		objJson.put("month_1_ago_num_rows",StringUtil.parseDouble(this.getMonth_1_ago_num_rows(),0));
//		objJson.put("month_2_ago_num_rows",StringUtil.parseDouble(this.getMonth_2_ago_num_rows(),0));
//		objJson.put("month_3_ago_num_rows",StringUtil.parseDouble(this.getMonth_3_ago_num_rows(),0));
//		objJson.put("month_4_ago_num_rows",StringUtil.parseDouble(this.getMonth_4_ago_num_rows(),0));
//		objJson.put("month_5_ago_num_rows",StringUtil.parseDouble(this.getMonth_5_ago_num_rows(),0));
//		objJson.put("month_6_ago_num_rows",StringUtil.parseDouble(this.getMonth_6_ago_num_rows(),0));
//		objJson.put("reads_activity",StringUtil.parseDouble(this.getReads_activity(),0));
//		objJson.put("recommend_part_type",this.getRecommend_part_type());
//		objJson.put("access_path",this.getAccess_path());
//		objJson.put("access_path_count",StringUtil.parseDouble(this.getAccess_path_count(),0));
//		objJson.put("total_cnt",StringUtil.parseDouble(this.getTotal_cnt(),0));
//		objJson.put("column_name",this.getColumn_name());
//		objJson.put("usage_cnt",StringUtil.parseDouble(this.getUsage_cnt(),0));
//		objJson.put("partition_key_recommend_rank",StringUtil.parseDouble(this.getPartition_key_recommend_rank(),0));
//		
//		return objJson;
//	}		
	
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