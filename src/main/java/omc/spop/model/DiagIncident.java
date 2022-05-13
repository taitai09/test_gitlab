package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("diagIncident")
public class DiagIncident extends Base implements Jsonable {
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String problem_id;
    private String create_time;
    private String close_time;
    private String status;
    private String flood_controlled;
    private String error_facility;
    private String error_number;
    private String error_arg1;
    private String error_arg2;
    private String error_arg3;
    private String error_arg4;
    private String error_arg5;
    private String error_arg6;
    private String error_arg7;
    private String error_arg8;
    private String error_arg9;
    private String error_arg10;
    private String error_arg11;
    private String error_arg12;
    private String signalling_component;
    private String signalling_subcomponent;
    private String suspect_component;
    private String suspect_subcomponent;
    private String ecid;
    private String impact;
    
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getClose_time() {
		return close_time;
	}
	public void setClose_time(String close_time) {
		this.close_time = close_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFlood_controlled() {
		return flood_controlled;
	}
	public void setFlood_controlled(String flood_controlled) {
		this.flood_controlled = flood_controlled;
	}
	public String getError_facility() {
		return error_facility;
	}
	public void setError_facility(String error_facility) {
		this.error_facility = error_facility;
	}
	public String getError_number() {
		return error_number;
	}
	public void setError_number(String error_number) {
		this.error_number = error_number;
	}
	public String getError_arg1() {
		return error_arg1;
	}
	public void setError_arg1(String error_arg1) {
		this.error_arg1 = error_arg1;
	}
	public String getError_arg2() {
		return error_arg2;
	}
	public void setError_arg2(String error_arg2) {
		this.error_arg2 = error_arg2;
	}
	public String getError_arg3() {
		return error_arg3;
	}
	public void setError_arg3(String error_arg3) {
		this.error_arg3 = error_arg3;
	}
	public String getError_arg4() {
		return error_arg4;
	}
	public void setError_arg4(String error_arg4) {
		this.error_arg4 = error_arg4;
	}
	public String getError_arg5() {
		return error_arg5;
	}
	public void setError_arg5(String error_arg5) {
		this.error_arg5 = error_arg5;
	}
	public String getError_arg6() {
		return error_arg6;
	}
	public void setError_arg6(String error_arg6) {
		this.error_arg6 = error_arg6;
	}
	public String getError_arg7() {
		return error_arg7;
	}
	public void setError_arg7(String error_arg7) {
		this.error_arg7 = error_arg7;
	}
	public String getError_arg8() {
		return error_arg8;
	}
	public void setError_arg8(String error_arg8) {
		this.error_arg8 = error_arg8;
	}
	public String getError_arg9() {
		return error_arg9;
	}
	public void setError_arg9(String error_arg9) {
		this.error_arg9 = error_arg9;
	}
	public String getError_arg10() {
		return error_arg10;
	}
	public void setError_arg10(String error_arg10) {
		this.error_arg10 = error_arg10;
	}
	public String getError_arg11() {
		return error_arg11;
	}
	public void setError_arg11(String error_arg11) {
		this.error_arg11 = error_arg11;
	}
	public String getError_arg12() {
		return error_arg12;
	}
	public void setError_arg12(String error_arg12) {
		this.error_arg12 = error_arg12;
	}
	public String getSignalling_component() {
		return signalling_component;
	}
	public void setSignalling_component(String signalling_component) {
		this.signalling_component = signalling_component;
	}
	public String getSignalling_subcomponent() {
		return signalling_subcomponent;
	}
	public void setSignalling_subcomponent(String signalling_subcomponent) {
		this.signalling_subcomponent = signalling_subcomponent;
	}
	public String getSuspect_component() {
		return suspect_component;
	}
	public void setSuspect_component(String suspect_component) {
		this.suspect_component = suspect_component;
	}
	public String getSuspect_subcomponent() {
		return suspect_subcomponent;
	}
	public void setSuspect_subcomponent(String suspect_subcomponent) {
		this.suspect_subcomponent = suspect_subcomponent;
	}
	public String getEcid() {
		return ecid;
	}
	public void setEcid(String ecid) {
		this.ecid = ecid;
	}
	public String getImpact() {
		return impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
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
		objJson.put("create_time",this.getCreate_time());
		objJson.put("close_time",this.getClose_time());
		objJson.put("status",this.getStatus());
		objJson.put("flood_controlled",this.getFlood_controlled());
		objJson.put("error_facility",this.getError_facility());
		objJson.put("error_number",this.getError_number());
		objJson.put("error_arg1",this.getError_arg1());
		objJson.put("error_arg2",this.getError_arg2());
		objJson.put("error_arg3",this.getError_arg3());
		objJson.put("error_arg4",this.getError_arg4());
		objJson.put("error_arg5",this.getError_arg5());
		objJson.put("error_arg6",this.getError_arg6());
		objJson.put("error_arg7",this.getError_arg7());
		objJson.put("error_arg8",this.getError_arg8());
		objJson.put("error_arg9",this.getError_arg9());
		objJson.put("error_arg10",this.getError_arg10());
		objJson.put("error_arg11",this.getError_arg11());
		objJson.put("error_arg12",this.getError_arg12());
		objJson.put("signalling_component",this.getSignalling_component());
		objJson.put("signalling_subcomponent",this.getSignalling_subcomponent());
		objJson.put("suspect_component",this.getSuspect_component());
		objJson.put("suspect_subcomponent",this.getSuspect_subcomponent());
		objJson.put("ecid",this.getEcid());
		objJson.put("impact",this.getImpact());

		return objJson; 
	}		
}
