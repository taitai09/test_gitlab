package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("hmRecommendation")
public class HmRecommendation extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String recommendation_id;
    private String name;
    private String type;
    private String status;
    private String description;
    private String repair_script;
    private String time_detected;
    
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
	public String getRecommendation_id() {
		return recommendation_id;
	}
	public void setRecommendation_id(String recommendation_id) {
		this.recommendation_id = recommendation_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRepair_script() {
		return repair_script;
	}
	public void setRepair_script(String repair_script) {
		this.repair_script = repair_script;
	}
	public String getTime_detected() {
		return time_detected;
	}
	public void setTime_detected(String time_detected) {
		this.time_detected = time_detected;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("recommendation_id",this.getRecommendation_id());
		objJson.put("name",this.getName());
		objJson.put("type",this.getType());
		objJson.put("status",this.getStatus());
		objJson.put("description",this.getDescription());
		objJson.put("repair_script",this.getRepair_script());
		objJson.put("time_detected",this.getTime_detected());

		return objJson; 
	}		
}
