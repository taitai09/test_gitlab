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

@Alias("reorgRecommendation")
public class ReorgRecommendation extends Base implements Jsonable {
	
    
    private String owner;
    private String table_name;
    private String tablespace_name;
    private String num_rows="0";
    private String allocated_space="0";
    private String used_space="0";
    private String reclaimable_space="0";
    private String reclaimable_space_ratio="0";

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
	public String getAllocated_space() {
		return allocated_space;
	}
	public void setAllocated_space(String allocated_space) {
		this.allocated_space = allocated_space;
	}
	public String getUsed_space() {
		return used_space;
	}
	public void setUsed_space(String used_space) {
		this.used_space = used_space;
	}
	public String getReclaimable_space() {
		return reclaimable_space;
	}
	public void setReclaimable_space(String reclaimable_space) {
		this.reclaimable_space = reclaimable_space;
	}
	public String getReclaimable_space_ratio() {
		return reclaimable_space_ratio;
	}
	public void setReclaimable_space_ratio(String reclaimable_space_ratio) {
		this.reclaimable_space_ratio = reclaimable_space_ratio;
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
	
//	@SuppressWarnings("unchecked")
//	public JSONObject toJSONObject() {
//		JSONObject objJson = new JSONObject();
//
//		objJson.put("dbid",this.getDbid());
//		objJson.put("gather_day",this.getGather_day());
//		objJson.put("owner",this.getOwner());
//		objJson.put("table_name",this.getTable_name());
//		objJson.put("tablespace_name",this.getTablespace_name());
//		objJson.put("num_rows",StringUtil.parseDouble(this.getAllocated_space(),0));
//		objJson.put("allocated_space",StringUtil.parseDouble(this.getAllocated_space(),0));
//		objJson.put("used_space",StringUtil.parseDouble(this.getUsed_space(),0));
//		objJson.put("reclaimable_space",StringUtil.parseDouble(this.getReclaimable_space(),0));
//		objJson.put("reclaimable_space_ratio",StringUtil.parseDouble(this.getReclaimable_space(),0));
//
//		objJson.put("run_first",this.getRun_first());
//		objJson.put("run_second",this.getRun_second());
//		objJson.put("run_third",this.getRun_third());
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