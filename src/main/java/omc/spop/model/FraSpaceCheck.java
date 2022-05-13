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

@Alias("fraSpaceCheck")
public class FraSpaceCheck extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String name;
    private String number_of_files;
    private String space_limit;
    private BigDecimal space_used;
    private BigDecimal space_reclaimable;
    private BigDecimal claim_before_usage_percent;
    private BigDecimal claim_after_usage_percent;
    private BigDecimal threshold_percent;
    private BigDecimal total_space;
    
    
	public BigDecimal getThreshold_percent() {
		return threshold_percent;
	}
	public void setThreshold_percent(BigDecimal threshold_percent) {
		this.threshold_percent = threshold_percent;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber_of_files() {
		return number_of_files;
	}
	public void setNumber_of_files(String number_of_files) {
		this.number_of_files = number_of_files;
	}
	public String getSpace_limit() {
		return space_limit;
	}
	public void setSpace_limit(String space_limit) {
		this.space_limit = space_limit;
	}
	public BigDecimal getSpace_used() {
		return space_used;
	}
	public void setSpace_used(BigDecimal space_used) {
		this.space_used = space_used;
	}
	public BigDecimal getSpace_reclaimable() {
		return space_reclaimable;
	}
	public void setSpace_reclaimable(BigDecimal space_reclaimable) {
		this.space_reclaimable = space_reclaimable;
	}
	public BigDecimal getClaim_before_usage_percent() {
		return claim_before_usage_percent;
	}
	public void setClaim_before_usage_percent(BigDecimal claim_before_usage_percent) {
		this.claim_before_usage_percent = claim_before_usage_percent;
	}
	public BigDecimal getClaim_after_usage_percent() {
		return claim_after_usage_percent;
	}
	public void setClaim_after_usage_percent(BigDecimal claim_after_usage_percent) {
		this.claim_after_usage_percent = claim_after_usage_percent;
	}
	
	public BigDecimal getTotal_space() {
		return total_space;
	}
	public void setTotal_space(BigDecimal total_space) {
		this.total_space = total_space;
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
