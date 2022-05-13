package omc.spop.model;

import java.math.BigDecimal;
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
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("asmDiskgroupCheck")
public class AsmDiskgroupCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String group_number;
    private String name;
    private String state;
    private String total_mb;
    private String free_mb;
    private BigDecimal space_used_percent;
    private BigDecimal threshold_percent;
    private BigDecimal total_space;
    private BigDecimal free_space;
    
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
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getGroup_number() {
		return group_number;
	}
	public void setGroup_number(String group_number) {
		this.group_number = group_number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTotal_mb() {
		return total_mb;
	}
	public void setTotal_mb(String total_mb) {
		this.total_mb = total_mb;
	}
	public String getFree_mb() {
		return free_mb;
	}
	public void setFree_mb(String free_mb) {
		this.free_mb = free_mb;
	}
	public BigDecimal getSpace_used_percent() {
		return space_used_percent;
	}
	public void setSpace_used_percent(BigDecimal space_used_percent) {
		this.space_used_percent = space_used_percent;
	}
	
	public BigDecimal getThreshold_percent() {
		return threshold_percent;
	}
	public void setThreshold_percent(BigDecimal threshold_percent) {
		this.threshold_percent = threshold_percent;
	}
	
	public BigDecimal getTotal_space() {
		return total_space;
	}
	public void setTotal_space(BigDecimal total_space) {
		this.total_space = total_space;
	}
	public BigDecimal getFree_space() {
		return free_space;
	}
	public void setFree_space(BigDecimal free_space) {
		this.free_space = free_space;
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
