package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("diagProblem")
public class DiagProblem extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String problem_id;
    private String problem_key;
    private String first_incident;
    private String firstinc_time;
    private String last_incident;
    private String lastinc_time;
    private String impact1;
    private String impact2;
    private String impact3;
    private String impact4;
    private String service_request;
    private String bug_number;
    
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
	public String getProblem_id() {
		return problem_id;
	}
	public void setProblem_id(String problem_id) {
		this.problem_id = problem_id;
	}
	public String getProblem_key() {
		return problem_key;
	}
	public void setProblem_key(String problem_key) {
		this.problem_key = problem_key;
	}
	public String getFirst_incident() {
		return first_incident;
	}
	public void setFirst_incident(String first_incident) {
		this.first_incident = first_incident;
	}
	public String getFirstinc_time() {
		return firstinc_time;
	}
	public void setFirstinc_time(String firstinc_time) {
		this.firstinc_time = firstinc_time;
	}
	public String getLast_incident() {
		return last_incident;
	}
	public void setLast_incident(String last_incident) {
		this.last_incident = last_incident;
	}
	public String getLastinc_time() {
		return lastinc_time;
	}
	public void setLastinc_time(String lastinc_time) {
		this.lastinc_time = lastinc_time;
	}
	public String getImpact1() {
		return impact1;
	}
	public void setImpact1(String impact1) {
		this.impact1 = impact1;
	}
	public String getImpact2() {
		return impact2;
	}
	public void setImpact2(String impact2) {
		this.impact2 = impact2;
	}
	public String getImpact3() {
		return impact3;
	}
	public void setImpact3(String impact3) {
		this.impact3 = impact3;
	}
	public String getImpact4() {
		return impact4;
	}
	public void setImpact4(String impact4) {
		this.impact4 = impact4;
	}
	public String getService_request() {
		return service_request;
	}
	public void setService_request(String service_request) {
		this.service_request = service_request;
	}
	public String getBug_number() {
		return bug_number;
	}
	public void setBug_number(String bug_number) {
		this.bug_number = bug_number;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("problem_id",this.getProblem_id());
		objJson.put("problem_key",this.getProblem_key());
		objJson.put("first_incident",this.getFirst_incident());
		objJson.put("firstinc_time",this.getFirstinc_time());
		objJson.put("last_incident",this.getLast_incident());
		objJson.put("lastinc_time",this.getLastinc_time());
		objJson.put("impact1",StringUtil.parseDouble(this.getImpact1(),0));
		objJson.put("impact2",StringUtil.parseDouble(this.getImpact2(),0));
		objJson.put("impact3",StringUtil.parseDouble(this.getImpact3(),0));
		objJson.put("impact4",StringUtil.parseDouble(this.getImpact4(),0));
		objJson.put("service_request",this.getService_request());
		objJson.put("bug_number",this.getBug_number());

		return objJson; 
	}		
}
