package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.06.11	이원식	최초작성
 **********************************************************/

@Alias("cpuLimitPredictionDetail")
public class CPULimitPredictionDetail extends Base implements Jsonable {
	private String prediction_dt;
	
    private String inst_id;
    private String inst_nm;
    private String snap_dt;
    private String cpu_limit;
    private String cpu_usage;
    private String past_cpu_usage_trend;
    private String future_cpu_usage_trend;
    private String whole_cpu_usage_trend;
    
	public String getPrediction_dt() {
		return prediction_dt;
	}
	public void setPrediction_dt(String prediction_dt) {
		this.prediction_dt = prediction_dt;
	}
	public String getInst_id() {
		return inst_id;
	}
	public void setInst_id(String inst_id) {
		this.inst_id = inst_id;
	}
	public String getInst_nm() {
		return inst_nm;
	}
	public void setInst_nm(String inst_nm) {
		this.inst_nm = inst_nm;
	}
	public String getSnap_dt() {
		return snap_dt;
	}
	public void setSnap_dt(String snap_dt) {
		this.snap_dt = snap_dt;
	}
	public String getCpu_limit() {
		return cpu_limit;
	}
	public void setCpu_limit(String cpu_limit) {
		this.cpu_limit = cpu_limit;
	}
	public String getCpu_usage() {
		return cpu_usage;
	}
	public void setCpu_usage(String cpu_usage) {
		this.cpu_usage = cpu_usage;
	}
	public String getPast_cpu_usage_trend() {
		return past_cpu_usage_trend;
	}
	public void setPast_cpu_usage_trend(String past_cpu_usage_trend) {
		this.past_cpu_usage_trend = past_cpu_usage_trend;
	}
	public String getFuture_cpu_usage_trend() {
		return future_cpu_usage_trend;
	}
	public void setFuture_cpu_usage_trend(String future_cpu_usage_trend) {
		this.future_cpu_usage_trend = future_cpu_usage_trend;
	}
	public String getWhole_cpu_usage_trend() {
		return whole_cpu_usage_trend;
	}
	public void setWhole_cpu_usage_trend(String whole_cpu_usage_trend) {
		this.whole_cpu_usage_trend = whole_cpu_usage_trend;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("prediction_dt",this.getPrediction_dt());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("inst_nm",this.getInst_nm());
		objJson.put("snap_dt",this.getSnap_dt());
		objJson.put("cpu_limit",StringUtil.parseDouble(this.getCpu_limit(),0));
		if(this.getCpu_usage() != null){
			objJson.put("cpu_usage",StringUtil.parseDouble(this.getCpu_usage(),0));
		}
		if(this.getPast_cpu_usage_trend() != null){
			objJson.put("past_cpu_usage_trend",StringUtil.parseDouble(this.getPast_cpu_usage_trend(),0));	
		}
		if(this.getFuture_cpu_usage_trend() != null){
			objJson.put("future_cpu_usage_trend",StringUtil.parseDouble(this.getFuture_cpu_usage_trend(),0));	
		}		
		if(this.getWhole_cpu_usage_trend() != null){
			objJson.put("whole_cpu_usage_trend",StringUtil.parseDouble(this.getWhole_cpu_usage_trend(),0));
		}
		
		return objJson;
	}
}