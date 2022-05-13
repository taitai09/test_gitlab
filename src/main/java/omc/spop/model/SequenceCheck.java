package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("sequenceCheck")
public class SequenceCheck extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String sequence_owner;
    private String sequence_name;
    private String min_value;
    private String max_value;
    private String increment_by;
    private String cycle_flag;
    private String order_flag;
    private String cache_size;
    private String last_number;
    private String used_percent;
    
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
	public String getSequence_owner() {
		return sequence_owner;
	}
	public void setSequence_owner(String sequence_owner) {
		this.sequence_owner = sequence_owner;
	}
	public String getSequence_name() {
		return sequence_name;
	}
	public void setSequence_name(String sequence_name) {
		this.sequence_name = sequence_name;
	}
	public String getMin_value() {
		return min_value;
	}
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}
	public String getMax_value() {
		return max_value;
	}
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}
	public String getIncrement_by() {
		return increment_by;
	}
	public void setIncrement_by(String increment_by) {
		this.increment_by = increment_by;
	}
	public String getCycle_flag() {
		return cycle_flag;
	}
	public void setCycle_flag(String cycle_flag) {
		this.cycle_flag = cycle_flag;
	}
	public String getOrder_flag() {
		return order_flag;
	}
	public void setOrder_flag(String order_flag) {
		this.order_flag = order_flag;
	}
	public String getCache_size() {
		return cache_size;
	}
	public void setCache_size(String cache_size) {
		this.cache_size = cache_size;
	}
	public String getLast_number() {
		return last_number;
	}
	public void setLast_number(String last_number) {
		this.last_number = last_number;
	}
	public String getUsed_percent() {
		return used_percent;
	}
	public void setUsed_percent(String used_percent) {
		this.used_percent = used_percent;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("sequence_owner",this.getSequence_owner());
		objJson.put("sequence_name",this.getSequence_name());
		objJson.put("min_value",StringUtil.parseDouble(this.getMin_value(),0));
		objJson.put("max_value",this.getMax_value());
		objJson.put("increment_by",StringUtil.parseDouble(this.getIncrement_by(),0));
		objJson.put("cycle_flag",this.getCycle_flag());
		objJson.put("order_flag",this.getOrder_flag());
		objJson.put("cache_size",StringUtil.parseDouble(this.getCache_size(),0));
		objJson.put("last_number",StringUtil.parseDouble(this.getLast_number(),0));

		return objJson; 
	}		
}
