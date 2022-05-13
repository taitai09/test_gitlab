package omc.spop.model;

import org.apache.ibatis.type.Alias;
import org.json.simple.JSONObject;

import omc.spop.base.Base;
import omc.spop.utils.JSONResult.Jsonable;
import omc.spop.utils.StringUtil;

/***********************************************************
 * 2018.04.19	이원식	최초작성
 **********************************************************/

@Alias("sharedPoolAdvisor")
public class SharedPoolAdvisor extends Base implements Jsonable {
	
    private String check_day;
    private String check_seq;
    private String inst_id;
    private String shared_pool_size_for_estimate;
    private String shared_pool_size_factor;
    private String estd_lc_size;
    private String estd_lc_memory_objects;
    private String estd_lc_time_saved;
    private String estd_lc_time_saved_factor;
    private String estd_lc_load_time;
    private String estd_lc_load_time_factor;
    private String estd_lc_memory_object_hits;
    
    
	public String getEstd_lc_memory_object_hits() {
		return estd_lc_memory_object_hits;
	}
	public void setEstd_lc_memory_object_hits(String estd_lc_memory_object_hits) {
		this.estd_lc_memory_object_hits = estd_lc_memory_object_hits;
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
	public String getShared_pool_size_for_estimate() {
		return shared_pool_size_for_estimate;
	}
	public void setShared_pool_size_for_estimate(String shared_pool_size_for_estimate) {
		this.shared_pool_size_for_estimate = shared_pool_size_for_estimate;
	}
	public String getShared_pool_size_factor() {
		return shared_pool_size_factor;
	}
	public void setShared_pool_size_factor(String shared_pool_size_factor) {
		this.shared_pool_size_factor = shared_pool_size_factor;
	}
	public String getEstd_lc_size() {
		return estd_lc_size;
	}
	public void setEstd_lc_size(String estd_lc_size) {
		this.estd_lc_size = estd_lc_size;
	}
	public String getEstd_lc_memory_objects() {
		return estd_lc_memory_objects;
	}
	public void setEstd_lc_memory_objects(String estd_lc_memory_objects) {
		this.estd_lc_memory_objects = estd_lc_memory_objects;
	}
	public String getEstd_lc_time_saved() {
		return estd_lc_time_saved;
	}
	public void setEstd_lc_time_saved(String estd_lc_time_saved) {
		this.estd_lc_time_saved = estd_lc_time_saved;
	}
	public String getEstd_lc_time_saved_factor() {
		return estd_lc_time_saved_factor;
	}
	public void setEstd_lc_time_saved_factor(String estd_lc_time_saved_factor) {
		this.estd_lc_time_saved_factor = estd_lc_time_saved_factor;
	}
	public String getEstd_lc_load_time() {
		return estd_lc_load_time;
	}
	public void setEstd_lc_load_time(String estd_lc_load_time) {
		this.estd_lc_load_time = estd_lc_load_time;
	}
	public String getEstd_lc_load_time_factor() {
		return estd_lc_load_time_factor;
	}
	public void setEstd_lc_load_time_factor(String estd_lc_load_time_factor) {
		this.estd_lc_load_time_factor = estd_lc_load_time_factor;
	}
	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();
		
		objJson.put("check_day",this.getCheck_day());
		objJson.put("check_seq",this.getCheck_seq());
		objJson.put("dbid",this.getDbid());
		objJson.put("db_name",this.getDb_name());
		objJson.put("inst_id",this.getInst_id());
		objJson.put("shared_pool_size_for_estimate",StringUtil.parseDouble(this.getShared_pool_size_for_estimate(),0));
		objJson.put("shared_pool_size_factor",StringUtil.parseFloat(this.getShared_pool_size_factor(),0));
		objJson.put("estd_lc_size",StringUtil.parseDouble(this.getEstd_lc_size(),0));
		objJson.put("estd_lc_memory_objects",StringUtil.parseDouble(this.getEstd_lc_memory_objects(),0));
		objJson.put("estd_lc_time_saved",StringUtil.parseDouble(this.getEstd_lc_time_saved(),0));
		objJson.put("estd_lc_time_saved_factor",StringUtil.parseFloat(this.getEstd_lc_time_saved_factor(),0));
		objJson.put("estd_lc_load_time",StringUtil.parseDouble(this.getEstd_lc_load_time(),0));
		objJson.put("estd_lc_load_time_factor",StringUtil.parseFloat(this.getEstd_lc_load_time_factor(),0));
		objJson.put("estd_lc_memory_object_hits",StringUtil.parseDouble(this.getEstd_lc_memory_object_hits(),0));
		
		return objJson; 
	}		
}
